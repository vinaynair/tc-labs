package labs.lab8;

import labs.common.Util;
import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetThread extends Thread {
	Logger LOG = LoggerFactory.getLogger(GetThread.class);
	private Cache _cache;
	private int _numberOfReads;

	public GetThread(Cache cache,int numberOfReads) {
		_cache = cache;
	}

	public void run() {

		for (int i = 0; i < _numberOfReads; i++) {
			Util.sleepFor(1000);
			getProductFromCache(_cache, "productid-" + i);
		}
	}

	void getProductFromCache(Cache products, String key) {
		LOG.info(_cache.getName()+"---got key[" + key + "] value[" + products.get(key));

	}

}
