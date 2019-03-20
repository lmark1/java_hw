package hr.fer.zemris.java.hw13.servlets.voting;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * This servlet produces a pie chart of currently gathered voting results.
 * 
 * @author Lovro Marković
 *
 */
public class GlasanjeGrafikaServlet extends HttpServlet {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Get candidate list
		Object value = req.getSession().getAttribute("votingList");
		List<Kandidat> candidateList = (List<Kandidat>) value;

		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Kandidat kandidat : candidateList) {
			if (kandidat.getVotes() != 0) {
				dataset.setValue(kandidat.getName(), kandidat.getVotes());
			}
		}

		JFreeChart pieChart = ChartFactory.createPieChart("Band chart", dataset,
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
