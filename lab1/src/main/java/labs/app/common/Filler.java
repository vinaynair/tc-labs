package labs.app.common;

import java.io.Serializable;

public class Filler implements Serializable {
	private static final long serialVersionUID = 6403532302225969639L;
	Object _id;
	Object _filler;

	public Filler(Object id, int sizeInBytes) {
		this(sizeInBytes);
		_id = id;

	}

	public Filler(int sizeInBytes) {
		_filler = new byte[sizeInBytes];
	}
}
