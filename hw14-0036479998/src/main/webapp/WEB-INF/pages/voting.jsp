<%@page import="hr.fer.zemris.java.p12.model.OptionEntry"%>
<%@page import="hr.fer.zemris.java.p12.model.PollEntry"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>

<%
	PollEntry poll = (PollEntry)request.getAttribute("poll");
	List<OptionEntry> options = (List<OptionEntry>)request.getAttribute("options");
%>

<html>
  <body>
	<h1><%=poll.getTitle()%></h1>
	<p><%=poll.getMessage()%></p>
	
	<% if(options == null || options.isEmpty()) { %>
    	No poll options available.
  	<% } else { %>
	<ol>
		<% for(OptionEntry entry : options) { %>
			<li>
				<a href="/voting-app/servleti/glasanje-glasaj?id=<%=entry.getId()%>">
					<%=entry.getTitle()%>
				</a>
			</li>
		<%}%>
	</ol>
	<%}%>
	
</body>
</html>