<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String name = (String) request.getAttribute("name");
    String login = (String) request.getAttribute("login");
    String email = (String) request.getAttribute("email");
    boolean complete=false;
    if(request.getAttribute("complete") != null){
        complete = (Boolean) request.getAttribute("complete");
    }
    String nameError = (String) request.getAttribute("nameError");
    String loginError = (String) request.getAttribute("loginError");
    String emailError = (String) request.getAttribute("emailError");
    String fileError = (String) request.getAttribute("fileError");
    String passwordError = (String) request.getAttribute("passwordError");
    String confPassError = (String) request.getAttribute("confPassError");

%>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card">
                <h2 class="card-title text-center">Register</h2>
                <div class="card-body py-md-4">
                    <form method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <%if(nameError!=null){ %> <b><%= nameError%></b> <%}%>
                            <input type="text" class="form-control" name="name" placeholder="Name"
                            <% if((name!=null&&nameError==null)&&!complete) { %> value="<%= name%>" <% } %> />
                        <br>
                            <%if(loginError!=null){ %> <b><%= loginError%></b> <%}%>
                            <input type="text" class="form-control" name="login" placeholder="Login"
                            <% if((login!=null&&loginError==null)&&!complete) { %> value="<%= login%>" <% } %> />
                        <br>
                            <%if(emailError!=null){ %> <b><%= emailError%></b> <%}%>
                            <input type="email" class="form-control" name="email" placeholder="Email"
                            <% if((email!=null&&emailError==null)&&!complete) { %> value="<%= email%>" <% } %> />
                        <br>
                            <%if(passwordError!=null){ %> <b><%= passwordError%></b> <%}%>
                            <input type="password" class="form-control" name="password" placeholder="Password">
                        <br>
                            <%if(confPassError!=null){ %> <b><%= confPassError%></b> <%}%>
                            <input type="password" class="form-control" name="confirmPassword" placeholder="Confirm password">
                        <br>
                            <%if(fileError!=null){ %> <b><%= fileError%></b> <%}%>
                            <input type="file" class="form-control" name="userAvatar">
                        <br>
                        <div class="d-flex flex-row align-items-center justify-content-between">
                            <button type="submit" class="btn btn-primary">Create Account</button>
                        </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<p>
    <% if(complete) { %>
    <div class="successful">Congratulate with registration on own site !<br><b><%= login%> </b></div></p>
    <% } %>
</p>

