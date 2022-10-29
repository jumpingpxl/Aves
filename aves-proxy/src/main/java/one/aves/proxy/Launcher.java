package one.aves.proxy;

import one.aves.api.console.ConsoleLogger;

import java.text.DecimalFormat;

public class Launcher {

	private static final ConsoleLogger LOGGER = ConsoleLogger.of(Launcher.class);

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		LOGGER.printInfo("Starting Aves Proxy...");

		new DefaultAves().start();

		double bootTime = (System.currentTimeMillis() - startTime) / 1000D;
		LOGGER.printInfo("Done in %s seconds!", new DecimalFormat("#.##").format(bootTime));
	}
}
