package cn.edu.cqupt.service.storage_maintenanc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.InspectRecord;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.dao.InspectDAO;
import cn.edu.cqupt.dao.UserDAO;

public class InspectService {
	
	private  InspectDAO inspectDAO =new InspectDAO();
	
	public boolean AddInspectRecord(InspectRecord ir){
		Boolean flag = false;
		flag = inspectDAO.AddInspectRecord(ir);
		return flag;
	}
	public  List<Map<String,String>> inspectService(Map<String,String> condition)
	{
		return inspectDAO.inspectOperate(condition);		
	}
	
	public  Boolean recordService(Map<String,String> condition)
	{
		Boolean flag = false;
		flag = inspectDAO.inspectRecord(condition);
		return flag;
	}
	
	public  List<InspectRecord> getAllinspectRecord()
	{
		return inspectDAO.getAllinspectRecord();
	}
	
	public  long getSum_inspectQuery(Map<String,String> condition) {
		
		long sum_inspectQuery = inspectDAO.getSum_inspectQuery(condition);
		return sum_inspectQuery;
	}
	
	public  long getSum_inspect(Map<String,String> condition)
	{
		long sum = inspectDAO.getSum_inspect(condition);
		return sum;
	}
}
