package cn.edu.cqupt.controller.sys_management;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.cqupt.beans.Basedata;
import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.beans.Parameter_Configuration;
import cn.edu.cqupt.dao.BasedataDAO;
import cn.edu.cqupt.service.sys_management.DataBackupService;
import cn.edu.cqupt.service.sys_management.DataRecoverService;
import cn.edu.cqupt.service.sys_management.HandleServiceOf9831;
import cn.edu.cqupt.service.sys_management.LogConfigurationService;
import cn.edu.cqupt.service.sys_management.Parameter_configurationService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;
import cn.edu.cqupt.util.StringUtil;

public class SystemManagementServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public SystemManagementServlet() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	private String curFolder;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");

		if (CurrentUser.isSystemManage(request)) {
			if (version.equals("1")) {
				curFolder = "qy";
				forwardByoperation(request, response, version);
			} else if (version.equals("2")) {
				curFolder = "jds";
				forwardByoperation(request, response, version);
			} else if (version.equals("3")) {
				curFolder = "jdj";
				forwardByoperation(request, response, version);
			} else if (version.equals("4")) {
				curFolder = "zhj";
				forwardByoperation(request, response, version);
			}
		} else {
			//当前用户没有权限
			GetError.limitVisit(request, response);
		}
	}

	/**
	 * 不同版本的path不同
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * */
	public void forwardByoperation(HttpServletRequest request,
			HttpServletResponse response, String version)
			throws ServletException, IOException {
		String operate = request.getParameter("operate");
		if (operate.equals("9831")) {
			try {
				String path = "/jsp/" + curFolder + "/sys_management/9831.jsp";
				select9831(request, response, path);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		} else if (operate.equals("basedata")) {
			String path = "/jsp/" + curFolder + "/sys_management/database.jsp";
			selectBaseData(request, response, path);
			return;
		} else if (operate.equals("dataBackup")) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/data_backups.jsp";
			dataBackup(request, response, path);
			return;
		} else if (operate.equals("logConfiguration")) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/log_configuration.jsp";
			logConfiguration(request, response, path);
			return;
		} else if (operate.equals("parameterConfiguration")) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/parameter_configuration.jsp";
			selectParameter(request, response, path, version);
			return;
		} else if (operate.equals("doDataBackup")) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/data_backups.jsp";
			doDataBackup(request, response, path);
			return;
		} else if (operate.equals("download")) {
			DownloadFile(request, response);
			return;
		} else if (operate.equals("LoadIn")) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/data_backups.jsp";
			loadIn(request, response, path);
		}
	}

	public void logConfiguration(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException,
			IOException {

		// 获取得到从前端传过来的数据
		HashMap<String, Object> condition = new HashMap<String, Object>(); // 条件
		String username = request.getParameter("username");
		if (username == null) {
			username = "";
		}
		String fromdate = request.getParameter("fromdate");
		if (fromdate == null) {
			fromdate = "";
		}
		String todate = request.getParameter("todate");
		if (todate == null) {
			todate = "";
		}
		String operateType = request.getParameter("operateType");
		if (operateType == null) {
			operateType = "";
		}
		String curPageNum = request.getParameter("curPageNum");// 页面传过来的“第几页”
		String pageSize = request.getParameter("pageSize");

		condition.put("username", username);
		condition.put("fromdate", fromdate);
		condition.put("todate", todate);
		condition.put("operateType", operateType);
		condition.put("curPageNum", curPageNum);
		condition.put("pageSize", pageSize);
		condition.put("sum", 0);
		// // 1. 判断
		// if(StringUtil.isEmpty(username)&&StringUtil.isEmpty(fromdate)&&StringUtil.isEmpty(todate)&&StringUtil.isEmpty(operateType)){
		// request.setAttribute("condition", condition);
		// request.setAttribute("message", "请输入要查询的信息！");
		// request.getRequestDispatcher(path).forward(request, response);
		// return;
		// }

		// 2. 判断from和to的是否只输入一个值，若只输入一个值，则返回信息：请输入正确的日期时间段！
		if ((StringUtil.isEmpty(fromdate) && StringUtil.isNotEmpty(todate))
				|| (StringUtil.isNotEmpty(fromdate) && StringUtil
						.isEmpty(todate))) {
			request.setAttribute("condition", condition);
			request.setAttribute("message", "请输入要查询的两个时间值！");
			request.getRequestDispatcher(path).forward(request, response);
			return;
		}

		// 3. 判断from和to的大小关系是否符合要求，若from大于to，则返回信息：请输入正确的日期时间段！
		if (StringUtil.isNotEmpty(fromdate) && StringUtil.isNotEmpty(todate)) {
			// 将string的from , to转化为date类型
			java.util.Date from = null;
			java.util.Date to = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				from = (java.util.Date) formatter.parse(fromdate);// (request.getParameter("from"));
				to = (java.util.Date) formatter.parse(todate);// (request.getParameter("to"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (from.after(to)) {
				request.setAttribute("condition", condition);
				request.setAttribute("message", "请输入正确的日期时间段！");
				request.getRequestDispatcher(path).forward(request, response); // 将数据传给前端
				return;
			}
		}

		// 调用service服务
		LogConfigurationService logCon = new LogConfigurationService();
		int sum = logCon.getTotalCountQueryLog(condition);
		condition.remove(sum);
		condition.put("sum", sum);

		// 4. 若没有查询到相关信息，则返回信息：没有相关记录！
		if (sum == 0) {
			request.setAttribute("condition", condition);
			request.setAttribute("message", "没有相关记录！");
			request.getRequestDispatcher(path).forward(request, response); // 将数据传给前端
			return;
		}

		List<Map<String, String>> logList = logCon.queryLog(condition);

		// 5. 若查询正常，则将数据传给前端
		request.setAttribute("condition", condition);
		request.setAttribute("logList", logList);
		request.getRequestDispatcher(path).forward(request, response); // 将数据传给前端

	}

	/**
	 * 跳转到数据备份页面
	 * */
	public void dataBackup(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException,
			IOException {
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 数据备份
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * */
	public void doDataBackup(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException,
			IOException {
		DataBackupService dataBackupService = new DataBackupService();
		String filename = request.getParameter("filename");
		if (StringUtil.isEmpty(filename) || filename.trim().equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");// 设置日期格式
			filename = df.format(new Date());// new Date()为获取当前系统时间
		}
		// 此处开始是新加入的！！！！！！！！！！！
		/**
		 * 1、在服务器的某个地方生成一个文件夹，起名uploadFilePlace 2、生成数据库数据 3、返回路径和文件名
		 */
		String tempFilePath = "dataBackFilePlace";
		tempFilePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ tempFilePath;
		// 判断是否有没有这个文件目录,如果没有，就生成文件夹dataBackFilePlace
		File tempFile = new File(tempFilePath);
		if (!tempFile.exists() && !tempFile.isDirectory()) {
			tempFile.mkdir();
		}
		String absolutePath = tempFilePath + File.separator + filename;
		dataBackupService.doDataBackup(absolutePath);
		System.out.println("filename:" + filename);
		System.out.println("tempFilePath:" + tempFilePath);
		System.out.println("absolutelyPath:" + absolutePath);
		// 这个就是所需的文件名和路径
		response.setContentType("text/plain;charset=UTF-8");
		if (absolutePath != null && !"".equals(absolutePath)) {
			response.getWriter().write(absolutePath);
		} else {
			response.getWriter().write(0);
		}
	}

	/**
	 * 下载服务器中的文件
	 * */
	public void DownloadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		String absolutePath = request.getParameter("absolutePath");
		absolutePath += ".sql";
		System.out.println("absolutePath:" + absolutePath);
		String fileName = null;
		String info[] = absolutePath.split("\\\\");
		fileName = info[info.length - 1];
		String agent = request.getHeader("User-Agent");
		// 浏览器兼容
		boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
		if (isMSIE) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		File file = new File(absolutePath);
		if (!file.exists()) {
			request.setAttribute("message", "您要下载的资源已删除");
		}
		// 设置响应头，控制浏览器下载文件
		response.setHeader("content-disposition", "attachment;filename="
				+ fileName);
		response.setContentType("application/vnd.ms-excel");
		// 读取要下载的文件
		FileInputStream in = new FileInputStream(absolutePath);
		// 创建输出流
		OutputStream out = response.getOutputStream();
		// 创建缓冲区
		byte buff[] = new byte[1024];
		int len = 0;
		// 循环将输入流输入到缓冲区中
		try {
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (file.exists()) {
			// 删除文件
			file.delete();
		}
	}

	/**
	 * 导入数据
	 * */
	public void loadIn(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException,
			IOException {
		// 上传导入文件到服务器中
		UploadFile uploadFile = new UploadFile();
		Map<String, String> map = uploadFile.uploadFileInDataRecover(request, response);
		List<String> words = new ArrayList<String>();
//		System.out.println("/*************servlet***********/");

		// 此判断防止没有选择文件的时候出现异常
		if (map != null && map.size() != 0) {
			// //文件上传之后在服务器中的路径
			String fileName = map.get("fileName");
//			System.out.println("fileName = "+fileName);
			String filePath = new String();
			for (int i = 0; i < fileName.length(); i++) {
				if (fileName.charAt(i) != '/') {
					filePath += fileName.charAt(i);
				}
			}
//			System.out.println("filePath = "+filePath);
			// 将上传的文件导入到内存中，返回一个二维数组
			DataRecoverService dataRecoverService = new DataRecoverService();
			boolean flag = dataRecoverService.readSQLFile(filePath);
			// 删除已经上传的文件，因为上传的目的只是为了这次的读取
			File tempFile = new File(filePath);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			if (flag) {
				words.add("导入成功");
			} else {
				words.add("导入失败");
			}
		} else {
			words.add("导入失败");
		}
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 参数配置查询
	 * */
	public void selectParameter(HttpServletRequest request,
			HttpServletResponse response, String path, String version)
			throws ServletException, IOException {
		Parameter_configurationService parameter_configurationService = new Parameter_configurationService();
		Parameter_Configuration T = parameter_configurationService
				.SelectParameter(version);
		if (version.equals("1")) {
			if (StringUtil.isEmpty(T.getId() + "")) {
				T.setMaintainCycle("");
				T.setCycle_ahead_days(0);
				T.setStore_ahead_days(0);
				T.setOut_ahead_days(0);
			}
		} else if (version.equals("2") || version.equals("3")
				|| version.equals("4")) {
			if (StringUtil.isEmpty(T.getId() + "")) {
				T.setMaintainCycle("");
				T.setCycle_ahead_days(0);
				T.setStore_ahead_days(0);
				T.setOut_ahead_days(0);
				T.setPrice_difference(0);
				T.setAlarm_cycle("");
				T.setAlarm_ahead_days(0);
			}
		}
		request.setAttribute("message", T);
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 基础数据库查询
	 * */
	public void selectBaseData(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException,
			IOException {
		HashMap<String, Object> key = new HashMap<String, Object>();
		BasedataDAO b = new BasedataDAO();
		String PMNM = request.getParameter("PMNM");
		key.put("PMNM", PMNM);
		System.out.println("PMNM" + PMNM);

		String PMCS = request.getParameter("PMCS");
		key.put("PMCS", PMCS);
		System.out.println(key.size());

		int curPageNum = 0;
		int pageSize = 0;
		if (request.getParameter("curPageNum") == null
				|| request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer
					.parseInt(request.getParameter("pageSize").trim());
		}

		ArrayList<Basedata> message = b.SearchOfBasedata(key, curPageNum,
				pageSize);
		int sum = b.SearchOfBasedataSum(key);
		request.setAttribute("Detailsum", sum);
		request.setAttribute("condition", key);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("message", message);
		path += "?curPageNum=" + curPageNum + "&pageSize=" + pageSize;
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 9831库查询
	 * */
	public void select9831(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException,
			IOException, SQLException {
		HandleServiceOf9831 handleServiceOf9831 = new HandleServiceOf9831();
		HashMap<String, Object> key = new HashMap<String, Object>();

		//首次访问，不需要这些参数 edited by lhs
		//条件查询放到了ServiceOf9831Servlet中
		// String PMNM = request.getParameter("PMNM");
		// key.put("PMNM", PMNM);
		// String PMCS = request.getParameter("PMCS");
		// key.put("PMCS", PMCS);
		// String PMBM = request.getParameter("PMBM");
		// key.put("PMBM", PMBM);
		// String LBQF = request.getParameter("LBQF");
		// key.put("LBQF", LBQF);
		// System.out.println(key.size());
		// System.out.println(PMNM);
		int curPageNum = 0;
		int pageSize = 0;
		if (request.getParameter("curPageNum") == null
				|| request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer
					.parseInt(request.getParameter("pageSize").trim());
		}
		key.put("curPageNum", curPageNum);
		key.put("pageSize", pageSize);

		ArrayList<Common9831> message = handleServiceOf9831.select9831(key);
		int sum = handleServiceOf9831.selectSum(key);
		request.setAttribute("Detailsum", sum);
		request.setAttribute("condition", key);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("message", message);
		path += "?curPageNum=" + curPageNum + "&pageSize=" + pageSize;
		request.getRequestDispatcher(path).forward(request, response);

		// System.out.println("sum:"+sum);
		// System.out.println("curPageNum:"+curPageNum);
		// System.out.println("pageSize:"+pageSize);
		// System.out.println("message:"+message.size());
	}
}
