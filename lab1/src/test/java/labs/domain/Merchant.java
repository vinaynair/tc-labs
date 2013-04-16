package labs.domain;

import java.io.Serializable;

public class Merchant implements Serializable{
	private static final long serialVersionUID = 1L;
	String _id;
	String _name;
	
	public Merchant(String id,String name){
		setId(id);
		setName(name);
	}
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		_id = id;
	}
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
}
