package cn.edu.cqupt.service.user_management;

import java.util.ArrayList;

import cn.edu.cqupt.beans.Role;
import cn.edu.cqupt.dao.RoleDAO;

public class RoleService {
	private static RoleDAO roleDAO = null;
	
	static {
		roleDAO = new RoleDAO();
	}
	
	/**
	 * 添加一个角色
	 * @param qy_role
	 * @return
	 */
	public boolean addRole(Role role){
		boolean flag=false;
		flag=roleDAO.addRole(role);
		return flag;
	}
	
	/**
	 * 删除一个角色
	 * @param qy_role
	 * @return
	 */
	public boolean deleteRole(long roleId){
		boolean flag=false;
		flag=roleDAO.deleteRole(roleId);
		return flag;
	}
	
	/**
	 * 更新一个角色的权限
	 * @param qy_role
	 * @return
	 */
	public boolean updateRole(Role role){
		boolean flag=false;
		flag=roleDAO.updateRole(role);
		return flag;
	}
	
	/**
	 * 得到全部角色的种类数
	 * @return
	 */
	public int getRoleCount(){
		int count=0;
		count=roleDAO.getRoleCount();
		return count;
	}
	
	/**
	 * 获取全部角色的角色名和权限
	 * @return
	 */
	public ArrayList<Role> searchAllRole() {
		ArrayList<Role> allQyRoleList=null;
		allQyRoleList=roleDAO.searchAllRole();
		return allQyRoleList;
	}
	
	/**
	 * 根据roleId获取Qy_role对象
	 * @param roleId
	 * @return
	 */
	public Role searchRoleById(int roleId){
		Role roleById=new Role();
		roleById=roleDAO.searchRoleById(roleId);
		return roleById;
	}
	
	/**
	 * 获取全部角色名
	 * @return
	 */
	public ArrayList<String> searchAllRoleName(){
		ArrayList<String> allRoleName=new ArrayList<String>();
		allRoleName=roleDAO.searchAllRoleName();
		return allRoleName;
	}
	
	/**
	 * 模糊查询获取角色
	 * @param searchType
	 * @param searchStr
	 * @return
	 */
	public ArrayList<Role> searchRoleLike(String searchType,String searchStr){
		ArrayList<Role> roleLikeList=new ArrayList<Role>();
		roleLikeList=roleDAO.searchRoleLike(searchType, searchStr);
		return roleLikeList;
	}
	
	/**
	 * 判断角色是否出现重复记录
	 * @param curRole
	 * @return
	 */
	public boolean repeatRole(String role) {
		boolean repeatflag=false;
		repeatflag=roleDAO.repeatRole(role);
		return repeatflag;
	}
}
