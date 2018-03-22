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
import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.dao.OutApplyDAO;
import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.query_business.ProductDetailService;
import cn.edu.cqupt.service.sys_management.HandleServiceOfBaseData;
import cn.edu.cqupt.service.transact_business.ApplyFormOperation;
import cn.edu.cqupt.service.transact_business.ApplyHandleService;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.CollectionTypeChange;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class BorrowServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2224738871199848407L;
	
	private UploadFile uploadFile = null;
	private ApplyFormOperation applyFormOperation = null;
	private ApplyHandleService applyHandleService = null;
	private ProductHandleService productHandleService = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String operate = request.getParameter("operate");
		String path ="";
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		//version==1表示是企业版
		if("1".equals(version)){
			//举例：判断当前用户是否具有 业务办理  权限
			//CurrentUser.isContractManage(request)
			if(CurrentUser.isContractManage(request)){
				if("borrowInOut".equals(operate)) {
					int curPageNum = 1;
					int pageSize = 10;
					if(request.getParameter("curPageNum") != null || request.getParameter("pageSize") != null) {
						curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request.getParameter("pageSize"));
					}
					List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
					result = this.queryProductBorrow(request, response,curPageNum,pageSize);
					int sum = (Integer) result.get(result.size() -1).get("sum");
					result.remove(result.size() - 1);
					Map<String,String> condition = this.getCondition(request);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("message", result);
					request.setAttribute("sum", sum);
					request.setAttribute("condition", condition);
				    path = "/jsp/qy/transact_business/borrowBusinessQueryProduct.jsp?curPageNum="+curPageNum+"&pageSize="+pageSize;
				}else if("updateInOut".equals(operate)) {
					int curPageNum = 1;
					int pageSize = 10;
					if(request.getParameter("curPageNum") != null || request.getParameter("pageSize") != null) {
						curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
						pageSize = Integer.parseInt(request.getParameter("pageSize"));
					}
					List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
					result = this.queryProductBorrow(request, response,curPageNum,pageSize);
					int sum = (Integer) result.get(result.size() -1).get("sum");
					result.remove(result.size() - 1);
					Map<String,String> condition = this.getCondition(request);
					request.setAttribute("curPageNum", curPageNum);
					request.setAttribute("pageSize", pageSize);
					request.setAttribute("message", result);
					request.setAttribute("sum", sum);
					request.setAttribute("condition", condition);
				    path = "/jsp/qy/transact_business/updateBusinessQueryProduct.jsp?curPageNum="+curPageNum+"&pageSize="+pageSize;
				}else if("getNextPage".equals(operate)){
					List<Map<String, String>> products = this.queryAjaxBorrowPros(request);
//					Map<String,Integer> inNum = new ProductHandleService().getEachProductModelNum();
					JSONArray jarray = new JSONArray();
					int sum=Integer.parseInt(products.get(products.size()-1).get("totalPageSize"));
					products.remove(products.size() -1);
					String curNum = request.getParameter("curPageNum");
					if("".equals(curNum) && curNum == null) { curNum = "1";}
					for(int i=0;i<products.size();i++) {
						JSONArray temp = new JSONArray();
						int j=0;
						temp.add(j++, products.get(i).get("productId"));
						temp.add(j++, (Integer.parseInt(curNum)-1)*10+i+1);
						temp.add(j++, products.get(i).get("batch"));
						temp.add(j++, products.get(i).get("productName"));
						temp.add(j++, products.get(i).get("productModel"));
						temp.add(j++, products.get(i).get("productUnit"));
						temp.add(j++, products.get(i).get("deviceNo"));
						temp.add(j++, products.get(i).get("productPrice"));
						temp.add(j++, 1);
						temp.add(j++, "轮换出库");
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
					//用于拼接在库型号产品数量
					/*JSONArray inNumArray = new JSONArray();
					for (String key : inNum.keySet()) {
						JSONObject jo = new JSONObject();
						jo.put("model", key);
						jo.put("totalNum", inNum.get(key));
						inNumArray.add(jo);
					}*/
					returnData.put("totalPage", sum);
//					returnData.put("nextPage", nextPage);
					returnData.put("items", jarray);
					returnData.put("nowPage", curNum);
					//returnData.put("numByModel", inNumArray);
					JSONObject returnJo = JSONObject.fromObject(returnData);
					
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(returnJo.toString());
					path = "";
				}else if("borrowCheck".equals(operate)) {
					
					path="/jsp/qy/transact_business/borrowCheckBusiness.jsp";
				}else if("applyUpdateInWarehouse".equals(operate)) {
					//message = saveInApply(request,response);
					path="/jsp/qy/transact_business/updateBusinessAddUpdateInApply.jsp";
				}else if("applyUpdateOutWarehouse".equals(operate)) {
					path="/jsp/qy/transact_business/updateBusinessAddUpdateOutApply.jsp";
				}
				else if("applyBorrowInWarehouse".equals(operate)) {
					//message = saveInApply(request,response);
					path="/jsp/qy/transact_business/borrowBusinessAddBorrowInApply.jsp";
				}else if("addInapply".equals(operate)) {
//					long inId = saveInApply(request,response);//将轮换申请存入数据库表中 并返回已存入申请记录的ID
//					if(saveInproductRelation(request,response,inId)){
//						message1="1";
//						//request.setAttribute("message", message1);
//					}
//					response.setContentType("text/plain;charset=UTF-8");
//					response.getWriter().write(message1);//将结果传回
				}else if ("borrowInOutCheck".equals(operate)) {
					/**
					 * 导入轮换出/入库申请
					 */
					List<String> message = new ArrayList<String>();
					// 上传导入文件到服务器中
					uploadFile = new UploadFile();
					Map<String, String> map = uploadFile.uploadFile(request,response);

					boolean flag = false;
					// 此判断防止没有选择文件的时候出现异常
					if (map != null && map.size() != 0) {
						// //文件上传之后在服务器中的路径
						String filePath = map.get("fileName");
						
						String[] meansStr = filePath.split("_");
						
						if(StringUtil.BORROW_IN.equals(meansStr[1]) || StringUtil.BORROW_OUT.equals(meansStr[1])){
							// 将上传的文件导入到内存中，返回一个二维数组
							applyFormOperation = new ApplyFormOperation();
							Map<Integer,List<ArrayList<String>>> dyadicMap = null;
							//118by liuyutian 没有读出文件
							dyadicMap = applyFormOperation.importAllSheetFromExcel(filePath, 3);
							
							//删除文件
							File tempFile = new File(filePath);
							if (tempFile.exists()) {
								tempFile.delete();
							}
							//数据库操作
							applyHandleService = new ApplyHandleService();
							productHandleService = new ProductHandleService();
							//添加日志
							this.saveLog(request, response, "导入", 0, "轮换审核文件导入");
							if(StringUtil.BORROW_IN.equals(meansStr[1])){
								//导入审核之后的文件并改变入库申请表和产品表中的状态
								flag = applyHandleService.saveInApplys(dyadicMap.get(0), dyadicMap.get(1), dyadicMap.get(2));
								//根据审核结果，相应地更新产品表
								if(flag)
									productHandleService.updateProduct_qy(dyadicMap.get(2));
							}else if(StringUtil.BORROW_OUT.equals(meansStr[1])){
								//导入审核之后的文件并改变出库申请表和产品表中的状态
								flag = applyHandleService.saveOutApplys(dyadicMap.get(0), dyadicMap.get(1), dyadicMap.get(2));
							}
						}//end if
					}//end if 
					
					if (flag) {
						message.add("导入成功");
					} else {
						message.add("导入失败!请选择正确的文件!");
					}
					request.setAttribute("runStatus", flag);
					request.setAttribute("msg", message);
					path = "/jsp/qy/transact_business/borrowCheckBusiness.jsp";
				}else if("addOutapply".equals(operate)) {
//					long inId = saveOutApply(request,response);
//					String data="0";
//					if(saveOutproductRelation(request,response,inId)){
//						data="1";
//						request.setAttribute("message", data);
//					}
//					//System.out.println(data);
//					response.setContentType("text/plain;charset=UTF-8");
//					response.getWriter().write(data);//将结果传回
					path ="";
				}else if("applyBorrowOutWarehouse".equals(operate)) {
					
					path="/jsp/qy/transact_business/borrowBusinessAddBorrowOutApply.jsp";
				}else if("BorrowOutWarehouse".equals(operate)){
					/**
					 * 企业轮换出库申请
					 * @author LiangYH
					 */

					//对应页面上面长表格
					String jsonStr1 = request.getParameter("pros");
					//对应页面下面短表格
					String jsonStr = request.getParameter("data");
					
					JSONArray proJarray = JSONArray.fromObject(jsonStr1);
					JSONObject jo = JSONObject.fromObject(jsonStr);
					
					System.out.println(proJarray.toString());
					System.out.println(jo.toString());
					
					//调整后后台修改  1012 by liuyutian
					HashMap<String,String> applyMap = new HashMap<String, String>();
					applyMap.put("outMeans", jo.getString("means").trim());
					applyMap.put("batch", jo.getString("batch").trim());
					applyMap.put("borrowLength", jo.getString("outTime").trim());
					applyMap.put("borrowReason", jo.getString("reason").trim());
					applyMap.put("remark", jo.getString("remark").trim());
					String ownedUnit = (String)request.getSession().getAttribute("ownedUnit");
					applyMap.put("ownedUnit", ownedUnit);
					
					//用于存放相关产品机号以及产品型号
					List<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();
					int len = proJarray.size();
					for(int i = 0; i < len; i++){
						HashMap<String,String> map = new HashMap<String, String>();
						map.put("productModel", proJarray.getJSONArray(i).getString(0).trim());
						map.put("deviceNo", proJarray.getJSONArray(i).getString(1).trim());
						mapList.add(map);
					}
					String message = "";
					
					//执行数据库操作
					ApplyHandleService applyHandleService = new ApplyHandleService();
					boolean runStatus = applyHandleService.borrowOutWarehouse(mapList,applyMap);
					//添加日志
					this.saveLog(request, response, "添加申请", 0, "轮换出库申请"
							);
					if(runStatus){
						message = "1";
					}else{
						message = "0";
					}
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(message);
					path = "";
				}else if("BorrowInWarehouse".equals(operate)){
					/**
					 * 企业轮换入库申请
					 * @author LiangYH
					 */
					
					//解析json
					String jsonStr = request.getParameter("data");
					JSONArray jarray = JSONArray.fromObject(jsonStr);
	
					List<HashMap<String, String>> mapList = new ArrayList<HashMap<String,String>>();
					//print debugging
					System.out.println("json = "+jarray.toString());
					
					int len = jarray.size();
					JSONObject jo = null;
					for(int i = 0; i < len; i++){
						HashMap<String,String> map = new HashMap<String, String>();
						
						jo = jarray.getJSONObject(i);
						map.put("inMeans", jo.getString("means").trim());
						map.put("batch", jo.getString("batch").trim());
						map.put("wholename", jo.getString("wholename").trim());
						map.put("unit", jo.getString("unit").trim());
						map.put("deviceNo", jo.getString("dNo").trim());
						map.put("PMNM", jo.getString("pmnm").trim());
						map.put("measure", jo.getString("measure").trim());
						map.put("manuf", jo.getString("manuf").trim());
						map.put("keeper", jo.getString("keeper").trim());
						map.put("price", jo.getString("price").trim());
						
						map.put("location", jo.getString("location").trim());
//						map.put("makeTime", jo.getString("makeTime").trim());
						map.put("maintain", jo.getString("maintain").trim());
						map.put("productId", jo.getString("preId"));
						/*
						//注意下面这个
						map.put("borrowLength", jo.getString("outTime"));
						map.put("borrowReason", jo.getString("reason"));
						*/
						map.put("remark", jo.getString("remark").trim());
						//execTime
						String ownedUnit = (String)request.getSession().getAttribute("ownedUnit");
						map.put("ownedUnit", ownedUnit);
						
						mapList.add(map);
					}
					
					String message = "";
					
					//执行数据库操作
					ApplyHandleService applyHandleService = new ApplyHandleService();
					//添加日志
					this.saveLog(request, response, "填写申请", 0, "填写轮换入库申请"
							);
					boolean runStatus = applyHandleService.borrowInWarehouse(mapList,StringUtil.BORROW_IN);
					if(runStatus){
						message = "1";
					}else{
						message = "0";
					}
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(message);
					
					path = "";
				}else if("gotoBorrowIn".equals(operate)) {
					// add by liuyutian 0823
					Map<String,List<String>> result = new HandleServiceOfBaseData().findAllPmnm();
					//查询所有不在库的机号
					List<String> dNos = new ProductHandleService().findAllInDeviceNo(false);
					request.setAttribute("pmnm", result.get("pmnm"));
					request.setAttribute("pname", result.get("pname"));
					request.setAttribute("dNos", dNos);
					path = queryBorrowOutPros(request);
				}else if("gotoBorrowOut".equals(operate)) {
					// add by liuyutian 0823
					
					/*//解析json
					String jsonStr = request.getParameter("outData");
					JSONArray jarray = JSONArray.fromObject(jsonStr);
					Map<String,List<String>> result = new HandleServiceOfBaseData().findAllPmnm();
					request.setAttribute("pmnm", result.get("pmnm"));
					request.setAttribute("pname", result.get("pname"));
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(jarray.toString());*/
					path = "jsp/qy/transact_business/borrowBusinessAddBorrowOutApply.jsp";
				}else if("queryProduct".equals(operate)){
					//用于企业轮换入库的产品查询
					
					/*String status = request.getParameter("status");
					String flag = request.getParameter("flag");
					String curPageNum = request.getParameter("curPageNum");*/
					
					queryBorrowOutPros(request);
					
					//path?
				}
				
				//request.setAttribute("message", message1);
				if(!"".equals(path)) {
					request.getRequestDispatcher(path).forward(request, response);
				}
			}
			
		}
	}

	private String queryBorrowOutPros(HttpServletRequest request) {
		String status = "轮换出库";
		String flag = "1";
		String curPageNum = request.getParameter("curPageNum");
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
		if(curPageNum == null) curPageNum = "1";
		
		List<Map<String,String>> resultList = null;
		
		productHandleService = new ProductHandleService();
		resultList = productHandleService.getProducts(status, flag,curPageNum,"10",condition);
		
		/*String sum = resultList.get(resultList.size()-1).get("totalPageSize");*/
		int sum = resultList.size();
		resultList.remove(resultList.size()-1);
		request.setAttribute("curPageNum", 1);
		request.setAttribute("pageSize", 10);
		request.setAttribute("products", resultList);
		request.setAttribute("sum", sum);
		String path = "jsp/qy/transact_business/borrowBusinessAddBorrowInApply.jsp?curPageNum=1&pageSize=10";
		return path;
	}
	
	//ajax getNextPage用
	private List<Map<String,String>> queryAjaxBorrowPros(HttpServletRequest request) {
		String status = "轮换出库";
		String flag = "1";
		String curPageNum = request.getParameter("curPageNum");
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
		if(curPageNum == null) curPageNum = "1";
		
		List<Map<String,String>> resultList = null;
		
		productHandleService = new ProductHandleService();
		resultList = productHandleService.getProducts(status, flag,curPageNum,"10",condition);
		return resultList;
	}
	/**
	 * 记录日志
	 * @author limengxin
	 * @param operateType
	 * @return
	 */
