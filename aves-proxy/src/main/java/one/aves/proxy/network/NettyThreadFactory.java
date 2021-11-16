package one.aves.proxy.network;

import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

public class NettyThreadFactory implements ThreadFactory {

	private final AtomicInteger threadNumber = new AtomicInteger();
	private final String nameFormat;

	public NettyThreadFactory(String nameFormat) {
		this.nameFormat = checkNotNull(nameFormat, "nameFormat");
	}

	@Override
	public Thread newThread(Runnable r) {
		String name = String.format(nameFormat, threadNumber.getAndIncrement());
		return new FastThreadLocalThread(r, name);
	}
}
