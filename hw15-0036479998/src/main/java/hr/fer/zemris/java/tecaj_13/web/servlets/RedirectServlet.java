package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is used for redirecting /index.html requests to
 * /servleti/index.html request.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet(name="redirect-servlet", urlPatterns={"/index.html", "/"})
public class RedirectServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.sendRedirect("/blog/servleti/main");
	}
}
