package one.aves.proxy.player;

import one.aves.api.network.connection.Connection;
import one.aves.api.player.Player;
import one.aves.proxy.network.DefaultConnection;

import javax.annotation.Nonnull;

public class DefaultPlayer implements Player {

	private final DefaultConnection connection;

	public DefaultPlayer(DefaultConnection connection) {
		this.connection = connection;
	}

	@Override
	public @Nonnull Connection connection() {
		return this.connection;
	}
}
