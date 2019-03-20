<%@ page session="true"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String pickedColor = (String) request.getSession()
			.getAttribute("pickedBgCol");
	if (pickedColor == null) {
		pickedColor = "white";
	}
%>

<html>
<body bgcolor="<%=pickedColor%>">
	<p>
		<a href="colors.jsp">Background color choser</a>
	</p>
	<p>
		<a href="/webapp2/trigonometric?a=0&b=90">Trigonometry table</a>
	</p>
	<form action="trigonometric" method="GET">
		Početni kut:<br>
		<input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br>
		<input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset"
			value="Reset">
	</form>
	<p>
		<a href="/webapp2/funny-story">Funny story</a>
	</p>
	<p>
		<a href="/webapp2/response">Response</a>
	</p>
	<p>
		<a href="/webapp2/power?a=1&b=100&n=3">Calculate powers of 1 and
			100</a>
	</p>
	<p>
		<a href="/webapp2/info">Application information</a>
	</p>
	<p>
		<a href="/webapp2/glasanje">Glasanje</a>
	</p>
</body>
</html>