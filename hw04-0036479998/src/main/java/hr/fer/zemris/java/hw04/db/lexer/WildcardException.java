package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Wildcard exception thrown if too many wildcard charaters are found.
 * 
 * @author Lovro Marković
 *
 */
public class WildcardException extends RuntimeException{

	/**
	 * Serial number of exception.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public WildcardException() {
	}

	/**
	 * Constructor expects a message explaining cause of exception.
	 * 
	 * @param message
	 *            Message type String.
	 */
	public WildcardException(String message) {
		super(message);
	}

	/**
	 * Constructor expects a cause of exception.
	 * 
	 * @param cause
	 *            Cause of exception type Throwable.
	 */
	public WildcardException(Throwable cause) {
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
	public WildcardException(String message, Throwable cause) {
		super(message, cause);
	}
}

