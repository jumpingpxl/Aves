package one.aves.api;

import one.aves.api.event.Event;
import one.aves.api.event.EventService;
import one.aves.api.network.packet.PacketRegistry;
import one.aves.api.service.Service;
import one.aves.api.service.ServiceProvider;

public interface Aves extends Service {

	static Aves get() {
		return ServiceProvider.get(Aves.class);
	}

	static Event fire(Event event) {
		return ServiceProvider.get(EventService.class).fire(event);
	}

	EventService eventService();

	PacketRegistry packetRegistry();

	String getServerId();
}
