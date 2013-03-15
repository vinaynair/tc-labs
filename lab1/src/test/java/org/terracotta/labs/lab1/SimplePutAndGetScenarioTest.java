package org.terracotta.labs.lab1;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.junit.Before;
import org.junit.Test;
import org.terracotta.labs.common.AbstractCacheTestScenarioSupport;

public class SimplePutAndGetScenarioTest extends
		AbstractCacheTestScenarioSupport {

	@Before
	public void setup() {
		usesEhcacheConfig("lab1-ehcache.xml");
		usesCache("customerCache");
	}
	@Test
	public void testGetPutAndThenGetAgain(){
		get();
		put();
		get();
	}
	public void put() {
		Cache customerCache = getCache("customerCache");
		customerCache.put(new Element("id123", "{name:'abc def',age:35}"));
		LOG.info("put customer");

		Cache productCache = getCache("productCache");
		productCache.put(new Element("id456",
				"{name:'coke',expiry:'01-02-2013'}"));
		LOG.info("put product");
	}

	public void get() {
		LOG.info("getting customer id123");
		Cache customerCache = getCache("customerCache");
		Object customer = customerCache.get("id123");
		LOG.info("got customer [" + customer + "]");

		LOG.info("getting product id456");
		Cache productCache = getCache("productCache");
		Object product = productCache.get("id456");
		LOG.info("got product [" + product + "]");
	}
}
