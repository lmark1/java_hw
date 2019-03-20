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

/**
 * This servlet serves as a provider for regular size pictures in 
 * the gallery.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servlets/picture-provider")
public class PictureProviderServlet extends HttpServlet{

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
		resp.setContentType("image/jpg");
		
		OutputStream o = resp.getOutputStream();
		pic.writeToStream(o);
		o.close();
	}
}
