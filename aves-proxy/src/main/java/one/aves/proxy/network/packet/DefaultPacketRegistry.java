package one.aves.proxy.network.packet;

import com.google.common.collect.Maps;
import one.aves.api.network.Direction;
import one.aves.api.network.connection.ConnectionState;
import one.aves.api.network.packet.Packet;
import one.aves.api.network.packet.PacketRegistry;
import one.aves.api.service.ServiceProvider;
import one.aves.api.util.Registry;
import one.aves.proxy.network.packet.handshake.serverbound.HandshakePacket;
import one.aves.proxy.network.packet.login.clientbound.EncryptionRequestPacket;
import one.aves.proxy.network.packet.login.clientbound.LoginDisconnectPacket;
import one.aves.proxy.network.packet.login.clientbound.LoginSuccessPacket;
import one.aves.proxy.network.packet.login.serverbound.EncryptionResponsePacket;
import one.aves.proxy.network.packet.login.serverbound.LoginStartPacket;
import one.aves.proxy.network.packet.play.clientbound.OutgoingPluginMessagePacket;
import one.aves.proxy.network.packet.play.clientbound.PlayDisconnectPacket;
import one.aves.proxy.network.packet.play.serverbound.IncomingPluginMessagePacket;
import one.aves.proxy.network.packet.status.clientbound.PongPacket;
import one.aves.proxy.network.packet.status.clientbound.StatusResponsePacket;
import one.aves.proxy.network.packet.status.serverbound.PingPacket;
import one.aves.proxy.network.packet.status.serverbound.StatusRequestPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DefaultPacketRegistry implements PacketRegistry {

	private final Map<Class<? extends Packet<?>>, ConnectionState> packetStates;
	private final Map<ConnectionState, Registry<Class<? extends Packet<?>>, Integer>> clientBound;
	private final Map<ConnectionState, Registry<Class<? extends Packet<?>>, Integer>> serverBound;

	public DefaultPacketRegistry() {
		ServiceProvider.register(PacketRegistry.class, this);

		this.packetStates = Maps.newHashMap();
		this.clientBound = Maps.newHashMap();
		this.serverBound = Maps.newHashMap();
	}

	@Override
	public void registerClientBound(ConnectionState state, int id,
	                                Class<? extends Packet<?>> packetClass) {
		this.register(Direction.CLIENTBOUND, state, id, packetClass);
	}

	@Override
	public void registerServerBound(ConnectionState state, int id,
	                                Class<? extends Packet<?>> packetClass) {
		this.register(Direction.SERVERBOUND, state, id, packetClass);
	}

	@Override
	public Packet<?> getPacket(ConnectionState state, Direction direction, int packetId) {
		Registry<Class<? extends Packet<?>>, Integer> registry = this.getRegistry(direction, state);
		Class<? extends Packet<?>> packetClass = registry.get(packetId);
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

	@Override
	public int getPacketId(ConnectionState state, Direction direction, Packet<?> packet) {
		return this.getPacketId(state, direction, (Class<? extends Packet<?>>) packet.getClass());
	}

	@Override
	public int getPacketId(ConnectionState state, Direction direction,
	                       Class<? extends Packet<?>> packet) {
		Registry<Class<? extends Packet<?>>, Integer> registry = this.getRegistry(direction, state);
		Integer identifier = registry.getIdentifier(packet);
		if (identifier == null) {
			throw new NullPointerException("Could not find packet id for " + packet.getSimpleName());
		}

		return identifier;
	}

	@Override
	public ConnectionState getConnectionState(Class<? extends Packet> packetClass) {
		return this.packetStates.get(packetClass);
	}

	private void register(Direction direction, ConnectionState state, int id,
	                      Class<? extends Packet<?>> packetClass) {
		Registry<Class<? extends Packet<?>>, Integer> registry = this.getRegistry(direction, state);
		if (!registry.register(id, packetClass)) {
			throw new IllegalArgumentException(
					String.format("Packet %s with id %s is already registered for state %s",
							packetClass.getSimpleName(), id, state));
		}

		this.packetStates.put(packetClass, state);
	}

	private Registry<Class<? extends Packet<?>>, Integer> getRegistry(Direction direction,
	                                                                  ConnectionState state) {
		Map<ConnectionState, Registry<Class<? extends Packet<?>>, Integer>> directionMap = null;
		if (direction == Direction.CLIENTBOUND) {
			directionMap = this.clientBound;
		}

		if (direction == Direction.SERVERBOUND) {
			directionMap = this.serverBound;
		}

		if (directionMap == null) {
			throw new NullPointerException("Could not find direction map for " + direction);
		}

		return directionMap.computeIfAbsent(state,
				k -> Registry.create(Registry.Rule.DO_NOT_OVERWRITE));
	}

	public PacketRegistry registerDefaultPackets() {
		//handshake
		this.register(Direction.SERVERBOUND, ConnectionState.HANDSHAKE, 0x00, HandshakePacket.class);

		//status
		this.registerStatusPacket(Direction.SERVERBOUND, 0x00, StatusRequestPacket.class);
		this.registerStatusPacket(Direction.CLIENTBOUND, 0x00, StatusResponsePacket.class);
		this.registerStatusPacket(Direction.SERVERBOUND, 0x01, PingPacket.class);
		this.registerStatusPacket(Direction.CLIENTBOUND, 0x01, PongPacket.class);

		//login
		this.registerLoginPacket(Direction.SERVERBOUND, 0x00, LoginStartPacket.class);
		this.registerLoginPacket(Direction.CLIENTBOUND, 0x00, LoginDisconnectPacket.class);
		this.registerLoginPacket(Direction.CLIENTBOUND, 0x01, EncryptionRequestPacket.class);
		this.registerLoginPacket(Direction.SERVERBOUND, 0x01, EncryptionResponsePacket.class);
		this.registerLoginPacket(Direction.CLIENTBOUND, 0x02, LoginSuccessPacket.class);

		//play
		this.registerPlayPacket(Direction.SERVERBOUND, 0x17, IncomingPluginMessagePacket.class);
		this.registerPlayPacket(Direction.CLIENTBOUND, 0x3F, OutgoingPluginMessagePacket.class);
		this.registerPlayPacket(Direction.CLIENTBOUND, 0x40, PlayDisconnectPacket.class);

		return this;
	}

	private void registerStatusPacket(Direction direction, int packetId,
	                                  Class<? extends Packet<?>> packetClass) {
		this.register(direction, ConnectionState.STATUS, packetId, packetClass);
	}

	private void registerLoginPacket(Direction direction, int packetId,
	                                 Class<? extends Packet<?>> packetClass) {
		this.register(direction, ConnectionState.LOGIN, packetId, packetClass);
	}

	private void registerPlayPacket(Direction direction, int packetId,
	                                Class<? extends Packet<?>> packetClass) {
		this.register(direction, ConnectionState.PLAY, packetId, packetClass);
	}
}
