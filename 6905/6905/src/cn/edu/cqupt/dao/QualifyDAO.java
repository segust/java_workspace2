package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import cn.edu.cqupt.beans.Qualify;
import cn.edu.cqupt.db.DBConnection;

public class QualifyDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public QualifyDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 添加一个资产文件
	 * 
	 * @param qualify
	 * @return
	 */
	public boolean addQualify(Qualify qualify) {
		boolean flag = false;
		try {
			this.pstmt = this.conn
					.prepareStatement("INSERT INTO qy_qualify(qualifyType,qualifyTitle,qualifyPath,ownedUnit,year,qualifyAttr) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE qualifyPath=VALUES(qualifyPath)");
			this.pstmt.setString(1, qualify.getQualifyType());
			this.pstmt.setString(2, qualify.getQualifyTitle());
			this.pstmt.setString(3, qualify.getQualifyPath());
			this.pstmt.setString(4, qualify.getOwnedUnit());
			this.pstmt.setString(5, qualify.getYear());
			this.pstmt.setString(6, qualify.getQualifyAttr());
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
	 * 删除一个资产文件
	 * 
	 * @param qualifyId
	 * @return
	 */
	public boolean deleteQualify(Long qualifyId) {
		boolean flag = false;
		try {
			this.pstmt = this.conn
					.prepareStatement("DELETE FROM qy_qualify WHERE qualifiyId='"
							+ qualifyId + "' ");
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
	 * 更新一个资产文件
	 * 
	 * @param qualifyId
	 * @param qualifyType
	 * @param qualifyPath
	 * @param qualifyTitle
	 * @return
	 */
	public boolean updateQualify(Long qualifyId, String qualifyType,
			String qualifyPath, String qualifyTitle) {
		boolean flag = false;
		try {
			this.pstmt = this.conn
					.prepareStatement("UPDATE qy_qualify SET qualifyType=?,"
							+ " qualifyTitle=?, qualifyPath=? WHERE qualifiyId=? ");
			this.pstmt.setString(1, qualifyType);
			this.pstmt.setString(2, qualifyTitle);
			this.pstmt.setString(3, qualifyPath);
			this.pstmt.setLong(4, qualifyId);
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
	 * 分页显示当前代储企业所有的资质文件
	 * @param curPageNum
	 * @param pageSize
	 * @param ownedUnit
	 * @return
	 */
	public ArrayList<Qualify> searchQualifyByPage(int curPageNum, int pageSize,String ownedUnit) {
		ArrayList<Qualify> allQualifyList = new ArrayList<Qualify>();
		String sql = "SELECT * FROM qy_qualify WHERE qy_qualify.ownedUnit=? ORDER BY qualifiyId LIMIT ?,?";
		try {
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, ownedUnit);
			this.pstmt.setInt(2, (curPageNum - 1) * pageSize);
			this.pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Qualify qualify = new Qualify(rs.getLong(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7));
				allQualifyList.add(qualify);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return allQualifyList;
	}
	
	/**
	 * 获取所有资质的个数
	 * 
	 * @param ownedUnit
	 * @return
	 */
	public long getQualifySum(String ownedUnit) {
		long qualifySum = 0;
		String sql = "SELECT COUNT(*) FROM qy_qualify WHERE qy_qualify.ownedUnit=?";
		try {
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, ownedUnit);
			this.rs = pstmt.executeQuery();
			if (rs.next())
				qualifySum = rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}

	/**
	 * 按标题和代储企业得到部分的资产信息
	 * 
	 * @param searchStr
	 * @param curPageNum
	 * @param pageSize
	 * @param ownedUnit
	 * @return
	 */
	public ArrayList<Qualify> getTitleQualifyByPage(String searchStr,
			int curPageNum, int pageSize,String ownedUnit) {
		ArrayList<Qualify> partQualifyList = new ArrayList<Qualify>();
		try {
			this.pstmt = this.conn
					.prepareStatement("SELECT * FROM qy_qualify WHERE qualifyTitle REGEXP ? AND ownedUnit=? ORDER BY qualifiyId LIMIT ?,?");
			this.pstmt.setString(1, ".*" + searchStr + ".*");
			this.pstmt.setString(2, ownedUnit);
			this.pstmt.setInt(3, (curPageNum - 1) * pageSize);
			this.pstmt.setInt(4, pageSize);
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Qualify qualify = new Qualify(rs.getInt(1), rs.getString(2), rs
						.getString(3), rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7));
				partQualifyList.add(qualify);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, this.rs);
		}
		return partQualifyList;
	}

	/**
	 * 按标题和所属企业得到部分的资产信息的记录条数
	 * 
	 * @param searchStr
	 * @param ownedUnit
	 * @return
	 */
	public long getTitleQualifySum(String searchStr,String ownedUnit) {
		long qualifySum = 0;
		String sql = "SELECT COUNT(*) FROM qy_qualify WHERE qualifyTitle REGEXP ? AND ownedUnit=?";
		try {
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, ".*" + searchStr + ".*");
			this.pstmt.setString(2, ownedUnit);
			this.rs = pstmt.executeQuery();
			if (rs.next())
				qualifySum = rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}
	
	/**
	 * 按类型和代储企业得到部分的资产信息
	 * 
	 * @param qualifyType
	 * @param curPageNum
	 * @param pageSize
	 * @param ownedUnit
	 * @return
	 */
	public ArrayList<Qualify> getTypeQualifyByPage(String qualifyType,
			int curPageNum, int pageSize,String ownedUnit) {
		ArrayList<Qualify> partQualifyList = new ArrayList<Qualify>();
		try {
			this.pstmt = this.conn
					.prepareStatement("SELECT * FROM qy_qualify WHERE qualifyType REGEXP ? AND ownedUnit=? ORDER BY qualifiyId LIMIT ?,?");
			this.pstmt.setString(1, qualifyType);
			this.pstmt.setString(2, ownedUnit);
			this.pstmt.setInt(3, (curPageNum - 1) * pageSize);
			this.pstmt.setInt(4, pageSize);
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Qualify qualify = new Qualify(rs.getInt(1), rs.getString(2), rs
						.getString(3), rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7));
				partQualifyList.add(qualify);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, this.rs);
		}
		return partQualifyList;
	}

	/**
	 * 按类型得到部分的资产信息的记录条数
	 * 
	 * @param searchType
	 * @param ownedUnit
	 * @return
	 */
	public long getTypeQualifySum(String searchType,String ownedUnit) {
		long qualifySum = 0;
		String sql = "SELECT COUNT(*) FROM qy_qualify WHERE qualifyType = ? AND ownedUnit=?";
		try {
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, searchType);
			this.pstmt.setString(2, ownedUnit);
			this.rs = pstmt.executeQuery();
			if (rs.next())
				qualifySum = rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}

	
	/**
	 * 按标题、类型和代储企业得到部分的资产信息 军代室及以上版本
	 * 
	 * @param searchStr
	 * @param qualifyType
	 * @param curPageNum
	 * @param pageSize
	 * @param ownedUnit
	 * @return
	 */
	public ArrayList<Qualify> getPartQualifyByPage(String searchStr,
			String searchType, String year,String searchAttr,int curPageNum, int pageSize,String ownedUnit) {
		ArrayList<Qualify> partQualifyList = new ArrayList<Qualify>();
		try {
			this.pstmt = this.conn
					.prepareStatement("SELECT * FROM qy_qualify a WHERE a.qualifyTitle REGEXP ?"
							+ " AND a.year REGEXP ? AND a.qualifyAttr REGEXP ? AND a.qualifyType REGEXP ? AND a.ownedUnit=? ORDER BY a.qualifiyId LIMIT ?,?");
			this.pstmt.setString(1, searchStr);
			this.pstmt.setString(2, year);
			this.pstmt.setString(3, searchAttr);
			this.pstmt.setString(4, searchType);
			this.pstmt.setString(5, ownedUnit);
			this.pstmt.setInt(6, (curPageNum - 1) * pageSize);
			this.pstmt.setInt(7, pageSize);
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Qualify qualify = new Qualify(rs.getLong(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7));
				partQualifyList.add(qualify);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, this.rs);
		}
		return partQualifyList;
	}

	/**
	 * 按标题和类型得到部分的资产信息的记录条数
	 * 
	 * @param searchStr
	 * @param searchType
	 * @param ownedUnit
	 * @return
	 */
	public long getPartQualifySum(String searchStr, String searchType,String year,String searchAttr,String ownedUnit) {
		long qualifySum = 0;
		String sql = "SELECT COUNT(*) FROM qy_qualify a WHERE a.qualifyTitle REGEXP ?"
				+ " AND a.year REGEXP ? AND a.qualifyAttr REGEXP ? AND a.qualifyType REGEXP ? AND a.ownedUnit=?";
		try {
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, searchStr);
			this.pstmt.setString(2, year);
			this.pstmt.setString(3, searchAttr);
			this.pstmt.setString(4, searchType);
			this.pstmt.setString(5, ownedUnit);
			this.rs = pstmt.executeQuery();
			if (rs.next())
				qualifySum = rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}

	/**
	 * getAllQualify 得到全部的资产信息
	 * 
	 * @return
	 */
	public ArrayList<Qualify> getAllQualify() {
		ArrayList<Qualify> allQualifyList = new ArrayList<Qualify>();
		try {
			this.pstmt = this.conn.prepareStatement("SELECT * FROM qy_qualify");
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				Qualify qualify = new Qualify(rs.getLong(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7));
				allQualifyList.add(qualify);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, this.rs);
		}
		return allQualifyList;
	}

	/**
	 * 通过资质文件id得到资质文件记录
	 * 
	 * @param qualifyId
	 * @return
	 */
	public Qualify getCurQualifyById(long qualifyId) {
		Qualify curQualify=new Qualify();
		try {
			this.pstmt = this.conn
					.prepareStatement("SELECT * FROM qy_qualify WHERE	qualifiyId=?");
			this.pstmt.setLong(1, qualifyId);
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				curQualify.setQualifyPath(rs.getString("qualifyPath"));
				curQualify.setQualifyTitle(rs.getString("qualifyTitle"));
				curQualify.setQualifyType(rs.getString("qualifyType"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, this.rs);
		}
		return curQualify;
	}

	/**
	 * 通过资质文件id得到资质文件路径
	 * 
	 * @param qualifyId
	 * @return
	 */
	public String getQualifyPathById(long qualifyId) {
		String qualifyPath = "";
		try {
			this.pstmt = this.conn
					.prepareStatement("SELECT qualifypath FROM qy_qualify WHERE qualifiyId=?");
			this.pstmt.setLong(1, qualifyId);
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				qualifyPath = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, this.rs);
		}
		return qualifyPath;
	}

	/**
	 * 判断是否有重复资质文件名和资质文件类型
	 * 
	 * @return
	 *//*
	public boolean repeatQualify(String qualifyTitle, String qualifyType,
			String ownedUnit) {
		boolean repeatFlag = false;
		try {
			this.pstmt = this.conn
					.prepareStatement("SELECT * FROM qy_qualify WHERE qy_qualify.qualifyTitle=? AND qy_qualify.qualifyType=? AND ownedUnit=?");
			this.pstmt.setString(1, qualifyTitle);
			this.pstmt.setString(2, qualifyType);
			this.pstmt.setString(3, ownedUnit);
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				repeatFlag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, this.rs);
		}
		return repeatFlag;
	}*/
}