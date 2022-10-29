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
		int maxRead = Math.min(5, this.byteBuf.readableBytes());
		for (int j = 0; j < maxRead; j++) {
			int k = this.byteBuf.readByte();
			i |= (k & 0x7F) << j * 7;
			if ((k & 0x80) != 128) {
				return i;
			}
		}

		return Integer.MIN_VALUE;
	}

	public String readString() {
		int length = this.readVarInt();
		String string = this.byteBuf.toString(this.byteBuf.readerIndex(), length,
				StandardCharsets.UTF_8);
		this.byteBuf.skipBytes(length);
		return string;
	}

	public byte[] readByteArray() {
		byte[] bytes = new byte[this.readVarInt()];
		this.readBytes(bytes);
		return bytes;
	}

	public void readBytes(byte[] bytes) {
		this.byteBuf.readBytes(bytes);
	}

	public void writeShort(int value) {
		this.byteBuf.writeShort(value);
	}

	public void writeByteArray(byte[] array) {
		this.writeVarInt(array.length);
		this.writeBytes(array);
	}

	public void writeBytes(byte... bytes) {
		this.byteBuf.writeBytes(bytes);
	}

	public void writeVarInt(int input) {
		while ((input & -128) != 0) {
			this.writeByte(input & 127 | 128);
			input >>>= 7;
		}

		this.writeByte(input);
	}

	public void writeString(String input) {
		byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
		this.writeVarInt(bytes.length);
		for (byte b : bytes) {
			this.writeByte(b);
		}
	}

	public void writeByte(int i) {
		this.byteBuf.writeByte(i);
	}

	public int readUnsignedShort() {
		return this.byteBuf.readUnsignedShort();
	}

	public byte readByte() {
		return this.byteBuf.readByte();
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


