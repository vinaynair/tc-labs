package demo.ehcache.bootstrap.model;

import java.io.Serializable;

public class Filler implements Serializable {
	public static int _1MB = 1000 * 1000;
	public static int _1KB = 1000;
	private static final long serialVersionUID = 1L;
	Object _id;
	Object _filler;
	Object _value;

	public Filler(Object id, int sizeInBytes, String value) {
		this(id, sizeInBytes);
		_value = value;

	}

	public Filler(Object id, int sizeInBytes) {
		this(sizeInBytes);
		_id = id;

	}

	public Filler(int sizeInBytes) {
		_filler = new byte[sizeInBytes];
	}

	public Object getId() {
		return _id;
	}

	public Object getValue() {
		return _value;
	}

	public void setValue(Object newValue) {
		_value = newValue;
	}
}
