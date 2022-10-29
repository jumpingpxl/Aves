package one.aves.proxy.network.packet.handshake.serverbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.connection.ConnectionState;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkHandshakeHandler;

public class HandshakePacket implements Packet<NetworkHandshakeHandler> {

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
