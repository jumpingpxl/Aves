package one.aves.proxy.event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.event.Event;
import one.aves.api.event.EventService;
import one.aves.api.event.Subscribe;
import one.aves.api.service.ServiceProvider;
import one.aves.proxy.util.MethodResolver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultEventService implements EventService {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(EventService.class);

	private final MethodResolver<Subscribe> methodResolver;
	private final Map<Class<? extends Event>, List<SubscribeMethod>> subscribeMethods;
	private final Set<Class<?>> checkedClasses;

	public DefaultEventService() {
		this.methodResolver = new MethodResolver<>(Subscribe.class, Event.class);
		this.subscribeMethods = Maps.newHashMap();
		this.checkedClasses = Sets.newHashSet();

		ServiceProvider.register(EventService.class, this);
	}

	@Override
	public <T extends Event> T fire(T event) {
		List<SubscribeMethod> methods = this.subscribeMethods.get(event.getClass());
		if (methods == null) {
			return event;
		}

		LOGGER.printInfo("Fired event %s to %d listeners", event.getClass().getSimpleName(),
				methods.size());
		for (SubscribeMethod method : methods) {
			try {
				method.invoke(event);
			} catch (InvocationTargetException e) {
				LOGGER.printException(
						"An error occurred while trying to invoke event listener method %s#%s", e.getCause(),
						method.getListenerName(), method.getMethod().getName());
			} catch (IllegalAccessException e) {
				LOGGER.printException("Failed to invoke event listener method %s#%s", e,
						method.getListenerName(), method.getMethod().getName());
			}
		}

		return event;
	}

	@Override
	public void registerListener(Object listener) {
		Class<?> listenerClass = listener.getClass();
		if (this.checkedClasses.contains(listenerClass)) {
			throw new IllegalArgumentException("Listener already registered");
		}

		this.checkedClasses.add(listenerClass);
		List<Class<?>> resolvedEvents = new ArrayList<>();
		boolean resolved = this.methodResolver.resolve(listenerClass, (method, annotation) -> {
			Class<?> parameterType = method.getParameterTypes()[0];
			resolvedEvents.add(parameterType);

			List<SubscribeMethod> subscribeMethods = this.subscribeMethods.get(parameterType);
			SubscribeMethod subscribeMethod = new SubscribeMethod(listener, method, annotation);
			if (subscribeMethods == null) {
				subscribeMethods = new ArrayList<>();
				subscribeMethods.add(subscribeMethod);
				this.subscribeMethods.put((Class<? extends Event>) parameterType, subscribeMethods);
				return;
			}

			subscribeMethods.add(subscribeMethod);
		});

		if (!resolved) {
			LOGGER.printWarning("No methods annotated with @%s found in class %s!",
					Subscribe.class.getSimpleName(), listenerClass.getSimpleName());
		}

		for (Class<?> resolvedEvent : resolvedEvents) {
			List<SubscribeMethod> subscribeMethods = this.subscribeMethods.get(resolvedEvent);
			if (subscribeMethods == null) {
				continue;
			}

			subscribeMethods.sort(Comparator.comparingInt(o -> o.getAnnotation().value()));
		}
	}

	@Override
	public void unregisterListener(Object listener) {

	}
}
