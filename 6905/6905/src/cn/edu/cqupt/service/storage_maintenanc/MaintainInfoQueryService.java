package cn.edu.cqupt.service.storage_maintenanc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.dao.LogDAO;
import cn.edu.cqupt.util.RestTime;

public class MaintainInfoQueryService {

	private LogDAO dao = null;
	public MaintainInfoQueryService() {
		dao = new LogDAO();
	}
	
	/**
	 * 查询维护记录
	 * 黄恺
	 * @param condition
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getMaintainLog(List<String> condition)
	{
		ArrayList<HashMap<String, Object>> maintainLog = dao.selectMaintainInfoDetail(condition);
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		//Date now = new Date();
		for(int i = 0; i < maintainLog.size(); i++)
		{
			HashMap<String, Object> src = maintainLog.get(i);
			HashMap<String, String> des = new HashMap<String, String>();
			des.put("productId", src.get("productId") + "");
			des.put("productModel", (String) src.get("productModel"));
			des.put("productUnit", (String) src.get("productUnit"));
			des.put("batch", (String) src.get("batch"));
			des.put("deviceNo", (String) src.get("deviceNo"));
			des.put("price", src.get("price") + "");
			des.put("num", src.get("num") + "");
			des.put("productType", (String) src.get("productType"));
			des.put("storageTime", (String) src.get("storageTime"));
			des.put("restKeepTime", src.get("restKeepTime") + "");
			des.put("manufacturer", (String) src.get("manufacturer"));
			des.put("keeper", (String) src.get("keeper"));
			des.put("maintainType", (String) src.get("maintainType"));
			String operateTime = ((Date)src.get("operateTime")).toLocaleString();
			des.put("operateTime", operateTime.substring(0, operateTime.length() - 7));
			des.put("remark", (String) src.get("remark"));
			result.add(des);
		}
		return result;
	}
	
	public ArrayList<HashMap<String, String>> getMaintainLogByPage(List<String> condition, int pageSize, int curPageNumber)
	{
		ArrayList<HashMap<String, Object>> maintainLog = dao.selectMaintainInfoDetailByPage(condition, pageSize, curPageNumber);
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
//		Date now = new Date();
		for(int i = 0; i < maintainLog.size(); i++)
		{
			HashMap<String, Object> src = maintainLog.get(i);
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
			Date deliveryTime = (Date) src.get("deliveryTime");
			des.put("restKeepTime", RestTime.CountRestStorageTimeInDays(storageTime, deliveryTime));
			des.put("manufacturer", (String) src.get("manufacturer"));
			des.put("keeper", (String) src.get("keeper"));
			des.put("maintainType", (String) src.get("maintainType"));
			String operateTime = ((Date)src.get("operateTime")).toLocaleString();
			des.put("operateTime", operateTime.substring(0, operateTime.length() - 7));			
			des.put("remark", (String) src.get("remark"));
			des.put("logId", src.get("logId") + "");
			result.add(des);
		}
		return result;
	}
	
	/**
	 * 查询检查记录
	 * @param condition
	 * @param pageSize
	 * @param curPageNumber
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getInspectLogByPage(List<String> condition, int pageSize, int curPageNumber)
	{
		ArrayList<HashMap<String, Object>> maintainLog = dao.selectInspectInfoDetailByPage(condition, pageSize, curPageNumber);
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		//Date now = new Date();
		for(int i = 0; i < maintainLog.size(); i++)
		{
			HashMap<String, Object> src = maintainLog.get(i);
			HashMap<String, String> des = new HashMap<String, String>();
			des.put("productId", src.get("productId") + "");
			des.put("productModel", (String) src.get("productModel"));
			des.put("productUnit", (String) src.get("productUnit"));
			des.put("batch", (String) src.get("batch"));
			des.put("deviceNo", (String) src.get("deviceNo"));
			des.put("price", src.get("price") + "");
			des.put("num", src.get("num") + "");
			des.put("productType", (String) src.get("productType"));
			des.put("storageTime", (String) src.get("storageTime"));
			des.put("restKeepTime", src.get("restKeepTime") + "");
			des.put("manufacturer", (String) src.get("manufacturer"));
			des.put("keeper", (String) src.get("keeper"));
			des.put("maintainType", (String) src.get("maintainType"));
			String operateTime = ((Date)src.get("operateTime")).toLocaleString();
			des.put("operateTime", operateTime.substring(0, operateTime.length() - 7));			des.put("remark", (String) src.get("remark"));
			result.add(des);
		}
		return result;
	}
	
	public int countResultNumber(List<String> condition)
	{
		return dao.countMaintainInfoDetail(condition);
	}
	public int countInspectNumber(List<String> condition){
		return dao.countInspectNumber(condition);
	}
}
