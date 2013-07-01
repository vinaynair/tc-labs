package demo.ehcache.spring.model;

import java.io.Serializable;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String productName;
	private int quantity;
	private String status;

	public Order() {

	}

	public Order(String id, String productName, int quantity, String status) {
		this.id = id;
		this.productName = productName;
		this.quantity = quantity;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "id:" + id + ", name:" + productName;
	}
}
