package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet calculates a map of all trigonmetric functions of sin and cos.
 * 
 * @author Lovro Marković
 *
 */
public class TrigServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class containig information about sin and cos values.
	 * 
	 * @author Lovro Marković
	 *
	 */
	public static class TrigValues {

		/**
		 * Angle in degrees.
		 */
		private double angle;

		/**
		 * Sin value of the given angle.
		 */
		private double sin;

		/**
		 * Cos value of the given angle.
		 */
		private double cos;

		/**
		 * Default constructor TrigValues.
		 * 
		 * @param angle
		 *            Given angle.
		 */
		public TrigValues(int angle) {
			this.angle = angle;

			sin = Math.sin(this.angle * Math.PI / 180);
			cos = Math.cos(this.angle * Math.PI / 180);
		}

		/**
		 * @return Cos value of the angle.
		 */
		public double getCos() {
			return cos;
		}

		/**
		 * @return Sin value of the angle.
		 */
		public double getSin() {
			return sin;
		}

		/**
		 * @return Returns angle.
		 */
		public double getAngle() {
			return angle;
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer a = 0;
		Integer b = 360;

		try {
			a = Integer.valueOf((String) req.getParameter("a"));
		} catch (Exception ignorable) {
		}

		try {
			b = Integer.valueOf((String)  req.getParameter("b"));
		} catch (Exception ignorable) {
		}

		if (a > b) {
			Integer temp = b;
			b = a;
			a = temp;
		}

		if (b > a + 720) {
			b = a + 720;
		}

		List<TrigValues> values = new ArrayList<>();

		for (int i = a; i <= b; i++) {
			values.add(new TrigValues(i));
		}

		req.setAttribute("result", values);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp")
				.forward(req, resp);
	}
}
