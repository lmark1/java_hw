package hr.fer.zemris.java.p12.model;

/**
 * This class represents an option entry in the polls.
 * 
 * @author Lovro Marković
 *
 */
public class OptionEntry {

	/**
	 * Entry ID.
	 */
	private long id;

	/**
	 * Entry title.
	 */
	private String title;

	/**
	 * Entry description.
	 */
	private String description;

	/**
	 * Entry poll ID.
	 */
	private int pollID;

	/**
	 * Entry vote count.
	 */
	private int votesCount;

	/**
	 * Default constructor for this class.
	 */
	public OptionEntry() {
	}

	/**
	 * Setter for description.
	 * 
	 * @param description
	 *            Description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Setter for ID.
	 * 
	 * @param id
	 *            Entry ID.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Setter for poll ID.
	 * 
	 * @param pollID
	 *            Poll ID.
	 */
	public void setPollID(int pollID) {
		this.pollID = pollID;
	}

	/**
	 * Setter for title.
	 * 
	 * @param title
	 *            Title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Setter for votes count.
	 * 
	 * @param votesCount
	 *            Vote count.
	 */
	public void setVotesCount(int votesCount) {
		this.votesCount = votesCount;
	}

	/**
	 * @return Returns entry description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Returns entry ID.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return Returns poll ID.
	 */
	public int getPollID() {
		return pollID;
	}

	/**
	 * @return Returns entry title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return Returns vote count.
	 */
	public int getVotesCount() {
		return votesCount;
	}
}
