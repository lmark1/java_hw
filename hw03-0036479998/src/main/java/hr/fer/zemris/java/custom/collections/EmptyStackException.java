package hr.fer.zemris.java.custom.collections;

/**
 * This class implements an exception. Exception is thrown when we try removing
 * elements from {@link ObjectStack} class instance while the stack is empty.
 * 
 * @author Lovro Marković
 *
 */
public class EmptyStackException extends RuntimeException {

	/**
	 * Serial number of exception.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public EmptyStackException() {
	}

	/**
	 * Constructor expects a message explaining cause of exception.
	 * 
	 * @param message
	 *            Message type String.
	 */
	public EmptyStackException(String message) {
		super(message);
	}

	/**
	 * Constructor expects a cause of exception.
	 * 
	 * @param cause
	 *            Cause of exception type Throwable.
	 */
	public EmptyStackException(Throwable cause) {
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
	public EmptyStackException(String message, Throwable cause) {
		super(message, cause);
	}
}
