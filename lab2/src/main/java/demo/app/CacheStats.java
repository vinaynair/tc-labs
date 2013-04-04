package demo.app;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheStats {
	private static Logger LOG = LoggerFactory.getLogger(CacheStats.class);

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		Cache cache = config.getCache();
		LOG.info("cache.size=" + cache.getSize());

		cache.removeAll();
		LOG.info("cleared cache and now cache.size=" + cache.getSize());

	}

}
