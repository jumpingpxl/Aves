package one.aves.proxy.network.packet.play.serverbound;

import one.aves.proxy.network.packet.play.common.PluginMessagePacket;

public class IncomingPluginMessagePacket extends PluginMessagePacket {

	public IncomingPluginMessagePacket(String channel, byte[] data) {
		super(channel, data);
	}

	public IncomingPluginMessagePacket() {
	}
}
