package Main;

import cqupt.db.Query;
import cqupt.db.QueryCallBack;
import util.TrajectoryRule;

import javax.sound.midi.SoundbankResource;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainClass {
    public static void main(String args[]) {
        Boolean isEnd;
        String S = "show tables";
        ArrayList<String> tables = (ArrayList<String>) Query.executeQueryTemplate(S, null, new QueryCallBack() {
            public Object doExecute(ResultSet rs) {
                ArrayList<String> tables = new ArrayList<String>();
                try {
                    while (rs.next()) {
                        String tableName = rs.getString("Tables_in_passinfo");
                        if (!tableName.equals("calendar") && !tableName.equals("info_collection") &&
                                !tableName.equals("newest testee") && !tableName.equals("spark hash test") &&
                                !tableName.equals("starktest") && !tableName.equals("test")) ;
                        tables.add(tableName);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return tables;
            }
        });
        for (String table : tables) {
            isEnd = false;
            for (int i = 1; isEnd != true; i++) {
                String sql = "select * from `" + table + "` order by license_no,pass_time asc limit " + (i - 1) * 5000 + "," + i * 5000;
                isEnd = (Boolean) Query.executeQueryTemplate(sql, null, new QueryCallBack() {
                    public Object doExecute(ResultSet rs) {
                        Boolean isEnd = false;
                        int count = 0;
                        try {
                            ArrayList<String> t = new ArrayList<String>();
                            String temLicense_no = null;
                            String temPass_port = null;
                            if (rs.next()) {
                                count++;
                                temLicense_no = rs.getString("license_no");
                                temPass_port = rs.getString("pass_port");
                            }
                            while (rs.next()) {
                                count++;
                                String license_no = rs.getString("license_no");
                                String pass_port = rs.getString("pass_port");
                                if (license_no != temLicense_no) {
                                    t.add(temPass_port);
                                    if (t.size() > 2) {
                                        TrajectoryRule rule = new TrajectoryRule();
                                        rule.TrajectoryWriter(t);
                                    }
                                    t = new ArrayList<String>();
                                    temLicense_no = license_no;
                                    temPass_port = pass_port;
                                } else {
                                    t.add(temPass_port);
                                    temPass_port = pass_port;
                                }
                            }
                            System.out.println(count);
                            if (count < 5000)
                                isEnd = true;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return isEnd;
                    }
                });
            }
        }
    }
}