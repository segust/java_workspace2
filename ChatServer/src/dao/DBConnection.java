package dao;

import java.sql.*;

public class DBConnection {
	static final String url = "jdbc:mysql://localhost:3306/chat?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	static final String name = "com.mysql.jdbc.Driver";
	static final String user = "root";
	static final String password = "000000";
	Connection con = null;
	static {
		try {
			Class.forName(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void closeConnection(ResultSet rs, Statement statement, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (statement != null)
				statement.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(Statement statement, Connection con) {
		closeConnection(null, statement, con);
	}

}