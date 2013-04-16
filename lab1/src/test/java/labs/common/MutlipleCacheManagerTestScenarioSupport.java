package labs.common;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Do I have a use for this?
 * 
 * @author vch
 * 
 */
public class MutlipleCacheManagerTestScenarioSupport {
	public Logger LOG;
	private static final String LOCAL = "local";
	private Map<String,CacheManager> _cacheManagers= new HashMap<String,CacheManager>();
	//private Map<String,Cache> _caches = new HashMap<String,Cache>();

	public MutlipleCacheManagerTestScenarioSupport() {
		LOG = LoggerFactory.getLogger(getClass());

	}
	protected void usesEhcacheConfig(String ehcacheConfig) {
		try {
			URL config = getClass().getResource("/" + ehcacheConfig);
			assertNotNull("Could not find " + ehcacheConfig
					+ " in classpath",config);
			CacheManager cacheManager = CacheManager.create(config);
			assertNotNull("Could not instantiate cachemanager " + ehcacheConfig
					+ " in classpath",cacheManager);
			_cacheManagers.put(LOCAL,cacheManager);
		} catch (net.sf.ehcache.CacheException e) {
			if (e.getCause() instanceof org.terracotta.license.LicenseException) {
				LOG.error(e.getCause().getMessage());
				fail("Could not find terracotta-license.key in classpath. Place it within src/test/resources folder");
			}
			throw e;
		}
	}

	@After
	public void cleanupCache() {
		LOG.info("cleaning cache manager");
		for (CacheManager cacheManager : _cacheManagers.values()) {
			cacheManager.shutdown();
		}

	}

	protected void usesCache(String cacheName) {
		getCache(cacheName,LOCAL);
	}
	protected Cache getCache(String cacheName){
		return getCache(cacheName,LOCAL);
	}

	protected Cache getCache(String cacheName,String port) {
		Cache cache = getCacheManager(port).getCache(cacheName);
		assertNotNull("cache name " + cacheName + " not defined", cache);
		return cache;
	}
	
	protected CacheManager getCacheManager() {
		return getCacheManager(LOCAL);
	}
	protected CacheManager getCacheManager(String port){
		CacheManager cacheManager = _cacheManagers.get(port);
		assertNotNull(cacheManager);
		return cacheManager;
	}
	
	
	

}
