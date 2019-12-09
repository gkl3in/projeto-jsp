<jsp:useBean id="calcula" class="beans.BeanCursoJsp" type="beans.BeanCursoJsp" scope="page"></jsp:useBean>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:setProperty property="*" name="calcula"/>
	<h3>Seja bem vindo ao sistema em JSP</h3>
	
	<a href="salvarUsuario?acao=listartodos"><img src="resources/img/user-female-icon.png" width="100px" height="100px" title="Cadastro de usuários"></a>
</body>
</html>