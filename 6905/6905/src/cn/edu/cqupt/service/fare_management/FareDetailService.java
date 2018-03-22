package cn.edu.cqupt.service.fare_management;


import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.cqupt.beans.FareDetail;
import cn.edu.cqupt.dao.FareDetailDAO;

public class FareDetailService {
	private static FareDetailDAO fareDetailDao = null;
	
	static {
		fareDetailDao = new FareDetailDAO();
	}
	/**
	 * 增加一条faredetail记录
	 */
	public boolean addFareDetail(FareDetail fareDetail) {
		boolean flag = false;
		flag = fareDetailDao.addFareDetail(fareDetail);
		return flag;
	}
	/**
	 * 通过fareId 该项纪录的明细
	 * @param fareId
	 * @return
	 */
	public ArrayList<FareDetail> getAllFareDetail(long fareId) { 
		return fareDetailDao.getAllFareDetail(fareId);
	}
	public boolean deleteByfareId(long fareId) {
		boolean flag = false;
		flag = fareDetailDao.deleteByfareId(fareId); 
		return flag;
	}

	/**
	 * 统计经费 通过fareType 多表联合查询
	 * @param fareType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public HashMap<String, Double> getStatisticsDetail(String fareType,String startDate,String endDate) { 
		return fareDetailDao.getStatisticsDetail(fareType,startDate,endDate);
	}  
}