//	private boolean saveLog(HttpServletRequest request,HttpServletResponse response,String operateType,long pId,String remark) {
//		boolean flag = false;
//		HttpSession session = request.getSession();
//		Log log = new Log();
//		log.setUserName((String)session.getAttribute("username"));
//		log.setOperateTime(new Date());
//		
//		log.setOperateType(operateType);
//		log.setProductId(pId);
//		log.setRemark(remark);
//		flag = UserLogService.SaveOperateLog(log);
//		return flag;
//	}
	/**
	 *@author limengxin
	 *这个函数的是在向申请表添加轮换申请记录之后  查找其对应的产品ID 把这个轮换申请记录的inId和产品ID 存入产品—申请关联表中
	 */
//		private boolean saveOutproductRelation(HttpServletRequest request, HttpServletResponse response,long inId) {
//			// TODO Auto-generated method stub
//			Map<String,String> condition = new HashMap<String, String>();
//			int num=0;
//			List<Integer> ids=new ArrayList<Integer>();
//			ProductHandleService service=new ProductHandleService();
//			boolean flag=false;
//			
//			String contractId = request.getParameter("contractId");
//		    String productModel = request.getParameter("productModel");
//			String unitName = request.getParameter("UnitName");
//			String productUnit = request.getParameter("productUnit");
//
//			//String productPrice = request.getParameter("Price");
//			String manufacturer = request.getParameter("manufacturer");
//			//String PMNM=request.getParameter("PMNM");
//			
//			//获取轮换申请中申请产品的个数
//			if(request.getParameter("Num") != null&&!"".equals(request.getParameter("Num"))) {
//				num=Integer.parseInt(request.getParameter("Num"));
//				}
//			
//			//组织查询条件condition
//			if(!"".equals(contractId) && contractId != null) {
//				condition.put("P.contractId", contractId);
//			}
//		    if(!"".equals(productModel) && productModel != null) {
//		    	condition.put("P.productModel", productModel);
//		    }
////			if(!"".equals(unitName) && unitName != null) {
////				condition.put("P.productUnit", unitName);
////			}
//			if(!"".equals(productUnit) && productUnit != null) {
//				condition.put("P.productUnit", productUnit);
//			}
////			if(!"".equals(productPrice) && productPrice != null) {
////				condition.put("P.productPrice", productPrice);
////			}
//			if(!"".equals(manufacturer) && manufacturer != null) {
//				condition.put("P.manufacturer", manufacturer);
//			}
////			if(!"".equals(PMNM) && PMNM != null) {
////				condition.put("P.PMNM", PMNM);
////			}
//			ids=service.queryProductID(condition);
//			//kangziheng定义service
//			//kangziheng调用service
//			//System.out.println("查到的产品个数："+ids.size());
//			int i;
//			for(i=0;i<num;i++){
//				System.out.println(ids.get(i)+"--"+inId);
//				flag=service.saveOutproductRelation(ids.get(i),inId);//将 产品ID和申请ID存入 产品-申请 关联表
//				System.out.println(flag);
//				flag=service.updateProductStatuc(ids.get(i),"轮换出库待审核");
//				System.out.println(flag);
//				if(flag){
//					flag=saveLog(request, response,"申请轮换出库",ids.get(i),request.getParameter("remark"));
//					System.out.println(flag);
//				}
//			}
//			return flag;
//		}
/**
 *@author limengxin
 *这个函数的是在向申请表添加轮换申请记录之后  查找其对应的产品ID 把这个轮换申请记录的inId和产品ID 存入产品—申请关联表中
 */
