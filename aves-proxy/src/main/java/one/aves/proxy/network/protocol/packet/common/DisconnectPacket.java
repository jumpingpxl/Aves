package one.aves.proxy.network.protocol.packet.common;

import com.google.gson.JsonObject;
import one.aves.api.component.Component;
import one.aves.api.component.ComponentParser;
import one.aves.api.connection.ProtocolVersion;
import one.aves.proxy.network.handler.NetworkHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;
import one.aves.proxy.network.protocol.NettyPacket;

public class DisconnectPacket implements NettyPacket<NetworkHandler> {

	private final Component component;

	public DisconnectPacket(Component component) {
		this.component = component;
	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {

	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		JsonObject jsonObject = ComponentParser.toJson(component, protocol, true);
		byteBuffer.writeString(jsonObject.toString());
	}
}
