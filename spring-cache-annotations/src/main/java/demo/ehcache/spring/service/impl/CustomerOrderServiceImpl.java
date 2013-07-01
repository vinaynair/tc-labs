package demo.ehcache.spring.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import demo.ehcache.spring.model.Customer;
import demo.ehcache.spring.model.Order;
import demo.ehcache.spring.service.CustomerOrderService;

@Path("/order/")
public class CustomerOrderServiceImpl implements CustomerOrderService {

	private Map<String, Customer> customerAndOrders = new HashMap<String, Customer>();

	@GET
	@Path("/customer/{customerID}")
	@Produces("application/json")
	@Cacheable(value = "customerAndOrders", key = "#customerID",unless="#result == null")
	public Customer getAllOrders(@PathParam("customerID") String customerID) {
		System.out.println("calling getAllOrders(" + customerID + ")");
		return customerAndOrders.get(customerID);

	}

	@PUT
	@Path("/customer/{customerID}")
	@Consumes("application/json")
	@Produces("application/json")
	@CachePut(value = "customerAndOrders", key = "#customerID")
	@CacheEvict(value = "allRestJsonResponses", key = "'/rest/services/order/customer/'+#customerID", beforeInvocation = false)
	//@CacheEvict(value = "allRestJsonResponses",allEntries=true)
	public Customer addOrder(@PathParam("customerID") String customerID,
			Order order) {
		System.out
				.println("calling addOrder(" + customerID + "," + order + ")");
		Customer customer = customerAndOrders.get(customerID);
		if (customer == null) {
			throw new RuntimeException("Customer not found");
		}
		customer.getOrders().add(order);
		return customer;
	}

	/**
	 * Lazy, therefore i have setup a helper method to populate the component
	 * through spring bean config
	 * 
	 * @param customers
	 */
	public void setCustomerAndOrders(List<Customer> customers) {
		for (Customer customer : customers) {
			customerAndOrders.put(customer.getId(), customer);
		}
		printAllCustomers();
	}

	private void printAllCustomers() {
		for (String customerID : customerAndOrders.keySet()) {
			System.out.println("customer with id " + customerID
					+ ", with value = " + customerAndOrders.get(customerID));
		}
	}

	

}
