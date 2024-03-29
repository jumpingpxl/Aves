package one.aves.proxy.network.handler;

import one.aves.api.console.ConsoleLogger;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.connection.ConnectionState;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.packet.handshake.serverbound.HandshakePacket;

public class NetworkHandshakeHandler implements NetworkHandler {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(NetworkHandshakeHandler.class);
	private final MinecraftConnection connection;

	public NetworkHandshakeHandler(MinecraftConnection connection) {
		this.connection = connection;
	}

	public void handle(HandshakePacket packet) {
		this.connection.setProtocolVersion(packet.getProtocolVersion());
		ConnectionState nextState = packet.nextState();
		if (nextState == ConnectionState.LOGIN) {
			LOGGER.printInfo("Client requested login");
			this.connection.setConnectionState(ConnectionState.LOGIN);
			this.connection.setNetworkHandler(new NetworkLoginHandler(this.connection));
			return;
		}

		if (nextState == ConnectionState.STATUS) {
			LOGGER.printInfo("Client requested status");
			this.connection.setConnectionState(ConnectionState.STATUS);
			this.connection.setNetworkHandler(new NetworkStatusHandler(this.connection));
			return;
		}

		LOGGER.printWarning("Client requested an invalid state (%s)", nextState);
	}
}
