package hr.fer.zemris.java.hw05.demo2;

import java.util.Iterator;

/**
 * Class which represents a collection of first n prime numbers. Where n is a
 * number given through the constructor.
 * 
 * @author Lovro Marković
 *
 */
public class PrimesCollection implements Iterable<Integer> {

	/**
	 * Number of prime numbers that will be calculated.
	 */
	private int primeMaxCount;

	/**
	 * Constructor for the PrimesCollection object. Initializes primeCount
	 * variable which indicates how many prime numbers will be in the
	 * collection.
	 * 
	 * @param primeMaxCount
	 *            Number of prime numbers in the collection.
	 */
	public PrimesCollection(int primeMaxCount) {
		this.primeMaxCount = primeMaxCount;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PrimeIterator();
	}

	/**
	 * Class implementing an iterator for Primes Collection.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private class PrimeIterator implements Iterator<Integer> {

		/**
		 * Current number of primes generated.
		 */
		private int primeCount;

		/**
		 * Current prime number generated.
		 */
		private int currentPrime;

		/**
		 * Default constructor for the PrimeIterator class. Initializes the
		 * class variables.
		 */
		public PrimeIterator() {
			this.primeCount = 0;
			this.currentPrime = 1;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * <p>
		 * Checks if there are more prime numbers left in the PrimesCollection
		 * </p>
		 */
		@Override
		public boolean hasNext() {
			return primeCount < primeMaxCount;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * <p>Returns the next prime number.</p>
		 */
		@Override
		public Integer next() {
			
			while(true) {
				currentPrime++;
				if (isPrime()) break;
			}
			
			primeCount++;
			return currentPrime;
		}
		
		/**
		 * Checks if currentPrime is in fact a prime number.
		 * 
		 * @return True if currentPrime is a prime number, otherwise false.
		 */
		private boolean isPrime() {
			
			if (currentPrime == 2) return true;
			if (currentPrime % 2 == 0) return false;
			
			for (int i = 3; i*i <= currentPrime; i+=2) {
				
				if (currentPrime % i == 0) return false;
			}
			
			return true;
		}

	}
}
