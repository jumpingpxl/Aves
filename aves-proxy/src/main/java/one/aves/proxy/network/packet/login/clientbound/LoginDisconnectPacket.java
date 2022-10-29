package one.aves.proxy.network.packet.login.clientbound;

import com.google.gson.JsonObject;
import one.aves.api.component.Component;
import one.aves.api.component.ComponentParser;
import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;

public class LoginDisconnectPacket implements Packet<NetworkHandler> {

	private final Component component;

	public LoginDisconnectPacket(Component component) {
		this.component = component;
	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		//todo inverted component parsing
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		JsonObject jsonObject = ComponentParser.toJson(this.component, protocol, true);
		byteBuffer.writeString(jsonObject.toString());
	}
}
