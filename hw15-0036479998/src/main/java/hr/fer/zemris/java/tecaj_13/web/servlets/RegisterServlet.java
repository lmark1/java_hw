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
 * This servlet is used for registering new users. GET method redirect users to
 * the form, post method collect form data and registers a new user.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String firstName = req.getParameter("fn");
		String lastName = req.getParameter("ln");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");

		String error = null;

		if (nick == null) {
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req,
					resp);
		} else if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
				|| nick.isEmpty() || password.isEmpty()) {
			error = new String("Please fill out all fields");

		} else if (DAOProvider.getDAO().getBlogUser(nick) != null) {
			error = new String("User with the same nickname already exists.");

		} else {

			// Create new user
			BlogUser newUser = new BlogUser();
			newUser.setEmail(email);
			newUser.setFirstName(firstName);
			newUser.setLastName(lastName);
			newUser.setNick(nick);
			newUser.setPasswordHash(Util.generateDigest(password));

			// Add new user to the database
			try{
				DAOProvider.getDAO().addNewUser(newUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendRedirect("/blog/index.html");
		}

		if (error != null) {
			req.setAttribute("error", error);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req,
					resp);
		}
	}
}
