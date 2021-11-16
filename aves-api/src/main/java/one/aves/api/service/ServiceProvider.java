package one.aves.api.service;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class ServiceProvider {

	private static final Map<Class<? extends Service>, Service> services = Maps.newHashMap();

	public final synchronized <T extends Service> T getService(
			Class<? extends Service> serviceClass) {
		return (T) services.get(serviceClass);
	}

	public final synchronized void addService(Service service) {
		services.put(service.getClass(), service);
	}
}
