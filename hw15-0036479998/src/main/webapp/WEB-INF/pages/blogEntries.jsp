<%@page import="hr.fer.zemris.java.tecaj_13.model.BlogEntry"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
List<BlogEntry> entries = (List<BlogEntry>)request.getAttribute("entries");
String author = (String)request.getAttribute("author");

String nick = (String)request.getSession().getAttribute("current.user.nick");
String firstName = (String)request.getSession().getAttribute("current.user.fn");
String lastName = (String)request.getSession().getAttribute("current.user.ln");
%>

<html>
<body>
	
	<% if (nick == null) { %>
		Not logged in.
	<%} else {%>
		<%=lastName%>, <%=firstName%>
		<form action="/blog/servleti/logout/">
			<input type="submit" value="Logout"/>
		</form>
	<%} %>
	
	<h1><%=author%>'s Blog</h1>

	<% if (entries == null || entries.isEmpty()) { %>
	<p>No blog entries available.</p>
	<% } else { %>
	<b>List of available blog entries:</b>
	<br>
	<ul>
		<% for(BlogEntry entry: entries) { %>
		<li><a
			href="/blog/servleti/author/<%=author%>/<%=entry.getId()%>/"> 
			<%=entry.getTitle()%>
			</a>
		</li>
		<% } %>
	</ul>
	<% } %>

	<% if (nick != null && nick.equals(author)) {%>
		<form action="/blog/servleti/author/<%=author%>/new" method="post">
			<input type="submit" value="Add new blog entry"/>
		</form>
	<% } %>
	
	<form action="/blog/servleti/main">
		<input type="submit" value="Back to main" /> <br>
	</form>
</body>
</html>