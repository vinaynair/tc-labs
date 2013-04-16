package labs.domain;

public class Order {
	 String _id;
	 Product _product;
	 Merchant _merchant;
	 Customer _customer;
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		_id = id;
	}
	public Product getProduct() {
		return _product;
	}
	public void setProduct(Product product) {
		_product = product;
	}
	public Merchant getMerchant() {
		return _merchant;
	}
	public void setMerchant(Merchant merchant) {
		_merchant = merchant;
	}
	public Customer getCustomer() {
		return _customer;
	}
	public void setCustomer(Customer customer) {
		_customer = customer;
	}
}
