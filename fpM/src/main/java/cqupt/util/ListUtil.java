package cqupt.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author
 * @date 2017年7月19日 下午5:00:03
 * @parameter 维护dayClause，类型为ArrayList<ArrayList<String>>,其中每一个ArrayList<String>为一天所经过路径的卡口列表
 * 
 */
public class ListUtil {

	public static ArrayList<ArrayList<String>> getList(ResultSet rs) {
		ArrayList<ArrayList<String>> dayClause = new ArrayList<ArrayList<String>>();
		try {
			ArrayList<String> dayInfo;
			if (rs.next()) {

				dayInfo = new ArrayList<String>();
				dayInfo.add(rs.getString("pass_port_name"));
				@SuppressWarnings("deprecation")
				int lastday = rs.getTimestamp("pass_time").getDate();

				while (rs.next()) {
					String pass_port_name = rs.getString("pass_port_name");
					@SuppressWarnings("deprecation")
					int today = rs.getTimestamp("pass_time").getDate();
					if (today != lastday) {
						dayClause.add(dayInfo);
						dayInfo = new ArrayList<String>();
						dayInfo.add(pass_port_name);
						lastday = today;
					} else {
						dayInfo.add(pass_port_name);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dayClause;
	}

}
