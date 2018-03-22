package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.cqupt.beans.Temperature;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;

public class TemperatureDAO {

	private Connection conn = null;

	/**
	 * 添加temperature记录
	 * 
	 * @param Qy_Temperature
	 * @return boolean 成功true 失败false
	 */
	public boolean addTemperature(Temperature temperature) {
		boolean flag = false;
		String sql = "INSERT INTO qy_temperature (temperature,curRecordDate,position) VALUES (?,?,?)";
		PreparedStatement pstmt = null;
		try {
			// 设置参数
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, temperature.getTemperature());
			pstmt.setString(2, temperature.getCurRecordDate());
			pstmt.setString(3, temperature.getPosition());
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
	 * 修改单个temperature记录
	 * 
	 * @param Qy_Temperature
	 * @return boolean 成功true 失败false
	 */
	public boolean updateTemperature(Temperature temperature) {
		boolean flag = false;
		String sql = "UPDATE qy_temperature SET temperature=?,curRecordDate=?,position=? WHERE temperatureId =?";
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, temperature.getTemperature());
			pstmt.setString(2, temperature.getCurRecordDate());
			pstmt.setString(3, temperature.getPosition());
			pstmt.setLong(4, temperature.getTemperatureId());
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
	 * 根据id获取添加原始一条记录的系统时间
	 * 
	 * @param temperatureId
	 * @return
	 */
	public String getOperateDate(long temperatureId) {
		String operateDate = "";
		String sql = "SELECT qy_temperature.operateDate FROM qy_temperature WHERE qy_temperature.temperatureId=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, temperatureId);
			rs = pstmt.executeQuery();
			if (rs.next())
				operateDate = rs.getString("operateDate");
			if (operateDate.split(";").length == 2)
				operateDate = operateDate.split(";")[0];
			return operateDate;
		} catch (Exception ex) {
			ex.printStackTrace();
			return operateDate;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	/**
	 * 删除单个temperature记录
	 * 
	 * @param temperatureId
	 *            温度id
	 * @return boolean 成功true 失败false
	 */
	public boolean deleteTemperature(Long temperatureId) {
		boolean flag = false;
		String sql = "DELETE FROM qy_temperature WHERE temperatureId =?";
		PreparedStatement pstmt = null;
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, temperatureId);
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
	 * 条件查询符合条件的全部数据
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            截止时间
	 * @param startTemp
	 *            温度下限
	 * @param endTemp
	 *            温度上限
	 * @return 当前页的所有记录
	 */
	public ArrayList<Temperature> getQualifyTemperature(Date startDate,
			Date endDate) {
		// 处理参数，拼接sql语句
		Timestamp sqlStartDate = MyDateFormat.changeToSqlDate(startDate);
		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM qy_temperature");
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			builder.append(" WHERE (qy_temperature.curRecordDate BETWEEN "
					+ "'" + sqlStartDate + "'" + " AND " + "'" + sqlEndDate
					+ "'" + ")");
		}

		builder.append(" ORDER BY qy_temperature.curRecordDate DESC");
		String sql = builder.toString();

		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Temperature> tempatureList = new ArrayList<Temperature>();
		try {
			conn = DBConnection.getConn();
			// 获取获取库链接对象
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			Temperature temperature = null;
			while (res.next()) {
				temperature = new Temperature();
				temperature.setTemperatureId(res.getLong("temperatureId"));
				temperature.setTemperature(res.getDouble("temperature"));
				temperature.setCurRecordDate(res.getString("curRecordDate"));
				temperature.setPosition(res.getString("position"));
				tempatureList.add(temperature);
			}
			return tempatureList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
	}

	/**
	 * 条件查询时当前页的数据
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            截止时间
	 * @param startTemp
	 *            温度下限
	 * @param endTemp
	 *            温度上限
	 * @return 当前页的所有记录
	 */
	/*
	 * public ArrayList<Temperature> getQualifyTemperature(Date startDate, Date
	 * endDate, Double startTemp, Double endTemp,int startIndex, int pageSize) {
	 * Timestamp sqlStartDate = MyDateFormat.changeToSqlDate(startDate);
	 * Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
	 * StringBuilder builder = new StringBuilder();
	 * builder.append("SELECT * FROM qy_temperature WHERE"); if (startDate !=
	 * null && !startDate.equals("") && endDate != null && !endDate.equals(""))
	 * { builder.append(" (qy_temperature.curRecordDate BETWEEN " + "'" +
	 * sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'" + ")"); } if
	 * (startDate != null && !startDate.equals("") && endDate != null &&
	 * !endDate.equals("") && startTemp != null && !startTemp.equals("") &&
	 * !endTemp.equals("") && endTemp != null) builder.append("AND"); if
	 * (startTemp != null && !startTemp.equals("") && endTemp != null &&
	 * !endTemp.equals("")) {
	 * builder.append(" (qy_temperature.temperature BETWEEN " + startTemp +
	 * " AND " + endTemp + ")"); }
	 * builder.append("  ORDER BY qy_temperature.curRecordDate DESC");
	 * builder.append(" LIMIT ?,?"); String sql = builder.toString();
	 * 
	 * PreparedStatement pstmt = null; ResultSet res = null;
	 * ArrayList<Temperature> tempatureList = new ArrayList<Temperature>();
	 * String operateDate=""; try{ conn = DBConnection.getConn(); pstmt =
	 * conn.prepareStatement(sql); pstmt.setInt(1, startIndex); pstmt.setInt(2,
	 * pageSize); res = pstmt.executeQuery(); Temperature temperature = null;
	 * while(res.next()){ temperature = new Temperature();
	 * temperature.setTemperatureId(res.getLong("temperatureId"));
	 * temperature.setTemperature(res.getDouble("temperature"));
	 * temperature.setCurRecordDate(res.getString("curRecordDate"));
	 * operateDate=res.getString("operateDate");
	 * if(operateDate.split(";").length==2){
	 * temperature.setOperateDate(operateDate.split(";")[0]);
	 * temperature.setUpdateDate(operateDate.split(";")[1]); } else{
	 * temperature.setOperateDate(operateDate); temperature.setUpdateDate(""); }
	 * temperature.setPosition(res.getString("position"));
	 * tempatureList.add(temperature); } return tempatureList; }catch(Exception
	 * ex){ ex.printStackTrace(); return null; }finally{
	 * DBConnection.close(conn, pstmt, res); } }
	 */

	/**
	 * 条件查询时当前页的数据
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            截止时间
	 * @return 当前页的所有记录
	 */
	public ArrayList<Temperature> getQualifyTemperature(Date startDate,
			Date endDate, int startIndex, int pageSize) {
		Timestamp sqlStartDate = MyDateFormat.changeToSqlDate(startDate);
		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM qy_temperature WHERE");
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			builder.append(" (qy_temperature.curRecordDate BETWEEN " + "'"
					+ sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'"
					+ ")");
		}

		builder.append("  ORDER BY qy_temperature.curRecordDate DESC");
		builder.append(" LIMIT ?,?");
		String sql = builder.toString();

		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Temperature> tempatureList = new ArrayList<Temperature>();
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startIndex);
			pstmt.setInt(2, pageSize);
			res = pstmt.executeQuery();
			Temperature temperature = null;
			while (res.next()) {
				temperature = new Temperature();
				temperature.setTemperatureId(res.getLong("temperatureId"));
				temperature.setTemperature(res.getDouble("temperature"));
				temperature.setCurRecordDate(res.getString("curRecordDate"));
				temperature.setPosition(res.getString("position"));
				tempatureList.add(temperature);
			}
			return tempatureList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
	}

