package one.aves.api.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Registry<V, I> {

	private final BiMap<I, V> values;
	private final Rule rule;

	private Registry(Rule rule) {
		this.values = HashBiMap.create();
		this.rule = rule;
	}

	public static <V, I> Registry<V, I> create(Rule rule) {
		return new Registry<>(rule);
	}

	public boolean register(I identifier, V value) {
		if (this.rule == Rule.DO_NOT_OVERWRITE && this.values.containsKey(identifier)) {
			return false;
		}

		this.values.put(identifier, value);
		return true;
	}

	public V get(I identifier) {
		return this.values.get(identifier);
	}

	public I getIdentifier(V value) {
		return this.values.inverse().get(value);
	}

	public boolean unregister(I identifier) {
		return this.values.remove(identifier) != null;
	}

	public boolean unregisterByValue(V value) {
		return this.values.inverse().remove(value) != null;
	}

	public enum Rule {
		OVERWRITE,
		DO_NOT_OVERWRITE
	}
}
