package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.db.DBConnection;

public class InfoDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = null;
	private boolean flag = false;

	public InfoDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 根据版本不同查询当前版本下的根目录
	 * 
	 * @param rootLevel
	 * @return
	 */
	public ArrayList<String> searchRootLevelContent(String rootLevel) {
		ArrayList<String> allRootLevelContentList = new ArrayList<String>();
		String rootLevelName = "";
		String rootLevelTable = "";
		if (rootLevel.equals("zhj")) {
			rootLevelName = "zhjName";
			rootLevelTable = "zhjinfo";
		} else if (rootLevel.equals("jdj")) {
			rootLevelName = "jdjName";
			rootLevelTable = "jdjinfo";
		} else if (rootLevel.equals("jds")) {
			rootLevelName = "jdsName";
			rootLevelTable = "jdsinfo";
		}

		sql = "SELECT " + rootLevelName + " FROM " + rootLevelTable;

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				allRootLevelContentList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return allRootLevelContentList;
	}

	/**
	 * 查询当前部门的下一级部门 得到目录
	 * 
	 * @param nextLevel
	 * @param curLevelName
	 * @return
	 */
	public ArrayList<String> searchNextLevelContent(String nextLevel,
			String curLevelName) {
		ArrayList<String> allNameList = new ArrayList<String>();
		String selfName = null;
		String selfTable = null;
		String ownedName = null;

		if (nextLevel.equals("jdj")) {
			selfName = "jdjName";
			selfTable = "jdjinfo";
			ownedName = "ownedZhjName";
		} else if (nextLevel.equals("jds")) {
			selfName = "jdsName";
			selfTable = "jdsinfo";
			ownedName = "ownedJdjName";
		} else if (nextLevel.equals("company")) {
			selfName = "companyName";
			selfTable = "companyinfo";
			ownedName = "ownedJdsName";
		}
		sql = "SELECT " + selfName + " FROM " + selfTable + " WHERE "
				+ ownedName + "= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, curLevelName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				allNameList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return allNameList;
	}

	/**
	 * 判断当前部门下是否还有下级部门
	 * 
	 * @param nextLevel
	 * @param curLevelName
	 * @return
	 */
	public boolean ifExistNextLevelContent(String nextLevel, String curLevelName) {
		boolean existFlag = false;
		String selfName = null;
		String selfTable = null;
		String ownedName = null;

		if (nextLevel.equals("jdj")) {
			selfName = "jdjName";
			selfTable = "jdjinfo";
			ownedName = "ownedZhjName";
		} else if (nextLevel.equals("jds")) {
			selfName = "jdsName";
			selfTable = "jdsinfo";
			ownedName = "ownedJdjName";
		} else if (nextLevel.equals("company")) {
			selfName = "companyName";
			selfTable = "companyinfo";
			ownedName = "ownedJdsName";
		}
		sql = "SELECT " + selfName + " FROM " + selfTable + " WHERE "
				+ ownedName + "= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, curLevelName);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				existFlag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return existFlag;
	}

	/**
	 * 查询当前部门的基本信息
	 * 
	 * @param curLevel
	 * @param curLevelName
	 * @return
	 */
	public ArrayList<String> searchCurLevelInfo(String curLevel,
			String curLevelName) {
		ArrayList<String> curLevelInfoList = new ArrayList<String>();
		String searchTable = null;
		String searchName = null;

		if (curLevel.equals("zhj")) {
			searchTable = "zhjinfo";
			searchName = "zhjName";
		} else if (curLevel.equals("jdj")) {
			searchTable = "jdjinfo";
			searchName = "jdjName";
		} else if (curLevel.equals("jds")) {
			searchTable = "jdsinfo";
			searchName = "jdsName";
		} else if (curLevel.equals("company")) {
			searchTable = "companyinfo";
			searchName = "companyName";
		}

		sql = "SELECT * FROM " + searchTable + " WHERE " + searchName + "= ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, curLevelName);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (curLevel.equals("zhj")) {
					curLevelInfoList.add(rs.getString("zhjName"));
					curLevelInfoList.add(rs.getString("zhjManager"));
					curLevelInfoList.add(rs.getString("leader"));
				} else if (curLevel.equals("jdj")) {
					curLevelInfoList.add(rs.getString("jdjName"));
					curLevelInfoList.add(rs.getString("ownedZhjName"));
					curLevelInfoList.add(rs.getString("jdjManager"));
					curLevelInfoList.add(rs.getString("leader"));
				} else if (curLevel.equals("jds")) {
					curLevelInfoList.add(rs.getString("jdsName"));
					curLevelInfoList.add(rs.getString("ownedJdjName"));
					curLevelInfoList.add(rs.getString("jdsManager"));
					curLevelInfoList.add(rs.getString("leader"));
				} else if (curLevel.equals("company")) {
					curLevelInfoList.add(rs.getString("companyName"));
					curLevelInfoList.add(rs.getString("ownedJdsName"));
					curLevelInfoList.add(rs.getString("companyManager"));
					curLevelInfoList.add(rs.getString("leader"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return curLevelInfoList;
	}

	/**
	 * 通过部门名字获取在数据库的id
	 * 
	 * @param curLevel
	 * @param curLevelName
	 * @return
	 */
	public long searchIdByName(String curLevel, String curLevelName) {
		long curId = 0;
		String searchTable = null;
		String searchName = null;
		if (curLevel.equals("zhj")) {
			searchTable = "zhjinfo";
			searchName = "zhjName";
			sql = "SELECT zhjId FROM " + searchTable + " WHERE " + searchName
					+ "= ?";
		} else if (curLevel.equals("jdj")) {
			searchTable = "jdjinfo";
			searchName = "jdjName";
			sql = "SELECT jdjId FROM " + searchTable + " WHERE " + searchName
					+ "= ?";
		} else if (curLevel.equals("jds")) {
			searchTable = "jdsinfo";
			searchName = "jdsName";
			sql = "SELECT jdsId FROM " + searchTable + " WHERE " + searchName
					+ "= ?";
		} else if (curLevel.equals("company")) {
			searchTable = "companyinfo";
			searchName = "companyName";
			sql = "SELECT companyId FROM " + searchTable + " WHERE "
					+ searchName + "= ?";
		}
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, curLevelName);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				curId = rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return curId;
	}

	/**
	 * 删除当前部门
	 * 
	 * @param curLevel
	 * @param curLevelName
	 * @return
	 */
	public boolean deleteInfo(String curLevel, String curLevelName) {
		String deleteTable = null;
		String deleteName = null;

		if (curLevel.equals("zhj")) {
			deleteTable = "zhjinfo";
			deleteName = "zhjName";
		} else if (curLevel.equals("jdj")) {
			deleteTable = "jdjinfo";
			deleteName = "jdjName";
		} else if (curLevel.equals("jds")) {
			deleteTable = "jdsinfo";
			deleteName = "jdsName";
		} else if (curLevel.equals("company")) {
			deleteTable = "companyinfo";
			deleteName = "companyName";
		}

		sql = "DELETE FROM " + deleteTable + " WHERE " + deleteName + "= ?";

		try {
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, curLevelName);
			if (pstmt.executeUpdate() >= 1)
				flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}

		return flag;
	}

	/**
	 * 添加下级部门
	 * 
	 * @param infoName
	 * @param ownedName
	 * @param infoManager
	 * @param leader
	 * @param curLevel
	 * @return
	 */
	public boolean addInfo(String infoName, String ownedName,
			String infoManager, String leader, String curLevel) {
		String addTable = null;

		if (curLevel.equals("zhj")) {
			addTable = "jdjinfo";
		} else if (curLevel.equals("jdj")) {
			addTable = "jdsinfo";
		} else if (curLevel.equals("jds")) {
			addTable = "companyinfo";
		}

		sql = "INSERT INTO " + addTable + " VALUES (null,?,?,?,?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, infoName);
			pstmt.setString(2, ownedName);
			pstmt.setString(3, infoManager);
			pstmt.setString(4, leader);
			if (pstmt.executeUpdate() > 0)
				;
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 更新指挥局
	 * 
	 * @param zhjId
	 * @param zhjName
	 * @param zhjManager
	 * @param leader
	 * @return
	 */
	public boolean updateInfo(long zhjId, String zhjName, String zhjManager,
			String leader) {
		sql = "UPDATE zhjinfo SET zhjName=" + zhjName + ",zhjManager="
				+ zhjManager + ",leader=" + leader + " WHERE zhjId=" + zhjId;

		try {
			pstmt = conn.prepareStatement(sql);
			flag = pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 更新除指挥局外的部门
	 * 
	 * @param infoId
	 * @param infoName
	 * @param ownedName
	 * @param infoManager
	 * @param leader
	 * @param curLevel
	 * @return
	 */
	public boolean updateInfo(long infoId, String infoName, String ownedName,
			String infoManager, String leader, String curLevel) {
		String updateId = null;
		String updateName = null;
		String updateOwnedName = null;
		String updateManager = null;
		String updateTable = null;
		String sql = null;

		if (curLevel.equals("jdj")) {
			updateId = "jdjId";
			updateName = "jdjName";
			updateOwnedName = "ownedzhjName";
			updateManager = "jdjManager";
			updateTable = "jdjinfo";
		} else if (curLevel.equals("jds")) {
			updateId = "jdsId";
			updateName = "jdsName";
			updateOwnedName = "ownedJdjName";
			updateManager = "jdsManager";
			updateTable = "jdsinfo";
		} else if (curLevel.equals("company")) {
			updateId = "companyId";
			updateName = "companyName";
			updateOwnedName = "ownedJdsName";
			updateManager = "companyManager";
			updateTable = "companyinfo";
		}
		sql = "UPDATE " + updateTable + " SET " + updateName + "=?,"
				+ updateOwnedName + "=?," + updateManager
				+ "=?,leader=? WHERE " + updateId + "=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, infoName);
			pstmt.setString(2, ownedName);
			pstmt.setString(3, infoManager);
			pstmt.setString(4, leader);
			pstmt.setLong(5, infoId);
			if (pstmt.executeUpdate() > 0)
				flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 根据版本获取所属部门下的代储企业集合
	 * 
	 * @param departmentName
	 *            部门名称
	 * @param version
	 *            版本
	 * @return
	 */
	public ArrayList<String> getCompanyNameList(String departmentName,
			int version) {
		ArrayList<String> companyNameList = new ArrayList<String>();
		String sql = "";
		switch (version) {
		case 2:
			sql = "SELECT a.companyName FROM companyinfo a WHERE a.ownedJdsName=? ORDER BY a.companyId ASC";
			break;
		case 3:
			sql = "SELECT a.companyName FROM companyinfo a INNER JOIN jdsinfo b INNER JOIN jdjinfo c ON a.ownedJdsName=b.jdsName AND b.ownedJdjName=c.jdjName WHERE c.jdjName=? ORDER BY a.companyId ASC";
			break;
		case 4:
			sql = "SELECT a.companyName FROM companyinfo a INNER JOIN jdsinfo b INNER JOIN jdjinfo c INNER JOIN zhjinfo d ON a.ownedJdsName=b.jdsName AND b.ownedJdjName=c.jdjName AND c.ownedZhjName=d.zhjName WHERE d.zhjName=? ORDER BY a.companyId ASC";
			break;
			//SELECT b.
		default:
			break;
		}
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, departmentName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				companyNameList.add(rs.getString("companyName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return companyNameList;
	}
	
	/**
	 * 根据版本获取军代局下的军代室集合
	 * 
	 * @param departmentName
	 *            部门名称
	 * @param version
	 *            版本
	 * @return
	 */
	public ArrayList<String> getJDSNameList(String departmentName) {
		ArrayList<String> JDSNameList = new ArrayList<String>();
		String sql = "Select b.jdsName From jdsinfo b Where b.ownedJdjName=? ORDER BY b.jdsId ASC";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, departmentName);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				//System.out.println(rs.getString("jdsName"));
				JDSNameList.add(rs.getString("jdsName"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return JDSNameList;
	}
	
	/**
	 * 根据军代室查询代储企业
	 * */
	public ArrayList<String> selectOwnedUnit(String JDS, ArrayList<String> ownedUnitList){
		String sql = "Select companyName FROM companyinfo where ownedJdsName = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, JDS);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("companyName"));
				ownedUnitList.add(rs.getString("companyName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return ownedUnitList;
	}
	
	/**
	 * 根据代储企业查军代局
	 * @param company
	 * @return
	 */
	public String getJdsThroughCompany(String company) {
		String ownedJdsName="";
		String sql = "Select ownedJdsName FROM companyinfo where companyName = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, company);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ownedJdsName=rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return ownedJdsName;
	}
	
	/**
	 * 根据企业名查出对应的军代室
	 * @param companys 
	 * @return key：企业名字；value：军代室名字
	 */
	public Map<String,String> getJdsThroughCompany(List<String> companys){
		String sql = "Select ownedJdsName FROM companyinfo where companyName = ?";
		Map<String,String> namesMap = new HashMap<String,String>();
		String ownedJdsName="";
		try {
//			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			for(String name:companys){
				pstmt.setString(1, name);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					ownedJdsName=rs.getString(1);
					namesMap.put(name, ownedJdsName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt,rs);
		}
		return namesMap;
	}
}