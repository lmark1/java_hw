package hr.fer.zemris.galerija.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.galerija.model.Picture;
import hr.fer.zemris.galerija.model.PictureDB;


/**
 * This servlet provides results of the tag button click.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servlets/thumbnails")
public class TagResultServlet extends HttpServlet{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String tag = req.getParameter("tag");
		List<Picture> pictures = PictureDB.getFilteredPictures(tag);
		
		Picture[] array = new Picture[pictures.size()];
		pictures.toArray(array);
		
		resp.setContentType("application/json;charset=UTF-8"); 
		
		Gson gson = new Gson();
		String jsonText = gson.toJson(array);
		
		resp.getWriter().write(jsonText);
		
		resp.getWriter().flush();
	}
}
