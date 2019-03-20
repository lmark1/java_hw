package hr.fer.zemris.java.hw05.observer1;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a simple integer storage wrapper. Will be used for
 * demonstrating observers.
 * 
 * @author Lovro Marković
 *
 */
public class IntegerStorage {

	/**
	 * Integer value that will be stored.
	 */
	private int value;

	/**
	 * List of observers currently monitoring this class instance.
	 */
	private List<IntegerStorageObserver> observers;

	/**
	 * List of all observers that have to be removed.
	 */
	private List<IntegerStorageObserver> removeList;

	/**
	 * Constructor for IntegerStorage objects. Initializes the value variable.
	 * 
	 * @param initialValue
	 *            Initial value of the value class variable.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Method for adding observers for monitoring the current instance of this
	 * class.
	 * 
	 * @param observer
	 *            Observer object that will be added to the internal list.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null is passed as observer argument or if
	 *             the observer is already in the internal list.
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException(
					"Null pointer passed as observer.");
		}

		if (observers == null) {
			observers = new ArrayList<>();
		}

		// If the observer is already in list
		for (IntegerStorageObserver integerStorageObserver : observers) {

			if (integerStorageObserver.equals(observer)) {
				throw new IllegalArgumentException(
						"Observer is already in the internal list");
			}
		}

		observers.add(observer);
	}

	/**
	 * Method for removing observers from the internal observer list. Places
	 * given observer in the remove queue. The values will be removed when the
	 * next change occurs.
	 * 
	 * @param observer
	 *            Observer that will be removed.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null is passed as observer argument
	 */
	public void removeObserver(IntegerStorageObserver observer) {

		if (observer == null) {
			throw new IllegalArgumentException(
					"Null pointer passed as observer.");
		}

		if (removeList == null) {
			removeList = new ArrayList<>();
		}

		removeList.add(observer);
	}

	/**
	 * Removes all current observers from the internal observer list.
	 */
	public void clearObservers() {

		// In case an observer wants to clear the list
		for (IntegerStorageObserver observer : observers) {
			removeObserver(observer);
		}
	}

	/**
	 * Getter for the currently stored int value in this intance.
	 * 
	 * @return Stored int value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Setter for modifiying the currently stored int value in this instance.
	 * 
	 * @param value
	 *            New value of the "value" variable.
	 */
	public void setValue(int value) {

		// Only if new value is different than the current value:
		if (this.value != value) {

			// Update current value
			this.value = value;

			// Notify all registered observers
			if (observers != null) {

				updateObservers();
				for (IntegerStorageObserver observer : observers) {
					observer.valueChanged(this);
				}
			}
		}
	}

	/**
	 * Method that removes all observers in the removeList from observers List.
	 * Also clears the removeList.
	 * 
	 */
	private void updateObservers() {

		if (removeList == null || removeList.isEmpty()) {
			return;
		}

		for (IntegerStorageObserver observer : removeList) {
			observers.remove(observer);
		}

		removeList.clear();
	}

}
