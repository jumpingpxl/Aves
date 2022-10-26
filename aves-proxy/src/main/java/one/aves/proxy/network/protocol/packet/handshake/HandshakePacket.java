package one.aves.proxy.network.protocol.packet.handshake;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkHandshakeHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

public class HandshakePacket implements NettyPacket<NetworkHandshakeHandler> {

	private int protocol;
	private String serverAddress;
	private int serverPort;
	private byte nextState;

	private ProtocolVersion protocolVersion;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		try {
			this.protocol = byteBuffer.readVarInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.serverAddress = byteBuffer.readString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.serverPort = byteBuffer.readUnsignedShort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.nextState = byteBuffer.readByte();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {

	}

	@Override
	public void handle(NetworkHandshakeHandler networkHandler) {
		networkHandler.handle(this);
	}

	public int getProtocol() {
		return protocol;
	}

	public ProtocolVersion getProtocolVersion() {
		if (this.protocolVersion == null) {
			this.protocolVersion = ProtocolVersion.getByProtocol(this.protocol);
		}

		return this.protocolVersion;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public byte getNextState() {
		return nextState;
	}
}
