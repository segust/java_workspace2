package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.edu.cqupt.beans.FareDetail;
import cn.edu.cqupt.db.DBConnection;

public class FareDetailDAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;

	public FareDetailDAO() {

	}

	/**
	 * 添加费用明细
	 */
	public boolean addFareDetail(FareDetail fareDetail) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_fareDetail(detailName,detailAmount,fareId,detailTime,voucherNo,abstract,remark) VALUES(?,?,?,?,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, fareDetail.getDetailName());
			this.pstmt.setDouble(2, fareDetail.getDetailAmount());
			this.pstmt.setLong(3, fareDetail.getFareId());
			this.pstmt.setString(4, fareDetail.getDetailTime());
			this.pstmt.setString(5, fareDetail.getVoucherNo());
			this.pstmt.setString(6, fareDetail.getDetailAbstract());
			this.pstmt.setString(7, fareDetail.getRemark());
			int count = this.pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 通过fareDetailId deleted fareDetail
	 * 
	 * @param fareDetailId
	 * @return
	 */
	public boolean deleteByfareDetailId(long fareDetailId) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sqlDelete = "DELETE FROM qy_faredetail WHERE fareDetailId=?";
			this.pstmt = this.conn.prepareStatement(sqlDelete);
			this.pstmt.setLong(1, fareDetailId);
			int countDelete = this.pstmt.executeUpdate();
			if (countDelete > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 通过fareId 删除
	 */
	public boolean deleteByfareId(long fareId) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sqlDelete = "DELETE FROM qy_faredetail WHERE fareId=?";
			this.pstmt = this.conn.prepareStatement(sqlDelete);
			this.pstmt.setLong(1, fareId);
			int countDelete = this.pstmt.executeUpdate();
			if (countDelete > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 
	 */
	public boolean updateFareDetail(FareDetail fareDetail) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sql = "UPDATE qy_faredetail SET DetailName=?,fareDetailAmount=? WHERE fareDetailId=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, fareDetail.getDetailName());
			this.pstmt.setDouble(2, fareDetail.getDetailAmount());
			this.pstmt.setLong(3, fareDetail.getFareId());
			int count = this.pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * get fareDetail by fareId
	 */

	public ArrayList<FareDetail> getAllFareDetail(long fareId) {
		ArrayList<FareDetail> allDetail = new ArrayList<FareDetail>();
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_faredetail where fareId= ?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, fareId);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				FareDetail fareDetail = new FareDetail();
				fareDetail.setFareDetailId(rs.getLong("fareDetailId"));
				fareDetail.setDetailName(rs.getString("detailName"));
				fareDetail.setDetailTime(rs.getString("detailTime"));
				fareDetail.setVoucherNo(rs.getString("voucherNo"));
				fareDetail.setRemark(rs.getString("remark"));
				fareDetail.setDetailAbstract(rs.getString("abstract"));
				fareDetail.setDetailAmount(rs.getDouble("detailAmount"));
				fareDetail.setFareId(rs.getLong("fareId"));
				allDetail.add(fareDetail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return allDetail;
	}

	/**
	 * 统计经费 通过fareId
	 * 
	 * 
	 */
	public HashMap<String, Double> getStatisticsDetail(
			String fareType, String startDate, String endDate) {
		LinkedHashMap<String, Double> sumMap = new LinkedHashMap<String, Double>();
//		String fareIds = "";
//		for (int i = 0; i < checkFare.size(); i++) { // 得到所勾选的
//			String s = checkFare.get(i).toString();
//			if (s != null) {
//				StringBuilder stringBuilder = new StringBuilder();// 拼接字符串
//				stringBuilder.append("'");
//				stringBuilder.append(s);
//				if (i != checkFare.size() - 1) {
//					stringBuilder.append("',");
//				} else {
//					stringBuilder.append("'");
//				}
//				fareIds = fareIds + stringBuilder.toString();
//			}
//		}
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT a.detailName,SUM(a.detailAmount) FROM qy_faredetail a INNER JOIN qy_fare b ON a.fareId=b.fareId ";
			if (!fareType.equals("")) {
				sql += "WHERE b.fareType IN (" + fareType + ") ";
				if (!startDate.equals("") && startDate != null
						&& !endDate.equals("") && endDate != null)
					sql += " AND DATE_FORMAT(a.detailTime,'%Y-%m-%d') BETWEEN '" + startDate + "' AND '"
							+ endDate+"'";
			}
			else if(!startDate.equals("") && startDate != null
					&& !endDate.equals("") && endDate != null)
				sql += " WHERE a.detailTime BETWEEN '" + startDate + "' AND '"
						+ endDate+"'";
			sql += " GROUP BY  a.detailName";
			this.pstmt = this.conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			// System.out.println("sql==" + sql);
			Double sumAmonut=0.0;
			while (rs.next()) {
				sumMap.put(rs.getString(1), rs.getDouble(2));
				sumAmonut+=rs.getDouble(2);
			}
			sumMap.put("合计", sumAmonut);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		// System.out.println("Dao=" + sumMap);
		return sumMap;
	}
}
