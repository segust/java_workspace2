package cn.edu.cqupt.util;

import java.util.Date;

public class RestTime {

	/**
	 * 根据存储期限（String类型）和新入库时间（Date类型）来获取按天计的剩余存储时间
	 * @param storageTime存储期限
	 * @param operateTime入库时间
	 * 因为存储期限storageTime在数据库中的存储格式是“XXX天”、“XXX年”、“XXXd”、“XXXY”，所以要解析storageTime字符串
	 * @return
	 */
	public static String CountRestStorageTimeInDays(String storageTime, Date operateTime)
	{
		Date now = new Date();//当前时间
		int numberInDay = 0;//按天数算存储期限
		int restStorageTime = 0;//剩余维护时间
		if(storageTime.endsWith("年"))
		{
			
			String numberStr = storageTime.substring(0, storageTime.length() - 1);//存储期限的数量
			if(numberStr.equals("一"))
				numberInDay = 365;
			else
				numberInDay = 365*Integer.parseInt(numberStr);
		}
		else if(storageTime.endsWith("个月"))
		{
			String numberStr = storageTime.substring(0, storageTime.length() - 2);//存储期限的数量
			try{
				numberInDay = 30*Integer.parseInt(numberStr);
			}catch(NumberFormatException e){
				if(numberStr.equals("一"))
					numberInDay = 30;
				else if(numberStr.equals("三"))
					numberInDay = 90;
				else if(numberStr.equals("六"))
					numberInDay = 180;
				else{
					return "存储期限有误！";
				}
			}
		}else{
			String unitStr = storageTime.substring(storageTime.length() - 1, storageTime.length());//存储期限的单位
			String numberStr = storageTime.substring(0, storageTime.length() - 1);//存储期限的数量
			int number = Integer.parseInt(numberStr);//数字，即XXX
			if(unitStr.equalsIgnoreCase("d") || unitStr.equals("天"))
				numberInDay = number;
			else if(unitStr.equalsIgnoreCase("m") || unitStr.equals("月"))
				numberInDay = number*30;
			else if(unitStr.equals("y") || unitStr.equals("年"))
				numberInDay = number*365;
			else{
				return "无效字段！";
				//throw new RuntimeException("数据库中存储期限无法有效读取。");
			}
		}
		
		long storageTimeInMills = MyDateFormat.javaDateMills(operateTime, now);
		int storageTimeInDays = (int)(storageTimeInMills/(1000*60*60*24));
		restStorageTime = numberInDay - storageTimeInDays;
		if(restStorageTime < 0)//如果剩余存储时间算出来小于0，则改为0
			restStorageTime = 0;
		return restStorageTime+"天";
	}
	
