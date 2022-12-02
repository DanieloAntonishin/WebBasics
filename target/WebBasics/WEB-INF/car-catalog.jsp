<%@ page import="step.learning.entities.User" %>
<%@ page import="step.learning.entities.Cars" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String home = request.getContextPath();
    String rentMessage = (String) session.getAttribute("rentMessage");
    List<Cars> carsList = (List<Cars>) request.getAttribute("CarsList");
%>
<div class="car-card-catalog">
    <%for(Cars car :carsList) { %>
    <div <% if(car.isActive()) { %> class="car-card-active" <% } else { %>  class="car-card-disable" <% } %>
        <h4><%=car.getModel()%></h4>
        <img class="car-card-img" src="<%=home%>/image/<%=car.getPics()%>" alt="<%=car.getModel()%>">
        <p class="car-card-text">Body Type: <%=car.getBodyType()%> | Price: <%=car.getPrice()%></p>
        <a class="car-card-link" href="<%=home%>/carinfo?id=<%=car.getId()%>">
           <% if(car.isActive()) { %>  Rent a car <% } else { %>  Car is already rented <% } %>
        </a>
    </div>
    <% } %>
<% if(rentMessage != null ) { %>
<h4><p><%=rentMessage%></p></h4>
<% } %>
</div>
