package cn.edu.cqupt.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

	static final String driver = "com.mysql.jdbc.Driver";
	//数据库地址
	//服务器地址为172.22.145.234
	//目前测试先在自己的数据库上完成
	static final String url = "jdbc:mysql://localhost:3306/6905";
	//db用户名
	static final String user = "root";
	//db密码
	static final String password = "";

	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
	public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {

		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection con, PreparedStatement pstmt) {
		close(con, pstmt, null);
	}
	
	public static void close(Connection con){
		close(con,null,null);
	}
	
	public static void close(PreparedStatement ... ps){
		try{
			int len = ps.length;
			for(int i = 0; i < len; i++){
				if(ps[i] != null){
					ps[i].close();
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] getValues(){
		String[] values = new String[3];
		values[0]=url;
		values[1]=user;
		values[2]=password;
		return values;
	} 
	
	/**
	 * 测试DB连接
	 * @param args
	 */
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = DBConnection.getConn();
			//stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con, stmt, rs);
		}
	}

}
