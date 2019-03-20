package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * This servlet will process requests mapped to /blog/author/*.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Added for hyperlink use
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println(req.getServletPath());
		System.out.println(req.getPathInfo());

		// Get path info
		String[] info = req.getPathInfo().substring(1).split("/");

		if (info.length == 1) {

			// request url is of type /author/ivica
			// Get all his blog entries
			BlogUser user = DAOProvider.getDAO().getBlogUser(info[0]);
			List<BlogEntry> entries = user.getBlogEntries();
			req.setAttribute("entries", entries);
			req.setAttribute("author", info[0]);
			req.getRequestDispatcher("/WEB-INF/pages/blogEntries.jsp")
					.forward(req, resp);

		} else if (info.length == 2) {

			if (info[1].toLowerCase().equals("new")) {
				// Process "new" request
				newRequest(req, resp, info);

			} else if (info[1].toLowerCase().equals("edit")) {
				
				editRequest(req, resp, info);
			} else {
				// Show entry
				showEntry(req, resp, info);
			}
		}
	}

	/**
	 * Handle edit request.
	 * 
	 * @param req
	 *            Servlet request.
	 * @param resp
	 *            Servlet response.
	 * @param info
	 *            Path info.
	 * @throws ServletException
	 *             Exception thrown if an error occurs while forwarding request.
	 * @throws IOException
	 *             Exception thrown if an error occurs while redirecting
	 *             request.
	 */
	private void editRequest(HttpServletRequest req, HttpServletResponse resp,
			String[] info) throws ServletException, IOException {
		
		String title = req.getParameter("title");
		String text = req.getParameter("etext");
		String error = null;

		System.out.println("title: " + title);
		System.out.println("text: " + text);
		
		String defaultTitle = null;
		String defaultText = null;
		
		Long id = null;
		try {
			id = Long.valueOf((String) req.getParameter("entryID"));
		} catch (Exception e) {
		}
		
		System.out.println(id);
		BlogEntry entry = null;
		if (id != null) {
			entry = DAOProvider.getDAO().getBlogEntry(id);
			if (entry != null) {
				defaultText = entry.getText();
				defaultTitle = entry.getTitle();
			}
		}
		
		
		if (title == null) {
			// Do nothing

		} else if (title.isEmpty()) {
			error = new String("Please enter title of the entry.");

		} else { 
			// Create new blog entry
			entry.setText(text);
			entry.setTitle(title);
			entry.setLastModifiedAt(new Date());
			
			resp.sendRedirect("/blog/servleti/author/" + info[0] + "/");
		}
		
		System.out.println(defaultText);
		System.out.println(defaultTitle);
		
		req.setAttribute("entryID", id);
		req.setAttribute("defaultTitle", defaultTitle);
		req.setAttribute("defaultText", defaultText);
		req.setAttribute("author", info[0]);
		req.setAttribute("method", info[1]);
		req.setAttribute("error", error);
		req.getRequestDispatcher("/WEB-INF/pages/newEntry.jsp").forward(req,
				resp);
	}

	/**
	 * Handles the request for showing entries.
	 * 
	 * @param req
	 *            Servlet request.
	 * @param resp
	 *            Servlet response.
	 * @param info
	 *            Path information.
	 * @throws ServletException
	 *             Exception thrown if an error occurs while forwarding request.
	 * @throws IOException
	 *             Exception thrown if an error occurs while redirecting
	 *             request.
	 */
	private void showEntry(ServletRequest req, HttpServletResponse resp,
			String[] info) throws ServletException, IOException {
		Long id = null;
		try {
			id = Long.valueOf(info[1]);
		} catch (Exception e) {
		}

		BlogEntry entry = null;
		if (id != null) {
			entry = DAOProvider.getDAO().getBlogEntry(id);
			if (entry != null) {
				req.setAttribute("blogEntry", entry);
				req.setAttribute("author", entry.getCreator().getNick());
				System.out.println(entry.getCreator().getNick());
			}
		}
		req.getRequestDispatcher("/WEB-INF/pages/showEntry.jsp").forward(req,
				resp);
	}

	
	/**
	 * Handle new request.
	 * 
	 * @param req
	 *            Servlet request.
	 * @param resp
	 *            Servlet response.
	 * @param info
	 *            Path info.
	 * @throws ServletException
	 *             Exception thrown if an error occurs while forwarding request.
	 * @throws IOException
	 *             Exception thrown if an error occurs while redirecting
	 *             request.
	 */
	private void newRequest(ServletRequest req, HttpServletResponse resp,
			String[] info) throws ServletException, IOException {
		String title = req.getParameter("title");
		String text = req.getParameter("etext");
		String error = null;

		System.out.println("title: " + title);
		System.out.println("text: " + text);
		if (title == null) {
			// Do nothing

		} else if (title.isEmpty()) {
			error = new String("Please enter title of the entry.");

		} else {

			// Create new blog entry
			BlogEntry blogEntry = new BlogEntry();
			blogEntry.setCreatedAt(new Date());
			blogEntry.setLastModifiedAt(blogEntry.getCreatedAt());
			blogEntry.setTitle(title);
			blogEntry.setText(text);

			BlogUser creator = DAOProvider.getDAO().getBlogUser(info[0]);
			System.out.println(creator.getNick());
			System.out.println();
			blogEntry.setCreator(creator);

			DAOProvider.getDAO().addBlogEntry(blogEntry, creator);
			resp.sendRedirect("/blog/servleti/author/" + info[0] + "/");
		}

		req.setAttribute("method", info[1]);
		req.setAttribute("author", info[0]);
		req.setAttribute("error", error);
		req.getRequestDispatcher("/WEB-INF/pages/newEntry.jsp").forward(req,
				resp);
	}

}
