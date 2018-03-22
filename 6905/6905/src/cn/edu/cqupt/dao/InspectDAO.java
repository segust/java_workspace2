package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.beans.InspectRecord;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.RestTime;

public class InspectDAO {

	/**
	 * 查询符合检查条件的物品
	 * @param condition
	 * @return list
	 */
	public List<Map<String, String>> inspectOperate(
			Map<String, String> condition) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		int curPageNum = Integer.parseInt(condition.get("curPageNum"));
		int pageSize = Integer.parseInt(condition.get("pageSize"));
		String productModel = condition.get("productModel");
		String productUnit = condition.get("unitName");
		String manufacturer = condition.get("manufacturer");
		String deviceNo = condition.get("deviceNo");
		String sql_inspect = "SELECT qy_product.productId,qy_product.productModel,"
				+ "qy_product.productUnit,qy_inapply.batch,qy_product.deviceNo,"
				+ "qy_product.productPrice,qy_inapply.num,qy_product.ProductType,"
				+ "qy_product.storageTime,qy_product.manufacturer,"
				+ "qy_product.keeper,qy_product.remark,qy_account.operateTime,"
				+ "qy_account.operateType FROM qy_account ,qy_inproductrelation,"
				+ "qy_inapply , qy_product "
				+ "WHERE qy_product.productId = qy_inproductrelation.productId "
				+ "AND qy_inproductrelation.inId = qy_inapply.inId "
				+ "AND qy_account.productId = qy_inproductrelation.productId "
				+ "AND qy_account.operateType='新入库'";
		if (productModel != "")
			sql_inspect += " AND qy_product.productModel regexp '"
					+ productModel + "'";
		if (productUnit != "")
			sql_inspect += " AND qy_product.productUnit regexp '" + productUnit + "'";
		if (manufacturer != "")
			sql_inspect += " AND qy_inapply.manufacturer regexp '"
					+ manufacturer + "'";
		if (deviceNo != "")
			sql_inspect += " AND qy_inapply.deviceNo regexp '" + deviceNo + "'";

		sql_inspect += " LIMIT " + (curPageNum - 1) * pageSize + "," + pageSize
				+ "";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConn();
			pstm = conn.prepareStatement(sql_inspect);
			rs = pstm.executeQuery();
			while (rs.next()) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("productId", rs.getInt("productId") + "");
				
				if(rs.getString("productModel")==null)
					map.put("productModel", "");
				else
				map.put("productModel", rs.getString("productModel"));
				
				if(rs.getString("productUnit")==null)
					map.put("unitName", "");
				else
					map.put("unitName", rs.getString("productUnit"));
				
				if(rs.getString("batch")==null)
					map.put("batch", "");
				else
					map.put("batch", rs.getString("batch"));
				
				if(rs.getString("deviceNo")==null)
					map.put("deviceNo", "");
				else
					map.put("deviceNo", rs.getString("deviceNo"));
	
				if(rs.getString("productPrice")==null)
					map.put("newPrice", "");
				else
					map.put("newPrice", rs.getString("productPrice"));
			
				if(rs.getString("num")==null)
					map.put("num", "");
				else
					map.put("num", rs.getString("num"));
				
				if(rs.getString("productType")==null)
					map.put("productType", "");
				else
					map.put("productType", rs.getString("productType"));
			
				if(rs.getString("StorageTime")==null)
					map.put("StorageTime", "");
				else
					map.put("StorageTime", rs.getString("StorageTime"));
				
				map.put("restKeepTime", RestTime.CountRestStorageTimeInDays(rs.getString("storageTime"),rs.getDate("operateTime")));
			
				if(rs.getString("manufacturer")==null)
					map.put("manufacturer", "");
				else
					map.put("manufacturer", rs.getString("manufacturer"));
			
				if(rs.getString("keeper")==null)
					map.put("keeper", "");
				else
					map.put("keeper", rs.getString("keeper"));
	
				if(rs.getString("remark")==null)
					map.put("remark", "");
				else
					map.put("remark", rs.getString("remark"));

