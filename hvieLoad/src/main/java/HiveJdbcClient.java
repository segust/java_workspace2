
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Handle data through hive on eclipse
 *
 * @author urey
 * @time 2013\12\26 19:14
 */
public class HiveJdbcClient {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://172.22.145.1:10000/passinfo";
    private static String user = "root";
    private static String password = "123456";
    private static String sql = "";

    private static HashMap<String, String> loadMap;

    public static void main(String[] args) {
        getMap getMap = new getMap();
        loadMap = getMap.getLoadmap();
        try {
            Class.forName(driverName);
            // Connection conn = DriverManager.getConnection(url, user,
            // password);
            // 默认使用端口10000, 使用默认数据库，用户名密码默认
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            System.out.println("HiveJdbcClient.main");
            for (Map.Entry en : loadMap.entrySet()) {
                sql = "load data inpath \"/passinfo/" + en.getValue() + "\" into table `" + en.getKey() + "`";
                System.out.println(sql);
                try {
                    stmt.executeQuery(sql);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            // 创建的表名
//            String tableName = "testHiveDriverTable";
//
//            /** 第二步:不存在就创建 **/
//            sql = " create table `" + tableName
//                    + "`(license_no string,pass_time string,pass_port string ,device string,"
//                    + "direction string,license_type string,license_color string,vehicle string,vehicle_len string,"
//                    + "vehicle_type string,speed string,feature_image string,panorama_image string,"
//                    + "pass_port_name string,direction_name string,lane string,lane_name string,"
//                    + "vehicle_body_type string,vehicle_loge_type string,data_source string,lat string,lng string,"
//                    + "etc1 string,etc2 string,etc3 string)" + "ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'" + "with serdeproperties (" + "\"separatorChar\"=\",\","
//                    + "\"quoteChar\"=\"'\"," + "\"escateChar\"=\"\\\\\")" + "stored as textfile";
//            stmt.executeQuery(sql);
//
//            // 执行“show tables”操作
//            sql = "show tables";
//            System.out.println("Running:" + sql);
//            ResultSet res = stmt.executeQuery(sql);
//            System.out.println("执行“show tables”运行结果:");
//            if (res.next()) {
//                System.out.println(res.getString(1));
//            }

            // 执行“describe table”操作
            // sql = "describe " + tableName;
            // System.out.println("Running:" + sql);
            // res = stmt.executeQuery(sql);
            // System.out.println("执行“describe table”运行结果:");
            // while (res.next()) {
            // System.out.println(res.getString(1) + "\t" + res.getString(2));
            // }

            // // 执行“load data into table”操作
            // String filepath = "/home/hadoop/file/test2_hive.txt";
            // sql = "load data local inpath '" + filepath + "' into table " +
            // tableName;
            // System.out.println("Running:" + sql);
            // res = stmt.executeQuery(sql);

            // 执行“select * query”操作
            // sql = "select * from " + tableName;
            // System.out.println("Running:" + sql);
            // res = stmt.executeQuery(sql);
            // System.out.println("执行“select * query”运行结果:");
            // while (res.next()) {
            // System.out.println(res.getInt(1) + "\t" + res.getString(2));
            // }
            //
            // // 执行“regular hive query”操作
            // sql = "select count(1) from " + tableName;
            // System.out.println("Running:" + sql);
            // res = stmt.executeQuery(sql);
            // System.out.println("执行“regular hive query”运行结果:");
            // while (res.next()) {
            // System.out.println(res.getString(1));
            //
            // }

            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}