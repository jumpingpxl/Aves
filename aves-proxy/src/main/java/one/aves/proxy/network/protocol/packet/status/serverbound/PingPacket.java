package one.aves.proxy.network.protocol.packet.status.serverbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkStatusHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

public class PingPacket implements NettyPacket<NetworkStatusHandler> {

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
