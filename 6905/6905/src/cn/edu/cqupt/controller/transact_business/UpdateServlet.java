package cn.edu.cqupt.controller.transact_business;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.dao.OutApplyDAO;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.sys_management.HandleServiceOfBaseData;
import cn.edu.cqupt.service.transact_business.ApplyFormOperation;
import cn.edu.cqupt.service.transact_business.ApplyHandleService;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class UpdateServlet extends HttpServlet {

	private UploadFile uploadFile = null;
	private ApplyFormOperation applyFormOperation = null;
	private ApplyHandleService applyHandleService = null;
	private ProductHandleService productHandleService = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		String operate = request.getParameter("operate");
		List<String> message = new ArrayList<String>();
		String path = "";
		// 获取当前版本
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		// version==1表示是企业版
		if ("1".equals(version)) {
			String ownedUnit = (String)request.getSession().getAttribute("ownedUnit");
			// 举例：判断当前用户是否具有 业务办理 权限
			// CurrentUser.isContractManage(request)
			if (CurrentUser.isContractManage(request)) {
				if ("updateInOut".equals(operate)) {
					applyHandleService = new ApplyHandleService();
					List<InApply> inApplyList = applyHandleService.getInApply(0, 10, true);
					
					request.setAttribute("inApplyList", inApplyList);
					
					path = "/jsp/qy/transact_business/updateBusinessQueryProduct.jsp";
				} else if ("updateIn".equals(operate)) {

					path = "/jsp/qy/transact_business/updateBusinessAddUpdateInApply.jsp";
				} else if ("updateCheck".equals(operate)) {
					path = "/jsp/qy/transact_business/updateCheckBusiness.jsp";
				} else if ("updateInOutCheck".equals(operate)) {
					/**
					 * 企业“导入”更新入库或者更新出库文件
					 */
					uploadFile = new UploadFile();
					Map<String, String> map = uploadFile.uploadFile(request,response);
					boolean flag = false;
					// 此判断防止没有选择文件的时候出现异常
					if (map != null && map.size() != 0) {
						// //文件上传之后在服务器中的路径
						String filePath = map.get("fileName");
						
						String[] meansStr = filePath.split("_");
						if(StringUtil.UPDATE_IN.equals(meansStr[1]) || StringUtil.UPDATE_OUT.equals(meansStr[1])){
							// 将上传的文件导入到内存中，返回一个二维数组
							applyFormOperation = new ApplyFormOperation();
							Map<Integer,List<ArrayList<String>>> dyadicMap = null;
							dyadicMap = applyFormOperation.importAllSheetFromExcel(filePath, 3);
							//删除已经上传的文件
							File tempFile = new File(filePath);
							if (tempFile.exists()) {
								tempFile.delete();
							}
							applyHandleService = new ApplyHandleService();
							productHandleService = new ProductHandleService();
							if(StringUtil.UPDATE_IN.equals(meansStr[1])){
								//导入更新入库数据到数据库
								this.saveLog(request, response, "导入文件", 0, "导入更新入库审核文件");
								flag = applyHandleService.saveInApplys(dyadicMap.get(0), dyadicMap.get(1), dyadicMap.get(2));
								//根据审核结果相应地更新产品表
								if(flag)
									productHandleService.updateProduct_qy(dyadicMap.get(2));
							} else if(StringUtil.UPDATE_OUT.equals(meansStr[1])){
								//导入更新出库数据到数据库
								this.saveLog(request, response, "导入文件", 0, "导入更新出库审核文件");
								flag = applyHandleService.saveOutApplys(dyadicMap.get(0), dyadicMap.get(1), dyadicMap.get(2));
								//进行相应判断
								if(flag)
									flag = applyHandleService.changeOutApplyChStatus(dyadicMap.get(0),ownedUnit);
							}
						}//end if
					} //end if
					if (flag) {
						message.add("导入成功");
					} else {
						message.add("导入失败!请选择正确的文件!");
					}
					request.setAttribute("runStatus", flag);
					request.setAttribute("msg", message);
					path = "/jsp/qy/transact_business/updateCheckBusiness.jsp";
				}else if("updateQueryOperate".equals(operate)){
					//组合查询和模糊查询
//					String contractId = request.getParameter("contractId").trim();
//					String productType = request.getParameter("productType").trim();
//					String unitName = request.getParameter("unitName").trim();
//					String operateType = request.getParameter("operateType").trim();
//					String fromDateTemp = request.getParameter("fromDate").trim();
//					String toDateTemp = request.getParameter("toDate").trim();
//					String status = request.getParameter("status").trim();
//					
////					判断前台传来的日期参数是否为空，避免在格式转换的时候抛出异常
//					java.sql.Timestamp fromDate = null;
//					java.sql.Timestamp toDate = null;
//					if(fromDateTemp != null && !"".equals(fromDateTemp)&&toDateTemp != null && !"".equals(toDateTemp)){
//						fromDate = MyDateFormat.changeToSqlDate(MyDateFormat.changeLongStringToDate(fromDateTemp));
//						toDate = MyDateFormat.changeToSqlDate(MyDateFormat.changeLongStringToDate(toDateTemp));
//					}
					//查询

					//List<InApply> inApplyList = inApplyDao.selectInApply(contractId, productType, unitName, null, fromDate, toDate, status,1,0);
					
					//request.setAttribute("inApplyList", inApplyList);
					
					path = "/jsp/qy/transact_business/updateBusinessQueryProduct.jsp";
				} else if("getNextPage".equals(operate)){
					String curPageNum = request.getParameter("curPageNum");
					List<Map<String, String>> products = this.queryAjaxUpdatePros(request);
					JSONArray jarray = new JSONArray();
					int sum=Integer.parseInt(products.get(products.size()-1).get("totalPageSize"));
					products.remove(products.size() -1);
					for(int i=0;i<products.size();i++) {
						JSONArray temp = new JSONArray();
						int j=0;
						temp.add(j++, products.get(i).get("productId"));
						temp.add(j++, i+(Integer.parseInt(curPageNum)-1)*10+1);
						temp.add(j++, products.get(i).get("batch"));
						temp.add(j++, products.get(i).get("productName"));
						temp.add(j++, products.get(i).get("productModel"));
						temp.add(j++, products.get(i).get("productUnit"));
						temp.add(j++, products.get(i).get("deviceNo"));
						temp.add(j++, products.get(i).get("productPrice"));
						temp.add(j++, 1);
						temp.add(j++, "更新出库");
						temp.add(j++, products.get(i).get("insertTime"));
						temp.add(j++, products.get(i).get("PMNM"));
						temp.add(j++, products.get(i).get("measureUnit"));
						temp.add(j++, products.get(i).get("storageTime"));
						temp.add(j++, products.get(i).get("location"));
						String cycleStr = products.get(i).get("maintainCycle");
						//作数据转换 addby lyt 2015.10.25
						if("30d".equals(cycleStr)) {
							cycleStr = "一个月";
						} else if("90d".equals(cycleStr)) {
							cycleStr = "三个月";
						} else if("180d".equals(cycleStr)) {
							cycleStr = "六个月";
						} else if("365d".equals(cycleStr)) {
							cycleStr = "一年";
						}
						temp.add(j++, cycleStr);
						temp.add(j++, products.get(i).get("manufacturer"));
						temp.add(j++, products.get(i).get("keeper"));
						temp.add(j++, products.get(i).get("remark"));
						jarray.add(temp);
					}
					Map<String,Object> returnData = new HashMap<String, Object>();
					returnData.put("totalPage", sum);
//					returnData.put("nextPage", nextPage);
					returnData.put("items", jarray);
					returnData.put("nowPage", curPageNum);
					JSONObject returnJo = JSONObject.fromObject(returnData);
//					System.out.println("returnData:"+returnJo.toString());
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(returnJo.toString());
					path = "";
				} else if("UpdateInWarehouse".equals(operate)){
					/**
					 * 企业更新入库
					 */
					this.saveLog(request, response, "填写申请", 0, "填写更新入库申请");
					//解析json
					String jsonStr = request.getParameter("data");
					JSONArray jarray = JSONArray.fromObject(jsonStr);
					//changed by lyt 1108
					//非公共的信息
					//JSONArray jObject_private = (JSONArray) jObject.get("sPeInfo");
					//公共信息
					//JSONObject publicOJ = (JSONObject) jObject.get("pubInfo");
					List<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();
					int privateLen = jarray.size();
					for(int i = 0; i < privateLen; i++){
						HashMap<String,String> map = new HashMap<String,String>();
						JSONObject jo = jarray.getJSONObject(i);
						map.put("inMeans", jo.getString("means").trim());
						map.put("batch", jo.getString("batch").trim());
						//delete 入库的时候没有！！
					//	map.put("oldNum", jo.getString("oldNum"));
						
						map.put("wholename", jo.getString("wholename").trim());
						map.put("unit", jo.getString("unit").trim());
						map.put("deviceNo", jo.getString("dNo").trim());
						//map.put("oldDeviceNo", jo.getString("oldNo").trim());
						
						
						map.put("PMNM", jo.getString("pmnm").trim());
						map.put("measure", jo.getString("measure").trim());
						map.put("manuf", jo.getString("manuf").trim());
						map.put("keeper", jo.getString("keeper").trim());
						map.put("price", jo.getString("price").trim());
						map.put("location", jo.getString("location").trim());
						map.put("makeTime", jo.getString("makeTime").trim());
						map.put("maintain", jo.getString("maintain").trim());
						map.put("remark", jo.getString("remark").trim());
						//execTime
						/*map.put("productType", jo.getString("productType").trim());
						map.put("oldType", jo.getString("oldType").trim());
						map.put("oldPrice", jo.getString("oldPrice").trim());
						map.put("productType", jo.getString("productType").trim());
						map.put("storageTime", jo.getString("storageTime").trim());*/
						
						map.put("ownedUnit", ownedUnit);
						map.put("productId", jo.getString("preId"));
						
						mapList.add(map);
					}
					
					applyHandleService = new ApplyHandleService();
					boolean flag = applyHandleService.updateInAwarehouse(mapList,StringUtil.UPDATE_IN);
					//add by lyt
					response.setContentType("text/plain,charset=utf-8");
					//end
					if(flag){
						//changed by lyt
						//message.add("提交成功");
						response.getWriter().write("1");
					}else{
						//changed by lyt
						//message.add("提交失败");
						response.getWriter().write("0");
					}
					path = "";
				}else if("UpdateOutWarehouse".equals(operate)){
					/**
					 * 企业更新出库
					 */
					this.saveLog(request, response, "填写申请", 0, "填写更新出库申请");
					String jsonProducts = request.getParameter("pros");
					String jsonApply = request.getParameter("data");
					
					JSONArray proJarray = JSONArray.fromObject(jsonProducts);
					JSONObject jo = JSONObject.fromObject(jsonApply);
					
					HashMap<String,String> applyMap = new HashMap<String, String>();
					applyMap.put("outMeans", jo.getString("means").trim());
					applyMap.put("batch", jo.getString("batch").trim());
					applyMap.put("remark", jo.getString("remark").trim());
					applyMap.put("ownedUnit", ownedUnit);
					applyMap.put("num", jo.getString("num"));
					applyMap.put("price", jo.getString("price"));
					applyMap.put("oldPrice", jo.getString("oldPrice"));
					
					//用于存放相关产品机号以及产品型号
					List<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();
					int len = proJarray.size();
					for(int i = 0; i < len; i++){
						HashMap<String,String> map = new HashMap<String, String>();
						map.put("productModel", proJarray.getJSONArray(i).getString(0).trim());
						map.put("deviceNo", proJarray.getJSONArray(i).getString(1).trim());
						mapList.add(map);
					}
					
					//解析json
//					String jsonStr = request.getParameter("data");
//					
//					System.out.println("result = "+jsonStr);
//					
//					JSONObject jObject = JSONObject.fromObject(jsonStr);
//					JSONArray jObject_private = (JSONArray) jObject.get("sPeInfo");
//					JSONObject publicOJ = (JSONObject) jObject.get("pubInfo");
//					
//					List<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();
//					int privateLen = jObject_private.size();
//					for(int i = 0; i < privateLen; i++){
//						HashMap<String,String> map = new HashMap<String,String>();
//						
//						map.put("outMeans", publicOJ.getString("means").trim());
//						map.put("batch", publicOJ.getString("batch").trim());
//						map.put("num", publicOJ.getString("num").trim());
//						map.put("oldNum", publicOJ.getString("oldNum"));
//						
//						JSONObject jo = (JSONObject) jObject_private.get(i);
//						
//						map.put("wholename", jo.getString("wholename").trim());
//						map.put("unit", jo.getString("unit").trim());
//						map.put("deviceNo", jo.getString("dNo").trim());
//						map.put("PMNM", jo.getString("pmnm").trim());
//						map.put("measure", jo.getString("measure").trim());
//						map.put("manuf", jo.getString("manuf").trim());
//						map.put("keeper", jo.getString("keeper").trim());
//						map.put("price", jo.getString("price").trim());
//						map.put("location", jo.getString("location").trim());
//						map.put("makeTime", jo.getString("makeTime").trim());
//						map.put("maintain", jo.getString("maintain").trim());
//						map.put("remark", jo.getString("remark").trim());
//						//execTime
//						map.put("productType", jo.getString("productType").trim());
//						map.put("oldType", jo.getString("oldType").trim());
//						map.put("oldPrice", jo.getString("oldPrice").trim());
//						map.put("productType", jo.getString("productType").trim());
//						map.put("storageTime", jo.getString("storageTime").trim());
////						String ownedUnit = (String)request.getSession().getAttribute("ownedUnit");
//						map.put("ownedUnit", ownedUnit);
//						
//						mapList.add(map);
//					}
//					
					applyHandleService = new ApplyHandleService();
					boolean flag = applyHandleService.updateOutAwarehouse(mapList,applyMap);
					//add by lyt
					response.setContentType("text/plain,charset=utf-8");
					//end
					if(flag){
						response.getWriter().write("1");
					}else{
						response.getWriter().write("0");
					}
					path="";
					//end
				}else if("gotoUpdateIn".equals(operate)) {
					Map<String,List<String>> result = new HandleServiceOfBaseData().findAllPmnm();
					//查询所有不在库的机号
					List<String> dNos = new ProductHandleService().findAllInDeviceNo(false);
					request.setAttribute("pmnm", result.get("pmnm"));
					request.setAttribute("pname", result.get("pname"));
					path = queryUpdateOutPros(request);
				}else if("gotoUpdateOut".equals(operate)) {
					//解析json
					String jsonStr = request.getParameter("outData");
					JSONArray jarray = JSONArray.fromObject(jsonStr);
					Map<String,List<String>> result = new HandleServiceOfBaseData().findAllPmnm();
					//查询所有不在库的机号
					List<String> dNos = new ProductHandleService().findAllInDeviceNo(false);
					request.setAttribute("pmnm", result.get("pmnm"));
					request.setAttribute("pname", result.get("pname"));
					request.setAttribute("UpdateOutData",jarray.toString());
					path="/jsp/qy/transact_business/updateBusinessAddUpdateOutApply.jsp";
				}else if("queryProduct".equals(operate)){
					//用于企业更新入库的查询
					
					String status = request.getParameter("status");
					String flag = request.getParameter("flag");
					String curPageNum = request.getParameter("curPageNum");
					
					if(curPageNum == null) curPageNum = "0";
					
					List<Map<String,String>> resultList = null;
					String operateTime = request.getParameter("operateTime");
					String productModel = request.getParameter("productModel");
					Map<String,String> condition = new HashMap<String,String>();
					if(operateTime != null && !"".equals(operateTime)) {
						condition.put("operateTime", operateTime);
					}
					if(productModel != null && !"".equals(productModel)) {
						condition.put("productModel", productModel);
					}
					productHandleService = new ProductHandleService();
					resultList = productHandleService.getProducts(status, flag,curPageNum,"10",condition);
					
					String sum = resultList.get(resultList.size()-1).get("totalPageSize");
					resultList.remove(resultList.size()-1);
					
					request.setAttribute("products", resultList);
					request.setAttribute("sum", sum);
					
					//path?
				}

				request.setAttribute("message", message);
				if(path != ""){
					request.getRequestDispatcher(path).forward(request, response);
				}
			}
		} 
	}
	private  List<Map<String, String>> queryPros(HttpServletRequest request,HttpServletResponse response,int curPageNum,int pageSize) {
		List<Map<String, String>> products = new ArrayList<Map<String,String>>();
		String status = "更新出库";
		String flag = "1";
		productHandleService = new ProductHandleService();
		products = productHandleService.getProducts(status, flag,curPageNum,10);
		
		return products;
	}
	private String queryUpdateOutPros(HttpServletRequest request) {
		String status = "更新出库";
		String flag = "1";
		String curPageNum = request.getParameter("curPageNum");
		
		if(curPageNum == null) curPageNum = "1";
		
		List<Map<String,String>> resultList = null;
		String operateTime = request.getParameter("operateTime");
		String productModel = request.getParameter("productModel");
		Map<String,String> condition = new HashMap<String,String>();
		if(operateTime != null && !"".equals(operateTime)) {
			String strs[] = operateTime.split(" ");
			condition.put("operateTime", strs[0]);
		}
		if(productModel != null && !"".equals(productModel)) {
			condition.put("productModel", productModel);
		}
		productHandleService = new ProductHandleService();
		resultList = productHandleService.getProducts(status, flag,curPageNum,"10",condition);
		
		/*String sum = resultList.get(resultList.size()-1).get("totalPageSize");*/
		int sum = resultList.size();
		resultList.remove(resultList.size()-1);
		request.setAttribute("curPageNum", 1);
		request.setAttribute("pageSize", 10);
		request.setAttribute("products", resultList);
		request.setAttribute("sum", sum);
		String path = "jsp/qy/transact_business/updateBusinessAddUpdateInApply.jsp?curPageNum=1&pageSize=10";
		return path;
	}
	//ajax getNextPage
	private List<Map<String,String>> queryAjaxUpdatePros(HttpServletRequest request) {
		String status = "更新出库";
		String flag = "1";
		String curPageNum = request.getParameter("curPageNum");
		
		if(curPageNum == null) curPageNum = "1";
		
		List<Map<String,String>> resultList = null;
		String operateTime = request.getParameter("operateTime");
		String productModel = request.getParameter("productModel");
		Map<String,String> condition = new HashMap<String,String>();
		if(operateTime != null && !"".equals(operateTime)) {
			String strs[] = operateTime.split(" ");
			condition.put("operateTime", strs[0]);
		}
		if(productModel != null && !"".equals(productModel)) {
			condition.put("productModel", productModel);
		}
		productHandleService = new ProductHandleService();
		resultList = productHandleService.getProducts(status, flag,curPageNum,"10",condition);
		
		return resultList;
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
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}
}
