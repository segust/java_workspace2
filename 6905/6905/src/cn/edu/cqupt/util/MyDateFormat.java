package cn.edu.cqupt.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyDateFormat {
	/**
	 * 将util.date转为 sql.date
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp changeToSqlDate(java.util.Date date) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String stringDate = sdf.format(date);
			try {
				date = sdf.parse(stringDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Timestamp time = new Timestamp(date.getTime());
			return time;
		} else {
			// 2015.06.12增加非空处理
			return null;
		}
	}

	/**
	 * 将sql.date转换成 java.date
	 * 
	 * @param timestamp
	 * @return
	 * @author liangYiHuai
	 */
	public static java.util.Date changeToDate(java.sql.Timestamp timestamp) {
		if (timestamp != null) {
			Calendar calendar = Calendar.getInstance();

			calendar.setTimeInMillis(timestamp.getTime());

			return calendar.getTime();
		} else {
			return null;
		}
	}

	public static java.util.Date changeStringToDate(String dateStr) {
		if (dateStr != null && !"null".equals(dateStr) && !"".equals(dateStr)) {
			// 去掉时分秒，否则报错
			// by lynn 2015.06.18
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = new java.util.Date();
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		} else {
			return null;
		}

	}

	/**
	 * 带时分秒的字符串转为Date
	 * 
	 * @param dateStr
	 * @return
	 * @author lhs
	 */
	public static java.util.Date changeLongStringToDate(String dateStr) {
		if (dateStr != null && !"".equals(dateStr) && !"null".equals(dateStr)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = new java.util.Date();
			try {
				date = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		} else {
			return null;
		}

	}

	/**
	 * 将date类型转化为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String changeDateToString(java.util.Date date) {
		if (date != null) {
			String dateStr = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dateStr = sdf.format(date);
			return dateStr;
		} else {
			return null;
		}
	}

	/**
	 * 将date类型转化为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String changeDateToFileString(java.util.Date date) {
		if (date != null) {
			String dateStr = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			dateStr = sdf.format(date);
			return dateStr;
		} else {
			return null;
		}
	}

	/**
	 * 将date类型转化为带时分秒字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String changeDateToLongString(java.util.Date date) {
		if (date != null) {
			String dateStr = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateStr = sdf.format(date);
			return dateStr;
		} else {
			return null;
		}
	}

	public static String changeDateToTimeStampString(java.util.Date date) {
		if (date != null) {
			String dateStr = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			dateStr = sdf.format(date);
			return dateStr;
		} else {
			return null;
		}
	}

	public static java.util.Date changeToJavaDate(java.sql.Date date) {
		if (date != null) {
			java.util.Date javaDate = new java.util.Date(date.getTime());
			return javaDate;
		} else {
			return null;
		}
	}

	/**
	 * 计算两个date之间相差的毫秒数
	 * 
	 * @param stroe
	 *            原有的date
	 * @param now
	 *            当前的date
	 */
	public static long javaDateMills(java.util.Date store, java.util.Date now) {
		if (store == null || now == null)
			return -1;
		return now.getTime() - store.getTime();
	}

	/**
	 * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
	 * 
	 * @param value
	 * @return Sting
	 */
	public static String formatDoubleNumber(double value) {
		if (value != 0.00) {
			java.text.DecimalFormat df = new java.text.DecimalFormat(
					"########.00");
			return df.format(value);
		} else {
			return "0.00";
		}
	}

}
