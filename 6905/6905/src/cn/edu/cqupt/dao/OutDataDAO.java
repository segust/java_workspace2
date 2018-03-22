package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;

public class OutDataDAO {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    /**
     * 查询合同表
     * */
	public List<ArrayList<String>> selectContract(String version){
		 List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_contract";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> contract = new ArrayList<String>();
				contract.add(rs.getString("contractId"));
				contract.add(rs.getInt("totalNumber")+"");
				contract.add(rs.getDouble("contractPrice")+"");
				contract.add(rs.getString("JDS"));
				contract.add(rs.getDate("signDate")+"");
				contract.add(rs.getString("attachment"));
				contract.add(rs.getString("buyer"));
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					contract.add(rs.getString("ownedUnit"));
				}
				data.add(contract);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}
	
	/**
     * 查询产品表
     * */
	public List <ArrayList<String>>selectProduct(String version){
		 List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_product";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> product = new ArrayList<String>();
				product.add(rs.getString("contractId"));
				product.add(rs.getLong("productId")+"");
				product.add(rs.getString("productCode"));
				product.add(rs.getString("PMNM"));
				product.add(rs.getString("productName"));
				product.add(rs.getString("productType"));
				product.add(rs.getString("productModel"));
				product.add(rs.getString("productUnit"));
				product.add(rs.getString("measureUnit"));
				product.add(rs.getString("productPrice"));
				product.add(rs.getDate("deliveryTime")+"");
				product.add(rs.getDate("latestMaintainTime")+"");
				product.add(rs.getString("manufacturer"));
				product.add(rs.getString("keeper"));
				product.add(rs.getString("buyer"));
				product.add(rs.getDate("signTime")+"");
				product.add(rs.getString("proStatus"));
				product.add(rs.getInt("restKeepTime")+"");
				product.add(rs.getInt("restMaintainTime")+"");
			/*	product.add(rs.getDouble("weight")+"");
				product.add(rs.getDouble("volume")+"");*/
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					product.add(rs.getString("ownedUnit"));
				}
				data.add(product);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}
	
	/**
     * 查询流水账表
     * */
	public List<ArrayList<String>> selectAccount(String version){
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_account";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> list=new ArrayList<String>();
				list.add(rs.getInt("id")+"");
				list.add(rs.getInt("productId")+"");
				list.add(rs.getString("operateType"));
				list.add(rs.getDate("operateTime")+"");
				list.add(rs.getString("userName"));
				list.add(rs.getString("remark"));
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					list.add(rs.getString("ownedUnit"));
				}
				data.add(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}

	
	/**
     * 查询入库申请表
     * */
	public List<ArrayList<String>> selectInApply(String version){
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_inapply";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> list=new ArrayList<String>();
				list.add(rs.getInt("inId")+"");
				list.add(rs.getString("contractId"));
				list.add(rs.getString("inMeans"));
				list.add(rs.getString("ProductType"));
				list.add(rs.getString("oldType"));
				list.add(rs.getString("wholeName"));
				list.add(rs.getString("unitName"));
				list.add(rs.getString("batch"));
				list.add(rs.getString("deviceNo"));
				list.add(rs.getString("unit"));
				list.add(rs.getDouble("newPrice")+"");
				list.add(rs.getDouble("oldPrice")+"");
				list.add(rs.getInt("num")+"");
				list.add(rs.getInt("oldNum")+"");
				list.add(rs.getString("measure"));
				list.add(rs.getString("manufacturer"));
				list.add(rs.getString("keeper"));
				list.add(rs.getString("productCode"));
				list.add(rs.getString("PMNM"));
				list.add(rs.getString("location"));
				list.add(rs.getString("storageTime"));
				list.add(rs.getString("maintainCycle"));
				list.add(rs.getDate("producedDate")+"");
				list.add(rs.getDate("execDate")+"");
				list.add(rs.getString("remark"));
				list.add(rs.getString("chStatus"));
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					list.add(rs.getString("ownedUnit"));
				}
				data.add(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}

	/**
     * 查询出库申请表
     * */
	public List<ArrayList<String>> selectOutApply(String version){
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_outapply";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> list=new ArrayList<String>();
				list.add(rs.getInt("outId")+"");
				list.add(rs.getString("contractId"));
				list.add(rs.getString("outMeans"));
				list.add(rs.getString("ProductType"));
				list.add(rs.getString("oldType"));
				list.add(rs.getString("wholeName"));
				list.add(rs.getString("unitName"));
				list.add(rs.getString("batch"));
				list.add(rs.getString("deviceNo"));
				list.add(rs.getString("unit"));
				list.add(rs.getDouble("newPrice")+"");
				list.add(rs.getDouble("oldPrice")+"");
				list.add(rs.getInt("num")+"");
				list.add(rs.getInt("oldNum")+"");
				list.add(rs.getString("measure"));
				list.add(rs.getString("manufacturer"));
				list.add(rs.getString("keeper"));
				list.add(rs.getString("productCode"));
				list.add(rs.getString("PMNM"));
				list.add(rs.getString("location"));
				list.add(rs.getString("storageTime"));
				list.add(rs.getString("maintainCycle"));
				list.add(rs.getDate("producedDate")+"");
				list.add(rs.getDate("execDate")+"");
				list.add(rs.getString("borrowLength"));
				list.add(rs.getString("remark"));
				list.add(rs.getString("chStatus"));
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					list.add(rs.getString("ownedUnit"));
				}
				data.add(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}

	/**
     * 查询器材明细账表
     * */
	public List<ArrayList<String>> selectEquipmentDetail(String version){
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_equipmentDetail";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> list=new ArrayList<String>();
				list.add(rs.getString("instoreYear"));
				list.add(rs.getString("productModel"));
				list.add(rs.getInt("packageNumber")+"");
				list.add(rs.getDouble("volume")+"");
				list.add(rs.getDouble("weight")+"");
				list.add(rs.getString("manufacturer"));
				list.add(rs.getString("productPrice"));
				list.add(rs.getString("packageDescription"));
				list.add(rs.getString("matchingInstruction"));
				list.add(rs.getString("PMNM"));
				list.add(rs.getString("year"));
				list.add(rs.getString("month"));
				list.add(rs.getString("day"));
				list.add(rs.getInt("listId")+"");
				list.add(rs.getString("synopsis"));
				list.add(rs.getInt("balanceQuantity")+"");
				list.add(rs.getDouble("income")+"");
				list.add(rs.getDouble("out")+"");
				list.add(rs.getDouble("balance")+"");
				list.add(rs.getString("remark"));
				list.add(rs.getString("id"));
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					list.add(rs.getString("ownedUnit"));
				}
				data.add(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}

	/**
     * 查询入库关系表
     * */
	public List<ArrayList<String>> selectInProductRelation(String version){
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_inproductrelation";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> list=new ArrayList<String>();
				list.add(rs.getInt("inId")+"");
				list.add(rs.getInt("productId")+"");
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					list.add(rs.getString("ownedUnit"));
				}
				data.add(list);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}
	
	/**
     * 查询出库关系表
     * */
	public List<ArrayList<String>> selectOutProductRelation(String version){
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_outproductrelation ";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> outproductrelation=new ArrayList<String>();
				outproductrelation.add(rs.getLong("outId")+"");
				outproductrelation.add(rs.getLong("productId")+"");
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					outproductrelation.add(rs.getString("ownedUnit"));
				}
				data.add(outproductrelation);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}
	
	/**
     * 查询日志表
     * */
	public List<ArrayList<String>> selectLog(String version){
		List<ArrayList<String>> data=new ArrayList<ArrayList<String>>();
		String sql="select * from qy_log where operateType regexp '维护'";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				ArrayList<String> log=new ArrayList<String>();
				log.add(rs.getLong("logId")+"");
				log.add(rs.getLong("productId")+"");
				log.add(rs.getString("operateType"));
				log.add(rs.getDate("operateTime")+"");
				log.add(rs.getString("userName"));
				log.add(rs.getString("maintainType"));
				log.add(rs.getString("inspectPerson"));
				log.add(rs.getString("remark"));
				if(version.equals("2")||version.equals("3")||version.equals("4")){
					log.add(rs.getString("ownedUnit"));
				}
				data.add(log);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return data;
	}
	
	/**
	 * 添加合同表
	 * */
	public boolean addContract(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_contract values(?,?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setString(1, list.get(i).get(0));
				pstmt.setInt(2, Integer.parseInt(list.get(i).get(1)));
				pstmt.setDouble(3, Double.parseDouble(list.get(i).get(2)));
				pstmt.setString(4, list.get(i).get(3));
				pstmt.setTimestamp(5, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(4))));
				pstmt.setString(6, list.get(i).get(5));
				pstmt.setString(7, list.get(i).get(6));
				pstmt.setString(8, list.get(i).get(7));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加产品表
	 * */
	public boolean addProduct(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_product values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setString(1, list.get(i).get(0));
				pstmt.setInt(2, Integer.parseInt(list.get(i).get(1)));
				pstmt.setString(3, list.get(i).get(2));
				pstmt.setString(4, list.get(i).get(3));
				pstmt.setString(5, list.get(i).get(4));
				pstmt.setString(6, list.get(i).get(5));
				pstmt.setString(7, list.get(i).get(6));
				pstmt.setString(8, list.get(i).get(7));
				pstmt.setString(9, list.get(i).get(8));
				pstmt.setString(10, list.get(i).get(9));
				pstmt.setTimestamp(11, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(10))));
				pstmt.setTimestamp(12, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(11))));
				pstmt.setString(13, list.get(i).get(12));
				pstmt.setString(14, list.get(i).get(13));
				pstmt.setString(15, list.get(i).get(14));
				pstmt.setTimestamp(16, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(15))));
				pstmt.setString(17, list.get(i).get(16));
				pstmt.setInt(18, Integer.parseInt(list.get(i).get(17)));
				pstmt.setInt(19, Integer.parseInt(list.get(i).get(18)));
				pstmt.setDouble(20, Double.parseDouble(list.get(i).get(19)));
				pstmt.setDouble(21, Double.parseDouble(list.get(i).get(20)));
				pstmt.setString(22, list.get(i).get(21));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加流水账表
	 * */
	public boolean addAccount(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_account values(?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setInt(1, Integer.parseInt(list.get(i).get(0)));
				pstmt.setInt(2, Integer.parseInt(list.get(i).get(1)));
				pstmt.setString(3, list.get(i).get(2));
				pstmt.setTimestamp(4, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(3))));
				pstmt.setString(5, list.get(i).get(4));
				pstmt.setString(6, list.get(i).get(5));
				pstmt.setString(7, list.get(i).get(6));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加入库申请表
	 * */
	public boolean addInApply(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_inapply values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setInt(1, Integer.parseInt(list.get(i).get(0)));
				pstmt.setString(2, list.get(i).get(1));
				pstmt.setString(3, list.get(i).get(2));
				pstmt.setString(4, list.get(i).get(3));
				pstmt.setString(5, list.get(i).get(4));
				pstmt.setString(6, list.get(i).get(5));
				pstmt.setString(7, list.get(i).get(6));
				pstmt.setString(8, list.get(i).get(7));
				pstmt.setString(9, list.get(i).get(8));
				pstmt.setString(10, list.get(i).get(9));
				pstmt.setDouble(11, Double.parseDouble(list.get(i).get(10)));
				pstmt.setDouble(12, Double.parseDouble(list.get(i).get(11)));
				pstmt.setInt(13, Integer.parseInt(list.get(i).get(12)));
				pstmt.setInt(14, Integer.parseInt(list.get(i).get(13)));
				pstmt.setString(15, list.get(i).get(14));
				pstmt.setString(16, list.get(i).get(15));
				pstmt.setString(17, list.get(i).get(16));
				pstmt.setString(18, list.get(i).get(17));
				pstmt.setString(19, list.get(i).get(18));
				pstmt.setString(20, list.get(i).get(19));
				pstmt.setString(21, list.get(i).get(20));
				pstmt.setString(22, list.get(i).get(21));
				pstmt.setTimestamp(23, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(22))));
				pstmt.setTimestamp(24, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(23))));
				pstmt.setString(25, list.get(i).get(24));
				pstmt.setString(26, list.get(i).get(25));
				pstmt.setTimestamp(27, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(26))));
				pstmt.setString(28, list.get(i).get(27));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加出库申请表
	 * */
	public boolean addOutApply(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_outapply values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setInt(1, Integer.parseInt(list.get(i).get(0)));
				pstmt.setString(2, list.get(i).get(1));
				pstmt.setString(3, list.get(i).get(2));
				pstmt.setString(4, list.get(i).get(3));
				pstmt.setString(5, list.get(i).get(4));
				pstmt.setString(6, list.get(i).get(5));
				pstmt.setString(7, list.get(i).get(6));
				pstmt.setString(8, list.get(i).get(7));
				pstmt.setString(9, list.get(i).get(8));
				pstmt.setString(10, list.get(i).get(9));
				pstmt.setDouble(11, Double.parseDouble(list.get(i).get(10)));
				pstmt.setDouble(12, Double.parseDouble(list.get(i).get(11)));
				pstmt.setInt(13, Integer.parseInt(list.get(i).get(12)));
				pstmt.setInt(14, Integer.parseInt(list.get(i).get(13)));
				pstmt.setString(15, list.get(i).get(14));
				pstmt.setString(16, list.get(i).get(15));
				pstmt.setString(17, list.get(i).get(16));
				pstmt.setString(18, list.get(i).get(17));
				pstmt.setString(19, list.get(i).get(18));
				pstmt.setString(20, list.get(i).get(19));
				pstmt.setString(21, list.get(i).get(20));
				pstmt.setString(22, list.get(i).get(21));
				pstmt.setTimestamp(23, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(22))));
				pstmt.setTimestamp(24, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(23))));
				pstmt.setInt(25, Integer.parseInt(list.get(i).get(24)));
				pstmt.setString(26, list.get(i).get(25));
				pstmt.setString(27, list.get(i).get(26));
				pstmt.setTimestamp(28, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(27))));
				pstmt.setString(29, list.get(i).get(28));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加器材明细账表
	 * */
	public boolean addEquipmentDetail(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_equipmentdetail values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
//				pstmt.setInt(1, i);
				pstmt.setString(1, list.get(i).get(0));
				pstmt.setString(2, list.get(i).get(1));
				pstmt.setInt(3, Integer.parseInt(list.get(i).get(2)));
				pstmt.setDouble(4, Double.parseDouble(list.get(i).get(3)));
				pstmt.setDouble(5, Double.parseDouble(list.get(i).get(4)));
				pstmt.setString(6, list.get(i).get(5));
				pstmt.setString(7, list.get(i).get(6));
				pstmt.setString(8, list.get(i).get(7));
				pstmt.setString(9, list.get(i).get(8));
				pstmt.setString(10, list.get(i).get(9));
				pstmt.setString(11, list.get(i).get(10));
				pstmt.setString(12, list.get(i).get(11));
				pstmt.setString(13, list.get(i).get(12));
				pstmt.setInt(14, Integer.parseInt(list.get(i).get(13)));
				pstmt.setString(15, list.get(i).get(14));
				pstmt.setInt(16, Integer.parseInt(list.get(i).get(15)));
				pstmt.setDouble(17, Double.parseDouble(list.get(i).get(16)));
				pstmt.setDouble(18, Double.parseDouble(list.get(i).get(17)));
				pstmt.setDouble(19, Double.parseDouble(list.get(i).get(18)));
				pstmt.setString(20, list.get(i).get(19));
				pstmt.setInt(21, Integer.parseInt(list.get(i).get(20)));
				pstmt.setString(22, list.get(i).get(21));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加入库关系表
	 * */
	public boolean addInProductRelation(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_inproductrelation values(?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setInt(1, Integer.parseInt(list.get(i).get(0)));
				pstmt.setInt(2, Integer.parseInt(list.get(i).get(1)));
				pstmt.setString(3, list.get(i).get(2));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加出库关系表
	 * */
	public boolean addOutProductRelation(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_outproductrelation values(?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				pstmt.setInt(1, Integer.parseInt(list.get(i).get(0)));
				pstmt.setInt(2, Integer.parseInt(list.get(i).get(1)));
				pstmt.setString(3, list.get(i).get(2));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * 添加日志表
	 * */
	public boolean addLog(List<ArrayList<String>> list){
		boolean flag=false;
		String sql="replace into qy_log values(?,?,?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			conn.setAutoCommit(false);
			pstmt=conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
//				pstmt.setInt(1, i);
				pstmt.setInt(1, Integer.parseInt(list.get(i).get(0)));
				pstmt.setInt(2, Integer.parseInt(list.get(i).get(1)));
				pstmt.setString(3, list.get(i).get(2));
				pstmt.setTimestamp(4, MyDateFormat.changeToSqlDate(MyDateFormat.changeStringToDate(list.get(i).get(3))));
				pstmt.setString(5, list.get(i).get(4));
				pstmt.setString(6, list.get(i).get(5));
				pstmt.setString(7, list.get(i).get(6));
				pstmt.setString(8, list.get(i).get(7));
				pstmt.setString(9, list.get(i).get(8));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.clearBatch();
		} catch (SQLException e1) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e1.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
}
