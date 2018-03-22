package cn.edu.cqupt.service.fare_management;

import java.util.ArrayList;

import cn.edu.cqupt.beans.Fare;
import cn.edu.cqupt.dao.FareDAO;

/**
 * 经费管理
 * 
 * @author lsy&yg
 * 
 */
public class FareService {
	private static FareDAO fareDao = null;

	static {
		fareDao = new FareDAO();
	}

	/**
	 * 插入一条费用记录
	 * 
	 * @param
	 * @return flag
	 */
	public boolean addFare(Fare fare) {
		boolean flag = false;
		flag = fareDao.addFare(fare);
		return flag;
	}

	/**
	 * 通过Id删除一条经费
	 * 
	 * @param fareId
	 * @return
	 */
	public boolean deleteFare(long fareId) {
		return fareDao.deleteFare(fareId);
	}

	/**
	 * 通过Id更新一条经费
	 * 
	 * @param fare
	 * @param fareId
	 * @return
	 */
	public boolean updateFare(Fare fare) {
		return fareDao.updateFare(fare);
	}

	/**
	 * 通过Id查询一条经费
	 * 
	 * @return
	 */
	public Fare getOneFareById(int fareId) {
		return fareDao.getOneFareById(fareId);
	}

	/**
	 * 获取所有费用记录
	 * 
	 * @return allFareMap
	 */
	public ArrayList<Fare> getAllFare(int curPageNum, int pageSize) {
		return fareDao.getAllFare(curPageNum, pageSize);
	}

	/**
	 * 获取所有费用记录排序后的fareID 
	 * @return allFareMap
	 */
	public ArrayList<Integer> getAllOrder() {
		return fareDao.getAllOrder();
	}

	/**
	 * 根据时间段和费用类型获取经费记录
	 * 
	 * @return
	 */
	public ArrayList<Fare> getAllFareByDateAndFareType(String startDate,
			String endDate, String type, String storeCompany, int curPageNum,
			int pageSize) {
		return fareDao.getAllFareByDateAndFareType(startDate, endDate, type,
				storeCompany, curPageNum, pageSize);
	}
	/**
	 * 根据时间段和费用类型获取经费记录
	 * 没有带页数和每页的大小
	 * 只得到fareId
	 * @return
	 */
	public ArrayList<Long> getAllFareByDateAndFareType(String startDate,
			String endDate, String type, String storeCompany ) {
		return fareDao.getAllFareByDateAndFareType(startDate, endDate, type,
				storeCompany);
	}

	
	/**
	 * 获取经费条数
	 * 
	 * @return
	 */
	public long getFareSum() {
		return fareDao.getFareSum();
	}

	/**
	 * 获取查询条件下的经费数目
	 * 
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public long getCheckFareSum(String type, String startTime, String endTime,
			String storeCompany) {
		return fareDao.getCheckFareSum(type, startTime, endTime, storeCompany);
	}

	/**
	 * 通过几条id获取经费记录(用于导出经费)
	 * 
	 * @param idArray
	 * @return
	 */
	public ArrayList<Fare> getSomeFareById(ArrayList<Long> idArray) {
		return fareDao.getSomeFareById(idArray);
	}

	/**
	 * 获取最新插入的经费记录
	 * 
	 * @return
	 */
	public Fare getLastFare() {
		return fareDao.getLastFare();
	}

	/**
	 * 按选择条件统计记录的经费金额
	 * 
	 * @param startDate
	 * @param endDate
	 * @param type
	 * @return
	 */
	public double getSumByDateAndOneFaretype(String startDate, String endDate,
			String type) {
		return fareDao.getSumByDateAndOneFaretype(startDate, endDate, type);
	}

	/**
	 * 判断数据库中是否有相同的记录,用于导入
	 * 
	 * @param type
	 * @param amount
	 * @param storeCompany
	 * @param jdRoom
	 * @param operateDate
	 * @param remark
	 * @return
	 */
	public long judgeFare(String type, String amount, String storeCompany,
			String jdRoom, String operateDate, String remark) {
		return fareDao.judgeFare(type, amount, storeCompany, jdRoom,
				operateDate, remark);
	}
	
	/**
	 * 判断数据库中是否有相同的记录,用于导入
	 * @param fare
	 * @return
	 */
	public long judgeFare(String fareId,String storeCompany,String operateTime) {
		return fareDao.judgeFare(Long.parseLong(fareId),storeCompany,operateTime);
	}
	
	/**
	 * 操作时间不同，根据费用id和代储企业判断数据库中是否有记录
	 * @param fare
	 * @return
	 */
	public long judgeUpdateFare(Fare fare){
		return fareDao.judgeUpdateFare(fare);
	}
}
