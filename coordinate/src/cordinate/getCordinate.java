package cordinate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cqupt.db.DBManager;

/**
 * @author
 * @date 2017年7月24日 下午8:03:04
 * @parameter
 */
public class getCordinate {

	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		Map<String, String[]> cordinate = new HashMap<String, String[]>();
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		conn = DBManager.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery("show tables;");
			while (rs.next()) {
				String tableName = rs.getString("Tables_in_passinfo");
				System.out.println(tableName);
				tableList.add(tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while (tableList.size() != 0) {
			try {
				String tablename = tableList.remove(0);
				String Sql = "select distinct(pass_port),lat,lng from `" + tablename
						+ "` where data_source<>'HW' and data_source not REGEXP '^SC.*'";
				rs = st.executeQuery(Sql);
				while (rs.next()) {
					String pass_port = rs.getString("pass_port");
					if (!cordinate.keySet().contains(pass_port)) {
						String[] cordinates = new String[2];
						cordinates[0] = rs.getString("lng");
						cordinates[1] = rs.getString("lat");
						cordinate.put(pass_port, cordinates);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBManager.closeConnection(conn, st, rs);
			}
		}
		for (Entry<String, String[]> en : cordinate.entrySet()) {
			System.out.println("new BMap.Point(" + en.getValue()[0] + en.getValue()[1] + "),");
		}

	}
}
