package one.aves.api.component;

import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class TextColor {

	public static final TextColor BLACK = new TextColor("black", '0');
	public static final TextColor DARK_BLUE = new TextColor("dark_blue", '1');
	public static final TextColor GREEN = new TextColor("dark_green", '2');
	public static final TextColor CYAN = new TextColor("dark_aqua", '3');
	public static final TextColor DARK_RED = new TextColor("dark_red", '4');
	public static final TextColor PURPLE = new TextColor("dark_purple", '5');
	public static final TextColor GOLD = new TextColor("gold", '6');
	public static final TextColor GRAY = new TextColor("gray", '7');
	public static final TextColor DARK_GRAY = new TextColor("dark_gray", '8');
	public static final TextColor BLUE = new TextColor("blue", '9');
	public static final TextColor LIME = new TextColor("green", 'a');
	public static final TextColor AQUA = new TextColor("aqua", 'b');
	public static final TextColor RED = new TextColor("red", 'c');
	public static final TextColor PINK = new TextColor("light_purple", 'd');
	public static final TextColor YELLOW = new TextColor("yellow", 'e');
	public static final TextColor WHITE = new TextColor("white", 'f');

	public static final Set<TextColor> VALUES = Collections.unmodifiableSet(
			Sets.newHashSet(BLACK, DARK_BLUE, GREEN, CYAN, DARK_RED, PURPLE, GOLD, GRAY, DARK_GRAY, BLUE,
					LIME, AQUA, RED, PINK, YELLOW, WHITE));

	private final String value;
	private final Character legacyCharacter;

	private TextColor(String value, Character legacyCharacter) {
		this.value = value;
		this.legacyCharacter = legacyCharacter;
	}

	private TextColor(String value) {
		Objects.requireNonNull(value, "Value cannot be null");
		this.value = value;
		this.legacyCharacter = null;
	}

	public static TextColor of(@Nonnull String value) {
		return new TextColor(value);
	}

	public static TextColor of(int rgb) {
		return TextColor.of(String.format("#%06X", (0xFFFFFF & rgb)));
	}

	public static TextColor of(@Nonnull Color color) {
		Objects.requireNonNull(color, "Color cannot be null");
		return TextColor.of(color.getRGB());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TextColor textColor = (TextColor) o;
		return value.equals(textColor.value) && Objects.equals(legacyCharacter,
				textColor.legacyCharacter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, legacyCharacter);
	}

	@Override
	public String toString() {
		return "TextColor{" + "value='" + value + '\'' + ", legacyCharacter=" + legacyCharacter + '}';
	}

	public @Nonnull String get() {
		return value;
	}

	public @Nullable Character getLegacyCharacter() {
		return legacyCharacter;
	}
}
