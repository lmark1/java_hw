package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/**
 * This class represents a DAO object provider.
 * 
 * @author Lovro Marković
 *
 */
public class DAOProvider {

	/**
	 * DAO singleton object.
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * @return Returns DAO object.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}