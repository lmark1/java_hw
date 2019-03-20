package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * This class represents a listener which methods are called when application
 * starts, or shuts down.
 * 
 * @author Lovro Marković
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		Properties dbProps = new Properties();

		// Try loading properties file
		try {
			Path propPath = Paths.get(sce.getServletContext()
					.getRealPath("WEB-INF/dbsettings.properties"));
			FileInputStream is = new FileInputStream(propPath.toString());
			dbProps.load(is);
			is.close();
		} catch (Exception e) {
			throw new DBException("Error reading database properties");
		}

		// Check if it contains all needed properties
		if (!dbProps.containsKey("host") || !dbProps.containsKey("port")
				|| !dbProps.containsKey("name") || !dbProps.containsKey("user")
				|| !dbProps.containsKey("password")) {
			throw new DBException(
					"Properties file does not contain all needed properties");
		}

		// Build connection URL
		StringBuilder sb = new StringBuilder();
		sb.append("jdbc:derby://");
		sb.append(dbProps.getProperty("host"));
		sb.append(":" + dbProps.getProperty("port"));
		sb.append("/" + dbProps.getProperty("name"));
		sb.append(";user=" + dbProps.getProperty("user"));
		sb.append(";password=" + dbProps.getProperty("password"));
		String connectionURL = sb.toString();

		// Test
		System.out.println(connectionURL);

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException(
					"Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		// Create tables.
		try {
			createTables(cpds);
			createPollEntries(cpds);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("Error occured while creating tables.");
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce
				.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create new table entries if tables are empty.
	 * 
	 * @param cpds
	 *            Data source.
	 * @throws SQLException
	 *             Exception thrown if connection cannot be established.
	 */
	private void createPollEntries(ComboPooledDataSource cpds)
			throws SQLException {
		Connection c = cpds.getConnection();

		// Create new voting polls
		int pollID = (int) createVotingPoll(c, "Glasanje za omiljeni bend:",
				"Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!")
						.longValue();
		int otherPollID = (int) createVotingPoll(c,
				"Glasanje za omiljena peciva:",
				"Koje Vam je najdraže pecivo, kliknite na link!").longValue();
		int voteCount = 0;
		
		// First poll voting entries
		createVotingEntry(c, "The Beatles",
				"https://www.youtube.com/watch?v=z9ypq6_5bsg", pollID,
				voteCount);
		createVotingEntry(c, "The Platters",
				"https://www.youtube.com/watch?v=H2di83WAOhU", pollID,
				voteCount);
		createVotingEntry(c, "The Beach Boys",
				"https://www.youtube.com/watch?v=2s4slliAtQU", pollID,
				voteCount);
		createVotingEntry(c, "The Four Seasons",
				"https://www.youtube.com/watch?v=y8yvnqHmFds", pollID,
				voteCount);
		createVotingEntry(c, "The Marcels",
				"https://www.youtube.com/watch?v=qoi3TH59ZEs", pollID,
				voteCount);
		createVotingEntry(c, "The Everly Brothers",
				"https://www.youtube.com/watch?v=tbU3zdAgiX8", pollID,
				voteCount);
		createVotingEntry(c, "The Mamas And The Papas",
				"https://www.youtube.com/watch?v=N-aK6JnyFmk", pollID,
				voteCount);
		
		// Initialize some other poll entries
		createVotingEntry(c, "Kiflice", "nema linka" , otherPollID, voteCount);
		createVotingEntry(c, "Burek", "nema linka" , otherPollID, voteCount);
		createVotingEntry(c, "Kruh", "nema linka" , otherPollID, voteCount);
			
	}

	/**
	 * Create voting entries.
	 * 
	 * @param c
	 *            Connection to the database.
	 * @param optionTitle
	 *            Entry title.
	 * @param optionLink
	 *            Entry Description (link).
	 * @param pollID
	 *            Poll ID.
	 * @param votesCount
	 *            Vote count.
	 */
	private void createVotingEntry(Connection c, String optionTitle,
			String optionLink, int pollID, int votesCount) {
		PreparedStatement pst = null;

		// Check if entry already exists in the poll
		if (entryExists(c, optionTitle, pollID)) {
			return;
		}

		try {
			pst = c.prepareStatement(
					"INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) "
							+ "VALUES (?,?,?,?)");
			pst.setString(1, optionTitle);
			pst.setString(2, optionLink);
			pst.setInt(3, pollID);
			pst.setInt(4, votesCount);

			try {
				pst.executeUpdate();

			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DBException(
					"Exception occured when writing initial DB entry.");
		}
	}

	/**
	 * Check if entry already exists in the given poll ID.
	 * 
	 * @param c
	 *            Connection to the database.
	 * @param optionTitle
	 *            Title of the opstion.
	 * @param pollID
	 *            ID of the poll the option is connected to.
	 * @return True if given entry already exists, otherwise false.
	 */
	private boolean entryExists(Connection c, String optionTitle, int pollID) {
		PreparedStatement pst = null;
		try {
			pst = c.prepareStatement("SELECT * FROM PollOptions");

			try {
				ResultSet rs = pst.executeQuery();

				while (rs != null && rs.next()) {
					if (rs.getString(2).equals(optionTitle)
							&& rs.getInt(4) == pollID) {
						// There is an entry that exists in the polls.
						rs.close();
						return true;
					}
				}

			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DBException(
					"Exception occured when writing initial DB entry.");
		}

		return false;
	}

	/**
	 * Check if the poll is created.
	 * 
	 * @param c
	 *            Connection to the data.
	 * @param name
	 *            Name of the poll
	 * @return Poll ID if the poll is created, -1 otherwise..
	 */
	private Long isPollCreated(Connection c, String name) {
		PreparedStatement pst = null;
		try {
			pst = c.prepareStatement("SELECT * FROM Polls");

			try {
				ResultSet rs = pst.executeQuery();

				while (rs != null && rs.next()) {
					System.out.println(rs.getString(2));
					if (rs.getString(2).equals(name)) {
						Long ID = rs.getLong(1);
						// There is an entry that exists in the polls.
						rs.close();
						return ID;
					}
				}

			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DBException(
					"Exception occured when writing initial DB entry.");
		}

		return -1L;
	}

	/**
	 * Creates new voting poll in the "Polls" table.
	 * 
	 * @param c
	 *            Connection to the database.
	 * @param description
	 *            Poll description.
	 * @param name
	 *            Name of the poll.
	 * @return Returns poll ID of a newly created poll.
	 */
	private Long createVotingPoll(Connection c, String name,
			String description) {

		// Check if poll is not in the table
		Long pollID = isPollCreated(c, name);
		if (pollID != -1L) {
			return pollID;
		}

		PreparedStatement pst = null;
		try {
			pst = c.prepareStatement(
					"INSERT INTO Polls (title, message) VALUES (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, name);
			pst.setString(2, description);

			try {
				pst.executeUpdate();
				ResultSet rset = pst.getGeneratedKeys();

				try {
					if (rset != null && rset.next()) {
						return rset.getLong(1);
					}
				} finally {
					try {
						rset.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}

			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DBException(
					"Exception occured when writing initial DB entry.");
		}

		return -1L;
	}

	/**
	 * Create tables if necessary.
	 * 
	 * @param cpds
	 *            Data source.
	 * @throws SQLException
	 *             Exception thrown if connection cannot be established.
	 */
	private void createTables(ComboPooledDataSource cpds) throws SQLException {
		Connection c = cpds.getConnection();

		// If table is not there create it
		try {
			PreparedStatement pst = c.prepareStatement(
					"CREATE TABLE Polls (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
							+ " title VARCHAR(150) NOT NULL, message CLOB(2048) NOT NULL)");
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			if (!e.getSQLState().equals("X0Y32")) {
				throw e;
			}
		}

		// If table is not there create it
		try {
			PreparedStatement pst = c.prepareStatement(
					"CREATE TABLE PollOptions (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
							+ "optionTitle VARCHAR(100) NOT NULL, optionLink VARCHAR(150) NOT NULL, "
							+ "pollID BIGINT, votesCount BIGINT,FOREIGN KEY (pollID) REFERENCES Polls(id))");
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			if (!e.getSQLState().equals("X0Y32")) {
				throw e;
			}
		}

	}

}