package one.aves.proxy.network.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import one.aves.api.network.ByteBuffer;
import one.aves.proxy.network.DefaultByteBuffer;

import java.util.List;

public class MessageSplitter extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
	                      List<Object> list) {
		byteBuf.markReaderIndex();
		byte[] bytes = new byte[3];
		for (int i = 0; i < bytes.length; i++) {
			if (!byteBuf.isReadable()) {
				byteBuf.resetReaderIndex();
				return;
			}

			bytes[i] = byteBuf.readByte();
			if (bytes[i] < 0) {
				continue;
			}

			ByteBuffer buffer = DefaultByteBuffer.of(Unpooled.wrappedBuffer(bytes));
			try {
				int length = buffer.readVarInt();
				if (byteBuf.readableBytes() >= length) {
					list.add(byteBuf.readBytes(length));
					return;
				}

				byteBuf.resetReaderIndex();
			} finally {
				buffer.release();
			}

			return;
		}

		throw new CorruptedFrameException("length wider than 21-bit");
	}
}
