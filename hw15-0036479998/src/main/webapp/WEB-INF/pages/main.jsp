<%@page import="hr.fer.zemris.java.tecaj_13.model.BlogUser"%>
<%@page import="java.util.List"%>
<%@page import="hr.fer.zemris.java.tecaj_13.dao.DAOProvider"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String errorMsg = (String) request.getAttribute("error");

	String defaultNick = (String) request.getAttribute("defaultNick");
	if (defaultNick == null) {
		defaultNick = "";
	}

	List<BlogUser> users = DAOProvider.getDAO().getAllUsers();
	
	
	String nick = (String)request.getSession().getAttribute("current.user.nick");
	String firstName = (String)request.getSession().getAttribute("current.user.fn");
	String lastName = (String)request.getSession().getAttribute("current.user.ln");
%>

<html>
<body>

	<% if (nick == null) { %>
	Not logged in.
	<%} else {%>
	<%=lastName%>,
	<%=firstName%>
	<form action="/blog/servleti/logout/">
		<input type="submit" value="Logout" />
	</form>
	<%} %>

	<h1>MyBlog</h1>

	<% if (nick == null) { %>
	<b>Please login.</b>
	<% if (errorMsg != null && !errorMsg.isEmpty()) { %>
	<p>
		Error message:<%=errorMsg%></p>
	<%}%>

	<form action="/blog/servleti/main" method="POST">
		Nickname: <br> <input type="text" name="nick"
			value=<%=defaultNick%>><br> Password:<br> <input
			type="password" name="password"><br> <input
			type="submit" value="Login"> <input type="reset"
			value="Reset">
	</form>

	<form action="/blog/servleti/register" method="POST">
		<input type="submit" value="Register" />
	</form>

	<%} else {%>
	<p>
		Welcome
		<%=nick%></p>
	<form action="/blog/servleti/author/<%=nick%>" method="POST">
		<input type="submit" value="Go to my blog" />
	</form>
	<% } %>

	<% if (users == null || users.isEmpty()) { %>
	No users currently registered.
	<% } else { %>
	<b>Currently registered users:</b>
	<br>
	<ul>
		<% for(BlogUser user: users) { %>
		<li><a href="/blog/servleti/author/<%=user.getNick()%>/"> <%=user.getLastName()%>,
				<%=user.getFirstName()%> - <%=user.getNick() %>
		</a></li>
		<% } %>
	</ul>
	<% } %>

</body>
</html>