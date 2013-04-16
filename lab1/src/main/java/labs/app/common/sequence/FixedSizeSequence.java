package labs.app.common.sequence;

import labs.app.common.Filler;

/**
 * @deprecated
 * @author vch
 *
 */
public class FixedSizeSequence extends AbstractSequence<Filler> {

	int _sizeInBytes;

	public FixedSizeSequence(int sizeInBytes, int upperBound) {
		super(upperBound);
		_sizeInBytes = sizeInBytes;
	}

	@Override
	Filler create(int inc) {
		return new Filler(new Integer(inc), _sizeInBytes);
	}

}
