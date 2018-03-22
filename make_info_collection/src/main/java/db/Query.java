package db;

import util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Query {

    /**
     * 执行JDBC查询操作 的模板方法，通过回掉实现对查询返回对象的封装。
     *
     * @param sql    sql语句
     * @param params sql参数
     * @param back   QueryCallBack的实现类，实现回掉。
     */
    public static void executeQueryTemplate(String sql, Object[] params, QueryCallBack back) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
            ps = conn.prepareStatement(sql);
            JDBCUtil.handleParamsForPreparedStatement(ps, params);
            rs = ps.executeQuery();
            back.doExecute(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.closeConnection(conn, ps, rs);
        }
    }

    /**
     * 批量进行增、删、改 JDBC操作的模板方法
     *
     * @param sql sql语句
     * @return sql语句影响的行数，若出错则返回-1.
     * @params sql语句的参数
     */
    public static int executeBatchDML(String sql, ArrayList<Object[]> paramList) {
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            conn = DBManager.getConnection();
            if (paramList == null) {
                return conn.prepareStatement(sql).executeUpdate();
            } else {
                conn.setAutoCommit(false);
                ps = conn.prepareStatement(sql);
                for (Object[] aParamList : paramList) {
                    JDBCUtil.handleParamsForPreparedStatement(ps, aParamList);
                    ps.addBatch();
                }
                int[] results = ps.executeBatch();
                conn.commit();
                for (int result1 : results) result += result1;
                return result == paramList.size() ? result : (-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                assert conn != null;
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBManager.closeConnection(ps, conn);
        }
    }
}
