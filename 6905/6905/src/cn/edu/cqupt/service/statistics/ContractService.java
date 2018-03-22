package cn.edu.cqupt.service.statistics;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.ContractDAO;
import cn.edu.cqupt.db.DBConnection;

public class ContractService {

	private Connection conn=null;
	private ContractDAO contractDAO=null;
	
	public ContractService() throws SQLException {
		this.conn = DBConnection.getConn();
		contractDAO = new ContractDAO(this.conn);
	}
	

	public  List<Map<String, String>> contractSearchStatistic(Map<String, String> map) {
		List<Map<String,String>> T=new ArrayList<Map<String,String>>();
		try {

			T = contractDAO.contractQueryStatistics(map);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return T;

	}
	
	public List<Map<String,String>> contractHandleDetail(String contractId)
	{
		List<Map<String,String>> T=new ArrayList<Map<String,String>>();
		T=contractDAO.contractOperationDetail(contractId);
		return T;
		
	}
	
	public List<HashMap<String, String>> OutInfoByProd(HashMap<String, String> condition){
		List<HashMap<String, String>> outInfoByProdList=new ArrayList<HashMap<String,String>>();
		outInfoByProdList=contractDAO.getProdIdByMoUnInTime(condition);
		return outInfoByProdList;
	}
	
	/*public static void main(String[] args)
	{
	Map<String,String> map=new HashMap<String,String>();
		map.put("contractId","10000");
//		map.put("signDate","2015-05-13");
//		map.put("manufacturer","huawei");
//		map.put("keeper", "hua");
//		map.put("productType", "gun");
//		map.put("productUnit", "tao");
//	map.put("productModel", "zheng");
	List<Map<String, String>> list = null;
	try {
		list = new ContractService ().contractSearchStatistic(map,curPageNum,pageSize);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	for(Map<String,String> ss:list)
	{
		for(String s:ss.keySet())
			System.out.println(s+"....."+ss.get(s));
	}
	
	
	}*/
}
