<%@page import="hr.fer.zemris.java.p12.model.PollEntry"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%
  List<PollEntry> polls = (List<PollEntry>)request.getAttribute("polls");
%>
<html>
  <body>

  <b>Currently available polls:</b><br>

  <% if(polls.isEmpty()) { %>
    No polls currently available.
  <% } else { %>
    <ul>
    <% for(PollEntry entry : polls) { %>
    <li>
    <a href="/voting-app/servleti/glasanje?pollID=<%=entry.getId()%>"><%=entry.getTitle()%></a>
    </li>  
    <% } %>  
    </ul>
  <% } %>

  </body>
</html>