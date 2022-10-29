package one.aves.proxy.network.packet.login.clientbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkLoginHandler;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class LoginSuccessPacket implements Packet<NetworkLoginHandler> {

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
