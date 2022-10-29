package one.aves.proxy.event;

import one.aves.api.event.Event;
import one.aves.api.event.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SubscribeMethod {

	private final Object instance;
	private final Method method;
	private final Subscribe annotation;
	private final String listenerName;

	public SubscribeMethod(Object instance, Method method, Subscribe annotation) {
		this.instance = instance;
		this.method = method;
		this.annotation = annotation;
		this.listenerName = instance.getClass().getSimpleName();
	}

	public Object getInstance() {
		return this.instance;
	}

	public Method getMethod() {
		return this.method;
	}

	public Subscribe getAnnotation() {
		return this.annotation;
	}

	public String getListenerName() {
		return this.listenerName;
	}

	public void invoke(Event event) throws InvocationTargetException, IllegalAccessException {
		this.method.invoke(this.instance, event);
	}
}
