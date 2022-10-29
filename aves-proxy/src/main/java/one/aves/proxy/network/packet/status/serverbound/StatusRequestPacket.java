package one.aves.proxy.network.packet.status.serverbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkStatusHandler;

public class StatusRequestPacket implements Packet<NetworkStatusHandler> {

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
