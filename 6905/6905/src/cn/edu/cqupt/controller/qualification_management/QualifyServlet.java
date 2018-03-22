package cn.edu.cqupt.controller.qualification_management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.beans.Qualify;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.qualification_management.QualifyService;
import cn.edu.cqupt.util.CrossPageCheck;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.DownloadFile;
import cn.edu.cqupt.util.GetError;
import cn.edu.cqupt.util.MyDateFormat;

public class QualifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private boolean flag = false;
	// private String message=null;
	private String url = null;
	private String operateType = null;
	private String operate = null;
	private String remark = null;
	private QualifyService qs = null;
	private String searchStr = "";
	private String searchType = "";
	private String year = "";
	private String searchAttr = "";

	public QualifyServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		qs = new QualifyService();
		operate = request.getParameter("operate");

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = (String) sctx.getInitParameter("version");

		// version==1表示是企业版
		if (version.equals("1")) {
			// 判断当前用户是否具有 资质管理 权限
			if (CurrentUser.isQualificationManage(request)) {
				// 这里面写你的主要Servlet的代码
				if (operate.equals("addQualify")) {
					flag = addQualify(request, response);
					url = "/QualifyServlet?operate=all";
				} else if (operate.equals("deleteQualify")) {
					flag = deleteQualify(request, response);
					if (flag) {
						request.setAttribute("deleteFlag", 1);
					} else {
						request.setAttribute("deleteFlag", 0);
					}

					url = "/QualifyServlet?operate=all";
				} else if (operate.equals("updateQualify")) {
					flag = updateQualify(request, response);
					if (flag) {
						request.setAttribute("updateFlag", 1);
					} else {
						request.setAttribute("updateFlag", 0);
					}
					url = "/QualifyServlet?operate=all";
				} else if (operate.equals("searchQualify")) {
					boolean flag = QualifyInfoLikeByPage(request, response);
					// 查询结果为空
					if (!flag)
						request.setAttribute("searchFlag", 0);
				} else if (operate.equals("downloadQualify")) {
					downloadQualify(request, response);
				} else if (operate.equals("exportQualify")) {
					exportQualify(request, response);
				} else if (operate.equals("all")) {
					boolean flag = QualifyInfoByPage(request, response);
					// 查询结果为空
					if (!flag)
						request.setAttribute("searchFlag", 0);
				}

				// 下载文件和导出文件不执行跳转功能
				if (!operate.equals("downloadQualify")
						&& !operate.equals("exportQualify")) {
					ServletContext context = getServletContext();
					RequestDispatcher dispatcher = context
							.getRequestDispatcher(url);
					dispatcher.forward(request, response);
				}
			} else {
				//不是有权限用户
				GetError.limitVisit(request, response);
			}
		} else {
			// 其他版本
			// 先判断是否有权限
			if (CurrentUser.isQualificationManage(request)) {
				// 军代局、军代室、指挥局导入资质文件
				if (operate.equals("importQualify")) {
					String zipPath = getTempZipFilePath(request); // 压缩文件地址
					String desPath = getUnzipFilePath(request); // 解压缩目的地址
					// 解压文件
					if (unpackZip(zipPath, desPath, request)) {
						// 解压成功
						response.getWriter().print("1");
					} else {
						// 解压失败
						response.getWriter().print("0");
					}
				}
				// 军代局、军代室、指挥局查看代储企业有哪些资质文件
				else if (operate.equals("searchQualify")) {
					getQualifyJSONInfoByPage(request, response);
				}
				// 军代局、军代室、指挥局下载资质文件
				else if (operate.equals("downloadQualify")) {
					downloadQualify(request, response);
				}
				// 军代局、军代室、指挥局按条件查看代储企业有哪些资质文件
				else if (operate.equals("searchLikeQualify")) {
					getLikeQualifyJSONInfoByPage(request, response);
				}
				// 军代局、军代室、指挥局查看某一代储企业资质文件有多少个
				else if (operate.equals("getQulifySum")) {
					getQualifySum(request, response);
				}
				// 军代局、军代室、指挥局查看某一代储企业符合条件的资质文件有多少个
				else if (operate.equals("getLikeQulifySum")) {
					getLikeQualifySum(request, response);
				}
			} else {
				//不是有权限用户
				GetError.limitVisit(request, response);
			}
		}

	}

	// 企业添加资质文件
	private boolean addQualify(HttpServletRequest request,
			HttpServletResponse resp) {
		String qualifyType = "";
		String filename = "";
		String qualifyAttr="";

		String tempFilePath = this.getServletContext().getRealPath(
				"/WEB-INF/upload/qualifytemp");
		String filePath = this.getServletContext().getRealPath(
				"/WEB-INF/upload/qualifyFile");
		filePath += File.separator
				+ request.getSession().getAttribute("ownedUnit");

		Qualify qualify = new Qualify();
		// Qualify repeatQualify = new Qualify();

		qs = new QualifyService();

		try {
			// 创建一个基于硬盘的FileItem工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置临时目录
			File tempFile = new File(tempFilePath);
			// 如果临时文件夹不存在，则创建一个新的
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}

			// 设置向硬盘写数据时所用的缓冲区的大小，此处为4K
			factory.setSizeThreshold(4 * 1024);
			factory.setRepository(tempFile);

			// 创建一个文件上传处理器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置允许上传的文件的最大尺寸，此处为100M
			upload.setSizeMax(100 * 1024 * 1024);

			// 监听文件上传
			upload.setProgressListener(new ProgressListener() {
				public void update(long pBytesRead, long pContentLength,
						int arg2) {
					// System.out.println("文件大小为" + pContentLength + ",当前已处理：" +
					// pBytesRead);
				}
			});
			// 解决中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3.判断提交的数据是否合法
			if (!upload.isMultipartContent(request)) {
				// 按照传统方法获取
				return flag;
			}
			// 4.使用解析器上传数据，解析返回的是List<FileItem>,FileItem对应一个form表单
			List /* FileItem */items = upload.parseRequest(request);

			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					// 找出文档类型
					if ("qualifyType".equals(item.getFieldName())) {
						filePath += File.separator + item.getString("UTF-8");
					}
				}
			}
			
			iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					// 找出年份
					if ("year".equals(item.getFieldName())) {
						filePath += File.separator + item.getString("UTF-8");
					}
				}
			}

			iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// 如果封装的是普通项输入数据
				if (item.isFormField()) {
					// 如果是普通项
					if ("qualifyType".equals(item.getFieldName())) {
						qualifyType = item.getString("UTF-8");
						qualify.setQualifyType(qualifyType);
						// repeatQualify.setQualifyType(item.getString("UTF-8"));
					} else if ("qualifyTitle".equals(item.getFieldName())) {
						qualify.setQualifyTitle(item.getString("UTF-8"));
						// repeatQualify.setQualifyTitle(item.getString("UTF-8"));
					} else if ("year".equals(item.getFieldName())) {
						qualify.setYear(item.getString("UTF-8"));
					} else if ("qualifyAttr".equals(item.getFieldName())) {
						qualifyAttr=item.getString("UTF-8");
						qualify.setQualifyAttr(item.getString("UTF-8"));
					}
				} else {
					// 如果封装的上传文件
					filename = item.getName();

					// 截取文件名
					int index = filename.lastIndexOf("\\");
					filename = filename.substring(index + 1, filename.length());
					long fileSize = item.getSize();

					// 没有文件则返回false不让上传
					if (filename.equals("") && fileSize == 0)
						return flag;

					// 设置存储目录
					File realFile = new File(filePath);
					// 如果存储文件夹不存在，则创建一个新的
					if (!realFile.exists()) {
						realFile.mkdirs();
					}

					String realSavePath = filePath + File.separator + filename;

					// 记录文件在数据库的路径
					qualify.setQualifyPath(realSavePath);

					File uploadedFile = new File(realSavePath);
					item.write(uploadedFile);

					// 删除临时文件
					item.delete();
				}
			}
			// 记录资质文件的所属单位
			qualify.setOwnedUnit((String) request.getSession().getAttribute(
					"ownedUnit"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 判断是否重复记录
		// String ownedUnit = (String) request.getSession().getAttribute(
		// "ownedUnit");
		// boolean repeatFlag =
		// qs.repeatQualify(repeatQualify.getQualifyTitle(),
		// repeatQualify.getQualifyType(), ownedUnit);
		//
		// // 用addFlag不用全局的flag是因为有重复记录 时flag会记录为第一次的成功提交
		// boolean addFlag = false;
		// if (!repeatFlag) {

		flag = qs.addQualify(qualify);
		if (flag) {
			request.setAttribute("addFlag", 1);
			operateType = "添加资质文件";
			remark = "文件类型：" + qualifyType + "，文件名：" + filename +"，文件属性："+qualifyAttr;
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username")); // 当前登录的用户名已经保存在session中
			log.setOperateTime(new Date()); // 记录当前用户进行**操作的时间
			log.setOperateType(operateType);
			log.setRemark(remark);
			// log.set** 其他字段根据你的需要再自己添加
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		} else {
			request.setAttribute("addFlag", 0);
		}

		// } else
		// request.setAttribute("repeatFlag", 1);
		return flag;
	}

	// 企业更新资质文件
	private boolean updateQualify(HttpServletRequest request,
			HttpServletResponse resp) {
		String tempFilePath = this.getServletContext().getRealPath(
				"/WEB-INF/temp");
		String filePath = this.getServletContext().getRealPath(
				"/WEB-INF/upload");
		// String filePath = this.getServletContext().getRealPath(
		// "/WEB-INF/upload/qualifyFile");

		qs = new QualifyService();

		long qualifyId = 0;
		String qualifyType = null;
		String qualifyOldTitle = null;
		String qualifyNewTitle = null;

		String filename = null;
		String realSavePath = null;
		String saveFilename = null;

		try {
			// 创建一个基于硬盘的FileItem工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置向硬盘写数据时所用的缓冲区的大小，此处为4K
			factory.setSizeThreshold(4 * 1024);
			// 设置临时目录
			factory.setRepository(new File(tempFilePath));

			// 创建一个文件上传处理器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置允许上传的文件的最大尺寸，此处为10M
			upload.setSizeMax(10 * 1024 * 1024);

			// 监听文件上传
			upload.setProgressListener(new ProgressListener() {
				public void update(long pBytesRead, long pContentLength,
						int arg2) {
				}
			});
			// 解决中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3.判断提交的数据是否合法
			if (!upload.isMultipartContent(request)) {
				// 按照传统方法获取
				return flag;
			}
			// 4.使用解析器上传数据，解析返回的是List<FileItem>,FileItem对应一个form表单
			List /* FileItem */items = upload.parseRequest(request);

			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				// 如果封装的是普通项输入数据
				if (item.isFormField()) {
					// 如果是普通项
					if ("qualifyId".equals(item.getFieldName())) {
						qualifyId = Long.parseLong(item.getString("UTF-8"));
					}
					if ("qualifyType".equals(item.getFieldName())) {
						qualifyType = item.getString("UTF-8");
					}
					if ("qualifyOldTitle".equals(item.getFieldName())) {
						qualifyOldTitle = item.getString("UTF-8");
					}
					if ("qualifyNewTitle".equals(item.getFieldName())) {
						qualifyNewTitle = item.getString("UTF-8");
					}
				} else {
					// 如果封装的上传文件
					filename = item.getName();
					// 不修改文件就跳过
					if (filename.equals("")) {
						continue;
					} else {
						// 截取文件名,存到数据库
						int index = filename.lastIndexOf("\\");
						filename = filename.substring(index + 1,
								filename.length());
						long fileSize = item.getSize();

						// 没有文件则返回false不让上传
						if (qualifyNewTitle.equals("") && fileSize == 0)
							return flag;

						// 使用UUID防止文件重名被覆盖
						saveFilename = makeFileName(qualifyNewTitle);
						// 经过Hash打散后的文件真实存储路径
						// realSavePath = makePath(saveFilename, filePath);
						// realSavePath = realSavePath + "\\" + saveFilename;
						realSavePath = filePath + "\\" + saveFilename;

						File uploadedFile = new File(realSavePath);
						item.write(uploadedFile);

						// 删除临时文件
						item.delete();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String oldFilePath = qs.getQualifyPathById(qualifyId);
		// 修改了具体的文件
		if (!filename.equals("")) {
			// System.out.println("----------");
			File file1 = new File(oldFilePath);
			/* 如果文件存在 */
			if (file1.exists()) {
				/* 删除旧文件 */
				qs.deleteFile(oldFilePath);
			}
			flag = qs.updateQualify(qualifyId, qualifyType, realSavePath,
					qualifyNewTitle);
		}
		// 没修改具体文件
		else {
			flag = qs.updateQualify(qualifyId, qualifyType, oldFilePath,
					qualifyNewTitle);
		}
		// 更新成功
		if (flag) {
			operateType = "修改资质文件";
			remark = "文件类型：" + qualifyType + "，原文件名：" + qualifyOldTitle
					+ "，新文件名：" + qualifyNewTitle;
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username")); // 当前登录的用户名已经保存在session中
			log.setOperateTime(new Date()); // 记录当前用户进行**操作的时间
			log.setOperateType(operateType);
			log.setRemark(remark);
			// log.set** 其他字段根据你的需要再自己添加
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		}
		return flag;
	}

	// 下载资质文件
	private void downloadQualify(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		OutputStream out = null;
		InputStream in = null;

		long qualifyId = Long.parseLong(request.getParameter("qualifyId"));

		if (qualifyId == 0) {
			response.getWriter().write("errorInput");
			return;
		}

		/* 根据ID获取文件路径 */
		String qualifyPath = new QualifyService().getQualifyPathById(qualifyId);

		/* 根据qualifyPath得到qualifyTitle */
		qualifyPath = qualifyPath.trim();
		String qualifyTitle = qualifyPath.substring(qualifyPath
				.lastIndexOf("\\") + 1);
		String logQualifyTitle = qualifyTitle;

		/* 读取文件 */
		File file = new File(qualifyPath);

		/* 如果文件存在 */
		if (file.exists()) {
			/* 创建输入流 */
			in = new FileInputStream(file);
			int length = in.available();

			qualifyTitle = DownloadFile
					.getNormalFilename(request, qualifyTitle);
			response.setContentType("application/force-download");
			response.setHeader("Content-Length", String.valueOf(length));
			response.setHeader("Content-Disposition", "attachment;filename="
					+ qualifyTitle);

			out = response.getOutputStream();
			int bytesRead = 0;
			byte[] buffer = new byte[512];
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
		in.close();
		out.close();

		Log log = new Log();
		log.setUserName((String) request.getSession().getAttribute("username")); 
		log.setOperateTime(new Date()); // 记录当前用户进行操作的时间
		log.setOperateType("下载资质文件");
		log.setRemark("文件名：" + logQualifyTitle);
		// log.set** 其他字段根据你的需要再自己添加
		UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
	}

	// 同步获取当前页的资质文件信息
	private boolean QualifyInfoByPage(HttpServletRequest request,
			HttpServletResponse response) {
		qs = new QualifyService();

		String ownedUnit = (String) request.getSession().getAttribute(
				"ownedUnit");
		// 获取全部查询数据的个数
		long qualifySum = qs.getQualifySum(ownedUnit);
		request.setAttribute("qualifySum", qualifySum);
		// 获得前台的传来的当前页码和按多少条分页的参数，获取当前页的数据
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));

		// 如果是经过添加或删除后再进行查找的话,需要重新计算分多少页
		int addFlag = -1;
		int deleteFlag = -1;
		if (request.getAttribute("addFlag") != null)
			addFlag = (Integer) request.getAttribute("addFlag");
		if (request.getAttribute("deleteFlag") != null)
			deleteFlag = (Integer) request.getAttribute("deleteFlag");
		// 添加一条记录后跳转到最后一页显示
		if (addFlag == 1)
			curPageNum = (int) (qualifySum % pageSize == 0 ? qualifySum
					/ pageSize : (qualifySum / pageSize + 1));
		// 删除一条记录时，如果是删的最后一条记录，则当前上一页为最后一页
		if (deleteFlag == 1) {
			int tempPageNum = (int) (qualifySum % pageSize == 0 ? qualifySum
					/ pageSize : (qualifySum / pageSize + 1));
			if (tempPageNum == curPageNum - 1)
				curPageNum = tempPageNum;
		}

		ArrayList<Qualify> curQualifyList = qs.searchQualifyByPage(curPageNum,
				pageSize, ownedUnit);
		request.setAttribute("curQualifyList", curQualifyList);

		// 取出当前页的id，存成long[]
		long[] curPageIdArray = new long[curQualifyList.size()];
		for (int i = 0; i < curQualifyList.size(); i++) {
			curPageIdArray[i] = curQualifyList.get(i).getQualifyId();
		}

		String checkedIdStr = CrossPageCheck.getCheckedIdStr(request,
				curPageIdArray);

		request.setAttribute("checkedIdStr", checkedIdStr);

		operateType = "";
		if (curQualifyList.size() != 0) {
			flag = true;
		} else {
			flag = false;
		}
		url = "/jsp/qy/qualification_management/qualifyinfo.jsp?curPageNum="
				+ curPageNum + "&pageSize=" + pageSize + "&operate=" + operate;
		return flag;
	}

	// 异步获取当前页的资质文件信息
	private void getQualifyJSONInfoByPage(HttpServletRequest request,
			HttpServletResponse response) {
		qs = new QualifyService();
		// 获得前台的传来的当前页码和按多少条分页的参数，获取当前页的数据
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String ownedUnit = request.getParameter("ownedUnit");

		ArrayList<Qualify> curQualifyList = qs.searchQualifyByPage(curPageNum,
				pageSize, ownedUnit);
		// list转成JSON
		JSONArray jsonArray = new JSONArray();
		for (Qualify qualify : curQualifyList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("qualifyId", qualify.getQualifyId());
			jsonObject.put("qualifyTitle", qualify.getQualifyTitle());
			jsonObject.put("qualifyType", qualify.getQualifyType());
			jsonArray.add(jsonObject);
		}
		try {
			response.getWriter().write(jsonArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 异步获取当前代储企业资质文件个数
	private void getQualifySum(HttpServletRequest request,
			HttpServletResponse response) {
		qs = new QualifyService();
		String ownedUnit = request.getParameter("ownedUnit");
		long sum = qs.getQualifySum(ownedUnit);
		try {
			response.getWriter().write(Long.toString(sum));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 同步 模糊查询时当前页的资质文件信息
	private boolean QualifyInfoLikeByPage(HttpServletRequest request,
			HttpServletResponse response) {
		qs = new QualifyService();

		ArrayList<Qualify> curQualifyList = new ArrayList<Qualify>();
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		searchStr = request.getParameter("searchStr");
		searchType = request.getParameter("searchType");
		year = request.getParameter("year");
		searchAttr = request.getParameter("searchAttr");
		String ownedUnit = (String) request.getSession().getAttribute(
				"ownedUnit");

		long qualifySum = qs.getPartQualifySum(searchStr, searchType, year,
				searchAttr, ownedUnit);
		request.setAttribute("qualifySum", qualifySum);
		curQualifyList = qs.getPartQualifyByPage(searchStr, searchType, year,
				searchAttr, curPageNum, pageSize, ownedUnit);
		request.setAttribute("curQualifyList", curQualifyList);

		// // 按标题和类型查询
		// if (!searchStr.equals("请输入查询的文件名") && !searchType.equals("所有类型")) {
		// long qualifySum = qs.getPartQualifySum(searchStr, searchType,
		// ownedUnit);
		// request.setAttribute("qualifySum", qualifySum);
		// curQualifyList = qs.getPartQualifyByPage(searchStr, searchType,
		// curPageNum, pageSize, ownedUnit);
		// request.setAttribute("curQualifyList", curQualifyList);
		// }
		//
		// // 只按类型查询
		// else if (!searchType.equals("所有类型")) {
		// long qualifySum = qs.getTypeQualifySum(searchType, ownedUnit);
		// request.setAttribute("qualifySum", qualifySum);
		// curQualifyList = qs.getTypeQualifyByPage(searchType, curPageNum,
		// pageSize, ownedUnit);
		// request.setAttribute("curQualifyList", curQualifyList);
		// }
		//
		// // 只按标题查询
		// else if (!searchStr.equals("请输入查询的文件名")) {
		// long qualifySum = qs.getTitleQualifySum(searchStr, ownedUnit);
		// request.setAttribute("qualifySum", qualifySum);
		// curQualifyList = qs.getTitleQualifyByPage(searchStr, curPageNum,
		// pageSize, ownedUnit);
		// request.setAttribute("curQualifyList", curQualifyList);
		// }
		//
		// // 全部查询
		// else {
		// operate = "all";
		// QualifyInfoByPage(request, response);
		// return true;
		// }

		if (curQualifyList.size() != 0) {
			url = "/jsp/qy/qualification_management/qualifyinfo.jsp?curPageNum="
					+ curPageNum
					+ "&pageSize="
					+ pageSize
					+ "&operate="
					+ operate;
			return true;
		} else {
			return false;
		}
	}

	// 异步 模糊查询时当前页的资质文件信息
	private void getLikeQualifyJSONInfoByPage(HttpServletRequest request,
			HttpServletResponse response) {
		qs = new QualifyService();
		ArrayList<Qualify> curQualifyList = new ArrayList<Qualify>();
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		searchStr = request.getParameter("searchStr");
		searchType = request.getParameter("searchType");
		year = request.getParameter("year");
		searchAttr = "上报";
		String ownedUnit = request.getParameter("ownedUnit");
		curQualifyList = qs.getPartQualifyByPage(searchStr, searchType, year,
				searchAttr, curPageNum, pageSize, ownedUnit);

		// // 按标题和类型查询
		// if (!searchStr.equals("请输入查询的文件名") && !searchType.equals("所有类型")) {
		// curQualifyList = qs.getPartQualifyByPage(searchStr, searchType,
		// curPageNum, pageSize, ownedUnit);
		// }
		// // 只按类型查询
		// else if (!searchType.equals("所有类型")) {
		// curQualifyList = qs.getTypeQualifyByPage(searchType, curPageNum,
		// pageSize, ownedUnit);
		// }
		// // 只按标题查询
		// else if (!searchStr.equals("请输入查询的文件名")) {
		// curQualifyList = qs.getTitleQualifyByPage(searchStr, curPageNum,
		// pageSize, ownedUnit);
		// }
		// // 全部查询
		// else {
		// getQualifyJSONInfoByPage(request, response);
		// return;
		// }
		// list转成JSON
		JSONArray jsonArray = new JSONArray();
		for (Qualify qualify : curQualifyList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("qualifyId", qualify.getQualifyId());
			jsonObject.put("qualifyTitle", qualify.getQualifyTitle());
			jsonObject.put("qualifyType", qualify.getQualifyType());
			jsonObject.put("year", qualify.getYear());
			jsonArray.add(jsonObject);
		}
		try {
			response.getWriter().write(jsonArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 异步 模糊查询时当前代储企业资质文件个数
	private void getLikeQualifySum(HttpServletRequest request,
			HttpServletResponse response) {
		qs = new QualifyService();
		String ownedUnit = request.getParameter("ownedUnit");
		searchStr = request.getParameter("searchStr");
		searchType = request.getParameter("searchType");
		year = request.getParameter("year");
		searchAttr = "上报";
		long sum = 0;
		// 按标题和类型查询
		// if (!searchStr.equals("请输入查询的文件名") && !searchType.equals("所有类型")) {
		sum = qs.getPartQualifySum(searchStr, searchType, year, searchAttr,
				ownedUnit);
		// }
		// // 只按类型查询
		// else if (!searchType.equals("所有类型")) {
		// sum = qs.getTypeQualifySum(searchType, ownedUnit);
		// }
		// // 只按标题查询
		// else if (!searchStr.equals("请输入查询的文件名")) {
		// sum = qs.getTitleQualifySum(searchStr, ownedUnit);
		// }
		// // 全部查询
		// else {
		// sum = qs.getQualifySum(ownedUnit);
		// }
		try {
			response.getWriter().write(Long.toString(sum));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除资质文件
	private boolean deleteQualify(HttpServletRequest request,
			HttpServletResponse response) {
		qs = new QualifyService();
		long qualifyId = Long.parseLong(request.getParameter("qualifyId")
				.trim());

		/* 根据ID获取文件路径和文件名 */
		Qualify curQualify = qs.getCurQualifyById(qualifyId);
		String qualifyPath = curQualify.getQualifyPath();
		String qualifyType = curQualify.getQualifyType();
		String qualifyTitle = curQualify.getQualifyTitle();
		String qualifyAttr=curQualify.getQualifyAttr();

		if (qs.deleteQualify(qualifyId) && qs.deleteFile(qualifyPath)) {
			flag = true;
		}
		if (flag) {
			operateType = "删除资质文件";
			remark = "文件类型：" + qualifyType + "，文件名：" + qualifyTitle+"，文件属性："+qualifyAttr;
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username")); // 当前登录的用户名已经保存在session中
			log.setOperateTime(new Date()); // 记录当前用户进行**操作的时间
			log.setOperateType(operateType);
			log.setRemark(remark);
			// log.set** 其他字段根据你的需要再自己添加
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		}
		return flag;
	}

	// 企业导出资质文件
	private void exportQualify(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String tempFilePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "/WEB-INF/upload/qualifytemp";
		// qualifyFilePath 当前企业全部资质文件在服务器上的路径
		String ownedUnit=(String)request.getSession().getAttribute("ownedUnit");
		String qualifyFilePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ "/WEB-INF/upload/qualifyFile"
				+ File.separator
				+ ownedUnit;

		String chooseIds=request.getParameter("checkedIdStr");
		String[] chooseIdsArray=chooseIds.split(",");
		String[] choosePathesArray=new String[chooseIdsArray.length];
		QualifyService qs=new QualifyService();
		for (int i = 0; i < choosePathesArray.length; i++) {
			choosePathesArray[i]=qs.getQualifyPathById(Long.parseLong(chooseIdsArray[i]));
			int tempIndex=choosePathesArray[i].lastIndexOf(ownedUnit)+ownedUnit.length();
			choosePathesArray[i]=choosePathesArray[i].substring(tempIndex+1, choosePathesArray[i].length());
		}
		
		
		// 压缩后文件的存放文件夹
		String zipFilePath = tempFilePath;

		// 临时存放zip的文件夹 没有则创建
		File tempZipFilePath = new File(zipFilePath);
		if (!tempZipFilePath.exists())
			tempZipFilePath.mkdirs();

		// 压缩文件的暂时的文件名
		String name = "临时压缩资质文件包"
				+ MyDateFormat.changeDateToFileString(new Date()) + ".zip";
		// 压缩后的文件路径+文件名形成绝对路径
		zipFilePath = zipFilePath + File.separator + name;

		// 打包文件
		this.compressByAnt(zipFilePath, qualifyFilePath,choosePathesArray);

		// 下载压缩文件的文件名
		String oldDownloadFileName = ownedUnit
				+ "资质文件"
				+ MyDateFormat.changeDateToFileString(new Date())
				+ ".zip";

		// 导出的文件的名字显示中文
		String downloadFileName = DownloadFile.getNormalFilename(request,
				oldDownloadFileName);

		// 设置响应头，控制浏览器下载文件
		DownloadFile.launchDownloadStream(response, zipFilePath,
				downloadFileName);

		// 记录导出资质文件用户操作到日志
		operateType = "导出资质文件压缩包";
		remark = "压缩包名：" + oldDownloadFileName;
		Log log = new Log();
		log.setUserName((String) request.getSession().getAttribute("username")); 
		log.setOperateTime(new Date()); // 记录当前用户进行操作的时间
		log.setOperateType(operateType);
		log.setRemark(remark);
		// log.set** 其他字段根据你的需要再自己添加
		UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库

		// 下载后删除放在qualifytemp文件夹下的临时压缩文件
		File tempZipFile = new File(zipFilePath);
		tempZipFile.delete();
	}

	/**
	 * 通过ant包提供的方法压缩文件
	 * 
	 * @param zipPathName
	 *            压缩zip的目的地址
	 * @param srcPathName
	 *            压缩zip的源文件地址
	 * @param choosePathesArray 选择的资质文件路径
	 * 				        
	 */
	private void compressByAnt(String zipPathName, String srcPathName,String[] choosePathesArray) {
		File zipFile = new File(zipPathName);
		File srcdir = new File(srcPathName);
		if (!srcdir.exists())
			throw new RuntimeException(srcPathName + "不存在！");

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		// 包括哪些文件或文件夹
		//fileSet.setIncludes("代储合同/2015/Git教程By廖雪峰.pdf"); 
		for (int i = 0; i < choosePathesArray.length; i++) {
			fileSet.setIncludes(choosePathesArray[i]); 
		}
		//fileSet.setExcludes(""); //排除哪些文件或文件夹
		zip.addFileset(fileSet);

		zip.execute();
	}

	/**
	 * 压缩包解压
	 * 
	 * @param srcPath
	 *            源压缩包路径
	 * @param desPath
	 *            解压缩包路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean unpackZip(String srcPath, String desPath,
			HttpServletRequest request) {
		boolean flag = false;
		String ownedUnit = request.getParameter("departmentName");
		File file = null;
		try {
			// 获取待解压的压缩包
			file = new File(srcPath);
			// 实例化ZipFile，并指定其编码格式，防止中文乱码和解压错误。每一个zip压缩文件都可以表示为一个ZipFile。
			ZipFile zipFile = new ZipFile(file, "gbk");

			// 获取压缩包里文件集合
			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile
					.getEntries();
			while (entries.hasMoreElements()) {
				// 获取压缩包里每一个文件
				ZipEntry zipEntry = entries.nextElement();
				String fileName = zipEntry.getName();

				// 获取或创建目标文件夹
				File temp = new File(desPath + fileName);
				// 具体读文件里面的文件或文件夹顺序不定,通过最后一位来判定
				String lastIndex = fileName.substring(fileName.length() - 1,
						fileName.length());
				// 如果是文件夹
				if (lastIndex.equals("/")) {
					// fileName去除最后一个反斜线
					fileName = fileName.substring(0, fileName.length() - 1);
					temp = new File(fileName);
					if (!temp.exists())
						temp.mkdirs();
				}
				// 如果是文件
				else {
					if (!temp.getParentFile().exists())
						temp.getParentFile().mkdirs();
					// 开流读写文件
					OutputStream os = new FileOutputStream(temp);
					// 通过ZipFile的getInputStream方法拿到具体的ZipEntry的输入流
					InputStream is = zipFile.getInputStream(zipEntry);
					int len = 0;
					byte[] b = new byte[256]; // 自己定义读取的快慢
					while ((len = is.read(b)) != -1)
						os.write(b, 0, len);
					os.close();
					is.close();

					// 获取资质文件的类型
					String tempPath = temp.getParentFile().getParentFile().getPath();
					int start = tempPath.lastIndexOf("\\") + 1;
					String qualifyType = tempPath.substring(start,
							tempPath.length());
					
					// 获取资质文件的年份
					tempPath = temp.getParentFile().getPath();
					start = tempPath.lastIndexOf("\\") + 1;
					String year = tempPath.substring(start,
							tempPath.length());

					// 获取资质文件路径和标题
					String qualifyPath = temp.getPath();
					start = qualifyPath.lastIndexOf("\\") + 1;
					int end = qualifyPath.lastIndexOf(".");
					String qualifyTitle = qualifyPath.substring(start, end);

					// get请求，重新编码
					// String ownedUnit = new
					// String(request.getParameter("departmentName").getBytes("iso-8859-1"),"UTF-8");

					// System.out.println("qualifyType:" + qualifyType
					// + "\tqualifyPath:" + qualifyPath + "\tqualifyTitle:"
					// + qualifyTitle + "\townedUnit:" + ownedUnit);

					// 导入的资质文件记录进入数据库
					Qualify qualify = new Qualify();
					qualify.setQualifyTitle(qualifyTitle);
					qualify.setQualifyPath(qualifyPath);
					qualify.setQualifyType(qualifyType);
					qualify.setYear(year);
					qualify.setQualifyAttr("上报");
					qualify.setOwnedUnit(ownedUnit);
					qs.addQualify(qualify);
				}
			}
			flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (flag) {
			// 记录用户导入资质文件操作到日志
			operateType = "导入资质文件压缩包";
			remark = "导入了" + ownedUnit + "的资质文件压缩包，压缩包文件名：" + file.getName();
			Log log = new Log();
			log.setUserName((String) request.getSession().getAttribute(
					"username")); // 当前登录的用户名已经保存在session中
			log.setOperateTime(new Date()); // 记录当前用户进行**操作的时间
			log.setOperateType(operateType);
			log.setRemark(remark);
			// log.set** 其他字段根据你的需要再自己添加
			UserLogService.SaveOperateLog(log); // 记录当前用户操作到数据库
		}
		return flag;
	}

	/**
	 * 删除文件夹下的所有文件和目录
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 * @author LiangYH&LiuHS
	 */
	public void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到临时在服务器上的压缩包的地址
	 * 
	 * @param request
	 * @return
	 */
	private String getTempZipFilePath(HttpServletRequest request) {
		String path = "";
		String tempFilePath = this.getServletContext().getRealPath(
				"/WEB-INF/upload/qualifytemp");
		String realFilePath = this.getServletContext().getRealPath(
				"/WEB-INF/upload/qualifyZip");

		// 判断文件夹是否存在，不存在则建立
		File tempFile = new File(tempFilePath);
		File realFile = new File(realFilePath);
		if (!tempFile.exists())
			tempFile.mkdirs();
		if (!realFile.exists())
			realFile.mkdirs();

		// 先删除qualifyZip文件夹下的文件
		delAllFile(realFilePath);

		// 删除成功后把新的zip文件放入qualifyZip文件夹
		// 获得磁盘文件条目工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
		factory.setRepository(new File(tempFilePath));
		// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(4 * 1024);

		// API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// 提交上来的信息都在这个list里面
			// 可以上传多个文件
			List<FileItem> list = upload.parseRequest(request);
			// 获取上传的文件
			FileItem item = getUploadFileItem(list);
			// 获取文件名
			String filename = getUploadFileName(item);
			
			//处理IE给全路径
			if(filename.lastIndexOf("\\")!=-1){
				filename=filename.substring(filename.lastIndexOf("\\")+1, filename.length());
			}

			// System.out.println("存放目录:" + realFilePath);
			// System.out.println("文件名:" + filename);

			// 写到磁盘上
			item.write(new File(realFilePath, filename));

			path = realFilePath + File.separator + filename;
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return path;
	}

	/**
	 * 获取解压压缩包后的文件夹地址
	 * 
	 * @param request
	 * @return
	 */
	private String getUnzipFilePath(HttpServletRequest request) {
		String path = "";

		String ownedUnit = request.getParameter("departmentName");
		// 因为是get请求，这里需要指定编码
		// try {
		// String ownedUnit = new
		// String(request.getParameter("departmentName").getBytes("iso-8859-1"),"UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		path = this.getServletContext().getRealPath(
				"/WEB-INF/upload/qualifyFile")
				+ File.separator + ownedUnit + File.separator;
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
		return path;
	}

	/**
	 * 获取从前台form表单传来的mutipart/form-data集合中的文件集
	 * 
	 * @param list
	 * @return
	 */
	private FileItem getUploadFileItem(List<FileItem> list) {
		for (FileItem fileItem : list) {
			if (!fileItem.isFormField()) {
				return fileItem;
			}
		}
		return null;
	}

	/**
	 * 通过fileItem获取文件名
	 * 
	 * @param item
	 * @return
	 */
	private String getUploadFileName(FileItem item) {
		// 获取路径名
		String value = item.getName();
		// 索引到最后一个反斜杠
		int start = value.lastIndexOf("/");
		// 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
		String filename = value.substring(start + 1);

		return filename;
	}

	/**
	 * 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
	 * 
	 * @param filename
	 *            文件的原始名称
	 * @return uuid+"_"+文件的原始名称
	 */
	private String makeFileName(String filename) {
		// 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
		return UUID.randomUUID().toString() + "_" + filename;
	}

	/**
	 * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
	 * 
	 * @param filename
	 *            文件名，要根据文件名生成存储目录
	 * @param savePath
	 *            文件存储路径
	 * @return 新的存储目录
	 */
	private String makePath(String filename, String savePath) {
		// 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		int dir1 = hashcode & 0xf; // 0--15
		int dir2 = (hashcode & 0xf0) >> 4; // 0-15
		// 构造新的保存目录
		String dir = savePath + "\\" + dir1 + "\\" + dir2; // upload\2\3
		// File既可以代表文件也可以代表目录
		File file = new File(dir);
		// 如果目录不存在
		if (!file.exists()) {
			// 创建目录
			file.mkdirs();
		}
		return dir;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}