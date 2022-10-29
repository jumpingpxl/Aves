package one.aves.proxy.network.packet.login.serverbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkLoginHandler;
import one.aves.proxy.util.EncryptionHelper;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

public class EncryptionResponsePacket implements Packet<NetworkLoginHandler> {

	private byte[] secretKeyEncrypted;
	private byte[] verifyTokenEncrypted;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.secretKeyEncrypted = byteBuffer.readByteArray();
		this.verifyTokenEncrypted = byteBuffer.readByteArray();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeByteArray(this.secretKeyEncrypted);
		byteBuffer.writeByteArray(this.verifyTokenEncrypted);
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
