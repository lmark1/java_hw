package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Various types of tokens for SmartLexer.
 * 
 * @author Lovro Marković
 *
 */
public enum SmartTokenType {

	/**
	 * Double constant token.
	 */
	CONSTANT_DOUBLE,

	/**
	 * Integer constant token.
	 */
	CONSTANT_INTEGER,

	/**
	 * Function token. Function starts with @, after which follows a letter and
	 * after than can follow zero or more letters, digits or underscores.
	 */
	FUNCTION,

	/**
	 * Math operator token. Valid operators are '+', '-', '/', '^'.
	 */
	OPERATOR,

	/**
	 * String token. Accepts following escapes: '\\', '\"', '\n', '\r', '\t'.
	 */
	STRING,

	/**
	 * Text token. Only supports '\\' and '\{' escapes.
	 */
	TEXT,

	/**
	 * Valid variable name starts with letter and after follows zero or more letters, digits or underscores.
	 */
	VARIABLE,
	
	/**
	 * Type assigned to a token when no more tokens are left to process.
	 */
	EOF,

	/**
	 * Defines start of a tag '{$'.
	 */
	START_TAG,

	/**
	 * Defines end of a tag '$}'
	 */
	END_TAG,
	
	/**
	 * Defines an empty tag '=' 
	 */
	EQUALS,
	
	/**
	 * Defines non - empty FOR tag
	 */
	FOR, 
	
	/**
	 * Defines an end for non - empty tags
	 */
	END
}
