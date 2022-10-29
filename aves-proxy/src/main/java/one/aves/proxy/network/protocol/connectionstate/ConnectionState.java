package one.aves.proxy.network.protocol.connectionstate;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.packet.common.clientbound.DisconnectPacket;
import one.aves.proxy.network.protocol.packet.handshake.serverbound.HandshakePacket;
import one.aves.proxy.network.protocol.packet.login.clientbound.EncryptionRequestPacket;
import one.aves.proxy.network.protocol.packet.login.clientbound.LoginSuccessPacket;
import one.aves.proxy.network.protocol.packet.login.serverbound.EncryptionResponsePacket;
import one.aves.proxy.network.protocol.packet.login.serverbound.LoginStartPacket;
import one.aves.proxy.network.protocol.packet.status.clientbound.PongPacket;
import one.aves.proxy.network.protocol.packet.status.clientbound.StatusResponsePacket;
import one.aves.proxy.network.protocol.packet.status.serverbound.PingPacket;
import one.aves.proxy.network.protocol.packet.status.serverbound.StatusRequestPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Consumer;

public enum ConnectionState {
	HANDSHAKE(-1, state -> {
		state.registerServerBound(0x00, HandshakePacket.class);
	}),
	STATUS(1, state -> {
		state.registerServerBound(0x00, StatusRequestPacket.class);
		state.registerClientBound(0x00, StatusResponsePacket.class);
		state.registerServerBound(0x01, PingPacket.class);
		state.registerClientBound(0x01, PongPacket.class);
	}),
	LOGIN(2, state -> {
		state.registerServerBound(0x00, LoginStartPacket.class);
		state.registerClientBound(0x00, DisconnectPacket.class);
		state.registerClientBound(0x01, EncryptionRequestPacket.class);
		state.registerServerBound(0x01, EncryptionResponsePacket.class);
		state.registerClientBound(0x02, LoginSuccessPacket.class);
	}),
	PLAY(0, state -> {

	});

	public static final AttributeKey<ConnectionState> ATTRIBUTE_KEY = AttributeKey.valueOf(
			"protocol");
	private static Map<Class<? extends NettyPacket<?>>, ConnectionState> packetStates;

	private final int id;
	private final BiMap<Integer, Class<? extends NettyPacket<?>>> clientBound;
	private final BiMap<Integer, Class<? extends NettyPacket<?>>> serverBound;

	ConnectionState(int id, Consumer<ConnectionState> state) {
		this.id = id;
		this.clientBound = HashBiMap.create();
		this.serverBound = HashBiMap.create();

		state.accept(this);
	}

	public static ConnectionState fromContext(ChannelHandlerContext context) {
		return context.channel().attr(ATTRIBUTE_KEY).get();
	}

	public static ConnectionState fromPacket(Class<? extends NettyPacket> packetClass) {
		return packetStates.get(packetClass);
	}

	/**
	 * Gets the connection state by id. this is done via a switch statement as {@link #values()}
	 * is really inefficient and caching the values would result in every packet being registered
	 * twice.
	 *
	 * @param id the state id
	 * @return the connection state by id
	 */
	public static ConnectionState getById(int id) {
		switch (id) {
			case -1:
				return HANDSHAKE;
			case 0:
				return PLAY;
			case 1:
				return STATUS;
			case 2:
				return LOGIN;
			default:
				throw new IllegalArgumentException("Unknown connection state id: " + id);
		}
	}

	public NettyPacket<?> get(Direction direction, int packetId) {
		Map<Integer, Class<? extends NettyPacket<?>>> directionMap = this.getDirectionMap(direction);
		if (directionMap == null) {
			throw new NullPointerException("Could not find direction map for " + direction);
		}

		Class<? extends NettyPacket<?>> packetClass = directionMap.get(packetId);
		if (packetClass == null) {
			return null;
		}

		try {
			return packetClass.getDeclaredConstructor().newInstance();
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException |
		         NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public int get(Direction direction, NettyPacket<?> nettyPacket) {
		return this.get(direction, nettyPacket.getClass());
	}

	public int get(Direction direction, Class<? extends NettyPacket> packetClass) {
		BiMap<Integer, Class<? extends NettyPacket<?>>> directionMap = this.getDirectionMap(direction);
		if (directionMap == null) {
			throw new NullPointerException("Could not find direction map for " + direction);
		}

		Integer integer = directionMap.inverse().get(packetClass);
		if (integer == null) {
			throw new NullPointerException("Could not find packet id for " + packetClass.getSimpleName());
		}

		return integer;
	}

	public int getId() {
		return this.id;
	}

	private ConnectionState registerClientBound(int packetId,
	                                            Class<? extends NettyPacket<?>> packetClass) {
		this.registerPacket(packetClass);
		this.clientBound.put(packetId, packetClass);
		return this;
	}

	private ConnectionState registerServerBound(int packetId,
	                                            Class<? extends NettyPacket<?>> packetClass) {
		this.registerPacket(packetClass);
		this.serverBound.put(packetId, packetClass);
		return this;
	}

	private void registerPacket(Class<? extends NettyPacket<?>> packetClass) {
		if (packetStates == null) {
			packetStates = Maps.newHashMap();
		}

		packetStates.put(packetClass, this);
	}

	private BiMap<Integer, Class<? extends NettyPacket<?>>> getDirectionMap(Direction direction) {
		if (direction == Direction.CLIENTBOUND) {
			return this.clientBound;
		}

		if (direction == Direction.SERVERBOUND) {
			return this.serverBound;
		}

		return null;
	}
}
