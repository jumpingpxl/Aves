package one.aves.proxy.network.protocol;

public enum ProtocolVersion {

	UNKNOWN(-1, "Unknown"),
	LEGACY(-2, "Legacy"),
	MINECRAFT_1_7_2(4, "1.7.2", "1.7.3", "1.7.4", "1.7.5"),
	MINECRAFT_1_7_6(5, "1.7.6", "1.7.7", "1.7.8", "1.7.9", "1.7.10"),
	MINECRAFT_1_8_0(47, "1.8", "1.8.1", "1.8.2", "1.8.3", "1.8.4", "1.8.5", "1.8.6", "1.8.7",
			"1.8" + ".8", "1.8.9"),
	MINECRAFT_1_9_0(107, "1.9"),
	MINECRAFT_1_9_1(108, "1.9.1"),
	MINECRAFT_1_9_2(109, "1.9.2"),
	MINECRAFT_1_9_4(110, "1.9.3", "1.9.4"),
	MINECRAFT_1_10_0(210, "1.10", "1.10.1", "1.10.2"),
	MINECRAFT_1_11_0(315, "1.11"),
	MINECRAFT_1_11_1(316, "1.11.1", "1.11.2"),
	MINECRAFT_1_12_0(335, "1.12"),
	MINECRAFT_1_12_1(338, "1.12.1"),
	MINECRAFT_1_12_2(340, "1.12.2"),
	MINECRAFT_1_13_0(393, "1.13"),
	MINECRAFT_1_13_1(401, "1.13.1"),
	MINECRAFT_1_13_2(404, "1.13.2"),
	MINECRAFT_1_14_0(477, "1.14"),
	MINECRAFT_1_14_1(480, "1.14.1"),
	MINECRAFT_1_14_2(485, "1.14.2"),
	MINECRAFT_1_14_3(490, "1.14.3"),
	MINECRAFT_1_14_4(498, "1.14.4"),
	MINECRAFT_1_15_0(573, "1.15"),
	MINECRAFT_1_15_1(575, "1.15.1"),
	MINECRAFT_1_15_2(578, "1.15.2"),
	MINECRAFT_1_16_0(735, "1.16"),
	MINECRAFT_1_16_1(736, "1.16.1"),
	MINECRAFT_1_16_2(751, "1.16.2"),
	MINECRAFT_1_16_3(753, "1.16.3"),
	MINECRAFT_1_16_4(754, "1.16.4", "1.16.5"),
	MINECRAFT_1_17_0(755, "1.17"),
	MINECRAFT_1_17_1(756, "1.17.1");

	private static final ProtocolVersion[] VALUES = values();

	private final int protocol;
	private final String[] names;

	ProtocolVersion(int protocol, String... names) {
		this.protocol = protocol;
		this.names = names;
	}

	public int getProtocol() {
		return protocol;
	}

	public String[] getNames() {
		return names;
	}

	public ProtocolVersion getByProtocol(int protocol) {
		ProtocolVersion version = UNKNOWN;
		for (ProtocolVersion protocolVersion : VALUES) {
			if (protocolVersion.protocol == protocol) {
				version = protocolVersion;
				break;
			}
		}

		return version;
	}
}
