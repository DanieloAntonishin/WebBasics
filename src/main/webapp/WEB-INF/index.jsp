<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String home = request.getContextPath();
%>
<jsp:include page="/WEB-INF/headerfragment.jsp" />

</section>
<section>
    <div class="container-centre container-background-text">
        <img style="width:100%;height: auto;opacity: 0.6" src="<%= home%>/img/index_background.jpg"/>
        <div class="background-text">
            <h1 class="main-h1">Rental Car</h1>
            <h3 class="main-h3">Change scenery, not standards.<br>
                It’s time to reclaim your moments and shift from no time to go time.<br>
                Whether you crave a long vacation on the open road or love exploring<br>
                city streets while on a business trip, we’re here to help you go wherever<br>
                the road may lead in any car you choose. Let’s go!<br><br>
                Here you can get acquainted with the range of cars</h3>
            <b><a class="index-link" href="<%=home%>/carscatalog">Car catalog</a></b>
        </div>
    </div>
</section>
<jsp:include page="/WEB-INF/footer.jsp" />
</body>
</html>
