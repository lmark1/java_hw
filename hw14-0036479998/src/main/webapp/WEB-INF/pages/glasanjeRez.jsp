<%@page import="hr.fer.zemris.java.p12.dao.DAOProvider"%>
<%@page import="hr.fer.zemris.java.p12.model.PollEntry"%>
<%@page import="hr.fer.zemris.java.p12.model.OptionEntry"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>

<%
// Get available voting options
List<OptionEntry> options = (List<OptionEntry>)request.getAttribute("options");
int pollID = options.get(0).getPollID();
PollEntry poll = DAOProvider.getDao().getPoll(pollID);
%>

<%
 	// Get max votes.
	int maxVotes = 0;
	for (int i = 0; i < options.size(); i++) {
		int currVotes = options.get(i).getVotesCount();
		if (currVotes > maxVotes) {
			maxVotes = currVotes;
		}
	}
%>

<html>
<head>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
</head>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<% for(OptionEntry entry : options) { %>
			<tr>
				<td><%=entry.getTitle() %></td>
				<td><%=entry.getVotesCount()%></td>
			</tr>
			<%}%>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img src="/voting-app/servleti/glasanje-grafika?pollID=<%=pollID%>"
		width="400" height="400" />
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a
			href="/voting-app/servleti/glasanje-xls?pollID=<%=pollID%>">ovdje</a>
	</p>
	
	<%if (poll.getTitle().equals("Glasanje za omiljeni bend:")) { %>
	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<%
			for (int i = 0; i < options.size(); i++) {
				OptionEntry c = options.get(i);
				if (c.getVotesCount() == maxVotes) {
		%>
		<li><a href="<%=c.getDescription()%>" target="_blank"><%=c.getTitle()%></a></li>
		<%
			}
			}
		%>
	</ul>
	<%}%>
	
</body>
</html>