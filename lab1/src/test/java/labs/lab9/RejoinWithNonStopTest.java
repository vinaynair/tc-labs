package labs.lab9;

import labs.common.AbstractCacheTestScenarioSupport;
import labs.domain.Product;
import labs.lab8.GetThread;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class RejoinWithNonStopTest extends AbstractCacheTestScenarioSupport {

	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab9-rejoin-ehcache.xml");
		usesCache("productCacheWithNonStopAndRejoin");

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
		Cache productsWithNonStopAndRejoin = getCache("productCacheWithNonStopAndRejoin");
		populateProductsAndOrders(productsWithNonStopAndRejoin);
		Thread getThreadWithNonStopCacheAndRejoin = new GetThread(productsWithNonStopAndRejoin,20);
		getThreadWithNonStopCacheAndRejoin.start();
		
		System.out.println("Close TSA & press Enter & watch the gets");
		System.in.read();
		System.out.println("closing get threads");
		// lazy to use the right way to stop a
		// thread
		getThreadWithNonStopCacheAndRejoin.stop();


	}

}
