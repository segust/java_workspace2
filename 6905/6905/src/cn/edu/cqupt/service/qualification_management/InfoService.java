package cn.edu.cqupt.service.qualification_management;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.InfoDAO;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.StringUtil;

public class InfoService {
	private Connection conn = null;
	private InfoDAO infoDAO = null;
	private boolean flag;

	public InfoService() {
		super();

	}

	public ArrayList<String> searchRootLevelContent(String rootLevel) {
		ArrayList<String> allRootLevelContentList = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			infoDAO = new InfoDAO(conn);
			allRootLevelContentList = infoDAO.searchRootLevelContent(rootLevel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allRootLevelContentList;
	}

	public ArrayList<String> searchNextLevelContent(String nextLevel,
			String curLevelName) {
		ArrayList<String> allNameList = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			infoDAO = new InfoDAO(conn);
			allNameList = infoDAO.searchNextLevelContent(nextLevel,
					curLevelName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allNameList;
	}

	public boolean ifExistNextLevelContent(String nextLevel, String curLevelName) {
		boolean existFlag = false;
		try {
			conn = DBConnection.getConn();
			infoDAO = new InfoDAO(conn);
			existFlag = infoDAO
					.ifExistNextLevelContent(nextLevel, curLevelName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existFlag;
	}

	public ArrayList<String> searchCurLevelInfo(String curLevel,
			String curLevelName) {
		ArrayList<String> curLevelInfoList = new ArrayList<String>();

		try {
			conn = DBConnection.getConn();
			infoDAO = new InfoDAO(conn);
			curLevelInfoList = infoDAO.searchCurLevelInfo(curLevel,
					curLevelName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return curLevelInfoList;
	}

	public boolean deleteInfo(String curLevel, String curLevelName) {
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			flag = infoDAO.deleteInfo(curLevel, curLevelName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean addInfo(String infoName, String ownedName,
			String infoManager, String leader, String curLevel) {
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			flag = infoDAO.addInfo(infoName, ownedName, infoManager, leader,
					curLevel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean updateInfo(long zhjId, String zhjName, String zhjManager,
			String leader) {

		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			flag = infoDAO.updateInfo(zhjId, zhjName, zhjManager, leader);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean updateInfo(long infoId, String infoName, String ownedName,
			String infoManager, String leader, String curLevel) {
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			flag = infoDAO.updateInfo(infoId, infoName, ownedName, infoManager,
					leader, curLevel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public long searchIdByName(String curLevel, String curLevelName) {
		long curId = 0;
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			curId = infoDAO.searchIdByName(curLevel, curLevelName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return curId;
	}

	public ArrayList<String> getCompanyNameList(String departmentName,
			int version) {
		ArrayList<String> companyNameList = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			companyNameList = infoDAO.getCompanyNameList(departmentName,
					version);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companyNameList;
	}

	/**
	 * 根据军代局查询军代室
	 * */
	public ArrayList<String> getJDSNameList(String departmentName) {
		ArrayList<String> JDSNameList = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			JDSNameList = infoDAO.getJDSNameList(departmentName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return JDSNameList;
	}

	/**
	 * 根据军代室查询代储企业
	 * */
	public ArrayList<String> getOwnedUnit(String JDS,
			ArrayList<String> ownedUnitList) {
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			ownedUnitList = infoDAO.selectOwnedUnit(JDS, ownedUnitList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ownedUnitList;
	}

	/**
	 * 返回ownedUnit
	 * */
	public HashMap<String, String> getOwnedUnitSQL(
			HashMap<String, String> condition, String version) {
		ArrayList<String> ownedUnitList = new ArrayList<String>();
		String sql = new String();
		if (StringUtil.isNotEmpty(condition.get("JDS")) && version.equals("3")) {
			ownedUnitList = getOwnedUnit(condition.get("JDS"), ownedUnitList);
		}
		if (StringUtil.isNotEmpty(condition.get("JDJ")) && version.equals("4")) {
			ArrayList<String> jdsList = getJDSNameList(condition.get("JDJ"));
			for (int i = 0; i < jdsList.size(); i++) {
				ownedUnitList = getOwnedUnit(jdsList.get(i), ownedUnitList);
			}
		}
		sql += " IN (";
		for (int i = 0; i < ownedUnitList.size(); i++) {
			sql += "'" + ownedUnitList.get(i) + "'";
			if (i != ownedUnitList.size() - 1)
				sql += ",";
		}
		sql += ")";
		condition.put("ownedUnitList", sql);
		return condition;
	}

	/**
	 * 根据企业获取军代室名字
	 * 
	 * @param companyName
	 * @return
	 */
	public String getJdsThroughCompany(String companyName) {
		String ownedJdsName = "";
		try {
			conn = DBConnection.getConn();
			InfoDAO infoDAO = new InfoDAO(conn);
			ownedJdsName = infoDAO.getJdsThroughCompany(companyName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ownedJdsName;
	}

	/**
	 * 根据企业获取军代室名字
	 * 
	 * @param companyNames
	 * @return key：企业名字；value：军代室名字
	 * @author liangyihuai
	 */
	public Map<String,String> getJdsThroughCompany(List<String> companyNames) {
		try {
			conn = DBConnection.getConn();
			infoDAO = new InfoDAO(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return infoDAO.getJdsThroughCompany(companyNames);
	}
	
	public static void main(String[] args) {
		// ArrayList<String> companyNameList = new InfoService()
		// .getCompanyNameList("军代室1", 2);
		// for (String eachCompanyName : companyNameList) {
		// System.out.println(eachCompanyName);
		// }
		// System.out.println(new InfoService().getJdsThroughCompany("代储企业1"));
	}

}