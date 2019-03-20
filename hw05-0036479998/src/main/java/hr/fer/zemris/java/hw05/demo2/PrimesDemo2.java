package hr.fer.zemris.java.hw05.demo2;

/**
 * Second demonstrative program for {@link PrimesCollection} class.
 * 
 * @author Lovro Marković
 *
 */
public class PrimesDemo2 {

	/**
	 * Main method of the program. Executes when program starts.
	 * 
	 * @param args Does not accept any arguments.
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5);
		
		for(Integer prime : primesCollection) {
	
			for(Integer prime2 : primesCollection) {
	
				System.out.println("Got prime pair: "+prime+", "+prime2);
	
			}
		}		
	}
}
