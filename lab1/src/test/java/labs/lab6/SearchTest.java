package labs.lab6;

import static junit.framework.Assert.assertEquals;
import labs.common.AbstractCacheTestScenarioSupport;
import labs.domain.Product;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.aggregator.Aggregators;

import org.junit.Before;
import org.junit.Test;

public class SearchTest extends AbstractCacheTestScenarioSupport {

	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab6-ehcache.xml");
		usesCache("productCache");
	}

	public void populateProductsAndOrders(Cache products) {
		LOG.info("populating products");
		for (int i = 0; i < 10000; i++) {
			Product p = new Product("productid-" + i, i, "product-name-" + i);
			products.put(new Element(p.getId(), p));
		}
	}

	@Test
	public void findAllProductsGreaterThan10$() {
		Cache products = getCache("productCache");
		populateProductsAndOrders(products);

		Attribute<Object> price = products.getSearchAttribute("price");
		// REMEMBER: query criteria is checked for type-safety with actual
		// javabean stored within the cache only RUNTIME. Example price is
		// double, passing
		// int will fail only at runtime.
		// TODO does it support querying for beans without a specific property
		// set? ( mongo does this, right )
		// TODO isn't BM sparse (like hbase)? must be given that its storing
		// objects and null property doesnt incur an storage
		Query priceQuery = products.createQuery().addCriteria(price.gt(10.00))
				.includeAggregator(Aggregators.count());
		Results matchedProducts = priceQuery.execute();
		// TODO where are query results stored( heap, offheap)
		// TODO where is query run? l1/l2?
		// TODO can we get partial data / object graph - select product.price
		// from product - back?
		assertEquals("should have 10 products that match price > 10$ criteria",
				9989, matchedProducts.all().get(0).getAggregatorResults()
						.get(0));
	}

	/**
	 * maybe not ideal
	 */
	@Test
	public void constructSearchableProgramaticallyAndThenFindProductsWithASpecificName() {
		Cache products = getCache("productCache");
		// create a new cache - lazy starting from an existing definition
		CacheConfiguration config = products.getCacheConfiguration();
		config.setName("productCache2");
		// adding searchattribute for product
		SearchAttribute nameSearchAttribute = new SearchAttribute();
		nameSearchAttribute.setName("name");
		config.getSearchable().addSearchAttribute(nameSearchAttribute);
		products = new Cache(config);
		getCacheManager().addCache(products);// adding new cache to the manager

		// load data into this new cache
		populateProductsAndOrders(products);
		// query
		Attribute<Object> name = products.getSearchAttribute("name");
		Results matchedNames = products.createQuery()
				.addCriteria(name.eq("product-name-10")).includeValues()
				.execute();
		// TODO is this the right way to find the count. What about aggregators?
		assertEquals(1, matchedNames.size());
	}

	// TODO
	public void timeItTakesToCreateSearchIndex() {

	}

	// TODO
	public void searchOnDistributedCache() {

	}

	// TODO
	public void aggregateQueries() {

	}

}
