package one.aves.proxy.network;

import io.netty.buffer.ByteBuf;
import one.aves.api.network.ByteBuffer;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DefaultByteBuffer implements ByteBuffer {

	private final ByteBuf byteBuf;

	private DefaultByteBuffer(ByteBuf byteBuf) {
		this.byteBuf = byteBuf;
	}

	public static ByteBuffer of(ByteBuf byteBuf) {
		return new DefaultByteBuffer(byteBuf);
	}

	@Override
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

	@Override
	public String readString() {
		int length = this.readVarInt();
		String string = this.byteBuf.toString(this.byteBuf.readerIndex(), length,
				StandardCharsets.UTF_8);
		this.byteBuf.skipBytes(length);
		return string;
	}

	@Override
	public byte[] readByteArray() {
		byte[] bytes = new byte[this.readVarInt()];
		this.readBytes(bytes);
		return bytes;
	}

	@Override
	public void writeVarInt(int value) {
		while ((value & -128) != 0) {
			this.writeByte(value & 127 | 128);
			value >>>= 7;
		}

		this.writeByte(value);
	}

	@Override
	public void writeString(String string) {
		byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
		this.writeByteArray(bytes);
	}

	@Override
	public void writeByteArray(byte... bytes) {
		this.writeVarInt(bytes.length);
		this.writeBytes(bytes);
	}

	@Override
	public void writeBytes(byte... bytes) {
		this.byteBuf.writeBytes(bytes);
	}

	@Override
	public int capacity() {
		return this.byteBuf.capacity();
	}

	@Override
	public void capacity(int var1) {
		this.byteBuf.capacity(var1);
	}

	@Override
	public int maxCapacity() {
		return this.byteBuf.maxCapacity();
	}

	@Override
	public void unwrap() {
		this.byteBuf.unwrap();
	}

	@Override
	public boolean isDirect() {
		return this.byteBuf.isDirect();
	}

	@Override
	public boolean isReadOnly() {
		return this.byteBuf.isReadOnly();
	}

	@Override
	public void asReadOnly() {
		this.byteBuf.asReadOnly();
	}

	@Override
	public int readerIndex() {
		return this.byteBuf.readerIndex();
	}

	@Override
	public void readerIndex(int var1) {
		this.byteBuf.readerIndex(var1);
	}

	@Override
	public int writerIndex() {
		return this.byteBuf.writerIndex();
	}

	@Override
	public void writerIndex(int var1) {
		this.byteBuf.writerIndex(var1);
	}

	@Override
	public void setIndex(int var1, int var2) {
		this.byteBuf.setIndex(var1, var2);
	}

	@Override
	public int readableBytes() {
		return this.byteBuf.readableBytes();
	}

	@Override
	public int writableBytes() {
		return this.byteBuf.writableBytes();
	}

	@Override
	public int maxWritableBytes() {
		return this.byteBuf.maxWritableBytes();
	}

	@Override
	public int maxFastWritableBytes() {
		return this.byteBuf.maxFastWritableBytes();
	}

	@Override
	public boolean isReadable() {
		return this.byteBuf.isReadable();
	}

	@Override
	public boolean isReadable(int var1) {
		return this.byteBuf.isReadable(var1);
	}

	@Override
	public boolean isWritable() {
		return this.byteBuf.isWritable();
	}

	@Override
	public boolean isWritable(int var1) {
		return this.byteBuf.isWritable(var1);
	}

	@Override
	public void clear() {
		this.byteBuf.clear();
	}

	@Override
	public void markReaderIndex() {
		this.byteBuf.markReaderIndex();
	}

	@Override
	public void resetReaderIndex() {
		this.byteBuf.resetReaderIndex();
	}

	@Override
	public void markWriterIndex() {
		this.byteBuf.markWriterIndex();
	}

	@Override
	public void resetWriterIndex() {
		this.byteBuf.resetWriterIndex();
	}

	@Override
	public void discardReadBytes() {
		this.byteBuf.discardReadBytes();
	}

	@Override
	public void discardSomeReadBytes() {
		this.byteBuf.discardSomeReadBytes();
	}

	@Override
	public void ensureWritable(int var1) {
		this.byteBuf.ensureWritable(var1);
	}

	@Override
	public int ensureWritable(int var1, boolean var2) {
		return this.byteBuf.ensureWritable(var1, var2);
	}

	@Override
	public boolean getBoolean(int var1) {
		return this.byteBuf.getBoolean(var1);
	}

	@Override
	public byte getByte(int var1) {
		return this.byteBuf.getByte(var1);
	}

	@Override
	public short getUnsignedByte(int var1) {
		return this.byteBuf.getUnsignedByte(var1);
	}

	@Override
	public short getShort(int var1) {
		return this.byteBuf.getShort(var1);
	}

	@Override
	public short getShortLE(int var1) {
		return this.byteBuf.getShortLE(var1);
	}

	@Override
	public int getUnsignedShort(int var1) {
		return this.byteBuf.getUnsignedShort(var1);
	}

	@Override
	public int getUnsignedShortLE(int var1) {
		return this.byteBuf.getUnsignedShortLE(var1);
	}

	@Override
	public int getMedium(int var1) {
		return this.byteBuf.getMedium(var1);
	}

	@Override
	public int getMediumLE(int var1) {
		return this.byteBuf.getMediumLE(var1);
	}

	@Override
	public int getUnsignedMedium(int var1) {
		return this.byteBuf.getUnsignedMedium(var1);
	}

	@Override
	public int getUnsignedMediumLE(int var1) {
		return this.byteBuf.getUnsignedMediumLE(var1);
	}

	@Override
	public int getInt(int var1) {
		return this.byteBuf.getInt(var1);
	}

	@Override
	public int getIntLE(int var1) {
		return this.byteBuf.getIntLE(var1);
	}

	@Override
	public long getUnsignedInt(int var1) {
		return this.byteBuf.getUnsignedInt(var1);
	}

	@Override
	public long getUnsignedIntLE(int var1) {
		return this.byteBuf.getUnsignedIntLE(var1);
	}

	@Override
	public long getLong(int var1) {
		return this.byteBuf.getLong(var1);
	}

	@Override
	public long getLongLE(int var1) {
		return this.byteBuf.getLongLE(var1);
	}

	@Override
	public char getChar(int var1) {
		return this.byteBuf.getChar(var1);
	}

	@Override
	public float getFloat(int var1) {
		return this.byteBuf.getFloat(var1);
	}

	@Override
	public float getFloatLE(int index) {
		return this.byteBuf.getFloatLE(index);
	}

	@Override
	public double getDouble(int var1) {
		return this.byteBuf.getDouble(var1);
	}

	@Override
	public double getDoubleLE(int index) {
		return this.byteBuf.getDoubleLE(index);
	}

	@Override
	public void getBytes(int var1, byte[] var2) {
		this.byteBuf.getBytes(var1, var2);
	}

	@Override
	public void getBytes(int var1, byte[] var2, int var3, int var4) {
		this.byteBuf.getBytes(var1, var2, var3, var4);
	}

	@Override
	public CharSequence getCharSequence(int var1, int var2, Charset var3) {
		return this.byteBuf.getCharSequence(var1, var2, var3);
	}

	@Override
	public void setBoolean(int var1, boolean var2) {
		this.byteBuf.setBoolean(var1, var2);
	}

	@Override
	public void setByte(int var1, int var2) {
		this.byteBuf.setByte(var1, var2);
	}

	@Override
	public void setShort(int var1, int var2) {
		this.byteBuf.setShort(var1, var2);
	}

	@Override
	public void setShortLE(int var1, int var2) {
		this.byteBuf.setShortLE(var1, var2);
	}

	@Override
	public void setMedium(int var1, int var2) {
		this.byteBuf.setMedium(var1, var2);
	}

	@Override
	public void setMediumLE(int var1, int var2) {
		this.byteBuf.setMediumLE(var1, var2);
	}

	@Override
	public void setInt(int var1, int var2) {
		this.byteBuf.setInt(var1, var2);
	}

	@Override
	public void setIntLE(int var1, int var2) {
		this.byteBuf.setIntLE(var1, var2);
	}

	@Override
	public void setLong(int var1, long var2) {
		this.byteBuf.setLong(var1, var2);
	}

	@Override
	public void setLongLE(int var1, long var2) {
		this.byteBuf.setLongLE(var1, var2);
	}

	@Override
	public void setChar(int var1, int var2) {
		this.byteBuf.setChar(var1, var2);
	}

	@Override
	public void setFloat(int var1, float var2) {
		this.byteBuf.setFloat(var1, var2);
	}

	@Override
	public void setFloatLE(int index, float value) {
		this.byteBuf.setFloatLE(index, value);
	}

	@Override
	public void setDouble(int var1, double var2) {
		this.byteBuf.setDouble(var1, var2);
	}

	@Override
	public void setDoubleLE(int index, double value) {
		this.byteBuf.setDoubleLE(index, value);
	}

	@Override
	public void setBytes(int var1, byte[] var2) {
		this.byteBuf.setBytes(var1, var2);
	}

	@Override
	public void setBytes(int var1, byte[] var2, int var3, int var4) {
		this.byteBuf.setBytes(var1, var2, var3, var4);
	}

	@Override
	public void setZero(int var1, int var2) {
		this.byteBuf.setZero(var1, var2);
	}

	@Override
	public int setCharSequence(int var1, CharSequence var2, Charset var3) {
		return this.byteBuf.setCharSequence(var1, var2, var3);
	}

	@Override
	public boolean readBoolean() {
		return this.byteBuf.readBoolean();
	}

	@Override
	public byte readByte() {
		return this.byteBuf.readByte();
	}

	@Override
	public short readUnsignedByte() {
		return this.byteBuf.readUnsignedByte();
	}

	@Override
	public short readShort() {
		return this.byteBuf.readShort();
	}

	@Override
	public short readShortLE() {
		return this.byteBuf.readShortLE();
	}

	@Override
	public int readUnsignedShort() {
		return this.byteBuf.readUnsignedShort();
	}

	@Override
	public int readUnsignedShortLE() {
		return this.byteBuf.readUnsignedShortLE();
	}

	@Override
	public int readMedium() {
		return this.byteBuf.readMedium();
	}

	@Override
	public int readMediumLE() {
		return this.byteBuf.readMediumLE();
	}

	@Override
	public int readUnsignedMedium() {
		return this.byteBuf.readUnsignedMedium();
	}

	@Override
	public int readUnsignedMediumLE() {
		return this.byteBuf.readUnsignedMediumLE();
	}

	@Override
	public int readInt() {
		return this.byteBuf.readInt();
	}

	@Override
	public int readIntLE() {
		return this.byteBuf.readIntLE();
	}

	@Override
	public long readUnsignedInt() {
		return this.byteBuf.readUnsignedInt();
	}

	@Override
	public long readUnsignedIntLE() {
		return this.byteBuf.readUnsignedIntLE();
	}

	@Override
	public long readLong() {
		return this.byteBuf.readLong();
	}

	@Override
	public long readLongLE() {
		return this.byteBuf.readLongLE();
	}

	@Override
	public char readChar() {
		return this.byteBuf.readChar();
	}

	@Override
	public float readFloat() {
		return this.byteBuf.readFloat();
	}

	@Override
	public float readFloatLE() {
		return this.byteBuf.readFloatLE();
	}

	@Override
	public double readDouble() {
		return this.byteBuf.readDouble();
	}

	@Override
	public double readDoubleLE() {
		return this.byteBuf.readDoubleLE();
	}

	@Override
	public void readBytes(int var1) {
		this.byteBuf.readBytes(var1);
	}

	@Override
	public void readSlice(int var1) {
		this.byteBuf.readSlice(var1);
	}

	@Override
	public void readRetainedSlice(int var1) {
		this.byteBuf.readRetainedSlice(var1);
	}

	@Override
	public void readBytes(byte[] var1) {
		this.byteBuf.readBytes(var1);
	}

	@Override
	public void readBytes(byte[] var1, int var2, int var3) {
		this.byteBuf.readBytes(var1, var2, var3);
	}

	@Override
	public CharSequence readCharSequence(int var1, Charset var2) {
		return this.byteBuf.readCharSequence(var1, var2);
	}

	@Override
	public int readBytes(FileChannel var1, long var2, int var4) throws IOException {
		return this.byteBuf.readBytes(var1, var2, var4);
	}

	@Override
	public void skipBytes(int var1) {
		this.byteBuf.skipBytes(var1);
	}

	@Override
	public void writeBoolean(boolean var1) {
		this.byteBuf.writeBoolean(var1);
	}

	@Override
	public void writeByte(int var1) {
		this.byteBuf.writeByte(var1);
	}

	@Override
	public void writeShort(int var1) {
		this.byteBuf.writeShort(var1);
	}

	@Override
	public void writeShortLE(int var1) {
		this.byteBuf.writeShortLE(var1);
	}

	@Override
	public void writeMedium(int var1) {
		this.byteBuf.writeMedium(var1);
	}

	@Override
	public void writeMediumLE(int var1) {
		this.byteBuf.writeMediumLE(var1);
	}

	@Override
	public void writeInt(int var1) {
		this.byteBuf.writeInt(var1);
	}

	@Override
	public void writeIntLE(int var1) {
		this.byteBuf.writeIntLE(var1);
	}

	@Override
	public void writeLong(long var1) {
		this.byteBuf.writeLong(var1);
	}

	@Override
	public void writeLongLE(long var1) {
		this.byteBuf.writeLongLE(var1);
	}

	@Override
	public void writeChar(int var1) {
		this.byteBuf.writeChar(var1);
	}

	@Override
	public void writeFloat(float var1) {
		this.byteBuf.writeFloat(var1);
	}

	@Override
	public void writeFloatLE(float value) {
		this.byteBuf.writeFloatLE(value);
	}

	@Override
	public void writeDouble(double var1) {
		this.byteBuf.writeDouble(var1);
	}

	@Override
	public void writeDoubleLE(double value) {
		this.byteBuf.writeDoubleLE(value);
	}

	@Override
	public void writeBytes(byte[] var1, int var2, int var3) {
		this.byteBuf.writeBytes(var1, var2, var3);
	}

	@Override
	public void writeZero(int var1) {
		this.byteBuf.writeZero(var1);
	}

	@Override
	public int writeCharSequence(CharSequence var1, Charset var2) {
		return this.byteBuf.writeCharSequence(var1, var2);
	}

	@Override
	public int indexOf(int var1, int var2, byte var3) {
		return this.byteBuf.indexOf(var1, var2, var3);
	}

	@Override
	public int bytesBefore(byte var1) {
		return this.byteBuf.bytesBefore(var1);
	}

	@Override
	public int bytesBefore(int var1, byte var2) {
		return this.byteBuf.bytesBefore(var1, var2);
	}

	@Override
	public int bytesBefore(int var1, int var2, byte var3) {
		return this.byteBuf.bytesBefore(var1, var2, var3);
	}

	@Override
	public boolean hasArray() {
		return this.byteBuf.hasArray();
	}

	@Override
	public byte[] array() {
		return this.byteBuf.array();
	}

	@Override
	public int arrayOffset() {
		return this.byteBuf.arrayOffset();
	}

	@Override
	public boolean hasMemoryAddress() {
		return this.byteBuf.hasMemoryAddress();
	}

	@Override
	public long memoryAddress() {
		return this.byteBuf.memoryAddress();
	}

	@Override
	public boolean isContiguous() {
		return this.byteBuf.isContiguous();
	}

	@Override
	public String toString(Charset var1) {
		return this.byteBuf.toString(var1);
	}

	@Override
	public String toString(int var1, int var2, Charset var3) {
		return this.byteBuf.toString(var1, var2, var3);
	}

	@Override
	public int compareTo(ByteBuffer var1) {
		return this.byteBuf.compareTo(var1.getByteBuf());
	}

	@Override
	public int refCnt() {
		return this.byteBuf.refCnt();
	}

	@Override
	public boolean release() {
		return this.byteBuf.release();
	}

	@Override
	public boolean release(int var1) {
		return this.byteBuf.release(var1);
	}

	public ByteBuf getByteBuf() {
		return this.byteBuf;
	}
}
