package hr.fer.zemris.galerija.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.galerija.model.Picture;
import hr.fer.zemris.galerija.model.PictureDB;
import hr.fer.zemris.galerija.model.ThumbnailDB;

/**
 * This servlet represents a thumbnail provider. It will write a 
 * thumbnail picture contents as a response.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servlets/thumbnail-provider")
public class ThumbnailProviderServlet extends HttpServlet{

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String name = req.getParameter("name");
		Picture pic = PictureDB.getPicture(name);
		
		if (!ThumbnailDB.contains(name)) {
			System.out.println("Creating thumbnail picture for " + name);
			ThumbnailDB.createThumbnail(pic);
		}
		
		resp.setContentType("image/jpg");
		OutputStream o = resp.getOutputStream();
		ThumbnailDB.writeToStream(o, name);
		o.close();
	}
}
