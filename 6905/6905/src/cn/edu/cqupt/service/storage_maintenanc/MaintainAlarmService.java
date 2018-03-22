package cn.edu.cqupt.service.storage_maintenanc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.util.RestTime;

public class MaintainAlarmService {

	private ProductDAO dao;
	public MaintainAlarmService()
	{
		this.dao = new ProductDAO();
	}
	public int autoAlarm(int remind)
	{
		int count = 0;
		List<String> condition = new ArrayList<String>();
		condition.add("");
		condition.add("");
		condition.add("");
		condition.add("");
		condition.add("");
		condition.add("");
		condition.add("");
		ArrayList<HashMap<String, Object>> maintainList = new ArrayList<HashMap<String, Object>>();
		maintainList = dao.selectMaintainDetail(condition);
		for(int i = 0; i < maintainList.size(); i++)
		{
			String maintainCycle = (String) maintainList.get(i).get("maintainCycle");
			Date latestMaintainTime = (Date) maintainList.get(i).get("latestMaintainTime");
			int restMaintainTime = RestTime.countRestMaintainTimeInDays(latestMaintainTime, maintainCycle);
			if(remind >= restMaintainTime)
				count++;
		}
		return count;
	}
}
