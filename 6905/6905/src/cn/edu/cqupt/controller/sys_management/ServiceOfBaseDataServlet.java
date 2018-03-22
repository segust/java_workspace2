package cn.edu.cqupt.controller.sys_management;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Basedata;
import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.Unit;
import cn.edu.cqupt.dao.BasedataDAO;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.sys_management.HandleServiceOf9831;
import cn.edu.cqupt.service.sys_management.HandleServiceOfBaseData;
import cn.edu.cqupt.service.transact_business.ApplyFormOperation;
import cn.edu.cqupt.service.transact_business.ApplyHandleService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.DownloadFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class ServiceOfBaseDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ServiceOfBaseDataServlet() {
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
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
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
//		response.setContentType("text/html");
//		response.setCharacterEncoding("UTF-8");
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");

		HandleServiceOfBaseData handleServiceOfBaseData = null;
		handleServiceOfBaseData = new HandleServiceOfBaseData();
		HashMap<String, Object> key = new HashMap<String, Object>();
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

		if (version.equals("1")) {
			curFolder = "qy";
			forwardByoperation(request, response, handleServiceOfBaseData,
					curPageNum, pageSize, key);
			return;
		} else if (version.equals("2")) {
			curFolder = "jds";
			forwardByoperation(request, response, handleServiceOfBaseData,
					curPageNum, pageSize, key);
			return;
		} else if (version.equals("3")) {
			curFolder = "jdj";
			forwardByoperation(request, response, handleServiceOfBaseData,
					curPageNum, pageSize, key);
			return;
		} else if (version.equals("4")) {
			curFolder = "zhj";
			forwardByoperation(request, response, handleServiceOfBaseData,
					curPageNum, pageSize, key);
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public void forwardByoperation(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, int curPageNum,
			int pageSize, HashMap<String, Object> key) throws ServletException,
			IOException {
		String operate = request.getParameter("operate");
		if ("select".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/database.jsp";
			selectBasedata(request, response, handleServiceOfBaseData,key,path);
			return;
		} else if ("add".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/database.jsp";
			addBasedata(request, response, handleServiceOfBaseData, curPageNum,
					pageSize, key, path);
			return;
		} else if ("edit".equals(operate)) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/UpdateOfBasedata.jsp";
			editBasedata(request, response, handleServiceOfBaseData, path);
			return;
		} else if ("save".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/database.jsp";
			updateBasedata(request, response, handleServiceOfBaseData,
					curPageNum, pageSize, key, path);
			return;
		} else if ("del".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/database.jsp";
			deleteBasedataById(request, response, handleServiceOfBaseData,
					curPageNum, pageSize, key, path);
			return;
		} else if ("delete".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/database.jsp";
			deleteBasedata(request, response, handleServiceOfBaseData,
					curPageNum, pageSize, key, path);
			return;
		} else if ("manageUnit".equals(operate)) {
			String path = "/jsp/" + curFolder + "/sys_management/manage_unit.jsp";
			addUnit(request, response, handleServiceOfBaseData);
			return;
		} else if ("deleteUnit".equals(operate)) {
			String path = "/jsp/" + curFolder
					+ "/sys_management/manage_unit.jsp";
			boolean flag = deleteUnit(request, response,
					handleServiceOfBaseData, path);
			response.setContentType("text/plain,charset=utf-8");
			String msg = "0";
			if (flag) {
				msg = "1";
			}
			response.getWriter().write(msg);
			return;
//		} else if ("updateUnit".equals(operate)) {
//			String path = "/jsp/" + curFolder
//					+ "/sys_management/manage_unit.jsp";
//			boolean flag = updateUnit(request, response,
//					handleServiceOfBaseData, path);
//			// List<Unit> T = selectUnit(request,
//			// response,handleServiceOfBaseData,path);
//			response.setContentType("text/plain,charset=utf-8");
//			String msg = "0";
//			if (flag) {
//				msg = "1";
//			}
//			response.getWriter().write(msg);
//			// request.setAttribute("UnitList", T);
//			// request.getRequestDispatcher(path).forward(request, response);
//			return;
		} else if ("selectUnit".equals(operate)) {
			String PMNM = request.getParameter("PMNM");
			System.out.println("pmnm1:"+PMNM);
			String path = "/jsp/" + curFolder
					+ "/sys_management/manage_unit.jsp";
			List<Unit> T = selectUnit(request, response,
					handleServiceOfBaseData, path, PMNM);
			request.setAttribute("UnitList", T);
			request.setAttribute("FKPMNM", request.getParameter("PMNM"));
			request.getRequestDispatcher(path).forward(request, response);
			return;
		} else if ("exportBaseDataAndUnit".equals(operate)) {
			// 执行查询数据库操作
			Map<String, Object> targetMap = null;
			targetMap = handleServiceOfBaseData.findAllBasedDataAndUnit();
			// 生成默认文件夹
			String tempFilePath = StringUtil.createFold(request, null);
			String fileName = "ExportForm"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
			// 文件和绝对路径
			String absolutePath = tempFilePath + File.separator + fileName;
			// 生成excel表
			ApplyFormOperation afo = new ApplyFormOperation();
			afo.exportForm(absolutePath,
					(List<ArrayList<String>>) targetMap.get("basedData"),
					(List<ArrayList<String>>) targetMap.get("unit"));
			response.setContentType("text/plain,charset=utf8");
			PrintWriter writer = response.getWriter();
			if (absolutePath != null && !"".equals(absolutePath)) {
				writer.write(absolutePath);
			} else {
				writer.write("0");
			}
			return;
		} else if ("download".equals(operate)) {
			String absolutePath = request.getParameter("path");
			String fileName = "军代室_基础数据库导出表_"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
			// 防止因为浏览器不同导致文件名乱码
			fileName = DownloadFile.getNormalFilename(request, fileName);
			// 改变响应头，发起下载流
			DownloadFile.launchDownloadStream(response, absolutePath, fileName);
			// 删除文件
			File file = new File(absolutePath);
			if (file.exists()) {
				file.delete();
			}
			return;
		} else if("addOneUnit".equals(operate)){
			try {
				addOneUnit(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
//			String FKPMNM = request.getParameter("FKPMNM");
//			String path = "/jsp/" + curFolder + "/sys_management/manage_unit.jsp";
//			List<Unit> T = selectUnit(request, response,handleServiceOfBaseData, path, FKPMNM);
//			request.setAttribute("UnitList", T);
//			request.setAttribute("FKPMNM", FKPMNM);
//			request.getRequestDispatcher(path).forward(request, response);
			return;
		}
	}

	// public void forwardJDSByoperation(HttpServletRequest
	// request,HttpServletResponse response,HandleServiceOfBaseData
	// handleServiceOfBaseData,int curPageNum,int pageSize,HashMap<String,
	// Object> key) throws ServletException, IOException{
	// String operate=request.getParameter("operate");
	// if("select".equals(operate)){
	// String path="/jsp/jds/sys_management/database.jsp";
	// selectBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("add".equals(operate)){
	// String path="/jsp/jds/sys_management/database.jsp";
	// addBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("edit".equals(operate)){
	// String path="/jsp/jds/sys_management/UpdateOfBasedata.jsp";
	// editBasedata(request, response, handleServiceOfBaseData, path);
	// return;
	// }else if("save".equals(operate)){
	// String path="/jsp/jds/sys_management/database.jsp";
	// updateBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("del".equals(operate)){
	// String path="/jsp/jds/sys_management/database.jsp";
	// deleteBasedataById(request, response, handleServiceOfBaseData,
	// curPageNum, pageSize, key, path);
	// return;
	// }else if("delete".equals(operate)){
	// String path="/jsp/jds/sys_management/database.jsp";
	// deleteBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("importBaseDataFile".equals(operate )){
	// // 上传文件
	// UploadFile uploadFile = new UploadFile();
	// Map<String, String> map = uploadFile.uploadFile(request,response);
	// // 文件上传之后在服务器中的路径
	// String filePath = map.get("fileName");
	// // 从excel表中读取数据
	// ApplyFormOperation applyFormOperationService = new ApplyFormOperation();
	// Map<Integer, List<ArrayList<String>>> dyadicArray = null;
	// dyadicArray = applyFormOperationService.importAllSheetFromExcel(filePath,
	// 2);
	// // 删除上传的文件
	// File tempFile = new File(filePath);
	// if (tempFile.exists()) {
	// tempFile.delete();
	// }
	// //数据库操作：插入
	// boolean runStatus = false;
	// runStatus = handleServiceOfBaseData.insertBasedAndUnit(
	// dyadicArray.get(0), dyadicArray.get(1));
	// if(runStatus){
	// response.getWriter().write("1");
	// }else{
	// response.getWriter().write("0");
	// }
	// return ;
	// }
	// }

	// public void forwardJDJByoperation(HttpServletRequest
	// request,HttpServletResponse response,HandleServiceOfBaseData
	// handleServiceOfBaseData,int curPageNum,int pageSize,HashMap<String,
	// Object> key) throws ServletException, IOException{
	// String operate=request.getParameter("operate");
	// if("select".equals(operate)){
	// String path="/jsp/jdj/sys_management/database.jsp";
	// selectBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("add".equals(operate)){
	// String path="/jsp/jdj/sys_management/database.jsp";
	// addBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("edit".equals(operate)){
	// String path="/jsp/jdj/sys_management/UpdateOfBasedata.jsp";
	// editBasedata(request, response, handleServiceOfBaseData, path);
	// return;
	// }else if("save".equals(operate)){
	// String path="/jsp/jdj/sys_management/database.jsp";
	// updateBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("del".equals(operate)){
	// String path="/jsp/jdj/sys_management/database.jsp";
	// deleteBasedataById(request, response, handleServiceOfBaseData,
	// curPageNum, pageSize, key, path);
	// return;
	// }else if("delete".equals(operate)){
	// String path="/jsp/jdj/sys_management/database.jsp";
	// deleteBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }
	// }
	//
	// public void forwardZHJByoperation(HttpServletRequest
	// request,HttpServletResponse response,HandleServiceOfBaseData
	// handleServiceOfBaseData,int curPageNum,int pageSize,HashMap<String,
	// Object> key) throws ServletException, IOException{
	// String operate=request.getParameter("operate");
	// if("select".equals(operate)){
	// String path="/jsp/zhj/sys_management/database.jsp";
	// selectBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("add".equals(operate)){
	// String path="/jsp/zhj/sys_management/database.jsp";
	// addBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("edit".equals(operate)){
	// String path="/jsp/zhj/sys_management/UpdateOfBasedata.jsp";
	// editBasedata(request, response, handleServiceOfBaseData, path);
	// return;
	// }else if("save".equals(operate)){
	// String path="/jsp/zhj/sys_management/database.jsp";
	// updateBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }else if("del".equals(operate)){
	// String path="/jsp/zhj/sys_management/database.jsp";
	// deleteBasedataById(request, response, handleServiceOfBaseData,
	// curPageNum, pageSize, key, path);
	// return;
	// }else if("delete".equals(operate)){
	// String path="/jsp/zhj/sys_management/database.jsp";
	// deleteBasedata(request, response, handleServiceOfBaseData, curPageNum,
	// pageSize, key, path);
	// return;
	// }
	// }

	/**
	 * 基础数据库库查询
	 * */
	public void selectBasedata(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData,HashMap<String, Object> key,String path)
			throws ServletException, IOException {
		String PMNM = request.getParameter("PMNM");
		String PMCS = request.getParameter("PMCS");
		key.put("PMNM", PMNM);
		key.put("PMCS", PMCS);
		BasedataDAO b = new BasedataDAO();
		System.out.println("PMNM" + PMNM);
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
	 * 基础数据库库查询 跳转页面时使用
	 * */
	public void selectBasedataInForward(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException,
			IOException {
		HashMap<String, Object> key = new HashMap<String, Object>();
		BasedataDAO b = new BasedataDAO();

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

//	/**
//	 * 基础数据库库查询 跳转页面时使用
//	 * */
//	public void selectBasedataIn(HttpServletRequest request,
//			HttpServletResponse response,
//			HandleServiceOfBaseData handleServiceOfBaseData,HashMap<String, Object> key)
//			throws ServletException, IOException {
//		int curPageNum = 0;
//		int pageSize = 10;
//		ArrayList<Basedata> list = handleServiceOfBaseData.selectBasedata(key,curPageNum,pageSize);
//		int sum = handleServiceOfBaseData.selectSum(key);
////		int nowpage=Integer.parseInt(request.getParameter("nowpage"));
//		int totalpage = sum%10>0?sum/10+1:sum/10;
//		JSONArray jsonArray2 = new JSONArray();
//		for (int i=0;i<list.size();i++){
//			JSONArray jsonArray1 = new JSONArray();
//			 jsonArray1.add(0,i);
////			 jsonArray1.add(0,i+10*(nowpage-1)+1);
//			 jsonArray1.add(1, list.get(i).getPMNM());
//			 jsonArray1.add(2, list.get(i).getPMBM());
//			 jsonArray1.add(3, list.get(i).getQCBM());
//			 jsonArray1.add(4, list.get(i).getPMCS());
//			 jsonArray1.add(5, list.get(i).getXHTH());
//			 jsonArray1.add(6, list.get(i).getJLDW());
//			 jsonArray1.add(7, list.get(i).getCKDJ());
//			 jsonArray1.add(8, list.get(i).getBZTJ());
//			 jsonArray1.add(9, list.get(i).getBZJS());
//			 jsonArray1.add(10, list.get(i).getBZZL());
//			 jsonArray1.add(11, list.get(i).getQCXS());
//			 jsonArray1.add(12, list.get(i).getMJYL());
//			 jsonArray1.add(13, list.get(i).getXHDE());
//			 jsonArray1.add(14, list.get(i).getXLDJ());
//			 jsonArray1.add(15, list.get(i).getSCCJNM());
//			 jsonArray1.add(16, list.get(i).getGHDWNM());
//			 jsonArray1.add(17, list.get(i).getZBSX());
//			 jsonArray1.add(18, list.get(i).getSCDXNF());
//			 jsonArray1.add(19, list.get(i).getLBQF());
//			 jsonArray1.add(20, list.get(i).getYJDBZ());
//			 jsonArray1.add(21, list.get(i).getSYBZ());
//			 jsonArray1.add(22, list.get(i).getSCBZ());
//			 jsonArray1.add(23, list.get(i).getZBBDSJ());
//			 jsonArray2.add(jsonArray1);
//		}	
//		HashMap <String,Object> page = new HashMap <String,Object>();
//		page.put("items", jsonArray2);
////		page.put("nowpage", nowpage);
//		page.put("totalpage", totalpage);
//		JSONObject returnJo = JSONObject.fromObject(page);
//		response.getWriter().write(returnJo.toString());
//	}

	/**
	 * 增加基础数据库记录
	 * */
	public void addBasedata(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		ArrayList<Basedata> T = new ArrayList<Basedata>();
		Basedata u = new Basedata();
		u.setPMNM(request.getParameter("PMNM"));
		u.setPMBM(request.getParameter("PMBM"));
		u.setQCBM(request.getParameter("QCBM"));
		u.setPMCS(request.getParameter("PMCS"));
		u.setXHTH(request.getParameter("XHTH"));
		u.setXLDJ(request.getParameter("XLDJ"));
		u.setXHDE(request.getParameter("XHDE"));
		u.setJLDW(request.getParameter("JLDW"));
		u.setMJYL(request.getParameter("MJYL"));
		u.setQCXS(request.getParameter("QCXS"));
		u.setBZZL(request.getParameter("BZZL"));
		u.setBZJS(request.getParameter("BZJS"));
		u.setBZTJ(request.getParameter("BZTJ"));
		u.setCKDJ(request.getParameter("CKDJ"));
		u.setSCCJNM(request.getParameter("SCCJNM"));
		u.setGHDWNM(request.getParameter("GHDWNM"));
		u.setZBSX(request.getParameter("ZBSX"));
		u.setLBQF(request.getParameter("LBQF"));
		u.setZBBDSJ(request.getParameter("ZBBDSJ"));
		u.setSYBZ(request.getParameter("SYBZ"));
		u.setYJDBZ(request.getParameter("YJDBZ"));
		u.setSCBZ(request.getParameter("SCBZ"));
		u.setSCDXNF(request.getParameter("SCDXNF"));
		T.add(u);

		boolean flag = handleServiceOfBaseData.addBasedata(T);
		// 记录用户增加一条基础数据库纪录的操作到日志
				if(flag){
					Log log = new Log();
					// 通过获取用户名username
					String username = (String) request.getSession().getAttribute("username");
					log.setUserName(username);
					log.setOperateType("添加一条基础数据库纪录");
					log.setOperateTime(new Date());
					UserLogService.SaveOperateLog(log);
		}
		selectBasedataInForward(request, response, path);
	}

	/**
	 * 进入编辑基础数据库一条记录的页面
	 * */
	public void editBasedata(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, String path)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		Basedata basedata = handleServiceOfBaseData.selectBasedataById(id);
		request.setAttribute("message", basedata);
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 编辑数据库一条记录
	 * */
	public void updateBasedata(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		Basedata basedata = new Basedata();
		basedata.setPMNM(request.getParameter("PMNM"));
		basedata.setPMBM(request.getParameter("PMBM"));
		basedata.setQCBM(request.getParameter("QCBM"));
		basedata.setPMCS(request.getParameter("PMCS"));
		basedata.setXHTH(request.getParameter("XHTH"));
		basedata.setXLDJ(request.getParameter("XLDJ"));
		basedata.setXHDE(request.getParameter("XHDE"));
		basedata.setJLDW(request.getParameter("JLDW"));
		basedata.setMJYL(request.getParameter("MJYL"));
		basedata.setQCXS(request.getParameter("QCXS"));
		basedata.setBZZL(request.getParameter("BZZL"));
		basedata.setBZJS(request.getParameter("BZJS"));
		basedata.setBZTJ(request.getParameter("BZTJ"));
		basedata.setCKDJ(request.getParameter("CKDJ"));
		basedata.setSCCJNM(request.getParameter("SCCJNM"));
		basedata.setGHDWNM(request.getParameter("GHDWNM"));
		basedata.setZBSX(request.getParameter("ZBSX"));
		basedata.setLBQF(request.getParameter("LBQF"));
		basedata.setZBBDSJ(request.getParameter("ZBBDSJ"));
		basedata.setSYBZ(request.getParameter("SYBZ"));
		basedata.setYJDBZ(request.getParameter("YJDBZ"));
		basedata.setSCBZ(request.getParameter("SCBZ"));
		basedata.setSCDXNF(request.getParameter("SCDXNF"));
		basedata.setId(Integer.parseInt(request.getParameter("id")));

		boolean flag = handleServiceOfBaseData.updateBasedata(basedata);
		// 记录用户修改一条基础数据库纪录的操作到日志
		if(flag){
			Log log = new Log();
			// 通过获取用户名username
			String username = (String) request.getSession().getAttribute("username");
			log.setUserName(username);
			log.setOperateType("修改一条基础数据库纪录");
			log.setOperateTime(new Date());
			UserLogService.SaveOperateLog(log);
			response.getWriter().write("1");
			}else {
				response.getWriter().write("0");
			}
	}

	/**
	 * 删除一条基础数据库纪录
	 * */
	public void deleteBasedataById(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		boolean flag = handleServiceOfBaseData.deleteBasedata(id);
//		selectBasedataIn(request, response, handleServiceOfBaseData,
//				curPageNum, pageSize, key, path);
		// 记录用户删除一条基础数据库纪录的操作到日志
				if(flag){
					Log log = new Log();
					// 通过获取用户名username
					String username = (String) request.getSession().getAttribute("username");
					log.setUserName(username);
					log.setOperateType("删除一条基础数据库纪录");
					log.setOperateTime(new Date());
					UserLogService.SaveOperateLog(log);
		}
		if (flag) {
			response.getWriter().write("1");
		} else {
			response.getWriter().write("0");
		}
//		selectBasedataInForward(request, response, path);
		
		// selectBasedataIn(request, response, handleServiceOfBaseData,
		// curPageNum, pageSize, key, path);
	}

	/**
	 * 批量删除基础数据库纪录
	 * */
	public void deleteBasedata(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, int curPageNum,
			int pageSize, HashMap<String, Object> key, String path)
			throws ServletException, IOException {
		String jsonStr = request.getParameter("data");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		List<Integer> T = new ArrayList<Integer>();
		System.out.println("T:" + T.size());
		System.out.println("jsonStr:" + jsonStr);
		System.out.println("jarray:" + jarray);

		for (int i = 0; i < jarray.size(); i++) {
			int id = Integer.parseInt(jarray.get(i).toString());
			T.add(id);
			System.out.println("id:" + id);
		}

		boolean flag = handleServiceOfBaseData.deleteBasedataBitch(T);
		// 记录用户批量删除基础数据库纪录的操作到日志
		if(flag){
			Log log = new Log();
			// 通过获取用户名username
			String username = (String) request.getSession().getAttribute("username");
			log.setUserName(username);
			log.setOperateType("批量删除基础数据库纪录");
			log.setOperateTime(new Date());
			UserLogService.SaveOperateLog(log);
}
		if (flag) {
			response.getWriter().write("1");
		} else {
			response.getWriter().write("0");
		}
	}
	/**
	 * 批量添加单元
	 * @throws IOException 
	 * */
	public void addUnit(HttpServletRequest request,HttpServletResponse response,
HandleServiceOfBaseData handleServiceOfBaseData) throws IOException{
		String jsonStr = request.getParameter("outData");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		List<String> pmnmlist = new ArrayList<String>();
		for (int i = 0; i < jarray.size(); i++){
			pmnmlist.add(jarray.getString(i));
		}
		boolean flag =false;
		flag = handleServiceOfBaseData.addUnit(pmnmlist);
		if(flag){
		response.getWriter().write("1");
		}else {
			response.getWriter().write("0");
		}
	}
	/**
	 * 查询未添加的单元
	 * @throws IOException 
	 * */
	public void getNotaddedUnit(HttpServletRequest request,HttpServletResponse response,
HandleServiceOfBaseData handleServiceOfBaseData) throws IOException{
		String PMNM = request.getParameter("PMNM");
		 ArrayList<HashMap<String,String>>list = new ArrayList<HashMap<String,String>>();
		 list=handleServiceOfBaseData.getNotaddedUnit(PMNM);
		 JSONArray jsonArray1 = new JSONArray();
		 JSONArray jsonArray2 = new JSONArray();
		 for(int i=0;i<list.size();i++){
			 int j=0;
			 jsonArray1.add(j++, list.get(i).get("PMNM"));
			 jsonArray1.add(j++, list.get(i).get("PMBM"));
			 jsonArray1.add(j++, list.get(i).get("QCBM"));
			 jsonArray1.add(j++, list.get(i).get("PMCS"));
			 jsonArray1.add(j++, list.get(i).get("XHTH"));
			 jsonArray1.add(j++, list.get(i).get("JLDW"));
			 jsonArray1.add(j++, list.get(i).get("CKDJ"));
			 jsonArray1.add(j++, list.get(i).get("BZTJ"));
			 jsonArray1.add(j++, list.get(i).get("BZJS"));
			 jsonArray1.add(j++, list.get(i).get("BZZL"));
			 jsonArray1.add(j++, list.get(i).get("QCXS"));
			 jsonArray1.add(j++, list.get(i).get("MJYL"));
			 jsonArray1.add(j++, list.get(i).get("XHDE"));
			 jsonArray1.add(j++, list.get(i).get("XLDJ"));
			 jsonArray1.add(j++, list.get(i).get("SCCJNM"));
			 jsonArray1.add(j++, list.get(i).get("GHDWNM"));
			 jsonArray1.add(j++, list.get(i).get("ZBSX"));
			 jsonArray1.add(j++, list.get(i).get("SCDXNF"));
			 jsonArray1.add(j++, list.get(i).get("LBQF"));
			 jsonArray1.add(j++, list.get(i).get("YJDBZ"));
			 jsonArray1.add(j++, list.get(i).get("SYBZ"));
			 jsonArray1.add(j++, list.get(i).get("SCBZ"));
			 jsonArray1.add(j++, list.get(i).get("ZBBDSJ"));
			 jsonArray2.add(jsonArray1); 
		 }
		 response.getWriter().write(jsonArray2.toString());
	}
	/**
	 * 查询已添加的单元
	 * @throws IOException 
	 * */
	public void getAlladdedUnit(HttpServletRequest request,HttpServletResponse response,
HandleServiceOfBaseData handleServiceOfBaseData) throws IOException{
		String PMNM = request.getParameter("PMNM");
		 ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		 list=handleServiceOfBaseData.getAlladdedUnit(PMNM);
		 JSONArray jsonArray1 = new JSONArray();
		 JSONArray jsonArray2 = new JSONArray();
		 for(int i=0;i<list.size();i++){
			 int j=0;
			 jsonArray1.add(j++, list.get(i).get("PMNM"));
			 jsonArray1.add(j++, list.get(i).get("PMBM"));
			 jsonArray1.add(j++, list.get(i).get("QCBM"));
			 jsonArray1.add(j++, list.get(i).get("PMCS"));
			 jsonArray1.add(j++, list.get(i).get("XHTH"));
			 jsonArray1.add(j++, list.get(i).get("JLDW"));
			 jsonArray1.add(j++, list.get(i).get("CKDJ"));
			 jsonArray1.add(j++, list.get(i).get("BZTJ"));
			 jsonArray1.add(j++, list.get(i).get("BZJS"));
			 jsonArray1.add(j++, list.get(i).get("BZZL"));
			 jsonArray1.add(j++, list.get(i).get("QCXS"));
			 jsonArray1.add(j++, list.get(i).get("MJYL"));
			 jsonArray1.add(j++, list.get(i).get("XHDE"));
			 jsonArray1.add(j++, list.get(i).get("XLDJ"));
			 jsonArray1.add(j++, list.get(i).get("SCCJNM"));
			 jsonArray1.add(j++, list.get(i).get("GHDWNM"));
			 jsonArray1.add(j++, list.get(i).get("ZBSX"));
			 jsonArray1.add(j++, list.get(i).get("SCDXNF"));
			 jsonArray1.add(j++, list.get(i).get("LBQF"));
			 jsonArray1.add(j++, list.get(i).get("YJDBZ"));
			 jsonArray1.add(j++, list.get(i).get("SYBZ"));
			 jsonArray1.add(j++, list.get(i).get("SCBZ"));
			 jsonArray1.add(j++, list.get(i).get("ZBBDSJ"));
			 jsonArray2.add(jsonArray1); 
		 }
		 response.getWriter().write(jsonArray2.toString());
	}
	/*public void manageUnit(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, String path)
			throws ServletException, IOException {
		Unit u = new Unit();
		u.setPMNM(request.getParameter("PMNM"));
		u.setUnitName(request.getParameter("UnitName"));
		if (request.getParameter("price") != null) {
			u.setPrice(Double.parseDouble(request.getParameter("price")));
		}
		u.setMeasure(request.getParameter("measure"));
		boolean flag = handleServiceOfBaseData.addUnit(u);
		// 记录用户增加一条单元纪录的操作到日志
		if(flag){
			Log log = new Log();
			// 通过获取用户名username
			String username = (String) request.getSession().getAttribute("username");
			log.setUserName(username);
			log.setOperateType("增加一条单元纪录");
			log.setOperateTime(new Date());
			UserLogService.SaveOperateLog(log);
}
		if (flag) {
			List<Unit> T = selectUnit(request, response,
					handleServiceOfBaseData, path);
			request.setAttribute("UnitList", T);
			request.getRequestDispatcher(path).forward(request, response);
		}
	}
	/**
	 * 管理单元
	 * */
	
	/*public void addUnit(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, String path)
			throws ServletException, IOException {
		Unit u = new Unit();
		u.setPMNM(request.getParameter("PMNM"));
		u.setUnitName(request.getParameter("UnitName"));
		if (request.getParameter("price") != null) {
			u.setPrice(Double.parseDouble(request.getParameter("price")));
		}
		u.setMeasure(request.getParameter("measure"));
		boolean flag = handleServiceOfBaseData.addUnit(u);
		// 记录用户增加一条单元纪录的操作到日志
		if(flag){
			Log log = new Log();
			// 通过获取用户名username
			String username = (String) request.getSession().getAttribute("username");
			log.setUserName(username);
			log.setOperateType("增加一条单元纪录");
			log.setOperateTime(new Date());
			UserLogService.SaveOperateLog(log);
}
		if (flag) {
			List<Unit> T = selectUnit(request, response,
					handleServiceOfBaseData, path);
			request.setAttribute("UnitList", T);
			request.getRequestDispatcher(path).forward(request, response);
		}
	}

	/**
	 * 删除一条单元纪录
	 * */
	public boolean deleteUnit(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, String path)
			throws ServletException, IOException {
		int unitId = Integer.parseInt(request.getParameter("unitId"));
		boolean flag = handleServiceOfBaseData.deleteUnit(unitId);
		// 记录用户删除一条单元纪录的操作到日志
				if(flag){
					Log log = new Log();
					// 通过获取用户名username
					String username = (String) request.getSession().getAttribute("username");
					log.setUserName(username);
					log.setOperateType("删除一条单元纪录");
					log.setOperateTime(new Date());
					UserLogService.SaveOperateLog(log);
		}
		return flag;
		// selectBasedataIn(request, response, handleServiceOfBaseData,
		// curPageNum, pageSize, key, path);
	}

	/**
	 * 编辑一条单元记录
	 * */
	/*public boolean updateUnit(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, String path)
			throws ServletException, IOException {
		/*
		 * String jsonStr = request.getParameter("data"); JSONArray jarray =
		 * JSONArray.fromObject(jsonStr); Unit u=new Unit(); for(int
		 * i=0;i<jarray.size();i++){ JSONObject jo = jarray.getJSONObject(i);
		 * u.setMeasure(jo.getString("measure"));
		 * u.setPMNM(jo.getString("PMNM"));
		 * u.setUnitName(jo.getString("UnitName"));
		 * u.setPrice(jo.getDouble("price")); }
		 */
		/*Unit u = new Unit();
		int unitId = Integer.parseInt(request.getParameter("unitId"));
		String UnitName = request.getParameter("unitname");
		double price = Double.parseDouble(request.getParameter("price"));
		String measure = request.getParameter("measure");
		u.setUnitId(unitId);
		u.setUnitName(UnitName);
		u.setPrice(price);
		u.setMeasure(measure);
		boolean flag = handleServiceOfBaseData.updateUnit(u);
		// 记录用户编辑一条单元纪录的操作到日志
		if(flag){
			Log log = new Log();
			// 通过获取用户名username
			String username = (String) request.getSession().getAttribute("username");
			log.setUserName(username);
			log.setOperateType("修改一条单元纪录");
			log.setOperateTime(new Date());
			UserLogService.SaveOperateLog(log);
}
		return flag;
		// selectBasedataIn(request, response, handleServiceOfBaseData,
		// curPageNum, pageSize, key, path);
	}

	/**
	 * 单元查询
	 * */
	public ArrayList<Unit> selectUnit(HttpServletRequest request,
			HttpServletResponse response,
			HandleServiceOfBaseData handleServiceOfBaseData, String path, String PMNM)
			throws ServletException, IOException {
		ArrayList<Unit> T = new ArrayList<Unit>();
		System.out.println("PMNM:"+PMNM);
		// String UnitName=request.getParameter("UnitName");
		// if(request.getParameter("price")!=null){
		// double price=Double.parseDouble(request.getParameter("price"));}
		// String measure=request.getParameter("measure");
		T = handleServiceOfBaseData.findAllUnitByPmnm(PMNM);
		return T;
		// selectBasedataIn(request, response, handleServiceOfBaseData,
		// curPageNum, pageSize, key, path);
	}
	
	/**
	 * 添加一条单元记录
	 * */
	public boolean addOneUnit(HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			ServletException, IOException{
		HandleServiceOf9831 handleServiceOf9831 = new HandleServiceOf9831();
		ArrayList<Common9831> common9831List = new ArrayList<Common9831>();
		Common9831 common9831 = new Common9831();
		
		String FKPMNM = request.getParameter("FKPMNM");
		common9831.setBZJS(request.getParameter("BZJS"));
		common9831.setBZTJ(request.getParameter("BZTJ"));
		common9831.setBZZL(request.getParameter("BZZL"));
		common9831.setCKDJ(request.getParameter("CKDJ"));
		common9831.setGHDWNM(request.getParameter("GHDWNM"));
		common9831.setJLDW(request.getParameter("JLDW"));
		common9831.setLBQF(request.getParameter("LBQF"));
		common9831.setMJYL(request.getParameter("MJYL"));
		common9831.setPMBM(request.getParameter("PMBM"));
		common9831.setPMCS(request.getParameter("PMCS"));
		common9831.setPMNM(request.getParameter("PMNM"));
		common9831.setQCBM(request.getParameter("QCBM"));
		common9831.setQCXS(request.getParameter("QCXS"));
		common9831.setSCBZ(request.getParameter("SCBZ"));
		common9831.setSCCJNM(request.getParameter("SCCJNM"));
		common9831.setSCDXNF(request.getParameter("SCDXNF"));
		common9831.setSYBZ(request.getParameter("SYBZ"));
		common9831.setXHDE(request.getParameter("XHDE"));
		common9831.setXHTH(request.getParameter("XHTH"));
		common9831.setXLDJ(request.getParameter("XLDJ"));
		common9831.setYJDBZ(request.getParameter("YJDBZ"));
		common9831.setZBSX(request.getParameter("ZBSX"));
		common9831List.add(common9831);
		
		boolean flag = handleServiceOf9831.addUnit(common9831List, FKPMNM);
		if(flag) {
			response.getWriter().write("1");
		}else {
			response.getWriter().write("0");
		}
		return flag;
	}
}
