package labs.domain;

import java.io.IOException;
import java.io.Serializable;

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	String _name;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public Person(String name) {
		setName(name);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		System.out.println("writing");
		System.out.println("wrote");
		
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		System.out.println("reading");
		System.out.println("read");

	}

}
