package one.aves.proxy.network.handler;

import one.aves.api.component.Component;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.event.events.network.LoginStartEvent;
import one.aves.api.event.events.network.LoginSuccessEvent;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.connection.GameProfile;
import one.aves.proxy.DefaultAves;
import one.aves.proxy.connection.IncompleteGameProfile;
import one.aves.proxy.network.DefaultConnection;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.packet.login.clientbound.EncryptionRequestPacket;
import one.aves.proxy.network.packet.login.clientbound.LoginSuccessPacket;
import one.aves.proxy.network.packet.login.serverbound.EncryptionResponsePacket;
import one.aves.proxy.network.packet.login.serverbound.LoginStartPacket;
import one.aves.proxy.player.DefaultPlayer;

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
		DefaultConnection connection = this.connection.connection();
		connection.updateGameProfile(new IncompleteGameProfile(packet.getUserName()));
		DefaultAves aves = this.connection.aves();

		LoginStartEvent event = new LoginStartEvent(connection);
		aves.eventService().fire(event);
		if (event.hasDisconnectReason()) {
			this.disconnect(event.getDisconnectReason());
			return;
		}

		EncryptionRequestPacket encryptionPacket = new EncryptionRequestPacket(aves.getServerId(),
				aves.getKeyPair().getPublic(), this.verifyToken);
		this.connection.sendPacket(encryptionPacket);
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
		DefaultConnection connection = this.connection.connection();
		aves.userAuthenticator().authenticate(aves.getServerId(),
				connection.gameProfile().getUserName(), aves.getKeyPair().getPublic(), this.secretKey,
				callback -> {
					if (callback.hasException()) {
						this.disconnect(callback.getException().getMessage());
						return;
					}

					GameProfile gameProfile = callback.get();
					LOGGER.printInfo("Accepting user %s with uuid %s", gameProfile.getUserName(),
							gameProfile.getUniqueId());
					connection.updateGameProfile(gameProfile);

					DefaultPlayer player = this.connection.createPlayer();
					LoginSuccessEvent event = new LoginSuccessEvent(player);
					aves.eventService().fire(event);
					if (event.hasDisconnectReason()) {
						this.disconnect(event.getDisconnectReason());
						return;
					}

					this.connection.sendPacket(
							new LoginSuccessPacket(gameProfile.getUniqueId(), gameProfile.getUserName()));
				});
	}

	private void disconnect(String reason) {
		this.disconnect(Component.text(reason));
	}

	private void disconnect(Component reason) {
		this.connection.disconnect(reason);
	}
}
