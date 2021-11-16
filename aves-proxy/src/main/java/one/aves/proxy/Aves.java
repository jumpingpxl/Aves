package one.aves.proxy;

import one.aves.api.AvesServer;
import one.aves.api.service.Service;
import one.aves.proxy.network.ConnectionHandler;

import java.net.InetSocketAddress;

public class Aves extends AvesServer implements Service {

	private final ConnectionHandler connectionHandler;

	protected Aves() {
		addService(this);

		connectionHandler = new ConnectionHandler();
	}

	protected void start() {
		connectionHandler.bind(new InetSocketAddress("0.0.0.0", 25565));
	}
}
