package one.aves.proxy.network.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class EncryptionTranslator {

	private final Cipher cipher;
	private byte[] bytes1 = new byte[0];
	private byte[] bytes2 = new byte[0];

	protected EncryptionTranslator(Cipher cipher) {
		this.cipher = cipher;
	}

	protected ByteBuf decipher(ChannelHandlerContext context, ByteBuf byteBuf)
			throws ShortBufferException {
		int readableBytes = byteBuf.readableBytes();
		byte[] bytes = this.readEncryption(readableBytes, byteBuf);

		ByteBuf buffer = context.alloc().heapBuffer(this.cipher.getOutputSize(readableBytes));
		buffer.writerIndex(
				this.cipher.update(bytes, 0, readableBytes, buffer.array(), buffer.arrayOffset()));
		return buffer;
	}

	protected void cipher(ByteBuf byteBufIn, ByteBuf byteBufOut) throws ShortBufferException {
		int readableBytes = byteBufIn.readableBytes();
		byte[] bytes = this.readEncryption(readableBytes, byteBufIn);
		int outputSize = this.cipher.getOutputSize(readableBytes);
		if (this.bytes2.length < outputSize) {
			this.bytes2 = new byte[outputSize];
		}

		byteBufOut.writeBytes(this.bytes2, 0, this.cipher.update(bytes, 0, readableBytes,
				this.bytes2));
	}

	private byte[] readEncryption(int readableBytes, ByteBuf byteBuf) {
		if (this.bytes1.length < readableBytes) {
			this.bytes1 = new byte[readableBytes];
		}

		byteBuf.readBytes(this.bytes1, 0, readableBytes);
		return this.bytes1;
	}
}
