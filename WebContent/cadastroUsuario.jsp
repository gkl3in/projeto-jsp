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
		onsubmit="return validarCampos()" enctype="multipart/form-data">
		<ul class="form-style-1">
			<li>
				<table>
					<tr>
						<td>Código:</td>
						<td><input type="text" readonly="readonly" id="id" name="id"
							value="${ user.id }"></td>

						<td>CEP:</td>
						<td><input type="text" id="cep" name="cep"
						onblur="consultaCep();" value="${ user.cep }"></td>
					</tr>
					<tr>
						<td>Login:</td>
						<td><input type="text" id="login" name="login"
							value="${ user.login }"></td>

						<td>Rua:</td>
						<td><input type="text" id="rua" name="rua"
						onblur="consultaCep();" value="${ user.rua }"></td>
					</tr>
					<tr>
						<td>Senha:</td>
						<td><input type="password" id="senha" name="senha"
							value="${ user.senha }"></td>

						<td>bairro:</td>
						<td><input type="text" id="bairro" name="bairro"
						onblur="consultaCep();" value="${ user.bairro }"></td>
					</tr>
					<tr>
						<td>Nome:</td>
						<td><input type="text" id="nome" name="nome"
							value="${ user.nome }"></td>

						<td>cidade:</td>
						<td><input type="text" id="cidade" name="cidade"
						onblur="consultaCep();" value="${ user.cidade }"></td>
					</tr>
					<tr>
						<td>Telefone:</td>
						<td><input type="text" id="fone" name="fone"
							value="${ user.fone }"></td>

						<td>uf:</td>
						<td><input type="text" id="uf" name="uf"
						onblur="consultaCep();" value="${ user.estado }"></td>
					</tr>
					<tr>
						<td>
							Foto:
						</td>
						<td><input type="file" name="foto">
						<input type="text" style="display:none;" name="fotoTemp" readonly="readonly" value ="${ user.fotoBase64 }">
						<input type="text" style="display:none;" name="contentTypeTemp" readonly="readonly" value ="${ user.contentType }"></td>
					</tr>
					<tr>
						<td>
							Currículo:
						</td>
						<td><input type="file" name="curriculo">
						<input type="text" style="display:none;" name="fotoTempPDF" readonly="readonly" value ="${ user.curriculoBase64 }">
						<input type="text" style="display:none;" name="contentTypeTempPDF" readonly="readonly" value ="${ user.contentTypeCurriculo }"></td>
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
				<th>Imagem</th>
				<th>Currículo</th>
				<th>Nome</th>
				<th>Delete</th>
				<th>Editar</th>
				<th>Fones</th>
			</tr>
			<c:forEach items="${ usuarios }" var="user">
				<tr>
					<td style="width: 150px"><c:out value="${ user.id }"></c:out></td>
					<td><a href="salvarUsuario?acao=download&tipo=imagem&user=${ user.id }"><img src="<c:out value="${ user.tempFoto }"></c:out>" width="32px" height="32px"
							title="Imagem do usuário"></a></td>
					<td><a href="salvarUsuario?acao=download&tipo=curriculo&user=${ user.id }">Currículo</a></td>
					<td><c:out value="${ user.nome }"></c:out></td>

					<td><a href="salvarUsuario?acao=delete&user=${ user.id }"><img
							src="resources/img/excluir.png" width="32px" height="32px"
							title="Excluir"></a></td>

					<td><a href="salvarUsuario?acao=editar&user=${ user.id }"><img
							src="resources/img/editar.png" width="32px" height="32px"
							title="Editar"></a></td>

					<td><a href="salvarTelefones?acao=addFone&user=${ user.id }"><img
							src="resources/img/phone.png" width="32px" height="32px"
							title="Telefones"></a></td>
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