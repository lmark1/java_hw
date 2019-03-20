package hr.fer.zemris.java.hw05.observer2;

/**
 * This class acts as an observer to the {@link IntegerStorage} objects. It
 * prints out the new value given to the observed object and its square.
 * 
 * @author Lovro Marković
 *
 */
public class SquareValue implements IntegerStorageObserver {

	/**
	 * {@inheritDoc}
	 * <p>
	 * In this instance, the method will calculate the square of the new value
	 * given to the {@link IntegerStorage} and write an appropriate message to
	 * the standard output.
	 * </p>
	 */
	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		IntegerStorage istorage = istorageChange.getIstorage();
		int value = istorage.getValue();
		
		System.out.format("Provided new value: %d, square is %d%n", value,
				value * value);
	}

}
