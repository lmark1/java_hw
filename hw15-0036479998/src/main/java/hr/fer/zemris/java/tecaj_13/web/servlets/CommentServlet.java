package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * This servlet is used for adding comments to blog entries.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/comment")
public class CommentServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String nick = (String) req.getSession()
				.getAttribute("current.user.nick");

		// Get user email
		String userEmail = null;
		if (nick == null) {
			userEmail = new String("Anonymous");

		} else {
			BlogUser user = DAOProvider.getDAO().getBlogUser(nick);
			userEmail = user.getEmail();
		}

		// Get entry
		String idString = req.getParameter("entryID");
		Long id = Long.valueOf(idString);
		BlogEntry entry = DAOProvider.getDAO().getBlogEntry(id);

		// Get text
		String text = req.getParameter("ctext");

		// CHeck
		System.out.println("email: " + userEmail);
		System.out.println("text: " + text);
		System.out.println("entry: " + entry.getTitle());

		// Make new comment
		BlogComment comment = new BlogComment();
		comment.setBlogEntry(entry);
		comment.setMessage(text);
		comment.setUsersEMail(userEmail);
		comment.setPostedOn(new Date());

		// Add comment
		DAOProvider.getDAO().addComment(comment, entry);

		resp.sendRedirect("/blog/servleti/author/"
				+ entry.getCreator().getNick() + "/" + id);
	}
}
