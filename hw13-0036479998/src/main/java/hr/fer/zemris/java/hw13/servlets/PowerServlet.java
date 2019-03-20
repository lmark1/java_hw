package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This is a servlet for calculating powers. It accepts three parameters a, b
 * and n. It will generate an Excel document with n pages. I-th page of the
 * document should contain a page with values a, b and a to the i-th power of b.
 * 
 * @author Lovro Marković
 *
 */
public class PowerServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Integer a = Integer.valueOf(req.getParameter("a"));
		Integer b = Integer.valueOf(req.getParameter("b"));
		Integer n = Integer.valueOf(req.getParameter("n"));

		if (a < -100 && a > 100 || b < -100 && a > 100 || n < 1 && n > 5) {

			req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req,
					resp);
		} else {

			HSSFWorkbook hwb = new HSSFWorkbook();

			for (int i = 1; i <= n; i++) {
				HSSFSheet sheet = hwb.createSheet("Power " + i);

				HSSFRow rowhead = sheet.createRow(0);
				rowhead.createCell(0).setCellValue(a);
				rowhead.createCell(1).setCellValue(Math.pow(a, i));

				HSSFRow row = sheet.createRow(1);
				row.createCell(0).setCellValue(b);
				row.createCell(1).setCellValue(Math.pow(b, i));
			}

			resp.setContentType("application/vnd.ms-excel");
			hwb.write(resp.getOutputStream());
			resp.getOutputStream().flush();
			hwb.close();
		}
	}
}
