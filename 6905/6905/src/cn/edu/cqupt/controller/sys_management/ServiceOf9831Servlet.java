package cn.edu.cqupt.controller.sys_management;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.dao.Common9831DAO;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.sys_management.ApplyFormOperation;
import cn.edu.cqupt.service.sys_management.HandleServiceOf9831;
import cn.edu.cqupt.service.transact_business.UploadFile;

public class ServiceOf9831Servlet extends HttpServlet {
	private UploadFile uploadFile = null;
	private ApplyFormOperation applyFormOperation = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ServiceOf9831Servlet() {
		super();
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

		HandleServiceOf9831 handleServiceOf9831 = null;
		try {
			handleServiceOf9831 = new HandleServiceOf9831();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String path = "";
		String operate = request.getParameter("operate");
		Common9831DAO b = new Common9831DAO();
		HashMap<String, Object> key = new HashMap<String, Object>();
		int curPageNum = 0;
		int pageSize = 0;
		if (request.getParameter("curPageNum") == null
				|| request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		key.put("curPageNum", curPageNum);
		key.put("pageSize", pageSize);

		if (version.equals("1")) {
			curFolder = "qy";
			forwardByoperation(request, response, handleServiceOf9831,
					curPageNum, pageSize, key);
			return;
		} else if (version.equals("2")) {
			curFolder = "jds";
			forwardByoperation(request, response, handleServiceOf9831,
					curPageNum, pageSize, key);
			return;
		} else if (version.equals("3")) {
			curFolder = "jdj";
			forwardByoperation(request, response, handleServiceOf9831,
					curPageNum, pageSize, key);
			return;
		} else if (version.equals("4")) {
			curFolder = "zhj";
			forwardByoperation(request, response, handleServiceOf9831,
					curPageNum, pageSize, key);
			return;
		}

		else if ("checkpwd".equals(operate)) {
			boolean flag = false;
			String data = "删除失败";
			String pwd = request.getParameter("pwd");
			HttpSession session = request.getSession();
			System.out.println("到达servlet");
			if (session.getAttribute("password").equals(pwd)) {
				flag = b.DeleteAll();
			}
			if (flag) {
				data = "删除成功";
				System.out.println("servlet执行完操作");
				path = "/jsp/qy/sys_management/9831.jsp?curPageNum="
						+ curPageNum + "&pageSize=" + pageSize;
				int sum = b.SearchOf9831Sum(key);
				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write(data);
				request.setAttribute("Detailsum", sum);
				request.setAttribute("condition", key);
				request.setAttribute("message", b.SearchOf9831(key));
				request.getRequestDispatcher(path).forward(request, response);
			}
		}
	}

	public void forwardByoperation(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, int curPageNum,
			int pageSize, HashMap<String, Object> key) throws ServletException,
			IOException {
		String operate = request.getParameter("operate");
		if ("select".equals(operate)) {
			// String path="/jsp/"+curFolder+"/sys_management/9831.jsp";
			select9831(request, response, handleServiceOf9831, key);
			return;
		} else if ("edit".equals(operate)) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/UpdateOf9831.jsp";
			edit9831(request, response, handleServiceOf9831, path);
			return;
		} else if ("save".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/9831.jsp";
			update9831(request, response, handleServiceOf9831, curPageNum,
					pageSize, key, path);
			return;
		} else if ("add".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/9831.jsp";
			add9831(request, response, handleServiceOf9831, curPageNum,
					pageSize, key, path);
			return;
		} else if ("del".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/9831.jsp";
			delete9831(request, response, handleServiceOf9831, curPageNum,
					pageSize, key, path);
			return;
		} else if ("intoBasedata".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/9831.jsp";
			intoBaseData9831(request, response, handleServiceOf9831,
					curPageNum, pageSize, key, path);
			return;
		} else if ("LoadIn9831".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/9831.jsp";
			loadIn9831(request, response, handleServiceOf9831, curPageNum,
					pageSize, key, path);
			return;
		}
	}

	/**
	 * 9831库查询
	 * */
	public void select9831(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, HashMap<String, Object> key)
			throws ServletException, IOException {
		String PMNM = request.getParameter("PMNM");
		String PMCS = request.getParameter("PMCS");
		String PMBM = request.getParameter("PMBM");
		String LBQF = request.getParameter("LBQF");
		int nowpage = Integer.parseInt(request.getParameter("nowpage"));
		int pageSize= Integer.parseInt(request.getParameter("pagesize"));
		key.put("PMNM", PMNM);
		key.put("PMCS", PMCS);
		key.put("PMBM", PMBM);
		key.put("LBQF", LBQF);
		key.put("curpagenum", nowpage);
		key.put("pageSize", pageSize);
		HashMap<String, Object> page = new HashMap<String, Object>();
		ArrayList<Common9831> list = handleServiceOf9831.selectAll9831(key);
		int sum = handleServiceOf9831.SearchOf9831Sum(key);
		int totalpage = sum % 10 > 0 ? sum / 10 + 1 : sum / 10;
		JSONArray jsonArray2 = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONArray jsonArray1 = new JSONArray();
			jsonArray1.add(0, i + 10 * (nowpage - 1) + 1);
			jsonArray1.add(1, list.get(i).getPMNM());
			jsonArray1.add(2, list.get(i).getPMBM());
			jsonArray1.add(3, list.get(i).getQCBM());
			jsonArray1.add(4, list.get(i).getPMCS());
			jsonArray1.add(5, list.get(i).getXHTH());
			jsonArray1.add(6, list.get(i).getJLDW());
			jsonArray1.add(7, list.get(i).getCKDJ());
			jsonArray1.add(8, list.get(i).getBZTJ());
			jsonArray1.add(9, list.get(i).getBZJS());
			jsonArray1.add(10, list.get(i).getBZZL());
			jsonArray1.add(11, list.get(i).getQCXS());
			jsonArray1.add(12, list.get(i).getMJYL());
			jsonArray1.add(13, list.get(i).getXHDE());
			jsonArray1.add(14, list.get(i).getXLDJ());
			jsonArray1.add(15, list.get(i).getSCCJNM());
			jsonArray1.add(16, list.get(i).getGHDWNM());
			jsonArray1.add(17, list.get(i).getZBSX());
			jsonArray1.add(18, list.get(i).getSCDXNF());
			jsonArray1.add(19, list.get(i).getLBQF());
			jsonArray1.add(20, list.get(i).getYJDBZ());
			jsonArray1.add(21, list.get(i).getSYBZ());
			jsonArray1.add(22, list.get(i).getSCBZ());
			jsonArray1.add(23, list.get(i).getZBBDSJ());
			jsonArray2.add(jsonArray1);
			page.put("items", jsonArray2);
		}
		page.put("nowpage", nowpage);
		page.put("totalpage", totalpage);
		JSONObject returnJo = JSONObject.fromObject(page);
		response.getWriter().write(returnJo.toString());
	}

