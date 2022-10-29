package one.aves.proxy.util;

import com.google.common.collect.Lists;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.util.StringHelper;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class MethodResolver<T extends Annotation> {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(MethodResolver.class);

	private final Class<T> annotationClass;
	private final List<Class<?>> expectedParameters;
	private final boolean keepParameterOrder;

	public MethodResolver(Class<T> annotationClass, Class<?> expectedParameter) {
		this(annotationClass, true, expectedParameter);
	}

	public MethodResolver(Class<T> annotationClass, boolean keepParameterOrder,
	                      Class<?>... expectedParameters) {
		this.annotationClass = annotationClass;
		this.keepParameterOrder = keepParameterOrder;
		if (expectedParameters == null || expectedParameters.length == 0) {
			throw new IllegalArgumentException("Expected parameters cannot be empty");
		}

		this.expectedParameters = Lists.newArrayList(expectedParameters);
		for (Annotation annotation : annotationClass.getAnnotations()) {
			if (annotation.annotationType() == Target.class) {
				Target target = (Target) annotation;
				for (ElementType elementType : target.value()) {
					if (elementType == ElementType.METHOD) {
						return;
					}
				}

				throw new IllegalArgumentException(
						"Annotation " + annotationClass.getSimpleName() + " is not a method annotation");
			}
		}
	}

	public boolean resolve(Class<?> methodClass, BiConsumer<Method, T> consumer) {
		int parameterCount = this.expectedParameters.size();

		boolean found = false;
		for (Method method : methodClass.getMethods()) {
			if (!method.isAnnotationPresent(this.annotationClass) || !this.parametersMatch(method,
					parameterCount)) {
				continue;
			}

			found = true;
			T annotation = method.getAnnotation(this.annotationClass);
			consumer.accept(method, annotation);
		}

		return found;
	}

	private boolean parametersMatch(Method method, int parameterCount) {
		if (method.getParameterCount() != parameterCount) {
			LOGGER.printWarning(
					"Method %s has annotation %s but wrong number of parameters (%d instead of %d)",
					method.getName(), this.annotationClass.getSimpleName(), method.getParameterCount(),
					parameterCount);
			return false;
		}

		List<Class<?>> parameters = Arrays.asList(method.getParameterTypes());
		if (this.keepParameterOrder) {
			for (int i = 0; i < parameterCount; i++) {
				Class<?> expectedParameter = this.expectedParameters.get(i);
				Class<?> parameter = parameters.get(i);
				if (!expectedParameter.isAssignableFrom(parameter)) {
					LOGGER.printWarning(
							"Method %s has annotation %s but wrong parameter type at index %d (%s instead of "
									+ "%s)", method.getName(), this.annotationClass.getSimpleName(), i,
							expectedParameter.getSimpleName(), parameter.getSimpleName());
					return false;
				}
			}

			return true;
		}

		if (!this.expectedParameters.containsAll(parameters)) {
			LOGGER.printWarning(
					"Method %s has annotation %s but wrong parameters (%s instead of %s - order is "
							+ "irrelevant)", method.getName(), this.annotationClass.getSimpleName(),
					StringHelper.join(parameters, ", "), StringHelper.join(this.expectedParameters, ", "));
			return false;
		}

		return true;
	}
}
