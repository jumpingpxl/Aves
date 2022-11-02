package one.aves.proxy.network;

import one.aves.api.component.Component;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.connection.Connection;
import one.aves.api.network.connection.ConnectionState;
import one.aves.api.network.connection.GameProfile;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.connection.EmptyGameProfile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class DefaultConnection implements Connection {

	private static final GameProfile EMPTY_GAME_PROFILE = new EmptyGameProfile();

	private final MinecraftConnection connection;
	private final ProtocolVersion protocolVersion;
	private ConnectionState connectionState;
	private GameProfile gameProfile;

	public DefaultConnection(MinecraftConnection connection, ProtocolVersion protocolVersion) {
		this.connection = connection;
		this.protocolVersion = protocolVersion;
		this.connectionState = ConnectionState.HANDSHAKE;
		this.gameProfile = EMPTY_GAME_PROFILE;
	}

	@Override
	public @Nonnull GameProfile gameProfile() {
		return this.gameProfile;
	}

	@Override
	public @Nonnull ProtocolVersion protocolVersion() {
		return this.protocolVersion;
	}

	@Override
	public @Nonnull ConnectionState connectionState() {
		return this.connectionState;
	}

	@Override
	public void sendPacket(@Nonnull Packet<?> packet, @Nullable Runnable completionListener) {
		Objects.requireNonNull(packet, "Packet cannot be null");
		this.connection.sendPacket(packet, completionListener);
	}

	@Override
	public void disconnect(@Nonnull Component reason) {
		Objects.requireNonNull(reason, "Reason cannot be null");
		this.connection.disconnect(reason);
	}

	public void updateConnectionState(ConnectionState connectionState) {
		this.connectionState = connectionState;
	}

	public void updateGameProfile(GameProfile gameProfile) {
		this.gameProfile = gameProfile;
	}
}
