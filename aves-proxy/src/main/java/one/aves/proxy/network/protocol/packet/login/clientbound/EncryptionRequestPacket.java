package one.aves.proxy.network.protocol.packet.login.clientbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkLoginHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.util.EncryptionHelper;

import javax.annotation.Nonnull;
import java.security.PublicKey;
import java.util.Objects;

public class EncryptionRequestPacket implements NettyPacket<NetworkLoginHandler> {

	private String hashedServerId;
	private PublicKey publicKey;
	private byte[] verifyToken;

	public EncryptionRequestPacket(@Nonnull String hashedServerId, @Nonnull PublicKey publicKey,
	                               @Nonnull byte[] verifyToken) {
		Objects.requireNonNull(hashedServerId, "Hashed server id cannot be null");
		Objects.requireNonNull(publicKey, "Public key cannot be null");
		Objects.requireNonNull(verifyToken, "Verify token cannot be null");
		this.hashedServerId = hashedServerId;
		this.publicKey = publicKey;
		this.verifyToken = verifyToken;
	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.hashedServerId = byteBuffer.readString();
		this.publicKey = EncryptionHelper.decodePublicKey(byteBuffer.readByteArray());
		this.verifyToken = byteBuffer.readByteArray();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeString(this.hashedServerId);
		byteBuffer.writeByteArray(this.publicKey.getEncoded());
		byteBuffer.writeByteArray(this.verifyToken);
	}

	public String getHashedServerId() {
		return this.hashedServerId;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}
}
