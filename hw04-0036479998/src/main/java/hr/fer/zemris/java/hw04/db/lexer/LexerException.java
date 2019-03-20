package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Lexer exception thrown if lexer fails to get new tokens.
 * 
 * @author Lovro Marković
 *
 */
public class LexerException extends RuntimeException{

	/**
	 * Serial number of exception.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public LexerException() {
	}

	/**
	 * Constructor expects a message explaining cause of exception.
	 * 
	 * @param message
	 *            Message type String.
	 */
	public LexerException(String message) {
		super(message);
	}

	/**
	 * Constructor expects a cause of exception.
	 * 
	 * @param cause
	 *            Cause of exception type Throwable.
	 */
	public LexerException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor expects both a message and a cause of exception.
	 * 
	 * @param message
	 *            Message type String.
	 * @param cause
	 *            Cause of exception type Throwable.
	 */
	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}
}
