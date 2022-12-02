<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userInput=(String) request.getAttribute("userInput");
%>
<jsp:include page="/WEB-INF/headerfragment.jsp" />
    <h1>Servlet API</h1>
    <img src="https://oracle-patches.com/images/63/servlet-java.jpg"  alt=""/>
    <form method="post">
        <label>Введите строку :<input name="userInput"/></label>
        <input type="submit" value="Отправить">
    </form>
    <p>
        <% if(userInput!=null) { %>
        Ранее было введено <b><%= userInput%></b>
        <% } %>
    </p>
</body>
</html>
