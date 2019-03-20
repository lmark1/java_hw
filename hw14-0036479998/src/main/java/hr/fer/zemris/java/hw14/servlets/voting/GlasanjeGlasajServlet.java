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

/**
 * Checks the passed vote ID. Updates the Options database accordingly.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// ID value.
		String value = req.getParameter("id");
		int id = Integer.valueOf(value);

		// update votes
		OptionEntry option = DAOProvider.getDao().getOptionByID(id);
		int votes = option.getVotesCount();
		votes++;
		option.setVotesCount(votes);
		DAOProvider.getDao().updateOptionEntry(option);
		
		List<OptionEntry> options = DAOProvider.getDao().getOptionsByPoll(option.getPollID());
		req.setAttribute("options", options);
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req,
				resp);
	}
}
