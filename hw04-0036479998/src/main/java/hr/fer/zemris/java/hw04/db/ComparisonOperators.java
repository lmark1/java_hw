package hr.fer.zemris.java.hw04.db;

/**
 * Class which defines operators and their functions.
 * 
 * @author Lovro Marković
 *
 */
public class ComparisonOperators {

	/**
	 * Less than operator. Returns true if value1 < string 2, otherwise false.
	 */
	public static final IComparisonOperator LESS = (String value1, String value2) -> value1.compareTo(value2) < 0;

	/**
	 * Less than or equals operator. Returns true if value1 <= value2, otherwise
	 * false.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (String value1,
			String value2) -> value1.compareTo(value2) <= 0;

	/**
	 * Greater than operator. Returns true if value1 > value2, otherwise false.
	 */
	public static final IComparisonOperator GREATER = (String value1, String value2) -> value1.compareTo(value2) > 0;

	/**
	 * Greater than or equals operator. Returns true if value1 >= value2,
	 * otherwise false.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (String value1,
			String value2) -> value1.compareTo(value2) >= 0;

	/**
	 * Equals operator. Returns true if value1 = value2, otherwise false.
	 */
	public static final IComparisonOperator EQUALS = (String value1, String value2) -> value1.compareTo(value2) == 0;

	/**
	 * Not equals operator. Returns true if value1 != value2, otherwise false.
	 */
	public static final IComparisonOperator NOT_EQUALS = (String value1,
			String value2) -> value1.compareTo(value2) != 0;

	/**
	 * Like operator. Value2 is a string which contains wildcard (*) characters.
	 * If there is more than 1 wildcard character an exception will be thrown. A
	 * wildcard character represents any number of letters or numbers at that
	 * position in the string.
	 */
	public static final IComparisonOperator LIKE = new LikeComparison();

	/**
	 * Nested static class implementing {@link IComparisonOperator}.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class LikeComparison implements IComparisonOperator {

		/**
		 * {@inheritDoc}
		 * 
		 * <p>Method checks like conditions. Like condition compares 2 strings
		 * where the second one may have wildcard characters.</p>
		 * 
		 * @throws IllegalArgumentException
		 *             Exception thrown if value2 contains too many wildcard
		 *             characters or if value1 contains any.
		 */
		@Override
		public boolean satisfied(String value1, String value2) {

			if (howManyWildCards(value2) > 1) {
				throw new IllegalArgumentException("String " + value2 + " has too many wildcard characters.");
			}

			if (howManyWildCards(value1) > 0) {
				throw new IllegalArgumentException("First string can't have wildcards");
			}

			value2 = value2.replaceAll("\\*", "\\.*");

			return value1.matches(value2);
		}

		/**
		 * Counts the number of wildcard(*) characters in a string.
		 * 
		 * @param value
		 *            String given for analysis.
		 * @return Number of wildcard characters in a string.
		 */
		private static int howManyWildCards(String value) {
			int wildCardCount = 0;

			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) == '*')
					wildCardCount++;
			}

			return wildCardCount;
		}
	}
}
