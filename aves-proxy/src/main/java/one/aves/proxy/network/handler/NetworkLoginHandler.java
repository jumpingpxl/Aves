package one.aves.proxy.network.handler;

import one.aves.api.component.Component;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.connection.GameProfile;
import one.aves.proxy.DefaultAves;
import one.aves.proxy.connection.PrematureGameProfile;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.packet.login.clientbound.EncryptionRequestPacket;
import one.aves.proxy.network.packet.login.clientbound.LoginDisconnectPacket;
import one.aves.proxy.network.packet.login.serverbound.EncryptionResponsePacket;
import one.aves.proxy.network.packet.login.serverbound.LoginStartPacket;

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

	public void handleLogin(LoginStartPacket packet) {
		LOGGER.printInfo("User %s requested login ", packet.getUserName());
		this.connection.updateGameProfile(new PrematureGameProfile(packet.getUserName()));

		DefaultAves aves = this.connection.aves();
		EncryptionRequestPacket encryptionPacket = new EncryptionRequestPacket(aves.getServerId(),
				aves.getKeyPair().getPublic(), this.verifyToken);
		this.connection.sendPacket(encryptionPacket);

		// todo event
		//Component component = Component.text("This server is running ").color(TextColor.GREEN);
		//component.append(Component.text("Aves").color(TextColor.GOLD));
		//component.append(Component.text("Cloud").color(TextColor.of(Color.pink)));
		//component.append(Component.text(" v0.0.1-SNAPSHOT").color(TextColor.GREEN));
		//this.connection.sendPacket(new DisconnectPacket(component));
	}

	public void handleEncryptionResponse(EncryptionResponsePacket packet) {
		LOGGER.printInfo("Encryption response received");
		DefaultAves aves = this.connection.aves();
		PrivateKey privatekey = aves.getKeyPair().getPrivate();

		if (!Arrays.equals(this.verifyToken, packet.getVerifyToken(privatekey))) {
			throw new IllegalStateException("Invalid nonce!");
		}

		this.secretKey = packet.getSecretKey(privatekey);
		this.connection.enableEncryption(this.secretKey);
		aves.userAuthenticator().authenticate(aves.getServerId(),
				this.connection.getGameProfile().getUserName(), aves.getKeyPair().getPublic(),
				this.secretKey, callback -> {
					if (callback.hasException()) {
						this.disconnect(callback.getException().getMessage());
						return;
					}

					GameProfile gameProfile = callback.get();
					LOGGER.printInfo("Accepting user %s with uuid %s", gameProfile.getUserName(),
							gameProfile.getUniqueId());
					this.connection.updateGameProfile(gameProfile);
				});
	}

	private void disconnect(String reason) {
		this.connection.sendPacket(new LoginDisconnectPacket(Component.text(reason)));
	}
}
