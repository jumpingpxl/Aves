package one.aves.api.event.events.network;

import one.aves.api.event.Event;
import one.aves.api.event.extras.DefaultCancellable;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.connection.Connection;
import one.aves.api.network.connection.ServerInfo;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ServerListPingEvent extends DefaultCancellable implements Event {

	private final Connection connection;
	private final ProtocolVersion protocolVersion;
	private ServerInfo serverInfo;

	public ServerListPingEvent(@Nonnull Connection connection, @Nonnull ServerInfo serverInfo) {
		Objects.requireNonNull(connection, "Connection cannot be null");
		Objects.requireNonNull(serverInfo, "Server info cannot be null");
		this.connection = connection;
		this.protocolVersion = connection.protocolVersion();
		this.serverInfo = serverInfo;
	}

	public @Nonnull Connection connection() {
		return this.connection;
	}

	public @Nonnull ProtocolVersion protocolVersion() {
		return this.protocolVersion;
	}

	public @Nonnull ServerInfo serverInfo() {
		return this.serverInfo;
	}

	public void setServerInfo(@Nonnull ServerInfo serverInfo) {
		Objects.requireNonNull(serverInfo, "Server info cannot be null. Cancel the event instead");
		this.serverInfo = serverInfo;
	}
}
