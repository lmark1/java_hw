package hr.fer.zemris.bf.lexer;

/**
 * This enumeration represents a type of token that will be generated by the
 * lexer.
 * 
 * @author Lovro Marković
 *
 */
public enum TokenType {

	/**
	 * End of file that is currently being analysed.
	 */
	EOF,
	
	/**
	 * Type of token representing varibales in expressions.
	 */
	VARIABLE, 
	
	/**
	 * Type of token repersenting constants in the expression.
	 */
	CONSTANT, 
	
	/**
	 * Type of token representing operators in the expression.
	 */
	OPERATOR,
	
	/**
	 * Type of token representing an open bracket in the expression.
	 */
	OPEN_BRACKET,
	
	/**
	 * Type of token representing an closed bracket in the expression.
	 */
	CLOSED_BRACKET
}