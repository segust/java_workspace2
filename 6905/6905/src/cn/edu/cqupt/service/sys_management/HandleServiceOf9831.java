package cn.edu.cqupt.service.sys_management;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.dao.Common9831DAO;
import cn.edu.cqupt.db.DBConnection;

public class HandleServiceOf9831 {
	
	private Connection conn=null;
	private Common9831DAO common9831dao=null;

	public HandleServiceOf9831() throws SQLException{
		this.conn = DBConnection.getConn();
		common9831dao=new Common9831DAO(this.conn);	
	}
	
	/**
	 * 查询总条数
	 * 方法太低级，需采用count
	 * */
	public int selectSum(HashMap<String, Object> key){
		int sum=0;
		sum=common9831dao.SearchOf9831Sum(key);
		return sum;
	}
	
	/**
	 * 查询9831库
	 * */
	public ArrayList<Common9831> select9831(HashMap<String, Object> key){
		ArrayList<Common9831> T=new ArrayList<Common9831>();
		T=common9831dao.SearchOf9831(key);
		return T;
	}
	/**
	 * 查询9831库
	 * */
	public ArrayList<Common9831> selectAll9831(HashMap<String, Object> key){
		ArrayList<Common9831> T=new ArrayList<Common9831>();
		T=common9831dao.SearchOf9831(key);
		return T;
	}
	/**
	 * 查询9831库的总条数
	 * */
	public int SearchOf9831Sum(HashMap<String, Object> key){
		int sum=0;
		sum=common9831dao.SearchOf9831Sum(key);
		return sum;
	}
	/**
	 * 根据id获取9831库记录
	 * */
	public Common9831 select9831ById(String id){
		Common9831 common9831=common9831dao.Get9831ById(id);
		return common9831;
	}
	
	/**
	 * 编辑一条9831库记录
	 * */
	public boolean edit9831(Common9831 common9831){
		boolean flag=false;
		flag=common9831dao.UpdateOf9831(common9831);
		return flag;
	}
	
	/**
	 * 新增9831记录
	 * 单条或多条都可以
	 * */
	public boolean add9831(ArrayList<Common9831> dyadicArray){
		boolean flag=false;
		flag=common9831dao.SavaOf9831(dyadicArray);
		return flag;
	}
	
	/**
	 * 新增qy_unit记录
	 * 单条或多条都可以
	 * */
	public boolean addUnit(ArrayList<Common9831> dyadicArray, String FKPMNM){
		boolean flag=false;
		flag=common9831dao.SavaOfUnit(dyadicArray, FKPMNM);
		return flag;
	}
	
	/**
	 * 根据删除9831一条记录
	 * */
	public boolean delete9831(String id){
		boolean flag=false;
		flag=common9831dao.DelOf9831(id);
		return flag;
	}
	
	/**
	 * 批量导入基础数据库
	 * */
	public boolean intoBaseData(List<String[]> T,String ownedUnit){
		boolean flag=false;
		flag=common9831dao.intoBaseData(T,ownedUnit);
		return flag;
	}
	
	/**
	 * 清空9831库
	 * */
	public boolean deleteAll9831(){
		boolean flag=false;
		flag=common9831dao.DeleteAll();
		return flag;
	}
}