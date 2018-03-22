package test;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.util.CurrentUser;

public class DemoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5063373575585490640L;

	/**
	 * Constructor of the object.
	 */
	public DemoServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		//version==1表示是企业版
		if(version.equals("1")){
			//举例：判断当前用户是否具有 业务办理  权限
			if(CurrentUser.isContractManage(request)){
				//这里面写你的主要Servlet的代码
				
				
				
				//记录日志的Demo代码
				HttpSession session=request.getSession();
				Log log=new Log();
				log.setUserName((String)session.getAttribute("username"));	//当前登录的用户名已经保存在session中
				log.setOperateTime(new Date());		//记录当前用户进行**操作的时间
				log.setOperateType("写你的操作类型，比如：登录、添加某某、删除某某。。。");
				//log.set**    其他字段根据你的需要再自己添加
				UserLogService.SaveOperateLog(log);		//记录当前用户操作到数据库
				
				//负责记录产品操作的日志和记录维护操作的日志，请负责该模块的小组自己添加
				
				
				
			}
			else{
				
			}
		}
		else{
			
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
