package one.aves.proxy.event;

import one.aves.api.component.Component;
import one.aves.api.component.TextColor;
import one.aves.api.event.Priority;
import one.aves.api.event.Subscribe;
import one.aves.api.event.events.network.ServerListPingEvent;

public class TestListener {

	@Subscribe(Priority.LATEST)
	public void onServerListPing(ServerListPingEvent event) {
		event.serverInfo().setOnlinePlayers(420);
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
}
