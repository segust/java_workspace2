package cn.edu.cqupt.controller.statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import cn.edu.cqupt.controller.transact_business.OutWarehouseServlet;
import cn.edu.cqupt.service.statistics.EquipmentCollectiveService;
import cn.edu.cqupt.service.statistics.EquipmentDetailAccountService;
import cn.edu.cqupt.service.statistics.EquipmentDetailService;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.service.query_business.ApplyFormOperation;
import cn.edu.cqupt.service.sys_management.OutDataService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.DeleteFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class EquipmentServlet extends HttpServlet {
	//因为是单机用户，所以可以把申明放到这里来
	private ApplyFormOperation applyFormOperationService = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public EquipmentServlet() {
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
		this.doPost(request, response);
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
	
	private String curFolder="";
	String path=new String(); 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		String operate=request.getParameter("operate");
		
		// version==1表示是企业版
		if (version.equals("1")) {
			curFolder="qy";
			this.forwardByoperation(request, response, operate,version);
		}
		// version==2表示是军代室版
		else if(version.equals("2")){
			curFolder="jds";
			this.forwardByoperation(request, response, operate,version);
		}
		else if(version.equals("3")){
			curFolder="jdj";
			this.forwardByoperation(request, response, operate,version);
		}
		else{
			curFolder="zhj";
			this.forwardByoperation(request, response, operate,version);
		}		
	}
	
	private void forwardByoperation(HttpServletRequest request, HttpServletResponse response,String operate,String version) throws ServletException, IOException {
		
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		if(operate.equals("equipmentDetail")){
			path="/jsp/" + curFolder + "/statistics/equipment_detail.jsp";
			T=equipmentDetail(request, response,version,path);
			return;
		}else if(operate.equals("equipmentDetailAccount")){
			path="/jsp/" + curFolder + "/statistics/equipment_detail_account.jsp";
			equipmentDetailAccount(request, response,path,version);
			return;
		}else if(operate.equals("equipmentCollective")){
			path="/jsp/" + curFolder + "/statistics/equipment_strength.jsp";
			equipmentCollective(request, response, path,version);
			return;
		}else if(operate.equals("LoadIn")){
			loadIn(request, response);
			String pagename=request.getParameter("pagename");
			if(pagename.equals("equipment_detail_account")){
				path="/jsp/" + curFolder + "/statistics/equipment_detail_account.jsp";
				equipmentDetailAccount(request, response,path,version);
				return;
			}else if(pagename.equals("equipment_detail")){
				path="/jsp/" + curFolder + "/statistics/equipment_detail.jsp";
				equipmentDetail(request, response,version,path);
				return;
			}else if(pagename.equals("equipment_strengh")){
				path="/jsp/" + curFolder + "/statistics/equipment_strength.jsp";
				equipmentCollective(request, response, path,version);
			}
		}
		else if("exportSingleForm".equals(operate)){
			path = exportSingleForm(request, response, version, path);

		}else if("download".equals(operate)) {
			download(request, response);
		}else if("findEquipmentDetailAccount".equals(operate)){
			try {
				findEquipmentDetailAccount(request, response, version);
				path="";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("equipmentCollectiveToWord".equals(operate)){
			equipmentCollectiveToWord(request, response, version);
			path="";
		}else if("equipmentDetailToWord".equals(operate)){
			equipmentDetailToWord(request, response);
			path="";
		}else if("equipmentDetailAccountToWord".equals(operate)){
			try {
				equipmentDetailAccountToWord(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			path="";
		}else if("allEquipmentCollectiveToWord".equals(operate)){
			allEquipmentCollectiveToWord(request, response, version);
			path="";
		}else if("allEquipmentDetailToWord".equals(operate)){
			allEquipmentDetailToWord(request, response, version);
			path="";
		}else if("allEquipmentDetailAccountToWord".equals(operate)){
			allEquipmentDetailToWord(request, response, version);
			path="";
		}
		if(!"".equals(path)) {
			request.setAttribute("message", T);
			request.getRequestDispatcher(path).forward(request, response);
		}		
	}

	private void download(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException,
			FileNotFoundException, IOException {
		response.setCharacterEncoding("utf-8");
		String absolutePath = request.getParameter("absolutePath");
		String pagename=request.getParameter("pagename");
		String fileName = null;
		if(pagename.equals("equipmentdetail")){
			fileName="器材明细统计导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
		}
		else if (pagename.equals("equipmentcollective")){
			fileName="器材实力汇总统计导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
		}
		else if (pagename.equals("equipmentdetailAccount")){
			fileName="器材明细账统计导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
		}
		else if(pagename.equals("equipmentCollectiveToWord")){
			fileName="通信装备代储维修器材实力汇总表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
		}
		else if(pagename.equals("equipmentDetailToWord")){
			fileName="通信装备代储维修器材明细表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
		}
		else if(pagename.equals("equipmentDetailAccountToWord")){
			fileName="通信装备代储维修器材明细账"+MyDateFormat.changeDateToFileString(new Date())+".zip";
		}
		else if(pagename.equals("allEquipmentCollectiveToWord")){
			fileName="通信装备代储维修器材实力汇总总表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
		}
		else if(pagename.equals("allEquipmentDetailToWord")){
			fileName="通信装备代储维修器材明细总表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
		}
		else if(pagename.equals("allEquipmentDetailAccountToWord")){
			fileName="通信装备代储维修器材明细账总表"+MyDateFormat.changeDateToFileString(new Date())+".zip";
		}
		String agent = request.getHeader("User-Agent");
		//浏览器兼容
		boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
		if (isMSIE) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		File file = new File(absolutePath);
		if(!file.exists()) {
			request.setAttribute("message", "您要下载的资源已删除");
			//request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
		//设置响应头，控制浏览器下载文件
		response.setHeader("content-disposition", "attachment;filename=" + fileName);
		response.setContentType("application/vnd.ms-excel");  
		//读取要下载的文件
		FileInputStream in = new FileInputStream(absolutePath);
		//创建输出流
		OutputStream out = response.getOutputStream();
		//创建缓冲区
		byte buff[] = new byte[1024];
		int len = 0;
		//循环将输入流输入到缓冲区中
		while((len=in.read(buff))>0) {
			out.write(buff, 0, len);
		}
		in.close();
		out.close();
		if(file.exists()) {
			//删除文件
			file.delete();
		}
	}

	private String exportSingleForm(HttpServletRequest request,
			HttpServletResponse response, String version, String path)
			throws IOException {
		String ownedUnit = (String) request.getSession().getAttribute("ownedUnit");
		OutDataService outDataService=new OutDataService();
		String jsonStr = request.getParameter("outData");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		applyFormOperationService = new ApplyFormOperation();
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();
		// 加入标题行
		String pagename=request.getParameter("pagename");
		ArrayList<String> onlineArray = new ArrayList<String>();
		if(pagename.equals("equipmentdetail")){
			String[] onlineList = { "序号", "在库年份", "器材代码", "器材名称", "承制单位",
					"计量单位", "单价(元)", "产品编号", "生产时间","入库时间","质量等级","存储期限(月)","备注"};
			for (int i = 0; i < onlineList.length; i++) {
				onlineArray.add(onlineList[i]);
			}
		}
		else if (pagename.equals("equipmentcollective")){
			String[] onlineList = { "序号", "在库年份", "器材代码", "名称型号", "计量单位",
					"数量", "单价(元)", "金额(元)", "备注"};
			for (int i = 0; i < onlineList.length; i++) {
				onlineArray.add(onlineList[i]);
			}
		}
		else if (pagename.equals("equipmentdetailAccount")){
			String[] onlineList = { "序号", "在库年份", "名称型号", "包装件数", "总体积",
					"总重量", "生产厂家",  "单价(万元)", "包装说明", "配套说明", "器材代码"};
			for (int i = 0; i < onlineList.length; i++) {
				onlineArray.add(onlineList[i]);
			}
		}
		dyadicArray.add(onlineArray);

		// 开始计算数据个数
		for (int i = 0; i < jarray.size(); i++) {
			ArrayList<String> array = new ArrayList<String>();
			JSONArray tempArray = jarray.getJSONArray(i);
			for (int j = 0; j < tempArray.size(); j++) {
				array.add(tempArray.get(j).toString());
			}
			dyadicArray.add(array);
		}
		/**
		 * 1、在服务器的某个地方生成一个文件夹，起名uploadFilePlace
		 * 2、把二维数组中的数据生成一个excel表中，并放在exportFormTempFile文件夹中
		 * 3、返回路径和文件名
		 */
		String tempFilePath = "uploadFilePlace";
		//changed by LiangYH 11/17
		tempFilePath = request.getSession().getServletContext().getRealPath("/") +File.separator+ tempFilePath;
		//判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists() && !tempFile.isDirectory()){
			tempFile.mkdir();
		}
		if(pagename.equals("equipmentdetail")){
			String fileName="器材明细统计导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
			String absolutePath = tempFilePath+File.separator+fileName;
			outDataService.exportData(absolutePath,version,ownedUnit,dyadicArray);;
			//这个就是所需的文件名和路径
			response.setContentType("text/plain;charset=UTF-8");
			if(absolutePath != null && !"".equals(absolutePath)) {
				response.getWriter().write(absolutePath);
			}else {
				response.getWriter().write(0);
			}

			path = "";
		}else if(pagename.equals("equipmentcollective")){
			String fileName="器材实力汇总统计导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
			String absolutePath = tempFilePath+File.separator+fileName;
			outDataService.exportData(absolutePath,version,ownedUnit,dyadicArray);;
			//这个就是所需的文件名和路径
			response.setContentType("text/plain;charset=UTF-8");
			if(absolutePath != null && !"".equals(absolutePath)) {
				response.getWriter().write(absolutePath);
			}else {
				response.getWriter().write(0);
			}

			path = "";	
		}else if(pagename.equals("equipmentdetailAccount")){
			String fileName="器材明细账统计导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
			String absolutePath = tempFilePath+File.separator+fileName;
			outDataService.exportData(absolutePath,version,ownedUnit,dyadicArray);;
			//这个就是所需的文件名和路径
			response.setContentType("text/plain;charset=UTF-8");
			if(absolutePath != null && !"".equals(absolutePath)) {
				response.getWriter().write(absolutePath);
			}else {
				response.getWriter().write(0);
			}

			path = "";	
		}
		return path;
	}

	private void equipmentDetailAccount(
			HttpServletRequest request, HttpServletResponse response, String filepath,String version) throws ServletException, IOException {
		List<HashMap<String,Object>> T=new ArrayList<HashMap<String,Object>>();
		HashMap<String, String> condition=new HashMap<String,String>();
		String inYear=request.getParameter("inYear")==null?"/*/*":request.getParameter("inYear");
		String QCBM=request.getParameter("QCBM")==null?"/*/*":"/*"+request.getParameter("QCBM")+"/*";
		String productName=request.getParameter("productName")==null?"/*/*":"/*"+request.getParameter("productName")+"/*";
		String productModel=request.getParameter("productModel")==null?"/*/*":"/*"+request.getParameter("productModel")+"/*";
		
		condition.put("inYear", inYear);
		condition.put("QCBM", QCBM);		
		condition.put("productName", productName);		
		condition.put("productModel", productModel);		
		String keeper="";
		String ownedUnit="";
		if(version.equals("1")){
			keeper = (String)request.getSession().getAttribute("ownedUnit");
			condition.put("keeper", keeper);
		}
		else if(version.equals("2")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				StringBuffer sb=new StringBuffer();
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				ArrayList<String> companyList=is.getCompanyNameList(ownedUnit, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
//				System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
			else{
				condition.put("keeper", "\""+keeper+"\"");
			}
		}else if(version.equals("3")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				StringBuffer sb=new StringBuffer();
				ArrayList<String> jdsList=is.getJDSNameList(ownedUnit);
				for (String eachJds : jdsList) {
					ArrayList<String> companyList=is.getCompanyNameList(eachJds, 2);
					for (String eachCompany : companyList) {
						sb.append("\""+eachCompany+"\",");
					}
				}
				String companyStr=sb.toString();
//				System.out.println("company:"+companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
			else{
				StringBuffer sb=new StringBuffer();
				ArrayList<String> companyList=is.getCompanyNameList(keeper, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
				//System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
		}
		int curPageNum = request.getParameter("curPageNum") == null?1:Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = request.getParameter("pageSize") == null?10:Integer.parseInt(request.getParameter("pageSize"));
		
		int count = 0;
		try {
			EquipmentDetailAccountService equipmentDetailAccountService = new EquipmentDetailAccountService();
			T = equipmentDetailAccountService.selectEquipmentDetailAccounts(condition,curPageNum,pageSize,version);
			count = equipmentDetailAccountService.selectEquipmentDetailAccountCount(condition,version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("curPageNum",curPageNum);
		request.setAttribute("pageSize",pageSize);
		inYear=inYear.equals("/*/*")?"":inYear;
		QCBM=QCBM.equals("/*/*")?"":QCBM;
		productName=productName.equals("/*/*")?"":productName;
		productModel=productModel.equals("/*/*")?"":productModel;
		keeper=keeper.equals("/*/*")?"":keeper;
		request.setAttribute("inYear",inYear);
		request.setAttribute("QCBM",QCBM);
		request.setAttribute("productName",productName);
		request.setAttribute("productModel",productModel);
		request.setAttribute("keeper",keeper);
		request.setAttribute("sum", count);
		request.setAttribute("T", T);
		request.getRequestDispatcher(filepath).forward(request, response);
	}

	public List<HashMap<String, Object>> equipmentDetail(HttpServletRequest request, HttpServletResponse response,String version,String path) throws ServletException, IOException{
		List<HashMap<String, Object>> T=new ArrayList<HashMap<String, Object>>();//返回值
		HashMap<String, String> condition=new HashMap<String, String>();	
		String inYear=request.getParameter("inYear")==null?"/*/*":request.getParameter("inYear");
		String QCBM=request.getParameter("QCBM")==null?"/*/*":"/*"+request.getParameter("QCBM")+"/*";
		String productName=request.getParameter("productName")==null?"/*/*":"/*"+request.getParameter("productName")+"/*";
		
		condition.put("inYear", inYear);
		condition.put("QCBM", QCBM);
		condition.put("productName", productName);
		String keeper="";
		String ownedUnit="";
		if(version.equals("1")){
			keeper = (String)request.getSession().getAttribute("ownedUnit");
			condition.put("keeper", keeper);
		}
		else if(version.equals("2")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				StringBuffer sb=new StringBuffer();
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				ArrayList<String> companyList=is.getCompanyNameList(ownedUnit, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
				//System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
			else{
				condition.put("keeper", "\""+keeper+"\"");
			}
		}else if(version.equals("3")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				StringBuffer sb=new StringBuffer();
				ArrayList<String> jdsList=is.getJDSNameList(ownedUnit);
				for (String eachJds : jdsList) {
					ArrayList<String> companyList=is.getCompanyNameList(eachJds, 2);
					for (String eachCompany : companyList) {
						sb.append("\""+eachCompany+"\",");
					}
				}
				String companyStr=sb.toString();
//				System.out.println("company:"+companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
			else{
				StringBuffer sb=new StringBuffer();
				ArrayList<String> companyList=is.getCompanyNameList(keeper, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
				//System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
		}
		int curPageNum = request.getParameter("curPageNum") == null?1:Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = request.getParameter("pageSize") == null?10:Integer.parseInt(request.getParameter("pageSize"));
		
		int count = 0;
		try {
			EquipmentDetailService equipmentDetailService = new EquipmentDetailService();
			T=equipmentDetailService.selectEquipmentDetail(condition,version,curPageNum,pageSize);
			count = equipmentDetailService.selectEquipmentDetailCount(condition,version);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		request.setAttribute("sum", count);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("message", T);
		inYear=inYear.equals("/*/*")?"":inYear;
		QCBM=QCBM.equals("/*/*")?"":QCBM;
		productName=productName.equals("/*/*")?"":productName;
		keeper=keeper.equals("/*/*")?"":keeper;
		request.setAttribute("inYear", inYear);
		request.setAttribute("QCBM", QCBM);
		request.setAttribute("productName", productName);
		request.setAttribute("keeper", keeper);
		request.getRequestDispatcher(path).forward(request, response);
		return T;
	}
	public void equipmentCollective(HttpServletRequest request, HttpServletResponse response,String path,String version) throws ServletException, IOException{

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		HashMap<String, String> condition=new HashMap<String, String>();  //条件	
		String inYear=request.getParameter("inYear")==null?"/*/*":request.getParameter("inYear");
		String QCBM=request.getParameter("QCBM")==null?"/*/*":"/*"+request.getParameter("QCBM")+"/*";
		String productName=request.getParameter("productName")==null?"/*/*":"/*"+request.getParameter("productName")+"/*";
		String productModel=request.getParameter("productModel")==null?"/*/*":"/*"+request.getParameter("productModel")+"/*";
		
		System.out.println("inYear:"+inYear);
		condition.put("inYear", inYear);
		condition.put("QCBM", QCBM);
		condition.put("productName", productName);	
		condition.put("productModel", productModel);	
		String keeper="";
		String ownedUnit="";
		if(version.equals("1")){
			keeper = (String)request.getSession().getAttribute("ownedUnit");
			condition.put("keeper", keeper);
		}
		else if(version.equals("2")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				StringBuffer sb=new StringBuffer();
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				ArrayList<String> companyList=is.getCompanyNameList(ownedUnit, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
				//System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
			else{
				condition.put("keeper", "\""+keeper+"\"");
			}
		}else if(version.equals("3")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				StringBuffer sb=new StringBuffer();
				ArrayList<String> jdsList=is.getJDSNameList(ownedUnit);
				for (String eachJds : jdsList) {
					ArrayList<String> companyList=is.getCompanyNameList(eachJds, 2);
					for (String eachCompany : companyList) {
						sb.append("\""+eachCompany+"\",");
					}
				}
				String companyStr=sb.toString();
//				System.out.println("company:"+companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
			else{
				StringBuffer sb=new StringBuffer();
				ArrayList<String> companyList=is.getCompanyNameList(keeper, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
				//System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
		}
		int curPageNum = request.getParameter("curPageNum") == null?1:Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = request.getParameter("pageSize") == null?10:Integer.parseInt(request.getParameter("pageSize"));
				
		EquipmentCollectiveService equipmenrCS = new EquipmentCollectiveService();
		list = equipmenrCS.equipmentCollective(condition,version,curPageNum,pageSize);   //结果集
		int count = equipmenrCS.equipmentCollectiveCount(condition, version);
//		System.out.println("count:"+count);
		
		double price = 0;
		double totalPrice = 0;
		List<Map<String, Object>> T_jsp=new ArrayList<Map<String, Object>>();//返回值
		if(list!=null){
			count = list.size();
			for (int i = (curPageNum-1)*pageSize; i < pageSize*curPageNum; i++) {
				if(i<list.size()){
					T_jsp.add(list.get(i));
					price += Double.parseDouble(list.get(i).get("totalPrice")+"");
				}else{
					break;
				}
			}		
			for (int i = 0; i < list.size(); i++) {
				totalPrice += Double.parseDouble(list.get(i).get("totalPrice")+"");
			}
		}

		//  若查询正常，则将数据传给前端
		request.setAttribute("price", price);
		request.setAttribute("totalPrice", totalPrice);
		request.setAttribute("sum", count);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("list", list);
		inYear=inYear.equals("/*/*")?"":inYear;
		QCBM=QCBM.equals("/*/*")?"":QCBM;
		keeper=keeper.equals("/*/*")?"":keeper;
		productName=productName.equals("/*/*")?"":productName;
		productModel=productModel.equals("/*/*")?"":productModel;
		request.setAttribute("inYear", inYear);
		request.setAttribute("QCBM", QCBM);
		request.setAttribute("productName", productName);
		request.setAttribute("productModel", productModel);
		request.setAttribute("keeper", keeper);
		request.getRequestDispatcher(path).forward(request, response);  //将数据传给前端		
	}
	
	/**
	 * 明细账展开
	 * @throws Exception 
	 * */
	public void findEquipmentDetailAccount(HttpServletRequest request, HttpServletResponse response,String version) throws Exception{
		String productName = request.getParameter("productName");
		String productModel = request.getParameter("productModel");
		String manufacturer = request.getParameter("manufacturer");
		Double productPrice = Double.parseDouble((String)request.getParameter("productPrice"));
		String QCBM = request.getParameter("QCBM");

		HashMap<String, String> condition = new HashMap<String, String>();

		condition.put("productName", productName);
		condition.put("productModel", productModel);
		condition.put("manufacturer", manufacturer);
		condition.put("productPrice", productPrice+"");
		condition.put("QCBM", QCBM);
//		System.out.println(condition.get("productName"));
//		System.out.println(condition.get("productModel"));
//		System.out.println(condition.get("manufacturer"));
//		System.out.println(condition.get("productPrice"));
//		System.out.println(condition.get("QCBM"));
		//查询
		EquipmentDetailAccountService equipmentDetailAccountService = new EquipmentDetailAccountService();
		List<HashMap<String,String>> T = equipmentDetailAccountService.selectOutList(condition);
//		System.out.println("T.size:"+T.size());
		//转换成JSON传回前台
		JSONArray jarray = new JSONArray();
		for (HashMap<String, String> hashMap : T) {
			JSONObject jo = JSONObject.fromObject(hashMap);
			jarray.add(jo);
		}
		response.getWriter().write(jarray.toString());
	}

	/**
	 * 导入数据
	 * */
	public void loadIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// 上传导入文件到服务器中
		UploadFile uploadFile = new UploadFile();
		Map<String, String> map = uploadFile.uploadFile(request,response);
		List<String> words = new ArrayList<String>();

		// 此判断防止没有选择文件的时候出现异常
		if(map != null && map.size() != 0){
			// //文件上传之后在服务器中的路径
			String filePath = map.get("fileName");
			// 将上传的文件导入到内存中，返回一个二维数组
			OutDataService outDataService=new OutDataService();
			boolean flag=(Boolean)outDataService.importData(filePath, 8, "").get("flag");
			//删除已经上传的文件，因为上传的目的只是为了这次的读取
			File tempFile = new File(filePath);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			if(flag){
				words.add("导入成功");
			}else{
				words.add("导入失败");
			}
		}else{
			words.add("导入失败");
		}	
	}
	
	/**
	 * 器材实力汇总导出成word
	 * @throws IOException 
	 * */
	public void equipmentCollectiveToWord(HttpServletRequest request, HttpServletResponse response,
			String version) throws IOException{
		String jsonStr = request.getParameter("outData");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();//要导出的数据
		// 开始计算数据个数
		for (int i = 0; i < jarray.size(); i++) {
			JSONArray tempArray = jarray.getJSONArray(i);
			ArrayList<String> array = new ArrayList<String>();
			array.add(i+1+"");
			for (int j = 0; j < tempArray.size(); j++) {
				if(!version.equals("1")){
					if(j==6)continue;
				}
				array.add(tempArray.get(j).toString());
			}
			dyadicArray.add(array);
		}
		/**
		* 1、在服务器的某个地方生成一个文件夹，起名uploadFilePlace
		* 2、把二维数组中的数据生成一个excel表中，并放在exportFormTempFile文件夹中
		* 3、返回路径和文件名
		*/
		String tempFilePath = "uploadFilePlace";
		tempFilePath = request.getSession().getServletContext().getRealPath("/") +File.separator+ tempFilePath;
		//判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists() && !tempFile.isDirectory()){
			tempFile.mkdir();
		}
		String fileName="通信装备代储维修器材实力汇总表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
		String absolutePath = tempFilePath+File.separator+fileName;
		ApplyFormOperation applyFormOperation = new ApplyFormOperation();
		applyFormOperation.writeEquipmentCollectiveXWPFFile(absolutePath, dyadicArray);
		//这个就是所需的文件名和路径
		response.setContentType("text/plain;charset=UTF-8");
		if(absolutePath != null && !"".equals(absolutePath)) {
			response.getWriter().write(absolutePath);
		}else {
			response.getWriter().write(0);
		}
	}
	
	/**
	 * 器材明细统计导出成word
	 * @throws IOException 
	 * */
	public void equipmentDetailToWord(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String jsonStr = request.getParameter("outData");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();//要导出的数据
		// 开始计算数据个数
		for (int i = 0; i < jarray.size(); i++) {
			JSONArray tempArray = jarray.getJSONArray(i);
			ArrayList<String> array = new ArrayList<String>();
			array.add(i+1+"");
			for (int j = 0; j < tempArray.size(); j++) {
				if(j==10)continue;
				array.add(tempArray.get(j).toString());
			}
			dyadicArray.add(array);
		}
		/**
		* 1、在服务器的某个地方生成一个文件夹，起名uploadFilePlace
		* 2、把二维数组中的数据生成一个excel表中，并放在exportFormTempFile文件夹中
		* 3、返回路径和文件名
		*/
		String tempFilePath = "uploadFilePlace";
		tempFilePath = request.getSession().getServletContext().getRealPath("/")+File.separator+tempFilePath;
		//判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists() && !tempFile.isDirectory()){
			tempFile.mkdir();
		}
		String fileName="通信装备代储维修器材明细表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
		String absolutePath = tempFilePath+File.separator+fileName;
		ApplyFormOperation applyFormOperation = new ApplyFormOperation();
		applyFormOperation.writeEquipmentDetailXWPFFile(absolutePath, dyadicArray);
		//这个就是所需的文件名和路径
		response.setContentType("text/plain;charset=UTF-8");
		if(absolutePath != null && !"".equals(absolutePath)) {
			response.getWriter().write(absolutePath);
		}else {
			response.getWriter().write(0);
		}
	}
	
	/**
	 * 器材明细账导出成word
	 * @throws Exception 
	 * */
	public void equipmentDetailAccountToWord(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String jsonStr = request.getParameter("outData");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		String wordAbsolutePath = new String();//word存储的绝对路径
//		System.out.println("jsonStr:"+jsonStr);
		
		/**
		* 1、在服务器的某个地方生成一个文件夹，起名WordFilePlace
		* 2、返回路径和文件名
		*/
		ArrayList<String> absolutelyList = new ArrayList<String>();
		String tempFilePath = "WordFilePlace";
		//changed by LiangYH 11/17
		tempFilePath = request.getSession().getServletContext().getRealPath("/") + File.separator+tempFilePath;
		wordAbsolutePath = tempFilePath;
		//判断是否有没有这个文件目录,如果没有，就生成文件夹WordFilePlace
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists() && !tempFile.isDirectory()){
			tempFile.mkdir();
		}
		
		for (int i = 0; i < jarray.size(); i++) {
			//获取页面传来的值
			JSONArray tempArray = jarray.getJSONArray(i);
			ArrayList<String> array = new ArrayList<String>();
			for (int j = 0; j < tempArray.size(); j++) {
				array.add(tempArray.get(j).toString());
			}
			//获取料单信息
			EquipmentDetailAccountService equipmentDetailAccountService = new EquipmentDetailAccountService();
			HashMap<String, String> condition = new HashMap<String, String>();
			String product = array.get(0);
			String[] info = product.split("\\+");
			String productName = info[0];
			String productModel = info[1];
			condition.put("productName", productName);
			condition.put("productModel", productModel);
			condition.put("manufacturer", array.get(4));
			condition.put("productPrice", Double.parseDouble((String)array.get(5))+"");
			condition.put("QCBM", array.get(6));
			List<HashMap<String,String>> T = equipmentDetailAccountService.selectOutList(condition);
			//导出成word
			String number = array.get(9);
			String[] title = new String[9];
			List<ArrayList<String>> dyadic = new ArrayList<ArrayList<String>>();
//			for (int j = 0; j < array.size(); j++) {
//				System.out.println(array.get(j));
//			}
			title[0] = array.get(0);
			title[1] = array.get(6);
			title[2] = array.get(1);
			title[3] = array.get(2);
			title[4] = array.get(3);
			title[5] = array.get(4);
			title[6] = array.get(5);
			title[7] = array.get(7);
			title[8] = array.get(8);
			for (int j = 0; j < T.size(); j++) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(T.get(j).get("year"));
				list.add(T.get(j).get("month"));
				list.add(T.get(j).get("day"));
				list.add(T.get(j).get("listId"));
				list.add("");
//				list.add(T.get(j).get("keeper"));//暂时不要，无法填写
				list.add(T.get(j).get("rest"));
				list.add("0");
				list.add(T.get(j).get("out"));
				list.add(T.get(j).get("rest"));
				list.add(T.get(j).get("remark"));
				dyadic.add(list);
			}
			
			String fileName="通信装备代储维修器材明细账"+i+MyDateFormat.changeDateToFileString(new Date())+".docx";
			String absolutePath = tempFilePath+File.separator+fileName;
			absolutelyList.add(absolutePath);
			ApplyFormOperation applyFormOperation = new ApplyFormOperation();
			applyFormOperation.writeEquipmentDetailAccountXWPFFile(absolutePath, number, title, dyadic);
		}
		/**
		 * 将word文件打包
		 * */
		String zipFilePath = "ZipFilePlace";
		//changed by LiangYH 11/17
		zipFilePath = request.getSession().getServletContext().getRealPath("/") +File.separator +zipFilePath;
		//判断是否有没有这个文件目录,如果没有，就生成文件夹ZipFilePlace
		File zipFile = new File(zipFilePath);
		if(!zipFile.exists() && !zipFile.isDirectory()){
			zipFile.mkdir();
		}
		String fileName = "通信装备代储维修器材明细账"+MyDateFormat.changeDateToFileString(new Date())+".zip";
		String absolutePath = zipFilePath+File.separator;
		OutWarehouseServlet outWarehouseServlet = new OutWarehouseServlet();
		outWarehouseServlet.fileToZip(wordAbsolutePath, absolutePath, fileName);
		//这个就是所需的文件名和路径
		response.setContentType("text/plain;charset=UTF-8");
		if(absolutePath != null && !"".equals(absolutePath) && jarray.size()!=0) {
			response.getWriter().write(absolutePath+fileName);
		}else {
			response.getWriter().write("0");
		}
		/**
		 * 删除WordFilePlace
		 * */
		File file = new File(wordAbsolutePath);
		if(!file.exists()){
			return;
		}else{
			if(!file.isDirectory()){
				return;
			}else{
				DeleteFile.deleteDirectory(wordAbsolutePath);
			}
		}
	}
	
	/**
	 * 器材实力汇总总表导出成word
	 * @throws IOException 
	 * */
	public void allEquipmentCollectiveToWord(HttpServletRequest request, HttpServletResponse response
			,String version) throws IOException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		HashMap<String, String> condition=new HashMap<String, String>();  //条件	
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();//要导出的数据
		EquipmentCollectiveService equipmenrCS = new EquipmentCollectiveService();
		
		int count = equipmenrCS.equipmentCollectiveCount(condition, version);
		int curPageNum = 1;
		int pageSize = count;
		list = equipmenrCS.equipmentCollective(condition,version,curPageNum,pageSize);   //结果集
		for (int i = 0; i < list.size(); i++) {
			ArrayList<String> dyadic = new ArrayList<String>();
			dyadic.add(i+1+"");
			dyadic.add(list.get(i).get("QCBM")+"");
			dyadic.add(list.get(i).get("productName")+""+list.get(i).get("productModel"));
			dyadic.add(list.get(i).get("measureUnit")+"");
			dyadic.add(list.get(i).get("num")+"");
			dyadic.add(list.get(i).get("productPrice")+"");
			dyadic.add(list.get(i).get("totalPrice")+"");
			dyadic.add(list.get(i).get("remark")+"");
			dyadicArray.add(dyadic);
		}
		/**
		* 1、在服务器的某个地方生成一个文件夹，起名uploadFilePlace
		* 2、把二维数组中的数据生成一个excel表中，并放在exportFormTempFile文件夹中
		* 3、返回路径和文件名
		*/
		String tempFilePath = "uploadFilePlace";
		tempFilePath = request.getSession().getServletContext().getRealPath("/") +File.separator+ tempFilePath;
		//判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists() && !tempFile.isDirectory()){
			tempFile.mkdir();
		}
		String fileName="通信装备代储维修器材实力汇总总表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
		String absolutePath = tempFilePath+File.separator+fileName;
		ApplyFormOperation applyFormOperation = new ApplyFormOperation();
		applyFormOperation.writeEquipmentCollectiveXWPFFile(absolutePath, dyadicArray);
		//这个就是所需的文件名和路径
		response.setContentType("text/plain;charset=UTF-8");
		if(absolutePath != null && !"".equals(absolutePath)) {
			response.getWriter().write(absolutePath);
		}else {
			response.getWriter().write(0);
		}
	}
	
	/**
	 * 器材明细统计总表导出成word
	 * */
	public void allEquipmentDetailToWord(HttpServletRequest request, HttpServletResponse response
			,String version){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> condition=new HashMap<String, String>();  //条件	
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();//要导出的数据
		EquipmentDetailService equipmentDetailService;
		try {
			equipmentDetailService = new EquipmentDetailService();
			int count = equipmentDetailService.selectEquipmentDetailCount(condition, version);
			int curPageNum = 1;
			int pageSize = count;
			list = equipmentDetailService.selectEquipmentDetail(condition, version, curPageNum, pageSize);
			for (int i = 0; i < list.size(); i++) {
			ArrayList<String> dyadic = new ArrayList<String>();
			dyadic.add(i+1+"");
			dyadic.add(list.get(i).get("QCBM")+"");
			dyadic.add(list.get(i).get("productName")+"");
			dyadic.add(list.get(i).get("manufacturer")+"");
			dyadic.add(list.get(i).get("measureUnit")+"");
			dyadic.add(list.get(i).get("productPrice")+"");
			dyadic.add("");
			dyadic.add(list.get(i).get("producedDate")+"");
			dyadic.add(list.get(i).get("operateTime")+"");
			dyadic.add("");
			dyadic.add(list.get(i).get("storageTime")+"");
			dyadic.add(list.get(i).get("remark")+"");
			dyadicArray.add(dyadic);
		}
			/**
			* 1、在服务器的某个地方生成一个文件夹，起名uploadFilePlace
			* 2、把二维数组中的数据生成一个excel表中，并放在exportFormTempFile文件夹中
			* 3、返回路径和文件名
			*/
			String tempFilePath = "uploadFilePlace";
			tempFilePath = request.getSession().getServletContext().getRealPath("/") +File.separator+ tempFilePath;
			//判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
			File tempFile = new File(tempFilePath);
			if(!tempFile.exists() && !tempFile.isDirectory()){
				tempFile.mkdir();
			}
			String fileName="通信装备代储维修器材明细总表"+MyDateFormat.changeDateToFileString(new Date())+".docx";
			String absolutePath = tempFilePath+File.separator+fileName;
			ApplyFormOperation applyFormOperation = new ApplyFormOperation();
			applyFormOperation.writeEquipmentDetailXWPFFile(absolutePath, dyadicArray);
			//这个就是所需的文件名和路径
			response.setContentType("text/plain;charset=UTF-8");
			if(absolutePath != null && !"".equals(absolutePath)) {
				response.getWriter().write(absolutePath);
			}else {
				response.getWriter().write(0);
			}
		} catch (Exception e) {
				e.printStackTrace();
			}
	}
}