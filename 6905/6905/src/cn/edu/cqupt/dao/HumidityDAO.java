package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.cqupt.beans.Humidity;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;

public class HumidityDAO {

	private Connection conn = null;

	/**
	 * 添加Humidity记录
	 * @param Humidity
	 * @return boolean 成功true 失败false
	 */
	public boolean addHumidity(Humidity humidity) {
		boolean flag = false;
		String sql = "INSERT INTO qy_humidity (humidity,curRecordDate,position) VALUES (?,?,?)";
		PreparedStatement pstmt = null;
		try {
			// 设置参数
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, humidity.getHumidity());
			pstmt.setString(2, humidity.getCurRecordDate());
			pstmt.setString(3, humidity.getPosition());
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
	 * 修改单个humidity记录
	 * 
	 * @param Humidity
	 * @return boolean 成功true 失败false
	 */
	public boolean updateHumidity(Humidity humidity) {
		boolean flag = false;
		String sql = "UPDATE qy_humidity SET humidity=?,curRecordDate=?,position=? WHERE humidityId =?";
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, humidity.getHumidity());
			pstmt.setString(2, humidity.getCurRecordDate());
			pstmt.setString(3, humidity.getPosition());
			pstmt.setLong(4, humidity.getHumidityId());
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
	 * @param humidityId
	 * @return
	 */
	public String getOperateDate(long humidityId) {
		String operateDate="";
		String sql = "SELECT qy_humidity.operateDate FROM qy_humidity WHERE qy_humidity.humidityId = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, humidityId);
			rs=pstmt.executeQuery();
			if(rs.next())
				operateDate=rs.getString("operateDate");
			if(operateDate.split(";").length==2)
				operateDate=operateDate.split(";")[0];
			return operateDate;
		} catch (Exception ex) {
			ex.printStackTrace();
			return operateDate;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	/**
	 * 删除单个humidity记录
	 * 
	 * @param humidityId
	 * @return boolean 成功true 失败false
	 */
	public boolean deleteHumidity(Long humidityId) {
		boolean flag = false;
		String sql = "DELETE FROM qy_humidity WHERE humidityId =?";
		PreparedStatement pstmt = null;
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, humidityId);
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
	 * 条件查询全部的湿度数据
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            截止时间
	 * @param startHumidity
	 *            湿度下限
	 * @param endHumidity
	 *            湿度上限
	 * @return 当前页的所有记录
	 */
	public ArrayList<Humidity> getQualifyHumidity(Date startDate,
			Date endDate) {
		// 处理参数，拼接sql语句
		Timestamp sqlStartDate = MyDateFormat.changeToSqlDate(startDate);
		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM qy_humidity");
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			builder.append(" WHERE (qy_humidity.curRecordDate BETWEEN " + "'"
					+ sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'"
					+ ")");
		}
		
		builder.append(" ORDER BY qy_humidity.curRecordDate DESC");
		String sql = builder.toString();

		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Humidity>  humidityList = new ArrayList<Humidity>();
		try{
			conn = DBConnection.getConn();
			//获取库链接对象
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			Humidity humidity = null;
			while(res.next()){
				humidity = new Humidity();
				humidity.setHumidityId(res.getLong("humidityId"));
				humidity.setHumidity(res.getDouble("humidity"));
				humidity.setCurRecordDate(res.getString("curRecordDate"));
				humidity.setPosition(res.getString("position"));
				humidityList.add(humidity);
			}
			return humidityList;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			DBConnection.close(conn, pstmt, res);
		}
	}

	/**
	 * 条件查询当前页的湿度数据
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            截止时间
	 * @return 当前页的所有记录
	 */
	public ArrayList<Humidity> getQualifyHumidity(Date startDate,
			Date endDate, int startIndex, int pageSize) {
		// 处理参数，拼接sql语句
		Timestamp sqlStartDate = MyDateFormat.changeToSqlDate(startDate);
		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT * FROM qy_humidity WHERE");
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			builder.append(" (qy_humidity.curRecordDate BETWEEN " + "'"
					+ sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'"
					+ ")");
		}
		
		builder.append(" ORDER BY qy_humidity.curRecordDate DESC");
		builder.append(" LIMIT ?,?");
		String sql = builder.toString();

		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Humidity>  humidityList = new ArrayList<Humidity>();
		try{
			conn = DBConnection.getConn();
			//获取库链接对象
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startIndex);
			pstmt.setInt(2, pageSize);
			res = pstmt.executeQuery();
			Humidity humidity = null;
			while(res.next()){
				humidity = new Humidity();
				humidity.setHumidityId(res.getLong("humidityId"));
				humidity.setHumidity(res.getDouble("humidity"));
				humidity.setCurRecordDate(res.getString("curRecordDate"));
				humidity.setPosition(res.getString("position"));
				humidityList.add(humidity);
			}
			return humidityList;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			DBConnection.close(conn, pstmt, res);
		}
	}
	
	/**
	 * 获取条件查询时的数据总数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public int getTotalQualifyRecords(Date startDate,
			Date endDate) {
		// 处理参数，拼接sql语句
		Timestamp sqlStartDate = MyDateFormat.changeToSqlDate(startDate);
		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(humidityId) FROM qy_humidity WHERE");
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			builder.append(" (qy_humidity.curRecordDate BETWEEN " + "'"
					+ sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'"
					+ ")");
		}
		
		String sql = builder.toString();
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet res = null;
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
	 * 获得数据库中所有记录条数
	 * @return
	 */
	public int getTotalRecords() {
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String sql = "SELECT COUNT(humidityId) FROM qy_humidity";
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
	 * 获取条件查询时的数据记录总数
	 * 
	 * @param startDate
	 * @param endDate
	 * @param startHumidity
	 * @param endHumidity
	 * @return
	 */
	public int getTotalQualifyRecords(Date startDate, Date endDate,
			Double startHumidity, Double endHumidity) { // 处理参数，拼接sql语句 
		Timestamp sqlStartDate =MyDateFormat.changeToSqlDate(startDate);
		Timestamp sqlEndDate = MyDateFormat.changeToSqlDate(endDate);
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(humidityId) FROM qy_humidity");
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			builder.append(" WHERE qy_humidity.curRecordDate BETWEEN " + "'"
					+ sqlStartDate + "'" + " AND " + "'" + sqlEndDate + "'"
					+ "");
		}
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("") && startHumidity != null
				&& !startHumidity.equals("") && !endHumidity.equals("")
				&& endHumidity != null)
			builder.append("AND");
		if (startHumidity != null && !startHumidity.equals("") && endHumidity != null
				&& !endHumidity.equals("")) {
			if (startDate != null && !startDate.equals("") && endDate != null
					&& !endDate.equals("")){
				builder.append(" qy_humidity.humidity BETWEEN " + startHumidity
						+ " AND " + endHumidity + "");
			}
			else{
				builder.append(" WHERE qy_humidity.humidity BETWEEN " + startHumidity
						+ " AND " + endHumidity + "");
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

	/**
	 * 得到humidity中的全部记录
	 * @return Map
	 */
	public ArrayList<Humidity> getAllHumidity() {
		String sql = "SELECT * FROM qy_humidity";
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Humidity> humidityList = new ArrayList<Humidity>();
		try {
			conn = DBConnection.getConn();
			// 获取获取库链接对象
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			Humidity humidity = null;
			while (res.next()) {
				humidity = new Humidity();
				humidity.setHumidityId(res.getLong("humidityId"));
				humidity.setHumidity(res.getDouble("humidity"));
				humidity.setCurRecordDate(res.getString("curRecordDate"));
				humidity.setPosition(res.getString("position"));
				humidityList.add(humidity);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
		return humidityList;
	}

	/**
	 * 根据记录humidityId查到单个记录
	 * @param humidityId
	 * @return Humidity
	 */
	public Humidity getOneHumidityById(Long humidityId) {
		String sql = "SELECT * FROM qy_humidity WHERE humidityId="
				+ humidityId;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		Humidity humidity = null;
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
			if (res.next()) {
				humidity = new Humidity();
				humidity.setHumidityId(res.getLong("humidityId"));
				humidity.setHumidity(res.getDouble("humidity"));
				humidity.setCurRecordDate(res.getString("curRecordDate"));
				humidity.setPosition(res.getString("position"));
			}
			return humidity;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
	}
	/**
	 * 得到全部查询时当前页中的记录
	 * @param startIndex 第“几”页
	 * @param pageSize 每页放的记录条数
	 * @return Map
	 */
	public ArrayList<Humidity> findPageRecords(int startIndex, int pageSize) {
		String sql = "SELECT * FROM qy_humidity ORDER BY qy_humidity.curRecordDate DESC LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet res = null;
		ArrayList<Humidity>  humidityList = new ArrayList<Humidity> ();
		try{
			conn = DBConnection.getConn();
			//获取获取库链接对象
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startIndex);
			pstmt.setInt(2, pageSize);
			res = pstmt.executeQuery();
			Humidity humidity = null;
			while(res.next()){
				humidity = new Humidity();
				humidity.setHumidityId(res.getLong("humidityId"));
				humidity.setHumidity(res.getDouble("humidity"));
				humidity.setCurRecordDate(res.getString("curRecordDate"));
				humidity.setPosition(res.getString("position"));
				humidityList.add(humidity);
			}
			return humidityList;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}finally{
			DBConnection.close(conn, pstmt, res);
		}
	}
	
	/**
	 * 判断是否存在重复湿度
	 * @param humidity
	 * @param operateDate
	 * @param position
	 * @return
	 */
	public boolean repeatHumidity(double humidity,String operateDate,String position){
		boolean repeatFlag=false;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String sql="SELECT * FROM qy_humidity WHERE	 qy_humidity.humidity=? AND qy_humidity.operateDate=? AND qy_humidity.position=?";
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, humidity);
			pstmt.setString(2, operateDate);
			pstmt.setString(3, position);
			res = pstmt.executeQuery();
			if (res.next()) {
				repeatFlag=true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, res);
		}
		return repeatFlag;
	}
	
	/**
	 * 在更新时将原记录插入qy_old_humidity
	 * @param humidityId 原一条湿度记录id
	 */
	public boolean insertOldRecord(Long humidityId){
		boolean flag=false;
		PreparedStatement pstmt = null;
		String sql="INSERT INTO qy_old_humidity (humidityId,humidity,curRecordDate,position) SELECT a.humidityId,a.humidity,a.curRecordDate,a.position FROM qy_humidity a WHERE a.humidityId=?";
		try {
			// 获取获取库链接对象
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, humidityId);
			if (pstmt.executeUpdate()>=1) {
				flag=true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
}
