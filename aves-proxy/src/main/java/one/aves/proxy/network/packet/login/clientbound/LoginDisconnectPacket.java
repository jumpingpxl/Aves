package one.aves.proxy.network.packet.login.clientbound;

import one.aves.api.component.Component;
import one.aves.proxy.network.handler.NetworkLoginHandler;
import one.aves.proxy.network.packet.common.DisconnectPacket;

public class LoginDisconnectPacket extends DisconnectPacket<NetworkLoginHandler> {

	public LoginDisconnectPacket(Component component) {
		super(component);
	}

	public LoginDisconnectPacket() {
	}
}
