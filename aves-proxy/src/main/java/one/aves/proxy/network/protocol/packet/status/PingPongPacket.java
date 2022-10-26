package one.aves.proxy.network.protocol.packet.status;

import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkStatusHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;

public class PingPongPacket implements StatusNettyPacket {

	private long clientTime;

	public PingPongPacket(long clientTime) {
		this.clientTime = clientTime;
	}

	public PingPongPacket() {
	}

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
		return clientTime;
	}
}
