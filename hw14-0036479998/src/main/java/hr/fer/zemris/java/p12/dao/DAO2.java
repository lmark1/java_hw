package hr.fer.zemris.java.p12.dao;

import java.util.List;

import hr.fer.zemris.java.p12.model.OptionEntry;
import hr.fer.zemris.java.p12.model.PollEntry;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO2 {

	/**
	 * Returns all poll options from the database.
	 * 
	 * @return Returns a list of all poll option.
	 * @throws DAOException
	 *             Exception thrown if reading from database causes an
	 *             exception.
	 */
	public List<OptionEntry> getAllPollOptions() throws DAOException;

	/**
	 * Get all poll options with the given pollID.
	 * 
	 * @param pollID
	 *            Given poll ID.
	 * @return List of all options with the given poll ID.
	 * @throws DAOException
	 *             Exception thrown if reading from database causes an
	 *             exception.
	 */
	public List<OptionEntry> getOptionsByPoll(long pollID) throws DAOException;
	
	/**
	 * Get a single option entry mapped to the given entry ID.
	 * 
	 * @param ID
	 *            Given option ID.
	 * @return List of all options with the given poll ID.
	 * @throws DAOException
	 *             Exception thrown if reading from database causes an
	 *             exception.
	 */
	public OptionEntry getOptionByID(long ID) throws DAOException;
	
	/**
	 * Updates given poll entry.
	 * 
	 * @param newEntry New entry.
	 */
	public void updateOptionEntry(OptionEntry newEntry);
	
	/**
	 * Returns all poll entries in Polls table.
	 * 
	 * @return List of poll entries.
	 * @throws DAOException
	 *             Exception thrown if reading from database causes an
	 *             exception.
	 */
	public List<PollEntry> getAllPolls() throws DAOException;

	/**
	 * Return poll with the appropriate ID.
	 * 
	 * @param pollID
	 *            Given pollID.
	 * @return PollEntry with the appropriate ID.
	 * @throws DAOException
	 *             Exception thrown if reading from database causes an
	 *             exception.
	 */
	public PollEntry getPoll(long pollID) throws DAOException;
}