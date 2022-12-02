<%@ page import="step.learning.entities.User" %>
<%@ page import="step.learning.entities.Cars" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String home = request.getContextPath();
    User authUser=(User) request.getAttribute("AuthUser");
    Cars car=(Cars) request.getAttribute("Car");
%>
<div class="user-profile">
    <div class="menu">
    <section class="user-section-left">
        <div class="container-centre">
            <img class="user-profile-img" src="<%=home%>/image/<%=authUser.getAvatar()%>" alt="<%=authUser.getLogin()%>"/>
            <h2><%=authUser.getLogin()%></h2>
            <div class="margin-top">
                <a href="#">Log out</a>
            </div>
            <% if(car != null) {%>
            <div class="margin-top">
                <p>Car in rent</p>
                <div>
                    <img class="auth-fragment-avatar" src="<%=home%>/image/<%=car.getPics()%>" alt="<%=car.getModel()%>"/>
                    <b data-field-name="carId"><%=car.getModel()%></b>
                    <button id="remove-rent-car">Remove from rent</button>
                </div>
            </div>
            <% } %>
        </div>
    </section>
    <section class="user-section-right">
        <div class="container-centre">
           <h2>Account Settings</h2>
            <div class="margin-top">
                <div class="input-user-block">
                    <div class="profile-field">
                    <p>Имя: <span data-field-name="name"><%=authUser.getName()%></span></p>
                    <p>Логин:  <span data-field-name="login"><%=authUser.getLogin()%></span></p>
                    <p id="email">Email:  <span data-field-name="email"><%=authUser.getEmail()%></span></p>
                        <% if(authUser.getEmailCodeAttempts()>3) {%>
                            <p class="not-confirm">Превышено количество попыток подтверждить код. Меняй почту и давай по новой</p>
                        <% } %>
                    </div>
                </div>
                <div class="profile-field">
                    <span>Аватарка:</span>
                    <input class="form-group" type="file" id="avatar-input" alt="avatar-input"/>
                    <br>
                    <input type="image" src="<%= home%>/img/submit.png" id="user-profile-button"/>
                </div>
                <p style="border: 2px solid cadetblue;margin: 10px">
                    <label>Пароль : <input type="password" name=""></label><br>
                    <label>Повтор : <input type="password" name=""></label><br>
                    <button id="change-pass-button">Update</button>
                </p>
            </div>
        </div>
    </section>
    </div>
</div>
<script>
    <% if(authUser.getEmailCode()!=null) { %>
        let elem =  document.getElementById("email")
        elem.setAttribute("class","not-confirm")
    <% if(authUser.getEmailCodeAttempts()<3) { %>
        let block=document.createDocumentFragment()
        let br = document.createElement("br")

        let link = document.createElement("a")
        link.innerHTML="Confirm email"
        link.className="js-link"
        link.href="<%=home%>/checkmail/"

        block.appendChild(br)
        block.appendChild(link)
        elem.appendChild(block)
    <% } } else { %>
          document.getElementById("email").setAttribute("class","confirm")
    <% } %>
    document.addEventListener("DOMContentLoaded",()=> {
        const changePassButton=document.querySelector("#change-pass-button")
        changePassButton.addEventListener('click',changePassClick)

        const removeRentCarButton=document.querySelector("#remove-rent-car")
        removeRentCarButton.addEventListener('click',removeRentClick)

        const avatarSaveButton=document.querySelector("#user-profile-button")
        avatarSaveButton.addEventListener('click',avatarSaveClick)

        for (let nameElement of document.querySelectorAll(".profile-field span")){
            nameElement.addEventListener("click", nameClick)
            nameElement.addEventListener("blur", nameBlur)
            nameElement.addEventListener("keydown", nameKeyDown)
        }
    })
    let nameKeyDown=(e)=>{
        if(e.keyCode==13)
        {
            e.preventDefault()
            //nameBlur(e)
            e.target.blur()
            return false
        }
    }
    let removeRentClick=()=>{
        <% if(car != null) {%>
        const url="/WebBasics_war_exploded/publishcar?id=<%=car.getId()%>&isRented=false"
        fetch(url,{
            method:"PUT",
            headers:{},
            body:""
        }).then(r=>r.text())
            .then(t=>{
                console.log(t)
                if(t==="OK")
                {
                    location=location;
                }
            })
        <% } %>
    }
    let changePassClick=(e)=>{
        let passwords = e.target.parentNode.querySelectorAll('input[type="password"]')
        if(passwords[0].value !== passwords[1].value)
        {
            alert("Папроли не совпадают")
            passwords[0].value=passwords[1].value=''
            return
        }
        if(passwords[0].value.length < 3)
        {
            alert("Пароль слишком короткий")
            passwords[0].value=passwords[1].value=''
            return
        }

        //console.log(passwords[0].value)
        const url="/WebBasics_war_exploded/registration?password="+ passwords[0].value
        fetch(url,{
            method:"PUT",
            headers:{},
            body:""
        }).then(r=>r.text())
            .then(t=> {
                let elem=document.createElement("p")
                if(t==="OK")
                {
                    elem.className="confirm"
                    elem.innerHTML="Password changed successfully"
                }
                else
                {
                    elem.className="not-confirm"
                    elem.innerHTML="Password changed unsuccessful. Try again"
                }
                e.target.parentNode.appendChild(elem)
                console.log(t)
            })
        passwords[0].value=passwords[1].value=''
    }
    let avatarSaveClick=()=>{
        const avatarInput=document.querySelector("#avatar-input")
        if(!avatarInput) throw "#avatarSaveInput Not found"
        if(avatarInput.files.length===0)
        {
            alert("Select a file")
            return
        }
        let formData=new FormData()
        formData.append("userAvatar",avatarInput.files[0])
        fetch("/WebBasics_war_exploded/registration",{
            method:"PUT",
            headers:{},
            body:formData  // наличие файла в formData автоматически сформирует multipart запрос
        }).then(r=>r.text())
            .then(t=> {
                console.log(t)
                if(t==="OK")
                {
                    location=location;
                }
                else{
                    alert(t);
                }
            })
    }
    let nameClick=(e)=>{
        e.target.setAttribute("contenteditable","true")
        e.target.focus();
        e.target.savedText=e.target.innerText
    }
    let nameBlur=(e)=>{
        e.target.removeAttribute("contenteditable")
        if( e.target.savedText !== e.target.innerText)
        {
            if(confirm("Сохранить изменения? ")){
                const fieldName=e.target.getAttribute("data-field-name")
                const url="/WebBasics_war_exploded/registration?"+fieldName+"="+e.target.innerText
                 //console.log(url); return;
                fetch(url,{
                    method:"PUT",
                    headers:{},
                    body:""
                }).then(r=>r.text())
                    .then(t=>{
                        console.log(t)
                        if(t==="OK")
                        {
                            location=location;
                        }
                        else{
                            alert(t);
                            e.target.innerText=e.target.savedText
                        }
                    }) // ok or error
            }
            else{
                e.target.innerText=e.target.savedText
            }
        }
    }
</script>


