<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String errorMsg = (String) request.getAttribute("error");
%>

<html>
<body>
	
	<h1>Registration page</h1>
	<p>Please fill out registration form.</p>
	
	<%
		if (errorMsg != null && !errorMsg.isEmpty()) {
	%>
	<p>
		Error message:
		<%=errorMsg%></p>
	<%}%>
	
	<form action="/blog/servleti/register" method="POST">
		First name: <br> 
		<input type="text" name="fn"> <br> 
		Last name: <br> 
		<input type="text" name="ln"> <br>
		E-mail: <br> 
		<input type="text" name="email"> <br>  
		Nickname: <br> 
		<input type="text" name="nick"> <br> 
		Password:<br> 
		<input type="password" name="password"><br> 
		<input type="submit" value="Register"> 
		<input type="reset" value="Reset">
	</form>

</body>
</html>