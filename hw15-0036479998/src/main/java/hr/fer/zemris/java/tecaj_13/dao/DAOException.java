package hr.fer.zemris.java.tecaj_13.dao;

/**
 * This class models an exception which will be thrown if an error occurs while
 * working with database.
 * 
 * @author Lovro Marković
 *
 */
public class DAOException extends RuntimeException {

	/**
	 * Serial version IS.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Exception constructor.
	 * 
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            Exception cause.
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Exception constructor.
	 * 
	 * @param message
	 *            Exception message.
	 */
	public DAOException(String message) {
		super(message);
	}
}