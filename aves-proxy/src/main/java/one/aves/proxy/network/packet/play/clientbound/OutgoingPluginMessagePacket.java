package one.aves.proxy.network.packet.play.clientbound;

import one.aves.proxy.network.packet.play.common.PluginMessagePacket;

public class OutgoingPluginMessagePacket extends PluginMessagePacket {

	public OutgoingPluginMessagePacket(String channel, byte[] data) {
		super(channel, data);
	}

	public OutgoingPluginMessagePacket() {
	}
}
