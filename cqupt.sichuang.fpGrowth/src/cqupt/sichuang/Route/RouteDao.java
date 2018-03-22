package cqupt.sichuang.Route;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import cqupt.db.DBManager;

/**
 * @author
 * @date 2017年7月26日 下午3:16:06
 * @parameter
 */
public class RouteDao {

	String license_no;
	ArrayList<String> tableList = new ArrayList<String>();// 存储数据库所有表名称
	ArrayList<String> dayRoute;// 存储单天路径信息
	ArrayList<ArrayList<String>> dayClause = new ArrayList<ArrayList<String>>();// 存储所有天的路径信息

	public RouteDao(String license_no) {

		this.license_no = license_no;
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		conn = DBManager.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery("show tables");
			while (rs.next()) {
				String tableName = rs.getString("Tables_in_passinfo");
				tableList.add(tableName);
				System.out.print(tableName + ",");
			}
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.closeConnection(conn, st, rs);
		}
	}

	public ArrayList<ArrayList<String>> getDayClause() {

		Connection conn = DBManager.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();

			for (String tableName : tableList) {
				dayRoute = new ArrayList<String>();
				String sql = "select pass_port_name from `" + tableName + "` where license_no='" + license_no
						+ "' order by pass_time;";
				rs = st.executeQuery(sql);
				while (rs.next()) {
					String node = rs.getString("pass_port_name");
					dayRoute.add(node);
				}
				dayClause.add(dayRoute);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.closeConnection(conn, st, rs);
		}

		return dayClause;
	}

	public static void main(String[] args) {
		RouteDao dao = new RouteDao("123");
	}
}
