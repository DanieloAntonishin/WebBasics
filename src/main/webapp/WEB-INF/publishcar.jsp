<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String model = (String) request.getAttribute("model");
    String bodyType = (String) request.getAttribute("bodyType");
    String about = (String) request.getAttribute("about");
    String horsePower = (String) request.getAttribute("horsePower");
    String price = (String) request.getAttribute("price");
    String consumption = (String) request.getAttribute("consumption");
    String engineVolume = (String) request.getAttribute("engineVolume");
  /*  int year =  request.getAttribute("year")!=null ?Integer.parseInt((String) request.getAttribute("year")): 1;
    double price = request.getAttribute("price")!=null?Double.parseDouble((String) request.getAttribute("price")):1;
    double mileage = request.getAttribute("mileage")!=null?Double.parseDouble((String) request.getAttribute("mileage")):1;*/
    boolean complete=false;
    if(request.getAttribute("complete") != null){
        complete = (Boolean) request.getAttribute("complete");
    }
    String modelError = (String) request.getAttribute("modelError");
    String bodyTypeError = (String) request.getAttribute("bodyTypeError");
    String fileError = (String) request.getAttribute("fileError");
    String aboutError = (String) request.getAttribute("aboutError");
    String horsePowerError = (String) request.getAttribute("horsePowerError");
    String priceError = (String) request.getAttribute("priceError");
    String consumptionError = (String) request.getAttribute("consumptionError");
    String engineVolumeError = (String) request.getAttribute("engineVolumeError");

%>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card">
                <h2 class="card-title text-center">Add new car</h2>
                <div class="card-body py-md-4">
                    <form method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <%if(modelError!=null){ %> <b><%= modelError%></b> <%}%>
                            <input type="text" class="form-control" name="model" placeholder="Model name"
                                    <% if((model!=null&&modelError==null)&&!complete)
                                    { %> value="<%= model%>" <% } %> />
                            <br>
                            <%if(bodyTypeError!=null){ %> <b><%= bodyTypeError%></b> <%}%>
                            <input type="text" class="form-control" name="bodyType" placeholder="Type of car body"
                                    <% if((bodyType!=null&&bodyTypeError==null)&&!complete)
                                    { %> value="<%= bodyType%>" <% } %> />
                            <br>
                            <%if(aboutError!=null){ %> <b><%= aboutError%></b> <%}%>
                            <input type="text" class="form-control" name="about" placeholder="About car"
                                    <% if((about!=null&&aboutError==null)&&!complete)
                                    { %> value="<%= about%>" <% } %> />
                            <br>
                            <%if(horsePowerError!=null){ %> <b><%= horsePowerError%></b> <%}%>
                            <input type="text" class="form-control" name="horsePower" placeholder="Horse power of car"
                                    <% if((horsePower!=null&&horsePowerError==null)&&!complete)
                                    { %> value="<%= horsePower%>" <% } %> />
                            <br>
                            <%if(priceError!=null){ %> <b><%= priceError%></b> <%}%>
                            <input type="text" class="form-control" name="price" placeholder="Price"
                                    <% if((price!=null&&priceError==null)&&!complete)
                                    { %> value="<%= price%>" <% } %> />
                            <br>
                            <%if(consumptionError!=null){ %> <b><%= consumptionError%></b> <%}%>
                            <input type="text" class="form-control" name="consumption" placeholder="Consumption"
                                    <% if((consumption!=null&&consumptionError==null)&&!complete)
                                    { %> value="<%= consumption%>" <% } %> />
                            <br>
                            <%if(engineVolumeError!=null){ %> <b><%= engineVolumeError%></b> <%}%>
                            <input type="text" class="form-control" name="engineVolume" placeholder="Capacity of engine"
                                    <% if((engineVolume!=null&&engineVolumeError==null)&&!complete)
                                    { %> value="<%= engineVolume%>" <% } %> />
                            <br>
                            <%if(fileError!=null){ %> <b><%= fileError%></b> <%}%>
                            <input type="file" class="form-control" name="carPics">
                            <br>
                            <div class="d-flex flex-row align-items-center justify-content-between">
                                <button type="submit" class="btn btn-primary">Publish car</button>
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
<div class="successful">Car add to catalog on own site !<br><b><%= model%> </b></div></p>
<% } %>
</p>

