package one.aves.api.service;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class ServiceProvider {

	private static final Map<Class<? extends Service>, Service> SERVICES = Maps.newHashMap();

	public static synchronized <T extends Service> T get(Class<T> serviceClass) {
		return (T) SERVICES.get(serviceClass);
	}

	public static synchronized void add(Service service) {
		Class<? extends Service> serviceClass = service.getClass();
		Service existingService = SERVICES.get(serviceClass);
		if (existingService != null) {
			throw new IllegalStateException(
					"Service " + serviceClass.getName() + " is already " + "registered exists");
		}

		SERVICES.put(serviceClass, service);
	}
}
