package cn.edu.cqupt.service.warehouse_management;

import java.util.ArrayList;
import java.util.Date;

import cn.edu.cqupt.beans.Page;
import cn.edu.cqupt.beans.Temperature;
import cn.edu.cqupt.dao.TemperatureDAO;

public class TemperatureService {
	private TemperatureDAO tDao = new TemperatureDAO();

	// 添加temperature记录
	public boolean addTemperature(Temperature temperature) {
		return tDao.addTemperature(temperature);
	}

	// 通过id查找一条记录
	public Temperature getOneTemperatureById(Long temperatureId) {
		return tDao.getOneTemperatureById(temperatureId);
	}

	// 通过id查找添加一条记录的系统时间
	public String getOperateDate(Long temperatureId) {
		return tDao.getOperateDate(temperatureId);
	}

	// 修改单个temperature记录
	public boolean updateTemperature(Temperature temerature) {
		return tDao.updateTemperature(temerature);
	}

	// 删除单个temperature记录
	public boolean deleteOneTemperature(Long temperatureId) {
		return tDao.deleteTemperature(temperatureId);
	}

	// 分页全部查询
	public Page getPageTemperature(String num, String size) {
		int currentPageNum = 1;
		if (num != null && !num.equals("")) {
			currentPageNum = Integer.parseInt(num);
		}
		int totalRecords = tDao.getTotalRecords();
		int pageSize = 10;
		if (num != null && !num.equals("")) {
			pageSize = Integer.parseInt(size);
		}
		Page page = new Page(currentPageNum, totalRecords, pageSize);
		ArrayList<Temperature> records = tDao.findPageRecords(page
				.getStartIndex(), page.getPageSize());
		page.setTempatureRecords(records);
		return page;
	}

	// 条件查询不分页，返回全部条件查询的数据
	public ArrayList<Temperature> getQualifyTemperature(Date startDate,
			Date endDate) {
		ArrayList<Temperature> temperatureList = tDao.getQualifyTemperature(
				startDate, endDate);
		return temperatureList;
	}

	// 全部查询不分页，返回全部查询的数据
	public ArrayList<Temperature> getAllTemperature() {
		ArrayList<Temperature> temperatureList = tDao.getAllTemperature();
		return temperatureList;
	}

	// 分页条件查询
	/*
	 * public Page getQualifyTemperature(Date startDate, Date endDate, Double
	 * startTemp, Double endTemp, String num,String size) { int currentPageNum =
	 * 1; if(num!=null&&!num.equals("")){ currentPageNum =
	 * Integer.parseInt(num); } int pageSize = 10;
	 * if(num!=null&&!num.equals("")){ pageSize = Integer.parseInt(size); }
	 * ArrayList<Temperature> records = null; int totalRecords =
	 * tDao.getTotalQualifyRecords(startDate, endDate, startTemp, endTemp); Page
	 * page = new Page(currentPageNum,totalRecords,pageSize); records =
	 * tDao.getQualifyTemperature(startDate, endDate, startTemp, endTemp,
	 * page.getStartIndex(), page.getPageSize());
	 * page.setTempatureRecords(records); return page; }
	 */

	// 分页的 条件查询
	public Page getQualifyTemperature(Date startDate, Date endDate, Double startTemp, Double endTemp,String num,
			String size) {
		int currentPageNum = 1;
		if (num != null && !num.equals("")) {
			currentPageNum = Integer.parseInt(num);
		}
		int pageSize = 10;
		if (num != null && !num.equals("")) {
			pageSize = Integer.parseInt(size);
		}
		ArrayList<Temperature> records = null;
		int totalRecords = tDao.getTotalQualifyRecords(startDate, endDate,startTemp,endTemp);
		Page page = new Page(currentPageNum, totalRecords, pageSize);
		records = tDao.getQualifyTemperature(startDate, endDate, page
				.getStartIndex(), page.getPageSize());
		page.setTempatureRecords(records);
		return page;
	}

//	// 判断是否重复提交
//	public boolean repeatTemperature(Temperature curTemperature) {
//		boolean repeatFlag = false;
//		repeatFlag = tDao.repeatTemperature(curTemperature.getTemperature(),
//				curTemperature.getOperateDate(), curTemperature.getPosition());
//		return repeatFlag;
//	}

	// 在更新时将原记录插入qy_old_temperature
	public boolean insertOldRecord(Long temperatureId) {
		boolean flag = false;
		flag = tDao.insertOldRecord(temperatureId);
		return flag;
	}

	// 根据记录id获取原始记录
	public ArrayList<Object> searchOldRecord(Long recordId, String recordType) {
		ArrayList<Object> oldRecordList = new ArrayList<Object>();
		oldRecordList = tDao.searchOldRecord(recordId, recordType);
		return oldRecordList;
	}
	
	// 获取满足条件总记录数
	public int getTotalQualifyRecords(Date startDate, Date endDate,
			Double startTemp, Double endTemp){
		int count=0;
		count=tDao.getTotalQualifyRecords(startDate, endDate, startTemp, endTemp);
		return  count;
	}
	
	// 获取总记录数
	public int getTotalRecords() {
		int count=0;
		count=tDao.getTotalRecords();
		return count;
	}
	
	/*
	 * public static void main(String[] args) { ArrayList<String>
	 * oldRecordList=new TemperatureService().searchOldRecord((long)114,
	 * "temperature"); for (String string : oldRecordList) {
	 * System.out.println(string); } }
	 */
}
