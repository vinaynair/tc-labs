package demo.app;

import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Timer;

import demo.common.Constants;

public class Config {

	public static int NUMBER_OF_ENTRIES = 10;
	public static int SIZE_OF_ENTRY = 1 * Constants._1KB;

	private String EHCACHE_CONFIG = "tsa-ehcache.xml";
	// private static String EHCACHE_CONFIG="local-offheap-ehcache.xml";
	public String CACHE_NAME = "myCache";

	public CacheManager _cacheManager;
	public Cache _cache;

	public Config() {
		load();
	}

	public void load() {
		URL url = Config.class.getResource("/" + EHCACHE_CONFIG);
		_cacheManager = CacheManager.newInstance(url);
		_cache = _cacheManager.getCache(CACHE_NAME);
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService.registerMBeans(_cacheManager, mBeanServer, false,
				false, false, true);
	}

	public Cache getCache() {
		return _cache;
	}

	/**
	 * get another cache from the cachemanger
	 * 
	 * @param name
	 * @return
	 */
	public Cache getCache(String name) {
		return _cacheManager.getCache(name);
	}

	// metric collector
	public final static Timer CALL_TIMER = Metrics.newTimer(Config.class,
			"each call", TimeUnit.MILLISECONDS, TimeUnit.SECONDS);

}
