package demo.ehcache.spring.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private List<Order> orders = new ArrayList<Order>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "id:" + id + ", name:" + name+", order.count="+orders.size();
	}
}
