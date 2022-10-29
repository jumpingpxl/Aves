package one.aves.proxy.network.protocol.packet.handshake.serverbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkHandshakeHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.connectionstate.ConnectionState;

public class HandshakePacket implements NettyPacket<NetworkHandshakeHandler> {

	private int protocol;
	private String serverAddress;
	private int serverPort;
	private ConnectionState nextState;

	private ProtocolVersion protocolVersion;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.protocol = byteBuffer.readVarInt();
		this.serverAddress = byteBuffer.readString();
		this.serverPort = byteBuffer.readUnsignedShort();
		this.nextState = ConnectionState.getById(byteBuffer.readVarInt());
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeVarInt(this.protocol);
		byteBuffer.writeString(this.serverAddress);
		byteBuffer.writeShort(this.serverPort);
		byteBuffer.writeVarInt(this.nextState.getId());
	}

	@Override
	public void handle(NetworkHandshakeHandler networkHandler) {
		networkHandler.handle(this);
	}

	public int getProtocol() {
		return this.protocol;
	}

	public ProtocolVersion getProtocolVersion() {
		if (this.protocolVersion == null) {
			this.protocolVersion = ProtocolVersion.getByProtocol(this.protocol);
		}

		return this.protocolVersion;
	}

	public String getServerAddress() {
		return this.serverAddress;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public ConnectionState nextState() {
		return this.nextState;
	}
}
