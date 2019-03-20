package hr.fer.zemris.java.hw13.servlets.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This servlet produces an XLS file of the current voting results.
 * 
 * @author Lovro Marković
 *
 */
public class GlasanjeXlsServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Object value = req.getSession().getAttribute("votingList");
		List<Kandidat> candidateList = (List<Kandidat>) value;

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("Voting results");
		for (int i = 0, len = candidateList.size(); i < len; i++) {
			Kandidat c = candidateList.get(i);
			HSSFRow rowhead = sheet.createRow(i);
			rowhead.createCell(0).setCellValue(c.getID());
			rowhead.createCell(1).setCellValue(c.getName());
			rowhead.createCell(2).setCellValue(c.getVotes());
			rowhead.createCell(3).setCellValue(c.getDescription());
		}

		resp.setContentType("application/vnd.ms-excel");
		hwb.write(resp.getOutputStream());
		resp.getOutputStream().flush();
		hwb.close();
	}
}
