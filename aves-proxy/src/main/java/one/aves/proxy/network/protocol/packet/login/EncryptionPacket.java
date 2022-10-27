package one.aves.proxy.network.protocol.packet.login;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkLoginHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.util.EncryptionHelper;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionPacket implements LoginNettyPacket {

	//EncryptionRequest
	private PublicKey publicKey;
	private byte[] verifyToken;

	//EncryptionResponse
	private byte[] secretKeyEncrypted;
	private byte[] verifyTokenEncrypted;

	public EncryptionPacket() {
		secretKeyEncrypted = new byte[0];
		verifyTokenEncrypted = new byte[0];
	}

	public EncryptionPacket(PublicKey publicKey, byte[] verifyToken) {
		this.publicKey = publicKey;
		this.verifyToken = verifyToken;
	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.secretKeyEncrypted = byteBuffer.readByteArray();
		this.verifyTokenEncrypted = byteBuffer.readByteArray();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeString("");
		byteBuffer.writeByteArray(publicKey.getEncoded());
		byteBuffer.writeByteArray(verifyToken);
	}

	@Override
	public void handle(NetworkLoginHandler networkHandler) {
		networkHandler.handleEncryptionResponse(this);
	}

	public SecretKey getSecretKey(PrivateKey key) {
		return EncryptionHelper.decryptSharedKey(key, this.secretKeyEncrypted);
	}

	public byte[] getVerifyToken(PrivateKey key) {
		return key == null ? this.verifyTokenEncrypted : EncryptionHelper.decryptData(key,
				this.verifyTokenEncrypted);
	}
}
