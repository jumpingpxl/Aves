package one.aves.proxy.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.EpollChannelOption;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.network.connection.Connection;
import one.aves.proxy.DefaultAves;

import java.net.InetSocketAddress;

public class ConnectionHandler {

	private static final WriteBufferWaterMark SERVER_WRITE_MARK = new WriteBufferWaterMark(1 << 20,
			1 << 21);
	private static final ConsoleLogger LOGGER = ConsoleLogger.of(ConnectionHandler.class);

	private final ServerChannelInitializer serverChannelInitializer;
	private final TransportType transportType;
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;

	public ConnectionHandler(DefaultAves aves) {
		this.serverChannelInitializer = new ServerChannelInitializer(aves);
		this.transportType = TransportType.bestType();
		this.bossGroup = this.transportType.createEventLoopGroup(TransportType.Type.BOSS);
		this.workerGroup = this.transportType.createEventLoopGroup(TransportType.Type.WORKER);
	}

	public void bind(InetSocketAddress address) {
		ServerBootstrap bootstrap = new ServerBootstrap().channelFactory(
						this.transportType.serverSocketChannelFactory)
				.group(this.bossGroup, this.workerGroup)
				.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, SERVER_WRITE_MARK)
				.childHandler(this.serverChannelInitializer)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.IP_TOS, 0x18)
				.localAddress(address);

		if (this.transportType
				== TransportType.EPOLL) {// && server.getConfiguration().useTcpFastOpen()) {
			bootstrap.option(EpollChannelOption.TCP_FASTOPEN, 3);
		}

		bootstrap.bind().addListener((ChannelFutureListener) future -> {
			Channel channel = future.channel();
			if (future.isSuccess()) {
				LOGGER.printInfo("Listening on %s.", channel.localAddress());
			} else {
				LOGGER.printSevere("Can't bind to %s! (Cause: %s)", address, future.cause());
			}
		});
	}

	public void registerConnection(Connection connection) {

	}

	public void unregisterConnection(Connection connection) {

	}
}
