package one.aves.api.event.events.network;

import one.aves.api.event.Event;
import one.aves.api.event.extras.Disconnectable;
import one.aves.api.network.connection.Connection;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LoginStartEvent extends Disconnectable implements Event {

	private final Connection connection;
	private final String userName;

	public LoginStartEvent(@Nonnull Connection connection) {
		Objects.requireNonNull(connection, "Connection cannot be null");
		this.connection = connection;
		this.userName = connection.gameProfile().getUserName();
	}

	public Connection connection() {
		return this.connection;
	}

	public String getUserName() {
		return this.userName;
	}
}
