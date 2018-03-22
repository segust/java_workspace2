package cn.edu.cqupt.controller.query_business;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;
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
import net.sf.json.JSONObject;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.service.query_business.ProductCollectiveService;
import cn.edu.cqupt.service.query_business.ProductDetailService;
import cn.edu.cqupt.service.sys_management.OutDataService;
import cn.edu.cqupt.service.transact_business.ApplyFormOperation;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class ProductQueryServlet extends HttpServlet {
	// 因为是单机用户，所以可以把申明放到这里来
	private ApplyFormOperation applyFormOperationService = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ProductQueryServlet() {
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

		int curPageNum = 0;
		int pageSize = 0;
		if (request.getParameter("curPageNum") == null
				|| request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
			pageSize = Integer
					.parseInt(request.getParameter("pageSize").trim());
		}

		if (version.equals("1")) {
			curFolder = "qy";
			try {
				forwardByOperation(request, response, version, curPageNum,
						pageSize);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (version.equals("2")) {
			curFolder = "jds";
			try {
				forwardByOperation(request, response, version, curPageNum,
						pageSize);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (version.equals("3")) {
			curFolder = "jdj";
			try {
				forwardByOperation(request, response, version, curPageNum,
						pageSize);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (version.equals("4")) {
			curFolder = "zhj";
			try {
				forwardByOperation(request, response, version, curPageNum,
						pageSize);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void forwardByOperation(HttpServletRequest request,
			HttpServletResponse response, String version, int curPageNum,
			int pageSize) throws SQLException, ServletException, IOException {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();// 返回值
		String operate = request.getParameter("operate");
		String path = new String();

		if (operate.equals("productDetailQuery")) {
			T = productDetailQuery(request, response, version);
			path = "/jsp/" + curFolder
					+ "/query_business/product_detail.jsp?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
		} else if (operate.equals("productCollectiveQuery")) {
			T = producCollectiveQuery(request, response,version);
			path = "/jsp/" + curFolder
					+ "/query_business/product_collective.jsp?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
		} else if (operate.equals("exportSingleForm")) {
			OutProduct(request, response, version);
			return;
		} else if (operate.equals("download")) {
			DownloadFile(request, response);
			return;
		} else if (operate.equals("LoadIn")) {
			loadIn(request, response);
			String pagename = request.getParameter("pagename");
			if (pagename.equals("collective")) {
				T = producCollectiveQuery(request, response,version);
				path = "/jsp/" + curFolder
						+ "/query_business/product_collective.jsp?curPageNum="
						+ curPageNum + "&pageSize=" + pageSize;
			} else if (pagename.equals("detail")) {
				T = productDetailQuery(request, response, version);
				path = "/jsp/" + curFolder
						+ "/query_business/product_detail.jsp?curPageNum="
						+ curPageNum + "&pageSize=" + pageSize;
			}
		} else if (operate.equals("inoutInfo")) {
			T = inapplyInfo(request, response);
			path = "/jsp/" + curFolder
					+ "/query_business/Showrecord.jsp?curPageNum=" + curPageNum
					+ "&pageSize=" + pageSize;
		} else if (operate.equals("borrowApply")) {
			T = borrowApply(request, response, version);
			path = "/jsp/" + curFolder
					+ "/transact_business/borrowApply.jsp?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
			//专门针对轮换出库
		} else if("getBorrowNextPage".equals(operate)) {
			//刘晏驰改 1116
			List<HashMap<String, Object>> products = this.borrowApply(request, response, version);
			JSONArray jarray = new JSONArray();
			int sum= products.size();
			sum = sum%10==0?sum/10:sum/10+1;
			Map<String,Integer> inNum = new ProductHandleService().getEachProductModelNum();
			String curNum = request.getParameter("curPageNum");
			if("".equals(curNum) && curNum == null) { curNum = "1";}
			for(int i=0;i<products.size();i++) {
				JSONArray temp = new JSONArray();
				int j=0;
				temp.add(j++, products.get(i).get("productId"));
				temp.add(j++, (Integer.parseInt(curNum)-1)*10+i+1);
				temp.add(j++, products.get(i).get("productModel"));
				temp.add(j++, products.get(i).get("productUnit"));
				temp.add(j++, products.get(i).get("batch"));
				temp.add(j++, products.get(i).get("deviceNo"));
				temp.add(j++, products.get(i).get("productPrice"));
				temp.add(j++, products.get(i).get("num"));
				temp.add(j++, MyDateFormat.changeDateToString((java.util.Date)products.get(i).get("execDate")));
				temp.add(j++, products.get(i).get("restMaintainTime"));
				temp.add(j++, products.get(i).get("restKeepTime"));
				temp.add(j++, products.get(i).get("Means"));
				temp.add(j++, products.get(i).get("storageTime"));
				temp.add(j++, products.get(i).get("nextMaintainTime"));
				temp.add(j++, products.get(i).get("manufacturer"));
				temp.add(j++, products.get(i).get("keeper"));
				temp.add(j++, products.get(i).get("remark"));
				jarray.add(temp);
			}
			Map<String,Object> returnData = new HashMap<String, Object>();
			returnData.put("totalPage", sum);
			returnData.put("items", jarray);
			returnData.put("nowPage", curNum);
			//用于拼接在库型号产品数量
			JSONArray inNumArray = new JSONArray();
			for (String key : inNum.keySet()) {
				JSONObject jo = new JSONObject();
				jo.put("model", key);
				jo.put("totalNum", inNum.get(key));
				inNumArray.add(jo);
			}
			returnData.put("numByModel", inNumArray);
			JSONObject returnJo = JSONObject.fromObject(returnData);
			response.getWriter().write(returnJo.toString());
			return;
		}else if("getUpdateNextPage".equals(operate)) {
			//刘晏驰改 1116
			List<HashMap<String, Object>> products = this.updateApply(request, response, version);
			Map<String,Integer> inNum = new ProductHandleService().getEachProductModelNum();
			JSONArray jarray = new JSONArray();
			int sum= products.size();
			sum = sum%10==0?sum/10:sum/10+1;
			String curNum = request.getParameter("curPageNum");
			if("".equals(curNum) && curNum == null) { curNum = "1";}
			for(int i=0;i<products.size();i++) {
				JSONArray temp = new JSONArray();
				int j=0;
				temp.add(j++, products.get(i).get("productId"));
				temp.add(j++, (Integer.parseInt(curNum)-1)*10+i+1);
				temp.add(j++, products.get(i).get("productModel"));
				temp.add(j++, products.get(i).get("productUnit"));
				temp.add(j++, products.get(i).get("batch"));
				temp.add(j++, products.get(i).get("deviceNo"));
				temp.add(j++, products.get(i).get("productPrice"));
				temp.add(j++, products.get(i).get("num"));
				temp.add(j++, MyDateFormat.changeDateToString((java.util.Date)products.get(i).get("execDate")));
				temp.add(j++, products.get(i).get("restMaintainTime"));
				temp.add(j++, products.get(i).get("restKeepTime"));
				temp.add(j++, products.get(i).get("Means"));
				temp.add(j++, products.get(i).get("storageTime"));
				temp.add(j++, products.get(i).get("nextMaintainTime"));
				temp.add(j++, products.get(i).get("manufacturer"));
				temp.add(j++, products.get(i).get("keeper"));
				temp.add(j++, products.get(i).get("remark"));
				jarray.add(temp);
			}
			Map<String,Object> returnData = new HashMap<String, Object>();
			returnData.put("totalPage", sum);
			returnData.put("items", jarray);
			returnData.put("nowPage", curNum);
			//用于拼接在库型号产品数量  lhs添加 16-1-18
			JSONArray inNumArray = new JSONArray();
			for (String key : inNum.keySet()) {
				JSONObject jo = new JSONObject();
				jo.put("model", key);
				jo.put("totalNum", inNum.get(key));
				inNumArray.add(jo);
			}
			returnData.put("numByModel", inNumArray);
			JSONObject returnJo = JSONObject.fromObject(returnData);
			response.getWriter().write(returnJo.toString());
			return;
		} else if (operate.equals("updateApply")) {
			T = updateApply(request, response, version);
			path = "/jsp/" + curFolder
					+ "/transact_business/updateApply.jsp?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
		} else if (operate.equals("addOutList")) {
			T = outList(request, response, version);
			path = "/jsp/" + curFolder
					+ "/transact_business/addOutList.jsp?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
		} else if (operate.equals("addBorrowOutList")) {
			T = outList(request, response, version);
			path = "/jsp/" + curFolder
					+ "/transact_business/addBorrowOutList.jsp?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
		} else if (operate.equals("addUpdateOutList")) {
			T = outList(request, response, version);
			path = "/jsp/" + curFolder
					+ "/transact_business/addUpdateOutList.jsp?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
		} else if(operate.equals("delectCollective")){
			String data = request.getParameter("data");
			JSONObject jo = JSONObject.fromObject(data);
			ProductCollectiveService proColSer = new ProductCollectiveService();
			boolean runStatus = proColSer.delectCollective(jo);
			int i= 0;
			if(runStatus){
				i = 0;
			}else{
				i = -1;
			}
			PrintWriter writer = null;
			try{
				writer = response.getWriter();
				writer.write(i);
			}finally{
				writer.close();
			}
		}

		request.setAttribute("message", T);
		request.getRequestDispatcher(path).forward(request, response);

		// for (int i = 0; i < T.size(); i++) {
		// System.out.print(T.get(i).get("productType")+" ");
		// System.out.print(T.get(i).get("productUnit")+" ");
		// System.out.print(T.get(i).get("productPrice")+" ");
		// System.out.print(T.get(i).get("restKeepTime")+" ");
		// System.out.print(T.get(i).get("batch")+" ");
		// System.out.print(T.get(i).get("deviceNo")+" ");
		// System.out.print(T.get(i).get("inMeans")+" ");
		// System.out.print(T.get(i).get("num")+" ");
		// System.out.print(T.get(i).get("execDate")+" ");
		// System.out.print(T.get(i).get("storageTime")+" ");
		// System.out.print(T.get(i).get("manufacturer")+" ");
		// System.out.print(T.get(i).get("keeper")+" ");
		// System.out.print(T.get(i).get("remark")+" ");
		// System.out.print(T.get(i).get("nextMaintainTime")+" ");
		// }
	}

	/**
	 * 产品明细查询
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 * */
	public List<HashMap<String, Object>> productDetailQuery(
			HttpServletRequest request, HttpServletResponse response,
			String version) throws SQLException, ServletException, IOException {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();// 返回值
		HashMap<String, String> condition = new HashMap<String, String>();// 条件
		ProductDetailService productDetailService = new ProductDetailService();
		String flag = "a";
		String ownedUnit = "";
		if(version.equals("2")){
			ownedUnit = request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			if(!"all".equals(ownedUnit))
			condition.put("ownedUnit", ownedUnit);
		}else if(version.equals("3")){
			ownedUnit = request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			if(!"all".equals(ownedUnit))
				condition.put("JDS", ownedUnit);
		}
		String productModel = request.getParameter("productModel")==null?"/*/*":"/*"+request.getParameter("productModel")+"/*";
		String productUnit = request.getParameter("productUnit")==null?"/*/*":"/*"+request.getParameter("productUnit")+"/*";
		String manufacturer = request.getParameter("manufacturer")==null?"/*/*":"/*"+request.getParameter("manufacturer").trim()+"/*";
		String Means = request.getParameter("Means")==null?"/*/*":request.getParameter("Means");
		String restKeepTime = request.getParameter("restKeepTime")==null?"/*/*":request.getParameter("restKeepTime");
		String deviceNo = request.getParameter("deviceNo")==null?"/*/*":"/*"+request.getParameter("deviceNo")+"/*";
		String type = request.getParameter("type")==null?"/*/*":"/*"+request.getParameter("type")+"/*";
		String restMaintainTime = request.getParameter("restMaintainTime")==null?"/*/*":request.getParameter("restMaintainTime").trim();

//		System.out.println(productModel);
//		System.out.println(productUnit);
//		System.out.println(restKeepTime);
//		System.out.println(restMaintainTime);
//		System.out.println(manufacturer);
//		System.out.println(Means);
//		System.out.println(deviceNo);
//		System.out.println(type);
		condition.put("productModel", productModel);
		condition.put("productUnit", productUnit);
		condition.put("restKeepTime", restKeepTime);
		condition.put("restMaintainTime", restMaintainTime);
		condition.put("manufacturer", manufacturer);
		condition.put("Means", Means);
		condition.put("deviceNo", deviceNo);

		int curPageNum = request.getParameter("curPageNum") == null?1:Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = request.getParameter("pageSize") == null?10:Integer.parseInt(request.getParameter("pageSize").trim());
		
		try {
			productDetailService = new ProductDetailService();
			T = productDetailService.selectProductDetail(condition, flag, version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int count = T.size();
		double price = 0;
		double totalPrice = 0;

		List<HashMap<String, Object>> T_jsp = new ArrayList<HashMap<String, Object>>();// 返回值
		if (T.size() > 0) {
			for (int i = (curPageNum - 1) * pageSize; i < (pageSize * curPageNum); i++) {
				if (i < T.size()) {
					T_jsp.add(T.get(i));
					// 当前页金额
					price += Double.parseDouble(T.get(i).get("productPrice")
							+ "");
				} else {
					break;
				}
			}
			// 总金额
			for (int i = 0; i < T.size(); i++) {
				totalPrice += Double.parseDouble(T.get(i).get("productPrice")
						+ "");
			}
		}

		/*System.out.println("cur:" + curPageNum);
		System.out.println("sum:" + count);*/
		// System.out.println("price:"+price);
		// System.out.println("totalPrice:"+totalPrice);

		productModel=productModel.equals("/*/*")?"":productModel;
		productUnit=productUnit.equals("/*/*")?"":productUnit;
		restKeepTime=restKeepTime.equals("/*/*")?"":restKeepTime;
		restMaintainTime=restMaintainTime.equals("/*/*")?"":restMaintainTime;
		manufacturer=manufacturer.equals("/*/*")?"":manufacturer;
		Means=Means.equals("/*/*")?"":Means;
		deviceNo=deviceNo.equals("/*/*")?"":deviceNo;
		type=type.equals("/*/*")?"":type;
		String keeper=ownedUnit;
		request.setAttribute("price", price);
		request.setAttribute("totalPrice", totalPrice);
		request.setAttribute("Detailsum", count);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("productModel", productModel);
		request.setAttribute("productUnit", productUnit);
		request.setAttribute("restKeepTime", restKeepTime);
		request.setAttribute("restMaintainTime", restMaintainTime);
		request.setAttribute("manufacturer", manufacturer);
		request.setAttribute("Means", Means);
		request.setAttribute("deviceNo", deviceNo);
		request.setAttribute("keeper", keeper);
		request.setAttribute("type", type);
		
		return T_jsp;
	}

	/**
	 * 产品明细查询
	 * 
	 * @throws SQLException
	 * */
	public List<HashMap<String, Object>> borrowApply(
			HttpServletRequest request, HttpServletResponse response,
			String version) throws SQLException {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();// 返回值
		HashMap<String, String> condition = new HashMap<String, String>();// 条件
		ProductDetailService productDetailService = new ProductDetailService();
		String flag = "b";
		if(version.equals("2")){
			String jds = (String)request.getSession().getAttribute("ownedUnit");
			List<String> companys = new InfoService().getCompanyNameList(jds, 2);
			request.setAttribute("companys", companys);
		}else if(version.equals("3")){
			String jdj = (String)request.getSession().getAttribute("ownedUnit");
			List<String> JDS = new InfoService().getJDSNameList(jdj);
			request.setAttribute("JDSes", JDS);
		}
		String productModel = request.getParameter("productModel");
		String productUnit = request.getParameter("productUnit");
		String manufacturer = request.getParameter("manufacturer");
		String Means = request.getParameter("Means");
		String restKeepTime = request.getParameter("restKeepTime");
		String restMaintainTime = request.getParameter("restMaintainTime");
		String deviceNo = request.getParameter("deviceNo");
		String type = request.getParameter("type");
		String keeper = request.getParameter("keeper");
		if (restMaintainTime != null && restMaintainTime.trim().equals(""))
			restMaintainTime = "";
		if (manufacturer != null && manufacturer.trim().equals(""))
			manufacturer = "";

		condition.put("productModel", productModel);
		condition.put("productUnit", productUnit);
		condition.put("restKeepTime", restKeepTime);
		condition.put("restMaintainTime", restMaintainTime);
		condition.put("manufacturer", manufacturer);
		condition.put("Means", Means);
		condition.put("deviceNo", deviceNo);
		condition.put("keeper", keeper);

		int curPageNum = 0;
		int pageSize = 0;
		if (request.getParameter("curPageNum") == null
				|| request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum")
					.trim());
			pageSize = Integer
					.parseInt(request.getParameter("pageSize").trim());
		}

		try {
			productDetailService = new ProductDetailService();
			T = productDetailService.selectProductDetail(condition, flag, version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int count = T.size();
//		System.out.println("cur:" + curPageNum);

		List<HashMap<String, Object>> T_jsp = new ArrayList<HashMap<String, Object>>();// 返回值
		if (T.size() > 0) {
			for (int i = (curPageNum - 1) * pageSize; i < (pageSize * curPageNum); i++) {
				if (i < T.size()) {
					T_jsp.add(T.get(i));
				} else {
					break;
				}
			}
		}

		request.setAttribute("Detailsum", count);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("productModel", productModel);
		request.setAttribute("productUnit", productUnit);
		request.setAttribute("restKeepTime", restKeepTime);
		request.setAttribute("restMaintainTime", restMaintainTime);
		request.setAttribute("manufacturer", manufacturer);
		request.setAttribute("Means", Means);
		request.setAttribute("deviceNo", deviceNo);
		request.setAttribute("type", type);
		return T_jsp;
	}

	/**
	 * 产品明细查询
	 * 
	 * @throws SQLException
	 * */
	public List<HashMap<String, Object>> updateApply(
			HttpServletRequest request, HttpServletResponse response,
			String version) throws SQLException {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();// 返回值
		HashMap<String, String> condition = new HashMap<String, String>();// 条件
		ProductDetailService productDetailService = new ProductDetailService();
		String flag = "b";
		if(version.equals("2")){
			String jds = (String)request.getSession().getAttribute("ownedUnit");
			List<String> companys = new InfoService().getCompanyNameList(jds, 2);
			request.setAttribute("companys", companys);
		}else if(version.equals("3")){
			String jdj = (String)request.getSession().getAttribute("ownedUnit");
			List<String> JDS = new InfoService().getJDSNameList(jdj);
			request.setAttribute("JDSes", JDS);
		}
		String productModel = request.getParameter("productModel");
		String productUnit = request.getParameter("productUnit");
		String manufacturer = request.getParameter("manufacturer");
		String Means = request.getParameter("Means");
		String restKeepTime = request.getParameter("restKeepTime");
		String restMaintainTime = request.getParameter("restMaintainTime");
		String deviceNo = request.getParameter("deviceNo");
		String type = request.getParameter("type");
		String keeper = request.getParameter("keeper");
		if (restMaintainTime != null && restMaintainTime.trim().equals(""))
			restMaintainTime = "";
		if (manufacturer != null && manufacturer.trim().equals(""))
			manufacturer = "";

		condition.put("productModel", productModel);
		condition.put("productUnit", productUnit);
		condition.put("restKeepTime", restKeepTime);
		condition.put("restMaintainTime", restMaintainTime);
		condition.put("manufacturer", manufacturer);
		condition.put("Means", Means);
		condition.put("deviceNo", deviceNo);
		condition.put("keeper", keeper);

		int curPageNum = 0;
		int pageSize = 0;
		if (request.getParameter("curPageNum") == null
				|| request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum")
					.trim());
			pageSize = Integer
					.parseInt(request.getParameter("pageSize").trim());
		}

		try {
			productDetailService = new ProductDetailService();
			T = productDetailService.selectProductDetail(condition, flag, version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int count = T.size();
//		System.out.println("cur:" + curPageNum);

		List<HashMap<String, Object>> T_jsp = new ArrayList<HashMap<String, Object>>();// 返回值
		if (T.size() > 0) {
			for (int i = (curPageNum - 1) * pageSize; i < (pageSize * curPageNum); i++) {
				if (i < T.size()) {
					T_jsp.add(T.get(i));
				} else {
					break;
				}
			}
		}

		request.setAttribute("Detailsum", count);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("productModel", productModel);
		request.setAttribute("productUnit", productUnit);
		request.setAttribute("restKeepTime", restKeepTime);
		request.setAttribute("restMaintainTime", restMaintainTime);
		request.setAttribute("manufacturer", manufacturer);
		request.setAttribute("Means", Means);
		request.setAttribute("deviceNo", deviceNo);
		request.setAttribute("type", type);
		return T_jsp;
	}

	/**
	 * 产品明细查询
	 * 
	 * @throws SQLException
	 * */
	public List<HashMap<String, Object>> outList(HttpServletRequest request,
			HttpServletResponse response, String version) throws SQLException {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();// 返回值
		HashMap<String, String> condition = new HashMap<String, String>();// 条件
		ProductDetailService productDetailService = new ProductDetailService();
		String flag = "c";
		if(version.equals("2")){
			String jds = (String)request.getSession().getAttribute("ownedUnit");
			List<String> companys = new InfoService().getCompanyNameList(jds, 2);
			request.setAttribute("companys", companys);
		}else if(version.equals("3")){
			String jdj = (String)request.getSession().getAttribute("ownedUnit");
			List<String> JDS = new InfoService().getJDSNameList(jdj);
			request.setAttribute("JDSes", JDS);
		}
		String ownedUnit = request.getParameter("ownedUnit");
		String productModel = request.getParameter("productModel");
		String productUnit = request.getParameter("productUnit");
		String manufacturer = request.getParameter("manufacturer");
		String Means = request.getParameter("Means");
		String restKeepTime = request.getParameter("restKeepTime");
		String restMaintainTime = request.getParameter("restMaintainTime");
		String deviceNo = request.getParameter("deviceNo");
		String type = request.getParameter("type");
		String keeper = request.getParameter("keeper");
		if (restMaintainTime != null && restMaintainTime.trim().equals(""))
			restMaintainTime = "";
		if (manufacturer != null && manufacturer.trim().equals(""))
			manufacturer = "";

		condition.put("ownedUnit", ownedUnit);
		condition.put("productModel", productModel);
		condition.put("productUnit", productUnit);
		condition.put("restKeepTime", restKeepTime);
		condition.put("restMaintainTime", restMaintainTime);
		condition.put("manufacturer", manufacturer);
		condition.put("Means", Means);
		condition.put("deviceNo", deviceNo);
		condition.put("keeper", keeper);

		int curPageNum = 0;
		int pageSize = 0;
		if (request.getParameter("curPageNum") == null
				|| request.getParameter("pageSize") == null) {
			curPageNum = 1;
			pageSize = 10;
		} else {
			curPageNum = Integer.parseInt(request.getParameter("curPageNum")
					.trim());
			pageSize = Integer
					.parseInt(request.getParameter("pageSize").trim());
		}

		try {
			productDetailService = new ProductDetailService();
			T = productDetailService.selectProductDetail(condition, flag, version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int count = T.size();
//		System.out.println("cur:" + curPageNum);

		List<HashMap<String, Object>> T_jsp = new ArrayList<HashMap<String, Object>>();// 返回值
		if (T.size() > 0) {
			for (int i = (curPageNum - 1) * pageSize; i < (pageSize * curPageNum); i++) {
				if (i < T.size()) {
					T_jsp.add(T.get(i));
				} else {
					break;
				}
			}
		}

		request.setAttribute("Detailsum", count);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("productModel", productModel);
		request.setAttribute("productUnit", productUnit);
		request.setAttribute("restKeepTime", restKeepTime);
		request.setAttribute("restMaintainTime", restMaintainTime);
		request.setAttribute("manufacturer", manufacturer);
		request.setAttribute("Means", Means);
		request.setAttribute("deviceNo", deviceNo);
		request.setAttribute("type", type);
		return T_jsp;
	}

	/**
	 * 导出表单数据
	 * 
	 * @throws SQLException
	 * */
	public void OutProduct(HttpServletRequest request,
			HttpServletResponse response, String version)
			throws ServletException, IOException {
		String ownedUnit = (String) request.getSession().getAttribute(
				"ownedUnit");
		OutDataService outDataService = new OutDataService();
		String jsonStr = request.getParameter("outData");
		JSONArray jarray = JSONArray.fromObject(jsonStr);
		ApplyFormOperation applyFormOperation = new ApplyFormOperation();
		List<ArrayList<String>> dyadicArray = new ArrayList<ArrayList<String>>();
		// 加入标题行
		String pagename = request.getParameter("pagename");
		ArrayList<String> onlineArray = new ArrayList<String>();
		if (pagename.equals("product_collective")) {
			ArrayList<String> onlineList = new ArrayList<String>();
			if(version.equals("1")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("产品单元");
				onlineList.add("器材代码");
				onlineList.add("计量单位");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("金额");
				onlineList.add("承制单位");
				onlineList.add("代储单位");
			}else if(version.equals("2")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("器材代码");
				onlineList.add("计量单位");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("金额");
				onlineList.add("代储单位");
			}else if(version.equals("3")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("器材代码");
				onlineList.add("计量单位");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("金额");
				onlineList.add("军代室");
			}else if(version.equals("4")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("器材代码");
				onlineList.add("计量单位");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("金额");
			}
			for (int i = 0; i < onlineList.size(); i++) {
				onlineArray.add(onlineList.get(i));
			}
		} else if (pagename.equals("product_detail")) {
			ArrayList<String> onlineList = new ArrayList<String>();
			if(version.equals("1")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("产品单元");
				onlineList.add("批次");
				onlineList.add("机号");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("器材类型");
				onlineList.add("操作日期");
				onlineList.add("剩余存放天数");
				onlineList.add("企业剩于维护天数");
				onlineList.add("操作类型");
				onlineList.add("存储期限");
				onlineList.add("企业下次维护日期");
				onlineList.add("承制单位");
				onlineList.add("代储单位");
				onlineList.add("备注");
			}else if(version.equals("2")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("单元名称");
				onlineList.add("批次");
				onlineList.add("机号");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("器材类型");
				onlineList.add("操作日期");
				onlineList.add("操作类型");
				onlineList.add("承制单位");
				onlineList.add("代储单位");
				onlineList.add("备注");
			}else if(version.equals("3")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("单元名称");
				onlineList.add("批次");
				onlineList.add("机号");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("器材类型");
				onlineList.add("操作日期");
				onlineList.add("操作类型");
				onlineList.add("承制单位");
				onlineList.add("备注");
			}else if(version.equals("4")){
				onlineList.add("序号");
				onlineList.add("产品型号");
				onlineList.add("单元名称");
				onlineList.add("批次");
				onlineList.add("机号");
				onlineList.add("单价(元)");
				onlineList.add("数量");
				onlineList.add("器材类型");
				onlineList.add("操作日期");
				onlineList.add("操作类型");
				onlineList.add("承制单位");
				onlineList.add("备注");
			}
			for (int i = 0; i < onlineList.size(); i++) {
				onlineArray.add(onlineList.get(i));
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
		tempFilePath = request.getSession().getServletContext().getRealPath("/")+ File.separator+tempFilePath;
		// 判断是否有没有这个文件目录,如果没有，就生成文件夹uploadFilePlace
		File tempFile = new File(tempFilePath);
		if (!tempFile.exists() && !tempFile.isDirectory()) {
			tempFile.mkdir();
		}
		if (pagename.equals("product_collective")) {
			String fileName = "设备汇总查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+"xlsx";
			String absolutePath = tempFilePath + File.separator + fileName;
			applyFormOperation.exportForm(dyadicArray, absolutePath);
//			outDataService.exportData(absolutePath, version, ownedUnit,dyadicArray);
			// 这个就是所需的文件名和路径
			response.setContentType("text/plain;charset=UTF-8");
			if (absolutePath != null && !"".equals(absolutePath)) {
				response.getWriter().write(absolutePath);
			} else {
				response.getWriter().write(0);
			}
		} else if (pagename.equals("product_detail")) {
			String fileName = "设备明细查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+"xlsx";
			String absolutePath = tempFilePath + File.separator + fileName;
			applyFormOperation.exportForm(dyadicArray, absolutePath);
//			outDataService.exportData(absolutePath, version, ownedUnit,dyadicArray);
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
		if (pagename.equals("product_collective")) {
			fileName = "设备汇总查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+"xlsx";
		} else if (pagename.equals("product_detail")) {
			fileName = "设备明细查询导出表"
					+ MyDateFormat.changeDateToFileString(new Date()) + "."+"xlsx";
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
	 * 产品汇总查询
	 * 
	 * @param pageSize
	 * @param curPageNum
	 * @throws IOException
	 * @throws ServletException
	 * @throws SQLException
	 * */
	public List<HashMap<String, Object>> producCollectiveQuery(
			HttpServletRequest request, HttpServletResponse response,String version)
			throws ServletException, IOException {
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();// 返回值
		HashMap<String, String> condition = new HashMap<String, String>();
		String productModel = request.getParameter("productModel")==null?"/*/*":"/*"+request.getParameter("productModel")+"/*";
		String productUnit = request.getParameter("productUnit")==null?"/*/*":"/*"+request.getParameter("productUnit")+"/*";
		condition.put("productModel", productModel);
		condition.put("productUnit", productUnit);
		String keeper="";
		String ownedUnit="";
		if(version.equals("1")){
			keeper = (String)request.getSession().getAttribute("ownedUnit");
			condition.put("keeper", keeper);
		}
		else if(version.equals("2")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			if(keeper.equals("all")){
				request.setAttribute("keeper", "所有企业");
			}else {
				request.setAttribute("keeper", keeper);
			}
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				StringBuffer sb=new StringBuffer();
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				ArrayList<String> companyList=is.getCompanyNameList(ownedUnit, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
				//changed by LiangYH;增加了if判断
				if(companyStr.length()>0){
					companyStr=companyStr.substring(0, companyStr.length()-1);
					condition.put("keeper", companyStr);
				}
			}
			else{
				condition.put("keeper", "\""+keeper+"\"");
			}
		}else if(version.equals("3")){
			keeper=request.getParameter("keeper")==null?"all":request.getParameter("keeper");
			if(keeper.equals("all")){
				request.setAttribute("keeper", "所有军代室");
			}else {
				request.setAttribute("keeper", keeper);
			}
			InfoService is=new InfoService();
			if(keeper.equals("all")){
				ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
				StringBuffer sb=new StringBuffer();
				ArrayList<String> jdsList=is.getJDSNameList(ownedUnit);
				for (String eachJds : jdsList) {
					ArrayList<String> companyList=is.getCompanyNameList(eachJds, 2);
					for (String eachCompany : companyList) {
						sb.append("\""+eachCompany+"\",");
					}
				}
				String companyStr=sb.toString();
				//System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
			else{
				StringBuffer sb=new StringBuffer();
				ArrayList<String> companyList=is.getCompanyNameList(keeper, 2);
				for (String eachCompany : companyList) {
					sb.append("\""+eachCompany+"\",");
				}
				String companyStr=sb.toString();
				//System.out.println(companyStr);
				companyStr=companyStr.substring(0, companyStr.length()-1);
				condition.put("keeper", companyStr);
			}
		}
		int curPageNum = request.getParameter("curPageNum") == null?1:Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = request.getParameter("pageSize") == null?10:Integer.parseInt(request.getParameter("pageSize"));

		double price = 0.0;
		String priceStr="";
		double totalPrice = 0.0;
		String totalPriceStr="";
		int sum = 0;
		try {
			ProductCollectiveService productCollectiveService = new ProductCollectiveService();
			T = productCollectiveService.selectProductCollective(condition,
					curPageNum, pageSize, version);
			sum = productCollectiveService.querySum(condition, version);
			// 总金额
			totalPrice = productCollectiveService.selectTotalPrice(condition,version);
			// 当前页金额
			for (int i = 0; i < T.size(); i++) {
				price += Double.parseDouble(T.get(i).get("productPrice") + "");
			}
			// 不用科学计数法显示
			totalPriceStr=MyDateFormat.formatDoubleNumber(totalPrice);
			priceStr=MyDateFormat.formatDoubleNumber(price);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("price:"+price);
		// System.out.println("totalPrice:"+totalPrice);

		productModel=productModel.equals("/*/*")?"":productModel;
		productUnit=productUnit.equals("/*/*")?"":productUnit;
		request.setAttribute("price", priceStr);
		request.setAttribute("totalPrice", totalPriceStr);
		request.setAttribute("sum", sum);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("productModel", productModel);
		request.setAttribute("productUnit", productUnit);

		return T;
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
			boolean flag = (Boolean) outDataService
					.importData(filePath, 10, "").get("flag");
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
	 * 查询产品出入库信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, Object>> inapplyInfo(
			HttpServletRequest request, HttpServletResponse response)
			throws SQLException {
		ProductCollectiveService productCollectiveService = new ProductCollectiveService();
		List<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		HashMap<String, String> condition = new HashMap<String, String>();
		String productId = request.getParameter("productId");
		String deviceNo = request.getParameter("deviceNo");
		String type = request.getParameter("type");
		condition.put("productId", productId);
		condition.put("deviceNo", deviceNo);
		condition.put("type", type);
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
		int sum = 0;
		try {
			ProductCollectiveService productInOutService = new ProductCollectiveService();
			sum = productInOutService.queryInOutSum(condition);
			T = productInOutService.ProductInOutInfo(condition, curPageNum,
					pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("deviceNo", deviceNo);
		request.setAttribute("type", type);
		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("sum", sum);
		return T;
	}
	
}
