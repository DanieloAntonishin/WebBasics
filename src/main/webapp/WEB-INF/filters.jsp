<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Daniel
  Date: 17.10.2022
  Time: 12:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String fromDemoFilter= (String) request.getAttribute("DemoFilter") ;
  String[] users=(String[]) request.getAttribute("users");
%>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
</head>
<body>
    <%if(fromDemoFilter==null){%>
       <i>Запрос не проходил фильтрацию DemoFilter</i>
    <%} else {%>
    <i>DemoFilter передал <b><%=fromDemoFilter%></b></i>
    <% } %>
    <p>Информация о БД: <%=request.getAttribute("viewInfo")%></p>
</body>
<p>
    <%for (String user :users ) {%>
        <p><%= user%></p>
    <%}%>
</p>
</html>
