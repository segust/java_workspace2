package action;

import java.sql.*;

import database.DBConnection;

public class Action {
	public String name;
	public String author;
	public int price;
	public String press;
	public int ID;
	public ResultSet rs = null;

	public int addBook(String BookName, String BookAuthor, int Price, String BookPress) {
		int i = 0;
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "insert into book (BookName,BookAuthor,Price,BookPress)values('" + BookName + "','"
					+ BookAuthor + "','" + Price + "','" + BookPress + "')";
			stm.execute(SQL);
			DBConnection.closeConnection(null, stm, con);
			i = 1;
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			i = 0;
		}
		return i;

	}

	public void deleBook(String BookName) {
		Connection con = DBConnection.getConnection();
		Statement stm;
		try {
			stm = con.createStatement();
			String SQL = "delete from book where BookName='" + BookName + "'";
			stm.execute(SQL);
			DBConnection.closeConnection(null, stm, con);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void modifyBook(String oldBookName, String BookName, String BookAuthor, int BookPrice, String BookPress) {
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "update book set BookName='" + BookName + "' ,BookAuthor='" + BookAuthor + "' ,Price='"
					+ BookPrice + "',BookPress='" + BookPress + "'where BookName='" + oldBookName + "'";
			stm.execute(SQL);
			DBConnection.closeConnection(null, stm, con);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public int searchBook(String BookName) {
		int i = 0;
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "select * from book where BookName='" + BookName + "'";
			stm.executeQuery(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			if (rs.next()) {
				ID = rs.getInt("BookID");
				name = rs.getString("BookName");
				author = rs.getString("BookAuthor");
				price = rs.getInt("Price");
				press = rs.getString("BookPress");
				i = 1;
			}
			DBConnection.closeConnection(null, stm, con);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return i;

	}

	// public String showList() {
	// String str = "";
	// Connection con = DBConnection.getConnection();
	// try {
	// Statement stm = con.createStatement();
	// String SQL = "select * from book";
	// stm.executeQuery(SQL);
	// ResultSet rs = stm.executeQuery(SQL);
	// rs.last();
	// int n = rs.getRow();
	// rs.beforeFirst();
	// for (int i = 1; i < n; i++) {
	// while (rs.next()) {
	// str=str+rs.getString(1) + "\t《" + rs.getString(2) + "》\t" +
	// rs.getString(3) + "\t"
	// + rs.getString(4)+"\t"+rs.getString(5)+"\n" ;
	// }
	// }
	// DBConnection.closeConnection(stm, con);
	// } catch (SQLException e) {
	// // TODO 自动生成的 catch 块
	// e.printStackTrace();
	// }
	// return str;
	// }

	public class list {
		Object[][] Object;
		int i;

		public Object[][] getObject() {
			return Object;
		}

		public void setObject(Object[][] object) {
			Object = object;
		}

		public int getI() {
			return i;
		}

		public void setI(int i) {
			this.i = i;
		}
	}

	public list showList() {
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "select * from book";
			stm.executeQuery(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			rs.last();
			int length = rs.getRow();
			rs.beforeFirst();
			Object[][] List = new Object[length][5];
			for (int i = 0; rs.next(); i++) {
				List[i][0] = rs.getInt(1);
				List[i][1] = rs.getString(2);
				List[i][2] = rs.getString(3);
				List[i][3] = rs.getInt(4);
				List[i][4] = rs.getString(5);
			}
			list list = new list();
			list.setObject(List);
			list.setI(length);
			return list;
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}

	}

}
