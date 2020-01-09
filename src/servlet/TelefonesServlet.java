package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BeanCursoJsp;
import dao.DaoUsuario;

@WebServlet("/salvarTelefones")
public class TelefonesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DaoUsuario daoUsuario = new DaoUsuario();

	public TelefonesServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String user = request.getParameter("user");
			BeanCursoJsp usuario = daoUsuario.consultar(user);
			request.getSession().setAttribute("userEscolhido", usuario);
	
			RequestDispatcher view = request.getRequestDispatcher("/telefones.jsp");
			//request.setAttribute("telefones", daoUsuario.listarUsuarios());
			//request.setAttribute("msg", "Telefone " + telefone.getNome() + " cadastrado com sucesso!");
			view.forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BeanCursoJsp beanCursoJsp = (BeanCursoJsp) request.getSession().getAttribute("userEscolhido");
		
		String numero = request.getParameter("numero");
		String tipo = request.getParameter("tipo");
	}

}
