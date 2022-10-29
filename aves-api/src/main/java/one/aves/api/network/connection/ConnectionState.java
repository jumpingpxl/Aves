package one.aves.api.network.connection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import one.aves.api.network.packet.Packet;
import one.aves.api.network.packet.PacketRegistry;
import one.aves.api.service.ServiceProvider;

public enum ConnectionState {
	HANDSHAKE(-1),
	STATUS(1),
	LOGIN(2),
	PLAY(0);

	public static final AttributeKey<ConnectionState> ATTRIBUTE_KEY = AttributeKey.valueOf(
			"protocol");

	private final int id;

	ConnectionState(int id) {
		this.id = id;
	}

	public static ConnectionState fromContext(ChannelHandlerContext context) {
		return context.channel().attr(ATTRIBUTE_KEY).get();
	}

	public static ConnectionState fromPacket(Class<? extends Packet> packetClass) {
		return ServiceProvider.get(PacketRegistry.class).getConnectionState(packetClass);
	}

	/**
	 * Gets the connection state by id. this is done via a switch statement as {@link #values()}
	 * is really inefficient and caching the values would result in every packet being registered
	 * twice.
	 *
	 * @param id the state id
	 * @return the connection state by id
	 */
	public static ConnectionState getById(int id) {
		return switch (id) {
			case -1 -> HANDSHAKE;
			case 0 -> PLAY;
			case 1 -> STATUS;
			case 2 -> LOGIN;
			default -> throw new IllegalArgumentException("Unknown connection state id: " + id);
		};
	}

	public int getId() {
		return this.id;
	}
}
