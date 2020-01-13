package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.codec.binary.Base64;

import beans.BeanCursoJsp;
import dao.DaoUsuario;

@WebServlet("/salvarUsuario")
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

			if (acao.equalsIgnoreCase("delete")) {
				daoUsuario.delete(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if (acao.equalsIgnoreCase("editar")) {

				BeanCursoJsp beanCursoJsp = daoUsuario.consultar(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("user", beanCursoJsp);
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if (acao.equalsIgnoreCase("listartodos")) {
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
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
					
					List<FileItem> fileItems = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
					
					for (FileItem fileItem : fileItems) {
						if (fileItem.getFieldName().equalsIgnoreCase("foto")) {
							String foto = new Base64().encodeBase64String(fileItem.get());
							System.out.println(foto);
						}
					}
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
				} else if ((id != null && !id.isEmpty()) && (daoUsuario.validarLogin(login)
						|| daoUsuario.consultar(id).getLogin().equalsIgnoreCase(login))) {
					daoUsuario.atualizar(usuario);
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
				request.setAttribute("msg", "Usuário " + usuario.getNome() + " cadastrado com sucesso!");
				view.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
