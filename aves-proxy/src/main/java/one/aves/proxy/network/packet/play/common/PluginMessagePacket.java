package one.aves.proxy.network.packet.play.common;

import one.aves.api.network.ByteBuffer;
import one.aves.api.network.Direction;
import one.aves.api.network.ProtocolVersion;
import one.aves.api.network.packet.Packet;
import one.aves.proxy.network.handler.NetworkPlayHandler;

public class PluginMessagePacket implements Packet<NetworkPlayHandler> {

	private String channel;
	private byte[] data;

	protected PluginMessagePacket(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}

	protected PluginMessagePacket() {

	}

	@Override
	public void decode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		this.channel = byteBuffer.readString();
		this.data = byteBuffer.readByteArray();
	}

	@Override
	public void encode(ByteBuffer byteBuffer, Direction direction, ProtocolVersion protocol) {
		byteBuffer.writeString(this.channel);
		byteBuffer.writeByteArray(this.data);
	}

	public String getChannel() {
		return this.channel;
	}

	public byte[] getData() {
		return this.data;
	}
}
