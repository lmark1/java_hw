<%@page import="hr.fer.zemris.java.tecaj_13.dao.DAOProvider"%>
<%@page import="hr.fer.zemris.java.tecaj_13.model.BlogEntry"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String errorMsg = (String) request.getAttribute("error");

String nick = (String)request.getSession().getAttribute("current.user.nick");
String firstName = (String)request.getSession().getAttribute("current.user.fn");
String lastName = (String)request.getSession().getAttribute("current.user.ln");

String author = (String)request.getAttribute("author");
String defaultText = (String)request.getAttribute("defaultText");
String defaultTitle = (String)request.getAttribute("defaultTitle");

if (defaultText == null || defaultText.isEmpty()) {
	defaultText = new String("Enter text here...");
}

if (defaultTitle == null || defaultTitle.isEmpty()) {
	defaultTitle = new String("Enter title here...");
}

String method = (String)request.getAttribute("method");
String methodText = null;
if (method != null && method.equals("new")) {
	methodText = new String("Add new entry.");
}
if (method != null && method.equals("edit")) {
	methodText = new String("Edit entry.");
}
Long entryID = (Long)request.getAttribute("entryID");
%>

<html>
<body>

	<% if (nick == null) { %>
		Not logged in. Permission denied.
	<%} else if (!nick.equals(author)){%>
		This is not Your blog. Permission denied.
	<%} else { %>
		<%=lastName%>,
		<%=firstName%>
		<form action="/blog/servleti/logout/">
			<input type="submit" value="Logout" />
		</form>
	
		<h1><%=methodText%></h1>
	
		<%
			if (errorMsg != null && !errorMsg.isEmpty()) {
		%>
		<p>
			Error message:
			<%=errorMsg%></p>
		<%}%>
		
		<form action="/blog/servleti/author/<%=nick%>/<%=method%>/" method="POST"
			id="entryform">
			Title: <br> <input type="text" name="title" value="<%=defaultTitle%>"> <br> 
			<input type="submit" value="<%=methodText%>"> 
			<input type="reset" value="Reset">
			<input type="hidden" name="entryID" value="<%=entryID%>">
		</form>
		<textarea rows="4" cols="50" name="etext" form="entryform"><%=defaultText%></textarea>
	<%} %>
</body>
</html>