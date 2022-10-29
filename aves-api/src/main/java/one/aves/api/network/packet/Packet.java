package one.aves.api.network.packet;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.ProtocolVersion;

public interface Packet<T extends NetworkHandler> {

	void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol);

	void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol);

	default void handle(T networkHandler) {
	}
}
