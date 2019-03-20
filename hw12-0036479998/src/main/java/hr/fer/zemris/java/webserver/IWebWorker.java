package hr.fer.zemris.java.webserver;

/**
 * This is an interface declared towards any object that can process current
 * request.
 * 
 * @author Lovro Marković
 *
 */
public interface IWebWorker {

	/**
	 * Method used for processing the request.
	 * 
	 * @param context
	 *            Given context.
	 * @throws Exception
	 *             Exception thrown if an error occurs during writing / reading
	 *             from streams.
	 */
	public void processRequest(RequestContext context) throws Exception;
}