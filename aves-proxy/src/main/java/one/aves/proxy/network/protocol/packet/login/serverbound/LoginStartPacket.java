package one.aves.proxy.network.protocol.packet.login.serverbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkLoginHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

public class LoginStartPacket implements NettyPacket<NetworkLoginHandler> {

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
