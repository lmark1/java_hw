package hr.fer.zemris.java.tecaj_13.web.servlets;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class implements a HTTP servlet interface. It attempts tu get a blog
 * entry with the given ID and forwards it to a jsp.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/prikazi")
public class PrikazBloga extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String sID = req.getParameter("id");
		Long id = null;
		try {
			id = Long.valueOf(sID);
		} catch (Exception ignorable) {
		}
		if (id != null) {
			BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
			if (blogEntry != null) {
				req.setAttribute("blogEntry", blogEntry);
				req.setAttribute("author", blogEntry.getCreator().getNick());
			}
		}
		req.getRequestDispatcher("/WEB-INF/pages/Prikaz.jsp").forward(req,
				resp);
	}

}