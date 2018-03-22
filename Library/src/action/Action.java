package action;

import java.sql.*;
import database.DBConnection;

public class Action {

	public void addBook(String BookName, String BookAuthor, int Price) {
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "insert into book (BookName,BookAuthor,Price)values('" + BookName + "','" + BookAuthor + "','"
					+ Price + "')";
			stm.execute(SQL);
			System.out.println("添加成功");
			System.out.println("*****************");
			DBConnection.closeConnection(null, stm, con);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void deleBook(String BookName) {
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "delete from book where BookName='" + BookName + "'";
			stm.execute(SQL);
			System.out.println("删除成功！");
			System.out.println("*****************");
			DBConnection.closeConnection(null, stm, con);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			System.out.println("未找到这本书！");
			System.out.println("******************");
		}
	}

	public void modifyBook(String oldBookName, String BookName, String BookAuthor, int Price) {
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "update book set BookName='" + BookName + "' ,BookAuthor='" + BookAuthor + "' ,Price='" + Price
					+ "'where BookName='" + oldBookName + "'";
			stm.execute(SQL);
			System.out.println("修改成功！");
			System.out.println("******************");
			DBConnection.closeConnection(null, stm, con);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			System.out.println("未找到这本书！");
			System.out.println("******************");
		}

	}

	public void searchBook(String BookName) {
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "select * from book where BookName='" + BookName + "'";
			stm.executeQuery(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			if (rs.next()) {
				System.out.println("ID:" + rs.getString("BookID") + "\t图书名称：《" + rs.getString("BookName") + "》\t作者："
						+ rs.getString("BookAuthor") + "\t价格：" + rs.getString("Price"));
			} else {
				System.out.println("未找到这本书。");
			}

			DBConnection.closeConnection(stm, con);
			System.out.println("***************");

		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("***************");
		}

	}

	public void showList() {
		Connection con = DBConnection.getConnection();
		try {
			Statement stm = con.createStatement();
			String SQL = "select * from book";
			stm.executeQuery(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			rs.last();
			int n = rs.getRow();
			rs.beforeFirst();
			for (int i = 1; i < n; i++) {
				while (rs.next()) {
					System.out.println(rs.getString(1) + "\t《" + rs.getString(2) + "》\t" + rs.getString(3) + "\t"
							+ rs.getString(4) + "");
				}
			}
			DBConnection.closeConnection(stm, con);
			System.out.println("****************");
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
