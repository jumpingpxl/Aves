package one.aves.proxy;

import one.aves.api.AvesServer;
import one.aves.api.service.Service;

public class Aves extends AvesServer implements Service {

	protected Aves() {
		addService(this);
	}
}
