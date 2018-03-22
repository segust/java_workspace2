package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;
/**
 * 
 * @author lynn
 * 用于增加操作员日志记录
 * 用于增加维护日志记录
 */
public class LogDAO {
	private  List<String> search = new ArrayList<String>();
	private  Connection conn = null;
	private  PreparedStatement pstmt = null;
	long sum_log=0;
	
	public LogDAO() {
	
	}
	public long getSum_Log()
	{
		return sum_log;
	}
	/**
	 * 
	 * @param log 
	 * @return
	 * @throws Exception
	 * 增加一条维护日志记录
	 */
	public boolean saveMainTainLog(Log log) {
		boolean flag = false;
		try {
			//2015.06.09 修改结构 conn统一在dao的每个函数体里开，关
			this.conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_log(productId,operateType,operateTime,"
					+ "userName,maintainType,inspectPerson,remark) VALUES(?,?,?,?,?,?,?);";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, log.getProductId());
			this.pstmt.setString(2, log.getOperateType());
			//model创建的是util.Date，而pstmt设置的是sql.Date,因此自定义工具类进行转换
			this.pstmt.setTimestamp(3, MyDateFormat.changeToSqlDate(log.getOperateTime()));
			this.pstmt.setString(4, log.getUserName());
			this.pstmt.setString(5, log.getMainTainType());
			this.pstmt.setString(6, log.getInspectPerson());
			this.pstmt.setString(7, log.getRemark());
			int count = this.pstmt.executeUpdate();
			if(count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}
	
	/**
	 * 
	 * @param  一个log对象
	 * @return
	 * @throws Exception
	 * 增加一条操作员日志记录
	 */
	public boolean saveOperateLog(Log log) {
		boolean flag = false;
		try {
			//2015.06.09 修改结构 conn统一在dao的每个函数体里开，关
			this.conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_log(productId,operateType,operateTime,"
					+ "userName,remark) VALUES(?,?,?,?,?);";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, log.getProductId());
			this.pstmt.setString(2, log.getOperateType());
			//model创建的是util.Date，而pstmt设置的是sql.Date,因此自定义工具类进行转换
			this.pstmt.setTimestamp(3, MyDateFormat.changeToSqlDate(log.getOperateTime()));
			this.pstmt.setString(4, log.getUserName());
			this.pstmt.setString(5,log.getRemark());
			int count = this.pstmt.executeUpdate();
			if(count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return flag;
	}
	
	
	/**
	 * 查询维护记录
	 * @param values
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> selectMaintainInfoDetail(List<String> values){
		
		ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT P.productId,P.productModel,P.productUnit,I.batch,I.deviceNo," +
					"I.newPrice,I.num,P.productType,I.storageTime,P.restKeepTime," +
					"P.manufacturer,P.keeper,L.maintainType,L.operateTime,L.remark " +
					"FROM qy_product AS P, qy_inapply AS I,qy_inproductrelation AS R," +
					"qy_log AS L WHERE L.productId = P.productId AND P.productId " +
					"= R.productId AND R.inId = I.inId AND L.operateType regexp '维护'");
			String search = "";//最后生成的查询语句
			boolean[] use = new boolean[5];//5个字段各自是否有效的标记
			for(int i = 0; i < values.size(); i++)
				if(!values.get(i).equals(""))
					use[i] = true;
						
			
			//下面开始构造查询语句
			
			if(use[0])
				sql.append(" and P.productModel regexp '" + values.get(0) + "'");
			if(use[1])
				sql.append(" and P.productUnit regexp '" + values.get(1) + "'");
			if(use[2])
				sql.append(" and P.manufacturer regexp '" + values.get(2) + "'");
			if(use[3])
				sql.append(" and L.maintainType regexp '" + values.get(3) + "'");
			if(use[4])
				sql.append(" and I.deviceNo regexp '" + values.get(4) + "'");
			
			search = sql.toString();
			search = search + " ORDER BY L.logId DESC";
			//查询语句构造完毕
			
			
			this.pstmt = this.conn.prepareStatement(search);
			ResultSet rs = this.pstmt.executeQuery();
			while(rs.next()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				long productId = rs.getLong(1);
				String productModel = rs.getString(2);
				String productUnit = rs.getString(3);
				String batch = rs.getString(4);
				String deviceNo = rs.getString(5);
				double price = rs.getDouble(6);
				int num = rs.getInt(7);
				String productType = rs.getString(8);
				String storageTime = rs.getString(9);
				int restKeepTime = rs.getInt(10);
				String manufacturer = rs.getString(11);
				String keeper = rs.getString(12);
				String maintainType = rs.getString(13);
				Date operateTime = MyDateFormat.changeToJavaDate(rs.getDate(14));
				String remark = rs.getString(15);
				map.put("productId", new Long(productId));
				map.put("productModel", productModel);
				if(productUnit != null)
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
				map.put("maintainType", maintainType);
				map.put("operateTime", operateTime);
				map.put("remark", remark);
				T.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}		
		return T;
	}
	/**
	 * 查询维护记录总条数
	 * @param values
	 * @return
	 */
	public int SelecthistorySum(String deviceNo,String productModel){
		int sum = 0;
		//String sql="Select count(*),operateTime,maintainType,userName,ownedUnit,remark From qy_log Where ownedUnit in(Select ownedUnit From qy_product Where deviceNo=?)";
	String sql="Select count(*) From qy_log Where productId in(Select productId From qy_product Where deviceNo=? And productModel=?)";
		try{
	    	this.conn = DBConnection.getConn();
	    	this.pstmt = this.conn.prepareStatement(sql);
	    	this.pstmt.setString(1, deviceNo);
	    	this.pstmt.setString(2, productModel);
			ResultSet rs = this.pstmt.executeQuery();
			while(rs.next()){
				sum = rs.getInt("count(*)");
			}
	    }catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}		
		return sum;
	    
	}
	/**
	 * 维护历史查询
	 * @param values
	 * @return
	 */
	public ArrayList< HashMap<String,Object> >selectMaintainhistory(String productModel,String deviceNo,int curPageNum,int pageSize){
		ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		String sql="Select operateTime,maintainType,userName,ownedUnit,remark From qy_log Where productId in(Select productId From qy_product Where deviceNo=? And productModel=? )LIMIT ?,?";
		try{
	    	this.conn = DBConnection.getConn();
	    	this.pstmt = this.conn.prepareStatement(sql);
	    	this.pstmt.setString(1, deviceNo);
	    	this.pstmt.setString(2, productModel);
	    	this.pstmt.setInt(3, (curPageNum-1)*pageSize);
	    	this.pstmt.setInt(4, pageSize);
			ResultSet rs = this.pstmt.executeQuery();
			while(rs.next()){
				//int sum = rs.getInt("count(*)");
				HashMap<String, Object> map = new HashMap<String, Object>();
				//map.put("sum", sum);
				map.put("operateTime", rs.getString("operateTime"));
				map.put("maintainType", rs.getString("maintainType"));
				map.put("userName", rs.getString("userName"));
				map.put("ownedUnit", rs.getString("ownedUnit"));
				map.put("remark", rs.getString("remark"));
				T.add(map);
			}
	    }catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}		
		return T;
	    
	}
	/**
	 * 查询维护记录，分页
	 * @param values
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> selectMaintainInfoDetailByPage(List<String> values,
			int pageSize, int curPageNumber){
		
		ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT P.productId,P.productModel,P.productUnit,I.batch,P.deviceNo," +
					"P.productPrice,I.num,P.productType,P.storageTime,P.restKeepTime," +
					"P.manufacturer,P.keeper,L.maintainType,L.operateTime,L.remark,L.logId,P.deliveryTime " +
					"FROM qy_product AS P, qy_inapply AS I,qy_inproductrelation AS R," +
					"qy_log AS L WHERE L.productId = P.productId AND P.productId " +
					"= R.productId AND R.inId = I.inId AND L.operateType regexp '维护'");
			String search = "";//最后生成的查询语句
			boolean[] use = new boolean[5];//5个字段各自是否有效的标记
			for(int i = 0; i < values.size(); i++)
				if(!values.get(i).equals(""))
					use[i] = true;
						
			
			//下面开始构造查询语句
			
			if(use[0])
				sql.append(" and P.productModel regexp '" + values.get(0) + "'");
			if(use[1])
				sql.append(" and P.productUnit regexp '" + values.get(1) + "'");
			if(use[2])
				sql.append(" and P.manufacturer regexp '" + values.get(2) + "'");
			if(use[3])
				sql.append(" and L.maintainType regexp '" + values.get(3) + "'");
			if(use[4])
				sql.append(" and P.deviceNo regexp '" + values.get(4) + "'");
			search = sql.toString();
			System.out.println(sql);
			search = search + " ORDER BY L.logId DESC limit " + (curPageNumber - 1)*pageSize + ", " + pageSize;
			//查询语句构造完毕
			
			
			this.pstmt = this.conn.prepareStatement(search);
			ResultSet rs = this.pstmt.executeQuery();
			while(rs.next()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				long productId = rs.getLong("productId");
				String productModel = rs.getString("productModel");
				String productUnit = rs.getString("productUnit");
				String batch = rs.getString("batch");
				String deviceNo = rs.getString("deviceNo");
				double price = rs.getDouble("productPrice");
				int num = rs.getInt("num");
				String productType = rs.getString("productType");
				String storageTime = rs.getString("storageTime");
				int restKeepTime = rs.getInt("restKeepTime");
				String manufacturer = rs.getString("manufacturer");
				String keeper = rs.getString("keeper");
				String maintainType = rs.getString("maintainType");
				Date operateTime = MyDateFormat.changeToJavaDate(rs.getDate("operateTime"));
				String remark = rs.getString("remark");
				int logId = rs.getInt("logId");
				Date deliveryTime = MyDateFormat.changeToJavaDate(rs.getDate("deliveryTime"));
				map.put("productId", new Long(productId));
				map.put("productModel", productModel);
				if(productUnit != null)
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
				map.put("maintainType", maintainType);
				map.put("operateTime", operateTime);
				map.put("remark", remark);
				map.put("logId", logId);
				map.put("deliveryTime", deliveryTime);
				T.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}		
		return T;
	}
	/**
	 * 查询inspectLog总数sum
	 * @param values
	 * @return
	 */
	public int countInspectNumber(List<String> values){
		
		int sum=0;
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT P.productId,P.productModel,P.productUnit,I.batch,I.deviceNo," +
					"I.newPrice,I.num,P.productType,I.storageTime,P.restKeepTime," +
					"P.manufacturer,P.keeper,L.maintainType,L.operateTime,L.remark " +
					"FROM qy_product AS P, qy_inapply AS I,qy_inproductrelation AS R," +
					"qy_log AS L WHERE L.productId = P.productId AND P.productId " +
					"= R.productId AND R.inId = I.inId AND L.operateType regexp '检查'");
			String search = "";//最后生成的查询语句
			boolean[] use = new boolean[5];//5个字段各自是否有效的标记
			for(int i = 0; i < values.size(); i++)
				if(!values.get(i).equals(""))
					use[i] = true;
			//下面开始构造查询语句
			if(use[0])
				sql.append(" and P.productModel regexp '" + values.get(0) + "'");
			if(use[1])
				sql.append(" and P.productUnit regexp '" + values.get(1) + "'");
			if(use[2])
				sql.append(" and P.manufacturer regexp '" + values.get(2) + "'");
			if(use[3])
				sql.append(" and L.maintainType regexp '" + values.get(3) + "'");
			if(use[4])
			
				sql.append(" and I.deviceNo regexp '" + values.get(4) + "'");
			search = sql.toString();
			//查询语句构造完毕
			
			this.pstmt = this.conn.prepareStatement(search);
			ResultSet rs = this.pstmt.executeQuery();
			while(rs.next()){
			
				sum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}		
		return sum;
	}
	
	/**
	 * 查询检查记录，分页
	 * @param values
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> selectInspectInfoDetailByPage(List<String> values,
			int pageSize, int curPageNumber){
		
		ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT P.productId,P.productModel,P.productUnit,I.batch,I.deviceNo," +
					"I.newPrice,I.num,P.productType,I.storageTime,P.restKeepTime," +
					"P.manufacturer,P.keeper,L.maintainType,L.operateTime,L.remark " +
					"FROM qy_product AS P, qy_inapply AS I,qy_inproductrelation AS R," +
					"qy_log AS L WHERE L.productId = P.productId AND P.productId " +
					"= R.productId AND R.inId = I.inId AND L.operateType regexp '检查'");
			String search = "";//最后生成的查询语句
			boolean[] use = new boolean[5];//5个字段各自是否有效的标记
			for(int i = 0; i < values.size(); i++)
				if(!values.get(i).equals(""))
					use[i] = true;
						
			
			//下面开始构造查询语句
			
			if(use[0])
				sql.append(" and P.productModel regexp '" + values.get(0) + "'");
			if(use[1])
				sql.append(" and P.productUnit regexp '" + values.get(1) + "'");
			if(use[2])
				sql.append(" and P.manufacturer regexp '" + values.get(2) + "'");
			if(use[3])
				sql.append(" and L.maintainType regexp '" + values.get(3) + "'");
			if(use[4])
				sql.append(" and I.deviceNo regexp '" + values.get(4) + "'");
			search = sql.toString();
			search = search+" order by L.logId desc";
			search = search + " limit " + (curPageNumber - 1)*pageSize + ", " + pageSize;
			//查询语句构造完毕
			this.pstmt = this.conn.prepareStatement(search);
			ResultSet rs = this.pstmt.executeQuery();
			while(rs.next()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				long productId = rs.getLong(1);
				String productModel = rs.getString(2);
				String productUnit = rs.getString(3);
				String batch = rs.getString(4);
				String deviceNo = rs.getString(5);
				double price = rs.getDouble(6);
				int num = rs.getInt(7);
				String productType = rs.getString(8);
				String storageTime = rs.getString(9);
				int restKeepTime = rs.getInt(10);
				String manufacturer = rs.getString(11);
				String keeper = rs.getString(12);
				String maintainType = rs.getString(13);
				Date operateTime = MyDateFormat.changeToJavaDate(rs.getDate(14));
				String remark = rs.getString(15);
				map.put("productId", new Long(productId));
				map.put("productModel", productModel);
				if(productUnit != null)
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
				map.put("maintainType", maintainType);
				map.put("operateTime", operateTime);
				map.put("remark", remark);
				T.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}		
		return T;
	}
	
	/**
	 * 查询维护记录
	 * @param values
	 * @return
	 */
	public int countMaintainInfoDetail(List<String> values){
		
		int count = 0;
		try {
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(*) " +
					"FROM qy_product AS P, qy_inapply AS I,qy_inproductrelation AS R," +
					"qy_log AS L WHERE L.productId = P.productId AND P.productId " +
					"= R.productId AND R.inId = I.inId AND L.operateType regexp '维护'");
			String search = "";//最后生成的查询语句
			boolean[] use = new boolean[5];//5个字段各自是否有效的标记
			for(int i = 0; i < values.size(); i++)
				if(!values.get(i).equals(""))
					use[i] = true;
			//下面开始构造查询语句
			
			if(use[0])
				sql.append(" and P.productModel regexp '" + values.get(0) + "'");
			if(use[1])
				sql.append(" and P.productUnit regexp '" + values.get(1) + "'");
			if(use[2])
				sql.append(" and P.manufacturer regexp '" + values.get(2) + "'");
			if(use[3])
				sql.append(" and L.maintainType regexp '" + values.get(3) + "'");
			if(use[4])
				sql.append(" and I.deviceNo regexp '" + values.get(4) + "'");
			
			search = sql.toString();
			//查询语句构造完毕
			
			
			this.pstmt = this.conn.prepareStatement(search);
			ResultSet rs = this.pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}
		return count;
	}
	
