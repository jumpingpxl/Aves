package one.aves.proxy.network.protocol.packet.login.clientbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkLoginHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class LoginSuccessPacket implements NettyPacket<NetworkLoginHandler> {

	private UUID uniqueId;
	private String userName;

	public LoginSuccessPacket(@Nonnull UUID uniqueId, @Nonnull String userName) {
		Objects.requireNonNull(uniqueId, "Unique id cannot be null");
		Objects.requireNonNull(userName, "User name cannot be null");
		this.uniqueId = uniqueId;
		this.userName = userName;
	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.uniqueId = UUID.fromString(byteBuffer.readString());
		this.userName = byteBuffer.readString();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeString(this.uniqueId.toString());
		byteBuffer.writeString(this.userName);
	}

	public UUID getUniqueId() {
		return this.uniqueId;
	}

	public String getUserName() {
		return this.userName;
	}
}
