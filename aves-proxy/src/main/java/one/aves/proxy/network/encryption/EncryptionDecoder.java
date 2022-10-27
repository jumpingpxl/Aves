package one.aves.proxy.network.encryption;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import javax.crypto.Cipher;
import java.util.List;

public class EncryptionDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final EncryptionTranslator translator;

	public EncryptionDecoder(Cipher cipher) {
		this.translator = new EncryptionTranslator(cipher);
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
	                      List<Object> list) throws Exception {
		list.add(this.translator.decipher(channelHandlerContext, byteBuf));
	}
}
