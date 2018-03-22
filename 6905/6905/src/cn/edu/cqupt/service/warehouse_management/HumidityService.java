package cn.edu.cqupt.service.warehouse_management;

import java.util.ArrayList;
import java.util.Date;

import cn.edu.cqupt.beans.Humidity;
import cn.edu.cqupt.beans.Page;
import cn.edu.cqupt.dao.HumidityDAO;

public class HumidityService {
	private HumidityDAO hDao = new HumidityDAO();

	// 添加humidity记录
	public boolean addHumidity(Humidity humidity) {
		return hDao.addHumidity(humidity);
	}

	// 通过id查找一条记录
	public Humidity getOneHumidityById(Long humidityId) {
		return hDao.getOneHumidityById(humidityId);
	}

	// 通过id查找原始一条记录添加的系统时间
	public String getOperateDate(Long humidityId) {
		return hDao.getOperateDate(humidityId);
	}

	// 修改单个humidity记录
	public boolean updateHumidity(Humidity humidity) {
		return hDao.updateHumidity(humidity);
	}

	// 删除单个humidity记录
	public boolean deleteOneHumidity(Long humidityId) {
		return hDao.deleteHumidity(humidityId);
	}

	// 分页全部查询
	public Page getPageHumidity(String num, String size) {
		int currentPageNum = 1;
		if (num != null && !num.equals("")) {
			currentPageNum = Integer.parseInt(num);
		}
		int totalRecords = hDao.getTotalRecords();
		int pageSize = 10;
		if (num != null && !num.equals("")) {
			pageSize = Integer.parseInt(size);
		}
		Page page = new Page(currentPageNum, totalRecords, pageSize);
		ArrayList<Humidity> records = hDao.findPageRecords(
				page.getStartIndex(), page.getPageSize());
		page.setHumidityRecords(records);
		return page;
	}

	// 全部查询不分页，返回全部查询的数据
	public ArrayList<Humidity> getAllHumidity() {
		ArrayList<Humidity> humidityList = hDao.getAllHumidity();
		return humidityList;
	}

	// 分页条件查询
	public Page getQualifyHumidity(Date startDate, Date endDate, String num,
			String size) {
		int currentPageNum = 1;
		if (num != null && !num.equals("")) {
			currentPageNum = Integer.parseInt(num);
		}
		int pageSize = 10;
		if (num != null && !num.equals("")) {
			pageSize = Integer.parseInt(size);
		}
		ArrayList<Humidity> records = null;
		int totalRecords = hDao.getTotalQualifyRecords(startDate, endDate);
		Page page = new Page(currentPageNum, totalRecords, pageSize);
		records = hDao.getQualifyHumidity(startDate, endDate,
				page.getStartIndex(), page.getPageSize());
		page.setHumidityRecords(records);
		return page;
	}

	// 条件查询不分页，返回全部条件查询的数据
	public ArrayList<Humidity> getQualifyHumidity(Date startDate, Date endDate) {
		ArrayList<Humidity> humidityList = hDao.getQualifyHumidity(startDate,
				endDate);
		return humidityList;
	}

	// 判断是否重复提交
	// public boolean repeatHumidity(Humidity curHumidity) {
	// boolean repeatflag = false;
	// repeatflag = hDao.repeatHumidity(curHumidity.getHumidity(), curHumidity
	// .getOperateDate(), curHumidity.getPosition());
	// return repeatflag;
	// }

	// 在更新时将原记录插入qy_old_temperature
	public boolean insertOldRecord(Long humidityId) {
		boolean flag = false;
		flag = hDao.insertOldRecord(humidityId);
		return flag;
	}

	// 获取湿度表总记录数
	public int getTotalRecords() {
		return hDao.getTotalRecords();
	}

	// 获取满足条件的湿度表记录数
	public int getTotalQualifyRecords(Date startDate, Date endDate,
			Double startHumidity, Double endHumidity) {
		return hDao.getTotalQualifyRecords(startDate, endDate, startHumidity,
				endHumidity);
	}
}
