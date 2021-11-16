package one.aves.proxy.network.protocol;

public interface NettyPacket {

	void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol);

	void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol);
}
