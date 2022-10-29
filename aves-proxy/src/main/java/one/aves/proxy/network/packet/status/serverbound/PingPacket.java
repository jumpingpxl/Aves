package one.aves.proxy.network.packet.status.serverbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkStatusHandler;

public class PingPacket implements Packet<NetworkStatusHandler> {

	private long clientTime;

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.clientTime = byteBuffer.readLong();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeLong(this.clientTime);
	}

	@Override
	public void handle(NetworkStatusHandler networkHandler) {
		networkHandler.handlePing(this);
	}

	public long getClientTime() {
		return this.clientTime;
	}
}
