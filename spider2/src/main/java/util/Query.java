package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DBConn;

/**
 * 这个类提供了JDBC的模板操作，但是必须在tomcat中运行。
 * 
 *
 */

public class Query {

	/**
	 * 增、删、改 JDBC操作的模板方法 tomcat
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            sql语句的参数
	 * @return sql语句影响的行数，若出错则返回-1.
	 */
	public static int executeDML(String sql, Object[] params) {
		Connection conn = null;
		PreparedStatement ps = null;
		int result = -1;
		try {
			conn = DBConn.getConn();
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParamsForPreparedStatement(ps, params);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			result = -1;
		} finally {
			DBConn.closeConnection(ps, conn);
		}
		return result;

	}

	/**
	 * 执行JDBC查询操作 的模板方法，通过回掉实现对查询返回对象的封装。 tomcat
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            sql参数
	 * @param back
	 *            QueryCallBack的实现类，实现回掉。
	 * @return
	 */
	public static Object executeQueryTemplate(String sql, Object[] params, QueryCallBack back) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DBConn.getConn();
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParamsForPreparedStatement(ps, params);
			rs = ps.executeQuery();
			return back.doExecute(rs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBConn.closeConnection(rs, ps, conn);
		}
	}

	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int[] executeDMLBatch(String sql, List<Object[]> params) {
		Connection conn = DBConn.getConn();
		PreparedStatement ps = null;
		int[] result = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for (Object[] param : params) {
				JDBCUtils.handleParamsForPreparedStatement(ps, param);
				ps.addBatch();
			}
			result = ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			result = null;
			e.printStackTrace();
		} finally {
			DBConn.closeConnection(ps, conn);
		}
		return result;
	}

	public static int[] executeDMLBatchWithSingleParams(String sql, Object[] params) {

		Connection conn = DBConn.getConn();
		PreparedStatement ps = null;
		int[] result = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for (Object param : params) {
				JDBCUtils.handleParamsForPreparedStatement(ps, new Object[] { param });
				ps.addBatch();
			}
			result = ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			result = null;
			e.printStackTrace();
		} finally {
			DBConn.closeConnection(ps, conn);
		}
		return result;
	}

	public static int[] executeDMLBatchForEachWithSingleParams(String sql, Object[] params) {
		int[] result = new int[params.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = Query.executeDML(sql, new Object[] { params[i] });
		}

		return result;
	}

	public static int[] executeDMLBatchForEach(String sql, List<Object[]> params) {
		int[] result = new int[params.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = Query.executeDML(sql, params.get(i));
		}
		return result;
	}

	public static void main(String[] args) {
		String sql = "INSERT INTO `page_rank`.`relationship` (`fans`, `follow`) VALUES (?, '2');";
		Object[] params = new Object[] { 1, 2 };
		// exetuteDMLBatchForEachWithSingleParams(sql, params);
	}

}
