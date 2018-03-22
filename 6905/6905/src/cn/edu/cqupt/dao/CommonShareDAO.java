package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.StringUtil;

/**
 * 从不同的dao类中提取出来的相同的部分，组成这个类
 * 目的是减少重复代码，同时便于修改维护
 * @author LiangYH
 *
 */
public class CommonShareDAO {
	
	/**
	 * 三表同时存入数据库,如果所插入实体的键在数据库表中已经存在，则update
	 * 参数第一行应该对应数据库的标题行
	 * @param firstDyadic apply表
	 * @param relationDyadic relation表
	 * @param productDyadic product表
	 * @param tableNames 分别对三个数据库表的名字 
	 * @param isThirdNotNull 第三个表是否不为null
	 * @return run Status
	 * @author LiangYH
	 */
	public boolean insertThreeTables(List<ArrayList<String>> firstDyadic, 
							List<ArrayList<String>> secondDyadic, 
							List<ArrayList<String>> thirdDyadic, 
							List<String> tableNames){
		boolean isSecondNotNull = false;
		boolean isThirdNotNull = false;
		if(secondDyadic != null && secondDyadic.size() > 0){
			isSecondNotNull = true;
		}
		if(thirdDyadic != null && thirdDyadic.size() > 0){
			isThirdNotNull = true;
		}
		
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		
		//生成sql语句
		String outListSQL = StringUtil.combineSQLString(tableNames.get(0), firstDyadic.get(0));
		String relationSQL = null;
		if(isSecondNotNull){
			relationSQL = StringUtil.combineSQLString(tableNames.get(1), secondDyadic.get(0));
		}
		String productSQL = null;
		if(isThirdNotNull){
			productSQL = StringUtil.combineSQLString(tableNames.get(2), thirdDyadic.get(0));
		}
		
		int applycolumnLen = firstDyadic.get(0).size();
		int relationcolumnLen = -1;
		if(isSecondNotNull){
			relationcolumnLen = secondDyadic.get(0).size();
		}
		int productcolumnLen = -1;
		if(isThirdNotNull){
			productcolumnLen = thirdDyadic.get(0).size();
		}
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(outListSQL);
			if(isSecondNotNull){
				ps2 = conn.prepareStatement(relationSQL);
			}
			if(isThirdNotNull){
				ps3 = conn.prepareStatement(productSQL);
			}
			
			int applylen = firstDyadic.size();
			for(int i = 1; i < applylen; i++){
				ArrayList<String> dataList = firstDyadic.get(i);
				int index = 1;
				for(int k = 0; k < applycolumnLen; k++){
					String tempStr = dataList.get(k);
					if("".equals(tempStr)){
						tempStr = null;
					}
					ps.setString(index++, tempStr);
				}
				ps.addBatch();
			}
			if(isSecondNotNull){
				int relationLen = secondDyadic.size();
				for(int i = 1; i < relationLen; i++){
					ArrayList<String> dateList = secondDyadic.get(i);
					int index = 1;
					for(int k = 0; k < relationcolumnLen; k++){
						String tempStr = dateList.get(k);
						if("".equals(tempStr)){
							tempStr = null;
						}
						ps2.setString(index++, tempStr);
					}
					ps2.addBatch();
				}
			}
			
			if(isThirdNotNull){
				int productLen = thirdDyadic.size();
				for(int i = 1; i < productLen; i++){
					ArrayList<String> dateList = thirdDyadic.get(i);
					int index = 1;
					for(int k = 0; k < productcolumnLen; k++){
						String tempStr = dateList.get(k);
						if("".equals(tempStr)){
							tempStr = null;
						}
						ps3.setString(index++, tempStr); 
					}
					ps3.addBatch();
				}
			}
			
			ps.executeBatch();
			if(isSecondNotNull){
				ps2.executeBatch();
			}
			if(isThirdNotNull)
				ps3.executeBatch();
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
			try {
				if(ps2 != null)ps2.close();
				if(ps3 != null)ps3.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 根据Id在apply、product和relation表中查询
	 * 如果是inApply表，那么查询的是qy_inApply、qy_inProductRelation、qy_product三个表
	 * 如果是outApply表，那么查询的是qy_outApply、qy_outProductRelation、qy_product三个表
	 * 
	 * 其中，返回结果是一个Map<String,Object>,键key:1/2/3，顺序对应申请表、关系表、产品表
	 * @param inIDs
	 * @param ownedUnit
	 * @param tableName
	 * @param isInApply inApply或者outApplys
	 * @return
	 * @author liangyihuai
	 */
	public Map<String,Object> queryMultiForm(List<Long> inIDs,String ownedUnit, List<String> tableName, boolean isInApply){
		Map<String,Object> map = new HashMap<String,Object>();
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		Connection conn = null;
		
		String idString = "outId";
		if(isInApply) idString = "inId";
			
		String sql_inApply = "Select * From "+tableName.get(0)+" Where "+idString+" = ? And ownedUnit = ?";
		String sql_inproductrelation = "Select * From "+tableName.get(1)+" Where "+idString+" = ? And ownedUnit = ?";
		String sql_product = "Select * From "+tableName.get(2)+" Where productId  IN ("
				+ "Select productId From "+tableName.get(1)+" Where "+idString+"=? And ownedUnit=?) AND ownedUnit=?";
		//存储查询出来的数据库表
		List<ArrayList<String>> inApplys = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> relations = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> products = new ArrayList<ArrayList<String>>();
		//存储标题
		ArrayList<String> inApply_headline = new ArrayList<String>();
		ArrayList<String> relation_headline = new ArrayList<String>();
		ArrayList<String> product_headline = new ArrayList<String>();
		//填充标题
		inApplys.add(inApply_headline);
		relations.add(relation_headline);
		products.add(product_headline);
		
		try {
			conn = DBConnection.getConn();
			ps1 = conn.prepareStatement(sql_inApply);
			ps2 = conn.prepareStatement(sql_inproductrelation);
			ps3 = conn.prepareStatement(sql_product);
			
			int len = inIDs.size();
			for(int i = 0; i < len; i++){
				long inID = inIDs.get(i);
				
				ps1.setLong(1, inID);
				ps1.setString(2, ownedUnit);
				rs1 =  ps1.executeQuery();
				while(rs1.next()){
					ArrayList<String> inApply = new ArrayList<String>();
					//得到结果集(rs)的结构信息，比如字段数、字段名等 
					ResultSetMetaData md = rs1.getMetaData();  
					int columnCount = md.getColumnCount(); 	
			        for(int k = 1; k <= columnCount; k++){
			        	inApply.add(rs1.getString(k));
			        	//增加标题
			        	if(inApply_headline.size() != columnCount)
			        		inApply_headline.add(md.getColumnName(k));
			        }
			        inApplys.add(inApply);
				}
				
				ps2.setLong(1, inID);
				ps2.setString(2, ownedUnit);
				rs2 = ps2.executeQuery();
				while(rs2.next()){
					ArrayList<String> relation = new ArrayList<String>();
					//得到结果集(rs)的结构信息，比如字段数、字段名等 
					ResultSetMetaData md2 = rs2.getMetaData();  
					int columnCount2 = md2.getColumnCount(); 	
			        for(int k = 1; k <= columnCount2; k++){
			        	relation.add(rs2.getString(k));
			        	//增加标题
			        	if(relation_headline.size() != columnCount2)
			        		relation_headline.add(md2.getColumnName(k));
			        }
			        relations.add(relation);
				}
				
				ps3.setLong(1, inID);
				ps3.setString(2, ownedUnit);
				ps3.setString(3, ownedUnit);
				rs3 = ps3.executeQuery();
				while(rs3.next()){
					ArrayList<String> product = new ArrayList<String>();
					//得到结果集(rs)的结构信息，比如字段数、字段名等 
					ResultSetMetaData md3 = rs3.getMetaData();  
					int columnCount3 = md3.getColumnCount(); 	
			        for(int k = 1; k <= columnCount3; k++){
			        	product.add(rs3.getString(k));
			        	//增加标题
			        	if(product_headline.size() != columnCount3)
			        		product_headline.add(md3.getColumnName(k));
			        }
			        products.add(product);
				}
				
//				while(rs2.next() && rs1.next() && rs3.next()){
//					ArrayList<String> inApply = new ArrayList<String>();
//					ArrayList<String> relation = new ArrayList<String>();
//					ArrayList<String> product = new ArrayList<String>();
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
			}//end for
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			map.put("1", inApplys);
			map.put("2", relations);
			map.put("3", products);
			
			try {
				ps1.close();
				ps2.close();
				ps3.close();
				rs1.close();
				rs2.close();
				rs3.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
//	public Map<String,Object> queryMultiForm(List<String> sqlList){
//		Map<String,Object> map = new HashMap<String,Object>();
//		PreparedStatement ps1 = null;
//		PreparedStatement ps2 = null;
//		PreparedStatement ps3 = null;
//		ResultSet rs1 = null;
//		ResultSet rs2 = null;
//		ResultSet rs3 = null;
//		Connection conn = null;
//
//		String sqlone = sqlList.get(0);
//		String sqltwo = sqlList.get(1);
//		String sqlthree = sqlList.get(2);
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
//			ps1 = conn.prepareStatement(sqlone);
//			ps2 = conn.prepareStatement(sqltwo);
//			ps3 = conn.prepareStatement(sqlthree);
//			
//			rs1 =  ps1.executeQuery();
//			rs2 = ps2.executeQuery();
//			rs3 = ps3.executeQuery();
//			
//			while(rs2.next() && rs1.next() && rs3.next()){
//				ArrayList<String> inApply = new ArrayList<String>();
//				ArrayList<String> relation = new ArrayList<String>();
//				ArrayList<String> product = new ArrayList<String>();
//		        
//				//得到结果集(rs)的结构信息，比如字段数、字段名等 
//				ResultSetMetaData md = rs1.getMetaData();  
//				int columnCount = md.getColumnCount(); 	
//		        for(int k = 1; k <= columnCount; k++){
//		        	inApply.add(rs1.getString(k));
//		        	//增加标题
//		        	if(inApply_headline.size() != columnCount)
//		        		inApply_headline.add(md.getColumnName(k));
//		        }
//		        
//		        //得到结果集(rs)的结构信息，比如字段数、字段名等 
//				ResultSetMetaData md2 = rs2.getMetaData();  
//				int columnCount2 = md2.getColumnCount(); 	
//		        for(int k = 1; k <= columnCount2; k++){
//		        	relation.add(rs2.getString(k));
//		        	//增加标题
//		        	if(relation_headline.size() != columnCount2)
//		        		relation_headline.add(md2.getColumnName(k));
//		        }
//		        
//		       //得到结果集(rs)的结构信息，比如字段数、字段名等 
//				ResultSetMetaData md3 = rs3.getMetaData();  
//				int columnCount3 = md3.getColumnCount(); 	
//		        for(int k = 1; k <= columnCount3; k++){
//		        	product.add(rs3.getString(k));
//		        	//增加标题
//		        	if(product_headline.size() != columnCount3)
//		        		product_headline.add(md3.getColumnName(k));
//		        }
//				inApplys.add(inApply);
//				relations.add(relation);
//				products.add(product);
//			}//end while
//			
//			map.put("1", inApplys);
//			map.put("2", relations);
//			map.put("3", products);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			DBConnection.close(null, ps1, rs1);
//			DBConnection.close(null, ps2, rs2);
//			DBConnection.close(conn, ps3, rs3);
//		}
//		return map;
//	}
}
