package cn.edu.cqupt.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.CommonShareDAO;
import cn.edu.cqupt.dao.LogDAO;

/**
 * 日志表的导入导出
 * @author LiangYH
 *
 */
public class LogInOutService {
 
	private LogDAO logDao = null;
	CommonShareDAO commonShareDao = null;
	
	public LogInOutService(){
		
	}
	
	/**
	 * 根据id查找log的所有字段
	 * @param logIDs
	 * @return
	 */
	public ArrayList<ArrayList<String>> queryLogs(List<Long> logIDs){
		if(logIDs == null)
			return null;
		
		this.logDao = new LogDAO();
		return logDao.queryLogsByID(logIDs);
	}
	
	/**
	 * 保存qy_log数据表
	 * @param logDyadic
	 * @return
	 */
	public boolean saveLogs(List<ArrayList<String>> logDyadic){
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("qy_log");
		if(logDyadic == null || logDyadic.size() == 0){
			return false;
		}
		
		this.commonShareDao = new CommonShareDAO();
		return commonShareDao.insertThreeTables(logDyadic, null, null, tableNames);
	}
	
}
