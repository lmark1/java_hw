package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Model of one instance of a token from query. Tokens are generated from the input query.
 * Spaces and tabs do not generate tokens. 
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
	 * Constructor for instance of class Token. Initializes token type and value type Object.
	 * 
	 * @param type TokenType variable representing type of token.
	 * @param value Value of token.
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
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Gets current type of token. 
	 * 
	 * @return Type of token.
	 */
	public TokenType getType() {
		return this.type;
	}
	
}

