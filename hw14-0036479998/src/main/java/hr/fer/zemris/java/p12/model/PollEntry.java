package hr.fer.zemris.java.p12.model;

/**
 * This class represents a poll entry in the  
 * @author lmark
 *
 */
public class PollEntry {

	/**
	 * Poll ID.
	 */
	private long id;
	
	/**
	 * Poll title.
	 */
	private String title;
	
	/**
	 * Poll message.
	 */
	private String message;
	
	/**
	 * Default constructor for this class.
	 */
	public PollEntry() {
	}
	
	/**
	 * Setter for Poll ID.
	 * @param id Poll ID.
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Setter for the poll message.
	 * @param message Poll message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Setter for poll title.
	 * @param title Poll title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return Returns poll ID.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return Returns poll message.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return Returns poll title.
	 */
	public String getTitle() {
		return title;
	}
}
