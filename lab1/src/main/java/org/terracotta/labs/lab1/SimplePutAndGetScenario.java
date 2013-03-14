package org.terracotta.labs.lab1;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePutAndGetScenario {
	private static Logger LOG = LoggerFactory.getLogger(SimplePutAndGetScenario.class);
	public static void main(String[] args) throws Exception {
		SimplePutAndGetScenario app = new SimplePutAndGetScenario();
		app.get();//try getting first
		app.put();//then put
		app.get();//then get again
		app.cleanup();
		//app.waitForUserInput();
	}
	void cleanup(){
		getCache("customerCache").flush();
		getCache("productCache").flush();
		CacheManager.getInstance().shutdown();
	}
	public void waitForUserInput() throws Exception {
		System.out.println("Enter to exit!");
		System.in.read();
	}

	private Cache getCache(String cacheName) {
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCache(cacheName);
		assert cache != null;
		return cache;
	}

	public void put() {
		Cache customerCache = getCache("customerCache");
		customerCache.put(new Element("id123", "{name:'abc def',age:35}"));
		LOG.info("put customer");
		
		Cache productCache = getCache("productCache");
		productCache.put(new Element("id456", "{name:'coke',expiry:'01-02-2013'}"));
		LOG.info("put product");
	}
	public void get(){
		LOG.info("getting customer id123");
		Cache customerCache = getCache("customerCache");
		Object customer=customerCache.get("id123");
		LOG.info("got customer ["+customer+"]");
		
		LOG.info("getting product id456");
		Cache productCache = getCache("productCache");
		Object product=productCache.get("id456");
		LOG.info("got product ["+product+"]");
	}

}
