package one.aves.api.event;

public interface Cancellable {

	boolean isCancelled();

	void setCancelled(boolean cancelled);
}
