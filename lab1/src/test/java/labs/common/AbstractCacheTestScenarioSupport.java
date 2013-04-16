package labs.common;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.cluster.ClusterScheme;
import net.sf.ehcache.config.TerracottaClientConfiguration;
import net.sf.ehcache.terracotta.TerracottaClient;

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
			System.out.println(getClass().getResource(ehcacheConfig));
			URL config = this.getClass().getResource(ehcacheConfig);
			assertNotNull("Could not find " + ehcacheConfig + " in classpath",
					config);
			/*try {
				LOG.info("toolkit");
				Toolkit toolkit = ToolkitFactory.createToolkit("toolkit:terracotta://localhost:9510");
			} catch (InvalidToolkitConfigException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ToolkitInstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//TODO: is there a better way than http://forums.terracotta.org/forums/posts/list/7306.page?
			System.setProperty("com.tc.tc.config.total.timeout", "2000");
			_cacheManager = CacheManager.create(config);
			assertNotNull("Could not instantiate cachemanager " + ehcacheConfig
					+ " in classpath", _cacheManager);

			/*TerracottaClientConfiguration tc = _cacheManager.getConfiguration()
					.getTerracottaConfiguration();
			boolean isClusterOnline = _cacheManager.getCluster(
					ClusterScheme.TERRACOTTA).isClusterOnline();
			System.out.println("isClusterOnline=" + isClusterOnline);
			if (tc != null && isClusterOnline == false) {
				fail("Cluster is offline, no proceeding with the tests");
			}*/

		} catch (net.sf.ehcache.CacheException e) {
			if (e.getCause() instanceof org.terracotta.license.LicenseException) {
				LOG.error(e.getCause().getMessage());
				fail("Could not find terracotta-license.key in classpath. Place it within src/test/resources folder");
				throw e;
			} else if ( e.getCause() instanceof RuntimeException){
				fail("Could not create cache Manager. Something nasty happened");
			}
			
		} catch (Throwable re){
			fail("Something bad happened. See"+re.getMessage());
			throw new RuntimeException(re);
		}
	}

	@After
	public void cleanupCache() {
		LOG.info("cleaning cache manager");
		if (_cacheManager != null)
			_cacheManager.shutdown();

	}

	protected void usesCache(String cacheName) {
		getCache(cacheName);
	}

	protected Cache getCache(String cacheName) {
		Cache cache = _cacheManager.getCache(cacheName);
		assertNotNull("cache name " + cacheName + " not defined", cache);
		return cache;
	}

	protected CacheManager getCacheManager() {
		return _cacheManager;
	}

}
