package one.aves.proxy.network.protocol.packet.login;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkLoginHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;

public class LoginPacket implements LoginNettyPacket {

	private String username;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.username = byteBuffer.readString();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {

	}

	@Override
	public void handle(NetworkLoginHandler networkHandler) {
		networkHandler.handleLogin(this);
	}

	public String getUsername() {
		return username;
	}
}
