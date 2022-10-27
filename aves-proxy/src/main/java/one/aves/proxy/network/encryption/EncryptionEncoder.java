package one.aves.proxy.network.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;

public class EncryptionEncoder extends MessageToByteEncoder<ByteBuf> {

	private final EncryptionTranslator translator;

	public EncryptionEncoder(Cipher cipher) {
		this.translator = new EncryptionTranslator(cipher);
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
	                      ByteBuf byteBuf2) throws Exception {
		this.translator.cipher(byteBuf, byteBuf2);
	}
}
