package one.aves.proxy.network;

import com.google.common.collect.Queues;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import one.aves.api.component.Component;
import one.aves.api.console.ConsoleLogger;
import one.aves.api.network.Direction;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.connection.ConnectionState;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.DefaultAves;
import one.aves.proxy.network.encryption.EncryptionDecoder;
import one.aves.proxy.network.encryption.EncryptionEncoder;
import one.aves.proxy.network.packet.common.DisconnectPacket;
import one.aves.proxy.network.packet.login.clientbound.LoginDisconnectPacket;
import one.aves.proxy.network.packet.play.clientbound.PlayDisconnectPacket;
import one.aves.proxy.player.DefaultPlayer;
import one.aves.proxy.util.EncryptionHelper;

import javax.crypto.SecretKey;
import java.util.Queue;

public class MinecraftConnection extends SimpleChannelInboundHandler<Packet<?>> {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(MinecraftConnection.class);

	private final Queue<OutgoingPacket> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
	private final DefaultAves aves;
	private final Direction direction;
	private DefaultConnection connection;
	private DefaultPlayer player;

	private Channel channel;
	private NetworkHandler networkHandler;
	private boolean encrypted;

	private boolean closed;

	public MinecraftConnection(DefaultAves aves, Direction direction) {
		this.aves = aves;
		this.direction = direction;
		LOGGER.printInfo("Created MinecraftConnection with direction %s", direction);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
		if (!this.channel.isOpen()) {
			return;
		}

		try {
			packet.handle(this.networkHandler);
		} catch (Exception e) {
			LOGGER.printException("Error while handling packet %s", e,
					packet.getClass().getSimpleName());
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext context) throws Exception {
		super.channelActive(context);
		this.channel = context.channel();
		this.channel.closeFuture().addListener(close -> {
			if (!this.closed) {
				LOGGER.printInfo("Connection closed by user");
			}
		});

		this.setConnectionState(ConnectionState.HANDSHAKE);
	}

	public void sendPacket(Packet<?> packet) {
		this.sendPacket(packet, null);
	}

	public void sendPacket(Packet<?> packet, Runnable completionListener) {
		if (!this.channel.isOpen()) {
			this.outboundPacketsQueue.add(new OutgoingPacket(packet, completionListener));
			return;
		}

		this.flushOutboundQueue();
		this.dispatchPacket(packet, completionListener);
	}

	private void flushOutboundQueue() {
		if (!this.isOpen()) {
			return;
		}

		while (!this.outboundPacketsQueue.isEmpty()) {
			OutgoingPacket outgoingPacket = this.outboundPacketsQueue.poll();
			this.dispatchPacket(outgoingPacket.packet, outgoingPacket.completionListener);
		}
	}

	private void dispatchPacket(Packet<?> packet, Runnable completionListener) {
		ConnectionState currentState = this.channel.attr(ConnectionState.ATTRIBUTE_KEY).get();
		ConnectionState packetState = ConnectionState.fromPacket(packet.getClass());
		if (currentState != packetState) {
			throw new IllegalStateException(
					"Wrong packet state (expected" + currentState + " but got " + packetState + ")");
		}

		Runnable runnable = () -> {
			if (packetState != currentState) {
				MinecraftConnection.this.setConnectionState(packetState);
			}

			ChannelFuture channelFuture = MinecraftConnection.this.channel.writeAndFlush(packet);
			if (completionListener != null) {
				channelFuture.addListener(future -> completionListener.run());
			}

			if (packet instanceof DisconnectPacket<?>) {
				channelFuture.addListener(future -> MinecraftConnection.this.close());
			}

			channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		};

		if (this.channel.eventLoop().inEventLoop()) {
			runnable.run();
		} else {
			this.channel.eventLoop().execute(runnable);
		}
	}

	public void setConnectionState(ConnectionState connectionState) {
		if (this.connection != null) {
			this.connection.updateConnectionState(connectionState);
		}

		this.channel.attr(ConnectionState.ATTRIBUTE_KEY).set(connectionState);
		this.channel.config().setAutoRead(true);
	}

	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		this.registerConnection(protocolVersion);
		this.channel.attr(ProtocolVersion.ATTRIBUTE_KEY).set(protocolVersion);
	}

	private void registerConnection(ProtocolVersion protocolVersion) {
		if (this.connection != null) {
			throw new IllegalStateException("Connection has already been initialized already set");
		}

		this.connection = new DefaultConnection(this, protocolVersion);
		this.aves.getConnectionHandler().registerConnection(this.connection);
	}

	public DefaultConnection connection() {
		return this.connection;
	}

	public void setNetworkHandler(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	public boolean isOpen() {
		return !this.closed && this.channel != null && this.channel.isOpen();
	}

	public void disconnect(Component reason) {
		if (!this.isOpen()) {
			throw new IllegalStateException("Connection is not open");
		}

		ConnectionState connectionState = this.connection.connectionState();
		switch (connectionState) {
			case LOGIN -> this.sendPacket(new LoginDisconnectPacket(reason));
			case PLAY -> this.sendPacket(new PlayDisconnectPacket(reason));
			case HANDSHAKE, STATUS -> throw new IllegalStateException(
					"Cannot disconnect in state " + connectionState);
			default -> throw new IllegalStateException("Invalid connection state");
		}
	}

	public DefaultPlayer createPlayer() {
		if (this.player != null) {
			throw new IllegalStateException("Player has already been created");
		}

		this.player = new DefaultPlayer(this.connection);
		return this.player;
	}

	public DefaultPlayer getPlayer() {
		return this.player;
	}

	public void close() {
		if (!this.isOpen()) {
			return;
		}

		LOGGER.printWarning("Closed connection");
		this.closed = true;
		this.channel.close().awaitUninterruptibly();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	public DefaultAves aves() {
		return this.aves;
	}

	public void enableEncryption(SecretKey secretKey) {
		this.encrypted = true;
		this.channel.pipeline().addBefore("splitter", "decrypt",
				new EncryptionDecoder(EncryptionHelper.createNetCipherInstance(2, secretKey)));
		this.channel.pipeline().addBefore("prepender", "encrypt",
				new EncryptionEncoder(EncryptionHelper.createNetCipherInstance(1, secretKey)));
	}

	static class OutgoingPacket {

		private final Packet<?> packet;
		private final Runnable completionListener;

		public OutgoingPacket(Packet<?> packet, Runnable completionListener) {
			this.packet = packet;
			this.completionListener = completionListener;
		}
	}
}
