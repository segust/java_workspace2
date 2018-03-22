package cn.edu.cqupt.controller.warehouse_management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Humidity;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.Page;
import cn.edu.cqupt.beans.Temperature;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.warehouse_management.HumidityService;
import cn.edu.cqupt.service.warehouse_management.TemperatureService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;
import cn.edu.cqupt.util.MyDateFormat;

public class TemperatureServlet extends HttpServlet {
	private static final long serialVersionUID = -3126795147025001495L;

	@Override
	public void init() throws ServletException {
		// 根据是否有传感器进行初始化
		String sensor = this.getServletContext().getInitParameter("sensor");
		if (sensor.equals("1")) {
			String position = this.getServletContext().getInitParameter(
					"warehouseLocation");
			String timeIntervalStr=this.getServletContext().getInitParameter("timeInterval");
			long timeInterval=(long) (Double.parseDouble(timeIntervalStr)*3600000);
			TempHumiRecord thr = new TempHumiRecord(timeInterval, position);
			//利用线程记录
			Thread t = new Thread(thr);
			t.start();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		// version==1表示是企业版
		if (version.equals("1")) {
			if (CurrentUser.isWarehouseManage(request)) {
				String operate = request.getParameter("operate");
				// 添加温度记录
				if ("addTemperature".equals(operate)) {
					// addTemperature();
					return;
				}
				// 得到当前页所有的温度记录
				else if ("getAllTemperature".equals(operate)) {
					getAllTemperatureByPage(request, response);
					return;
				}
				// 得到全部的温度记录
				else if ("getAllJSONTemperature".equals(operate)) {
					getAllTemperatue(response);
					return;
				}
				// 条件查询当前页温度记录
				else if ("getQualifyTemperature".equals(operate)) {
					getQualifyTemperature(request, response);
					return;
				}
				// 条件查询全部的温度记录
				else if ("getAllQualifyTemperature".equals(operate)) {
					getAllQualifyTemperature(request, response);
					return;
				}
				// 删除单个温度记录
				else if ("deleteOneTemperature".equals(operate)) {
					// deleteOneTemperature(request, response);
					return;
				}
				// 修改单个温度记录
				else if ("updateTemperature".equals(operate)) {
					// updateTemperature(request, response);
					return;
				}
				// 查询修改后的原始记录
				else if ("searchOldRecord".equals(operate)) {
					// searchOldRecord(request, response);
					return;
				}
				// 条件查询满足条件的记录数
				else if ("getTotalQualifyRecords".equals(operate)) {
					getTotalQualifyRecords(request, response);
					return;
				}
				// 以下代码是用户首次访问时执行。
				TemperatureService temperatureService = new TemperatureService();
				String pageSize = request.getParameter("pageSize");
				String startTemp = request.getParameter("startTemp");
				String endTemp = request.getParameter("endTemp");
				Page page = temperatureService
						.getPageTemperature("1", pageSize);
				page.setUrl("TemperatureServlet?operate=getAllTemperature");
				request.setAttribute("page", page);
				request.setAttribute("startTemp", startTemp);
				request.setAttribute("endTemp", endTemp);
				request.getRequestDispatcher(
						"jsp/qy/warehouse_management/temperature/getAllTemperature.jsp")
						.forward(request, response);
			} else {// 当前用户不是合格用户
				GetError.limitVisit(request, response);
			}
		} else {// 表示不是企业版
			response.getWriter().write("您使用的不是企业版");
		}

	}

	// 修改单个温度记录
	// private void updateTemperature(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// TemperatureService temperatureService = new TemperatureService();
	// // 获取参数
	// Long temperatureId = Long.parseLong(request
	// .getParameter("temperatureId"));
	// Double temperature = Double.parseDouble(request
	// .getParameter("temperature"));
	// String curRecordDate = request.getParameter("curRecordDate");
	// String operateDate = temperatureService.getOperateDate(temperatureId);
	// operateDate += ";";
	// operateDate += MyDateFormat.changeDateToLongString(new Date());
	// String position = request.getParameter("position");
	// // 填充模型
	// Temperature tempObj = new Temperature();
	// tempObj.setTemperatureId(temperatureId);
	// tempObj.setTemperature(temperature);
	// tempObj.setCurRecordDate(curRecordDate);
	// tempObj.setPosition(position);
	// // 执行修改
	// // 在更新时先将原记录插入qy_old_temperature
	// if (temperatureService.insertOldRecord(temperatureId)) {
	// // 再修改qy_temperature
	// boolean flag = temperatureService.updateTemperature(tempObj);
	// // 页面跳转
	// if (flag) {
	// request.setAttribute("updateFlag", 1);
	// // 记录用户修改温度操作
	// Log log = new Log();
	// log.setUserName((String) request.getSession().getAttribute(
	// "username"));
	// log.setOperateTime(new Date()); // 记录当前用户操作的时间
	// log.setOperateType("修改温度,温度编号:" + temperatureId);
	// UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	// getAllTemperatureByPage(request, response);
	// } else {
	// request.setAttribute("updateFlag", 0);
	// }
	// } else {
	// request.setAttribute("updateFlag", 0);
	// }
	// }

	// 删除单个温度记录
	// private void deleteOneTemperature(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// TemperatureService temperatureService = new TemperatureService();
	// // 获取参数
	// Long temperatureId = Long.parseLong(request
	// .getParameter("temperatureId"));
	// // 执行操作
	// boolean flag = temperatureService.deleteOneTemperature(temperatureId);
	// if (flag) {
	// request.setAttribute("deleteFlag", 1);
	// // 记录用户删除温度的操作
	// Log log = new Log();
	// log.setUserName((String) request.getSession().getAttribute(
	// "username"));
	// log.setOperateTime(new Date()); // 记录当前用户操作的时间
	// log.setOperateType("删除温度，温度编号为:" + temperatureId);
	// UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	// getAllTemperatureByPage(request, response);
	// } else {
	// request.setAttribute("deleteFlag", 0);
	// getAllTemperatureByPage(request, response);
	// }
	// }

	// 条件查询分页的温度记录
	private void getQualifyTemperature(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		TemperatureService temperatureService = new TemperatureService();
		// 获取参数
		String strStartDate = request.getParameter("startDate");
		Date startDate = null;
		if (strStartDate != null && !strStartDate.equals(""))
			startDate = MyDateFormat.changeStringToDate(strStartDate);

		String strEndDate = request.getParameter("endDate");
		Date endDate = null;
		if (strEndDate != null && !strEndDate.equals(""))
			endDate = MyDateFormat.changeStringToDate(strEndDate);

		String strStartTemp = request.getParameter("startTemp");
		String strEndTemp = request.getParameter("endTemp");
		Double startTemp = null;
		Double endTemp = null;
		if (!strStartTemp.trim().equals("") && strStartTemp != null)
			startTemp = Double.parseDouble(strStartTemp);
		if (!strEndTemp.trim().equals("") && strEndTemp != null)
			endTemp = Double.parseDouble(strEndTemp);

		// //获取
		// int allCount=temperatureService.getTotalRecords();
		// int matchCount=temperatureService.getTotalQualifyRecords(startDate,
		// endDate, startTemp, endTemp);
		// int unmatchCount=allCount-matchCount;

		String num = request.getParameter("num");// 页面传过来的“第几页”
		String pageSize = request.getParameter("pageSize");
		// 执行操作，封装到域对象
		Page page = temperatureService.getQualifyTemperature(startDate,
				endDate, startTemp, endTemp, num, pageSize);
		// Page page = temperatureService.getQualifyTemperature(startDate,
		// endDate,strStartTemp,strEndTemp, num, pageSize);
		page.setUrl("TemperatureServlet?operate=getQualifyTemperature");
		request.setAttribute("page", page);
		request.setAttribute("startDate", strStartDate);
		request.setAttribute("endDate", strEndDate);
		request.setAttribute("startTemp", strStartTemp);
		request.setAttribute("endTemp", strEndTemp);
		// 页面跳转
		request.getRequestDispatcher(
				"jsp/qy/warehouse_management/temperature/getAllTemperature.jsp")
				.forward(request, response);
		// 执行操作，封装到域对象
	}

	// 条件查询全部的温度记录
	private void getAllQualifyTemperature(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		TemperatureService temperatureService = new TemperatureService();
		// 获取参数
		String strStartDate = request.getParameter("startDate");
		Date startDate = null;
		if (strStartDate != null && !strStartDate.equals(""))
			startDate = MyDateFormat.changeStringToDate(strStartDate);

		String strEndDate = request.getParameter("endDate");
		Date endDate = null;
		if (strEndDate != null && !strEndDate.equals(""))
			endDate = MyDateFormat.changeStringToDate(strEndDate);

		// String strStartTemp = request.getParameter("startTemp");
		// String strEndTemp = request.getParameter("endTemp");

		ArrayList<Temperature> temperatureList = temperatureService
				.getQualifyTemperature(startDate, endDate);
		// list转成JSON
		JSONArray jsonArray = new JSONArray();
		for (Temperature temperature : temperatureList) {
			JSONObject jsonObject = new JSONObject();
			String dateStr = temperature.getCurRecordDate();
			String[] dateStrArray = dateStr.split("[-| |:]");
			jsonObject.put("year", dateStrArray[0]);
			jsonObject.put("month", dateStrArray[1]);
			jsonObject.put("day", dateStrArray[2]);
			jsonObject.put("hour", dateStrArray[3]);
			jsonObject.put("minute", dateStrArray[4]);
			jsonObject.put("second", dateStrArray[5]);
			jsonObject.put("date", dateStr);
			jsonObject.put("temperature", temperature.getTemperature());
			jsonArray.add(jsonObject);
		}
		response.getWriter().write(jsonArray.toString());
	}

	// 得到当前页的温度记录
	private void getAllTemperatureByPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		TemperatureService temperatureService = new TemperatureService();
		String num = request.getParameter("num");
		String pageSize = request.getParameter("pageSize");
		Page page = temperatureService.getPageTemperature(num, pageSize);
		page.setUrl("TemperatureServlet?operate=getAllTemperature");
		request.setAttribute("page", page);
		request.setAttribute("startTemp", request.getParameter("startTemp"));
		request.setAttribute("endTemp", request.getParameter("endTemp"));
		request.getRequestDispatcher(
				"jsp/qy/warehouse_management/temperature/getAllTemperature.jsp")
				.forward(request, response);
	}

