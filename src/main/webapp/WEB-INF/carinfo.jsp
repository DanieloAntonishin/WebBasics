<%@ page import="step.learning.entities.User" %>
<%@ page import="step.learning.entities.Cars" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String home = request.getContextPath();
    User authUser = (User) request.getAttribute("AuthUser");
    Cars car = (Cars) request.getAttribute("Car");
%>
<div>
    <div class="menu">
        <section class="car-section-left">
            <div class="container-centre">
                <img class="car-img" src="<%=home%>/image/<%=car.getPics()%>" alt="<%=car.getModel()%>">
            </div>
        </section>
        <section class="car-section-right">
                <p><h4><span data-field-name="model" ><%=car.getModel()%></span></h4></p>
                <p>Body Type: <span data-field-name="bodyType" ><%=car.getBodyType()%></span></p>
                <p>Horse Power: <span data-field-name="horsePower" ><%=car.getHorsePower()%></span></p>
                <p>Engine Volume: <span data-field-name="engineVolume"><%=car.getEngineVolume()%></span></p>
                <p>Consumption: <span data-field-name="consumption"><%=car.getConsumption()%></span></p>
                <p>Price: <span data-field-name="price"><%=car.getPrice()%> </span>/ per day</p>
                <p>Additional information about car: <span data-field-name="about"><%=car.getAbout()%></span></p>
                <hr>
                <div class="container-centre">
                    <input type="hidden" name="carId" value="<%=car.getId()%>">
                    <%if(authUser == null) { %>
                        <div>You must be logged in</div>
                        <button id="rent-button" class="car-card-button"  disabled>Rent a car</button>
                    <% } else { %>
                        <button id="rent-button" class="car-card-button" >Rent a car</button>
                    <% } %>
                </div>
        </section>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded",()=> {
        <% if(authUser!=null&&authUser.getLogin().equals("person")) { %>
            for (let nameElement of document.querySelectorAll(".car-section-right span")){
                nameElement.addEventListener("click", nameClick)
                nameElement.addEventListener("blur", nameBlur)
                nameElement.addEventListener("keydown", nameKeyDown)
            }
        <% } else { %>
        const rentCarButton=document.querySelector("#rent-button")
        rentCarButton.addEventListener('click',rentCarClick)
        <% } %>
    })
    let rentCarClick=()=>{

        let menu = document.querySelector(".menu");
        let p = document.createElement("p")
        alert("hi")
        p.innerHTML="Car rented successful"
        menu.appendChild(p)
        document.querySelector("#rent-button").disable = true
        const url="/WebBasics_war_exploded/publishcar?id=<%=car.getId()%>&isRented=true"
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
    }
    let nameKeyDown=(e)=>{
        if(e.keyCode==13)
        {
            e.preventDefault()
            //nameBlur(e)
            e.target.blur()
            return false
        }
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
                const url="/WebBasics_war_exploded/publishcar?id=<%=car.getId()%>&"+fieldName+"="+e.target.innerText
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