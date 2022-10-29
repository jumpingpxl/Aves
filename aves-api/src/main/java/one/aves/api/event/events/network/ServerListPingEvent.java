package one.aves.api.event.events.network;

import one.aves.api.connection.ProtocolVersion;
import one.aves.api.connection.ServerInfo;
import one.aves.api.event.DefaultCancellable;
import one.aves.api.event.Event;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ServerListPingEvent extends DefaultCancellable implements Event {

	private final ProtocolVersion protocolVersion;
	private ServerInfo serverInfo;

	public ServerListPingEvent(@Nonnull ProtocolVersion protocolVersion,
	                           @Nonnull ServerInfo serverInfo) {
		Objects.requireNonNull(serverInfo, "Server info cannot be null");
		this.serverInfo = serverInfo;
		this.protocolVersion = protocolVersion;
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
