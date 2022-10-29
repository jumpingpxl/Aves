package one.aves.api.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public interface ByteBuffer {

	static int getVarIntSize(int input) {
		for (int i = 1; i < 5; ++i) {
			if ((input & -1 << i * 7) == 0) {
				return i;
			}
		}

		return 5;
	}

	int readVarInt();

	String readString();

	byte[] readByteArray();

	void writeVarInt(int value);

	void writeString(String string);

	void writeByteArray(byte... bytes);

	void writeBytes(byte... bytes);

	int capacity();

	void capacity(int var1);

	int maxCapacity();

	void unwrap();

	boolean isDirect();

	boolean isReadOnly();

	void asReadOnly();

	int readerIndex();

	void readerIndex(int var1);

	int writerIndex();

	void writerIndex(int var1);

	void setIndex(int var1, int var2);

	int readableBytes();

	int writableBytes();

	int maxWritableBytes();

	int maxFastWritableBytes();

	boolean isReadable();

	boolean isReadable(int var1);

	boolean isWritable();

	boolean isWritable(int var1);

	void clear();

	void markReaderIndex();

	void resetReaderIndex();

	void markWriterIndex();

	void resetWriterIndex();

	void discardReadBytes();

	void discardSomeReadBytes();

	void ensureWritable(int var1);

	int ensureWritable(int var1, boolean var2);

	boolean getBoolean(int var1);

	byte getByte(int var1);

	short getUnsignedByte(int var1);

	short getShort(int var1);

	short getShortLE(int var1);

	int getUnsignedShort(int var1);

	int getUnsignedShortLE(int var1);

	int getMedium(int var1);

	int getMediumLE(int var1);

	int getUnsignedMedium(int var1);

	int getUnsignedMediumLE(int var1);

	int getInt(int var1);

	int getIntLE(int var1);

	long getUnsignedInt(int var1);

	long getUnsignedIntLE(int var1);

	long getLong(int var1);

	long getLongLE(int var1);

	char getChar(int var1);

	float getFloat(int var1);

	float getFloatLE(int index);

	double getDouble(int var1);

	double getDoubleLE(int index);

	void getBytes(int var1, byte[] var2);

	void getBytes(int var1, byte[] var2, int var3, int var4);

	CharSequence getCharSequence(int var1, int var2, Charset var3);

	void setBoolean(int var1, boolean var2);

	void setByte(int var1, int var2);

	void setShort(int var1, int var2);

	void setShortLE(int var1, int var2);

	void setMedium(int var1, int var2);

	void setMediumLE(int var1, int var2);

	void setInt(int var1, int var2);

	void setIntLE(int var1, int var2);

	void setLong(int var1, long var2);

	void setLongLE(int var1, long var2);

	void setChar(int var1, int var2);

	void setFloat(int var1, float var2);

	void setFloatLE(int index, float value);

	void setDouble(int var1, double var2);

	void setDoubleLE(int index, double value);

	void setBytes(int var1, byte[] var2);

	void setBytes(int var1, byte[] var2, int var3, int var4);

	void setZero(int var1, int var2);

	int setCharSequence(int var1, CharSequence var2, Charset var3);

	boolean readBoolean();

	byte readByte();

	short readUnsignedByte();

	short readShort();

	short readShortLE();

	int readUnsignedShort();

	int readUnsignedShortLE();

	int readMedium();

	int readMediumLE();

	int readUnsignedMedium();

	int readUnsignedMediumLE();

	int readInt();

	int readIntLE();

	long readUnsignedInt();

	long readUnsignedIntLE();

	long readLong();

	long readLongLE();

	char readChar();

	float readFloat();

	float readFloatLE();

	double readDouble();

	double readDoubleLE();

	void readBytes(int var1);

	void readSlice(int var1);

	void readRetainedSlice(int var1);

	void readBytes(byte[] var1);

	void readBytes(byte[] var1, int var2, int var3);

	CharSequence readCharSequence(int var1, Charset var2);

	int readBytes(FileChannel var1, long var2, int var4) throws IOException;

	void skipBytes(int var1);

	void writeBoolean(boolean var1);

	void writeByte(int var1);

	void writeShort(int var1);

	void writeShortLE(int var1);

	void writeMedium(int var1);

	void writeMediumLE(int var1);

	void writeInt(int var1);

	void writeIntLE(int var1);

	void writeLong(long var1);

	void writeLongLE(long var1);

	void writeChar(int var1);

	void writeFloat(float var1);

	void writeFloatLE(float value);

	void writeDouble(double var1);

	void writeDoubleLE(double value);

	void writeBytes(byte[] var1, int var2, int var3);

	void writeZero(int var1);

	int writeCharSequence(CharSequence var1, Charset var2);

	int indexOf(int var1, int var2, byte var3);

	int bytesBefore(byte var1);

	int bytesBefore(int var1, byte var2);

	int bytesBefore(int var1, int var2, byte var3);

	boolean hasArray();

	byte[] array();

	int arrayOffset();

	boolean hasMemoryAddress();

	long memoryAddress();

	boolean isContiguous();

	String toString(Charset var1);

	String toString(int var1, int var2, Charset var3);

	int hashCode();

	boolean equals(Object var1);

	int compareTo(ByteBuffer var1);

	String toString();

	int refCnt();

	boolean release();

	boolean release(int var1);

	ByteBuf getByteBuf();
}
