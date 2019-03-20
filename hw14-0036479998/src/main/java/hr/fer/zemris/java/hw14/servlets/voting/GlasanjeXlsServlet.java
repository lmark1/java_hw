package hr.fer.zemris.java.hw14.servlets.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.OptionEntry;

/**
 * This servlet produces an XLS file of the current voting results.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// ID value.
		String value = req.getParameter("pollID");
		int id = Integer.valueOf(value);

		// Get candidate list
		List<OptionEntry> options = DAOProvider.getDao().getOptionsByPoll(id);

		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet("Voting results");
		for (int i = 0, len = options.size(); i < len; i++) {
			OptionEntry c = options.get(i);
			HSSFRow rowhead = sheet.createRow(i);
			rowhead.createCell(0).setCellValue(c.getId());
			rowhead.createCell(1).setCellValue(c.getTitle());
			rowhead.createCell(2).setCellValue(c.getVotesCount());
			rowhead.createCell(3).setCellValue(c.getDescription());
		}

		resp.setContentType("application/vnd.ms-excel");
		hwb.write(resp.getOutputStream());
		resp.getOutputStream().flush();
		hwb.close();
	}
}
