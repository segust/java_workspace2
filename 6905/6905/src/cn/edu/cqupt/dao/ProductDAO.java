package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.RestTime;
import cn.edu.cqupt.util.StringUtil;
import net.sf.json.JSONObject;

public class ProductDAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public ProductDAO() {

	}

	public ProductDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @author austin 业务查询→产品明细查询
	 * */
	/**
	 * searchAllUserByPage 分页显示所有的产品明细查询结果 查询入库的 返回的hashmap中存放的是hashmap
	 * flag的具体含义见ProductDetailService
	 * 
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, HashMap<String, Object>> selectProductDetailInapply(
			HashMap<String, String> condition, String flag) throws Exception {
		// List<HashMap<String, Object>> T = new ArrayList<HashMap<String,
		// Object>>();// 返回值
		HashMap<Integer, HashMap<String, Object>> T = new HashMap<Integer, HashMap<String, Object>>();
		try {
			StringBuffer sql = new StringBuffer(
					"select "
							+ "a.productId,"
							+ "a.productModel,"
							+ "a.productUnit,"
							+ "a.deviceNo,"
							+ "a.productPrice,"
							+ "a.restKeepTime,"
							+ "a.restMaintainTime,"
							+ "a.latestMaintainTime,"
							+ "a.maintainCycle,"
							+ "a.manufacturer,"
							+ "a.keeper,"
							+ "a.remark,"
							+ "a.storageTime,"
							+ "a.ownedUnit,"
							+ "b.batch,"
							+ "b.inMeans,"
							+ "b.execDate,"
							+ "b.chStatus,"
							+ "c.insertTime"
							+ " from qy_product a,qy_inapply b,qy_inproductrelation c"
							+ " where c.inId=b.inId and c.productId=a.productId"
							+ " and c.ownedUnit=a.ownedUnit and c.ownedUnit=b.ownedUnit"
							+ " and (c.inId,c.productId) in(select max(c.inId),c.productId from qy_product a,qy_inproductrelation c");
			if (flag.equals("b") || flag.equals("c")) {
				sql.append(" where a.proStatus not in ('未申请','合同销毁','进库待审核','未到库') and a.proStatus not regexp '出库$' and a.proStatus not regexp '待审核' and a.productId=c.productId group by c.productId)");

			} else if (flag.equals("a")) {
				sql.append(" where a.proStatus not in ('未申请','合同销毁','进库待审核','未到库') and a.productId=c.productId  group by c.productId)");
			}

			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp '"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp '"
						+ condition.get("productUnit") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("manufacturer"))) {
				if (StringUtil.isNotEmpty(condition.get("manufacturer"))) {
					sql.append(" and");
					sql.append(" a.manufacturer regexp '"
							+ condition.get("manufacturer") + "'");
				}
			}
			if (StringUtil.isNotEmpty(condition.get("deviceNo"))) {
				sql.append(" and");
				sql.append(" a.deviceNo regexp '" + condition.get("deviceNo")
						+ "'");
			}
			if (StringUtil.isNotEmpty(condition.get("ownedUnit"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit='" + condition.get("ownedUnit") + "'");
			}

			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("productId");
				Date insertTime = rs.getDate("insertTime");
				String productModel = rs.getString("productModel");
				String productUnit = rs.getString("productUnit");
				double price = rs.getInt("productPrice");
				String batch = rs.getString("batch");
				String deviceNo = rs.getString("deviceNo");
				String inMeans = rs.getString("inMeans");
				String storageTime = rs.getString("storageTime");
				String manufacturer = rs.getString("manufacturer");
				String keeper = rs.getString("keeper");
				String remark = rs.getString("remark");
				Date execDate = rs.getDate("execDate");
				String maintainCycle = rs.getString("maintainCycle");
				Date latestMaintainTime = rs.getDate("latestMaintainTime");
				String ownedUnit = rs.getString("ownedUnit");
				String restKeepTime = RestTime.CountRestStorageTimeInDays(
						storageTime, execDate);
				int restMaintainTime = RestTime.countRestMaintainTimeInDays(
						latestMaintainTime, maintainCycle);
				// 操作日期
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, restMaintainTime);
				String nextMaintainTime = sf.format(calendar.getTime());

				if (StringUtil.isNotEmpty(condition.get("restKeepTime"))) {
					restKeepTime = restKeepTime.substring(0,
							restKeepTime.length() - 1);
					if (Integer.parseInt(restKeepTime) > Integer
							.parseInt(condition.get("restKeepTime")))
						// 小于多少天
						continue;
				}
				if (StringUtil.isNotEmpty(condition.get("restMaintainTime"))) {
					if (restMaintainTime > Integer.parseInt(condition
							.get("restMaintainTime")))// 小于多少天
						continue;
				}

				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("productId", productId);
				list.put("insertTime", insertTime);
				list.put("productModel", productModel);
				list.put("productUnit", productUnit);
				list.put("productPrice", price);
				list.put("restKeepTime", restKeepTime);
				list.put("restMaintainTime", restMaintainTime);
				list.put("batch", batch);
				list.put("deviceNo", deviceNo);
				list.put("Means", inMeans);
				list.put("num", 1);
				list.put("execDate", rs.getDate("execDate"));
				list.put("storageTime", storageTime);
				list.put("manufacturer", manufacturer);
				list.put("keeper", keeper);
				list.put("remark", remark);
				list.put("nextMaintainTime", nextMaintainTime);
				list.put("ownedUnit", ownedUnit);
				T.put(productId, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return T;
	}

	/**
	 * searchAllUserByPage 分页显示所有的产品明细查询结果 查询出库的 flag的具体含义见ProductDetailService
	 * 
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, HashMap<String, Object>> selectProductDetailOutapply(
			HashMap<String, String> condition, String flag) throws Exception {
		// List<HashMap<String, Object>> T = new ArrayList<HashMap<String,
		// Object>>();// 返回值
		HashMap<Integer, HashMap<String, Object>> T = new HashMap<Integer, HashMap<String, Object>>();
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select "
							+ "a.productId,"
							+ "a.productModel,"
							+ "a.productUnit,"
							+ "a.deviceNo,"
							+ "a.productPrice,"
							+ "a.restKeepTime,"
							+ "a.restMaintainTime,"
							+ "a.latestMaintainTime,"
							+ "a.maintainCycle,"
							+ "a.manufacturer,"
							+ "a.keeper,"
							+ "a.remark,"
							+ "a.storageTime,"
							+ "a.ownedUnit,"
							+ "b.batch,"
							+ "b.outMeans,"
							+ "b.execDate,"
							+ "b.chStatus,"
							+ "c.insertTime"
							+ " from qy_product a,qy_outapply b,qy_outproductrelation c"
							+ " where c.outId=b.outId and c.productId=a.productId"
							+ " and c.ownedUnit=a.ownedUnit and c.ownedUnit=b.ownedUnit"
							+ " and (c.outId,c.productId) in(select max(c.outId),c.productId from qy_product a,qy_outproductrelation c"
							+ " where a.proStatus not in ('未申请','合同销毁','进库待审核','未到库') and a.productId=c.productId group by c.productId)");

			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp '"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp '"
						+ condition.get("productUnit") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("manufacturer"))) {
				if (StringUtil.isNotEmpty(condition.get("manufacturer").trim())) {
					sql.append(" and");
					sql.append(" a.manufacturer regexp '"
							+ condition.get("manufacturer") + "'");
				}
			}
			if (StringUtil.isNotEmpty(condition.get("deviceNo"))) {
				sql.append(" and");
				sql.append(" a.deviceNo regexp '" + condition.get("deviceNo")
						+ "'");
			}
			if (StringUtil.isNotEmpty(condition.get("ownedUnit"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit='" + condition.get("ownedUnit") + "'");
			}
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("productId");
				Date insertTime = rs.getDate("insertTime");
				String productModel = rs.getString("productModel");
				String productUnit = rs.getString("productUnit");
				double price = rs.getInt("productPrice");
				String batch = rs.getString("batch");
				String deviceNo = rs.getString("deviceNo");
				String outMeans = rs.getString("outMeans");
				String storageTime = rs.getString("storageTime");
				String manufacturer = rs.getString("manufacturer");
				String keeper = rs.getString("keeper");
				String remark = rs.getString("remark");
				Date execDate = rs.getDate("execDate");
				String maintainCycle = rs.getString("maintainCycle");
				Date latestMaintainTime = rs.getDate("latestMaintainTime");
				String ownedUnit = rs.getString("ownedUnit");
				String restKeepTime = RestTime.CountRestStorageTimeInDays(
						storageTime, execDate);
				int restMaintainTime = RestTime.countRestMaintainTimeInDays(
						latestMaintainTime, maintainCycle);
				// 操作日期
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, restMaintainTime);
				String nextMaintainTime = sf.format(calendar.getTime());

				if (StringUtil.isNotEmpty(condition.get("restKeepTime"))) {
					restKeepTime = restKeepTime.substring(0,
							restKeepTime.length() - 1);
					if (Integer.parseInt(restKeepTime) > Integer
							.parseInt(condition.get("restKeepTime")))
						// 小于多少天
						continue;
				}
				if (StringUtil.isNotEmpty(condition.get("restMaintainTime"))) {
					if (restMaintainTime > Integer.parseInt(condition
							.get("restMaintainTime")))// 小于多少天
						continue;
				}

				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("productId", productId);
				list.put("insertTime", insertTime);
				list.put("productModel", productModel);
				list.put("productUnit", productUnit);
				list.put("productPrice", price);
				list.put("restKeepTime", restKeepTime);
				list.put("restMaintainTime", restMaintainTime);
				list.put("batch", batch);
				list.put("deviceNo", deviceNo);
				list.put("Means", outMeans);
				list.put("num", 1);
				list.put("execDate", rs.getDate("execDate"));
				list.put("storageTime", storageTime);
				list.put("manufacturer", manufacturer);
				list.put("keeper", keeper);
				list.put("remark", remark);
				list.put("nextMaintainTime", nextMaintainTime);
				list.put("ownedUnit", ownedUnit);
				T.put(productId, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return T;
	}

	/**
	 * searchAllUserByPage 分页显示所有的产品明细查询结果 查询发料单的 flag的具体含义见ProductDetailService
	 * 
	 * @return
	 * @throws Exception
	 */
	public HashMap<Integer, HashMap<String, Object>> selectProductDetailOutlist(
			HashMap<String, String> condition, String flag) throws Exception {
		// List<HashMap<String, Object>> T = new ArrayList<HashMap<String,
		// Object>>();// 返回值
		HashMap<Integer, HashMap<String, Object>> T = new HashMap<Integer, HashMap<String, Object>>();
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select "
							+ "a.productId,"
							+ "a.productModel,"
							+ "a.productUnit,"
							+ "a.deviceNo,"
							+ "a.productPrice,"
							+ "a.restKeepTime,"
							+ "a.restMaintainTime,"
							+ "a.latestMaintainTime,"
							+ "a.maintainCycle,"
							+ "a.manufacturer,"
							+ "a.keeper,"
							+ "a.remark,"
							+ "a.storageTime,"
							+ "a.ownedUnit,"
							+ "b.outMeans,"
							+ "c.insertTime"
							+ " from qy_product a,qy_outlist b,qy_outlistproductrelation c"
							+ " where c.listId=b.listId and c.productId=a.productId"
							+ " and c.ownedUnit=a.ownedUnit and c.ownedUnit=b.ownedUnit"
							+ " and (c.listId,c.productId) in(select max(c.listId),c.productId from qy_product a,qy_outlistproductrelation c"
							+ " where a.proStatus not in ('未申请','合同销毁','进库待审核','未到库') and a.productId=c.productId group by c.productId)");

			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp '"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp '"
						+ condition.get("productUnit") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("manufacturer"))) {
				if (StringUtil.isNotEmpty(condition.get("manufacturer").trim())) {
					sql.append(" and");
					sql.append(" a.manufacturer regexp '"
							+ condition.get("manufacturer") + "'");
				}
			}
			if (StringUtil.isNotEmpty(condition.get("deviceNo"))) {
				sql.append(" and");
				sql.append(" a.deviceNo regexp '" + condition.get("deviceNo")
						+ "'");
			}
			if (StringUtil.isNotEmpty(condition.get("ownedUnit"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit='" + condition.get("ownedUnit") + "'");
			}
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("productId");
				Date insertTime = rs.getDate("insertTime");
				String productModel = rs.getString("productModel");
				String productUnit = rs.getString("productUnit");
				double price = rs.getInt("productPrice");
				String deviceNo = rs.getString("deviceNo");
				String outMeans = rs.getString("outMeans");
				String storageTime = rs.getString("storageTime");
				String manufacturer = rs.getString("manufacturer");
				String keeper = rs.getString("keeper");
				String remark = rs.getString("remark");
				Date execDate = rs.getDate("insertTime");
				String maintainCycle = rs.getString("maintainCycle");
				Date latestMaintainTime = rs.getDate("latestMaintainTime");
				String ownedUnit = rs.getString("ownedUnit");
				String restKeepTime = RestTime.CountRestStorageTimeInDays(
						storageTime, execDate);
				int restMaintainTime = RestTime.countRestMaintainTimeInDays(
						latestMaintainTime, maintainCycle);
				// 操作日期
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, restMaintainTime);
				String nextMaintainTime = sf.format(calendar.getTime());

				if (StringUtil.isNotEmpty(condition.get("restKeepTime"))) {
					restKeepTime = restKeepTime.substring(0,
							restKeepTime.length() - 1);
					if (Integer.parseInt(restKeepTime) > Integer
							.parseInt(condition.get("restKeepTime")))
						// 小于多少天
						continue;
				}
				if (StringUtil.isNotEmpty(condition.get("restMaintainTime"))) {
					if (restMaintainTime > Integer.parseInt(condition
							.get("restMaintainTime")))// 小于多少天
						continue;
				}

				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("productId", productId);
				list.put("insertTime", insertTime);
				list.put("productModel", productModel);
				list.put("productUnit", productUnit);
				list.put("productPrice", price);
				list.put("restKeepTime", restKeepTime);
				list.put("restMaintainTime", restMaintainTime);
				list.put("batch", "");
				list.put("deviceNo", deviceNo);
				list.put("Means", outMeans);
				list.put("num", 1);
				list.put("execDate", insertTime);
				list.put("storageTime", storageTime);
				list.put("manufacturer", manufacturer);
				list.put("keeper", keeper);
				list.put("remark", remark);
				list.put("nextMaintainTime", nextMaintainTime);
				list.put("ownedUnit", ownedUnit);
				T.put(productId, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return T;
	}

	/**
	 * 查询产品明细总条数
	 * 
	 * @return
	 */
	public int queryDetailSum(HashMap<String, Object> condition, String version) {
		int count = 0;
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select "
							+ "a.productModel,"
							+ "a.productUnit,"
							+ "a.deviceNo,"
							+ "a.productPrice,"
							+ "a.restKeepTime,"
							+ "a.restMaintainTime,"
							+ "a.latestMaintainTime,"
							+ "a.maintainCycle,"
							+ "a.manufacturer,"
							+ "a.keeper,"
							+ "a.remark,"
							+ "a.storageTime,"
							+ "a.ownedUnit,"
							+ "b.batch,"
							+ "b.inMeans,"
							+ "b.execDate,"
							+ "b.chStatus,"
							+ "count(*)"
							+ " from qy_product a,qy_inapply b,qy_inproductrelation c"
							+ " where c.inId=b.inId and c.productId=a.productId"
							+ " and (c.inId,c.productId) in(select max(c.inId),c.productId from qy_product a,qy_inproductrelation c"
							+ " where a.proStatus not in ('未申请','合同销毁','进库待审核','未到库') and a.productId=c.productId group by c.productId)");

			// 设置查询sql条件..^[已].{0,}入库$
			if (version.equals("2") || version.equals("3")
					|| version.equals("4")) {
				sql.append(" and c.ownedUnit=a.ownedUnit and c.ownedUnit=b.ownedUnit");
			}
			if (StringUtil.isNotEmpty((String) condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp '"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty((String) condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp '"
						+ condition.get("productUnit") + "'");
			}
			if (StringUtil.isNotEmpty((String) condition.get("restKeepTime"))) {
				sql.append(" and");
				sql.append(" a.restKeepTime<'" + condition.get("restKeepTime")
						+ "'");// 小于多少天
			}
			if (StringUtil.isNotEmpty((String) condition
					.get("restMaintainTime"))) {
				sql.append(" and");
				sql.append(" a.restMaintainTime<'"
						+ condition.get("restMaintainTime") + "'");// 小于多少天
			}
			if (StringUtil.isNotEmpty((String) condition.get("manufacturer"))) {
				sql.append(" and");
				sql.append(" b.manufacturer regexp '"
						+ condition.get("manufacturer") + "'");
			}
			if (StringUtil.isNotEmpty((String) condition.get("inMeans"))) {
				sql.append(" and");
				sql.append(" b.inMeans regexp '" + condition.get("inMeans")
						+ "'");
			}
			if (StringUtil.isNotEmpty((String) condition.get("deviceNo"))) {
				sql.append(" and");
				sql.append(" b.deviceNo regexp '" + condition.get("deviceNo")
						+ "'");
			}
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt("count(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		System.out.println("productDetalSum:" + count);
		return count;
	}

	/**
	 * @author austin 业务查询→产品汇总查询
	 * */
	/**
	 * searchAllUserByPage 分页显示所有的设备汇总查询结果
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> selectProductCollective(
			HashMap<String, String> condition, int curPageNum, int pageSize,
			String version) throws Exception {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select "
							+ "a.productModel,"
							+ "a.productUnit,"
							+ "a.wholeName,"
							+ "e.QCBM,"
							+ "a.measureUnit,"
							+ "a.productPrice,"
							+ "a.manufacturer,"
							+ "a.keeper,"
							+ "count(*),");
							if(version.equals("3")){
								sql.append(" b.ownedJdsName"
								+" from qy_product a,companyinfo b,qy_basedata e"
								+" where a.prostatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM and a.ownedUnit=b.companyName");
							}else{
								sql.append("a.ownedUnit"
								+" from qy_product a,qy_basedata e"
								+" where a.prostatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM");
							}

			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp '"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp '"
						+ condition.get("productUnit") + "'");
			}
			if (version.equals("1")) {
				if (StringUtil.isEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			} else if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN (" + condition.get("keeper")
							+ ")");
				}
			} else if (version.equals("3") || version.equals("4")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN (" + condition.get("keeper")
							+ ")");
				}
			}
			if(version.equals("3")){
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,b.ownedJdsName");
			}else{
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,a.ownedUnit");
			}
			sql.append(" limit ?,?");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setInt(1, (curPageNum - 1) * pageSize);
			pstmt.setInt(2, pageSize);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String productModel = rs.getString("productModel");
				String QCBM = rs.getString("QCBM");
				String productUnit = rs.getString("productUnit");
				String wholeName=rs.getString("wholeName");
				String measureUnit = rs.getString("measureUnit");
				double productPrice = rs.getDouble("productPrice");
				String manufacturer = rs.getString("manufacturer");
				String keeper=rs.getString("keeper");
				String ownedUnit = "";
				if(version.equals("3")){
					ownedUnit = rs.getString("ownedJdsName");
				}else{
					ownedUnit = rs.getString("ownedUnit");
				}
				int count = rs.getInt("count(*)");

				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("productModel", productModel);
				list.put("QCBM", QCBM);
				list.put("wholeName", wholeName);
				list.put("productUnit", productUnit);
				list.put("measureUnit", measureUnit);
				list.put("productPrice", productPrice);
				list.put("manufacturer", manufacturer);
				list.put("keeper", keeper);
				list.put("ownedUnit", ownedUnit);
				list.put("num", count);
				list.put("totalPrice", count * productPrice);
				T.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return T;
	}

	/**
	 * 查询产品汇总总条数
	 * 
	 * @return
	 */
	public int querySum(HashMap<String, String> condition, String version) {
		int sum = 0;
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select "
							+ "a.productModel,"
							+ "a.productUnit,"
							+ "e.QCBM,"
							+ "a.measureUnit,"
							+ "a.productPrice,"
							+ "a.manufacturer,"
							+ "count(*),");
							if(version.equals("3")){
								sql.append(" b.ownedJdsName"
								+" from qy_product a,companyinfo b,qy_basedata e"
								+" where a.prostatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM and a.ownedUnit=b.companyName");
							}else{
								sql.append("a.ownedUnit"
								+" from qy_product a,qy_basedata e"
								+" where a.prostatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM");
							}

			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp'"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp'"
						+ condition.get("productUnit") + "'");
			}
			if (version.equals("1")) {
				if (StringUtil.isEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			} else if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			} else if (version.equals("3") || version.equals("4")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			}
			if(version.equals("3")){
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,b.ownedJdsName");
			}else{
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,a.ownedUnit");
			}
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				sum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return sum;
	}

	/**
	 * 按设备统计总金额
	 */
	public double selectTotalPrice(
			HashMap<String, String> condition,String version) throws Exception {
		double totalPrice = 0.0;
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select "
							+ "sum(a.productPrice),");
							if(version.equals("3")){
								sql.append(" b.ownedJdsName"
								+" from qy_product a,companyinfo b,qy_basedata e"
								+" where a.prostatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM and a.ownedUnit=b.companyName");
							}else{
								sql.append("a.ownedUnit"
								+" from qy_product a,qy_basedata e"
								+" where a.prostatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM");
							}

			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp '"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp '"
						+ condition.get("productUnit") + "'");
			}
			if (version.equals("1")) {
				//isEmpty改为isNotEmpty edited by lhs 16-01-22
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			} else if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN (" + condition.get("keeper")
							+ ")");
				}
			} else if (version.equals("3") || version.equals("4")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN (" + condition.get("keeper")
							+ ")");
				}
			}
			if(version.equals("3")){
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,b.ownedJdsName");
			}else{
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,a.ownedUnit");
			}
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				totalPrice += rs.getDouble("sum(a.productPrice)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return totalPrice;
	}
	/*public double selectTotalPrice(HashMap<String, String> condition,
			String version) {
		double totalPrice = 0;
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select "
							+ "sum(a.productPrice)");
			if(version.equals("3")){
				sql.append(" from qy_product a,companyinfo b,qy_9831 e"
				+" where a.prostatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM and a.ownedUnit=b.companyName");
			}else{
				sql.append(" from qy_product a,qy_9831 e where a.proStatus not in('未申请','合同销毁','进库待审核','未到库') and a.PMNM=e.PMNM");
			}

			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" and");
				sql.append(" a.productModel regexp'"
						+ condition.get("productModel") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productUnit"))) {
				sql.append(" and");
				sql.append(" a.productUnit regexp'"
						+ condition.get("productUnit") + "'");
			}
			if (version.equals("1")) {
				if (StringUtil.isEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			}
			if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			}
			if (version.equals("3")) {
				if (StringUtil.isNotEmpty(condition.get("JDS"))) {
					sql.append(" and");
					sql.append(" a.ownedUnit IN ("
							+ condition.get("ownedUnitList") + ")");
				}
			}
			if(version.equals("3")){
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,b.ownedJdsName");
			}else{
				sql.append(" group by a.productModel,a.productUnit,e.QCBM,a.measureUnit,a.productPrice,a.manufacturer,a.ownedUnit");
			}
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				totalPrice += rs.getDouble("sum(a.productPrice)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return totalPrice;
	}

	/**
	 * @author austin 业务查询→按产品统计查询
	 * */
	public List<Map<String, String>> selectProductStatistics(
			Map<String, String> map) throws Exception {
		String InStausRegexp1 = ".*已入库.*";
		String InStausRegexp2 = ".*[出库待审核].*"; // 匹配入库正则表达式
		String InStausRegexp3 = "[已].*[入库]";
		String outStatusRegexp1 = ".*已出库.*"; // 匹配出库正则表达式
		String outStatusRegexp2 = ".*[入库待审核]";
		List<String> param = new ArrayList<String>();
		String sql = "select qy_product.contractId,qy_product.productName,qy_product.productPrice,qy_contract.totalNumber"
				+ ",qy_contract.contractPrice,qy_product.proStatus,qy_product.productUnit,"
				+ "qy_product.manufacturer,qy_product.keeper "
				+ "from qy_product inner join qy_contract on qy_contract.contractId=qy_product.contractId and ";
		for (String keys : map.keySet()) {
			String part = null;
			if (keys.equals("productModel") && map.get("productModel") != null
					&& !map.get("productModel").equals("")
					&& !map.get("productModel").equals("null")) {
				part = "qy_product.productModel" + "=" + "'" + map.get(keys)
						+ "'";
				param.add(part);
			}

			if (keys.equals("productUnit") && map.get("productUnit") != null
					&& !map.get("productModel").equals("")
					&& !map.get("productModel").equals("null")) {
				part = "qy_product.productUnit" + "=" + "'" + map.get(keys)
						+ "'";
				param.add(part);
			}

		}

		if (param.size() == 1) {
			sql = sql + param.get(0);
		}
		if (param.size() == 2) {
			sql = sql + param.get(0) + " and " + param.get(1);

		}

		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		Map<String, Map<String, Double[]>> productStatistic = new HashMap<String, Map<String, Double[]>>(); // Double数组中存放的数据依次是出库数量、在库数量、产品总数量、产品单价、合同金额。而key值为了多需求需要，用设备名称、单元名称、承制单位、代储单位组合的key键值。
		pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		try {
			while (rs.next()) {
				// 此处少一个"规格"字段
				String contractId = rs.getString("contractId");
				String productName = rs.getString("productName"); // 设备名称
				String productUnit = rs.getString("productUnit"); // 单元名称
				String productPrice = rs.getString("productPrice");
				String proStatus = rs.getString("proStatus");
				String manufacturer = rs.getString("manufacturer"); // 承制单位
				String keeper = rs.getString("keeper"); // 代储单位
				int totalNumber = rs.getInt("totalNumber"); // 此字段在合同里边
				double contractPrice = rs.getDouble("contractPrice"); // 此字段在合同里边
				String combineKey = productName + " " + productUnit + " "
						+ manufacturer + " " + keeper + "	" + productPrice
						+ "	" + String.valueOf(totalNumber);
				// System.out.println("combineKey..............." + combineKey);
				if (productStatistic.containsKey(combineKey)) {
					Map<String, Double[]> tem = productStatistic
							.get(combineKey);
					if (tem.containsKey(contractId)) {
						Double[] num = tem.get(contractId);
						num[2] += 1.0;
						if (Pattern.compile(InStausRegexp1).matcher(proStatus)
								.matches()
								|| Pattern.compile(InStausRegexp2)
										.matcher(proStatus).matches()
								|| Pattern.compile(InStausRegexp3)
										.matcher(proStatus).matches()) { // 入库
							num[1] += 1.0;
						}
						if (Pattern.compile(outStatusRegexp1)
								.matcher(proStatus).matches()
								|| Pattern.compile(outStatusRegexp2)
										.matcher(proStatus).matches()) { // 出库
							num[0] += 1.0;
						}
						tem.put(contractId, num);
					} else {
						tem.put(contractId, new Double[5]);
						Double[] array = tem.get(contractId);
						array[3] = Double.valueOf(productPrice);
						array[4] = contractPrice;
						array[2] = 1.0;
						if (Pattern.compile(InStausRegexp1).matcher(proStatus)
								.matches()
								|| Pattern.compile(InStausRegexp2)
										.matcher(proStatus).matches()
								|| Pattern.compile(InStausRegexp3)
										.matcher(proStatus).matches()) { // 入库
							array[1] = 1.0;
							array[0] = 0.0;
						}
						if (Pattern.compile(outStatusRegexp1)
								.matcher(proStatus).matches()
								|| Pattern.compile(outStatusRegexp2)
										.matcher(proStatus).matches()) { // 出库
							array[0] = 1.0;
							array[1] = 0.0;
						}
					}

				} else {
					productStatistic.put(combineKey,
							new HashMap<String, Double[]>());
					Map<String, Double[]> arr = productStatistic
							.get(combineKey);
					arr.put(contractId, new Double[5]);
					Double[] array = arr.get(contractId);
					array[3] = Double.valueOf(productPrice);
					array[4] = contractPrice;
					array[2] = 1.0;
					if (Pattern.compile(InStausRegexp1).matcher(proStatus)
							.matches()
							|| Pattern.compile(InStausRegexp2)
									.matcher(proStatus).matches()
							|| Pattern.compile(InStausRegexp3)
									.matcher(proStatus).matches()) { // 入库
						array[1] = 1.0;
						array[0] = 0.0;
					}
					if (Pattern.compile(outStatusRegexp1).matcher(proStatus)
							.matches()
							|| Pattern.compile(outStatusRegexp2)
									.matcher(proStatus).matches()) { // 出库
						array[0] = 1.0;
						array[1] = 0.0;
					}
				}

			}
			double allContractAccount = 0;
			HashMap<String, String> endInfo = new HashMap<String, String>();
			for (String keys : productStatistic.keySet()) {
				Map<String, String> val = new HashMap<String, String>();
				Map<String, Double[]> ma = productStatistic.get(keys);
				double inStore = 0;
				double outStore = 0;
				// double allNumber= 0;
				double inStoreAccount = 0;
				double contractAccount = 0;

				for (String id : ma.keySet()) {
					Double[] arr = ma.get(id);
					outStore += arr[0];
					inStore += arr[1];
					// allNumber+=(arr[0]+arr[1]);
					inStoreAccount += (arr[1] * arr[3]);
					contractAccount += arr[4];

				}
				allContractAccount += contractAccount;
				String[] info = keys.split(" ");
				val.put("productName", info[0]);
				val.put("productUnit", info[1]);
				val.put("manufacturer", info[2]);
				val.put("keeper", info[3]);
				val.put("productPrice", info[4]);
				val.put("totalNumber", "haha");
				val.put("inStore", String.valueOf(inStore));
				val.put("outStore", String.valueOf(outStore));
				// val.put("allNumber", String.valueOf(allNumber));
				val.put("inStoreAccount", String.valueOf(inStoreAccount));
				val.put("contractAccount", String.valueOf(contractAccount));
				T.add(val);
			}
			endInfo.put("allContractAccount",
					String.valueOf(allContractAccount));
			T.add(endInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return T;
	}

	/**
	 * @author austin 实力统计→器材实力汇总统计
	 * */
	public ArrayList<Map<String, Object>> selectEquipmentCollective(
			HashMap<String, String> condition, String version, int curPageNum,
			int pageSize) throws Exception {
		ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();// 返回值

		String inYear = (String) condition.get("inYear");
		String inStoreYear = new String();
		// 获取当前日期
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 + "";
		String day = cal.get(Calendar.DATE) + "";
		String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
		String minute = cal.get(Calendar.MINUTE) + "";
		String second = cal.get(Calendar.SECOND) + "";

		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"SELECT e.productName,e.productModel,e.measureUnit,e.productPrice,")
					.append("e.remark,e.QCBM,e.ownedUnit,count(*),count(*)*e.productPrice")
					.append(" FROM")
					.append(" (SELECT c.productName,c.productModel,c.measureUnit,c.productPrice,c.remark,d.QCBM,c.ownedUnit,b.operateType")
					.append(" FROM(SELECT * FROM qy_account a WHERE a.operateTime<? ORDER BY a.operateTime DESC) b,qy_product c,qy_basedata d")
					.append(" WHERE b.productId = c.productId AND c.PMNM = d.PMNM AND b.ownedUnit = c.ownedUnit AND b.ownedUnit = d.ownedUnit");

			if (StringUtil.isNotEmpty((String) (condition.get("QCBM")))) {
				sql.append(" AND d.QCBM REGEXP '" + condition.get("QCBM") + "'");
			}
			if (StringUtil.isNotEmpty((String) (condition.get("productName")))) {
				sql.append(" AND c.productName REGEXP '"
						+ condition.get("productName") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" AND c.productModel REGEXP '"
						+ condition.get("productModel") + "'");
			}
			if (version.equals("1")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			} else if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			} else if (version.equals("3")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			}
			sql.append(" GROUP BY b.productId) e");
			sql.append(" WHERE e.operateType regexp '入库'");
			sql.append(" GROUP BY e.productName,e.productModel,e.measureUnit,e.productPrice,e.remark,e.QCBM,e.ownedUnit");
			sql.append(" LIMIT ?,?");
			if (StringUtil.isEmpty(inYear) || "null".equals(inYear)
					|| year.equals(inYear) || "/*/*".equals(inYear)) {
				// 为现在年时
				inStoreYear = year;
				inYear = year + "-" + month + "-" + day + " " + hour + ":"
						+ minute + ":" + second;
			} else if (StringUtil.isNotEmpty(inYear) && !"null".equals(inYear)
					&& Integer.parseInt(inYear) < Integer.parseInt(year)) {
				// 不为现在年时
				if(Integer.parseInt(inYear) < 1000 && Integer.parseInt(inYear) > 99){
					inYear = "0"+inYear;
				}
				if(Integer.parseInt(inYear) < 100 && Integer.parseInt(inYear) > 0){
					inYear = "00"+inYear;
				}
				inStoreYear = inYear;
				inYear += "-12-31 23:59:59";
				System.out.println("inYear:"+inYear);
			} else if (Integer.parseInt(inYear) > Integer.parseInt(year)) {
				// 大于现在年时，应在前台做js判断，暂时写为0
				inYear = "0";
			}
			// System.out.println(sql.toString());

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, inYear);
			pstmt.setInt(2, (curPageNum - 1) * pageSize);
			pstmt.setInt(3, pageSize);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String QCBM = rs.getString("QCBM");
				String productName = rs.getString("productName");
				String productModel = rs.getString("productModel");
				String measureUnit = rs.getString("measureUnit");
				double productPrice = rs.getDouble("productPrice");
				String remark = rs.getString("remark");
				int count = rs.getInt("count(*)");
				double totalPrice = rs.getDouble("count(*)*e.productPrice");
				String ownedUnit = new String();
				String jds = new String();
				String jdj = new String();
				if (version.equals("2")) {
					ownedUnit = rs.getString("ownedUnit");
				} else if (version.equals("3")) {
					ownedUnit = rs.getString("ownedUnit");
					jds = selectJDS(ownedUnit);
				} else if (version.equals("4")) {
					ownedUnit = rs.getString("ownedUnit");
					jds = selectJDS(ownedUnit);
					jdj = selectJDJ(jds);
				}

				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("inYear", inStoreYear);
				list.put("QCBM", QCBM);
				list.put("productName", productName);
				list.put("productModel", productModel);
				list.put("measureUnit", measureUnit);
				list.put("productPrice", productPrice);
				list.put("remark", remark);
				list.put("num", count);
				list.put("totalPrice", totalPrice);
				if (version.equals("2")) {
					list.put("ownedUnit", ownedUnit);
				} else if (version.equals("3")) {
					list.put("ownedUnit", ownedUnit);
					list.put("jds", jds);
				} else if (version.equals("4")) {
					list.put("ownedUnit", ownedUnit);
					list.put("jds", jds);
					list.put("jdj", jdj);
				}

				resultList.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt);
		}

		return resultList;
	}

	// 汇总统计总数目
	public int selectEquipmentCollectiveCount(
			HashMap<String, String> condition, String version) throws Exception {
		int count = 0;

		String inYear = condition.get("inYear");
		// 获取当前日期
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 + "";
		String day = cal.get(Calendar.DATE) + "";
		String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
		String minute = cal.get(Calendar.MINUTE) + "";
		String second = cal.get(Calendar.SECOND) + "";

		try {
			this.conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer("SELECT count(*)")
					.append(" FROM")
					.append(" (SELECT e.productName,e.productModel,e.measureUnit,")
					.append("e.productPrice,e.remark,e.QCBM,count(*),count(*)*e.productPrice")
					.append(" FROM")
					.append(" (SELECT c.productName,c.productModel,c.measureUnit,c.productPrice,c.remark,d.QCBM,b.operateType")
					.append(" FROM(SELECT * FROM qy_account a WHERE a.operateTime<? ORDER BY a.operateTime DESC) b,qy_product c,qy_basedata d")
					.append(" WHERE b.productId = c.productId AND c.PMNM = d.PMNM AND b.ownedUnit = c.ownedUnit")
					.append(" AND b.operateType REGEXP '入库'");
			if (StringUtil.isNotEmpty((String) (condition.get("QCBM")))
					&& !"null".equals((String) (condition.get("QCBM")))) {
				sql.append(" AND d.QCBM REGEXP '" + condition.get("QCBM") + "'");
			}
			if (StringUtil.isNotEmpty((condition.get("productName")))
					&& !"null".equals((String) (condition.get("productName")))) {
				sql.append(" AND c.productName REGEXP '"
						+ condition.get("productName") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productModel"))) {
				sql.append(" AND c.productModel REGEXP '"
						+ condition.get("productModel") + "'");
			}
			if (version.equals("1")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			} else if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			} else if (version.equals("3")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			}
			sql.append(" GROUP BY b.productId) e");
			sql.append(" WHERE e.operateType regexp '入库'");
			sql.append(" GROUP BY e.productName,e.productModel,e.measureUnit,e.productPrice,e.remark,e.QCBM) f");
			if (StringUtil.isEmpty(inYear) || "null".equals(inYear)
					|| year.equals(inYear) || "/*/*".equals(inYear)) {
				// 为现在年时
				inYear = year + "-" + month + "-" + day + " " + hour + ":"
						+ minute + ":" + second;
			} else if (StringUtil.isNotEmpty(inYear) && !"null".equals(inYear)
					&& Integer.parseInt(inYear) < Integer.parseInt(year)) {
				// 不为现在年时
				if(Integer.parseInt(inYear) < 1000 && Integer.parseInt(inYear) > 99){
					inYear = "0"+inYear;
				}
				if(Integer.parseInt(inYear) < 100 && Integer.parseInt(inYear) > 0){
					inYear = "00"+inYear;
				}
				inYear += "-12-31 23:59:59";
			} else if (Integer.parseInt(inYear) > Integer.parseInt(year)) {
				// 大于现在年时，应在前台做js判断，暂时写为0
				return 0;
			}
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, inYear);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count += rs.getInt("count(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt);
		}

		return count;
	}

	/**
	 * @author austin 实力统计→器材明细统计
	 * */
	public List<HashMap<String, Object>> selectEquipmentDetail(
			HashMap<String, String> condition, String version, int curPageNum,
			int pageSize) throws Exception {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();

		String inYear = condition.get("inYear");
		String inStoreYear = new String();
		// 获取当前日期
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 + "";
		String day = cal.get(Calendar.DATE) + "";
		String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
		String minute = cal.get(Calendar.MINUTE) + "";
		String second = cal.get(Calendar.SECOND) + "";

		try {
			StringBuffer sql = new StringBuffer("SELECT * FROM ")
					.append("(SELECT b.productId,b.operateType,b.operateTime,c.productName,c.productModel,c.manufacturer,c.measureUnit,c.productPrice,c.producedDate,c.storageTime,c.keeper,c.remark,d.QCBM,c.ownedUnit")
					.append(" FROM (SELECT * FROM qy_account a WHERE a.operateTime<? ORDER BY a.operateTime DESC) b,qy_product c,qy_basedata d")
					.append(" WHERE b.productId = c.productId AND c.PMNM = d.PMNM");
			if (StringUtil.isNotEmpty(condition.get("QCBM"))) {
				sql.append(" AND d.QCBM REGEXP '" + condition.get("QCBM") + "'");
			}
			if (StringUtil.isNotEmpty(condition.get("productName"))) {
				sql.append(" AND c.productName REGEXP '"
						+ condition.get("productName") + "'");
			}
			if (version.equals("1")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			} else if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			} else if (version.equals("3")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			}
			sql.append(" GROUP BY b.productId) f");
			sql.append(" WHERE f.operateType REGEXP '入库'");
			sql.append(" LIMIT ?,?");

			if (StringUtil.isEmpty(inYear) || "null".equals(inYear)
					|| year.equals(inYear) || "/*/*".equals(inYear)) {
				// 为现在年时
				inStoreYear = year;
				inYear = year + "-" + month + "-" + day + " " + hour + ":"
						+ minute + ":" + second;
			} else if (StringUtil.isNotEmpty(inYear) && !"null".equals(inYear)
					&& Integer.parseInt(inYear) < Integer.parseInt(year)) {
				// 不为现在年时
				if(Integer.parseInt(inYear) < 1000 && Integer.parseInt(inYear) > 99){
					inYear = "0"+inYear;
				}
				if(Integer.parseInt(inYear) < 100 && Integer.parseInt(inYear) > 0){
					inYear = "00"+inYear;
				}
				inStoreYear = inYear;
				inYear += "-12-31 23:59:59";
			} else if (Integer.parseInt(inYear) > Integer.parseInt(year)) {
				// 大于现在年时，应在前台做js判断，暂时写为0
				inYear = "0";
			}
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, inYear);
			pstmt.setInt(2, (curPageNum - 1) * pageSize);
			pstmt.setInt(3, pageSize);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String QCBM = rs.getString("QCBM");
				String keeper = rs.getString("keeper");
				String productName = rs.getString("productName");
				String manufacturer = rs.getString("manufacturer");
				String measureUnit = rs.getString("measureUnit");
				double productPrice = rs.getDouble("productPrice");
				Date operateTime = rs.getDate("operateTime");
				String productModel = rs.getString("productModel");
				String producedDate = rs.getString("producedDate");
				String storageTime = rs.getString("storageTime");
				String remark = rs.getString("remark");
				String productId = rs.getString("productId");
				String operateType = rs.getString("operateType");

				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("productModel", productModel);
				list.put("QCBM", QCBM);
				list.put("productName", productName);
				list.put("manufacturer", manufacturer);
				list.put("measureUnit", measureUnit);
				list.put("productPrice", productPrice);
				list.put("producedDate", producedDate);
				list.put("operateTime", operateTime);
				list.put("inYear", inStoreYear);
				list.put("storageTime", storageTime);
				list.put("remark", remark);
				list.put("keeper", keeper);
				list.put("productId", productId);
				list.put("operateType", operateType);

				if (version.equals("2")) {
					list.put("ownedUnit", rs.getString("ownedUnit"));
				} else if (version.equals("3")) {
					String jds = selectJDS(rs.getString("ownedUnit"));
					list.put("ownedUnit", rs.getString("ownedUnit"));
					list.put("jds", jds);
				} else if (version.equals("4")) {
					String jds = selectJDS(rs.getString("ownedUnit"));
					String jdj = selectJDJ(jds);
					list.put("ownedUnit", rs.getString("ownedUnit"));
					list.put("jds", jds);
					list.put("jdj", jdj);
				}
				T.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt);
		}
		return T;
	}

	public int selectEquipmentDetailCount(HashMap<String, String> condition,
			String version) throws Exception {
		int count = 0;

		String inYear = condition.get("inYear");
		// 获取当前日期
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 + "";
		String day = cal.get(Calendar.DATE) + "";
		String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
		String minute = cal.get(Calendar.MINUTE) + "";
		String second = cal.get(Calendar.SECOND) + "";

		StringBuffer sql = new StringBuffer("SELECT count(*)")
				.append(" FROM")
				.append(" (SELECT b.productId,b.operateType,b.operateTime,c.productName,c.productModel,c.manufacturer,")
				.append("c.measureUnit,c.productPrice,c.producedDate,c.storageTime,c.keeper,c.remark,d.QCBM,c.ownedUnit")
				.append(" FROM (SELECT * FROM qy_account a WHERE a.operateTime<? ORDER BY a.operateTime DESC) b,qy_product c,qy_basedata d")
				.append(" WHERE b.productId = c.productId AND c.PMNM = d.PMNM AND b.ownedUnit = c.ownedUnit");
		if (StringUtil.isNotEmpty(condition.get("QCBM"))) {
			sql.append(" AND d.QCBM REGEXP '" + condition.get("QCBM") + "'");
		}
		if (StringUtil.isNotEmpty(condition.get("productName"))) {
			sql.append(" AND c.productName REGEXP '"
					+ condition.get("productName") + "'");
		}
		if (version.equals("1")) {
			if (StringUtil.isNotEmpty(condition.get("keeper"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit ='" + condition.get("keeper") + "'");
			}
		} else if (version.equals("2")) {
			if (StringUtil.isNotEmpty(condition.get("keeper"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit IN(" + condition.get("keeper") + ")");
			}
		} else if (version.equals("3")) {
			if (StringUtil.isNotEmpty(condition.get("keeper"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit IN(" + condition.get("keeper") + ")");
			}
		}
		sql.append(" GROUP BY b.productId) e");
		sql.append(" WHERE e.operateType REGEXP '入库'");

		if (StringUtil.isEmpty(inYear) || "null".equals(inYear)
				|| year.equals(inYear) || "/*/*".equals(inYear)) {
			// 为现在年时
			inYear = year + "-" + month + "-" + day + " " + hour + ":" + minute
					+ ":" + second;
		} else if (StringUtil.isNotEmpty(inYear) && !"null".equals(inYear)
				&& Integer.parseInt(inYear) < Integer.parseInt(year)) {
			// 不为现在年时
			if(Integer.parseInt(inYear) < 1000 && Integer.parseInt(inYear) > 99){
				inYear = "0"+inYear;
			}
			if(Integer.parseInt(inYear) < 100 && Integer.parseInt(inYear) > 0){
				inYear = "00"+inYear;
			}
			inYear += "-12-31 23:59:59";
		} else if (Integer.parseInt(inYear) > Integer.parseInt(year)) {
			return 0;
		}
		try {
			Connection conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, inYear);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count += rs.getInt("count(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(null, this.pstmt);
		}

		return count;
	}

	/**
	 * @author austin 实力统计→器材明细账统计→展开明细
	 * */
	public List<String> select() throws Exception {
		List<String> T = new ArrayList<String>();
		return T;
	}

	/*
	 * 查询产品维护信息，只返回需要维护的产品 values中的信息包含前端传过来的产品型号、型号/单元、承制单位、操作类型、剩余存放天数和
	 * 维护剩余天数、机号
	 */
	public ArrayList<HashMap<String, Object>> selectMaintainDetail(
			List<String> condition) {

		ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("select P.productModel,P.productUnit,I.batch,I.deviceNo,P.productPrice,"
					+ "I.num,P.productType,P.storageTime,P.restKeepTime,P.manufacturer,"
					+ "P.keeper,P.maintainCycle,P.latestMaintainTime,A.operateTime,"
					+ "P.productId from qy_account as A,qy_inproductrelation as R,"
					+ "qy_product as P,qy_inApply as I where A.operateType = '新入库' and "
					+ "P.contractId = I.contractId and A.productId = P.productId and I.inId = R.inId"
					+ " and P.productId = R.productId and P.maintainCycle is not NULL "
					+ "and P.latestMaintainTime is not null");
			int useful = 0;// 有效字段条数
			boolean[] use = new boolean[7];// 7个字段各自是否有效的标记
			for (int i = 0; i < 7; i++) {
				if (condition.get(i) != "") {
					useful++;
					use[i] = true;
				}
			}
			// 下面开始构造查询语句
			if (useful != 0) {

				if (use[0])
					sql.append(" and P.productModel regexp '"
							+ condition.get(0) + "'");
				if (use[1])
					sql.append(" and P.productUnit regexp '" + condition.get(1)
							+ "'");
				if (use[2])
					sql.append(" and P.manufacturer regexp '"
							+ condition.get(2) + "'");
				if (use[3])
					sql.append(" and I.inMeans regexp '" + condition.get(3)
							+ "'");
				// if(use[4])
				// sql.append(" and P.restKeepTime <= " + list.get(4));
				// if(use[5])
				// sql.append(" and P.restMaintainTime <= " + list.get(5));
				if (use[6])
					sql.append(" and I.deviceNo = '" + condition.get(6) + "'");
			}
			this.pstmt = this.conn.prepareStatement(sql.toString());
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String productModel = rs.getString(1);
				String productUnit = rs.getString(2);
				String batch = rs.getString(3);
				String deviceNo = rs.getString(4);
				double price = rs.getDouble(5);
				int num = rs.getInt(6);
				String productType = rs.getString(7);
				String storageTime = rs.getString(8);
				int restKeepTime = rs.getInt(9);
				String manufacturer = rs.getString(10);
				String keeper = rs.getString(11);
				String maintainCycle = rs.getString(12);
				Date latestMaintainTime = MyDateFormat.changeToJavaDate(rs
						.getDate(13));
				Date operateTime = MyDateFormat
						.changeToJavaDate(rs.getDate(14));
				int productId = rs.getInt(15);
				map.put("productModel", productModel);
				if (productUnit != null)
					map.put("productUnit", productUnit);
				else
					map.put("productUnit", "");
				map.put("batch", batch);
				map.put("deviceNo", deviceNo);
				map.put("price", price);
				map.put("num", num + "");
				map.put("productType", productType);
				map.put("storageTime", storageTime);
				map.put("restKeepTime", restKeepTime);
				map.put("manufacturer", manufacturer);
				map.put("keeper", keeper);
				map.put("maintainCycle", maintainCycle);
				map.put("latestMaintainTime", latestMaintainTime);
				map.put("operateTime", operateTime);
				map.put("productId", new Integer(productId));
				T.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return T;
	}

	/*
	 * 查询产品维护信息，只返回需要维护的产品 values中的信息包含前端传过来的产品型号、型号/单元、承制单位、操作类型、剩余存放天数和
	 * 维护剩余天数、机号
	 */
	public int countMaintainNumber(List<String> condition) {
		int count = 0;
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*)"
					+ " from qy_account as A,qy_inproductrelation as R,"
					+ "qy_product as P,qy_inApply as I where A.operateType = '新入库' and "
					+ "P.contractId = I.contractId and A.productId = P.productId and I.inId = R.inId"
					+ " and P.productId = R.productId");
			int useful = 0;// 有效字段条数
			boolean[] use = new boolean[7];// 7个字段各自是否有效的标记
			for (int i = 0; i < 7; i++) {
				if (condition.get(i) != "") {
					useful++;
					use[i] = true;
				}
			}
			// 下面开始构造查询语句
			if (useful != 0) {

				if (use[0])
					sql.append(" and P.productModel regexp '"
							+ condition.get(0) + "'");
				if (use[1])
					sql.append(" and P.productUnit regexp '" + condition.get(1)
							+ "'");
				if (use[2])
					sql.append(" and P.manufacturer regexp '"
							+ condition.get(2) + "'");
				if (use[3])
					sql.append(" and I.inMeans regexp '" + condition.get(3)
							+ "'");
				// if(use[4])
				// sql.append(" and P.restKeepTime <= " + list.get(4));
				// if(use[5])
				// sql.append(" and P.restMaintainTime <= " + list.get(5));
				if (use[6])
					sql.append(" and I.deviceNo = '" + condition.get(6) + "'");
			}
			this.pstmt = this.conn.prepareStatement(sql.toString());
			rs = this.pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return count;
	}

	/*
	 * 查询产品维护信息，只返回需要维护的产品 values中的信息包含前端传过来的产品型号、型号/单元、承制单位、操作类型、剩余存放天数和
	 * 维护剩余天数、机号
	 */
	public ArrayList<HashMap<String, Object>> selectMaintainDetailByPage(
			List<String> condition) {

		ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("select P.productModel,P.productUnit,I.batch,P.deviceNo,P.productPrice,"
					+ "I.num,P.productType,P.storageTime,P.restKeepTime,P.manufacturer,"
					+ "P.keeper,P.maintainCycle,P.latestMaintainTime,A.operateTime,"
					+ "P.productId from qy_account as A,qy_inproductrelation as R,"
					+ "qy_product as P,qy_inApply as I where A.operateType = '新入库' and "
					+ "P.contractId = I.contractId and A.productId = P.productId and I.inId = R.inId"
					+ " and P.productId = R.productId and P.latestMaintainTime is not NULL");
			int useful = 0;// 有效字段条数
			boolean[] use = new boolean[7];// 7个字段各自是否有效的标记
			for (int i = 0; i < 7; i++) {
				if (condition.get(i) != "") {
					useful++;
					use[i] = true;
				}
			}
			// 下面开始构造查询语句
			if (useful != 0) {

				if (use[0])
					sql.append(" and P.productModel regexp '"
							+ condition.get(0) + "'");
				if (use[1])
					sql.append(" and P.productUnit regexp '" + condition.get(1)
							+ "'");
				if (use[2])
					sql.append(" and P.manufacturer regexp '"
							+ condition.get(2) + "'");
				if (use[3])
					sql.append(" and I.inMeans regexp '" + condition.get(3)
							+ "'");
				// if(use[4])
				// sql.append(" and P.restKeepTime <= " + list.get(4));
				// if(use[5])
				// sql.append(" and P.restMaintainTime <= " + list.get(5));
				if (use[6])
					sql.append(" and I.deviceNo = '" + condition.get(6) + "'");
			}
//			sql.append(" LIMIT " + (curPageNum - 1) * pageSize + "," + pageSize
//					+ "");
			this.pstmt = this.conn.prepareStatement(sql.toString());
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String productModel = rs.getString(1);
				String productUnit = rs.getString(2);
				String batch = rs.getString(3);
				String deviceNo = rs.getString(4);
				double price = rs.getDouble(5);
				int num = rs.getInt(6);
				String productType = rs.getString(7);
				String storageTime = rs.getString(8);
				int restKeepTime = rs.getInt(9);
				String manufacturer = rs.getString(10);
				String keeper = rs.getString(11);
				String maintainCycle = rs.getString(12);
				Date latestMaintainTime = MyDateFormat.changeToJavaDate(rs
						.getDate(13));
				Date operateTime = MyDateFormat
						.changeToJavaDate(rs.getDate(14));
				int productId = rs.getInt(15);
				map.put("productModel", productModel);
				if (productUnit != null)
					map.put("productUnit", productUnit);
				else
					map.put("productUnit", "");
				map.put("batch", batch);
				map.put("deviceNo", deviceNo);
				map.put("price", price);
				map.put("num", num + "");
				map.put("productType", productType);
				map.put("storageTime", storageTime);
				map.put("restKeepTime", restKeepTime);
				map.put("manufacturer", manufacturer);
				map.put("keeper", keeper);
				map.put("maintainCycle", maintainCycle);
				map.put("latestMaintainTime", latestMaintainTime);
				map.put("operateTime", operateTime);
				map.put("productId", new Integer(productId));
				T.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return T;
	}

	/**
	 * @author limengxin 业务办理—》新增合同后 在合同中添加产品
	 */
	// 2015.06.12 by liuyutian test pass
	// miss volume and weight
	// 需要后期优化批处理
	public boolean saveProduct(List<Product> products, String ownedUnit) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			// 要进行productCount次插入
			// 新增产品时去掉机号
			String sql = "INSERT INTO qy_product(contractId, productCode,wholeName"
					+ ",productType,productModel,productUnit,measureUnit"
					+ ",productPrice,deliveryTime,latestMaintainTime"
					+ ",manufacturer, keeper,buyer,signTime,"
					+ "restKeepTime,restMaintainTime,PMNM,ownedUnit,productName)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			int i = 0;
			while (i != products.size()) {
				int count = Integer.parseInt(products.get(i).getProductCount());
				if (count > 1) {
					int j = 0;
					while (j != count) {
						setProAttribute(products.get(i),ownedUnit);
						j++;
					}
				} else if (count == 1) {
					setProAttribute(products.get(i),ownedUnit);
				}
				i++;
			}
			int count[] = this.pstmt.executeBatch();
			conn.commit();
			if (count.length > 0) {
				flag = true;
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	private void setProAttribute(Product product, String ownedUnit)
			throws SQLException {
		this.pstmt.setString(1, product.getContractId());
		this.pstmt.setString(2, product.getProductCode());
		this.pstmt.setString(3, product.getWholeName());
		this.pstmt.setString(4, product.getProductType());
		this.pstmt.setString(5, product.getProductModel());
		this.pstmt.setString(6, product.getProductUnit());
		this.pstmt.setString(7, product.getMeasureUnit());
		this.pstmt.setDouble(8, Double.parseDouble(product.getProductPrice()));
		// this.pstmt.setDate(9, null);
		this.pstmt.setTimestamp(9,
				MyDateFormat.changeToSqlDate(product.getDeliveryTime()));
		this.pstmt.setTimestamp(10,
				MyDateFormat.changeToSqlDate(product.getLastMainTainTime()));
		this.pstmt.setString(11, product.getManufacturer());
		this.pstmt.setString(12, product.getKeeper());
		this.pstmt.setString(13, product.getBuyer());
		this.pstmt.setTimestamp(14,
				MyDateFormat.changeToSqlDate(product.getSignTime()));
		this.pstmt.setInt(15, product.getRestKeepTime());
		this.pstmt.setInt(16, product.getRestMaintainTime());
		this.pstmt.setString(17, product.getPMNM());
		// this.pstmt.setString(18, product.getDeviceNo());
		this.pstmt.setString(18, ownedUnit);
		String name = product.getProductName();
		this.pstmt.setString(19, name);
		this.pstmt.addBatch();
	}

	/**
	 * singleApply对应的添加的product 业务办理—》新增合同后 在合同中添加单个产品
	 * 
	 * @author HuangKai
	 * @param product
	 * @return
	 */
	public boolean saveProductInApply(Product product) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			// 要进行productCount次插入
			String sql = "INSERT INTO qy_product(contractId, productCode,productName"
					+ ",productType,productModel,productUnit,measureUnit"
					+ ",productPrice,manufacturer, keeper,signTime,"
					+ "proStatus,PMNM)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, product.getContractId());
			this.pstmt.setString(2, product.getProductCode());
			this.pstmt.setString(3, product.getProductName());
			this.pstmt.setString(4, product.getProductType());
			this.pstmt.setString(5, product.getProductModel());
			this.pstmt.setString(6, product.getProductUnit());
			this.pstmt.setString(7, product.getMeasureUnit());
			this.pstmt.setString(8, product.getProductPrice());
			this.pstmt.setString(9, product.getManufacturer());
			this.pstmt.setString(10, product.getKeeper());
			this.pstmt.setTimestamp(11,
					MyDateFormat.changeToSqlDate(product.getSignTime()));
			this.pstmt.setString(12, product.getProStatus());
			this.pstmt.setString(13, product.getPMNM());
			int count = this.pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	public boolean updateProductCode(String productId, String productCode) {
		try {
			this.conn = DBConnection.getConn();
			String sql = "update qy_product set productCode = ? where productId = ?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, productCode);
			this.pstmt.setString(2, productId);
			int x = this.pstmt.executeUpdate();
			if (x > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
	}

	public boolean setLastMaintainTime(long productId, Timestamp stamp) {
		try {
			this.conn = DBConnection.getConn();
			String sql = "update qy_product set latestMaintainTime = ? where productId = ?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(2, productId);
			this.pstmt.setTimestamp(1, stamp);
			int x = this.pstmt.executeUpdate();
			if (x > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}

	}

	public List<String> getProductIdInApply(String contractId,
			String productModel, String productUnit) {
		List<String> productIds = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = "select productId from qy_product where contractId = ? and "
					+ "productModel = ? and productUnit = ? and proStatus = '未申请'";
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, contractId);
			this.pstmt.setString(2, productModel);
			this.pstmt.setString(3, productUnit);
			ResultSet rs = this.pstmt.executeQuery();
			while (rs.next()) {
				productIds.add(rs.getString(1));
			}
			return productIds;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	/**
	 * 
	 * @param productId
	 * @return
	 */
	public boolean DeleteProductById(long productId) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "DELETE FROM qy_product WHERE productId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, productId);
			int count = pstmt.executeUpdate();
			if (count == 1) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 根据合同编号查询对应产品
	 * 
	 * @param contractId
	 * @return
	 */
	public List<Product> queryProductByContractId(String contractId,
			int curPageNum, int pageSize, String status) {
		List<Product> pros = new ArrayList<Product>();
		try {
			conn = DBConnection.getConn();
			String sql = "";
			if ("all".equals(status)) {
				sql = "select * from qy_product where contractId=? group by productModel,productUnit,productName,proStatus order by productId desc LIMIT ?,?";
			} else {
				sql = "select * from qy_product where contractId=? and proStatus="
						+ status
						+ " group by productModel,productUnit,productName LIMIT ?,?";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			pstmt.setInt(2, (curPageNum - 1) * pageSize);
			pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Product product = new Product();
				product.setContractId(rs.getString("contractId"));
				product.setProductId(rs.getLong("productId"));
				product.setProductCode(rs.getString("productCode"));
				product.setPMNM(rs.getString("PMNM"));
				product.setProductName(rs.getString("productName"));
				product.setProductType(rs.getString("productType"));
				product.setProductModel(rs.getString("productModel"));
				product.setProductUnit(rs.getString("productUnit"));
				product.setMeasureUnit(rs.getString("measureUnit"));
				product.setProductPrice(rs.getString("productPrice"));
				product.setDeliveryTime(rs.getDate("deliveryTime"));
				product.setLastMainTainTime(rs.getDate("latestMaintainTime"));
				product.setManufacturer(rs.getString("manufacturer"));
				product.setKeeper(rs.getString("keeper"));
				product.setBuyer(rs.getString("buyer"));
				product.setSignTime(rs.getDate("signTime"));
				product.setProStatus(rs.getString("proStatus"));
				product.setRestKeepTime(rs.getInt("restKeepTime"));
				product.setRestMaintainTime(rs.getInt("restMaintainTime"));
				pros.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return pros;
	}

	/**
	 * 计算合同对应产品数量
	 * 
	 * @param contractId
	 * @return
	 */
	public List<Integer> queryProNumByContractId(String contractId,
			String status) {
		List<Integer> counts = new ArrayList<Integer>();
		try {
			conn = DBConnection.getConn();
			String sql = "";
			if ("all".equals(status)) {
				sql = "select count(*),proStatus from qy_product where contractId=? group by productModel,productUnit,productName,proStatus;";
			} else {
				sql = "select count(*),proStatus from qy_product where contractId=? and proStatus='"
						+ status
						+ "' group by productModel,productUnit,productName";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int count = rs.getInt("count(*)");
				counts.add(Integer.valueOf(count));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return counts;
	}

	/**
	 * 用于查看所有产品
	 * 
	 * @param contractId
	 * @return
	 */
	public List<HashMap<String, String>> queryProductsByContractId(
			String contractId, int curPageNum, int pageSize, String status) {
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		try {
			conn = DBConnection.getConn();
			String sql = "";
			if ("all".equals(status)) {
				sql = "select wholeName,productName,contractId,productModel,productUnit,productPrice,measureUnit,deliveryTime,keeper,manufacturer,count(*),proStatus from qy_product where contractId=? and proStatus not in ('合同销毁') group by productModel,productUnit,productName,proStatus,productPrice,measureUnit  ORDER BY productId DESC LIMIT ?,?;";
			} else {
				sql = "select wholeName,productName,contractId,productModel,productUnit,productPrice,measureUnit,deliveryTime,keeper,manufacturer,count(*),proStatus from qy_product where contractId=? and proStatus='"
						+ status
						+ "' group by productModel,productUnit,productName,productPrice,measureUnit ORDER BY productId DESC LIMIT ?,?";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			pstmt.setInt(2, (curPageNum - 1) * pageSize);
			pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> result = new HashMap<String, String>();
				String productName = rs.getString("productName");
				String contractid = rs.getString("contractId");
				String productModel = rs.getString("productModel");
				String productUnit = rs.getString("productUnit");
				String productPrice = rs.getString("productPrice");
				String measureUnit = rs.getString("measureUnit");
				String deliveryTime = rs.getString("deliveryTime");
				String keeper = rs.getString("keeper");
				String manufacturer = rs.getString("manufacturer");
				String proStatus = rs.getString("proStatus");
				String wholeName = rs.getString("wholeName");
				int count = rs.getInt("count(*)");
				result.put("count", Integer.toString(count));
				result.put("contractid", contractid);
				result.put("productModel", productModel);
				result.put("productUnit", productUnit);
				result.put("productPrice", productPrice);
				result.put("measureUnit", measureUnit);
				result.put("deliveryTime", deliveryTime);
				result.put("keeper", keeper);
				result.put("manufacturer", manufacturer);
				result.put("proStatus", proStatus);
				result.put("productName", productName);
				result.put("wholeName", wholeName);
				results.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return results;
	}

	/**
	 * 用于新入库使用 根据合同号查询每个产品
	 * 
	 * @param contractId
	 * @return
	 */
	public List<HashMap<String, String>> queryProductDetailByContractId(
			String contractId, int curPageNum, int pageSize, String status) {
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		try {
			conn = DBConnection.getConn();
			String sql = "";
			if ("all".equals(status)) {
				sql = "select productId,PMNM,deviceNo,productName,wholeName,contractId,productModel,productUnit,productPrice,measureUnit,deliveryTime,keeper,manufacturer,proStatus from qy_product where contractId=? LIMIT ?,?;";
			} else {
				sql = "select productId,PMNM,deviceNo,productName,wholeName,contractId,productModel,productUnit,productPrice,measureUnit,deliveryTime,keeper,manufacturer,proStatus from qy_product where contractId=? and proStatus='"
						+ status + "' LIMIT ?,?";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			pstmt.setInt(2, (curPageNum - 1) * pageSize);
			pstmt.setInt(3, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> result = new HashMap<String, String>();
				String productName = rs.getString("productName");
				String contractid = rs.getString("contractId");
				String productModel = rs.getString("productModel");
				String productUnit = rs.getString("productUnit");
				String productPrice = rs.getString("productPrice");
				String measureUnit = rs.getString("measureUnit");
				String deliveryTime = rs.getString("deliveryTime");
				String keeper = rs.getString("keeper");
				String manufacturer = rs.getString("manufacturer");
				String proStatus = rs.getString("proStatus");
				String deviceNo = rs.getString("deviceNo");
				String PMNM = rs.getString("PMNM");
				String wholeName = rs.getString("wholeName");
				String productId = rs.getString("productId");
				int count = 1;
				result.put("count", Integer.toString(count));
				result.put("contractid", contractid);
				result.put("productModel", productModel);
				result.put("productUnit", productUnit);
				result.put("productPrice", productPrice);
				result.put("measureUnit", measureUnit);
				result.put("deliveryTime", deliveryTime);
				result.put("keeper", keeper);
				result.put("manufacturer", manufacturer);
				result.put("proStatus", proStatus);
				result.put("productName", productName);
				result.put("deviceNo", deviceNo);
				result.put("PMNM", PMNM);
				result.put("wholeName", wholeName);
				result.put("productId", productId);
				results.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return results;
	}

	/**
	 * 用于修改产品使用
	 * 
	 * @param product
	 * @return
	 */
	public boolean updateProduct(Product pro) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "UPDATE qy_product SET PMNM=?,productName=?,productModel=?,productUnit=?"
					+ ",productPrice=?,measureUnit=?,deliveryTime =?,keeper=?,manufacturer=? WHERE productId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pro.getPMNM());
			pstmt.setString(2, pro.getProductName());
			pstmt.setString(3, pro.getProductModel());
			pstmt.setString(4, pro.getProductUnit());
			pstmt.setString(5, pro.getProductPrice());
			pstmt.setString(6, pro.getMeasureUnit());
			pstmt.setTimestamp(7,
					MyDateFormat.changeToSqlDate(pro.getDeliveryTime()));
			pstmt.setString(8, pro.getKeeper());
			pstmt.setString(9, pro.getManufacturer());
			pstmt.setLong(10, pro.getProductId());
			int count = pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 
	 * @return
	 */
	public boolean deleteProductById(String pId) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "DELETE FROM qy_product WHERE productId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, Long.parseLong(pId));
			int count = pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * @author huangkai 业务查询→产品明细查询
	 * */
	public Product selectProductDetail(long productId) {
		Product product = new Product();
		try {
			this.conn = DBConnection.getConn();
			String sql = "select * from qy_product where productId = ?;";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, productId);
			this.rs = this.pstmt.executeQuery();
			if (rs.next()) {
				product.setProductId(productId);
				product.setContractId(rs.getString("contractId"));
				product.setProductCode(rs.getString("productCode"));
				product.setPMNM(rs.getString("PMNM"));
				product.setProductName(rs.getString("productName"));
				product.setProductType(rs.getString("productType"));
				product.setProductModel(rs.getString("productModel"));
				product.setProductUnit(rs.getString("productUnit"));
				product.setMeasureUnit(rs.getString("measureUnit"));
				product.setProductPrice(rs.getString("productPrice"));
				product.setDeliveryTime(rs.getDate("deliveryTime"));
				product.setLastMainTainTime(rs.getDate("latestMaintainTime"));
				product.setManufacturer(rs.getString("manufacturer"));
				product.setKeeper(rs.getString("keeper"));
				product.setBuyer(rs.getString("buyer"));
				product.setSignTime(rs.getDate("signTime"));
				product.setProStatus(rs.getString("proStatus"));
				product.setRestKeepTime(rs.getInt("restKeepTime"));
				product.setRestMaintainTime(rs.getInt("restMaintainTime"));
			}
			return product;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
	}

	/**
	 * author limengxin
	 * 
	 * @param productId
	 * @return
	 */
	public boolean updateProductStatuc(long productId, String status) {
		try {
			this.conn = DBConnection.getConn();
			String sql = "update qy_product set proStatus = ? where productId = ?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, status);
			this.pstmt.setLong(2, productId);
			int x = this.pstmt.executeUpdate();
			if (x > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
	}

	/**
	 * author huangkai
	 * 
	 * @param productId
	 * @return
	 */
	public boolean updateProductProStatuc(String productId) {
		try {
			this.conn = DBConnection.getConn();
			String sql = "update qy_product set proStatus = '进库待审核' where productId = ?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, productId);
			int x = this.pstmt.executeUpdate();
			if (x > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
	}

	/**
	 * 根据合同编号、产品型号和产品单元查询对应产品
	 * 
	 * @param contractId
	 * @return
	 */
	public List<String> queryProductByContractIdProductModelAndProductUnit(
			String contractId, String productModel, String productUnit) {
		List<String> results = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT productName,"
					+ "measureUnit,productPrice,PMNM,manufacturer,keeper,productCode"
					+ " FROM qy_product"
					+ " WHERE contractId = ? AND productModel = ? AND productUnit = ?"
					+ " AND proStatus = '未申请' GROUP BY productCode";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			this.pstmt.setString(2, productModel);
			this.pstmt.setString(3, productUnit);
			rs = pstmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				if (count == 0) {
					results.add(rs.getString("productName"));
					results.add(productModel);
					results.add(productUnit);
					results.add(rs.getString("measureUnit"));
					results.add(rs.getDouble("productPrice") + "");
					results.add(rs.getString("PMNM"));
					results.add(rs.getString("manufacturer"));
					results.add(rs.getString("keeper"));
				}
				results.add(rs.getString("productCode"));
				count++;
			}
			results.add(count + "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return results;
	}

	/**
	 * 根据品名内码、产品型号和产品单元查询对应产品
	 * 
	 * @param pmnm
	 *            ,pname,punit
	 * @return
	 */
	public List<String> queryDeviceNoInByThreeP(String pmnm,
			String productName, String productUnit) {
		List<String> results = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_product"
					+ " WHERE PMNM = ? AND productName = ? AND productUnit = ?"
					+ " AND proStatus REGEXP '^[已].{0,}入库$'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pmnm);
			this.pstmt.setString(2, productName);
			this.pstmt.setString(3, productUnit);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				results.add(rs.getString("deviceNo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return results;
	}

	/**
	 * 根据品名内码、产品型号和产品单元查询对应产品
	 * 
	 * @param pmnm
	 *            ,pname,punit
	 * @return
	 */
	public List<String> queryDeviceNoApplyByThreeP(String pmnm,
			String productName, String productUnit) {
		List<String> results = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_product"
					+ " WHERE PMNM = ? AND productName = ? AND productUnit = ?"
					+ " AND proStatus = '未申请'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pmnm);
			this.pstmt.setString(2, productName);
			this.pstmt.setString(3, productUnit);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				results.add(rs.getString("deviceNo"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return results;
	}

	/**
	 * 轮换管理中对产品ID的查询
	 * 
	 * @param
	 * @author limengxin
	 */
	public List<Integer> queryProductID(Map<String, String> condition) {
		// List<Contract> result = new ArrayList<Contract>();
		List<Integer> ids = new ArrayList<Integer>();
		try {
			conn = (Connection) DBConnection.getConn();
			// 疑问：
			// 在轮换和更新 对产品查询时 相当于每条记录的主键是 产品ID+申请ID 而不是对应到每个产品 而申请入库是要对应到每个产品的
			// 所以这个sql语句用了DISTINCT
			// 解决方案 是在查询产品的页面 就只查产品表中的属性 不级联 申请表查 这样查出来的就是产品了
			StringBuffer sql = new StringBuffer(
					"select DISTINCT P.productId from qy_product as P,qy_inapply as I ,qy_inproductrelation as R"
							+ " where P.productId=R.productId and R.inId=I.inId ");

			// 设置查询sql条件

			if (condition.size() > 0) {
				// sql.append(" where ");
				// int count = 0;
				for (String key : condition.keySet()) {
					if (!"".equals(condition.get(key))
							&& condition.get(key) != null) {

						sql.append(" and " + key + "= '" + condition.get(key)
								+ "'");
						// count ++;

					}
				}
			}
			// sql.append(" group by P.productModel,P.productUnit,I.deviceNo,P.productPrice,P.measureUnit,I.producedDate,P.restKeepTime,I.maintainCycle,P.manufacturer,P.keeper,P.contractId,P.proStatus ");
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("productId");

				ids.add(new Integer(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return ids;
	}

	/**
	 * 轮换管理中对产品的查询
	 * 
	 * @param
	 * @author limengxin
	 */
	public List<HashMap<String, Object>> queryProductBorrow(
			Map<String, String> condition, int curPageNum, int pageSize) {
		// List<Contract> result = new ArrayList<Contract>();
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		try {
			conn = (Connection) DBConnection.getConn();
			// StringBuffer sql = new
			// StringBuffer("select * from qy_contract,qy_product");
			// select
			// P.productModel,P.productUnit,I.deviceNo,P.productPrice,count(*),P.measureUnit,I.producedDate,P.restKeepTime,I.maintainCycle,P.manufacturer,P.keeper,P.contractId,P.proStatus
			// from qy_product as P,qy_inapply as I where
			// P.contractId=I.contractId and P.productCode=I.productCode group
			// by productModel,productUnit,contractId;
			StringBuffer sql = new StringBuffer(
					"select P.productModel,P.productUnit,I.deviceNo,"
							+ "P.productPrice,count(DISTINCT P.productId),P.measureUnit,I.producedDate,P.restKeepTime,I.maintainCycle,"
							+ "P.manufacturer,P.keeper,P.contractId,P.proStatus  from qy_product as P,qy_inapply as I ,qy_inproductrelation as R"
							+ " where P.productId=R.productId and R.inId=I.inId  ");
			// +"where P.contractId=I.contractId and P.productCode=I.productCode ");
			StringBuffer sql2 = new StringBuffer(
					"select count(distinct P.productId) from qy_product as P,qy_inapply as I "
							+ "where P.contractId=I.contractId and P.productCode=I.productCode ");
			// 设置查询sql条件
			if (condition.size() > 0) {
				// sql.append(" where");
				for (String key : condition.keySet()) {
					if (!"".equals(condition.get(key))
							&& condition.get(key) != null) {
						sql.append(" and " + key + " REGEXP '"
								+ condition.get(key) + "'");
						sql2.append(" and " + key + " REGEXP '"
								+ condition.get(key) + "'");
					}
				}
			}

			sql.append(" group by P.productModel,P.productUnit,I.deviceNo,P.productPrice,P.measureUnit,I.producedDate,P.restKeepTime,I.maintainCycle,P.manufacturer,P.keeper,P.contractId,P.proStatus limit ?,?");

			pstmt = conn.prepareStatement(sql.toString());
			PreparedStatement p2 = conn.prepareStatement(sql2.toString());
			pstmt.setInt(1, (curPageNum - 1) * pageSize);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			ResultSet rs2 = p2.executeQuery();
			while (rs.next()) {

				// 一个哈希List 表示查询出来的一条记录
				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("productModel", rs.getString("productModel"));
				list.put("productUnit", rs.getString("productUnit"));
				list.put("deviceNo", rs.getString("deviceNo"));
				list.put("productPrice", rs.getDouble("productPrice"));
				list.put("count", rs.getInt("count(DISTINCT P.productId)"));
				list.put("measureUnit", rs.getString("measureUnit"));
				list.put("producedDate", rs.getDate("producedDate"));
				list.put("restKeepTime", rs.getInt("restKeepTime"));
				list.put("maintainCycle", rs.getString("maintainCycle"));
				list.put("manufacturer", rs.getString("manufacturer"));
				list.put("keeper", rs.getString("keeper"));
				list.put("contractId", rs.getString("contractId"));
				list.put("proStatus", rs.getString("proStatus"));
				// 一个哈希List 表示查询出来的一条记录
				// 一个T存储了整个查询结果 一个表
				// System.out.println(list.toString());
				T.add(list);
			}
			if (rs2.next()) {
				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("sum", rs2.getInt(1));
				T.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return T;
	}

	/**
	 * 分页显示所有的按设备统计查询结果 这是查外面的
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> productQueryStatistic(
			Map<String, String> parameter) throws Exception {
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		String companyStr = parameter.get("keeper");
		String sql = "SELECT a.productName,a.productModel,a.productUnit,a.productPrice,COUNT(*) AS productNum,a.manufacturer,a.keeper FROM qy_product a"
				+ " WHERE a.proStatus NOT IN (?,?,?) AND a.productModel REGEXP ? AND a.productUnit REGEXP ? AND a.ownedUnit IN("
				+ companyStr
				+ ")"
				+ " GROUP BY a.productModel,a.productUnit LIMIT ?,?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "未到库");
			pstmt.setString(2, "未申请");
			pstmt.setString(3, "合同销毁");
			pstmt.setString(4, parameter.get("productModel") == "" ? "/*/*"
					: "/*" + parameter.get("productModel") + "/*");
			pstmt.setString(5, parameter.get("productUnit") == "" ? "/*/*"
					: "/*" + parameter.get("productUnit") + "/*");
			int curPageNum = Integer.parseInt(parameter.get("curPageNum"));
			int pageSize = Integer.parseInt(parameter.get("pageSize"));
			pstmt.setInt(6, (curPageNum - 1) * pageSize);
			pstmt.setInt(7, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ResultSetMetaData rmd = rs.getMetaData();
				Map<String, String> result = new HashMap<String, String>();
				for (int i = 1; i <= rmd.getColumnCount(); i++) {
					result.put(rmd.getColumnName(i), rs.getString(i));
				}
				T.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return T;
	}

	/**
	 * 分页显示所有的按设备统计查询结果 这是查里面的
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> productOperationDetail(
			String productModel, String productUnit) {
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		String sql = "SELECT a.contractId,a.productName,a.productModel,a.productUnit,a.productPrice,SUM(a.productNum) AS productNum,SUM(IFNULL(b.productNum,0)) AS productInNum,SUM(IFNULL(c.productNum,0)) AS productOutNum,a.manufacturer,a.keeper,d.signDate,d.contractPrice,d.totalNumber"
				+ " FROM temp_all_product_nostatus a"
				+ " LEFT JOIN qy_contract d ON a.contractId=d.contractId"
				+ " LEFT JOIN temp_in_product b ON a.operateTime=b.operateTime AND a.productModel=b.productModel AND a.productUnit=b.productUnit"
				+ " LEFT JOIN temp_out_product c ON a.operateTime=c.operateTime AND a.productModel=c.productModel AND a.productUnit=c.productUnit"
				+ " WHERE a.productModel=? AND a.productUnit=?"
				+ " GROUP BY a.contractId";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productModel);
			pstmt.setString(2, productUnit);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ResultSetMetaData rmd = rs.getMetaData();
				Map<String, String> result = new HashMap<String, String>();
				for (int i = 1; i <= rmd.getColumnCount(); i++) {
					result.put(rmd.getColumnName(i), rs.getString(i));
				}
				T.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return T;
	}

	/**
	 * 通过合同Id查询产品条数
	 * 
	 * @return
	 */
	public int getSumByContractId(String contractId, String status) {
		int count = 0;
		try {
			conn = DBConnection.getConn();
			String sql = "";
			if ("all".equals(status)) {
				sql = "select count(*) from qy_product p where p.contractId=? group by productModel,productUnit,productName,proStatus";
			} else {
				sql = "select count(*) from qy_product p where p.contractId=? and proStatus='"
						+ status
						+ "' group by productModel,productUnit,productName";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			rs.last();
			count = rs.getRow();
			rs.beforeFirst();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return count;
	}

	/**
	 * 通过合同Id查询产品条数 不group by 用于新入库
	 * 
	 * @return
	 */
	public int getDetailSumByContractId(String contractId, String status) {
		int count = 0;
		try {
			conn = DBConnection.getConn();
			String sql = "";
			if ("all".equals(status)) {
				sql = "select count(*) from qy_product p where p.contractId=?";
			} else {
				sql = "select count(*) from qy_product p where p.contractId=? and proStatus='"
						+ status + "'";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return count;
	}

	/**
	 * 通过inId查询产品
	 * 
	 * @return
	 */
	public Product getProByInId(long inId) {
		Product pro = new Product();
		long pid = 0;
		try {
			conn = DBConnection.getConn();
			String sql = "select * from qy_inproductrelation where inId =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, inId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pid = rs.getLong("productId");
				pro = this.getProById(pid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return pro;
	}

	/**
	 * 根据产品id查询产品
	 * 
	 * @param pid
	 * @return
	 */
	public Product getProById(long pid) {
		Product pro = new Product();
		try {
			conn = DBConnection.getConn();
			String sql = "select * from qy_product where productId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pro.setProductId(rs.getLong("productId"));
				pro.setBuyer(rs.getString("buyer"));
				pro.setContractId(rs.getString("contractid"));
				pro.setDeliveryTime(rs.getDate("deliveryTime"));
				pro.setKeeper(rs.getString("keeper"));
				pro.setLastMainTainTime(rs.getDate("latestMaintainTime"));
				pro.setManufacturer(rs.getString("manufacturer"));
				pro.setMeasureUnit(rs.getString("measureUnit"));
				pro.setPMNM(rs.getString("PMNM"));
				pro.setProductPrice(rs.getString("productPrice"));
				pro.setProductCode(rs.getString("productCode"));
				pro.setProductId(rs.getLong("productId"));
				pro.setProductModel(rs.getString("productModel"));
				pro.setProductName(rs.getString("productName"));
				pro.setProductType(rs.getString("productType"));
				pro.setProductUnit(rs.getString("productUnit"));
				pro.setProStatus(rs.getString("proStatus"));
				pro.setRestKeepTime(rs.getInt("restKeepTime"));
				pro.setRestMaintainTime(rs.getInt("restMaintainTime"));
				pro.setSignTime(rs.getDate("signTime"));
				pro.setLocation(rs.getString("location"));
				pro.setProducedDate(rs.getString("producedDate"));
				pro.setMaintainCycle(rs.getString("maintainCycle"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return pro;
	}

	private Product setPro(ResultSet rs) throws SQLException {
		Product pro = new Product();
		pro.setProductId(rs.getLong("productId"));
		pro.setBuyer(rs.getString("buyer"));
		pro.setContractId(rs.getString("contractid"));
		pro.setDeliveryTime(rs.getDate("deliveryTime"));
		pro.setKeeper(rs.getString("keeper"));
		pro.setLastMainTainTime(rs.getDate("latestMaintainTime"));
		pro.setManufacturer(rs.getString("manufacturer"));
		pro.setMeasureUnit(rs.getString("measureUnit"));
		pro.setPMNM(rs.getString("PMNM"));
		pro.setProductPrice(rs.getString("productPrice"));
		pro.setProductCode(rs.getString("productCode"));
		pro.setProductId(rs.getLong("productId"));
		pro.setProductModel(rs.getString("productModel"));
		pro.setProductName(rs.getString("productName"));
		pro.setProductType(rs.getString("productType"));
		pro.setProductUnit(rs.getString("productUnit"));
		pro.setProStatus(rs.getString("proStatus"));
		pro.setRestKeepTime(rs.getInt("restKeepTime"));
		pro.setRestMaintainTime(rs.getInt("restMaintainTime"));
		pro.setSignTime(rs.getDate("signTime"));
		pro.setLocation(rs.getString("location"));
		pro.setProducedDate(rs.getString("producedDate"));
		pro.setMaintainCycle(rs.getString("maintainCycle"));
		pro.setWholeName(rs.getString("wholeName"));
		pro.setStorageTime(rs.getString("storageTime"));
		return pro;
	}

	public long getSum_maintainQuery(List<String> condition) {
		long sum_maintainQuery = 0;
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("select P.productModel,P.productUnit,I.batch,I.deviceNo,P.productPrice,"
					+ "I.num,P.productType,I.storageTime,P.restKeepTime,P.manufacturer,"
					+ "I.keeper,I.maintainCycle,P.latestMaintainTime,A.operateTime,"
					+ "P.productId from qy_account as A,qy_inproductrelation as R,"
					+ "qy_product as P,qy_inApply as I where A.operateType = '新入库' and "
					+ "P.contractId = I.contractId and A.productId = P.productId and I.inId = R.inId"
					+ " and P.productId = R.productId");
			int useful = 0;// 有效字段条数
			boolean[] use = new boolean[7];// 7个字段各自是否有效的标记
			for (int i = 0; i < 7; i++) {
				if (condition.get(i) != "") {
					useful++;
					use[i] = true;
				}
			}
			// 下面开始构造查询语句
			if (useful != 0) {

				if (use[0])
					sql.append(" and P.productModel regexp '"
							+ condition.get(0) + "'");
				if (use[1])
					sql.append(" and P.productUnit regexp '" + condition.get(1)
							+ "'");
				if (use[2])
					sql.append(" and P.manufacturer regexp '"
							+ condition.get(2) + "'");
				if (use[3])
					sql.append(" and I.inMeans regexp '" + condition.get(3)
							+ "'");
				// if(use[4])
				// sql.append(" and P.restKeepTime <= " + list.get(4));
				// if(use[5])
				// sql.append(" and P.restMaintainTime <= " + list.get(5));
				if (use[6])
					sql.append(" and I.deviceNo = '" + condition.get(6) + "'");
			}
			this.pstmt = this.conn.prepareStatement(sql.toString());
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				sum_maintainQuery++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return sum_maintainQuery;
	}

	/**
	 * 改变产品状态
	 * 
	 * @param pId
	 * @return
	 */
	public boolean changeProStatus(String[] pId, String[] status) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			String sql = "Update qy_product set proStatus=? where productId = ?";
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < pId.length; i++) {
				pstmt.setString(1, status[i]);
				pstmt.setString(2, pId[i]);
				pstmt.addBatch();
			}
			int[] count = pstmt.executeBatch();
			conn.commit();
			if (count.length > 0) {
				flag = true;
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 根据发料单查询出库产品
	 * 
	 * @param conditions
	 * @return
	 */
	public List<List<Product>> getOutPros(List<Map<String, String>> conditions) {
		List<List<Product>> pros = new ArrayList<List<Product>>();
		try {
			conn = DBConnection.getConn();
			for (Map<String, String> condition : conditions) {
				StringBuffer sql = new StringBuffer("Select * from qy_product");
				List<Product> pps = new ArrayList<Product>();
				if (condition.size() > 0) {
					for (String key : condition.keySet()) {
						if (!"".equals(condition.get(key))
								&& condition.get(key) != null) {
							if ("productPrice".equals(key)) {
								sql.append(" and " + key + "="
										+ condition.get(key));
							} else if ("realNum".equals(key)) {

							} else
								sql.append(" and " + key + "= '"
										+ condition.get(key) + "'");
						}
					}
				}
				pstmt = conn.prepareStatement(sql.toString().replaceFirst(
						"and", "where"));
				rs = pstmt.executeQuery();
				int count = 0;
				int real = Integer.parseInt(condition.get("realNum"));
				while (rs.next() && count != real) {
					Product pro = new Product();
					pro = setPro(rs);
					long pId = pro.getProductId();
					String locationSql = "select location from qy_inproductrelation as i,qy_inapply as a where i.inId = a.inId and i.productId=?";
					pstmt = conn.prepareStatement(locationSql);
					pstmt.setLong(1, pId);
					ResultSet rs1 = pstmt.executeQuery();
					// 因为前台不需要使用productType，用它来暂时存放location
					if (rs1.next()) {
						pro.setProductType(rs1.getString(1));
					}
					if (!pro.isEmpty(pro)) {
						pps.add(pro);
					}
					if (rs1 != null) {
						rs1.close();
					}
					count++;
				}
				if (pps.size() > 0) {
					pros.add(pps);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}

		return pros;
	}

	/**
	 * 根据轮换出入库申请，向器材明细账插入记录
	 * 
	 * @return
	 */
	public void insertEquipmentDetailAccount(
			List<ArrayList<String>> dyadicArray, String flg) {
		int inIdIndex = 0;
		int inId = 0;
		String sql_1 = null;
		String sql_2 = null;
		Long productId = null;
		String execdata = null;
		String year = null;
		String month = null;
		String day = null;
		int allInstoreNumber = 0;
		Map<String, Double[]> result = new HashMap<String, Double[]>();// key值是由在库年份，名称型号，生产厂家，单价，器材代码，料单编号,产品单价等组成，value值中存放的分别为收入数量，发出数量，结存数量，重量，体积
		Map<String, String> forInsert = new HashMap<String, String>();
		ArrayList<String> firstRow = dyadicArray.get(0);
		int firstRowLen = firstRow.size();
		for (int i = 0; i < firstRowLen; i++) {
			if ("inId".equals(firstRow.get(i))) {
				inIdIndex = i;
			}
		}
		int len = dyadicArray.size();
		// 注意，这里应该从1开始，因为第一行是标题
		for (int i = 1; i < len; i++) {
			inId = Integer.parseInt(dyadicArray.get(i).get(inIdIndex));
		}
		if ("inApply".equals(flg)) {
			sql_1 = "select productId,execDate from qy_inproductrelation inner join qy_inapply on qy_inproductrelation.inId=qy_inapply.inId and qy_inapply.inId=? and qy_inproductrelation.inId=?";
		}
		if ("outApply".equals(flg)) {
			sql_1 = "select productId,execDate from qy_outproductrelation inner join qy_outapply on qy_outproductrelation.listId=qy_outapply.outId and qy_outapply.outId=? and qy_outproductrelation.listId=?";
		}

		try {
			pstmt = conn.prepareStatement(sql_1);
			pstmt.setLong(1, inId);
			pstmt.setLong(2, inId);
			rs = pstmt.executeQuery();
			if (rs.isAfterLast() == rs.isBeforeFirst()) {
				return;
			} else {
				while (rs.next()) {
					productId = rs.getLong("productId");
					execdata = String.valueOf(rs.getDate("execDate"));
				}
			}
			// System.out.println("productId............."+execdata);
			String[] data = execdata.split("-");
			year = data[0];
			month = data[1];
			day = data[2].split(" ")[0];
			rs.close();
			pstmt.close();

			sql_2 = "select deliveryTime,productModel,productPrice,PMNM,manufacturer,proStatus,weight,volume from qy_product where productId=? and deliveryTime is not null and productModel is not null and productPrice is not null "
					+ "and PMNM is not null and manufacturer is not null and proStatus is not null and weight is not null and volume is not null";
			pstmt = conn.prepareStatement(sql_2);
			pstmt.setString(1, String.valueOf(productId));
			// System.out.println("sql.................."+sql_2);
			rs = pstmt.executeQuery();
			if (rs.isAfterLast() == rs.isBeforeFirst()) {// 判断rs里面的ResultSet是否为空。
				return;
			} else {
				while (rs.next()) {
					Calendar calenda = Calendar.getInstance();
					int currentYear = calenda.get(Calendar.YEAR);
					String deliveryTime = rs.getString("deliveryTime");
					String deliveryYear = deliveryTime.split("-")[0];
					int instoreTime = currentYear
							- Integer.valueOf(deliveryYear);
					String proStatus = rs.getString("proStatus");
					String keys = String.valueOf(instoreTime) + "	"
							+ rs.getString("productModel") + "	"
							+ rs.getString("manufacturer") + "	"
							+ rs.getString("PMNM") + "	" + String.valueOf(inId)
							+ "	" + rs.getString("productPrice");
					if (!result.containsKey(keys)) {
						Double[] arr = new Double[5];
						arr[3] = rs.getDouble("weight");
						arr[4] = rs.getDouble("volume");
						if (proStatus.equals("已入库")
								|| proStatus.equals("轮换出库待审核")
								|| proStatus.equals("轮换已入库")
								|| proStatus.equals("轮换出库未通过")
								|| proStatus.equals("更新出库待审核")
								|| proStatus.equals("更新已入库")
								|| proStatus.equals("更新出库未通过")
								|| proStatus.equals("出库待审核")
								|| proStatus.equals("出库未通过")
								&& !proStatus.equals("合同销毁")) {
							arr[2] = 1.0;
							arr[1] = 0.0;
							arr[0] = 1.0;
						}
						if (proStatus.equals("未申请")
								|| proStatus.equals("进库待审核")
								|| proStatus.equals("进库未通过")
								|| proStatus.equals("轮换入库待审核")
								|| proStatus.equals("轮换已出库")
								|| proStatus.equals("轮换库未通过")
								|| proStatus.equals("更新进库待审核")
								|| proStatus.equals("更新已出库")
								|| proStatus.equals("更新进库未通过")
								|| proStatus.equals("已出库")
								&& !proStatus.equals("合同销毁")) {
							arr[0] = 1.0;
							arr[1] = 1.0;
							arr[2] = 0.0;
						}
						result.put(keys, arr);
					} else {
						Double[] array = result.get(keys);
						array[3] += rs.getDouble("weight");
						array[4] += rs.getDouble("volume");
						array[0] += 1.0;
						if (proStatus.equals("已入库")
								|| proStatus.equals("轮换出库待审核")
								|| proStatus.equals("轮换已入库")
								|| proStatus.equals("轮换出库未通过")
								|| proStatus.equals("更新出库待审核")
								|| proStatus.equals("更新已入库")
								|| proStatus.equals("更新出库未通过")
								|| proStatus.equals("出库待审核")
								|| proStatus.equals("出库未通过")
								&& !proStatus.equals("合同销毁")) {
							array[2] += 1.0;
						}
						if (proStatus.equals("未申请")
								|| proStatus.equals("进库待审核")
								|| proStatus.equals("进库未通过")
								|| proStatus.equals("轮换入库待审核")
								|| proStatus.equals("轮换已出库")
								|| proStatus.equals("轮换库未通过")
								|| proStatus.equals("更新进库待审核")
								|| proStatus.equals("更新已出库")
								|| proStatus.equals("更新进库未通过")
								|| proStatus.equals("已出库")
								&& !proStatus.equals("合同销毁")) {
							array[1] += 1.0;
						}
						result.put(keys, array);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (String info : result.keySet()) {
			allInstoreNumber += result.get(info)[2]; // 统计结存总数量。

		}
		for (String info : result.keySet()) {
			Double[] data = result.get(info);
			String[] cont = info.split("	");
			forInsert.put("instoreYear", cont[0]);
			forInsert.put("productModel", cont[1]);
			forInsert.put("manufacturer", cont[2]);
			forInsert.put("PMNM", cont[3]);
			forInsert.put("listId", cont[4]);
			forInsert.put("productPrice", cont[5]);
			forInsert.put("allInstoreNumber", String.valueOf(allInstoreNumber));
			forInsert.put("allIn", String.valueOf(data[0]));
			forInsert.put("Out", String.valueOf(data[1]));
			forInsert.put("balance", String.valueOf(data[2]));
			forInsert.put("allWeight", String.valueOf(data[3]));
			forInsert.put("allVolume", String.valueOf(data[4]));
		}
		try {
			rs.close();
			pstmt.close();
			String sql_3 = "insert into qy_equipmentdetail(instoreYear,productModel,packageNumbers,volumes,weights,manufacturers,productPrices,packageDescriptions,matchingInstructions,PMNMs,years,months,days,listIds,synopsises,balanceQuantitys,incomes,outs,balances,remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql_3);

			pstmt.setString(1, forInsert.get("instoreYear"));
			pstmt.setString(2, forInsert.get("productModel"));
			pstmt.setInt(3, 2);
			pstmt.setDouble(4, Double.valueOf(forInsert.get("allVolume")));
			pstmt.setDouble(5, Double.valueOf(forInsert.get("allWeight")));
			pstmt.setString(6, forInsert.get("manufacturer"));
			System.out.println("manufacturer..........."
					+ forInsert.get("manufacturer"));
			pstmt.setString(7, forInsert.get("productPrice"));
			System.out.println("productPrice..........."
					+ Double.valueOf(forInsert.get("productPrice")));
			pstmt.setString(8, "包装说明");
			pstmt.setString(9, "配套说明");
			pstmt.setString(10, forInsert.get("PMNM"));
			System.out.println("PMNM..........." + forInsert.get("PMNM"));
			pstmt.setString(11, year);
			pstmt.setString(12, month);
			pstmt.setString(13, day);
			pstmt.setLong(14, Long.valueOf(forInsert.get("listId")));
			pstmt.setString(15, "内容摘要");
			pstmt.setInt(16, Integer.valueOf(forInsert.get("allInstoreNumber")));
			System.out.println("allInstoreNumber..........."
					+ Integer.valueOf(forInsert.get("allInstoreNumber")));
			pstmt.setDouble(17, Double.valueOf(forInsert.get("allIn")));
			System.out.println("allIn..........."
					+ Double.valueOf(forInsert.get("allIn")));
			pstmt.setDouble(18, Double.valueOf(forInsert.get("Out")));
			System.out.println("Out..........."
					+ Double.valueOf(forInsert.get("Out")));
			pstmt.setDouble(19, Double.valueOf(forInsert.get("balance")));
			System.out.println("balance..........."
					+ Double.valueOf(forInsert.get("balance")));
			pstmt.setString(20, "备注");
			// System.out.println("sql.................."+sql_3);
			pstmt.addBatch();
			pstmt.executeBatch();
			// conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(this.conn, this.pstmt);
		}

	}

	/**
	 * @author austin 实力统计→器材明细账统计
	 * */
	public List<HashMap<String, Object>> selectEquipmentDetailAccount(
			HashMap<String, String> condition, int curPageNum, int pageSize,
			String version) throws Exception {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();// 返回值

		String inYear = (String) condition.get("inYear");
		String inStoreYear = new String();
		// 获取当前日期
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 + "";
		String day = cal.get(Calendar.DATE) + "";
		String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
		String minute = cal.get(Calendar.MINUTE) + "";
		String second = cal.get(Calendar.SECOND) + "";

		try {
			StringBuffer sql = new StringBuffer(
					"SELECT e.productName,e.productModel,e.manufacturer,e.productPrice,")
					.append("e.QCBM,e. BZJS ,e.BZTJ*count(*) ,e.BZZL*count(*),count(*) ")
					.append(" FROM")
					.append(" (SELECT c.productName,c.productModel,c.manufacturer,")
					.append("c.productPrice,d.QCBM,d. BZJS,d.BZTJ,d.BZZL,b.operateType")
					.append(" FROM (SELECT * FROM qy_account a WHERE a.operateTime<? ORDER BY a.operateTime DESC) b,qy_product c,qy_basedata d")
					.append(" WHERE b.productId = c.productId AND c.PMNM = d.PMNM AND b.ownedUnit = c.ownedUnit");

			if (StringUtil.isNotEmpty((String) (condition.get("QCBM")))) {
				sql.append(" AND d.QCBM REGEXP '" + condition.get("QCBM") + "'");
			}
			if (StringUtil.isNotEmpty((String) (condition.get("productName")))) {
				sql.append(" AND c.productName REGEXP '"
						+ condition.get("productName") + "'");
			}
			if (StringUtil.isNotEmpty((String) (condition.get("productModel")))) {
				sql.append(" AND c.productModel REGEXP '"
						+ condition.get("productModel") + "'");
			}
			if (version.equals("1")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit ='" + condition.get("keeper")
							+ "'");
				}
			} else if (version.equals("2")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			} else if (version.equals("3")) {
				if (StringUtil.isNotEmpty(condition.get("keeper"))) {
					sql.append(" and");
					sql.append(" c.ownedUnit IN(" + condition.get("keeper")
							+ ")");
				}
			}
			sql.append(" GROUP BY b.productId) e");
			sql.append(" WHERE e.operateType REGEXP '入库'");
			sql.append(" GROUP BY e.productName,e.productModel,e.manufacturer,e.productPrice,e.QCBM,e. BZJS,e.BZTJ,e.BZZL");
			sql.append(" LIMIT ?,?");
			if (StringUtil.isEmpty(inYear) || "null".equals(inYear)
					|| year.equals(inYear) || "/*/*".equals(inYear)) {
				// 为现在年时
				inStoreYear = year;
				inYear = year + "-" + month + "-" + day + " " + hour + ":"
						+ minute + ":" + second;
			} else if (StringUtil.isNotEmpty(inYear) && !"null".equals(inYear)
					&& Integer.parseInt(inYear) < Integer.parseInt(year)) {
				// 不为现在年时
				if(Integer.parseInt(inYear) < 1000 && Integer.parseInt(inYear) > 99){
					inYear = "0"+inYear;
				}
				if(Integer.parseInt(inYear) < 100 && Integer.parseInt(inYear) > 0){
					inYear = "00"+inYear;
				}
				inStoreYear = inYear;
				inYear += "-12-31 23:59:59";
			} else if (Integer.parseInt(inYear) > Integer.parseInt(year)) {
				// 大于现在年时，应在前台做js判断，暂时写为0
				inYear = "0";
			}
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, inYear);
			pstmt.setInt(2, (curPageNum - 1) * pageSize);
			pstmt.setInt(3, pageSize);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String productName = rs.getString("productName");
				String productModel = rs.getString("productModel");
				String manufacturer = rs.getString("manufacturer");
				double productPrice = rs.getDouble("productPrice");
				String QCBM = rs.getString("QCBM");
				String BZJS = rs.getString("BZJS");
				String BZTJ = rs.getString("e.BZTJ*count(*)");// 最终结果需*数量
				String BZZL = rs.getString("e.BZZL*count(*)");// 最终结果需*数量
				int count = rs.getInt("count(*)");

				HashMap<String, Object> list = new HashMap<String, Object>();
				list.put("inYear", inStoreYear);
				list.put("productName", productName);
				list.put("productModel", productModel);
				list.put("manufacturer", manufacturer);
				list.put("productPrice", productPrice);
				list.put("QCBM", QCBM);
				list.put("BZJS", BZJS);
				list.put("BZTJ", BZTJ);
				list.put("BZZL", BZZL);
				list.put("count", count);

				T.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return T;
	}

	/**
	 * 器材明细账数量统计
	 */
	public int selectEquipmentDetailAccountCount(
			HashMap<String, String> condition, String version) {
		int count = 0;
		String inYear = (String) condition.get("inYear");
		// 获取当前日期
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		String month = cal.get(Calendar.MONTH) + 1 + "";
		String day = cal.get(Calendar.DATE) + "";
		String hour = cal.get(Calendar.HOUR_OF_DAY) + "";
		String minute = cal.get(Calendar.MINUTE) + "";
		String second = cal.get(Calendar.SECOND) + "";

		StringBuffer sql = new StringBuffer("SELECT count(*)")
				.append(" FROM")
				.append(" (SELECT e.productName,e.productModel,e.manufacturer,e.productPrice,e.QCBM,")
				.append("e. BZJS ,e.BZTJ*count(*) ,e.BZZL*count(*),count(*) ")
				.append(" FROM")
				.append(" (SELECT c.productName,c.productModel,c.manufacturer,c.productPrice,d.QCBM,d. BZJS,d.BZTJ,d.BZZL,b.operateType")
				.append(" FROM (SELECT * FROM qy_account a WHERE a.operateTime<? ORDER BY a.operateTime DESC) b,qy_product c,qy_basedata d")
				.append(" WHERE b.productId = c.productId AND c.PMNM = d.PMNM AND b.ownedUnit = c.ownedUnit");
		if (StringUtil.isNotEmpty((String) (condition.get("QCBM")))
				&& !"null".equals((String) (condition.get("QCBM")))) {
			sql.append(" AND d.QCBM REGEXP '" + condition.get("QCBM") + "'");
		}
		if (StringUtil.isNotEmpty((String) (condition.get("productName")))
				&& !"null".equals((String) (condition.get("productName")))) {
			sql.append(" AND c.productName REGEXP '"
					+ condition.get("productName") + "'");
		}
		if (StringUtil.isNotEmpty((String) (condition.get("productModel")))) {
			sql.append(" AND c.productModel REGEXP '"
					+ condition.get("productModel") + "'");
		}
		if (version.equals("1")) {
			if (StringUtil.isNotEmpty(condition.get("keeper"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit ='" + condition.get("keeper") + "'");
			}
		} else if (version.equals("2")) {
			if (StringUtil.isNotEmpty(condition.get("keeper"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit IN(" + condition.get("keeper") + ")");
			}
		} else if (version.equals("3")) {
			if (StringUtil.isNotEmpty(condition.get("keeper"))) {
				sql.append(" and");
				sql.append(" c.ownedUnit IN(" + condition.get("keeper") + ")");
			}
		}
		sql.append(" GROUP BY b.productId) e");
		sql.append(" WHERE e.operateType REGEXP '入库'");
		sql.append(" GROUP BY e.productName,e.productModel,e.manufacturer,e.productPrice,e.QCBM,e. BZJS,e.BZTJ,e.BZZL)  f");
		if (StringUtil.isEmpty(inYear) || "null".equals(inYear)
				|| year.equals(inYear) || "/*/*".equals(inYear)) {
			// 为现在年时
			inYear = year + "-" + month + "-" + day + " " + hour + ":" + minute
					+ ":" + second;
		} else if (StringUtil.isNotEmpty(inYear) && !"null".equals(inYear)
				&& Integer.parseInt(inYear) < Integer.parseInt(year)) {
			// 不为现在年时
			if(Integer.parseInt(inYear) < 1000 && Integer.parseInt(inYear) > 99){
				inYear = "0"+inYear;
			}
			if(Integer.parseInt(inYear) < 100 && Integer.parseInt(inYear) > 0){
				inYear = "00"+inYear;
			}
			inYear += "-12-31 23:59:59";
		} else if (Integer.parseInt(inYear) > Integer.parseInt(year)) {
			return 0;
		}
		try {
			this.conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, inYear);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				count += rs.getInt("count(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}

		return count;
	}

	/**
	 * 查询料单信息 查询发料单号
	 */
	public List<HashMap<String, String>> selectOutList(
			HashMap<String, String> condition) throws Exception {
		List<HashMap<String, String>> T = new ArrayList<HashMap<String, String>>();// 返回值
		String productName = condition.get("productName");
		String productModel = condition.get("productModel");
		String manufacturer = condition.get("manufacturer");
		String productPrice = condition.get("productPrice");
		String QCBM = condition.get("QCBM");

		StringBuffer sql = new StringBuffer(
				"SELECT DISTINCT c.listId,a.keeper FROM qy_product a")
				.append(" INNER JOIN qy_basedata b")
				.append(" ON a.PMNM=b.PMNM")
				.append(" INNER JOIN qy_outlistproductrelation c")
				.append(" ON a.productId=c.productId")
				.append(" WHERE a.productName =? AND a.productModel =? AND a.manufacturer =? AND a.productPrice =? AND b.QCBM =?");

		try {
			this.conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, productName);
			pstmt.setString(2, productModel);
			pstmt.setString(3, manufacturer);
			pstmt.setString(4, productPrice);
			pstmt.setString(5, QCBM);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String listId = rs.getString("listId");
				String keeper = rs.getString("keeper");

				HashMap<String, String> account = new HashMap<String, String>();
				account.put("listId", listId);
				account.put("keeper", keeper);
				T.add(account);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}

		return T;
	}

	/**
	 * 查询料单信息 查询发料单信息
	 */
	public List<HashMap<String, String>> selectOutListMessage(
			List<HashMap<String, String>> T, ArrayList<String> listId)
			throws Exception {
		StringBuffer sql = new StringBuffer(
				"SELECT a.listId,a.date,a.askCount,a.remark FROM qy_outlist a");

		if (listId.size() != 0) {
			sql.append(" WHERE a.listId IN (");
			for (int i = 0; i < listId.size(); i++) {
				if (i != listId.size() - 1)
					sql.append("'" + listId.get(i) + "',");
				else
					sql.append("'" + listId.get(i) + "')");
			}
		}

		// System.out.println(sql.toString());
		try {
			this.conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next() && i < T.size()) {
				String date = rs.getDate("date") + "";
				int askCount = rs.getInt("askCount");
				String remark = rs.getString("remark");
				String[] info = date.split("-");
				// 当天出库的产品不加时分秒就无法查询为出库
				date += " 23:59:59";

				T.get(i).put("date", date);
				T.get(i).put("year", info[0]);
				T.get(i).put("month", info[1]);
				T.get(i).put("day", info[2]);
				T.get(i).put("out", askCount + "");
				T.get(i).put("remark", remark);

				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}

		return T;
	}

	/**
	 * 查询料单信息 统计在库数量
	 */
	public List<HashMap<String, String>> selectEquipmentDetailAccountNum(
			List<HashMap<String, String>> T, HashMap<String, String> condition) {
		String sql = "CALL num(?,?,?,?,?,?)";
		try {
			this.conn = DBConnection.getConn();
			for (int i = 0; i < T.size(); i++) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, T.get(i).get("date"));
				pstmt.setString(2, condition.get("productName"));
				pstmt.setString(3, condition.get("productModel"));
				pstmt.setString(4, condition.get("manufacturer"));
				pstmt.setString(5, condition.get("productPrice"));
				pstmt.setString(6, condition.get("QCBM"));
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					int count = rs.getInt("count(*)");
					T.get(i).put("rest", count + "");
					T.get(i).put("num", count + "");
					T.get(i).put("income", 0 + "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return T;
	}

	/**
	 * 根据企业查询军代室
	 * */
	public String selectJDS(String qy) {
		String jds = new String();
		String sql = "select ownedJdsName from companyinfo where companyName=?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, qy);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				jds = rs.getString("ownedJdsName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return jds;
	}

	/**
	 * 根据军代室查询军代局
	 * */
	public String selectJDJ(String jds) {
		String jdj = new String();
		String sql = "select ownedJdjName from jdsinfo where jdsName=?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, jds);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				jdj = rs.getString("ownedJdjName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return jdj;
	}

	/**
	 * 根据军代局查询指挥局
	 * */
	public String selectZHJ(String jdj) {
		String zhj = new String();
		String sql = "select ownedZhjName from jdjinfo where jdjName=?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, jdj);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				zhj = rs.getString("ownedZhjName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return zhj;
	}

	/**
	 * 检验在product表中是否存在指定的deviceNo
	 * 
	 * @param deviceNo
	 * @return exist or not
	 * @author LiangYH
	 */
	public boolean checkByDeviceNo(String deviceNo) {
		String sql = "Select deviceNo From qy_product Where deviceNo=?";

		boolean flag = false;

		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deviceNo);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return flag;
	}

	/**
	 * 通过申请表查询产品
	 * 
	 * @param inId
	 * @return
	 */
	public List<Product> findProsByInId(String inId) {
		List<Product> pros = new ArrayList<Product>();
		try {
			conn = DBConnection.getConn();
			String sql = "select * from qy_product as p,qy_inproductrelation as i where p.productId=i.productId and i.inId=? group by p.productId";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Product pro = new Product();
				pro = setProduct(rs);
				pros.add(pro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return pros;
	}

	private Product setProduct(ResultSet rs) throws SQLException {
		Product pro = new Product();
		pro.setContractId(rs.getString("contractId"));
		pro.setProductId(rs.getLong("productId"));
		pro.setProductCode(rs.getString("productCode"));
		pro.setPMNM(rs.getString("PMNM"));
		pro.setProductName(rs.getString("productName"));
		pro.setProductType(rs.getString("productType"));
		pro.setProductModel(rs.getString("productModel"));
		pro.setProductUnit(rs.getString("productUnit"));
		pro.setMeasureUnit(rs.getString("measureUnit"));
		pro.setProductPrice(rs.getString("productPrice"));
		pro.setDeliveryTime(rs.getDate("deliveryTime"));
		pro.setLastMainTainTime(rs.getDate("latestMaintainTime"));
		pro.setManufacturer(rs.getString("manufacturer"));
		pro.setKeeper(rs.getString("keeper"));
		pro.setBuyer(rs.getString("buyer"));
		pro.setSignTime(rs.getDate("signTime"));
		pro.setProStatus(rs.getString("proStatus"));
		pro.setRestKeepTime(rs.getInt("restKeepTime"));
		pro.setRestMaintainTime(rs.getInt("restMaintainTime"));
		pro.setDeviceNo(rs.getString("deviceNo"));
		pro.setLocation(rs.getString("location"));
		pro.setStorageTime(rs.getString("storageTime"));
		pro.setProducedDate(rs.getString("producedDate"));
		pro.setSignTime(rs.getDate("signTime"));
		pro.setBorrowLength(rs.getInt("borrowLength"));
		pro.setBorrowReason(rs.getString("borrowReason"));
		pro.setRemark(rs.getString("remark"));
		pro.setWholeName(rs.getString("wholeName"));
		pro.setMaintainCycle(rs.getString("maintainCycle"));
		pro.setOldPrice(rs.getString("oldPrice"));
		return pro;
	}

	/**
	 * 通过申请表查询出库产品
	 * 
	 * @param outId
	 * @return
	 */
	public List<Product> findProsByOutId(String outId) {
		List<Product> pros = new ArrayList<Product>();
		try {
			conn = DBConnection.getConn();
			String sql = "select * from qy_product as p,qy_outproductrelation as o where p.productId=o.productId and o.outId=?";
			// select p.*,oa.oldPrice,oa.oldNum from qy_product as
			// p,qy_outproductrelation as o,qy_outapply as oa where
			// p.productId=o.productId and o.outId=oa.outId and o.outId=?
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, outId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Product pro = new Product();
				pro = setoutProduct(rs);
				pros.add(pro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return pros;
	}

	/**
	 * 通过申请表查询产品原始价格
	 * 
	 * @param outId
	 * @return
	 */
	public List<HashMap<String, String>> findOldPrice(String outId) {
		List<HashMap<String, String>> productPrice = new ArrayList<HashMap<String, String>>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT p.*,b.productPrice as oprice FROM qy_product AS p "
					+ "INNER JOIN qy_outproductrelation AS o "
					+ "ON p.productId=o.productId "
					+ "INNER JOIN qy_product b "
					+ "ON p.otherProductId = b.productId " + "WHERE o.outId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, outId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				ResultSetMetaData metaData = rs.getMetaData();
				int len2 = metaData.getColumnCount();
				for (int k = 1; k <= len2; k++) {
					map.put(metaData.getColumnName(k), rs.getString(k));
					if (k==35)
						map.put("oldprice", rs.getString(k));
				}
				
				productPrice.add(map);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return productPrice;
	}

	private Product setoutProduct(ResultSet rs) throws SQLException {
		Product pro = new Product();
		pro.setContractId(rs.getString("contractId"));
		pro.setProductId(rs.getLong("productId"));
		pro.setProductCode(rs.getString("productCode"));
		pro.setPMNM(rs.getString("PMNM"));
		pro.setProductName(rs.getString("productName"));
		pro.setProductType(rs.getString("productType"));
		pro.setProductModel(rs.getString("productModel"));
		pro.setProductUnit(rs.getString("productUnit"));
		pro.setMeasureUnit(rs.getString("measureUnit"));
		pro.setProductPrice(rs.getString("productPrice"));
		pro.setDeliveryTime(rs.getDate("deliveryTime"));
		pro.setLastMainTainTime(rs.getDate("latestMaintainTime"));
		pro.setManufacturer(rs.getString("manufacturer"));
		pro.setKeeper(rs.getString("keeper"));
		pro.setBuyer(rs.getString("buyer"));
		pro.setSignTime(rs.getDate("signTime"));
		pro.setProStatus(rs.getString("proStatus"));
		pro.setRestKeepTime(rs.getInt("restKeepTime"));
		pro.setRestMaintainTime(rs.getInt("restMaintainTime"));
		pro.setDeviceNo(rs.getString("deviceNo"));
		pro.setLocation(rs.getString("location"));
		pro.setProducedDate(rs.getString("producedDate"));
		pro.setSignTime(rs.getDate("signTime"));
		pro.setBorrowLength(rs.getInt("borrowLength"));
		pro.setBorrowReason(rs.getString("borrowReason"));
		pro.setRemark(rs.getString("remark"));
		pro.setWholeName(rs.getString("wholeName"));
		pro.setMaintainCycle(rs.getString("maintainCycle"));
		pro.setStorageTime(rs.getString("storageTime"));
		/*
		 * double alloldprice; int oldnum;
		 * if(!"".equals(rs.getString("oldPrice")) && rs.getString("oldPrice")
		 * != null) { alloldprice =
		 * Double.parseDouble(rs.getString("oldPrice")); oldnum =
		 * Integer.parseInt(rs.getString("oldnum")); }
		 * 
		 * double oldprice = alloldprice / oldnum;
		 */
		pro.setOldPrice(rs.getString("productPrice"));
		return pro;
	}

	/**
	 * 检查产品机号是否唯一
	 * 
	 * @param deviceNo
	 * @return
	 */
	public boolean isDeviceNoExist(String deviceNo) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_product WHERE deviceNo =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deviceNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return flag;
	}

	/**
	 * 通过机号查询产品信息 主要用于自动填写申请表
	 * 
	 * @param condition
	 * @return
	 */
	public Product findProductyNo(String deviceNo, boolean isIn) {
		Product pro = new Product();
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"SELECT * FROM qy_product WHERE deviceNo=?");
			if (isIn) {
				sql.append(" and proStatus REGEXP '^[已]|入库$'");
			} else {
				sql.append(" and proStatus REGEXP '^[已]|出库$'");
			}
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, deviceNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pro = setPro(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return pro;
	}

	/**
	 * 通过机号查询产品信息 主要用于自动填写新入库
	 * 
	 * @param condition
	 * @return
	 */
	public Product findProductyNo(String deviceNo) {
		Product pro = new Product();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_product WHERE deviceNo=? and proStatus=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deviceNo);
			pstmt.setString(2, "未申请");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pro = setPro(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return pro;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> findDeviceByContract(String contractId) {
		List<String> deviceNo = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_product WHERE contractId=? AND proStatus='未申请'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String temp = rs.getString("deviceNo");
				deviceNo.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return deviceNo;
	}

	public boolean updateProductWhenInApply(String devideNo, String location,
			String producedDate, String storageTime, String remark,
			String wholeName, String maintianCycle, String proStatus,
			Date latestMaintainTime, String productCode) {
		String sql = "update qy_product set location = ?, producedDate = ?, storageTime = ?, remark = ?,"
				+ "wholeName = ?, maintainCycle = ?, productCode = ?, proStatus = ?,latestMaintainTime = ?"
				+ " where deviceNo = ?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, location);
			pstmt.setString(2, producedDate);
			pstmt.setString(3, storageTime);
			pstmt.setString(4, remark);
			pstmt.setString(5, wholeName);
			pstmt.setString(6, maintianCycle);
			pstmt.setString(7, productCode);
			pstmt.setString(8, proStatus);
			pstmt.setTimestamp(9,
					MyDateFormat.changeToSqlDate(latestMaintainTime));
			pstmt.setString(10, devideNo);

			int i = pstmt.executeUpdate();
			System.out.println(sql);
			if (i > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	/**
	 * 根据机号查产品id，返回一个product类，但是只包含在查同种添加产品时拥有的产品数据，不能查出入库时添加的产品信息
	 * 
	 * @param deviceNo
	 * @return
	 */
	public int queryProductIdByDeviceNoandProuctModel(String deviceNo,
			String productModel) {
		String sql = "select productId from qy_product where deviceNo = ? and productModel = ?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deviceNo);
			pstmt.setString(2, productModel);
			this.rs = this.pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
			else
				return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	/**
	 * 查找所有机号
	 * 
	 * @return
	 */
	public List<String> findAllInDeviceNo(boolean isIn) {
		List<String> result = new ArrayList<String>();
		String sql = "SELECT *FROM qy_product WHERE proStatus REGEXP ?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			if (isIn) {
				pstmt.setString(1, "^已.{0,}入库$");
			} else {
				pstmt.setString(1, "^已.{0,}出库$");
			}
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				String temp = rs.getString("deviceNo");
				result.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}

	/**
	 * 军代室查找代储企业对应在库产品
	 * 
	 * @return
	 */
	public List<Map<String, Object>> findAllPmnmByKeeper(String keeper,
			int curPageNum) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// String sql =
		// "SELECT * FROM qy_product WHERE ownedUnit =? AND proStatus REGEXP '^[已].{0,}入库$' ORDER BY productModel,productUnit  LIMIT ?,?";
		String sql = "SELECT * FROM qy_product WHERE ownedUnit =? AND proStatus REGEXP '^[已].{0,}入库$' ORDER BY productModel,productUnit";

		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keeper);
			// pstmt.setInt(2, (curPageNum - 1) * 10);
			// pstmt.setInt(3, 10);
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Map<String, Object> productMap = new HashMap<String, Object>();
				ResultSetMetaData metaData = rs.getMetaData();
				int len2 = metaData.getColumnCount();
				for (int k = 1; k <= len2; k++) {
					if ("productId".equals(metaData.getColumnName(k))) {
						// 用于service 手动group by后拼接id
						List<String> ids = new ArrayList<String>();
						ids.add(rs.getString(k));
						productMap.put(metaData.getColumnName(k), ids);
					} else
						productMap.put(metaData.getColumnName(k),
								rs.getString(k));
				}
				// 将结果返回后，在service手动处理数量
				productMap.put("count", 1);
				result.add(productMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}

	/**
	 * 查询具有这个品名内码的产品数量
	 * 
	 * @param PMNM
	 * @return
	 */
	public Map<String, String> getCountByPMNM(Map<String, String> paramMap) {
		String sql = "Select count(*),productPrice From qy_product Where PMNM=? And productModel=? And measureUnit=? And proStatus REGEXP '^[已].{0,}入库$'";
		Map<String, String> result = new HashMap<String, String>();
		int count = -1;
		String price = "";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paramMap.get("PMNM"));
			pstmt.setString(2, paramMap.get("productModel"));
			pstmt.setString(3, paramMap.get("measureUnit"));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
				price = rs.getString(2);
				result.put("count", Integer.toString(count));
				result.put("price", price);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}

	/**
	 * 查询产品出入库信息
	 * 
	 * @param condition
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 */
	public List<HashMap<String, Object>> queryInOut(
			HashMap<String, String> condition, int curPageNum, int pageSize) {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		String productId = condition.get("productId");
		String deviceNo = condition.get("deviceNo");
		String type = condition.get("type");
		String in_sql = "SELECT qy_inapply.inMeans,qy_inapply.chStatus, qy_inproductrelation.insertTime "
				+ "FROM qy_inapply , qy_inproductrelation WHERE qy_inapply.inId = qy_inproductrelation.inId AND qy_inproductrelation.productId='"
				+ productId
				+ "'"
				+ "LIMIT "
				+ (curPageNum - 1)
				* pageSize
				+ "," + pageSize + "";
		String out_sql = "SELECT qy_outapply.outMeans,qy_outapply.chStatus, qy_outproductrelation.insertTime "
				+ "FROM qy_outapply , qy_outproductrelation WHERE qy_outapply.outId = qy_outproductrelation.outId AND qy_outproductrelation.productId='"
				+ productId
				+ "'"
				+ "UNION "
				+ "SELECT qy_outlist.outMeans, qy_outlist.listId,qy_outlistproductrelation.insertTime "
				+ "FROM qy_outlist, qy_outlistproductrelation WHERE qy_outlist.listId = qy_outlistproductrelation.listId AND qy_outlistproductrelation.productId='"
				+ productId
				+ "'"
				+ "LIMIT "
				+ (curPageNum - 1)
				* pageSize
				+ "," + pageSize + "";
		Connection conn = null;
		// PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConn();
			if (type.equalsIgnoreCase("inApply")) {
				pstmt = conn.prepareStatement(in_sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> inmap = new HashMap<String, Object>();
					inmap.put("deviceNo", deviceNo);
					inmap.put("inMeans", rs.getString("inMeans"));
					inmap.put("insertTime", rs.getString("insertTime"));
					inmap.put("chStatus", rs.getString("chStatus"));
					T.add(inmap);
				}
			} else if (type.equalsIgnoreCase("outApply")) {
				pstmt = conn.prepareStatement(out_sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					HashMap<String, Object> outmap = new HashMap<String, Object>();
					// outmap.put("deviceNo", deviceNo);
					outmap.put("deviceNo", deviceNo);
					outmap.put("outMeans", rs.getString("outMeans"));
					outmap.put("insertTime", rs.getString("insertTime"));
					if (rs.getString("chStatus").indexOf("审核") > 0)
						outmap.put("chStatus", rs.getString("chStatus"));
					else
						outmap.put("chStatus", "已发料出库");
					T.add(outmap);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return T;
	}

	/**
	 * 查询产品出入信息总条目
	 * 
	 * @param condition
	 * @return
	 */
	public int queryInOutSum(HashMap<String, String> condition) {
		int sum = 0;
		String productId = condition.get("productId");
		// String deviceNo = condition.get("deviceNo");
		String type = condition.get("type");

		String in_sql = "SELECT qy_inapply.inMeans,qy_inapply.chStatus, qy_inproductrelation.insertTime "
				+ "FROM qy_inapply , qy_inproductrelation WHERE qy_inapply.inId = qy_inproductrelation.inId AND qy_inproductrelation.productId='"
				+ productId + "'";
		String out_sql = "SELECT qy_outapply.outMeans,qy_outapply.chStatus, qy_outproductrelation.insertTime "
				+ "FROM qy_outapply , qy_outproductrelation WHERE qy_outapply.outId = qy_outproductrelation.outId AND qy_outproductrelation.productId='"
				+ productId
				+ "'"
				+ "UNION "
				+ "SELECT qy_outlist.outMeans, qy_outlist.listId,qy_outlistproductrelation.insertTime "
				+ "FROM qy_outlist, qy_outlistproductrelation WHERE qy_outlist.listId = qy_outlistproductrelation.listId AND qy_outlistproductrelation.productId='"
				+ productId + "'";
		Connection conn = null;
		// PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConn();
			if (type.equalsIgnoreCase("inApply")) {
				pstmt = conn.prepareStatement(in_sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					sum++;
				}
			} else if (type.equalsIgnoreCase("outApply")) {
				pstmt = conn.prepareStatement(out_sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					sum++;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return sum;
	}

	/**
	 * 查看特定的产品编码是否已经存在
	 * 
	 * @param productModel
	 * @param deviceNos
	 * @return
	 */
	public boolean checkDeviceNoAlreadyExist(String productModel,
			String[] deviceNos) {
		String sql = "select * from qy_product where productModel = ? and "
				+ "deviceNo in ('test'";
		for (int i = 0; i < deviceNos.length; i++) {
			sql = sql + ",'" + deviceNos[i] + "'";
		}
		sql += ")";
		System.out.println(sql);
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productModel);
			rs = pstmt.executeQuery();
			if (rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return false;
	}

	/**
	 * 根据productId批量存储机号
	 * 
	 * @param productIds
	 * @param deviceNos
	 * @return
	 */
	public boolean storeDeviceNoBatch(int[] productIds, String[] deviceNos) {
		String sql = "update qy_product set deviceNo = ?, proStatus = '未申请' where productId = ?";
		int res[] = new int[productIds.length];
		if (productIds.length != deviceNos.length)
			return false;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < productIds.length; i++) {
				pstmt.setString(1, deviceNos[i]);
				pstmt.setInt(2, productIds[i]);
				pstmt.addBatch();
			}
			res = pstmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		for (int i = 0; i < res.length; i++)
			if (res[i] == 0)
				return false;
		return true;
	}

	/*
	 * 根据条件找出指定数量个productId，用于更新机号
	 */
	public ArrayList<Integer> getProductIdsByContractIdProductModelProductNameAndProductUnit(
			String contractId, String productModel, String productUnit,
			String productName, int number) {
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (number == 0)
			return list;
		// edit by liuyutian 20151209
		//去掉productUnit
		String sql = "select productId from qy_product where contractId = ? and productModel = ?"
				+ " and productName = ? and proStatus = '未到库' limit ?";
		System.out.println("contractId:" + contractId);
		System.out.println("productModel:" + productModel);
		System.out.println("productUnit:" + productUnit);
		System.out.println("productName:" + productName);
		System.out.println("limit:" + number);
		try {
			conn = DBConnection.getConn();
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, contractId);
			this.pstmt.setString(2, productModel);
			//this.pstmt.setString(3, productUnit);
			this.pstmt.setString(3, productName);
			this.pstmt.setInt(4, number);
			System.out.println("sql:" + sql);
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return list;
	}

	/**
	 * 根据产品id查找
	 * 
	 * @param productID
	 * @return 该product在数据库中的字段名和值
	 */
	public Map<String, String> selectProductByID(int productID) {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "Select * From qy_product Where productId=" + productID;

		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				ResultSetMetaData metaData = rs.getMetaData();
				int len = metaData.getColumnCount();
				for (int i = 1; i <= len; i++) {
					map.put(metaData.getCatalogName(i), rs.getString(i));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return map;
	}

	/**
	 * 查询产品
	 * 
	 * @param productStatus
	 * @param flag
	 * @return
	 * @author LiangYH
	 */
	public List<Map<String, String>> selectProduct(String productStatus,
			String flag, String curPageNum, String pageSize,
			Map<String, String> condition) {

		PreparedStatement selectCountPs = null;
		ResultSet selectCountRs = null;

		List<Map<String, String>> targetList = new ArrayList<Map<String, String>>();

		// 查询产品表
		StringBuffer selectProductSql = new StringBuffer();
		// 查询产品总页数
		StringBuffer selectProductCountSql = new StringBuffer();
		if (condition.size() == 0) {
			// 此处是没有condition的情况
			if ("轮换出库".equals(productStatus)) {
				// selectCountSql =
				// "Select count(*) from qy_product Where status REGEXP '轮换.{0,}出库' And flag="
				// + flag;
				selectProductSql
						.append("Select p.productId,o.batch,p.productName,p.productModel,p.productUnit,p.deviceNo"
								+ ",p.productPrice,o.outMeans,i.insertTime,p.PMNM,p.measureUnit,p.storageTime,"
								+ "p.location,p.maintainCycle,p.manufacturer,p.keeper,p.remark"
								+ " from qy_product p"
								+ " inner join qy_outproductrelation i"
								+ " on p.productId = i.productId"
								+ " inner join qy_outapply o"
								+ " on i.outId = o.outId"
								+ " Where p.status REGEXP '轮换.{0,}出库' And p.flag=1"
								+ " order by i.insertTime desc");
				selectProductCountSql.append("Select count(*)"
						+ " from qy_product p"
						+ " inner join qy_outproductrelation i"
						+ " on p.productId = i.productId"
						+ " inner join qy_outapply o" + " on i.outId = o.outId"
						+ " Where p.status REGEXP '轮换.{0,}出库' And p.flag=1");
				selectProductSql.append(" Limit "
						+ (Integer.parseInt(curPageNum) - 1)
						* Integer.parseInt(pageSize) + "," + pageSize);
			} else if ("更新出库".equals(productStatus)) {
				// selectCountSql =
				// "Select count(*) from qy_product Where status REGEXP '更新.{0,}出库' And flag="
				// + flag;
				selectProductSql
						.append("Select p.productId,o.batch,p.productName,p.productModel,p.productUnit,p.deviceNo"
								+ ",p.productPrice,o.outMeans,i.insertTime,p.PMNM,p.measureUnit,p.storageTime,"
								+ "p.location,p.maintainCycle,p.manufacturer,p.keeper,p.remark"
								+ " from qy_product p"
								+ " inner join qy_outproductrelation i"
								+ " on p.productId = i.productId"
								+ " inner join qy_outapply o"
								+ " on i.outId = o.outId"
								+ " Where p.status REGEXP '更新.{0,}出库' And p.flag=1"
								+ " order by i.insertTime desc");
				selectProductCountSql.append("Select count(*)"
						+ " from qy_product p"
						+ " inner join qy_outproductrelation i"
						+ " on p.productId = i.productId"
						+ " inner join qy_outapply o" + " on i.outId = o.outId"
						+ " Where p.status REGEXP '更新.{0,}出库' And p.flag=1");
				selectProductSql.append(" Limit "
						+ (Integer.parseInt(curPageNum) - 1)
						* Integer.parseInt(pageSize) + "," + pageSize);
			}
		} else {
			// 此处是有condition的情况
			if ("轮换出库".equals(productStatus)) {
				selectProductSql
						.append("Select p.productId,o.batch,p.productName,p.productModel,p.productUnit,p.deviceNo"
								+ ",p.productPrice,o.outMeans,i.insertTime,p.PMNM,p.measureUnit,p.storageTime,"
								+ "p.location,p.maintainCycle,p.manufacturer,p.keeper,p.remark"
								+ " from qy_product p"
								+ " inner join qy_outproductrelation i"
								+ " on p.productId = i.productId"
								+ " inner join qy_outapply o"
								+ " on i.outId = o.outId"
								+ " Where p.status REGEXP '轮换.{0,}出库' And p.flag=1");
				selectProductCountSql.append("Select count(*)"
						+ " from qy_product p"
						+ " inner join qy_outproductrelation i"
						+ " on p.productId = i.productId"
						+ " inner join qy_outapply o" + " on i.outId = o.outId"
						+ " Where p.status REGEXP '轮换.{0,}出库' And p.flag=1");
				for (String key : condition.keySet()) {
					if ("operateTime".equals(key)) {
						selectProductSql
								.append(" AND i.insertTime - str_to_date('"
										+ condition.get(key)
										+ "','%Y-%m-%d') <=0");
						selectProductCountSql
								.append(" AND i.insertTime - str_to_date('"
										+ condition.get(key)
										+ "','%Y-%m-%d') <=0");
					} else {
						selectProductSql.append(" AND " + key + " REGEXP '"
								+ condition.get(key) + "'");
						selectProductCountSql.append(" AND " + key
								+ " REGEXP '" + condition.get(key) + "'");
					}
				}
				selectProductSql.append(" order by i.insertTime desc Limit "
						+ (Integer.parseInt(curPageNum) - 1)
						* Integer.parseInt(pageSize) + "," + pageSize);
			} else if ("更新出库".equals(productStatus)) {
				selectProductSql
						.append("Select p.productId,o.batch,p.productName,p.productModel,p.productUnit,p.deviceNo"
								+ ",p.productPrice,o.outMeans,i.insertTime,p.PMNM,p.measureUnit,p.storageTime,"
								+ "p.location,p.maintainCycle,p.manufacturer,p.keeper,p.remark"
								+ " from qy_product p"
								+ " inner join qy_outproductrelation i"
								+ " on p.productId = i.productId"
								+ " inner join qy_outapply o"
								+ " on i.outId = o.outId"
								+ " Where p.status REGEXP '更新.{0,}出库' And p.flag=1");
				selectProductCountSql.append("Select count(*)"
						+ " from qy_product p"
						+ " inner join qy_outproductrelation i"
						+ " on p.productId = i.productId"
						+ " inner join qy_outapply o" + " on i.outId = o.outId"
						+ " Where p.status REGEXP '更新.{0,}出库' And p.flag=1");
				for (String key : condition.keySet()) {
					if ("operateTime".equals(key)) {
						selectProductSql
								.append(" AND i.insertTime - str_to_date('"
										+ condition.get(key)
										+ "','%Y-%m-%d') <=0");
						selectProductCountSql
								.append(" AND i.insertTime - str_to_date('"
										+ condition.get(key)
										+ "','%Y-%m-%d') <=0");
					} else {
						selectProductSql.append(" AND " + key + " REGEXP '"
								+ condition.get(key) + "'");
						selectProductCountSql.append(" AND " + key
								+ " REGEXP '" + condition.get(key) + "'");
					}
				}
				selectProductSql.append(" order by i.insertTime desc Limit "
						+ (Integer.parseInt(curPageNum) - 1)
						* Integer.parseInt(pageSize) + "," + pageSize);
			}
		}

		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(selectProductSql.toString());
			selectCountPs = conn.prepareStatement(selectProductCountSql
					.toString());
			rs = pstmt.executeQuery();
			selectCountRs = selectCountPs.executeQuery();
			while (rs.next()) {
				Map<String, String> productMap = new HashMap<String, String>();
				ResultSetMetaData metaData = rs.getMetaData();
				int len2 = metaData.getColumnCount();
				for (int k = 1; k <= len2; k++) {
					productMap.put(metaData.getColumnName(k), rs.getString(k));
				}
				targetList.add(productMap);
			}

			int totalPageSize = 0;
			if (selectCountRs.next()) {
				totalPageSize = selectCountRs.getInt(1);
				totalPageSize = totalPageSize % 10 == 0 ? totalPageSize / 10
						: totalPageSize / 10 + 1;
			}
			Map<String, String> count = new HashMap<String, String>();
			count.put("totalPageSize", totalPageSize + "");
			targetList.add(count);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return targetList;
	}

	public List<Map<String, String>> selectProduct(String productStatus,
			String flag, int curPageNum, int pageSize) {
		PreparedStatement selectCountPs = null;
		ResultSet selectCountRs = null;
		PreparedStatement selectApplyPs = null;
		ResultSet selectApplyRs = null;

		List<Map<String, String>> targetList = new ArrayList<Map<String, String>>();

		// 查询产品表
		String selectProductSql = "Select * From qy_product Where status='"
				+ productStatus + "' And flag=" + flag
				+ " ORDER BY productId DESC Limit ?,?";
		// 查询在产品表中符合条件的条数
		String selectCountSql = "Select count(*) from qy_product Where status='"
				+ productStatus + "' And flag=" + flag;
		// 查询申请表
		String selectApply = "Select batch,execDate From qy_outapply "
				+ "Where outId = (Select outId From qy_outproductrelation Where productId=? ORDER BY outId DESC LIMIT 1)";

		try {
			conn = DBConnection.getConn();

			pstmt = conn.prepareStatement(selectProductSql);
			pstmt.setInt(1, (curPageNum - 1) * pageSize);
			pstmt.setInt(2, pageSize);
			selectApplyPs = conn.prepareStatement(selectApply);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Map<String, String> productMap = new HashMap<String, String>();
				ResultSetMetaData metaData = rs.getMetaData();
				int len2 = metaData.getColumnCount();
				for (int k = 1; k <= len2; k++) {
					productMap.put(metaData.getColumnName(k), rs.getString(k));
				}

				selectApplyPs.setString(1, rs.getString("productId"));
				selectApplyRs = selectApplyPs.executeQuery();

				String batch = "";
				String execDate = "";
				if (selectApplyRs.next()) {
					batch = selectApplyRs.getString("batch");
					execDate = selectApplyRs.getString("execDate");
				}
				productMap.put("batch", batch);
				productMap.put("execDate", execDate);

				targetList.add(productMap);
			}

			int totalPageSize = 1;
			selectCountPs = conn.prepareStatement(selectCountSql);
			selectCountRs = selectCountPs.executeQuery();
			if (selectCountRs.next())
				totalPageSize = selectCountRs.getInt(1);
			totalPageSize = totalPageSize % 10 == 0 ? totalPageSize / 10
					: totalPageSize / 10 + 1;
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("totalPageSize", "" + totalPageSize);
			targetList.add(tempMap);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return targetList;
	}

	public List<HashMap<String, String>> getUpdateDetailByProductId(
			String productId) {
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> result2 = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT P.productId,P.`status`,P.otherProductId "
				+ "FROM qy_product AS P WHERE P.contractId = (SELECT contractId FROM qy_product WHERE productId = ?) "
				+ "AND (P.flag = 1 OR P.flag = 2 OR P.flag = 3 OR P.flag = 4) ";
		String sql2 = "";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, productId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String theProductId = rs.getString("productId");
				String status = rs.getString("status");
				String otherProductId = rs.getString("otherProductId");
				if (status == null || status.equals("")
						|| otherProductId == null || otherProductId.equals(""))
					continue;
				if (status.split(",").length == 1) {
					HashMap<String, String> one = new HashMap<String, String>();
					one.put("productId", theProductId);
					one.put("status", status);
					one.put("otherProductId", otherProductId);
					result.add(one);
				} else {
					String[] statuses = status.split(",");
					String[] otherProductIds = otherProductId.split(",");
					HashMap<String, String> one = new HashMap<String, String>();
					HashMap<String, String> two = new HashMap<String, String>();
					one.put("productId", theProductId);
					one.put("status", statuses[0]);
					one.put("otherProductId", otherProductIds[0]);
					result.add(one);
					if(otherProductIds.length > 1)
					{
						two.put("productId", theProductId);
						two.put("status", statuses[1]);
						two.put("otherProductId", otherProductIds[1]);
						result.add(two);
					}
					
					
				}
			}
			HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();
			sql2 = "SELECT P.productId, P.contractId, P.productModel, P.deviceNo, P.proStatus, P.flag, "
					+ " A.operateTime FROM qy_product AS P, qy_account AS A WHERE "
					+ "P.productId = A.productId AND (";
			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String> one = new HashMap<String, String>();
				String theproductId = (String) result.get(i).get("productId");
				String status = (String) result.get(i).get("status");
				String otherProductId = (String) result.get(i).get(
						"otherProductId");
				one.put("productId", theproductId);
				one.put("status", status);
				one.put("otherProductId", otherProductId);
				resultMap.put(theproductId, one);
				if (i == 0)
					sql2 += "(P.productId = '" + theproductId
							+ "' AND A.operateType = '" + status + "') ";
				else
					sql2 += "OR (P.productId = '" + theproductId
							+ "' AND A.operateType = '" + status + "') ";
			}
			sql2 += ")";
			this.pstmt = this.conn.prepareStatement(sql2);
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				String theProductId = rs.getString("productId");
				String contractId = rs.getString("contractId");
				String productModel = rs.getString("productModel");
				String deviceNo = rs.getString("deviceNo");
				String proStatus = rs.getString("proStatus");
				String operateTime = rs.getString("operateTime");
				String flag = rs.getString("flag");
				HashMap<String, String> one = resultMap.get(theProductId);
				one.put("contractId", contractId);
				one.put("productModel", productModel);
				one.put("deviceNo", deviceNo);
				one.put("proStatus", proStatus);
				one.put("flag", flag);
				one.put("operateTime", operateTime);
				result2.add(one);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return result2;
	}

	public List<HashMap<String, String>> getUpdateDetailByProductModel(
			String productModel) {
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> result2 = new ArrayList<HashMap<String, String>>();
		String sql = "SELECT P.productId,P.`status`,P.otherProductId "
				+ "FROM qy_product AS P WHERE P.productModel = ? "
				+ "AND (P.flag = 1 OR P.flag = 2 OR P.flag = 3 OR P.flag = 4) ";
		String sql2 = "";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, productModel);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String theProductId = rs.getString("productId");
				String status = rs.getString("status");
				String otherProductId = rs.getString("otherProductId");
				if (status == null || status.equals("")
						|| otherProductId == null || otherProductId.equals(""))
					continue;
				if (status.split(",").length == 1) {
					HashMap<String, String> one = new HashMap<String, String>();
					one.put("productId", theProductId);
					one.put("status", status);
					one.put("otherProductId", otherProductId);
					result.add(one);
				} else {
					String[] statuses = status.split(",");
					String[] otherProductIds = otherProductId.split(",");
					HashMap<String, String> one = new HashMap<String, String>();
					HashMap<String, String> two = new HashMap<String, String>();
					one.put("productId", theProductId);
					one.put("status", statuses[0]);
					one.put("otherProductId", otherProductIds[0]);

					two.put("productId", theProductId);
					two.put("status", statuses[1]);
					two.put("otherProductId", otherProductIds[1]);
					result.add(one);
					result.add(two);
				}
			}
			HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();
			sql2 = "SELECT P.productId, P.contractId, P.productModel, P.deviceNo, P.proStatus, A.operateType, P.flag, "
					+ " A.operateTime FROM qy_product AS P, qy_account AS A WHERE "
					+ "P.productId = A.productId AND (";
			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String> one = new HashMap<String, String>();
				String theproductId = (String) result.get(i).get("productId");
				String status = (String) result.get(i).get("status");
				String otherProductId = (String) result.get(i).get(
						"otherProductId");
				one.put("productId", theproductId);
				one.put("status", status);
				one.put("otherProductId", otherProductId);
				resultMap.put(theproductId, one);
				if (i == 0)
					sql2 += "(P.productId = '" + theproductId
							+ "' AND A.operateType = '" + status + "') ";
				else
					sql2 += "OR (P.productId = '" + theproductId
							+ "' AND A.operateType = '" + status + "') ";
			}
			sql2 += ")";
			this.pstmt = this.conn.prepareStatement(sql2);
			this.rs = this.pstmt.executeQuery();
			while (rs.next()) {
				String theProductId = rs.getString("productId");
				String contractId = rs.getString("contractId");
				String theproductModel = rs.getString("productModel");
				String deviceNo = rs.getString("deviceNo");
				String proStatus = rs.getString("proStatus");
				String operateTime = rs.getString("operateTime");
				String operateType = rs.getString("operateType");
				String flag = rs.getString("flag");
				HashMap<String, String> one = resultMap.get(theProductId);
				one.put("contractId", contractId);
				one.put("productModel", theproductModel);
				one.put("deviceNo", deviceNo);
				one.put("proStatus", proStatus);
				one.put("flag", flag);
				one.put("operateTime", operateTime);
				one.put("operateType", operateType);
				result2.add(one);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return result2;
	}

	/**
	 * 企业导入更新入库申请表的时候，相应地，改变产品表的状态（不适用于更新出库）
	 * 
	 * @param dyadic
	 *            导入excel表的时候相应的产品表
	 * @return
	 */
	public boolean updateProduct_qy(List<ArrayList<String>> dyadic) {
		String updateProductSql_yes = "Update qy_product Set flag = ?,otherProductId=? Where productId=?";
		String updateProductSql_no = "Update qy_product Set flag=?,otherProductId=? Where productId=?";

		PreparedStatement updateProductPs_yes = null;
		PreparedStatement updateProductPs_no = null;

		boolean runStatus = true;

		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			updateProductPs_yes = conn.prepareStatement(updateProductSql_yes);
			updateProductPs_no = conn.prepareStatement(updateProductSql_no);

			ArrayList<String> firstRow = dyadic.get(0);
			int proStatusIndex = 16;
			int productIdIndex = 1;
			int otherProductIdIndex = 32;
			int len = firstRow.size();
			for (int i = 0; i < len; i++) {
				if ("proStatus".equals(firstRow.get(i)))
					proStatusIndex = i;
				if ("productId".equals(firstRow.get(i)))
					productIdIndex = i;
				if ("otherProductId".equals(firstRow.get(i)))
					otherProductIdIndex = i;
			}

			boolean yesFlag = false;
			boolean noFlag = false;
			int rowLen = dyadic.size();
			for (int i = 1; i < rowLen; i++) {
				String proStatus = dyadic.get(i).get(proStatusIndex);
				if (proStatus.indexOf("未") == -1
						&& proStatus.indexOf("不") == -1) {
					// 如果没有“未”、“不”字，表示已经审核通过

					// update原产品
					yesFlag = true;
					updateProductPs_yes.setInt(1, 2);
					updateProductPs_yes.setString(2,
							dyadic.get(i).get(productIdIndex));
					updateProductPs_yes.setString(3,
							dyadic.get(i).get(otherProductIdIndex));
					updateProductPs_yes.addBatch();
				} else {
					// 审核不通过

					// update原产品
					noFlag = true;
					updateProductPs_no.setInt(1, 1);
					updateProductPs_no.setString(2, null);
					updateProductPs_no.setString(3,
							dyadic.get(i).get(otherProductIdIndex));
					updateProductPs_no.addBatch();
				}
			}
			if (yesFlag)
				updateProductPs_yes.executeBatch();
			if (noFlag)
				updateProductPs_no.executeBatch();

			conn.commit();
		} catch (SQLException e) {
			try {
				runStatus = false;
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, updateProductPs_yes, rs);
			DBConnection.close(updateProductPs_no);
		}
		return runStatus;
	}

	public HashMap<String, Object> queryUpdateHistory(String DeviceNo,
			String productModel, String means) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		String sql = "";
		if (means.equals("更新入库") || means.equals("轮换出库")) {
			sql = "Select productModel,deviceNo From qy_product Where otherproductId In(Select ProductId From qy_product Where deviceNo=?And productModel=?)";
		} else if (means.equals("更新出库")) {
			sql = "Select productModel,deviceNo From qy_product Where productId In(Select otherProductId From qy_product Where deviceNo=?And productModel=?)";
		}
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, DeviceNo);
			pstmt.setString(2, productModel);
			res = pstmt.executeQuery();
			while (res.next()) {
				if (means.equals("更新入库") || means.equals("轮换出库")) {
					info.put("oldproductModel", res.getString("productModel"));
					info.put("olddeviceNo", res.getString("deviceNo"));
				} else if (means.equals("更新出库")) {
					info.put("newproductModel", res.getString("productModel"));
					info.put("newdeviceNo", res.getString("deviceNo"));
				}
			}
		} catch (SQLException ex) {
			
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return info;
	}

	/**
	 * 更新产品状态（这里用于导入发料单的时候，相应地改变产品的状态）
	 * <p>
	 * 企业版：flag 为1 ，status append 发料调拨出库|发料轮换出库|发料更新出库 其他版本：flag为2 ，status
	 * append 发料调拨出库|发料轮换出库|发料更新出库
	 * </p>
	 * 
	 * @param dyadic
	 *            outList-product关系表二维数组 第一行为标题行,标题行中有：productId和ownedUnit字段
	 * @param proStatus
	 *            状态，产品将要改变成这个状态
	 * @param outListType
	 *            发料出库方式，比如发料轮换出库、发料更新出库
	 * @param flag
	 *            产品表中的flag字段
	 * @return
	 * @author liangyihuai
	 */
	public boolean updateProductStatus(List<ArrayList<String>> dyadic,
			String proStatus, String outListType, String flag) {
		// 查询产品表
		String selectSql = "SELECT status FROM qy_product WHERE productId=? And ownedUnit=?";
		// 更新产品表
		String updateSql = "UPDATE qy_product SET proStatus=?,status=?,flag=? WHERE productId=? And ownedUnit=?";

		boolean runStatus = true;

		PreparedStatement selectPs = null;
		PreparedStatement updatePs = null;

		// 得到id字段所在的列
		int productIdIndex = -1;
		int ownedUnitIndex = -1;
		ArrayList<String> firstRow = dyadic.get(0);
		for (int i = 0; i < firstRow.size(); i++) {
			if ("productId".equals(firstRow.get(i))) {
				productIdIndex = i;
			} else if ("ownedUnit".equals(firstRow.get(i))) {
				ownedUnitIndex = i;
			}
		}
		if (productIdIndex == -1 || ownedUnitIndex == -1)
			return false;

		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			selectPs = conn.prepareStatement(selectSql);
			updatePs = conn.prepareStatement(updateSql);

			String status = "";

			// 从第二行开始，第一行为标题行
			for (int i = 1; i < dyadic.size(); i++) {
				String productId = dyadic.get(i).get(productIdIndex);
				String ownedUnit = dyadic.get(i).get(ownedUnitIndex);

				// 查询产品表
				selectPs.setString(1, productId);
				selectPs.setString(2, ownedUnit);
				rs = selectPs.executeQuery();
				if (rs.next()) {
					status = rs.getString(1);
					if (status != null && !"".equals(status)) {
						if (!status.endsWith(outListType))
							status = status + "," + outListType;
					} else {
						status = outListType;
					}
				}

				// 更新产品表
				updatePs.setString(1, proStatus);
				updatePs.setString(2, status);
				updatePs.setString(3, flag);
				updatePs.setString(4, productId);
				updatePs.setString(5, ownedUnit);
				updatePs.addBatch();

			}
			updatePs.executeBatch();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			runStatus = false;
			e.printStackTrace();
		}
		return runStatus;
	}

	/**
	 * 按设备统计查询 结果总数量
	 * 
	 * @return
	 * @throws Exception
	 * @author Liuhs
	 */
	public int getProductQueryStatisticSum(Map<String, String> parameter)
			throws Exception {
		int productSum = 0;
		String companyStr = parameter.get("keeper");
		String sql = "SELECT COUNT(DISTINCT a.productModel,a.productUnit) FROM qy_product a WHERE a.proStatus NOT IN (?,?,?) AND a.productModel REGEXP ? AND a.productUnit REGEXP ? AND a.ownedUnit IN ("
				+ companyStr + ")";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "未到库");
			pstmt.setString(2, "未申请");
			pstmt.setString(3, "合同销毁");
			pstmt.setString(4, parameter.get("productModel") == "" ? "/*/*"
					: "/*" + parameter.get("productModel") + "/*");
			pstmt.setString(5, parameter.get("productUnit") == "" ? "/*/*"
					: "/*" + parameter.get("productUnit") + "/*");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				productSum = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productSum;
	}

	/**
	 * 统计产品的数量
	 * 
	 * @param productModel
	 *            产品型号
	 * @param productUnit
	 *            产品单元
	 * @return 产品数量
	 * @author liangyihuai
	 */
	public int getProductNum(String productModel, String productUnit) {
		String sql = "SELECT COUNT(*) FROM qy_product WHERE productModel=? AND productUnit = ? AND proStatus regexp '已入库'";
		int num = 0;
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productModel);
			pstmt.setString(2, productUnit);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 统计已入库的并且产品型号相同的产品的数量
	 * @return key:productModel;value:num
	 * @author liangyihuai
	 */
	public Map<String,Integer> getEachProductModelNum(){
		String sql = "SELECT productModel,COUNT(*) FROM qy_product WHERE proStatus='已入库' GROUP BY productModel";
		Map<String,Integer> map = new HashMap<String,Integer>();
		
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString("productModel"), rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 删除企业版设备汇总查询中的某条记录
	 * <p>删除的条件是前台页面的字段</p>
	 * @param productModel
	 * @param productUnit
	 * @param measureUnit
	 * @param price
	 * @param manufacturer
	 * @param ownedUnit
	 * @return
	 * @author liangyihuai
	 */
	public boolean delectCollective(String productModel, String productUnit,
			String measureUnit,double price,String manufacturer,String ownedUnit){
		
		String sql = "DELETE FROM qy_product WHERE productModel='"+productModel+
				"' AND productUnit='"+productUnit+"' AND measureUnit='"+measureUnit+
				"' AND productPrice="+price+" AND manufacturer='"+manufacturer+"' AND ownedUnit='"+ownedUnit+"'";
//		String sql = "DELETE FROM qy_product WHERE productModel=? AND productUnit=?"
//				+ " AND measureUnit=? AND productPrice=? AND manufacturer=? "
//				+ "AND ownedUnit=?";
//		
		boolean flag = true;
		try{
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, productModel);
//			pstmt.setString(2, productUnit);
//			pstmt.setString(3, measureUnit);
//			pstmt.setDouble(4, price);
//			pstmt.setString(5, manufacturer);
//			pstmt.setString(6, ownedUnit);
			
			int i = pstmt.executeUpdate();
			if(i == 0){
				flag = false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			flag =false;
		}
		return flag;
	}
}
