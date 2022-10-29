package one.aves.proxy.network.protocol.packet.status.serverbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkStatusHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

public class StatusRequestPacket implements NettyPacket<NetworkStatusHandler> {

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		// the status request packet is empty
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		// the status request packet is empty
	}

	@Override
	public void handle(NetworkStatusHandler networkHandler) {
		networkHandler.handleStatusRequest(this);
	}
}