				list.add(map);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstm, rs);
		}

		return list;
	}

	/**
	 * @param condition
	 * @return返回该符合条件数据的总数getSum_inspect
	 */
	public long getSum_inspect(Map<String, String> condition) {
		long sum_inspect = 0;
		String productModel = condition.get("productModel");
		String unitName = condition.get("unitName");
		String manufacturer = condition.get("manufacturer");
		String deviceNo = condition.get("deviceNo");
		String sql_inspect = "SELECT qy_product.productId,qy_product.productModel,"
				+ "qy_inapply.unitName,qy_inapply.batch,qy_inapply.deviceNo,"
				+ "qy_inapply.newPrice,qy_inapply.num,qy_inapply.ProductType,"
				+ "qy_inapply.storageTime,qy_inapply.manufacturer,"
				+ "qy_inapply.keeper,qy_inapply.remark,qy_account.operateTime,"
				+ "qy_account.operateType FROM qy_account ,qy_inproductrelation,"
				+ "qy_inapply , qy_product "
				+ "WHERE qy_product.productId = qy_inproductrelation.productId "
				+ "AND qy_inproductrelation.inId = qy_inapply.inId "
				+ "AND qy_account.productId = qy_inproductrelation.productId "
				+ "AND qy_account.operateType='新入库'";

		if (productModel != "")
			sql_inspect += " AND qy_product.productModel regexp '"
					+ productModel + "'";
		if (unitName != "")
			sql_inspect += " AND qy_inapply.unitName regexp '" + unitName + "'";

		if (manufacturer != "")
			sql_inspect += " AND qy_inapply.manufacturer regexp '"
					+ manufacturer + "'";

		if (deviceNo != "")
			sql_inspect += " AND qy_inapply.deviceNo regexp '" + deviceNo + "'";

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConn();
			pstm = conn.prepareStatement(sql_inspect);
			rs = pstm.executeQuery();
			while (rs.next()) {
				sum_inspect++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstm, rs);
		}

		return sum_inspect;

	}
/**
 * 
 * @param 需要存储的数据map
 * @return 是否存储成功
 */
	public Boolean inspectRecord(Map<String, String> map) {

		Date inspectTime = new Date();
		String productModel = map.get("productModel");
		String unitName = map.get("unitName");
		String batch = map.get("batch");
		String deviceNo = map.get("deviceNo");
		String price = map.get("price");
		String num = map.get("num");
		String productType = map.get("productType");
		String storageTime = map.get("storageTime");
		String restKeepTime = map.get("restKeepTime");
		String manufacturer = map.get("manufacturer");
		String keeper = map.get("keeper");
		String userName = map.get("userName");
		String remark = map.get("remark");

		if(productModel.equalsIgnoreCase("\"null\""))	productModel="";
		if(unitName.equalsIgnoreCase("\"null\""))	unitName="";
		if(batch.equalsIgnoreCase("\"null\""))	batch="";
		if(deviceNo.equalsIgnoreCase("\"null\""))	deviceNo="";
		if(price.equalsIgnoreCase("\"null\""))	price="";
		if(num.equalsIgnoreCase("\"null\""))	num="";
		if(productType.equalsIgnoreCase("\"null\""))	productType="";
		if(storageTime.equalsIgnoreCase("\"null\""))	storageTime="";
		if(restKeepTime.equalsIgnoreCase("\"null\""))	restKeepTime="";
		if(manufacturer.equalsIgnoreCase("\"null\""))	manufacturer="";
		if(keeper.equalsIgnoreCase("\"null\""))	keeper="";
		if(userName.equalsIgnoreCase("\"null\""))	userName="";
		if(remark.equalsIgnoreCase("\"null\""))	remark="";

		String sql = "insert into qy_inspectRecord(productModel," + "unitName,"
				+ "batch," + "deviceNo," + "price," + "num," + "productType,"
				+ "storageTime," + "restKeepTime," + "JDInspect,"
				+ "inspectTime," + "manufacturer," + "keeper," + "userName,"
				+ "remark)values('"
				+ productModel
				+ "','"
				+unitName
				+ "','"
				+ batch
				+ "','"
				+ deviceNo
				+ "','"
				+price
				+ "','"
				+num
				+ "','"
				+productType
				+ "','"
				+storageTime
				+ "','"
				+restKeepTime
				+ "','"
				+ map.get("JDInspect")
				+ "','"
				+ MyDateFormat.changeToSqlDate(inspectTime)
				+ "','"
				+manufacturer
				+ "','"
				+keeper
				+ "','"
				+ userName+ "','" + remark + "')";
		Connection conn = null;

		PreparedStatement pstm = null;

		ResultSet rs = null;

		try {
			conn = DBConnection.getConn();

			pstm = conn.prepareStatement(sql);

			pstm.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();

			return false;

		} finally {

			DBConnection.close(conn, pstm, rs);
		}

		return true;

	}
