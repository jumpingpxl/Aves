package one.aves.proxy.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel channel) {
		channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter",
						new LengthFieldPrepender(4))
				//				.addLast("prepender", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
				//				0,4, 0, 4))
				.addLast("encoder", new PacketEncoder()).addLast("decoder", new PacketDecoder()).addLast(
						"handler", new MinecraftConnection());
	}
}
