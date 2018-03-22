package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.RestTime;
import cn.edu.cqupt.util.StringUtil;

public class InApplyDAO {

	/**
	 * 对入库申请表的数据库相关操作
	 * 
	 * @author LiangYiHuai
	 */

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public InApplyDAO() {

	}

	/**
	 * 审核（改变）入库申请文件
	 * <p>
	 * 企业新增轮换入库或者更新入库申请：
	 *	1、复制原产品，更新新产品的flag=1和otherProductId = 原产品id以及status=轮换入库或者更新入库
	 *	2、更新原产品flag = 2、不用otherProductId = 新产品的id
     * </p>
     * <p>
	 *	军代室审核轮换入库或者更新入库申请：
	 *	通过：
	 *	1、更新新产品的flag = 2， proStatus = 已入库
	 *	2、对于原产品，append原产品的otherProductId字段（所以需要查出新产品的otherProductId字段的内容）
     * </p>
     * <p>
	 *	不通过：
	 *	1、更新新产品的proStatus = 合同销毁
	 *	2、查询出原产品的otherProductId字段，如果有逗号，则去掉逗号后面的id，如果没有逗号，则不用改变。
	 *  （因为最多只有一个逗号）
	 * </p>
	 * @return
	 * @author LiangYiHuai
	 */
	public boolean changeInApplyCheckStatus(List<ArrayList<String>> dyadicArray,String checkPerson, String applyType) {
		boolean flag = false;

		Connection conn1 = null;
		Connection conn2 = null;

		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
//		PreparedStatement selectApplyPs = null;
		PreparedStatement selectOldProductPs = null;
		PreparedStatement updateOldProductPs = null;
//		ResultSet selectApplyRs = null;
		ResultSet selectOldProductRs = null;
		PreparedStatement updateNewProductNoPs = null;
		PreparedStatement selectNewProductPs = null;
		ResultSet selectNewProductRs = null;

		/**
		 * 判断inId和chStatus所在列的索引
		 */
		ArrayList<String> firstRow = dyadicArray.get(0);
		int firstRowLen = firstRow.size();
		int inIdIndex = -1;
		int chStatusIndex = -1;
		int ownedUnitIndex = -1;
		for (int i = 0; i < firstRowLen; i++) {
			if ("inId".equals(firstRow.get(i))) {
				inIdIndex = i;
			} else if ("chStatus".equals(firstRow.get(i))) {
				chStatusIndex = i;
			}else if("ownedUnit".equals(firstRow.get(i))){
				ownedUnitIndex = i;
			}
		}
		
		//因为有一些地方没"审核人"字段，所以此处不加checkPerson
		if (inIdIndex == -1 | chStatusIndex == -1 || ownedUnitIndex == -1) {
			return false;
		}

		String sqlInApply = "Update qy_inapply set chStatus = ? ,checkPerson=? "
				+ "Where inId= ? And ownedUnit=?";
		//查询新产品的productId和ownedUnit
		String selectNewProductSql = "SELECT productId,ownedUnit,otherProductId FROM qy_product "
				+ "WHERE productId in (Select productId From qy_inproductrelation"
				+ " where inId = ? And ownedUnit=?) AND ownedUnit=?";
		//审核通过，更新新插入的产品的状态
		String updateNewProductSql = "Update qy_product set proStatus = ?,flag=?"
				+ " where productId in (Select productId From qy_inproductrelation"
				+ " where inId = ? And ownedUnit=?) AND ownedUnit=?";
		//审核不通过，更新新插入的产品的状态
//		String updateNewProductNoSql = "Update qy_product set proStatus = ?,flag=?,otherProductId=?"
//				+ " where productId in (Select productId From qy_inproductrelation"
//				+ " where inId = ? And ownedUnit=?) AND ownedUnit=?";
		//查询原产品
		String selectOldProductSql = "Select otherProductId From qy_product Where productId = ? And ownedUnit = ?";
//		String selectApplySql = "SELECT productId,otherProductId FROM qy_product " +
//				"WHERE productId IN (SELECT productId FROM qy_inproductrelation WHERE inId=? AND ownedUnit =?) AND ownedUnit=?";
		//更新原产品（被对账）的状态以及对账
		String updateOldProductSql = "UPDATE qy_product SET otherProductId=? WHERE productId=? AND ownedUnit=?";
		
		try {
			conn1 = DBConnection.getConn();
			conn2 = DBConnection.getConn();
			conn1.setAutoCommit(false);
			conn2.setAutoCommit(false);

			pstmt1 = conn1.prepareStatement(sqlInApply);
			pstmt2 = conn2.prepareStatement(updateNewProductSql);
//			selectApplyPs = conn2.prepareStatement(selectApplySql);
			selectOldProductPs = conn2.prepareStatement(selectOldProductSql);
			updateOldProductPs = conn2.prepareStatement(updateOldProductSql);
			updateNewProductNoPs = conn2.prepareStatement(updateNewProductSql);
			selectNewProductPs = conn2.prepareStatement(selectNewProductSql);
			
			int inId = 0;
			String chStatus = null;
			String proStatus = null;
			String ownedUnit = null;
			
			boolean accessFlag = false;
			boolean refuseFlag = false;
			
			
			int len = dyadicArray.size();
			//第一行是标题
			for (int i = 1; i < len; i++) {
				inId = Integer.parseInt(dyadicArray.get(i).get(inIdIndex));
				chStatus = dyadicArray.get(i).get(chStatusIndex);
				ownedUnit = dyadicArray.get(i).get(ownedUnitIndex);
				
				//执行更新申请表
				pstmt1.setString(1, chStatus);
				pstmt1.setString(2, checkPerson);
				pstmt1.setInt(3, inId);
				pstmt1.setString(4, ownedUnit);
				pstmt1.addBatch();

				//如果状态中没有“未”、“不”字，并且有“通过”两个字，那就表示审核通过
				if(chStatus.indexOf("未") == -1 && chStatus.indexOf("不") == -1 
						&& chStatus.indexOf("通过") != -1){
					
					
					proStatus = "已入库";
					accessFlag = true;
					
					//更新刚插入的产品
					//change by liuyutian 改掉新入库审核后flag变为2的bug 2015.11.06
					if("RK".equals(applyType)) {
						pstmt2.setString(1, proStatus);
						pstmt2.setString(2, null);
						pstmt2.setInt(3, inId);
						pstmt2.setString(4, ownedUnit);
						pstmt2.setString(5, ownedUnit);
					}else {
						pstmt2.setString(1, proStatus);
						pstmt2.setInt(2, 2);
						pstmt2.setInt(3, inId);
						pstmt2.setString(4, ownedUnit);
						pstmt2.setString(5, ownedUnit);
					}
					
					//查询新产品
					selectNewProductPs.setInt(1, inId);
					selectNewProductPs.setString(2, ownedUnit);
					selectNewProductPs.setString(3, ownedUnit);
					selectNewProductRs = selectNewProductPs.executeQuery();
					String newProductId = "";
					String otherProductId = "";//原产品的productId
					while(selectNewProductRs.next()){
						newProductId = selectNewProductRs.getString("productId");
						otherProductId = selectNewProductRs.getString("otherProductId");
						//查询原产品otherProductId字段
						selectOldProductPs.setString(1, otherProductId);
						selectOldProductPs.setString(2, ownedUnit);
						selectOldProductRs = selectOldProductPs.executeQuery();
						//原产品中otherProductId字段中的内容
						String otherProductId_old = "";
						if(selectOldProductRs.next()){
							otherProductId_old = selectOldProductRs.getString("otherProductId");
						}
						if(otherProductId_old == null)
							otherProductId_old = newProductId;
						else
							if(!otherProductId_old.endsWith(newProductId))
								otherProductId_old = otherProductId_old+","+newProductId;
							
						//更新原产品otherProductId字段的内容
						updateOldProductPs.setString(1, otherProductId_old);
						updateOldProductPs.setString(2, otherProductId);
						updateOldProductPs.setString(3, ownedUnit);
						updateOldProductPs.execute();
					}
				}else{
					//如果审核不通过
					if("RK".equals(applyType)) proStatus = "未申请";
					else if("LHRK".equals(applyType) || "GXRK".equals(applyType)) proStatus = "合同销毁";
//					else if("LHCK".equals(applyType)||"GXCK".equals(applyType))proStatus = "已入库";
					
					refuseFlag = true;
					
					//更新刚插入的产品
					updateNewProductNoPs.setString(1, proStatus);
					updateNewProductNoPs.setInt(2, 1);
					updateNewProductNoPs.setInt(3, inId);
					updateNewProductNoPs.setString(4, ownedUnit);
					updateNewProductNoPs.setString(5, ownedUnit);
					
					//查询新产品
					selectNewProductPs.setInt(1, inId);
					selectNewProductPs.setString(2, ownedUnit);
					selectNewProductPs.setString(3, ownedUnit);
					selectNewProductRs = selectNewProductPs.executeQuery();
					String otherProductId = "";//原产品的productId
					while(selectNewProductRs.next()){
						otherProductId = selectNewProductRs.getString("otherProductId");
						//查询原产品otherProductId字段
						selectOldProductPs.setString(1, otherProductId);
						selectOldProductPs.setString(2, ownedUnit);
						selectOldProductRs = selectOldProductPs.executeQuery();
						//原产品中otherProductId字段中的内容
						String otherProductId_old = "";
						if(selectOldProductRs.next()){
							otherProductId_old = selectOldProductRs.getString("otherProductId");
						}
						if(otherProductId_old != null){
							int index = otherProductId_old.indexOf(",");
							if(index != -1){
								otherProductId_old = otherProductId_old.substring(0, index);
							}
						}
						//更新原产品otherProductId字段的内容
						updateOldProductPs.setString(1, otherProductId_old);
						updateOldProductPs.setString(2, otherProductId);
						updateOldProductPs.setString(3, ownedUnit);
						updateOldProductPs.execute();
					}
				}
				//审核通过时，更新新插入的产品
				if(accessFlag && pstmt2 != null) pstmt2.addBatch();
				//审核不通过时，更新新插入的产品
				if(refuseFlag && updateNewProductNoPs != null) updateNewProductNoPs.addBatch();
			}

			pstmt1.executeBatch();
			if(accessFlag && pstmt2 != null)pstmt2.executeBatch();
			if(refuseFlag && updateNewProductNoPs != null)updateNewProductNoPs.executeBatch();

			conn1.commit();
			conn2.commit();

			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn1.rollback();
				conn2.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DBConnection.close(conn1, pstmt1);
			try {
				conn2.close();
				if(pstmt2 != null)pstmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 把apply对象中的信息插入到数据库中
	 * 
	 * @param apply
	 * @return long 如果插入失败，return -1；插入成功返回inId
	 */
//	public long saveApply(InApply apply) {
//		boolean flag = false;
//		ResultSet rs = null;
//		PreparedStatement pstmt1 = null;
//		long inId = -1;
//		String sql = "Insert into qy_inapply ("
//				+ "inMeans,"
//				+ "ProductType,"
//				+ "oldType,"
//				+ "wholeName,"
//				+ "unitName,"
//				+ "batch,"
//				+ "deviceNo,"
//				+ "unit,"
//				+ "newPrice,"
//				+ "oldPrice,"
//				+ "num,"
//				+ "oldNum,"
//				+ "measure,"
//				+ "manufacturer,"
//				+ "keeper,"
//				+ "location,"
//				+ "storageTime,"
//				+ "maintainCycle,"
//				+ "producedDate,"
//				+ "execDate,"
//				+ "remark, "
//				+ "contractId,"
//				+ "chStatus,"
//				+ "productCode,"
//				+ "PMNM) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
//		String sql1 = "SELECT @@IDENTITY AS 'inId';";
//		try {
//			conn = DBConnection.getConn();
//			pstmt = conn.prepareStatement(sql);
//			pstmt1 = conn.prepareStatement(sql1);
//			pstmt.setString(1, apply.getInMeans());
//			pstmt.setString(2, apply.getProductType());
//			pstmt.setString(3, apply.getOldType());
//			pstmt.setString(4, apply.getWholeName());
//			pstmt.setString(5, apply.getUnitName());
//			pstmt.setString(6, apply.getBatch());
//			pstmt.setString(7, apply.getDeviceNo());
//			pstmt.setString(8, apply.getUnit());
//			if (apply.getNewPrice() != null) {
//				pstmt.setDouble(9, Double.valueOf(apply.getNewPrice()));
//			}
//			if (apply.getOldPrice() != null) {
//				pstmt.setDouble(10, Double.valueOf(apply.getOldPrice()));
//			}
//
//			pstmt.setInt(11, apply.getNum());
//			pstmt.setInt(12, apply.getOldNum());
//			pstmt.setString(13, apply.getMeasure());
//			// System.out.print(apply.getMeasure());
//			pstmt.setString(14, apply.getManufacturer());
//			pstmt.setString(15, apply.getKeeper());
//			pstmt.setString(16, apply.getLocation());
//			pstmt.setString(17, apply.getStorageTime());
//			pstmt.setString(18, apply.getMaintainCycle());
//			pstmt.setTimestamp(19,
//					MyDateFormat.changeToSqlDate(apply.getProducedDate()));
//			pstmt.setTimestamp(20,
//					MyDateFormat.changeToSqlDate(apply.getExecDate()));
//			pstmt.setString(21, apply.getRemark());
//			pstmt.setString(22, apply.getContractId());
//			pstmt.setString(23, apply.getChStatus());
//			pstmt.setString(24, apply.getProductCode());
//			pstmt.setString(25, apply.getPMNM());
//			pstmt.execute();
//			// System.out.println(pstmt.execute());
//			flag = true;
//			// 插入成功后 获取插入记录的ID
//			if (flag) {
//
//				rs = pstmt1.executeQuery();
//				while (rs.next()) {
//					inId = rs.getLong("inId");
//					System.out.println(inId);
//				}
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBConnection.close(conn, pstmt);
//		}
//		return inId;
//	}

	/**
	 * 存入数据库
	 * @param dyadic
	 * 			这个二位数组的第一行应该对应数据库的标题行
	 * @return
	 */
	public boolean saveInApplys(List<ArrayList<String>> dyadic){
		
		if(dyadic == null)return false;
		
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		
		String sql = StringUtil.combineSQLString("qy_inapply", dyadic.get(0));
		
		System.out.println("sql = "+sql);
		
		int columnLen = dyadic.get(0).size();
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			
			int len = dyadic.size();
			for(int i = 1; i < len; i++){
				ArrayList<String> dataList = dyadic.get(i);
				int index = 1;
				for(int k = 0; k < columnLen; k++){
					String tempStr = dataList.get(k);
					if("".equals(tempStr)){
						tempStr = null;
					}
					ps.setString(index++, tempStr);
				}
				ps.execute();
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				flag = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, ps);
		}
		return flag;
	}
	
	/**
	 * 存入数据库
	 * 参数第一行应该对应数据库的标题行
	 * @param applyDyadic apply表
	 * @param relationDyadic relation表
	 * @param productDyadic product表
	 * @return
	 */
//	public boolean saveInApplys(List<ArrayList<String>> applyDyadic, 
//							List<ArrayList<String>> relationDyadic, 
//							List<ArrayList<String>> productDyadic){
//		
//		if(applyDyadic == null || relationDyadic==null || productDyadic==null)
//			return false;
//		
//		boolean flag = true;
//		Connection conn = null;
//		PreparedStatement ps = null;
//		PreparedStatement ps2 = null;
//		PreparedStatement ps3 = null;
//		
//		//生成sql语句
//		String applySQL = StringUtil.combineSQLString("qy_inapply", applyDyadic.get(0));
//		String relationSQL = StringUtil.combineSQLString("qy_inproductrelation", relationDyadic.get(0));
//		String productSQL = StringUtil.combineSQLString("qy_product", productDyadic.get(0));
//		
//		int applycolumnLen = applyDyadic.get(0).size();
//		int relationcolumnLen = relationDyadic.get(0).size();
//		int productcolumnLen = productDyadic.get(0).size();
//		try {
//			conn = DBConnection.getConn();
//			conn.setAutoCommit(false);
//			ps = conn.prepareStatement(applySQL);
//			ps2 = conn.prepareStatement(relationSQL);
//			ps3 = conn.prepareStatement(productSQL);
//			
//			int applylen = applyDyadic.size();
//			for(int i = 1; i < applylen; i++){
//				ArrayList<String> dataList = applyDyadic.get(i);
//				int index = 1;
//				for(int k = 0; k < applycolumnLen; k++){
//					String tempStr = dataList.get(k);
//					if("".equals(tempStr)){
//						tempStr = null;
//					}
//					ps.setString(index++, tempStr);
//				}
//				ps.execute();
//				ps.addBatch();
//			}
//			int relationLen = relationDyadic.size();
//			for(int i = 1; i < relationLen; i++){
//				ArrayList<String> dateList = relationDyadic.get(i);
//				int index = 1;
//				for(int k = 0; k < relationcolumnLen; k++){
//					String tempStr = dateList.get(k);
//					if("".equals(tempStr)){
//						tempStr = null;
//					}
//					ps2.setString(index++, tempStr);
//				}
//				ps2.execute();
//				ps2.addBatch();
//			}
//			int productLen = productDyadic.size();
//			for(int i = 1; i < productLen; i++){
//				ArrayList<String> dateList = productDyadic.get(i);
//				int index = 1;
//				for(int k = 0; k < productcolumnLen; k++){
//					String tempStr = dateList.get(k);
//					if("".equals(tempStr)){
//						tempStr = null;
//					}
//					ps3.setString(index++, tempStr); 
//				}
//				ps3.execute();
//				ps3.addBatch();
//			}
//			
//			ps.executeBatch();
//			ps2.executeBatch();
//			ps3.executeBatch();
//			conn.commit();
//		} catch (SQLException e) {
//			try {
//				conn.rollback();
//				flag = false;
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}finally{
//			DBConnection.close(conn, ps);
//			try {
//				ps2.close();
//				ps3.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return flag;
//	}
//	
	/**
	 * 把一组apply对象中的信息插入到数据库中
	 * 
	 * @param apply
	 * @return 批处理方法executeBatch()执行返回的值
	 */
	/*public boolean saveApply(List<InApply> list) {

		boolean flag = false;

		java.util.Date insertTime = new java.util.Date();
		//改变下对齐方式，方便查看byLiuyutian
		String sql = "Insert into qy_inapply ("
				+ "inMeans,productType,oldType,wholeName,unitName,batch,deviceNo,"
				+ "unit,newPrice,oldPrice,num,oldNum,measure,manufacturer,keeper,"
				+ "location,storageTime,maintainCycle,producedDate,execDate,"
				+ "remark, contractId,chStatus,productCode,"
				+ "PMNM,insertTime,inId" +
						") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE "
						+ "inMeans=VALUES(inMeans),productType=VALUES(productType),oldType=VALUES(oldType),"
						+ "wholeName=VALUES(wholeName),unitName=VALUES(unitName),batch=VALUES(batch),"
						+ "deviceNo=VALUES(deviceNo),unit=VALUES(unit),newPrice=VALUES(newPrice),oldPrice=VALUES(oldPrice),"
						+ "num=VALUES(num),oldNum=VALUES(oldNum),measure=VALUES(measure),manufacturer=VALUES(manufacturer),"
						+ "keeper=VALUES(keeper),location=VALUES(location),storageTime=VALUES(storageTime),maintainCycle=VALUES(maintainCycle),"
						+ "producedDate=VALUES(producedDate),execDate=VALUES(execDate),remark=VALUES(remark),contractId=VALUES(contractId),"
						+ "chStatus=VALUES(chStatus),productCode=VALUES(productCode),PMNM=VALUES(PMNM),insertTime=VALUES(insertTime)";
		try {

			conn = DBConnection.getConn();
			// 2015.06.02 add by liuyutian
			conn.setAutoCommit(false);
			// end
			pstmt = conn.prepareStatement(sql);

			Iterator<InApply> iter = list.iterator();
			InApply apply = null;
			while (iter.hasNext()) {
				apply = iter.next();

				pstmt.setString(1, apply.getInMeans());
				pstmt.setString(2, apply.getProductType());
				pstmt.setString(3, apply.getOldType());
				pstmt.setString(4, apply.getWholeName());
				pstmt.setString(5, apply.getUnitName());
				pstmt.setString(6, apply.getBatch());
				pstmt.setString(7, apply.getDeviceNo());
				pstmt.setString(8, apply.getUnit());
				pstmt.setDouble(9, Double.valueOf(apply.getNewPrice()));
				pstmt.setDouble(10, Double.valueOf(apply.getOldPrice()));
				pstmt.setInt(11, apply.getNum());
				pstmt.setInt(12, apply.getOldNum());
				pstmt.setString(13, apply.getMeasure());
				pstmt.setString(14, apply.getManufacturer());
				pstmt.setString(15, apply.getKeeper());
				pstmt.setString(16, apply.getLocation());
				pstmt.setString(17, apply.getStorageTime());
				pstmt.setString(18, apply.getMaintainCycle());
				pstmt.setTimestamp(19,
						MyDateFormat.changeToSqlDate(apply.getProducedDate()));
				pstmt.setTimestamp(20,
						MyDateFormat.changeToSqlDate(apply.getExecDate()));
				pstmt.setString(21, apply.getRemark());
				pstmt.setString(22, apply.getContractId());
				pstmt.setString(23, apply.getChStatus());
				pstmt.setString(24, apply.getProductCode());
				pstmt.setString(25, apply.getPMNM());

				// 添加查入的时间，相同插入批次的，时间一样
				pstmt.setTimestamp(26, MyDateFormat.changeToSqlDate(insertTime));
				pstmt.setLong(27, apply.getInId());
				pstmt.addBatch();
			}
			int[] returnArray = pstmt.executeBatch();
			// 2015.06.02 add by liuyutian
			conn.commit();
			if (returnArray.length == list.size()) {
				flag = true;
			}
			// end
			pstmt.clearBatch();
		} catch (SQLException e) {
			// 2015.06.12 add by liuyutian
			try {
				conn.rollback();
			} catch (SQLException sqlE) {
				sqlE.printStackTrace();
			}
			// end
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
*/
	public List<HashMap<String, Object>> getInApply(int curPageNum, int pageSize) {
		String sql = "Select * from qy_inapply where inMeans IN ('新入库','轮换入库','更新入库','维护入库','作战入库') order by inId desc limit ?, ? ";
		String sql2 = "Select count(*) from qy_inapply where inMeans IN ('新入库','轮换入库','更新入库','维护入库','作战入库')";
		String sql3 = "select productId from qy_inproductrelation where inId =?";
		String sql4 = "select * from qy_product where productId=?";

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		long pid = 0;
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (curPageNum - 1) * pageSize);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();

			pstmt = conn.prepareStatement(sql2);
			ResultSet rs2 = pstmt.executeQuery();

			ResultSet rsid = null;
			ResultSet rsMo = null;

			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				InApply apply = new InApply();
				apply.setInId(rs.getLong("inId"));

				pstmt = conn.prepareStatement(sql3);
				pstmt.setLong(1, apply.getInId());
				rsid = pstmt.executeQuery();
				if (rsid.next()) {
					pid = rsid.getLong("productId");
				}

				pstmt = conn.prepareStatement(sql4);
				pstmt.setLong(1, pid);
				Product pro = new Product();
				rsMo = pstmt.executeQuery();
				if (rsMo.next()) {
					pro.setProductModel(rsMo.getString("productModel"));
				}

				apply.setContractId(rs.getString("contractId"));
				apply.setInMeans(rs.getString("inMeans"));
				apply.setProductType(rs.getString("ProductType"));
				apply.setOldType(rs.getString("oldType"));
				apply.setWholeName(rs.getString("wholeName"));
				apply.setUnitName(rs.getString("unitName"));
				apply.setBatch(rs.getString("batch"));
				apply.setDeviceNo(rs.getString("deviceNo"));
				apply.setUnit(rs.getString("unit"));
				apply.setNewPrice(rs.getDouble("newPrice"));
				apply.setOldPrice(rs.getDouble("oldPrice"));
				apply.setNum(rs.getInt("num"));
				apply.setOldNum(rs.getInt("oldNum"));
				apply.setMeasure(rs.getString("measure"));
				apply.setManufacturer(rs.getString("manufacturer"));
				apply.setKeeper(rs.getString("keeper"));
				apply.setProductCode(rs.getString("productCode"));
				apply.setPMNM(rs.getString("PMNM"));
				apply.setLocation(rs.getString("location"));
				apply.setStorageTime(rs.getString("storageTime"));
				apply.setMaintainCycle(rs.getString("maintainCycle"));
				apply.setProducedDate(MyDateFormat.changeToDate(rs
						.getTimestamp("producedDate")));
				apply.setExecDate(MyDateFormat.changeToDate(rs
						.getTimestamp("execDate")));
				apply.setRemark(rs.getString("remark"));
				apply.setChStatus(rs.getString("chStatus"));
				apply.setOwnedUnit(rs.getString("ownedUnit"));
				map.put("apply", apply);
				map.put("product", pro);
				list.add(map);
			}
			if (rs2.next()) {
				int sum = rs2.getInt("count(*)");
				System.out.println("sum" +sum);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("sum", sum);
				list.add(map);
			}
			if (rs2 != null) {
				rs2.close();
			}
			if (rsid != null) {
				rsid.close();
			}
			if (rsMo != null) {
				rsMo.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return list;
	}

	/*public List<InApply> selectByLatestInsertTime() {
		String sql = "Select * From qy_inapply Where execDate in (Select Max(execDate) From qy_inapply)";

		ResultSet rs = null;

		List<InApply> list = new ArrayList<InApply>();
		
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				InApply apply = new InApply();
				
				apply.setInId(rs.getLong("inId"));
				apply.setContractId(rs.getString("contractId"));
				apply.setInMeans(rs.getString("inMeans"));
				apply.setProductType(rs.getString("ProductType"));
				apply.setOldType(rs.getString("oldType"));
				apply.setWholeName(rs.getString("wholeName"));
				apply.setUnitName(rs.getString("unitName"));
				apply.setBatch(rs.getString("batch"));
				apply.setDeviceNo(rs.getString("deviceNo"));
				apply.setUnit(rs.getString("unit"));
				apply.setNewPrice(rs.getDouble("newPrice"));
				apply.setOldPrice(rs.getDouble("oldPrice"));
				apply.setNum(rs.getInt("num"));
				apply.setOldNum(rs.getInt("oldNum"));
				apply.setMeasure(rs.getString("measure"));
				apply.setManufacturer(rs.getString("manufacturer"));
				apply.setKeeper(rs.getString("keeper"));
				apply.setProductCode(rs.getString("productCode"));
				apply.setPMNM(rs.getString("PMNM"));
				apply.setLocation(rs.getString("location"));
				apply.setStorageTime(rs.getString("storageTime"));
				apply.setMaintainCycle(rs.getString("maintainCycle"));
				apply.setProducedDate(MyDateFormat.changeToDate(rs
						.getTimestamp("producedDate")));
				apply.setExecDate(MyDateFormat.changeToDate(rs
						.getTimestamp("execDate")));
				apply.setRemark(rs.getString("remark"));
				apply.setChStatus(rs.getString("chStatus"));
				
				
				list.add(apply);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return list;
	}
*/
	/*public InApply getInApply(long inId) {
		String sql = "Select * from qy_inapply where inId = ?";
		ResultSet rs = null;
		InApply apply = new InApply();
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, inId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				apply.setInId(rs.getLong("inId"));
				apply.setContractId(rs.getString("contractId"));
				apply.setInMeans(rs.getString("inMeans"));
				apply.setProductType(rs.getString("ProductType"));
				apply.setOldType(rs.getString("oldType"));
				apply.setWholeName(rs.getString("wholeName"));
				apply.setUnitName(rs.getString("unitName"));
				apply.setBatch(rs.getString("batch"));
				apply.setDeviceNo(rs.getString("deviceNo"));
				apply.setUnit(rs.getString("unit"));
				apply.setNewPrice(rs.getDouble("newPrice"));
				apply.setOldPrice(rs.getDouble("oldPrice"));
				apply.setNum(rs.getInt("num"));
				apply.setOldNum(rs.getInt("oldNum"));
				apply.setMeasure(rs.getString("measure"));
				apply.setManufacturer(rs.getString("manufacturer"));
				apply.setKeeper(rs.getString("keeper"));
				apply.setProductCode(rs.getString("productCode"));
				apply.setPMNM(rs.getString("PMNM"));
				apply.setLocation(rs.getString("location"));
				apply.setStorageTime(rs.getString("storageTime"));
				apply.setMaintainCycle(rs.getString("maintainCycle"));
				apply.setProducedDate(MyDateFormat.changeToDate(rs
						.getTimestamp("producedDate")));
				apply.setExecDate(MyDateFormat.changeToDate(rs
						.getTimestamp("execDate")));
				apply.setRemark(rs.getString("remark"));
				apply.setChStatus(rs.getString("chStatus"));

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return apply;
	}
*/
	

	public long getIdByExecDate(java.util.Date date) {
		long inId;
		try {
			this.conn = DBConnection.getConn();
			String sql = "select inId from qy_inapply where execDate = ?";
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setTimestamp(1, MyDateFormat.changeToSqlDate(date));
			ResultSet rs = this.pstmt.executeQuery();
			if (rs.next()) {
				inId = rs.getLong(1);
				return inId;
			}
			return 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return (Long) null;
		} finally {
			DBConnection.close(conn, pstmt);
		}
	}

	public boolean saveSingleApply(InApply apply) {
		boolean flag = false;
		String sql = "Insert into qy_inapply ("
				+ "contractId,"
				+ "inMeans,"
				+ "wholeName,"
				+ "unitName,"
				+ "newPrice,"
				+ "num,"
				+ "measure,"
				+ "manufacturer,"
				+ "keeper,"
				+ "location,"
				+ "maintainCycle,"
				+ "producedDate,"
				+ "execDate,"
				+ "chStatus,"
				+ "productCode,"
				+ "PMNM,remark,batch,deviceNo,storageTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, apply.getContractId());
			pstmt.setString(2, apply.getInMeans());
			pstmt.setString(3, apply.getWholeName());
			pstmt.setString(4, apply.getUnitName());
			pstmt.setDouble(5, Double.valueOf(apply.getNewPrice()));
			pstmt.setInt(6, apply.getNum());
			pstmt.setString(7, apply.getMeasure());
			pstmt.setString(8, apply.getManufacturer());
			pstmt.setString(9, apply.getKeeper());
			pstmt.setString(10, apply.getLocation());
			pstmt.setString(11, apply.getMaintainCycle());
			pstmt.setTimestamp(12,
					MyDateFormat.changeToSqlDate(apply.getProducedDate()));
			pstmt.setTimestamp(13,
					MyDateFormat.changeToSqlDate(apply.getExecDate()));
			pstmt.setString(14, apply.getChStatus());
			pstmt.setString(15, apply.getProductCode());
			pstmt.setString(16, apply.getPMNM());
			this.pstmt.setString(17, "暂无");
			pstmt.setString(18, apply.getBatch());
			pstmt.setString(19, apply.getDeviceNo());
			pstmt.setString(20, apply.getStorageTime());
			int x = this.pstmt.executeUpdate();
			if (x > 0)
				flag = true;
			else
				flag = false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	public boolean addInProductRelation(long inId, long productId) {
		try {
			this.conn = DBConnection.getConn();
			String sql = "insert into qy_inproductrelation(inId,productId) values (?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setLong(1, inId);
			this.pstmt.setLong(2, productId);
			int x = this.pstmt.executeUpdate();
			if (x > 0)
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
	 * 计算总行数
	 * 
	 * @return
	 */
	public int getSum() {
		int count = 0;
		try {
			conn = DBConnection.getConn();
			String sql = "select count(*) from qy_inapply";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {

		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return count;
	}

	public List<InApply> getInApply(int curPageNum, int pageSize, boolean flag) {
		String sql = "Select * from qy_inapply order by inId desc limit ?, ? ";
		ResultSet rs = null;

		List<InApply> list = new ArrayList<InApply>();

		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, (curPageNum - 1) * pageSize);
			pstmt.setInt(2, pageSize);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				InApply apply = new InApply();
				apply.setInId(rs.getLong("inId"));
				apply.setContractId(rs.getString("contractId"));
				apply.setInMeans(rs.getString("inMeans"));
				apply.setProductType(rs.getString("ProductType"));
				apply.setOldType(rs.getString("oldType"));
				apply.setWholeName(rs.getString("wholeName"));
				apply.setUnitName(rs.getString("unitName"));
				apply.setBatch(rs.getString("batch"));
				apply.setDeviceNo(rs.getString("deviceNo"));
				apply.setUnit(rs.getString("unit"));
				apply.setNewPrice(rs.getDouble("newPrice"));
				apply.setOldPrice(rs.getDouble("oldPrice"));
				apply.setNum(rs.getInt("num"));
				apply.setOldNum(rs.getInt("oldNum"));
				apply.setMeasure(rs.getString("measure"));
				apply.setManufacturer(rs.getString("manufacturer"));
				apply.setKeeper(rs.getString("keeper"));
				apply.setProductCode(rs.getString("productCode"));
				apply.setPMNM(rs.getString("PMNM"));
				apply.setLocation(rs.getString("location"));
				apply.setStorageTime(rs.getString("storageTime"));
				apply.setMaintainCycle(rs.getString("maintainCycle"));
				apply.setProducedDate(MyDateFormat.changeToDate(rs
						.getTimestamp("producedDate")));
				apply.setExecDate(MyDateFormat.changeToDate(rs
						.getTimestamp("execDate")));
				apply.setRemark(rs.getString("remark"));
				apply.setChStatus(rs.getString("chStatus"));

				list.add(apply);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return list;
	}

	

	/**
	 * 根据产品表里面的品名代码和名称型号来查询
	 * 
	 * @param resultList将查询的结果放到这里面来
	 * @param conditionList
	 *            map中的key有productCode和productModel，作为查询条件
	 * @return 函数执行的状态
	 */
	public boolean selectInApply(List<InApply> resultList,
			List<Map<String, String>> conditionList) {
		boolean runStatus = false;

		String sql = "select * from qy_inapply where inId in("
				+ "Select inId From qy_inproductrelation Where productId In ("
				+ "Select productId from qy_product where productCode=? And productModel=?))";
		try {
			conn = DBConnection.getConn();
			// conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(sql);

			String productCode = "";
			String productModel = "";
			for (Map<String, String> conditionMap : conditionList) {
				try {
					productCode = conditionMap.get("productCode");
					productModel = conditionMap.get("productModel");

					pstmt.setString(1, productCode);
					pstmt.setString(2, productModel);
					rs = pstmt.executeQuery();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("数据库查询异常");
					runStatus = false;
				}

				while (rs.next()) {
					InApply apply = new InApply();

					apply.setInId(rs.getLong("inId"));
					apply.setContractId(rs.getString("contractId"));
					apply.setInMeans(rs.getString("inMeans"));
					apply.setProductType(rs.getString("ProductType"));
					apply.setOldType(rs.getString("oldType"));
					apply.setWholeName(rs.getString("wholeName"));
					apply.setUnitName(rs.getString("unitName"));
					apply.setBatch(rs.getString("batch"));
					apply.setDeviceNo(rs.getString("deviceNo"));
					apply.setUnit(rs.getString("unit"));
					apply.setNewPrice(rs.getDouble("newPrice"));
					apply.setOldPrice(rs.getDouble("oldPrice"));
					apply.setNum(rs.getInt("num"));
					apply.setOldNum(rs.getInt("oldNum"));
					apply.setMeasure(rs.getString("measure"));
					apply.setManufacturer(rs.getString("manufacturer"));
					apply.setKeeper(rs.getString("keeper"));
					apply.setProductCode(rs.getString("productCode"));
					apply.setPMNM(rs.getString("PMNM"));
					apply.setLocation(rs.getString("location"));
					apply.setStorageTime(rs.getString("storageTime"));
					apply.setMaintainCycle(rs.getString("maintainCycle"));
					apply.setProducedDate(MyDateFormat.changeToDate(rs
							.getTimestamp("producedDate")));
					apply.setExecDate(MyDateFormat.changeToDate(rs
							.getTimestamp("execDate")));
					apply.setRemark(rs.getString("remark"));
					apply.setChStatus(rs.getString("chStatus"));

					resultList.add(apply);
				}
				// pstmt.addBatch();
			}
			// pstmt.executeBatch();
			// conn.commit();
			// conn.setAutoCommit(true);
			runStatus = true;
		} catch (SQLException e) {
			runStatus = false;
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return runStatus;
	}

	/**
	 * 
	 * 组合查询、模糊查询以及时间段查询
	 * 
	 * @param contractId
	 *            合同id
	 * @param productType
	 *            产品类型
	 * @param unitName
	 *            单元名称
	 * @param operateType
	 *            操作类型，只能取值为newIn,circleIn,circleOut,renowIn,renewOut
	 * @param fromDate
	 *            开始的时间
	 * @param toDate
	 *            结束时的时间
	 * @param status
	 *            审核状态
	 * @param keeper
	 * @param manufacturer
	 * @return 数组中的元素是hashMap，第一个Map的键是count（只有一个entry）
	 *         其余的是Map的键是apply和product，其中前者是InApply对象 后者是Product对象
	 */
	public List<HashMap<String, Object>> selectInApply(String contractId,
			String productModel, String unitName, String operateType,
			String fromDate, String toDate, String status, String keeper, 
			String manufacturer, int curPageNum, int pageSize) {
		
		Connection conn2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;

		PreparedStatement pstmt2 = null;
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		// 标记所传入的形参operateType是否为空
		boolean operateTypeFlag = false;
		StringBuffer buf = new StringBuffer();
		buf.append(" Select distinct i.inId,i.contractid,inMeans,batch,num,execDate,oldNum,chStatus,i.ownedUnit from qy_inapply as i,qy_product as p,"
				+ "qy_inproductrelation as r "
				+ "where i.inId = r.inId and r.productId = p.productId");
		StringBuffer sum = new StringBuffer();
		sum.append(" Select distinct count(*) from qy_inapply as i,qy_product as p,"
				+ "qy_inproductrelation as r "
				+ "where i.inId = r.inId and r.productId = p.productId");
		if (contractId != null && !"".equals(contractId)
				&& !"null".equals(contractId)) {
			buf.append(" and i.contractId regexp '" + contractId + "'");
			sum.append(" and i.contractId regexp '" + contractId + "'");
		}
		if (productModel != null && !"".equals(productModel)
				&& !"null".equals(productModel)) {
			buf.append(" and p.productModel regexp '" + productModel + "'");
			sum.append(" and p.productModel regexp '" + productModel + "'");
		}
		if (unitName != null && !"".equals(unitName)
				&& !"null".equals(unitName)) {
			buf.append(" and p.productUnit regexp '" + unitName + "'");
			sum.append(" and p.productUnit regexp '" + unitName + "'");
		}
		
		if (keeper != null && !"".equals(keeper) && !"null".equals(keeper) && !"所有企业".equals(keeper) &&!"所有军代室".equals(keeper)) {
			buf.append(" and i.ownedUnit regexp '" + keeper + "'");
			sum.append(" and i.ownedUnit regexp '" + keeper + "'");
		}
		if (manufacturer != null && !"".equals(manufacturer)
				&& !"null".equals(manufacturer)) {
			buf.append(" and i.manufacturer regexp '" + manufacturer + "'");
			sum.append(" and i.manufacturer regexp '" + manufacturer + "'");
		}
		if (operateType != null && !"".equals(operateType)
				&& !"allIn".equals(operateType) && !"null".equals(operateType)) {
			buf.append(" and i.inMeans = ? ");
			sum.append(" and i.inMeans = ? ");
			operateTypeFlag = true;
		}else if("allIn".equals(operateType)) {
			buf.append(" and i.inMeans in ('新入库','轮换入库','更新入库','维护入库','作战入库') ");
			sum.append(" and i.inMeans in ('新入库','轮换入库','更新入库','维护入库','作战入库') ");
		}

		/**
		 * 使用时间段查询 如果开始时间和结束时间都不为空，则查询这个时间段内的信息
		 * 如果开始时间为非空，结束时间为空，则查询从“开始时间”到现在的信息
		 * 如果开始时间为空，结束时间为非空，则查询“最早的时间”到“结束时间”这个时间段内的信息
		 */
		if (fromDate != null && toDate != null && !"".equals(fromDate)
				&& !"".equals(toDate) && !"null".equals(fromDate)
				&& !"null".equals(toDate)) {
			buf.append(" and i.execDate BETWEEN DATE('"+ fromDate+ "') And DATE('" + toDate + "') ");
			sum.append(" and i.execDate BETWEEN DATE('"+ fromDate+ "') And DATE('" + toDate + "') ");
		} else if (fromDate != null && !"".equals(fromDate)
				&& !"null".equals(fromDate)) {
			buf.append(" and i.execDate BETWEEN DATE('"+ fromDate+ "') And CURRENT_DATE() ");
			sum.append(" and i.execDate BETWEEN DATE('"+ fromDate+ "') And CURRENT_DATE() ");
		} else if (toDate != null && !"".equals(toDate)
				&& !"null".equals(toDate)) {
			buf.append(" and i.execDate <= DATE('" + toDate + "') ");
			sum.append(" and i.execDate <= DATE('" + toDate + "') ");
		}

		if (status != null && !"".equals(status)) {
			buf.append(" and i.chStatus regexp '" + status + "'");
			sum.append(" and i.chStatus regexp '" + status + "'");
		}

		buf.append(" Order By i.inId DESC limit ?,?");
		sum.append(" Order By i.inId DESC");
		try {
			conn = DBConnection.getConn();
			System.out.println(buf.toString());
			pstmt = conn.prepareStatement(buf.toString());

			conn2 = DBConnection.getConn();
			pstmt2 = conn2.prepareStatement(sum.toString());

			int i = 1;
			if (operateTypeFlag) {
				if ("newIn".equals(operateType)) {
					pstmt.setString(i, "新入库");
					pstmt2.setString(i, "新入库");
					i++;
				} else if ("circleIn".equals(operateType)) {
					pstmt.setString(i, "轮换入库");
					pstmt2.setString(i, "轮换入库");
					i++;
				} else if ("renowIn".equals(operateType)) {
					pstmt.setString(i, "更新入库");
					pstmt2.setString(i, "更新入库");
					i++;
				}else{
					pstmt.setString(i, "1");
					pstmt2.setString(i, "1");
					i++;
				}
			}
			
			pstmt.setInt(i++, (curPageNum - 1) * pageSize);
			pstmt.setInt(i++, pageSize);
			rs = pstmt.executeQuery();
			rs2 = pstmt2.executeQuery();
			
			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				InApply apply = new InApply();
				apply.setInId(rs.getLong("inId"));
				apply.setContractId(rs.getString("contractId"));
				apply.setInMeans(rs.getString("inMeans"));
				apply.setOwnedUnit(rs.getString("ownedUnit"));
				/*apply.setProductType(rs.getString("ProductType"));
				apply.setOldType(rs.getString("oldType"));
				apply.setWholeName(rs.getString("wholeName"));
				apply.setUnitName(rs.getString("unitName"));*/
				apply.setBatch(rs.getString("batch"));
//				apply.setDeviceNo(rs.getString("deviceNo"));
//				apply.setUnit(rs.getString("unit"));
				/*if (rs.getString("newPrice") != null) {
					apply.setNewPrice(Double.parseDouble(rs.getString("newPrice")));
				}
				if (rs.getString("oldPrice") != null) {
					apply.setOldPrice(Double.parseDouble(rs.getString("oldPrice")));
				}*/
				apply.setNum(rs.getInt("num"));
				apply.setOldNum(rs.getInt("oldNum"));
				/*apply.setMeasure(rs.getString("measure"));
				apply.setManufacturer(rs.getString("manufacturer"));
				apply.setKeeper(rs.getString("keeper"));
				apply.setProductCode(rs.getString("productCode"));
				apply.setPMNM(rs.getString("PMNM"));
				apply.setLocation(rs.getString("location"));*/

				/*apply.setStorageTime(rs.getString("storageTime"));

				apply.setMaintainCycle(rs.getString("maintainCycle"));
				apply.setProducedDate(MyDateFormat.changeToDate(rs
						.getTimestamp("producedDate")));*/
				apply.setExecDate(MyDateFormat.changeToDate(rs
						.getTimestamp("execDate")));
				//apply.setRemark(rs.getString("remark"));
				apply.setChStatus(rs.getString("chStatus"));
				Product pro = new Product();
				//pro.setProductModel(rs.getString("productModel"));
				
				map.put("apply", apply);
				map.put("product", pro);
				list.add(map);
			}
			if (rs2.next()) {
				int count = rs2.getInt("count(*)");
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("count", count);
				list.add(map);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
			DBConnection.close(conn2, pstmt2, rs2);
		}
		return list;
	}
	
	/**
	 * @author limengxin 这个函数是军代室版本---列表查询--查询申请 用滴
	 */
//	public List<HashMap<String, Object>> selectApply(String contractId,
//			String productType, String unitName, String operateType,
//			String keeper, String manufacturer, java.sql.Timestamp fromDate,
//			java.sql.Timestamp toDate, String status, int curPageNum,
//			int pageSize) {
//		ResultSet rs = null;
//		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//
//		// 标记所传入的形参是否为空
//		boolean operateTypeFlag = false;
//		boolean timeFlag = false;
//		
//		int i = 1;
//		StringBuffer buf = new StringBuffer();
//		buf.append(" Select distinct * from qy_inapply as i,qy_product as p,"
//				+ "qy_inproductrelation as r "
//				+ "where i.inId = r.inId and r.productId = p.productId");
//		StringBuffer sum = new StringBuffer();
//		sum.append(" Select distinct count(*) from qy_inapply as i,"
//				+ "qy_product as p,qy_inproductrelation as r "
//				+ "where i.inId = r.inId and r.productId = p.productId");
//		if (contractId != null && !"".equals(contractId)&& !"null".equals(contractId)) {
//			buf.append(" and i.contractId regexp '" + contractId + "'");
//			sum.append(" and i.contractId regexp '" + contractId + "'");
//		}
//		if (keeper != null && !"".equals(keeper) && !"null".equals(keeper)) {
//			buf.append(" and i.keeper regexp '" + keeper + "'");
//			sum.append(" and i.keeper regexp '" + keeper + "'");
//		}
//		if (manufacturer != null && !"".equals(manufacturer)
//				&& !"null".equals(manufacturer)) {
//			buf.append(" and i.manufacturer regexp '" + manufacturer + "'");
//			sum.append(" and i.manufacturer regexp '" + manufacturer + "'");
//		}
//		if (productType != null && !"".equals(productType)
//				&& !"null".equals(productType)) {
//			buf.append(" and p.productModel regexp '" + productType + "'");
//			sum.append(" and p.productModel regexp '" + productType + "'");
//		}
//		if (unitName != null && !"".equals(unitName)
//				&& !"null".equals(unitName)) {
//			buf.append(" and i.unitName regexp '" + unitName + "'");
//			sum.append(" and i.unitName regexp '" + unitName + "'");
//		}
//		if (operateType != null && !"".equals(operateType)
//				&& !"allIn".equals(operateType) && !"null".equals(operateType)) {
//			buf.append(" and i.inMeans = ? ");
//			sum.append(" and i.inMeans = ? ");
//			operateTypeFlag = true;
//		}else if("allIn".equals(operateType)) {
//			buf.append(" and i.inMeans in ('新入库','轮换入库','更新入库','维护入库','作战入库') ");
//			sum.append(" and i.inMeans in ('新入库','轮换入库','更新入库','维护入库','作战入库') ");
//		}
//		
//		if (fromDate != null && toDate != null) {
//			buf.append(" and i.contractId in (Select contractId From qy_contract Where (? < signDate) && (signDate <?)) ");
//			sum.append(" and i.contractId in (Select contractId From qy_contract Where (? < signDate) && (signDate <?)) ");
//			timeFlag = true;
//		}
//		if (status != null && !"".equals(status)) {
//			buf.append(" and i.chStatus regexp '" + status + "'");
//			sum.append(" and i.chStatus regexp '" + status + "'");
//		}
//
//		buf.append("Order By i.inId ASC limit ?,?");
//		sum.append("Order By i.inId ASC");
//		try {
//			conn = DBConnection.getConn();
//			pstmt = conn.prepareStatement(buf.toString());
//			PreparedStatement pstmt2 = conn.prepareStatement(sum.toString());
//
//			if (operateTypeFlag) {
//				if ("newIn".equals(operateType)) {
//					pstmt.setString(i, "新入库");
//					pstmt2.setString(i, "新入库");
//					i++;
//				} else if ("circleIn".equals(operateType)) {
//					pstmt.setString(i, "轮换入库");
//					pstmt2.setString(i, "轮换入库");
//					i++;
//				
//				} else if ("renowIn".equals(operateType)) {
//					pstmt.setString(i, "更新入库");
//					pstmt2.setString(i, "更新入库");
//					i++;
//				} 
//			}
//			if (timeFlag) {
//				pstmt.setTimestamp(i, fromDate);
//				pstmt2.setTimestamp(i, fromDate);
//				i++;
//				pstmt.setTimestamp(i, toDate);
//				pstmt2.setTimestamp(i, toDate);
//				i++;
//			}
//			pstmt.setInt(i++, (curPageNum - 1) * pageSize);
//			pstmt.setInt(i++, pageSize);
//			rs = pstmt.executeQuery();
//			ResultSet rs2 = pstmt2.executeQuery();
//			while (rs.next()) {
//				HashMap<String, Object> map = new HashMap<String, Object>();
//				InApply apply = new InApply();
//				apply.setInId(rs.getLong("inId"));
//				apply.setOwnedUnit("ownedUnit");
//				apply.setContractId(rs.getString("contractId"));
//				apply.setInMeans(rs.getString("inMeans"));
//				apply.setProductType(rs.getString("ProductType"));
//				apply.setOldType(rs.getString("oldType"));
//				apply.setWholeName(rs.getString("wholeName"));
//				apply.setUnitName(rs.getString("unitName"));
//				apply.setBatch(rs.getString("batch"));
//				apply.setDeviceNo(rs.getString("deviceNo"));
//				apply.setUnit(rs.getString("unit"));
//				if (rs.getString("newPrice") != null) {
//					apply.setNewPrice(Double.parseDouble(rs
//							.getString("newPrice")));
//				}
//				if (rs.getString("oldPrice") != null) {
//					apply.setOldPrice(Double.parseDouble(rs
//							.getString("oldPrice")));
//				}
//				apply.setNum(rs.getInt("num"));
//				apply.setOldNum(rs.getInt("oldNum"));
//				apply.setMeasure(rs.getString("measure"));
//				apply.setManufacturer(rs.getString("manufacturer"));
//				apply.setKeeper(rs.getString("keeper"));
//				apply.setProductCode(rs.getString("productCode"));
//				apply.setPMNM(rs.getString("PMNM"));
//				apply.setLocation(rs.getString("location"));
//
//				apply.setStorageTime(rs.getString("storageTime"));
//
//				apply.setMaintainCycle(rs.getString("maintainCycle"));
//				apply.setProducedDate(MyDateFormat.changeToDate(rs
//						.getTimestamp("producedDate")));
//				apply.setExecDate(MyDateFormat.changeToDate(rs
//						.getTimestamp("execDate")));
//				apply.setRemark(rs.getString("remark"));
//				apply.setChStatus(rs.getString("chStatus"));
//				Product pro = new Product();
//				pro.setProductModel(rs.getString("productModel"));
//				map.put("apply", apply);
//				map.put("product", pro);
//				list.add(map);
//			}
//			if (rs2.next()) {
//				int count = rs2.getInt("count(*)");
//				HashMap<String, Object> map = new HashMap<String, Object>();
//				map.put("count", count);
//				list.add(map);
//			}
//			if (rs2 != null) {
//				rs2.close();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBConnection.close(conn, pstmt, rs);
//		}
//		return list;
//	}

	/**
	 * @author limengxin 军代室 列表查询 中 产品详细信息的查询
	 * @param Inid
	 * @return
	 */
	public HashMap<String, Object> queryProductbyApplyId(int Inid) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		try {
			conn = DBConnection.getConn();
			String sql = "select batch,location from qy_inapply where inId =? ";
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setLong(1, Inid);

			PreparedStatement pstmt2 = null;
			ResultSet rs2 = null;
			String sql2 = "select L.productId,L.operateType,L.operateTime from qy_inproductrelation as R,qy_log as L where R.inId =? AND R.productId=L.productId and L.operateType regexp '申请'";
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setLong(1, Inid);

			ResultSet rs = this.pstmt.executeQuery();
			rs2 = pstmt2.executeQuery();
			while (rs.next()) {
				info.put("batch", rs.getString("batch"));
				info.put("location", rs.getString("location"));
				System.out.print("jjjj:" + Inid + rs.getString("location"));
			}
			// StringBuffer buf = new StringBuffer();
			List<String> list = new ArrayList<String>();
			while (rs2.next()) {
				list.add("产品：" + rs2.getLong("L.productId") + " 时间："
						+ rs2.getDate("L.operateTime") + "操作类型："
						+ rs2.getString("L.operateType") + "\r\n");
			}
			System.out.println(list.toString());
			info.put("updateHistory", list);
			DBConnection.close(conn, pstmt2, rs2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
			// DBConnection.close(conn, pstmt2, rs2);
		}
		return info;
	}
	
	/**
	 * @author  军代室 列表查询 中 产品详细信息的查询
	 * @param Inid
	 * @return
	 */
	public ArrayList<String> queryDeviceNobyApplyId(int Inid) {
		ArrayList<String> deviceNo = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = "select deviceNo from qy_inproductrelation where inId=?";
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setLong(1, Inid);
			ResultSet rs = this.pstmt.executeQuery();
			while (rs.next()) {
				deviceNo.add(rs.getString("deviceNo"));
				//info.put("location", rs.getString("location"));
				//System.out.print("jjjj:" + Inid + rs.getString("location"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
			// DBConnection.close(conn, pstmt2, rs2);
		}
		return deviceNo;
	}
	
	/**
	 * @author  军代室 列表查询 中 产品详细信息的查询
	 * @param Inid
	 * @return
	 */
	public ArrayList<String> queryDeviceNobyOutId(int Outid) {
		ArrayList<String> deviceNo = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = "select deviceNo from qy_outproductrelation where outId=?";
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setLong(1, Outid);
			ResultSet rs = this.pstmt.executeQuery();
			while (rs.next()) {
				deviceNo.add(rs.getString("deviceNo"));
				//info.put("location", rs.getString("location"));
				//System.out.print("jjjj:" + Inid + rs.getString("location"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
			// DBConnection.close(conn, pstmt2, rs2);
		}
		return deviceNo;
	}
	/**
	 * @author 根据机号来查询其他需要展示的内容
	 * @param 
	 * @return
	 */
	public  HashMap<String, Object> queryInProductbyDeviceNo(String DeviceNo,int inId) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		Connection conn1 = null;
		Connection conn2 = null;
		Connection conn3 = null;
        Connection conn4 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet res1 = null;
		ResultSet res2 = null;
		ResultSet res3 = null;
		String sql1="Select  productModel,producedDate,productType,storageTime,location, productUnit,maintainCycle, productPrice,measureUnit, manufacturer,keeper,PMNM,contractId,buyer From qy_product where deviceNo=?";
		String sql2="Select  batch,num,execDate,inMeans From qy_inApply where  inId=?";
		String sql3="Select JDS From qy_contract where contractId in (Select contractId From qy_product where deviceNo=?)";
		try{
			conn1 = DBConnection.getConn();
			conn2 = DBConnection.getConn();
			conn3 = DBConnection.getConn();
			
			pstmt1 = conn1.prepareStatement(sql1);
			pstmt1.setString(1,DeviceNo);
			res1 = pstmt1.executeQuery();
			while(res1.next()){
				info.put("productModel",res1.getString("productModel"));
				if(res1.getString("productType")==null){
					info.put("productType","");
				}else{
					info.put("productType",res1.getString("productType"));
				}
				info.put("productUnit",res1.getString("productUnit"));
				info.put("productPrice",res1.getString("productPrice"));
				info.put("measureUnit",res1.getString("measureUnit"));
				info.put("manufacturer",res1.getString("manufacturer"));
				info.put("keeper",res1.getString("keeper"));
				info.put("PMNM",res1.getString("PMNM"));
				info.put("contractId",res1.getString("contractId"));
				info.put("maintainCycle",res1.getString("maintainCycle"));
				info.put("buyer",res1.getString("buyer"));
				info.put("producedDate",res1.getString("producedDate"));
				info.put("location",res1.getString("location"));
				info.put("storageTime",res1.getString("storageTime"));
			}
			pstmt2 = conn2.prepareStatement(sql2);
			pstmt2.setInt(1,inId);
			res2 = pstmt2.executeQuery();
			while(res2.next()){
				info.put("batch",res2.getString("batch"));
				info.put("num",res2.getInt("num"));
				info.put("execDate",res2.getString("execDate"));
				info.put("Means",res2.getString("inMeans"));
			}
			pstmt3 = conn3.prepareStatement(sql3);
			pstmt3.setString(1,DeviceNo);
			res3 = pstmt3.executeQuery();
			while(res3.next()){
				info.put("JDS",res3.getString("JDS"));
		}
			}catch (SQLException ex) {
//				try {
//					conn.rollback();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
				ex.printStackTrace();
			} finally {
				DBConnection.close(conn1, pstmt1);
				DBConnection.close(conn2, pstmt2);
				DBConnection.close(conn3, pstmt3);
			}
		return info;
	}
	
	
	/**
	 * @author 根据机号来查询其他需要展示的内容
	 * @param 
	 * @return
	 */
	public  HashMap<String, Object> queryOutProductbyDeviceNo(String DeviceNo,int outId) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		Connection conn1 = null;
		Connection conn2 = null;
		Connection conn3 = null;
        Connection conn4 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet res1 = null;
		ResultSet res2 = null;
		ResultSet res3 = null;
		String sql1="Select  productModel,productType,storageTime,producedDate,location, productUnit,maintainCycle, productPrice,measureUnit, manufacturer,keeper,PMNM,contractId,buyer From qy_product where deviceNo=?";
		String sql2="Select  batch,num,execDate,outMeans From qy_outApply where  outId=?";
		String sql3="Select JDS From qy_contract where contractId in (Select contractId From qy_product where deviceNo=?)";
		try{
			conn1 = DBConnection.getConn();
			conn2 = DBConnection.getConn();
			conn3 = DBConnection.getConn();
			
			pstmt1 = conn1.prepareStatement(sql1);
			pstmt1.setString(1,DeviceNo);
			res1 = pstmt1.executeQuery();
			while(res1.next()){
				info.put("productModel",res1.getString("productModel"));
				if(res1.getString("productType")==null){
					info.put("productType","");
				}else{
					info.put("productType",res1.getString("productType"));
				}
				info.put("productUnit",res1.getString("productUnit"));
				info.put("productPrice",res1.getString("productPrice"));
				info.put("measureUnit",res1.getString("measureUnit"));
				info.put("manufacturer",res1.getString("manufacturer"));
				info.put("location",res1.getString("location"));
				info.put("storageTime",res1.getString("storageTime"));
				info.put("producedDate",res1.getString("producedDate"));
				info.put("keeper",res1.getString("keeper"));
				info.put("PMNM",res1.getString("PMNM"));
				info.put("contractId",res1.getString("contractId"));
				info.put("maintainCycle",res1.getString("maintainCycle"));
				info.put("buyer",res1.getString("buyer"));
			}
			pstmt2 = conn2.prepareStatement(sql2);
			pstmt2.setInt(1,outId);
			res2 = pstmt2.executeQuery();
			while(res2.next()){
				info.put("batch",res2.getString("batch"));
				info.put("num",res2.getInt("num"));
				info.put("execDate",res2.getString("execDate"));
				info.put("Means",res2.getString("outMeans"));
			}
			pstmt3 = conn3.prepareStatement(sql3);
			pstmt3.setString(1,DeviceNo);
			res3 = pstmt3.executeQuery();
			while(res3.next()){
				info.put("JDS",res3.getString("JDS"));
		}
			}catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				DBConnection.close(conn1, pstmt1);
				DBConnection.close(conn2, pstmt2);
				DBConnection.close(conn3, pstmt3);
			}
		return info;
	}
	/*public boolean ChangApplyStatuebyInid(List<Integer> Inids, String opFlag,String type) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = null;
			if ("1".equals(opFlag)) {
				sql = "UPDATE qy_inapply SET chStatus='"+type+"已通过' WHERE inId = ?";
			} else if ("2".equals(opFlag)) {
				sql = "UPDATE qy_inapply SET chStatus='"+type+"未通过' WHERE inId = ?";
			}

			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < Inids.size(); i++) {
				this.pstmt.setLong(1, Inids.get(i));
				this.pstmt.executeUpdate();
			}
			flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return flag;

	}*/
	
//	/**
//	 * 企业版的轮换入库申请的操作
//	 * @author LiangYH
//	 */
//	public boolean borrowInAwareOperate(List<HashMap<String, String>> mapList){
//		boolean runStatus = true;
//		
//		PreparedStatement ps1 = null;
//		PreparedStatement ps2 = null;
//		PreparedStatement ps4 = null;
//		PreparedStatement ps5 = null;
//		ResultSet rs = null;
//		ResultSet product_rs = null;
//		
//		PreparedStatement selectOldProductPs = null;
//		ResultSet selectOldProductRs = null;
//		PreparedStatement insertProductPs = null;
//		PreparedStatement selectNewProductIDPs = null;
//		ResultSet selectNewProductIDRs = null;
//		PreparedStatement updateOldProductPs = null;
//		
//		//增加入库申请表记录
//		String inApply_insert_sql = "Insert Into qy_inapply"
//				+ "(inMeans,batch,execDate,ownedUnit,chStatus,num)"
//				+ "values(?,?,NOW(),?,'轮换入库待审核',?)";
//		//查询刚刚插入记录的键outId
//		String inId_select_sql = "SELECT @@IDENTITY AS 'inId'";
//		//增加产品关系表记录
//		String inProductRelation_insert_sql = "Insert Into qy_inproductrelation "
//				+"(inId,ownedUnit,productId,insertTime,deviceNo)values(?,?,?,NOW(),?) "
//				+"ON DUPLICATE KEY UPDATE "
//				+"inId=VALUES(inId),ownedUnit=VALUES(ownedUnit),productId=VALUES(productId),insertTime=VALUES(insertTime)";
//		//查找原产品的所以信息
//		String selectOldProductSql = "Select * From qy_product Where productId=?";
//		//插入新产品
//		String insertProductSql = "";
//		//查找刚刚插入的产品的id
//		String selectNewProductIDSql = "SELECT @@IDENTITY AS 'productId'";
//		//更新原产品
//		String updateOldProductSql = "Update qy_product Set otherProductId=?,flag=? Where productId=?";
//		
//		int inId = -1;
//		try {
//			conn = DBConnection.getConn();
//			conn.setAutoCommit(false);
//			
//			ps1 = conn.prepareStatement(inApply_insert_sql);
//			ps2 = conn.prepareStatement(inId_select_sql);
//			ps4 = conn.prepareStatement(inProductRelation_insert_sql);
//			
//			selectOldProductPs = conn.prepareStatement(selectOldProductSql);
//			selectNewProductIDPs = conn.prepareStatement(selectNewProductIDSql);
//			updateOldProductPs = conn.prepareStatement(updateOldProductSql);
//			
//			//这里多个产品对应一条申请
//			HashMap<String,String> map = null;
//			map = mapList.get(0);
//			ps1.setString(1,map.get("inMeans"));
//			ps1.setString(2, map.get("batch"));
//			ps1.setString(3, map.get("ownedUnit"));
//			ps1.setInt(4, mapList.size());
//			ps1.execute();
//			
//			//查询最后插入的条目的outId
//			rs = ps2.executeQuery();
//			if(rs.next()) inId = rs.getInt("inId");
//			
//			int len = mapList.size();
//			for(int i = 0; i < len; i++){
//				map = mapList.get(i);
//				
//				//查找原产品
//				selectOldProductPs.setString(1, map.get("productId"));
//				selectOldProductRs = selectOldProductPs.executeQuery();
//				Map<String,String> oldProductMap = new HashMap<String,String>();
//				if(selectOldProductRs.next()){
//					ResultSetMetaData metaData = selectOldProductRs.getMetaData();
//					int len2 = metaData.getColumnCount();
//					for(int k = 1; k <= len2; k++){
//						oldProductMap.put(metaData.getColumnName(k), selectOldProductRs.getString(k));
//					}
//				}
//				
//				//插入新的product
//				oldProductMap.remove("productId");//去掉productId
////				oldProductMap.put("otherProductId", map.get("oldProductId"));
//				oldProductMap.put("otherProductId", map.get("productId"));
//				oldProductMap.put("deviceNo", map.get("deviceNo"));
//				oldProductMap.put("status", "轮换入库");
//				oldProductMap.put("proStatus", "轮换入库待审核");
//				insertProductSql = combineSql(oldProductMap);
//				insertProductPs = conn.prepareStatement(insertProductSql);
//				insertProductPs.execute();
//				if(insertProductPs != null)insertProductPs.close();
//				
//				//查询刚刚插入的product的id
//				int tempNewProductID = 0;
//				selectNewProductIDRs = selectNewProductIDPs.executeQuery();
//				if(selectNewProductIDRs.next())
//					tempNewProductID = selectNewProductIDRs.getInt(1);
//				
//				//更新原来的产品
////				updateOldProductPs.setInt(1, tempNewProductID);
//				updateOldProductPs.setInt(1, 2);
//				updateOldProductPs.setString(2, map.get("productId"));
////				updateOldProductPs.setString(3, "轮换出库");
//				updateOldProductPs.execute();
//				
//				//插入申请-产品关系表
//				ps4.setInt(1, inId);
//				ps4.setString(2, map.get("ownedUnit"));
//				ps4.setInt(3, tempNewProductID);
//				ps4.setString(4, map.get("deviceNo"));
//				ps4.execute();
//				
//			}//end for circle
//			
//			//提交
//			conn.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			try {
//				//回滚
//				conn.rollback();
//				runStatus = false;
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		}finally{
//			DBConnection.close(conn, ps1, rs);
//			DBConnection.close(null, ps2, product_rs);
//			DBConnection.close(null, ps4, selectOldProductRs);
//			DBConnection.close(null, selectNewProductIDPs, selectNewProductIDRs);
//			DBConnection.close(ps5, insertProductPs, updateOldProductPs);
//		}
//		return runStatus;
//	}
//	
	/**
	 * 组合sql语句
	 * @param map
	 * @return
	 */
	private String combineSql(Map<String,String> map){
		StringBuilder builder = new StringBuilder();
		StringBuilder builder2 = new StringBuilder();
		builder.append("Insert Into qy_product (");
		builder2.append(")values(");
		
		Set<Entry<String,String>> sets = map.entrySet();
		Iterator<Entry<String,String>> iter = sets.iterator();
		String tempStr = "";
		while(iter.hasNext()){
			Entry<String,String> entry = iter.next();
			builder.append(entry.getKey()+",");
			tempStr = entry.getValue();
			if(tempStr == null || "".equals(tempStr) || "null".equals(tempStr)){
				tempStr = null;
				builder2.append("null,");
			}else{
				builder2.append("'"+tempStr+"',");
			}
		}
		builder.deleteCharAt(builder.length()-1);
		builder2.deleteCharAt(builder2.length()-1);
		
		builder2.append(")");
		
		builder.append(builder2);
		System.out.println(builder.toString());
		return builder.toString();
	}
	
	/**
	 * 这个函数是针对企业版更新或者轮换入库的
	 * @param mapList
	 * @param inMeans 更新入库或者轮换入库
	 * @return run status
	 * @author LiangYH
	 */
	public boolean update_borrow_InAwareOperate(List<HashMap<String,String>> mapList,String inMeans){
		boolean runStatus = true;
		
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps4 = null;
//		PreparedStatement update_inApply_ps = null;
		ResultSet rs = null;
		
		PreparedStatement selectOldProductPs = null;
		ResultSet selectOldProductRs = null;
		PreparedStatement insertProductPs = null;
		PreparedStatement selectNewProductIDPs = null;
		ResultSet selectNewProductIDRs = null;
		PreparedStatement updateOldProductPs = null;
		//增加入库申请表记录
		String inApply_insert_sql = "Insert Into qy_inapply"
				+ "(inMeans,batch,execDate,ownedUnit,chStatus,num)"
				+ "values(?,?,NOW(),?,?,?)";
		//查询刚刚插入记录的键outId
		String inId_select_sql = "SELECT @@IDENTITY AS 'inId'";
		
		//增加产品关系表记录
		String inProductRelation_insert_sql = "Insert Into qy_inproductrelation "
						+ "(inId,ownedUnit,productId,insertTime,deviceNo)"
						+ "values(?,?,?,NOW(),?) "
						+ "ON DUPLICATE KEY UPDATE "
						+ "inId=VALUES(inId),ownedUnit=VALUES(ownedUnit),"
						+ "productId=VALUES(productId),insertTime=VALUES(insertTime)";
		// 改变出库申请表中的备注
//		String update_inApply_sql = "Update qy_inapply set remark = ? Where inId= ? And ownedUnit=?";
		//查找原产品的所以信息
		String selectOldProductSql = "Select * From qy_product Where productId=?";
		//插入新产品
		String insertProductSql = "";
	/*	if(StringUtil.UPDATE_IN.equals(inMeans)) {
			insertProductSql = "";
		}*/
		//查找刚刚插入的产品的id
		String selectNewProductIDSql = "SELECT @@IDENTITY AS 'productId'";
		//更新原产品
		String updateOldProductSql = "Update qy_product Set flag=? Where productId=?";
		
		int inId = -1;
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			//新增申请表
			ps1 = conn.prepareStatement(inApply_insert_sql);
			//取得上面的inId
			ps2 = conn.prepareStatement(inId_select_sql);
			//插入关系表
			ps4 = conn.prepareStatement(inProductRelation_insert_sql);
//			update_inApply_ps = conn.prepareStatement(update_inApply_sql);
			
			selectOldProductPs = conn.prepareStatement(selectOldProductSql);
			selectNewProductIDPs = conn.prepareStatement(selectNewProductIDSql);
			updateOldProductPs = conn.prepareStatement(updateOldProductSql);
			
			//做一个模板
			HashMap<String,String> map = mapList.get(0);
			
			//这里多个产品对应一条申请
			ps1.setString(1,map.get("inMeans"));
			ps1.setString(2, map.get("batch"));
			ps1.setString(3, map.get("ownedUnit"));
			ps1.setString(4,inMeans+"待审核");
			ps1.setInt(5, mapList.size());
			ps1.execute();
			
			//查询最后插入的条目的outId
			rs = ps2.executeQuery();
			if(rs.next())  inId = rs.getInt("inId");
			
			int len = mapList.size();
			for(int i = 0; i < len; i++){
				map = mapList.get(i);
				//查找原产品
				selectOldProductPs.setString(1, map.get("productId"));
				selectOldProductRs = selectOldProductPs.executeQuery();
				Map<String,String> oldProductMap = new HashMap<String,String>();
				if(selectOldProductRs.next()){
					ResultSetMetaData metaData = selectOldProductRs.getMetaData();
					int len2 = metaData.getColumnCount();
					for(int k = 1; k <= len2; k++){
						oldProductMap.put(metaData.getColumnName(k), selectOldProductRs.getString(k));
					}
				}
				//去掉productId，避免数据库重复key报异常
				String oldId=  oldProductMap.get("productId");
				if(StringUtil.BORROW_IN.equals(inMeans)) {
					oldProductMap.remove("productId");
//					oldProductMap.put("otherProductId", map.get("oldProductId"));
					oldProductMap.put("otherProductId", oldId);
					oldProductMap.put("deviceNo", map.get("deviceNo"));
					oldProductMap.put("status", inMeans);
					oldProductMap.put("proStatus", inMeans+"待审核");
				}else if(StringUtil.UPDATE_IN.equals(inMeans)) {
					//如果是更新则添加所有产品
					//value需要修改
					
					oldProductMap.remove("productId");
					oldProductMap.put("productName", map.get("wholename"));
					oldProductMap.put("productUnit", map.get("unit"));
					oldProductMap.put("deviceNo", map.get("deviceNo"));
					oldProductMap.put("productPrice", map.get("price"));
					
					oldProductMap.put("manufacturer", map.get("manuf"));
					oldProductMap.put("keeper", map.get("keeper"));
					oldProductMap.put("ownedUnit", map.get("ownedUnit"));
					oldProductMap.put("location", map.get("location"));
					
					oldProductMap.put("producedDate", map.get("makeTime"));
					oldProductMap.put("remark", map.get("remark"));
					oldProductMap.put("maintainCycle", map.get("maintain"));
					//维护自己的flag和status
//					oldProductMap.put("otherProductId", map.get("oldProductId"));
					oldProductMap.put("otherProductId", oldId);
					oldProductMap.put("status", inMeans);
					oldProductMap.put("proStatus", inMeans+"待审核");
				}
				
				//插入新的product
				insertProductSql = combineSql(oldProductMap);
				insertProductPs = conn.prepareStatement(insertProductSql);
				insertProductPs.execute();
				if(insertProductPs != null) insertProductPs.close();
				
				//查询刚刚插入的product的id
				int tempNewProductID = 0;
				selectNewProductIDRs = selectNewProductIDPs.executeQuery();
				if(selectNewProductIDRs.next())
					tempNewProductID = selectNewProductIDRs.getInt(1);
				
				//更新新的产品
//				updateOldProductPs.setInt(1, Integer.parseInt(oldId));
//				updateOldProductPs.setInt(2, 1);
//				updateOldProductPs.setString(3,String.valueOf(tempNewProductID));
//				updateOldProductPs.addBatch();
				
				// 更新旧的产品
//				updateOldProductPs.setString(1, null);
				updateOldProductPs.setInt(1, 2);
				updateOldProductPs.setString(2,oldId);
				updateOldProductPs.addBatch();
				
				updateOldProductPs.executeBatch();
				//插入申请-产品关系表
				ps4.setInt(1, inId);
				ps4.setString(2, map.get("ownedUnit"));
				ps4.setInt(3, tempNewProductID);
				ps4.setString(4, map.get("deviceNo"));
				ps4.execute();
				
				//update申请表
//				double newPrice = Double.parseDouble(map.get("price"));
//				double oldPrice = Double.parseDouble(map.get("oldPrice"));
//				int oldNum = Integer.parseInt(map.get("oldNum"));
//				int newNum = Integer.parseInt(map.get("num"));
//				double price_dif = Math.abs(oldPrice*oldNum - newPrice*newNum);
//				update_inApply_ps.setString(1, String.valueOf(price_dif));
//				update_inApply_ps.setInt(2, inId);
//				update_inApply_ps.setString(3, map.get("ownedUnit"));
//				update_inApply_ps.execute();
				
			}//end for circle
			
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
				runStatus = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			DBConnection.close(conn, ps1, rs);
			DBConnection.close(null, ps2, selectOldProductRs);
			DBConnection.close(null, ps4, selectNewProductIDRs);
			DBConnection.close(selectOldProductPs,
					insertProductPs,selectNewProductIDPs,updateOldProductPs);
		}
		return runStatus;
	}
	
	/**
	 * 根据inId在inapply、product和relation表中查询
	 * 
	 * key:applys\relations\products
	 */
//	public Map<String,Object> queryMultiFormByInID(List<Long> inIDs,String ownedUnit){
//		Map<String,Object> map = new HashMap<String,Object>();
//		PreparedStatement ps1 = null;
//		PreparedStatement ps2 = null;
//		PreparedStatement ps3 = null;
//		ResultSet rs1 = null;
//		ResultSet rs2 = null;
//		ResultSet rs3 = null;
//		Connection conn = null;
//		
//		String sql_inApply = "Select * From qy_inapply Where inId = ? And ownedUnit = ?";
//		String sql_inproductrelation = "Select * From qy_inproductrelation Where inId = ? And ownedUnit = ?";
//		String sql_product = "Select * From qy_product Where productId  IN ("
//				+ "Select productId From qy_inproductrelation Where inId=? And ownedUnit=?)";
//		
//		List<ArrayList<String>> inApplys = new ArrayList<ArrayList<String>>();
//		List<ArrayList<String>> relations = new ArrayList<ArrayList<String>>();
//		List<ArrayList<String>> products = new ArrayList<ArrayList<String>>();
//		
//		ArrayList<String> inApply_headline = new ArrayList<String>();
//		ArrayList<String> relation_headline = new ArrayList<String>();
//		ArrayList<String> product_headline = new ArrayList<String>();
//		//填充标题
//		inApplys.add(inApply_headline);
//		relations.add(relation_headline);
//		products.add(product_headline);
//		
//		try {
//			conn = DBConnection.getConn();
//			ps1 = conn.prepareStatement(sql_inApply);
//			ps2 = conn.prepareStatement(sql_inproductrelation);
//			ps3 = conn.prepareStatement(sql_product);
//			
//			int len = inIDs.size();
//			for(int i = 0; i < len; i++){
//				long inID = inIDs.get(i);
//				
//				ps1.setLong(1, inID);
//				ps1.setString(2, ownedUnit);
//				rs1 =  ps1.executeQuery();
//				
//				ps2.setLong(1, inID);
//				ps2.setString(2, ownedUnit);
//				rs2 = ps2.executeQuery();
//				
//				ps3.setLong(1, inID);
//				ps3.setString(2, ownedUnit);
//				rs3 = ps3.executeQuery();
//				
//				ArrayList<String> inApply = new ArrayList<String>();
//				ArrayList<String> relation = new ArrayList<String>();
//				ArrayList<String> product = new ArrayList<String>();
//				
//				while(rs2.next() && rs1.next() && rs3.next()){
//			        
//					//得到结果集(rs)的结构信息，比如字段数、字段名等 
//					ResultSetMetaData md = rs1.getMetaData();  
//					int columnCount = md.getColumnCount(); 	
//			        for(int k = 1; k <= columnCount; k++){
//			        	inApply.add(rs1.getString(k));
//			        	//增加标题
//			        	if(inApply_headline.size() != columnCount)
//			        		inApply_headline.add(md.getColumnName(k));
//			        }
//			        
//			        //得到结果集(rs)的结构信息，比如字段数、字段名等 
//					ResultSetMetaData md2 = rs2.getMetaData();  
//					int columnCount2 = md2.getColumnCount(); 	
//			        for(int k = 1; k <= columnCount2; k++){
//			        	relation.add(rs2.getString(k));
//			        	//增加标题
//			        	if(relation_headline.size() != columnCount2)
//			        		relation_headline.add(md2.getColumnName(k));
//			        }
//			        
//			       //得到结果集(rs)的结构信息，比如字段数、字段名等 
//					ResultSetMetaData md3 = rs3.getMetaData();  
//					int columnCount3 = md3.getColumnCount(); 	
//			        for(int k = 1; k <= columnCount3; k++){
//			        	product.add(rs3.getString(k));
//			        	//增加标题
//			        	if(product_headline.size() != columnCount3)
//			        		product_headline.add(md3.getColumnName(k));
//			        }
//					inApplys.add(inApply);
//					relations.add(relation);
//					products.add(product);
//				}//end while
//			}//end for
//			
//			map.put("applys", inApplys);
//			map.put("relations", relations);
//			map.put("products", products);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				ps1.close();
//				ps2.close();
//				ps3.close();
//				rs1.close();
//				rs2.close();
//				rs3.close();
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return map;
//	}
//	
	public boolean updateOwnedUnit(List<Long> inIDs, String ownedUnit){
		PreparedStatement ps = null;
		boolean runStatus = true;
		
		String sql = "Update qy_inapply Set ownedUnit=? Where inId=?";
		try {
			Connection conn = DBConnection.getConn();
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			
			int len = inIDs.size();
			for(int i = 0; i < len; i++){
				ps.setString(1, ownedUnit);
				ps.setLong(2, inIDs.get(i));
				ps.execute();
				ps.addBatch();
			}
			
			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				runStatus = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, ps);
		}
		return runStatus;
	}
	
	/**
	 * 把apply对象中的信息插入到数据库中
	 * 这个方法只用插入真正有用的数据。以前的方法插入了很多没用的、只用保存在productId表中的数据，这样做是不合理的
	 * @param apply
	 * @return long 如果插入失败，return -1；插入成功返回inId
	 */
	public long saveApplySimple(InApply apply) {
		boolean flag = false;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		long inId = -1;
		String sql = "Insert into qy_inapply ("
				+ "inMeans,"
				+ "batch,"
				+ "execDate,"
				+ "num,"
				+ "contractId,"
				+ "chStatus,"
				+ "ownedUnit) values(?,?,?,?,?,?,?);";
		String sql1 = "SELECT @@IDENTITY AS 'inId';";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sql1);
			pstmt.setString(1, apply.getInMeans());
			pstmt.setString(2, apply.getBatch());
			pstmt.setTimestamp(3,
					MyDateFormat.changeToSqlDate(apply.getExecDate()));
			pstmt.setInt(4, apply.getNum());
			pstmt.setString(5, apply.getContractId());
			pstmt.setString(6, apply.getChStatus());
			pstmt.setString(7, apply.getOwnedUnit());
			pstmt.execute();
			// System.out.println(pstmt.execute());
			flag = true;
			// 插入成功后 获取插入记录的ID
			if (flag) {

				rs = pstmt1.executeQuery();
				while (rs.next()) {
					inId = rs.getLong("inId");
					System.out.println(inId);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return inId;
	}
	
}
