package cqupt.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import cqupt.bean.DBConfiguration;


public class DBManager {

	private static DBConfiguration dbConfig;
	private static DBConnPool pool;
	static{
		Properties pro = new Properties();
		try {
			pro.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		dbConfig = new DBConfiguration();
		dbConfig.setDriver(pro.getProperty("driver"));
		dbConfig.setPassword(pro.getProperty("password"));
		dbConfig.setUrl(pro.getProperty("url"));
		dbConfig.setUser(pro.getProperty("user"));
		dbConfig.setUsingDB(pro.getProperty("usingDB"));
		dbConfig.setPoolMaxSize(Integer.parseInt(pro.getProperty("poolMaxSize")));
		dbConfig.setPoolMixSize(Integer.parseInt(pro.getProperty("poolMinSize")));
	}

	/**
	 * 获取数据库连接，实际是从pool里面拿走一个
	 * @return
	 */
	public static Connection getConnection(){
		if(pool==null){
			pool = new DBConnPool();
		}
		return pool.getConnection();
	}
	
	/**
	 * 新建数据库连接
	 * @return
	 */
	public static Connection createConnection(){
		try {
			Class.forName(dbConfig.getDriver());
			return DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn 数据库连接
	 * @param statement 
	 * @param prepStatement
	 * @param rs
	 */
	private static void closeConnection(Connection conn, Statement statement, PreparedStatement prepStatement,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (prepStatement != null) {
				prepStatement.close();
			}
			pool.closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void closeConnection(Connection conn, Statement statement) {

		closeConnection(conn, statement, null, null);
	}

	public static void closeConnection(Connection conn, Statement statement,
			ResultSet rs) {

		closeConnection(conn, statement,null , rs);
	}
	
	public static void closeConnection(Connection conn, PreparedStatement prepStatement) {

		closeConnection(conn, null, prepStatement, null);
	}
	
	public static void closeConnection(Connection conn, PreparedStatement prepStatement,
			ResultSet rs) {

		closeConnection(conn, null, prepStatement, rs);
	}
	public static void closeConnection(PreparedStatement prepStatement,Connection conn) {

		closeConnection(conn, null, prepStatement, null);
	}

	public static DBConfiguration getDbConfig() {
		return dbConfig;
	}
}
