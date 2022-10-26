package one.aves.proxy.network.protocol.packet.status;

import one.aves.api.connection.ProtocolVersion;
import one.aves.api.connection.ServerPing;
import one.aves.proxy.network.handler.NetworkStatusHandler;
import one.aves.proxy.network.protocol.ByteBuffer;
import one.aves.proxy.network.protocol.Direction;

public class StatusPacket implements StatusNettyPacket {

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		//the server bound packet is empty
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		String input = new ServerPing().toJsonString(protocol);
		System.out.println("StatusPacket.encode() input: " + input);
		byteBuffer.writeString(input);
		//byteBuffer.writeString(GsonFactory.serialize(GsonIdentifier.SERVER_INFO, new ServerPing()));
	}

	@Override
	public void handle(NetworkStatusHandler networkHandler) {
		networkHandler.handleStatusRequest(this);
	}
}
