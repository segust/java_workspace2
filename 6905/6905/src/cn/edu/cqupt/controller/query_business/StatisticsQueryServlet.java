package cn.edu.cqupt.controller.query_business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.service.query_business.ApplyFormOperation;
import cn.edu.cqupt.service.statistics.ContractService;
import cn.edu.cqupt.service.statistics.ProductService;
import cn.edu.cqupt.service.sys_management.OutDataService;
import cn.edu.cqupt.service.transact_business.ContractHandleService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class StatisticsQueryServlet extends HttpServlet {
	// 因为是单机用户，所以可以把申明放到这里来
	private ApplyFormOperation applyFormOperationService = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public StatisticsQueryServlet() {
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

		if (version.equals("1")) {
			curFolder = "qy";
			forwardByOperation(request, response, version);
		} else if (version.equals("2")) {
			curFolder = "jds";
			forwardByOperation(request, response, version);
		} else if (version.equals("3")) {
			curFolder = "jdj";
			forwardByOperation(request, response, version);
		} else if (version.equals("4")) {
			curFolder = "zhj";
			forwardByOperation(request, response, version);
		}
	}

	public void forwardByOperation(HttpServletRequest request,
			HttpServletResponse response, String version)
			throws ServletException, IOException {
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();// 返回值
		String operate = request.getParameter("operate");
		String path = "";

		if (operate.equals("contractStatistic")) {
			List<Contract> contractList = this.contractStatistic(request,
					response);
			request.setAttribute("contractList", contractList);
			path = "/jsp/" + curFolder
					+ "/query_business/contract_statistic.jsp";
		} else if (operate.equals("productStatistic")) {
			T = productStatistic(request, response, version);
			path = "/jsp/" + curFolder
					+ "/query_business/product_statistic.jsp";
		} else if (operate.equals("contractOprateDetail")) {
			contractOprateDetail(request, response);
			return;
		} else if (operate.equals("productOprateDetail")) {
			productOperateDetail(request, response);
			return;
		} else if (operate.equals("exportSingleForm")) {
			OutStatistics(request, response, version);
			return;
		} else if (operate.equals("download")) {
			DownloadFile(request, response);
			return;
		} else if(operate.equals("outInfoByProd")){
			OutInfoByProd(request,response);
			return;
		}
		// else if (operate.equals("LoadIn")) {
		// loadIn(request, response);
		// String pagename = request.getParameter("pagename");
		// if (pagename.equals("contract")) {
		// T = contractStatistic(request, response);
		// path = "/jsp/" + curFolder
		// + "/query_business/contract_statistic.jsp";
		// } else if (pagename.equals("product")) {
		// T = productStatistic(request, response);
		// path = "/jsp/" + curFolder
		// + "/query_business/product_statistic.jsp";
		// }
		// }

		if (!"".equals(path)) {
			request.setAttribute("message", T);
			request.getRequestDispatcher(path).forward(request, response);
		}
	}

	//按合同统计
	public List<Contract> contractStatistic(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> condition = new HashMap<String, String>();
		String contractId = request.getParameter("contractId") == null ? ""
				: request.getParameter("contractId");
		String JDS = request.getParameter("JDS") == null ? "" : request
				.getParameter("JDS");
		String signDate = request.getParameter("signDate") == null ? ""
				: request.getParameter("signDate");
		int curPageNum = 1;
		int pageSize = 10;
		if (request.getParameter("curPageNum") != null
				&& request.getParameter("curPageNum") != "")
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		if (request.getParameter("pageSize") != null
				&& request.getParameter("pageSize") != "")
			pageSize = Integer.parseInt(request.getParameter("pageSize"));

		int version = Integer.parseInt(this.getServletContext()
				.getInitParameter("version"));
		String keeper = request.getParameter("keeper") == null ? "all"
				: request.getParameter("keeper");
		String companyStr=this.getCurSearchCompanyStr(version, keeper, request);

		condition.put("contractId", contractId);
		condition.put("JDS", JDS);
		condition.put("signDate", signDate);
		condition.put("keeper", companyStr);

		ContractHandleService chs = new ContractHandleService();
		List<Contract> contractList = chs.queryContract(condition, curPageNum,
				pageSize);
		int sum = chs.querySum(condition);
		double contractPriceSum = chs.getContractPriceSum(condition); // 合同总金额
		double contractPricePageSum = 0.0;
		for (Contract eachContract : contractList) {
			contractPricePageSum += eachContract.getContractPrice();
		}
		DecimalFormat df = new DecimalFormat("#.00");

		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("contractPriceSum", df.format(contractPriceSum));
		request.setAttribute("contractPricePageSum",
				df.format(contractPricePageSum));
		request.setAttribute("contractId", contractId);
		request.setAttribute("JDS", JDS);
		request.setAttribute("signDate", signDate);
		request.setAttribute("sum", sum);
		request.setAttribute("keeper", keeper);
		return contractList;
	}

	//按设备统计
	public List<Map<String, String>> productStatistic(
			HttpServletRequest request, HttpServletResponse response,
			String version) {
		Map<String, String> parameter = new HashMap<String, String>();
		List<Map<String, String>> productStasList = new ArrayList<Map<String, String>>();
		int productSum = 0;
		String productModel = request.getParameter("productModel") == null ? ""
				: request.getParameter("productModel");
		String productUnit = request.getParameter("productUnit") == null ? ""
				: request.getParameter("productUnit");
		String curPageNum = request.getParameter("curPageNum") == null ? "1"
				: request.getParameter("curPageNum");
		String pageSize = request.getParameter("pageSize") == null ? "10"
				: request.getParameter("pageSize");
		parameter.put("productModel", productModel);
		parameter.put("productUnit", productUnit);
		parameter.put("curPageNum", curPageNum);
		parameter.put("pageSize", pageSize);
		int version2 = Integer.parseInt(this.getServletContext()
				.getInitParameter("version"));
		String keeper = request.getParameter("keeper") == null ? "all"
				: request.getParameter("keeper");
		String companyStr=this.getCurSearchCompanyStr(version2, keeper, request);
		parameter.put("keeper", companyStr)	;
		try {
			ProductService ps = new ProductService();
			productStasList = ps.productSearchStatistic(parameter);
			productSum = ps.getProductQueryStatisticSum(parameter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("curPageNum", Integer.parseInt(curPageNum));
		request.setAttribute("pageSize", Integer.parseInt(pageSize));
		request.setAttribute("productModel", productModel);
		request.setAttribute("productUnit", productUnit);
		request.setAttribute("sum", productSum);
		return productStasList;
	}
	
	//按合同统计内层，合同下每个产品的信息
	public List<Map<String, String>> contractOprateDetail(
			HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, String>> contractProductInfoList = new ArrayList<Map<String, String>>();
		String contractId = request.getParameter("contractId");
		try {
			contractProductInfoList = new ContractService()
					.contractHandleDetail(contractId);
			JSONArray jsonArray = JSONArray.fromObject(contractProductInfoList);
			response.getWriter().write(jsonArray.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contractProductInfoList;
	}

	//按设备统计的内层，设备在不同合同下的信息
	public void productOperateDetail(HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		String productModel = request.getParameter("productModel");
		String productUnit = request.getParameter("productUnit");
		try {
			T = new ProductService().productHandleDetail(productModel,
					productUnit);
			JSONArray jsonArray = JSONArray.fromObject(T);
			response.getWriter().write(jsonArray.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//统计当前合同下每个产品的出库信息
	public void OutInfoByProd(HttpServletRequest request,HttpServletResponse response){
		String productModel=request.getParameter("productModel")==null?"":request.getParameter("productModel");
		String productUnit=request.getParameter("productUnit")==null?"":request.getParameter("productUnit");
		String operateTime=request.getParameter("operateTime")==null?"":request.getParameter("operateTime");
		HashMap<String, String> condition=new HashMap<String, String>();
		if(!"".equals(productModel))
			condition.put("productModel", productModel);
		if(!"".equals(productUnit))
			condition.put("productUnit", productUnit);
		if(!"".equals(operateTime))
			condition.put("operateTime", operateTime);
		try {
			ContractService cs = new ContractService();
			List<HashMap<String, String>> outInfoByProdList=cs.OutInfoByProd(condition);
			JSONArray jarray=JSONArray.fromObject(outInfoByProdList);
			response.getWriter().write(jarray.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 导出表单数据
	 * 
	 * @throws SQLException
	 * */
	public void OutStatistics(HttpServletRequest request,
			HttpServletResponse response, String version)
			throws ServletException, IOException {
		String ownedUnit = (String) request.getSession().getAttribute(
				"ownedUnit");
		OutDataService outDataService = new OutDataService();
		String jsonStr = request.getParameter("outData");
		System.out.println("jsonStr:" + jsonStr);// //////////////////////////////////////////
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		applyFormOperationService = new ApplyFormOperation();
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();
		// 加入标题行
		String pagename = request.getParameter("pagename");
		ArrayList<String> onlineArray = new ArrayList<String>();
		if (pagename.equals("contract_statistics")) {
			String[] onlineList = { "序号", "合同编号", "产品名称", "单价", "定货数量", "合同金额",
					"设备数量", "设备金额", "出库数量", "入库时间", "承制单位", "代储单位", "JD室",
					"签订日期", "操作明细" };
			for (int i = 0; i < onlineList.length; i++) {
				onlineArray.add(onlineList[i]);
			}
		} else if (pagename.equals("product_statistics")) {
			String[] onlineList = { "序号", "设备名称", "单元名称", "规格", "单价", "库存数量",
					"出库数量", "库存金额", "承制单位", "代储单位", "订货数量", "合同总金额", "明细" };
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
		 * 2、把二维数组中的数据生成一个excel表中，并放在exportFormTempFile文件夹中 3、返回路径和文件名
		 */
		String tempFilePath = "uploadFilePlace";
		//changed by LiangYH 11/17
		tempFilePath = request.getSession().getServletContext().getRealPath("/")+File.separator+tempFilePath;
		// 判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
		File tempFile = new File(tempFilePath);
		if (!tempFile.exists() && !tempFile.isDirectory()) {
			tempFile.mkdir();
		}
		if (pagename.equals("contract_statistics")) {
			String fileName = "按合同统计查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
			String absolutePath = tempFilePath + File.separator + fileName;
			outDataService.exportData(absolutePath, version, ownedUnit,
					dyadicArray);
			;
			// 这个就是所需的文件名和路径
			response.setContentType("text/plain;charset=UTF-8");
			if (absolutePath != null && !"".equals(absolutePath)) {
				response.getWriter().write(absolutePath);
			} else {
				response.getWriter().write(0);
			}
		} else if (pagename.equals("product_statistics")) {
			String fileName = "按设备统计查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
			String absolutePath = tempFilePath + File.separator + fileName;
			outDataService.exportData(absolutePath, version, ownedUnit,
					dyadicArray);
			// 这个就是所需的文件名和路径
			response.setContentType("text/plain;charset=UTF-8");
			if (absolutePath != null && !"".equals(absolutePath)) {
				response.getWriter().write(absolutePath);
			} else {
				response.getWriter().write(0);
			}
		}
	}

	public void DownloadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		String absolutePath = request.getParameter("absolutePath");
		String pagename = request.getParameter("pagename");
		String fileName = null;
		if (pagename.equals("contract_statistics")) {
			fileName = "按合同统计查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
		} else if (pagename.equals("product_statistics")) {
			fileName = "按设备统计查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
		}
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
			// request.getRequestDispatcher("/message.jsp").forward(request,
			// response);
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
	public void loadIn(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 上传导入文件到服务器中
		UploadFile uploadFile = new UploadFile();
		Map<String, String> map = uploadFile.uploadFile(request, response);
		List<String> words = new ArrayList<String>();

		// 此判断防止没有选择文件的时候出现异常
		if (map != null && map.size() != 0) {
			// //文件上传之后在服务器中的路径
			String filePath = map.get("fileName");
			// 将上传的文件导入到内存中，返回一个二维数组
			OutDataService outDataService = new OutDataService();
			boolean flag = (Boolean) outDataService.importData(filePath, 8, "")
					.get("flag");
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
	}
	
	/**
	 * 根据版本和选择的军代局、军代室或代储企业得到代储企业字符串
	 * @param version
	 * @return
	 */
	public String getCurSearchCompanyStr(int version,String keeper,HttpServletRequest request){
		String ownedUnit = (String) request.getSession().getAttribute(
				"ownedUnit");
		ArrayList<String> companyList = new ArrayList<String>();
		StringBuffer companySb = new StringBuffer();
		String companyStr = "";
		// 根据版本号传代储企业。。。
		switch (version) {
		case 1:{
			companyStr="'"+ownedUnit+"'";
			break;
		}
		case 2:
			//军代室版
			if ("all".equals(keeper)) {
				companyList = new InfoService()
						.getCompanyNameList(ownedUnit, 2);
				for (String company : companyList) {
					companySb.append("'" + company + "',");
				}
				companyStr = companySb.toString();
				//changed by LiangYH;增加了if判断
				if(companyStr.length() > 0){
					companyStr = companyStr.substring(0, companyStr.length() - 1);
				}
			}
			else{
				companyStr="'"+keeper+"'";
			}
			break;
		case 3:
			//军代室版
			if ("all".equals(keeper)) {
				ArrayList<String> jdsList=new InfoService().getJDSNameList(ownedUnit);
				for (String jds : jdsList) {
					companyList = new InfoService().getCompanyNameList(jds, 2);
					for (String company : companyList) {
						companySb.append("'" + company + "',");
					}
				}
			}
			else{
				companyList = new InfoService().getCompanyNameList(keeper, 2);
				for (String company : companyList) {
					companySb.append("'" + company + "',");
				}
			}
			companyStr = companySb.toString();
			companyStr = companyStr.substring(0, companyStr.length() - 1);
			break;
		case 4:
			//指挥局版
			companyList = new InfoService().getCompanyNameList(ownedUnit, 4);
			for (String company : companyList) {
				companySb.append("'" + company + "',");
			}
			companyStr = companySb.toString();
			companyStr = companyStr.substring(0, companyStr.length() - 1);
//		default:
//			companyStr="'"+ownedUnit+"'";
//			break;
		}
		return companyStr;
	}
}
