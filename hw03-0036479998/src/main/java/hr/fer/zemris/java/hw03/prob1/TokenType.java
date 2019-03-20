package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeration that defines various types of {@link Token} data.
 * 
 * @author Lovro Marković
 *
 */
public enum TokenType {

	/**
	 * Type assigned to a token when no more tokens are left to process.
	 */
	EOF,

	/**
	 * Array of one or more characters for which method
	 * {@link Character#isLetter(char)} return true.
	 */
	WORD,

	/**
	 * Array of one or more numbers that can be cast as type Long.
	 */
	NUMBER,

	/**
	 * All characters that are left in the input when enums type WORD, NUMBER and
	 * all empty spaces('\r', '\n', '\t', ' ') are removed.
	 */
	SYMBOL
}
