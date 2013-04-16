package labs.app.common.sequence;

import labs.app.common.Filler;
import net.sf.ehcache.Element;

public class FixedSizeElementSequence extends AbstractSequence<Element> {
	int _sizeInBytes;

	public FixedSizeElementSequence(int upperBound, int sizeInBytes) {
		super(upperBound);
		_sizeInBytes = sizeInBytes;
	}

	@Override
	Element create(int inc) {
		return new Element(new Integer(inc), new Filler(new Integer(inc),
				_sizeInBytes));
	}

}
