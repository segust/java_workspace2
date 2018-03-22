package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.edu.cqupt.beans.Attach;
import cn.edu.cqupt.db.DBConnection;

/**
 * 经费管理-附件操作
 * 
 * @author lsy&yg
 * 
 */
public class AttachDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;

	public AttachDAO() {
	}

	/**
	 * addAttach 向一个经费管理项目中添加一个附件
	 * 
	 * @param
	 * @return
	 */
	public boolean addAttach(Attach attach, int fareId) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_attach VALUES(null,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, attach.getAttachTitle());
			this.pstmt.setString(2, attach.getAttachPath());
			this.pstmt.setInt(3, fareId);
			int count = this.pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt);
		}
		return flag;
	}

	/**
	 * deleteAttach 一个经费管理项目中删除一个附件
	 * 
	 * @param
	 * @return
	 */
	public boolean deleteAttachByFareId(long fareId) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sql = "DELETE qy_attach.* FROM qy_attach WHERE fareId=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, fareId);
			int count = this.pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt);
		}
		return flag;
	}

	/**
	 * 删除一个经费下的某一个附件
	 * 
	 * @param attachId
	 * @return
	 */
	public boolean deleteAttachByAttachTitle(int attachId) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sql = "DELETE FROM qy_attach WHERE attachId=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setInt(1, attachId);
			int count = this.pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt);
		}
		return flag;
	}

	/**
	 * getAllAttachInAFare 得到一个经费项目的全部附件信息
	 * 
	 * @return allQualifyMap
	 */
	public ArrayList<Attach> getAllAttachInAFare(long fareId) {
		ArrayList<Attach> allAttach = new ArrayList<Attach>();
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT qy_attach.* FROM qy_fare,qy_attach WHERE qy_fare.fareId=? "
					+ "AND qy_fare.fareID=qy_attach.fareID ";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, fareId);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Attach attach = new Attach();
				attach.setAttachId(rs.getInt("attachId"));
				attach.setAttachTitle(rs.getString("attachTitle"));
				attach.setAttachPath(rs.getString("attachPath"));
				attach.setFareId(rs.getInt("fareId"));
				allAttach.add(attach);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt, rs);
		}
		return allAttach;
	}

	/**
	 * 通过fareId 得到所有的title(uuid名)用来删除本地文件
	 * 
	 * @param fareId
	 * @return
	 */
	public ArrayList<String> getAllTitleInAFare(long fareId) {
		ArrayList<String> allTitle = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultSet rs = null;
		String sql = "SELECT attachTitle  from qy_attach WHERE fareId=?";
		try {
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, fareId);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				String attachTitle = rs.getString("attachTitle");
				allTitle.add(attachTitle);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allTitle;
	}

	/**
	 * 根据附件id获取附件在服务器的路径
	 * @param attachId
	 * @return
	 */
	public String getAttachPathById(int attachId) {
		String attachPath = "";
		try {
			conn = DBConnection.getConn();
			ResultSet rs = null;
			String sql = "SELECT a.attachPath from qy_attach a WHERE a.attachId=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setInt(1, attachId);
			rs = this.pstmt.executeQuery();
			if (rs.next()) {
				attachPath = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attachPath;
	}

}
