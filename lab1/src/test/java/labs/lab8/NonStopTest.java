package labs.lab8;

import labs.common.AbstractCacheTestScenarioSupport;
import labs.domain.Product;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class NonStopTest extends AbstractCacheTestScenarioSupport {

	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab8-ehcache.xml");
		usesCache("productCacheWithoutNonStop");
		usesCache("productCacheWithNonStop");

	}

	void putProductInCache(Cache products, int i) {
		Product p = new Product("productid-" + i, i, "product-name-" + i,
				new String[] { "merchanta-" + i, "merchantb" + i });
		products.put(new Element(p.getId(), p));

	}

	void populateProductsAndOrders(Cache products) {
		LOG.info("populating products");
		for (int i = 0; i < 10000; i++) {
			putProductInCache(products, i);
		}
	}

	@Test
	@Ignore
	public void putProductsAndSeeNonStopBehavior() throws Exception {
		Cache productsWithNonStop = getCache("productCacheWithNonStop");
		Cache productsWithoutNonStop = getCache("productCacheWithoutNonStop");

		populateProductsAndOrders(productsWithNonStop);
		populateProductsAndOrders(productsWithoutNonStop);
		Thread getThreadWithNonStopCache = new GetThread(productsWithNonStop,20);
		getThreadWithNonStopCache.start();
		Thread getThreadWithoutNonStopCache = new GetThread(
				productsWithoutNonStop,20);
		getThreadWithoutNonStopCache.start();
		System.out.println("Close TSA & press Enter & watch the gets");
		System.in.read();
		System.out.println("closing get threads");
		// lazy to use the right way to stop a
		// thread
		getThreadWithNonStopCache.stop();
		getThreadWithoutNonStopCache.stop();

	}

	public void readFromCache(String key) {

	}

}
