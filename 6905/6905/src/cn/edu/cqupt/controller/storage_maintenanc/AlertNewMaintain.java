package cn.edu.cqupt.controller.storage_maintenanc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.cqupt.beans.Parameter_Configuration;
import cn.edu.cqupt.service.storage_maintenanc.CheckAlarmService;
import cn.edu.cqupt.service.storage_maintenanc.InspectAlarmService;
import cn.edu.cqupt.service.storage_maintenanc.MaintainAlarmService;
import cn.edu.cqupt.service.storage_maintenanc.UpdateReturnAlarmService;
import cn.edu.cqupt.service.sys_management.Parameter_configurationService;

public class AlertNewMaintain extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -739735145481262699L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * // long timed = Long.parseLong(request.getParameter("timed")); Random
		 * rand = new Random(); while(true) { try { Thread.sleep(1000); int i =
		 * rand.nextInt(100); if(i>20 && i< 56) { long responseTime =
		 * System.currentTimeMillis();
		 * response.setContentType("text/plain;charset=UTF-8");
		 * //System.out.println("result: " + i + ", response time: " +
		 * responseTime); response.getWriter().write(i+""); break; } } catch
		 * (InterruptedException e) { e.printStackTrace(); } }
		 */
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		String jsonStr = "";

		if ("1".equals(version)) {
			int remind = 0;
			Parameter_configurationService paraService = new Parameter_configurationService();
			MaintainAlarmService alarm = new MaintainAlarmService();
			Parameter_Configuration paras = paraService
					.SelectParameter(version);
			remind = paras.getCycle_ahead_days();
			int count = 0;
			count = alarm.autoAlarm(remind);

			String path = "Maintain?operateType=maintainQuery&curPageNum=1&pageSize=10&restMaintainTime="
					+ remind;
			response.setContentType("text/plain;charset=UTF-8");
			if (count != 0) {
				// System.out.println("result: " + i + ", response time: " +
				// responseTime);
				jsonStr += "{\"protect\":{\"num\":" + count + ",\"link\":\"" + path + "\"}";
			} else {
				jsonStr += "{\"protect\":{\"num\":0,\"link\":\"\"}";
			}
		} else if ("2".equals(version)) {
			InspectAlarmService service = new InspectAlarmService();
			boolean flag = service.inspectAlarm(version);
			jsonStr = "{\"inspect\":\"" + flag + "\"";
		} else if ("3".equals(version)) {
			CheckAlarmService service = new CheckAlarmService();
			boolean flag = service.inspectAlarm(version);
			jsonStr = "{\"check\":\"" + flag + "\"";
		}

		UpdateReturnAlarmService alarmService = new UpdateReturnAlarmService();
		response.setContentType("text/plain;charset=UTF-8");
		String path = "UpdateQueryServlet?operate=updateQuery&curPageNum=1&pageSize=10";
		int count = alarmService.getUpdateReturnNumber();
		if (count != 0) {
			// System.out.println("result: " + i + ", response time: " +
			// responseTime);
			jsonStr += ",\"update\":{\"num\":" + count + ",\"link\":\"" + path + "\"}}";
		} else {
			jsonStr += ",\"update\":{\"num\":0,\"link\":\"" + path + "\"}}";
		}
//		System.out.println(jsonStr);
		response.getWriter().write(jsonStr);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
