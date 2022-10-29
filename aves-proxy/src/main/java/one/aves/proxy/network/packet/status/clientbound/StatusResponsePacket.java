package one.aves.proxy.network.packet.status.clientbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.connection.ServerInfo;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkStatusHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StatusResponsePacket implements Packet<NetworkStatusHandler> {

	private final ServerInfo serverInfo;

	public StatusResponsePacket(@Nonnull ServerInfo serverInfo) {
		Objects.requireNonNull(serverInfo, "Server ping cannot be null");
		this.serverInfo = serverInfo;
	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		//todo
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeString(this.serverInfo.toJsonString(protocol));
	}
}
