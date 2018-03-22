package util;

import db.Query;
import db.QueryCallBack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class TableSet {
    private static HashSet<String> tableSet = new HashSet<String>();

    public static HashSet<String> getTableSet() {
        String sql = "show tables";
        Query.executeQueryTemplate(sql, null, new QueryCallBack() {
            public Object doExecute(ResultSet rs) {
                try {
                    while (rs.next()) {
                        tableSet.add(rs.getString("Tables_in_passinfo"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return tableSet;
            }
        });
        return tableSet;
    }
}
