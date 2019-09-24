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
</head>
<body>

	<form action="LoginServlet" method="post">
	
		<input type="text" id="login" name="login">
		<br/>
		<input type="text" id="senha" name="senha">
		<br/>
		<input type="submit" value="Logar"/>
	</form>
</body>
</html>