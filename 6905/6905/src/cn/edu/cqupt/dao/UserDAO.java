package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import cn.edu.cqupt.beans.User;
import cn.edu.cqupt.db.DBConnection;

public class UserDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public UserDAO() {
	}

	/**
	 * addUser 管理员向数据库里面添加一个用户
	 * 
	 * @return
	 */
	public boolean addUser(User user) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_user(identifyNum,password,name,role,duty,ownedUnit,authorityUnit) VALUES(?,?,?,?,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, user.getIdentifyNum());
			this.pstmt.setString(2, user.getPassword());
			this.pstmt.setString(3, user.getName());
			this.pstmt.setString(4, user.getRole());
			this.pstmt.setString(5, user.getDuty());
			this.pstmt.setString(6, user.getOwnedUnit());
			this.pstmt.setString(7, user.getAuthorityUnit());
			int count = this.pstmt.executeUpdate();
			if (count > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * updateUser 对一个用户的内容进行更改
	 * 
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user) {
		boolean flag=false;
		try {
			conn=DBConnection.getConn();
			String sql = "UPDATE qy_user SET identifyNum=?,name=?,role=?,duty=?,ownedUnit=?,authorityUnit=? WHERE identifyNum=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, user.getIdentifyNum());
			this.pstmt.setString(2, user.getName());
			this.pstmt.setString(3, user.getRole());
			this.pstmt.setString(4, user.getDuty());
			this.pstmt.setString(5, user.getOwnedUnit());
			this.pstmt.setString(6, user.getAuthorityUnit());
			this.pstmt.setString(7, user.getIdentifyNum());
			int count = pstmt.executeUpdate();
			if(count>0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 更新用户密码
	 * @param password
	 * @param identifyNum
	 * @return
	 */
	public boolean updatePwd(String password,String identifyNum) {
		boolean flag=false;
		String sql="UPDATE qy_user SET password=? WHERE identifyNum=?";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setString(2, identifyNum);
			int count=pstmt.executeUpdate();
			if(count>0)
				flag=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, this.pstmt, this.rs);
		}
		return flag;
	}
	
	/**
	 * deleteUser 删除一个用户
	 * 
	 * @param userId
	 * @return
	 */
	public boolean deleteUser(String identifyNum) {
		boolean flag=false;
		try {
			conn=DBConnection.getConn();
			String sql = "DELETE FROM qy_user WHERE identifyNum=?";
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, identifyNum);
			int count = pstmt.executeUpdate();
			if(count>0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
	}

	/**
	 * 验证用户输入信息是否正确
	 * 0:密码或用户名出错
	 * 1:登录成功
	 * -1:数据库连接异常
	 * @param user
	 * @return
	 */
	public  int validateUser(User user) {
		int loginFlag=0;
		String sql = "SELECT * FROM qy_user where identifyNum=? and password=?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getIdentifyNum());
			pstmt.setString(2, user.getPassword());
			rs = pstmt.executeQuery();
			if (rs.next())
				loginFlag = 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			loginFlag=-1;
			System.out.println("数据库连接异常");
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		//return loginFlag;
		return loginFlag;
	}

	/**
	 * searchAllUserByPage 分页显示所有的用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<User> searchUserByPage(int curPageNum,int pageSize) {
		ArrayList<User> allUserList = new ArrayList<User>();
		String sql = "SELECT * FROM qy_user ORDER BY userId LIMIT ?,?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setInt(1, (curPageNum-1)*pageSize);
			this.pstmt.setInt(2, pageSize);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setUserId(rs.getLong("userId"));
				user.setIdentifyNum(rs.getString("identifyNum"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setOwnedUnit(rs.getString("ownedUnit"));
				user.setAuthorityUnit(rs.getString("authorityUnit"));
				user.setDuty(rs.getString("duty"));
				user.setUserId(rs.getInt("userId"));
				allUserList.add(user);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return allUserList;
	}

	/**
	 * 根据IdentifyNum获取Name
	 * 
	 * @return
	 */
	public String getNameByIdentifyNum(String identifyNum) {
		String userName = "";
		try {
			conn=DBConnection.getConn();
			String sql = "SELECT name FROM qy_user WHERE identifyNum=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, identifyNum);
			rs = pstmt.executeQuery();
			if(rs.next()){
				userName = rs.getString("name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return userName;
	}
	
	/**
	 * 通过用户账号获取用户对象
	 * @param identifyNum  用户登录名 eg:00001
	 * @return
	 */
	public User getUserByIdentifyNum(String identifyNum){
		User user=new User();
		String sql="SELECT * FROM qy_user WHERE identifyNum=?";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, identifyNum);
			rs=pstmt.executeQuery();
			if (rs.next()) {
				user.setIdentifyNum(identifyNum);
				user.setName(rs.getString("name"));
				user.setOwnedUnit(rs.getString("ownedUnit"));
				user.setAuthorityUnit(rs.getString("authorityUnit"));
				user.setDuty(rs.getString("duty"));
				user.setRole(rs.getString("role"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return user;
	}
	
	/**
	 * getRolenameByUserId 根据 用户identifyNum查询roleName
	 * 
	 * @return
	 */
	public String getRolenameByUserId(String identifyNum) {
		String roleName="";
		try {
			String sql = "SELECT role FROM qy_user WHERE identifyNum=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, identifyNum);
			rs = pstmt.executeQuery();
			if(rs.next()){
				roleName = rs.getString("role");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, this.pstmt,this.rs);
		}
		return roleName;
	}

	/**zyk备注:把十个字段全部取出来
	 * getUserRolePower 通过用户的identifyNum得到用户的权限
	 *          
	 * @param identifyNum
	 * @return
	 */
	public HashMap<String, Integer> getUserRolePower(String identifyNum) {
		HashMap<String, Integer> userRolePowerMap = new HashMap<String, Integer>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_role WHERE role="
					+ "(SELECT role from qy_user WHERE identifyNum=?)";
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, identifyNum);
			this.rs = this.pstmt.executeQuery();
			// 如果用户输入的信息正确，则获取该用户的权限列表
			if (rs.next()) {
				userRolePowerMap.put("contractManage", rs
						.getInt("contractManage"));
				userRolePowerMap.put("queryBusiness", rs
						.getInt("queryBusiness"));
				userRolePowerMap.put("borrowUpdate", rs
						.getInt("borrowUpdate"));
				userRolePowerMap.put("storeMantain", rs.getInt("storeMantain"));
				userRolePowerMap.put("warehouseManage", rs
						.getInt("warehouseManage"));
				userRolePowerMap.put("statistics", rs.getInt("statistics"));
				userRolePowerMap.put("fareManage", rs.getInt("fareManage"));
				userRolePowerMap.put("qualificationManage", rs
						.getInt("qualificationManage"));
				userRolePowerMap.put("systemManage", rs.getInt("systemManage"));
				userRolePowerMap.put("userManage", rs.getInt("userManage"));
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, this.pstmt, this.rs);
		}
		return userRolePowerMap;
	}
	
	/**
	 * 模糊查询用户
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<User> searchUserLikeByPage(String searchType,String searchStr,int curPageNum,int pageSize){
		ArrayList<User> userLikeList = new ArrayList<User>();
		try {
			conn=DBConnection.getConn();
			String sql = "SELECT * FROM qy_user WHERE "+searchType+" LIKE '%"+searchStr+"%' ORDER BY userId LIMIT ?,?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setInt(1, (curPageNum-1)*pageSize);
			this.pstmt.setInt(2, pageSize);
			this.rs = this.pstmt.executeQuery();
			while(this.rs.next()){
				User user = new User();
				user.setUserId(rs.getLong(1));
				user.setIdentifyNum(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setName(rs.getString(4));
				user.setRole(rs.getString(5));
				user.setDuty(rs.getString(6));
				user.setOwnedUnit(rs.getString(7));
				user.setAuthorityUnit(rs.getString(8));
				userLikeList.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, this.pstmt, this.rs);
		}
		return userLikeList;
	}
	
	/**
	 * 模糊查询用户的个数
	 * @param 
	 * @return
	 */
	public long getLikeUserSum(String searchType,String searchStr){
		long likeUserSum=0;
		try {
			conn=DBConnection.getConn();
			String sql = "SELECT COUNT(*) FROM qy_user WHERE "+searchType+" LIKE '%"+searchStr+"%'";
			this.pstmt = this.conn.prepareStatement(sql);
			this.rs = this.pstmt.executeQuery();
			if(this.rs.next()){
				likeUserSum=rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, this.pstmt, this.rs);
		}
		return likeUserSum;
	}
	
	/**
	 * 获取所有用户的个数
	 * @return
	 */
	public long getUserSum(){
		long userSum=0;
		String sql="SELECT COUNT(*) FROM qy_user";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next())
				userSum=rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userSum;
	}
	
	/**
	 * 根据账号和所属单位判断是否有重复记录
	 * @param identifyNum
	 * @param ownedUnit
	 * @return
	 */
	public boolean repeatUser(String identifyNum,String ownedUnit){
		boolean repeatFlag = false;
		String sql="SELECT * FROM qy_user WHERE qy_user.identifyNum=? and qy_user.ownedUnit=?";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, identifyNum);
			pstmt.setString(2, ownedUnit);
			rs=pstmt.executeQuery();
			if(rs.next()){
				repeatFlag=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, this.pstmt, this.rs);
		}
		return repeatFlag;
	}
	
	/**
	 * 获取用户所属单位
	 * @param identifyNum
	 * @return
	 */
	public String getOwnedUnitByIdentifyNum(String identifyNum){
		String ownedUnit="";
		String sql="SELECT qy_user.ownedUnit FROM qy_user where identifyNum=?";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, identifyNum);
			rs=pstmt.executeQuery();
			if(rs.next()){
				ownedUnit=rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, this.pstmt, this.rs);
		}
		return ownedUnit;
	}
}