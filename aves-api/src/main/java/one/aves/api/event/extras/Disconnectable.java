package one.aves.api.event.extras;

import one.aves.api.component.Component;

public class Disconnectable {

	private Component disconnectReason;

	protected Disconnectable() {
	}

	public boolean hasDisconnectReason() {
		return this.disconnectReason != null;
	}

	public Component getDisconnectReason() {
		return this.disconnectReason;
	}

	public void setDisconnectReason(Component disconnectReason) {
		this.disconnectReason = disconnectReason;
	}

	public void setDisconnectReason(String disconnectReason) {
		if (disconnectReason == null) {
			this.disconnectReason = null;
		} else {
			this.disconnectReason = Component.text(disconnectReason);
		}
	}
}
