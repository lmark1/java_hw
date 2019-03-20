package hr.fer.zemris.java.webserver;

/**
 * This interface defines client worker.
 * 
 * @author Lovro Marković
 *
 */
public interface IDispatcher {
	
	/**
	 * This method analyses given path, and determines how to process
	 * it.
	 * 
	 * @param urlPath Given URL path.
	 * @throws Exception Exception throw when there is an error.
	 */
	void DispatchRequest(String urlPath) throws Exception;
	
}
