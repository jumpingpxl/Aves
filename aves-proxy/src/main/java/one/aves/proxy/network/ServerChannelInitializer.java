package one.aves.proxy.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import one.aves.api.network.Direction;
import one.aves.api.network.packet.PacketRegistry;
import one.aves.proxy.DefaultAves;
import one.aves.proxy.network.channel.MessagePrepender;
import one.aves.proxy.network.channel.MessageSplitter;
import one.aves.proxy.network.channel.PacketDecoder;
import one.aves.proxy.network.channel.PacketEncoder;
import one.aves.proxy.network.handler.NetworkHandshakeHandler;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {

	private final DefaultAves aves;
	private final PacketRegistry packetRegistry;

	public ServerChannelInitializer(DefaultAves aves) {
		this.aves = aves;
		this.packetRegistry = aves.packetRegistry();
	}

	@Override
	protected void initChannel(Channel channel) {
		try {
			channel.config().setOption(ChannelOption.TCP_NODELAY, true);
		} catch (ChannelException e) {
			e.printStackTrace();
		}

		MinecraftConnection channelHandler = new MinecraftConnection(this.aves, Direction.SERVERBOUND);
		channelHandler.setNetworkHandler(new NetworkHandshakeHandler(channelHandler));

		channel.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter",
				new MessageSplitter()).addLast("decoder",
				new PacketDecoder(Direction.SERVERBOUND, this.packetRegistry)).addLast("prepender",
				new MessagePrepender()).addLast("encoder",
				new PacketEncoder(Direction.CLIENTBOUND, this.packetRegistry)).addLast("packet_handler",
				channelHandler);
	}
}
