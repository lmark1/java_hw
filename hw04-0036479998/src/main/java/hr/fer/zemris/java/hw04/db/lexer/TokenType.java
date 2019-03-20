package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Enumeration for token types in the query sequence.
 * 
 * @author Lovro Marković
 *
 */
public enum TokenType {
	
	/**
	 * Attribute name od the query: firstName, lastName, jmbag. Case sensitive.
	 */
	ATTRIBUTE_NAME,
	
	/**
	 * Operators can be: =, !=, <, <=, >, >=, LIKE.
	 */
	COMPARISON_OPERATOR,
	
	/**
	 * AND operator. Case insensitive.
	 */
	AND_OPERATOR,
	
	/**
	 * Must be written in quotes, quote cannot be written in the string ( no escaping needed ).
	 */
	STRING_LITERAL,
	
	/**
	 * End of file token type.
	 */
	EOF
}