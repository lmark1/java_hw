package hr.fer.zemris.bf.parser;

/**
 * This class represents an exception that can occur while parsing strings.
 * 
 * @author Lovro Marković
 *
 */
public class ParserException extends RuntimeException {

	/**
	 * Serial number of exception.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ParserException() {
	}

	/**
	 * Constructor expects a message explaining cause of exception.
	 * 
	 * @param message
	 *            Message type String.
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Constructor expects a cause of exception.
	 * 
	 * @param cause
	 *            Cause of exception type Throwable.
	 */
	public ParserException(Throwable cause) {
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
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
