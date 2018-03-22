package cn.edu.cqupt.controller.query_business;

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
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.service.query_business.ApplyFormOperation;
import cn.edu.cqupt.service.query_business.UpdateDetailService;
import cn.edu.cqupt.service.query_business.UpdateQueryService;
import cn.edu.cqupt.service.sys_management.OutDataService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class UpdateQueryServlet extends HttpServlet {
	//因为是单机用户，所以可以把申明放到这里来
			private ApplyFormOperation applyFormOperationService = null;
			private UploadFile uploadFile = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public UpdateQueryServlet() {
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
		String jds = (String)request.getSession().getAttribute("ownedUnit");
		List<String> companys = new InfoService().getCompanyNameList(jds, 2);
		
		// version==1表示是企业版
		if (version.equals("1")) {
			curFolder="qy";
			this.forwardByoperation(request, response, operate,version, companys);
		}
		// version==2表示是军代室版
		else if(version.equals("2")){
			curFolder="jds";
			this.forwardByoperation(request, response, operate,version, companys);
		}
		else if(version.equals("3")){
			curFolder="jdj";
			this.forwardByoperation(request, response, operate,version, companys);
		}
		else{
			curFolder="zhj";
			this.forwardByoperation(request, response, operate,version, companys);
		}
	}
	
	private void forwardByoperation(HttpServletRequest request, HttpServletResponse response,
			String operate,String version, List<String> companys) throws ServletException, IOException {
		
		if(operate.equals("updateQuery")){
			 path="/jsp/" + curFolder + "/query_business/update_query.jsp";
			this.updateQuery(request, response, path,version, companys);		
			return;
		}
		if(operate.equals("updateQueryByProductModel")){
			 path="/jsp/" + curFolder + "/query_business/showDetailByProM.jsp";
			this.updateQueryByProductModel(request, response, path, version);
			return;					
		}	
		if(operate.equals("updateQueryByContractId")){
			path="/jsp/" + curFolder + "/query_business/showDetailByCon.jsp";
			this.updateQueryByContractId(request, response, path, version);
			return;				
			
		}else if(operate.equals("updateQueryDetailByProductId")){
			path="/jsp/" + curFolder + "/query_business/showDetailByCon.jsp";
			this.updateQueryDetailByProductId(request, response, path, version);
			return;
		}else if(operate.equals("updateQueryDetailByProductModel")){
			path="/jsp/" + curFolder + "/query_business/showDetailByProM.jsp";
			this.updateQueryDetailByProductModel(request, response, path, version);
			return;
		}
		else if(operate.equals("exportSingleForm")){
			OutUpdate(request, response, version);
			return;
		}else if(operate.equals("download")){
			DownloadFile(request, response);
			return;
		}else if(operate.equals("LoadInUpdate")){
			LoadInUpdate(request, response);
			path="/jsp/" + curFolder + "/query_business/update_query.jsp";
			this.updateQuery(request, response, path,version,companys);
		}else if(!"".equals(path)) {
			request.getRequestDispatcher(path).forward(request, response);
		}		
		
	}

	private void updateQueryByProductModel(HttpServletRequest request, HttpServletResponse response,String path,String version) throws ServletException, IOException{
		
		HashMap<String, Object> condition=new HashMap<String, Object>();  //条件	
		String ProductModel = request.getParameter("ProductModel");
		if(ProductModel == null){
			ProductModel = "";
		}
		String fromdate = request.getParameter("fromdate");
		if(fromdate == null){
			fromdate = "";
		}
		String todate = request.getParameter("todate");
		if(todate == null){
			todate = "";
		}
		String queryType = request.getParameter("queryType");
		if(queryType == null){
			queryType = "";
		}
		String productName = request.getParameter("productName");
		if(productName == null){
			productName = "";
		}
		String productUnit = request.getParameter("productUnit");
		if(productUnit == null){
			productUnit = "";
		}
		String measureUnit = request.getParameter("measureUnit");
		if(measureUnit == null){
			measureUnit = "";
		}
		String productPrice = request.getParameter("productPrice");
		if(productPrice == null){
			productPrice = "";
		}
		String manufacturer = request.getParameter("manufacturer");
		if(manufacturer == null){
			manufacturer = "";
		}
		String keeper = request.getParameter("keeper");
		if(keeper == null){
			keeper = "";
		}		
		String jds=new String();
		if(version.equals("3")||version.equals("4")){
			jds=request.getParameter("JDS");
			if(jds == null){
				jds = "";
			}
		}
		condition.put("ProductModel", ProductModel);
		condition.put("fromdate", fromdate);
		condition.put("todate", todate);
		condition.put("productName", productName);
		condition.put("productUnit", productUnit);
		condition.put("measureUnit", measureUnit);
		condition.put("productPrice", productPrice);
		condition.put("manufacturer", manufacturer);
		condition.put("keeper", keeper);		
		condition.put("queryType",queryType);
		condition.put("jds", jds);
	
		UpdateQueryService updateQS = new UpdateQueryService();
		List<Map<String, String>> recordList = updateQS.selectDetailByProductModel(condition);	   //结果集
		
		// 若查询正常，则将数据传给前端
		request.setAttribute("recordList", recordList);
		request.getRequestDispatcher(path).forward(request, response);  //将数据传给前端			
	}

	private void updateQueryDetailByProductId(HttpServletRequest request, HttpServletResponse response,String path,String version) 
			throws ServletException, IOException
	{
		String productId = request.getParameter("productId");
		String pageSize = request.getParameter("pageSize");
		String curPageNum = request.getParameter("curPageNum");
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("curPageNum", curPageNum);
		condition.put("pageSize", pageSize);
		UpdateDetailService service = new UpdateDetailService();
		List<HashMap<String, String>> recordList = service.getInfoByProductId(productId, condition);
		int sum = service.getInfoByProductIdCount(productId, condition);
		request.setAttribute("recordList", recordList);
		request.setAttribute("sum", sum);
		request.setAttribute("productId", productId);
		request.setAttribute("condition", condition);
		request.getRequestDispatcher(path).forward(request, response);  //将数据传给前端	
	}
	
	private void updateQueryDetailByProductModel(HttpServletRequest request, HttpServletResponse response,String path,String version) 
			throws ServletException, IOException
	{
		String productModel = request.getParameter("productModel");
		String pageSize = request.getParameter("pageSize");
		String curPageNum = request.getParameter("curPageNum");
		UpdateDetailService service = new UpdateDetailService();
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("curPageNum", curPageNum);
		condition.put("pageSize", pageSize);
		List<HashMap<String, String>> recordList = service.getInfoByProductModel(productModel, condition);
		int sum = service.getInfoByProductModelCount(productModel, condition);
		request.setAttribute("recordList", recordList);
		request.setAttribute("sum", sum);
		request.setAttribute("productModel", productModel);
		request.setAttribute("condition", condition);
		request.getRequestDispatcher(path).forward(request, response);  //将数据传给前端	
	}
	
	private void updateQueryByContractId(HttpServletRequest request, HttpServletResponse response,String path,String version) throws ServletException, IOException{

		HashMap<String, Object> condition=new HashMap<String, Object>();  //条件	
		String contractId = request.getParameter("contractId");
		if(contractId == null){
			contractId = "";
		}
		String fromdate = request.getParameter("fromdate");
		if(fromdate == null){
			fromdate = "";
		}
		String todate = request.getParameter("todate");
		if(todate == null){
			todate = "";
		}
		String queryType = request.getParameter("queryType");
		if(queryType == null){
			queryType = "";
		}		
		String jds=new String();
		if(version.equals("3")||version.equals("4")){
			jds=request.getParameter("JDS");
			if(jds == null){
				jds = "";
			}
		}
		condition.put("contractId", contractId);
		condition.put("fromdate", fromdate);
		condition.put("todate", todate);
		condition.put("queryType",queryType);
		condition.put("jds", jds);
		
		UpdateQueryService updateQS = new UpdateQueryService();            
		List<Map<String, String>> recordList = updateQS.selectDetailByContractId(condition);	   //结果集	
		// 若查询正常，则将数据传给前端
		request.setAttribute("recordList", recordList);
		request.getRequestDispatcher(path).forward(request, response);  //将数据传给前端			
	}	
	
	/**
	 * 导出表单数据
	 * @throws SQLException 
	 * */
	public void OutUpdate(HttpServletRequest request, HttpServletResponse response, String version) throws ServletException, IOException{
		String ownedUnit = (String) request.getSession().getAttribute("ownedUnit");
//		System.out.println("ownedUnit:"+ownedUnit);/////////////////////////
		OutDataService outDataService=new OutDataService();
		String jsonStr = request.getParameter("outData");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
//		System.out.println("jsonStr:"+jsonStr);///////////////////////////////
		applyFormOperationService = new ApplyFormOperation();
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();
		// 加入标题行
		ArrayList<String> onlineArray = new ArrayList<String>();
			String[] onlineList = { "序号", "合同编号", "设备名称", "单元名称",
				"规格", "单价", "库存数量","承制单位","代储单位","订货数量","合同总金额","操作明细"};
				for (int i = 0; i < onlineList.length; i++) {
					onlineArray.add(onlineList[i]);
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
		tempFilePath = request.getSession().getServletContext().getRealPath("/") +File.separator+tempFilePath;
		//判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists() && !tempFile.isDirectory()){
			tempFile.mkdir();
		}
		String fileName="LHUpdateExportFile"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
		String absolutePath = tempFilePath+File.separator+fileName;
		outDataService.exportData(absolutePath,version,ownedUnit,dyadicArray);;
		//这个就是所需的文件名和路径
		response.setContentType("text/plain;charset=UTF-8");
		if(absolutePath != null && !"".equals(absolutePath)) {
			response.getWriter().write(absolutePath);
		}else {
			response.getWriter().write(0);
		}
		}
	public void DownloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		String absolutePath = request.getParameter("absolutePath");
		String fileName = null;
		fileName="轮换更新查询导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
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
		try {
			while((len=in.read(buff))>0) {
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
		if(file.exists()) {
			//删除文件
			file.delete();
		}
	}
	public void updateQuery(HttpServletRequest request, HttpServletResponse response,
			String path,String version, List<String> companys) throws ServletException, IOException{

//		System.out.println(path + "," + version);
		HashMap<String, Object> condition=new HashMap<String, Object>();  //条件
		String ownedUnit = request.getParameter("keeper");
		if(version.equals("1"))
			ownedUnit = "";
		if(ownedUnit == null)
			ownedUnit = "";
		String productModel = request.getParameter("productModel");
		if(productModel == null){
			productModel = "";
		}
		String fromdate = request.getParameter("fromdate");
		if(fromdate == null){
			fromdate = "";
		}
		String todate = request.getParameter("todate");
		if(todate == null){
			todate = "";
		}
		String queryType = request.getParameter("queryType");
		if(queryType == null)
			queryType = "";
		else if(queryType.equals("1"))
			queryType = "更新";
		else if(queryType.equals("2"))
			queryType = "轮换";
		else
			queryType = "";
		String jds=new String();
		if(version.equals("3")||version.equals("4")){
			jds=request.getParameter("JDS");
			if(jds == null){
				jds = "";
			}
		}
		String curPageNum = request.getParameter("curPageNum");//页面传过来的“第几页”
		String pageSize = request.getParameter("pageSize");

		condition.put("productModel", productModel);
		condition.put("fromdate", fromdate);
		condition.put("todate", todate);
		condition.put("queryType",queryType);
		condition.put("jds", jds);
		condition.put("curPageNum", curPageNum);
		condition.put("pageSize", pageSize);
		condition.put("sum", 0);
		condition.put("ownedUnit", ownedUnit);
//		System.out.println("condition:" + condition);
		UpdateQueryService updateQS = new UpdateQueryService();            
		int sum = updateQS.selectBorrowAndUpdateCount(condition);	    //全部信息的个数
		List<Map<String, Object>> list = updateQS.selectBorrowAndUpdate(condition);//结果集
//		for(int i = 0; i < list.size(); i++)
//			System.out.println(list.get(i));
		condition.remove("sum");
		condition.put("sum", sum);	
		//若查询正常，则将数据传给前端
		request.setAttribute("condition", condition);
		request.setAttribute("list", list);
		request.setAttribute("companys", companys);
		request.getRequestDispatcher(path).forward(request, response);  //将数据传给前端	
	} 
	/**
	 * 导入表单数据
	 * @throws SQLException 
	 * */
 private void LoadInUpdate(HttpServletRequest request, HttpServletResponse response){
	// 上传导入文件到服务器中
			uploadFile = new UploadFile();
			Map<String, String> map = uploadFile.uploadFile(request,response);
			List<String> words = new ArrayList<String>();

			// 此判断防止没有选择文件的时候出现异常
			if(map != null && map.size() != 0){
				// //文件上传之后在服务器中的路径
				String filePath = map.get("fileName");
				// 将上传的文件导入到内存中，返回一个二维数组
				OutDataService outdataservice = new OutDataService();
			boolean	flag=(Boolean)outdataservice.importData(filePath, 8, "").get("flag");
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
}
