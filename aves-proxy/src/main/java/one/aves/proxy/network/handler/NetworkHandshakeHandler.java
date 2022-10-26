package one.aves.proxy.network.handler;

import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.protocol.connectionstate.ConnectionState;
import one.aves.proxy.network.protocol.packet.handshake.HandshakePacket;

public class NetworkHandshakeHandler implements NetworkHandler {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(NetworkHandshakeHandler.class);
	private final MinecraftConnection connection;

	public NetworkHandshakeHandler(MinecraftConnection connection) {
		this.connection = connection;
	}

	public void handle(HandshakePacket packet) {
		this.connection.setProtocolVersion(packet.getProtocolVersion());
		byte nextState = packet.getNextState();
		if (nextState == ConnectionState.LOGIN.getId()) {
			LOGGER.printInfo("Client requested login");
			this.connection.setConnectionState(ConnectionState.LOGIN);
			return;
		}

		if (nextState == ConnectionState.STATUS.getId()) {
			LOGGER.printInfo("Client requested status");
			this.connection.setConnectionState(ConnectionState.STATUS);
			this.connection.setNetworkHandler(new NetworkStatusHandler(this.connection));
			return;
		}

		LOGGER.printWarning("Client requested an invalid state (%s)", nextState);
	}
}
