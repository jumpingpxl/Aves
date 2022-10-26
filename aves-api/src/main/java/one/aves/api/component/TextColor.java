package one.aves.api.component;

import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class TextColor {

	public static final TextColor BLACK = new TextColor("#000000", '0');
	public static final TextColor DARK_BLUE = new TextColor("#0000aa", '1');
	public static final TextColor GREEN = new TextColor("#00aa00", '2');
	public static final TextColor CYAN = new TextColor("#00aaaa", '3');
	public static final TextColor DARK_RED = new TextColor("#aa0000", '4');
	public static final TextColor PURPLE = new TextColor("#aa00aa", '5');
	public static final TextColor GOLD = new TextColor("#ffaa00", '6');
	public static final TextColor GRAY = new TextColor("#aaaaaa", '7');
	public static final TextColor DARK_GRAY = new TextColor("#555555", '8');
	public static final TextColor BLUE = new TextColor("#5555ff", '9');
	public static final TextColor LIME = new TextColor("#55ff55", 'a');
	public static final TextColor AQUA = new TextColor("#55ffff", 'b');
	public static final TextColor RED = new TextColor("#ff5555", 'c');
	public static final TextColor PINK = new TextColor("#ff55ff", 'd');
	public static final TextColor YELLOW = new TextColor("#ffff55", 'e');
	public static final TextColor WHITE = new TextColor("#ffffff", 'f');

	public static final Set<TextColor> VALUES = Collections.unmodifiableSet(
			Sets.newHashSet(BLACK, DARK_BLUE, GREEN, CYAN, DARK_RED, PURPLE, GOLD, GRAY, DARK_GRAY, BLUE,
					LIME, AQUA, RED, PINK, YELLOW, WHITE));

	private final String value;
	private final Character legacyCharacter;

	private TextColor(String hex, Character legacyCharacter) {
		this.value = hex;
		this.legacyCharacter = legacyCharacter;
	}

	private TextColor(String hex) {
		Objects.requireNonNull(hex, "Hex code cannot be null");
		this.value = hex;
		this.legacyCharacter = null;
	}

	public static TextColor of(@Nonnull String hex) {
		return new TextColor(hex);
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
