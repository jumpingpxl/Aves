package one.aves.api.console;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleLogger {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss] ");
	private final Logger logger;

	private ConsoleLogger(ClassLoader classLoader) {
		logger = Logger.getLogger(classLoader.getName());
	}

	public static ConsoleLogger getLogger(Class<?> clazz) {
		return new ConsoleLogger(clazz.getClassLoader());
	}

	public void printInfo(String message, Object... params) {
		print(Level.INFO, message, params);
	}

	public void printWarning(String message, Object... params) {
		print(Level.WARNING, message, params);
	}

	public void printSevere(String message, Object... params) {
		print(Level.SEVERE, message, params);
	}

	private void print(Level level, String message, Object... params) {
		logger.log(level, dateFormat.format(System.currentTimeMillis()) + message, params);
	}
}
