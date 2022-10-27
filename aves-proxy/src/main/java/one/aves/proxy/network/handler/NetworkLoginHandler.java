package one.aves.proxy.network.handler;

import one.aves.api.component.Component;
import one.aves.api.component.TextColor;
import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.network.MinecraftConnection;
import one.aves.proxy.network.protocol.packet.login.DisconnectPacket;
import one.aves.proxy.network.protocol.packet.login.LoginPacket;

public class NetworkLoginHandler implements NetworkHandler {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(NetworkLoginHandler.class);
	private final MinecraftConnection connection;

	public NetworkLoginHandler(MinecraftConnection connection) {
		this.connection = connection;
	}

	public void handleLogin(LoginPacket packet) {
		LOGGER.printInfo("User %s requested login ", packet.getUsername());
		Component component = Component.text("This server is running ").color(TextColor.GREEN);
		component.append(Component.text("Aves").color(TextColor.GOLD));
		component.append(Component.text("Cloud").color(TextColor.YELLOW));
		component.append(Component.text(" v0.0.1-SNAPSHOT").color(TextColor.GREEN));
		this.connection.sendPacket(new DisconnectPacket(component));
	}
}
