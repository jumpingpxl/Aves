package one.aves.proxy.network.channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import one.aves.proxy.network.protocol.ByteBuffer;

public class MessagePrepender extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf out)
			throws Exception {
		int readableBytes = byteBuf.readableBytes();
		int size = ByteBuffer.getVarIntSize(readableBytes);
		if (size > 3) {
			throw new IllegalArgumentException("unable to fit " + readableBytes + " into " + 3);
		}

		ByteBuffer byteBuffer = ByteBuffer.of(out);
		byteBuffer.ensureWritable(size + readableBytes);
		byteBuffer.writeVarInt(readableBytes);
		out.writeBytes(byteBuf);
	}
}