	/**
	 * 获取条件查询时的数据记录总数
	 * 
	 * @param startDate
	 * @param endDate
	 * @param startTemp
	 * @param endTemp
	 * @return
	 */

	public int getTotalQualifyRecords(Date startDate, Date endDate,
			Double startTemp, Double endTemp) { // 处理参数，拼接sql语句 
		Timestamp sqlStartDate =MyDateFormat.changeToSqlDate(startDate);
		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(temperatureId) FROM qy_temperature");
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			builder.append(" WHERE qy_temperature.curRecordDate BETWEEN " + "'"
					+ sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'"
					+ "");
		}
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("") && startTemp != null
				&& !startTemp.equals("") && !endTemp.equals("")
				&& endTemp != null)
			builder.append("AND");
		if (startTemp != null && !startTemp.equals("") && endTemp != null
				&& !endTemp.equals("")) {
			if (startDate != null && !startDate.equals("") && endDate != null
					&& !endDate.equals("")){
				builder.append(" qy_temperature.temperature BETWEEN " + startTemp
						+ " AND " + endTemp + "");
			}
			else{
				builder.append(" WHERE qy_temperature.temperature BETWEEN " + startTemp
						+ " AND " + endTemp + "");
			}
		}
		String sql = builder.toString();
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try { // 设置参数
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			if (res.next()) {
				count = res.getInt(1);
			}
			return count;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

//	/**
//	 * 获取条件查询时的数据记录总数
//	 * 
//	 * @param startDate
//	 * @param endDate
//	 * @return
//	 */
//	public int getTotalQualifyRecords(Date startDate, Date endDate) {
//		// 处理参数，拼接sql语句
//		Timestamp sqlStartDate = MyDateFormat.changeToSqlDate(startDate);
//		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
//		StringBuilder builder = new StringBuilder();
//		builder.append("SELECT COUNT(temperatureId) FROM qy_temperature WHERE");
//		if (startDate != null && !startDate.equals("") && endDate != null
//				&& !endDate.equals("")) {
//			builder.append(" (qy_temperature.curRecordDate BETWEEN " + "'"
//					+ sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'"
//					+ ")");
//		}
//
//		String sql = builder.toString();
//		int count = 0;
//		PreparedStatement pstmt = null;
//		ResultSet res = null;
//		try {
//			// 设置参数
//			conn = DBConnection.getConn();
//			pstmt = conn.prepareStatement(sql);
//			res = pstmt.executeQuery();
//			if (res.next()) {
//				count = res.getInt(1);
//			}
//			return count;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return 0;
//		} finally {
//			DBConnection.close(conn, pstmt);
//		}
//	}

	/**
	 * 获得全部查询时数据库中所有记录条数
	 * 
	 * @return
	 */
	public int getTotalRecords() {
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String sql = "SELECT COUNT(temperatureId) FROM qy_temperature";
		try {
			// 设置参数
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			if (res.next()) {
				count = res.getInt(1);
			}
			return count;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	/**
	 * 得到Temperature中的全部记录
	 * 
	 * @return Map
	 */
	public ArrayList<Temperature> getAllTemperature() {
		String sql = "SELECT * FROM qy_temperature";
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Temperature> temperatureList = new ArrayList<Temperature>();
		try {
			conn = DBConnection.getConn();
			// 获取获取库链接对象
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			Temperature temperature = null;
			while (res.next()) {
				temperature = new Temperature();
				temperature.setTemperatureId(res.getLong("temperatureId"));
				temperature.setTemperature(res.getDouble("temperature"));
				temperature.setCurRecordDate(res.getString("curRecordDate"));
				temperature.setPosition(res.getString("position"));
				temperatureList.add(temperature);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
		return temperatureList;
	}

	/**
	 * 根据记录temperatureId查到单个记录
	 * 
	 * @param temperatureId
	 * @return Temperature
	 */
	public Temperature getOneTemperatureById(Long temperatureId) {
		String sql = "SELECT * FROM qy_temperature WHERE temperatureId="
				+ temperatureId;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		Temperature temperature = null;
		try {
			// 获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			if (res.next()) {
				temperature = new Temperature();
				temperature.setTemperatureId(res.getLong("temperatureId"));
				temperature.setTemperature(res.getDouble("temperature"));
				temperature.setCurRecordDate(res.getString("curRecordDate"));
				temperature.setPosition(res.getString("position"));
			}
			return temperature;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
	}

	/**
	 * 得到全部查询时当前页中的记录
	 * 
	 * @param startIndex
	 *            第“几”页
	 * @param pageSize
	 *            每页放的记录条数
	 * @return
	 */
	public ArrayList<Temperature> findPageRecords(int startIndex, int pageSize) {
		String sql = "SELECT * FROM qy_temperature ORDER BY qy_temperature.curRecordDate DESC  LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Temperature> temperatureList = new ArrayList<Temperature>();
		try {
			conn = DBConnection.getConn();
			// 获取获取库链接对象
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startIndex);
			pstmt.setInt(2, pageSize);
			res = pstmt.executeQuery();
			Temperature temperature = null;
			while (res.next()) {
				temperature = new Temperature();
				temperature.setTemperatureId(res.getLong("temperatureId"));
				temperature.setTemperature(res.getDouble("temperature"));
				temperature.setCurRecordDate(res.getString("curRecordDate"));
				temperature.setPosition(res.getString("position"));
				temperatureList.add(temperature);
			}
			return temperatureList;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
	}

//	/**
//	 * 获取数据库最后一条温度记录
//	 * 
//	 * @return
//	 */
//	public boolean repeatTemperature(double temperature, String operateDate,
//			String position) {
//		boolean repeatFlag = false;
//		PreparedStatement pstmt = null;
//		ResultSet res = null;
//		String sql = "SELECT * FROM qy_temperature WHERE qy_temperature.temperature=? AND qy_temperature.operateDate=? AND qy_temperature.position=?";
//		try {
//			// 获取获取库链接对象
//			conn = DBConnection.getConn();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setDouble(1, temperature);
//			pstmt.setString(2, operateDate);
//			pstmt.setString(3, position);
//			res = pstmt.executeQuery();
//			if (res.next()) {
//				repeatFlag = true;
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		} finally {
//			DBConnection.close(conn, pstmt, res);
//		}
//		return repeatFlag;
//	}

	/**
	 * 在更新时将原记录插入qy_old_temperature
	 * 
	 * @param temperatureId
	 *            原一条温度记录id
	 */
	public boolean insertOldRecord(Long temperatureId) {
		boolean flag = false;
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO qy_old_temperature (temperatureId,temperature,curRecordDate,position) SELECT a.temperatureId,a.temperature,a.curRecordDate,a.position FROM qy_temperature a WHERE a.temperatureId=?";
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, temperatureId);
			if (pstmt.executeUpdate() >= 1) {
				flag = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 根据记录id获取原始记录
	 * 
	 * @param recordId
	 * @param recordType
	 * @return
	 */
	public ArrayList<Object> searchOldRecord(Long recordId, String recordType) {
		ArrayList<Object> oldRecordList = new ArrayList<Object>();
		String searchTable = "";
		String tableId = "";
		String sql = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		if (recordType.equals("temperature")) {
			searchTable = "qy_old_temperature a ";
			tableId = "temperatureId";
		} else if (recordType.equals("humidity")) {
			searchTable = "qy_old_humidity a ";
			tableId = "humidityId";
		}

		sql = "SELECT a." + recordType + ",a.curRecordDate,a.position FROM "
				+ searchTable + " WHERE a." + tableId + "=?";
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, recordId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				oldRecordList.add(rs.getDouble(recordType));
				oldRecordList.add(rs.getString("curRecordDate"));
				oldRecordList.add(rs.getString("position"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return oldRecordList;
	}
}
