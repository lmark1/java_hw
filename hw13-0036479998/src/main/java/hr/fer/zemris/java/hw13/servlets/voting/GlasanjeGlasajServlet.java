package hr.fer.zemris.java.hw13.servlets.voting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generates and updates glasanje-rezultati.txt based on the given votes.
 * 
 * @author Lovro Marković
 *
 */
public class GlasanjeGlasajServlet extends HttpServlet {

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
		
		if (!(value instanceof List<?>)) {
			req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req,
					resp);
		} else {
			List<Kandidat> candidateList = (List<Kandidat>) value;

			// Get new vote ID.
			String newVote = req.getParameter("id");

			// Find voting list path
			Path votingPath = Paths.get(req.getSession().getServletContext()
					.getRealPath("/WEB-INF/glasanje-rezultati.txt"));
			// Initialize candidate list votes.
			if (Files.exists(votingPath)) {
				List<String> resultList = Files.readAllLines(votingPath);
				updateCandidateList(resultList, candidateList);
			}

			// Update candidate list
			for (Kandidat kandidat : candidateList) {
				if (kandidat.getID().equals(newVote)) {
					kandidat.incrementVotes();
				}
			}

			updateVotingList(votingPath, candidateList);
			resp.sendRedirect(
					req.getContextPath()
							+ "/glasanje-rezultati");
		}
	}

	/**
	 * Update current candidate list with the values in the resultList.
	 * 
	 * @param resultList
	 *            List of all results.
	 * @param cList
	 *            List of all the candidate object.
	 * 
	 */
	private void updateCandidateList(List<String> resultList,
			List<Kandidat> cList) {

		// Map all the values from the result list - String - ID / Integer -
		// voteCount
		Map<String, Integer> resultMap = new HashMap<>();
		for (int i = 0; i < resultList.size(); i++) {
			String entry = resultList.get(i);
			String[] splitEntry = entry.split("\t");
			resultMap.put(splitEntry[0], Integer.valueOf(splitEntry[1]));
		}

		// Set new vote count values
		for (int i = 0; i < cList.size(); i++) {
			String ID = cList.get(i).getID();
			Integer newVoteCount = resultMap.get(ID);
			cList.get(i).setVoteCount(newVoteCount);
		}
	}

	/**
	 * Write new voting results to the list.
	 * 
	 * @param path
	 *            Path to the voting list.
	 * @param clist
	 *            Voting candidates
	 * @throws IOException
	 *             Exception thrown when an error occurs while writing.
	 */
	private void updateVotingList(Path path, List<Kandidat> clist)
			throws IOException {
		StringBuilder sb = new StringBuilder();

		for (Kandidat kandidat : clist) {
			String newEntry = new String(
					kandidat.getID() + "\t" + kandidat.getVotes());
			sb.append(newEntry + "\n");
		}

		Files.write(path, sb.toString().getBytes());
	}
}
