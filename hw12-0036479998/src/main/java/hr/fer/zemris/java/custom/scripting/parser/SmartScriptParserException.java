package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown if any other exception is caught during parsing.
 * 
 * @author Lovro Marković
 *
 */
public class SmartScriptParserException extends RuntimeException{

	/**
	 * Serial number of exception.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SmartScriptParserException() {
	}

	/**
	 * Constructor expects a message explaining cause of exception.
	 * 
	 * @param message
	 *            Message type String.
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}

	/**
	 * Constructor expects a cause of exception.
	 * 
	 * @param cause
	 *            Cause of exception type Throwable.
	 */
	public SmartScriptParserException(Throwable cause) {
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
	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}

}
