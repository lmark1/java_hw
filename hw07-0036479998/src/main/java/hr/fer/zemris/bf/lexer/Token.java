package hr.fer.zemris.bf.lexer;

/**
 * This class represents a token genereated by the lexer class.
 * 
 * @author Lovro Marković
 *
 */
public class Token {
	/**
	 * Type of the currently instanced token.
	 */
	private TokenType type;

	/**
	 * Value of the instanced token.
	 */
	private Object value;

	/**
	 * Constructor for instance of class Token. Initializes token type and value
	 * type Object.
	 * 
	 * @param type
	 *            TokenType variable representing type of token.
	 * @param value
	 *            Value of token.
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Gets current token value.
	 * 
	 * @return Value of token.
	 */
	public Object getTokenValue() {
		return this.value;
	}

	/**
	 * Gets current type of token.
	 * 
	 * @return Type of token.
	 */
	public TokenType getTokenType() {
		return this.type;
	}

	/**
	 * Returns a String representation of a token value.
	 * 
	 * @throws ClassCastException
	 *             Exception thrown if token value can not be cast into any
	 *             known token value type (Character, String or Boolean).
	 */
	public String toString() {
		StringBuilder newString = new StringBuilder();
		
		newString.append("Type: " + type + ", ");
		
		if (value == null) {
			newString.append("Value: null");
			return newString.toString();
		}
		
		newString.append("Value: " + value.toString() + ", ");
		newString.append("Value is instance of: " + value.getClass().getName());
		
		return newString.toString();
	}
}
