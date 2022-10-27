package one.aves.proxy.network.handler;

import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.protocol.packet.login.EncryptionPacket;
import one.aves.proxy.network.protocol.packet.login.LoginPacket;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Random;

public class NetworkLoginHandler implements NetworkHandler {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(NetworkLoginHandler.class);
	private final byte[] verifyToken = new byte[4];
	private final MinecraftConnection connection;
	private SecretKey secretKey;

	public NetworkLoginHandler(MinecraftConnection connection) {
		this.connection = connection;
		new Random().nextBytes(this.verifyToken);
	}

	public void handleLogin(LoginPacket packet) {
		LOGGER.printInfo("User %s requested login ", packet.getUsername());

		EncryptionPacket encryptionPacket = new EncryptionPacket(
				this.connection.aves().getKeyPair().getPublic(), verifyToken);
		this.connection.sendPacket(encryptionPacket);

		//Component component = Component.text("This server is running ").color(TextColor.GREEN);
		//component.append(Component.text("Aves").color(TextColor.GOLD));
		//component.append(Component.text("Cloud").color(TextColor.of(Color.pink)));
		//component.append(Component.text(" v0.0.1-SNAPSHOT").color(TextColor.GREEN));

		//this.connection.sendPacket(new DisconnectPacket(component));
	}

	public void handleEncryptionResponse(EncryptionPacket packet) {
		LOGGER.printInfo("Encryption response received");
		PrivateKey privatekey = this.connection.aves().getKeyPair().getPrivate();

		if (!Arrays.equals(this.verifyToken, packet.getVerifyToken(privatekey))) {
			throw new IllegalStateException("Invalid nonce!");
		}

		this.secretKey = packet.getSecretKey(privatekey);
		this.connection.enableEncryption(this.secretKey);
	}
}
