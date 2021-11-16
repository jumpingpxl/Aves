package one.aves.proxy.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import one.aves.proxy.network.protocol.NettyPacket;

public class PacketEncoder extends MessageToByteEncoder<NettyPacket> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, NettyPacket nettyPacket,
	                      ByteBuf byteBuf) throws Exception {

	}
}
