<%@page import="hr.fer.zemris.java.hw13.servlets.voting.Kandidat"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>

<%
	// Get current color
	String pickedColor = (String) request.getSession()
			.getAttribute("pickedBgCol");
	if (pickedColor == null) {
		pickedColor = "white";
	}
%>

<%
	//Find highest number of votes
	Object value = request.getSession().getAttribute("votingList");

	@SuppressWarnings("unchecked")
	List<Kandidat> candidateList = (List<Kandidat>) value;

	int maxVotes = 0;
	for (int i = 0; i < candidateList.size(); i++) {
		int currVotes = candidateList.get(i).getVotes();
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
<body bgcolor="<%=pickedColor%>">
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
			<c:forEach var="entry" items="${votingList}">
				<tr>
					<td>${entry.name}</td>
					<td>${entry.votes}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img src="/webapp2/glasanje-grafika" width="400" height="400" />
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="/webapp2/glasanje-xls">ovdje</a>
	</p>
	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<%
			for (int i = 0; i < candidateList.size(); i++) {
				Kandidat c = candidateList.get(i);
				if (c.getVotes() == maxVotes) {
		%>
		<li><a href="<%=c.getDescription()%>" target="_blank"><%=c.getName()%></a></li>
		<%
			}
			}
		%>
	</ul>
</body>
</html>