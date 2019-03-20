package hr.fer.zemris.java.hw13.servlets.voting;

/**
 * This class represents a candidate in voting.
 * 
 * @author Lovro Marković
 *
 */
public class Kandidat {

	/**
	 * Candidate number.
	 */
	private String ID;

	/**
	 * Candidate name.
	 */
	private String name;

	/**
	 * Candidate description.
	 */
	private String description;

	/**
	 * Current number of votes.
	 */
	private int votes = 0;

	/**
	 * Constructor for the candidate entry.
	 * 
	 * @param ID
	 *            ID Number.
	 * @param name
	 *            Name.
	 * @param description
	 *            Description.
	 */
	public Kandidat(String ID, String name, String description) {
		this.ID = ID;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return Candidate description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Candidate name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Candidate ID.
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @return Return total votes.
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * Increase number of votes.
	 */
	public void incrementVotes() {
		votes++;
	}

	/**
	 * Set new vote count.
	 * 
	 * @param voteCount
	 *            New vote count.
	 */
	public void setVoteCount(int voteCount) {
		votes = voteCount;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ID + " ");
		sb.append(name + " ");
		sb.append(description + " ");
		sb.append(votes);
		return sb.toString();
	}
}
