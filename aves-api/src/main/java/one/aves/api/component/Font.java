package one.aves.api.component;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Font {

	public static final Font UNIFORM = Font.of("uniform");
	public static final Font ALT = Font.of("alt");
	public static final Font DEFAULT = Font.of("default");

	private final String actualName;

	private Font(String actualName) {
		this.actualName = actualName;
	}

	public static Font of(@Nonnull String namespace, @Nonnull String name) {
		Objects.requireNonNull(name, "Font name space cannot be null");
		Objects.requireNonNull(name, "Font name cannot be null");
		return new Font(namespace + ":" + name);
	}

	public static Font of(@Nonnull String name) {
		Objects.requireNonNull(name, "Font name cannot be null");
		return new Font("minecraft:" + name);
	}

	public String getActualName() {
		return this.actualName;
	}

	@Override
	public String toString() {
		return "Font{" + "actualName='" + this.actualName + '\'' + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		Font font = (Font) o;
		return this.actualName.equals(font.actualName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.actualName);
	}
}
