package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * This class provides entity manager factory objects. It will return
 * the currently set Entity manager or null if it is not initalized.
 * 
 * @author Lovro MArković
 *
 */
public class JPAEMFProvider {

	/**
	 * Currently available entity manager factory.
	 */
	public static EntityManagerFactory emf;

	/**
	 * @return Currently available entity manager factory.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Sets a new entity manager factory.
	 * 
	 * @param emf
	 *            New entity manager factory.
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}