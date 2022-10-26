package one.aves.api.console;

import java.text.SimpleDateFormat;
import java.util.logging.Level;

public class ConsoleLogger {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss:SSS] ");
	//private final Logger logger;
	private final String name;

	private ConsoleLogger(Class<?> cls) {
		this.name = cls.getSimpleName();
		//logger = Logger.getLogger(classLoader.getName());
	}

	public static ConsoleLogger getLogger(Class<?> clazz) {
		return new ConsoleLogger(clazz);
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

	public void printException(String message, Throwable throwable, Object... params) {
		print(Level.SEVERE, message, throwable, params);
	}

	private void print(Level level, String message, Object... params) {
		System.out.println(
				dateFormat.format(System.currentTimeMillis()) + level.getName() + " - " + this.name + " "
						+ "-> "
						+ this.getMessage(message, params));
	}

	private void print(Level level, String message, Throwable throwable, Object... params) {
		this.print(level, message, params);
		throwable.printStackTrace();
	}

	private String getMessage(String message, Object... params) {
		return String.format(message, params);
	}
}
