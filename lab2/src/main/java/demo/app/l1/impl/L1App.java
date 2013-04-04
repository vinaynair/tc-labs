package demo.app.l1.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.terracotta.TerracottaBootstrapCacheLoader;
import demo.app.Config;
import demo.app.l1.RemoteCacheApi;
import demo.common.Filler;

/**
 * Update and read the value within the filler entry
 * 
 * @author vch
 * 
 */
public class L1App implements RemoteCacheApi {

	Config _config;
	Cache _cache;
	Cache _bootStrappedCache;

	public L1App() {
		_config = new Config();// load configuration
		_cache = _config.getCache();
	}

	public boolean setCache(String cacheName) {
		Cache cache = _config.getCache(cacheName);
		if (cache != null) {
			_cache = cache;
			return true;
		} else
			return false;
	}
	
	public void shutdown(){
		_cache.getCacheManager().shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see demo.app.l1.RemoteCacheApi#read(java.lang.String)
	 */
	public String read(String key) {
		Element e = _cache.get(key);
		if (e == null)
			return null;
		Filler filler = (Filler) e.getObjectValue();
		return filler.getValue().toString();
	}

	public boolean update(String key, String value) {
		Filler filler = new Filler(Integer.parseInt(key), Config.SIZE_OF_ENTRY,
				value);
		_cache.put(new Element(key, filler));
		return true;
	}
}
