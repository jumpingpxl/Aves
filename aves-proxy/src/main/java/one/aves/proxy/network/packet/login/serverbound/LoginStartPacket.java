package one.aves.proxy.network.packet.login.serverbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkLoginHandler;

public class LoginStartPacket implements Packet<NetworkLoginHandler> {

	private String userName;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.userName = byteBuffer.readString();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeString(this.userName);
	}

	@Override
	public void handle(NetworkLoginHandler networkHandler) {
		networkHandler.handleLogin(this);
	}

	public String getUserName() {
		return this.userName;
	}
}
