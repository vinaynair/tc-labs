package labs.app.common.sequence;

import java.util.Iterator;

/**
 * Iterator with reset function
 * @author vch
 *
 * @param <E>
 */
public interface Sequence<E> extends Iterator<E>{
	void reset();
}
