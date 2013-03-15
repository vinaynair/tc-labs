package org.terracotta.labs.common;

import java.net.URL;

import junit.framework.Assert;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: ability to configure caches without xml <br/>
 * TODO: ability to setup clustered/distributed cache server
 * 
 * @author vch
 * 
 */
public class AbstractCacheTestScenarioSupport {
	public Logger LOG;
	private CacheManager _cacheManager;

	public AbstractCacheTestScenarioSupport() {
		LOG = LoggerFactory.getLogger(getClass());

	}

	protected void usesEhcacheConfig(String ehcacheConfig) {
		try {
			URL config = getClass().getResource("/" + ehcacheConfig);
			Assert.assertNotNull("Could not find " + ehcacheConfig
					+ " in classpath",config);
			_cacheManager = CacheManager.create(config);
			Assert.assertNotNull("Could not instantiate cachemanager " + ehcacheConfig
					+ " in classpath",_cacheManager);
		} catch (net.sf.ehcache.CacheException e) {
			if (e.getCause() instanceof org.terracotta.license.LicenseException) {
				LOG.error(e.getCause().getMessage());
				Assert.fail("Could not find terracotta-license.key in classpath. Place it within src/test/resources folder");
			}
			throw e;
		}
	}

	@After
	public void cleanupCache() {
		LOG.info("cleaning cache");
		if (_cacheManager != null)
			_cacheManager.shutdown();

	}

	protected void usesCache(String cacheName) {
		getCache(cacheName);
	}

	protected Cache getCache(String cacheName) {
		Cache cache = _cacheManager.getCache(cacheName);
		Assert.assertNotNull("cache name " + cacheName + " not defined", cache);
		return cache;
	}

}
