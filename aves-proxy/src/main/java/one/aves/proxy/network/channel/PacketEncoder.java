package one.aves.proxy.network.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import one.aves.api.connection.ProtocolVersion;
import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.connectionstate.ConnectionState;

public class PacketEncoder extends MessageToByteEncoder<NettyPacket> {

	private static final ConsoleLogger LOGGER = ConsoleLogger.getLogger(PacketEncoder.class);
	private final Direction direction;

	public PacketEncoder(Direction direction) {
		this.direction = direction;
	}

	@Override
	protected void encode(ChannelHandlerContext context, NettyPacket nettyPacket, ByteBuf byteBuf) {
		int packetId;
		try {
			packetId = ConnectionState.fromContext(context).get(this.direction, nettyPacket);
		} catch (Exception e) {
			LOGGER.printSevere("Could not get packet id from packet %s",
					nettyPacket.getClass().getSimpleName());
			throw e;
		}

		ByteBuffer byteBuffer = ByteBuffer.of(byteBuf);
		byteBuffer.writeVarInt(packetId);
		try {
			nettyPacket.encode(byteBuffer, this.direction, ProtocolVersion.fromContext(context));
		} catch (Exception e) {
			LOGGER.printException("Error while encoding packet %d", e, packetId);
		}
	}
}
