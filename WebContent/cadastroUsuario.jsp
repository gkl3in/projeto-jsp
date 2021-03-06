<%@page import="beans.BeanCursoJsp"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cadastro de usu�rios</title>
<link rel="stylesheet" type="text/css" href="resources/css/cadastro.css">

<!-- Adicionando JQuery -->
<script src="https://code.jquery.com/jquery-3.4.1.min.js"
	integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
	crossorigin="anonymous"></script>
</head>
<body>
	<a href="acessoLiberado.jsp">In�cio</a>
	<center>
		<h1>Cadastro de usu�rio</h1>
		<h3 style="color: red;">${ msg }</h3>
	</center>
	<form action="salvarUsuario" method="post" id="formUser"
		onsubmit="return validarCampos()" enctype="multipart/form-data">
		<ul class="form-style-1">
			<li>
				<table>
					<tr>
						<td>C�digo:</td>
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
						<td><input type="file" name="foto"></td>
						
						<td>Ativo:</td>
						<td><input type="checkbox" id="ativo" name="ativo"
							<%
								if(request.getAttribute("user") != null) {
									BeanCursoJsp user = (BeanCursoJsp) request.getAttribute("user");
									if(user.isAtivo()){
										out.print(" ");
										out.print("checked=\"checked\"");
										out.print(" ");
									}
								}
							%>
						></td>
					</tr>
					<tr>
						<td>
							Curr�culo:
						</td>
						<td><input type="file" name="curriculo"></td>
						
						<td>Sexo:</td>
						<td>
							<input type="radio" name="sexo"
							
							<%
								if(request.getAttribute("user") != null){
									BeanCursoJsp user = (BeanCursoJsp) request.getAttribute("user");
									if(user.getSexo().equalsIgnoreCase("masculino")){
										out.print(" ");
										out.print("checked=\"checked\"");
										out.print(" ");
									}
								}
							%>
							 value="masculino">Masculino</input>
							<input type="radio" name="sexo"
														<%
								if(request.getAttribute("user") != null){
									BeanCursoJsp user = (BeanCursoJsp) request.getAttribute("user");
									if(user.getSexo().equalsIgnoreCase("feminino")){
										out.print(" ");
										out.print("checked=\"checked\"");
										out.print(" ");
									}
								}
							%>
							 value="feminino">Feminino</input>
						</td>
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
				<th>Curr�culo</th>
				<th>Nome</th>
				<th>Delete</th>
				<th>Editar</th>
				<th>Fones</th>
			</tr>
			<c:forEach items="${ usuarios }" var="user">
				<tr>
					<td style="width: 150px"><c:out value="${ user.id }"></c:out></td>
					<c:if test="${ user.fotoBase64Miniatura != null}">
						<td><a href="salvarUsuario?acao=download&tipo=imagem&user=${ user.id }"><img src="<c:out value="${ user.fotoBase64Miniatura }"></c:out>" width="32px" height="32px"
							title="Imagem do usu�rio"></a></td>
					</c:if>
					<c:if test="${ user.fotoBase64Miniatura == null}">
						<td><img alt="Imagem User" src="resources/img/iconeUser.jpg" width="32px" height="32px" onclick="alert('N�o possui imagem')"> </td>
					</c:if>
					<c:if test="${ user.curriculoBase64 != null}">
						<td><a href="salvarUsuario?acao=download&tipo=curriculo&user=${ user.id }"><img alt="pdf" src="resources/img/pdf-icon.png" width="32px" height="32px"></a></td>
					</c:if>
					<c:if test="${ user.curriculoBase64 == null}">
						<td><img alt="Imagem User" src="resources/img/pdfoff.jpg" width="32px" height="32px" onclick="alert('N�o possui PDF');"> </td>
					</c:if>
					<td><c:out value="${ user.nome }"></c:out></td>

					<td><a href="salvarUsuario?acao=delete&user=${ user.id }" onclick="return confirm('Confirmar a exclus�o?')"><img
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
		
		function limpa_formul�rio_cep() {
            // Limpa valores do formul�rio de cep.
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
                            limpa_formul�rio_cep();
                            alert("CEP n�o encontrado.");
                        }
                    });
                }
                else {
                    limpa_formul�rio_cep();
                    alert("Formato de CEP inv�lido.");
                }
            }
            else {
                limpa_formul�rio_cep();
            }
        }
	</script>
</body>
</html>