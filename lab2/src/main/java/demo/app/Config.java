package demo.app;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;

import demo.common.Constants;

public class Config {

	public static int NUMBER_OF_ENTRIES = 1000000;
	public static int SIZE_OF_ENTRY = 1 * Constants._1KB;

	private static String EHCACHE_CONFIG = "tsa-ehcache.xml";
	// private static String EHCACHE_CONFIG="local-offheap-ehcache.xml";
	public static String CACHE_NAME = "myCache";

	public static CacheManager CACHE_MANAGER;
	public static Cache CACHE;

	static {
		URL url = PutAndGet.class.getResource("/" + EHCACHE_CONFIG);
		CACHE_MANAGER = CacheManager.newInstance(url);
		CACHE = CACHE_MANAGER.getCache(CACHE_NAME);
	}

	// metric collector
	public final static Timer CALL_TIMER = Metrics.newTimer(Config.class,
			"each call", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

}