	/**
	 * 9831库查询 跳转页面时使用
	 * 
	 * @throws SQLException
	 * */
	public void select9831In(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		ArrayList<Common9831> message = handleServiceOf9831.select9831(key);
		int sum = handleServiceOf9831.selectSum(key);

		request.setAttribute("Detailsum", sum);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("condition", key);
		request.setAttribute("message", message);
		path += "?curPageNum=" + curPageNum + "&pageSize=" + pageSize;
		request.getRequestDispatcher(path).forward(request, response);

		System.out.println("sum:" + sum);
		System.out.println("curPageNum:" + curPageNum);
		System.out.println("pageSize:" + pageSize);
		System.out.println("message:" + message.size());
	}

	/**
	 * 进入编辑页面
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * */
	public void edit9831(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, String path)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		Common9831 common9831 = handleServiceOf9831.select9831ById(id);
		request.setAttribute("message", common9831);
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 编辑一条9831记录
	 * */
	public void update9831(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		Common9831 common9831 = new Common9831();
		common9831.setId(Integer.parseInt(request.getParameter("id")));
		common9831.setPMNM(request.getParameter("PMNM"));
		common9831.setPMBM(request.getParameter("PMBM"));
		common9831.setQCBM(request.getParameter("QCBM"));
		common9831.setPMCS(request.getParameter("PMCS"));
		common9831.setXHTH(request.getParameter("XHTH"));
		common9831.setXLDJ(request.getParameter("XLDJ"));
		common9831.setXHDE(request.getParameter("XHDE"));
		common9831.setJLDW(request.getParameter("JLDW"));
		common9831.setMJYL(request.getParameter("MJYL"));
		common9831.setQCXS(request.getParameter("QCXS"));
		common9831.setBZZL(request.getParameter("BZZL"));
		common9831.setBZJS(request.getParameter("BZJS"));
		common9831.setBZTJ(request.getParameter("BZTJ"));
		common9831.setCKDJ(request.getParameter("CKDJ"));
		common9831.setSCCJNM(request.getParameter("SCCJNM"));
		common9831.setGHDWNM(request.getParameter("GHDWNM"));
		common9831.setZBSX(request.getParameter("ZBSX"));
		common9831.setLBQF(request.getParameter("LBQF"));
		common9831.setZBBDSJ(request.getParameter("ZBBDSJ"));
		common9831.setSYBZ(request.getParameter("SYBZ"));
		common9831.setYJDBZ(request.getParameter("YJDBZ"));
		common9831.setSCBZ(request.getParameter("SCBZ"));
		common9831.setSCDXNF(request.getParameter("SCDXNF"));

		boolean flag = handleServiceOf9831.edit9831(common9831);
		select9831In(request, response, handleServiceOf9831, curPageNum,
				pageSize, key, path);
	}

