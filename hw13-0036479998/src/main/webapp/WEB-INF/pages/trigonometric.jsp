<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>

<%
	String pickedColor = (String) request.getSession()
			.getAttribute("pickedBgCol");
	if (pickedColor == null) {
		pickedColor = "white";
	}
%>

<body bgcolor="<%=pickedColor%>">
	<table border="1">
		<tr>
			<th>Angle</th>
			<th>Sine</th>
			<th>Cosine</th>
		</tr>

		<c:forEach var="entry" items="${result}">
			<tr>
				<td>${entry.angle}</td>
				<td>${entry.sin}</td>
				<td>${entry.cos}</td>
			</tr>
		</c:forEach>

	</table>
</body>