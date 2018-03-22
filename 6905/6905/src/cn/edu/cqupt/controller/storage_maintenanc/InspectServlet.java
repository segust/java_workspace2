package cn.edu.cqupt.controller.storage_maintenanc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
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
import cn.edu.cqupt.beans.InspectRecord;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.storage_maintenanc.InspectService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.MyDateFormat;

public class InspectServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5063373575585490640L;

	/**
	 * Constructor of the object.
	 */
	public InspectServlet() {
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

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");

		// version==1表示是企业版
		if (true) {
			// 举例：判断当前用户是否具有 业务办理 权限
			if (CurrentUser.isContractManage(request)) {
				// 这里面写你的主要Servlet的代码

				Boolean power = CurrentUser.isStoreMantain(request);
				//根据操作类型执行不同内容
				if (power) {
					if (request.getParameter("operate").equalsIgnoreCase(
							"inspect")) {
						this.AddInspectRecord(request, response,version);
					}

					else if (request.getParameter("operate").equalsIgnoreCase(
							"record")) {
						this.recordOperate(request, response,version);
					}

					else if (request.getParameter("operate").equalsIgnoreCase(
							"inspectQuery")) {
						this.inspectQueryOperate(request, response,version);
					}
				}
			}
		}
	}

	private void inspectQueryOperate(HttpServletRequest request,
			HttpServletResponse response,String version) throws ServletException, IOException {
		InspectService service = new InspectService();
		List<InspectRecord> list = service.getAllinspectRecord();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		for (int i=0;i<list.size();i++){
			 int j=0;
			 jsonArray1.add(j++, list.get(i).getUnit());
			 jsonArray1.add(j++, list.get(i).getDate().toString());
			 jsonArray1.add(j++, list.get(i).getSite());
			 jsonArray1.add(j++, list.get(i).getItem());
			 jsonArray1.add(j++, list.get(i).getSuggest());
			 jsonArray1.add(j++, list.get(i).getFeedback());
			 jsonArray1.add(j++, list.get(i).getRemark());
			 jsonArray2.add(jsonArray1);
		}
		response.getWriter().write(jsonArray2.toString());
	}

	private void recordOperate(HttpServletRequest request,
			HttpServletResponse response,String version) throws ServletException, IOException {

		Boolean InspectFlag = false;
		Boolean LogFlag = false;

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");

		String JDInspect = request.getParameter("JDInspect");
		
		String data = request.getParameter("data");
		JSONArray record = JSONArray.fromObject(data);
		if (record.size() == 0) {
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("请选择已检查内容！");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < record.size(); i++) {
			JSONArray singleRecord = record.getJSONArray(i);
			map.put("productId", singleRecord.getString(0));
			map.put("productModel", singleRecord.getString(1));
			map.put("unitName", singleRecord.getString(2));
			map.put("batch", singleRecord.getString(3));
			map.put("deviceNo", singleRecord.getString(4));
			map.put("price", singleRecord.getString(5));
			map.put("num", singleRecord.getString(6));
			map.put("productType", singleRecord.getString(7));
			map.put("storageTime", singleRecord.getString(8));
			map.put("restKeepTime", singleRecord.getString(9));
			map.put("manufacturer", singleRecord.getString(10));
			map.put("keeper", singleRecord.getString(11));
			map.put("remark", singleRecord.getString(13));
			map.put("userName", userName);
			map.put("JDInspect", JDInspect);
			InspectService service = new InspectService();
			InspectFlag = service.recordService(map);

			if (InspectFlag) {
				Log log = new Log();
				log.setProductId(Integer.valueOf(singleRecord.getString(0)));
				log.setOperateType("检查");
				log.setOperateTime(new Date());
				log.setUserName(userName);
				if (JDInspect.equalsIgnoreCase("0")) {
					log.setInspectPerson("军代室");
				} else if (JDInspect.equalsIgnoreCase("1"))
					log.setInspectPerson("军代局");
				log.setRemark(singleRecord.getString(13));
				LogFlag = UserLogService.saveInspectLog(log);
			}

			if (LogFlag)
				continue;
			else
				break;
		}

		if (LogFlag == true) {
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("保存成功！");
		} else {

			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("保存失败！");
		}

	}

	private void AddInspectRecord(HttpServletRequest request,
			HttpServletResponse response,String version) throws ServletException, IOException {
       InspectRecord ir =new InspectRecord();
	    String jsonStr = request.getParameter("data");
		//JSONArray jarray = JSONArray.fromObject(jsonStr);
	    JSONObject jsonObj = JSONObject.fromObject(jsonStr);
			 String unit = jsonObj.getString("unit")==null? "":jsonObj.getString("unit");
			 String date = jsonObj.getString("date")==null? "":jsonObj.getString("date");
			 System.out.println("111111"+date);
			 String site = jsonObj.getString("site")==null? "":jsonObj.getString("site");
			 String item = jsonObj.getString("item")==null? "":jsonObj.getString("item");
			 String suggest = jsonObj.getString("suggest")==null? "":jsonObj.getString("suggest");
			 String feedback = jsonObj.getString("feedback")==null? "":jsonObj.getString("feedback");
			 String remark = jsonObj.getString("remark")==null? "":jsonObj.getString("remark");
			 ir.setUnit(unit);
			    ir.setDate(MyDateFormat.changeStringToDate(date));
			    ir.setSite(site);
		        ir.setItem(item);
			    ir.setSuggest(suggest);
			    ir.setFeedback(feedback);
			    ir.setRemark(remark);	
		InspectService service = new InspectService();
		boolean flag;
		flag = service.AddInspectRecord(ir);
		 System.out.println("2222"+flag);
		if (flag == true) {
			JSONObject job = new JSONObject();
			job.put("status", true);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write(job.toString());
		} else {
			JSONObject job = new JSONObject();
			job.put("status", false);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write(job.toString());
		}
//		if(version.equalsIgnoreCase("1")){
//		request.getRequestDispatcher(
//				"/jsp/qy/storage_maintenanc/addInspect.jsp").forward(request,
//				response);
//		}else if(version.equalsIgnoreCase("2")){
//			request.getRequestDispatcher(
//				"/jsp/jds/storage_maintenanc/addInspect.jsp").forward(request,
//				response);	
//		}else if(version.equalsIgnoreCase("3")){
//			request.getRequestDispatcher(
//					"/jsp/jdj/storage_maintenanc/addInspect.jsp").forward(request,
//					response);	
//		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
