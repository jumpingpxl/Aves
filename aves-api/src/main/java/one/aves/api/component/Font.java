package one.aves.api.component;

import java.util.Objects;

public class Font {

	public static final Font UNIFORM = new Font("minecraft:uniform");
	public static final Font ALT = new Font("minecraft:alt");
	public static final Font DEFAULT = new Font("minecraft:default");

	private final String actualName;

	private Font(String actualName) {
		this.actualName = actualName;
	}

	public String getActualName() {
		return actualName;
	}

	@Override
	public String toString() {
		return "Font{" + "actualName='" + actualName + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Font font = (Font) o;
		return actualName.equals(font.actualName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(actualName);
	}
}
