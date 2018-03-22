package Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import cqupt.db.DBManager;
import model.RegularPoints;

public class MainClass {
	public static void main(String[] args) {
		System.out.println("输入想要查询的车牌号：");
		Scanner scan = new Scanner(System.in);
		String license_no = scan.nextLine();
		scan.close();
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;

		long startTime = System.currentTimeMillis();

		conn = DBManager.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(
					"select * from `info_collection` where license_no='" + license_no + "' order by pass_time");
			RegularPoints re = new RegularPoints(rs);
			re.getRegularPoints();
			System.out.println("\n查询时间：" + (System.currentTimeMillis() - startTime));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.closeConnection(conn, st, rs);
		}

	}
}