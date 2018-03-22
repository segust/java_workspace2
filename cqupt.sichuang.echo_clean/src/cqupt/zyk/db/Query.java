package cqupt.zyk.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cqupt.zyk.util.JDBCUtil;



public class Query {

	/**
	 * 增、删、改 JDBC操作的模板方法
	 * @param sql sql语句
	 * @param params sql语句的参数
	 * @return sql语句影响的行数，若出错则返回-1.
	 */
	public static int executeDML(String sql, Object[] params){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBManager.getConnection();
			ps = conn.prepareStatement(sql);
			cqupt.zyk.util.JDBCUtil.handleParamsForPreparedStatement(ps, params);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			DBManager.closeConnection(ps,conn);
		}
	}
	
	/**
	 * 执行JDBC查询操作 的模板方法，通过回掉实现对查询返回对象的封装。
	 * @param sql sql语句
	 * @param params sql参数
	 * @param back QueryCallBack的实现类，实现回掉。
	 * @return 
	 */
	public static Object executeQueryTemplate(String sql,Object[] params,QueryCallBack back){
		PreparedStatement ps =null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DBManager.getConnection();
			ps = conn.prepareStatement(sql);
			JDBCUtil.handleParamsForPreparedStatement(ps, params);
			rs = ps.executeQuery();
			return back.doExecute(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			DBManager.closeConnection(conn, ps, rs);
		}
	}
	
	/**
	 * 批量进行增、删、改 JDBC操作的模板方法
	 * @param sql sql语句
	 * @param params sql语句的参数
	 * @return sql语句影响的行数，若出错则返回-1.
	 */
	public static int executeBatchDML(String sql, ArrayList<Object[]> paramList){
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = DBManager.getConnection();
			if(paramList==null){
				return conn.prepareStatement(sql).executeUpdate();
			}else{
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(sql);
				for(int i=0;i<paramList.size();i++){
					JDBCUtil.handleParamsForPreparedStatement(ps, paramList.get(i));
					ps.addBatch();
				}
				int[] results = ps.executeBatch();
				conn.commit();
				for(int i=0;i<results.length;i++)result += results[i];
				return result==paramList.size()?result:(-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}finally{
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DBManager.closeConnection(ps,conn);
		}
	}
}
