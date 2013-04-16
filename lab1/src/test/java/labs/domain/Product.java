package labs.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable{
	private static final long serialVersionUID = 1L;
	String _id;
	double _price;
	String _name;
	String _type;
	List<Merchant> _merchants = new ArrayList<Merchant>();

	public Product(String id, double price, String name) {
		_id = id;
		_price = price;
		_name = name;
	}
	public Product(String id, double price, String name,String[] merchantNames) {
		this(id,price,name);
		for (int i = 0; i < merchantNames.length; i++) {
			String string = merchantNames[i];
			_merchants.add(new Merchant(""+i,string+"a"));
			_merchants.add(new Merchant(""+(i+1),string+"b"));
		}
	}

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		_id = id;
	}

	public double getPrice() {
		return _price;
	}

	public void setPrice(double price) {
		_price = price;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	public List<Merchant> getMerchants() {
		return _merchants;
	}

	public void setMerchants(List<Merchant> merchants) {
		_merchants = merchants;
	}

	@Override
	public String toString() {
		return "product with id["+getId()+"]";
	}
}
