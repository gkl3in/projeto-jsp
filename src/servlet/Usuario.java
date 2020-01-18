package servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.codec.binary.Base64;

import beans.BeanCursoJsp;
import dao.DaoUsuario;

@WebServlet("/salvarUsuario")
@MultipartConfig
public class Usuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DaoUsuario daoUsuario = new DaoUsuario();

	public Usuario() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String acao = request.getParameter("acao");
			String user = request.getParameter("user");

			if ("delete".equalsIgnoreCase(acao)) {

				daoUsuario.delete(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if ("editar".equalsIgnoreCase(acao)) {

				BeanCursoJsp beanCursoJsp = daoUsuario.consultar(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("user", beanCursoJsp);
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if ("listartodos".equalsIgnoreCase(acao)) {

				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if ("download".equalsIgnoreCase(acao)) {

				BeanCursoJsp beanCursoJsp = daoUsuario.consultar(user);
				if (beanCursoJsp != null) {
					response.setHeader("Content-Disposition", "attachment;filename=arquivo." + 
										beanCursoJsp.getContentType().split("\\/")[1]);
					
					byte[] imageFotoBytes = new Base64().decodeBase64(beanCursoJsp.getFotoBase64());
					
					OutputStream os = response.getOutputStream();
					
					os.write(imageFotoBytes);
					os.flush();
					os.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String acao = request.getParameter("acao");

		if (acao != null && acao.equalsIgnoreCase("reset")) {
			try {
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String id = request.getParameter("id");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String nome = request.getParameter("nome");
			BeanCursoJsp usuario = new BeanCursoJsp();
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setId((id != null && !id.isEmpty()) ? Long.parseLong(id) : null);
			usuario.setNome(nome);
			usuario.setFone(request.getParameter("fone"));
			usuario.setBairro(request.getParameter("bairro"));
			usuario.setRua(request.getParameter("rua"));
			usuario.setCidade(request.getParameter("cidade"));
			usuario.setCep(request.getParameter("cep"));
			usuario.setEstado(request.getParameter("uf"));

			try {
				
				/* Início upload de imagens */
				if (ServletFileUpload.isMultipartContent(request)) {

					Part imagemFoto = request.getPart("foto");
					
					String fotoBase64 = new Base64().
							encodeBase64String(converteStreamParaByte(imagemFoto.getInputStream()));
					
					usuario.setFotoBase64(fotoBase64);
					usuario.setContentType(imagemFoto.getContentType());
				}
				/* fim upload de imagens */

				if (login == null || login.isEmpty()) {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "Login deve ser informado");
				} else if (senha == null || senha.isEmpty()) {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "senha deve ser informada");
				} else if (nome == null || nome.isEmpty()) {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "nome deve ser informado");
				} else if ((id == null || id.isEmpty()) && daoUsuario.validarLogin(login)) {
					daoUsuario.salvar(usuario);
					request.setAttribute("msg", "Usuário " + usuario.getNome() + " cadastrado com sucesso!");
				} else if ((id != null && !id.isEmpty()) && (daoUsuario.validarLogin(login)
						|| daoUsuario.consultar(id).getLogin().equalsIgnoreCase(login))) {
					daoUsuario.atualizar(usuario);
					request.setAttribute("msg", "Usuário alterado com sucesso!");
				} else {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "Login já existente");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private byte[] converteStreamParaByte(InputStream imagem) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = imagem.read();

		while (reads != -1) {
			baos.write(reads);
			reads = imagem.read();
		}
		return baos.toByteArray();
	}
}
