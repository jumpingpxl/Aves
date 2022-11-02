package one.aves.proxy.connection;

import one.aves.api.network.connection.GameProfile;

import java.util.Map;
import java.util.UUID;

public class EmptyGameProfile implements GameProfile {

	@Override
	public UUID getUniqueId() {
		throw new UnsupportedOperationException("Game profile is not ready yet");
	}

	@Override
	public String getUserName() {
		throw new UnsupportedOperationException("Game profile is not ready yet");
	}

	@Override
	public Map<String, Property> getProperties() {
		throw new UnsupportedOperationException("Game profile is not ready yet");
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
}
