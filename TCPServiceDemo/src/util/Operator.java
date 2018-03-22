package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Book;

public class Operator {
	private int length = 0;

	public void add(Book book) throws SQLException {
		Connection con = Driver.getConnection();
		int BookPrice = Integer.parseInt(book.getPrice());
		Statement stm = null;
		try {
			stm = con.createStatement();
			String SQL = "insert into book (BookName,BookAuthor,Price,BookPress)values('" + book.getBookname() + "','"
					+ book.getAuthor() + "','" + BookPrice + "','" + book.getPress() + "')";
			stm.execute(SQL);
		} catch (SQLException e) {
			throw e;
		} finally {
			Driver.closeConnection(null, stm, con);
		}
	}

	public void delete(Book book) throws SQLException {
		Connection con = Driver.getConnection();
		Statement stm = null;
		try {
			stm = con.createStatement();
			String SQL = "delete from book where BookName='" + book.getBookname() + "'";
			stm.execute(SQL);
		} catch (SQLException e) {
			throw e;
		} finally {
			Driver.closeConnection(null, stm, con);
		}
	}

	public void modify(String oldbook, Book book) throws SQLException {
		Connection con = Driver.getConnection();
		int BookPrice = Integer.parseInt(book.getPrice());
		Statement stm = null;
		try {
			stm = con.createStatement();
			String SQL = "update book set BookName='" + book.getBookname() + "' ,BookAuthor='" + book.getAuthor()
					+ "' ,Price='" + BookPrice + "',BookPress='" + book.getPrice() + "'where BookName='" + oldbook
					+ "'";
			stm.execute(SQL);
		} catch (SQLException e) {
			throw e;
		} finally {
			Driver.closeConnection(null, stm, con);
		}
	}

	public String search(Book book) throws SQLException {
		String content = null;
		Connection con = Driver.getConnection();
		Statement stm = null;
		try {
			stm = con.createStatement();
			String SQL = "select * from book where BookName='" + book.getBookname() + "'";
			stm.executeQuery(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			if (rs.next()) {
				String id = rs.getString("BookID");
				String name = rs.getString("BookName");
				String author = rs.getString("BookAuthor");
				String price = rs.getString("Price");
				String press = rs.getString("BookPress");
				content = id + "#" + name + "#" + author + "#" + price + "#" + press;
			} else {
				content = "查询失败！";
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			Driver.closeConnection(null, stm, con);
		}
		return content;
	}

	public String showlist() throws SQLException {

		Connection con = Driver.getConnection();
		StringBuffer list = new StringBuffer(1024);
		Statement stm = null;
		try {
			stm = con.createStatement();
			String SQL = "select * from book";

			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()) {
				list.append(rs.getString("BookID"));
				list.append("#");
				list.append(rs.getString("BookName"));
				list.append("#");
				list.append(rs.getString("BookAuthor"));
				list.append("#");
				list.append(rs.getString("Price"));
				list.append("#");
				list.append(rs.getString("BookPress"));
				list.append("#");
				length++;
			}
			if (length == 0) {
				list.append("没有图书！");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			Driver.closeConnection(null, stm, con);
		}
		return list.toString();
	}

	public boolean login(String username, String pass) throws SQLException {
		boolean i = false;
		Connection con = Driver.getConnection();
		Statement stm = null;
		try {
			stm = con.createStatement();
			String SQL = "select * from user where UserName = '" + username + "' and UserPassword='" + pass + "'";
			ResultSet rs = stm.executeQuery(SQL);
			while (rs.next()) {
				i = true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			Driver.closeConnection(stm, con);
		}
		return i;
	}

	public int getLength() {
		return length;
	}
}
