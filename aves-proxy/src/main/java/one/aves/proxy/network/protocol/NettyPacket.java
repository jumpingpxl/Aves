package one.aves.proxy.network.protocol;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkHandler;

public interface NettyPacket<T extends NetworkHandler> {

	void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol);

	void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol);

	default void handle(T networkHandler) {
	}
}
