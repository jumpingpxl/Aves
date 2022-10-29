package one.aves.api.connection;

import java.util.Map;
import java.util.UUID;

public interface GameProfile {

	UUID getUniqueId();

	String getUserName();

	Map<String, Property> getProperties();

	class Property {

		private final String name;
		private final String value;
		private final String signature;

		public Property(String name, String value, String signature) {
			this.name = name;
			this.value = value;
			this.signature = signature;
		}

		public String getName() {
			return this.name;
		}

		public String getValue() {
			return this.value;
		}

		public String getSignature() {
			return this.signature;
		}

		public boolean hasSignature() {
			return this.signature != null;
		}
	}
}
