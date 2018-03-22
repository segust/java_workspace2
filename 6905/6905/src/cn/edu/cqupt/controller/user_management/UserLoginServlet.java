package cn.edu.cqupt.controller.user_management;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.User;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.user_management.UserService;
import cn.edu.cqupt.util.RSAUtil;

public class UserLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4705218939623131768L;

	/**
	 * Constructor of the object.
	 */
	public UserLoginServlet() {
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
		doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");

		// 全局变量获取当前版本
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		//将版本放到session中
		request.getSession().setAttribute("version", version);
		
		HttpSession session = request.getSession();

		// 通过UserService和UserDAO获取用户名和权限列表
		UserService urs = new UserService();

		// 获取输入的用户账号和密码
		String identifyNum = request.getParameter("identifyNum");
		String password = request.getParameter("password");
		password=RSAUtil.string2RSA(password);

		// 将用户输入的用户名和密码封装成qy_user对象
		User user = new User();
		user.setIdentifyNum(identifyNum);
		user.setPassword(password);

		// 验证用户输入信息是否正确
		int loginFlag = urs.validateUser(user);
		// 登录跳转后的页面
		String responseUrl="";

		// 如果正确
		if (loginFlag==1) {
			// 记录用户登录的操作到日志
			Log log = new Log();
			// 通过identifyNum获取用户名username
			String username = urs.getUserNameByIdentifyNum(identifyNum);
			// 通过identifyNum获取用户所属单位
			String ownedUnit=urs.getOwnedUnitByIdentifyNum(identifyNum);
			log.setUserName(username);
			log.setOperateType("登录");
			log.setOperateTime(new Date());
			UserLogService.SaveOperateLog(log);
			
			// 得到当前登录用户的权限
			// HashMap的内容举个栗子：
			// key:UserManage value:1 这就表示当前用户拥有用户管理模块的权限
			// key:SystemManage value:0 这就表示当前用户不拥有系统管理模块的权限
			HashMap<String, Integer> userPowerMap = new HashMap<String, Integer>();
			userPowerMap = urs.getUserRolePower(identifyNum);

			// 将权限列表放入session对象
			session.setAttribute("userPowerMap", userPowerMap);

			// 将username和password等需要的信息放入session
			session.setAttribute("username", username);
			session.setAttribute("password", password);
			session.setAttribute("identifyNum",identifyNum);
			session.setAttribute("ownedUnit", ownedUnit);//ownedUnit用户所属单位
			
			//将提醒的信息放入request
			/*boolean inspectAlarmFlag=new InspectAlarmService().inspectAlarm(version);
			boolean checkAlarmFlag=new CheckAlarmService().inspectAlarm(version);
			request.setAttribute("inspectAlarmFlag", inspectAlarmFlag);
			request.setAttribute("checkAlarmFlag", checkAlarmFlag);*/
			
			// 跳转到欢迎页面welcome.jsp
			responseUrl+="1,";
			if(version.equals("1")){
				//request.getRequestDispatcher("jsp/qy/welcome.jsp").forward(request, response);
				responseUrl+="../../jsp/qy/welcome.jsp";
				System.out.println(responseUrl);
			}
			else if(version.equals("2")){
				//request.getRequestDispatcher("jsp/jds/welcome.jsp").forward(request, response);
				responseUrl+="../../jsp/jds/welcome.jsp";
			}
			else if(version.equals("3")){
				//request.getRequestDispatcher("jsp/jdj/welcome.jsp").forward(request, response);
				responseUrl+="../../jsp/jdj/welcome.jsp";
			}
			else if(version.equals("4")){
				//request.getRequestDispatcher("jsp/zhj/welcome.jsp").forward(request, response);
				responseUrl+="../../jsp/zhj/welcome.jsp";
			}
		} else if(loginFlag==0) {
			// 用户输入信息有误，跳转到主页面
			responseUrl="0";
		}
		else if(loginFlag==-1){
			responseUrl="-1";
		}
		try {
			response.getWriter().write(responseUrl);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
}
