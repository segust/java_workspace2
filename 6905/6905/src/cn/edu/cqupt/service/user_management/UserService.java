package cn.edu.cqupt.service.user_management;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.cqupt.beans.User;
import cn.edu.cqupt.dao.UserDAO;

public class UserService {
	private static UserDAO userDAO =new UserDAO();
	
	//每个方法的解释看对应DAO里面方法的注释
	
	public boolean addUser(User user){
		boolean flag = false;
		flag = userDAO.addUser(user);
		return flag;
	}
	
	public boolean updateuser(User user){
		boolean flag = false;
		flag = userDAO.updateUser(user);
		return flag;
	}
	
	public boolean updatePwd(String password,String identifyNum){
		boolean flag=false;
		flag=userDAO.updatePwd(password, identifyNum);
		return flag;
	}
	
	public boolean deleteUser(String identifyNum){
		boolean flag = false;
		flag=userDAO.deleteUser(identifyNum);
		return flag;
	}
	
	public HashMap<String, Integer>  getUserRolePower(String identifyNum){
		HashMap<String, Integer> userRolePowerMap=new HashMap<String, Integer>();
		userRolePowerMap=userDAO.getUserRolePower(identifyNum);
		return userRolePowerMap;
	}
	
	public String getUserNameByIdentifyNum(String identifyNum) {
		String username="";
		username=userDAO.getNameByIdentifyNum(identifyNum);
		return username;
	}
	
	public ArrayList<User> searchUserByPage(int curPageNum,int pageSize){
		ArrayList<User> allUserList=new ArrayList<User>();
		allUserList=userDAO.searchUserByPage(curPageNum,pageSize);
		return allUserList;
	}
	
	public int validateUser(User user) {
		int loginFlag=0;
		loginFlag=userDAO.validateUser(user);
		return loginFlag;
	}
	
	public String getRolenameByUserId(String identifyNum){
		String roleName = "";
		roleName = userDAO.getRolenameByUserId(identifyNum);
		return roleName;
	}
	
	public User getUserByIdentifyNum(String identifyNum) {
		User user=new User();
		user=userDAO.getUserByIdentifyNum(identifyNum);
		return user;
	}
	
	public ArrayList<User> searchUserLikeByPage(String searchType,String searchStr,int curPageNum,int pageSize){
		ArrayList<User> userLikeList=new ArrayList<User>();
		userLikeList=userDAO.searchUserLikeByPage(searchType, searchStr,curPageNum,pageSize);
		return userLikeList;
	}
	
	public long getUserSum(){
		long userSum=0;
		userSum=userDAO.getUserSum();
		return userSum;
	}
	
	public long getLikeUserSum(String searchType,String searchStr){
		long likeUserSum=0;
		likeUserSum=userDAO.getLikeUserSum(searchType, searchStr);
		return likeUserSum;
	}
	
	public boolean repeatUser(String identifyNum,String ownedUnit) {
		boolean repeatflag=false;
		repeatflag=userDAO.repeatUser(identifyNum, ownedUnit);
		return repeatflag;
	}
	
	public String getOwnedUnitByIdentifyNum(String identifyNum) {
		String ownedUnit="";
		ownedUnit=userDAO.getOwnedUnitByIdentifyNum(identifyNum);
		return ownedUnit;
	}
}
