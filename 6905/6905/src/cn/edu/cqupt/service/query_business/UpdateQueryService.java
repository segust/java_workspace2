package cn.edu.cqupt.service.query_business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.AccountDAO;
import cn.edu.cqupt.dao.ContractDAO;
import cn.edu.cqupt.util.MyDateFormat;

public class UpdateQueryService {
	
	private static ContractDAO contractDAO = new ContractDAO();
	private static AccountDAO accountDAO = new AccountDAO();

	public List<Map<String, Object>> selectBorrowAndUpdate(HashMap<String, Object> condition){
		
		List<Map<String, Object>> list = contractDAO.selectBorrowAndUpdate(condition);
		List<Map<String, Object>> temp = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < list.size(); i++)
		{
			String productId = (String) list.get(i).get("productId");
			for(int j = 0; j < list.size(); j++)
			{
				if(i != j)
				{
					if(productId.equals(list.get(j).get("productId")))
						if(MyDateFormat.javaDateMills((Date)list.get(j).get("operateTime"), 
								(Date)list.get(i).get("operateTime")) > 0)
						{
							Map<String, Object> m = new HashMap<String, Object>();
							list.remove(j);
							list.add(j, m);
						}
						else
						{
							Map<String, Object> m = new HashMap<String, Object>();
							list.remove(i);
							list.add(i, m);
						}
							
				}
			}
		}
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).size() > 2)
				temp.add(list.get(i));
		int curPageNum = Integer.parseInt((String)condition.get("curPageNum"));
		int pageSize = Integer.parseInt((String)condition.get("pageSize"));
		for(int i = (curPageNum - 1)*pageSize; i < temp.size() && i < curPageNum*pageSize; i++)
		{
			Date d = (Date) temp.get(i).get("operateTime");
			temp.get(i).remove("operateTime");
			temp.get(i).put("operateTime", d.toLocaleString().substring(0, 10));
			result.add(temp.get(i));
		}
			
		
		return result;
	}
	
	public int selectBorrowAndUpdateCount(HashMap<String, Object> condition){	
		
		int count = 0;
		List<Map<String, Object>> list = contractDAO.selectBorrowAndUpdate(condition);
		for(int i = 0; i < list.size(); i++)
		{
			String productId = (String) list.get(i).get("productId");
			for(int j = 0; j < list.size(); j++)
			{
				if(i != j)
				{
					if(productId.equals(list.get(j).get("productId")))
						if(MyDateFormat.javaDateMills((Date)list.get(j).get("operateTime"), 
								(Date)list.get(i).get("operateTime")) > 0)
							list.remove(j);
						else
							list.remove(i);
				}
			}
		}
		count = list.size();
		return count;		
	}

	public List<Map<String, String>> selectDetailByProductModel(HashMap<String, Object> condition){

		List<Map<String, String>> list = null;
		try {
			list = accountDAO.selectProductAccountById(condition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Map<String, String>> selectDetailByContractId(HashMap<String, Object> condition){

		List<Map<String, String>> list = null;
		try {
			list = accountDAO.selectContractAccountById(condition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args)
	{
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("pageSize", 8 + "");
		condition.put("curPageNum", 1 + "");
		condition.put("fromdate", "2015-10-1");
		condition.put("todate", "2015-10-10");
		condition.put("prodyctModel", "K");
		UpdateQueryService service = new UpdateQueryService();
		System.out.println(service.selectBorrowAndUpdate(condition));
	}
}
