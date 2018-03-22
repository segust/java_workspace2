package cn.edu.cqupt.controller.warehouse_management;

import java.io.IOException;
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
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.warehouse_management.HumidityService;
import cn.edu.cqupt.service.warehouse_management.TemperatureService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;
import cn.edu.cqupt.util.MyDateFormat;

public class HumidityServlet extends HttpServlet {

	private static final long serialVersionUID = 2775828793107432461L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		// version==1表示是企业版
		if (version.equals("1")) {
			if (CurrentUser.isWarehouseManage(request)) {
				String operate = request.getParameter("operate");
				// 添加湿度记录
				if ("addHumidity".equals(operate)) {
					// addHumidity(request, response);
					return;
				}
				// 得到当前页所有的湿度记录
				else if ("getAllHumidity".equals(operate)) {
					getAllHumidity(request, response);
					return;
				}
				// 得到全部的湿度记录
				else if ("getAllJSONHumidity".equals(operate)) {
					getAllHumidity(response);
					return;
				}
				// 条件查询当前页湿度记录
				else if ("getQualifyHumidity".equals(operate)) {
					getQualifyHumidity(request, response);
					return;
				}
				// 条件查询全部的湿度记录
				else if ("getAllQualifyHumidity".equals(operate)) {
					getAllQualifyHumidity(request, response);
					return;
				}
				// 删除单个湿度记录
				else if ("deleteOneHumidity".equals(operate)) {
					// deleteOneHumidity(request, response);
					return;
				}
				// 修改单个湿度记录
				else if ("updateHumidity".equals(operate)) {
					// updateHumidity(request, response);
					return;
				}
				// 条件查询满足条件的记录数
				else if ("getTotalQualifyRecords".equals(operate)) {
					getTotalQualifyRecords(request, response);
					return;
				}
				// 以下代码是用户首次访问时执行。
				HumidityService humidityService = new HumidityService();
				String pageSize = request.getParameter("pageSize");
				String startHumidity = request.getParameter("startHumidity");
				String endHumidity = request.getParameter("endHumidity");
				Page page = humidityService.getPageHumidity("1", pageSize);
				page.setUrl("HumidityServlet?operate=getAllHumidity");
				request.setAttribute("page", page);
				request.setAttribute("startHumidity", startHumidity);
				request.setAttribute("endHumidity", endHumidity);
				request.getRequestDispatcher(
						"jsp/qy/warehouse_management/humidity/getAllHumidity.jsp")
						.forward(request, response);
			} else {
				// 当前用户没有权限
				GetError.limitVisit(request, response);
			}
		} else {// 表示不是企业版
			response.getWriter().write("您使用的不是企业版");
		}

	}

	// 修改单个湿度记录
	// private void updateHumidity(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// HumidityService humidityService = new HumidityService();
	// // 获取参数
	// Long humidityId = Long.parseLong(request.getParameter("humidityId"));
	// Double humidity = Double.parseDouble(request.getParameter("humidity"));
	// String curRecordDate = request.getParameter("curRecordDate");
	// String operateDate = humidityService.getOperateDate(humidityId);
	// operateDate += ";";
	// operateDate += MyDateFormat.changeDateToLongString(new Date());
	// String position = request.getParameter("position");
	// // 填充模型
	// Humidity humiObj = new Humidity();
	// humiObj.setHumidityId(humidityId);
	// humiObj.setHumidity(humidity);
	// humiObj.setCurRecordDate(curRecordDate);
	// humiObj.setPosition(position);
	// // 执行修改
	// // 在更新前先将原纪录插入qy_old_humidity
	// if (humidityService.insertOldRecord(humidityId)) {
	// boolean flag = humidityService.updateHumidity(humiObj);
	// // 页面跳转
	// if (flag) {
	// request.setAttribute("updateFlag", 1);
	// // 记录用户修改湿度操作
	// Log log = new Log();
	// log.setUserName((String) request.getSession().getAttribute(
	// "username"));
	// log.setOperateTime(new Date()); // 记录当前用户操作的时间
	// log.setOperateType("修改湿度,湿度编号:" + humidityId);
	// UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	// getAllHumidity(request, response);
	// } else {
	// request.setAttribute("updateFlag", 0);
	// }
	// } else {
	// request.setAttribute("updateFlag", 0);
	// }
	// }

	// 删除单个湿度记录
	// private void deleteOneHumidity(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// HumidityService humidityService = new HumidityService();
	// // 获取参数
	// Long humidityId = Long.parseLong(request.getParameter("humidityId"));
	// // 执行操作
	// boolean flag = humidityService.deleteOneHumidity(humidityId);
	// if (flag) {
	// request.setAttribute("deleteFlag", 1);
	// // 记录用户删除湿度的操作
	// Log log = new Log();
	// log.setUserName((String) request.getSession().getAttribute(
	// "username"));
	// log.setOperateTime(new Date()); // 记录当前用户操作的时间
	// log.setOperateType("删除湿度，湿度编号为:" + humidityId);
	// UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	// getAllHumidity(request, response);
	// } else {
	// request.setAttribute("updateFlag", 0);
	// }
	// }

	// 条件查询分页的湿度记录
	private void getQualifyHumidity(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HumidityService humidityService = new HumidityService();
		// 获取参数
		String strStartDate = request.getParameter("startDate");
		Date startDate = null;
		if (strStartDate != null && !strStartDate.equals(""))
			startDate = MyDateFormat.changeStringToDate(strStartDate);

		String strEndDate = request.getParameter("endDate");
		Date endDate = null;
		if (strEndDate != null && !strEndDate.equals(""))
			endDate = MyDateFormat.changeStringToDate(strEndDate);

		String strStartHumidity = request.getParameter("startHumidity");
		String strEndHumidity = request.getParameter("endHumidity");

		String num = request.getParameter("num");// 页面传过来的“第几页”
		String pageSize = request.getParameter("pageSize");
		// 执行操作，封装到域对象
		Page page = humidityService.getQualifyHumidity(startDate, endDate, num,
				pageSize);
		page.setUrl("HumidityServlet?operate=getQualifyHumidity");
		request.setAttribute("page", page);
		request.setAttribute("startDate", strStartDate);
		request.setAttribute("endDate", strEndDate);
		request.setAttribute("startHumidity", strStartHumidity);
		request.setAttribute("endHumidity", strEndHumidity);
		// 页面跳转
		request.getRequestDispatcher(
				"jsp/qy/warehouse_management/humidity/getAllHumidity.jsp")
				.forward(request, response);
	}

	// 条件查询全部的湿度记录
	private void getAllQualifyHumidity(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HumidityService humidityService = new HumidityService();
		// 获取参数
		String strStartDate = request.getParameter("startDate");
		Date startDate = null;
		if (strStartDate != null && !strStartDate.equals(""))
			startDate = MyDateFormat.changeStringToDate(strStartDate);

		String strEndDate = request.getParameter("endDate");
		Date endDate = null;
		if (strEndDate != null && !strEndDate.equals(""))
			endDate = MyDateFormat.changeStringToDate(strEndDate);

		// String strStartHumidity = request.getParameter("startHumidity");
		// String strEndHumidity = request.getParameter("endHumidity");

		ArrayList<Humidity> humidityList = humidityService.getQualifyHumidity(
				startDate, endDate);
		// list转成JSON
		JSONArray jsonArray = new JSONArray();
		for (Humidity humidity : humidityList) {
			JSONObject jsonObject = new JSONObject();
			String dateStr = humidity.getCurRecordDate();
			String[] dateStrArray = dateStr.split("[-| |:]");
			jsonObject.put("year", dateStrArray[0]);
			jsonObject.put("month", dateStrArray[1]);
			jsonObject.put("day", dateStrArray[2]);
			jsonObject.put("hour", dateStrArray[3]);
			jsonObject.put("minute", dateStrArray[4]);
			jsonObject.put("second", dateStrArray[5]);
			jsonObject.put("date", dateStr);
			jsonObject.put("humidity", humidity.getHumidity());
			jsonArray.add(jsonObject);
		}
		response.getWriter().write(jsonArray.toString());
	}

	// 得到当前页所有的湿度记录
	private void getAllHumidity(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HumidityService humidityService = new HumidityService();
		String num = request.getParameter("num");
		String pageSize = request.getParameter("pageSize");
		Page page = humidityService.getPageHumidity(num, pageSize);
		page.setUrl("HumidityServlet?operate=getAllHumidity");
		request.setAttribute("page", page);
		request.setAttribute("startHumidity",
				request.getParameter("startHumidity"));
		request.setAttribute("endHumidity", request.getParameter("endHumidity"));
		request.getRequestDispatcher(
				"jsp/qy/warehouse_management/humidity/getAllHumidity.jsp")
				.forward(request, response);
	}

	// 得到全部的humidity记录,返回JSON格式数据
	private void getAllHumidity(HttpServletResponse response)
			throws IOException {
		HumidityService humidityService = new HumidityService();
		ArrayList<Humidity> humidityList = humidityService.getAllHumidity();
		// 将list转为JSON
		JSONArray jsonArray = new JSONArray();
		for (Humidity humidity : humidityList) {
			JSONObject jsonObject = new JSONObject();
			String dateStr = humidity.getCurRecordDate();
			String[] dateStrArray = dateStr.split("[-| |:]");
			jsonObject.put("year", dateStrArray[0]);
			jsonObject.put("month", dateStrArray[1]);
			jsonObject.put("day", dateStrArray[2]);
			jsonObject.put("hour", dateStrArray[3]);
			jsonObject.put("minute", dateStrArray[4]);
			jsonObject.put("second", dateStrArray[5]);
			jsonObject.put("date", dateStr);
			jsonObject.put("humidity", humidity.getHumidity());
			jsonArray.add(jsonObject);
		}
		response.getWriter().write(jsonArray.toString());
	}

	// 添加湿度记录
	// private void addHumidity(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// HumidityService humidityService = new HumidityService();
	// // 获取参数
	// Double humidity = Double.parseDouble(request.getParameter("humidity"));
	// String curRecordDate = request.getParameter("curRecordDate");
	// String position = request.getParameter("position");
	// // 填充模型
	// Humidity humiObj = new Humidity();
	// humiObj.setHumidity(humidity);
	// humiObj.setCurRecordDate(curRecordDate);
	// humiObj.setPosition(position);
	// // 判断是否重复提交
	// boolean addFlag = false;
	// // 执行操作
	// addFlag = humidityService.addHumidity(humiObj);
	// // 页面跳转
	// if (addFlag) {
	// // request.setAttribute("addFlag", 1);
	// // // 记录用户添加湿度操作到日志
	// // Log log = new Log();
	// // log.setUserName((String) request.getSession().getAttribute(
	// // "username"));
	// // log.setOperateTime(new Date()); // 记录当前用户操作的时间
	// // log.setOperateType("添加湿度");
	// // log.setRemark("湿度：" + humiObj.getHumidity() + "记录湿度时间："
	// // + humiObj.getOperateDate() + "记录湿度位置："
	// // + humiObj.getPosition());
	// // UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	// getAllHumidity(request, response);
	// } else {
	// request.setAttribute("addFlag", 0);
	// getAllHumidity(request, response);
	// }
	// }

	// 获取满足条件记录数，计算不满足条件记录数，湿度
	private void getTotalQualifyRecords(HttpServletRequest request,
			HttpServletResponse response) {
		HumidityService hs = new HumidityService();
		// 获取参数
		String strStartDate = request.getParameter("startDate");
		Date startDate = null;
		if (strStartDate != null && !strStartDate.equals(""))
			startDate = MyDateFormat.changeStringToDate(strStartDate);

		String strEndDate = request.getParameter("endDate");
		Date endDate = null;
		if (strEndDate != null && !strEndDate.equals(""))
			endDate = MyDateFormat.changeStringToDate(strEndDate);

		String strStartHumidity = request.getParameter("startHumdity");
		String strEndHumidity = request.getParameter("endHumidity");
		Double startHumdity = null;
		Double endHumdity = null;
		if (!strStartHumidity.trim().equals("") && strStartHumidity != null)
			startHumdity = Double.parseDouble(strStartHumidity);
		if (!strEndHumidity.trim().equals("") && strEndHumidity != null)
			endHumdity = Double.parseDouble(strEndHumidity);

		int allCount = hs.getTotalRecords();
		int matchCount = hs.getTotalQualifyRecords(startDate, endDate,
				startHumdity, endHumdity);
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

}
