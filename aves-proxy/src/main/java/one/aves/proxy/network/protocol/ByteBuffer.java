package one.aves.proxy.network.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public abstract class ByteBuffer extends ByteBuf {

	public int readVarInt() {
		int i = 0;
		int maxRead = Math.min(5, readableBytes());
		for (int j = 0; j < maxRead; j++) {
			int k = readByte();
			i |= (k & 0x7F) << j * 7;
			if ((k & 0x80) != 128) {
				return i;
			}
		}

		return Integer.MIN_VALUE;
	}

	public String readString() {
		int length = readVarInt();
		String string = toString(readerIndex(), length, StandardCharsets.UTF_8);
		skipBytes(length);
		return string;
	}
}


