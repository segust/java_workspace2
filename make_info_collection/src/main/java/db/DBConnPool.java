package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnPool {

	/**
	 * 数据库连接池
	 */
	private List<Connection> pool;
	/**
	 * 连接池中最大连接数
	 */
	private static final int POOL_MAX_SIZE = DBManager.getDbConfig().getPoolMaxSize();
	/**
	 * 连接池中最小连接数
	 */
	private static final int POOL_MIN_SIZE = DBManager.getDbConfig().getPoolMixSize();
	
	/**
	 * 初始化连接池，并且判断当连接数小于最小连接数时，需要增加连接
	 */
	public void initPool() {
		if (pool == null) {
			pool = new ArrayList<Connection>();
		}
		long startTime = System.currentTimeMillis();
		while (pool.size() < DBConnPool.POOL_MIN_SIZE) {
			pool.add(DBManager.createConnection());
		}
		System.out.print("初始化数据库连接池耗时：");
		System.out.println(System.currentTimeMillis()-startTime);
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public synchronized Connection getConnection(){
		int last_index = pool.size();
		Connection conn = pool.get(last_index-1);
		pool.remove(last_index-1);
		return conn;
	}
	/**
	 数据库连接
	 * @param conn
	 */
	public synchronized void closeConnection(Connection conn){
		if(pool.size()>=POOL_MAX_SIZE){
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			pool.add(conn);
		}
	}
	public DBConnPool() {
		initPool();
	}
}
