package demo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheStats {
	private static Logger LOG = LoggerFactory.getLogger(CacheStats.class);
	
	public static void main(String[] args) throws Exception {
		LOG.info("cache.size="+Config.CACHE.getSize());
		Config.CACHE.removeAll();
		LOG.info("cache.size="+Config.CACHE.getSize());
		Config.CACHE_MANAGER.removeCache(Config.CACHE_NAME);
	}

}
