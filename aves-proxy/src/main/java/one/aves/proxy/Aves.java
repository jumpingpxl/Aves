package one.aves.proxy;

import one.aves.api.AvesServer;
import one.aves.api.service.Service;
import one.aves.proxy.network.ConnectionHandler;
import one.aves.proxy.util.EncryptionHelper;
import one.aves.proxy.util.UserAuthenticator;

import java.net.InetSocketAddress;
import java.security.KeyPair;

public class Aves extends AvesServer implements Service {

	private final ConnectionHandler connectionHandler;
	private final KeyPair keyPair;
	private final UserAuthenticator userAuthenticator;
	private final String serverId;

	protected Aves() {
		add(this);

		this.serverId = "";

		this.keyPair = EncryptionHelper.generateKeyPair();
		this.userAuthenticator = new UserAuthenticator();

		this.connectionHandler = new ConnectionHandler(this);
	}

	protected void start() {
		this.connectionHandler.bind(new InetSocketAddress("0.0.0.0", 25565));
	}

	public KeyPair getKeyPair() {
		return this.keyPair;
	}

	public String getServerId() {
		return this.serverId;
	}

	public UserAuthenticator userAuthenticator() {
		return this.userAuthenticator;
	}
}
