package hr.fer.zemris.java.hw05.observer1;

/**
 * Interface defining the observer type objects.
 * 
 * @author Lovro Marković
 *
 */
public interface IntegerStorageObserver {
	
	/**
	 * Method which every observer object has to implement. This method will be 
	 * called each time the observed subject changes and execute certain commands.
	 * 
	 * @param istorage IntegerStorage object which is being observed.
	 */
	public void valueChanged(IntegerStorage istorage);
}
