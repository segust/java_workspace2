package MainClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import cqupt.zyk.db.Query;
import cqupt.zyk.db.QueryCallBack;

public class MainClass {
	public static void main(String[] args) {

		ArrayList<Object[]> paramList = new ArrayList<Object[]>();
		String SQL = "select * from `info_collection` order by license_no,pass_time desc";
		Query.executeQueryTemplate(SQL, null, new QueryCallBack() {
			@Override
			public Object doExecute(ResultSet rs) {
				String lastLicense_No = "";
				String lastPass_Port = "";
				try {
					while (rs.next()) {
						String license_no = rs.getString("license_no");
						String pass_port = rs.getString("pass_port");
						Timestamp pass_time = rs.getTimestamp("pass_time");
						String lat = rs.getString("lat");
						String lng = rs.getString("lng");
						String pass_port_name = rs.getString("pass_port_name");
						if (!license_no.equals("未知")) {
							if (license_no.equals(lastLicense_No)) {
								if (!pass_port.equals(lastPass_Port)) {
									Object[] params = new Object[3];
									params[0] = license_no;
									params[1] = pass_time;
									params[2] = pass_port;
									params[3] = lat;
									params[4] = lng;
									params[5] = pass_port_name;
									paramList.add(params);
									lastLicense_No = license_no;
									lastPass_Port = pass_port;
								}
							} else {
								Object[] params = new Object[3];
								params[0] = license_no;
								params[1] = pass_time;
								params[2] = pass_port;
								params[3] = lat;
								params[4] = lng;
								params[5] = pass_port_name;
								paramList.add(params);
								lastPass_Port = pass_port;
								lastLicense_No = license_no;
							}
							int i = 0;
							if (paramList.size() % 3000 == 0) {
								i++;
								String sql = "insert into `info_collection_tmp` values (?,?,?,?,?,?)";
								Query.executeBatchDML(sql, paramList);
								System.out.println(String.valueOf(3000 * i) + " items has been loaded");
								paramList.clear();
							}
						}
						String sql = "insert into `info_collection_tmp` values (?,?,?,?,?,?)";
						Query.executeBatchDML(sql, paramList);
						paramList.clear();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return lastPass_Port;
			}
		});
	}
}
