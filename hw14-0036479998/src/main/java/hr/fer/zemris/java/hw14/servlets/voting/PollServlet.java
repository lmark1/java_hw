package hr.fer.zemris.java.hw14.servlets.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollEntry;

/**
 * This servlet is used for extracting available voting options from the
 * database.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/index.html")
public class PollServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<PollEntry> polls = DAOProvider.getDao().getAllPolls();
		req.setAttribute("polls", polls);

		req.getRequestDispatcher("/WEB-INF/pages/polls.jsp").forward(req, resp);
	}
}
