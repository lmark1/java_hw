package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Model of one instance of a token from input. Tokens are generated from the input string.
 * Spaces and tabs do not generate tokens. 
 * 
 * @author Lovro Marković
 *
 */
public class SmartToken {
	
	/**
	 * Type of the currently instanced token.
	 */
	private SmartTokenType type;
	
	/**
	 * Value of the instanced token.
	 */
	private Object value;
	
	/**
	 * Constructor for instance of class Token. Initializes token type and value type Object.
	 * 
	 * @param type TokenType variable representing type of token.
	 * @param value Value of token.
	 */
	public SmartToken(SmartTokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Gets current token value.
	 * 
	 * @return Value of token.
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Gets current type of token. 
	 * 
	 * @return Type of token.
	 */
	public SmartTokenType getType() {
		return this.type;
	}
	
}
