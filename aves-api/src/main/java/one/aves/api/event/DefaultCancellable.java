package one.aves.api.event;

public abstract class DefaultCancellable implements Cancellable {

	private boolean cancelled;

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
