package one.aves.proxy.network.packet.common;

import com.google.gson.JsonObject;
import one.aves.api.component.Component;
import one.aves.api.component.ComponentParser;
import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.NetworkHandler;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;

public class DisconnectPacket<T extends NetworkHandler> implements Packet<T> {

	private Component component;

	protected DisconnectPacket(Component component) {
		this.component = component;
	}

	protected DisconnectPacket() {

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
