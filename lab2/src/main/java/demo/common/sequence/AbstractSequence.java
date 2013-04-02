package demo.common.sequence;


/**
 * Threadsafe instance of Sequence
 * @author vch
 *
 * @param <E>
 */
public abstract class AbstractSequence<E> implements Sequence<E> {
	int _pointer = 0;
	Object _lock = new Object();
	int _upperBound=0;
	
	AbstractSequence(int upperBound){
		_upperBound=upperBound;
	}
	
	abstract E create(int inc) ;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.terracotta.demo.basic.common.Sequence#reset()
	 */
	public void reset() {
		_pointer = 0;
	}
	
	public void remove() {
		throw new UnsupportedOperationException(
				"not required, therefore not implemented");
	}
	public boolean hasNext() {
		return _pointer<_upperBound;
	}

	public E next() {
		return create(inc());
	}

	
	
	int inc() {
		synchronized (_lock) {
			_pointer++;
			return _pointer;
		}
	}

	int currentPointer() {
		return _pointer;
	}
}
