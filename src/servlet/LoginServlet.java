package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BeanCursoJsp;
import connection.SingleConnection;
import dao.DaoLogin;
import utils.GenericTools;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DaoLogin daoLogin = new DaoLogin();
	
    public LoginServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			BeanCursoJsp beanCursoJsp = new BeanCursoJsp();
	
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			
			if (!GenericTools.isNull(login)
					&& !GenericTools.isNull(senha)) {

				if (daoLogin.validarLogin(login, senha)) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("acessoLiberado.jsp");
					dispatcher.forward(request, response);
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("acessoNegado.jsp");
					dispatcher.forward(request, response);
				}
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
				dispatcher.forward(request, response);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
