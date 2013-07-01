package demo.ehcache.spring.service;

import demo.ehcache.spring.model.Customer;
import demo.ehcache.spring.model.Order;

public interface CustomerOrderService {
	Customer getAllOrders(String customerID);

	Customer addOrder(String customerID,Order order);
	
	

}
