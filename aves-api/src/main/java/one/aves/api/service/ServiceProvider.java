package one.aves.api.service;

import com.google.common.collect.Maps;

import java.util.Map;

public class ServiceProvider {

	private static final Map<Class<? extends Service>, Service> SERVICES = Maps.newHashMap();

	private ServiceProvider() {
	}

	public static synchronized <T extends Service> T get(Class<T> serviceClass) {
		return (T) SERVICES.get(serviceClass);
	}

	public static synchronized void register(Class<? extends Service> cls, Service service) {
		Class<? extends Service> serviceClass = service.getClass();
		if (cls != serviceClass && !cls.isAssignableFrom(serviceClass)) {
			throw new IllegalArgumentException("Service class mismatch");
		}

		Service existingService = SERVICES.get(cls);
		if (existingService != null) {
			throw new IllegalStateException("Service " + cls.getName() + " is already registered");
		}

		SERVICES.put(cls, service);
	}
}
