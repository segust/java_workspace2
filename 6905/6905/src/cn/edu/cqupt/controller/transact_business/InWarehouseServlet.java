package cn.edu.cqupt.controller.transact_business;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;
import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.service.transact_business.ApplyFormOperation;
import cn.edu.cqupt.service.transact_business.ApplyHandleService;
import cn.edu.cqupt.service.transact_business.ContractHandleService;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.CrossPageCheck;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.DownloadFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class InWarehouseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2545310391508698531L;
	private ApplyFormOperation applyFormOperationService = null;
	private UploadFile uploadFile = null;
	private ApplyHandleService applyHandleService = null;
	private ContractHandleService contractHandleService;

	public void init() throws ServletException {
		applyHandleService = new ApplyHandleService();
		applyFormOperationService = new ApplyFormOperation();
		new ProductHandleService();
		contractHandleService = new ContractHandleService();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();

		String operate = request.getParameter("operate");
		String checkPerson = (String) session.getAttribute("username");
		String message = "";
		String path = "";
		// 获取当前版本
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		int version_int = Integer.parseInt(version);
		// version==1表示是企业版
		if ("1".equals(version)) {
			// String ownedUnit =
			// (String)request.getSession().getAttribute("ownedUnit");

			// 举例：判断当前用户是否具有 业务办理 权限
			if (CurrentUser.isContractManage(request)) {
				if ("queryApply".equals(operate)) {
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("pageSize") == null) {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request
								.getParameter("pageSize"));
					}
					List<HashMap<String, Object>> inApplyList = applyHandleService
							.getInApply(curPageNum, pageSize);
					int sum = (Integer) inApplyList.get(inApplyList.size() - 1)
							.get("sum");
					inApplyList.remove(inApplyList.size() - 1);

					request.setAttribute("allIn", true);
					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("query", "1");
					request.setAttribute("inApplyList", inApplyList);
					request.setAttribute("isIn", true);
					request.setAttribute("isBorrowOutApply", false);
					request.setAttribute("isUpdateOutApply", false);

					path = "/jsp/qy/transact_business/InBusinessQueryApply.jsp?curPageNum="
							+ curPageNum + "&pageSize=" + pageSize;
				} else if ("inCheck".equals(operate)) {

					path = "/jsp/qy/transact_business/InBusinessCheckBusiness.jsp";
				} else if ("searchInApply".equals(operate)) {
					/**
					 * 企业入库申请管理查询
					 */
					int curPageNum = 1;
					int pageSize = 10;
					if (request.getParameter("curPageNum") != null
							&& request.getParameter("pageSize") != null) {
						curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request.getParameter("pageSize"));
					}
					searchApply(request,version);

					path = "/jsp/qy/transact_business/InBusinessQueryApply.jsp?curPageNum="+curPageNum+"&pageSize="+pageSize;
				} else if ("exportSingleForm".equals(operate)) {
					/**
					 * 导出文件
					 */
					createExportFile_QY_JDS(request, response, session,applyHandleService);

					return;
				} else if ("download".equals(operate)) {
					String absolutePath = request.getParameter("absolutePath");
					String exportType = request.getParameter("exportType");

					// 下载文件
					downloadExportFile(request, response, exportType,
							absolutePath, version_int);

					return;

				} else if ("importExcelForm".equals(operate)) {
					/**
					 * 企业导入入库申请审核文件
					 */
					uploadFile = new UploadFile();
					Map<String, String> map = uploadFile.uploadFile(request,response);
					boolean flag = false;
					if (map != null && map.size() > 0) {
						// 文件上传之后在服务器中的路径
						String filePath = map.get("fileName");
						
						String[] meansStr = filePath.split("_");
						if(StringUtil.NEW_IN.equals(meansStr[1])){
							// 读取所上传的文件
							Map<Integer, List<ArrayList<String>>> dyadicMap = null;
							dyadicMap = applyFormOperationService.importAllSheetFromExcel(filePath, 3);
							// 删除上传的文件
							File tempFile = new File(filePath);
							if (tempFile.exists()) {
								tempFile.delete();
							}
							
							// 记录日志
							doLog(request, "入库申请", "导入入库申请excel表");
							
							// 数据库操作,保存申请表
							flag = applyHandleService.saveInApplys(dyadicMap.get(0), 
									dyadicMap.get(1),dyadicMap.get(2));
						}
					}
					if (flag) {
						message = "导入成功";
					} else {
						message = "导入失败！请选择正确的文件！";
					}
					request.setAttribute("msg", message);
					path = "/jsp/qy/transact_business/InBusinessCheckBusiness.jsp";
				}
			}
		} else if ("2".equals(version)) {
			// 此为军代室版本
			if (CurrentUser.isContractManage(request)) {
				if ("searchInApply".equals(operate)) {
					/**
					 * JDS入库申请管理查询
					 */
					searchApply(request,version);
					path = "/jsp/jds/transact_business/listQuery.jsp";
				} else if ("listQueryDetail".equals(operate)) {
					String status = request.getParameter("status");
					// 用申请ID查申请中产品的信息
					ArrayList<String> deviceNo = new ArrayList<String>();
					if (status.equals("in")) {
						int Inid = Integer.parseInt(request.getParameter("inId"));
						ApplyHandleService service2 = new ApplyHandleService();
						deviceNo = service2.queryDeviceNobyApplyId(Inid);
						request.setAttribute("inId", Inid);
					} else if (status.equals("out")) {
						int Outid = Integer.parseInt(request
								.getParameter("outId"));
						ApplyHandleService service2 = new ApplyHandleService();
						deviceNo = service2.queryDeviceNobyOutId(Outid);
						request.setAttribute("outId", Outid);
					}
					request.setAttribute("deviceNo", deviceNo);
					request.setAttribute("status", status);
					path = "jsp/jds/transact_business/showList.jsp";
				} else if ("queryInProductbyDeviceNo".equals(operate)) {
					String deviceNo = request.getParameter("deviceNo").trim();
					int Inid = Integer.parseInt(request.getParameter("inId"));
					HashMap<String, Object> info = new HashMap<String, Object>();
					ApplyHandleService service3 = new ApplyHandleService();
					info = service3.queryInProductbyDeviceNo(deviceNo, Inid);
					// /System.out.println("info="+info);
					// Iterator iter = info.entrySet().iterator();
					// while (iter.hasNext()) {
					// Map.Entry entry = (Map.Entry) iter.next();
					// Object key = entry.getKey();
					// Object val = entry.getValue();
					// System.out.println("key="+key);
					System.out.println("productType="+info.get("productType"));
					// }

					StringBuffer sb = new StringBuffer();
					if (info.size() > 0) {
						// JSONObject jsonObject = JSONObject.fromObject(info);
						// System.out.println("=====");
						// System.out.println(jsonObject.toString());
						// System.out.println("--------------");
						// sb.append(jsonObject.toString());
						// 将hashmap拼接成json
						String string = "{";
						for (Iterator it = info.entrySet().iterator(); it
								.hasNext();) {
							Entry e = (Entry) it.next();
							string += "'" + e.getKey() + "':";
							string += "'" + e.getValue() + "',";
						}
						string = string.substring(0, string.lastIndexOf(","));
						string += "}";
						response.getWriter().write(string);
					}
					// response.getWriter().write(sb.toString());
					path = "";
				} else if ("queryOutProductbyDeviceNo".equals(operate)) {
					String deviceNo = request.getParameter("deviceNo").trim();
					int Outid = Integer.parseInt(request.getParameter("outId"));
					// 用申请ID查申请中产品的信息
					HashMap<String, Object> info = new HashMap<String, Object>();
					ApplyHandleService service3 = new ApplyHandleService();
					info = service3.queryOutProductbyDeviceNo(deviceNo, Outid);
					StringBuffer sb = new StringBuffer();
					if (info.size() > 0) {
						String string = "{";
						for (Iterator it = info.entrySet().iterator(); it
								.hasNext();) {
							Entry e = (Entry) it.next();
							string += "'" + e.getKey() + "':";
							string += "'" + e.getValue() + "',";
						}
						string = string.substring(0, string.lastIndexOf(","));
						string += "}";
						response.getWriter().write(string);
					}
					path = "";
				} else if ("queryUpdateHistory".equals(operate)) {
					this.queryUpdateHistory(request,response);
				} else if ("showMaintainHistory".equals(operate)) {
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("curPageNum") == ""
							|| request.getParameter("pageSize") == null
							|| request.getParameter("pageSize") == "") {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request.getParameter(
								"pageSize").trim());
					}
					String deviceNo = request.getParameter("deviceNo").trim();
					String productModel = request.getParameter("productModel")
							.trim();
					ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
					ApplyHandleService service = new ApplyHandleService();
					T = service.selectMaintainhistory(productModel, deviceNo,
							curPageNum, pageSize);
					int sum = 0;

					sum = service.SelecthistorySum(deviceNo,productModel);

					request.setAttribute("MaintainHistory", T);
					request.setAttribute("deviceNo", deviceNo);
					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					path = "jsp/jds/transact_business/showHistory.jsp?&deviceNo="
							+ deviceNo
							+ "&curPageNum="
							+ curPageNum
							+ "&pageSize=" + pageSize;
				} else if ("jdsCheck".equals(operate)) {
					// List<Integer> Inids=new ArrayList<Integer>();
					// String jsonStr = request.getParameter("outData");
					// String checkType = request.getParameter("checkType");
					// JSONArray jarray = JSONArray.fromObject(jsonStr);
					// for(int i=0;i<jarray.size();i++){
					// //Inid 已经获取到 组成数组发到 DAO 去做状态变化
					// Inids.add(Integer.parseInt((String) jarray.get(i)));
					// }
					// String opFlag=request.getParameter("opFlag");
					// ApplyHandleService service1= new
					// ApplyHandleService();
					// System.out.println(service1.ChangApplyStatuebyInid(Inids,opFlag,checkType));
				} else if ("importApply".equals(operate)) {// 军代室 导入申请excel
					path = "jsp/jds/transact_business/importApply.jsp";
				} else if ("showApply".equals(operate)) {// 军代室 显示
					// 导入申请后 将申请 ID 通过request返回到这里
					// List<Integer> idList=(List<Integer>)
					// request.getAttribute("idList");
					// int curPageNum1 = 0;
					// int pageSize1 = 0;
					// if (request.getParameter("curPageNum") == null
					// || request.getParameter("pageSize") == null) {
					// curPageNum1 = 1;
					// pageSize1 = 10;
					// } else {
					// curPageNum1 = Integer.parseInt(request
					// .getParameter("curPageNum"));
					// pageSize1 = Integer.parseInt(request
					// .getParameter("pageSize"));
					// }
					// // 把下面一行代码 改成获取刚刚导入的文件 里的 申请 尽量不要改数据类型吧
					// List<HashMap<String, Object>> inApplyList1 =
					// applyHandleService
					// .getInApply(curPageNum1, pageSize1);
					// // int sum =inApplyList.size();
					// int sum = (Integer) inApplyList1.get(
					// inApplyList1.size() - 1).get("sum");
					// inApplyList1.remove(inApplyList1.size() - 1);
					//
					// request.setAttribute("sum", sum);
					// request.setAttribute("curPageNum", curPageNum1);
					// request.setAttribute("pageSize", pageSize1);
					// request.setAttribute("query", "1");
					// request.setAttribute("inApplyList", inApplyList1);
					//
					// path =
					// "/jsp/jds/transact_business/showApply.jsp?curPageNum="
					// + curPageNum1 + "&pageSize=" + pageSize1;
					// path = "jsp/jds/transact_business/showApply.jsp";
				} else if ("addOutList".equals(operate)) {
					path = "jsp/jds/transact_business/addOutList.jsp";

				} else if ("addAddOutList".equals(operate)) {
					String[] array = request.getParameterValues("0");
					if (array.length > 0) {
						for (int i = 0; i < array.length; i++) {
						}
					} else {
						System.out.print("为空");
					}

					path = "jsp/jds/transact_business/addOutList.jsp";
				} else if ("exportSingleForm".equals(operate)) {
					/**
					 * 生成将要下载的文件，并传给前台文件的绝对路径
					 * 
					 * @author LiangYH
					 */
					createExportFile_QY_JDS(request, response, session,
							applyHandleService);

				} else if ("download".equals(operate)) {
					/**
					 * 获取前台传来的文件绝对路径，发起下载流
					 * 
					 * @author LiangYH
					 */
					String absolutePath = request.getParameter("absolutePath");
					String exportType = request.getParameter("exportType");

					downloadExportFile(request, response, exportType,
							absolutePath, version_int);

				} else if ("listQueryApply".equals(operate)) {// 无条件查询
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("pageSize") == null) {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request
								.getParameter("pageSize"));
					}
					List<HashMap<String, Object>> inApplyList = applyHandleService
							.getInApply(curPageNum, pageSize);
					int sum = (Integer) inApplyList.get(inApplyList.size() - 1).get("sum");
					inApplyList.remove(inApplyList.size() - 1);
					ArrayList<String> companys = new InfoService()
							.getCompanyNameList((String) request.getSession()
									.getAttribute("ownedUnit"), 2);
					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("query", "1");
					request.setAttribute("inApplyList", inApplyList);
					request.setAttribute("isIn", true);
					request.setAttribute("isBorrowOutApply", false);
					request.setAttribute("isUpdateOutApply", false);
					request.setAttribute("companys", companys);
					path = "/jsp/jds/transact_business/listQuery.jsp?curPageNum="
							+ curPageNum + "&pageSize=" + pageSize;
				} else if ("jdsCheck".equals(operate)) {
					// //列表查询中是否通过
					// List<Integer> Inids=new ArrayList<Integer>();
					// List<String> ownedUnits=new ArrayList<String>();
					// String jsonStr = request.getParameter("outData");
					// String checkType = request.getParameter("checkType");
					// JSONArray jarray = JSONArray.fromObject(jsonStr);
					// for(int i=0;i<jarray.size();i++){
					// //jarray.get(i).split(",")
					// //Inid 已经获取到 组成数组发到 DAO 去做状态变化
					// Inids.add(Integer.parseInt((String) jarray.get(i)));
					// }
					// String opFlag=request.getParameter("opFlag");
					// ApplyHandleService service= new ApplyHandleService();
					// System.out.println(service.ChangApplyStatuebyInid(Inids,opFlag,checkType));
				} else if ("importApply".equals(operate)) {// 军代室 导入申请excel
					path = "jsp/jds/transact_business/importApply.jsp";
				} else if ("showApply".equals(operate)) {// 军代室 显示
					// 导入申请后 将申请 ID 通过request返回到这里
					// List<Integer> idList=(List<Integer>)
					// request.getAttribute("idList");
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("pageSize") == null) {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request
								.getParameter("pageSize"));
					}
					List<HashMap<String, Object>> inApplyList = null;
					inApplyList = applyHandleService.getInApply(curPageNum,
							pageSize);
					int sum = (Integer) inApplyList.get(inApplyList.size() - 1)
							.get("sum");
					inApplyList.remove(inApplyList.size() - 1);

					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("query", "1");
					request.setAttribute("inApplyList", inApplyList);

					path = "/jsp/jds/transact_business/showApply.jsp?curPageNum="
							+ curPageNum + "&pageSize=" + pageSize;
				} else if ("importExcelFormForJDS".equals(operate)) {
					/**
					 * 军代室导入审核文件
					 */
					// 日志
//					doLog(request, "导入审核文件", "导入的文件可能为轮换出库、更新出入库、新入库");
					request.setAttribute("isImport", true);
					boolean flag = importFile(request, response,applyHandleService);
					if (flag)
						path = "/jsp/jds/transact_business/getApplyInfo.jsp";
					else
						path = "/InWarehouseServlet?operate=importApply";

				} else if ("checkFileSynchroInJDS".equals(operate)) {
					// 军代室的审核导入的文件
					checkFileInJDS(request, response, checkPerson,applyHandleService);

					return;
				}
			}

		} else if ("3".equals(version)) {
			// 军代局版本
			if (CurrentUser.isContractManage(request)) {
				if ("listApply".equals(operate)) {
					/**
					 * 跳到“列表查询”页面的时候 查询inApply表
					 */
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
					List<HashMap<String, Object>> inApplyList = null;
					inApplyList = applyHandleService.getInApply_JDJ(curPageNum,pageSize);
					int sum = 0;
					if(inApplyList != null){
						sum = (Integer) inApplyList.get(inApplyList.size()-1).get("sum");
						inApplyList.remove(inApplyList.size() - 1);
					}
					ArrayList<String> jdsList=new InfoService().getJDSNameList(request.getSession().getAttribute("ownedUnit").toString());
					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("companys", jdsList);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("query", "1");
					request.setAttribute("inApplyList", inApplyList);
					request.setAttribute("isIn", true);
					request.setAttribute("isBorrowOutApply", false);
					request.setAttribute("isUpdateOutApply", false);
					path = "/jsp/jdj/transact_business/listQuery.jsp";

				} else if ("listQuery".equals(operate)) {
					/**
					 * 军代局”列表查询“中的条件组合查询
					 */
					queryApply_JDJ_ZHJ(request, applyHandleService);

					path = "";
				} else if ("searchInApply".equals(operate)) {
					/**
					 * JDJ入库申请管理查询
					 */
					searchApply(request,version);
					path = "/jsp/jdj/transact_business/listQuery.jsp";
				} else if ("backUpUpdata".equals(operate)) {
					// 导入上报文件--只作备份操作
					path = "/jsp/jdj/transact_business/importUpdata.jsp";
				} else if ("backUpDowndata".equals(operate)) {
					// 导入下发文件--只作备份操作
					path = "/jsp/jdj/transact_business/importDowndata.jsp";
				} else if ("importApply".equals(operate)) {
					// 导入可以审核的申请表
					path = "/jsp/jdj/transact_business/importApply.jsp";
				} else if ("importCheckFile".equals(operate)) {
					/**
					 * 军代局导入审核文件
					 */
					// 日志

					boolean flag = importFile(request, response,applyHandleService);
					if (flag)
						path = "/jsp/jdj/transact_business/getApplyInfo.jsp";
					else
						path = "/InWarehouseServlet?operate=importApply";
				} else if ("checkFileInJDJ".equals(operate)) {
					// 军代局的审核导入的文件
					checkFileInJDS(request, response, checkPerson,applyHandleService);

					return;
				} else if ("exportSingleForm".equals(operate)) {
					/**
					 * 生成将要下载的文件，并传给前台文件的绝对路径
					 */
					createExportFile(request, response, session,
							applyHandleService);

				} else if ("download".equals(operate)) {
					/**
					 * 获取前台传来的文件绝对路径，发起下载流
					 */
					String absolutePath = request.getParameter("absolutePath");
					String exportType = request.getParameter("exportType");

					downloadExportFile(request, response, exportType,
							absolutePath, version_int);

				} else if ("importBackupUpFiles".equals(operate)) {
					/**
					 * 上报备份的数据（导入文件）
					 */
					// 日志
					doLog(request, "上报备份数据", "");

					boolean runStatus = importBackUpFile(request, response,applyHandleService);
					//delete by lyt 11-09
					/*response.setContentType("text/html,charset=utf8");
					if (runStatus) {
						response.getWriter().write("1");
					} else {
						response.getWriter().write("0");
					}*/
					request.setAttribute("runStatus", runStatus);
					path = "/jsp/jdj/transact_business/importUpdata.jsp";
				} else if ("importBackupDownFiles".equals(operate)) {
					/**
					 * 下发信息数据（导入文件）
					 */
					boolean runStatus = importBackUpFile(request, response,
							applyHandleService);
					
					//delete by lyt 11-09
					/*response.setContentType("text/html,charset=utf8");
					if (runStatus) {
						response.getWriter().write("1");
					} else {
						response.getWriter().write("0");
					}*/
					request.setAttribute("runStatus", runStatus);
					path = "/jsp/jdj/transact_business/importDowndata.jsp";
				} else if ("listQueryDetail".equals(operate)) {
					String status = request.getParameter("status");
					// 用申请ID查申请中产品的信息
					ArrayList<String> deviceNo = new ArrayList<String>();
					if (status.equals("in")) {
						int Inid = Integer.parseInt(request.getParameter("inId"));
						ApplyHandleService service2 = new ApplyHandleService();
						deviceNo = service2.queryDeviceNobyApplyId(Inid);
						request.setAttribute("inId", Inid);
					} else if (status.equals("out")) {
						int Outid = Integer.parseInt(request.getParameter("outId"));
						ApplyHandleService service2 = new ApplyHandleService();
						deviceNo = service2.queryDeviceNobyOutId(Outid);
						request.setAttribute("outId", Outid);
					}
					request.setAttribute("deviceNo", deviceNo);
					request.setAttribute("status", status);
					path = "jsp/jdj/transact_business/showList.jsp";
				} else if ("queryInProductbyDeviceNo".equals(operate)) {
					String deviceNo = request.getParameter("deviceNo").trim();
					int Inid = Integer.parseInt(request.getParameter("inId"));
					// 用申请ID查申请中产品的信息
					HashMap<String, Object> info = new HashMap<String, Object>();
					ApplyHandleService service3 = new ApplyHandleService();
					info = service3.queryInProductbyDeviceNo(deviceNo, Inid);
					StringBuffer sb = new StringBuffer();
					if (info.size() > 0) {
						String string = "{";
						for (Iterator it = info.entrySet().iterator(); it
								.hasNext();) {
							Entry e = (Entry) it.next();
							string += "'" + e.getKey() + "':";
							string += "'" + e.getValue() + "',";
						}
						string = string.substring(0, string.lastIndexOf(","));
						string += "}";
						response.getWriter().write(string);
					}
					path = "";
				} else if ("queryOutProductbyDeviceNo".equals(operate)) {
					String deviceNo = request.getParameter("deviceNo").trim();
					int Outid = Integer.parseInt(request.getParameter("outId"));
					// 用申请ID查申请中产品的信息
					HashMap<String, Object> info = new HashMap<String, Object>();
					ApplyHandleService service3 = new ApplyHandleService();
					info = service3.queryOutProductbyDeviceNo(deviceNo, Outid);
					StringBuffer sb = new StringBuffer();
					if (info.size() > 0) {
						String string = "{";
						for (Iterator it = info.entrySet().iterator(); it
								.hasNext();) {
							Entry e = (Entry) it.next();
							string += "'" + e.getKey() + "':";
							string += "'" + e.getValue() + "',";
						}
						string = string.substring(0, string.lastIndexOf(","));
						string += "}";
						response.getWriter().write(string);
					}
					path = "";
				} else if ("queryUpdateHistory".equals(operate)) {
					this.queryUpdateHistory(request, response);
				}else if ("showMaintainHistory".equals(operate)) {
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("curPageNum") == ""
							|| request.getParameter("pageSize") == null
							|| request.getParameter("pageSize") == "") {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request.getParameter(
								"pageSize").trim());
					}
					String deviceNo = request.getParameter("deviceNo").trim();
					String productModel = request.getParameter("productModel")
							.trim();

					ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
					ApplyHandleService service = new ApplyHandleService();
					T = service.selectMaintainhistory(productModel, deviceNo,
							curPageNum, pageSize);
					int sum = 0;

					sum = service.SelecthistorySum(deviceNo,productModel);

					request.setAttribute("MaintainHistory", T);
					request.setAttribute("deviceNo", deviceNo);
					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					path = "jsp/jds/transact_business/showHistory.jsp?&productModel="
							+ productModel
							+ "&deviceNo="
							+ deviceNo
							+ "&curPageNum="
							+ curPageNum
							+ "&pageSize="
							+ pageSize;
				}
			}
			// 业务办理权限结束
		} else if ("4".equals(version)) {
			// 指挥局版本
			if (CurrentUser.isContractManage(request)) {
				if ("listApply".equals(operate)) {
					/**
					 * 跳到“列表查询”页面的时候 查询inApply表
					 */
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("pageSize") == null) {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request
								.getParameter("pageSize"));
					}
					List<HashMap<String, Object>> inApplyList = applyHandleService
							.getInApply(curPageNum, pageSize);
					int sum = (Integer) inApplyList.get(inApplyList.size() - 1).get("sum");
					inApplyList.remove(inApplyList.size() - 1);

					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("query", "1");
					request.setAttribute("inApplyList", inApplyList);
					request.setAttribute("isIn", true);
					request.setAttribute("isBorrowOutApply", false);
					request.setAttribute("isUpdateOutApply", false);
					path = "/jsp/zhj/transact_business/listQuery.jsp?curPageNum="
							+ curPageNum + "&pageSize=" + pageSize;
				} else if ("listQuery".equals(operate)) {
					/**
					 * 指挥局”列表查询“中的条件组合查询
					 */
					queryApply_JDJ_ZHJ(request, applyHandleService);

					path = "";
				} else if ("searchInApply".equals(operate)) {
					/**
					 * ZHJ入库申请管理查询
					 */
					searchApply(request,version);
					path = "/jsp/zhj/transact_business/listQuery.jsp";
				} else if ("importCheckFile".equals(operate)) {
					/**
					 * 指挥局导入审核文件
					 */
					doLog(request, "导入审核文件", "导入的文件可能为轮换出库、更新出入库、新入库");
					boolean flag = importFile(request, response,applyHandleService);
					if (flag)
						path = "/jsp/zhj/transact_business/getApplyInfo.jsp";
					else
						path = "/InWarehouseServlet?operate=importApply";
				} else if ("importBackupUpFiles".equals(operate)) {
					/**
					 * 指挥局 上报备份的数据（导入文件）
					 */
					// 日志
					doLog(request, "上报备份数据", "");

					boolean runStatus = importFile(request, response,
							applyHandleService);
					//delete by lyt 11-09
					/*response.setContentType("text/html,charset=utf8");
					if (runStatus) {
						response.getWriter().write("1");
					} else {
						response.getWriter().write("0");
					}*/
					request.setAttribute("runStatus", runStatus);
					path = "/jsp/zhj/transact_business/importUpdata.jsp";
				} else if ("checkFileInZHJ".equals(operate)) {
					// 日志
					doLog(request, "导入审核文件", "");
					// 指挥局的审核导入的文件
					checkFileInJDS(request, response, checkPerson,applyHandleService);

					return;
				} else if ("exportSingleForm".equals(operate)) {
					/**
					 * 生成将要下载的文件，并传给前台文件的绝对路径
					 */
					createExportFile(request, response, session,
							applyHandleService);

				} else if ("download".equals(operate)) {
					/**
					 * 获取前台传来的文件绝对路径，发起下载流
					 */
					String absolutePath = request.getParameter("absolutePath");
					String exportType = request.getParameter("exportType");

					downloadExportFile(request, response, exportType,
							absolutePath, version_int);

				} else if ("backUpUpdata".equals(operate)) {
					// 导入上报文件--只作备份操作
					path = "/jsp/zhj/transact_business/importUpdata.jsp";
				} else if ("outListQuery".equals(operate)) {
					// 查询出库信息列表
					path = "/jsp/zhj/transact_business/outListQuery.jsp";
				} else if ("addOutList".equals(operate)) {
					// 导入发料单
					path = "/jsp/zhj/transact_business/addOutList1.jsp";
				} else if ("importApply".equals(operate)) {
					// 导入可以审核的申请表
					path = "/jsp/zhj/transact_business/importApply.jsp";
				} else if ("listQueryDetail".equals(operate)) {
					String status = request.getParameter("status");
					// 用申请ID查申请中产品的信息
					ArrayList<String> deviceNo = new ArrayList<String>();
					if (status.equals("in")) {
						int Inid = Integer.parseInt(request.getParameter("inId"));
						ApplyHandleService service2 = new ApplyHandleService();
						deviceNo = service2.queryDeviceNobyApplyId(Inid);
						request.setAttribute("inId", Inid);
					} else if (status.equals("out")) {
						int Outid = Integer.parseInt(request
								.getParameter("outId"));
						ApplyHandleService service2 = new ApplyHandleService();
						deviceNo = service2.queryDeviceNobyOutId(Outid);
						request.setAttribute("outId", Outid);
					}
					request.setAttribute("deviceNo", deviceNo);
					request.setAttribute("status", status);
					path = "jsp/zhj/transact_business/showList.jsp";
				} else if ("queryInProductbyDeviceNo".equals(operate)) {
					String deviceNo = request.getParameter("deviceNo").trim();
					int Inid = Integer.parseInt(request.getParameter("inId"));
					// 用申请ID查申请中产品的信息
					HashMap<String, Object> info = new HashMap<String, Object>();
					ApplyHandleService service3 = new ApplyHandleService();
					info = service3.queryInProductbyDeviceNo(deviceNo, Inid);
					StringBuffer sb = new StringBuffer();
					if (info.size() > 0) {
						String string = "{";
						for (Iterator it = info.entrySet().iterator(); it
								.hasNext();) {
							Entry e = (Entry) it.next();
							string += "'" + e.getKey() + "':";
							string += "'" + e.getValue() + "',";
						}
						string = string.substring(0, string.lastIndexOf(","));
						string += "}";
						response.getWriter().write(string);
					}
					path = "";
				}else if ("queryUpdateHistory".equals(operate)) {
					this.queryUpdateHistory(request, response);
				} else if ("backUpUpdata".equals(operate)) {
					// 导入上报文件--只作备份操作
					path = "/jsp/zhj/transact_business/importUpdata.jsp";
				} else if ("outListQuery".equals(operate)) {
					// 查询出库信息列表
					path = "/jsp/zhj/transact_business/outListQuery.jsp";
				} else if ("addOutList".equals(operate)) {
					// 导入发料单
					path = "/jsp/zhj/transact_business/addOutList1.jsp";
				} else if ("importApply".equals(operate)) {
					// 导入可以审核的申请表
					path = "/jsp/zhj/transact_business/importApply.jsp";
				} else if ("listQueryDetail".equals(operate)) {
					int Inid = Integer.parseInt(request.getParameter("inId"));
					// 用申请ID查申请中产品的信息
					ArrayList<String> deviceNo = new ArrayList<String>();
					ApplyHandleService service2 = new ApplyHandleService();
					deviceNo = service2.queryDeviceNobyApplyId(Inid);
					request.setAttribute("deviceNo", deviceNo);
					// request.setAttribute("updateHistory",
					// info.get("updateHistory"));
					path = "jsp/zhj/transact_business/showList.jsp";
				} else if ("searchInApply".equals(operate)) {
					/**
					 * JDS入库申请管理查询
					 */
					searchApply(request,version);
					path = "/jsp/zhj/transact_business/listQuery.jsp";
				} 
				else if ("queryInProductbyDeviceNo".equals(operate)) {
					String deviceNo = request.getParameter("deviceNo").trim();
					int Inid = Integer.parseInt(request.getParameter("inId"));
					// 用申请ID查申请中产品的信息
					HashMap<String, Object> info = new HashMap<String, Object>();
					ApplyHandleService service3 = new ApplyHandleService();
					info = service3.queryInProductbyDeviceNo(deviceNo, Inid);
					StringBuffer sb = new StringBuffer();
					if (info.size() > 0) {
						JSONObject jsonObject = JSONObject.fromObject(info);
						sb.append(jsonObject.toString());
					}
					response.getWriter().write(sb.toString());
					path = "";
				} else if ("showMaintainHistory".equals(operate)) {
					int curPageNum = 0;
					int pageSize = 0;
					if (request.getParameter("curPageNum") == null
							|| request.getParameter("curPageNum") == ""
							|| request.getParameter("pageSize") == null
							|| request.getParameter("pageSize") == "") {
						curPageNum = 1;
						pageSize = 10;
					} else {
						curPageNum = Integer.parseInt(request
								.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request.getParameter(
								"pageSize").trim());
					}
					String deviceNo = request.getParameter("deviceNo").trim();
					String productModel = request.getParameter("productModel")
							.trim();
					ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
					ApplyHandleService service = new ApplyHandleService();
					T = service.selectMaintainhistory(productModel, deviceNo,
							curPageNum, pageSize);
					int sum = 0;

					sum = service.SelecthistorySum(deviceNo,productModel);

					request.setAttribute("MaintainHistory", T);
					request.setAttribute("deviceNo", deviceNo);
					request.setAttribute("sum", sum);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					path = "jsp/zhj/transact_business/showHistory.jsp?&deviceNo="
							+ deviceNo
							+ "&curPageNum="
							+ curPageNum
							+ "&pageSize=" + pageSize;
				}
			}
		}
		if (!"".equals(path)) {
			request.getRequestDispatcher(path).forward(request, response);
		}
	}
	
	public void queryUpdateHistory(HttpServletRequest request,HttpServletResponse response){
		String deviceNo = request.getParameter("deviceNo").trim();
		String productModel = request.getParameter("productModel").trim();
		String means=request.getParameter("means").trim();
		HashMap<String, Object> info = new HashMap<String, Object>();
		ApplyHandleService service = new ApplyHandleService();
		info = service.queryUpdateHistory(deviceNo, productModel,means);
		HashMap<String, Object> newinfo = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		if (means.equals("更新出库")) {
			String newproductModel = (String) info.get("newproductModel")==null?"":(String) info.get("newproductModel");
			String newdeviceNo = (String) info.get("newdeviceNo")==null?"":(String) info.get("newdeviceNo");
			newinfo.put("newproductModel", newproductModel);
			newinfo.put("newdeviceNo", newdeviceNo);
			newinfo.put("oldproductModel", productModel);
			newinfo.put("olddeviceNo", deviceNo);
			if (newinfo.size() > 0) {
				JSONArray jsonArray = JSONArray
						.fromObject(newinfo);
				sb.append(jsonArray.toString());
			}
		} else if (means.equals("更新入库")||means.equals("轮换出库")) {
			String oldproductModel = (String) info.get("oldproductModel")==null?"":(String) info.get("oldproductModel");
			String olddeviceNo = (String) info.get("olddeviceNo")==null?"":(String) info.get("olddeviceNo");
			newinfo.put("oldproductModel", oldproductModel);
			newinfo.put("olddeviceNo", olddeviceNo);
			newinfo.put("newproductModel", productModel);
			newinfo.put("newdeviceNo", deviceNo);
			if (newinfo.size() > 0) {
				JSONArray jsonArray = JSONArray
						.fromObject(newinfo);
				sb.append(jsonArray.toString());
			}
		}else if(means.equals("新入库")||means.equals("轮换入库")){
			sb.append("0");
		}
		//System.out.println("fsaaf"+sb.toString());
		try {
			response.getWriter().write(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 组合条件查询(四个版本)
	 * 
	 * @param request
	 * @author LiangYH
	 */
	private void searchApply(HttpServletRequest request,String version) {
		String contractId = request.getParameter("contractId");
		String productType = request.getParameter("productType");
		String unitName = request.getParameter("unitName");
		String operateType = request.getParameter("operateType");
		String[] time1 = request.getParameter("fromDate").split(" ");
		String fromDateTemp = time1[0];
		String[] time2 = request.getParameter("toDate").split(" ");
		String toDateTemp = time2[0];
		String status = request.getParameter("status");
		String ownedUnit = request.getParameter("ownedUnit");
		
		int curPageNum = 1;
		int pageSize = 10;
		if (request.getParameter("curPageNum") != null
				&& request.getParameter("pageSize") != null) {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}

		// 操作类型是否为入库
		boolean isIn = false;
		// 操作类型是否为轮换出库申请
		boolean isBorrowOutApply = false;
		// 操作类型是否为更新出库申请
		boolean isUpdateOutApply = false;
		List<HashMap<String, Object>> applyList = null;

		long[] curPageIdArray = null;
		if ("renewOut".equals(operateType) || "circleOut".equals(operateType)) {
			
			// 向出库申请表中查询
			if("3".equals(version)){
				applyList = applyHandleService.selectOutApply_JDJ(contractId,
						productType, unitName,operateType, fromDateTemp,
						toDateTemp, status, ownedUnit, null,curPageNum, pageSize);
			}else{
				applyList = applyHandleService.selectOutApply(contractId,
						productType, unitName,operateType, fromDateTemp,
						toDateTemp, status, ownedUnit, null,curPageNum, pageSize);
			}
			// 取出当前页的id，存成long[]
			curPageIdArray = new long[applyList.size() - 1];
			for (int i = 0; i < applyList.size()-1; i++) {
				OutApply temp = (OutApply) applyList.get(i).get("apply");
				curPageIdArray[i] = temp.getOutId();
			}
		} else {
			// 向入库申请表中查询
			//军代局的查询，需把企业换成相应的军代室
			if("3".equals(version)){
				applyList = applyHandleService.selectInApply_JDJ(contractId,
						productType, unitName, operateType, ownedUnit,null,
						fromDateTemp, toDateTemp, status, curPageNum, pageSize);
			}else{
				applyList = applyHandleService.selectInApply(contractId,
						productType, unitName, operateType, ownedUnit,null,
						fromDateTemp, toDateTemp, status, curPageNum, pageSize);
			}
			isIn = true;
			// 取出当前页的id，存成long[]
			curPageIdArray = new long[applyList.size() - 1];
			for (int i = 0; i < applyList.size()-1; i++) {
				InApply temp = (InApply) applyList.get(i).get("apply");
				curPageIdArray[i] = temp.getInId();
			}
		}

		// 将已选id传回前台
		String checkedIdStr = CrossPageCheck.getCheckedIdStr(request,curPageIdArray);
		request.setAttribute("checkedIdStr", checkedIdStr);

		if ("circleOut".equals(operateType)) {
			isBorrowOutApply = true;
		}
		if ("renewOut".equals(operateType)) {
			isUpdateOutApply = true;
		}
		int count = 0;
		int tempLen = applyList.size()-1;
		count = (Integer) applyList.get(tempLen).get("count");
		applyList.remove(tempLen);

		// 向前台返回查询的数据
		request.setAttribute("sum", count);
		if ("allIn".equals(operateType)) {
			request.setAttribute("allIn", true);
		} else {
			request.setAttribute("allIn", false);
		}
		//查询代储企业
		ArrayList<String> companys = new InfoService()
				.getCompanyNameList((String)request.getSession().getAttribute("ownedUnit"), 2);
		// map数组
		request.setAttribute("inApplyList", applyList);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("query", "2");
		request.setAttribute("isIn", isIn);
		request.setAttribute("isBorrowOutApply", isBorrowOutApply);
		request.setAttribute("isUpdateOutApply", isUpdateOutApply);
		request.setAttribute("operateType", operateType);
		request.setAttribute("companys", companys);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * 根据导出类型改变相应的下载文件名
	 * 
	 * @param fileName
	 * @param exportType
	 * @param version
	 *            版本号
	 * @return
	 */
	private String buildExcelNameByType(String exportType, int version) {
		String fileName = "";
		String versionName = "";

		switch (version) {
		case 1:
			versionName = "企业申请";
			break;
		case 2:
			versionName = "军代室审核";
			break;
		case 3:
			versionName = "军代局审核";
			break;
		case 4:
			versionName = "指挥局审核";
			break;
		default:
			break;
		}
		if ("newIn".equals(exportType)) {
			fileName = versionName + "_"+StringUtil.NEW_IN+"_";
		} else if ("circleIn".equals(exportType)) {
			fileName = versionName + "_"+StringUtil.BORROW_IN+"_";
		} else if ("circleOut".equals(exportType)) {
			fileName = versionName + "_"+StringUtil.BORROW_OUT+"_";
		} else if ("renewIn".equals(exportType)) {
			fileName = versionName + "_"+StringUtil.UPDATE_IN+"_";
		} else if ("renewOut".equals(exportType)) {
			fileName = versionName + "_"+StringUtil.UPDATE_OUT+"_";
		} else {
			fileName = "_入库_";
		}
		return fileName + MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
	}

	/**
	 * 军代局和指挥局的条件组合查询申请表
	 * 
	 * @param request
	 */
	public void queryApply_JDJ_ZHJ(HttpServletRequest request,
			ApplyHandleService applyHandleService) {
		String productType = request.getParameter("productType");
		String unitName = request.getParameter("unitName");
		String operateType = request.getParameter("operateType");
		String manufacturer = request.getParameter("manufacturer");
		String keeper = request.getParameter("keeper");
		String fromDateTemp = request.getParameter("fromDate");
		String toDateTemp = request.getParameter("toDate");
		// String status = request.getParameter("status").trim();

		int curPageNum = 1;
		int pageSize = 10;
		if (request.getParameter("curPageNum") != null
				&& request.getParameter("pageSize") != null) {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}

		// 执行查询操作
		List<HashMap<String, Object>> inApplyList = null;
		if ("renewOut".equals(operateType) || "circleOut".equals(operateType)) {
			inApplyList = applyHandleService.selectOutApply(null, productType,
					unitName, operateType, fromDateTemp, toDateTemp, null,
					keeper, manufacturer, curPageNum, pageSize);
		} else {
			inApplyList = applyHandleService.selectInApply(null, productType,
					unitName, operateType, keeper, manufacturer, fromDateTemp,
					toDateTemp, null, curPageNum, pageSize);
		}
		int count = 0;
		if (inApplyList.size() > 0) {
			count = (Integer) inApplyList.get(0).get("count");
			inApplyList.remove(0);
		}

		request.setAttribute("sum", count);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("query", "1");
		// 此时inApplyList是一个size=2的数组，其中的元素为map，键为：apply和product
		request.setAttribute("inApplyList", inApplyList);
	}

	/**
	 * 在军代局和指挥局导出文件的时候，用于生成将要导出的文件
	 * 
	 * <p>
	 * 军代局和指挥局在审核入库申请的之后，不需要导出合同表
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void createExportFile(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			ApplyHandleService applyHandleService) throws IOException {
		String jsonStr = request.getParameter("outData");
		String exportType = request.getParameter("exportType");
		String ownedUnit_JDS = request.getParameter("ownedUnit");
		
		if (ownedUnit_JDS == null || "".equals(ownedUnit_JDS))
			ownedUnit_JDS = (String) request.getSession().getAttribute(
					"ownedUnit");

		// print debugging
		System.out.println("ownedUnit_jds = " + ownedUnit_JDS);

		JSONArray jarray = JSONArray.fromObject(jsonStr);
		// 8-22
		List<Long> targetArray = new ArrayList<Long>();
		int len = jarray.size();
		for (int i = 0; i < len; i++) {
			targetArray.add(Long.parseLong((String) jarray.getJSONArray(i).get(
					0)));
		}

		// 执行查询数据库操作
		Map<String, Object> dyadicMap = null;
		if ("circleOut".equals(exportType) || "renewOut".equals(exportType)) {
			// 出库申请表
			dyadicMap = applyHandleService.queryMultiFormByOutID(targetArray,
					ownedUnit_JDS);
		} else {
			// 入库申请表
			dyadicMap = applyHandleService.queryMultiFormByInId(targetArray,
					ownedUnit_JDS);
		}

		String tempFilePath = "";
		// 生成默认文件夹
		tempFilePath = StringUtil.createFold(request, null);
		String fileName = "ExportForm"
				+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
		// 文件和绝对路径
		String absolutePath = tempFilePath + File.separator + fileName;
		// 生成excel表
		applyFormOperationService.exportForm(absolutePath,
				(List<ArrayList<String>>) dyadicMap.get("1"),
				(List<ArrayList<String>>) dyadicMap.get("2"),
				(List<ArrayList<String>>) dyadicMap.get("3"));
		response.setContentType("text/plain,charset=utf8");
		PrintWriter writer = response.getWriter();
		if (absolutePath != null && !"".equals(absolutePath)) {
			writer.write(absolutePath);
		} else {
			writer.write("0");
		}
	}

	/**
	 * 在在企业和军代室在导出文件的时候，用于生成将要导出的文件
	 * <p>
	 * 如果是入库申请，那么导出的数据库表有：inApply、product、relation、contract
	 * 如果是其他申请，那么导出的数据库表有：（out）inApply、product、relation
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void createExportFile_QY_JDS(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			ApplyHandleService applyHandleService) throws IOException {
		//将要导出的数据
		String jsonStr = request.getParameter("outData");
		//操作方式（轮换入库、轮换出库、、、、、、）
		String exportType = request.getParameter("exportType");
		//
		
		doLog(request, "导出文件", "操作方式为："+exportType);
		String ownedUnit_JDS = request.getParameter("ownedUnit");
		
		if (ownedUnit_JDS == null || "".equals(ownedUnit_JDS))
			ownedUnit_JDS = (String) request.getSession().getAttribute("ownedUnit");

		// print debugging
		System.out.println("ownedUnit_jds = " + ownedUnit_JDS);

		JSONArray jarray = JSONArray.fromObject(jsonStr);
		List<Long> targetArray = new ArrayList<Long>();
		int len = jarray.size();
		for (int i = 0; i < len; i++) {
			targetArray.add(Long.parseLong((String) jarray.getJSONArray(i).get(0)));
		}
		
		Map<String, Object> dyadicMap = null;
		List<ArrayList<String>> contractDyadic = null;
		if ("circleOut".equals(exportType) || "renewOut".equals(exportType)) {
			// 执行查询数据库操作，出库申请表
			dyadicMap = applyHandleService.queryMultiFormByOutID(targetArray,ownedUnit_JDS);
		} else if ("newIn".equals(exportType)) {
			// 查询inApply、product、relation
			dyadicMap = applyHandleService.queryMultiFormByInId(targetArray,ownedUnit_JDS);
			// 查询contact表
			contractDyadic = contractHandleService
					.queryContracts((List<ArrayList<String>>) dyadicMap.get("3"));
		} else {
			// 数据库操作：多表查询
			dyadicMap = applyHandleService.queryMultiFormByInId(targetArray,ownedUnit_JDS);
		}
		
		String tempFilePath = "";
		// 生成默认文件夹
		tempFilePath = StringUtil.createFold(request, null);
		String fileName = "ExportForm"
				+ MyDateFormat.changeDateToFileString(new Date()) + "."+StringUtil.SUFFIX_EXECL;
		// 文件和绝对路径
		String absolutePath = tempFilePath + File.separator + fileName;
		// 生成excel表
		if ("newIn".equals(exportType)) {
			// 如果为新入库，需要导出合同
			applyFormOperationService.exportForm(absolutePath,
					(List<ArrayList<String>>) dyadicMap.get("1"),
					(List<ArrayList<String>>) dyadicMap.get("2"),
					(List<ArrayList<String>>) dyadicMap.get("3"),
					contractDyadic);
		} else {
			applyFormOperationService.exportForm(absolutePath,
					(List<ArrayList<String>>) dyadicMap.get("1"),
					(List<ArrayList<String>>) dyadicMap.get("2"),
					(List<ArrayList<String>>) dyadicMap.get("3"));
		}
		response.setContentType("text/plain,charset=utf8");
		PrintWriter writer = response.getWriter();
		if (absolutePath != null && !"".equals(absolutePath)) {
			writer.write(absolutePath);
		} else {
			writer.write("0");
		}
	}

	/**
	 * 根据文件绝对路径发起下载流，下载文件。在四个版本上面通用
	 * <p>
	 * 根据版本号来确定下载文件的名字
	 * </p>
	 * @param request
	 * @param response
	 * @param exportType 下载的文件类型，比如更新入库
	 * @param absolutePath 下载文件的绝对路径
	 * @param version 版本号（企业、军代室、军代局、指挥局）
	 */
	public void downloadExportFile(HttpServletRequest request,
			HttpServletResponse response, String exportType,
			String absolutePath, int version) {
		// 组合一个文件名
		String fileName = buildExcelNameByType(exportType, version);

		// 防止因为浏览器不同导致文件名乱码
		fileName = DownloadFile.getNormalFilename(request, fileName);

		// 改变响应头，发起下载流
		DownloadFile.launchDownloadStream(response, absolutePath, fileName);
		doLog(request, "下载文件", "文件名为："+fileName);
		// 删除文件
		File file = new File(absolutePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 军代室、军代局、指挥局导入审核文件
	 * <p>
	 * 如果是入库申请，那么导入的数据库表有：inApply、product、relation、contract
	 * 如果是其他申请，那么导入的数据库表有：（out）inApply、product、relation
	 * </p>
	 * <p>
	 * 导入的文件类型（更新入库申请、更新出库申请、轮换入库申请、轮换出库申请、新入库）的判定
	 * 是通过导入的文件的文件名来判定的，所以，当将要导入的文件名被修改之后，
	 * 系统会认为导入文件被篡改了，不允许导入。
	 * </p>
	 * 
	 * @param request
	 * @param response
	 */
	public boolean importFile(HttpServletRequest request,
			HttpServletResponse response, ApplyHandleService applyHandleService) {
		// 上传文件
		uploadFile = new UploadFile();
		Map<String, String> map = uploadFile.uploadFile(request, response);
		
		if(map == null || map.size() == 0){
			request.setAttribute("runStatus", false);
			return false;
		}
		// 文件上传之后在服务器中的路径
		String filePath = map.get("fileName");
		//add by lyt 用来写日志
		// 操作类型(应该把RK放在最后面（更"新入库"、"新入库"）)
		String fileType ="";
		String applyType = "";
		
		String[] meansStr = filePath.split("_");
		if(StringUtil.UPDATE_OUT.equals(meansStr[1])){
//		if (filePath.indexOf(StringUtil.UPDATE_OUT) != -1){
			
				applyType = "GXCK";
				fileType="更新出库";
			}
		else if (StringUtil.BORROW_IN.equals(meansStr[1]))
			{
				applyType = "LHRK";
				fileType="轮换入库";
			}
		else if (StringUtil.BORROW_OUT.equals(meansStr[1]))
			{
				applyType = "LHCK";
				fileType="轮换出库";
			}
		else if (StringUtil.UPDATE_IN.equals(meansStr[1]))
			{
				applyType = "GXRK";
				fileType="更新入库";
			}
		else if (StringUtil.NEW_IN.equals(meansStr[1]))
			{
				applyType = "RK";
				fileType="新入库";
			}
		else {
			request.setAttribute("runStatus", false);
			return false;
		}
		// print
		doLog(request, "导入审核文件", "文件类型为："+fileType);
		// 从excel表中读取数据
		applyFormOperationService = new ApplyFormOperation();
		Map<Integer, List<ArrayList<String>>> dyadicArray = null;

		if ("RK".equals(applyType)) {
			//入库申请，多从excel表中读取一个contract表
			dyadicArray = applyFormOperationService.importAllSheetFromExcel(filePath, 4);
		} else {
			dyadicArray = applyFormOperationService.importAllSheetFromExcel(filePath, 3);
		}
		// 删除上传的文件
		File tempFile = new File(filePath);
		if (tempFile.exists()) {
			tempFile.delete();
		}

		// 操作类型是否为入库
		boolean isIn = false;
		// 操作类型是否为轮换出库申请
		boolean isBorrowOutApply = false;
		// 操作类型是否为更新出库申请
		boolean isUpdateOutApply = false;

		boolean runStatus = false;

		if ("RK".equals(applyType)) {
			isIn = true;
			//保存in申请表、关系表、产品表
			runStatus = applyHandleService.saveInApplys(dyadicArray.get(0),
					dyadicArray.get(1), dyadicArray.get(2));
			if (runStatus) {
				//保存合同表
				runStatus = contractHandleService.saveContracts(dyadicArray.get(3));
			}
		} else if ("LHRK".equals(applyType) || "GXRK".equals(applyType)) {
			isIn = true;
			//保存in申请表、关系表、产品表
			runStatus = applyHandleService.saveInApplys(dyadicArray.get(0),
					dyadicArray.get(1), dyadicArray.get(2));
		} else if ("LHCK".equals(applyType) || "GXCK".equals(applyType)) {
			if ("LHCK".equals(applyType))
				isBorrowOutApply = true;
			else
				isUpdateOutApply = true;
			//保存out申请表、关系表、产品表
			runStatus = applyHandleService.saveOutApplys(dyadicArray.get(0),
					dyadicArray.get(1), dyadicArray.get(2));
		}

		if (runStatus) {
			request.setAttribute("dyadicArray", dyadicArray.get(0));
		} else {
			request.setAttribute("dyadicArray", "");
		}
		// print debugging
		if (runStatus)
			System.out.println("插入成功");
		else
			System.out.println("插入失败");

		request.setAttribute("runStatus", runStatus);// 执行状态
		request.setAttribute("username",request.getSession().getAttribute("username"));
		request.setAttribute("isIn", isIn);
		request.setAttribute("isBorrowOutApply", isBorrowOutApply);
		request.setAttribute("isUpdateOutApply", isUpdateOutApply);
		request.setAttribute("applyType", applyType);// 申请方式

		return runStatus;
	}

	/**
	 * 上报备份数据\下发信息数据的导入
	 * 
	 * <p>
	 * 这个方法和上面的方法的功能基本相同，
	 * 只是此方法无论是入库还是出库，都只导出三个表（申请表、关系表、产品表）
	 * </p>
	 * @param request
	 * @param response
	 * @param applyHandleService
	 * @return
	 */
	public boolean importBackUpFile(HttpServletRequest request,
			HttpServletResponse response, ApplyHandleService applyHandleService) {
		// 上传文件
		uploadFile = new UploadFile();
		Map<String, String> map = uploadFile.uploadFile(request, response);
		if(map == null || map.size() == 0){
			request.setAttribute("runStatus", false);
			return false;
		}
		
		// 文件上传之后在服务器中的路径
		String filePath = map.get("fileName");
		// 操作类型
		String applyType = map.get("applyType");
		if (applyType == null) {
			applyType = request.getParameter("applyType");
		}
		// print
		System.out.println("applyType = " + applyType);
		// 从excel表中读取数据
		applyFormOperationService = new ApplyFormOperation();
		Map<Integer, List<ArrayList<String>>> dyadicArray = null;

		dyadicArray = applyFormOperationService.importAllSheetFromExcel(
				filePath, 3);
		// 删除上传的文件
		File tempFile = new File(filePath);
		if (tempFile.exists()) {
			tempFile.delete();
		}

		// 操作类型是否为入库
		boolean isIn = false;
		// 操作类型是否为轮换出库申请
		boolean isBorrowOutApply = false;
		// 操作类型是否为更新出库申请
		boolean isUpdateOutApply = false;

		boolean runStatus = false;

		if ("RK".equals(applyType) || "LHRK".equals(applyType)) {
			isIn = true;
			// 操作数据库:插入申请表、关系表、产品表
			runStatus = applyHandleService.saveInApplys(dyadicArray.get(0),
					dyadicArray.get(1), dyadicArray.get(2));
		} else if ("LHCK".equals(applyType) || "GXCK".equals(applyType)) {
			if ("LHCK".equals(applyType))
				isBorrowOutApply = true;
			else
				isUpdateOutApply = true;

			// 操作数据库:插入申请表、关系表、产品表
			runStatus = applyHandleService.saveOutApplys(dyadicArray.get(0),
					dyadicArray.get(1), dyadicArray.get(2));
		}
		if (runStatus) {
			request.setAttribute("dyadicArray", dyadicArray.get(0));
		} else {
			request.setAttribute("dyadicArray", "");
		}
		// print debugging
		if (runStatus)
			System.out.println("插入成功");
		else
			System.out.println("插入失败");

		request.setAttribute("runStatus", runStatus);// 执行状态
		request.setAttribute("username",request.getSession().getAttribute("username"));
		request.setAttribute("isIn", isIn);
		request.setAttribute("isBorrowOutApply", isBorrowOutApply);
		request.setAttribute("isUpdateOutApply", isUpdateOutApply);
		request.setAttribute("applyType", applyType);// 申请方式

		return runStatus;
	}

	/**
	 * 军代室,军代局、指挥局,审核导入的文件
	 * 
	 * @param request
	 * @param response
	 * @param checkPerson
	 *            审核人（当前登陆的用户）
	 * @throws IOException
	 */
	public void checkFileInJDS(HttpServletRequest request,HttpServletResponse response, 
			String checkPerson,ApplyHandleService applyHandleService) throws IOException {
		String applyType = (String) request.getParameter("applyType");
		String jsonStr = request.getParameter("data");
		String ownedUnit_JDS = request.getParameter("ownedUnit");
		// print
//		System.out.println("applyType = " + applyType);
//		System.out.println("data = " + jsonStr);
//		System.out.println("ownedUnit = " + ownedUnit_JDS);

		JSONArray jsonDyadic = JSONArray.fromObject(jsonStr);
		List<ArrayList<String>> dyadic = new ArrayList<ArrayList<String>>();

		// 增加标题行
		ArrayList<String> headLineArray = new ArrayList<String>();
		boolean flag = false;
		JSONArray tempArray = null;
		if ("RK".equals(applyType) || "LHRK".equals(applyType)||"GXRK".equals(applyType)) {
			headLineArray.add("inId");
			headLineArray.add("chStatus");
			headLineArray.add("ownedUnit");
			dyadic.add(headLineArray);

			for (int i = 0; i < jsonDyadic.size(); i++) {
				ArrayList<String> array = new ArrayList<String>();

				tempArray = jsonDyadic.getJSONArray(i);

				// 只取inId和审核状态
				array.add(tempArray.get(0).toString());
				array.add(tempArray.get(1).toString());
				array.add(ownedUnit_JDS);
				dyadic.add(array);
			}
			doLog(request, "审核文件", "审核入库申请文件");
			// 更新数据库inApply表和product表的审核状态
			flag = applyHandleService.changeInApplyCheckStatus(dyadic,checkPerson, applyType);
		} else {
			headLineArray.add("outId");
			headLineArray.add("chStatus");
			headLineArray.add("ownedUnit");
			dyadic.add(headLineArray);

			for (int i = 0; i < jsonDyadic.size(); i++) {
				ArrayList<String> array = new ArrayList<String>();

				tempArray = jsonDyadic.getJSONArray(i);

				// 只取inId和审核状态
				array.add(tempArray.get(0).toString());
				array.add(tempArray.get(1).toString());
				array.add(ownedUnit_JDS);
				dyadic.add(array);
			}
			doLog(request, "审核文件", "审核出库申请文件");
			flag = applyHandleService.changeOutApplyChStatus(dyadic,checkPerson);
		}// end else
		response.setContentType("text/plain,charset=utf8");
		PrintWriter writer = response.getWriter();
		if (flag) {
			writer.write("保存成功！");
		} else {
			writer.write("0");
		}
		return;
	}

	/**
	 * 日志
	 * 
	 * @param request
	 * @param opertetype
	 * @param remark
	 */
	private void doLog(HttpServletRequest request, String opertetype,
			String remark) {
		// 记录日志的代码
		HttpSession session = request.getSession();
		Log log = new Log();
		// 当前登录的用户名已经保存在session中
		log.setUserName((String) session.getAttribute("username")); 
		// 记录当前用户进行**操作的时间
		log.setOperateTime(new Date()); 
		log.setOperateType(opertetype);
		log.setRemark(remark);
		// 记录当前用户操作到数据库
		UserLogService.SaveOperateLog(log); 
	}
}
