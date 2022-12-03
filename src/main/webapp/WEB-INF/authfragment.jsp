<%@ page import="step.learning.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String home = request.getContextPath();
    String authError=(String) request.getAttribute("AuthError");
    User authUser=(User) request.getAttribute("AuthUser");
%>
<div class="auth-fragment">
    <div class="container-centre">
    <% if(authUser ==null) { %>
    <form method="post" action="">
        <lable>Login: <input type="text" name="userLogin"></lable>
        <lable>Password: <input type="password" name="userPassword"></lable>
        <button type="submit" >Auth</button>
    </form>
    <p><%if(authError!=null) { %></p>
    <div class="auth-error"><%=authError%></div>
    <% }} else {%>
    <form method="get" action="" >
    <span>Hello, </span>
    <b><%= authUser.getName()%></b>
        <a href="<%=home%>/profile" class="auth-profile-a">
         <img class="auth-fragment-avatar"
         src="<%=home%>/image/<%=authUser.getAvatar()%>"
         alt="<%=authUser.getLogin()%>"/></a>
<%-- region Если почта требует подтверждения, то выводим ссылку --%>
        <% if(authUser.getEmailCodeAttempts() > 3) {%>
            <span>Превышено количество попыток подтвердить код!</span>
        <% } else if(authUser.getEmailCode() != null) { %>
         <div class="inline">
            <button class="email-icon" title="Почта не подтверждена">&#x1F4E8;</button>
         </div>
        <% } %>
<%-- endgion --%>
        <button type="submit" name="logOut" value="Yes" >Log out</button>
    </form>
    <%}%>
    </div>
</div>

<script >
    let email=document.querySelector(".email-icon")
    let parentNode=email.parentNode
    let emailCodeButton = null
    let userId=null
    <% if(authUser!=null) {%>
    userId='<%=authUser.getId()%>'
    <% } %>
    email.onclick = ()=> {
        parentNode.innerHTML=`<lable>Введите код подтверждения почты<input type="text" name="confirm">
        <a class="submitEmailLink" >Send</a></label>`;
        emailCodeButton = document.querySelector(".submitEmailLink");
        emailCodeButton.addEventListener('click',emailCodeClick)
    }
    let emailCodeClick=()=>{
        console.log("w")
        let fieldName = document.getElementsByName("confirm")[0].value
        const url="/WebBasics_war_exploded/checkmail/?userid="+userId+"&confirm="+fieldName
        //console.log(url); return;
        fetch(url,{
            method:"GET",
            headers:{}
        }).then(() => {location.reload()})
    }

</script>