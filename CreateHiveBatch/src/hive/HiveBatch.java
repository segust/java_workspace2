package hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handle data through hive on eclipse
 * 
 * @author segust
 * @time 2017\8\20 19:14
 */
public class HiveBatch {

	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
	private static String url = "jdbc:hive://172.22.145.1：10000/passinfo";
	private static String user = "root";
	private static String password = "cquptmiis2016+";
	private static String sql = "";

	public static String[] tableList;

	public static void main(String[] args) {
		setList();
		try {
			Class.forName(driverName);
			// Connection conn = DriverManager.getConnection(url, user,
			// password);
			// 默认使用端口10000, 使用默认数据库，用户名密码默认
			Connection conn = DriverManager.getConnection(url, user, password);
			Statement stmt = conn.createStatement();

			for (String table : tableList) {
				sql = " create table `" + table
						+ "`(license_no string,pass_time string,pass_port string ,device string,"
						+ "direction string,license_type string,license_color string,vehicle string,vehicle_len string,"
						+ "vehicle_type string,speed string,feature_image string,panorama_image string,"
						+ "pass_port_name string,direction_name string,lane string,lane_name string,"
						+ "vehicle_body_type string,vehicle_loge_type string,data_source string,lat string,lng string,"
						+ "etc1 string,etc2 string,etc3 string)" + "with serdeproperties(" + "\"separatorChar\"=\",\""
						+ "\"quoteChar\"=\"'\"" + "\"escateChar\"=\"\\\\\"" + ")" + "stored as textfile;";
				stmt.executeQuery(sql);
			}

			// // 创建的表名
			// String tableName = "testHiveDriverTable";
			//
			// /** 第一步:存在就先删除 **/
			// sql = "drop table " + tableName;
			// stmt.executeQuery(sql);
			//
			// /** 第二步:不存在就创建 **/
			// sql = "create table " + tableName
			// + " (key int, value string) row format delimited fields
			// terminated by '\t'";
			// stmt.executeQuery(sql);
			//
			// // 执行“show tables”操作
			// sql = "show tables '" + tableName + "'";
			// System.out.println("Running:" + sql);
			// res = stmt.executeQuery(sql);
			// System.out.println("执行“show tables”运行结果:");
			// if (res.next()) {
			// System.out.println(res.getString(1));
			// }

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
			conn = null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public static void setList() {
		TableList list = new TableList();
		tableList = list.getList();
	}
}