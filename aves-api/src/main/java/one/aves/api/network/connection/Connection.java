package one.aves.api.network.connection;

import one.aves.api.component.Component;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public interface Connection {

	@Nonnull
	GameProfile gameProfile();

	@Nonnull
	ProtocolVersion protocolVersion();

	@Nonnull
	ConnectionState connectionState();

	void sendPacket(@Nonnull Packet<?> packet, @Nullable Runnable onCompletion);

	void disconnect(@Nonnull Component reason);

	default void sendPacket(@Nonnull Packet<?> packet) {
		this.sendPacket(packet, null);
	}

	default void disconnect(@Nonnull String reason) {
		Objects.requireNonNull(reason, "Reason cannot be null");
		this.disconnect(Component.text(reason));
	}
}
