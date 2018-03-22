package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.cqupt.db.DBConnection;

public class OutListProductRelationDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public OutListProductRelationDAO() {
		
	}
	/**
	 * 
	 * @param lId
	 * @param pId
	 * @return
	 */
	public boolean addRelation(String lId, String[] pId) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			String sql = "Insert into qy_outlistproductrelation (productId,listId) values (?,?)";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < pId.length; i++) {
				pstmt.setString(1, pId[i]);
				pstmt.setString(2, lId);
				pstmt.addBatch();
			}
			int[] count = pstmt.executeBatch();
			conn.commit();
			if(count.length > 0) {
				flag = true;
			}
		}catch(Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	
}
