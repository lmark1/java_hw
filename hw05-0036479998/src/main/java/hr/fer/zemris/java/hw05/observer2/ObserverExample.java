package hr.fer.zemris.java.hw05.observer2;

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
		
		//All observer references registered at the start of program
		istorage.addObserver(observer);
		istorage.addObserver(new DoubleValue(3));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(0));
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(-5));
		istorage.addObserver(new ChangeCounter());
	
		istorage.setValue(5);
		System.out.println();
		
		istorage.setValue(2);
		System.out.println();
		
		istorage.setValue(25);
		System.out.println();
		
		istorage.setValue(13);
		System.out.println();
		
		istorage.setValue(22);
		System.out.println();
		
		istorage.setValue(15);
		System.out.println();
		
		istorage.setValue(3);
		System.out.println();
		
		istorage.setValue(3);
		}
}
