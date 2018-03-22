package cn.edu.cqupt.controller.transact_business;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;

/**
 * 进行申请记录的操作处理
 * 
 * @author lynn
 *
 */
public class ApplyHandleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1960331207476741118L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		String operate = request.getParameter("operate");
		String path = "";
		List<String> msgs = new ArrayList<String>();
		// version==1表示是企业版
		if (version.equals("1")) {
			// 举例：判断当前用户是否具有 业务办理 权限
			// CurrentUser.isContractManage(request)
			if (CurrentUser.isContractManage(request)) {
				if ("singleIn".equals(operate)) {
					path = "/jsp/qy/transact_business/singleApply.jsp";
				} else if ("batchIn".equals(operate)) {
					path = "/jsp/qy/transact_business/batchApply.jsp";
				} else if ("searchByInId".equals(operate)) {
					String inId = request.getParameter("inId");

					ProductHandleService service = new ProductHandleService();
					List<Product> pros = service.findProsByInId(inId);
					JSONArray jsonArray = new JSONArray();
					for (Product pro : pros) {
						JSONObject jo = new JSONObject();
						jo.put("contractid", pro.getContractId());
						jo.put("productid", pro.getProductId());
						jo.put("productcode", pro.getProductCode());
						jo.put("pmnm", pro.getPMNM());
						jo.put("productname", pro.getProductName());
						jo.put("producttype", pro.getProductType());
						jo.put("productmodel", pro.getProductModel());
						jo.put("productunit", pro.getProductUnit());
						jo.put("measureunit", pro.getMeasureUnit());
						jo.put("productprice", pro.getProductPrice());
						jo.put("deliverytime", MyDateFormat
								.changeDateToString(pro.getDeliveryTime()));
						jo.put("latestmaintain", MyDateFormat
								.changeDateToString(pro.getLastMainTainTime()));
						jo.put("manuf", pro.getManufacturer());
						jo.put("keeper", pro.getKeeper());
						jo.put("oldprice", pro.getProductPrice());
						jo.put("buyer", pro.getBuyer());
						jo.put("signtime", MyDateFormat.changeDateToString(pro
								.getSignTime()));
						jo.put("status", pro.getProStatus());
						jo.put("restkeep", pro.getRestKeepTime());
						jo.put("restmaintain", pro.getRestMaintainTime());
						jo.put("deviceNo", pro.getDeviceNo());
						jo.put("location", pro.getLocation());
						jo.put("produced", pro.getProducedDate());
						jo.put("storage", pro.getStorageTime());
						jo.put("borrowlength", pro.getBorrowLength());
						jo.put("reason", pro.getBorrowReason());
						jo.put("remark", pro.getRemark());
						jo.put("wholeName", pro.getWholeName());
						String maintainCycle=pro.getMaintainCycle();
						if("30d".equals(maintainCycle)){
							maintainCycle="一个月";
						}else if("180d".equals(maintainCycle)){
							maintainCycle="半年";
						}else if("365d".equals(maintainCycle)){
							maintainCycle="一年";
						}
						jo.put("maintainCycle", maintainCycle);
						jo.put("status", pro.getProStatus());
						jsonArray.add(jo);
					}
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write(jsonArray.toString());
					path = "";
				} else if ("searchByOutId".equals(operate)) {
					String outId = request.getParameter("outId");

					ProductHandleService service = new ProductHandleService();
					List<HashMap<String, String>> fdo = service.findOldPrice(outId);
					JSONArray jsonArray = new JSONArray();
					for (int i=0;i<fdo.size();i++) {
						JSONObject jo = new  JSONObject();
						//jo = JSONObject.fromObject(fdo.get(i));
						//jsonArray.add(jo);
						jo.put("contractid", fdo.get(i).get("contractId"));
						jo.put("productid", fdo.get(i).get("productId"));
						jo.put("productcode",fdo.get(i).get("productCode"));
						jo.put("pmnm", fdo.get(i).get("PMNM"));
						jo.put("productname", fdo.get(i).get("productName"));
						jo.put("producttype", fdo.get(i).get("productType"));
						jo.put("productmodel", fdo.get(i).get("productModel"));
						jo.put("productunit",fdo.get(i).get("productUnit"));
						jo.put("measureunit", fdo.get(i).get("measureUnit"));
						jo.put("productprice", fdo.get(i).get("productPrice"));
						jo.put("deliverytime", fdo.get(i).get("deliveryTime"));
						jo.put("latestmaintain", fdo.get(i).get("latestMaintainTime"));
						jo.put("manuf", fdo.get(i).get("manufacturer"));
						jo.put("keeper", fdo.get(i).get("keeper"));
						jo.put("buyer", fdo.get(i).get("buyer"));
						jo.put("signtime",fdo.get(i).get("signTime"));
						jo.put("status", fdo.get(i).get("proStatus"));
						jo.put("restkeep", fdo.get(i).get("restKeepTime"));
						jo.put("restmaintain", fdo.get(i).get("restMaintainTime"));
						jo.put("deviceNo", fdo.get(i).get("deviceNo"));
						jo.put("location", fdo.get(i).get("location"));
						jo.put("produced",fdo.get(i).get("producedDate"));
						jo.put("storage", fdo.get(i).get("storageTime"));
						jo.put("borrowlength", fdo.get(i).get("borrowLength"));
						jo.put("reason", fdo.get(i).get("borrowReason"));
						jo.put("remark",fdo.get(i).get("remark"));
						jo.put("wholeName",fdo.get(i).get("wholeName"));
						jo.put("maintainCycle", fdo.get(i).get("maintainCycle"));
						jo.put("oldprice", fdo.get(i).get("oldprice"));
						jsonArray.add(jo);
					}
					response.setContentType("text/plain;charset=UTF-8");
					System.out.println(jsonArray.toString());
					response.getWriter().write(jsonArray.toString());
					path = "";
				}else if("justForTranslate".equals(operate)){
					//这段代码的作用是：帮助前台页面跳转。和下面translate_get一起工作
					//具体是：在A页面跳转之前，发来数据data，存入session
					
					String data = request.getParameter("data");
//					String link = request.getParameter("link");
					//把数据放到session中
					request.getSession().setAttribute("data_translate", data);
//					if(link != null) path = link;
					return;
				}else if("translate_get".equals(operate)){
					//这段代码的作用是：帮助前台页面跳转。和上面justForTranslate一起工作
					//具体是：跳到B页面之后，从session中获取data，传给前台
					
					Object ob = request.getSession().getAttribute("data_translate");
					// addby lyt 取值后，把dataRemove掉
					request.getSession().removeAttribute("data_translate");
					//用JSON的形式向前台发送
					JSONObject jsonObject = JSONObject.fromObject(ob);
					PrintWriter writer = response.getWriter();
					writer.write(jsonObject.toString());
					writer.close();
					
					request.getSession().removeAttribute("data_translate");
					return;
				}
				if (!"".equals(path)) {
					request.getRequestDispatcher(path).forward(request,response);
				}
			}
		}
		if (version.equals("2")) {
			if ("searchByInId".equals(operate)) {
				String inId = request.getParameter("inId");

				ProductHandleService service = new ProductHandleService();
				List<Product> pros = service.findProsByInId(inId);
				JSONArray jsonArray = new JSONArray();
				for (Product pro : pros) {
					JSONObject jo = new JSONObject();
					jo.put("contractid", pro.getContractId());
					jo.put("productid", pro.getProductId());
					jo.put("productcode", pro.getProductCode());
					jo.put("pmnm", pro.getPMNM());
					jo.put("productname", pro.getProductName());
					jo.put("producttype", pro.getProductType());
					jo.put("productmodel", pro.getProductModel());
					jo.put("productunit", pro.getProductUnit());
					jo.put("measureunit", pro.getMeasureUnit());
					jo.put("productprice", pro.getProductPrice());
					jo.put("oldprice", pro.getProductPrice());
					jo.put("deliverytime", MyDateFormat.changeDateToString(pro
							.getDeliveryTime()));
					jo.put("latestmaintain", MyDateFormat
							.changeDateToString(pro.getLastMainTainTime()));
					jo.put("manuf", pro.getManufacturer());
					jo.put("keeper", pro.getKeeper());
					jo.put("buyer", pro.getBuyer());
					jo.put("signtime",
							MyDateFormat.changeDateToString(pro.getSignTime()));
					jo.put("status", pro.getProStatus());
					jo.put("restkeep", pro.getRestKeepTime());
					jo.put("restmaintain", pro.getRestMaintainTime());
					jo.put("deviceNo", pro.getDeviceNo());
					jo.put("location", pro.getLocation());
					jo.put("produced", pro.getProducedDate());
					jo.put("storage", pro.getStorageTime());
					jo.put("borrowlength", pro.getBorrowLength());
					jo.put("reason", pro.getBorrowReason());
					jo.put("remark", pro.getRemark());
					jo.put("wholeName", pro.getWholeName());
					String maintainCycle=pro.getMaintainCycle();
					if("30d".equals(maintainCycle)){
						maintainCycle="一个月";
					}else if("180d".equals(maintainCycle)){
						maintainCycle="半年";
					}else if("365d".equals(maintainCycle)){
						maintainCycle="一年";
					}
					jo.put("maintainCycle", maintainCycle);
					jo.put("status", pro.getProStatus());
					System.out.println(jo);
					jsonArray.add(jo);
				}

				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write(jsonArray.toString());
				path = "";

			} else if ("searchByOutId".equals(operate)) {
				String outId = request.getParameter("outId");

				ProductHandleService service = new ProductHandleService();
				List<HashMap<String, String>> fdo = service.findOldPrice(outId);
				JSONArray jsonArray = new JSONArray();
				for (int i=0;i<fdo.size();i++) {
					JSONObject jo = new  JSONObject();
					//jo = JSONObject.fromObject(fdo.get(i));
					//jsonArray.add(jo);
					jo.put("contractid", fdo.get(i).get("contractId"));
					jo.put("productid", fdo.get(i).get("productId"));
					jo.put("productcode",fdo.get(i).get("productCode"));
					jo.put("pmnm", fdo.get(i).get("PMNM"));
					jo.put("productname", fdo.get(i).get("productName"));
					jo.put("producttype", fdo.get(i).get("productType"));
					jo.put("productmodel", fdo.get(i).get("productModel"));
					jo.put("productunit",fdo.get(i).get("productUnit"));
					jo.put("measureunit", fdo.get(i).get("measureUnit"));
					jo.put("productprice", fdo.get(i).get("productPrice"));
					jo.put("deliverytime", fdo.get(i).get("deliveryTime"));
					jo.put("latestmaintain", fdo.get(i).get("latestMaintainTime"));
					jo.put("manuf", fdo.get(i).get("manufacturer"));
					jo.put("keeper", fdo.get(i).get("keeper"));
					jo.put("buyer", fdo.get(i).get("buyer"));
					jo.put("signtime",fdo.get(i).get("signTime"));
					jo.put("status", fdo.get(i).get("proStatus"));
					jo.put("restkeep", fdo.get(i).get("restKeepTime"));
					jo.put("restmaintain", fdo.get(i).get("restMaintainTime"));
					jo.put("deviceNo", fdo.get(i).get("deviceNo"));
					jo.put("location", fdo.get(i).get("location"));
					jo.put("produced",fdo.get(i).get("producedDate"));
					jo.put("storage", fdo.get(i).get("storageTime"));
					jo.put("borrowlength", fdo.get(i).get("borrowLength"));
					jo.put("reason", fdo.get(i).get("borrowReason"));
					jo.put("remark",fdo.get(i).get("remark"));
					jo.put("wholeName",fdo.get(i).get("wholeName"));
					jo.put("maintainCycle", fdo.get(i).get("maintainCycle"));
					jo.put("oldprice", fdo.get(i).get("oldprice"));
					jsonArray.add(jo);
				}
				response.setContentType("text/plain;charset=UTF-8");
				System.out.println(jsonArray.toString());
				response.getWriter().write(jsonArray.toString());
				path = "";
			}
		}
		if (version.equals("3")) {
			if ("searchByInId".equals(operate)) {
				String inId = request.getParameter("inId");

				ProductHandleService service = new ProductHandleService();
				List<Product> pros = service.findProsByInId(inId);
				JSONArray jsonArray = new JSONArray();
				for (Product pro : pros) {
					JSONObject jo = new JSONObject();
					jo.put("contractid", pro.getContractId());
					jo.put("productid", pro.getProductId());
					jo.put("productcode", pro.getProductCode());
					jo.put("pmnm", pro.getPMNM());
					jo.put("productname", pro.getProductName());
					jo.put("producttype", pro.getProductType());
					jo.put("productmodel", pro.getProductModel());
					jo.put("productunit", pro.getProductUnit());
					jo.put("oldprice", pro.getProductPrice());
					jo.put("measureunit", pro.getMeasureUnit());
					jo.put("productprice", pro.getProductPrice());
					jo.put("deliverytime", MyDateFormat.changeDateToString(pro
							.getDeliveryTime()));
					jo.put("latestmaintain", MyDateFormat
							.changeDateToString(pro.getLastMainTainTime()));
					jo.put("manuf", pro.getManufacturer());
					jo.put("keeper", pro.getKeeper());
					jo.put("buyer", pro.getBuyer());
					jo.put("signtime",
							MyDateFormat.changeDateToString(pro.getSignTime()));
					jo.put("status", pro.getProStatus());
					jo.put("restkeep", pro.getRestKeepTime());
					jo.put("restmaintain", pro.getRestMaintainTime());
					jo.put("deviceNo", pro.getDeviceNo());
					jo.put("location", pro.getLocation());
					jo.put("produced", pro.getProducedDate());
					jo.put("storage", pro.getStorageTime());
					jo.put("borrowlength", pro.getBorrowLength());
					jo.put("reason", pro.getBorrowReason());
					jo.put("remark", pro.getRemark());
					jo.put("wholeName", pro.getWholeName());
					String maintainCycle=pro.getMaintainCycle();
					if("30d".equals(maintainCycle)){
						maintainCycle="一个月";
					}else if("180d".equals(maintainCycle)){
						maintainCycle="半年";
					}else if("365d".equals(maintainCycle)){
						maintainCycle="一年";
					}
					jo.put("maintainCycle", maintainCycle);
					jo.put("status", pro.getProStatus());
					jsonArray.add(jo);
				}
				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write(jsonArray.toString());
				path = "";

			} else if ("searchByOutId".equals(operate)) {
				String outId = request.getParameter("outId");

				ProductHandleService service = new ProductHandleService();
				List<HashMap<String, String>> fdo = service.findOldPrice(outId);
				JSONArray jsonArray = new JSONArray();
				for (int i=0;i<fdo.size();i++) {
					JSONObject jo = new  JSONObject();
					//jo = JSONObject.fromObject(fdo.get(i));
					//jsonArray.add(jo);
					jo.put("contractid", fdo.get(i).get("contractId"));
					jo.put("productid", fdo.get(i).get("productId"));
					jo.put("productcode",fdo.get(i).get("productCode"));
					jo.put("pmnm", fdo.get(i).get("PMNM"));
					jo.put("productname", fdo.get(i).get("productName"));
					jo.put("producttype", fdo.get(i).get("productType"));
					jo.put("productmodel", fdo.get(i).get("productModel"));
					jo.put("productunit",fdo.get(i).get("productUnit"));
					jo.put("measureunit", fdo.get(i).get("measureUnit"));
					jo.put("productprice", fdo.get(i).get("productPrice"));
					jo.put("deliverytime", fdo.get(i).get("deliveryTime"));
					jo.put("latestmaintain", fdo.get(i).get("latestMaintainTime"));
					jo.put("manuf", fdo.get(i).get("manufacturer"));
					jo.put("keeper", fdo.get(i).get("keeper"));
					jo.put("buyer", fdo.get(i).get("buyer"));
					jo.put("signtime",fdo.get(i).get("signTime"));
					jo.put("status", fdo.get(i).get("proStatus"));
					jo.put("restkeep", fdo.get(i).get("restKeepTime"));
					jo.put("restmaintain", fdo.get(i).get("restMaintainTime"));
					jo.put("deviceNo", fdo.get(i).get("deviceNo"));
					jo.put("location", fdo.get(i).get("location"));
					jo.put("produced",fdo.get(i).get("producedDate"));
					jo.put("storage", fdo.get(i).get("storageTime"));
					jo.put("borrowlength", fdo.get(i).get("borrowLength"));
					jo.put("reason", fdo.get(i).get("borrowReason"));
					jo.put("remark",fdo.get(i).get("remark"));
					jo.put("wholeName",fdo.get(i).get("wholeName"));
					jo.put("maintainCycle", fdo.get(i).get("maintainCycle"));
					jo.put("oldprice", fdo.get(i).get("oprice"));
					jsonArray.add(jo);
				}
				response.setContentType("text/plain;charset=UTF-8");
				System.out.println(jsonArray.toString());
				response.getWriter().write(jsonArray.toString());
				path = "";
			}
		}
		if (version.equals("4")) {
			if ("searchByInId".equals(operate)) {
				String inId = request.getParameter("inId");

				ProductHandleService service = new ProductHandleService();
				List<Product> pros = service.findProsByInId(inId);
				JSONArray jsonArray = new JSONArray();
				for (Product pro : pros) {
					JSONObject jo = new JSONObject();
					jo.put("contractid", pro.getContractId());
					jo.put("productid", pro.getProductId());
					jo.put("productcode", pro.getProductCode());
					jo.put("pmnm", pro.getPMNM());
					jo.put("productname", pro.getProductName());
					jo.put("producttype", pro.getProductType());
					jo.put("productmodel", pro.getProductModel());
					jo.put("productunit", pro.getProductUnit());
					jo.put("oldprice", pro.getProductPrice());
					jo.put("measureunit", pro.getMeasureUnit());
					jo.put("productprice", pro.getProductPrice());
					jo.put("deliverytime", MyDateFormat.changeDateToString(pro
							.getDeliveryTime()));
					jo.put("latestmaintain", MyDateFormat
							.changeDateToString(pro.getLastMainTainTime()));
					jo.put("manuf", pro.getManufacturer());
					jo.put("keeper", pro.getKeeper());
					jo.put("buyer", pro.getBuyer());
					jo.put("signtime",
							MyDateFormat.changeDateToString(pro.getSignTime()));
					jo.put("status", pro.getProStatus());
					jo.put("restkeep", pro.getRestKeepTime());
					jo.put("restmaintain", pro.getRestMaintainTime());
					jo.put("deviceNo", pro.getDeviceNo());
					jo.put("location", pro.getLocation());
					jo.put("produced", pro.getProducedDate());
					jo.put("storage", pro.getStorageTime());
					jo.put("borrowlength", pro.getBorrowLength());
					jo.put("reason", pro.getBorrowReason());
					jo.put("remark", pro.getRemark());
					jo.put("wholeName", pro.getWholeName());
					String maintainCycle=pro.getMaintainCycle();
					if("30d".equals(maintainCycle)){
						maintainCycle="一个月";
					}else if("180d".equals(maintainCycle)){
						maintainCycle="半年";
					}else if("365d".equals(maintainCycle)){
						maintainCycle="一年";
					}
					jo.put("maintainCycle", maintainCycle);
					jo.put("status", pro.getProStatus());
					jsonArray.add(jo);
				}
				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write(jsonArray.toString());
				path = "";

			} else if ("searchByOutId".equals(operate)) {
				String outId = request.getParameter("outId");

				ProductHandleService service = new ProductHandleService();
				List<HashMap<String, String>> fdo = service.findOldPrice(outId);
				JSONArray jsonArray = new JSONArray();
				for (int i=0;i<fdo.size();i++) {
					JSONObject jo = new  JSONObject();
					//jo = JSONObject.fromObject(fdo.get(i));
					//jsonArray.add(jo);
					jo.put("contractid", fdo.get(i).get("contractId"));
					jo.put("productid", fdo.get(i).get("productId"));
					jo.put("productcode",fdo.get(i).get("productCode"));
					jo.put("pmnm", fdo.get(i).get("PMNM"));
					jo.put("productname", fdo.get(i).get("productName"));
					jo.put("producttype", fdo.get(i).get("productType"));
					jo.put("productmodel", fdo.get(i).get("productModel"));
					jo.put("productunit",fdo.get(i).get("productUnit"));
					jo.put("measureunit", fdo.get(i).get("measureUnit"));
					jo.put("productprice", fdo.get(i).get("productPrice"));
					jo.put("deliverytime", fdo.get(i).get("deliveryTime"));
					jo.put("latestmaintain", fdo.get(i).get("latestMaintainTime"));
					jo.put("manuf", fdo.get(i).get("manufacturer"));
					jo.put("keeper", fdo.get(i).get("keeper"));
					jo.put("buyer", fdo.get(i).get("buyer"));
					jo.put("signtime",fdo.get(i).get("signTime"));
					jo.put("status", fdo.get(i).get("proStatus"));
					jo.put("restkeep", fdo.get(i).get("restKeepTime"));
					jo.put("restmaintain", fdo.get(i).get("restMaintainTime"));
					jo.put("deviceNo", fdo.get(i).get("deviceNo"));
					jo.put("location", fdo.get(i).get("location"));
					jo.put("produced",fdo.get(i).get("producedDate"));
					jo.put("storage", fdo.get(i).get("storageTime"));
					jo.put("borrowlength", fdo.get(i).get("borrowLength"));
					jo.put("reason", fdo.get(i).get("borrowReason"));
					jo.put("remark",fdo.get(i).get("remark"));
					jo.put("wholeName",fdo.get(i).get("wholeName"));
					jo.put("maintainCycle", fdo.get(i).get("maintainCycle"));
					jo.put("oldprice", fdo.get(i).get("oprice"));
					jsonArray.add(jo);
				}
				response.setContentType("text/plain;charset=UTF-8");
				System.out.println(jsonArray.toString());
				response.getWriter().write(jsonArray.toString());
				path = "";
			}
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
