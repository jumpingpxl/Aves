package one.aves.proxy.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class PacketDecoder extends ChannelInboundHandlerAdapter {

	public PacketDecoder() {
		System.out.println("init pc");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			tryDecode(ctx, buf);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	private void tryDecode(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
		if (!ctx.channel().isActive() || !byteBuf.isReadable()) {
			byteBuf.release();
			return;
		}

		int packetLength = readVarInt(byteBuf);
		int packetId = readVarInt(byteBuf);
		System.out.println("ic -> " + packetId);
		if (packetId == 0x00) {
			int protocol = readVarInt(byteBuf);
			String host = readString(byteBuf);
			int port = byteBuf.readUnsignedShort();
			byte nextPacket = byteBuf.readByte();
			System.out.println("HANDSHAKE -> " + protocol + ";" + host + ";" + port + ";" + nextPacket);
		}
	}

	public int readVarInt(ByteBuf buf) {
		int read = readVarIntSafely(buf);
		if (read == Integer.MIN_VALUE) {
			System.out.println("nooooo " + read);
		}
		return read;
	}

	private String readString(ByteBuf buf) {
		int length = readVarInt(buf);
		String string = buf.toString(buf.readerIndex(), length, StandardCharsets.UTF_8);
		buf.skipBytes(length);
		return string;
	}

	public int readVarIntSafely(ByteBuf buf) {
		int i = 0;
		int maxRead = Math.min(5, buf.readableBytes());
		for (int j = 0; j < maxRead; j++) {
			int k = buf.readByte();
			i |= (k & 0x7F) << j * 7;
			if ((k & 0x80) != 128) {
				return i;
			}
		}

		return Integer.MIN_VALUE;
	}
}
