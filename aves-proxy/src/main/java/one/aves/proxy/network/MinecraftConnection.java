package one.aves.proxy.network;

import com.google.common.collect.Queues;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import one.aves.api.connection.ProtocolVersion;
import one.aves.api.console.ConsoleLogger;
import one.aves.proxy.Aves;
import one.aves.proxy.network.encryption.EncryptionDecoder;
import one.aves.proxy.network.encryption.EncryptionEncoder;
import one.aves.proxy.network.handler.NetworkHandler;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;
import one.aves.proxy.network.protocol.connectionstate.ConnectionState;
import one.aves.proxy.util.EncryptionHelper;

import javax.crypto.SecretKey;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MinecraftConnection extends SimpleChannelInboundHandler<NettyPacket> {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(MinecraftConnection.class);

	private final Aves aves;
	private final Direction direction;
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue =
			Queues.newConcurrentLinkedQueue();

	private Channel channel;

	private NetworkHandler networkHandler;
	private boolean encrypted;

	public MinecraftConnection(Aves aves, Direction direction) {
		this.aves = aves;
		this.direction = direction;
		LOGGER.printInfo("Created MinecraftConnection with direction %s", direction);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext,
	                            NettyPacket nettyPacket) {
		if (!this.channel.isOpen()) {
			return;
		}

		try {
			nettyPacket.handle(this.networkHandler);
		} catch (Exception e) {
			LOGGER.printException("Error while handling packet %s", e,
					nettyPacket.getClass().getSimpleName());
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext context) throws Exception {
		super.channelActive(context);
		this.channel = context.channel();
		this.setConnectionState(ConnectionState.HANDSHAKE);
	}

	public void sendPacket(NettyPacket<?> nettyPacket) {
		if (!this.channel.isOpen()) {
			this.readWriteLock.writeLock().lock();
			try {
				this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(nettyPacket));
			} finally {
				this.readWriteLock.writeLock().unlock();
			}

			return;
		}

		this.flushOutboundQueue();
		this.dispatchPacket(nettyPacket);
	}

	private void flushOutboundQueue() {
		if (!this.isOpen()) {
			return;
		}

		this.readWriteLock.readLock().lock();
		try {
			while (!this.outboundPacketsQueue.isEmpty()) {
				InboundHandlerTuplePacketListener poll = this.outboundPacketsQueue.poll();
				this.dispatchPacket(poll.packet, poll.futureListeners);
			}
		} finally {
			this.readWriteLock.readLock().unlock();
		}
	}

	private void dispatchPacket(NettyPacket<?> packet,
	                            GenericFutureListener<? extends Future<? super Void>>... futureListeners) {
		ConnectionState currentState = this.channel.attr(ConnectionState.ATTRIBUTE_KEY).get();
		ConnectionState packetState = ConnectionState.fromPacket(packet.getClass());
		if (currentState != packetState) {
			LOGGER.printInfo("Disabled auto read as states dont match up (%s - %s)", currentState,
					packetState);
			this.channel.config().setAutoRead(false);
		}

		if (this.channel.eventLoop().inEventLoop()) {
			if (packetState != currentState) {
				this.setConnectionState(packetState);
			}

			ChannelFuture channelFuture = this.channel.writeAndFlush(packet, this.channel.voidPromise());
			if (futureListeners != null) {
				//channelFuture.addListeners(futureListeners);
			}

			// channelFuture.addListener(future -> {LOGGER.printInfo("Dispatched packet %s",
			//	packet.getClass().getSimpleName());});

			//channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		} else {
			this.channel.eventLoop().execute(new Runnable() {
				public void run() {
					if (packetState != currentState) {
						MinecraftConnection.this.setConnectionState(packetState);
					}

					ChannelFuture channelFuture = MinecraftConnection.this.channel.writeAndFlush(packet);
					if (futureListeners != null) {
						channelFuture.addListeners(futureListeners);
					}

					channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
				}
			});
		}
	}

	public void setConnectionState(ConnectionState connectionState) {
		this.channel.attr(ConnectionState.ATTRIBUTE_KEY).set(connectionState);
		this.channel.config().setAutoRead(true);
	}

	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		this.channel.attr(ProtocolVersion.ATTRIBUTE_KEY).set(protocolVersion);
	}

	public void setNetworkHandler(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	public boolean isOpen() {
		return this.channel != null && this.channel.isOpen();
	}

	public void close() {
		if (!this.isOpen()) {
			return;
		}

		LOGGER.printWarning("Closed connection");
		this.channel.close().awaitUninterruptibly();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	public Aves aves() {
		return aves;
	}

	public void enableEncryption(SecretKey secretKey) {
		this.encrypted = true;
		this.channel.pipeline().addBefore("splitter", "decrypt",
				new EncryptionDecoder(EncryptionHelper.createNetCipherInstance(2, secretKey)));
		this.channel.pipeline().addBefore("prepender", "encrypt",
				new EncryptionEncoder(EncryptionHelper.createNetCipherInstance(1, secretKey)));
	}

	static class InboundHandlerTuplePacketListener {

		private final NettyPacket packet;
		private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

		public InboundHandlerTuplePacketListener(NettyPacket inPacket,
		                                         GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
			this.packet = inPacket;
			this.futureListeners = inFutureListeners;
		}
	}
}
