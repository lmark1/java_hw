<%@page import="java.util.Random"%>
<%@ page session="true" %>

<%
	String pickedColor = (String) request.getSession()
			.getAttribute("pickedBgCol");
	if (pickedColor == null) {
		pickedColor = "white";
	}
	
	Random rand = new Random();
	String[] colors = new String[]  {
		"black", "blue", "red", "yellow", "cyan", "green", "magenta", 
		"olive", "purple"
	};
	
	int index = rand.nextInt(colors.length);
	String randomColor = colors[index];
%>

<body bgcolor="<%=pickedColor%>">
	<font color="<%=randomColor%>">
	Funny story: My programming skills. 
	...
	hahahahhaha
	:(
	</font>
</body>