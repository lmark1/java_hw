package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumerator defining states of {@link Lexer}.
 * 
 * @author Lovro Marković
 *
 */
public enum LexerState {
	
	/**
	 * Basic state of lexer, parses WORDs, NUMBERs and SYMBOLs including escapes normally.
	 */
	BASIC, 
	
	/**
	 * Extended state of lexer. Starts when '#' appears. Does not generate NUMBER tokens and
	 * parses all chars as WORD. State lasts until '#' appears again. 
	 */
	EXTENDED
}
