package one.aves.proxy.network.protocol.handshake;

import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.ProtocolVersion;

public class HandshakePacket implements NettyPacket {

	private int protocolVersion;
	private String serverAddress;
	private int serverPort;
	private byte nextState;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		protocolVersion = byteBuffer.readVarInt();
		serverAddress = byteBuffer.readString();
		serverPort = byteBuffer.readUnsignedShort();
		nextState = byteBuffer.readByte();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {

	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public byte getNextState() {
		return nextState;
	}
}
