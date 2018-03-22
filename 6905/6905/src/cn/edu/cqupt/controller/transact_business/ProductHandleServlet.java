package cn.edu.cqupt.controller.transact_business;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.beans.Unit;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.sys_management.HandleServiceOfBaseData;
import cn.edu.cqupt.service.transact_business.AddInApplyService;
import cn.edu.cqupt.service.transact_business.ContractHandleService;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;

public class ProductHandleServlet extends HttpServlet {
	private ProductHandleService service = new ProductHandleService();
	/**
	 * 
	 */
	private static final long serialVersionUID = -2623633867304734939L;

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String operate = request.getParameter("operate");
		String message = "";
		String path ="";
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		//version==1表示是企业版
				if("1".equals(version)){
					//举例：判断当前用户是否具有 业务办理  权限
					if(CurrentUser.isContractManage(request)){
						if("addproduct".equals(operate)) {
							//增加日志
							String contractId = request.getParameter("contractid");
							request.setAttribute("message", message);
							request.setAttribute("contractid", contractId);
							String jsonStr = request.getParameter("data");
							JSONArray jarray = JSONArray.fromObject(jsonStr);
							List<Product> pros = new ArrayList<Product>();
							for (int i = 0; i < jarray.size(); i++) {
								Product pro = new Product();
								JSONObject obj = jarray.getJSONObject(i);
								pro.setContractId(obj.getString("contractId").trim());
								pro.setWholeName(obj.getString("productName").trim());
								pro.setProductModel(obj.getString("productModel").trim());
								pro.setProductUnit(obj.getString("productUnit").trim());
								pro.setMeasureUnit(obj.getString("measureUnit").trim());
								pro.setProductPrice(obj.getString("price").trim());
								pro.setProductCount(obj.getString("productCount").trim());
								pro.setSignTime(MyDateFormat.changeStringToDate(obj.getString("signTime").trim()));
								pro.setDeliveryTime(MyDateFormat.changeStringToDate(obj.getString("deliveryTime").trim()));
								pro.setManufacturer(obj.getString("manufacturer").trim());
								pro.setKeeper(obj.getString("keeper").trim());
								pro.setBuyer(obj.getString("buyer").trim());
								pro.setProductName(pro.getWholeName()+pro.getProductUnit());
								//没有存进去
								pro.setDeviceNo(obj.getString("deviceNo").trim());
								pro.setPMNM(obj.getString("PMNM").trim());
								pros.add(pro);
								this.saveLog(request, response, "新增产品", 0, "产品型号："+pro.getProductModel()+",单元："+pro.getProductUnit());
							}
							String ownedUnit = (String)request.getSession().getAttribute("ownedUnit");
							message = addProduct(pros,ownedUnit);
							response.setContentType("text/plain;charset=UTF-8");
							response.getWriter().write(message);
							path = "";
						}else if("gotoproduct".equals(operate)) {
							//20151209
							//这里需要查询所有的整机名称、单元名称
							String status = "all";
							String contractId = request.getParameter("contractid");
							ContractHandleService service = new ContractHandleService();
							Contract contract = service.queryContractById(contractId);
							HandleServiceOfBaseData baseService = new HandleServiceOfBaseData();
							Map<String,List<String>> result = baseService.findAllPmnm();
							request.setAttribute("contract", contract);
							request.setAttribute("pmnm", result.get("pmnm"));
							request.setAttribute("pname", result.get("pname"));
							request.setAttribute("uname", result.get("uname"));
							path = queryProduct(request, response,status,"addProduct");
						}else if("getNextPage".equals(operate)) {
							int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
//							int isNext = Integer.parseInt(request.getParameter("isNext"));
							/*int nextPag;
							if(isNext == 0) {
								nextPage = curPageNum -1;
							}else {
								nextPage = curPageNum + 1;
							}*/
							List<HashMap<String, String>> products = this.queryProductsByContractId(request,response,curPageNum,10,"all");
//							List<Integer> counts = this.queryProNumByContractId(request, response,status);
							JSONArray jarray = new JSONArray();
							for(int i=0;i<products.size();i++) {
								JSONArray temp = new JSONArray();
								int j=0;
								temp.add(j++, products.get(i).get("contractid"));
								temp.add(j++, products.get(i).get("productModel"));
								temp.add(j++,products.get(i).get("productName"));
								temp.add(j++,products.get(i).get("productUnit"));
								temp.add(j++, products.get(i).get("productPrice"));
								temp.add(j++, products.get(i).get("measureUnit"));
								temp.add(j++, products.get(i).get("count"));
								temp.add(j++, Double.parseDouble(products.get(i).get("count"))*Double.parseDouble(products.get(i).get("productPrice")));
								temp.add(j++, products.get(i).get("deliveryTime"));
								temp.add(j++, products.get(i).get("manufacturer"));
								temp.add(j++, products.get(i).get("keeper"));
								temp.add(j++, products.get(i).get("proStatus"));
								jarray.add(temp);
							}
							int sum = this.querySumByContractId(request,response,"all");
							sum = sum%10==0?sum/10:(sum/10+1);
							Map<String,Object> returnData = new HashMap<String, Object>();
							returnData.put("totalPage", sum);
//							returnData.put("nextPage", nextPage);
							returnData.put("items", jarray);
							returnData.put("nowPage", curPageNum);
							JSONObject returnJo = JSONObject.fromObject(returnData);
//							System.out.println("returnData:"+returnJo.toString());
							response.setContentType("text/plain,charset=utf-8");
							response.getWriter().write(returnJo.toString());
							path = "";
						}else if("queryproduct".equals(operate)) {
							String status = "未到库";
							path = queryProductUnArrival(request, response,status,"queryProduct");
						}else if(operate.equalsIgnoreCase("singleInApply"))
						{
							String productId = request.getParameter("productId");
							Product queryProduct = this.queryProductByProductId(productId);
							request.setAttribute("product", queryProduct);
							path = "/jsp/qy/transact_business/singleApply.jsp";
						}
						else if(operate.equalsIgnoreCase("batchInApply"))
						{
							String contractId = request.getParameter("contractId");
							String model = request.getParameter("model");
							String unit = request.getParameter("unit"); 
							List<String> result = this.queryProductsByContractIdProductModelAndProductUnit(contractId, model, unit);
							result.add(contractId);
							request.setAttribute("result", result);
							request.setAttribute("isAddBatch", true);
							path = "/jsp/qy/transact_business/batchApply.jsp";
						}else if("goForPmnm".equals(operate)) {
							String pmnm = request.getParameter("pmnm").trim();
							//pmnm = new String(pmnm.getBytes("ISO-8859-1"),"utf-8");//
							Map<String,String> result = new HandleServiceOfBaseData().findInfoByPmnm(pmnm);
							//这里不用查询unit，故注释
							//by 刘雨恬
							//List<Unit> units = new HandleServiceOfBaseData().findAllUnitByPmnm(pmnm);
							response.setContentType("text/plain;charset=UTF-8");
							StringBuffer sb = new StringBuffer();
							if(result.size() >0) {
								JSONObject jsonObject = JSONObject.fromObject(result);
								sb.append(jsonObject.toString());
							}
							/*if(units.size()>0) {
								JSONArray jsonObject2 = JSONArray.fromObject(units);
								sb.append(","+jsonObject2.toString().replace("[",""));
							}else {
								sb.append("]");
							}
							sb.insert(0, '[');*/
							response.getWriter().write(sb.toString());
							path = "";
						}else if("checkDeviceNo".equals(operate)) {
							String deviceNo = request.getParameter("dNo");
							boolean exist = service.isDeviceNoExist(deviceNo);
							response.setContentType("text/plain;charset=UTF-8");
							String msg = "1";
							if(exist) {
								msg = "0";
							}
							response.getWriter().write(msg);
						}else if("getDeviceNo".equals(operate)) {
							String pmnm = request.getParameter("pmnm").trim();
							String productName = request.getParameter("pname").trim();
							String productUnit = request.getParameter("punit").trim();
							List<String> deviceNo = service.queryDeviceNoInByThreeP(pmnm, productName, productUnit);
							JSONArray jarray = JSONArray.fromObject(deviceNo);
							response.setContentType("text/plain,charset=utf-8");
							response.getWriter().write(jarray.toString());
						}else if("getDeviceNoNot".equals(operate)) {
							String pmnm = request.getParameter("pmnm");
							String productName = request.getParameter("pname");
							String productUnit = request.getParameter("punit");
							List<String> deviceNo = service.queryDeviceNoApplyByThreeP(pmnm, productName, productUnit);
							JSONArray jarray = JSONArray.fromObject(deviceNo);
							response.setContentType("text/plain,charset=utf-8");
							response.getWriter().write(jarray.toString());
						}else if("goForUnit".equals(operate)) {
							String unitName = request.getParameter("unitName");
							Unit unit = service.findUnitByName(unitName);
							JSONObject jo = JSONObject.fromObject(unit);
							response.setContentType("text/plain,charset=utf-8");
							response.getWriter().write(jo.toString());
						}else if("findByNo".equals(operate)) {
							String deviceNo = request.getParameter("deviceNo").trim();
							String isIn = request.getParameter("isIn");
							boolean flag = false;
							if("1".equals(isIn)) {
								flag = true;
							}
							Product pro = new ProductHandleService().findProductyNo(deviceNo,flag);
							JSONObject jo = new JSONObject();
							jo.put("productName", pro.getProductName());
							jo.put("productModel", pro.getProductModel());
							jo.put("unitName", pro.getProductUnit());
							jo.put("pmnm", pro.getPMNM());
							jo.put("price", pro.getProductPrice());
							jo.put("measure", pro.getMeasureUnit());
							jo.put("manuf", pro.getManufacturer());
							jo.put("keeper", pro.getKeeper());
							jo.put("location", pro.getLocation());
							jo.put("pDate", pro.getProducedDate());
							jo.put("maintain", pro.getMaintainCycle());
							jo.put("wholename", pro.getWholeName());
							jo.put("storage", pro.getStorageTime());
							response.setContentType("text/plain,charset=utf-8");
							response.getWriter().write(jo.toString());
						}else if("findByNoPre".equals(operate)) {
							String deviceNo = request.getParameter("deviceNo").trim();
							Product pro = new ProductHandleService().findProductyNo(deviceNo);
							JSONObject jo = new JSONObject();
							jo.put("productName", pro.getProductName());
							jo.put("productModel", pro.getProductModel());
							jo.put("unitName", pro.getProductUnit());
							jo.put("pmnm", pro.getPMNM());
							jo.put("price", pro.getProductPrice());
							jo.put("measure", pro.getMeasureUnit());
							jo.put("manuf", pro.getManufacturer());
							jo.put("keeper", pro.getKeeper());
							jo.put("location", pro.getLocation());
							jo.put("pDate", pro.getProducedDate());
							jo.put("maintain", pro.getMaintainCycle());
							jo.put("wholename", pro.getWholeName());
							System.out.println(jo.toString());
							response.setContentType("text/plain,charset=utf-8");
							response.getWriter().write(jo.toString());
						}
						if(!"".equals(path)) {
							request.setAttribute("message", message);
							request.getRequestDispatcher(path).forward(request, response);
						}
					}
					else{
						System.out.println("没有权限");
					}
				}
				else if("2".equals(version)){
					if(CurrentUser.isContractManage(request)) {
						if("goForPmnmAndKeeper".equals(operate)) {
							String pmnm = request.getParameter("pmnm").trim();
							String keeper = request.getParameter("keeper").trim();
							List<String> result = new HandleServiceOfBaseData().findInfoByPmnmAndKeeper(pmnm,keeper);
							JSONArray jo = JSONArray.fromObject(result);
							/*String productModel = request.getParameter("productModel").trim();
							String measureUnit = request.getParameter("measureUnit").trim();
							
							response.setContentType("text/plain;charset=UTF-8");
							Map<String,String> paramsMap = new HashMap<String,String>();
							paramsMap.put("PMNM", pmnm);
							paramsMap.put("productModel", productModel);
							paramsMap.put("measureUnit", measureUnit);
							int count = -1;
							count = new ProductHandleService().getProductCountByPMNM(paramsMap);
							//pmnm = new String(pmnm.getBytes("ISO-8859-1"),"utf-8");//
							
							Map<String,String> result = new HandleServiceOfBaseData().findInfoByPmnmAndKeeper(pmnm,keeper);
							JSONObject jo = JSONObject.fromObject(result);
							jo.put("count", count);
							response.getWriter().write(jo.toString());*/
							response.getWriter().write(jo.toString());
							path = "";
						}else if("goForMeasure".equals(operate)) {
							String pmnm = request.getParameter("pmnm").trim();
							String model = request.getParameter("model").trim();
							String keeper = request.getParameter("keeper").trim();
							List<String> result = new HandleServiceOfBaseData().findInfoByPmnmModel(pmnm, keeper, model);
							JSONArray jo = JSONArray.fromObject(result);
							response.getWriter().write(jo.toString());
							path = "";
						}else if("goForCount".equals(operate)) {
							String pmnm = request.getParameter("pmnm").trim();
							String keeper = request.getParameter("keeper").trim();
							String productModel = request.getParameter("model").trim();
							String measureUnit = request.getParameter("measure").trim();
							
							response.setContentType("text/plain;charset=UTF-8");
							Map<String,String> paramsMap = new HashMap<String,String>();
							paramsMap.put("PMNM", pmnm);
							paramsMap.put("productModel", productModel);
							paramsMap.put("measureUnit", measureUnit);
							Map<String,String> result = new HashMap<String, String>();
							result = new ProductHandleService().getProductCountByPMNM(paramsMap);
							//pmnm = new String(pmnm.getBytes("ISO-8859-1"),"utf-8");//
							
							JSONObject jo = JSONObject.fromObject(result);
							response.getWriter().write(jo.toString());
						}
						
						if(!"".equals(path)) {
							request.setAttribute("message", message);
							request.getRequestDispatcher(path).forward(request, response);
						}
					}
				}
	}

	private String queryProduct(HttpServletRequest request,
			HttpServletResponse response,String status,String curPath) {
		String path;
		int curPageNum;
		int pageSize;
		if(request.getParameter("curPageNum") == null || request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		}else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		List<HashMap<String, String>> products = this.queryProductsByContractId(request,response,curPageNum,pageSize,status);
//		List<Integer> counts = this.queryProNumByContractId(request, response,status);
		int sum = this.querySumByContractId(request,response,status);
		request.setAttribute("contractid", request.getParameter("contractid"));
		request.setAttribute("sum", sum);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
//		request.setAttribute("counts", counts);
		request.setAttribute("products", products);
		path = "/jsp/qy/transact_business/"+curPath+".jsp?curPageNum="+curPageNum+"&pageSize="+pageSize;
		return path;
	}
	
	private String queryProductUnArrival(HttpServletRequest request,
			HttpServletResponse response,String status,String curPath) {
		String path;
		int curPageNum;
		int pageSize;
		if(request.getParameter("curPageNum") == null || request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		}else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}
		List<HashMap<String, String>> products = this.queryProductsByContractId(request,response,curPageNum,pageSize,status);
