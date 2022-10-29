package one.aves.proxy.network.protocol.packet.status.clientbound;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkStatusHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

public class PongPacket implements NettyPacket<NetworkStatusHandler> {

	private long clientTime = -1;

	public PongPacket(long clientTime) {
		this.clientTime = clientTime;
	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.clientTime = byteBuffer.readLong();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeLong(this.clientTime);
	}

	public long getClientTime() {
		return this.clientTime;
	}
}
