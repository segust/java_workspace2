package cn.edu.cqupt.controller.transact_business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.transact_business.AddBatchService;
import cn.edu.cqupt.service.transact_business.AddInApplyService;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;

public class AddInApplyServlet extends HttpServlet {
	
	private AddInApplyService service;
	private ProductHandleService pService = new ProductHandleService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		String operate = request.getParameter("operate");
		String version = (String) this.getServletContext().getInitParameter("version");
		if("1".equals(version)) {
			if(CurrentUser.isContractManage(request)) {
				if(operate.equals("inApply"))
				{
					HttpSession session = request.getSession();
					String ownedUnit = session.getAttribute("ownedUnit") + "";
					String jsonStr = request.getParameter("data");
					List<HashMap<String, Object>> in = new ArrayList<HashMap<String, Object>>();//要往后台传的数据都在这个数据结构里
					System.out.println(jsonStr);
					JSONArray jsonArray = JSONArray.fromObject(jsonStr);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jo = jsonArray.getJSONObject(i);
						HashMap<String, Object> piece = new HashMap<String, Object>();
						piece.put("deviceNo", jo.get("dNo").toString().trim());
						piece.put("contractId", jo.get("contractid") + "");
						piece.put("inMeans", jo.get("means") + "");
						piece.put("batch", jo.get("batch") + "");
						piece.put("ownedUnit", ownedUnit);
						piece.put("location", jo.get("location") + "");
						piece.put("producedDate", jo.get("makeTime") + "");//本来应该是makeTime，前台没有传过来
						if(jo.get("storagetime").toString().endsWith("年"))
							piece.put("storageTime", jo.get("storagetime"));
						else
							piece.put("storageTime", jo.get("storagetime").toString().trim() + "年");
						piece.put("remark", jo.get("remark") + "");
						piece.put("wholeName", jo.get("wholename").toString().trim());
						piece.put("maintainCycle", jo.get("maintain") + "");
						piece.put("proStatus", "进库待审核");
						piece.put("productCode", jo.get("productcode") + "");
						piece.put("productModel", jo.get("productmodel") + "");
						in.add(piece);
					}
					
					AddBatchService addBatchService = new AddBatchService();
					
					boolean flag = addBatchService.storeBatch(in);
					//添加日志
					this.saveLog(request, response, "新入库申请", 0, "入库批次："
							+ in.get(0).get("batch"));
//					request.setAttribute("contractid", request.getParameter("contractId"));
//					RequestDispatcher disp = context.getRequestDispatcher("/jsp/qy/transact_business/singleApply.jsp");
//					disp.forward(request, response);
					response.setContentType("text/plain,charset=UTF-8");
					if(flag)
						response.getWriter().write("1");
					else
						response.getWriter().write("0");
				}
				else if("goToInApply".equals(operate)){
					String status = "未申请";
					String contractId = request.getParameter("contractid");
					List<String> dNos = new ProductHandleService().findDeviceByContract(contractId);
					request.setAttribute("contractId", contractId);
					request.setAttribute("dNos", dNos);
//					String path ="/jsp/qy/transact_business/singleApply.jsp";
					String path = queryProduct(request, response,status,"singleApply");
					request.getRequestDispatcher(path).forward(request, response);
				}else if("getNextPage".equals(operate)) {
					String status = "未申请";
					int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
					List<HashMap<String, String>> products = this.queryProductsByContractId(request,response,curPageNum,10,status);
					JSONArray jarray = new JSONArray();
					for(int i=0;i<products.size();i++) {
						JSONArray temp = new JSONArray();
						int j=0;
						temp.add(j++, products.get(i).get("productId"));
						temp.add(j++, products.get(i).get("contractid"));
						temp.add(j++, products.get(i).get("productModel"));
						temp.add(j++, products.get(i).get("deviceNo"));
						temp.add(j++, products.get(i).get("productName"));
						temp.add(j++, products.get(i).get("productUnit"));
						temp.add(j++, products.get(i).get("PMNM"));
						temp.add(j++, products.get(i).get("productPrice"));
						temp.add(j++, products.get(i).get("measureUnit"));
						temp.add(j++, /*products.get(i).get("count")*/1);
						temp.add(j++, products.get(i).get("deliveryTime"));
						temp.add(j++, products.get(i).get("manufacturer"));
						temp.add(j++, products.get(i).get("keeper"));
						temp.add(j++, products.get(i).get("proStatus"));
						jarray.add(temp);
					}
					int sum = this.querySumByContractId(request,response,status);
					sum = sum%10==0?sum/10:(sum/10+1);
					Map<String,Object> returnData = new HashMap<String, Object>();
					returnData.put("totalPage", sum);
//					returnData.put("nextPage", nextPage);
					returnData.put("items", jarray);
					returnData.put("nowPage", curPageNum);
					JSONObject returnJo = JSONObject.fromObject(returnData);
					System.out.println("returnData:"+returnJo.toString());
					response.setContentType("text/plain,charset=utf-8");
					response.getWriter().write(returnJo.toString());
				}else if("updatePro".equals(operate)) {
					boolean flag = false;
					Product pro = new Product();
					String jsonStr = request.getParameter("data");
					JSONArray jarray = JSONArray.fromObject(jsonStr);
					//System.out.println("今天是1月6日"+jarray.toString());
					pro.setProductId(Long.parseLong(jarray.getString(0)));
					pro.setProductModel(jarray.getString(2));
					pro.setProductName(jarray.getString(4));
					pro.setProductUnit(jarray.getString(5));
					pro.setPMNM(jarray.getString(6));
					pro.setProductPrice(jarray.getString(7));
					pro.setMeasureUnit(jarray.getString(8));
					pro.setDeliveryTime(MyDateFormat.changeStringToDate(jarray.getString(10)));
					pro.setManufacturer(jarray.getString(12));
					pro.setKeeper(jarray.getString(13));
					flag = pService.updateProduct(pro);
					//添加日志
					this.saveLog(request, response, "修改", pro.getProductId(), "修改产品信息");
					response.setContentType("text/plain,charset=UTF-8");
					if(flag)
						response.getWriter().write("修改成功！");
					else
						response.getWriter().write("修改失败！");
				}else if("deletePro".equals(operate)) {
					boolean flag = false;
					String productId = request.getParameter("productId");
					if(productId != null && !"".equals(productId)) {
						flag = pService.DeleteProductById(Long.parseLong(productId));
						//添加日志
						this.saveLog(request, response, "删除", Long.parseLong(productId), "删除产品信息");
					}
					response.setContentType("text/plain,charset=UTF-8");
					if(flag)
						response.getWriter().write("1");
					else
						response.getWriter().write("0");
				}
				else{
					System.out.println("error");
				}
			}
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
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
	
	private List<HashMap<String, String>> queryProductsByContractId(HttpServletRequest request,
			HttpServletResponse response,int curPageNum,int pageSize,String status) {
		List<HashMap<String, String>> products = new ArrayList<HashMap<String,String>>();
		String contractId = request.getParameter("contractid");
		products = pService.queryProductDetailByContractId(contractId,curPageNum,pageSize,status);
		return products;
	}
	
	private int querySumByContractId(HttpServletRequest request,
			HttpServletResponse response, String status) {
		int sum = 0;
		String contractId = request.getParameter("contractid");
		sum = pService.getDetailSumByContractId(contractId,status);
		return sum;
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
	
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException
	{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		this.doGet(req, resp);
	}
}
