package one.aves.api.event;

public class Priority {

	public static final byte FIRST = -128;
	public static final byte EARLY = -64;
	public static final byte NORMAL = 0;
	public static final byte LATE = 64;
	public static final byte LATEST = 127;

	private Priority() {
		// utility class
	}
}
