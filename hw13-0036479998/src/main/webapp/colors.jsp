<%@ page session="true" %>

<%
	String pickedColor = (String) request.getSession()
			.getAttribute("pickedBgCol");
	if (pickedColor == null) {
		pickedColor = "white";
	}
%>

<html>

<body bgcolor="<%=pickedColor%>">
	<a href="/webapp2/setcolor?color=white">WHITE</a>
	<a href="/webapp2/setcolor?color=red">RED</a>
	<a href="/webapp2/setcolor?color=green">GREEN</a>
	<a href="/webapp2/setcolor?color=cyan">CYAN</a>
</body>
</html>