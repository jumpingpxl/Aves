package one.aves.proxy;

import one.aves.api.Aves;
import one.aves.api.event.EventService;
import one.aves.api.service.ServiceProvider;
import one.aves.proxy.event.DefaultEventService;
import one.aves.proxy.event.TestListener;
import one.aves.proxy.network.ConnectionHandler;
import one.aves.proxy.util.EncryptionHelper;
import one.aves.proxy.util.UserAuthenticator;

import java.net.InetSocketAddress;
import java.security.KeyPair;

public class DefaultAves extends Aves {

	private final ConnectionHandler connectionHandler;
	private final KeyPair keyPair;
	private final UserAuthenticator userAuthenticator;
	private final EventService eventService;
	private final String serverId;

	protected DefaultAves() {
		ServiceProvider.register(Aves.class, this);

		this.serverId = "";

		this.keyPair = EncryptionHelper.generateKeyPair();
		this.userAuthenticator = new UserAuthenticator();

		this.eventService = new DefaultEventService();

		this.connectionHandler = new ConnectionHandler(this);

		this.eventService.registerListener(new TestListener());
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

	public EventService eventService() {
		return this.eventService;
	}
}
