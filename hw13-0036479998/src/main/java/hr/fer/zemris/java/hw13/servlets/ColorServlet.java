package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is activated when user clicks on one of four color links: WHITE,
 * RED, GREEN, CYAN. It remembers the color userd pressed.
 * 
 * @author Lovro Marković
 *
 */
public class ColorServlet extends HttpServlet {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String color = req.getParameter("color");
		req.getSession().setAttribute("pickedBgCol", color);

		// TODO Što napraviti s ovime, jel ovo valja ?
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
