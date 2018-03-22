package database;

import java.sql.*;

public class DBConnection {
	static final String url="jdbc:mysql://localhost:3306/library?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	static final String name="com.mysql.jdbc.Driver";
	static final String user="root";
	static final String password="000000";
	Connection con=null;
	static{
		try {
			Class.forName(name);
		} catch (ClassNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}	
	}
	public static Connection getConnection(){
		try {
			return DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		}
	}
	public static void closeConnection(ResultSet rs,Statement statement,Connection con){
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		if(statement!=null)
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		if(con!=null)
			try {
				con.close();
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
	}
	public static void closeConnection(Statement statement,Connection con){
		closeConnection(null,statement,con);
	}
	public static void main(String[] args){
		
	}
}

