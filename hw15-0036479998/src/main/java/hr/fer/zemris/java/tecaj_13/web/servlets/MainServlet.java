package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.model.Util;

/**
 * This servlet formats the request to the main jsp.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req,
				resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");
		String hashPassword = Util.generateDigest(password);
		
		// Check parameters
		System.out.println();
		System.out.println("Found nick - " + nick);
		System.out.println("Found hash - " + hashPassword);
				
		// Initialize error message.
		String error = null;
		String defaultNick = null;
		BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
		
		if (nick.isEmpty() || password.isEmpty()) {
			
			// If user did not fill out both fields send error.
			error = new String("Please fill out both fields of the form.");
			
		} else if (user == null) {
			
			// If user does not exist
			error = new String("User with that given nick does not exist.");
			
		} else if (!user.getPasswordHash().equals(hashPassword)) { 		
			
			// If user exists check the password
			error = new String("Incorrect password.");
			defaultNick = nick;
				
		} else {
					
			// If user exists and the password is correct store user information
			req.getSession().setAttribute("current.user.id", user.getId());
			req.getSession().setAttribute("current.user.nick", user.getNick());
			req.getSession().setAttribute("current.user.fn", user.getFirstName());
			req.getSession().setAttribute("current.user.ln", user.getLastName());
			
			resp.sendRedirect("/blog/servleti/main");
		}
			
		
		// Forward the request back to the main page.
		if (error != null) {
			
			req.setAttribute("error", error);
			req.setAttribute("defaultNick", defaultNick);
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req,
					resp);
		}
	}

}