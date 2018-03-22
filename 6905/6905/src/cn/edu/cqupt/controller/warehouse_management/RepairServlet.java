package cn.edu.cqupt.controller.warehouse_management;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.RepairInfo;
import cn.edu.cqupt.service.warehouse_management.RepairService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;

public class RepairServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 788043397922842500L;

	/**
	 * Constructor of the object.
	 */
	public RepairServlet() {
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
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		// version==1表示是企业版
		if (version.equals("1")) {
			if (CurrentUser.isWarehouseManage(request)) {
				String operate = request.getParameter("operate");
				// 添加一条设备记录
				if ("addRepairInfo".equals(operate)) {
					addRepairInfo(request,response);
					return;
				}
				else if("getCurDeviceRepairInfo".equals(operate)){
					getRepairInfoByOneDevice(request, response);
					return;
				}
			} else {
				//当前用户没有权限
				GetError.limitVisit(request, response);
			}
		} else {// 表示不是企业版
			response.getWriter().write("您使用的不是企业版");
		}
	}
	
	// 添加一条维修记录
	private void addRepairInfo(HttpServletRequest request,HttpServletResponse response){
		RepairInfo repairInfo=new RepairInfo();
		//获取前台的参数
		String repairMan=request.getParameter("repairMan");
		long deviceId=Long.parseLong(request.getParameter("deviceId"));
		String deviceName=request.getParameter("deviceName");
		String deviceNo=request.getParameter("deviceNo");
		String repairTime=request.getParameter("repairTime");
		String repairReason=request.getParameter("repairReason");
		
		repairInfo.setDeviceId(deviceId);
		repairInfo.setDeviceName(deviceName);
		repairInfo.setDeviceNo(deviceNo);
		repairInfo.setRepairMan(repairMan);
		repairInfo.setRepairReason(repairReason);
		repairInfo.setRepairTime(repairTime);
		
		RepairService rs=new RepairService();
		
		try {
			if(rs.addRepair(repairInfo))
				response.getWriter().print("1");
			else
				response.getWriter().print("0");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
	}
	
	// 更新一条维修记录
	private void updateRepairInfo(HttpServletRequest request,HttpServletResponse response){
		RepairInfo repairInfo=new RepairInfo();
		long repairId=Long.parseLong(request.getParameter("repairId"));
		String repairMan=request.getParameter("repairman");
		String repairTime=request.getParameter("repairTime");
		String repairReason=request.getParameter("repairReason");
		
		repairInfo.setRepairMan(repairMan);
		repairInfo.setRepairTime(repairTime);
		repairInfo.setRepairReason(repairReason);
		
		RepairService rs=new RepairService();
		
		try {
			if(rs.updateRepa(repairInfo, repairId))
				response.getWriter().print("1");
			else
				response.getWriter().print("0");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	// 删除一条维修记录
	private void deleteRepairInfo(HttpServletRequest request,HttpServletResponse response){
		RepairService rs=new RepairService();
		long repairId=Long.parseLong(request.getParameter("repairId"));
		
		try {
			if(rs.deleteDevice(repairId))
				response.getWriter().print("1");
			else
				response.getWriter().print("0");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	// 查询某一设备的当前页的维修记录
	private void getRepairInfoByOneDevice(HttpServletRequest request,HttpServletResponse response){
		int curPageNum=Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize=Integer.parseInt(request.getParameter("pageSize"));
		long deviceId=Long.parseLong(request.getParameter("deviceId"));
		
		ArrayList<RepairInfo> curRepairInfoList=new RepairService().searchRepairInfoByPage(curPageNum, pageSize, deviceId);
		
		//list转JSON
		JSONArray jsonArray=new JSONArray();
		for (RepairInfo repairInfo : curRepairInfoList) {
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("repairId", repairInfo.getRepairId());
			jsonObject.put("repairMan", repairInfo.getRepairMan());
			jsonObject.put("repairTime", repairInfo.getRepairTime());
			jsonObject.put("repairReason", repairInfo.getRepairReason());
			jsonArray.add(jsonObject);
		}
		
		try {
			response.getWriter().print(jsonArray.toString());
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
