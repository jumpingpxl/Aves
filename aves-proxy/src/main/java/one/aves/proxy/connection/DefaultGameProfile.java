package one.aves.proxy.connection;

import one.aves.api.connection.GameProfile;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class DefaultGameProfile implements GameProfile {

	private final UUID uniqueId;
	private final String userName;
	private final Map<String, Property> properties;

	public DefaultGameProfile(UUID uniqueId, String userName, Map<String, Property> properties) {
		this.uniqueId = uniqueId;
		this.userName = userName;
		this.properties = Collections.unmodifiableMap(properties);
	}

	@Override
	public UUID getUniqueId() {
		return this.uniqueId;
	}

	@Override
	public String getUserName() {
		return this.userName;
	}

	@Override
	public Map<String, Property> getProperties() {
		return this.properties;
	}
}