	/**
	 * 
	 * @param mainTain 一个qy_log对象
	 * @return
	 * @throws Exception
	 * 系统管理--->日志管理（查询操作日志记录）
	 */
	public List<Map<String, String>> queryOperateLog(HashMap<String, Object> condition) throws Exception {

		List<Map<String, String>> logList = new ArrayList<Map<String, String>>();
		
		try {	
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT userName,operateType,operateTime,remark FROM `qy_log` WHERE ownedUnit="+"\"\""+" ");
			String search = "";               //生成的查询语句
			
			String userName = (String) condition.get("username");
			String fromdate = (String) condition.get("fromdate");
			String todate = (String) condition.get("todate");
			String operateType = (String) condition.get("operateType");
			int curPageNum = Integer.parseInt((String) condition.get("curPageNum"));//页面传过来的“第几页”
			int pageSize = Integer.parseInt((String) condition.get("pageSize"));
			
			 //4个字段各自是否有效的标记
			if(StringUtil.isNotEmpty(userName))
				sql.append(" AND userName LIKE '" + "%" + userName + "%" + "'");
			if(StringUtil.isNotEmpty(fromdate) && StringUtil.isNotEmpty(todate))
			{
				//将String类型的from 和 to 转化为date型数据
				Date from = null;
				Date to = null;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				try {
					from = formatter.parse(fromdate);
					to = formatter.parse(todate);
				} catch (ParseException e) {
					e.printStackTrace();
				}	
				sql.append(" AND operateTime >= '" + MyDateFormat.changeToSqlDate(from) + "' AND operateTime <= '" + MyDateFormat.changeToSqlDate(to)+ "'");
			}

			if(StringUtil.isNotEmpty(operateType))
				sql.append(" AND operateType LIKE '" + "%" + operateType + "%" + "'");

			sql.append(" ORDER BY operateTime desc");    //根据时间排列
			sql.append("  LIMIT " + (curPageNum-1)*pageSize + "," + pageSize + " ");  
			search = sql.toString();
			//查询语句构造完毕

			this.pstmt = this.conn.prepareStatement(search);
			ResultSet rest = this.pstmt.executeQuery();
			while(rest.next())  {       //循环遍历查询结果集，封装在log中， log再存放到list中   					

				Map<String, String> log = new HashMap<String, String>();
				log.put("userName", rest.getString("userName"));       //1.用户名
				log.put("operateType",rest.getString("operateType"));  //2.操作				
				log.put("operateTime", rest.getString("operateTime"));	//3.操作时间
				String remark = rest.getString("remark");
				if(remark == null){
					log.put("remark","无");   //4.备注
				}else
					log.put("remark",remark);   //4.备注
				
				logList.add(log);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(null, this.pstmt);
		}
		return logList;		
	}
	//系统管理--->日志管理（查询操作日志记录）  返回数据总数
	public int queryOperateLogCount(HashMap<String, Object> condition) throws Exception {

		int pageNum = 0;
		
		try {	
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT COUNT(*) FROM `qy_log` WHERE ownedUnit="+"\"\""+" ");
			String search = "";               //生成的查询语句
			
			String userName = (String) condition.get("username");
			String fromdate = (String) condition.get("fromdate");
			String todate = (String) condition.get("todate");
			String operateType = (String) condition.get("operateType");
			
			//下面开始构造查询语句
			 //4个字段各自是否有效的标记
			if(StringUtil.isNotEmpty(userName))
				sql.append(" AND userName LIKE '" + "%" + userName + "%" + "'");
			if(StringUtil.isNotEmpty(fromdate) && StringUtil.isNotEmpty(todate))
			{
				//将String类型的from 和 to 转化为date型数据
				Date from = null;
				Date to = null;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				try {
					from = formatter.parse(fromdate);
					to = formatter.parse(todate);
				} catch (ParseException e) {
					e.printStackTrace();
				}	
				sql.append(" AND operateTime >= '" + MyDateFormat.changeToSqlDate(from) + "' AND operateTime <= '" + MyDateFormat.changeToSqlDate(to)+ "'");
			}

			if(StringUtil.isNotEmpty(operateType))
				sql.append(" AND operateType LIKE '" + "%" + operateType + "%" + "'");

			sql.append(" ORDER BY operateTime desc");    //根据时间排列
			search = sql.toString();
			//查询语句构造完毕

			this.pstmt = this.conn.prepareStatement(search);
			ResultSet rest = this.pstmt.executeQuery();
			while(rest.next())  {       //循环遍历查询结果集，封装在log中， log再存放到list中   					

				pageNum = rest.getInt("COUNT(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(null, this.pstmt);
		}
		return pageNum;		
	}
	public boolean queryInspectAlarmLog(int year, String month)
	{
		String sql = "select * from qy_log where operateTime REGEXP '" + year 
				+ "-" + month + "' and operateType REGEXP '检查提醒'";
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			ResultSet rs = this.pstmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("出错！");
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}
	}
	
