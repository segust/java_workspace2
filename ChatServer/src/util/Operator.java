package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.DBConnection;
import model.User;

public class Operator {
	Connection con = DBConnection.getConnection();
	Statement stm = null;
	ResultSet rs;
	User user;

	public User login(String username, String password) {
		try {
			stm = con.createStatement();
			String SQL = "select * from userinfo where id = '" + username + "' and password ='" + password + "'";
			rs = stm.executeQuery(SQL);
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("username");
				String sex = rs.getString("sex");
				int age = rs.getInt("age");
				user = new User(id, name, sex, age);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			user = null;
		} finally {
			DBConnection.closeConnection(stm, con);
		}
		return user;
	}

	public String findName(int id) {
		String username = null;
		try {
			stm = con.createStatement();
			String SQL = "select username from userinfo where id = '" + id + "'";
			rs = stm.executeQuery(SQL);
			while (rs.next()) {
				username = rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection(stm, con);
		}
		return username;

	}

}
