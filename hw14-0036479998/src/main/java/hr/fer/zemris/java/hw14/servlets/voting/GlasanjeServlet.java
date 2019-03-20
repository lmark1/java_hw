package hr.fer.zemris.java.hw14.servlets.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.OptionEntry;
import hr.fer.zemris.java.p12.model.PollEntry;

/**
 * This servlet is used for extracting available voting options from the file in
 * WEB-INF directory.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// ID value.
		String value = req.getParameter("pollID");
		int id = Integer.valueOf(value);
		
		// Get all options
		List<OptionEntry> options = DAOProvider.getDao().getOptionsByPoll(id);
		req.setAttribute("options", options);
		
		// Get poll information
		PollEntry entry = DAOProvider.getDao().getPoll(id);
		req.setAttribute("poll", entry);
		
		req.getRequestDispatcher("/WEB-INF/pages/voting.jsp").forward(req, resp);
	}
}
