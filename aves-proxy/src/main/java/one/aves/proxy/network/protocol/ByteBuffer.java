package one.aves.proxy.network.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ByteBuffer {

	private final ByteBuf byteBuf;

	private ByteBuffer(ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
	}

	public static ByteBuffer of(ByteBuf byteBuf) {
		return new ByteBuffer(byteBuf);
	}

	public static int getVarIntSize(int input) {
		for (int i = 1; i < 5; ++i) {
			if ((input & -1 << i * 7) == 0) {
				return i;
			}
		}

		return 5;
	}

	public int readVarInt() {
		int i = 0;
		int maxRead = Math.min(5, byteBuf.readableBytes());
		for (int j = 0; j < maxRead; j++) {
			int k = byteBuf.readByte();
			i |= (k & 0x7F) << j * 7;
			if ((k & 0x80) != 128) {
				return i;
			}
		}

		return Integer.MIN_VALUE;
	}

	public String readString() {
		int length = readVarInt();
		String string = byteBuf.toString(byteBuf.readerIndex(), length, StandardCharsets.UTF_8);
		byteBuf.skipBytes(length);
		return string;
	}

	public byte[] readByteArray() {
		byte[] bytes = new byte[this.readVarInt()];
		this.readBytes(bytes);
		return bytes;
	}

	public void readBytes(byte[] bytes) {
		byteBuf.readBytes(bytes);
	}

	public void writeByteArray(byte[] array) {
		this.writeVarInt(array.length);
		this.writeBytes(array);
	}

	public void writeBytes(byte... bytes) {
		byteBuf.writeBytes(bytes);
	}

	public void writeVarInt(int input) {
		while ((input & -128) != 0) {
			writeByte(input & 127 | 128);
			input >>>= 7;
		}

		writeByte(input);
	}

	public void writeString(String input) {
		byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
		writeVarInt(bytes.length);
		for (byte b : bytes) {
			writeByte(b);
		}
	}

	private void writeByte(int i) {
		byteBuf.writeByte(i);
	}

	public int readUnsignedShort() {
		return byteBuf.readUnsignedShort();
	}

	public byte readByte() {
		return byteBuf.readByte();
	}

	public boolean release() {
		return this.byteBuf.release();
	}

	public long readLong() {
		return this.byteBuf.readLong();
	}

	public void writeLong(long value) {
		this.byteBuf.writeLong(value);
	}

	public int readableBytes() {
		return this.byteBuf.readableBytes();
	}

	public void ensureWritable(int value) {
		this.byteBuf.ensureWritable(value);
	}
}


