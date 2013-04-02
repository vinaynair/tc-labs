package demo.common.sequence;


public class IntegerSequence extends AbstractSequence<Integer> {
	public IntegerSequence(int upperBound){
		super(upperBound);
	}

	@Override
	Integer create(int inc) {
		// TODO Auto-generated method stub
		return new Integer(inc);
	}
	

	

}
