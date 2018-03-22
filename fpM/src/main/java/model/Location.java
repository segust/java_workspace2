package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author
 * @date 2017年7月16日 下午3:26:22
 * @parameter
 */
public class Location {
	String license_no;

	ResultSet rs = null;

	public Location(ResultSet rs) {
		this.rs = rs;
	}

	@SuppressWarnings("deprecation")
	public void getLocation() {
		Timestamp currenttime = null;
		Timestamp lasttime = null;
		try {
			if (rs.next()) {

				lasttime = rs.getTimestamp("pass_time");

				System.out.println("回家及上班时间：");
				while (rs.next()) {
					currenttime = rs.getTimestamp("pass_time");
					if (currenttime.getDate() != lasttime.getDate()) {
						System.out.print("\t" + lasttime + " - ");
						System.out.println(currenttime);
					}
					lasttime = currenttime;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(currenttime.toString() + "  " + lasttime.toString());
		}
	}

}
