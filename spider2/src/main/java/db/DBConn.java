package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DBConn {
	
	static DataSource datasource;
	
	static{
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:mysql://172.22.147.15:3306/page_rank?"
				+ "useUnicode=true&amp;characterEncoding=utf-8;failOverReadOnly=false");
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername("root");
		p.setPassword("");
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(2);
		p.setInitialSize(1);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(1);
		p.setMaxIdle(2);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setFairQueue(true);
		p.setDefaultAutoCommit(true);
		p.setJdbcInterceptors(
				"org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
				"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		DBConn.datasource = new DataSource(p);
	}
	
	public static Connection getConn(){
		Connection result = null;
		try {
			Future<Connection> future = datasource.getConnectionAsync();
			result = future.get();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void closeConnection(ResultSet rs,Statement s,Connection conn)
	{
		try {
			if (rs != null) rs.close();
			if (s != null) s.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection(Statement s,Connection conn)
	{
		closeConnection(null,s,conn);
	}
	
}
