package labs.lab1;

import labs.common.AbstractCacheTestScenarioSupport;
import labs.domain.Product;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplePutAndGetScenarioTest extends
		AbstractCacheTestScenarioSupport {

	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab1-ehcache.xml");
		usesCache("customerCache");
		usesCache("productCache");
	}

	@Test
	public void testGetPutAndThenGetAgain() {
		get();
		put();
		get();
	}

	@Test
	public void testReferenceWhileOnHeap() {
		Cache cache = getCache("productCacheOnHeap");
		Product p = new Product("1", 10D, "abc");
		cache.put(new Element(1, p));
		p.setName("def");

		Product cachedProduct = (Product) cache.get(1).getObjectValue();
		Assert.assertEquals(cachedProduct, p);
		p.setName("def");
		p = (Product) cache.get(1).getObjectValue();
		Assert.assertEquals(
				"while object was on heap , we have a pure object reference",
				"def", p.getName());
	}

	@Test
	public void testReferenceWhileOffHeap() {
		Cache cache = getCache("productCacheOffHeap");
		Product p = new Product("1", 10D, "abc");
		cache.put(new Element(1, p));
		cache.put(new Element(2, p));
		cache.put(new Element(3, p));
		p.setName("def");

		cache.evictExpiredElements();
		// find which one went off-heap
		int offHeapKey = -1;
		if (cache.isElementOffHeap(1))
			offHeapKey = 1;
		else if (cache.isElementOffHeap(2))
			offHeapKey = 2;
		else if (cache.isElementOffHeap(3))
			offHeapKey = 3;

		Assert.assertTrue("somebody must have went off-heap", offHeapKey != -1);
		
		Product cachedProduct = (Product) cache.get(offHeapKey)
				.getObjectValue();
		Assert.assertTrue(
				"after I perform get on someone off-heap, i come on-heap",
				cache.isElementInMemory(offHeapKey));
		Assert.assertNotSame("offheap is serialized copy and not a reference",
				p.getName(), cachedProduct.getName());

		p.setName("def");
		p = (Product) cache.get(offHeapKey).getObjectValue();
		Assert.assertNotSame(
				"while object was on heap , we have a pure object reference",
				"def", p.getName());
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
