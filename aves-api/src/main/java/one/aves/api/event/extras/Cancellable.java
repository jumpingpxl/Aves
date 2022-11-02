package one.aves.api.event.extras;

public interface Cancellable {

	boolean isCancelled();

	void setCancelled(boolean cancelled);
}
