package one.aves.api.util.result;

import java.util.function.Consumer;

public interface Callback<T> extends Consumer<Result<T>> {

	@Override
	void accept(Result<T> result);

	default void acceptRaw(T value) {
		this.accept(Result.ofNullable(value));
	}

	default void acceptException(Exception exception) {
		this.accept(Result.ofException(exception));
	}
}
