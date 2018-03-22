package cn.edu.cqupt.service.statistics;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.service.qualification_management.InfoService;

public class EquipmentDetailAccountService {
	private Connection conn=null;
	private ProductDAO productDAO=null;
	
	public EquipmentDetailAccountService() throws SQLException {
		this.conn = DBConnection.getConn();
		productDAO = new ProductDAO(this.conn);
	}
	
	/**
	 * 器材明细账统计
	 * */
	public List<HashMap<String, Object>> selectEquipmentDetailAccounts(HashMap<String, String> condition,
			int curPageNum, int pageSize, String version) throws Exception{
		List<HashMap<String,Object>> T=new ArrayList<HashMap<String, Object>>();
		condition = new InfoService().getOwnedUnitSQL(condition, version);
		T= productDAO.selectEquipmentDetailAccount(condition,curPageNum,pageSize,version);
		return T;
	}
	
	/**
	 * 器材明细账统计数量
	 * */
	public int selectEquipmentDetailAccountCount(HashMap<String, String> condition,String version) throws Exception{
		condition = new InfoService().getOwnedUnitSQL(condition, version);
		int count = productDAO.selectEquipmentDetailAccountCount(condition,version);
		return count;
	}
	
	/**
	 * 查询发料单
	 * 先查询发料单号，再根据发料单号的日期查询相应的在库数量
	 * */
	public List<HashMap<String, String>> selectOutList(HashMap<String, String> condition) throws Exception{
		List<HashMap<String,String>> T=new ArrayList<HashMap<String, String>>();
		//查询发料单号，代储企业
		T = productDAO.selectOutList(condition);
//		System.out.println("T.size:"+T.size());
		//查询日期，发出数量
		ArrayList<String> listId = new ArrayList<String>();
		for (int i = 0; i < T.size(); i++) {
			listId.add(T.get(i).get("listId"));
		}
		T = productDAO.selectOutListMessage(T, listId);
		//查询某日期下在库数量
		T = productDAO.selectEquipmentDetailAccountNum(T, condition);
		
//		for (int i = 0; i < T.size(); i++) {
//			HashMap<String, String> account = T.get(i);
//			System.out.print(account.get("listId")+"  ");
//			System.out.print(account.get("keeper")+"  ");
//			System.out.print(account.get("date")+"  ");
//			System.out.print(account.get("year")+"  ");
//			System.out.print(account.get("month")+"  ");
//			System.out.print(account.get("day")+"  ");
//			System.out.print(account.get("out")+"  ");
//			System.out.print(account.get("rest")+"  ");
//			System.out.print(account.get("num")+"  ");
//			System.out.print(account.get("income")+"  ");
//			System.out.print(account.get("remark")+"  ");
//			System.out.println();
//		}
		return T;
	}
	
	/*
	 * 器材明细账统计
	 * */
	public void insertEquipmentDetailAccounts(List<ArrayList<String>> dyadicArray,String flg)
	{
		productDAO.insertEquipmentDetailAccount(dyadicArray,flg);
	}
	
	
	
//public static void main(String[] args)
//{
////HashMap<String,Object> condition=new HashMap<String,Object>();
//	List<ArrayList<String>> dyadicArray=new ArrayList<ArrayList<String>>();
//List<HashMap<String,Object>> T=new ArrayList<HashMap<String,Object>>();
//ArrayList<String> list=new ArrayList<String>();
//ArrayList<String> li=new ArrayList<String>();
//list.add("inId");
//li.add("22");
//dyadicArray.add(list);
//dyadicArray.add(li);
//try {
//	new EquipmentDetailAccountService().insertEquipmentDetailAccounts(dyadicArray,"inApply");
//} catch (SQLException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//} catch (Exception e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
//for(HashMap<String,Object> map:T)
//{
//	for(String keys:map.keySet())
//	{
//		System.out.println("keys.............."+(String)map.get(keys));
//	}
//}
//
//}
}
