package one.aves.api.player;

import one.aves.api.component.Component;
import one.aves.api.network.connection.Connection;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface Player {

	@Nonnull
	Connection connection();

	default void disconnect(@Nonnull String reason) {
		this.connection().disconnect(reason);
	}

	default void disconnect(@Nonnull Component reason) {
		this.connection().disconnect(reason);
	}

	default @Nonnull UUID getUniqueId() {
		return this.connection().gameProfile().getUniqueId();
	}

	default @Nonnull String getUserName() {
		return this.connection().gameProfile().getUserName();
	}
}
