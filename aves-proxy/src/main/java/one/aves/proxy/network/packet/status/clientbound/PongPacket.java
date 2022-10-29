package one.aves.proxy.network.packet.status.clientbound;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkStatusHandler;

public class PongPacket implements Packet<NetworkStatusHandler> {

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
