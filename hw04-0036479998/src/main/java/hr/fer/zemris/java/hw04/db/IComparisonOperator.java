package hr.fer.zemris.java.hw04.db;

/**
 * Interfice which defines the comparison operator.
 * 
 * @author Lovro Marković
 *
 */
public interface IComparisonOperator {

	/**
	 * Method which defines the comparison operation between 2 given strings.
	 * 
	 * @param value1 First string given.
	 * @param value2 Second string given.
	 * @return Result of a certain operation between given Strings.
	 */
	public boolean satisfied(String value1, String value2);
}