//		List<Integer> counts = this.queryProNumByContractId(request, response,status);
		int sum = this.querySumByContractId(request,response,status);
		String contractId = request.getParameter("contractid");
		System.out.println("contractid:" +contractId);
		request.setAttribute("contractid", contractId);
		request.setAttribute("sum", sum);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
//		request.setAttribute("counts", counts);
		request.setAttribute("products", products);
		path = "/jsp/qy/transact_business/"+curPath+".jsp?curPageNum="+curPageNum+"&pageSize="+pageSize;
		return path;
	}

	private int querySumByContractId(HttpServletRequest request,
			HttpServletResponse response, String status) {
		int sum = 0;
		String contractId = request.getParameter("contractid");
		sum = service.getSumByContractId(contractId,status);
		return sum;
	}


	private List<HashMap<String, String>> queryProductsByContractId(HttpServletRequest request,
			HttpServletResponse response,int curPageNum,int pageSize,String status) {
		List<HashMap<String, String>> products = new ArrayList<HashMap<String,String>>();
		String contractId = request.getParameter("contractid");
		products = service.queryProductByContractId(contractId,curPageNum,pageSize,status);
		return products;
	}

	private String addProduct(List<Product> pros,String ownedUnit) {
		String message = "";
		boolean flag = service.SaveProduct(pros,ownedUnit);
		if(flag) {
			message = "1";
		}else {
			message = "0";
		}
		return message;
	}
	
	private Product queryProductByProductId(String productId)
	{
		AddInApplyService inService = new AddInApplyService();
		return inService.singleApplyQueryProduct(Long.parseLong(productId));
	}
	
	/**
	 * 记录日志
	 * @param operateType
	 * @return
	 */
	private boolean saveLog(HttpServletRequest request,HttpServletResponse response,String operateType,long pId,String remark) {
		boolean flag = false;
		HttpSession session = request.getSession();
		Log log = new Log();
		log.setUserName((String)session.getAttribute("username"));
		log.setOperateTime(new Date());
		log.setOperateType(operateType);
		log.setProductId(pId);
		log.setRemark(remark);
		flag = UserLogService.SaveOperateLog(log);
		return flag;
	}
	
	private List<String> queryProductsByContractIdProductModelAndProductUnit(String contractId, String model, String unit) {
		List<String> info = new ArrayList<String>();
		info = service.getProductByContractIdProductModelAndProductUnit(contractId, model, unit);
		return info;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
