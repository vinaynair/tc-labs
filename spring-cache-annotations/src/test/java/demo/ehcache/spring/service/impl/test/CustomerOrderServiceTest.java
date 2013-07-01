package demo.ehcache.spring.service.impl.test;

import junit.framework.Assert;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.ehcache.spring.model.Customer;
import demo.ehcache.spring.model.Order;
import demo.ehcache.spring.service.CustomerOrderService;

public class CustomerOrderServiceTest {
	@Test
	public void testCachingOfComponents() {
		// get beans managed by spring
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"beans.xml");
		// get the accompanying cache
		Cache customerAndOrdersCache = CacheManager.newInstance().getCache(
				"customerAndOrders");
		customerAndOrdersCache.removeAll();

		CustomerOrderService customerOrderService = (CustomerOrderService) context
				.getBean("customerOrderServiceBean");

		// getting the customer should populate the cache
		Customer customer = customerOrderService.getAllOrders("1");
		Assert.assertEquals(0, customer.getOrders().size());

		Customer customerFromCache = (Customer) customerAndOrdersCache.get("1")
				.getObjectValue();
		Assert.assertTrue(customerFromCache != null);
		Assert.assertTrue(customerFromCache.getOrders().size() == 0);

		// addition of an order should update the cache
		// customerOrderService.addOrder(new AddOrderRequest("1", new Order("1",
		// "iphone", 1, "submitted")));
		customerOrderService.addOrder("1", new Order("1", "iphone", 1,
				"submitted"));
		Assert.assertEquals(1, customer.getOrders().size());

		customerFromCache = (Customer) customerAndOrdersCache.get("1")
				.getObjectValue();
		Assert.assertTrue(customerFromCache != null);
		Assert.assertTrue(customerFromCache.getOrders().size() == 1);

	}
}
