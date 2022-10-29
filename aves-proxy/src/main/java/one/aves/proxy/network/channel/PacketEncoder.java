package one.aves.proxy.network.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.connection.ConnectionState;
import one.aves.api.network.packet.Packet;
import one.aves.api.network.packet.PacketRegistry;
import one.aves.proxy.network.DefaultByteBuffer;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(PacketEncoder.class);
	private final Direction direction;
	private final PacketRegistry packetRegistry;

	public PacketEncoder(Direction direction, PacketRegistry packetRegistry) {
		this.direction = direction;
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void encode(ChannelHandlerContext context, Packet packet, ByteBuf byteBuf) {
		int packetId;
		try {
			ConnectionState state = ConnectionState.fromContext(context);
			packetId = this.packetRegistry.getPacketId(state, this.direction, packet);
		} catch (Exception e) {
			LOGGER.printSevere("Could not get packet id from packet %s",
					packet.getClass().getSimpleName());
			throw e;
		}

		ByteBuffer byteBuffer = DefaultByteBuffer.of(byteBuf);
		byteBuffer.writeVarInt(packetId);
		try {
			packet.encode(byteBuffer, this.direction, ProtocolVersion.fromContext(context));
		} catch (Exception e) {
			LOGGER.printException("Error while encoding packet %d", e, packetId);
		}
	}
}
