package cqupt.util;

import cqupt.db.Query;
import cqupt.db.QueryCallBack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class getHoliday {
    private static Map<String, Integer> HolidayMap = new HashMap<String, Integer>();

    public static Map<String, Integer> getMap() {
        String sql = "Select * from `calendar`";
        Query.executeQueryTemplate(sql, null, new QueryCallBack() {
            public Object doExecute(ResultSet rs) {

                try {
                    while (rs.next()) {
                        String date = rs.getString("date");
                        int isHoliday = rs.getInt("property");
                        HolidayMap.put(date, isHoliday);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        return HolidayMap;

    }
}
