package one.aves.proxy.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import one.aves.proxy.network.channel.MessagePrepender;
import one.aves.proxy.network.channel.MessageSplitter;
import one.aves.proxy.network.channel.PacketDecoder;
import one.aves.proxy.network.channel.PacketEncoder;
import one.aves.proxy.network.handler.NetworkHandshakeHandler;
import one.aves.proxy.network.protocol.Direction;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel channel) {
		try {
			channel.config().setOption(ChannelOption.TCP_NODELAY, true);
		} catch (ChannelException e) {
			e.printStackTrace();
		}

		MinecraftConnection channelHandler = new MinecraftConnection(Direction.SERVERBOUND);
		channelHandler.setNetworkHandler(new NetworkHandshakeHandler(channelHandler));
		channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter",
				new MessageSplitter()).addLast("decoder", new PacketDecoder(Direction.SERVERBOUND)).addLast(
				"prepender", new MessagePrepender()).addLast("encoder",
				new PacketEncoder(Direction.CLIENTBOUND)).addLast("packet_handler", channelHandler);
	}
}
