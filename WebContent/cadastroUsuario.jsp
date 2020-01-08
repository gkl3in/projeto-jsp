<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cadastro de usuários</title>
<link rel="stylesheet" type="text/css" href="resources/css/cadastro.css">

<!-- Adicionando JQuery -->
<script src="https://code.jquery.com/jquery-3.4.1.min.js"
	integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
	crossorigin="anonymous"></script>
</head>
<body>
	<a href="acessoLiberado.jsp">Início</a>
	<center>
		<h1>Cadastro de usuário</h1>
		<h3 style="color: red;">${ msg }</h3>
	</center>
	<form action="salvarUsuario" method="post" id="formUser"
		onsubmit="return validarCampos()">
		<ul class="form-style-1">
			<li>
				<table>
					<tr>
						<td>Código:</td>
						<td><input type="text" readonly="readonly" id="id" name="id"
							value="${ user.id }"></td>
					</tr>
					<tr>
						<td>Login:</td>
						<td><input type="text" id="login" name="login"
							value="${ user.login }"></td>
					</tr>
					<tr>
						<td>Senha:</td>
						<td><input type="password" id="senha" name="senha"
							value="${ user.senha }"></td>
					</tr>
					<tr>
						<td>Nome:</td>
						<td><input type="text" id="nome" name="nome"
							value="${ user.nome }"></td>
					</tr>
					<tr>
						<td>Telefone:</td>
						<td><input type="text" id="fone" name="fone"
							value="${ user.fone }"></td>
					</tr>
					<tr>
						<td>CEP:</td>
						<td><input type="text" id="cep" name="cep"
							onblur="consultaCep();"></td>
					</tr>
					<tr>
						<td>Rua:</td>
						<td><input type="text" id="rua" name="rua"
							onblur="consultaCep();"></td>
					</tr>
					<tr>
						<td>bairro:</td>
						<td><input type="text" id="bairro" name="bairro"
							onblur="consultaCep();"></td>
					</tr>
					<tr>
						<td>cidade:</td>
						<td><input type="text" id="cidade" name="cidade"
							onblur="consultaCep();"></td>
					</tr>
					<tr>
						<td>uf:</td>
						<td><input type="text" id="uf" name="uf"
							onblur="consultaCep();"></td>
					</tr>
					<tr>

						<td></td>
						<td><input type="submit" value="Salvar"> <input
							type="submit" value="Cancelar"
							onclick="document.getElementById('formUser').action= 'salvarUsuario?acao=reset'"></td>
					</tr>
				</table>
			</li>
		</ul>
	</form>

	<div class="container">
		<table class="responsive-table">
			<tr>
				<th>ID</th>
				<th>Usuário</th>
				<th>Nome</th>
				<th>Telefone</th>
				<th>Delete</th>
				<th>Editar</th>
			</tr>
			<c:forEach items="${ usuarios }" var="user">
				<tr>
					<td style="width: 150px"><c:out value="${ user.id }"></c:out></td>
					<td style="width: 150px"><c:out value="${ user.login }"></c:out></td>
					<td><c:out value="${ user.nome }"></c:out></td>
					<td><c:out value="${ user.fone }"></c:out></td>

					<td><a href="salvarUsuario?acao=delete&user=${ user.id }"><img
							src="resources/img/excluir.png" width="20px" height="20px"
							title="Excluir"></a></td>
					<td><a href="salvarUsuario?acao=editar&user=${ user.id }"><img
							src="resources/img/editar.png" width="20px" height="20px"
							title="Editar"></a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<script type="text/javascript">
		function validarCampos() {
			if (document.getElementById('formUser').action == "http://localhost:8080/projeto-jsp/salvarUsuario?acao=reset") {
				return true;
			} else if (document.getElementById('login').value == '') {
				alert('Informe o Login!');
				return false;
			} else if (document.getElementById('nome').value == '') {
				alert('Informe o nome!');
				return false;
			} else if (document.getElementById('senha').value == '') {
				alert('Informe o senha!');
				return false;
			} else {
				return true;
			}
		}
		
		function limpa_formulário_cep() {
            // Limpa valores do formulário de cep.
            $("#rua").val("");
            $("#bairro").val("");
            $("#cidade").val("");
            $("#uf").val("");
            $("#ibge").val("");
        }
		
		function consultaCep(){
            var cep = $("#cep").val().replace(/\D/g, '');

            if (cep != "") {
                var validacep = /^[0-9]{8}$/;

                if(validacep.test(cep)) {
                    $("#rua").val("...");
                    $("#bairro").val("...");
                    $("#cidade").val("...");
                    $("#uf").val("...");
                    $("#ibge").val("...");

                    //Consulta o webservice viacep.com.br/
                    $.getJSON("https://viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {

                        if (!("erro" in dados)) {
                            $("#rua").val(dados.logradouro);
                            $("#bairro").val(dados.bairro);
                            $("#cidade").val(dados.localidade);
                            $("#uf").val(dados.uf);
                            $("#ibge").val(dados.ibge);
                        }
                        else {
                            limpa_formulário_cep();
                            alert("CEP não encontrado.");
                        }
                    });
                }
                else {
                    limpa_formulário_cep();
                    alert("Formato de CEP inválido.");
                }
            }
            else {
                limpa_formulário_cep();
            }
        }
	</script>
</body>
</html>