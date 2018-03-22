package cn.edu.cqupt.util;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CurrentUser {
	static HttpSession session=null;
	static HashMap<String, Integer> userPowerMap=new HashMap<String, Integer>();
	
	public CurrentUser() {
		// TODO Auto-generated constructor stub
	}
	
	
	@SuppressWarnings("unchecked")
	public static void getUserPower(HttpServletRequest request){
		session=request.getSession();
		userPowerMap=(HashMap<String, Integer>)session.getAttribute("userPowerMap");
	}
	
	
	/**
	 * 用户是否具有业务办理权限的判定
	 * @return
	 */
	public static  boolean isContractManage(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("contractManage")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有业务查询权限的判定
	 * @return
	 */
	public static boolean isQueryBusiness(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("queryBusiness")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有存储维护权限的判定
	 * @return
	 */
	public static boolean isStoreMantain(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("storeMantain")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有库房管理权限的判定
	 * @return
	 */
	public static boolean isWarehouseManage(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("warehouseManage")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有实力统计权限的判定
	 * @return
	 */
	public static boolean isStatistics(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("statistics")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有经费管理权限的判定
	 * @return
	 */
	public static boolean isFareManage(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("fareManage")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有资质管理权限的判定
	 * @return
	 */
	public static boolean isQualificationManage(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("qualificationManage")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有系统管理权限的判定
	 * @return
	 */
	public static boolean isSystemManage(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("systemManage")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有用户管理的管理员权限的判定
	 * @return
	 */
	public static boolean isUserManage(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("userManage")==1)
			return true;
		else
			return false;
	}
	
	/**
	 * 用户是否具有轮换更新权限的判定
	 * @return
	 */
	public static boolean isBorrowUpdate(HttpServletRequest request) {
		getUserPower(request);
		if(userPowerMap.get("borrowUpdate")==1)
			return true;
		else
			return false;
	}
}
