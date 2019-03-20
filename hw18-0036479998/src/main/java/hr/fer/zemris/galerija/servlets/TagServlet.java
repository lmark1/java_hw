package hr.fer.zemris.galerija.servlets;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.galerija.model.PictureDB;

/**
 * This class represents a servlet servlet which fetches all the picture tags 
 * and writes them as response.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servlets/tags")
public class TagServlet extends HttpServlet{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Set<String> tagSet = PictureDB.getTags();
		
		String[] array = new String[tagSet.size()];
		tagSet.toArray(array);
		
		resp.setContentType("application/json;charset=UTF-8"); 
		
		Gson gson = new Gson();
		String jsonText = gson.toJson(array);
		
		resp.getWriter().write(jsonText);
		
		resp.getWriter().flush();
	}
}
