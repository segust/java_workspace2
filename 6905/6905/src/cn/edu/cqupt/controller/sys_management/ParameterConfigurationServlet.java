package cn.edu.cqupt.controller.sys_management;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.cqupt.beans.Parameter_Configuration;
import cn.edu.cqupt.service.sys_management.Parameter_configurationService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;
import cn.edu.cqupt.util.StringUtil;
public class ParameterConfigurationServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of the object.
	 */
	public ParameterConfigurationServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	private String curFolder;
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		if(CurrentUser.isSystemManage(request)){
			if(version.equals("1")){
				curFolder="qy";
				forwardByoperation(request, response, version);
			}else if(version.equals("2")){
				curFolder="jds";
				forwardByoperation(request, response, version);
			}else if(version.equals("3")){
				curFolder="jdj";
				forwardByoperation(request, response, version);
			}else if(version.equals("4")){
				curFolder="zhj";
				forwardByoperation(request, response, version);
			}
		}
		else{
			//当前用户没有权限
			GetError.limitVisit(request, response);
		}
	}
	
	public void forwardByoperation(HttpServletRequest request,HttpServletResponse response,String version) throws ServletException, IOException{
		String operate = request.getParameter("operate");
		// 添加parameter_configuration记录
		if (operate.equals("parameter_configuration")) {
			String path="/jsp/"+curFolder+"/sys_management/parameter_configuration.jsp";
			addParameter_configuration(request, response,path,version);
			return;
		}
	}
	
	// 添加Parameter_configuration记录
	private void addParameter_configuration(HttpServletRequest request,
			HttpServletResponse response,String path,String version) throws ServletException, IOException {
		Parameter_Configuration T=new Parameter_Configuration();
		Parameter_configurationService parameter_configurationService = new Parameter_configurationService();
		// 填充模型
		Parameter_Configuration parameter_configuration = new Parameter_Configuration();
		if(StringUtil.isNotEmpty(request.getParameter("maintain_cycle"))){
			parameter_configuration.setMaintainCycle(request.getParameter("maintain_cycle"));			
		}
		if(StringUtil.isNotEmpty(request.getParameter("cycle_ahead_days"))){
			parameter_configuration.setCycle_ahead_days(Integer.parseInt(request.getParameter("cycle_ahead_days")));			
		}
		if(StringUtil.isNotEmpty(request.getParameter("store_ahead_days"))){
			parameter_configuration.setStore_ahead_days(Integer.parseInt(request.getParameter("store_ahead_days")));			
		}
		if(StringUtil.isNotEmpty(request.getParameter("out_ahead_days"))){
			parameter_configuration.setOut_ahead_days(Integer.parseInt(request.getParameter("out_ahead_days")));			
		}
		if(StringUtil.isNotEmpty(request.getParameter("price_difference"))){
			parameter_configuration.setPrice_difference(Integer.parseInt(request.getParameter("price_difference")));			
		}
		if(StringUtil.isNotEmpty(request.getParameter("alarm_cycle"))){
			parameter_configuration.setAlarm_cycle(request.getParameter("alarm_cycle"));
		}
		if(StringUtil.isNotEmpty(request.getParameter("alarm_ahead_days"))){
			parameter_configuration.setAlarm_ahead_days(Integer.parseInt(request.getParameter("alarm_ahead_days")));
		}
		System.out.println(parameter_configuration.getMaintainCycle());
		System.out.println(parameter_configuration.getCycle_ahead_days());
		System.out.println(parameter_configuration.getStore_ahead_days());
		System.out.println(parameter_configuration.getOut_ahead_days());
		System.out.println(parameter_configuration.getPrice_difference());
		System.out.println(parameter_configuration.getAlarm_cycle());
		System.out.println(parameter_configuration.getAlarm_ahead_days());
		// 执行操作
		parameter_configurationService.addParameter_Configuration(parameter_configuration,version);
		T=parameter_configurationService.SelectParameter(version);
		//判断是否为空
		if(version.equals("1")){
			if(StringUtil.isEmpty(T.getId()+"")){
				T.setMaintainCycle("");
				T.setCycle_ahead_days(0);
				T.setStore_ahead_days(0);
				T.setOut_ahead_days(0);
			}
		}else if(version.equals("2")){
			if(StringUtil.isEmpty(T.getId()+"")){
				T.setStore_ahead_days(0);
				T.setOut_ahead_days(0);
				T.setPrice_difference(0);
				T.setAlarm_cycle("");
				T.setAlarm_ahead_days(0);
			}
		}else if(version.equals("3")){
			if(StringUtil.isEmpty(T.getId()+"")){
				T.setStore_ahead_days(0);
				T.setOut_ahead_days(0);
				T.setPrice_difference(0);
				T.setAlarm_cycle("");
				T.setAlarm_ahead_days(0);
			}
		}else if(version.equals("4")){
			if(StringUtil.isEmpty(T.getId()+"")){
				T.setStore_ahead_days(0);
				T.setOut_ahead_days(0);
				T.setPrice_difference(0);
				T.setAlarm_cycle("");
				T.setAlarm_ahead_days(0);
			}
		}
		// 页面跳转
		request.setAttribute("message", T);
		request.getRequestDispatcher(path).forward(request, response);
		return;
	}
}
