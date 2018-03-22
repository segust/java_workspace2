package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import cn.edu.cqupt.beans.Fare;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;

/**
 * 经费管理
 * 
 * @author lsy&yg&lhs
 * 
 */
public class FareDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;

	public FareDAO() {

	}

	/**
	 * addFare 添加一条经费记录
	 * 
	 * @param fare
	 * @return
	 */
	public boolean addFare(Fare fare) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_fare(fareType,fareAmount,storeCompany,jdRoom,"
					+ "operateDate,remark) VALUES(?,?,?,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, fare.getFareType());
			this.pstmt.setDouble(2, fare.getFareAmount());
			this.pstmt.setString(3, fare.getStoreCompany());
			this.pstmt.setString(4, fare.getJdRoom());
			this.pstmt.setString(5, fare.getOperateDate());
			this.pstmt.setString(6, fare.getRemark());
			int count = this.pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * deleteFare 删除一条经费记录
	 * 
	 * @param fareID
	 * @return
	 */
	public boolean deleteFare(long fareId) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sqlDelete = "DELETE FROM qy_fare WHERE fareId=?";
			this.pstmt = this.conn.prepareStatement(sqlDelete);
			this.pstmt.setLong(1, fareId);
			int countDelete = this.pstmt.executeUpdate();
			if (countDelete > 0 ) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * updateFare 更新一条经费记录
	 * 
	 * @param
	 * @return
	 */
	public boolean updateFare(Fare fare) {
		boolean flag = false;
		try {
			this.conn = DBConnection.getConn();
			String sql = "UPDATE qy_fare SET fareType=?,fareAmount=?,storeCompany=?,jdRoom=?,"
					+ "operateDate=?,remark=?  WHERE fareId=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, fare.getFareType());
			this.pstmt.setDouble(2, fare.getFareAmount());
			this.pstmt.setString(3, fare.getStoreCompany());
			this.pstmt.setString(4, fare.getJdRoom());
			this.pstmt.setString(5,fare.getOperateDate());
			this.pstmt.setString(6, fare.getRemark());
			this.pstmt.setLong(7, fare.getFareId());
			int count = this.pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * getOneFareById 通过Id获取一条经费记录
	 * 
	 * @param fareId
	 * @return fare
	 */
	public Fare getOneFareById(int fareId) {
		Fare fare = new Fare();
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_fare WHERE fareId=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setInt(1, fareId);
			rs = this.pstmt.executeQuery();
			if (rs.next()) {
				fare.setFareType(rs.getString("fareType"));
				fare.setFareAmount(rs.getDouble("fareAmount"));
				fare.setStoreCompany(rs.getString("storeCompany"));
				fare.setJdRoom(rs.getString("jdRoom"));
				fare.setOperateDate(rs.getString("operateDate"));
				fare.setRemark(rs.getString("remark"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return fare;
	}

	/**
	 * getAllFare 获取全部经费记录 
	 * @return allFare
	 */
	public ArrayList<Fare> getAllFare(int curPageNum, int pageSize) {
		ArrayList<Fare> allFare = new ArrayList<Fare>();
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_fare  ORDER BY operateDate DESC LIMIT ?,? ";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setInt(1, (curPageNum - 1) * pageSize);
			this.pstmt.setInt(2, pageSize);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Fare fare = new Fare();
				fare.setFareId(rs.getInt("fareID"));
				fare.setFareType(rs.getString("fareType"));
				fare.setFareAmount(rs.getDouble("fareAmount"));
				fare.setStoreCompany(rs.getString("storeCompany"));
				fare.setJdRoom(rs.getString("jdRoom"));
				fare.setOperateDate(rs.getString("operateDate"));
				fare.setRemark(rs.getString("remark"));
				allFare.add(fare);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return allFare;
	}
	/**
	 *  
	 * 获取所有的经费记录的顺序 
	 */
	public ArrayList<Integer> getAllOrder( ) {
		ArrayList<Integer> allOrder = new ArrayList<Integer>();
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT fareId FROM qy_fare ORDER BY operateDate DESC";
			this.pstmt = this.conn.prepareStatement(sql); 
			rs = this.pstmt.executeQuery();
			while (rs.next()) { 
				allOrder.add(rs.getInt("fareID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return allOrder;
	}

	/**
	 * getAllFareByDateAndFaretype 根据时间段和费用类型获取经费记录
	 * 
	 * @return
	 */
	public ArrayList<Fare> getAllFareByDateAndFareType(String startDate,
			String endDate, String type, String storeCompany, int curPageNum,
			int pageSize) {
		ArrayList<Fare> dateAndFareType = new ArrayList<Fare>();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT * FROM qy_fare ");
		boolean testType = false;
		boolean testDate = false;
		if (type != null && !type.equals("")) {
			stringBuilder.append(" WHERE fareType IN (" + type + ")");
			testType = true;
		}
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			testDate = true;
			Timestamp startSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
					.changeStringToDate(startDate.trim()));
			Timestamp endSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
					.changeStringToDate(endDate.trim()));
			if (testType) {// 有type则不加where,没type再加
				stringBuilder.append(" AND operateDate BETWEEN '"
						+ startSqlDate//
						+ "' AND '" + endSqlDate + "'");
			} else {
				stringBuilder.append(" WHERE operateDate BETWEEN '"
						+ startSqlDate//
						+ "' AND '" + endSqlDate + "'");
			}
		}
		if (storeCompany != null && !storeCompany.equals("")) {
			if (testType || testDate) {
				stringBuilder.append(" AND storeCompany REGEXP  '.*"
						+ storeCompany + ".*' ");
			} else {
				stringBuilder.append(" WHERE storeCompany REGEXP  '.*"
						+ storeCompany + ".*' ");
			}
		}
		stringBuilder.append(" ORDER BY operateDate DESC");
		stringBuilder.append(" LIMIT " + (curPageNum - 1) * pageSize + ", "
				+ pageSize);
		String sql = stringBuilder.toString(); 
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Fare fare = new Fare();
				fare.setFareId(rs.getInt("fareID"));
				fare.setFareType(rs.getString("fareType"));
				fare.setFareAmount(rs.getDouble("fareAmount"));
				fare.setStoreCompany(rs.getString("storeCompany"));
				fare.setJdRoom(rs.getString("jdRoom"));
				fare.setOperateDate(rs.getString("operateDate"));
				fare.setRemark(rs.getString("remark"));
				dateAndFareType.add(fare);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return dateAndFareType;
	}
	/**
	 * getAllFareByDateAndFaretype 根据时间段和费用类型获取经费记录  没有页码和每页大小
	 *  同一个方法传的参数不一样而已    这里是用于统计经费明细的，所以只需要他的fareId即可
	 * @return
	 */
	public ArrayList<Long> getAllFareByDateAndFareType(String startDate,
			String endDate, String type, String storeCompany) {
		ArrayList<Long> dateAndFareType = new ArrayList<Long>();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT fareId FROM qy_fare ");
		boolean testType = false;
		boolean testDate = false;
		if (type != null && !type.equals("")) {
			stringBuilder.append(" WHERE fareType IN (" + type + ")");
			testType = true;
		}
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			testDate = true;
			Timestamp startSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
					.changeStringToDate(startDate.trim()));
			Timestamp endSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
					.changeStringToDate(endDate.trim()));
			if (testType) {// 有type则不加where,没type再加
				stringBuilder.append(" AND operateDate BETWEEN '"
						+ startSqlDate//
						+ "' AND '" + endSqlDate + "'");
			} else {
				stringBuilder.append(" WHERE operateDate BETWEEN '"
						+ startSqlDate//
						+ "' AND '" + endSqlDate + "'");
			}
		}
		if (storeCompany != null && !storeCompany.equals("")) {
			if (testType || testDate) {
				stringBuilder.append(" AND storeCompany REGEXP  '.*"
						+ storeCompany + ".*' ");
			} else {
				stringBuilder.append(" WHERE storeCompany REGEXP  '.*"
						+ storeCompany + ".*' ");
			}
		} 
		String sql = stringBuilder.toString();  
		//System.out.println("sql="+sql);
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			while (rs.next()) { 
				dateAndFareType.add(rs.getLong("fareID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return dateAndFareType;
	} 
	/**
	 * 计算经费总条数
	 * 
	 * @return
	 */
	public long getFareSum() {
		String sql = "SELECT COUNT(*) FROM qy_fare";
		ResultSet rs = null;
		int sum = 0;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				sum = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return sum;
	}

	/**
	 * 条件查询显示的经费数
	 * 
	 * @return
	 */
	public long getCheckFareSum(String type, String startDate, String endDate,
			String storeCompany) {
		long checkFareSum = 0;
		// String sql = null;
		// if (type != null && !type.equals("")) {
		// sql = "SELECT COUNT(*) FROM qy_fare WHERE fareType IN(" + type
		// + ")";
		// if (startTime != null && !startTime.equals("") && endTime != null
		// && !endTime.equals("")) {
		// Timestamp startSqlDate = MyDateFormat
		// .changeToSqlDate(MyDateFormat
		// .changeLongStringToDate(startTime));
		// Timestamp endSqlDate = MyDateFormat
		// .changeToSqlDate(MyDateFormat
		// .changeLongStringToDate(endTime));
		// sql = "SELECT COUNT(*) FROM qy_fare WHERE fareType IN(" + type
		// + ") AND operateDate BETWEEN '" + startSqlDate
		// + "' AND '" + endSqlDate + "'";
		// }
		// } else if (startTime != null && !startTime.equals("")
		// && endTime != null && !endTime.equals("")) {
		// Timestamp startSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
		// .changeLongStringToDate(startTime));
		// Timestamp endSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
		// .changeLongStringToDate(endTime));
		// sql = "SELECT COUNT(*) FROM qy_fare WHERE operateDate BETWEEN '"
		// + startSqlDate + "' AND '" + endSqlDate + "'";
		// }
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT COUNT(*) FROM qy_fare  ");
		boolean testType = false;
		boolean testDate = false;
		if (type != null && !type.equals("")) {
			stringBuilder.append("WHERE fareType IN (" + type + ")");
			testType = true;
		}
		if (startDate != null && !startDate.equals("") && endDate != null
				&& !endDate.equals("")) {
			testDate = true;
			Timestamp startSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
					.changeStringToDate(startDate.trim()));
			Timestamp endSqlDate = MyDateFormat.changeToSqlDate(MyDateFormat
					.changeStringToDate(endDate.trim()));
			if (testType) {// 有type则不加where,没type再加
				stringBuilder.append("AND operateDate BETWEEN '" + startSqlDate//
						+ "' AND '" + endSqlDate + "'");
			} else {
				stringBuilder.append("WHERE operateDate BETWEEN '"
						+ startSqlDate//
						+ "' AND '" + endSqlDate + "'");
			}
		}
		if (storeCompany != null && !storeCompany.equals("")) {
			if (testType || testDate) {
				stringBuilder.append("AND storeCompany REGEXP  '.*"
						+ storeCompany + ".*' ");
			} else {
				stringBuilder.append("WHERE storeCompany REGEXP  '.*"
						+ storeCompany + ".*' ");
			}
		}
		String sql = stringBuilder.toString();
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				checkFareSum = rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return checkFareSum;
	}

	/**
	 * 
	 * 通过几个Id联合查找经费记录(用于导出经费)
	 * 
	 * @param idArray
	 * @return
	 */
	public ArrayList<Fare> getSomeFareById(ArrayList<Long> idArray) {
		ResultSet rs = null;
		ArrayList<Fare> someFareArray = new ArrayList<Fare>();
		String id = "";
		String ids = "";
		for (int i = 0; i < idArray.size(); i++) {
			id = String.valueOf(idArray.get(i));
			StringBuilder sb = new StringBuilder();// 拼接字符串
			sb.append(id);
			if (i != idArray.size() - 1) {
				sb.append(",");
			} else {
				sb.append("");
			}
			ids = ids + sb.toString();
		}
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_fare WHERE fareId IN (" + ids + ")  ORDER BY operateDate DESC ";
			 this.pstmt = this.conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Fare fare = new Fare();
				fare.setFareId(Long.parseLong(rs.getString("fareId")));
				fare.setFareType(rs.getString("fareType"));
				fare.setFareAmount(rs.getDouble("fareAmount"));
				fare.setStoreCompany(rs.getString("storeCompany"));
				fare.setJdRoom(rs.getString("jdRoom"));
				fare.setOperateDate(rs.getString(("operateDate")));
				fare.setRemark(rs.getString("remark"));
				someFareArray.add(fare);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return someFareArray;
	}

	/**
	 * 获得数据库中最新插入的一条记录
	 * 
	 * @param fareId
	 * @return fare
	 */
	public Fare getLastFare() {
		Fare fare = new Fare();
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_fare ORDER BY fareId DESC LIMIT 0,1";
			this.pstmt = this.conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			if (rs.next()) {
				fare.setFareId(rs.getLong("fareId"));
				fare.setFareType(rs.getString("fareType"));
				fare.setFareAmount(rs.getDouble("fareAmount"));
				fare.setStoreCompany(rs.getString("storeCompany"));
				fare.setJdRoom(rs.getString("jdRoom"));
				fare.setOperateDate(rs.getString("operateDate"));
				fare.setRemark(rs.getString("remark"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt, rs);
		}
		return fare;
	}

	/**
	 * 按选择条件统计记录的经费金额
	 * 
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @return
	 */
	public double getSumByDateAndOneFaretype(String startDate, String endDate,
			String type) {
		double total = 0;
		String s = null;
		String e = null;
		if (startDate != "" && endDate != "") {// 两个都不为空时，才可以执行日期转化
			s = startDate.trim();
			e = endDate.trim();
		}
		String sql = "";
		if ((s == null) || (e == null)) {// if date is null.
			sql = "SELECT SUM(fareAmount) FROM qy_fare WHERE fareType ='"
					+ type + "'";
		} else {
			if (type == "") {// if type is null.
				sql = "SELECT SUM(c.temp_amount) FROM (SELECT a.fareAmount AS temp_amount FROM qy_fare INNER JOIN qy_faredetail b ON a.fareId=b.fareId WHERE DATE_FORMAT(b.detailTime,'%Y-%m-%d') BETWEEN '"
						+ s + "' AND '" + e + "' GROUP BY a.fareId) c";
			} else {// id type and date are not null
				sql = "SELECT SUM(c.temp_amount) FROM (SELECT a.fareAmount AS temp_amount FROM qy_fare a INNER JOIN qy_faredetail b ON a.fareId=b.fareId WHERE DATE_FORMAT(b.detailTime,'%Y-%m-%d') BETWEEN'"
						+ s + "' AND '" + e + "' AND a.fareType ='" + type + "' GROUP BY a.fareId) c";
			}
		}

		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			if (rs.next()) {
				total = rs.getDouble(1);// get the total fare
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		DBConnection.close(this.conn, this.pstmt, rs);
		return total;
	}

	/**
	 * 判断数据库中是否有相同的记录,用于军代室导入经费
	 * 
	 * @param type
	 * @param amount
	 * @param storeCompany
	 * @param jdRoom
	 * @param operateDate
	 * @param remark
	 * @return
	 */
	public long judgeFare(String type, String amount, String storeCompany,
			String jdRoom, String operateDate, String remark) {
		long fareId = -1;//
		String sql = "SELECT * from qy_fare WHERE fareType='" + type
				+ "' AND fareAmount='" + amount + "' AND storeCompany='"
				+ storeCompany + "' and jdRoom='" + jdRoom
				+ "' and operateDate='" + operateDate + "'and remark='"
				+ remark + "'";  
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			if (rs.next()) { 
				fareId=rs.getLong(1);
			}
			DBConnection.close(this.conn, this.pstmt, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fareId;
	}
	
	/**
	 * 判断数据库中是否有相同的记录,用于军代室导入经费
	 * @param fare
	 * @return
	 */
	public long judgeFare(long fareId,String storeCompany,String operateTime) {
		long tempNum = -1;//
		String sql = "SELECT * from qy_fare a WHERE a.fareId=? AND a.storeCompany=? AND a.operateDate=?";
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, fareId);
			this.pstmt.setString(2, storeCompany);
			this.pstmt.setString(3, operateTime);
			rs = this.pstmt.executeQuery();
			if (rs.next()) { 
				tempNum=rs.getLong(1);
			}
			DBConnection.close(this.conn, this.pstmt, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tempNum;
	}
	
	/**
	 * 操作时间不同，根据费用id和代储企业判断数据库中是否有记录
	 * @param fare
	 * @return
	 */
	public long judgeUpdateFare(Fare fare) {
		long fareId = -1;//
		String sql = "SELECT * from qy_fare a WHERE a.fareId=? AND a.storeCompany=?";
		ResultSet rs = null;
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, fare.getFareId());
			this.pstmt.setString(2, fare.getStoreCompany());
			rs = this.pstmt.executeQuery();
			if (rs.next()) { 
				fareId=rs.getLong(1);
			}
			DBConnection.close(this.conn, this.pstmt, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fareId;
	}
}
