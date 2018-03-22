package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.Account;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

/**
 * 
 * @author lynn
 * 记录产品操作流水账
 */
public class AccountDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public AccountDAO() {
		
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 * 记录产品操作流水账
	 */
	public boolean saveAccount(Account account) {
		boolean flag = false;
		try {
			//2015.06.09 修改结构 conn统一在dao的每个函数体里开，关
			this.conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_account(productId,operateType,operateTime,"
					+ "userName,remark) VALUES(?,?,?,?,?);";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, account.getProductId());
			this.pstmt.setString(2, account.getOperateType());
			//model创建的是util.Date，而pstmt设置的是sql.Date,因此自定义工具类进行转换
			this.pstmt.setTimestamp(3, MyDateFormat.changeToSqlDate(account.getOperateTime()));
			this.pstmt.setString(4, account.getUserName());
			this.pstmt.setString(5,account.getRemark());
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
	 * @author austin
	 * 业务查询→按产品统计查询→操作明细查询
	 * */
	public List<Map<String,String>> selectProductAccountById(HashMap<String, Object> condition) throws Exception{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		String productModel = (String)condition.get("productModel");
		String fromdate = (String)condition.get("fromdate");
		String todate = (String)condition.get("todate");
		String productName = (String)condition.get("productName");
		String productUnit = (String)condition.get("productUnit");
		String measureUnit = (String)condition.get("measureUnit");
		String productPrice = (String)condition.get("productPrice");
		String manufacturer = (String)condition.get("manufacturer");
		String keeper = (String)condition.get("keeper");
		String queryType = (String)condition.get("queryType");
		String jds =(String)condition.get("jds");
		try {	
			this.conn = DBConnection.getConn();
			StringBuilder sql = new StringBuilder();
			String search = "";               //生成的查询语句
			sql.append("SELECT P.productModel, P.contractId, AC.operateType , AC.operateTime " +
					"FROM qy_product P LEFT OUTER JOIN qy_account AC ON P.productId = AC.productId " +
					"WHERE (P.productId ,AC.id) IN(SELECT P.productId, MAX(AC.id) FROM qy_product P, qy_account AC " +
					"WHERE P.productId = AC.productId GROUP BY P.productId ) ");			
			
			sql.append(" AND P.productName = '" + productName + "'  AND P.productUnit = '" + productUnit + "'  AND P.measureUnit = '" + measureUnit + "' " +
					" AND P.productPrice = '" + productPrice + "'  AND P.manufacturer = '" + manufacturer + "'  AND P.keeper = '" + keeper + "' ");				
			if(StringUtil.isNotEmpty(queryType)){
				if(queryType.endsWith("1"))
					sql.append(" AND AC.operateType REGEXP '更新' ");
			    else if(queryType.endsWith("2"))
			    	sql.append(" AND AC.operateType REGEXP '轮换' ");	
			    else if(queryType.endsWith("3"))
			    	sql.append(" AND AC.operateType REGEXP '更新|轮换' ");		
			}
			else {	
				sql.append(" AND AC.operateType REGEXP '更新|轮换' ");
			}
			if(StringUtil.isNotEmpty(fromdate) && StringUtil.isNotEmpty(todate)){
				//将String类型的from 和 to 转化为date型数据				
				java.util.Date from = MyDateFormat.changeLongStringToDate(fromdate);
				java.util.Date to = MyDateFormat.changeLongStringToDate(todate);
				sql.append(" AND (AC.operateTime >= '" + MyDateFormat.changeToSqlDate(from) + "' " +
						"AND AC.operateTime <= '" + MyDateFormat.changeToSqlDate(to)+ "') ");
			}
			if(StringUtil.isNotEmpty(productModel))
					sql.append(" AND P.productModel = '" + productModel + "'");	
			if(StringUtil.isNotEmpty(jds))
				sql.append(" AND P.jds REGEXP '" + jds + "'");				
			search = sql.toString();
			this.pstmt = this.conn.prepareStatement(search);		
			ResultSet rest = this.pstmt.executeQuery();
			while(rest.next())  {       //循环遍历查询结果集，封装在map中， map再存放到list中   					
				Map<String,String> map = new HashMap<String,String>();						
				map.put("productModel", rest.getString("productModel"));     //1.产品型号
				map.put("contractId", rest.getString("contractId"));         //2.合同编号
				map.put("operateType", rest.getString("operateType"));       //3.产品状态
				map.put("operateTime", rest.getString("operateTime"));       //4.操作时间
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(null, this.pstmt);
		}
		return list;
	}
	
	/**
	 * @author austin
	 * 业务查询→按合同统计查询→操作明细查询
	 * */
	public List<Map<String, String>> selectContractAccountById(HashMap<String, Object> condition) throws Exception{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();

		String contractId = (String)condition.get("contractId");
		String fromdate = (String)condition.get("fromdate");
		String todate = (String)condition.get("todate");
		String queryType = (String)condition.get("queryType");
		String jds =(String)condition.get("jds");
		
		try {	
			this.conn = DBConnection.getConn();	
			//在库轮换更新明细查询
			StringBuilder sql = new StringBuilder();
			String search = "";               //生成的查询语句
			sql.append("SELECT C.contractId, P.deviceNo, P.proStatus " +
					"FROM qy_contract C LEFT OUTER JOIN qy_product P ON C.contractId = P.contractId ");
			
			if(StringUtil.isNotEmpty(fromdate) && StringUtil.isNotEmpty(todate)) {
				//将String类型的from 和 to 转化为date型数据				
				java.util.Date from = MyDateFormat.changeLongStringToDate(fromdate);
				java.util.Date to = MyDateFormat.changeLongStringToDate(todate);
				sql.append("LEFT OUTER JOIN qy_account AC ON P.productId = AC.productId " +
						"WHERE (P.productId ,AC.id) IN(SELECT P.productId, MAX(AC.id) FROM qy_product P, qy_account AC " +
						"WHERE P.productId = AC.productId GROUP BY P.productId ) ");					
				sql.append(" AND (AC.operateTime >= '" + MyDateFormat.changeToSqlDate(from) + "' " +
						"AND AC.operateTime <= '" + MyDateFormat.changeToSqlDate(to)+ "')");
				
				if(StringUtil.isNotEmpty(queryType)){
					if(queryType.endsWith("1"))
						sql.append(" AND P.proStatus REGEXP '更新' ");
				    else if(queryType.endsWith("2"))
				    	sql.append(" AND P.proStatus REGEXP '轮换' ");	
				    else if(queryType.endsWith("3"))
				    	sql.append(" AND P.proStatus REGEXP '更新|轮换' ");		
				}				
				if(StringUtil.isNotEmpty(contractId))
					sql.append(" AND C.contractId = '" + contractId + "'");				
			}
			else{				
				if(StringUtil.isNotEmpty(queryType)){
					if(queryType.endsWith("1"))
						sql.append(" WHERE P.proStatus REGEXP '更新' ");
				    else if(queryType.endsWith("2"))
				    	sql.append(" WHERE P.proStatus REGEXP '轮换' ");	
				    else if(queryType.endsWith("3"))
				    	sql.append(" WHERE P.proStatus REGEXP '更新|轮换' ");				    			
				}
				else {	
					sql.append(" WHERE P.proStatus REGEXP '更新|轮换' ");
				}
				if(StringUtil.isNotEmpty(contractId))
					sql.append(" AND C.contractId = '" + contractId + "'");				
			}			
			if(StringUtil.isNotEmpty(jds))
				sql.append(" AND P.jds REGEXP '" + jds + "'");				
			search = sql.toString();
			this.pstmt = this.conn.prepareStatement(search);		
			ResultSet rest = this.pstmt.executeQuery();
			while(rest.next())  {       //循环遍历查询结果集，封装在map中， map再存放到list中   					
				Map<String,String> map = new HashMap<String,String>();			
				map.put("contractId", rest.getString("contractId"));    //1.合同编号
				map.put("deviceNo", rest.getString("deviceNo"));        //2.设备码
				map.put("means", rest.getString("proStatus"));          //3.轮换更新状态
				list.add(map);
			}				
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(null, this.pstmt);
		}
		return list;
	}	
	
	/**
	 * 查询有多少产品已经更新出库并且已经快要到期，返回到期的产品条数
	 * 产品一共让借出去几天 - (现在 - 产品借出去的时间) < 更新出库提醒时间
	 * @param storageDays 更新出库提醒时限
	 * @param now 现在
	 * @author HuangKai
	 * @return
	 */
	public int getUpdateReturnAlarmNumber(int storageDays, Date now)
	{
		String sql = "SELECT A.operateTime, O.borrowLength FROM qy_product AS P, " +
				"qy_account AS A, qy_outapply AS O, qy_outproductrelation AS R " +
				"WHERE P.productId = A.productId AND P.productId = R.productId AND " +
				"R.outId = O.outId AND A.operateType = '更新出库' " +
				"AND O.outMeans = '更新出库'";
		int count = 0;
		try{
			this.conn = DBConnection.getConn();
			this.pstmt = conn.prepareStatement(sql);
			this.rs = this.pstmt.executeQuery();
			while(rs.next()){
				Date operate = rs.getDate(1);
				int borrow = rs.getInt(2);
				if((borrow*30 - MyDateFormat.javaDateMills(operate, now)/(1000*3600*24)) < storageDays)
					count++;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return count;
		
	}
	
	public int getOutAlarmDays()
	{
		String sql = "select out_ahead_days from qy_parameter_configuration";
		int aheadDays = 0;
		try {	
			this.conn = DBConnection.getConn();
			this.pstmt = conn.prepareStatement(sql);
			this.rs = this.pstmt.executeQuery();
			if(rs.next())
				aheadDays = rs.getInt(1);
		}catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return aheadDays;
	}
}
