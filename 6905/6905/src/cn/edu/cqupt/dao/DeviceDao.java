package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.edu.cqupt.beans.Device;
import cn.edu.cqupt.db.DBConnection;

public class DeviceDao {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public DeviceDao() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 添加一台设备
	 * @param device
	 * @return
	 */
	public boolean addDevice(Device device) {
		boolean flag = false;
		try {
			conn = DBConnection.getConn();
			String sql = "INSERT INTO qy_deviceinfo(devicename,deviceno,position,deviceintime,repairtime,status) VALUES(?,?,?,?,?,?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, device.getDeviceName());
			this.pstmt.setString(2, device.getDeviceNo());
			this.pstmt.setString(3, device.getPosition());
			this.pstmt.setString(4, device.getDeviceInTime());
			this.pstmt.setString(5, device.getRepairTime());
			this.pstmt.setString(6, device.getStatus());
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
	 * 对一个设备的信息修改
	 * @param device
	 * @param deviceId
	 * @return
	 */
	public boolean updateDevice(Device device,long deviceId) {
		boolean flag=false;
		try {
			conn=DBConnection.getConn();
			String sql = "UPDATE qy_deviceinfo a SET a.devicename=?,a.deviceno=?,a.position=?,a.deviceintime=?,a.status=? WHERE a.deviceid=?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, device.getDeviceName());
			this.pstmt.setString(2, device.getDeviceNo());
			this.pstmt.setString(3, device.getPosition());
			this.pstmt.setString(4, device.getDeviceInTime());
			this.pstmt.setString(5, device.getStatus());
			this.pstmt.setLong(6,deviceId);
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
	 * 删除一个设备
	 * @param deviceId
	 * @return
	 */
	public boolean deleteDevice(long deviceId) {
		boolean flag=false;
		try {
			conn=DBConnection.getConn();
			String sql = "DELETE FROM qy_deviceinfo WHERE qy_deviceinfo.deviceId=?";
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setLong(1, deviceId);
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
	 * 分页显示所有的设备
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Device> searchDeviceByPage(int curPageNum,int pageSize) {
		ArrayList<Device> curDeviceList = new ArrayList<Device>();
		String sql = "SELECT * FROM qy_deviceinfo a ORDER BY a.deviceintime DESC LIMIT ?,?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			this.pstmt.setInt(1, (curPageNum-1)*pageSize);
			this.pstmt.setInt(2, pageSize);
			rs = this.pstmt.executeQuery();
			while (rs.next()) {
				Device device=new Device();
				device.setDeviceId(rs.getLong("deviceid"));
				device.setDeviceName(rs.getString("devicename"));
				device.setDeviceNo(rs.getString("deviceno"));
				device.setPosition(rs.getString("position"));
				device.setDeviceInTime(rs.getString("deviceintime"));
				device.setRepairTime(rs.getString("repairtime"));
				device.setStatus(rs.getString("status"));
				curDeviceList.add(device);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return curDeviceList;
	}
	
	/**
	 * 获取设备表记录数
	 * 
	 * @return
	 * @throws Exception
	 */
	public long getDeviceSum() {
		long sum=0;
		String sql = "SELECT COUNT(*) FROM qy_deviceinfo";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			if (rs.next()) {
				sum=rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return sum;
	}
	
	/**
	 * 获取设备的类型
	 * @return
	 */
	public ArrayList<String> getDeviceType(){
		ArrayList<String> deviceTypeList=new ArrayList<String>();
		String sql = "SELECT a.devicename FROM qy_deviceinfo a GROUP BY a.devicename;";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = this.pstmt.executeQuery();
			int count=1;
			while (rs.next()) {
				deviceTypeList.add(rs.getString(count));
			}
		} catch (SQLException e) {
			// TODO: handle exception
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return deviceTypeList;
	}
	
	/**
	 * 根据条件查询当前页设备信息
	 * @param sql
	 * @return
	 */
	public ArrayList<Device> searchDeviceInfoByCondition(String deviceStartTime,String deviceEndTime,String deviceType,String deviceStatus,int curPageNum,int pageSize){
		ArrayList<Device> curDeviceList=new ArrayList<Device>();
		String sql="SELECT * FROM qy_deviceinfo a WHERE a.devicename REGEXP ? AND a.status REGEXP ?";
		if(deviceStartTime.trim().length()!=0&&deviceEndTime.trim().length()!=0)
			sql+=" AND a.deviceintime BETWEEN '"+deviceStartTime+"' AND '"+deviceEndTime+"'";
		sql+=" ORDER BY a.deviceintime DESC LIMIT "+curPageNum+","+pageSize;
		try {
			conn=DBConnection.getConn();
			this.pstmt=conn.prepareStatement(sql);
			if(!deviceType.equals("所有设备"))
				this.pstmt.setString(1, "/*"+deviceType+"/*");
			else
				this.pstmt.setString(1, "/*/*");
			if(!deviceStatus.equals("全部状态"))
				this.pstmt.setString(2, "/*"+deviceStatus+"/*");
			else
				this.pstmt.setString(2, "/*/*");
			rs=this.pstmt.executeQuery();
			while (rs.next()) {
				Device device=new Device();
				device.setDeviceId(rs.getLong("deviceid"));
				device.setDeviceName(rs.getString("devicename"));
				device.setDeviceNo(rs.getString("deviceno"));
				device.setPosition(rs.getString("position"));
				device.setDeviceInTime(rs.getString("deviceintime"));
				device.setRepairTime(rs.getString("repairtime"));
				device.setStatus(rs.getString("status"));
				curDeviceList.add(device);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return curDeviceList;
		
	}
	
	/**
	 * 根据条件查询满足条件的设备个数
	 * @param sql
	 * @return
	 */
	public long getDeviceSumByCondition(String deviceStartTime,String deviceEndTime,String deviceType,String deviceStatus) {
		long deviceSum=0;
		String sql="SELECT COUNT(*) FROM qy_deviceinfo a WHERE a.devicename REGEXP ? AND a.status REGEXP ?";
		if(deviceStartTime.trim().length()!=0&&deviceEndTime.trim().length()!=0)
			sql+=" AND a.deviceintime BETWEEN '"+deviceStartTime+"' AND '"+deviceEndTime+"'";
		try {
			conn=DBConnection.getConn();
			this.pstmt=conn.prepareStatement(sql);
			if(!deviceType.equals("所有设备"))
				this.pstmt.setString(1, "/*"+deviceType+"/*");
			else
				this.pstmt.setString(1, "/*/*");
			if(!deviceStatus.equals("全部状态"))
				this.pstmt.setString(2, "/*"+deviceStatus+"/*");
			else
				this.pstmt.setString(2, "/*/*");
			rs=this.pstmt.executeQuery();
			if(rs.next())
				deviceSum=rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return deviceSum;
	}
	
	/**
	 * 通过deviceId获取device
	 * @param deviceId
	 * @return
	 */
	public Device getDeviceById(long deviceId){
		Device device=new Device();
		String sql="SELECT * FROM qy_deviceinfo a WHERE a.deviceid=?";
		try {
			conn=DBConnection.getConn();
			this.pstmt=conn.prepareStatement(sql);
			this.pstmt.setLong(1, deviceId);
			this.rs=this.pstmt.executeQuery();
			if(rs.next()){
				device.setDeviceName(rs.getString("devicename"));
				device.setDeviceNo(rs.getString("deviceno"));
				device.setPosition(rs.getString("position"));
				device.setDeviceInTime(rs.getString("deviceintime"));
				device.setStatus(rs.getString("status"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return device;
	}
}
