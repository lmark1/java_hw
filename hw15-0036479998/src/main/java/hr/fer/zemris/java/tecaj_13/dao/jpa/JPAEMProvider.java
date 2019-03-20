package hr.fer.zemris.java.tecaj_13.dao.jpa;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * This class provides entity manager objects. It will return the currently set
 * Entity manager or null if it is not initalized.
 * 
 * @author Lovro MArković
 *
 */
public class JPAEMProvider {

	/**
	 * Thread local variable containg Local data objects.
	 */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * @return Returns entity manager.
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if (ldata == null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Attempts to close thread local variable containg entity managers.
	 * 
	 * @throws DAOException
	 *             Exception thrown when the thread local variable can't be
	 *             closed.
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if (ldata == null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch (Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch (Exception ex) {
			if (dex != null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if (dex != null)
			throw dex;
	}

	/**
	 * Wrapper class for entity manager variables.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class LocalData {
		
		/**
		 * Entity manager variable.
		 */
		EntityManager em;
	}

}