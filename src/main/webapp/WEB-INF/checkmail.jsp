<%@ page import="step.learning.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8"  %>
<%
    User authUser = (User) request.getAttribute("AuthUser");
    String home = request.getContextPath();
    String confirm = (String) request.getAttribute("confirm");
    String confirmError = (String) request.getAttribute("confirmError");
%>
<div class="check-mail">
    <% if(confirm!=null) { /* почта подтверждена */%>
        <b>Почта подтверждена</b>
    <% } else {%>
    <% if(authUser==null) { /* Пользователь не авторизован */ %>
        <h2> Авторизируйтесь (логин и пароль в верхней панели) </h2>
    <% } else if(authUser.getEmailCode()==null) { /* подтверждение не требуется*/ %>
        <h2> Почта подтверждена, для изминения перейдите <a href="<%=home%>/profile">личный в кабинет </a></h2>
    <% } else if(authUser.getEmailCodeAttempts()>3) { /* превышено кол-во попыток*/ %>
    <h2>Превышено количество попыток. Меняй почту и давай по новой</h2>
    <img class="imgBan" src="<%= home%>/img/ban.jpg"/>
    <% } else { /* подтверждение трубуется */ %>
        <h1>Подтверждаем почту</h1>
        <form>
            <div class="profile-field">
            <label>Введите код из сообщения в электронной почте<br>
                <input type="text" name="confirm"></label>
                <br>
                <input type="image" src="<%= home%>/img/submit.png" id="user-profile-button"/>
            </div>
        </form>
    <% } %>
    <% if(confirmError!=null) { %>
        <b><%= confirmError %></b>
    <% } %>
    <% } %>
</div>
