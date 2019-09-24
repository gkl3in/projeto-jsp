<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%@ page isErrorPage="true" %>
	
	<%= exception %>
	<% out.print(request.getParameter("nome")); %>
	<%= session.getAttribute("curso") %>
	
		<!--
		ANTIGO INDEX
	<%@ page import="java.util.Date" %>
	<%@ include file="pagina-include.jsp" %>
	<%@ page errorPage="receber-nome.jsp" %>
	
	<%= application.getInitParameter("Estado") %>
	<myprefix:minhatag/>
	<form action="receber-nome.jsp">
		<input type="text" name="nome" id="nome"/>
		<input type="submit" value="Enviar">
	</form>
	<% session.setAttribute("curso", "curso de JSP"); %>
	<jsp:forward page="receber-nome.jsp"></jsp:forward>
	-->
</body>
</html>