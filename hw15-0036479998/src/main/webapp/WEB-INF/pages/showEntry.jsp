<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String nick = (String) request.getSession()
			.getAttribute("current.user.nick");
	String firstName = (String) request.getSession()
			.getAttribute("current.user.fn");
	String lastName = (String) request.getSession()
			.getAttribute("current.user.ln");
	String author = (String) request.getAttribute("author");
%>
<html>
<body>

	<%
		if (nick == null) {
	%>
	Not logged in.
	<br>
	<%
		} else {
	%>
	<%=lastName%>,
	<%=firstName%>
	<form action="/blog/servleti/logout/">
		<input type="submit" value="Logout" /> <br>
	</form>
	<%
		}
	%>

	<c:choose>
		<c:when test="${blogEntry==null}">
      	Entry does not exist. <br>
      	<form action="/blog/servleti/main/"
				method="get">
				<input type="submit" value="Go back" /> <br>
			</form>
		</c:when>
		<c:otherwise>
			<h1>
				<c:out value="${blogEntry.title}" />
			</h1>
			<p>
				<c:out value="${blogEntry.text}" />
			</p>
			<c:if test="${!blogEntry.comments.isEmpty()}">
				<ul>
					<c:forEach var="e" items="${blogEntry.comments}">
						<li><div style="font-weight: bold">
								[Korisnik=
								<c:out value="${e.usersEMail}" />
								]
								<c:out value="${e.postedOn}" />
							</div>
							<div style="padding-left: 10px;">
								<c:out value="${e.message}" />
							</div></li>
					</c:forEach>
				</ul>
			</c:if>

			<%
				if (nick != null && nick.equals(author)) {
			%>
			<form action="/blog/servleti/author/<%=author%>/edit" method="post">
				<input type="submit" value="Edit this blog entry" /> <input
					type="hidden" name="entryID" value="${blogEntry.id}">
			</form>
			<%
				} else {
			%>
			<p>You don't have permission to edit this entry.</p>
			<%
				}
			%>

			<p>Enter a new comment:</p>
			<textarea rows="4" cols="50" name="ctext" form="commform">Enter text here...</textarea>
			<form action="/blog/servleti/comment" method="POST" id="commform">
				<input type="hidden" name="entryID" value="${blogEntry.id}">
				<input type="submit" value="Add new comment"> <input
					type="reset" value="Reset">
			</form>

			<form action="/blog/servleti/author/${blogEntry.creator.nick}/"
				method="post">
				<input type="submit" value="Go back" /> <br>
			</form>
		</c:otherwise>
	</c:choose>

</body>
</html>