/**
 * @param restKeepTime 
 * 修正数据库中restKeepTime类型，满足查询要求
 * @return int型restKeepTime
 */
	private int getDay(String restKeepTime) {
		
		String Day = restKeepTime.substring(0, restKeepTime.length() - 1);
		int restKeepDay = Integer.parseInt(Day);
		return restKeepDay;
	}

	/**
	 * 查询检查记录表qy_inspect
	 * @return 是否查询成功
	 * 
	 */
	/*public List<InspectRecord> inspectQuery(Map<String, String> condition) {

		List<InspectRecord> list = new ArrayList<InspectRecord>();
		int curPageNum = Integer.parseInt(condition.get("curPageNum"));
		int pageSize = Integer.parseInt(condition.get("pageSize"));
		String productModel = condition.get("productModel");
		String JDInspect = condition.get("JDInspect");
		String unitName = condition.get("unitName");
		String manufacturer = condition.get("manufacturer");
		String deviceNo = condition.get("deviceNo");
		String restKeepTime = condition.get("restKeepTime");
		String sql_inspectQuery = "select * from qy_inspectrecord";
		int mark = 0;
		if (productModel != "") {
			sql_inspectQuery += " WHERE productModel regexp '" + productModel
					+ "'";
			mark++;
		}

		if (unitName != "") {
			if (mark > 0)
				sql_inspectQuery += " AND unitName regexp '" + unitName + "'";
			if (mark == 0) {
				sql_inspectQuery += " WHERE unitName regexp '" + unitName + "'";
				mark++;
			}
		}

		if (JDInspect != "" & (JDInspect.equals("0") || JDInspect.equals("1"))) {
			if (mark > 0)
				sql_inspectQuery += " AND JDInspect regexp '" + JDInspect + "'";
			else if (mark == 0) {
				sql_inspectQuery += " WHERE JDInspect regexp '" + JDInspect
						+ "'";
				mark++;
			}
		}

		if (manufacturer != "") {
			if (mark > 0)
				sql_inspectQuery += " AND manufacturer regexp '" + manufacturer
						+ "'";

			else if (mark == 0) {
				sql_inspectQuery += " WHERE manufacturer regexp '"
						+ manufacturer + "'";
				mark++;
			}
		}

		if (deviceNo != "") {
			if (mark > 0)
				sql_inspectQuery += " AND deviceNo regexp '" + deviceNo + "'";

			else if (mark == 0) {
				sql_inspectQuery += " WHERE deviceNo regexp '" + deviceNo + "'";
				mark++;
			}
		}
		sql_inspectQuery += " ORDER BY qy_inspectrecord.inspectId DESC";
		sql_inspectQuery += " LIMIT " + (curPageNum - 1) * pageSize + ","
				+ pageSize + "";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConn();
			pstm = conn.prepareStatement(sql_inspectQuery);
			rs = pstm.executeQuery();
			while (rs.next()) {
				//查询条件restKeepTime(查询条件以天为单位)
				if (restKeepTime != "") {
					int restKeepDay = this.getDay(rs.getString("restKeepTime"));//数据库中剩余存放时间转换成int型天数
					if (Integer.parseInt(restKeepTime) > restKeepDay) {//实现数据剩余时间对比
						InspectRecord inspect = new InspectRecord();
						inspect.setProduceModel(rs.getString("productModel"));
						inspect.setUnitName(rs.getString("unitName"));
						inspect.setBatch(rs.getString("batch"));
						inspect.setDeviceNo(rs.getString("deviceNo"));
						inspect.setPrice(rs.getDouble("price"));
						inspect.setNum(rs.getInt("num"));
						inspect.setProductType(rs.getString("productType"));
						inspect.setStorageTime(rs.getString("storageTime"));
						inspect.setRestKeepTime(rs.getString("restKeepTime"));
						inspect.setJDInspect(rs.getInt("JDInspect"));
						inspect.setInspectTime(rs.getDate("inspectTime"));
						inspect.setManufacturer(rs.getString("manufacturer"));
						inspect.setKeeper(rs.getString("keeper"));
						inspect.setUserName(rs.getString("userName"));
						inspect.setRemark(rs.getString("remark"));

						list.add(inspect);
					} else if (Integer.parseInt(restKeepTime) <= restKeepDay)
						continue;
				} else if (restKeepTime == "") {
					InspectRecord inspect = new InspectRecord();
					inspect.setProduceModel(rs.getString("productModel"));
					inspect.setUnitName(rs.getString("unitName"));
					inspect.setBatch(rs.getString("batch"));
					inspect.setDeviceNo(rs.getString("deviceNo"));
					inspect.setPrice(rs.getDouble("price"));
					inspect.setNum(rs.getInt("num"));
					inspect.setProductType(rs.getString("productType"));
					inspect.setStorageTime(rs.getString("storageTime"));
					inspect.setRestKeepTime(rs.getString("restKeepTime"));
					inspect.setJDInspect(rs.getInt("JDInspect"));
					inspect.setInspectTime(rs.getDate("inspectTime"));
					inspect.setManufacturer(rs.getString("manufacturer"));
					inspect.setKeeper(rs.getString("keeper"));
					inspect.setUserName(rs.getString("userName"));
					inspect.setRemark(rs.getString("remark"));

					list.add(inspect);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstm, rs);
		}
		return list;
	}*/

	/**
	 * 批量存储检查记录
	 * */
	public boolean AddInspectRecord( InspectRecord ir)
	{  boolean flag=false;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
		String sql="insert into qy_inspectrecord (unit,date,site,item,suggest,feedback,remark) values (?,?,?,?,?,?,?)";
		try{
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);  
			pstmt.setString(1,ir.getUnit());
			pstmt.setTimestamp(2,MyDateFormat.changeToSqlDate(ir.getDate()));
			pstmt.setString(3, ir.getSite());
			pstmt.setString(4, ir.getItem());
			pstmt.setString(5, ir.getSuggest());
			pstmt.setString(6, ir.getFeedback());
			pstmt.setString(7, ir.getRemark());
			 System.out.println(sql);
		 int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return flag;
	}
	/**
	 * 查询所有存储检查记录
	 * */
	public List<InspectRecord> getAllinspectRecord(){
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<InspectRecord> recordlist = new ArrayList<InspectRecord>();
		InspectRecord ir = new InspectRecord();
		String sql ="Select * from qy_inspectrecord";
		try {
			conn = DBConnection.getConn();
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			while (rs.next()) {
				ir.setUnit(rs.getString("unit"));
				ir.setDate(rs.getDate("date"));
				ir.setSite(rs.getString("site"));
				ir.setItem(rs.getString("item"));
				ir.setSuggest(rs.getString("suggest"));
				ir.setFeedback(rs.getString("feedback"));
				ir.setRemark(rs.getString("feedback"));
			}
			recordlist.add(ir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordlist;
	}	
	public long getSum_inspectQuery(Map<String, String> condition) {
		long sum_inspectQuery = 0;
		String productModel = condition.get("productModel");
		String JDInspect = condition.get("JDInspect");
		String unitName = condition.get("unitName");
		String manufacturer = condition.get("manufacturer");
		String deviceNo = condition.get("deviceNo");
		String restKeepTime = condition.get("restKeepTime");
		String sql_inspectQuery = "select * from qy_inspectrecord";
		int mark = 0;
		if (productModel != "") {
			sql_inspectQuery += " WHERE productModel regexp '" + productModel
					+ "'";
			mark++;
		}

		if (unitName != "") {
			if (mark > 0)
				sql_inspectQuery += " AND unitName regexp '" + unitName + "'";
			if (mark == 0) {
				sql_inspectQuery += " WHERE unitName regexp '" + unitName + "'";
				mark++;
			}
		}

		if (JDInspect != "" & (JDInspect.equals("0") || JDInspect.equals("1"))) {
			if (mark > 0)
				sql_inspectQuery += " AND JDInspect regexp '" + JDInspect + "'";
			else if (mark == 0) {
				sql_inspectQuery += " WHERE JDInspect regexp '" + JDInspect
						+ "'";
				mark++;
			}
		}

		if (manufacturer != "") {
			if (mark > 0)
				sql_inspectQuery += " AND manufacturer regexp '" + manufacturer
						+ "'";

			else if (mark == 0) {
				sql_inspectQuery += " WHERE manufacturer regexp '"
						+ manufacturer + "'";
				mark++;
			}
		}

		if (deviceNo != "") {
			if (mark > 0)
				sql_inspectQuery += " AND deviceNo regexp '" + deviceNo + "'";

			else if (mark == 0) {
				sql_inspectQuery += " WHERE deviceNo regexp '" + deviceNo + "'";
				mark++;
			}
		}
		sql_inspectQuery += " ORDER BY qy_inspectrecord.inspectId DESC";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConn();
			pstm = conn.prepareStatement(sql_inspectQuery);
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (restKeepTime != "") {
					int restKeepDay = this.getDay(rs.getString("restKeepTime"));//数据库中剩余存放时间转换成int型天数
					if (Integer.parseInt(restKeepTime) > restKeepDay) {
						sum_inspectQuery++;
					} else if (Integer.parseInt(restKeepTime) <= restKeepDay)
						continue;
				} else if (restKeepTime == "") {
					sum_inspectQuery++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstm, rs);
		}
		return sum_inspectQuery;
	}

}
