package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.edu.cqupt.beans.Role;
import cn.edu.cqupt.db.DBConnection;

public class RoleDAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public RoleDAO() {
		
	}

	/**
	 * addRole 增加一个角色
	 * 
	 * @param role
	 * @return
	 * @throws SQLException
	 */
	public boolean addRole(Role role){
		boolean flag = false;
		String sql = "INSERT INTO qy_role(role,contractManage,queryBusiness,borrowUpdate,storeMantain,warehouseManage,statistics,fareManage,qualificationManage,systemManage,userManage) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn=DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, role.getRole());
			pstmt.setInt(2, role.getContractManage());
			pstmt.setInt(3, role.getQueryBusiness());
			pstmt.setInt(4, role.getBorrowUpdate());
			pstmt.setInt(5, role.getStoreMantain());
			pstmt.setInt(6, role.getWarehouseManage());
			pstmt.setInt(7, role.getStatistics());
			pstmt.setInt(8, role.getFareManage());
			pstmt.setInt(9, role.getQualificationManage());
			pstmt.setInt(10, role.getSystemManage());
			pstmt.setInt(11, role.getUserManage());
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;

	}

	/**
	 * updateRole 更改角色的相关信息
	 * 
	 * @param role
	 * @return
	 * @throws SQLException
	 */
	public boolean updateRole(Role role) {
		boolean flag = false;
		String sql = "UPDATE qy_role SET role=?,contractManage=?,queryBusiness=?,storeMantain=?,warehouseManage=?,"
				+ "statistics=?,fareManage=?,qualificationManage=?,systemManage=?,userManage=?,borrowUpdate=? WHERE roleId=?";
		try {
			conn=DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, role.getRole());
			pstmt.setInt(2, role.getContractManage());
			pstmt.setInt(3, role.getQueryBusiness());
			pstmt.setInt(4, role.getStoreMantain());
			pstmt.setInt(5, role.getWarehouseManage());
			pstmt.setInt(6, role.getStatistics());
			pstmt.setInt(7, role.getFareManage());
			pstmt.setInt(8, role.getQualificationManage());
			pstmt.setInt(9, role.getSystemManage());
			pstmt.setInt(10, role.getUserManage());
			pstmt.setInt(11, role.getBorrowUpdate());
			pstmt.setLong(12, role.getRoleId());
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * deleteRole 删除一个角色
	 * 
	 * @param roleId
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteRole(long roleId) {
		boolean flag = false;
		String sql = "DELETE FROM qy_role WHERE roleId=?";
		try {
			conn=DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, roleId);
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}

	/**
	 * searchAllRole 得到所有的角色
	 * 
	 * @return
	 */
	public ArrayList<Role> searchAllRole() {
		ArrayList<Role> allRoleList = new ArrayList<Role>();
		String sql = "SELECT * FROM qy_role ORDER BY roleId";
		try {
			conn=DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Role role = new Role();
				role.setRoleId(rs.getLong("roleId"));
				role.setRole(rs.getString("role"));
				role.setContractManage(rs.getInt("contractManage"));
				role.setFareManage(rs.getInt("fareManage"));
				role
						.setQualificationManage(rs
								.getInt("qualificationManage"));
				role.setQueryBusiness(rs.getInt("queryBusiness"));
				role.setStoreMantain(rs.getInt("storeMantain"));
				role.setStatistics(rs.getInt("statistics"));
				role.setUserManage(rs.getInt("userManage"));
				role.setSystemManage(rs.getInt("systemManage"));
				role.setWarehouseManage(rs.getInt("warehouseManage"));
				role.setBorrowUpdate(rs.getInt("borrowUpdate"));
				allRoleList.add(role);
			}
			return allRoleList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return allRoleList;
	}
	
	/**
	 * 根据roleId获取当前Qy_role对象
	 * @param roleId
	 * @return
	 */
	public Role searchRoleById(int roleId){
		Role roleById=new Role();
		String sql="SELECT * FROM qy_role WHERE roleId=?";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, roleId);
			rs=pstmt.executeQuery();
			if(rs.next()){
				roleById.setRole(rs.getString("role"));
				roleById.setContractManage(rs.getInt("contractManage"));
				roleById.setFareManage(rs.getInt("fareManage"));
				roleById
						.setQualificationManage(rs
								.getInt("qualificationManage"));
				roleById.setQueryBusiness(rs.getInt("queryBusiness"));
				roleById.setStoreMantain(rs.getInt("storeMantain"));
				roleById.setStatistics(rs.getInt("statistics"));
				roleById.setUserManage(rs.getInt("userManage"));
				roleById.setSystemManage(rs.getInt("systemManage"));
				roleById.setWarehouseManage(rs.getInt("warehouseManage"));
				roleById.setBorrowUpdate(rs.getInt("borrowUpdate"));
			}
			return roleById;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return roleById;
	}
	
	/**
	 * getRoleCount 获取全部角色的数目
	 * 
	 * @return
	 */
	public int getRoleCount() {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM qy_role";
		try {
			conn=DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				count++;
			}
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return count;
	}
	
	/**
	 * 获取全部的角色名
	 * @return
	 */
	public ArrayList<String> searchAllRoleName(){
		ArrayList<String> allRoleName=new ArrayList<String>();
		String sql = "SELECT ROLE FROM qy_role";
		try {
			conn=DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				allRoleName.add(rs.getString("role"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return allRoleName;
	}
	
	/**
	 * 模糊查询角色
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Role> searchRoleLike(String searchType,String searchStr){
		ArrayList<Role> roleLikeList = new ArrayList<Role>();
		try {
			conn=DBConnection.getConn();
			String sql = "SELECT * FROM qy_role WHERE "+searchType+" LIKE '%"+searchStr+"%' ORDER BY roleId";
			this.pstmt = this.conn.prepareStatement(sql);
			this.rs = this.pstmt.executeQuery();
			while(this.rs.next()){
				Role role = new Role();
				role.setRoleId(rs.getLong("roleId"));
				role.setRole(rs.getString("role"));
				role.setContractManage(rs.getInt("contractManage"));
				role.setFareManage(rs.getInt("fareManage"));
				role.setQualificationManage(rs.getInt("qualificationManage"));
				role.setQueryBusiness(rs.getInt("queryBusiness"));
				role.setStoreMantain(rs.getInt("storeMantain"));
				role.setStatistics(rs.getInt("statistics"));
				role.setUserManage(rs.getInt("userManage"));
				role.setSystemManage(rs.getInt("systemManage"));
				role.setWarehouseManage(rs.getInt("warehouseManage"));
				role.setBorrowUpdate(rs.getInt("borrowUpdate"));
				roleLikeList.add(role);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleLikeList;
	}
	
	/**
	 * 根据角色名判断是否有重复记录
	 * @param role
	 * @return
	 */
	public boolean repeatRole(String role) {
		boolean repeatFlag=false;
		try {
			conn=DBConnection.getConn();
			String sql = "SELECT * FROM qy_role WHERE qy_role.role=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, role);
			this.rs = this.pstmt.executeQuery();
			if(this.rs.next()){
				repeatFlag=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return repeatFlag;
	}
}
