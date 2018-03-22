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

import cn.edu.cqupt.beans.OutList;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.controller.transact_business.OutWarehouseServlet;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class OutListDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public OutListDAO() {
		
	}
	
	/**
	 * 将导入的发料单存入数据库
	 * @param list
	 * @return
	 */
	public boolean saveList(List<OutList> lists) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			//2015.7.20插入发料单列表去重复
			String sql = "Insert into qy_outlist (listId,fileNo,deliverNo,diliverMean"
					+ ",PMNM,productModel,oldModel,unit,quanlity,askCount,realCount"
					+ ",num,oldNum,outMeans,money,remark,price,date,orderId) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE "
					+ "fileNo=VALUES(fileNo),deliverNo=VALUES(deliverNo),diliverMean=VALUES(diliverMean),"
					+ "PMNM=VALUES(PMNM),productModel=VALUES(productModel),oldModel=VALUES(oldModel),"
					+ "unit=VALUES(unit),quanlity=VALUES(quanlity),askCount=VALUES(askCount),realCount=VALUES(realCount),"
					+ "num=VALUES(num),oldNum=VALUES(oldNum),outMeans=VALUES(outMeans),money=VALUES(money),remark=VALUES(remark),"
					+ "price=VALUES(price),date=VALUES(date)";
			pstmt = conn.prepareStatement(sql);
			for (OutList list : lists) {
				pstmt.setString(1, list.getListId());
				pstmt.setString(2, list.getFileNo());
				pstmt.setString(3, list.getDeliverNo());
				pstmt.setString(4, list.getDiliverMean());
				pstmt.setString(5, list.getPMNM());
				pstmt.setString(6, list.getProductModel());
				pstmt.setString(7, list.getOldModel());
				pstmt.setString(8, list.getUnit());
				pstmt.setString(9, list.getQuanlity());
				pstmt.setString(10, list.getAskCount());
				pstmt.setString(11, list.getRealCount());
				pstmt.setString(12, list.getNum());
				pstmt.setString(13, list.getOldNum());
				pstmt.setString(14, list.getOutMeans());
				pstmt.setDouble(15, list.getMoney());
				pstmt.setString(16, list.getRemark());
				pstmt.setDouble(17, list.getPrice());
				pstmt.setTimestamp(18, MyDateFormat.changeToSqlDate(list.getDate()));
				pstmt.setInt(19, list.getOrderId());
				pstmt.addBatch();
			}
			int[] counts = pstmt.executeBatch();
			conn.commit();
			if(counts.length > 0) {
				flag = true;
			}
		}catch(Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 按照料单编号查询料单
	 * @param listId
	 * @return
	 */
	public List<OutList> findListById(String listId,String date) {
		List<OutList> lists = new ArrayList<OutList>();
		try {
			conn = DBConnection.getConn();
			String sql ="Select * from qy_outlist where listId=? and date=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, listId);
			pstmt.setString(2, date);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				OutList list = new OutList();
				list.setListId(rs.getString("listId"));
				list.setFileNo(rs.getString("fileNo"));
				list.setDeliverNo(rs.getString("deliverNo"));
				list.setDiliverMean(rs.getString("diliverMean"));
				list.setPMNM(rs.getString("PMNM"));
				list.setProductModel(rs.getString("productModel"));
				list.setOldModel(rs.getString("oldModel"));
				list.setUnit(rs.getString("unit"));
				list.setQuanlity(rs.getString("quanlity"));
				list.setAskCount(rs.getString("askCount"));
				list.setRealCount(rs.getString("realCount"));
				list.setNum(rs.getString("num"));
				list.setOldNum(rs.getString("oldNum"));
				list.setOutMeans(rs.getString("outMeans"));
				list.setMoney(rs.getDouble("money"));
				list.setRemark(rs.getString("remark"));
				list.setPrice(rs.getDouble("price"));
				list.setOutMeans(rs.getString("outMeans"));
				list.setDate(rs.getTimestamp("date"));
				lists.add(list);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt);
		}
		return lists;
	}
	
	/**
	 * 出料单导出的一系列操作
	 * 1、改变产品状态
	 * 2、向outlist表和outlistProductRelation表中插入数据
	 * 3、查询出刚刚插入的outlist和outlistProductRelation数据，并返回
	 * 
	 * <p>
	 * notice：一条发料单会对应多条产品
	 * </p>
	 * <p>
	 * 如果是轮换/更新发料单，那么需要修改产品表的proStatus字段为已出库，在原有的status后面append上“发料轮换出库”和“发料更新出库”，
	 * 修改flag字段为2。对于直接出库发料单，则update产品表的proStatus，在原有的status后面append上“已出库”
	 * </p>
	 * @param headMap word文件上面四个数据,key:"FLDH""AYWH""YDBH""YSFS"
	 * @param tableDyadic word文件下面的数据，二维数组
	 * @param productIDDyadic 产品id，二维数组
	 * @param keeper 代储企业
	 * @param outMeans 发料单出库方式
	 * @return 刚刚插入的outlist和outlistProductRelation数据
	 * @author liangyihuai
	 */
	public Map<String,Object> operationOutListInJDS(Map<String,String> headMap,
			List<ArrayList<String>> tableDyadic, List<ArrayList<String>> productIDDyadic,String keeper,String outListType){
		boolean runStatus = true;
		
		PreparedStatement updateProduct_ps = null;
		PreparedStatement insertOutlist_ps = null;
		PreparedStatement queryOutlistId_ps = null;
		PreparedStatement queryListId_ps = null;
		PreparedStatement insertRelation_ps = null;
		PreparedStatement queryOutlist_ps = null;
		PreparedStatement queryRelation_ps = null;
		PreparedStatement selectProduct_ps = null;
		
		ResultSet queryOutlistId_rs = null;
		ResultSet queryListId_rs = null;
		ResultSet queryOutList_rs = null;
		ResultSet queryRelation_rs = null;
		ResultSet selectProduct_rs = null;
		
		//最终函数返回的map
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//用于存储所查询出来的outlist表和relation表
		List<ArrayList<String>> outlistStoreDyadic = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> relationStoreDyadic = new ArrayList<ArrayList<String>>();
		//标题行
		ArrayList<String> outlistHeadline = new ArrayList<String>();
		ArrayList<String> relationHeadline = new ArrayList<String>();
		//向二维数组中加入标题行
		outlistStoreDyadic.add(outlistHeadline);
		relationStoreDyadic.add(relationHeadline);
		//在二位数组tableDyadic后面添加下面五个字段
		for(int i = 0; i < tableDyadic.size(); i++){
			ArrayList<String> tempList = tableDyadic.get(i);
			tempList.add(MyDateFormat.changeDateToTimeStampString(new Date()));
			tempList.add(keeper);
			tempList.add(headMap.get("FLDH"));
			tempList.add(headMap.get("AYWH"));
			tempList.add(headMap.get("YDBH"));
			tempList.add(headMap.get("YSFS"));
		}
		
		//查询产品表，主要查询status字段
		String selectProductSql = "Select status ,flag From qy_product Where  productId=? And ownedUnit=?";
		//update product
		String updateProductSql = "Update qy_product Set proStatus=?,status=?,flag=?"
				+ " Where productId=? And ownedUnit=?";
		//插入发料单表
		String insertOutlistSql = "Insert Into qy_outlist(orderId,PMNM,"
				+ "productModel,unit,quanlity,askCount,realCount,num,price,"
				+ "money,remark,date,ownedUnit,listId,fileNo,deliverNo,diliverMean,outMeans)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
		//查询最近插入outlist表的id
		String queryOutlistIdSql = "SELECT @@IDENTITY AS 'id'";
		//查出outlist表中的listId
		String queryListIdSql = "Select listId From qy_outlist Where id=?";
		//插入关系表
		String insertRelationSql = "Insert Into qy_outlistproductrelation("
				+ "listId,productId,keeper,insertTime,ownedUnit)"
				+ "values(?,?,?,NOW(),?)On DUPLICATE KEY UPDATE "
				+ "listId=Values(listId),productId =Values(productId),"
				+ "keeper=Values(keeper),insertTime=Values(insertTime),ownedUnit=Values(ownedUnit)";
		//查找刚刚插入的发料单
		String queryOutlist = "Select * From qy_outlist Where id=?";
		//查找刚刚插入的关系表
		String queryRelationSql = "Select * From qy_outlistproductrelation "
				+ "Where listId=? And productId=? And keeper=? And ownedUnit=?";
		
		try{
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);

			updateProduct_ps = conn.prepareStatement(updateProductSql);
			insertOutlist_ps = conn.prepareStatement(insertOutlistSql);
			queryOutlistId_ps = conn.prepareStatement(queryOutlistIdSql);
			queryListId_ps = conn.prepareStatement(queryListIdSql);
			insertRelation_ps = conn.prepareStatement(insertRelationSql);
			queryOutlist_ps = conn.prepareStatement(queryOutlist);
			queryRelation_ps = conn.prepareStatement(queryRelationSql);
			selectProduct_ps = conn.prepareStatement(selectProductSql);
			
			int len = productIDDyadic.size();
			ArrayList<String> tempProductIDs = null;
			for(int i = 0; i < len; i++){
				tempProductIDs = productIDDyadic.get(i);
				
				//一条发料单可能会对应多个product
//				for(int x = 0; x < tempProductIDs.size(); x++){
//					//更新产品表
//					updateProduct_ps.setString(1, "已出库");
//					updateProduct_ps.setString(2, tempProductIDs.get(i));
//					updateProduct_ps.setString(3, keeper);
//					updateProduct_ps.addBatch();
//				}
//				updateProduct_ps.executeBatch();
				
				//插入outlist表
				ArrayList<String> dataList = tableDyadic.get(i);
				int index = 1;
				int tempLen = tableDyadic.get(0).size();
				for(int k = 0; k < tempLen; k++){
					String tempStr = dataList.get(k);
					if("".equals(tempStr)){
						tempStr = null;
					}
					insertOutlist_ps.setString(index++, tempStr);
				}
				insertOutlist_ps.setString(index++, outListType);
				insertOutlist_ps.execute();
				
				//查找最近插入的数据的id
				queryOutlistId_rs = queryOutlistId_ps.executeQuery();
				int id_outlist = -1;
				if(queryOutlistId_rs.next()){
					id_outlist = queryOutlistId_rs.getInt("id");
				}
				
				//根据刚刚插入的数据的id查找对应的listId
				String listId = "";
				queryListId_ps.setInt(1, id_outlist);
				queryListId_rs = queryListId_ps.executeQuery();
				if(queryListId_rs.next()){
					listId = queryListId_rs.getString("listId");
				}
				for(int k = 0; k < tempProductIDs.size(); k++){
					selectProduct_ps.setString(1, tempProductIDs.get(k));
					selectProduct_ps.setString(2, keeper);
					selectProduct_rs = selectProduct_ps.executeQuery();
					String status = "";
					int flag = 0;
					if(selectProduct_rs.next()){
						status = selectProduct_rs.getString(1);
						flag = selectProduct_rs.getInt(2);
					}
					
					status = changeStatus(outListType, status);
					
					if(!StringUtil.DIRECT_OUTLIST.equals(outListType)){
						flag = 1;
					}
					
					//更新产品表
					updateProduct_ps.setString(1, "已出库");
					updateProduct_ps.setString(2, status);
					updateProduct_ps.setInt(3, flag);
					updateProduct_ps.setString(4, tempProductIDs.get(k));
					updateProduct_ps.setString(5, keeper);
					updateProduct_ps.addBatch();
					
					String productId = tempProductIDs.get(k);
					String ownedUnit = keeper;
					//插入关系表
					insertRelation_ps.setString(1, listId);
					insertRelation_ps.setString(2, productId);
					insertRelation_ps.setString(3, keeper);
					insertRelation_ps.setString(4, ownedUnit);
					insertRelation_ps.execute();
					
					//查出刚刚插入关系表中的实体
					queryRelation_ps.setString(1, listId);
					queryRelation_ps.setString(2, productId);
					queryRelation_ps.setString(3, keeper);
					queryRelation_ps.setString(4, ownedUnit);
					queryRelation_rs = queryRelation_ps.executeQuery();
					while(queryRelation_rs.next()){
						ArrayList<String> tempList2 = new ArrayList<String>();
						
						//得到结果集(rs)的结构信息，比如字段数、字段名等 
						ResultSetMetaData md = queryRelation_rs.getMetaData();  
						int columnCount = md.getColumnCount(); 	
				        for(int j = 1; j <= columnCount; j++){
				        	tempList2.add(queryRelation_rs.getString(j));
				        	//增加标题
				        	if(relationHeadline.size() != columnCount)
				        		relationHeadline.add(md.getColumnName(j));
				        }
				        relationStoreDyadic.add(tempList2);
					}
				}
				updateProduct_ps.executeBatch();
				
				//查出刚刚插入的outlist表的实体
				queryOutlist_ps.setInt(1,id_outlist);
				queryOutList_rs = queryOutlist_ps.executeQuery();
				while(queryOutList_rs.next()){
					ArrayList<String> tempOutlist = new ArrayList<String>();
					
					//得到结果集(rs)的结构信息，比如字段数、字段名等 
					ResultSetMetaData md = queryOutList_rs.getMetaData();  
					int columnCount = md.getColumnCount(); 	
			        for(int k = 1; k <= columnCount; k++){
			        	tempOutlist.add(queryOutList_rs.getString(k));
			        	//增加标题
			        	if(outlistHeadline.size() != columnCount){
			        		outlistHeadline.add(md.getColumnName(k));
			        	}
			        }
			        outlistStoreDyadic.add(tempOutlist);
				}
			}//end for
			conn.commit();
		}catch(Exception e){
			try {
				runStatus = false;
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
			DBConnection.close(null, updateProduct_ps, queryOutlistId_rs);
			DBConnection.close(null, insertOutlist_ps,queryListId_rs);
			DBConnection.close(null, queryOutlistId_ps, queryOutList_rs);
			DBConnection.close(null, queryListId_ps, queryRelation_rs);
			DBConnection.close(insertRelation_ps,queryRelation_ps);
			
			resultMap.put("runStatus", runStatus);
		}
		resultMap.put("outList",outlistStoreDyadic);
		resultMap.put("relation", relationStoreDyadic);
		
		return resultMap;
	}
	
	/**
	 * 
	 * @param outListType 发料单出库方式
	 * @param status 产品表中的status字段的数据
	 * @return
	 */
	private String changeStatus(String outListType,String status){
		String tempType = "";
//		if(StringUtil.DIRECT_OUTLIST.equals(outListType))
//			outListType = "已出库";
		if(status != null){ 
			if(status.endsWith(outListType)){
				tempType = status;
			}else{
				tempType = status+","+outListType;
			}
		}else{
			tempType = outListType;
			
		}
		return tempType;
	}
	
	/**
	 * 查询发料单信息
	 * */
	public List<HashMap<String,Object>> searchList(int curPageNum,int pageSize){
		List<HashMap<String,Object>> inApplyList = new ArrayList<HashMap<String,Object>>();
		String sql = "select distinct fileNo,listId,diliverMean,deliverNo from qy_outlist limit ?,?";
		try {
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, (curPageNum-1)*pageSize);
			pstmt.setInt(2, (pageSize*curPageNum)-1);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				HashMap<String, Object> list = new HashMap<String, Object>();
				String fileNo = rs.getString("fileNo");
				String listId = rs.getString("listId");
				String diliverMean = rs.getString("diliverMean");
				String deliverNo = rs.getString("deliverNo");
				
				list.put("fileNo", fileNo);
				list.put("listId", listId);
				list.put("diliverMean", diliverMean);
				list.put("deliverNo", deliverNo);
				inApplyList.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt);
		}
		return inApplyList;
	}
	
	/**
	 * 查询发料单数量
	 * */
	public int searchListSum() {
		int count = 0;
		String sql = "select count(distinct fileNo,listId,diliverMean,deliverNo) from qy_outlist";
		try {
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				count = rs.getInt("count(distinct fileNo,listId,diliverMean,deliverNo)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt);
		}
		return count;
	}
	
	/**
	 * 以下部分是用于出库操作
	 * */
	
	/**
	 * 查询发料单信息
	 * 已出库部分
	 * */
//	public HashMap<String,HashMap<String, String>> selectOutListForOut(String type){
//		HashMap<String,HashMap<String, String>> resultMap = new HashMap<String,HashMap<String, String>>();
//		String sql = "select "
//				 	+ "o.PMNM,"
//				 	+ "o.productModel,"
//				 	+ "o.unit,"
//				 	+ "o.quanlity,"
//				 	+ "o.askCount,"
//				 	+ "o.realCount,"
//				 	+ "o.num,"
//				 	+ "o.price,"
//				 	+ "o.money,"
//				 	+ "o.remark,"
//				 	+ "o.outMeans,"
//				 	+ "o.listId,"
//				 	+ "o.ownedUnit,"
//				 	+ "o.fileNo,"
//				 	+ "o.deliverNo,"
//				 	+ "o.diliverMean,"
//				 	+ "o.oldModel,"
//				 	+ "o.oldNum,"
//				 	+ "o.date,"
//				 	+ "o.orderId,"
//				 	+ "count(*)"
//				 	+ " from qy_outlist o,qy_outlistproductrelation r,qy_product p"
//				 	+ " where o.listId=r.listId"
//				 	+ " and r.productId=p.productId"
//				 	+ " and o.PMNM=p.PMNM"
//				 	+ " and o.productModel=p.productModel"
//					+ " and p.proStatus regexp '^已.*出库$'";
//		if(type.equals("allot")){
//			    sql += " and o.outMeans='调拨出库'";
//		}else if(type.equals("turn")){
//			    sql += " and o.outMeans='轮换出库'";
//		}else if(type.equals("update")){
//				sql += " and o.outMeans='更新出库'";
//		}
//			    sql += " and o.ownedUnit=r.ownedUnit"
//				 	+ " and o.ownedUnit=p.ownedUnit"
//				 	+ " group by "
//				 	+ "o.PMNM,"
//				 	+ "o.productModel,"
//				 	+ "o.unit,"
//				 	+ "o.quanlity,"
//				 	+ "o.askCount,"
//				 	+ "o.realCount,"
//				 	+ "o.num,"
//				 	+ "o.price,"
//				 	+ "o.money,"
//				 	+ "o.remark,"
//				 	+ "o.outMeans,"
//				 	+ "o.listId,"
//				 	+ "o.ownedUnit,"
//				 	+ "o.fileNo,"
//				 	+ "o.deliverNo,"
//				 	+ "o.diliverMean,"
//				 	+ "o.oldModel,"
//				 	+ "o.oldNum,"
//				 	+ "o.date,"
//				 	+ "o.orderId";
//		try {
//			conn = DBConnection.getConn();
//			pstmt=conn.prepareStatement(sql);
//			ResultSet rs=pstmt.executeQuery();
//			while(rs.next()){
//				String PMNM = rs.getString("PMNM");
//				String productModel = rs.getString("productModel");
//				String unit = rs.getString("unit");
//				String quanlity = rs.getString("quanlity");
//				String askCount = rs.getString("askCount");
//				String realCount = rs.getString("realCount");
//				String num = rs.getString("num");
//				double price = rs.getDouble("price");
//				double money = rs.getDouble("money");
//				String remark = rs.getString("remark");
//				String outMeans = rs.getString("outMeans");
//				String listId = rs.getString("listId");
//				String ownedUnit = rs.getString("ownedUnit");
//				String fileNo = rs.getString("fileNo");
//				String deliverNo = rs.getString("deliverNo");
//				String diliverMean = rs.getString("diliverMean");
//				String oldModel = rs.getString("oldModel");
//				String oldNum = rs.getString("oldNum");
//				Date date = rs.getDate("date");
//				int orderId = rs.getInt("orderId");
//				int count = rs.getInt("count(*)");
//				int leftCount = 0;
//				
//				HashMap<String, String> list = new HashMap<String, String>();
//				list.put("PMNM", PMNM);
//				list.put("productModel", productModel);
//				list.put("unit", unit);
//				list.put("quanlity", quanlity);
//				list.put("askCount", askCount);
//				list.put("realCount", realCount);
//				list.put("num", num);
//				list.put("price", price+"");
//				list.put("money", money+"");
//				list.put("remark", remark);
//				list.put("outMeans", outMeans);
//				list.put("listId", listId);
//				list.put("ownedUnit", ownedUnit);
//				list.put("fileNo", fileNo);
//				list.put("deliverNo", deliverNo);
//				list.put("diliverMean", diliverMean);
//				list.put("oldModel", oldModel);
//				list.put("oldNum", oldNum);
//				list.put("date", date+"");
//				list.put("orderId", orderId+"");
//				list.put("count", count+"");
//				list.put("leftCount", leftCount+"");
//				
//				String key = PMNM+productModel+unit+quanlity+askCount+realCount+num+price
//						+money+remark+outMeans+listId+ownedUnit+fileNo+deliverNo+diliverMean
//						+oldModel+oldNum+date+orderId;
//				
//				resultMap.put(key, list);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			DBConnection.close(conn, pstmt);
//		}
//		return resultMap;
//	}
	
	/**
	 * 查询发料单信息
	 * 待出库部分
	 * */
//	public HashMap<String,HashMap<String, String>> selectOutListForIn(String type, HashMap<String,HashMap<String, String>> resultMap){
//		String sql = "select "
//				 	+ "o.PMNM,"
//				 	+ "o.productModel,"
//				 	+ "o.unit,"
//				 	+ "o.quanlity,"
//				 	+ "o.askCount,"
//				 	+ "o.realCount,"
//				 	+ "o.num,"
//				 	+ "o.price,"
//				 	+ "o.money,"
//				 	+ "o.remark,"
//				 	+ "o.outMeans,"
//				 	+ "o.listId,"
//				 	+ "o.ownedUnit,"
//				 	+ "o.fileNo,"
//				 	+ "o.deliverNo,"
//				 	+ "o.diliverMean,"
//				 	+ "o.oldModel,"
//				 	+ "o.oldNum,"
//				 	+ "o.date,"
//				 	+ "o.orderId,"
//				 	+ "count(*)"
//				 	+ " from qy_outlist o,qy_outlistproductrelation r,qy_product p"
//				 	+ " where o.listId=r.listId"
//				 	+ " and r.productId=p.productId"
//				 	+ " and o.PMNM=p.PMNM"
//				 	+ " and o.productModel=p.productModel"
//					+ " and p.proStatus regexp '待出库$'";
//		if(type.equals("allot")){
//		    	sql += " and o.outMeans='调拨出库'";
//		}else if(type.equals("turn")){
//		    sql += " and o.outMeans='轮换出库'";
//		}else if(type.equals("update")){
//			sql += " and o.outMeans='更新出库'";
//		}
//		    	sql += " and o.ownedUnit=r.ownedUnit"
//				 	+ " and o.ownedUnit=p.ownedUnit"
//				 	+ " group by "
//				 	+ "o.PMNM,"
//				 	+ "o.productModel,"
//				 	+ "o.unit,"
//				 	+ "o.quanlity,"
//				 	+ "o.askCount,"
//				 	+ "o.realCount,"
//				 	+ "o.num,"
//				 	+ "o.price,"
//				 	+ "o.money,"
//				 	+ "o.remark,"
//				 	+ "o.outMeans,"
//				 	+ "o.listId,"
//				 	+ "o.ownedUnit,"
//				 	+ "o.fileNo,"
//				 	+ "o.deliverNo,"
//				 	+ "o.diliverMean,"
//				 	+ "o.oldModel,"
//				 	+ "o.oldNum,"
//				 	+ "o.date,"
//				 	+ "o.orderId";
//		try {
//			conn = DBConnection.getConn();
//			pstmt=conn.prepareStatement(sql);
//			ResultSet rs=pstmt.executeQuery();
//			while(rs.next()){
//				String PMNM = rs.getString("PMNM");
//				String productModel = rs.getString("productModel");
//				String unit = rs.getString("unit");
//				String quanlity = rs.getString("quanlity");
//				String askCount = rs.getString("askCount");
//				String realCount = rs.getString("realCount");
//				String num = rs.getString("num");
//				double price = rs.getDouble("price");
//				double money = rs.getDouble("money");
//				String remark = rs.getString("remark");
//				String outMeans = rs.getString("outMeans");
//				String listId = rs.getString("listId");
//				String ownedUnit = rs.getString("ownedUnit");
//				String fileNo = rs.getString("fileNo");
//				String deliverNo = rs.getString("deliverNo");
//				String diliverMean = rs.getString("diliverMean");
//				String oldModel = rs.getString("oldModel");
//				String oldNum = rs.getString("oldNum");
//				Date date = rs.getDate("date");
//				int orderId = rs.getInt("orderId");
//				int count = 0;
//				int leftCount = rs.getInt("count(*)");
//				
//				String key = PMNM+productModel+unit+quanlity+askCount+realCount+num+price
//						+money+remark+outMeans+listId+ownedUnit+fileNo+deliverNo+diliverMean
//						+oldModel+oldNum+date+orderId;
//				
//				if(resultMap.get(key)==null){
//					HashMap<String, String> list = new HashMap<String, String>();
//					list.put("PMNM", PMNM);
//					list.put("productModel", productModel);
//					list.put("unit", unit);
//					list.put("quanlity", quanlity);
//					list.put("askCount", askCount);
//					list.put("realCount", realCount);
//					list.put("num", num);
//					list.put("price", price+"");
//					list.put("money", money+"");
//					list.put("remark", remark);
//					list.put("outMeans", outMeans);
//					list.put("listId", listId);
//					list.put("ownedUnit", ownedUnit);
//					list.put("fileNo", fileNo);
//					list.put("deliverNo", deliverNo);
//					list.put("diliverMean", diliverMean);
//					list.put("oldModel", oldModel);
//					list.put("oldNum", oldNum);
//					list.put("date", date+"");
//					list.put("orderId", orderId+"");
//					list.put("count", count+"");
//					list.put("leftCount", leftCount+"");
//					
//					resultMap.put(key, list);
//				}else if(resultMap.get(key)!=null){
//					HashMap<String, String> list = resultMap.get(key);
//					resultMap.remove(key);
//					
//					list.remove("leftCount");
//					list.put("leftCount", leftCount+"");
//					
//					resultMap.put(key, list);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			DBConnection.close(conn, pstmt);
//		}
//		return resultMap;
//	}
	
	
	/**
	 * 查询要出库的机号
	 * */
	public ArrayList<Product> selectDeviceNo(HashMap<String, String> condition){
		ArrayList<Product> productList = new ArrayList<Product>();
		String sql = "select "
					+"p.deviceNo,"
					+"p.location"
					+" from qy_outlist o,qy_outlistproductrelation r,qy_product p"
					+" where o.listId=?"
					+" and o.ownedUnit=?"
					+" and o.PMNM=?"
					+" and o.productModel=?"
					+" and o.listId=r.listId"
					+" and r.productId=p.productId"
					+" and o.ownedUnit=p.ownedUnit"
					+" and o.ownedUnit=r.ownedUnit"
					+" and o.PMNM=p.PMNM"
					+" and o.productModel=p.productModel"
					+" and p.proStatus regexp '待出库$'"
					+" limit 0,?";
		try {
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, condition.get("listId"));
			pstmt.setString(2, condition.get("ownedUnit"));
			pstmt.setString(3, condition.get("PMNM"));
			pstmt.setString(4, condition.get("productModel"));
			pstmt.setInt(5, Integer.parseInt(condition.get("count")));//表示出库数量
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				String deviceNo = rs.getString("deviceNo");
				String location = rs.getString("location");
				
				Product product = new Product();
				product.setDeviceNo(deviceNo);
				product.setLocation(location);
				
				productList.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return productList;
	}
	
	/**
	 * 产品出库
	 * */
	public List<Map<String,String>> updateProStatus(HashMap<String, String> condition){
		List<Map<String,String>> resultOutPro = new ArrayList<Map<String,String>>();
		PreparedStatement idPs = null;
		ResultSet idRs = null;
		StringBuffer selectId = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		selectId.append("select p.productId,p.productModel,p.deviceNo,p.location"
				+ " From qy_outlist o"
				+ " inner join qy_outlistproductrelation r"
				+ " on o.listId = r.listId"
				+ " inner join qy_product p"
				+ " on r.productId = p.productId"
				+ " where o.listId=? and p.PMNM=? and p.productModel=? and p.proStatus regexp '待出库$' and p.status REGEXP '"+condition.get("outType")+"'"
				+" limit 0,?");
		sql.append("update"
					+" qy_product"
					+" set"
					+" proStatus = '已出库', flag=1"
					+" where productId=?");
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			idPs = conn.prepareStatement(selectId.toString());
			int index = 1;
			idPs.setString(index++, condition.get("listId"));
			idPs.setString(index++, condition.get("PMNM"));
			idPs.setString(index++, condition.get("productModel"));
			idPs.setInt(index++, Integer.parseInt(condition.get("count")));//表示出库数量
			idRs = idPs.executeQuery();
			while (idRs.next()) {
				Map<String,String> temp = new HashMap<String, String>();
				ResultSetMetaData dataMeta = idRs.getMetaData();
				for (int i = 1; i <=dataMeta.getColumnCount(); i++) {
					temp.put(dataMeta.getColumnName(i), idRs.getString(i));
				}
				resultOutPro.add(temp);
			}
			pstmt=conn.prepareStatement(sql.toString());
			for (int i = 0; i < resultOutPro.size(); i++) {
				pstmt.setString(1, resultOutPro.get(i).get("productId"));
				pstmt.addBatch();
			}
			int[] count = pstmt.executeBatch();
			Map<String,String> temp = new HashMap<String, String>();
			temp.put("runStatus", "false");
			resultOutPro.add(temp);
			if(count.length > 0) {
				Map<String,String> temp1 =  resultOutPro.get(resultOutPro.size() - 1);
				temp1.put("runStatus", "true");
				resultOutPro.set(resultOutPro.size() - 1, temp1);
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return resultOutPro;
	}
	
	/**
	 * 查询发料单
	 * @param type 发料单类型
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getOutList(String type,int curPageNum,int pageSize){
		ArrayList<HashMap<String, String>> outList=new ArrayList<HashMap<String,String>>();
		StringBuffer sqlbf=new StringBuffer();
		sqlbf.append("SELECT a.listId,a.fileNo,a.deliverNo,a.diliverMean,a.outMeans,a.date FROM qy_outlist a GROUP BY a.listId HAVING a.outMeans REGEXP ");
		if("allot".equals(type))
			sqlbf.append("'调拨'");
		else if("turn".equals(type))
			sqlbf.append("'轮换'");
		else if("update".equals(type))
			sqlbf.append("'更新'");
		sqlbf.append("LIMIT ?,?");
		String sql=sqlbf.toString();
		try {
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, (curPageNum-1)*pageSize);
			pstmt.setInt(2, pageSize);
			rs=pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData rmd=rs.getMetaData();
				HashMap<String, String> tempHashmap=new HashMap<String, String>();
				for (int i = 1; i <= rmd.getColumnCount(); i++) {
					tempHashmap.put(rmd.getColumnName(i), rs.getString(i));
				}
				outList.add(tempHashmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return outList;
	}
	
	/**
	 * 查询发料单内部
	 * @param type 发料单类型
	 * @return
	 */
	public ArrayList<HashMap<String, String>> selectOutListDetail(String type,String listId,int curPageNum,int pageSize){
		ArrayList<HashMap<String, String>> outList=new ArrayList<HashMap<String,String>>();
		StringBuffer sqlbf=new StringBuffer();
		if("allot".equals(type))
			type="调拨";
		else if("turn".equals(type))
			type="轮换";
		else if("update".equals(type))
			type="更新";
		sqlbf.append("SELECT a.listId,a.a.PMNM,a.price,a.productModel,a.outMeans,a.unit,a.quanlity,a.askCount,a.realCount,a.num,a.money,a.remark,"
				+ "a.totalNum-IFNULL(o.totalNum,0) as innum,IFNULL(o.totalNum,0) as outnum "
				+ " FROM outlist_proDetail a "
				+ " LEFT JOIN outlist_proDetail_toout o "
				+ " on a.listId = o.listId"
				+ " WHERE a.listId = ? AND a.outMeans REGEXP '"+type+"'");
		sqlbf.append(" LIMIT ?,?");
		String sql=sqlbf.toString();
		try {
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, listId);
			pstmt.setInt(2, (curPageNum-1)*pageSize);
			pstmt.setInt(3, pageSize);
			rs=pstmt.executeQuery();
			while(rs.next()){
				ResultSetMetaData rmd=rs.getMetaData();
				HashMap<String, String> tempHashmap=new HashMap<String, String>();
				for (int i = 1; i <= rmd.getColumnCount(); i++) {
					tempHashmap.put(rmd.getColumnName(i), rs.getString(i));
				}
				outList.add(tempHashmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return outList;
	}
	
	/**
	 * 获取发料单条数
	 * @param type 发料单类型
	 * @return
	 */
	public int getOutListNum(String type){
		int outListNum=0;
		StringBuffer sqlbf=new StringBuffer();
		sqlbf.append("SELECT COUNT(DISTINCT a.listId) FROM qy_outlist a WHERE a.outMeans REGEXP ");
		if("allot".equals(type))
			sqlbf.append("'调拨'");
		else if("turn".equals(type))
			sqlbf.append("'轮换'");
		else if("update".equals(type))
			sqlbf.append("'更新'");
		String sql=sqlbf.toString();
		try {
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()){
				outListNum=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return outListNum;
	}
	
	
	/**
	 * 获取发料单产品条数
	 * @param type 发料单类型
	 * @return
	 */
	public int getOutListProNum(String type,String listId){
		int outListNum=0;
		StringBuffer sqlbf = new StringBuffer();
		if("allot".equals(type))
			type="调拨";
		else if("turn".equals(type))
			type="轮换";
		else if("update".equals(type))
			type="更新";
		sqlbf.append("SELECT count(*)"
				+ " FROM outlist_proDetail a"
				+ " INNER JOIN outlist_proDetail_toout o"
				+ " on a.listId = o.listId"
				+ " WHERE a.listId = ? AND a.outMeans REGEXP '"+type+"'");
		String sql=sqlbf.toString();
		try {
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, listId);
			rs=pstmt.executeQuery();
			if(rs.next()){
				outListNum=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return outListNum;
	}
}
