package action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DBConnection;

public class Login {
	private int i=0;
	Connection con = DBConnection.getConnection();


	public Login(String usn, char[] pas) {
		try {
			Statement stm = con.createStatement();
			String SQL = "select UserPassword from user where UserName = '" + usn + "'";
			ResultSet password = stm.executeQuery(SQL);
			if (password.next()) {
				if (password.getString(1).equals(String.valueOf(pas))) {
					i = 1;
				}
			}
			DBConnection.closeConnection(stm, con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public int getI() {
		return i;
	}
}
