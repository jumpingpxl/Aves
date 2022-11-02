package one.aves.proxy.network.packet.play.clientbound;

import one.aves.api.component.Component;
import one.aves.proxy.network.handler.NetworkPlayHandler;
import one.aves.proxy.network.packet.common.DisconnectPacket;

public class PlayDisconnectPacket extends DisconnectPacket<NetworkPlayHandler> {

	public PlayDisconnectPacket(Component component) {
		super(component);
	}

	public PlayDisconnectPacket() {
	}
}
