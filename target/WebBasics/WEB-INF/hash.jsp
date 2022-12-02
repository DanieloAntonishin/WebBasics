<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String md5Input=(String) request.getAttribute("md5HashUserInput");
    String sha1Input=(String) request.getAttribute("sha1HashUserInput");
%>
<jsp:include page="/WEB-INF/headerfragment.jsp" />
<section class="hash">
    <form method="post">
        <h1>Encrypt your input</h1>
        <input name="userInput" type="text" placeholder="Input text for hash here!"/>
        <button type="submit">Send</button>
    </form>
    <%if(!(md5Input==null&&sha1Input==null)) {%>
    <section>
        <h3>Encrypted your input</h3>
        <p>Md-5 hash = <%=md5Input%></p>
        <p>Sha-1 hash = <%=sha1Input%></p>
    </section>
    <% } %>
</section>
</body>
</html>
