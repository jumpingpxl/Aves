package one.aves.proxy.connection;

import one.aves.api.connection.GameProfile;

import java.util.Map;
import java.util.UUID;

public class PrematureGameProfile implements GameProfile {

	private final String userName;

	public PrematureGameProfile(String userName) {
		this.userName = userName;
	}

	@Override
	public UUID getUniqueId() {
		throw new IllegalStateException("Game profile is not ready yet");
	}

	@Override
	public String getUserName() {
		return this.userName;
	}

	@Override
	public Map<String, Property> getProperties() {
		throw new IllegalStateException("Game profile is not ready yet");
	}
}
