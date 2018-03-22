package cn.edu.cqupt.service.storage_maintenanc;

import java.util.Date;

import cn.edu.cqupt.dao.AccountDAO;

public class UpdateReturnAlarmService {

	private AccountDAO dao;
	private Date now;
	public UpdateReturnAlarmService(){
		this.dao = new AccountDAO();
		this.now = new Date();
	}
	
	public int getUpdateReturnNumber()
	{
		return dao.getUpdateReturnAlarmNumber(getOutAlarmDays(), now);
	}
	
	private int getOutAlarmDays()
	{
		return dao.getOutAlarmDays();
	}
	
	public static void main(String[] args){
		UpdateReturnAlarmService alarmService = new UpdateReturnAlarmService();
		System.out.println(alarmService.getUpdateReturnNumber());
	}
}
