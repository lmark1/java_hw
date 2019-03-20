package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Data acess object interface. Defines all the methods which communicate with
 * the database.
 * 
 * @author Lovro Marković
 *
 */
public interface DAO {

	/**
	 * Returns an entry with the appropriate ID. If it doesnot exist returns
	 * null;
	 * 
	 * @param id
	 *            Entry ID.
	 * @return Entry or null if it doesn't exist.
	 * @throws DAOException
	 *             Exception thrown if an error occurs while communicating with
	 *             database.
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Returns blog user object with the nick. If it does not exist returns
	 * null;
	 * 
	 * @param nick
	 *            User nick.
	 * @return Blog user object or null if it doesn't exist.
	 * @throws DAOException
	 *             Exception thrown if an error occurs while communicating with
	 *             database.
	 */
	public BlogUser getBlogUser(String nick) throws DAOException;

	/**
	 * Returns a list of all blog users.
	 * 
	 * @return List of all blog users.
	 * @throws DAOException
	 *             Exception thrown if an error occurs while communicating with
	 *             database.
	 */
	public List<BlogUser> getAllUsers() throws DAOException;

	/**
	 * Add a new user to the database.
	 * 
	 * @param newUser
	 *            New user.
	 * @throws DAOException
	 *             Exception thrown if an error occurs while communicating with
	 *             database.
	 */
	public void addNewUser(BlogUser newUser) throws DAOException;

	/**
	 * Add a new blog entry.
	 * 
	 * @param entry
	 *            Blog entry that will be added.
	 * @param user
	 *            User the blog entry is mapped to.
	 * @throws DAOException
	 *             Exception thrown if an error occurs while communicating with
	 *             database.
	 */
	public void addBlogEntry(BlogEntry entry, BlogUser user)
			throws DAOException;

	/**
	 * Add a new comment to the blog entry.
	 * 
	 * @param comment
	 *            New comment.
	 * @param entry
	 *            Blog entry.
	 * @throws DAOException
	 *             Exception thrown if an error occurs while communicating with
	 *             database.
	 */
	public void addComment(BlogComment comment, BlogEntry entry)
			throws DAOException;
}