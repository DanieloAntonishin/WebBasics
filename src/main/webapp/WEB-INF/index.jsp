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
    <div class="container-centre">
    <h1>Rental Car</h1>
    <h3>Change scenery, not standards.
        It’s time to reclaim your moments and shift from no time to go time.<br>
        Whether you crave a long vacation on the open road or love exploring<br>
        city streets while on a business trip, we’re here to help you go wherever<br>
        the road may lead in any car you choose. Let’s go!</h3>
    </div>
</section>
</body>
</html>