	/**
	 * 根据维护周期（String类型）和上次维护时间（Date类型）来获取按天计的剩余存储时间
	 * @param latestMaintainTime上次维护时间
	 * @param maintainCycle维护周期
	 * 因为maintainCycle维护周期在数据库中的存储格式是“XXX天”、“XXX年”、“XXXd”、“XXXY”，所以要解析storageTime字符串
	 * @return
	 */
	public static int countRestMaintainTimeInDays(Date latestMaintainTime, String maintainCycle)
	{
		Date now = new Date();//当前时间
		int restMaintainTime = 0;//剩余维护天数
		int numberInDayMaintain = 0;//按天数算维护周期
		if(maintainCycle.endsWith("年"))
		{
			
			String numberStr = maintainCycle.substring(0, maintainCycle.length() - 1);//存储期限的数量
			if(numberStr.equals("一"))
				numberInDayMaintain = 365;
			else
				numberInDayMaintain = 365*Integer.parseInt(numberStr);
		}
		else if(maintainCycle.endsWith("个月"))
		{
			String numberStr = maintainCycle.substring(0, maintainCycle.length() - 2);//存储期限的数量
			try{
				numberInDayMaintain = 30*Integer.parseInt(numberStr);
			}catch(NumberFormatException e){
				if(numberStr.equals("一"))
					numberInDayMaintain = 30;
				else if(numberStr.equals("三"))
					numberInDayMaintain = 90;
				else if(numberStr.equals("六"))
					numberInDayMaintain = 180;
				else{
					return -1;
				}
			}
		}else{
			String unitStr = maintainCycle.substring(maintainCycle.length() - 1, maintainCycle.length());//存储期限的单位
			String numberStr = maintainCycle.substring(0, maintainCycle.length() - 1);//存储期限的数量
			int number = Integer.parseInt(numberStr);//数字，即XXX
			if(unitStr.equalsIgnoreCase("d") || unitStr.equals("天"))
				numberInDayMaintain = number;
			else if(unitStr.equalsIgnoreCase("m") || unitStr.equals("月"))
				numberInDayMaintain = number*30;
			else if(unitStr.equals("y") || unitStr.equals("年"))
				numberInDayMaintain = number*365;
			else{
				return -1;
				//throw new RuntimeException("数据库中存储期限无法有效读取。");
			}
		}
		long spendMaintainTimeInMills = MyDateFormat.javaDateMills(latestMaintainTime, now);
		int spendMaintainTimeInDays = (int)(spendMaintainTimeInMills/(1000*60*60*24));//离上次维护已经过了几天
		restMaintainTime = numberInDayMaintain - spendMaintainTimeInDays;
		if(restMaintainTime < 0)
			restMaintainTime = 0;
		return restMaintainTime;
	}
	
	/**
	 * 根据维护周期（String类型）和上次维护时间（Date类型）来获取按天计的剩余存储时间
	 * @param latestMaintainTime
	 * @param maintainCycle 这个字符串应该严格注意一下格式：x天、x个月、x年
	 * @return
	 * @author LiangYH
	 */
	public static int calculateRestMaintainTime(Date latestMaintainTime, String maintainCycle){
		System.out.println("mainatainCycle = "+maintainCycle);
		
		Date now = new Date();//当前时间
		int restMaintainTime = 0;//剩余维护天数
		int numberInDayMaintain = 0;//按天数算维护周期
		String unitStrMaintain = maintainCycle.substring(maintainCycle.length() - 1, maintainCycle.length());
//		String numberStrMaintain = maintainCycle.substring(0, maintainCycle.length() - 1);
		String numberStrMaintain = null;
		if(unitStrMaintain.equalsIgnoreCase("d") || unitStrMaintain.equals("天")){
			numberStrMaintain = maintainCycle.substring(0, maintainCycle.length() - 1);
			numberInDayMaintain = Integer.parseInt(numberStrMaintain);
		}
		else if(unitStrMaintain.equalsIgnoreCase("m") || unitStrMaintain.equals("月")){
			numberStrMaintain = maintainCycle.substring(0, maintainCycle.length()-1);
			if("一".equals(numberStrMaintain)){
				numberStrMaintain = "1";
			}else if("三".equals(numberStrMaintain)){
				numberStrMaintain = "3";
			}else if("六".equals(numberStrMaintain)){
				numberStrMaintain = "6";
			}
			
			System.out.println("number = "+numberStrMaintain);
			numberInDayMaintain = Integer.parseInt(numberStrMaintain)*30;
		}
		else if(unitStrMaintain.equals("y") || unitStrMaintain.equals("年")){
			numberStrMaintain = maintainCycle.substring(0, maintainCycle.length() - 1);
			numberInDayMaintain = Integer.parseInt(numberStrMaintain)*365;
		}
		else{
			return 0;
			//throw new RuntimeException("数据库中维护周期无法有效读取。");
		}
		
		long spendMaintainTimeInMills = MyDateFormat.javaDateMills(latestMaintainTime, now);
		int spendMaintainTimeInDays = (int)(spendMaintainTimeInMills/(1000*60*60*24));//离上次维护已经过了几天
		restMaintainTime = numberInDayMaintain - spendMaintainTimeInDays;
		if(restMaintainTime < 0)
			restMaintainTime = 0;
		return restMaintainTime;
	}
}
