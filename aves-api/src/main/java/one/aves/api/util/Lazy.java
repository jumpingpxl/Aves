package one.aves.api.util;

import java.util.function.Supplier;

public class Lazy<T> {

	private final Supplier<T> supplier;
	private T value;

	private Lazy(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public static <T> Lazy<T> of(Supplier<T> supplier) {
		return new Lazy<>(supplier);
	}

	public T get() {
		if (this.value == null) {
			this.value = this.supplier.get();
		}

		return this.value;
	}

	public T getFromSupplier() {
		return this.supplier.get();
	}

	public void reset() {
		this.value = null;
	}
}
