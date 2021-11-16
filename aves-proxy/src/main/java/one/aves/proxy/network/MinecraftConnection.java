package one.aves.proxy.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.network.protocol.connectionstate.ConnectionState;

public class MinecraftConnection extends ChannelInboundHandlerAdapter {

	private static final ConsoleLogger LOGGER = ConsoleLogger.getLogger(MinecraftConnection.class);

	private ConnectionState connectionState;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//LOGGER.info("MC " + msg.toString());
		super.channelRead(ctx, msg);
	}
}
