package one.aves.api.network.packet;

import one.aves.api.network.Direction;
import one.aves.api.network.connection.ConnectionState;
import one.aves.api.service.Service;

public interface PacketRegistry extends Service {

	void registerClientBound(ConnectionState state, int id, Class<? extends Packet<?>> packetClass);

	void registerServerBound(ConnectionState state, int id, Class<? extends Packet<?>> packetClass);

	Packet<?> getPacket(ConnectionState state, Direction direction, int packetId);

	int getPacketId(ConnectionState state, Direction direction, Packet<?> packet);

	int getPacketId(ConnectionState state, Direction direction, Class<? extends Packet<?>> packet);

	ConnectionState getConnectionState(Class<? extends Packet> packetClass);
}
