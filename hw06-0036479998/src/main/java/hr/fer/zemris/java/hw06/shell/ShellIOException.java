package hr.fer.zemris.java.hw06.shell;

/**
 * Class implements an exception that is thrown during the use of MyShell
 * program.
 * 
 * @author Lovro Marković
 *
 */
public class ShellIOException extends RuntimeException {
	/**
	 * Serial number of exception.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ShellIOException() {
	}

	/**
	 * Constructor expects a message explaining cause of exception.
	 * 
	 * @param message
	 *            Message type String.
	 */
	public ShellIOException(String message) {
		super(message);
	}

	/**
	 * Constructor expects a cause of exception.
	 * 
	 * @param cause
	 *            Cause of exception type Throwable.
	 */
	public ShellIOException(Throwable cause) {
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
	public ShellIOException(String message, Throwable cause) {
		super(message, cause);
	}
}
