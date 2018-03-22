package cn.edu.cqupt.service.storage_maintenanc;

import java.util.Date;
import java.util.List;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.Parameter_Configuration;
import cn.edu.cqupt.dao.LogDAO;
import cn.edu.cqupt.dao.ParameterDAO;

public class InspectAlarmService {

	private ParameterDAO paraDAO;
	private LogDAO logDAO;
	
	public boolean inspectAlarm(String version)
	{
		boolean flag = false;
		this.paraDAO = new ParameterDAO();
		this.logDAO = new LogDAO();
		Date now = new Date();
		int year = now.getYear() + 1900;//今年
		int day = now.getDate();//今天多少号
		int month = now.getMonth() + 1;//现在几月份
		String monthStr = month + "";
		if(monthStr.length() == 1)
			monthStr = "0" + monthStr;
		if(logDAO.queryInspectAlarmLog(year, monthStr))//如果这个月已经检查过了，就不提醒，也不执行下面的代码
		{
			//System.out.println("本月已经提醒过了。");
			flag = false;
		}
		else
		{
			java.util.Calendar cal = java.util.Calendar.getInstance();
			int maxDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);//这个月一共几天
			Parameter_Configuration paras = paraDAO.selectParameter(version);
			String inspect_alarm_cycle = paras.getAlarm_cycle();
			int aheaddays = paras.getAlarm_ahead_days();
			String numberStr = inspect_alarm_cycle.substring(0, inspect_alarm_cycle.length() - 1);//提醒周期的数量
			int number = Integer.parseInt(numberStr);
			if((month % number) == 0)//这个月底需要提醒
			{
				if(day > (maxDay - aheaddays))//到了该提醒的那几天
				{
					flag = true;
					Log log = new Log();
					log.setOperateType("检查提醒");
					log.setInspectPerson("");
					log.setMainTainType("");
					log.setOperateTime(now);
					log.setProductId(0);
					log.setRemark("检查提醒，时间："+ year + "-" + month + "-" + day);
					this.logDAO.saveOperateLog(log);
				}
				
			}
			else
				flag = false;
		}
		return flag;
	}
}
