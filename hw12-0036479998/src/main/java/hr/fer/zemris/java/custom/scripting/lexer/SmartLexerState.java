package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Defines states of smart lexer.
 * 
 * @author Lovro Marković
 *
 */

public enum SmartLexerState {

	/**
	 * Lexer is processing text data.
	 */
	TEXT,
	
	/**
	 * Lexer is processing tag data.
	 */
	TAG
}
