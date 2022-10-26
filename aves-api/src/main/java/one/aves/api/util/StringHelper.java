package one.aves.api.util;

import java.util.Collection;

public class StringHelper {

	private StringHelper() {

	}

	public static String join(Collection<?> collection, String delimiter) {
		StringBuilder builder = new StringBuilder();
		for (Object object : collection) {
			builder.append(object.toString()).append(delimiter);
		}

		if (builder.length() > 0) {
			builder.delete(builder.length() - delimiter.length(), builder.length());
		}

		return builder.toString();
	}
}
