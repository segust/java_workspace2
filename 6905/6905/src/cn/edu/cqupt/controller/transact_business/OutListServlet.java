package cn.edu.cqupt.controller.transact_business;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.service.transact_business.OutListHandleService;

public class OutListServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public OutListServlet() {
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
	private String curFolder;
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		if(version.equals("1")){
			curFolder="qy";
			try {
				forwardByOperation(request, response, version);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(version.equals("2")){
			curFolder="jds";
			try {
				forwardByOperation(request, response, version);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(version.equals("3")){
			curFolder="jdj";
			try {
				forwardByOperation(request, response, version);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(version.equals("4")){
			curFolder="zhj";
			try {
				forwardByOperation(request, response, version);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void forwardByOperation(HttpServletRequest request, HttpServletResponse response,String version) throws SQLException, ServletException, IOException{
		String operate=request.getParameter("operate");
		String path=new String();
		
		if(operate.equals("selectOutListAllot")){
			//问题1：path中是否要curPageNum和pagesize
			//问题2：返回的具体页面
			path="/jsp/"+curFolder+"/transact_business/";
			selectOutList(request,response,path,operate);
		}else if(operate.equals("selectOutListTurn")){
			path="/jsp/"+curFolder+"/transact_business/";
			selectOutList(request,response,path,operate);
		}else if(operate.equals("selectOutListUpdate")){
			path="/jsp/"+curFolder+"/transact_business/";
			selectOutList(request,response,path,operate);
		}else if(operate.equals("selectDeviceNoAndUpdateProStatus")){
			selectDeviceNoAndUpdateProStatus(request,response);
		} else if("selectDetail".equals(operate)) {
			path="/jsp/"+curFolder+"/transact_business/";
			selectOutListDetail(request,response,path);
		}
	}
	
	public void selectOutListDetail(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
		//判断是调拨|轮换|更新
		String type = request.getParameter("type");
		String listId = request.getParameter("listId");
		int curPageNum = 0;
		int pageSize = 0;		
		if(request.getParameter("curPageNum") == null ||request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		}else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum").trim());
			pageSize = Integer.parseInt(request.getParameter("pageSize").trim());
		}
		path+="outListQueryDetail.jsp?curPageNum="+curPageNum+"&pageSize="+pageSize;
		OutListHandleService outListHandleService = new OutListHandleService();
		ArrayList<HashMap<String, String>> resultList = outListHandleService.selectOutListDetail(type,listId,curPageNum,pageSize);
		int sum = outListHandleService.getOutListProNum(type,listId);
		request.setAttribute("Detailsum", sum);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("message", resultList);
		request.setAttribute("listId", listId);
		request.getRequestDispatcher(path).forward(request, response);
		
	}
	
	public void selectOutList(HttpServletRequest request, HttpServletResponse response, String path, String operate) throws ServletException, IOException{
		int curPageNum = 0;
		int pageSize = 0;		
		if(request.getParameter("curPageNum") == null ||request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		}else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum").trim());
			pageSize = Integer.parseInt(request.getParameter("pageSize").trim());
		}
		
		String type = new String();
		if(operate.equals("selectOutListAllot")){
			path +="outListQuery.jsp";
			type = "allot";
		}else if(operate.equals("selectOutListTurn")){
			path +="turnOutListQuery.jsp";
			type = "turn";
		}else if(operate.equals("selectOutListUpdate")){
			path +="updateOutListQuery.jsp";
			type = "update";
		}
		path+="?curPageNum="+curPageNum+"&pageSize="+pageSize;
		
		OutListHandleService outListHandleService = new OutListHandleService();
		ArrayList<HashMap<String, String>> resultList = outListHandleService.selectOutList(type,curPageNum,pageSize);
		int sum = outListHandleService.getOutListNum(type);
		
		request.setAttribute("Detailsum", sum);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("message", resultList);
		request.getRequestDispatcher(path).forward(request, response);
	}
	
	public void selectDeviceNoAndUpdateProStatus(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String PMNM = request.getParameter("PMNM");
		String productModel = request.getParameter("productModel");
		String listId = request.getParameter("listId");
		//String ownedUnit = request.getParameter("ownedUnit");
		// add by lyt 1107
		String type = request.getParameter("outType");
		
		//用户输入的数量
		String count = request.getParameter("count");
		
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("PMNM", PMNM);
		condition.put("productModel", productModel);
		condition.put("listId", listId);
		// change by lyt 1107
		//condition.put("ownedUnit", ownedUnit);
		//end change
		
		condition.put("count", count);
		condition.put("outType", type);
		
		OutListHandleService outListHandleService = new OutListHandleService();
		//ArrayList<Product> productList = outListHandleService.selectDeviceNo(condition);
		//changed by lyt 1107
		List<Map<String,String>> productList= outListHandleService.updateProStatus(condition);
		
		/*if(flag){
			JSONArray jarray = new JSONArray();
			for (Product product : productList) {
				JSONObject jo = new JSONObject();
				jo.put("productModel",product.getProductModel());
				jo.put("deviceNo", product.getDeviceNo());
				jo.put("location", product.getLocation());
				jarray.add(jo);
			}*/
			JSONArray jarray = new JSONArray();
			if(productList != null) {
				if("true".equals(productList.get(productList.size()-1).get("runStatus"))) {
					for (int i = 0; i < productList.size()-1; i++) {
						JSONObject jo = JSONObject.fromObject(productList.get(i));
						jarray.add(jo);
					}
				}
			}
			response.getWriter().write(jarray.toString());
	}
}
