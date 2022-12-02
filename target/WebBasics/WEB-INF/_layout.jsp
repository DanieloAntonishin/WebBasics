<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageBody="/WEB-INF/"+request.getAttribute("pageBody");
    String home = request.getContextPath();
%>
<!doctype html>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<%=home%>/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/authfragment.jsp" />
    <jsp:include page="<%=pageBody%>"/>
    <jsp:include page="/WEB-INF/footer.jsp" />
</body>
</html>
