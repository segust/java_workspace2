package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.ResultSetMetaData;

import cn.edu.cqupt.beans.Humidity;
import cn.edu.cqupt.beans.Unit;
import cn.edu.cqupt.db.DBConnection;
public class UnitDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public UnitDAO() {
		
	}
	/**
	 * 添加Unit记录
	 * @param Unit
	 * @return boolean 成功true 失败false
	 */
	public boolean addUnit(List<String> pmnmlist) {
		boolean flag = false;
		String sql = "Update set u.flag='1' from qy_unit u Where u.PMNM=?" ;
		PreparedStatement pstmt = null;
		try {
			// 设置参数
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			for(int i =0;i<pmnmlist.size();i++){
				pstmt.setString(1,pmnmlist.get(i));
				}
			
			// 执行sql
			int count = pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
			return flag;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	

	/**
	 * 删除单个Unit记录
	 * 
	 * @param humidityId
	 * @return boolean 成功true 失败false
	 */
	public boolean deleteUnit(int UnitId) {
		boolean flag = false;
		String sql = "DELETE FROM qy_unit WHERE id=?";
		PreparedStatement pstmt = null;
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, UnitId);
			int count = pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
			return flag;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}
	
	/**
	 * 得到Unit中已添加的全部记录
	 * @return Map
	 */
	public ArrayList<HashMap<String,String>> getAlladdedUnit(String PMNM) {
		String sql = "SELECT * FROM qy_unit WHERE qy_unit.flag=1";
		PreparedStatement pstmt = null;
		ResultSet res = null;
		HashMap<String,String> map = new HashMap<String,String>();
		ArrayList<HashMap<String,String>> unitlist = new ArrayList<HashMap<String,String>>();
		try {
			conn = DBConnection.getConn();
			// 获取获取库链接对象
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, PMNM);
			res = pstmt.executeQuery();
			while (res.next()) {
				ResultSetMetaData metaData =(ResultSetMetaData) res.getMetaData();
				int len2 = metaData.getColumnCount();
				for(int k = 1; k <= len2; k++){
					map.put(metaData.getColumnName(k),res.getString(k));
				}
				unitlist.add(map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
		return unitlist;
	}
	/**
	 * 得到Unit中未添加的全部记录
	 * @return Map
	 */
	public ArrayList<HashMap<String,String>> getNotaddedUnit(String PMNM) {
		String sql = "SELECT * FROM qy_unit WHERE qy_unit.flag=0 AND FKPMNM=?";
		PreparedStatement pstmt = null;
		ResultSet res = null;
		HashMap<String,String> map = new HashMap<String,String>();
		ArrayList<HashMap<String,String>> unitlist = new ArrayList<HashMap<String,String>>();
		try {
			conn = DBConnection.getConn();
			// 获取获取库链接对象
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, PMNM);
			res = pstmt.executeQuery();
			while (res.next()) {
				ResultSetMetaData metaData =(ResultSetMetaData) res.getMetaData();
				int len2 = metaData.getColumnCount();
				for(int k = 1; k <= len2; k++){
					map.put(metaData.getColumnName(k),res.getString(k));
				}
				unitlist.add(map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
		return unitlist;
	}
	public Unit findUnitByName(String unitName) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
