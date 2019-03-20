package hr.fer.zemris.galerija.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.galerija.model.Picture;
import hr.fer.zemris.galerija.model.PictureDB;

/**
 * This servlet is used for providing picture information.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servlets/picture-info")
public class PictureInfoServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String name = req.getParameter("name");
		System.out.println(name);
		Picture pic = PictureDB.getPicture(name);
		
		Picture[] array = new Picture[1];
		array[0] = pic;
		
		resp.setContentType("application/json;charset=UTF-8"); 
		
		Gson gson = new Gson();
		String jsonText = gson.toJson(array);
		System.out.println(jsonText);
		
		resp.getWriter().write(jsonText);
		
		resp.getWriter().flush();
	}
}
