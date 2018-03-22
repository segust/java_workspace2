package cn.edu.cqupt.controller.warehouse_management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.edu.cqupt.beans.Device;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.warehouse_management.DeviceService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.GetError;

public class DeviceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6223523775769017027L;

	/**
	 * Constructor of the object.
	 */
	public DeviceServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	// 记录日志的相关全局变量
	private String operateType="";	//日志操作类型
	private String remark="";		//日志备注

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
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");
		
		// version==1表示是企业版
		if (version.equals("1")) {
			if (CurrentUser.isWarehouseManage(request)) {
				String operate = request.getParameter("operate");
				// 添加一条设备记录
				if ("addDevice".equals(operate)) {
					addDevice(request, response);
					return;
				}
				// 得到当前页所有的设备记录
				else if ("getCurPageDevice".equals(operate)) {
					getCurDeviceByPage(request, response);
					return;
				}
				// 删除单个设备记录
				else if ("deleteOneDevice".equals(operate)) {
					deleteOneDevice(request, response);
					return;
				}
				// 修改当前设备记录
				else if ("updateDevice".equals(operate)) {
					updateDevice(request, response);
				}
				// 第一次进入设备管理页面
				else if ("firstGetDevice".equals(operate)) {
					firstGetDevice(request, response);
				}
				// 按条件查询当前页设备信息
				else if ("searchDeviceInfoByCondition".equals(operate)) {
					searchDeviceInfoByCondition(request, response);
				}
				// 获取按条件查询的设备的总数
				else if ("getDeviceSumByCondition".equals(operate)) {
					getDeviceSumByCondition(request, response);
				}
			} else {
				//当前用户没有权限
				GetError.limitVisit(request, response);
			}
		} else {// 表示不是企业版
			response.getWriter().write("您使用的不是企业版");
		}

	}

	// 添加一条设备记录
	private void addDevice(HttpServletRequest request,
			HttpServletResponse response) {
		String deviceName = request.getParameter("deviceName");
		String deviceNo = request.getParameter("deviceNo");
		String position = request.getParameter("position");
		String deviceInTime = request.getParameter("deviceInTime");
		String repairTime = request.getParameter("repairTime");
		String status = request.getParameter("status");

		Device device = new Device();
		device.setDeviceName(deviceName);
		device.setDeviceNo(deviceNo);
		device.setPosition(position);
		device.setDeviceInTime(deviceInTime);
		device.setRepairTime(repairTime);
		device.setStatus(status);

		DeviceService ds = new DeviceService();
		try {
			if (ds.addDevice(device)){
				operateType = "添加设备记录";
				remark = "设备名："+deviceName+"，设备编号："+deviceNo;
				Log log = new Log();
				log.setUserName((String) request.getSession().getAttribute("username")); 
				log.setOperateTime(new Date()); // 记录当前用户进行操作的时间
				log.setOperateType(operateType);
				log.setRemark(remark);
				UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
				response.getWriter().print("1");
			}
			else
				response.getWriter().print("0");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取当前页设备记录
	private void getCurDeviceByPage(HttpServletRequest request,
			HttpServletResponse response) {
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));

		ArrayList<Device> curDeviceList = new DeviceService()
				.searchDeviceByPage(curPageNum, pageSize);

		// list转JSON
		JSONArray jsonArray = new JSONArray();
		for (Device device : curDeviceList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("devieName", device.getDeviceName());
			jsonObject.put("deviceNo", device.getDeviceNo());
			jsonObject.put("position", device.getPosition());
			jsonObject.put("deviceInTime", device.getDeviceInTime());
			jsonObject.put("repairTime", device.getRepairTime());
			jsonObject.put("status", device.getStatus());
			jsonArray.add(jsonObject);
		}

		try {
			response.getWriter().print(jsonArray.toString());
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 第一次进入获取设备记录信息和设备类型
	private void firstGetDevice(HttpServletRequest request,
			HttpServletResponse response) {
		DeviceService ds = new DeviceService();
		ArrayList<Device> firstDeviceList = ds.searchDeviceByPage(1, 10);
		ArrayList<String> deviceTypeList = ds.getDeviceType();
		long sum = ds.getDeviceSum();
		long pageSum = getPageNum(sum, 10);

		request.setAttribute("firstDeviceList", firstDeviceList);
		request.setAttribute("pageSum", pageSum);
		request.setAttribute("deviceTypeList", deviceTypeList);

		try {
			request.getRequestDispatcher(
					"jsp/qy/warehouse_management/deviceinfo/showDeviceInfo.jsp")
					.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除一条设备记录
	private void deleteOneDevice(HttpServletRequest request,
			HttpServletResponse response) {
		long deviceId = Long.parseLong(request.getParameter("deviceId"));
		DeviceService ds = new DeviceService();
		try {
			if (ds.deleteDevice(deviceId)){
				Device device=ds.getDeviceById(deviceId);
				operateType = "删除设备记录";
				remark = "设备名："+device.getDeviceName()+"，设备编号："+device.getDeviceNo();
				Log log = new Log();
				log.setUserName((String) request.getSession().getAttribute("username")); 
				log.setOperateTime(new Date()); // 记录当前用户进行操作的时间
				log.setOperateType(operateType);
				log.setRemark(remark);
				UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
				response.getWriter().print("1");
			}
			else
				response.getWriter().print("0");
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	// 更新设备记录
	private void updateDevice(HttpServletRequest request,
			HttpServletResponse response) {
		long deviceId = Long.parseLong(request.getParameter("deviceId"));
		String deviceName = request.getParameter("deviceName");
		String deviceNo = request.getParameter("deviceNo");
		String position = request.getParameter("position");
		String deviceInTime = request.getParameter("deviceInTime");
		String status = request.getParameter("status");

		Device device = new Device();
		device.setDeviceName(deviceName);
		device.setDeviceNo(deviceNo);
		device.setPosition(position);
		device.setDeviceInTime(deviceInTime);
		device.setStatus(status);

		DeviceService ds = new DeviceService();
		Device oldDevice=ds.getDeviceById(deviceId);
		try {
			if (ds.updateDevice(device, deviceId)){
				operateType = "修改设备记录";
				remark = "原设备记录："+oldDevice.toString()+"；修改设备记录："+device.toString();
				Log log = new Log();
				log.setUserName((String) request.getSession().getAttribute("username")); 
				log.setOperateTime(new Date()); // 记录当前用户进行操作的时间
				log.setOperateType(operateType);
				log.setRemark(remark);
				UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
				response.getWriter().print("1");
			}
			else
				response.getWriter().print("0");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 按条件查询设备信息
	private void searchDeviceInfoByCondition(HttpServletRequest request,
			HttpServletResponse response) {
		String deviceStartTime = request.getParameter("deviceStartTime");
		String deviceEndTime = request.getParameter("deviceEndTime");
		String deviceType = request.getParameter("deviceType");
		String deviceStatus = request.getParameter("deviceStatus");
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));

		DeviceService ds = new DeviceService();
		// 获取按条件查询的当前页设备列表
		ArrayList<Device> curDeviceList = ds.searchDeviceByCondition(
				deviceStartTime, deviceEndTime, deviceType, deviceStatus,
				curPageNum, pageSize);

		// list转JSON
		JSONArray jsonArray = new JSONArray();
		for (Device device : curDeviceList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("deviceId", device.getDeviceId());
			jsonObject.put("deviceName", device.getDeviceName());
			jsonObject.put("deviceNo", device.getDeviceNo());
			jsonObject.put("position", device.getPosition());
			jsonObject.put("deviceInTime", device.getDeviceInTime());
			jsonObject.put("status", device.getStatus());
			jsonObject.put("repairTime", device.getRepairTime());
			jsonArray.add(jsonObject);
		}

		try {
			response.getWriter().print(jsonArray.toString());
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void getDeviceSumByCondition(HttpServletRequest request,
			HttpServletResponse response) {
		String deviceStartTime = request.getParameter("deviceStartTime");
		String deviceEndTime = request.getParameter("deviceEndTime");
		String deviceType = request.getParameter("deviceType");
		String deviceStatus = request.getParameter("deviceStatus");

		DeviceService ds = new DeviceService();
		long sum = ds.getDeviceSumByCondition(deviceStartTime, deviceEndTime,
				deviceType, deviceStatus);

		try {
			response.getWriter().print(sum);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 计算分多少页
	public long getPageNum(long sum, int pageSize) {
		long pageSum;
		pageSum = sum % pageSize == 0 ? (sum / pageSize) : (sum / pageSize + 1);
		return pageSum;
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
