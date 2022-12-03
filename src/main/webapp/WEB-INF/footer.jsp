<%@ page import="step.learning.services.DataService" %>
<%@ page import="step.learning.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String home = request.getContextPath();
    User authUser=(User) request.getAttribute("AuthUser");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="footer">
    <a class="footer-link" href="<%=home%>/">Home</a>
    <%if(authUser!=null && authUser.getLogin().equals("admin")) { %>
    <a class="footer-link" href="<%=home%>/publishcar">Add car</a>
    <% } %>
    <a class="footer-link" href="<%=home%>/carscatalog">Car catalog</a>
    <a class="footer-link" href="<%=home%>/registration">Sign Up</a>
</div>
</body>
</html>
