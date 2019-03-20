package hr.fer.zemris.java.hw05.observer1;

/**
 * Porgram which demonstrates the use of observers.
 * 
 * @author Lovro Marković
 *
 */
public class ObserverExample {

	/**
	 * Main method of the program. Executed when the program is ran.
	 * 
	 * @param args Does not accept any arguments.
	 */
	public static void main(String[] args) {
		
		IntegerStorage istorage = new IntegerStorage(20);
		IntegerStorageObserver observer = new SquareValue();
		
		istorage.addObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.removeObserver(observer);
		
		istorage.addObserver(new ChangeCounter());
		
		DoubleValue dv = new DoubleValue(1);
		istorage.addObserver(dv);
		istorage.addObserver(new DoubleValue(2));
		IntegerStorageObserver o = new DoubleValue(2);
		istorage.addObserver(o);
		istorage.addObserver(new DoubleValue(0));
		IntegerStorageObserver o1 = new DoubleValue(3);
		istorage.addObserver(o1);
		istorage.addObserver(new DoubleValue(-5));
		
		istorage.setValue(13);
		
		istorage.removeObserver(o1);
		istorage.removeObserver(o);
		
		istorage.setValue(22);
		istorage.setValue(15);
		istorage.setValue(3);
		istorage.setValue(3);
		}
}
