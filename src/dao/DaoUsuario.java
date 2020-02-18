package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.BeanCursoJsp;
import connection.SingleConnection;

public class DaoUsuario {

	private Connection connection;

	public DaoUsuario() {
		connection = SingleConnection.getConnection();
	}

	public void salvar(BeanCursoJsp usuario) {

		try {
			String sql = "insert into usuario(login, senha, nome, fone, cep, rua,"
					+ " bairro, cidade, estado, fotobase64, contenttype, curriculo_foto_base64, "
					+ "content_type_curriculo, fotoBase64Miniatura, ativo ) "
					+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement insert = connection.prepareStatement(sql);
			insert.setString(1, usuario.getLogin());
			insert.setString(2, usuario.getSenha());
			insert.setString(3, usuario.getNome());
			insert.setString(4, usuario.getFone());
			insert.setString(5, usuario.getCep());
			insert.setString(6, usuario.getRua());
			insert.setString(7, usuario.getBairro());
			insert.setString(8, usuario.getCidade());
			insert.setString(9, usuario.getEstado());
			insert.setString(10, usuario.getFotoBase64());
			insert.setString(11, usuario.getContentType());
			insert.setString(12, usuario.getCurriculoBase64());
			insert.setString(13, usuario.getContentTypeCurriculo());
			insert.setString(14, usuario.getFotoBase64Miniatura());
			insert.setBoolean(15, usuario.isAtivo());
			insert.execute();
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public List<BeanCursoJsp> listarUsuarios() throws Exception {

		List<BeanCursoJsp> listarUsuarios = new ArrayList<BeanCursoJsp>();

		String sql = "select * from usuario where login <> 'admin' order by id";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {

			BeanCursoJsp usuario = new BeanCursoJsp();
			usuario.setLogin(resultado.getString("login"));
			usuario.setSenha(resultado.getString("senha"));
			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
			usuario.setFone(resultado.getString("fone"));
			usuario.setCep(resultado.getString("cep"));
			usuario.setRua(resultado.getString("rua"));
			usuario.setBairro(resultado.getString("bairro"));
			usuario.setCidade(resultado.getString("cidade"));
			usuario.setEstado(resultado.getString("estado"));
			// usuario.setFotoBase64(resultado.getString("fotobase64"));
			usuario.setFotoBase64Miniatura(resultado.getString("fotoBase64Miniatura"));
			usuario.setContentType(resultado.getString("contenttype"));
			usuario.setCurriculoBase64(resultado.getString("curriculo_foto_base64"));
			usuario.setContentTypeCurriculo(resultado.getString("content_type_curriculo"));

			listarUsuarios.add(usuario);
		}
		return listarUsuarios;
	}

	public void delete(String id) {

		try {
			String sql = "delete from usuario where id = '" + id + "' and login <> 'admin' ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public BeanCursoJsp consultar(String id) throws SQLException {

		String sql = "select * from usuario where id='" + id + "' and login <> 'admin'";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			BeanCursoJsp beanCursoJsp = new BeanCursoJsp();
			beanCursoJsp.setLogin(resultSet.getString("login"));
			beanCursoJsp.setSenha(resultSet.getString("senha"));
			beanCursoJsp.setId(resultSet.getLong("id"));
			beanCursoJsp.setNome(resultSet.getString("nome"));
			beanCursoJsp.setFone(resultSet.getString("fone"));
			beanCursoJsp.setBairro(resultSet.getString("bairro"));
			beanCursoJsp.setCep(resultSet.getString("cep"));
			beanCursoJsp.setCidade(resultSet.getString("cidade"));
			beanCursoJsp.setEstado(resultSet.getString("estado"));
			beanCursoJsp.setRua(resultSet.getString("rua"));
			beanCursoJsp.setFotoBase64(resultSet.getString("fotobase64"));
			beanCursoJsp.setFotoBase64Miniatura(resultSet.getString("fotoBase64Miniatura"));
			beanCursoJsp.setContentType(resultSet.getString("contenttype"));
			beanCursoJsp.setContentTypeCurriculo(resultSet.getString("content_type_curriculo"));
			beanCursoJsp.setCurriculoBase64(resultSet.getString("curriculo_foto_base64"));

			return beanCursoJsp;
		}
		return null;
	}

	public void atualizar(BeanCursoJsp usuario) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" update usuario set login = ?, senha = ?, nome = ?, fone = ?, cep = ?, rua = ?, ");
			sql.append(" bairro = ?, cidade = ?, estado = ?, ativo = ? ");
			if (usuario.isAtualizarImage()) {
				sql.append(", fotobase64 = ?, contenttype = ? ");
			}
			if (usuario.isAtualizarPdf()) {
				sql.append(", content_type_curriculo = ?, curriculo_foto_base64 = ? ");
			}
			if (usuario.isAtualizarImage()) {
				sql.append(", fotobase64miniatura = ? ");
			}
			sql.append(" where id = " + usuario.getId());

			PreparedStatement statement = connection.prepareStatement(sql.toString());
			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getFone());
			statement.setString(5, usuario.getCep());
			statement.setString(6, usuario.getRua());
			statement.setString(7, usuario.getBairro());
			statement.setString(8, usuario.getCidade());
			statement.setString(9, usuario.getEstado());
			statement.setBoolean(10, usuario.isAtivo());
			if (usuario.isAtualizarImage()) {
				statement.setString(11, usuario.getFotoBase64());
				statement.setString(12, usuario.getContentType());
			}
			if (usuario.isAtualizarPdf()) {
				if (usuario.isAtualizarPdf() && !usuario.isAtualizarImage()) {
					statement.setString(11, usuario.getContentTypeCurriculo());
					statement.setString(12, usuario.getCurriculoBase64());
				} else {
					statement.setString(13, usuario.getContentTypeCurriculo());
					statement.setString(14, usuario.getCurriculoBase64());
				}
			} else {
				if (usuario.isAtualizarImage()) {
					statement.setString(13, usuario.getFotoBase64Miniatura());
				}
			}
			if (usuario.isAtualizarImage() && usuario.isAtualizarPdf()) {
				statement.setString(15, usuario.getFotoBase64Miniatura());
			}

			statement.executeUpdate();
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean validarLogin(String login) throws SQLException {
		String sql = "select * from usuario where login='" + login + "'";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		return resultSet.next() ? false : true;
	}
}
