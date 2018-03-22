package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.cqupt.beans.Parameter_Configuration;
import cn.edu.cqupt.db.DBConnection;

public class ParameterDAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public ParameterDAO(){
		
	}
	
	/**
	 * @author 
	 * @param Qy_Parameter
	 * @return boolean 成功true 失败false
	 * */
	public boolean insertParameter(Parameter_Configuration parameter_configuration,String version){
		boolean flag=false;			
		String sql="SELECT * FROM qy_parameter_configuration";
		try {
			// 设置参数
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			boolean mark=false;
			if (rs.next()){
				mark=true;
			}
			if(version.equals("1")){
				String sqlUpdate="Update qy_parameter_configuration set maintain_cycle=?,cycle_ahead_days=?,store_ahead_days=?,out_ahead_days=? where id=1";
				String sqlInsert="INSERT INTO qy_parameter_configuration (id,maintain_cycle,cycle_ahead_days,store_ahead_days,out_ahead_days,price_difference,alarm_cycle,alarm_ahead_days) VALUES (?,?,?,?,?,null,null,null)";
				if(mark){
					pstmt = conn.prepareStatement(sqlUpdate);
					pstmt.setString(1, parameter_configuration.getMaintainCycle());
					pstmt.setInt(2, parameter_configuration.getCycle_ahead_days());
					pstmt.setInt(3, parameter_configuration.getStore_ahead_days());
					pstmt.setInt(4, parameter_configuration.getOut_ahead_days());
					pstmt.executeUpdate();
				}else{
					pstmt = conn.prepareStatement(sqlInsert);
					pstmt.setInt(1, 1);
					pstmt.setString(2, parameter_configuration.getMaintainCycle());
					pstmt.setInt(3, parameter_configuration.getCycle_ahead_days());
					pstmt.setInt(4, parameter_configuration.getStore_ahead_days());
					pstmt.setInt(5, parameter_configuration.getOut_ahead_days());
					pstmt.executeUpdate();
				}
			}else if(version.equals("2")){
				String sqlUpdate="Update qy_parameter_configuration set store_ahead_days=?,out_ahead_days=?,price_difference=?,alarm_cycle=?,alarm_ahead_days=? where id=1";
				String sqlInsert="INSERT INTO qy_parameter_configuration (id,maintain_cycle,cycle_ahead_days,store_ahead_days,out_ahead_days,price_difference,alarm_cycle,alarm_ahead_days) VALUES (?,null,null,?,?,?,?,?)";
				if(mark){
					pstmt = conn.prepareStatement(sqlUpdate);
					pstmt.setInt(1, parameter_configuration.getStore_ahead_days());
					pstmt.setInt(2, parameter_configuration.getOut_ahead_days());
					pstmt.setInt(3, parameter_configuration.getPrice_difference());
					pstmt.setString(4, parameter_configuration.getAlarm_cycle());
					pstmt.setInt(5, parameter_configuration.getAlarm_ahead_days());
					pstmt.executeUpdate();
				}else{
					pstmt = conn.prepareStatement(sqlInsert);
					pstmt.setInt(1, 1);
					pstmt.setInt(2, parameter_configuration.getStore_ahead_days());
					pstmt.setInt(3, parameter_configuration.getOut_ahead_days());
					pstmt.setInt(4, parameter_configuration.getPrice_difference());
					pstmt.setString(5, parameter_configuration.getAlarm_cycle());
					pstmt.setInt(6, parameter_configuration.getAlarm_ahead_days());
					pstmt.executeUpdate();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt);
		}
		return flag;
	}
	
	/**
	 * @author 
	 * 查询参数
	 * */
	public Parameter_Configuration selectParameter(String version){
		Parameter_Configuration T=new Parameter_Configuration();
		String sql="SELECT * FROM qy_parameter_configuration";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				if(version.equals("1")){
					T.setId(rs.getInt("id"));
					T.setMaintainCycle(rs.getString("maintain_cycle"));
					T.setCycle_ahead_days(rs.getInt("cycle_ahead_days"));
					T.setStore_ahead_days(rs.getInt("store_ahead_days"));
					T.setOut_ahead_days(rs.getInt("out_ahead_days"));
					T.setAlarm_cycle(rs.getString("alarm_cycle"));
				}else if(version.equals("2")||version.equals("3")||version.equals("4")){
					T.setId(rs.getInt("id"));
					T.setMaintainCycle(rs.getString("maintain_cycle"));
					T.setCycle_ahead_days(rs.getInt("cycle_ahead_days"));
					T.setStore_ahead_days(rs.getInt("store_ahead_days"));
					T.setOut_ahead_days(rs.getInt("out_ahead_days"));
					//T.setPrice_difference(rs.getInt("price_difference"));
					T.setAlarm_cycle(rs.getString("alarm_cycle"));
					T.setAlarm_ahead_days(rs.getInt("alarm_ahead_days"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt,rs);
		}
		return T;
	}
}
