<jsp:useBean id="calcula" class="beans.BeanCursoJsp" type="beans.BeanCursoJsp" scope="page"></jsp:useBean>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="myprefix" uri="WEB-INF/testetag.tld" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="resources/css/estilo.css">

</head>
<body>
	<div class="login-page">
		<center><h4>Projeto didático</h4></center>
  		<div class="form">
			<form action="LoginServlet" method="post" class="login-form">
				<input type="text" placeholder="login" id="login" name="login">
				<br/>
				<input type="text" placeholder="senha" id="senha" name="senha">
				<br/>
				<button type="submit" value="Logar">logar</button>
			</form>
	  	</div>
	</div>
</body>
</html>