package labs.lab6a;


import labs.common.AbstractCacheTestScenarioSupport;
import labs.domain.Product;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.aggregator.Aggregators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollectionsSearchTest extends AbstractCacheTestScenarioSupport {

	@Before
	public void setupEnv() {
		usesEhcacheConfig("lab6a-ehcache.xml");
		usesCache("productCache");
		
	}

	public void populateProductsAndOrders(Cache products) {
		LOG.info("populating products");
		for (int i = 0; i < 10000; i++) {
			Product p = new Product("productid-" + i, i, "product-name-" + i,
					new String[] { "merchanta-" + i, "merchantb" + i });
			products.put(new Element(p.getId(), p));
		}
	}

	@Test
	public void findAllProductsWithSpecificMerchant() {
		Cache products = getCache("productCache");
		populateProductsAndOrders(products);

		Attribute<String> merchant = products.getSearchAttribute("merchants");
		Query merchantQuery = products.createQuery()
				.addCriteria(merchant.ilike("*merchant*"))
				.addOrderBy(merchant, Direction.ASCENDING).includeValues()
				.includeAggregator(Aggregators.count());
		;
		Results matchedProducts = merchantQuery.execute();
		for (Result result : matchedProducts.all()) {
			Object o = result.getValue();
			LOG.debug(o.toString());
		}
		Assert.assertEquals("total search results must be same as cache size in our case",1000,
				 matchedProducts.all().get(0).getAggregatorResults().get(0));
	}

}
