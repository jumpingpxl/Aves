package one.aves.api.component;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public enum TextDecoration {
	BOLD('l'),
	ITALIC('o'),
	UNDERLINE('n'),
	STRIKETHROUGH('m'),
	OBFUSCATED('k'),
	NONE('r');

	public static final Set<TextDecoration> VALUES = Collections.unmodifiableSet(
			Sets.newHashSet(TextDecoration.values()));

	private final char legacyCharacter;
	private final String actualName;

	TextDecoration(char legacyCharacter) {
		this.legacyCharacter = legacyCharacter;
		this.actualName = name().toLowerCase();
	}

	public char getLegacyCharacter() {
		return legacyCharacter;
	}

	public String getActualName() {
		return actualName;
	}

	@Override
	public String toString() {
		return "TextDecoration{" + "name='" + this.name() + '\'' + ", legacyCharacter="
				+ legacyCharacter + '}';
	}
}
