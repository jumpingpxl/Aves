package one.aves.api.event;

import one.aves.api.service.Service;

public interface EventService extends Service {

	<T extends Event> T fire(T event);

	void registerListener(Object listener);

	void unregisterListener(Object listener);
}
