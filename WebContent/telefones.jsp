<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cadastro de telefones</title>
<link rel="stylesheet" type="text/css" href="resources/css/cadastro.css">
</head>
<body>
	<a href="acessoLiberado.jsp">Início</a>
	<center>
		<h1>Cadastro de telefones</h1>
		<h3 style="color: red;">${ msg }</h3>
	</center>
	<form action="salvarTelefones" method="post" id="formUser"
		onsubmit="return validarCampos()">
		<ul class="form-style-1">
			<li>
				<table>
					<tr>
						<td>user:</td>
						<td><input type="text" readonly="readonly" id="id" name="id"
							class="field-long" value="${ userEscolhido.id }"></td>
							
							<td>Nome:</td>
						<td><input type="text" readonly="readonly" id="nome" name="nome"
							value="${ userEscolhido.nome }"></td>
					</tr>
					<tr>
						<td>Número:</td>
						<td><input type="text" id="numero" name="numero"></td>
						<td>Tipo:</td>
						<td>
							<select id="tipo" name="tipo">
								<option>Casa</option>
								<option>Contato</option>
								<option>Celular</option>
							</select>
						</td>
					</tr>
					<tr>

						<td></td>
						<td><input type="submit" value="Salvar"></td>
					</tr>
				</table>
			</li>
		</ul>
	</form>

	<div class="container">
		<table class="responsive-table">
			<tr>
				<th>ID</th>
				<th>Número</th>
				<th>Tipo</th>
				<th>Delete</th>
			</tr>
			<c:forEach items="${ telefones }" var="fone">
				<tr>
					<td style="width: 150px"><c:out value="${ fone.id }"></c:out></td>
					<td style="width: 150px"><c:out value="${ fone.numero }"></c:out></td>
					<td><c:out value="${ fone.tipo }"></c:out></td>

					<td><a href="salvarTelefones?acao=deleteFone&foneId=${ fone.id }"><img
							src="resources/img/excluir.png" width="20px" height="20px"
							title="Excluir"></a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<script type="text/javascript">
		function validarCampos() {
			if (document.getElementById('numero').value == '') {
				alert('informe o número!');
				return false;
			} else if (document.getElementById('tipo').value == '') {
				alert('informe o tipo!');
				return false;
			}
			return true;
		}
	</script>
</body>
</html>