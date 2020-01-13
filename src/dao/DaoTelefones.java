package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Produto;
import beans.Telefones;
import connection.SingleConnection;

public class DaoTelefones {

	private Connection connection;

	public DaoTelefones() {
		connection = SingleConnection.getConnection();
	}

	public void salvar(Telefones telefone) {

		try {
			String sql = "insert into telefone(numero, tipo, usuario) values (?, ?, ?)";
			PreparedStatement insert = connection.prepareStatement(sql);
			insert.setString(1, telefone.getNumero());
			insert.setString(2, telefone.getTipo());
			insert.setLong(3, telefone.getUsuario());
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

	public List<Telefones> listar(Long user) throws Exception {
		List<Telefones> listar = new ArrayList<Telefones>();

		String sql = "select * from telefone where usuario = " + user;

		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			Telefones telefones = new Telefones();

			telefones.setId(resultSet.getLong("id"));
			telefones.setNumero(resultSet.getString("numero"));
			telefones.setTipo(resultSet.getString("tipo"));
			telefones.setUsuario(resultSet.getLong("usuario"));
			listar.add(telefones);
		}

		return listar;
	}

	public void delete(String id) {
		try {
			String sql = "delete from telefone where id = '" + id + "'";
			PreparedStatement preparedStatement = connection
					.prepareStatement(sql);
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
}
