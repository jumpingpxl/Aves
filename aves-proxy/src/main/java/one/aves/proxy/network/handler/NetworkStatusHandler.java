package one.aves.proxy.network.handler;

import one.aves.api.console.ConsoleLogger;
import one.aves.api.event.events.network.ServerListPingEvent;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.connection.ServerInfo;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.packet.status.clientbound.PongPacket;
import one.aves.proxy.network.packet.status.clientbound.StatusResponsePacket;
import one.aves.proxy.network.packet.status.serverbound.PingPacket;
import one.aves.proxy.network.packet.status.serverbound.StatusRequestPacket;

public class NetworkStatusHandler implements NetworkHandler {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(NetworkStatusHandler.class);
	private final MinecraftConnection connection;
	private boolean requestedStatus;

	public NetworkStatusHandler(MinecraftConnection connection) {
		this.connection = connection;
	}

	public void handlePing(PingPacket packet) {
		LOGGER.printInfo("Client requested ping " + packet.getClientTime());
		this.connection.sendPacket(new PongPacket(packet.getClientTime()));
		this.connection.close();
	}

	public void handleStatusRequest(StatusRequestPacket packet) {
		LOGGER.printInfo("Client requested status " + this.requestedStatus);
		if (this.requestedStatus) {
			this.connection.close();
			return;
		}

		ServerListPingEvent event = new ServerListPingEvent(this.connection.protocolVersion(),
				new ServerInfo());
		this.connection.aves().eventService().fire(event);
		if (event.isCancelled()) {
			this.connection.close();
			return;
		}

		this.requestedStatus = true;
		//todo use server info from event
		this.connection.sendPacket(new StatusResponsePacket(event.serverInfo()));
	}
}
