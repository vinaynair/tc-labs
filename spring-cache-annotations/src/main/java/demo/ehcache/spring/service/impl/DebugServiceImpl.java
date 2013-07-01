package demo.ehcache.spring.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import demo.ehcache.spring.model.CacheContents;
import demo.ehcache.spring.service.DebugService;
@Path("/debug/")
public class DebugServiceImpl implements DebugService {

	@GET
	@Path("/cache")
	@Produces("application/json")
	public List<CacheContents> getCacheDetails() {
		List<CacheContents> cacheContents = new ArrayList<CacheContents>();
		CacheManager cacheManager = CacheManager.getInstance();
		String[] cacheNames = cacheManager.getCacheNames();
		for (int i = 0; i < cacheNames.length; i++) {
			Ehcache cache = cacheManager.getEhcache(cacheNames[i]);
			CacheContents cacheContent = new CacheContents();
			cacheContents.add(cacheContent);
			cacheContent.setName(cacheNames[i]);
			if (cache == null)
				continue;
			System.out.println("*****" + cache.getStatistics() + ", put count="
					+ cache.getLiveCacheStatistics().getPutCount());
			List keys = cache.getKeys();
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				Object key = (Object) iterator.next();
				Object value = cache.getQuiet(key).getObjectValue();
				System.out.println("cache[" + cache.getName() + "] key[" + key
						+ "]value[" + value + "]");
				cacheContent.getKeyValues().put(key.toString(),
						value.toString());
			}
			
		}
		return cacheContents;
	}

}
