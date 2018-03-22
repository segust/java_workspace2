package cn.edu.cqupt.controller.transact_business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.xmlbeans.xml.stream.ChangePrefixMapping;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.OutList;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.service.query_business.ProductDetailService;
import cn.edu.cqupt.service.sys_management.HandleServiceOfBaseData;
import cn.edu.cqupt.service.transact_business.ApplyFormOperation;
import cn.edu.cqupt.service.transact_business.ApplyHandleService;
import cn.edu.cqupt.service.transact_business.OutListHandleService;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.DownloadFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class OutWarehouseServlet extends HttpServlet {
	
	private OutListHandleService outListHandleService = null;
	private ApplyFormOperation applyFormOperation = null;
	private UploadFile uploadFile = null;
	
	
	public void init() throws ServletException {
		outListHandleService = new OutListHandleService();
		applyFormOperation = new ApplyFormOperation();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String operate = request.getParameter("operate");
		System.out.println("OutWarehouseServlet operate = "+operate);
		String path ="";
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		//version==1表示是企业版
		if("1".equals(version)) {
			//举例：判断当前用户是否具有 业务办理  权限
			if(CurrentUser.isContractManage(request)){
				if("outWarehouse".equals(operate)) {
					request.setAttribute("ischeck", false);
				    path = "/jsp/qy/transact_business/OutBusiness.jsp";
				}else if("checkout".equals(operate)) {
//					List<OutList> lists = getWordInfo(request, response);
//					if(lists.size() >0){
//						//存入料单数据库
//						boolean isSave = saveOutList(lists);
//						List<OutList> queryLists = findLists(lists.get(0).getListId(),MyDateFormat.changeDateToLongString(lists.get(0).getDate()));
//						ProductHandleService service = new ProductHandleService();
//						List<Map<String,String>> conditions = getOutConditions(queryLists);
//						List<List<Product>> products = service.getOutPros(conditions);
//						//ChangeProStatus();
//						//addRelation();
//						//修改日志
//						doLog(request,"导入发料单","将发料单同步于本地数据库");
//						request.setAttribute("isSave",isSave);
//						request.setAttribute("ischeck", true);
//						request.setAttribute("products", products);
//						request.setAttribute("isSame", (lists.equals(queryLists) && queryLists.size() ==products.size()));
//						request.setAttribute("queryLists", queryLists);
//					}
//					 path = "/jsp/qy/transact_business/OutBusiness.jsp";
				}else if("confirmOut".equals(operate)) {
					String jsonStr = request.getParameter("data");
					JSONArray jarray = JSONArray.fromObject(jsonStr);
					String[] pId = new String[jarray.size()];
					ProductHandleService pservice = new ProductHandleService();
					for (int i = 0; i < pId.length; i++) {
						Product pro = new Product();
						if(!"".equals(pId[i]) && pId[i] != null)
							pro = pservice.getProByInId(Long.parseLong(pId[i]));
						this.saveLog(request, response, "出库产品", pro.getProductId(), "产品型号："
								+ pro.getProductModel()+",机号："+pro.getDeviceNo());
					}
					String[] status = new String[jarray.size()];
					for (int i = 0; i < jarray.size(); i++) {
						pId[i]= jarray.getString(i);
						status[i] = "已出库";
					}
					//可以用date唯一确定一批发料单
					String date = request.getParameter("date");
					boolean flag = false;
					if(saveRealation(pId,date)) {
						flag = changeProStatus(pId,status);
					}
					response.setContentType("text/plain;charset=UTF-8");
					if(flag) {
						response.getWriter().write("1");
						doLog(request,"产品出库","根据发料单查询产品并出库");
					}else {
						response.getWriter().write("0");
					}
				}else if("importOutlist".equals(operate)){
					doLog(request, "导入发料单", "增加或者更新发料单");
					// 导入发料单
					List<OutList> queryLists = importOutlist(request, response, outListHandleService,version);
					boolean isSave = false;
					if(queryLists != null)
						isSave = true;
 					request.setAttribute("ischeck", true);
					request.setAttribute("queryLists", queryLists);
					request.setAttribute("isSave", isSave);
					path="jsp/qy/transact_business/OutBusiness.jsp";

				}
				if(!"".equals(path)) {
					request.getRequestDispatcher(path).forward(request, response);
				}
			}
			
		}else if("2".equals(version)) {
			if(CurrentUser.isContractManage(request)) {
				if("addOutList".equals(operate)) {
					//填写发料单
					String jds = (String)request.getSession().getAttribute("ownedUnit");
					List<String> companys = new InfoService().getCompanyNameList(jds, 2);
					List<HashMap<String,Object>> queryResult = new ArrayList<HashMap<String,Object>>();
					HashMap<String,String> condition = new HashMap<String, String>();
					try {
						queryResult = new ProductDetailService().selectProductDetail(condition, "c","3");
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					doLog(request, "填写发料单", "调拨出库发料单");
					request.setAttribute("queryResult", queryResult);
					request.setAttribute("companys", companys);
					path = "jsp/jds/transact_business/addOutList1.jsp";
				}else if("addBorrowList".equals(operate)){
					//填写轮换出库发料单
					doLog(request, "填写发料单", "轮换出库发料单");
					String jds = (String)request.getSession().getAttribute("ownedUnit");
					List<String> companys = new InfoService().getCompanyNameList(jds, 2);
					List<HashMap<String,Object>> queryResult = new ArrayList<HashMap<String,Object>>();
					HashMap<String,String> condition = new HashMap<String, String>();
					try {
						queryResult = new ProductDetailService().selectProductDetail(condition, "c",version);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					request.setAttribute("queryResult", queryResult);
					request.setAttribute("companys", companys);
					path = "jsp/jds/transact_business/addBorrowOutList.jsp";
				}else if("addUpdateList".equals(operate)){
					//填写轮换出库发料单
					doLog(request, "填写发料单", "更新出库发料单");
					String jds = (String)request.getSession().getAttribute("ownedUnit");
					List<String> companys = new InfoService().getCompanyNameList(jds, 2);
					List<HashMap<String,Object>> queryResult = new ArrayList<HashMap<String,Object>>();
					HashMap<String,String> condition = new HashMap<String, String>();
					try {
						queryResult = new ProductDetailService().selectProductDetail(condition, "c", version);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					request.setAttribute("queryResult", queryResult);
					request.setAttribute("companys", companys);
					path = "jsp/jds/transact_business/addUpdateOutList.jsp";
				}else if("addBorrowList".equals(operate)) {
					path="jsp/jds/transact_business/addBorrowOutList.jsp";
				}else if("addUpdateList".equals(operate)) {
					path="jsp/jds/transact_business/addUpdateOutList.jsp";
				}else if("outWarehouse".equals(operate)) {
					request.setAttribute("ischeck", false);
				    path = "/jsp/jds/transact_business/OutBusiness.jsp";
				}else if("goForkeeper".equals(operate)) {
					int curPageNum =1;
					String backData = request.getParameter("curPageNum");
					if(!"".equals(backData) && backData != null) {
						curPageNum = Integer.parseInt(backData);
					}
					String keeper = request.getParameter("keeper").trim();
					List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
					result = new ProductHandleService().findAllPmnmByKeeper3(keeper,curPageNum);
					JSONArray jarray = new JSONArray();
					int sum = 0;
					if(result != null) {
						for(int i=0;i<result.size();i++) {
							JSONArray temp = new JSONArray();
							JSONArray tempIds = JSONArray.fromObject((List<String>)result.get(i).get("productId"));
							int j=0;
							temp.add(j++, tempIds);
							temp.add(j++, (curPageNum -1)*10+(i+1));
							temp.add(j++, result.get(i).get("productName"));
							temp.add(j++, result.get(i).get("PMNM"));
							temp.add(j++, result.get(i).get("productModel"));
							temp.add(j++, result.get(i).get("productUnit"));
							temp.add(j++, result.get(i).get("deviceNo"));
							temp.add(j++, result.get(i).get("productPrice"));
							temp.add(j++, result.get(i).get("measureUnit"));
							temp.add(j++, result.get(i).get("count"));
							temp.add(j++, result.get(i).get("storageTime"));
							temp.add(j++, result.get(i).get("manufacturer"));
							temp.add(j++, result.get(i).get("keeper"));
							temp.add(j++, result.get(i).get("remark"));
							jarray.add(temp);
						}
						sum =  result.size()%10==0?(result.size()/10):(result.size()/10+1);
					}
					Map<String,Object> returnData = new HashMap<String, Object>();
					returnData.put("nowPage", curPageNum);
					returnData.put("totalPage", sum);
					returnData.put("items", jarray);
					JSONObject returnJo = JSONObject.fromObject(returnData); 
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(returnJo.toString());
					path="";
				}else if("importOutlist".equals(operate)) {
					//导入发料单
					//20150716 testok
					List<OutList> queryLists = importOutlist(request, response, outListHandleService,version);
					boolean isSave = false;
					if(queryLists != null)
						isSave = true;
 					request.setAttribute("ischeck", true);
					request.setAttribute("queryLists", queryLists);
					request.setAttribute("isSave", isSave);
					path="jsp/jds/transact_business/OutBusiness.jsp";
				}else if("addAddOutList".equals(operate)){
//					path = "jsp/jds/transact_business/addOutList.jsp";
				}else if("showOutList".equals(operate)) {
//					//显示发料单信息
//					List<OutList> lists = getWordInfo(request, response);
//					if(lists.size() >0){
//						//存入料单数据库
//						boolean isSave = saveOutList(lists);
//						List<OutList> queryLists = findLists(lists.get(0).getListId(),MyDateFormat.changeDateToLongString(lists.get(0).getDate()));
//						//修改日志
//						doLog(request,"导入发料单","将发料单同步于本地数据库");
//						request.setAttribute("ischeck", true);
//						request.setAttribute("isSave", isSave);
//						request.setAttribute("queryLists", queryLists);
//					}
//					path = "jsp/jds/transact_business/OutBusiness.jsp";
				}else if("ExportOutListFileJDS".equals(operate)){
					/**
					 * 军代室导出出料单
					 */
					exportOutlistFile(request, response,applyFormOperation,outListHandleService);
					
					return;
				}else if("downloadOutlistFile".equals(operate)){
					/**
					 * 军代室下载发料单
					 */
					String absolutePath = request.getParameter("path");
					
					downloadFile(request, response, absolutePath);
					
					//删除所有文件
					int index = -1;
					if((index = absolutePath.lastIndexOf("\\")) == -1){
						index = absolutePath.lastIndexOf("/");
					}
					this.delAllFile(absolutePath.substring(0, index));
					return ;
				}else if("importOutList".equals(operate)){
					// 导入发料单
					importOutlist(request, response, outListHandleService,version);
					//日志
					return ;
				}
				if(!"".equals(path)) {
					request.getRequestDispatcher(path).forward(request, response);
				}
			}
		}else if("3".equals(version)) {
			if(CurrentUser.isContractManage(request)) {
				if("outListQuery".equals(operate)) {
					OutListHandleService outListHandleService = new OutListHandleService();
					//查询出库信息列表
					int curPageNum = 0;
					int pageSize = 0;	
					int Detailsum = outListHandleService.searchListSum();
					if(request.getParameter("curPageNum") == null ||request.getParameter("pageSize") == null) {
						curPageNum = 1;
						pageSize = 10;
					}else {
						curPageNum = Integer.parseInt(request.getParameter("curPageNum").trim());
						pageSize = Integer.parseInt(request.getParameter("pageSize").trim());
					}
					
					List<HashMap<String,Object>> inApplyList = outListHandleService.searcheList(curPageNum, pageSize);
					
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("Detailsum", Detailsum);
					request.setAttribute("inApplyList", inApplyList);
					path="/jsp/jdj/transact_business/outListQuery.jsp?curPageNum="+curPageNum+"&pageSize="+pageSize+"&Detailsum="+Detailsum;
				}else if("outWarehouse".equals(operate)) {
					request.setAttribute("ischeck", false);
				    path = "/jsp/jdj/transact_business/OutBusiness.jsp";
				}else if("importOutList".equals(operate)) {
					//导入发料单
					path="/jsp/jdj/transact_business/importOutList.jsp";
				}else if("importOutlist".equals(operate)){
					// 军代局导入发料单
					List<OutList> queryLists = importOutlist(request, response, outListHandleService,version);
					boolean isSave = false;
					if(queryLists != null)
						isSave = true;
 					request.setAttribute("ischeck", true);
					request.setAttribute("queryLists", queryLists);
					request.setAttribute("isSave", isSave);
					path="jsp/jdj/transact_business/OutBusiness.jsp";
				}
			}
			if(!"".equals(path)) {
				request.getRequestDispatcher(path).forward(request, response);
			}
		}else if("4".equals(version)) {
			if(CurrentUser.isContractManage(request)) {
				if("outListQuery".equals(operate)) {
					//查询出库信息列表
					OutListHandleService outListHandleService = new OutListHandleService();
					int curPageNum = 0;
					int pageSize = 0;	
					int Detailsum = outListHandleService.searchListSum();
					if(request.getParameter("curPageNum") == null ||request.getParameter("pageSize") == null) {
						curPageNum = 1;
						pageSize = 10;
					}else {
						curPageNum = Integer.parseInt(request.getParameter("curPageNum").trim());
						pageSize = Integer.parseInt(request.getParameter("pageSize").trim());
					}
					
					List<HashMap<String,Object>> inApplyList = outListHandleService.searcheList(curPageNum, pageSize);
					
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("Detailsum", Detailsum);
					request.setAttribute("inApplyList", inApplyList);
					path="/jsp/jdj/transact_business/outListQuery.jsp?curPageNum="+curPageNum+"&pageSize="+pageSize+"&Detailsum="+Detailsum;
				}else if("addOutList".equals(operate)) {
					//填写发料单
					doLog(request, "填写发料单", "调拨出库发料单");
					String zhj = (String)request.getSession().getAttribute("ownedUnit");
					List<String> companys = new InfoService().getCompanyNameList(zhj, 4);
					List<HashMap<String,Object>> queryResult = new ArrayList<HashMap<String,Object>>();
					HashMap<String,String> condition = new HashMap<String, String>();
					try {
						queryResult = new ProductDetailService().selectProductDetail(condition, "c","3");
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					request.setAttribute("queryResult", queryResult);
					request.setAttribute("companys", companys);
					path="/jsp/zhj/transact_business/addOutList1.jsp";
				}else if("goForkeeper".equals(operate)) {
					int curPageNum =1;
					String backData = request.getParameter("curPageNum");
					if(!"".equals(backData) && backData != null) {
						curPageNum = Integer.parseInt(backData);
					}
					String keeper = request.getParameter("keeper").trim();
					List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
					result = new ProductHandleService().findAllPmnmByKeeper3(keeper,curPageNum);
					JSONArray jarray = new JSONArray();
					int sum = 0;
					if(result != null) {
						for(int i=0;i<result.size();i++) {
							JSONArray temp = new JSONArray();
							JSONArray tempIds = JSONArray.fromObject((List<String>)result.get(i).get("productId"));
							int j=0;
							temp.add(j++, tempIds);
							temp.add(j++, (curPageNum -1)*10+(i+1));
							temp.add(j++, result.get(i).get("productName"));
							temp.add(j++, result.get(i).get("PMNM"));
							temp.add(j++, result.get(i).get("productModel"));
							temp.add(j++, result.get(i).get("productUnit"));
							temp.add(j++, result.get(i).get("deviceNo"));
							temp.add(j++, result.get(i).get("productPrice"));
							temp.add(j++, result.get(i).get("measureUnit"));
							temp.add(j++, result.get(i).get("count"));
							temp.add(j++, result.get(i).get("storageTime"));
							temp.add(j++, result.get(i).get("manufacturer"));
							temp.add(j++, result.get(i).get("keeper"));
							temp.add(j++, result.get(i).get("remark"));
							jarray.add(temp);
						}
						sum =  result.size()%10==0?(result.size()/10):(result.size()/10+1);
					}
					Map<String,Object> returnData = new HashMap<String, Object>();
					returnData.put("nowPage", curPageNum);
					returnData.put("totalPage", sum);
					returnData.put("items", jarray);
					JSONObject returnJo = JSONObject.fromObject(returnData); 
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(returnJo.toString());
					path="";
				}else if("addBorrowList".equals(operate)){
					//填写轮换出库发料单
					doLog(request, "填写发料单", "轮换出库发料单");
					String zhj = (String)request.getSession().getAttribute("ownedUnit");
					List<String> companys = new InfoService().getCompanyNameList(zhj, 4);
					List<HashMap<String,Object>> queryResult = new ArrayList<HashMap<String,Object>>();
					HashMap<String,String> condition = new HashMap<String, String>();
					try {
						queryResult = new ProductDetailService().selectProductDetail(condition, "c",version);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					request.setAttribute("queryResult", queryResult);
					request.setAttribute("companys", companys);
					path = "jsp/zhj/transact_business/addBorrowOutList.jsp";
				}else if("addUpdateList".equals(operate)){
					//填写轮换出库发料单
					doLog(request, "填写发料单", "更新出库发料单");
					String zhj = (String)request.getSession().getAttribute("ownedUnit");
					List<String> companys = new InfoService().getCompanyNameList(zhj, 4);
					List<HashMap<String,Object>> queryResult = new ArrayList<HashMap<String,Object>>();
					HashMap<String,String> condition = new HashMap<String, String>();
					try {
						queryResult = new ProductDetailService().selectProductDetail(condition, "c", version);
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					request.setAttribute("queryResult", queryResult);
					request.setAttribute("companys", companys);
					path = "jsp/zhj/transact_business/addUpdateOutList.jsp";
				}else if("ExportOutListFileJDS".equals(operate)){
					/**
					 * 指挥局导出出料单
					 */
					exportOutlistFile(request, response,applyFormOperation,outListHandleService);
					
					return;
				}else if("downloadOutlistFile".equals(operate)){
					/**
					 * 指挥局下载发料单
					 */
					String absolutePath = request.getParameter("path");
					
					downloadFile(request, response, absolutePath);
					
					//删除所有文件
					int index = -1;
					if((index = absolutePath.lastIndexOf("\\")) == -1){
						index = absolutePath.lastIndexOf("/");
					}
					this.delAllFile(absolutePath.substring(0, index));
					return ;
				}
//				}else if("importOutlist".equals(operate)){
//					// 指挥局导入发料单
//					uploadFile = new UploadFile();
//					Map<String, String> map = uploadFile.uploadFile(request,response);
//					boolean flag = false;
//					if (map != null && map.size() > 0) {
//						// 文件上传之后在服务器中的路径
//						String filePath = map.get("fileName");
//						Map<Integer,List<ArrayList<String>>> mapDyadic = null;
//						mapDyadic = applyFormOperation.importAllSheetFromExcel(filePath, 2);
//						//操作数据库
//						flag = outListHandleService.insertOutlistAndRelation(mapDyadic.get(0), mapDyadic.get(1));
//					}
//					response.setContentType("text/plain,charset=utf8");
//					if(flag){
//						response.getWriter().write("1");
//					}else{
//						response.getWriter().write("0");
//					}
//					return ;
//				}
			}
			if(!"".equals(path)) {
				request.getRequestDispatcher(path).forward(request, response);
			}
		}//版本4结束
		else {
			
		}
	}

	private boolean saveRealation(String[] pId, String lId) {
		boolean flag = false;
		OutListHandleService service = new OutListHandleService();
		flag = service.addRelation(lId, pId);
		return flag;
	}

	private boolean changeProStatus(String[] pId, String[] status) {
		boolean flag = false;
		ProductHandleService service = new ProductHandleService();
		flag = service.changeProStatus(pId, status);
		return flag;
	}

//	private List<Map<String,String>> getOutConditions(List<OutList> queryLists) {
//		List<Map<String,String>> conditions = new ArrayList<Map<String,String>>();
//		for (OutList list:queryLists) {
//			Map<String, String> condition = new HashMap<String, String>();
//			if(list.getPMNM() != null && !"".equals(list.getPMNM())) {
//				condition.put("PMNM", list.getPMNM());
//			}
//			if(list.getProductModel() != null && !"".equals(list.getProductModel())) {
//				condition.put("productModel", list.getProductModel());
//			}
//			if(list.getUnit() != null && !"".equals(list.getUnit())) {
//				condition.put("productUnit", list.getUnit());
//			}
//			if(list.getPrice() != 0 && Double.toString(list.getPrice()) != null && !"".equals(Double.toString(list.getPrice()))) {
//				condition.put("productPrice", Double.toString(list.getPrice()));
//			}
//			if(list.getRealCount() != null && !"".equals(list.getRealCount())) {
//				condition.put("realNum", list.getRealCount());
//			}
//			conditions.add(condition);
//		}
//		return conditions;
//	}

	private void doLog(HttpServletRequest request,String opertetype,String remark) {
		// 记录日志的代码
		HttpSession session = request.getSession();
		Log log = new Log();
		log.setUserName((String) session.getAttribute("username")); // 当前登录的用户名已经保存在session中
		log.setOperateTime(new Date()); // 记录当前用户进行**操作的时间
		log.setOperateType(opertetype);
		log.setRemark(remark);
		UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	}

//	private List<OutList> findLists(String listId,String date) {
//		List<OutList> lists = outListHandleService.findListById(listId,date);
//		return lists;
//	}

//	private List<OutList> getWordInfo(HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		OutList list = new OutList();
//		List<OutList> lists = new ArrayList<OutList>();
//		UploadFile uploadFile = new UploadFile();
//		Map<String, String> map = uploadFile.uploadFile(request,
//				response);
//		// 文件上传之后在服务器中的路径
//		String filePath = map.get("fileName");
//		ApplyFormOperation applyFormOperationService = new ApplyFormOperation();
//		try {
//			Map<String,String> wordTitle = applyFormOperationService.readWordParagraph(filePath);
//			 List<ArrayList<String>> wordTable = applyFormOperationService.importTableInWordFile(filePath);
//			 for (String key : wordTitle.keySet()) {
//					if("案由文号".equals(key)) {
//						list.setFileNo(wordTitle.get(key));
//					}else if("发料单号".equals(key)) {
//						list.setListId(wordTitle.get(key));
//					}else if("运输方式".equals(key)) {
//						list.setDiliverMean(wordTitle.get(key));
//					}else if("运单编号".equals(key)) {
//						list.setDeliverNo(wordTitle.get(key));
//					}
//				}
//			 list.setOutMeans("出库");
//			 list.setDate(new Date());
//			 for (int i = 1; i < wordTable.size(); i++) {
//				 boolean flag = false;
//				 OutList temp = (OutList) list.clone();
//				 if(wordTable.get(i).get(0) != null && !"".equals(wordTable.get(i).get(0))) {
//					 temp.setOrderId(Integer.parseInt(wordTable.get(i).get(0)));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(1) != null && !"".equals(wordTable.get(i).get(1))) {
//					 temp.setPMNM(wordTable.get(i).get(1));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(2) != null && !"".equals(wordTable.get(i).get(2))) {
//					 temp.setProductModel(wordTable.get(i).get(2));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(3) != null && !"".equals(wordTable.get(i).get(3))) {
//					 temp.setUnit(wordTable.get(i).get(3));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(4) != null && !"".equals(wordTable.get(i).get(4))) {
//					 temp.setQuanlity(wordTable.get(i).get(4));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(5) != null && !"".equals(wordTable.get(i).get(5))) {
//					 temp.setAskCount(wordTable.get(i).get(5));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(6) != null && !"".equals(wordTable.get(i).get(6))) {
//					 temp.setRealCount(wordTable.get(i).get(6));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(7) != null && !"".equals(wordTable.get(i).get(7))) {
//					 temp.setNum(wordTable.get(i).get(7));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(8) != null && !"".equals(wordTable.get(i).get(8))) {
//					 temp.setPrice(Double.parseDouble(wordTable.get(i).get(8)));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(9) != null && !"".equals(wordTable.get(i).get(9))) {
//					 temp.setMoney(Double.parseDouble(wordTable.get(i).get(9)));
//					 flag = true;
//				 }
//				 if(wordTable.get(i).get(10) != null && !"".equals(wordTable.get(i).get(10))) {
//					 temp.setRemark(wordTable.get(i).get(10));
//					 flag = true;
//				 }
//				 if(flag) {
//					 lists.add(temp);
//				 }
//			}
//			 File tempFile = new File(filePath);
//				if (tempFile.exists()) {
//					tempFile.delete();
//				}
//		} catch (InvalidFormatException e) {
//			e.printStackTrace();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		return lists;
//	}



//	private boolean saveOutList(List<OutList> lists) {
//		OutListHandleService service = new OutListHandleService();
//		boolean flag = service.saveList(lists);
//		return flag;
//	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
	
	/**
	 * 通过ant包提供的方法压缩文件
	 * 
	 * @param zipPathName
	 *            压缩zip的目的地址
	 * @param srcPathName
	 *            压缩zip的源文件地址
	 * @param choosePathesArray 选择的资质文件路径
	 * 				        
	 */
	private void compressByAnt(String zipPathName, String srcPathName) {
		File zipFile = new File(zipPathName);
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new RuntimeException(srcPathName + "不存在！");

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		// 包括哪些文件或文件夹
		fileSet.setIncludes("*"); 
		//fileSet.setExcludes(""); //排除哪些文件或文件夹
		zip.addFileset(fileSet);

		zip.execute();
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
	 * 
	 * @param sourceFilePath
	 *            待压缩的文件路径
	 * @param zipFilePath 
	 *            压缩后存放路径
	 * @param fileName
	 *            压缩后文件的名称
	 * @return flag
	 * @author LiangYH
	 */
	public boolean fileToZip(String sourceFilePath, String zipFilePath,
			String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);

		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFile.exists() == false) {
			System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath
					+ " 不存在. <<<<<<");
		} else {
			try {
				File temp = new File(zipFilePath);
				if (!temp.exists() && !temp.isDirectory()) {
					temp.mkdir();
				}

				File zipFile = new File(zipFilePath + "/" + fileName);

				// File[] sourceFiles = sourceFile.listFiles();

				List<File> fileList = loadFileName(sourceFile);

				if (fileList.size() == 0) {
					System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath
							+ " 里面不存在文件,无需压缩. <<<<<<");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(fos);
					for (int i = 0; i < fileList.size(); i++) {
						File file = fileList.get(i);

						String tempFileName = getEntryName(sourceFilePath, file);
						int index = -1;
						if((index = tempFileName.indexOf("/")) == -1){
							index = tempFileName.indexOf("\\");
						}
						tempFileName = tempFileName.substring(index+1);
						// 创建ZIP实体,并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(tempFileName);
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(file);

						int read = 0;
						byte[] b = new byte[1024];
						while ((read = fis.read(b)) != -1) {
							zos.write(b, 0, read);
						}
						if (fis != null) 
							fis.close();
					}
					flag = true;
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}finally{
				try {
					if (zos != null) 
						zos.close();
					if (fos != null) 
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return flag;
	}

	/**
	 * 获取目录下面的所有文件
	 * @param file
	 * @return
	 */
	private List<File> loadFileName(File file) {
		List<File> fileNameList = new ArrayList<File>();
		if (file.isFile()) {
			fileNameList.add(file);
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				//递归
				fileNameList.addAll(loadFileName(f));
			}
		}
		return fileNameList;
	}

	//得到将要压缩在压缩包内的路径
	private static String getEntryName(String base,File file) {
        File baseFile = new File(base);
        
        String filename = file.getPath();
        
        //这种情况：E:/A
        //A的parentFile是E:/
        if(baseFile.getParentFile().getParentFile()==null){
            return filename.substring(baseFile.getParent().length());
        }
        return filename.substring(baseFile.getParent().length()+1);
    }
	
	/**
	 * 删除文件夹下的所有文件和目录
	 * @param path 文件夹完整绝对路径
	 * @return
	 * @author LiangYH
	 */
	public boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	private void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导入发料单
	 * @param request
	 * @param response
	 * @param outListHandleService
	 * @param version
	 * @return
	 * @throws IOException
	 */
	private List<OutList> importOutlist(HttpServletRequest request, 
			HttpServletResponse response,OutListHandleService outListHandleService, String version) throws IOException{
		List<OutList> outList = new ArrayList<OutList>();
		doLog(request, "导入发料单", "");
		Map<Integer,List<ArrayList<String>>> mapDyadic = null;
		uploadFile = new UploadFile();
		Map<String, String> map = uploadFile.uploadFile(request,response);
		boolean flag = false;
		if (map != null && map.size() > 0) {
			//文件上传之后在服务器中的路径
			String filePath = map.get("fileName");
			String productStatus = "";
			String outListType = "";
			//如果不是调拨、更新、轮换发料出库，则表示所选择的文件错误
			if(filePath.indexOf(StringUtil.DIRECT_OUTLIST) != -1){
				productStatus = "调拨待出库";
				outListType = StringUtil.DIRECT_OUTLIST;
			}else if(filePath.indexOf(StringUtil.BORROW_OUTLIST) != -1){
				productStatus = "轮换待出库";
				outListType = StringUtil.BORROW_OUTLIST;
			}else if(filePath.indexOf(StringUtil.UPDATE_OUTLIST) != -1){
				productStatus = "更新待出库";
				outListType = StringUtil.UPDATE_OUTLIST;
			}else{ 
				return null;
			}	
			if(!"1".equals(version))
				productStatus = StringUtil.OUT_WARED;
			
			mapDyadic = applyFormOperation.importAllSheetFromExcel(filePath, 2);
			//操作数据库
			flag = outListHandleService.insertOutlistAndRelation(mapDyadic.get(0), mapDyadic.get(1),productStatus,outListType,version);
		}
		if(flag) {
			List<ArrayList<String>> temp = mapDyadic.get(0);
			for (int i = 1; i < temp.size(); i++) {
				OutList list = new OutList();
				list.setListId(temp.get(i).get(1));
				list.setFileNo(temp.get(i).get(2));
				list.setDeliverNo(temp.get(i).get(3));
				list.setDiliverMean(temp.get(i).get(4));
				list.setPMNM(temp.get(i).get(5));
				list.setProductModel(temp.get(i).get(6));
				list.setOldModel(temp.get(i).get(7));
				list.setUnit(temp.get(i).get(8));
				list.setQuanlity(temp.get(i).get(9));
				list.setAskCount(temp.get(i).get(10));
				list.setRealCount(temp.get(i).get(11));
				list.setNum(temp.get(i).get(12));
				list.setOldNum(temp.get(i).get(13));
				list.setOutMeans(temp.get(i).get(14));
				if(temp.get(i).get(15) != null || !"".equals(temp.get(i).get(15))) {
					list.setMoney(Double.parseDouble(temp.get(i).get(15)));
				}
				list.setRemark(temp.get(i).get(16));
				if(temp.get(i).get(17) != null || !"".equals(temp.get(i).get(17))) {
					list.setPrice(Double.parseDouble(temp.get(i).get(17)));
				}
				list.setDate(MyDateFormat.changeStringToDate(temp.get(i).get(18)));
				if(temp.get(i).get(19) != null || !"".equals(temp.get(i).get(19))) {
					list.setOrderId(Integer.parseInt(temp.get(i).get(19)));
				}
				list.setOwnedUnit(temp.get(i).get(20));
				outList.add(list);
			}
			return outList;
		}else
			return null;
	}
	
	/**
	 * 军代室导出出料单
	 */
	@SuppressWarnings("unchecked")
	public void exportOutlistFile(HttpServletRequest request, HttpServletResponse response,
			ApplyFormOperation applyFormOperation,
			OutListHandleService outListHandleService) throws IOException{
		doLog(request, "导出", "导出出库发料单");
		//word文件上面四个内容
		String header = request.getParameter("header");
		//word文件下面表单的内容
		String tableContent = request.getParameter("content");
		String keeper = request.getParameter("keeper");
		//direct|borrow|update
		String outListType = request.getParameter("outListType");
		
		System.out.println("header = "+header);
		System.out.println("tableContent = "+tableContent);
		//案由文号(AYWH)、发料单号(FLDH)、运输方式(YSFS)、运单编号(YDBH)
		Map<String,String> targetMap = new HashMap<String,String>();
		JSONObject jo = JSONObject.fromObject(header);
		targetMap.put("AYWH", (String) jo.get("AYWH"));
		targetMap.put("FLDH", (String) jo.get("FLDH"));
		targetMap.put("YSFS", (String) jo.get("YSFS"));
		targetMap.put("YDBH", (String) jo.get("YDBH"));
		
		//持有productID
		List<ArrayList<String>> productIDDyadic = new ArrayList<ArrayList<String>>();
		List<ArrayList<String>> targetDyadic = null;
		//解析tableContent
		targetDyadic = resolveJSONToDyadic(tableContent,productIDDyadic);
		
		outListType = translateOutMeans(outListType);
		
		//数据库操作,
		Map<String,Object> resultMap = null;
		//key:runStatus,outList,relation
		resultMap = outListHandleService.operationOutListInJDS(targetMap,targetDyadic,productIDDyadic,keeper,outListType);
		
		boolean runStatus = (Boolean) resultMap.get("runStatus");
		if(runStatus){
			String zipFoldName = "uploadZipFilePlace"; 
			//将要压缩的文件夹的所在目录
			//changed by LiangYH 11/17
			String zipFileDirectory = request.getSession().getServletContext().getRealPath("/")+File.separator+zipFoldName;
			//将要压缩的文件夹名字
			String tempZipFoldName = MyDateFormat.changeDateToTimeStampString(new Date());
			//将要压缩的目录
			String tempZipFileDirectory = zipFileDirectory+File.separator+tempZipFoldName;
			
			File temp = new File(tempZipFileDirectory);
			if(!temp.exists()&&!temp.isDirectory()){
				//新建目录
				temp.mkdirs();
			}
			String ownedUnit = (String)request.getSession().getAttribute("ownedUnit");
			//String downloadFileName = ownedUnit+"_"+applyType+"_发料单"+MyDateFormat.changeDateToFileString(new Date())+".zip";
			//word文件名
			String wordFileName = ownedUnit+"_"+outListType+"_发料单-Word文档"+MyDateFormat.changeDateToFileString(new Date())+".docx";
			//生成word文件
			applyFormOperation.writeXWordFile(tempZipFileDirectory+File.separator+wordFileName, targetMap, targetDyadic);
			//excel文件的名字
			String excelFileName = ownedUnit+"_"+outListType+"_发料单-数据"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
			
			List<ArrayList<String>> outListDyadic = (List<ArrayList<String>>) resultMap.get("outList");
			List<ArrayList<String>> relationDyadic = (List<ArrayList<String>>) resultMap.get("relation");
			//生成excel文件
			applyFormOperation.exportForm(tempZipFileDirectory+File.separator+excelFileName, outListDyadic,relationDyadic);
			//压缩文件的名字
			String zipFileName = "zipFile"+MyDateFormat.changeDateToTimeStampString(new Date())+".zip";
			//压缩word文件和excel文件
//			fileToZip(tempZipFileDirectory, zipFileDirectory, zipFileName);
			String tempAbsolutZipName = zipFileDirectory+File.separator+zipFileName;
			compressByAnt(tempAbsolutZipName,tempZipFileDirectory);
			
			//将要压缩的文件的绝对路径
			String absolutePath = zipFileDirectory+File.separator+zipFileName;
			//print debugging
			System.out.println("absolutePath = "+ absolutePath);
			
			response.getWriter().write(absolutePath);
		}
	}
	
	/**
	 * 根据前台传来的英文outMeans翻译成中文
	 * 
	 * direct--发料调拨出库；
	 * borrow--发料轮换出库；
	 * update--发料更新出库；
	 * @param outMeans
	 * @return
	 */
	private String translateOutMeans(String outListType){
		if("direct".equals(outListType))
			outListType = StringUtil.DIRECT_OUTLIST;
		else if("borrow".equals(outListType))
			outListType = StringUtil.BORROW_OUTLIST;
		else if("update".equals(outListType))
			outListType = StringUtil.UPDATE_OUTLIST;
		else outListType = "";
		return outListType;
	}
	/**
	 * 解析JSON
	 * @param content 前台使用json传来的二维数组 
	 * @param 用于保存吃json中解析出来的productId
	 * @return 解析之后的List<ArrayList<String>>
	 */
	private List<ArrayList<String>> resolveJSONToDyadic(String content, List<ArrayList<String>> productIDDyadic){
		List<ArrayList<String>> targetDyadic = new ArrayList<ArrayList<String>>();
		JSONArray ja = JSONArray.fromObject(content);
		int size = ja.size();
		//循环得到发料单信息
		for(int i = 0; i < size; i++){
			 
			ArrayList<String> tempList = new ArrayList<String>();
			JSONArray tempArray = JSONArray.fromObject(ja.get(i));
			//循環得到發料單信息
			for(int k = 0; k < tempArray.size()-1; k++){
				tempList.add(tempArray.getString(k));
			}
			targetDyadic.add(tempList);
			//再得到最後一列pids
			String[] ids  = tempArray.getString(tempArray.size() - 1).split(",");
			ArrayList<String> productIDs = new ArrayList<String>();
			//change by LYH 10/25
			/*for(int m=0;m<ids.length;m++) {
				productIDs.add(ids[m]);
			}*/
			//change by liuyutian 11.06
			for(int m=0;m<Integer.parseInt(tempList.get(6));m++) {
				productIDs.add(ids[m]);
			}
			//end
			productIDDyadic.add(productIDs);
			
		}

		return targetDyadic;
	}
	
//	private List<ArrayList<String>> resolveJSONToDyadic2(String content, List<ArrayList<String>> productIDDyadic){
//		List<ArrayList<String>> targetDyadic = new ArrayList<ArrayList<String>>();
//		JSONArray ja = JSONArray.fromObject(content);
//		
//		int size = ja.size();
//		for(int i = 0; i < size; i++){
//			ArrayList<String> tempList = new ArrayList<String>();
//			ArrayList<String> productIDs = new ArrayList<String>();
//			
//			JSONArray tempArray = JSONArray.fromObject(ja.get(i));
//		
//			JSONArray ar1 = (JSONArray) tempArray.get(0);
//			JSONArray ar2 = (JSONArray) tempArray.get(1);
//			
//			int len1 = ar1.size();
//			int len2 = ar2.size();
//			
//			for(int k = 0; k < len1; k++){
//				tempList.add(ar1.getString(k));
//			}
//			for(int k = 0; k < len2; k++){
//				productIDs.add(ar2.getString(k));
//			}
//			targetDyadic.add(tempList);
//			productIDDyadic.add(productIDs);
//		}
//		return targetDyadic;
//	}
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, String absolutePath){
		String ownedUnit = (String)request.getSession().getAttribute("ownedUnit");
		//类型判定 outListType=direct|borrow|update
		String applyType = request.getParameter("outListType");
		if("direct".equals(applyType)) applyType = StringUtil.DIRECT_OUT;
		else if("borrow".equals(applyType)) applyType = StringUtil.BORROW_OUT;
		else if("update".equals(applyType)) applyType = StringUtil.UPDATE_OUT;
		else applyType = "出库";
		
		//导出的zip文件的默认文件名
		String downloadFileName = ownedUnit+"_"+applyType+"_发料单"+MyDateFormat.changeDateToFileString(new Date())+".zip";
		//标准化下载的中文名
		downloadFileName = DownloadFile.getNormalFilename(request, downloadFileName);
		//发起下载流
		DownloadFile.launchDownloadStream(response, absolutePath, downloadFileName);
	}
	/**
	 * 记录日志
	 * 
	 * @param operateType
	 * @return
	 */
	private boolean saveLog(HttpServletRequest request,
			HttpServletResponse response, String operateType, long pId,
			String remark) {
		boolean flag = false;
		HttpSession session = request.getSession();
		Log log = new Log();
		log.setUserName((String) session.getAttribute("username"));
		log.setOperateTime(new Date());
		log.setOperateType(operateType);
		log.setProductId(pId);
		log.setRemark(remark);
		flag = UserLogService.SaveOperateLog(log);
		return flag;
	}
}