	/**
	 * 新增一条9831库记录
	 * */
	public void add9831(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		ArrayList<Common9831> T = new ArrayList<Common9831>();
		Common9831 common9831 = new Common9831();
		common9831.setPMNM(request.getParameter("PMNM"));
		common9831.setPMBM(request.getParameter("PMBM"));
		common9831.setQCBM(request.getParameter("QCBM"));
		common9831.setPMCS(request.getParameter("PMCS"));
		common9831.setXHTH(request.getParameter("XHTH"));
		common9831.setXLDJ(request.getParameter("XLDJ"));
		common9831.setXHDE(request.getParameter("XHDE"));
		common9831.setJLDW(request.getParameter("JLDW"));
		common9831.setMJYL(request.getParameter("MJYL"));
		common9831.setQCXS(request.getParameter("QCXS"));
		common9831.setBZZL(request.getParameter("BZZL"));
		common9831.setBZJS(request.getParameter("BZJS"));
		common9831.setBZTJ(request.getParameter("BZTJ"));
		common9831.setCKDJ(request.getParameter("CKDJ"));
		common9831.setSCCJNM(request.getParameter("SCCJNM"));
		common9831.setGHDWNM(request.getParameter("GHDWNM"));
		common9831.setZBSX(request.getParameter("ZBSX"));
		common9831.setLBQF(request.getParameter("LBQF"));
		common9831.setZBBDSJ(request.getParameter("ZBBDSJ"));
		common9831.setSYBZ(request.getParameter("SYBZ"));
		common9831.setYJDBZ(request.getParameter("YJDBZ"));
		common9831.setSCBZ(request.getParameter("SCBZ"));
		common9831.setSCDXNF(request.getParameter("SCDXNF"));
		T.add(common9831);

		boolean flag = handleServiceOf9831.add9831(T);

		select9831In(request, response, handleServiceOf9831, curPageNum,
				pageSize, key, path);
	}

	/**
	 * 删除一条9831库记录
	 * */
	public void delete9831(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		boolean flag = handleServiceOf9831.delete9831(id);
		select9831In(request, response, handleServiceOf9831, curPageNum,
				pageSize, key, path);
	}

	/**
	 * 批量导入至基础数据库
	 * */
	public void intoBaseData9831(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		String jsonStr = request.getParameter("data");
		String ownedUnit = (String) request.getSession().getAttribute(
				"ownedUnit");
		// System.out.println(ownedUnit);
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		// String ownedUnit =request.getSession();
		List<String[]> T = new ArrayList<String[]>();
		System.out.println("jarray:" + jarray);

		for (int i = 0; i < jarray.size(); i++) {
			JSONArray tempArray = jarray.getJSONArray(i);
			// System.out.println("tempArray:"+tempArray);
			String[] data = new String[tempArray.size()];
			for (int j = 0; j < tempArray.size(); j++) {
				data[j] = tempArray.getString(j);
				// System.out.println(data[j]);
			}
			T.add(data);
		}

		boolean flag = handleServiceOf9831.intoBaseData(T, ownedUnit);
		response.setContentType("text/html,charset=utf8");
		// 记录用户批量导入基础数据库的操作到日志
		if (flag) {
			Log log = new Log();
			// 通过获取用户名username
			String username = (String) request.getSession().getAttribute(
					"username");
			log.setUserName(username);
			log.setOperateType("从9831库批量导入基础数据库");
			log.setOperateTime(new Date());
			UserLogService.SaveOperateLog(log);
		}
		if (flag) {
			response.getWriter().write("1");
		} else {
			response.getWriter().write("0");
		}
		// select9831In(request, response, handleServiceOf9831, curPageNum,
		// pageSize, key,path);
	}

	/**
	 * 导入9831库
	 * */
	public void loadIn9831(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOf9831 handleServiceOf9831, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		// 上传导入文件到服务器中
		uploadFile = new UploadFile();
		Map<String, String> map = uploadFile.uploadFile(request, response);
		List<String> words = new ArrayList<String>();

		// 此判断防止没有选择文件的时候出现异常
		if (map != null && map.size() != 0) {
			// //文件上传之后在服务器中的路径
			String filePath = map.get("fileName");
			// 将上传的文件导入到内存中，返回一个二维数组
			applyFormOperation = new ApplyFormOperation();
			// 导入excek数据，同时插入到数据库
			boolean flag = applyFormOperation.importForm(filePath, 0);
			// 删除已经上传的文件，因为上传的目的只是为了这次的读取
			File tempFile = new File(filePath);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			// 记录用户导入9831库的操作到日志
			if (flag) {
				Log log = new Log();
				// 通过identifyNum获取用户名username
				String username = (String) request.getSession().getAttribute(
						"username");
				// 通过identifyNum获取用户所属单位
				log.setUserName(username);
				log.setOperateType("导入9831库");
				log.setOperateTime(new Date());
				UserLogService.SaveOperateLog(log);
			}
			if (flag) {
				words.add("导入成功");
			} else {
				words.add("导入失败");
			}
		} else {
			words.add("导入失败");
		}
		select9831In(request, response, handleServiceOf9831, curPageNum,
				pageSize, key, path);
	}

}
