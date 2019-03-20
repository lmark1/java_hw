package hr.fer.zemris.java.hw14.servlets.voting;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.OptionEntry;

/**
 * This servlet produces a pie chart of currently gathered voting results.
 * 
 * @author Lovro Marković
 *
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

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

		DefaultPieDataset dataset = new DefaultPieDataset();
		for (OptionEntry kandidat : options) {
			if (kandidat.getVotesCount() != 0) {
				dataset.setValue(kandidat.getTitle(), kandidat.getVotesCount());
			}
		}

		JFreeChart pieChart = ChartFactory.createPieChart("Voting results", dataset,
				true, true, false);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ChartUtilities.writeChartAsPNG(bos, pieChart, 400, 400);

		resp.setContentType("image/png");
		OutputStream os = new BufferedOutputStream(resp.getOutputStream());
		os.write(bos.toByteArray());
		os.flush();
		os.close();
	}
}
