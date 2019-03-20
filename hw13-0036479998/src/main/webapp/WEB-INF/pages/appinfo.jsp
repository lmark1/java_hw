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

<%
		Long startTime = (Long) session.getServletContext()
				.getAttribute("start");

	String sb = null;
	if (startTime == null) {
		sb = "ne mogu dohvatiti start time";
	} else {
		long currentTime = System.currentTimeMillis();
		long delta = currentTime - startTime;

		sb = new String();

		// Calculate days
		int days = (int) (delta / (1000 * 60 * 60 * 24));
		if (days != 0) {
			sb += (days + " Days");
			delta = delta - days * 1000 * 60 * 60 * 24;
		}

		// Calculate hours
		int hours = (int) (delta / (1000 * 60 * 60));
		if (hours != 0) {
			if (sb.length() != 0) {
				sb += (", ");
			}
			sb += (hours + " Hours");
			delta = delta - hours * 1000 * 60 * 60;
		}
		

		// Calculate minutes
		int minutes = (int) (delta / (1000 * 60));
		if (minutes != 0) {
			if (sb.length() != 0) {
				sb += (", ");
			}
			sb += (minutes + " Minutes");
			delta = delta - minutes * 1000 * 60;
		}
		

		// Calculate seconds
		int seconds = (int) (delta / 1000);
		if (seconds != 0) {
			if (sb.length() != 0) {
				sb += (", ");
			}
			sb += (seconds + " Seconds");
			delta = delta - seconds * 1000;
		}
		

		// Calculate miliseconds
		if (delta != 0) {
			if (sb.length() != 0) {
				sb += (", ");
			}
			sb += (delta + " Miliseconds");
		}
	}

	String currentDelta = sb;
%>

<body bgcolor="<%=pickedColor%>">
	<p>Time elapsed from application start:</p>
	<p><%=currentDelta%></p>
</body>