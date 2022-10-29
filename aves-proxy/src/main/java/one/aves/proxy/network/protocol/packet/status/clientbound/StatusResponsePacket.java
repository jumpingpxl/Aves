package one.aves.proxy.network.protocol.packet.status.clientbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.api.connection.ServerInfo;
import one.aves.proxy.network.handler.NetworkStatusHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StatusResponsePacket implements NettyPacket<NetworkStatusHandler> {

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
