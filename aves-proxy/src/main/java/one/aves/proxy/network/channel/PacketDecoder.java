package one.aves.proxy.network.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.connection.ConnectionState;
import one.aves.api.network.packet.Packet;
import one.aves.api.network.packet.PacketRegistry;
import one.aves.proxy.network.DefaultByteBuffer;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(PacketDecoder.class);
	private final Direction direction;
	private final PacketRegistry packetRegistry;

	public PacketDecoder(Direction direction, PacketRegistry packetRegistry) {
		this.direction = direction;
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) {
		if (byteBuf.readableBytes() == 0) {
			return;
		}

		ByteBuffer byteBuffer = DefaultByteBuffer.of(byteBuf);
		int packetId = byteBuffer.readVarInt();
		ConnectionState state = ConnectionState.fromContext(context);
		Packet<?> packet = this.packetRegistry.getPacket(state, this.direction, packetId);
		if (packet == null) {
			LOGGER.printSevere("Bad packet id %d", packetId);
			return;
		}

		packet.decode(byteBuffer, this.direction, ProtocolVersion.fromContext(context));
		if (byteBuffer.readableBytes() > 0) {
			byteBuf.resetReaderIndex();
			LOGGER.printSevere("Packet %s (%d) was larger than expected, found %d bytes extra",
					packet.getClass().getSimpleName(), packetId, byteBuffer.readableBytes());
			return;
		}

		list.add(packet);
	}
}
