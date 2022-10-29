package one.aves.api.connection;

import com.google.common.collect.Sets;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.Set;

public class ProtocolVersion {

	private static final Set<ProtocolVersion> VALUES = Sets.newHashSet();

	public static final ProtocolVersion UNKNOWN = new ProtocolVersion(-1, "unknown");
	public static final ProtocolVersion MC_1_7_2 = new ProtocolVersion(4, "1.7.2", "1.7.3", "1.7.4",
			"1.7.5");
	public static final ProtocolVersion MC_1_7_6 = new ProtocolVersion(5, "1.7.6", "1.7.7", "1.7.8",
			"1.7.9", "1.7.10");
	public static final ProtocolVersion MC_1_8_0 = new ProtocolVersion(47, "1.8", "1.8.1", "1.8.2",
			"1.8.3", "1.8.4", "1.8.5", "1.8.6", "1.8.7", "1.8.8", "1.8.9");
	public static final ProtocolVersion MC_1_9_0 = new ProtocolVersion(107, "1.9");
	public static final ProtocolVersion MC_1_9_1 = new ProtocolVersion(108, "1.9.1");
	public static final ProtocolVersion MC_1_9_2 = new ProtocolVersion(109, "1.9.2");
	public static final ProtocolVersion MC_1_9_4 = new ProtocolVersion(110, "1.9.3", "1.9.4");
	public static final ProtocolVersion MC_1_10_0 = new ProtocolVersion(210, "1.10", "1.10.1",
			"1.10.2");
	public static final ProtocolVersion MC_1_11_0 = new ProtocolVersion(315, "1.11");
	public static final ProtocolVersion MC_1_11_1 = new ProtocolVersion(316, "1.11.1", "1.11.2");
	public static final ProtocolVersion MC_1_12_0 = new ProtocolVersion(335, "1.12");
	public static final ProtocolVersion MC_1_12_1 = new ProtocolVersion(338, "1.12.1");
	public static final ProtocolVersion MC_1_12_2 = new ProtocolVersion(340, "1.12.2");
	public static final ProtocolVersion MC_1_13_0 = new ProtocolVersion(393, "1.13");
	public static final ProtocolVersion MC_1_13_1 = new ProtocolVersion(401, "1.13.1");
	public static final ProtocolVersion MC_1_13_2 = new ProtocolVersion(404, "1.13.2");
	public static final ProtocolVersion MC_1_14_0 = new ProtocolVersion(477, "1.14");
	public static final ProtocolVersion MC_1_14_1 = new ProtocolVersion(480, "1.14.1");
	public static final ProtocolVersion MC_1_14_2 = new ProtocolVersion(485, "1.14.2");
	public static final ProtocolVersion MC_1_14_3 = new ProtocolVersion(490, "1.14.3");
	public static final ProtocolVersion MC_1_14_4 = new ProtocolVersion(498, "1.14.4");
	public static final ProtocolVersion MC_1_15_0 = new ProtocolVersion(573, "1.15");
	public static final ProtocolVersion MC_1_15_1 = new ProtocolVersion(575, "1.15.1");
	public static final ProtocolVersion MC_1_15_2 = new ProtocolVersion(578, "1.15.2");
	public static final ProtocolVersion MC_1_16_0 = new ProtocolVersion(735, "1.16");
	public static final ProtocolVersion MC_1_16_1 = new ProtocolVersion(736, "1.16.1");
	public static final ProtocolVersion MC_1_16_2 = new ProtocolVersion(751, "1.16.2");
	public static final ProtocolVersion MC_1_16_3 = new ProtocolVersion(753, "1.16.3");
	public static final ProtocolVersion MC_1_16_4 = new ProtocolVersion(754, "1.16.4", "1.16.5");
	public static final ProtocolVersion MC_1_17_0 = new ProtocolVersion(755, "1.17");
	public static final ProtocolVersion MC_1_17_1 = new ProtocolVersion(756, "1.17.1");
	public static final ProtocolVersion MC_1_18_0 = new ProtocolVersion(757, "1.18", "1.18.1");
	public static final ProtocolVersion MC_1_18_2 = new ProtocolVersion(758, "1.18.2");
	public static final ProtocolVersion MC_1_19_0 = new ProtocolVersion(759, "1.19");
	public static final ProtocolVersion MC_1_19_1 = new ProtocolVersion(760, "1.19.1", "1.19.2");

	public static final AttributeKey<ProtocolVersion> ATTRIBUTE_KEY = AttributeKey.valueOf(
			"version");

	private static final int SNAPSHOT_OFFSET = 0x40000000;
	private final int protocol;
	private final String[] names;
	private final boolean unknown;
	private boolean snapshot;

	protected ProtocolVersion(int protocol, String... names) {
		this(protocol, false, names);
	}

	protected ProtocolVersion(int protocol, boolean unknown, String... names) {
		this.protocol = protocol;
		this.names = names;
		this.unknown = unknown;

		if (!unknown) {
			VALUES.add(this);
		}
	}

	private static ProtocolVersion get(int protocol) {
		for (ProtocolVersion version : VALUES) {
			if (version.protocol == protocol) {
				return version;
			}
		}

		return null;
	}

	public static ProtocolVersion getByProtocol(int protocol) {
		ProtocolVersion version = ProtocolVersion.get(protocol);
		if (version != null) {
			return version;
		}

		ProtocolVersion protocolVersion = new ProtocolVersion(protocol, true, "");
		if (protocolVersion.isSnapshot()) {
			protocolVersion.names[0] = "snapshot_" + (protocol - SNAPSHOT_OFFSET);
		} else {
			protocolVersion.names[0] = "unknown" + protocol;
		}

		return protocolVersion;
	}

	public static ProtocolVersion fromContext(ChannelHandlerContext context) {
		ProtocolVersion protocolVersion = context.channel().attr(ATTRIBUTE_KEY).get();
		if (protocolVersion == null) {
			return UNKNOWN;
		}

		return protocolVersion;
	}

	public int getProtocol() {
		return this.protocol;
	}

	public String[] getNames() {
		return this.names;
	}

	public boolean isBefore(ProtocolVersion version) {
		return this.protocol < version.protocol;
	}

	public boolean isAfter(ProtocolVersion version) {
		return this.protocol >= version.protocol;
	}

	public boolean isUnknown() {
		return this.unknown;
	}

	public boolean isSnapshot() {
		if (!this.unknown) {
			return false;
		}

		if (this.snapshot) {
			return true;
		}

		if (this.protocol < MC_1_16_3.protocol) {
			this.snapshot = true;
			return true;
		}

		this.snapshot = this.protocol - SNAPSHOT_OFFSET >= 0;
		return this.snapshot;
	}

	@Override
	public String toString() {
		return this.names[0];
	}
}
