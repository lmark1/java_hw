package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * This class implements DAO interface.
 * 
 * @author Lovro Marković
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager()
				.find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getBlogUser(String nick) throws DAOException {
		BlogUser user = null;

		try {
			user = (BlogUser) JPAEMProvider.getEntityManager()
					.createQuery("select u from BlogUser as u where u.nick=:un")
					.setParameter("un", nick).getSingleResult();

		} catch (NoResultException ignorable) {
		}

		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlogUser> getAllUsers() throws DAOException {
		List<BlogUser> users = null;

		try {
			users = (List<BlogUser>) JPAEMProvider.getEntityManager()
					.createQuery("select u from BlogUser u").getResultList();
		} catch (NoResultException ignorable) {
		}

		return users;
	}

	@Override
	public void addNewUser(BlogUser newUser) throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		EntityTransaction tr = em.getTransaction();

		if (!tr.isActive()) {
			tr.begin();
		}

		em.persist(newUser);
	}

	@Override
	public void addBlogEntry(BlogEntry entry, BlogUser user)
			throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();
		EntityTransaction tr = em.getTransaction();

		if (!tr.isActive()) {
			tr.begin();
		}

		user.getBlogEntries().add(entry);
		em.persist(entry);
	}

	@Override
	public void addComment(BlogComment comment, BlogEntry entry)
			throws DAOException {

		EntityManager em = JPAEMProvider.getEntityManager();
		EntityTransaction tr = em.getTransaction();

		if (!tr.isActive()) {
			tr.begin();
		}

		entry.getComments().add(comment);
		em.persist(comment);
	}
}
