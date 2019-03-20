package hr.fer.zemris.java.hw05.observer2;

/**
 * Class which encapsulates the subject - IntegerStorage object, and its
 * previous and current stored integer values.
 * 
 * @author Lovor Marković
 *
 */
public class IntegerStorageChange {

	/**
	 * Reference to the integer storage object.
	 */
	private IntegerStorage istorage;

	/**
	 * Value of a stored integer before any change has occured.
	 */
	private int valueBeforeChange;

	/**
	 * New value of the currently stored integer.
	 */
	private int currentValue;

	/**
	 * Constructor for the IntegerStorageChange objects. Initializes all
	 * variables using given IntegerStorage object.
	 * 
	 * @param istorage
	 *            IntegerStorage object used for initialization.
	 * @param oldValue
	 *            Old value of the integer storage object.
	 */
	public IntegerStorageChange(IntegerStorage istorage, int oldValue) {
		this.istorage = istorage;
		this.valueBeforeChange = oldValue;
		this.currentValue = istorage.getValue();
	}

	/**
	 * Getter for the currentValue variable.
	 * 
	 * @return currentValue variable.
	 */
	public int getCurrentValue() {
		return currentValue;
	}

	/**
	 * Getter for the IntegerStorage reference.
	 * 
	 * @return IntegerStorage refernce.
	 */
	public IntegerStorage getIstorage() {
		return istorage;
	}

	/**
	 * Getter for the integer value before change.
	 * 
	 * @return Integer value before change.
	 */
	public int getValueBeforeChange() {
		return valueBeforeChange;
	}
}
