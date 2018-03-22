package cn.edu.cqupt.service.warehouse_management;

import java.util.ArrayList;

import cn.edu.cqupt.beans.RepairInfo;
import cn.edu.cqupt.dao.RepairDao;

public class RepairService {
	private RepairDao repairDao = new RepairDao();

	public boolean addRepair(RepairInfo repairInfo) {
		return repairDao.addRepairInfo(repairInfo);
	}

	public boolean updateRepa(RepairInfo repairInfo, long repairId) {
		return repairDao.updateRepair(repairInfo, repairId);
	}

	public boolean deleteDevice(long repairId) {
		return repairDao.deleteRepair(repairId);
	}

	public ArrayList<RepairInfo> searchRepairInfoByPage(int curPageNum, int pageSize, long deviceId) {
		return repairDao.searchRepairInfoByPage(curPageNum, pageSize, deviceId);
	}

	public long getRepairSumByOneDevice(long deviceId) {
		return repairDao.getRepairSumByOneDevice(deviceId);
	}
}
