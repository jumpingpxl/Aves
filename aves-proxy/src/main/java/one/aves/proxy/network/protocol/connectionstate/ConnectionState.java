package one.aves.proxy.network.protocol.connectionstate;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.packet.handshake.HandshakePacket;
import one.aves.proxy.network.protocol.packet.status.PingPongPacket;
import one.aves.proxy.network.protocol.packet.status.StatusPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Consumer;

public enum ConnectionState {
	HANDSHAKE(-1, state -> {
		state.registerServerBound(0x00, HandshakePacket.class);
	}),
	STATUS(1, state -> {
		state.registerServerBound(0x00, StatusPacket.class);
		state.registerClientBound(0x00, StatusPacket.class);
		state.registerServerBound(0x01, PingPongPacket.class);
		state.registerClientBound(0x01, PingPongPacket.class);
	}),
	LOGIN(2, state -> {

	}),
	PLAY(0, state -> {

	});

	public static final AttributeKey<ConnectionState> ATTRIBUTE_KEY = AttributeKey.valueOf(
			"protocol");
	private static Map<Class<? extends NettyPacket<?>>, ConnectionState> PACKET_STATES;

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
		return PACKET_STATES.get(packetClass);
	}

	public int getId() {
		return id;
	}

	public NettyPacket get(Direction direction, int packetId) {
		Map<Integer, Class<? extends NettyPacket<?>>> directionMap = this.getDirectionMap(direction);
		if (directionMap == null) {
			throw new NullPointerException("Could not find direction map for " + direction);
		}

		Class<? extends NettyPacket> packetClass = directionMap.get(packetId);
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

	public int get(Direction direction, NettyPacket nettyPacket) {
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

	private ConnectionState registerClientBound(int packetId,
	                                            Class<? extends NettyPacket<?>> packetClass) {
		if (PACKET_STATES == null) {
			PACKET_STATES = Maps.newHashMap();
		}

		PACKET_STATES.put(packetClass, this);
		clientBound.put(packetId, packetClass);
		return this;
	}

	private ConnectionState registerServerBound(int packetId,
	                                            Class<? extends NettyPacket<?>> packetClass) {
		if (PACKET_STATES == null) {
			PACKET_STATES = Maps.newHashMap();
		}

		PACKET_STATES.put(packetClass, this);
		serverBound.put(packetId, packetClass);
		return this;
	}

	private BiMap<Integer, Class<? extends NettyPacket<?>>> getDirectionMap(Direction direction) {
		if (direction == Direction.CLIENTBOUND) {
			return clientBound;
		}

		if (direction == Direction.SERVERBOUND) {
			return serverBound;
		}

		return null;
	}
}
