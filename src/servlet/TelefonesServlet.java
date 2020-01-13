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
import beans.Telefones;
import dao.DaoTelefones;
import dao.DaoUsuario;

@WebServlet("/salvarTelefones")
public class TelefonesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DaoUsuario daoUsuario = new DaoUsuario();
	private DaoTelefones daoTelefones = new DaoTelefones();

	public TelefonesServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ("addFone".equalsIgnoreCase(request.getParameter("acao"))) {

				BeanCursoJsp usuario = daoUsuario.consultar(request.getParameter("user"));
				request.getSession().setAttribute("userEscolhido", usuario);
				request.setAttribute("telefones", daoTelefones.listar(usuario.getId()));
			} else if("deleteFone".equalsIgnoreCase(request.getParameter("acao"))){

				BeanCursoJsp beanCursoJsp = (BeanCursoJsp) request.getSession().getAttribute("userEscolhido");

				daoTelefones.delete(request.getParameter("foneId"));
				request.setAttribute("msg", "Telefone deletado com sucesso!");
				request.setAttribute("telefones", daoTelefones.listar(beanCursoJsp.getId()));
			}
			
			RequestDispatcher view = request.getRequestDispatcher("/telefones.jsp");
			view.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			BeanCursoJsp beanCursoJsp = (BeanCursoJsp) request.getSession().
											getAttribute("userEscolhido");
	
			String numero = request.getParameter("numero");
			String tipo = request.getParameter("tipo");
	
			Telefones telefones = new Telefones();
			telefones.setNumero(numero);
			telefones.setTipo(tipo);
			telefones.setUsuario(beanCursoJsp.getId());
			
			daoTelefones.salvar(telefones);
			
			RequestDispatcher view = request.getRequestDispatcher("/telefones.jsp");
			request.getSession().setAttribute("userEscolhido", beanCursoJsp);
			request.setAttribute("telefones", daoTelefones.listar(telefones.getUsuario()));
			request.setAttribute("msg", "Salvo com sucesso!");
			view.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
