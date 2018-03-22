package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class ContractDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public ContractDAO() {

	}

	public ContractDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @author austin
	 * 业务查询→按合同统计查询
	 * */
	/**
	 * searchAllUserByPage 分页显示所有的按合同统计查询结果
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> contractQueryStatistics(Map<String,String> map)throws Exception{
		conn=DBConnection.getConn();
		String InStausRegexp1=".*已入库.*";		
		String InStausRegexp2=".*[出库待审核].*";	//匹配入库正则表达式
		String InStausRegexp3="[已].*[入库]";
		String outStatusRegexp1=".*已出库.*";		//匹配出库正则表达式
		String outStatusRegexp2=".*[入库待审核]";
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		Map<String, Double[]> statistic = new HashMap<String,Double[]>();	//double数组中的的第一个元素存放的是在库的产品的数量，第二个存放的出库产品的数量，第三个存放的是产品的单价
		Map<String,String> totalAmount=new HashMap<String,String>();
		double allProductAmount = 0;
		double allcontractAmount=0;
		double allInstore=0;
		double allOutstore=0;
		int totalNumber=0;
		List<String> param = new ArrayList<String>();
		String sql = "select qy_contract.contractId,qy_product.productName,qy_product.productPrice,qy_product.productType"
				+ ",qy_contract.totalNumber,qy_contract.contractPrice,qy_product.proStatus,qy_product.productModel,"
				+ "qy_product.productUnit,qy_product.deliveryTime,qy_product.manufacturer,qy_product.keeper,qy_contract.JDS,"
				+ "qy_contract.signDate,qy_account.operateTime from qy_contract inner join qy_product,qy_account where qy_contract.contractId=qy_product.contractId and qy_product.productId=qy_account.productId and qy_product.proStatus is not null";
		for (String keys : map.keySet()) {
			String part = null;
			if (keys.equals("contractId")&&map.get("contractId")!=null&&!map.get("contractId").equals("")&&!map.get("contractId").equals("null")) {
				part = " and qy_contract.contractId regexp" + "'" + map.get(keys) + "'"+" and "+"qy_product.contractId regexp" + "'" + map.get(keys) +"'";
				param.add(part);
			}
			if (keys.equals("JDS")&&map.get("JDS")!=null&&!map.get("JDS").equals("")&&!map.get("JDS").equals("null")) {
				part = " and qy_contract.JDS regexp" + "'" + map.get(keys) + "'";
				param.add(part);
			}
			if (keys.equals("signDate")&&map.get("signDate")!=null&&!map.get("signDate").equals("")&&!map.get("signDate").equals("null")) {
				part = " and qy_contract.signDate regexp" + "'" + map.get(keys) + "'"+" and "+"qy_product.signTime regexp" + "'" + map.get(keys) + "'";
				param.add(part);
			}

		}

		if (param.size() == 1) {
			sql = sql + param.get(0);
		} else if (param.size() == 2) {
			sql = sql + param.get(0) + param.get(1);
		} else if(param.size() ==3) {
			sql = sql + param.get(0) + param.get(1) + param.get(2);
		}
		else
		{;}
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				totalNumber= rs.getInt("totalNumber");  //订货总量
				String contractId=rs.getString("contractId");  //合同编号
				String productName=rs.getString("productName");  //产品名称
				String productModel=rs.getString("productModel");
				String productType=rs.getString("productType");
				String productUnit=rs.getString("productUnit");
				double contractPrice=rs.getDouble("contractPrice"); //合同金额
				String proStatus= rs.getString("proStatus"); //产品状态
				String productPrice=rs.getString("productPrice");  //单价
				String deliveryTime=rs.getString("deliveryTime"); //入库时间
				String manufacturer=rs.getString("manufacturer");  //承制单位
				String keeper= rs.getString("keeper");  //代储单位
				String JDS=rs.getString("JDS");  //军代室
				String signDate=String.valueOf(rs.getDate("signDate"));
				String operateTime=String.valueOf(rs.getDate("operateTime"));
				/*统计合同的总金额和不同合同中的产品出库数量，库存的总价值，库存数量*/
				String keys=contractId+"	"+productName+"	"+manufacturer+"	"+keeper+"	"+deliveryTime+"	"+signDate+"	"+JDS+"	"+productModel+"	"+productType+"	"+productUnit+"	"+operateTime; //加产品型号，产品类型，产品单元是为了后面的操作详情。
				if(!statistic.containsKey(keys))
				{

					statistic.put(keys,new Double[5]);
					Double[] arr=statistic.get(keys);
					arr[0]=0.0;  	//在库数量
					arr[1]=0.0;
					arr[2]=1.0;		//设备总量
					arr[3]=Double.valueOf(productPrice);
					arr[4]=contractPrice;

					if(Pattern.compile(InStausRegexp1).matcher(proStatus).matches()||Pattern.compile(InStausRegexp2).matcher(proStatus).matches()||Pattern.compile(InStausRegexp3).matcher(proStatus).matches())//在库
					{
						arr[0]=1.0;
					}
					if(Pattern.compile(outStatusRegexp1).matcher(proStatus).matches()||Pattern.compile(outStatusRegexp2).matcher(proStatus).matches())//出库
					{
						arr[1]=1.0;
					}
				}

				else
				{
					Double[] arr=statistic.get(keys);
					arr[2]+=1.0;
					if(Pattern.compile(InStausRegexp1).matcher(proStatus).matches()||Pattern.compile(InStausRegexp2).matcher(proStatus).matches()||Pattern.compile(InStausRegexp3).matcher(proStatus).matches())//在库
					{
						arr[0]+=1.0;
					}
					if(Pattern.compile(outStatusRegexp1).matcher(proStatus).matches()||Pattern.compile(outStatusRegexp2).matcher(proStatus).matches())//出库
					{
						arr[1]+=1.0;
					}
				}


			}
			/*
			 * 计算设备金额，设备数量（即库存金额，库存数量）
			 * */
			//double allInstoreAccount=0.0;
			for(String info:statistic.keySet())
			{
				Map<String,String> result=new HashMap<String,String>();
				double inStore,outStore,inStoreAmount;
				String[] arr_info=info.split("	");
				Double[] arr=statistic.get(info);
				inStore=arr[0];  //此字段代表产品的库存数量
				outStore=arr[1];  
				//totalNumber=arr[2];			//此字段代表订货数量
				if(inStore!=0)
				{
					inStoreAmount=arr[0]*arr[3];  //此字段代表产品的库存金额
					allProductAmount+=inStoreAmount;  //计算库存设备总金额
				}
				else
				{
					inStoreAmount=0;
				}
				result.put("contractId", arr_info[0]);
				result.put("productName", arr_info[1]);
				result.put("productPrice", String.valueOf(arr[3]));
				result.put("totalNumber", String.valueOf(totalNumber));
				result.put("contractPrice", String.valueOf(arr[4]));
				result.put("equipNumber", String.valueOf(arr[2]));			//equipNumber指的是库存总量和出库数量之和
				result.put("inStoreAmount", String.valueOf(inStoreAmount));
				result.put("outStore", String.valueOf(outStore));
				result.put("deliveryTime", arr_info[4]);
				result.put("manufacturer", arr_info[2]);
				result.put("keeper", arr_info[3]);
				result.put("JDS", arr_info[6]);
				result.put("signDate", arr_info[5]);
				result.put("productModel",arr_info[7]);
				result.put("productType",arr_info[8]);
				result.put("productUnit",arr_info[9]);
				result.put("operateTime", arr_info[10]);
				T.add(result);	
			}

			/*
			 * 计算合同总金额,库存总量，出库总量
			 * */

			for(String info:statistic.keySet())
			{
				Double[] data=statistic.get(info);
				allcontractAmount+=data[4];
				allInstore+=data[0];
				allOutstore+=data[1];

			}
			totalAmount.put("allcontractAmount", String.valueOf(allcontractAmount));
			totalAmount.put("allProductAmount", String.valueOf(allProductAmount));
			totalAmount.put("allInstore", String.valueOf(allInstore));
			totalAmount.put("allOutstore", String.valueOf(allOutstore));
			T.add(totalAmount);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return T;
	}

	public List<Map<String,String>> contractOperationDetail(Map<String,String> info)
	{  
		List<Map<String,String>> T=new ArrayList<Map<String,String>>();
		String sql="select qy_inapply.num,qy_inapply.inMeans,qy_product.PMNM,qy_product.productCode,qy_product.deviceNo,qy_inapply.execDate,qy_inapply.remark from qy_inapply inner join qy_product,qy_inproductrelation where qy_product.contractId"+"="+"'"+info.get("contractId")+"'"
				  +" and qy_inapply.contractId=qy_product.contractId and qy_inapply.inId=qy_inproductrelation.inId and qy_product.productId=qy_inproductrelation.productId " +
			  		"union all select qy_outapply.num,qy_outapply.outMeans,qy_product.PMNM,qy_product.productCode,qy_product.deviceNo,qy_outapply.execDate,qy_outapply.remark from qy_outapply inner join qy_product,qy_outproductrelation where qy_product.contractId"+"="+"'"+info.get("contractId")+"'"
			  +" and qy_outapply.contractId=qy_product.contractId and qy_outapply.outId=qy_outproductrelation.outId and qy_product.productId=qy_outproductrelation.productId GROUP BY qy_product.productId";

		try {

			pstmt = conn.prepareStatement(sql);	
			rs = pstmt.executeQuery();			
			while(rs.next())
			{
				Map<String,String> result=new HashMap<String,String>();
				String inMeans=null;
				String outMeans=null;
				try{
					inMeans=rs.getString("inMeans");
					outMeans=rs.getString("outMeans");
					result.put("operationType", outMeans);
					result.put("operationType", inMeans);
				}catch(Exception e)
				{
					try{
						inMeans=rs.getString("inMeans");
						result.put("operationType", inMeans);
					}catch(Exception e1)
					{
						outMeans=rs.getString("outMeans");
						result.put("operationType", outMeans);
					}

				}

				result.put("productModel", info.get("productModel"));
				result.put("productType",  info.get("productType"));
				result.put("PMNM", rs.getString("PMNM"));
				result.put("unitName", info.get("productUnit"));
				result.put("manufacturer", info.get("manufacturer"));
				result.put("keeper", info.get("keeper"));
				result.put("num", String.valueOf(rs.getInt("num")));
				result.put("productCode", rs.getString("productCode"));
				result.put("deviceNo", rs.getString("deviceNo"));
				result.put("execDate", String.valueOf(rs.getDate("execDate")));
				result.put("remark", rs.getString("remark"));
				System.out.println(rs.getString("remark"));
				T.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally
		{
			DBConnection.close(conn, pstmt, rs);
		}
		return T;

	}

	/**
	 * @author HuangKai
	 * 轮换更新查询
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> selectBorrowAndUpdate2(HashMap<String, Object> condition){
		
		List<Map<String,Object>> resultlist = new ArrayList<Map<String, Object>>();
		String productModel = (String) condition.get("productModel");
		String fromdate = (String) condition.get("fromdate");
		String todate = (String) condition.get("todate");
		String queryType = (String) condition.get("queryType");
		String ownedUnit = (String) condition.get("ownedUnit");
		String sql ="SELECT P.productId, P.productModel, P.productName, P.productUnit, " +
				"P.measureUnit, P.manufacturer, P.keeper, P.productPrice, P.`status`, " +
				"P.deviceNo, A.operateTime FROM qy_product AS P, qy_account AS A WHERE " +
				"P.`status` REGEXP '轮换|更新' AND P.productId = A.productId AND " +
				"A.operateType = P.`status`";
		
		if(!StringUtil.isEmpty(fromdate))
			sql += "AND A.operateTime > '" + fromdate + "' ";
		if(!StringUtil.isEmpty(todate))
			sql += "AND A.operateTime < '" + todate + "' ";
		if(!StringUtil.isEmpty(productModel))
			sql += " AND P.productModel REGEXP '" + productModel + "' ";
		if(!StringUtil.isEmpty(queryType))
			sql += "AND p.status REGEXP '" + queryType + "' ";
		if(!StringUtil.isEmpty(ownedUnit) && !ownedUnit.equalsIgnoreCase("All"))
			sql += "AND P.ownedUnit = '" + ownedUnit + "' ";
//		System.out.println(sql);
		try {	
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			this.rs = this.pstmt.executeQuery();
			while(rs.next())
			{
				String productId = rs.getString("productId");
				String productModel2 = rs.getString("productModel");
				String productName = rs.getString("productName");
				String productUnit = rs.getString("productUnit");
				String measureUnit = rs.getString("measureUnit");
				String manufacturer = rs.getString("manufacturer");
				String keeper = rs.getString("keeper");
				String productPrice = rs.getString("productPrice");
				String status = rs.getString("status");
				String deviceNo = rs.getString("deviceNo");
				Date operateTime = MyDateFormat.changeToJavaDate(rs.getDate("operateTime"));
				if(status.split(";").length == 1)
				{
					Map<String,Object> one = new HashMap<String, Object>();
					one.put("productId", productId);
					one.put("productModel", productModel2);
					one.put("productName", productName);
					one.put("productUnit", productUnit);
					one.put("measureUnit", measureUnit);
					one.put("manufacturer", manufacturer);
					one.put("keeper", keeper);
					one.put("productPrice", productPrice);
					one.put("status", status);
					one.put("deviceNo", deviceNo);
					one.put("operateTime", operateTime);
					resultlist.add(one);
				}
				else{
					Map<String,Object> one = new HashMap<String, Object>();
					Map<String,Object> two = new HashMap<String, Object>();
					String[] statuses = status.split(";");
					one.put("productId", productId);
					one.put("productModel", productModel2);
					one.put("productName", productName);
					one.put("productUnit", productUnit);
					one.put("measureUnit", measureUnit);
					one.put("manufacturer", manufacturer);
					one.put("keeper", keeper);
					one.put("productPrice", productPrice);
					one.put("status", statuses[0]);
					one.put("deviceNo", deviceNo);
					one.put("operateTime", operateTime);
					resultlist.add(one);
					
					two.put("productId", productId);
					two.put("productModel", productModel2);
					two.put("productName", productName);
					two.put("productUnit", productUnit);
					two.put("measureUnit", measureUnit);
					two.put("manufacturer", manufacturer);
					two.put("keeper", keeper);
					two.put("productPrice", productPrice);
					two.put("status", statuses[1]);
					two.put("deviceNo", deviceNo);
					two.put("operateTime", operateTime);
					resultlist.add(two);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		
		return resultlist;
	}
	
public List<Map<String, Object>> selectBorrowAndUpdate(HashMap<String, Object> condition){
		List<Map<String,Object>> resultlist = new ArrayList<Map<String, Object>>();
		List<Map<String,Object>> resultlist2 = new ArrayList<Map<String, Object>>();
		String productModel = (String) condition.get("productModel");
		String fromdate = (String) condition.get("fromdate");
		String todate = (String) condition.get("todate");
		String queryType = (String) condition.get("queryType");
		String ownedUnit = (String) condition.get("ownedUnit");
		String sql ="SELECT P.productId, P.`status` " +
				"FROM qy_product AS P WHERE " +
				"P.`status` REGEXP '轮换|更新' AND p.proStatus NOT REGEXP '待审核' ";
		String sql2 = "";
		if(!StringUtil.isEmpty(productModel))
			sql += " AND P.productModel REGEXP '" + productModel + "' ";
		if(!StringUtil.isEmpty(queryType))
			sql += "AND p.status REGEXP '" + queryType + "' ";
		if(!StringUtil.isEmpty(ownedUnit) && !ownedUnit.equalsIgnoreCase("All"))
			sql += "AND P.ownedUnit = '" + ownedUnit + "' ";
		//System.out.println("??======"+sql);
		try {	
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			this.rs = this.pstmt.executeQuery();
			while(rs.next())
			{
				String productId = rs.getString("productId");
				String status = rs.getString("status");
				if(status.split(",").length == 1)
				{
					Map<String,Object> one = new HashMap<String, Object>();
					one.put("productId", productId);
					one.put("status", status);
					resultlist.add(one);
				}
				else{
					Map<String,Object> one = new HashMap<String, Object>();
					Map<String,Object> two = new HashMap<String, Object>();
					String[] statuses = status.split(",");
					if(statuses[0].contains(queryType))
					{
						one.put("productId", productId);
						one.put("status", statuses[0]);
						resultlist.add(one);
					}
						
					
					if(statuses[1].contains(queryType))
					{
						two.put("productId", productId);
						two.put("status", statuses[1]);
						resultlist.add(two);
					}
				}
			}
			sql2 = "SELECT P.productId, P.productModel, P.productName, P.productUnit, " +
					"P.measureUnit, P.manufacturer, P.keeper, P.productPrice, P.`status`, " +
					"P.deviceNo, A.operateTime FROM qy_product AS P, qy_account AS A WHERE " +
					"P.productId = A.productId AND (";
			HashMap<String, HashMap<String, Object>> resultMap = new HashMap<String, HashMap<String,Object>>();
			for(int i = 0; i < resultlist.size(); i++)
			{
				HashMap<String, Object> one = new HashMap<String, Object>();
				String productId = (String) resultlist.get(i).get("productId");
				String status = (String) resultlist.get(i).get("status");
				one.put("productId", productId);
				one.put("status", status);
				resultMap.put(productId, one);
				if(i == 0)
					sql2 += "(P.productId = '" + productId + "' AND A.operateType = '" + status + "') ";
				else
					sql2 += "OR (P.productId = '" + productId + "' AND A.operateType = '" + status + "') ";
			}
			sql2 += ")";
			if(resultlist.size() == 0)
				return resultlist2;
			if(!StringUtil.isEmpty(fromdate))
				sql2 += " AND A.operateTime > '" + fromdate + "' ";
			if(!StringUtil.isEmpty(todate))
				sql2 += " AND A.operateTime < '" + todate + "' ";
			System.out.println(sql2);
			sql2+="ORDER BY A.operateType DESC";
			this.pstmt = this.conn.prepareStatement(sql2);
			this.rs = this.pstmt.executeQuery();
			while(rs.next())
			{
				String productId = rs.getString("productId");
				String productModel2 = rs.getString("productModel");
				String productName = rs.getString("productName");
				String productUnit = rs.getString("productUnit");
				String measureUnit = rs.getString("measureUnit");
				String manufacturer = rs.getString("manufacturer");
				String keeper = rs.getString("keeper");
				String productPrice = rs.getString("productPrice");
				String deviceNo = rs.getString("deviceNo");
				Date operateTime = MyDateFormat.changeToJavaDate(rs.getDate("operateTime"));
				HashMap<String,Object> one = resultMap.get(productId);
				System.out.println("one:" + one);
				one.put("productModel", productModel2);
				one.put("productName", productName);
				one.put("productUnit", productUnit);
				one.put("measureUnit", measureUnit);
				one.put("manufacturer", manufacturer);
				one.put("keeper", keeper);
				one.put("productPrice", productPrice);
				one.put("deviceNo", deviceNo);
				one.put("operateTime", operateTime);
				resultlist2.add(one);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		
		return resultlist2;
	}
	
	
	
	/**
	 * @author HuangKai
	 * 轮换更新查询
	 * @param condition
	 * @return
	 */
	public int selectBorrowAndUpdateCount(HashMap<String, Object> condition){
		
		int count = 0;
		String productModel = (String) condition.get("productModel");
		String fromdate = (String) condition.get("fromdate");
		String todate = (String) condition.get("todate");
		String queryType = (String) condition.get("queryType");
		String sql = "SELECT count(*) " +
				"FROM qy_product AS P, qy_account AS A WHERE P.`status` REGEXP '轮换|更新' " +
				"AND P.productId = A.productId ";
		if(!StringUtil.isEmpty(productModel))
			sql += " AND P.productModel REGEXP '" + productModel + "' ";
		if(!StringUtil.isEmpty(queryType))
			sql += "AND p.status REGEXP '" + queryType + "' ";
		if(!StringUtil.isEmpty(fromdate))
			sql += "AND A.operateTime > '" + fromdate + "' ";
		if(!StringUtil.isEmpty(todate))
			sql += "AND A.operateTime < '" + todate + "' ";
		try {	
			this.conn = DBConnection.getConn();
			this.pstmt = this.conn.prepareStatement(sql);
			this.rs = this.pstmt.executeQuery();
			if(rs.next())
				count = rs.getInt(1);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return count;
	}
	
	
	/**
	 * 新增合同
	 * @param contract
	 * @return
	 * @throws Exception 
	 */
	public boolean saveContract(Contract contract) {
		boolean flag = false;
		try {
			//2015.06.09 修改结构 conn统一在dao的每个函数体里开，关
			this.conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_contract(contractId,totalNumber,"
					+ "contractPrice,JDS,signDate,attachment,buyer,ownedUnit) VALUES(?,?,?,?,?,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, contract.getContractId());
			this.pstmt.setInt(2, contract.getTotalNumber());
			this.pstmt.setDouble(3, contract.getContractPrice());
			this.pstmt.setString(4, contract.getJDS());
			this.pstmt.setTimestamp(5, MyDateFormat.changeToSqlDate(contract.getSignDate()));
			this.pstmt.setString(6, contract.getAttachment());
			this.pstmt.setString(7, contract.getBuyer());
			this.pstmt.setString(8, contract.getOwnUnit());
			int count = this.pstmt.executeUpdate();
			if(count > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 按合同号，型号/单元，产品名称，签订日期查询合同
	 * 
	 * @param condition
	 * @return
	 */
	public List<Contract> queryContract(Map<String, String> condition,
			int curPageNum, int pageSize) {
		List<Contract> result = new ArrayList<Contract>();
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"SELECT DISTINCT c.contractId,c.totalNumber,c.contractPrice,c.JDS,c.signDate,c.attachment,c.buyer FROM qy_contract c");
			// 设置查询sql条件
			if (condition.size() > 0) {
				if (condition.get("productModel") != null
						|| condition.get("productUnit") != null) {
					sql.append(",qy_product p AND c.contractId = p.contractId");
				}
				if (condition.get("productModel") != null) {
					sql.append(" AND productModel REGEXP '"
							+ condition.get("productModel") + "'");
					condition.remove("productModel");
				}
				if (condition.get("productUnit") != null) {
					sql.append(" AND productUnit REGEXP '"
							+ condition.get("productUnit") + "'");
					condition.remove("productUnit");
				}
				if (condition.get("keeper") != null) {
					sql.append(" AND c.ownedUnit IN("
							+ condition.get("keeper") + ")");
					condition.remove("keeper");
				}
				for (String key : condition.keySet()) {
					if (!"".equals(condition.get(key))
							&& condition.get(key) != null) {
						sql.append(" AND " + key + " REGEXP '"
								+ condition.get(key) + "'");
					}
				}
			}
			sql.append(" ORDER BY c.signDate DESC LIMIT ?,?");
			// System.out.println("sql:\t"+sql);
			pstmt = conn.prepareStatement(sql.toString().replaceFirst("AND",
					"WHERE"));
			this.pstmt.setInt(1, (curPageNum - 1) * pageSize);
			this.pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Contract contract = new Contract();
				contract.setContractId(rs.getString("contractId"));
				contract.setContractPrice(rs.getDouble("contractPrice"));
				contract.setTotalNumber(rs.getInt("totalNumber"));
				contract.setJDS(rs.getString("JDS"));
				contract.setSignDate(rs.getDate("signDate"));
				contract.setBuyer(rs.getString("buyer"));
				contract.setAttachment(rs.getString("attachment"));
				result.add(contract);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}
	
	/**
	 * 获取总金额
	 * @param condition
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 */
	public double getContractPriceSum(Map<String, String> condition) {
		double contractPriceSum = 0.0;
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"SELECT c.contractPrice FROM qy_contract c");
			// 设置查询sql条件
			if (condition.size() > 0) {
				if (condition.get("productModel") != null
						|| condition.get("productUnit") != null) {
					sql.append(",qy_product p AND c.contractId = p.contractId");
				}
				if (condition.get("productModel") != null) {
					sql.append(" AND productModel REGEXP '"
							+ condition.get("productModel") + "'");
					condition.remove("productModel");
				}
				if (condition.get("productUnit") != null) {
					sql.append(" AND productUnit REGEXP '"
							+ condition.get("productUnit") + "'");
					condition.remove("productUnit");
				}
				if (condition.get("ownedUnit") != null) {
					sql.append(" AND c.ownedUnit REGEXP '"
							+ condition.get("ownedUnit") + "'");
					condition.remove("ownedUnit");
				}
				for (String key : condition.keySet()) {
					if (!"".equals(condition.get(key))
							&& condition.get(key) != null) {
						sql.append(" AND " + key + " REGEXP '"
								+ condition.get(key) + "'");
					}
				}
			}
			sql.append(" GROUP BY c.contractId");
			pstmt = conn.prepareStatement(sql.toString().replaceFirst("AND",
					"WHERE"));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				contractPriceSum += rs.getDouble(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return contractPriceSum;
	}

	/**
	 * 按合同Id查询合同
	 * @param contractId
	 * @return
	 */
	public Contract queryContractById(String contractId) {
		Contract contract = new Contract();
		try{
			conn =DBConnection.getConn();
			String sql = "select * from qy_contract where contractId =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				contract.setContractId(rs.getString("contractId"));
				contract.setAttachment(rs.getString("attachment"));
				contract.setBuyer(rs.getString("buyer"));
				contract.setContractPrice(rs.getDouble("contractPrice"));
				contract.setJDS(rs.getString("JDS"));
				contract.setSignDate(rs.getDate("signDate"));
				contract.setTotalNumber(rs.getInt("totalNumber"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt,rs);
		}
		return contract;
	}

	/**
	 * 更新合同
	 * @param contract
	 * @return
	 */
	public boolean UpdateContract(Contract contract) {
		boolean flag = false;
		try{
			conn = DBConnection.getConn();
			String sql = "update qy_contract set totalNumber=?,contractPrice=?,JDS=?"
					+ ",signDate=?,buyer=? where contractId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, contract.getTotalNumber());
			pstmt.setDouble(2, contract.getContractPrice());
			pstmt.setString(3, contract.getJDS());
			pstmt.setTimestamp(4, MyDateFormat.changeToSqlDate(contract.getSignDate()));
			pstmt.setString(5, contract.getBuyer());
			pstmt.setString(6, contract.getContractId());
			int count = pstmt.executeUpdate();
			if(count == 1) {
				flag = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 删除合同,并删除对应产品
	 * @param ContractId
	 * @return
	 */
	public boolean DeleteContract(String contractId) {
		boolean flag = false;
		try{
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			String sql = "delete from qy_contract where contractId =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			int count1 = pstmt.executeUpdate();
			//删除合同时要将相应产品一起修改
			String sql1 = "update qy_product set proStatus='合同销毁' where contractId =?";
			PreparedStatement pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setString(1, contractId);
			pstmt1.executeUpdate();
			conn.commit();
			if(count1 >0) {
				flag = true;
			}
		}catch(Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 根据合同编号查询附件路径
	 * @param contractId
	 * @return
	 */
	public String findAttachByContractId(String contractId) {
		String path = "";
		try{
			conn = DBConnection.getConn();
			String sql ="select attachment from qy_contract where contractId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				path = rs.getString("attachment");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt,rs);
		}
		return path;
	}

	/**
	 * 删除附件
	 * @param contractId
	 * @return
	 */
	public boolean deleteAttah(String contractId,String attchment) {
		boolean flag = false;
		try{
			conn = DBConnection.getConn();
			String sql = "update qy_contract set attachment = ? where contractId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(2, contractId);
			pstmt.setString(1, attchment);
			int count = pstmt.executeUpdate();
			if(count == 1) {
				flag = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 查询总条数
	 * 
	 * @return
	 */
	public int querySum(Map<String, String> condition) {
		int count1 = 0;
		try {
			conn = DBConnection.getConn();
			StringBuffer sql = new StringBuffer(
					"select count(distinct c.contractId) from qy_contract c,qy_product p");
			// 设置查询sql条件
			if (condition.size() > 0) {
				if (condition.get("productModel") != null
						|| condition.get("productUnit") != null) {
					sql.append(" AND c.contractId = p.contractId");
				}
				if (condition.get("productModel") != null) {
					sql.append(" AND productModel REGEXP '"
							+ condition.get("productModel") + "'");
					condition.remove("productModel");
				}
				if (condition.get("productUnit") != null) {
					sql.append(" AND productUnit REGEXP '"
							+ condition.get("productUnit") + "'");
					condition.remove("productUnit");
				}
				if (condition.get("ownedUnit") != null) {
					sql.append(" AND c.ownedUnit REGEXP '"
							+ condition.get("ownedUnit") + "'");
					condition.remove("ownedUnit");
				}
				for (String key : condition.keySet()) {
					if (!"".equals(condition.get(key))
							&& condition.get(key) != null) {
						sql.append(" AND " + key + " REGEXP '"
								+ condition.get(key) + "'");
					}
				}
			}
			pstmt = conn.prepareStatement(sql.toString().replaceFirst("AND",
					"WHERE"));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count1 = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return count1;
	}
	
	/**
	 * 根据product id 查询contract表的所有字段信息
	 * @return contract表,其中第一行是数据库表的标题行
	 * @author liangyihuai
	 */
	public List<ArrayList<String>> queryContracts(List<String> productIDs){
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		Connection conn = null;
		
		String contract_sql = "Select * From qy_contract Where contractId IN ("
				+ "SELECT contractId FROM qy_product WHERE productId=?) GROUP BY contractId,ownedUnit";
		
		List<ArrayList<String>> contracts = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> contract_headline = new ArrayList<String>();
		//填充标题
		contracts.add(contract_headline);
		
		try {
			conn = DBConnection.getConn();
			ps1 = conn.prepareStatement(contract_sql);
			
			int len = productIDs.size();
			for(int i = 0; i < len; i++){
				String productID = productIDs.get(i);
				//查询合同表
				ps1.setString(1, productID);
				rs1 =  ps1.executeQuery();
				
				while(rs1.next()){
					ArrayList<String> contract = new ArrayList<String>();
			        
					//得到结果集(rs)的结构信息，比如字段数、字段名等 
					ResultSetMetaData md = rs1.getMetaData();  
					int columnCount = md.getColumnCount(); 	
			        for(int k = 1; k <= columnCount; k++){
			        	contract.add(rs1.getString(k));
			        	//增加标题
			        	if(contract_headline.size() != columnCount)
			        		contract_headline.add(md.getColumnName(k));
			        }
			        if(!isContains(contracts, contract)) contracts.add(contract);
				}//end while
			}//end for
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, ps1, rs1);
		}
		return contracts;
	}
	
	/**
	 * 判断dyadic是否包含list
	 * @param dyadic
	 * @param list
	 * @return
	 */
	private boolean isContains(List<ArrayList<String>> dyadic, ArrayList<String> list){
		int len = dyadic.size();
		for(int i = 0; i < len; i++){
			ArrayList<String> tempList = dyadic.get(i);
			//contractId、ownedUnit
			if(list.get(0).equals(tempList.get(0)) && list.get(7).equals(tempList.get(7))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 按合同统计产品出入库数量
	 * 方法-》使用多个视图
	 * @param contractId
	 * @return
	 */
	public List<Map<String, String>> contractOperationDetail(String contractId) {
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		String sql = "SELECT a.productName,a.productModel,a.productUnit,a.productPrice,a.productNum,IFNULL(b.productNum,0) AS productInNum,IFNULL(c.productNum,0) AS productOutNum,a.operateTime,a.manufacturer,a.keeper"
				+ " FROM temp_all_product_nostatus a"
				+ " LEFT JOIN temp_in_product b"
				+ " ON a.operateTime=b.operateTime AND a.productModel=b.productModel AND a.productUnit=b.productUnit"
				+ " LEFT JOIN temp_out_product c"
				+ " ON a.operateTime=c.operateTime AND a.productModel=c.productModel AND a.productUnit=c.productUnit" + " WHERE a.contractId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, String> result = new HashMap<String, String>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					result.put(metaData.getColumnName(i), rs.getString(i));
				}
				T.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return T;
	}
	
	/**
	 * 查看合同号是否重复
	 */
	public boolean isExistCid(String contractId) {
		boolean isExist = false;
		try {
			String sql ="SELECT * FROM qy_contract WHERE contractId=?";
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, contractId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				isExist = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return isExist;
	}
	
	/**
	 * 通过产品型号、单元和入库时间获取产品ids
	 * @return
	 * @author Liuhs
	 */
	public List<HashMap<String, String>> getProdIdByMoUnInTime(HashMap<String, String> condition){
		List<HashMap<String, String>> outInfoByProdList=new ArrayList<HashMap<String,String>>();
		StringBuffer sb = new StringBuffer("SELECT a.productId FROM qy_account a INNER JOIN qy_product b ON a.productId=b.productId");
		for (Entry<String, String> item : condition.entrySet()) {
			sb.append(" AND "+item.getKey()+"='"+item.getValue()+"'");
		}
		String sql=sb.toString().replaceFirst("AND", "WHERE");
		sb=new StringBuffer("SELECT a.operateTime,COUNT(*) FROM qy_account a WHERE a.productId IN(");
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sb.append(rs.getLong(1)+",");
			}
			String sql2=sb.toString();
			sql2=sql2.substring(0, sql2.length()-1);
			sql2+=") AND a.operateType REGEXP '^.{0,}出库' GROUP BY a.operateTime";
			
			pstmt=conn.prepareStatement(sql2);
			rs=pstmt.executeQuery();
			while(rs.next()){
				HashMap<String, String> outInfoByProdMap=new HashMap<String, String>();
				outInfoByProdMap.put("outTime", rs.getString(1));
				outInfoByProdMap.put("outNum", rs.getString(2));
				outInfoByProdList.add(outInfoByProdMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return outInfoByProdList;
	}
}
