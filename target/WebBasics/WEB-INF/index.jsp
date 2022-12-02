<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="step.learning.services.DataService" %>
<%@ page import="java.sql.Statement" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String home = request.getContextPath();
    DataService dataService = (DataService) request.getAttribute("DataService");
%>
<jsp:include page="/WEB-INF/headerfragment.jsp" />

</section>
<hr>
<section>
    <a class="auth-sing-up" href="<%=home%>/publishcar">Add car</a>
    <a class="auth-sing-up" href="<%=home%>/carscatalog">Car catalog</a>
    <table>
        <tr><th>Id</th><th>Login</th><th>Name</th></tr>
    <%--<%String  sql="SELECT c.`id`,c.`model`,c.`price`,c.`pics` FROM Cars c";
    try(Statement statement=dataService.getConnection().createStatement()) {
        ResultSet res = statement.executeQuery(sql);
        while (res.next()) { %>
            <tr>
            <td><%=res.getString(1)%></td>
            <td><%=res.getString(2)%></td>
            <td><%=res.getFloat(3)%></td>
                 <img class="auth-fragment-avatar"
                           src="<%=home%>/image/<%=res.getString(4)%>"
                           alt="<%=res.getString(2)%>"/>
            </tr>
        <% }
        res.close();
    }catch (SQLException e) {
    throw new RuntimeException(e);
    }%>--%>
</table>
</section>
</body>
</html>
