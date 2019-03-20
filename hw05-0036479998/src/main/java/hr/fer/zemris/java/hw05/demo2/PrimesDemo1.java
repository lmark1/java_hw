package hr.fer.zemris.java.hw05.demo2;

/**
 * Demonstrative program for {@link PrimesCollection} class.
 * 
 * @author Lovro Marković
 *
 */
public class PrimesDemo1 {
	
	/**
	 * Main method of the program. Executes when program starts.
	 * 
	 * @param args Does not accept any arguments.
	 */
	public static void main(String[] args) {
		
		PrimesCollection primesCollection = new PrimesCollection(6); // 5: how many of them
		
		for(Integer prime : primesCollection) {
			System.out.println("Got prime: "+prime);
		}
	}

}
