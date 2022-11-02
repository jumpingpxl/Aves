package one.aves.api.event.events.network;

import one.aves.api.event.Event;
import one.aves.api.event.extras.Disconnectable;
import one.aves.api.player.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LoginSuccessEvent extends Disconnectable implements Event {

	private final Player player;

	public LoginSuccessEvent(@Nonnull Player player) {
		Objects.requireNonNull(player, "Player cannot be null");
		this.player = player;
	}

	public Player player() {
		return this.player;
	}
}
