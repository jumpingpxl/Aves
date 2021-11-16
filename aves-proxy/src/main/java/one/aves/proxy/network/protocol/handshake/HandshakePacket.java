package one.aves.proxy.network.protocol.handshake;

import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.ProtocolVersion;

public class HandshakePacket implements NettyPacket {

	private int protocolVersion;
	private String serverAdress;
	private int serverPort;
	private byte nextState;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		protocolVersion = byteBuffer.readVarInt();
		serverAdress = byteBuffer.readString();
		serverPort = byteBuffer.readUnsignedShort();
		nextState = byteBuffer.readByte();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {

	}
}
