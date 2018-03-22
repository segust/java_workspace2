package cn.edu.cqupt.service.qualification_management;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import cn.edu.cqupt.beans.Qualify;
import cn.edu.cqupt.dao.QualifyDAO;
import cn.edu.cqupt.db.DBConnection;

public class QualifyService {
	private Connection conn=null;
	private QualifyDAO qualifyDAO=null;
	private ArrayList<Qualify> qualifyList=null;
	
	public boolean addQualify(Qualify qualify){
		boolean flag=false;
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			flag=qualifyDAO.addQualify(qualify);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean deleteQualify(Long qualifyId){
		boolean flag=false;
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			flag=qualifyDAO.deleteQualify(qualifyId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean updateQualify(Long qualifyId, String qualifyType, String qualifyPath,String qualifyTitle){
		boolean flag=false;
		try { 
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			flag=qualifyDAO.updateQualify(qualifyId, qualifyType, qualifyPath,qualifyTitle);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	public ArrayList<Qualify> searchQualifyByPage(int curPageNum,int pageSize,String ownedUnit){
		ArrayList<Qualify> allQualifyList=new ArrayList<Qualify>();
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			allQualifyList=qualifyDAO.searchQualifyByPage(curPageNum,pageSize,ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allQualifyList;
	}
	
	public long getQualifySum(String ownedUnit){
		long qualifySum=0;
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			qualifySum=qualifyDAO.getQualifySum(ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}
	
	public ArrayList<Qualify> getTitleQualifyByPage(String searchStr,int curPageNum,int pageSize,String ownedUnit){
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			qualifyList=qualifyDAO.getTitleQualifyByPage(searchStr,curPageNum,pageSize,ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifyList;
	}
	
	public long getTitleQualifySum(String searchStr,String ownedUnit){
		long qualifySum=0;
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			qualifySum=qualifyDAO.getTitleQualifySum(searchStr,ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}
	
	public ArrayList<Qualify> getTypeQualifyByPage(String qualifyType,int curPageNum,int pageSize,String ownedUnit){
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			qualifyList=qualifyDAO.getTypeQualifyByPage(qualifyType,curPageNum,pageSize,ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifyList;
	}
	
	public long getTypeQualifySum(String searchType,String ownedUnit){
		long qualifySum=0;
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			qualifySum=qualifyDAO.getTypeQualifySum(searchType,ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}
	
	
	public ArrayList<Qualify> getPartQualifyByPage(String searchStr, String searchType,String year,String searchAttr,int curPageNum,int pageSize,String ownedUnit){
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			if(!searchStr.equals("请输入查询的文件名"))
				searchStr=".*"+searchStr+".*";
			else
				searchStr=".*.*";
			if(!searchType.equals("所有类型"))
				searchType=".*"+searchType+".*";
			else
				searchType=".*.*";
			if(!year.equals("请输入查询的年份"))
				year=".*"+year+".*";
			else
				year=".*.*";
			if(!searchAttr.equals("所有"))
				searchAttr=".*"+searchAttr+".*";
			else
				searchAttr=".*.*";
			qualifyList=qualifyDAO.getPartQualifyByPage(searchStr, searchType,year,searchAttr,curPageNum,pageSize,ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifyList;
	}
	
	public long getPartQualifySum(String searchStr,String searchType,String year,String searchAttr,String ownedUnit){
		long qualifySum=0;
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			if(!searchStr.equals("请输入查询的文件名"))
				searchStr=".*"+searchStr+".*";
			else
				searchStr=".*.*";
			if(!searchType.equals("所有类型"))
				searchType=".*"+searchType+".*";
			else
				searchType=".*.*";
			if(!year.equals("请输入查询的年份"))
				year=".*"+year+".*";
			else
				year=".*.*";
			if(!searchAttr.equals("所有"))
				searchAttr=".*"+searchAttr+".*";
			else
				searchAttr=".*.*";
			qualifySum=qualifyDAO.getPartQualifySum(searchStr,searchType,year,searchAttr,ownedUnit);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifySum;
	}
	
	public ArrayList<Qualify> getAllQualify(){
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			qualifyList=qualifyDAO.getAllQualify();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qualifyList;
	}
	
	public boolean deleteFile(String qualifyPath){
		boolean flag=false;
		File file=new File(qualifyPath);
		flag=file.delete();
		return flag;
	}
	
	public Qualify getCurQualifyById(long qualifyId) {
		Qualify curQualify=new Qualify();
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			curQualify=qualifyDAO.getCurQualifyById(qualifyId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return curQualify;
	}
	
	public String getQualifyPathById(long qualifyId) {
		String qualifyPath="";
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			qualifyPath=qualifyDAO.getQualifyPathById(qualifyId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return qualifyPath;
	}
	
	/*public boolean repeatQualify(String qualifyTitle,String qualifyType,String ownedUnit) {
		boolean repeatFlag=false;
		try {
			conn=DBConnection.getConn();
			qualifyDAO=new QualifyDAO(conn);
			repeatFlag=qualifyDAO.repeatQualify(qualifyTitle, qualifyType, ownedUnit);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return repeatFlag;
	}*/
}