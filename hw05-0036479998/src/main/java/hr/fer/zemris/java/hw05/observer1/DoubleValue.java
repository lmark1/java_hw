package hr.fer.zemris.java.hw05.observer1;

/**
 * This class acts as an observer to the {@link IntegerStorage} objects. It
 * prints out doubled updated value to the standard output for the first n times
 * and then it will be deregistered.
 * 
 * @author Lovro Marković
 *
 */
public class DoubleValue implements IntegerStorageObserver {

	/**
	 * Number of times this observer will be able to print out the value before
	 * its deregistered.
	 */
	private int maxChanges;
	
	/**
	 * Number of changes occured since this object is registered.
	 */
	private int changeCounter;	
	
	/**
	 * Constructor for DoubleValue object. Initializes the changeCounter variable.
	 * 
	 * @param maxChanges Initial value of the changecounter variable.
	 */
	public DoubleValue(int maxChanges) {
		
		this.maxChanges = maxChanges;
		changeCounter = 0;
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Prints out doubled updated value to the standard output. After
	 * changeCounter times the observer is deregistered.
	 * </p>
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		changeCounter++;
		
		if (changeCounter > maxChanges) {
			istorage.removeObserver(this);
			changeCounter = 0;
			return;
		}
		
		int value = istorage.getValue();
		System.out.format("Double value: %d%n", 2*value);
	}

}
