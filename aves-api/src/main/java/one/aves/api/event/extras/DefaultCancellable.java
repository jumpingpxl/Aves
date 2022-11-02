package one.aves.api.event.extras;

public abstract class DefaultCancellable implements Cancellable {

	private boolean cancelled;

	protected DefaultCancellable() {
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
