package one.aves.proxy;

import one.aves.api.AvesServer;
import one.aves.api.service.Service;
import one.aves.proxy.network.ConnectionHandler;
import one.aves.proxy.util.EncryptionHelper;

import java.net.InetSocketAddress;
import java.security.KeyPair;

public class Aves extends AvesServer implements Service {

	private final ConnectionHandler connectionHandler;
	private final KeyPair keyPair;

	protected Aves() {
		add(this);

		keyPair = EncryptionHelper.generateKeyPair();

		connectionHandler = new ConnectionHandler(this);
	}

	protected void start() {
		connectionHandler.bind(new InetSocketAddress("0.0.0.0", 25565));
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}
}
