package hr.fer.zemris.java.hw13.servlets.voting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is used for extracting available voting options from the file in
 * WEB-INF directory.
 * 
 * @author Lovro Marković
 *
 */
public class GlasanjeServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ServletContext context = req.getSession().getServletContext();

		// If context is not yet initialized.
		if (context.getAttribute("votingList") == null) {
			String fileName = context
					.getRealPath("/WEB-INF/glasanje-definicija.txt");

			List<String> lines = Files.readAllLines(Paths.get(fileName));

			List<Kandidat> entryList = new ArrayList<>();
			for (String entry : lines) {
				String[] splitEntry = entry.split("\t");
				entryList.add(new Kandidat(splitEntry[0], splitEntry[1],
						splitEntry[2]));
			}

			req.getSession().setAttribute("votingList", entryList);
		}

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp")
				.forward(req, resp);
	}
}
