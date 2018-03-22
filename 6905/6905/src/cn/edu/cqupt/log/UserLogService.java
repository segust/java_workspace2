package cn.edu.cqupt.log;


import java.util.Date;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.dao.LogDAO;

public class UserLogService {
	private static LogDAO logDAO = null;
	
	static {
		logDAO = new LogDAO();
	}
	/**
	 * 记录当前用户的操作到日志
	 * @param log
	 * @return
	 */
	public static boolean SaveOperateLog(Log log) {
		boolean flag = false;
		flag = logDAO.saveOperateLog(log);
		return flag;
	}
	
	/**
	 * 增加维护记录
	 * @param productId
	 * @param username
	 * @param maintainType
	 * @param remark
	 * @return
	 */
	public static boolean SaveMainTainLog(String productId, String username,
			String maintainType, String remark) {
		LogDAO logDAO = new LogDAO();
		boolean flag = false;
		Log log = new Log();
		Date now = new Date();
		log.setProductId(Long.parseLong(productId));
		log.setUserName(username);
		log.setOperateTime(now);
		log.setMainTainType(maintainType);
		log.setOperateType("维护");
		log.setRemark(remark);	
		try {
			flag = logDAO.saveMainTainLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 增加检查记录
	 * @param productId
	 * @param username
	 * @param maintainType
	 * @param remark
	 * @return
	 */
	public static boolean SaveInspectLog(String productId, String username,
			String maintainType, String remark) {
		LogDAO logDAO = new LogDAO();
		boolean flag = false;
		Log log = new Log();
		Date now = new Date();
		log.setProductId(Long.parseLong(productId));
		log.setUserName(username);
		log.setOperateTime(now);
		log.setMainTainType(maintainType);
		log.setOperateType("检查");
		log.setRemark(remark);	
		try {
			flag = logDAO.saveMainTainLog(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 记录检查日志
	 * @param log
	 * @return
	 */
	public static Boolean saveInspectLog(Log log)
	{
		Boolean flag = logDAO.saveMainTainLog(log);
		
		return flag;
	}
}
