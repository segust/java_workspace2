package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.edu.cqupt.beans.RepairInfo;
import cn.edu.cqupt.db.DBConnection;

public class RepairDao {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public RepairDao() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 添加一条设备维修记录
	 * 
	 * @param repairInfo
	 * @return
	 */
	public boolean addRepairInfo(RepairInfo repairInfo) {
		boolean flag = false;
		PreparedStatement pstmt2 = null;
		try {
			conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_repairinfo(did,devicename,deviceno,repairman,repairtime,repairreason) VALUES(?,?,?,?,?,?)";
			String sql2 = "UPDATE qy_deviceinfo a SET a.repairtime=? WHERE a.deviceid=?";
			this.pstmt = this.conn.prepareStatement(sql);
			pstmt2 = this.conn.prepareStatement(sql2);
			this.pstmt.setLong(1, repairInfo.getDeviceId());
			this.pstmt.setString(2, repairInfo.getDeviceName());
			this.pstmt.setString(3, repairInfo.getDeviceNo());
			this.pstmt.setString(4, repairInfo.getRepairMan());
			this.pstmt.setString(5, repairInfo.getRepairTime());
			this.pstmt.setString(6, repairInfo.getRepairReason());
			pstmt2.setString(1, repairInfo.getRepairTime());
			pstmt2.setLong(2, repairInfo.getDeviceId());
			int count = this.pstmt.executeUpdate();
			int count2 = pstmt2.executeUpdate();
			if (count > 0 && count2 > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 对一条维修记录修改
	 * 
	 * @param repairInfo
	 * @param repairId
	 * @return
	 */
	public boolean updateRepair(RepairInfo repairInfo, long repairId) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "UPDATE qy_repairinfo a SET a.repairman=?,a.repairtime=?,a.repairreason=? WHERE a.repairid=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, repairInfo.getRepairMan());
			this.pstmt.setString(2, repairInfo.getRepairTime());
			this.pstmt.setString(3, repairInfo.getRepairReason());
			this.pstmt.setLong(4, repairId);
			int count = pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 删除一条维修记录
	 * 
	 * @param repairId
	 * @return
	 */
	public boolean deleteRepair(long repairId) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "DELETE FROM qy_repairinfo WHERE qy_repairinfo.repairId=?";
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setLong(1, repairId);
			int count = pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 分页显示某一个设备的维修记录
	 * 
	 * @param curPageNum
	 * @param pageSize
	 * @param deviceId
	 * @return
	 */
	public ArrayList<RepairInfo> searchRepairInfoByPage(int curPageNum,
			int pageSize, long deviceId) {
		ArrayList<RepairInfo> curRepairInfoList = new ArrayList<RepairInfo>();
		String sql = "SELECT * FROM qy_repairinfo WHERE qy_repairinfo.did=? ORDER BY qy_repairinfo.repairid LIMIT ?,?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setLong(1, deviceId);
			this.pstmt.setInt(2, (curPageNum - 1) * pageSize);
			this.pstmt.setInt(3, pageSize);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				RepairInfo repairInfo = new RepairInfo();
				repairInfo.setRepairId(rs.getLong("repairid"));
				repairInfo.setDeviceId(rs.getLong("did"));
				repairInfo.setRepairMan(rs.getString("repairman"));
				repairInfo.setRepairTime(rs.getString("repairtime"));
				repairInfo.setRepairReason(rs.getString("repairreason"));
				curRepairInfoList.add(repairInfo);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return curRepairInfoList;
	}

	/**
	 * 获取某一设备维修记录表的记录数
	 * 
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public long getRepairSumByOneDevice(long deviceId) {
		long sum = 0;
		String sql = "SELECT COUNT(*) FROM qy_repairinfo WHERE qy_repairinfo.did=?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, deviceId);
			rs = this.pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return sum;
	}
}
