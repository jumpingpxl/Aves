package one.aves.api.util.result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Result<T> {

	private T value;
	private Exception exception;
	private Consumer<Result<T>> callback;

	private Result(T value, Exception exception) {
		this.value = value;
		this.exception = exception;
	}

	public static <T> Result<T> of(@Nonnull T value) {
		Objects.requireNonNull(value, "Value cannot be null");
		return new Result<>(value, null);
	}

	public static <T> Result<T> ofNullable(@Nullable T value) {
		return value == null ? empty() : of(value);
	}

	public static <T> Result<T> ofException(@Nonnull Exception exception) {
		Objects.requireNonNull(exception, "Exception cannot be null");
		return new Result<>(null, exception);
	}

	public static <T> Result<T> empty() {
		return new Result<>(null, null);
	}

	public boolean isPresent() {
		return this.value != null;
	}

	public boolean hasException() {
		return this.exception != null;
	}

	public boolean isEmpty() {
		return !this.isPresent() && !this.hasException();
	}

	public @Nonnull T get() {
		if (!this.isPresent()) {
			throw new IllegalStateException("Value not present");
		}

		return this.value;
	}

	public @Nullable T getOrDefault(@Nullable T defaultValue) {
		return this.isPresent() ? this.value : defaultValue;
	}

	public @Nullable T getOrCompute(@Nonnull Supplier<T> supplier) {
		Objects.requireNonNull(supplier, "Supplier cannot be null");
		return this.isPresent() ? this.value : supplier.get();
	}

	public void ifPresent(@Nonnull Consumer<T> consumer) {
		Objects.requireNonNull(consumer, "Consumer cannot be null");
		if (this.isPresent()) {
			consumer.accept(this.value);
		}
	}

	public @Nonnull Exception getException() {
		if (!this.hasException()) {
			throw new IllegalStateException("No exception present");
		}

		return this.exception;
	}

	public void setException(@Nonnull Exception exception) {
		Objects.requireNonNull(exception, "Exception cannot be null");
		if (this.hasException()) {
			throw new IllegalStateException("Cannot overwrite already set exception");
		}

		if (this.isPresent()) {
			throw new IllegalStateException("Cannot set exception when value is present");
		}

		this.exception = exception;
		if (this.callback != null) {
			this.callback.accept(this);
		}
	}

	public void set(@Nonnull T value) {
		Objects.requireNonNull(value, "Value cannot be null");
		if (this.isPresent()) {
			throw new IllegalStateException("Cannot overwrite already set result");
		}

		if (this.hasException()) {
			throw new IllegalStateException("Cannot set value when exception is present");
		}

		this.value = value;
		if (this.callback != null) {
			this.callback.accept(this);
		}
	}

	public @Nonnull Result<T> setCallback(@Nonnull Consumer<Result<T>> callback) {
		Objects.requireNonNull(callback, "Callback cannot be null");
		if (!this.isEmpty()) {
			throw new IllegalStateException("Cannot set callback when result is not empty");
		}

		this.callback = callback;
		return this;
	}
}
