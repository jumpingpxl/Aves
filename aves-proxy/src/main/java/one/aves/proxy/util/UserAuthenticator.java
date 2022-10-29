package one.aves.proxy.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import one.aves.api.util.result.Callback;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.net.Proxy;
import java.security.PublicKey;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UserAuthenticator {

	private static final String THREAD_NAME = "User Authenticator #%d";
	private static final AtomicInteger AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);

	private final YggdrasilAuthenticationService authenticationService;
	private final MinecraftSessionService sessionService;

	public UserAuthenticator() {
		this.authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY,
				UUID.randomUUID().toString());
		this.sessionService = this.authenticationService.createMinecraftSessionService();
	}

	private void authenticate(String serverId, String userName, PublicKey publicKey,
	                          SecretKey secretKey, Callback<GameProfile> callback)
			throws AuthenticationException {

		byte[] serverIdHashBytes = EncryptionHelper.getServerIdHash(serverId, publicKey, secretKey);
		if (serverIdHashBytes == null) {
			callback.acceptException(new AuthenticationException("Could not generate server id hash!"));
			return;
		}

		String serverIdHash = new BigInteger(serverIdHashBytes).toString(16);
		new Thread(String.format(THREAD_NAME, AUTHENTICATOR_THREAD_ID.incrementAndGet())) {
			@Override
			public void run() {
				GameProfile gameProfile;
				try {
					gameProfile = UserAuthenticator.this.sessionService.hasJoinedServer(
							new GameProfile(null, userName), serverIdHash, null);
				} catch (AuthenticationUnavailableException e) {
					callback.acceptException(new AuthenticationUnavailableException(
							"Authentication servers are down. Please try " + "again later, sorry!"));
					return;
				}

				if (gameProfile == null) {
					callback.acceptException(new InvalidCredentialsException("Failed to verify username!"));
					return;
				}

				callback.acceptRaw(gameProfile);
			}
		}.start();
	}
}