	public boolean queryCheckAlarmLog(int year, String month)
	{
		String sql = "select * from qy_log where operateTime REGEXP '" + year 
				+ "-" + month + "' and operateType REGEXP '抽查提醒'";
		try {
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			ResultSet rs = this.pstmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("出错！");
		}finally {
			DBConnection.close(this.conn, this.pstmt);
		}
	}
	
	/**
	 * 根据log id 查询log表的所有字段信息
	 * @param logIDs
	 * @return log表,其中第一行是数据库表的标题行
	 */
	public ArrayList<ArrayList<String>> queryLogsByID(List<Long> logIDs){
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		Connection conn = null;
		
		String sql_log = "Select * From qy_log Where logId = ?";
		
		ArrayList<ArrayList<String>> logs = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> log_headline = new ArrayList<String>();
		//填充标题
		logs.add(log_headline);
		
		try {
			conn = DBConnection.getConn();
			ps1 = conn.prepareStatement(sql_log);
			
			int len = logIDs.size();
			for(int i = 0; i < len; i++){
				long inID = logIDs.get(i);
				
				ps1.setLong(1, inID);
				rs1 =  ps1.executeQuery();
				
				
				while(rs1.next()){
					ArrayList<String> log = new ArrayList<String>();
			        
					//得到结果集(rs)的结构信息，比如字段数、字段名等 
					ResultSetMetaData md = rs1.getMetaData();  
					int columnCount = md.getColumnCount(); 	
			        for(int k = 1; k <= columnCount; k++){
			        	log.add(rs1.getString(k));
			        	//增加标题
			        	if(log_headline.size() != columnCount)
			        		log_headline.add(md.getColumnName(k));
			        }
			      
			        logs.add(log);
				}//end while
			}//end for
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				ps1.close();
				rs1.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return logs;
	}

}
