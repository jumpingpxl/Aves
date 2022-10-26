package one.aves.proxy.network.handler;

import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.protocol.packet.status.PingPongPacket;
import one.aves.proxy.network.protocol.packet.status.StatusPacket;

public class NetworkStatusHandler implements NetworkHandler {

	private static final ConsoleLogger LOGGER = ConsoleLogger.getLogger(NetworkStatusHandler.class);
	private final MinecraftConnection connection;
	private boolean requestedStatus;

	public NetworkStatusHandler(MinecraftConnection connection) {
		this.connection = connection;
	}

	public void handlePing(PingPongPacket packet) {
		LOGGER.printInfo("Client requested ping " + packet.getClientTime());
		this.connection.sendPacket(new PingPongPacket(packet.getClientTime()));
		this.connection.close();
	}

	public void handleStatusRequest(StatusPacket packet) {
		LOGGER.printInfo("Client requested status " + this.requestedStatus);
		if (this.requestedStatus) {
			this.connection.close();
			return;
		}

		this.requestedStatus = true;
		this.connection.sendPacket(new StatusPacket());
	}
}
