package one.aves.proxy.event;

import one.aves.api.component.Component;
import one.aves.api.component.Font;
import one.aves.api.component.TextColor;
import one.aves.api.event.Priority;
import one.aves.api.event.Subscribe;
import one.aves.api.event.events.network.LoginStartEvent;
import one.aves.api.event.events.network.LoginSuccessEvent;
import one.aves.api.event.events.network.ServerListPingEvent;

import java.awt.*;

public class TestListener {

	@Subscribe(Priority.LATEST)
	public void onServerListPing(ServerListPingEvent event) {
		event.serverInfo().setOnlinePlayers(420);
		//event.setCancelled(true);
	}

	@Subscribe(Priority.EARLY)
	public void onPing(ServerListPingEvent event) {
		Component component = Component.text("Hello, ").color(TextColor.YELLOW);
		component.append("I am ").color(TextColor.LIME);
		component.append("Aves").color(TextColor.GOLD);
		component.append(".").color(TextColor.LIME);
		event.serverInfo().setDescription(component);

		event.serverInfo().setOnlinePlayers(512);
	}

	@Subscribe
	public void onLoginStart(LoginStartEvent event) {
		Component component = Component.text("This server is running ").color(TextColor.GREEN);
		component.append(Component.text("Aves").color(TextColor.GOLD).font(Font.ALT));
		component.append(Component.text(" v0.0.1-SNAPSHOT").color(TextColor.of(Color.pink)));
		event.setDisconnectReason(component);
	}

	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent event) {
		Component component =
				Component.text(event.player().getUserName() + ", ").color(TextColor.AQUA);
		component.append(Component.text("this server is running ").color(TextColor.GREEN));
		component.append(Component.text("Aves").color(TextColor.GOLD).font(Font.ALT));
		component.append(Component.text(" v0.0.1-SNAPSHOT").color(TextColor.of(Color.pink)));
		//event.setDisconnectReason(component);
	}
}
