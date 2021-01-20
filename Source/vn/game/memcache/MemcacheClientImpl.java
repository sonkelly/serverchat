package vn.game.memcache;

//import com.punch.framework.common.ILoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;

import vn.game.common.LoggerContext;

class MemcacheClientImpl extends AbstractMemcacheClient {
	private final Logger mLog = LoggerContext.getLoggerFactory().getLogger(MemcacheClientImpl.class);
	private MemcachedClient mClient;
	private String mNameSpace;
	private final int MAX_KEY_LEN = 450;

	public MemcacheClientImpl(String aAddrList, String aNameSpace) throws Throwable {
		this.mClient = new MemcachedClient(new InetSocketAddress("localhost", 9501));
                //this.mClient = new MemcachedClient(new InetSocketAddress("125.212.192.97", 9501));
		this.mNameSpace = aNameSpace;
	}

	public void set(String aKey, int aExpiration, Object aSerializableObj) {
		if (aSerializableObj != null) {
			// System.out.append("came here");

			try {
				String fullKey = this.mNameSpace + "_" + aKey;

				fullKey.replace("/", "SsS");

				if (fullKey.length() <= MAX_KEY_LEN) {

					this.mClient.set(fullKey, aExpiration, aSerializableObj);
				}
			} catch (Throwable t) {
				this.mLog.error("[MEMCACHE]", t);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public Object get(String aKey) {
		Object valObj = null;
		String fullKey = this.mNameSpace + "_" + aKey;

		fullKey.replace("/", "SsS");
		if (fullKey.length() <= MAX_KEY_LEN) {
			// System.out.println("full key" + fullKey);
			// return this.mClient.get(fullKey);
			Future f = this.mClient.asyncGet(fullKey);
			try {
				valObj = f.get(5L, TimeUnit.SECONDS);
			} catch (Throwable t) {
				this.mLog.warn("[MEMCACHE] get key = " + fullKey + " timeout within 5 seconds.");

				f.cancel(true);
			}
		}

		return valObj;
	}

	public void delete(String aKey) {
		String fullKey;
		try {
			fullKey = this.mNameSpace + "_" + aKey;

			fullKey.replace("/", "SsS");
			this.mClient.delete(fullKey);
		} catch (Throwable t) {
			this.mLog.error("[MEMCACHE]", t);
		}
	}

	public void close() {
		try {
			this.mClient.shutdown();
		} catch (Throwable t) {
			this.mLog.error("[MEMCACHE]", t);
		}
	}
}