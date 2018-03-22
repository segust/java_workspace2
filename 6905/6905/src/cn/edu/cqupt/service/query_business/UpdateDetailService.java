package cn.edu.cqupt.service.query_business;
/**
 * @author huangkai
 * 分页在这里进行
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.edu.cqupt.dao.ProductDAO;

public class UpdateDetailService {

	private ProductDAO dao;
	public UpdateDetailService(){
		dao = new ProductDAO();
	}
	
	public List<HashMap<String, String>> getInfoByProductModel(String productModel, HashMap<String, String> condition)
	{
		List<HashMap<String, String>> data = dao.getUpdateDetailByProductModel(productModel);
		List<HashMap<String, String>> temp = buildUpdateInfo(data);
		int pageSize = Integer.parseInt(condition.get("pageSize"));
		int curPageNum = Integer.parseInt(condition.get("curPageNum"));
		List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		for(int i = (curPageNum - 1)*pageSize; (i < temp.size())&&(i < curPageNum*pageSize); i++)
		{
			result.add(temp.get(i));
		}
//		return data;
		return result;
	}
	
	public int getInfoByProductModelCount(String productModel, HashMap<String, String> condition)
	{
		List<HashMap<String, String>> data = dao.getUpdateDetailByProductModel(productModel);
		List<HashMap<String, String>> temp = buildUpdateInfo(data);
//		return data;
		return temp.size();
	}
	
	public List<HashMap<String, String>> getInfoByProductId(String productId, HashMap<String, String> condition)
	{
		List<HashMap<String, String>> data = dao.getUpdateDetailByProductId(productId);
		List<HashMap<String, String>> temp = buildUpdateInfo(data);
		int pageSize = Integer.parseInt(condition.get("pageSize"));
		int curPageNum = Integer.parseInt(condition.get("curPageNum"));
		List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		for(int i = (curPageNum - 1)*pageSize; (i < temp.size())&&(i < curPageNum*pageSize); i++)
		{
			result.add(temp.get(i));
		}
		return result;
	}
	
	public int getInfoByProductIdCount(String productId, HashMap<String, String> condition)
	{
		List<HashMap<String, String>> data = dao.getUpdateDetailByProductId(productId);
		List<HashMap<String, String>> temp = buildUpdateInfo(data);
		return temp.size();
	}
	
	public static void main(String[] args)
	{
//		UpdateDetailService service = new UpdateDetailService();
//		List<HashMap<String, String>> data = service.getInfoByProductId("2");
//		for(int i = 0; i < data.size(); i++)
//		{
//			HashMap<String, String> one = data.get(i);
////			if(one.get("otherProductId") != null)
//				System.out.println(one);
//		}
	}
	
	private List<HashMap<String, String>> buildUpdateInfo(List<HashMap<String, String>> data)
	{
		List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		ConcurrentHashMap<String, HashMap<String, String>> temp = new ConcurrentHashMap<String, HashMap<String,String>>();
		for(int i = 0; i < data.size(); i++)
		{
			HashMap<String, String> one = data.get(i);
			if(one.get("otherProductId") == null)
				result.add(one);
			else
				temp.put(one.get("productId"), one);
		}
		for(ConcurrentHashMap.Entry<String, HashMap<String, String>> entry:temp.entrySet())
		{
			if(entry.getValue().get("flag").equals("2"))
			{
				if(Integer.parseInt(entry.getKey()) < Integer.parseInt(entry.getValue().get("otherProductId")))
					result.add(mixMap(entry.getValue(), temp.get(entry.getValue().get("otherProductId"))));
//				if(temp.containsKey(entry.getValue().get("otherProductId")))
//					result.add(mixMap(entry.getValue(), temp.get(entry.getValue().get("otherProductId"))));
//				else
//					result.add(entry.getValue());
			}
//			else if(entry.getValue().get("flag").equals("2"))
//				if(!temp.containsKey(entry.getValue().get("otherProductId")))
//				{
//					result.add(entry.getValue());
//				}
		}
		return result;
	}
	
	private HashMap<String, String> mixMap(HashMap<String, String> one, HashMap<String, String> two)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		for(Map.Entry<String, String> entry:one.entrySet())
		{
			result.put(entry.getKey() + "1", entry.getValue());
		}
		for(Map.Entry<String, String> entry:two.entrySet())
		{
			result.put(entry.getKey() + "2", entry.getValue());
		}
		
		return result;
	}
}
