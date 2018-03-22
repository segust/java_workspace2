package cn.edu.cqupt.service.storage_maintenanc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.RestTime;

public class MaintainQueryService {

	private static ProductDAO dao = new ProductDAO();
	

	public ArrayList<HashMap<String, String>> getMaintainInfoByPage(List<String> condition, int curPageNum, int pageSize)
	{
		ArrayList<HashMap<String, Object>> maintainList = new ArrayList<HashMap<String, Object>>();
		maintainList = dao.selectMaintainDetailByPage(condition);
		int restMaintainDayCondition = Integer.MAX_VALUE;
		if(!condition.get(5).equals(""))
			restMaintainDayCondition = Integer.parseInt(condition.get(5));
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> result2 = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i < maintainList.size(); i++)
		{
			HashMap<String, Object> src = maintainList.get(i);
			HashMap<String, String> des = new HashMap<String, String>();
			des.put("productId", src.get("productId") + "");
			des.put("productModel", (String) src.get("productModel"));
			des.put("productUnit", (String) src.get("productUnit"));
			des.put("batch", (String) src.get("batch"));
			des.put("deviceNo", (String) src.get("deviceNo"));
			des.put("price", src.get("price") + "");
			des.put("num", src.get("num") + "");
			des.put("productType", (String) src.get("productType"));
			String storageTime = (String) src.get("storageTime");
			des.put("storageTime", storageTime);
			Date operateTime = (Date) src.get("operateTime");//新入库时间
			des.put("restStorageTime", RestTime.CountRestStorageTimeInDays(storageTime, operateTime));
			
			/*
			 * 下面计算剩余维护天数
			 * @param latestMaintainTime 上次维护时间
			 * @param maintainCycle 维护周期
			 */
			Date latestMaintainTime = (Date)src.get("latestMaintainTime");
			String maintainCycle = (String)src.get("maintainCycle");
//			int restMaintainTime = 0;//剩余维护天数
//			int numberInDayMaintain = 0;//按天数算维护周期
//			String unitStrMaintain = maintainCycle.substring(maintainCycle.length() - 1, maintainCycle.length());
//			String numberStrMaintain = maintainCycle.substring(0, maintainCycle.length() - 1);
//			int numberMaintain = Integer.parseInt(numberStrMaintain);			
//			if(unitStrMaintain.equalsIgnoreCase("d") || unitStrMaintain.equals("天"))
//				numberInDayMaintain = numberMaintain;
//			else if(unitStrMaintain.equalsIgnoreCase("m") || unitStrMaintain.equals("月"))
//				numberInDayMaintain = numberMaintain*30;
//			else if(unitStrMaintain.equals("y") || unitStrMaintain.equals("年"))
//				numberInDayMaintain = numberMaintain*365;
//			else{
//					des.put("restMaintainTime", "无效字段！");
//				//throw new RuntimeException("数据库中维护周期无法有效读取。");
//			}
//			long spendMaintainTimeInMills = MyDateFormat.javaDateMills(latestMaintainTime, now);
//			int spendMaintainTimeInDays = (int)(spendMaintainTimeInMills/(1000*60*60*24));//离上次维护已经过了几天
//			restMaintainTime = numberInDayMaintain - spendMaintainTimeInDays;
//			if(restMaintainTime < 0)
//				restMaintainTime = 0;
//			if(!condition.get(5).equals("") && restMaintainTime > Integer.parseInt(condition.get(5)))
//				continue;
			int restMaintainTimeInDays = RestTime.countRestMaintainTimeInDays(latestMaintainTime, maintainCycle);
			if(restMaintainTimeInDays <= restMaintainDayCondition)
				des.put("restMaintainTime", restMaintainTimeInDays + "天");
			else
				continue;
			
			des.put("manufacturer", (String) src.get("manufacturer"));
			des.put("keeper", (String) src.get("keeper"));
			des.put("username", condition.get(7));
			result.add(des);
		}
		for(int i = pageSize*(curPageNum-1); i < pageSize*curPageNum && i < result.size(); i++)
		{
			result2.add(result.get(i));
		}
		
		return result2;
	}
	
	public long getSum(List<String> condition) {
		ArrayList<HashMap<String, Object>> maintainList = new ArrayList<HashMap<String, Object>>();
		maintainList = dao.selectMaintainDetailByPage(condition);
		int restMaintainDayCondition = Integer.MAX_VALUE;
		if(!condition.get(5).equals(""))
			restMaintainDayCondition = Integer.parseInt(condition.get(5));
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for(int i = 0; i < maintainList.size(); i++)
		{
			HashMap<String, Object> src = maintainList.get(i);
			HashMap<String, String> des = new HashMap<String, String>();
			des.put("productId", src.get("productId") + "");
			des.put("productModel", (String) src.get("productModel"));
			des.put("productUnit", (String) src.get("productUnit"));
			des.put("batch", (String) src.get("batch"));
			des.put("deviceNo", (String) src.get("deviceNo"));
			des.put("price", src.get("price") + "");
			des.put("num", src.get("num") + "");
			des.put("productType", (String) src.get("productType"));
			String storageTime = (String) src.get("storageTime");
			des.put("storageTime", storageTime);
			Date operateTime = (Date) src.get("operateTime");//新入库时间
			des.put("restStorageTime", RestTime.CountRestStorageTimeInDays(storageTime, operateTime));
			
			/*
			 * 下面计算剩余维护天数
			 * @param latestMaintainTime 上次维护时间
			 * @param maintainCycle 维护周期
			 */
			Date latestMaintainTime = (Date)src.get("latestMaintainTime");
			String maintainCycle = (String)src.get("maintainCycle");
			int restMaintainTimeInDays = RestTime.countRestMaintainTimeInDays(latestMaintainTime, maintainCycle);
			if(restMaintainTimeInDays <= restMaintainDayCondition)
				des.put("restMaintainTime", restMaintainTimeInDays + "天");
			else
				continue;
			
			des.put("manufacturer", (String) src.get("manufacturer"));
			des.put("keeper", (String) src.get("keeper"));
			des.put("username", condition.get(7));
			result.add(des);
		}
		long sum = result.size();
		return sum;
	}

}