	// 得到全部的temperature记录,返回JSON格式数据
	private void getAllTemperatue(HttpServletResponse response)
			throws IOException {
		TemperatureService temperatureService = new TemperatureService();
		ArrayList<Temperature> temperatureList = temperatureService
				.getAllTemperature();
		// 将list转为JSON
		JSONArray jsonArray = new JSONArray();
		for (Temperature temperature : temperatureList) {
			JSONObject jsonObject = new JSONObject();
			String dateStr = temperature.getCurRecordDate();
			String[] dateStrArray = dateStr.split("[-| |:]");
			jsonObject.put("year", dateStrArray[0]);
			jsonObject.put("month", dateStrArray[1]);
			jsonObject.put("day", dateStrArray[2]);
			jsonObject.put("hour", dateStrArray[3]);
			jsonObject.put("minute", dateStrArray[4]);
			jsonObject.put("second", dateStrArray[5]);
			jsonObject.put("date", dateStr);
			jsonObject.put("temperature", temperature.getTemperature());
			jsonArray.add(jsonObject);
		}
		response.getWriter().write(jsonArray.toString());
	}

	// 添加temperature记录
	// private void addTemperature() throws ServletException, IOException {
	// TemperatureService temperatureService = new TemperatureService();
	// HumidityService hs = new HumidityService();
	// // 获取参数
	// // 传感器传来温湿度，日期是当前时间，位置写在web.xml里
	// double temperature = 0.0;
	// double humidity = 0.0;
	// try {
	// TempHumiThread tempHumiThread = new TempHumiThread(2000);
	// double[] tempHumiRecord = tempHumiThread.call();
	// temperature = tempHumiRecord[0];
	// humidity = tempHumiRecord[1];
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// String curRecordDate = MyDateFormat.changeDateToLongString(new Date());
	// String position = this.getServletContext().getInitParameter(
	// "warehouseLocation");
	// // 填充模型
	// Temperature tempObj = new Temperature();
	// tempObj.setTemperature(temperature);
	// tempObj.setCurRecordDate(curRecordDate);
	// tempObj.setPosition(position);
	//
	// Humidity humidityObj = new Humidity();
	// humidityObj.setHumidity(humidity);
	// humidityObj.setCurRecordDate(curRecordDate);
	// humidityObj.setPosition(position);
	// // 判断是否重复提交
	// // boolean repeatFlag = temperatureService.repeatTemperature(tempObj);
	//
	// boolean addTempFlag = false, addHumiFlag = false;
	// // if (!repeatFlag) {
	// // 执行操作
	// addTempFlag = temperatureService.addTemperature(tempObj);
	// addHumiFlag = hs.addHumidity(humidityObj);
	// // 添加成功
	// if (addTempFlag && addHumiFlag) {
	// // request.setAttribute("addFlag", 1);
	// // 记录用户添加温度操作到日志
	// // Log log = new Log();
	// // log.setUserName((String)
	// // HttpServletRequest.getSession().getAttribute(
	// // "username"));
	// // log.setOperateTime(new Date()); // 记录当前用户操作的时间
	// // log.setOperateType("添加温湿度");
	// // log.setRemark("温度：" + tempObj.getTemperature() + "记录温度的时间："
	// // + tempObj.getCurRecordDate() + "记录温度位置："
	// // + tempObj.getPosition());
	// // UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	// // getAllTemperatureByPage(request, response);
	// } else {
	// // request.setAttribute("addFlag", 0);
	// // getAllTemperatureByPage(request, response);
	// }
	// // }
	// // 重复提交
	// // else {
	// // request.setAttribute("repeatFlag", 1);
	// // getAllTemperatureByPage(request, response);
	// // }
	// }

