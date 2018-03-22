package cn.edu.cqupt.controller.storage_maintenanc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import cn.edu.cqupt.log.LogInOutService;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.storage_maintenanc.MaintainInfoQueryService;
import cn.edu.cqupt.service.storage_maintenanc.MaintainQueryService;
import cn.edu.cqupt.service.storage_maintenanc.UpdateLatestMaintainTimeAfterMaintainService;
import cn.edu.cqupt.service.transact_business.ApplyFormOperation;
import cn.edu.cqupt.service.transact_business.UploadFile;
import cn.edu.cqupt.util.DownloadFile;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class MaintainServlet extends HttpServlet {

	private MaintainQueryService queryService;
	private MaintainInfoQueryService infoQueryService;
	private UpdateLatestMaintainTimeAfterMaintainService updateMaintainTimeService;
	private LogInOutService logInOutService = null;
	private UploadFile uploadFileService = null;
	private ApplyFormOperation applyFormOperation = null;
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException
	{
		ServletContext context = getServletContext();		
		String operateType = (String) request.getParameter("operateType");
		String version = context.getInitParameter("version");
		if("1".equals(version))
		{
			if(operateType.equalsIgnoreCase("maintainQuery"))
			{
				HttpSession session = request.getSession();
				String username = (String) session.getAttribute("username");
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/qy/storage_maintenanc/addMaintain.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String inMeans = request.getParameter("inMeans");
				if(inMeans == null)
					inMeans = "";
				String restKeepTime = request.getParameter("restKeepTime");
				if(restKeepTime == null)
					restKeepTime = "";
				String restMaintainTime = request.getParameter("restMaintainTime");
				if(restMaintainTime == null)
					restMaintainTime = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(inMeans);
				requirements.add(restKeepTime);
				requirements.add(restMaintainTime);
				requirements.add(deviceNo);
				requirements.add(username);
				
				queryService = new MaintainQueryService();
				ArrayList<HashMap<String, String>> result = queryService.getMaintainInfoByPage(requirements, curPageNum, pageSize);
				long sum = queryService.getSum(requirements);
				request.setAttribute("sum", sum);
				request.setAttribute("result", result);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			else if("exportLog".equals(operateType)){
				/**
				 * 导出qy_log表
				 * @author LiangYH
				 */
				String logIDsStr = request.getParameter("logId");
				System.out.println(logIDsStr);
				JSONArray ja = JSONArray.fromObject(logIDsStr);
				
				//解析JSON
				List<Long> logIDs = new ArrayList<Long>();
				int len = ja.size();
				for(int i = 0; i < len; i++){
					logIDs.add(ja.getLong(i));
				}
				//查询
				logInOutService = new LogInOutService();
				ArrayList<ArrayList<String>> targetDyadic = null;
				targetDyadic = logInOutService.queryLogs(logIDs);
				
				String tempFilePath = request.getSession().getServletContext()
									.getRealPath("/")+ StringUtil.UPLOAD_FOLDER;
				File tempFile = new File(tempFilePath);
				if (!tempFile.exists() && !tempFile.isDirectory()) {
					tempFile.mkdir();
				}
				String fileName = "ExportForm"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
				String absolutePath = tempFilePath + File.separator+ fileName;
				
				//生成excel文件
				applyFormOperation = new ApplyFormOperation();
				boolean runStatus = false;
				runStatus = applyFormOperation.exportForm(absolutePath, targetDyadic);
				
				response.setContentType("text/plain,charset=utf8");
				if(runStatus){
					response.getWriter().write(absolutePath);
				}else{
					response.getWriter().write("0");
				}
				return ;
			}else if("download".equals(operateType)){
				String absolutePath = request.getParameter("path");
				String fileName = "存储维护导出表"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
				// 防止因为浏览器不同导致文件名乱码
				fileName = DownloadFile.getNormalFilename(request, fileName);
				// 改变响应头，发起下载流
				DownloadFile.launchDownloadStream(response, absolutePath,fileName);
				// 删除文件
				File file = new File(absolutePath);
				if (file.exists()) {
					file.delete();
				}
				return ;
			}
			else if(operateType.equalsIgnoreCase("maintainInfoQuery"))
			{
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/qy/storage_maintenanc/queryMaintain.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String maintainType = request.getParameter("maintainType");
				if(maintainType == null)
					maintainType = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(maintainType);
				requirements.add(deviceNo);
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				infoQueryService = new MaintainInfoQueryService();
				
				ArrayList<HashMap<String, String>> result = infoQueryService.getMaintainLogByPage(requirements, pageSize, curPageNum);
				long sum = infoQueryService.countResultNumber(requirements);
				request.setAttribute("result", result);
				request.setAttribute("sum", sum);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			
			else if(operateType.equalsIgnoreCase("saveMaintainLog"))
			{
				String jsonStr = request.getParameter("data");
				JSONArray array = JSONArray.fromObject(jsonStr);
				if (array.size() == 0) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("请选择已检查内容！");
					return;
				}
				//为了使代码更易理解，这里先把json传过来的东西存到一个List里面（info），再在后面从List里面取
				List<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
				//专门用一个List来存productId，方便后面的修改
				List<String> productIds = new ArrayList<String>();
				for(int i =0;i<array.size();i++) {
					HashMap<String, String> info = new HashMap<String, String>();
					JSONArray tempArray = array.getJSONArray(i);
					info.put("productId", tempArray.get(0).toString());
					productIds.add(tempArray.get(0).toString());
					info.put("username", tempArray.get(12).toString());
					info.put("maintainType", tempArray.get(13).toString());
					info.put("remark", tempArray.get(14).toString());
					infos.add(info);
				}
				updateMaintainTimeService = new UpdateLatestMaintainTimeAfterMaintainService();
				boolean isOK = true;
				for(int i = 0; i < infos.size(); i++)
				{
					HashMap<String, String> req = infos.get(i);
					String productId = req.get("productId");
					String username = req.get("username");
					String remark = req.get("remark");
					String maintainType = req.get("maintainType");
					if(!UserLogService.SaveMainTainLog(productId, username, maintainType, remark))
						isOK = false;
				}
				if(updateMaintainTimeService.updateMaintainTime(productIds) == false)
					isOK = false;
				if (isOK) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存成功！");
					return;
				} else {
	
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存失败！");
					return;
				}
			}
		}
		
		if("2".equals(version))
		{
			if(operateType.equalsIgnoreCase("maintainQuery"))
			{
				HttpSession session = request.getSession();
				String username = (String) session.getAttribute("username");
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jds/storage_maintenanc/addMaintain.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String inMeans = request.getParameter("inMeans");
				if(inMeans == null)
					inMeans = "";
				String restKeepTime = request.getParameter("restKeepTime");
				if(restKeepTime == null)
					restKeepTime = "";
				String restMaintainTime = request.getParameter("restMaintainTime");
				if(restMaintainTime == null)
					restMaintainTime = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(inMeans);
				requirements.add(restKeepTime);
				requirements.add(restMaintainTime);
				requirements.add(deviceNo);
				requirements.add(username);
				
				queryService = new MaintainQueryService();
				ArrayList<HashMap<String, String>> result = queryService.getMaintainInfoByPage(requirements, curPageNum, pageSize);
				long sum = queryService.getSum(requirements);
				request.setAttribute("sum", sum);
				request.setAttribute("result", result);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			else if(operateType.equalsIgnoreCase("maintainInfoQuery"))
			{
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jds/storage_maintenanc/queryMaintain.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String maintainType = request.getParameter("maintainType");
				if(maintainType == null)
					maintainType = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(maintainType);
				requirements.add(deviceNo);
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				infoQueryService = new MaintainInfoQueryService();
				
				ArrayList<HashMap<String, String>> result = infoQueryService.getMaintainLogByPage(requirements, pageSize, curPageNum);
				long sum = infoQueryService.countResultNumber(requirements);
				request.setAttribute("result", result);
				request.setAttribute("sum", sum);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			
			else if(operateType.equalsIgnoreCase("saveMaintainLog"))
			{
				String jsonStr = request.getParameter("data");
				JSONArray array = JSONArray.fromObject(jsonStr);
				if (array.size() == 0) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("请选择已检查内容！");
					return;
				}
				//为了使代码更易理解，这里先把json传过来的东西存到一个List里面（info），再在后面从List里面取
				List<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
				//专门用一个List来存productId，方便后面的修改
				List<String> productIds = new ArrayList<String>();
				for(int i =0;i<array.size();i++) {
					HashMap<String, String> info = new HashMap<String, String>();
					JSONArray tempArray = array.getJSONArray(i);
					info.put("productId", tempArray.get(0).toString());
					productIds.add(tempArray.get(0).toString());
					info.put("username", tempArray.get(12).toString());
					info.put("maintainType", tempArray.get(13).toString());
					info.put("remark", tempArray.get(14).toString());
					infos.add(info);
				}
				updateMaintainTimeService = new UpdateLatestMaintainTimeAfterMaintainService();
				boolean isOK = true;
				for(int i = 0; i < infos.size(); i++)
				{
					HashMap<String, String> req = infos.get(i);
					String productId = req.get("productId");
					String username = req.get("username");
					String remark = req.get("remark");
					String maintainType = req.get("maintainType");
					if(!UserLogService.SaveMainTainLog(productId, username, maintainType, remark))
						isOK = false;
				}
				
				if(updateMaintainTimeService.updateMaintainTime(productIds) == false)
					isOK = false;
				if (isOK) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存成功！");
					return;
				} else {
	
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存失败！");
					return;
				}
			}
			
			else if(operateType.equalsIgnoreCase("inspect"))
			{
				HttpSession session = request.getSession();
				String username = (String) session.getAttribute("username");
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jds/storage_maintenanc/addInspect.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String inMeans = request.getParameter("inMeans");
				if(inMeans == null)
					inMeans = "";
				String restKeepTime = request.getParameter("restKeepTime");
				if(restKeepTime == null)
					restKeepTime = "";
				String restMaintainTime = request.getParameter("restMaintainTime");
				if(restMaintainTime == null)
					restMaintainTime = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(inMeans);
				requirements.add(restKeepTime);
				requirements.add(restMaintainTime);
				requirements.add(deviceNo);
				requirements.add(username);
				
				queryService = new MaintainQueryService();
				ArrayList<HashMap<String, String>> result = queryService.getMaintainInfoByPage(requirements, curPageNum, pageSize);
				long sum = queryService.getSum(requirements);
				request.setAttribute("sum", sum);
				request.setAttribute("result", result);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			
			
			else if("queryInspect".equals(operateType))
			{
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jds/storage_maintenanc/queryInspect.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String maintainType = request.getParameter("maintainType");
				if(maintainType == null)
					maintainType = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(maintainType);
				requirements.add(deviceNo);
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				infoQueryService = new MaintainInfoQueryService();
				
				ArrayList<HashMap<String, String>> result = infoQueryService.getInspectLogByPage(requirements, pageSize, curPageNum);
				long sum = infoQueryService.countInspectNumber(requirements);
				request.setAttribute("result", result);
				request.setAttribute("sum", sum);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			else if(operateType.equalsIgnoreCase("saveInspectLog"))
			{
				String jsonStr = request.getParameter("data");
				JSONArray array = JSONArray.fromObject(jsonStr);
				if (array.size() == 0) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("请选择已检查内容！");
					return;
				}
				//为了使代码更易理解，这里先把json传过来的东西存到一个List里面（info），再在后面从List里面取
				List<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
				//专门用一个List来存productId，方便后面的修改
				List<String> productIds = new ArrayList<String>();
				for(int i =0;i<array.size();i++) {
					HashMap<String, String> info = new HashMap<String, String>();
					JSONArray tempArray = array.getJSONArray(i);
					info.put("productId", tempArray.get(0).toString());
					productIds.add(tempArray.get(0).toString());
					info.put("username", tempArray.get(13).toString());
					info.put("remark", tempArray.get(14).toString());
					info.put("maintainType", tempArray.get(15).toString());
					infos.add(info);
				}
				updateMaintainTimeService = new UpdateLatestMaintainTimeAfterMaintainService();
				boolean isOK = true;
				for(int i = 0; i < infos.size(); i++)
				{
					HashMap<String, String> req = infos.get(i);
					String productId = req.get("productId");
					String username = req.get("username");
					String remark = req.get("remark");
					String maintainType = req.get("maintainType");
					if(!UserLogService.SaveInspectLog(productId, username, maintainType, remark))
						isOK = false;
				}
				if(updateMaintainTimeService.updateMaintainTime(productIds) == false)
					isOK = false;
				if (isOK) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存成功！");
					return;
				} else {
	
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存失败！");
					return;
				}
			}else if("importLog".equals(operateType)){
				/**
				 * 导入qy_log表
				 * @author LiangYH
				 */
				//上传将要导入的文件
				uploadFileService = new UploadFile();
				Map<String,String> fileNameMap = null;
				fileNameMap = uploadFileService.uploadFile(request, response);
				String filePath = fileNameMap.get("fileName");
				//读取上传的文件
				applyFormOperation = new ApplyFormOperation();
				List<ArrayList<String>> dyadic = null;
				dyadic = applyFormOperation.importForm(filePath, 0);
				//保存数据
				logInOutService = new LogInOutService();
				boolean runStatus = logInOutService.saveLogs(dyadic);
				
				response.setContentType("text/plain,charset=utf8");
				if(runStatus){
					response.getWriter().write("1");
				}else{
					response.getWriter().write("0");
				}
				return ;
			}else if("exportLog".equals(operateType)){
				/**
				 * 导出qy_log表
				 * @author LiangYH
				 */
				String logIDsStr = request.getParameter("logId");
				System.out.println(logIDsStr);
				JSONArray ja = JSONArray.fromObject(logIDsStr);
				
				//解析JSON
				List<Long> logIDs = new ArrayList<Long>();
				int len = ja.size();
				for(int i = 0; i < len; i++){
					logIDs.add(ja.getLong(i));
				}
				//查询
				logInOutService = new LogInOutService();
				ArrayList<ArrayList<String>> targetDyadic = null;
				targetDyadic = logInOutService.queryLogs(logIDs);
				
				String tempFilePath = request.getSession().getServletContext()
									.getRealPath("/")+ StringUtil.UPLOAD_FOLDER;
				File tempFile = new File(tempFilePath);
				if (!tempFile.exists() && !tempFile.isDirectory()) {
					tempFile.mkdir();
				}
				String fileName = "ExportForm"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
				String absolutePath = tempFilePath + File.separator+ fileName;
				
				//生成excel文件
				applyFormOperation = new ApplyFormOperation();
				boolean runStatus = false;
				runStatus = applyFormOperation.exportForm(absolutePath, targetDyadic);
				
				response.setContentType("text/plain,charset=utf8");
				if(runStatus){
					response.getWriter().write(absolutePath);
				}else{
					response.getWriter().write("0");
				}
				return ;
			}else if("download".equals(operateType)){
				String absolutePath = request.getParameter("path");
				String fileName = "存储维护导出表_"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
				// 防止因为浏览器不同导致文件名乱码
				fileName = DownloadFile.getNormalFilename(request, fileName);
				// 改变响应头，发起下载流
				DownloadFile.launchDownloadStream(response, absolutePath,fileName);
				// 删除文件
				File file = new File(absolutePath);
				if (file.exists()) {
					file.delete();
				}
				return ;
			}
			else
			{
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/login/login.jsp");
				disp.forward(request, response);
			}
		}if("3".equals(version))
		{
			if(operateType.equalsIgnoreCase("maintainQuery"))
			{
				HttpSession session = request.getSession();
				String username = (String) session.getAttribute("username");
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jdj/storage_maintenanc/addMaintain.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String inMeans = request.getParameter("inMeans");
				if(inMeans == null)
					inMeans = "";
				String restKeepTime = request.getParameter("restKeepTime");
				if(restKeepTime == null)
					restKeepTime = "";
				String restMaintainTime = request.getParameter("restMaintainTime");
				if(restMaintainTime == null)
					restMaintainTime = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(inMeans);
				requirements.add(restKeepTime);
				requirements.add(restMaintainTime);
				requirements.add(deviceNo);
				requirements.add(username);
				
				queryService = new MaintainQueryService();
				ArrayList<HashMap<String, String>> result = queryService.getMaintainInfoByPage(requirements, curPageNum, pageSize);
				long sum = queryService.getSum(requirements);
				request.setAttribute("sum", sum);
				request.setAttribute("result", result);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			else if(operateType.equalsIgnoreCase("maintainInfoQuery"))
			{
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jdj/storage_maintenanc/queryMaintain.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String maintainType = request.getParameter("maintainType");
				if(maintainType == null)
					maintainType = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(maintainType);
				requirements.add(deviceNo);
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				infoQueryService = new MaintainInfoQueryService();
				
				ArrayList<HashMap<String, String>> result = infoQueryService.getMaintainLogByPage(requirements, pageSize, curPageNum);
				long sum = infoQueryService.countResultNumber(requirements);
				request.setAttribute("result", result);
				request.setAttribute("sum", sum);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			else if("exportLog".equals(operateType)){
				/**
				 * 导出qy_log表
				 * @author LiangYH
				 */
				String logIDsStr = request.getParameter("logId");
				System.out.println(logIDsStr);
				JSONArray ja = JSONArray.fromObject(logIDsStr);
				
				//解析JSON
				List<Long> logIDs = new ArrayList<Long>();
				int len = ja.size();
				for(int i = 0; i < len; i++){
					logIDs.add(ja.getLong(i));
				}
				//查询
				logInOutService = new LogInOutService();
				ArrayList<ArrayList<String>> targetDyadic = null;
				targetDyadic = logInOutService.queryLogs(logIDs);
				
				String tempFilePath = request.getSession().getServletContext()
									.getRealPath("/")+ StringUtil.UPLOAD_FOLDER;
				File tempFile = new File(tempFilePath);
				if (!tempFile.exists() && !tempFile.isDirectory()) {
					tempFile.mkdir();
				}
				String fileName = "ExportForm"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
				String absolutePath = tempFilePath + File.separator+ fileName;
				
				//生成excel文件
				applyFormOperation = new ApplyFormOperation();
				boolean runStatus = false;
				runStatus = applyFormOperation.exportForm(absolutePath, targetDyadic);
				
				response.setContentType("text/plain,charset=utf8");
				if(runStatus){
					response.getWriter().write(absolutePath);
				}else{
					response.getWriter().write("0");
				}
				return ;
			}	else if("download".equals(operateType)){
				String absolutePath = request.getParameter("path");
				String fileName = "存储维护导出表_"+MyDateFormat.changeDateToFileString(new Date())+"."+StringUtil.SUFFIX_EXECL;
				// 防止因为浏览器不同导致文件名乱码
				fileName = DownloadFile.getNormalFilename(request, fileName);
				// 改变响应头，发起下载流
				DownloadFile.launchDownloadStream(response, absolutePath,fileName);
				// 删除文件
				File file = new File(absolutePath);
				if (file.exists()) {
					file.delete();
				}
				return ;
			}
			else if(operateType.equalsIgnoreCase("saveMaintainLog"))
			{
				String jsonStr = request.getParameter("data");
				JSONArray array = JSONArray.fromObject(jsonStr);
				if (array.size() == 0) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("请选择已检查内容！");
					return;
				}
				//为了使代码更易理解，这里先把json传过来的东西存到一个List里面（info），再在后面从List里面取
				List<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
				//专门用一个List来存productId，方便后面的修改
				List<String> productIds = new ArrayList<String>();
				for(int i =0;i<array.size();i++) {
					HashMap<String, String> info = new HashMap<String, String>();
					JSONArray tempArray = array.getJSONArray(i);
					info.put("productId", tempArray.get(0).toString());
					productIds.add(tempArray.get(0).toString());
					info.put("username", tempArray.get(12).toString());
					info.put("maintainType", tempArray.get(13).toString());
					info.put("remark", tempArray.get(14).toString());
					infos.add(info);
				}
				updateMaintainTimeService = new UpdateLatestMaintainTimeAfterMaintainService();
				boolean isOK = true;
				for(int i = 0; i < infos.size(); i++)
				{
					HashMap<String, String> req = infos.get(i);
					String productId = req.get("productId");
					String username = req.get("username");
					String remark = req.get("remark");
					String maintainType = req.get("maintainType");
					if(!UserLogService.SaveMainTainLog(productId, username, maintainType, remark))
						isOK = false;
				}
				
				if(updateMaintainTimeService.updateMaintainTime(productIds) == false)
					isOK = false;
				if (isOK) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存成功！");
					return;
				} else {
	
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存失败！");
					return;
				}
			}
			
			if(operateType.equalsIgnoreCase("inspect"))
			{
				HttpSession session = request.getSession();
				String username = (String) session.getAttribute("username");
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jdj/storage_maintenanc/addInspect.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String inMeans = request.getParameter("inMeans");
				if(inMeans == null)
					inMeans = "";
				String restKeepTime = request.getParameter("restKeepTime");
				if(restKeepTime == null)
					restKeepTime = "";
				String restMaintainTime = request.getParameter("restMaintainTime");
				if(restMaintainTime == null)
					restMaintainTime = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(inMeans);
				requirements.add(restKeepTime);
				requirements.add(restMaintainTime);
				requirements.add(deviceNo);
				requirements.add(username);
				
				queryService = new MaintainQueryService();
				ArrayList<HashMap<String, String>> result = queryService.getMaintainInfoByPage(requirements, curPageNum, pageSize);
				long sum = queryService.getSum(requirements);
				request.setAttribute("sum", sum);
				request.setAttribute("result", result);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			
			
			else if("queryInspect".equals(operateType))
			{
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/jdj/storage_maintenanc/queryInspect.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String maintainType = request.getParameter("maintainType");
				if(maintainType == null)
					maintainType = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(maintainType);
				requirements.add(deviceNo);
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				infoQueryService = new MaintainInfoQueryService();
				
				ArrayList<HashMap<String, String>> result = infoQueryService.getInspectLogByPage(requirements, pageSize, curPageNum);
				long sum = infoQueryService.countInspectNumber(requirements);
				request.setAttribute("result", result);
				request.setAttribute("sum", sum);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
			else if(operateType.equalsIgnoreCase("saveInspectLog"))
			{
				String jsonStr = request.getParameter("data");
				JSONArray array = JSONArray.fromObject(jsonStr);
				if (array.size() == 0) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("请选择已检查内容！");
					return;
				}
				//为了使代码更易理解，这里先把json传过来的东西存到一个List里面（info），再在后面从List里面取
				List<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
				//专门用一个List来存productId，方便后面的修改
				List<String> productIds = new ArrayList<String>();
				for(int i =0;i<array.size();i++) {
					HashMap<String, String> info = new HashMap<String, String>();
					JSONArray tempArray = array.getJSONArray(i);
					info.put("productId", tempArray.get(0).toString());
					productIds.add(tempArray.get(0).toString());
					info.put("username", tempArray.get(13).toString());
					info.put("remark", tempArray.get(14).toString());
					info.put("maintainType", tempArray.get(15).toString());
					infos.add(info);
				}
				updateMaintainTimeService = new UpdateLatestMaintainTimeAfterMaintainService();
				boolean isOK = true;
				for(int i = 0; i < infos.size(); i++)
				{
					HashMap<String, String> req = infos.get(i);
					String productId = req.get("productId");
					String username = req.get("username");
					String remark = req.get("remark");
					String maintainType = req.get("maintainType");
					if(!UserLogService.SaveInspectLog(productId, username, maintainType, remark))
						isOK = false;
				}
				if(updateMaintainTimeService.updateMaintainTime(productIds) == false)
					isOK = false;
				if (isOK) {
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存成功！");
					return;
				} else {
	
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write("保存失败！");
					return;
				}
			}
			
			else
			{
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/login/login.jsp");
				disp.forward(request, response);
			}
		}if("4".equals(version)){
			if(operateType.equalsIgnoreCase("maintainInfoQuery"))
			{
				List<String> requirements = new ArrayList<String>();
				RequestDispatcher disp = context.getRequestDispatcher("/jsp/zhj/storage_maintenanc/queryMaintain.jsp");
				String productModel = request.getParameter("productModel");
				if(productModel == null)
					productModel = "";
				String productUnit = request.getParameter("productUnit");
				if(productUnit == null)
					productUnit = "";
				String manufacturer = request.getParameter("manufacturer");
				if(manufacturer == null)
					manufacturer = "";
				String maintainType = request.getParameter("maintainType");
				if(maintainType == null)
					maintainType = "";
				String deviceNo = request.getParameter("deviceNo");
				if(deviceNo == null)
					deviceNo = "";
				requirements.add(productModel);
				requirements.add(productUnit);
				requirements.add(manufacturer);
				requirements.add(maintainType);
				requirements.add(deviceNo);
				String curPageNumStr = request.getParameter("curPageNum");
				String pageSizeStr = request.getParameter("pageSize");
				int curPageNum = Integer.parseInt(curPageNumStr);
				int pageSize = Integer.parseInt(pageSizeStr);
				infoQueryService = new MaintainInfoQueryService();
				
				ArrayList<HashMap<String, String>> result = infoQueryService.getMaintainLogByPage(requirements, pageSize, curPageNum);
				long sum = infoQueryService.countResultNumber(requirements);
				request.setAttribute("result", result);
				request.setAttribute("sum", sum);
				request.setAttribute("condition", requirements);
				request.setAttribute("curPageNum", curPageNumStr);
				request.setAttribute("pageSize", pageSizeStr);
				disp.forward(request, response);
				return;
			}
		}
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException,IOException
	{
		doPost(request, response);
	}
	
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException,IOException
	{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		doGet(req, resp);
	}
	
}
