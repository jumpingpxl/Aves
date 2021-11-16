package one.aves.proxy.network.protocol;

public class PacketHandler {

	private static final Class<? extends NettyPacket>[] STATUS;
	private static Class<? extends NettyPacket>[] LOGIN;
	private static Class<? extends NettyPacket>[] PLAY;

	static {
		STATUS = new Class[]{NettyPacket.class};
	}
}