	// 查询修改前的原始记录,返回JSON
	// private void searchOldRecord(HttpServletRequest request,
	// HttpServletResponse response) {
	// TemperatureService temperatureService = new TemperatureService();
	// Long recordId = Long.parseLong(request.getParameter("recordId"));
	// String recordType = request.getParameter("recordType");
	//
	// ArrayList<Object> recordList = temperatureService.searchOldRecord(
	// recordId, recordType);
	//
	// // list转json
	// JSONArray jsonArray = new JSONArray();
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("recordNum", (Double) recordList.get(0));
	// jsonObject.put("curRecordDate", recordList.get(1));
	// jsonObject.put("position", recordList.get(2));
	// jsonArray.add(jsonObject);
	//
	// try {
	// response.getWriter().print(jsonArray.toString());
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// 获取满足条件记录数，计算不满足条件记录数，温度
	private void getTotalQualifyRecords(HttpServletRequest request,
			HttpServletResponse response) {
		TemperatureService ts = new TemperatureService();
		// 获取参数
		String strStartDate = request.getParameter("startDate");
		Date startDate = null;
		if (strStartDate != null && !strStartDate.equals(""))
			startDate = MyDateFormat.changeStringToDate(strStartDate);

		String strEndDate = request.getParameter("endDate");
		Date endDate = null;
		if (strEndDate != null && !strEndDate.equals(""))
			endDate = MyDateFormat.changeStringToDate(strEndDate);

		String strStartTemp = request.getParameter("startTemp");
		String strEndTemp = request.getParameter("endTemp");
		Double startTemp = null;
		Double endTemp = null;
		if (!strStartTemp.trim().equals("") && strStartTemp != null)
			startTemp = Double.parseDouble(strStartTemp);
		if (!strEndTemp.trim().equals("") && strEndTemp != null)
			endTemp = Double.parseDouble(strEndTemp);

		int allCount = ts.getTotalRecords();
		int matchCount = ts.getTotalQualifyRecords(startDate, endDate,
				startTemp, endTemp);
		int unmatchCount = allCount - matchCount;

		try {
			response.getWriter().print(matchCount + ";" + unmatchCount);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// 内部类 开一个记录温湿度的线程
	class TempHumiRecord implements Runnable {
		private long timeInterval = 0;
		private String position = "";

		public TempHumiRecord(long timeInterval, String position) {
			this.timeInterval = timeInterval;
			this.position = position;
		}

		public void run() {
			MyThread thread = null;
			while (true) {
				try {
					thread = new MyThread(timeInterval, position);
					Thread newThread = new Thread(thread);
					newThread.start();
					Thread.sleep(timeInterval);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	class MyThread implements Runnable {
		private long timeInterval = 0;
		private double temperature = 0.0;
		private double humidity = 0.0;
		private String position = "";
		private Runtime r = null;
		Process p = null;

		public MyThread(long timeInterval, String position) {
			this.timeInterval = timeInterval;
			this.position = position;
		}

		public void run() {
			r = Runtime.getRuntime();
			try {
				p = r.exec("E:\\Debug\\testTemprature.exe");
				BufferedReader stdout = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

				String line = "";
				int count = 1;
				while ((line = stdout.readLine()) != null) {
					if (count == 1) {
						// 记录温度
						temperature = Double.parseDouble(line.substring(0, 3));
						//System.out.println("temperature\t" + temperature);
						TemperatureService ts = new TemperatureService();
						Temperature tempObj = new Temperature();
						tempObj.setTemperature(temperature);
						tempObj.setCurRecordDate(MyDateFormat
								.changeDateToLongString(new Date()));
						tempObj.setPosition(position);
						ts.addTemperature(tempObj);
					} else if (count == 2) {
						// 记录湿度
						humidity = Double.parseDouble(line.substring(0, 3));
						//System.out.println("humidity\t" + humidity);
						HumidityService hs = new HumidityService();
						Humidity humiObj = new Humidity();
						humiObj.setHumidity(humidity);
						humiObj.setCurRecordDate(MyDateFormat
								.changeDateToLongString(new Date()));
						humiObj.setPosition(position);
						hs.addHumidity(humiObj);
					}
					count++;
					p.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				p.destroy();
			}
		}

	}
}
