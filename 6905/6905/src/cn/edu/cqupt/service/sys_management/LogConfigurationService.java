package cn.edu.cqupt.service.sys_management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.LogDAO;

public class LogConfigurationService {

private static LogDAO logDAO = new LogDAO();
	
	public List<Map<String, String>> queryLog(HashMap<String, Object> condition){

		List<Map<String, String>> list = null;
		try {
			list = logDAO.queryOperateLog(condition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return list;
	}
	
	public int getTotalCountQueryLog(HashMap<String, Object> condition){
		
		int totalCount = 0;
		try {
			totalCount = logDAO.queryOperateLogCount(condition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalCount;
	}
}