//	private boolean saveInproductRelation(HttpServletRequest request, HttpServletResponse response,long inId) {
//		// TODO Auto-generated method stub
//		Map<String,String> condition = new HashMap<String, String>();
//		int num=0;
//		boolean flag=false;
//		List<Integer> ids=new ArrayList<Integer>();
//		ProductHandleService service=new ProductHandleService();
//		
//		
//		String contractId = request.getParameter("contractId");
//	    String productModel = request.getParameter("productModel");
//		String unitName = request.getParameter("UnitName");
//		String productUnit = request.getParameter("productUnit");
//
//		//String productPrice = request.getParameter("Price");
//		String manufacturer = request.getParameter("manufacturer");
//		//String PMNM=request.getParameter("PMNM");
//		
//		//获取轮换申请中申请产品的个数
//		if(request.getParameter("Num") != null&&!"".equals(request.getParameter("Num"))) {
//			num=Integer.parseInt(request.getParameter("Num"));
//			}
//		
//		//组织查询条件condition
//		if(!"".equals(contractId) && contractId != null) {
//			condition.put("P.contractId", contractId);
//		}
//	    if(!"".equals(productModel) && productModel != null) {
//	    	condition.put("P.productModel", productModel);
//	    }
//
//		if(!"".equals(productUnit) && productUnit != null) {
//			condition.put("P.productUnit", productUnit);
//		}
//
//		if(!"".equals(manufacturer) && manufacturer != null) {
//			condition.put("P.manufacturer", manufacturer);
//		}
////		if(!"".equals(PMNM) && PMNM != null) {
////			condition.put("P.PMNM", PMNM);
////		}
//		ids=service.queryProductID(condition);
//		System.out.println("查到的产品个数："+ids.size());
//		int i;
//		if(ids.size() >=num) {
//			for(i=0;i<num;i++){
//				System.out.println(ids.get(i)+inId);
//				flag=service.saveInproductRelation(ids.get(i),inId);//将 产品ID和申请ID存入 产品-申请 关联表
//				if("LHRK".equals(request.getParameter("type"))) {
//					flag=service.updateProductStatuc(ids.get(i),"轮换入库待审核");
//					if(flag){
//						saveLog(request, response,"申请轮换入库",ids.get(i),request.getParameter("remark"));
//					}
//				}else if("GXRK".equals(request.getParameter("type"))) {
//					flag=service.updateProductStatuc(ids.get(i),"更新入库待审核");
//					if(flag){
//						saveLog(request, response,"申请更新入库",ids.get(i),request.getParameter("remark"));
//					}
//				}
//			}
//		}
//		return flag;
//	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}


	/**
	 * 轮换管理中的查询产品在 servlet中的函数
	 * @author limengxin
	 */
	public List<HashMap<String, Object>> queryProductBorrow(HttpServletRequest request, HttpServletResponse response,int curPageNum,int pageSize){
		List<HashMap<String, Object>> result=new ArrayList<HashMap<String, Object>>();//返回值
		//HashMap<String, Object> condition=new HashMap<String, Object>();//条件
		ProductHandleService service=new ProductHandleService();
		Map<String, String> condition = getCondition(request);
		result = service.queryProductBorrow(condition,curPageNum,pageSize);
		return result;
		
	}
	private  List<Map<String, String>> queryPros(HttpServletRequest request,HttpServletResponse response,int curPageNum,int pageSize) {
		List<Map<String, String>> products = new ArrayList<Map<String,String>>();
		String status = "轮换出库";
		String flag = "1";
		productHandleService = new ProductHandleService();
		products = productHandleService.getProducts(status, flag,curPageNum,10);
		
		return products;
	}
	private Map<String, String> getCondition(HttpServletRequest request) {
		Map<String,String> condition = new HashMap<String, String>();
		String contractId = request.getParameter("P.contractId");

		String productModel = request.getParameter("productmodel");
		String unitName = request.getParameter("unitname");
		String signDate = request.getParameter("signdate");
		String productName = request.getParameter("productName");
		if(!"".equals(contractId) && contractId != null && !"null".equals(contractId)) {
			condition.put("P.contractId", contractId);
		}
	    if(!"".equals(productModel) && productModel != null && !"null".equals(productModel)) {
	    	condition.put("productModel", productModel);
	    }
		if(!"".equals(unitName) && unitName != null && !"null".equals(unitName)) {
			condition.put("productUnit", unitName);
		}
		if(!"".equals(signDate) && signDate != null && !"null".equals(signDate)) {
			condition.put("signTime", signDate);
		}if(!"".equals(productName) && productName != null && !"null".equals(productName)) {
			condition.put("productName", productName);
		}
		return condition;
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
