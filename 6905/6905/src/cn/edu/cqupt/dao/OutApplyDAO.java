package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.RestTime;
import cn.edu.cqupt.util.StringUtil;

public class OutApplyDAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;

	public OutApplyDAO() {

	}

	/**
	 * 改变qy_outapply表中的审核状态
	 * 
	 * @param deviceNo
	 * @param checkStatus
	 * 
	 * @return 执行成功与否
	 * 
	 * @author LiangYiHuai
	 */
	public boolean changeCheckStatus(long outId, String checkStatus) {
		boolean flag = false;
		String sql = "Update qy_outapply set chStatus = ? where outId=?";

		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, checkStatus);
			pstmt.setLong(2, outId);

			pstmt.execute();
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}

		return flag;
	}

	/**
	 * 审核（改变）出库申请表
	 * 	<p>
	 *  通过：
	 *	只改产品的状态proStatus = 已出库
     *	</p>
     *	<p>
	 *	不通过：
	 *	flag = 0， 对于status字段，去掉前面append上去的内容（如果有逗号），
	 *  如果没有逗号，不改变。对于proStatus字段，则变为已入库
	 *  </p>
	 *  <p>
	 *  企业新增：改变flag = 1， status append上去，出库的时候flag永远为1，
	 *  （至于什么时候flag变为2，全部都由入库申请的审核情况决定。）
	 *  </p>
	 * @param dyadicArray 申请表二维数组，第一行为标题行
	 * @param checkPerson 审核人，比如军代室1
	 * @return
	 * @author liangyihuai
	 */
	public boolean changeOutApplyCheckStatus(List<ArrayList<String>> dyadicArray, String checkPerson) {
		boolean flag = true;

		Connection conn1 = null;

		PreparedStatement pstmt1 = null;
		PreparedStatement selectProductPs = null;
		ResultSet selectProductRs = null;
		PreparedStatement updateProductPs = null;

		//判断outId、chStatus和ownedUnit所在列的索引
		ArrayList<String> firstRow = dyadicArray.get(0);
		int firstRowLen = firstRow.size();
		int outIdIndex = -1;
		int chStatusIndex = -1;
		int ownedUnitIndex = -1;
		for (int i = 0; i < firstRowLen; i++) {
			if ("outId".equals(firstRow.get(i))) {
				outIdIndex = i;
			} else if ("chStatus".equals(firstRow.get(i))) {
				chStatusIndex = i;
			} else if ("ownedUnit".equals(firstRow.get(i))) {
				ownedUnitIndex = i;
			}

		}
		if (outIdIndex == -1 || chStatusIndex == -1 || ownedUnitIndex == -1) {
			return false;
		}
		//更新出库申请表的状态和审核人
		String sqlInApply = "Update qy_outapply set chStatus = ?,checkPerson=? "
				+ "Where outId= ? And ownedUnit=?";
		
		String selectProductSql = "Select status,productId, ownedUnit From qy_product "
				+ "where productId IN (Select productId From qy_outproductrelation"
				+ " where outId = ? And ownedUnit=?)";
		//更新产品表的状态。一个入库申请可能对应多个产品id
//		String sqlProduct = "Update qy_product set proStatus = ?"
//				+ " where productId IN (Select productId From qy_outproductrelation"
//				+ " where outId = ? And ownedUnit=?)";
		String updateProductSql = "Update qy_product set proStatus = ?,status=?, flag=? Where productId=? And ownedUnit=?";
		try {
			conn1 = DBConnection.getConn();
			conn1.setAutoCommit(false);

			pstmt1 = conn1.prepareStatement(sqlInApply);
			selectProductPs = conn1.prepareStatement(selectProductSql);
			updateProductPs = conn1.prepareStatement(updateProductSql);
			
			String outId = "";
			String chStatus = null;
			String proStatus = null;
			String ownedUnit = null;
			int len = dyadicArray.size();
			// 注意，这里应该从1开始，因为第一行是标题
			for (int i = 1; i < len; i++) {
				outId = dyadicArray.get(i).get(outIdIndex);
				chStatus = dyadicArray.get(i).get(chStatusIndex);
				ownedUnit = dyadicArray.get(i).get(ownedUnitIndex);
				
				//更新申请表
				pstmt1.setString(1, chStatus);
				pstmt1.setString(2, checkPerson);
				pstmt1.setString(3, outId);
				pstmt1.setString(4, ownedUnit);
				pstmt1.addBatch();

				// 如果状态中没有“未”、“不”字，并且有“通过”两个字，那就表示审核通过
				if (chStatus.indexOf("未") == -1 && chStatus.indexOf("不") == -1
						&& chStatus.indexOf("通过") != -1) {
					proStatus = "已出库";
					
					selectProductPs.setString(1, outId);
					selectProductPs.setString(2, ownedUnit);
					selectProductRs = selectProductPs.executeQuery();
					while(selectProductRs.next()){
						String productId = selectProductRs.getString("productId");
						String status = selectProductRs.getString("status");
						
						updateProductPs.setString(1, proStatus);
						updateProductPs.setString(2, status);
						updateProductPs.setString(3, "1");
						updateProductPs.setString(4, productId);
						updateProductPs.setString(5, ownedUnit);
						updateProductPs.addBatch();
					}
					updateProductPs.executeBatch();
				}else{
					//审核不通过
//					if("RK".equals(applyType)) proStatus = "未申请";
//					else if("LHRK".equals(applyType) || "GXRK".equals(applyType)) proStatus = "合同销毁";
//					else if("LHCK".equals(applyType)||"GXCK".equals(applyType))proStatus = "已入库";
					proStatus = "已入库";
					
					selectProductPs.setString(1, outId);
					selectProductPs.setString(2, ownedUnit);
					selectProductRs = selectProductPs.executeQuery();
					while(selectProductRs.next()){
						String productId = selectProductRs.getString("productId");
						String status = selectProductRs.getString("status");	
						if(status != null){
							int index = status.indexOf(",");
							if(index != -1){
								status = status.substring(0, index);
							}
						}
						updateProductPs.setString(1, proStatus);
						updateProductPs.setString(2, status);
						updateProductPs.setString(3, "0");
						updateProductPs.setString(4, productId);
						updateProductPs.setString(5, ownedUnit);
						updateProductPs.addBatch();
					}
					updateProductPs.executeBatch();
				}

			}
			pstmt1.executeBatch();
			conn1.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			flag = false;
			try {
				conn1.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			DBConnection.close(conn1, pstmt1,selectProductRs);
			DBConnection.close(selectProductPs,updateProductPs);
		}
		return flag;
	}

	/**
	 * 审核出库文件
	 * @param dyadicArray 申请表二维数组
	 * @param checkPerson 审核人
	 * @param applyType 审核状态
	 * @return
	 * @author liangyihuai
	 */
//	public boolean changeOutApplyCheckStatus(List<ArrayList<String>> dyadicArray,String checkPerson, String applyType) {
//		boolean flag = false;
//
//		Connection conn1 = null;
//		Connection conn2 = null;
//
//		PreparedStatement pstmt1 = null;
//		PreparedStatement pstmt2 = null;
//		PreparedStatement selectApplyPs = null;
//		PreparedStatement updateProductPs = null;
//		ResultSet selectApplyRs = null;
//		PreparedStatement updateNewProductNoPs = null;
//
//		//判断outId、chStatus和ownedUnit所在列的索引
//		ArrayList<String> firstRow = dyadicArray.get(0);
//		int firstRowLen = firstRow.size();
//		int inIdIndex = -1;
//		int chStatusIndex = -1;
//		int ownedUnitIndex = -1;
//		for (int i = 0; i < firstRowLen; i++) {
//			if ("outId".equals(firstRow.get(i))) {
//				inIdIndex = i;
//			} else if ("chStatus".equals(firstRow.get(i))) {
//				chStatusIndex = i;
//			}else if("ownedUnit".equals(firstRow.get(i))){
//				ownedUnitIndex = i;
//			}
//		}
//		//因为有一些地方没"审核人"字段，所以此处不加checkPerson
//		if (inIdIndex == -1 | chStatusIndex == -1 || ownedUnitIndex == -1) {
//			return false;
//		}
//		//更新申请表
//		String sqlInApply = "Update qy_outapply set chStatus = ? ,checkPerson=? "
//				+ "Where outId= ? And ownedUnit=?";
//		//审核通过，更新新插入的产品的状态
//		String sqlProduct = "Update qy_product set proStatus = ?,flag=?"
//				+ " where productId in (Select productId From qy_outproductrelation"
//				+ " where outId = ? And ownedUnit=?) AND ownedUnit=?";
//		//审核不通过，更新新插入的产品的状态
//		String updateNewProductNoSql = "Update qy_product set proStatus = ?,flag=?,otherProductId=?"
//				+ " where productId in (Select productId From qy_outproductrelation"
//				+ " where outId = ? And ownedUnit=?) AND ownedUnit=?";
//		//查询产品表，找到原产品
//		String selectApplySql = "SELECT productId,otherProductId FROM qy_product " +
//				"WHERE productId IN (SELECT productId FROM qy_outproductrelation WHERE outId=? AND ownedUnit =?) AND ownedUnit=?";
//		//更新原产品（被对账）的状态以及对账
//		String updateProductSql = "UPDATE qy_product SET flag=?, otherProductId=? WHERE productId=? AND ownedUnit=?";
//		
//		try {
//			conn1 = DBConnection.getConn();
//			conn2 = DBConnection.getConn();
//			conn1.setAutoCommit(false);
//			conn2.setAutoCommit(false);
//
//			pstmt1 = conn1.prepareStatement(sqlInApply);
//			pstmt2 = conn2.prepareStatement(sqlProduct);
//			selectApplyPs = conn2.prepareStatement(selectApplySql);
//			updateProductPs = conn2.prepareStatement(updateProductSql);
//			updateNewProductNoPs = conn2.prepareStatement(updateNewProductNoSql);
//			
//			int outId = 0;
//			String chStatus = null;
//			String proStatus = null;
//			String ownedUnit = null;
//			
//			boolean accessFlag = false;
//			boolean refuseFlag = false;
//			
//			//第一行是标题
//			int len = dyadicArray.size();
//			for (int i = 1; i < len; i++) {
//				outId = Integer.parseInt(dyadicArray.get(i).get(inIdIndex));
//				chStatus = dyadicArray.get(i).get(chStatusIndex);
//				ownedUnit = dyadicArray.get(i).get(ownedUnitIndex);
//				
//				pstmt1.setString(1, chStatus);
//				pstmt1.setString(2, checkPerson);
//				pstmt1.setInt(3, outId);
//				pstmt1.setString(4, ownedUnit);
//
//				//如果状态中没有“未”、“不”字，并且有“通过”两个字，那就表示审核通过
//				if(chStatus.indexOf("未") == -1 && chStatus.indexOf("不") == -1 
//						&& chStatus.indexOf("通过") != -1){
//					
//					proStatus = "已出库";
//					accessFlag = true;
//					
//					//更新刚插入的产品
//					pstmt2.setString(1, proStatus);
//					pstmt2.setInt(2, 2);
//					pstmt2.setInt(3, outId);
//					pstmt2.setString(4, ownedUnit);
//					pstmt2.setString(5, ownedUnit);
//					
//					//原产品查询
//					selectApplyPs.setInt(1, outId);
//					selectApplyPs.setString(2, ownedUnit);
//					selectApplyPs.setString(3, ownedUnit);
//					selectApplyRs = selectApplyPs.executeQuery();
//					
//					while(selectApplyRs.next()){
//						Long productId = selectApplyRs.getLong(1);
//						Long otherProductId = selectApplyRs.getLong(2);
//					
//						//更新原产品
//						updateProductPs.setInt(1, 2);
//						updateProductPs.setLong(2, productId);
//						updateProductPs.setLong(3, otherProductId);
//						updateProductPs.setString(4, ownedUnit);
//						updateProductPs.addBatch();
//						
//					}
//					updateProductPs.executeBatch();
//				}else{
//					//如果审核不通过
//					if("RK".equals(applyType)) proStatus = "未申请";
//					else if("LHRK".equals(applyType) || "GXRK".equals(applyType)) proStatus = "合同销毁";
//					else if("LHCK".equals(applyType)||"GXCK".equals(applyType))proStatus = "已入库";
//					
//					refuseFlag = true;
//					
//					//更新刚插入的产品
//					updateNewProductNoPs.setString(1, proStatus);
//					updateNewProductNoPs.setInt(2, 1);
//					updateNewProductNoPs.setString(3,null);
//					updateNewProductNoPs.setInt(4, outId);
//					updateNewProductNoPs.setString(5, ownedUnit);
//					updateNewProductNoPs.setString(6, ownedUnit);
//					
//					//原产品查询
//					selectApplyPs.setInt(1, outId);
//					selectApplyPs.setString(2, ownedUnit);
//					selectApplyPs.setString(3, ownedUnit);
//					selectApplyRs = selectApplyPs.executeQuery();
//					
//					while(selectApplyRs.next()){
////						Long productId = selectApplyRs.getLong(1);
//						Long otherProductId = selectApplyRs.getLong(2);
//					
//						//更新原产品
//						updateProductPs.setInt(1, 1);//本来的值就是1
//						updateProductPs.setString(2, null);
//						updateProductPs.setLong(3, otherProductId);
//						updateProductPs.setString(4, ownedUnit);
//						updateProductPs.addBatch();
//					}
//					//执行更新原产品
//					updateProductPs.executeBatch();
//				}
//				//执行更新申请表
//				pstmt1.addBatch();
//				//审核通过时，更新新插入的产品
//				if(accessFlag && pstmt2 != null) pstmt2.addBatch();
//				//审核不通过时，更新新插入的产品
//				if(refuseFlag && updateNewProductNoPs != null) updateNewProductNoPs.addBatch();
//			}
//
//			pstmt1.executeBatch();
//			if(accessFlag && pstmt2 != null)pstmt2.executeBatch();
//			if(refuseFlag && updateNewProductNoPs != null)updateNewProductNoPs.executeBatch();
//
//			conn1.commit();
//			conn2.commit();
//
//			flag = true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			try {
//				conn1.rollback();
//				conn2.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		} finally {
//			DBConnection.close(conn1, pstmt1);
//			try {
//				conn2.close();
//				if(pstmt2 != null)pstmt2.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return flag;
//	}
//	
	/**
	 * 把apply对象中的信息插入到数据库中
	 * 
	 * @param apply
	 * @return 如果插入失败，return false；
	 */
	public long saveOutApply(OutApply apply) {
		boolean flag = false;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		long inId = -1;
		String sql = "Insert into qy_outapply ("
				+ "outMeans,"
				+ "ProductType,"
				+ "oldType,"
				+ "wholeName,"
				+ "unitName,"
				+ "batch,"
				+ "deviceNo,"
				+ "unit,"
				+ "newPrice,"
				+ "oldPrice,"
				+ "num,"
				+ "oldNum,"
				+ "measure,"
				+ "manufacturer,"
				+ "keeper,"
				+ "location,"
				+ "storageTime,"
				+ "maintainCycle,"
				+ "producedDate,"
				+ "execDate,"
				+ "remark, "
				+ "contractId,"
				+ "chStatus,"
				+ "productCode,"
				+ "PMNM,"
				+ "borrowLength) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql1 = "SELECT @@IDENTITY AS 'inId';";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt1 = conn.prepareStatement(sql1);
			pstmt.setString(1, apply.getOutMeans());
			pstmt.setString(2, apply.getProductType());
			pstmt.setString(3, apply.getOldType());
			pstmt.setString(4, apply.getWholeName());
			pstmt.setString(5, apply.getUnitName());
			pstmt.setString(6, apply.getBatch());
			pstmt.setString(7, apply.getDeviceNo());
			pstmt.setString(8, apply.getUnit());
			if (apply.getNewPrice() != null) {
				pstmt.setDouble(9, Double.valueOf(apply.getNewPrice()));
			}
			if (apply.getOldPrice() != null) {
				pstmt.setDouble(10, Double.valueOf(apply.getOldPrice()));
			}
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
			pstmt.setString(26, apply.getBorrowLengthString());
			pstmt.execute();
			flag = true;
			// 插入成功后 获取插入记录的ID
			if (flag) {

				rs = pstmt1.executeQuery();
				while (rs.next()) {
					inId = rs.getLong("inId");
					System.out.println("chuku啊" + inId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return inId;
	}

	/**
	 * 存入数据库，
	 * @param applyDyadic apply表
	 * @param relationDyadic relation表
	 * @param productDyadic product表
	 * @return
	 */
//	public boolean saveOutApplys(List<ArrayList<String>> applyDyadic, 
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
//		String applySQL = StringUtil.combineSQLString("qy_outapply", applyDyadic.get(0));
//		String relationSQL = StringUtil.combineSQLString("qy_outproductrelation", relationDyadic.get(0));
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
	
	/**
	 * 把apply对象中的信息插入到数据库中
	 * 
	 * @param apply
	 * @return 如果插入失败，return false；
	 * @author LiangYH
	 */
	public boolean saveOutApply(List<OutApply> list) {
		boolean flag = false;
		int returnArray[] = null;
		// 表示插入数据库的时间
		java.util.Date insertTime = new java.util.Date();

		String sql = "Insert into qy_outapply ("
				+ "outMeans,ProductType,oldType,wholeName,unitName,batch,deviceNo,unit,"
				+ "newPrice,oldPrice,num,oldNum,measure,manufacturer,keeper,location,"
				+ "storageTime,maintainCycle,producedDate,execDate,remark, contractId,"
				+ "chStatus,productCode,PMNM,borrowLength, insertTime,ownedUnit,checkPerson,outId) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) On DUPLICATE KEY UPDATE"
				+ " ProductType=VALUES(ProductType),oldType=VALUES(oldType),"
				+ "wholeName=VALUES(wholeName), unitName=VALUES(unitName),batch=VALUES(batch),"
				+ "deviceNo=VALUES(deviceNo), unit=VALUES(unit), newPrice=VALUES(newPrice),oldPrice=VALUES(oldPrice),"
				+ "num=VALUES(num), oldNum=VALUES(oldNum), measure=VALUES(measure),"
				+ "manufacturer=VALUES(manufacturer),keeper=VALUES(keeper),location=VALUES(location),"
				+ "storageTime=VALUES(storageTime),maintainCycle=VALUES(maintainCycle),producedDate=VALUES(producedDate),"
				+ "execDate=VALUES(execDate),remark=VALUES(remark),"
				+ "contractId=VALUES(contractId),chStatus=VALUES(chStatus),"
				+ "productCode=VALUES(productCode),PMNM=VALUES(PMNM),"
				+ "borrowLength=VALUES(borrowLength),insertTime=VALUES(insertTime),"
				+ "checkPerson=VALUES(checkPerson)";
		try {
			conn = DBConnection.getConn();
			// 2015.06.12 add by liuyutian
			conn.setAutoCommit(false);
			// end
			pstmt = conn.prepareStatement(sql);

			Iterator<OutApply> iter = list.iterator();
			OutApply apply = null;
			while (iter.hasNext()) {
				apply = iter.next();

				pstmt.setString(1, apply.getOutMeans());
				pstmt.setString(2, apply.getProductType());
				pstmt.setString(3, apply.getOldType());
				pstmt.setString(4, apply.getWholeName());
				pstmt.setString(5, apply.getUnitName());
				pstmt.setString(6, apply.getBatch());
				pstmt.setString(7, apply.getDeviceNo());
				pstmt.setString(8, apply.getUnit());
				pstmt.setDouble(9, apply.getNewPrice());
				pstmt.setDouble(10, apply.getOldPrice());
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
				pstmt.setString(26, apply.getBorrowLengthString());

				pstmt.setTimestamp(27, MyDateFormat.changeToSqlDate(insertTime));
				pstmt.setString(28, apply.getOwnedUnit());
				pstmt.setString(29, apply.getCheckPerson());
				pstmt.setLong(30, apply.getOutId());
				pstmt.addBatch();
			}
			returnArray = pstmt.executeBatch();
			// 2015.06.02 add by liuyutian
			conn.commit();
			if (returnArray.length == list.size()) {
				flag = true;
			}
			// end
			pstmt.clearBatch();
		} catch (SQLException ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * 根据入库申请id查找入库申请
	 * 
	 * @param inId
	 * @return
	 */
	public OutApply getOutApply(long inId) {
		String sql = "Select * from qy_outapply where inId = ?";
		ResultSet rs = null;
		OutApply apply = new OutApply();
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, inId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				apply.setOutId(rs.getLong("outId"));
				apply.setContractId(rs.getString("contractId"));
				apply.setOutMeans(rs.getString("outMeans"));
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
				apply.setBorrowLengthString(rs.getString("borrowLength"));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return apply;
	}

	/**
	 * 查询出库申请,支持组合查询，其中unitName和wholeName是模糊查询
	 * 
	 * @param contractId合同编号
	 * @param ProductType型号
	 * @param unitName
	 *            单元
	 * @param wholeName
	 *            产品名称（整机名称）
	 * @param signDate
	 *            签订日期
	 * @return Qy_OutApply 的对象数组
	 * @author LiangYiHuai
	 */
	public List<OutApply> selectOutApply(String contractId, String productType,
			String unitName, String wholeName, java.sql.Date signDate) {
		ResultSet rs = null;
		List<OutApply> list = new ArrayList<OutApply>();

		// 标记所传入的形参是否为空
		boolean contractIdFlag = false;
		boolean productTypeFlag = false;
		boolean unitNameFlag = false;
		boolean wholeNameFlag = false;
		boolean signDateFlag = false;

		int i = 1;

		StringBuffer buf = new StringBuffer();
		buf.append("Select * From qy_outapply ");

		if (contractId != null && !"".equals(contractId)) {
			buf.append("and contractId=? ");
			contractIdFlag = true;
		}
		if (productType != null && !"".equals(productType)) {
			buf.append("and productType=? ");
			productTypeFlag = true;
		}
		if (unitName != null && !"".equals(unitName)) {
			buf.append("and unitName like ? ");
			unitNameFlag = true;
		}
		if (wholeName != null && !"".equals(wholeName)) {
			buf.append("and wholeName like ? ");
			wholeNameFlag = true;
		}
		if (signDate != null) {
			buf.append("and contractId in (Select contractId From qy_contract Where signDate=?) ");
			signDateFlag = true;
		}
		buf.append("Order By outId ASC");

		String sql = buf.toString().replaceFirst("and", "where");
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			if (contractIdFlag) {
				pstmt.setString(i, contractId);
				i++;
			}
			if (productTypeFlag) {
				pstmt.setString(i, productType);
				i++;
			}
			if (unitNameFlag) {
				pstmt.setString(i, "%" + unitName + "%");
				i++;
			}
			if (wholeNameFlag) {
				pstmt.setString(i, "%" + wholeName + "%");
				i++;
			}
			if (signDateFlag) {
				pstmt.setDate(i, signDate);
				i++;
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				OutApply apply = new OutApply();

				apply.setOutId(rs.getLong("outId"));
				apply.setContractId(rs.getString("contractId"));
				apply.setOutMeans(rs.getString("outMeans"));
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
				apply.setBorrowLengthString(rs.getString("borrowLength"));

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
	 * 1、改变出库申请表的状态(主要是在更新出库中用到这个方法)
	 * 2、如果oldPrice减newPrice大于qy_parameter_configuration中的price——difference的值，
	 * 在outapply表的remark后追加：“差价大于系统值” 3、如果outApply中的 borrowlength >= 90
	 * 在outapply表的remark后追加：“出库时间大于三个月” 
	 * 
	 * @param dyadicArray
	 *            第一行是标题行
	 * @return
	 * @author LiangYH
	 */
//	public boolean changeChStatusAndRemark(List<ArrayList<String>> dyadicArray,String checkPerson) {
//		boolean flag = false;
//
//		Connection conn1 = null;
//
//		PreparedStatement pstmt3 = null;
//		PreparedStatement pstmt4 = null;
//		PreparedStatement pstmt1 = null;
//		PreparedStatement pstmt2 = null;
//
//		ResultSet rs = null;
//		ResultSet rs2 = null;
//		/**
//		 * 判断inId和chStatus和remark所在列的索引
//		 */
//		ArrayList<String> firstRow = dyadicArray.get(0);
//		int firstRowLen = firstRow.size();
//		int outIdIndex = -1;
//		int chStatusIndex = -1;
//		int ownedUnitIndex = -1;
//		for (int i = 0; i < firstRowLen; i++) {
//			if ("outId".equals(firstRow.get(i))) {
//				outIdIndex = i;
//			} else if ("chStatus".equals(firstRow.get(i))) {
//				chStatusIndex = i;
//			}else if("ownedUnit".equals(firstRow.get(i))){
//				ownedUnitIndex = i;
//			}
//
//		}
//		if (outIdIndex == -1 | chStatusIndex == -1||ownedUnitIndex == -1) {
//			return false;
//		}
//		String querySql = "Select newPrice,oldPrice,borrowLength,remark  "
//				+ "From qy_outapply Where outID=? And ownedUnit=?";
//		String querySql2 = "Select price_difference From qy_parameter_configuration";
//		// 改变出库申请表中的审核状态和备注
//		String sqlInApply = "Update qy_outapply set chStatus = ? , remark = ?,"
//				+ "checkPerson=? Where outId= ? And ownedUnit=?";
//
//		try {
//			conn1 = DBConnection.getConn();
//			// 设置非自动提交
//			conn1.setAutoCommit(false);
//
//			pstmt3 = conn1.prepareStatement(querySql);
//			pstmt4 = conn1.prepareStatement(querySql2);
//			pstmt1 = conn1.prepareStatement(sqlInApply);
//
//			int outId = 0;
//			String chStatus = null;
//			String proStatus = null;
//			String ownedUnit = null;
//			int len = dyadicArray.size();
//			// 注意，这里应该从1开始，因为第一行是标题
//			for (int i = 1; i < len; i++) {
//				outId = Integer.parseInt(dyadicArray.get(i).get(outIdIndex));
//				chStatus = dyadicArray.get(i).get(chStatusIndex);
//				ownedUnit = dyadicArray.get(i).get(ownedUnitIndex);
//				
//				pstmt3.setInt(1, outId);
//				pstmt3.setString(2, ownedUnit);
//
//				// 查询
//				rs = pstmt3.executeQuery();
//				rs2 = pstmt4.executeQuery();
//
//				double newPrice = 0.0;
//				double oldPrice = 0.0;
//				int borrowLength = -1;
//				String remark = "";
//				if (rs.next()) {
//					newPrice = rs.getDouble("newPrice");
//					oldPrice = rs.getDouble("oldPrice");
//					borrowLength = rs.getInt("borrowLength");
//					remark = rs.getString("remark");
//				}
//				int price_different = -1;
//				if (rs2.next()) {
//					price_different = rs2.getInt("price_difference");
//				}
//
//				if (Math.abs(oldPrice - newPrice) > price_different) {
//					remark = remark + ";差价大于系统值";
//				}
//				if (borrowLength > 90) {
//					remark = remark + ";出库时间大于三个月";
//				}
//
//				pstmt1.setString(1, chStatus);
//				pstmt1.setString(2, remark);
//				pstmt1.setString(3, checkPerson);
//				pstmt1.setInt(4, outId);
//				pstmt1.setString(5, ownedUnit);
//
//				pstmt1.execute();
//				pstmt1.addBatch();
//
//				// 如果状态中没有“未”“不”字，并且有“通过”两个字，那就表示审核通过
//				if (chStatus.indexOf("未") == -1 && chStatus.indexOf("不") == -1
//						&& chStatus.indexOf("通过") != -1) {
//					proStatus = "已出库";
//
//					// 一个入库申请可能对应多个产品id，所以这里的proStatus后面用in，而不是等号
//					String sqlProduct = "Update qy_product set proStatus = ?"
//							+ " where productId in ("
//							+ "Select productId From qy_outproductrelation where outId = ? And ownedUnit=?)";
//
//					pstmt2 = conn1.prepareStatement(sqlProduct);
//
//					pstmt2.setString(1, proStatus);
//					pstmt2.setInt(2, outId);
//					pstmt2.setString(3, ownedUnit);
//				}
//				// 非空判断，防止空异常
//				if (pstmt2 != null) {
//					pstmt2.execute();
//					pstmt2.addBatch();
//				}
//			}
//
//			pstmt1.executeBatch();
//			if (pstmt2 != null)
//				pstmt2.executeBatch();
//
//			conn1.commit();
//
//			flag = true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			try {
//				conn1.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//		} finally {
//			DBConnection.close(conn1, pstmt1);
//			try {
//				if (pstmt2 != null)
//					pstmt2.close();
//				pstmt3.close();
//				pstmt4.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//		}
//		return flag;
//	}

	/**
	 * 得到最近一批插入的时间
	 * 
	 * @return
	 * @author LiangYH
	 */
	/*public List<OutApply> getOutApplyOfLatestInsertTime() {
		String sql = "Select * From qy_outapply Where execDate in ("
				+ "Select Max(execDate) From qy_outapply)";

		ResultSet rs = null;

		List<OutApply> list = new ArrayList<OutApply>();

		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				OutApply apply = new OutApply();

				apply.setOutId(rs.getLong("outId"));
				apply.setContractId(rs.getString("contractId"));
				apply.setOutMeans(rs.getString("outMeans"));
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
				apply.setBorrowLengthString(rs.getString("borrowLength"));

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
	 *            操作类型，只能取值为,,circleOut,,renewOut
	 * @param fromDate
	 *            开始的时间
	 * @param toDate
	 *            结束时的时间
	 * @param status
	 *            审核状态
	 * @return 数组中的第一个元素是hashMap，其键是count（只有一个entry）
	 *         其余的是hashMap，其键是apply和product，其中前者是outApply对象 后者是Product对象
	 */
	public List<HashMap<String, Object>> selectOutApply(String contractId,
			String productModel, String unitName, String operateType,
			String fromDate, String toDate, String status,String keeper,
			String manufacturer, int curPageNum, int pageSize) {
		Connection conn2 = null;

		ResultSet rs = null;
		ResultSet rs2 = null;

		PreparedStatement pstmt2 = null;
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		// 标记所传入的形参operateType是否为空
		boolean operateTypeFlag = false;
		StringBuffer buf = new StringBuffer();
		//add bylyt 加入代储企业查询
		buf.append(" Select distinct o.outId,o.outMeans,batch,num,execDate,chStatus,o.ownedUnit from qy_outapply as o,qy_product as p,"
				+ "qy_outproductrelation as r "
				+ "where o.outId = r.outId and r.productId = p.productId");
		StringBuffer sum = new StringBuffer();
		sum.append(" Select distinct count(*) from qy_outapply as o,qy_product as p,"
				+ "qy_outproductrelation as r "
				+ "where o.outId = r.outId and r.productId = p.productId");
		if (contractId != null && !"".equals(contractId)
				&& !"null".equals(contractId)) {
			buf.append(" and o.contractId regexp '" + contractId + "'");
			sum.append(" and o.contractId regexp '" + contractId + "'");
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
		if (operateType != null && !"".equals(operateType)
				&& !"allIn".equals(operateType) && !"null".equals(operateType)) {
			buf.append(" and o.outMeans = ? ");
			sum.append(" and o.outMeans = ? ");
			operateTypeFlag = true;
		}
		if (keeper != null && !"".equals(keeper) && !"null".equals(keeper)&& !"所有企业".equals(keeper)&&!"所有军代室".equals(keeper)) {
			buf.append(" and o.ownedUnit regexp '" + keeper + "'");
			sum.append(" and o.ownedUnit regexp '" + keeper + "'");
		}
		if (manufacturer != null && !"".equals(manufacturer)
				&& !"null".equals(manufacturer)) {
			buf.append(" and i.manufacturer regexp '" + manufacturer + "'");
			sum.append(" and i.manufacturer regexp '" + manufacturer + "'");
		}
		/**
		 * 使用时间段查询 如果开始时间和结束时间都不为空，则查询这个时间段内的信息
		 * 如果开始时间为非空，结束时间为空，则查询从“开始时间”到现在的信息
		 * 如果开始时间为空，结束时间为非空，则查询“最早的时间”到“结束时间”这个时间段内的信息
		 */
		if (fromDate != null && toDate != null && !"".equals(fromDate)
				&& !"".equals(toDate) && !"null".equals(fromDate)
				&& !"null".equals(toDate)) {
			buf.append(" and o.contractId in (Select contractId From qy_contract "
					+ "Where signDate BETWEEN DATE('"
					+ fromDate
					+ "') And DATE('" + toDate + "')) ");
			sum.append(" and o.contractId in (Select contractId From qy_contract "
					+ "Where signDate BETWEEN DATE('"
					+ fromDate
					+ "') And DATE('" + toDate + "')) ");
		} else if (fromDate != null && !"".equals(fromDate)
				&& !"null".equals(fromDate)) {
			buf.append(" and o.contractId in (Select contractId From qy_contract "
					+ "Where signDate BETWEEN DATE('"
					+ fromDate
					+ "') And CURRENT_DATE()) ");
			sum.append(" and o.contractId in (Select contractId From qy_contract "
					+ "Where signDate BETWEEN DATE('"
					+ fromDate
					+ "') And CURRENT_DATE()) ");
		} else if (toDate != null && !"".equals(toDate)
				&& !"null".equals(toDate)) {
			buf.append(" and o.contractId in (Select contractId From qy_contract "
					+ "Where  signDate <= DATE('" + toDate + "')) ");
			sum.append(" and o.contractId in (Select contractId From qy_contract "
					+ "Where  signDate <= DATE('" + toDate + "')) ");
		}

		if (status != null && !"".equals(status)) {
			buf.append(" and o.chStatus regexp '" + status + "'");
			sum.append(" and o.chStatus regexp '" + status + "'");
		}

		buf.append(" Order By o.outId DESC limit ?,?");
		sum.append(" Order By o.outId DESC");
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(buf.toString());

			conn2 = DBConnection.getConn();
			pstmt2 = conn2.prepareStatement(sum.toString());

			int i = 1;
			if (operateTypeFlag) {
				if ("circleOut".equals(operateType)) {
					pstmt.setString(i, "轮换出库");
					pstmt2.setString(i, "轮换出库");
					i++;
				} else if ("renewOut".equals(operateType)) {
					pstmt.setString(i, "更新出库");
					pstmt2.setString(i, "更新出库");
					i++;
				} else {
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
				OutApply apply = new OutApply();

				apply.setOutId(rs.getLong("outId"));
//				apply.setContractId(rs.getString("contractId"));
				apply.setOutMeans(rs.getString("outMeans"));
				apply.setOwnedUnit(rs.getString("ownedUnit"));
				/*apply.setProductType(rs.getString("ProductType"));
				apply.setOldType(rs.getString("oldType"));
				apply.setWholeName(rs.getString("wholeName"));
				apply.setUnitName(rs.getString("unitName"));*/
				apply.setBatch(rs.getString("batch"));
				//apply.setDeviceNo(rs.getString("deviceNo"));
				/*apply.setUnit(rs.getString("unit"));
				apply.setNewPrice(rs.getDouble("newPrice"));
				apply.setOldPrice(rs.getDouble("oldPrice"));*/
				apply.setNum(rs.getInt("num"));
				/*apply.setOldNum(rs.getInt("oldNum"));
				apply.setMeasure(rs.getString("measure"));
				apply.setManufacturer(rs.getString("manufacturer"));
				apply.setKeeper(rs.getString("keeper"));
				apply.setProductCode(rs.getString("productCode"));
				apply.setPMNM(rs.getString("PMNM"));
				apply.setLocation(rs.getString("location"));
				apply.setStorageTime(rs.getString("storageTime"));
				apply.setMaintainCycle(rs.getString("maintainCycle"));
				apply.setProducedDate(MyDateFormat.changeToDate(rs
						.getTimestamp("producedDate")));*/
				apply.setExecDate(MyDateFormat.changeToDate(rs
						.getTimestamp("execDate")));
//				apply.setRemark(rs.getString("remark"));
				apply.setChStatus(rs.getString("chStatus"));
//				apply.setBorrowLengthString(rs.getString("borrowLength"));

				Product pro = new Product();
//				pro.setProductModel(rs.getString("productModel"));

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
	 * 企业版的轮换出库申请的操作
	 * <p>
	 * 其中需要更新产品表的status字段，具体为在原来数据基础上面append上去的，并逗号隔开。
	 * 所以在ｕｐｄａｔｅ之前需要查询产品表的这个字段。
	 *　企业更新出库申请、轮换发料单出库、更新发料单出库也会进行上面所述ｕｐｄａｔｅ动作。
	 * </p>
	 * @return runStatus
	 * @author LiangYH
	 */
	public boolean borrowOutAwareOperate(List<HashMap<String, String>> mapList,HashMap<String,String> applyMap){
		boolean runStatus = true;
		
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement update_outApply_ps = null;
		ResultSet rs = null;
		ResultSet product_rs = null;
		
		//增加出库申请表记录
		//0901 增加申请表状态
		String outApply_insert_sql = "Insert Into qy_outapply"
				+ "(outMeans,batch,execDate,ownedUnit,borrowLength,borrowReason,chStatus,num)values(?,?,NOW(),?,?,?,'轮换出库待审核',?)";
		//查询刚刚插入记录的键outId
		String outId_select_sql = "SELECT @@IDENTITY AS 'outId'";
		//查询产品表的相关字段
		String product_select_sql = "Select status, latestMaintainTime,productId,maintainCycle From qy_product Where deviceNo=? AND productModel = ?";
		//更新产品表的状态
		String product_update_sql = "Update qy_product "
				+ "Set restMaintainTime=?,borrowLength=?,borrowReason=?,remark=?,ownedUnit=?,proStatus=?,flag=?,status=?"
				+ " where productId = ?";
		//增加产品关系表记录
		String outProductRelation_insert_sql = "Insert Into qy_outproductrelation "
									+ "(outId,ownedUnit,deviceNo,productId,insertTime)values(?,?,?,?,?) "
									+ "ON DUPLICATE KEY UPDATE "
						+ "outId=VALUES(outId),ownedUnit=VALUES(ownedUnit),productId=VALUES(productId),insertTime=VALUES(insertTime)";
		
		// 改变出库申请表中的备注
		String update_outApply_sql = "Update qy_outapply set remark = ? Where outId= ? And ownedUnit=?";
		
		int outId = -1;
		try {
			conn = DBConnection.getConn();
			//取消自动提交
			conn.setAutoCommit(false);
			
			ps1 = conn.prepareStatement(outApply_insert_sql);
			ps2 = conn.prepareStatement(outId_select_sql);
			ps3 = conn.prepareStatement(product_update_sql);
			ps4 = conn.prepareStatement(outProductRelation_insert_sql);
			ps5 = conn.prepareStatement(product_select_sql);
			update_outApply_ps = conn.prepareStatement(update_outApply_sql);
			
			//这里多个产品对应一条申请
			HashMap<String,String> map = null;
			map = applyMap;
			
			ps1.setString(1,map.get("outMeans"));
			ps1.setString(2, map.get("batch"));
//			ps1.setString(3, map.get("deviceNo"));
			ps1.setString(3, map.get("ownedUnit"));
			ps1.setInt(4, Integer.parseInt(map.get("borrowLength")));
			ps1.setString(5, map.get("borrowReason"));
			ps1.setInt(6, mapList.size());
			
			ps1.execute();
			//查询最后插入的条目的outId
			rs = ps2.executeQuery();
			
			if(rs.next()){
				outId = rs.getInt("outId");
			}
			
			int len = mapList.size();
			
			for(int i = 0; i < len; i++){
				map = mapList.get(i);
				
				//查询product表,为下面的操作准备数据
				ps5.setString(1, map.get("deviceNo"));
				ps5.setString(2, map.get("productModel"));
				product_rs = ps5.executeQuery();
				
				java.util.Date latestMaintainTime = null;
				String productId = "";
				String maintainCycle = "";
//				String otherProductId = "";
				String status = "";
				if(product_rs.next()){
					latestMaintainTime = product_rs.getDate("latestMaintainTime");
					productId = product_rs.getString("productId");
					maintainCycle = product_rs.getString("maintainCycle");
					status = product_rs.getString("status");
				}
				
				//debugging
				if(latestMaintainTime == null || productId.length() == 0){
					System.err.println("error:latestMaintainTime == null || productId.length() == 0");
				}
				
				//更新product表
				//changed by lyt 1107
				/*if(status != null)
					status = status+","+StringUtil.BORROW_OUT;
				else 
					status = StringUtil.BORROW_OUT;*/
				status = changeStatus(StringUtil.BORROW_OUT, status);
				int restMaintainTime = RestTime.countRestMaintainTimeInDays(latestMaintainTime, maintainCycle);
				ps3.setInt(1, restMaintainTime);
				ps3.setString(2, applyMap.get("borrowLength"));
				ps3.setString(3, applyMap.get("borrowReason"));
				ps3.setString(4, applyMap.get("remark"));
				ps3.setString(5, applyMap.get("ownedUnit"));
				ps3.setString(6, StringUtil.BORROW_OUT+"待审核");
				ps3.setInt(7, 1);
				ps3.setString(8, status);
				ps3.setString(9, productId);
				ps3.executeUpdate();
//				ps3.addBatch();
				
				//插入申请-产品关系表
				ps4.setInt(1,outId);
				ps4.setString(2, applyMap.get("ownedUnit"));
				ps4.setString(3, map.get("deviceNo"));
				ps4.setString(4, productId);
				
				ps4.setString(5, MyDateFormat.changeDateToLongString(new java.util.Date()));
				
				ps4.execute();
				
				//update申请表中的备注（remark）
				int borrowLength = Integer.parseInt(applyMap.get("borrowLength"));
				String remark = "";
				if(borrowLength <= 3*30){
					remark = "军代室可审核";
				}else if(borrowLength <= 6*30){
					remark ="军代局可审核";
				}else{
					remark = "指挥局可审核";
				}
				update_outApply_ps.setString(1, remark);
				update_outApply_ps.setInt(2, outId);
				update_outApply_ps.setString(3, applyMap.get("ownedUnit"));
				
				update_outApply_ps.execute();
			}//end of for
			
			//提交
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				//回滚
				conn.rollback();
				runStatus = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			DBConnection.close(conn, ps1, rs);
			DBConnection.close(null, ps2, product_rs);
			DBConnection.close(ps3,ps4,ps5,update_outApply_ps);
			
		}
		return runStatus;
	}
	
	/**
	 * 企业版更新出库的
	 *  <p>
	 * 其中需要更新产品表的status字段，具体为在原来数据基础上面append上去的，并用逗号隔开。
	 * 所以在update之前需要查询产品表的这个字段。
	 *　企业轮换出库申请、轮换发料单出库、更新发料单出库也会进行上面所述update动作。
	 * </p>
	 * @param mapList
	 * @return run status
	 */
	public boolean updateOutAwareOperate(List<HashMap<String,String>> mapList, 
								Map<String,String>applyMap){
		boolean runStatus = true;
		
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement query_config_ps = null;
		PreparedStatement update_outApply_ps = null;
		ResultSet rs = null;
		ResultSet product_rs = null;
		
		//增加入库申请表记录
		String inApply_insert_sql = "Insert Into qy_outapply"
				+ "(outMeans,batch,execDate,ownedUnit,chStatus,oldNum,num,newPrice,oldPrice)"
				+ "values(?,?,NOW(),?,'更新出库待审核',?,?,?,?)";
		//查询刚刚插入记录的键outId
		String inId_select_sql = "SELECT @@IDENTITY AS 'outId'";
		//查询product表
		String product_select_sql = "Select status,latestMaintainTime,productId, maintainCycle "
				+ "From qy_product Where deviceNo=? And productModel=?";
		//更新产品表的状态
		String product_update_sql = "Update qy_product "
				+ "Set restMaintainTime=?,remark=?,proStatus=?,flag=?,status=? "
				+ "where productId = ?";
		//增加产品关系表记录
		String inProductRelation_insert_sql = "Insert Into qy_outproductrelation "
					+ "(outId,ownedUnit,productId,insertTime,deviceNo)"
					+ "values(?,?,?,?,?)ON DUPLICATE KEY UPDATE "
					+ "outId=VALUES(outId),ownedUnit=VALUES(ownedUnit),"
					+ "productId=VALUES(productId),insertTime=VALUES(insertTime)";
		// 改变出库申请表中的备注
		String update_outApply_sql = "Update qy_outapply set remark = ? Where outId= ? And ownedUnit=?";
		
		
		try {
			conn = DBConnection.getConn();
			//取消自动提交
			conn.setAutoCommit(false);
			
			ps1 = conn.prepareStatement(inApply_insert_sql);
			ps2 = conn.prepareStatement(inId_select_sql);
			ps3 = conn.prepareStatement(product_update_sql);
			ps4 = conn.prepareStatement(inProductRelation_insert_sql);
			ps5 = conn.prepareStatement(product_select_sql);
			update_outApply_ps = conn.prepareStatement(update_outApply_sql);
			
			HashMap<String,String> map = null;
			map = mapList.get(0);
			
			ps1.setString(1,applyMap.get("outMeans"));
			ps1.setString(2, applyMap.get("batch"));
			ps1.setString(3, applyMap.get("ownedUnit"));
			ps1.setInt(4, mapList.size());//原数量
			ps1.setString(5, applyMap.get("num"));//新数量（用户填的）
			ps1.setString(6, applyMap.get("price"));
			ps1.setString(7, applyMap.get("oldPrice"));
			ps1.execute();
			
			//查询最后插入的条目的outId
			rs = ps2.executeQuery();
			int outId = -1;
			if(rs.next()) outId = rs.getInt("outId");

			int len = mapList.size();
			for(int i = 0; i < len; i++){
				map = mapList.get(i);
				
				//查询product表,为下面的操作准备数据
				ps5.setString(1, map.get("deviceNo"));
				ps5.setString(2, map.get("productModel"));
				product_rs = ps5.executeQuery();
				
				java.util.Date latestMaintainTime = null;
				String productId = "";
				String maintainCycle = "";
				String status = "";
				if(product_rs.next()){
					latestMaintainTime = product_rs.getDate("latestMaintainTime");
					productId = product_rs.getString("productId");
					maintainCycle = product_rs.getString("maintainCycle");
					status = product_rs.getString("status");
				}
				
				//更新product表
				//changed by lyt 1107
				/*if(status != null)
					status = status+","+StringUtil.UPDATE_OUT;
				else 
					status = StringUtil.UPDATE_OUT;*/
				status = changeStatus(StringUtil.UPDATE_OUT, status);
				int restMaintainTime = RestTime.countRestMaintainTimeInDays(latestMaintainTime, maintainCycle);
				ps3.setInt(1, restMaintainTime);
				ps3.setString(2, applyMap.get("remark"));
				ps3.setString(3, StringUtil.UPDATE_OUT+"待审核");
				ps3.setInt(4, 1);
				ps3.setString(5, status);
				ps3.setString(6, productId);
				ps3.execute();
				
				//插入申请-产品关系表
				ps4.setInt(1, outId);
				ps4.setString(2, applyMap.get("ownedUnit"));
				ps4.setString(3, productId);
				ps4.setString(4, MyDateFormat.changeDateToLongString(new java.util.Date()));
				ps4.setString(5, map.get("deviceNo"));
				ps4.execute();
				
				//update申请表的remark
				double newPrice = Double.parseDouble(applyMap.get("price"));
				double oldPrice = Double.parseDouble(applyMap.get("oldPrice"));
				int oldNum = mapList.size();
				int newNum = Integer.parseInt(applyMap.get("num"));

				//更新出库申请表
				double price_dif = Math.abs(oldPrice*oldNum - newPrice*newNum);
				update_outApply_ps.setString(1, String.valueOf(price_dif));
				update_outApply_ps.setInt(2, outId);
				update_outApply_ps.setString(3, applyMap.get("ownedUnit"));
				update_outApply_ps.execute();
				
			}//end for circle
			
			//提交
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				//回滚
				conn.rollback();
				runStatus = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			DBConnection.close(conn, ps1, rs);
			DBConnection.close(null, ps2, product_rs);
			DBConnection.close(ps3,ps4,ps5,query_config_ps,update_outApply_ps);
		}
		return runStatus;
	}
	
	/**
	 * 根据outId在outapply、product和relation表中查询
	 * 
	 * key:applys\relations\products
	 */
//	public Map<String,Object> queryMultiFormByOutID(List<Long> outIDs,String ownedUnit){
//		Map<String,Object> map = new HashMap<String,Object>();
//		PreparedStatement ps1 = null;
//		PreparedStatement ps2 = null;
//		PreparedStatement ps3 = null;
//		ResultSet rs1 = null;
//		ResultSet rs2 = null;
//		ResultSet rs3 = null;
//		Connection conn = null;
//		
//		String sql_outApply = "Select * From qy_outapply Where outId = ? And ownedUnit= ?";
//		String sql_inproductrelation = "Select * From qy_outproductrelation Where outId = ? And ownedUnit=?";
//		String sql_product = "Select * From qy_product Where productId IN("
//				+ "Select productId From qy_outproductrelation Where outId=? And ownedUnit=?)";
//		
//		List<ArrayList<String>> outApplys = new ArrayList<ArrayList<String>>();
//		List<ArrayList<String>> relations = new ArrayList<ArrayList<String>>();
//		List<ArrayList<String>> products = new ArrayList<ArrayList<String>>();
//		
//		ArrayList<String> inApply_headline = new ArrayList<String>();
//		ArrayList<String> relation_headline = new ArrayList<String>();
//		ArrayList<String> product_headline = new ArrayList<String>();
//		//填充标题
//		outApplys.add(inApply_headline);
//		relations.add(relation_headline);
//		products.add(product_headline);
//		
//		try {
//			conn = DBConnection.getConn();
//			ps1 = conn.prepareStatement(sql_outApply);
//			ps2 = conn.prepareStatement(sql_inproductrelation);
//			ps3 = conn.prepareStatement(sql_product);
//			
//			int len = outIDs.size();
//			for(int i = 0; i < len; i++){
//				long inID = outIDs.get(i);
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
//				ArrayList<String> outApply = new ArrayList<String>();
//				ArrayList<String> relation = new ArrayList<String>();
//				ArrayList<String> product = new ArrayList<String>();
//				
//				while(rs2.next() && rs1.next() && rs3.next()){
//			        
//					//得到结果集(rs)的结构信息，比如字段数、字段名等 
//					ResultSetMetaData md = rs1.getMetaData();  
//					int columnCount = md.getColumnCount(); 	
//			        for(int k = 1; k <= columnCount; k++){
//			        	outApply.add(rs1.getString(k));
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
//					outApplys.add(outApply);
//					relations.add(relation);
//					products.add(product);
//				}//end while
//			}//end for
//			
//			map.put("applys", outApplys);
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
	
	/**
	 * 
	 * @param outApply 发料单出库方式
	 * @param status 产品表中的status字段的数据
	 * @return
	 */
	private String changeStatus(String outApplyType,String status){
		String tempType = "";
		if(status != null){ 
			if(status.endsWith(outApplyType)){
				tempType = status;
			}else{
				tempType = status+","+outApplyType;
			}
		} else {
			tempType = outApplyType;
		}
		return tempType;
	}
}