package hr.fer.zemris.java.hw05.observer1;

/**
 * This class acts as an observer to the {@link IntegerStorage} objects. It
 * counts how many times the observed object value has been changed.
 * 
 * @author Lovro Marković
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/**
	 * Counts the number of times the observed value has been changed since the
	 * registration of this observer.
	 */
	private int changeCounter = 0;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Method counts the number of times the stored value has been changed.
	 * </p>
	 */
	@Override
	public void valueChanged(IntegerStorage istorage) {
		changeCounter++;
		System.out.format("Number of value changes since tracking: %d%n",
				changeCounter);
	}

}
