package one.aves.proxy.network.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import one.aves.api.connection.ProtocolVersion;
import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.connectionstate.ConnectionState;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(PacketDecoder.class);
	private final Direction direction;

	public PacketDecoder(Direction direction) {
		this.direction = direction;
	}

	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) {
		if (byteBuf.readableBytes() == 0) {
			return;
		}

		ByteBuffer byteBuffer = ByteBuffer.of(byteBuf);
		int packetId = byteBuffer.readVarInt();
		NettyPacket nettyPacket = ConnectionState.fromContext(context).get(this.direction, packetId);
		if (nettyPacket == null) {
			LOGGER.printSevere("Bad packet id %d", packetId);
			return;
		}

		nettyPacket.decode(byteBuffer, this.direction, ProtocolVersion.fromContext(context));
		if (byteBuffer.readableBytes() > 0) {
			byteBuf.resetReaderIndex();
			LOGGER.printSevere("Packet %s (%d) was larger than expected, found %d bytes extra",
					nettyPacket.getClass().getSimpleName(), packetId, byteBuffer.readableBytes());
			return;
		}

		list.add(nettyPacket);
	}
}
