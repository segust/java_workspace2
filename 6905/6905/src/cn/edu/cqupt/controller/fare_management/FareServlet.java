package cn.edu.cqupt.controller.fare_management;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import cn.edu.cqupt.beans.Attach;
import cn.edu.cqupt.beans.Fare;
import cn.edu.cqupt.beans.FareDetail;
import cn.edu.cqupt.beans.Log;
import cn.edu.cqupt.log.UserLogService;
import cn.edu.cqupt.service.fare_management.AttachService;
import cn.edu.cqupt.service.fare_management.FareDetailService;
import cn.edu.cqupt.service.fare_management.FareService;
import cn.edu.cqupt.util.CurrentUser;
import cn.edu.cqupt.util.DownloadFile;
import cn.edu.cqupt.util.GetError;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

/**
 * 经费管理
 * 
 * @author lsy&yg&lhs
 * 
 */
public class FareServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	// curFolder表示根据版本的不同，进入网页所在的文件夹不同
	private String curFolder = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取当前版本（全局变量）
		ServletContext sctx = this.getServletContext();
		String version = sctx.getInitParameter("version");
		CurrentUser.isFareManage(request);// 经费管理权限
		// version==1表示是企业版
		if (version.equals("1")) {
			if (CurrentUser.isFareManage(request)) {
				curFolder = "qy";
				try {
					forwardByoperation(request, response);
				} catch (FileUploadException e) {
					e.printStackTrace();
				}
			} else {
				// 当前用户没有权限
				GetError.limitVisit(request, response);
			}
		}
		// version==2表示是军代室版
		else if (version.equals("2")) {
			if (CurrentUser.isFareManage(request)) {
				curFolder = "jds";
				try {
					forwardByoperation(request, response);
				} catch (FileUploadException e) {
					e.printStackTrace();
				}
			} else {
				// 当前用户没有权限
				GetError.limitVisit(request, response);
			}
		}
		// version==3表示是军代局版
		else if (version.equals("3")) {
			if (CurrentUser.isFareManage(request)) {
				curFolder = "jdj";
				try {
					forwardByoperation(request, response);
				} catch (FileUploadException e) {
					e.printStackTrace();
				}
			} else {
				// 当前用户没有权限
				GetError.limitVisit(request, response);
			}
		}
		// version==4表示是指挥局版
		else if (version.equals("4")) {
			if (CurrentUser.isFareManage(request)) {
				curFolder = "zhj";
				try {
					forwardByoperation(request, response);
				} catch (FileUploadException e) {
					e.printStackTrace();
				}
			} else {
				// 当前用户没有权限
				GetError.limitVisit(request, response);
			}
		}
	}

	// 获取用户的操作函数，决定进入那一个页面
	private void forwardByoperation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			FileUploadException {
		String operate = request.getParameter("operate");
		if ("check".equals(operate)) {
			getFareByDateAndFareType(request, response);
			return;
		} else if ("statistics".equals(operate)) {// 用来统计经费记录的方法
			getSumByDateAndFareType(request, response);
			return;
		} else if ("statisticsDetail".equals(operate)) {// 用来统计明细的方法
			getDetailByDateAndFareType(request, response);
			return;
		} else if ("add".equals(operate)) {
			addFare(request, response);
			return;
		} else if ("delete".equals(operate)) {
			deleteFare(request, response);
			return;
		} else if ("edit".equals(operate)) {
			updateFare(request, response);
			return;
		} else if ("attach".equals(operate)) {
			getAllAttachInAFare(request, response);
			return;
		} else if ("download".equals(operate)) {
			downloadAttach(request, response);
			return;
		} else if ("upload".equals(operate)) {
			uploadAttach(request, response);
			return;
		} else if ("deleteAttach".equals(operate)) {
			deleteAttach(request, response);
			return;
		} else if ("exportStatisticsFare".equals(operate)) {
			exportStatisticsFare(request, response);
			return;
		} else if ("export".equals(operate)) {
			exportExcel(request, response);
			return;
		} else if ("import".equals(operate)) {
			importExcel(request, response);
			return;
		}
		getFareByDateAndFareType(request, response);
	}

	/**
	 * 按选择条件统计记录的经费金额
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getSumByDateAndFareType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		FareService fareService = new FareService();
		double total = 0;
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		BigDecimal b2 = new BigDecimal(Double.toString(total));
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		String[] types = request.getParameterValues("fareType");

		if (request.getParameterValues("fareType") == null) {
			// types = new String[5];
			// types[0] = "器材购置费";
			types = new String[4];
			types[0] = "运杂费";
			types[1] = "轮换费";
			types[2] = "维护保养费";
			types[3] = "其他";
		}

		LinkedHashMap<String, Double> sumMap = new LinkedHashMap<String, Double>();
		String typesStr = " ";
		if (types != null && !types.equals("")) {// 如果没勾选type则type=null
			for (int i = 0; i < types.length; i++) { // 得到所勾选的
				String type = types[i];// 其中的一个type
				double sum = fareService.getSumByDateAndOneFaretype(startDate,
						endDate, type);
				BigDecimal b1 = new BigDecimal(Double.toString(sum));
				sumMap.put(type, sum);
				b2 = b2.add(b1);
				typesStr += type + "  ,  ";
			}
			typesStr = typesStr + "]";
			typesStr = typesStr.replace(",  ]", "  ");
		} else {
			double sum = fareService.getSumByDateAndOneFaretype(startDate,
					endDate, "");
			BigDecimal b1 = new BigDecimal(Double.toString(sum));
			b2 = b2.add(b1);
			typesStr = "无";
		}
		// 获取查询条件下的经费数目
		sumMap.put("合计", b2.doubleValue());
		request.setAttribute("startTime", startDate);
		request.setAttribute("endTime", endDate);
		request.setAttribute("sumMap", sumMap);
		request.setAttribute("typesStr", typesStr);
		long f = 3;
		request.setAttribute("fareSum", f);
		String path = "jsp/" + curFolder
				+ "/fare_management/statisticsFare.jsp?curPageNum="
				+ curPageNum + "&pageSize=" + pageSize;
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 按选择条件统计明细 这个方法暂时没用了
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	// private void getDetailByDateAndFareType11(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException {
	// FareService fs = new FareService();
	// FareDetailService fds = new FareDetailService();
	// int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
	// int pageSize = Integer.parseInt(request.getParameter("pageSize"));
	// String startDate = request.getParameter("startTime");
	// String endDate = request.getParameter("endTime");
	// String type = request.getParameter("builtType");
	// String storeCompany = "";
	// String[] types = request.getParameterValues("fareType");
	// if (type == "") {
	// if (types != null && !types.equals("")) {// 如果没勾选type则type=null
	// for (int i = 0; i < types.length; i++) { // 得到所勾选的
	// String s = types[i];
	// if (s != null) {
	// StringBuilder stringBuilder = new StringBuilder();// 拼接字符串
	// stringBuilder.append("'");
	// stringBuilder.append(s);
	// if (i != types.length - 1) {
	// stringBuilder.append("',");
	// } else {
	// stringBuilder.append("'");
	// }
	// type = type + stringBuilder.toString();
	// }
	// }
	// }
	// }
	// System.out.println("统计查询的type=" + type);
	// ArrayList<Long> checkFare = new ArrayList();// 存放的是所有的fareId
	// ArrayList<FareDetail> allDetail = new ArrayList();// 存放的是总的detail
	// ArrayList<FareDetail> alDetail = new ArrayList();// 存放的是单个的fareId的detail
	// checkFare = fs.getAllFareByDateAndFareType(startDate, endDate, type,
	// storeCompany);
	// for (int i = 0; i < checkFare.size(); i++) {
	// // System.out.println("checkFare.get(i)="+checkFare.get(i));
	// alDetail = fds.getAllFareDetail(checkFare.get(i)); // 应该是ArrayList
	// // 里面的ArrayLIst
	// for (int h = 0; h < alDetail.size(); h++) {
	// allDetail.add(alDetail.get(h));
	// }
	// }
	// // 1. 去重
	// HashSet hs = new HashSet();
	// for (int k = 0; k < allDetail.size(); k++) {
	// // System.out.println("name=="+allDetail.get(k).getDetailName());
	// hs.add(allDetail.get(k).getDetailName());// 拍完序之后加到map上
	// }
	// Iterator it1 = hs.iterator();
	// String s = "";
	// int i = hs.size();
	// int ite = 0;
	// String[] arr = new String[i];
	// while (it1.hasNext()) {
	// s = (String) it1.next();
	// arr[ite] = s;
	// ite++;
	// }
	// // 2.排序
	// Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
	// Arrays.sort(arr, cmp);
	// for (int c = 0; c < arr.length; c++) {
	// System.out.println("排序之后=" + arr[c]);
	// }
	// // 3. 计算明细的金额
	// LinkedHashMap<String, Double> sumMap = new LinkedHashMap<String,
	// Double>();
	// double total = 0;
	// BigDecimal b2 = new BigDecimal(Double.toString(total));
	// for (int p = 0; p < arr.length; p++) {
	// for (int d = 0; d < allDetail.size(); d++) {
	// if (allDetail.get(d).getDetailName().equals(arr[p])) {// 得到相同的就把他们的金额相加
	// double sum = allDetail.get(d).getDetailAmount();
	// BigDecimal b1 = new BigDecimal(Double.toString(sum));
	// b2 = b2.add(b1);
	// }
	//
	// }
	// sumMap.put(arr[p], b2.doubleValue());
	// }
	// request.setAttribute("startTime", startDate);
	// request.setAttribute("endTime", endDate);
	// request.setAttribute("sumMap", sumMap);
	// long f = 3;
	// request.setAttribute("fareSum", f);
	// String path = "jsp/" + curFolder
	// + "/fare_management/statisticsDetail.jsp?curPageNum="
	// + curPageNum + "&pageSize=" + pageSize;
	// request.getRequestDispatcher(path).forward(request, response);
	//
	// }

	/**
	 * 按选择条件统计明细
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getDetailByDateAndFareType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		FareService fs = new FareService();
		FareDetailService fds = new FareDetailService();
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		String type = request.getParameter("builtType");
		String storeCompany = "";
		String[] types = request.getParameterValues("fareType");
		if (type == "") {
			if (types != null && !types.equals("")) {// 如果没勾选type则type=null
				for (int i = 0; i < types.length; i++) { // 得到所勾选的
					String s = types[i];
					if (s != null) {
						StringBuilder stringBuilder = new StringBuilder();// 拼接字符串
						stringBuilder.append("'");
						stringBuilder.append(s);
						if (i != types.length - 1) {
							stringBuilder.append("',");
						} else {
							stringBuilder.append("'");
						}
						type = type + stringBuilder.toString();
					}
				}
			} else {
				// type = "'器材购置费','运杂费','轮换费','维护保养费','其他'";
				type = "'运杂费','轮换费','维护保养费','其他'";
			}
		}
		// System.out.println("统计查询的type=" + type);

		ArrayList<Long> checkFare = new ArrayList<Long>();// 存放的是所有的fareId
		// ArrayList<FareDetail> allDetail = new ArrayList<FareDetail>();//
		// 存放的是总的detail
		// ArrayList<FareDetail> alDetail = new ArrayList<FareDetail>();//
		// 存放的是单个的fareId的detail

		checkFare = fs.getAllFareByDateAndFareType(startDate, endDate, type,
				storeCompany);
		// System.out.println("checkFare=" + checkFare);
		HashMap<String, Double> sumMap = new HashMap<String, Double>();

		sumMap = fds.getStatisticsDetail(type, startDate, endDate);
		// System.out.println("sum=" + sumMap);
		request.setAttribute("startTime", startDate);
		request.setAttribute("endTime", endDate);
		request.setAttribute("sumMap", sumMap);
		type = type.replaceAll("'", "  ");
		request.setAttribute("type", type);
		// long f = 3;
		// request.setAttribute("fareSum", f);
		String path = "jsp/" + curFolder
				+ "/fare_management/statisticsDetail.jsp?curPageNum="
				+ curPageNum + "&pageSize=" + pageSize;
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 导出统计经费记录
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void exportStatisticsFare(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 获得表格内容
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Double> statisticsFareMap = (LinkedHashMap<String, Double>) session
				.getAttribute("statisticsFareMap");
		String startTime = (String) session.getAttribute("statisticsStartTime");
		String endTime = (String) session.getAttribute("statisticsEndTime");
		// 获得表头内容
		String excelHead = request.getParameter("excelHead");
		String[] excelHeadArray = excelHead.split(",");

		// 以当前的时间命名
		String time = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒")
				.format(new Date());
		// 指定下载的文件名
		String fileName = time + "导出统计经费记录." + StringUtil.SUFFIX_EXECL;
		// 解决不同浏览器的乱码问题
		fileName = DownloadFile.getNormalFilename(request, fileName);
		// 处理中文乱码
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);
		// 指定下载的类型为excel
		response.setContentType("application/vnd.ms-excel");
		// 禁止缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		OutputStream output = response.getOutputStream();
		BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
		// 创建一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建一个工作表
		HSSFSheet sheet = workbook.createSheet();
		// 定义表头
		HSSFRow rowHead = sheet.createRow(0);
		// 往表头添加内容
		for (int i = 0; i < excelHeadArray.length; i++) {
			HSSFCellStyle cellStyleTitle = workbook.createCellStyle();
			// 指定单元格居中对齐
			cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyleTitle.setWrapText(true);
			// 设置表头边框
			cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			// 设置表头字体
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体增粗
			font.setFontName("宋体");
			font.setFontHeight((short) 220);// 字体大小
			cellStyleTitle.setFont(font);
			sheet.setColumnWidth(i, 4000);// 单元格宽度
			rowHead.createCell(i).setCellStyle(cellStyleTitle);
			rowHead.createCell(i).setCellValue(excelHeadArray[i]);
		}
		int i = 0;// 用于标记行号
		for (Map.Entry<String, Double> entry : statisticsFareMap.entrySet()) {
			HSSFRow row = sheet.createRow(i + 1);
			for (int j = 0; j < excelHeadArray.length; j++) {
				// 设置单元格样式
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				// 指定单元格居中对齐
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// 指定单元格垂直居中对齐
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				// 指定当单元格内容显示不下时自动换行
				cellStyle.setWrapText(true);
				// 设置单元格边框
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				row.createCell(j).setCellStyle(cellStyle);
				switch (j + 1) {
				case 1:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(i + 1 + ""));
					break;
				case 2:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(entry.getKey()));
					break;
				case 3:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(startTime));
					break;
				case 4:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(endTime));
					break;
				case 5:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(entry.getValue() + ""));
					break;
				}
			}
			i++;// 行号+1
		}
		try {
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();

		} catch (IOException e) {
			e.printStackTrace();
			// System.out.println("Output is closed ");
		}
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void exportExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 获得所有fareId
		ArrayList<Long> idList = new ArrayList<Long>();// 存储id的一个arraylist
		// 得到总的字符串
		String idStr1 = request.getParameter("idStr1");
		String idStr = "";// 最后生成的不重复的字符串
		if (idStr1 == null) {
			idStr1 = "";// idArray若为空则将其设为;了！
		}
		if (idStr1 != "") {
			String[] subs = idStr1.split(";");
			// 没选择的checkbox
			String noSelect = request.getParameter("noSelect1");
			if (noSelect == null) {
				noSelect = "";
			}
			String[] noSelectArray = null;
			if (noSelect != "") {
				noSelectArray = noSelect.split(";");// 得到没被选择的fareId存在一个数组中
			}
			HashSet hs = new HashSet();
			for (int i = 0; i < subs.length; i++) {
				hs.add(subs[i]);
			}
			Iterator it = hs.iterator();
			if (noSelectArray != null) {
				for (int i = 0; i < noSelectArray.length; i++) {// 存在数组noSelectArray的全部删除
					hs.remove(noSelectArray[i]);
				}
			}
			Iterator it1 = hs.iterator();
			String s = "";
			while (it1.hasNext()) {
				s = (String) it1.next();
				idStr = idStr + s + ";";// 再把重新得到的字符串组合
				idList.add(Long.parseLong(s));
			}
		}
		// 获得表头内容
		// 直接写成固定的
		String[] excelHeadArray = new String[] { "费用ID", "费用类型", "费用金额",
				"代储企业", "军代室", "操作时间", "备注" };

		// 以当前的时间命名
		String time = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒")
				.format(new Date());
		// 指定下载的文件名
		HttpSession session = request.getSession();
		String fileName = session.getAttribute("username") + "-"
				+ session.getAttribute("ownedUnit") + "-" + time + "导出经费记录."
				+ StringUtil.SUFFIX_EXECL;

		// 解决不同浏览器的乱码问题
		fileName = DownloadFile.getNormalFilename(request, fileName);
		// 处理中文乱码
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);
		// 指定下载的类型为excel
		response.setContentType("application/vnd.ms-excel");
		// 禁止缓存
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		OutputStream output = response.getOutputStream();
		BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
		// 创建一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建一个工作表
		HSSFSheet sheet = workbook.createSheet();

		// 定义表头
		HSSFRow rowHead = sheet.createRow(0);

		// 往表头添加内容
		for (int i = 0; i < excelHeadArray.length; i++) {
			HSSFCellStyle cellStyleTitle = workbook.createCellStyle();
			// 指定单元格居中对齐
			cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 指定单元格垂直居中对齐
			cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 指定当单元格内容显示不下时自动换行
			cellStyleTitle.setWrapText(true);
			// 设置表头边框
			cellStyleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			// 设置表头字体
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 字体增粗
			font.setFontName("宋体");
			font.setFontHeight((short) 220);// 字体大小
			cellStyleTitle.setFont(font);
			sheet.setColumnWidth(i, 4000);// 单元格宽度
			// 添加风格
			rowHead.createCell(i).setCellStyle(cellStyleTitle);
			// 添加值
			rowHead.createCell(i).setCellValue(excelHeadArray[i]);
		}

		// 定义单元格内容
		ArrayList<Fare> someFareArray = new ArrayList<Fare>();
		FareService fareService = new FareService();
		FareDetailService fds = new FareDetailService();
		someFareArray = fareService.getSomeFareById(idList);
		Fare fare = new Fare();
		for (int i = 0; i < someFareArray.size(); i++) {
			fare = someFareArray.get(i);
			HSSFRow row = sheet.createRow(i + 1);
			ArrayList<FareDetail> fareDetailList = fds.getAllFareDetail(fare
					.getFareId());
			// 定义k变量，明细的下标
			int k = 0;
			for (int j = 0; j < excelHeadArray.length + fareDetailList.size(); j++) {
				// 设置单元格样式
				HSSFCellStyle cellStyle = workbook.createCellStyle();
				// 指定单元格居中对齐
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// 指定单元格垂直居中对齐
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				// 指定当单元格内容显示不下时自动换行
				cellStyle.setWrapText(true);
				// 设置单元格边框
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				// 添加风格
				row.createCell(j).setCellStyle(cellStyle);

				switch (j + 1) {
				case 1:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(fare.getFareId() + ""));
					break;
				case 2:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(fare.getFareType()));
					break;
				case 3:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(fare.getFareAmount() + ""));
					break;
				case 4:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(fare.getStoreCompany()));
					break;
				case 5:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(fare.getJdRoom()));
					break;
				case 6:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(fare.getOperateDate() + ""));
					break;
				case 7:
					row.createCell(j).setCellValue(
							new HSSFRichTextString(fare.getRemark()));
					break;
				// case 8:
				// // make fareDetail String
				// String fs = "";
				// for (int k = 0; k < fareDetailList.size(); k++) {
				// fs = fs + fareDetailList.get(k).getDetailName() + ":"
				// + fareDetailList.get(k).getDetailAmount() + ";";
				// }
				// row.createCell(j).setCellValue(new HSSFRichTextString(fs));
				// break;
				default:
					String eachFareDetailStr = "";
					eachFareDetailStr += "项目:"
							+ fareDetailList.get(k).getDetailName() + ",";
					eachFareDetailStr += "金额:"
							+ fareDetailList.get(k).getDetailAmount() + ",";
					eachFareDetailStr += "时间:"
							+ fareDetailList.get(k).getDetailTime() + ",";
					eachFareDetailStr += "凭证号:"
							+ fareDetailList.get(k).getVoucherNo() + ",";
					eachFareDetailStr += "摘要:"
							+ fareDetailList.get(k).getDetailAbstract() + ",";
					eachFareDetailStr += "备注:"
							+ fareDetailList.get(k).getRemark();
					row.createCell(j).setCellValue(
							new HSSFRichTextString(eachFareDetailStr));
					rowHead.createCell(j).setCellValue("明细" + (k + 1));
					k++;
					break;
				}
			}

		}
		try {
			bufferedOutPut.flush();
			workbook.write(bufferedOutPut);
			bufferedOutPut.close();
		} catch (IOException e) {
			e.printStackTrace();
			// System.out.println("Output is closed ");
		} finally {
			someFareArray.clear();
		}
	}

	/**
	 * 导入经费记录
	 * 
	 * @throws FileUploadException
	 * @throws ServletException
	 * 
	 */
	private void importExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			FileUploadException, ServletException {
		FareService fareService = new FareService();
		FareDetailService fareDetailService = new FareDetailService();
		String savePath = this.getServletContext().getRealPath(
				"/WEB-INF/upload");
		DiskFileItemFactory factory = new DiskFileItemFactory();// 磁盘对象
		File file = new File(this.getServletContext().getRealPath(
				"/WEB-INF/temp"));
		// 如果文件夹不存在，则创建一个新的
		if (!file.exists()) {
			file.mkdirs();
		}
		factory.setSizeThreshold(1024 * 8); // 8k的缓冲区,文件大于8K则保存到临时目录
		factory.setRepository(file);// 用临时文件保存解析出来的数据
		ServletFileUpload upload = new ServletFileUpload(factory);// 声明解析的request对象
		// 判断是普通表单，还是带文件上传的表单。文件上传的表单值不能按普通表单接收值那样直接获取
		if (!ServletFileUpload.isMultipartContent(request)) {
			return;
		}
		upload.setFileSizeMax(1024 * 1024 * 100);// 设置每个文件的大小不能超过100M
		upload.setSizeMax(1024 * 1024 * 500);// 设置一共上传的文件大小不能超过500M

		ArrayList<ArrayList<String>> excelArray = new ArrayList<ArrayList<String>>();
		int addNum = 0;
		int updateNum = 0;

		List<FileItem> list = upload.parseRequest(request);// 把每个提交的表单项打包成一个list
		for (FileItem item : list) {
			// 判断为普通的表单输入域还是文件输入域
			if (item.isFormField()) {
				return;
			} else {
				String filename = item.getName();
				if (filename == null || filename.trim().equals("")) {
					continue;
				}
				// 截取文件名
				filename = filename.substring(filename.lastIndexOf("\\") + 1);
				InputStream in = item.getInputStream();
				// 防止文件覆盖
				String saveFilename = makeFileName(filename);
				// 真正的存储路径
				String realSavePath = makePath(saveFilename, savePath);
				String allPath = realSavePath + "\\" + saveFilename;
				FileOutputStream out = new FileOutputStream(allPath);
				byte buffer[] = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				// 将上传的文件导入到内存中，返回一个二维数组
				excelArray = importForm(allPath, 0);
				for (int i = 1; i < excelArray.size(); i++) {
					Fare fare = new Fare();
					fare.setFareId(Long.parseLong(excelArray.get(i).get(0)));
					fare.setFareType(excelArray.get(i).get(1));
					fare.setFareAmount(Double.parseDouble(excelArray.get(i)
							.get(2)));
					fare.setStoreCompany(excelArray.get(i).get(3));
					fare.setJdRoom(excelArray.get(i).get(4));
					fare.setOperateDate(excelArray.get(i).get(5));
					fare.setRemark(excelArray.get(i).get(6));
					// long num =
					// fareService.judgeFare(excelArray.get(i).get(1),
					// excelArray.get(i).get(2), excelArray.get(i).get(3),
					// excelArray.get(i).get(4), excelArray.get(i).get(5),
					// excelArray.get(i).get(6));
					long num = fareService.judgeFare(excelArray.get(i).get(0),
							excelArray.get(i).get(3), excelArray.get(i).get(5));
					boolean flag = false;// 默认是不相同的
					if (num != -1) {
						flag = true; // 不是-1，说明 是存在相同的
					}

					Fare fare1 = new Fare();
					if (flag) {
						// 存在相同的记录
					} else {
						// 不存在相同记录，可以导入
						// 根据费用id和代储企业是否在数据库有记录，决定是做更新还是添加操作
						num = fareService.judgeUpdateFare(fare);
						// 如果fareId和storeCompany相同，operateTime不同，则做更新费用记录处理
						if (num != -1) {
							updateNum++;
							fareService.updateFare(fare);
							fare1 = fare;
						}
						// 否则做添加费用处理
						else {
							addNum++;
							fareService.addFare(fare);
							fare1 = fareService.getLastFare();
						}
						fareDetailService.deleteByfareId(fare1.getFareId());// 删除detail，然后再添加

						// 更新或添加明细
						int k = 7;
						int rowSize = excelArray.get(i).size();
						while (!excelArray.get(i).get(k).equals("")) {
							String detail = excelArray.get(i).get(k);
							String[] detailItems = detail.split(",");
							FareDetail fd = new FareDetail();
							for (int j = 0; j < detailItems.length; j++) {
								String tempItem = detailItems[j].split(":")[1];
								switch (j) {
								case 0:
									fd.setDetailName(tempItem);
									break;
								case 1:
									fd.setDetailAmount(Double
											.parseDouble(tempItem));
									break;
								case 2:
									fd.setDetailTime(tempItem);
									break;
								case 3:
									fd.setVoucherNo(tempItem);
									break;
								case 4:
									fd.setDetailAbstract(tempItem);
									break;
								case 5:
									fd.setRemark(tempItem);
									break;
								default:
									break;
								}
							}
							k++;
							long id = fare1.getFareId();
							fd.setFareId(id);
							fareDetailService.addFareDetail(fd);
							if (k >= rowSize)
								break;
						}
					}

					in.close();
					out.close();
					item.delete();
					// 上传到服务器之后就删除，只需要读取
					File file1 = new File(allPath);
					flag = file1.delete();
					// if (flag == true) {
					// System.out.println("删除成功！");
					// }
				}
			}
		}
		request.setAttribute("all", excelArray.size() - 1); // -1是因为第一行表头不算
		request.setAttribute("addNum", addNum);
		request.setAttribute("updateNum", updateNum);
		request.setAttribute("excelArray", excelArray);
		request.getRequestDispatcher("jsp/jds/fare_management/importResult.jsp")
				.forward(request, response);
	}

	/**
	 * 删除一个附件
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteAttach(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获取前台传来的attachId
		int attachId = Integer.parseInt(request.getParameter("attachId"));

		AttachService attachService = new AttachService();
		// 通过attachId获取文件路径
		String attachPath = attachService.getAttachPathById(attachId);

		File file = new File(attachPath);

		// 确保数据库里的记录删除后，才可以删除文件本身
		if (attachService.deleteAttachByAttachTitle(attachId)) {
			file.delete();
		}
		// 回到附件列表
		getAllAttachInAFare(request, response);
	}

	/**
	 * 上传附件
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void uploadAttach(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		int fareId = Integer.parseInt(request.getParameter("fareId"));

		String storeCompany = (String) request.getParameter("storeCompany");
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		String type = request.getParameter("builtType");//

		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("builtType", type);
		request.setAttribute("startTime", startDate);
		request.setAttribute("endTime", endDate);
		request.setAttribute("storeCompany", storeCompany);

		AttachService attachService = new AttachService();
		Attach attach = new Attach();
		attach.setFareId(fareId);

		// 设置文件保存路径
		String savePath = this.getServletContext().getRealPath(
				"/WEB-INF/upload");
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();// 磁盘对象
			File file = new File(this.getServletContext().getRealPath(
					"/WEB-INF/temp"));
			// 如果文件夹不存在，则创建一个新的
			if (!file.exists()) {
				file.mkdirs();
			}
			factory.setSizeThreshold(1024 * 8); // 8k的缓冲区,文件大于8K则保存到临时目录
			factory.setRepository(file);// 用临时文件保存解析出来的数据
			ServletFileUpload upload = new ServletFileUpload(factory);// 声明解析的request对象
			// 判断是普通表单，还是带文件上传的表单。文件上传的表单值不能按普通表单接收值那样直接获取
			if (!ServletFileUpload.isMultipartContent(request)) {
				return;
			}
			upload.setFileSizeMax(1024 * 1024 * 100);// 设置每个文件的大小不能超过100M
			upload.setSizeMax(1024 * 1024 * 500);// 设置一共上传的文件大小不能超过500M
			List<FileItem> list = upload.parseRequest(request);// 把每个提交的表单项打包成一个list
			for (FileItem item : list) {
				// 判断为普通的表单输入域还是文件输入域
				if (item.isFormField()) {
					return;
				} else {
					String filename = item.getName();
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 截取文件名
					filename = filename
							.substring(filename.lastIndexOf("\\") + 1);
					InputStream in = item.getInputStream();
					// 防止文件覆盖
					String saveFilename = makeFileName(filename);// uuid name
					// 真正的存储路径
					String realSavePath = makePath(saveFilename, savePath);
					String allPath = realSavePath + "\\" + saveFilename;

					attach.setAttachTitle(saveFilename);
					attach.setAttachPath(allPath);
					// 向数据库中插入数据
					attachService.addAttach(attach, fareId);

					request.setAttribute("fareId", fareId);
					FileOutputStream out = new FileOutputStream(allPath);
					byte buffer[] = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					in.close();
					out.close();
					item.delete();
				}
			}
			request.getRequestDispatcher(
					"jsp/" + curFolder + "/fare_management/uploadSuccess.jsp")
					.forward(request, response);
		} catch (FileUploadBase.FileSizeLimitExceededException e) {
			e.printStackTrace();
			request.setAttribute("message", "文件上传失败！！");
			request.getRequestDispatcher("/fileLoad/message.jsp").forward(
					request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载附件
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void downloadAttach(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String storeCompany = (String) request.getParameter("storeCompany");
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		String type = request.getParameter("builtType");// 翻页的时候用

		request.setAttribute("curPageNum", curPageNum);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("builtType", type);
		request.setAttribute("startTime", startDate);
		request.setAttribute("endTime", endDate);
		request.setAttribute("storeCompany", storeCompany);

		String realName = request.getParameter("realName");
		String path = request.getParameter("path"); // 最终路径
		File file = new File(path);
		if (!file.exists()) {
			request.setAttribute("message", "文件不存在！");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
			return;
		}
		// 解决不同浏览器的乱码问题
		String fileName = DownloadFile.getNormalFilename(request, realName);
		response.setHeader("content-disposition", "attachment;filename="
				+ fileName);
		FileInputStream in = new FileInputStream(path);
		OutputStream out = response.getOutputStream();
		byte buffer[] = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * 得到一个经费项目的全部附件信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getAllAttachInAFare(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		String storeCompany = (String) request.getParameter("storeCompany");
		String builtType = (String) request.getParameter("builtType");

		request.setAttribute("startTime", startDate);
		request.setAttribute("endTime", endDate);
		request.setAttribute("storeCompany", storeCompany);
		request.setAttribute("builtType", builtType);

		AttachService attachService = new AttachService();
		int fareId = Integer.parseInt(request.getParameter("fareId"));
		ArrayList<Attach> allAttach = attachService.getAllAttachInAFare(fareId);
		request.setAttribute("allAttach", allAttach);
		String path = "jsp/" + curFolder + "/fare_management/allAttach.jsp";
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 修改一条经费记录
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void updateFare(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String builtType = (String) request.getParameter("builtType");
		int a = builtType.indexOf("'");

		if (a < 0 && !"".equals(builtType)) { // 超链接传过来的type不含单引号，需要重新加上
			builtType = builtType.replaceAll(",", "','");
			builtType = "'" + builtType + "'";
		}

		FareService fareService = new FareService();
		Fare fare = new Fare();
		long fareId = Long.parseLong(request.getParameter("fareId"));
		fare.setFareId(fareId);
		fare.setFareType(request.getParameter("type"));
		fare.setFareAmount(Double.parseDouble(request.getParameter("amount")));
		fare.setStoreCompany(request.getParameter("company"));
		fare.setJdRoom(request.getParameter("JdRoom"));
		fare.setOperateDate(MyDateFormat.changeDateToString(new Date()));
		fare.setRemark(request.getParameter("remark"));

		String fareDetails = request.getParameter("fareDetails");
		JSONArray fareDetailsJson = JSONArray.fromObject(fareDetails);

		// String detailName = request.getParameter("Hidden1");
		// String detailAmount = request.getParameter("Hidden2");
		// String[] name = detailName.split(";");
		// String[] amount = detailAmount.split(";");
		//
		// if (name[0] != "") {
		// double total = 0;
		// BigDecimal b2 = new BigDecimal(Double.toString(total));
		// for (int i = 0; i < name.length; i++) {
		// double sum1 = Double.parseDouble(amount[i]);
		// BigDecimal b1 = new BigDecimal(Double.toString(sum1));
		// b2 = b2.add(b1);
		// }
		// fare.setFareAmount(b2.doubleValue());
		// }
		// 先修改大费用
		if (fareService.updateFare(fare)) {
			// 再修改明细
			FareDetailService fareDetailService = new FareDetailService();
			if (fareDetailsJson.size() != 0) {
				boolean tempFlag = true;
				fareDetailService.deleteByfareId(fareId);
				for (int i = 0; i < fareDetailsJson.size(); i++) {
					FareDetail fareDetail = new FareDetail();
					JSONObject jsonObject = fareDetailsJson.getJSONObject(i);
					fareDetail
							.setDetailName(jsonObject.getString("detailName"));
					fareDetail
							.setDetailTime(jsonObject.getString("detailTime"));
					fareDetail.setVoucherNo(jsonObject.getString("voucherNo"));
					fareDetail.setDetailAmount(jsonObject
							.getDouble("detailAmount"));
					fareDetail.setDetailAbstract(jsonObject
							.getString("abstract"));
					fareDetail.setRemark(jsonObject.getString("remark"));
					fareDetail.setFareId(fareId);
					if (!fareDetailService.addFareDetail(fareDetail)) {
						tempFlag = false;
						break;
					}
				}
				if (tempFlag) {
					// 修改成功,记录到日志
					response.getWriter().write("1");
				} else
					response.getWriter().write("0");
			}
		} else {
			response.getWriter().write("0");
		}
		// request.setAttribute("builtType", builtType);
		// request.setAttribute("startTime", startDate);
		// request.setAttribute("endTime", endDate);
		// request.setAttribute("storeCompany", storeCompany);
		// // 获取查询条件下的经费数目
		// long checkFareSum = fareService.getCheckFareSum(startDate, endDate,
		// builtType, storeCompany);
		// request.setAttribute("checkFareSum", checkFareSum);
		// ArrayList<Fare> checkFare = fareService.getAllFareByDateAndFareType(
		// startDate, endDate, builtType, storeCompany, curPageNum,
		// pageSize);
		// request.setAttribute("check", checkFare);
		// String path = "jsp/" + curFolder
		// + "/fare_management/checkFare.jsp?curPageNum=" + curPageNum
		// + "&pageSize=" + pageSize + "&fareId=" + fareId;
		// request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 删除一条经费记录
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteFare(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		FareService fareService = new FareService();
		long fareId = Long.parseLong(request.getParameter("fareId"));// 删除一条经费记录
		Fare delFare = fareService.getOneFareById((int) fareId);
		boolean flag = fareService.deleteFare(fareId);
		AttachService attachService = new AttachService();

		ArrayList<String> allTitles = new ArrayList<String>();// 通过数据库，得到该条经费的全部uuid名
		allTitles = attachService.getAllTitleInAFare(fareId);
		for (int i = 0; i < allTitles.size(); i++) {
			deleteFileByTitle(allTitles.get(i));// 删除文件本身
		}
		flag = attachService.deleteAttach(fareId);// fare被删除后，数据库中相关的attach记录也删除了
		// 删除成功，记录到日志
		if (flag) {
			Log log = new Log();
			String username = (String) request.getSession().getAttribute(
					"username");
			log.setUserName(username);
			log.setOperateType("删除一条经费记录");
			log.setOperateTime(new Date());
			log.setRemark("删除的费用信息：\n" + delFare.toString());
			UserLogService.SaveOperateLog(log);
		}
		getFareByDateAndFareType(request, response);
	}

	/**
	 * 删除服务器上的文件
	 * 
	 * @param attachTitle
	 */
	private void deleteFileByTitle(String attachTitle) {
		String path = makePath(attachTitle, this.getServletContext()
				.getRealPath("/WEB-INF/upload"));
		File file = new File(path + "\\" + attachTitle);
		file.delete();
	}

	/**
	 * 查找所有费用记录
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void findAllFare(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		FareService fareService = new FareService();
		// 获取全部费用总数
		long fareSum = fareService.getFareSum();
		request.setAttribute("fareSum", fareSum);

		// 获得前台的传来的当前页码和按多少条分页的参数，获取当前页的数据
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));

		// 获取当页数据
		ArrayList<Fare> allFare = fareService.getAllFare(curPageNum, pageSize);
		request.setAttribute("allFare", allFare);
		// 根据当前页数和每页显示数目显示数据
		String path = "jsp/" + curFolder
				+ "/fare_management/allFare.jsp?curPageNum=" + curPageNum
				+ "&pageSize=" + pageSize;
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * 增加一条经费记录
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addFare(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		FareService fareService = new FareService();
		FareDetailService fareDetailService = new FareDetailService();
		long sum = Long.parseLong(request.getParameter("sum"));
		Fare fare = new Fare();
		// fare.setFareId(sum + 1);
		fare.setFareType(request.getParameter("type"));
		fare.setFareAmount(Double.parseDouble(request.getParameter("allMoney")));
		fare.setStoreCompany(request.getParameter("company"));
		fare.setJdRoom(request.getParameter("JdRoom"));
		fare.setOperateDate(request.getParameter("date").trim());
		fare.setRemark(request.getParameter("remark"));

		// 获取前台传过来的明细，明细添加到另外一张表
		// String detailName = request.getParameter("Hidden1");
		// String detailAmount = request.getParameter("Hidden2");
		// String[] name = detailName.split(";");
		// String[] amount = detailAmount.split(";");

		String fareDetails = request.getParameter("fareDetails");
		JSONArray fareDetailsJson = JSONArray.fromObject(fareDetails);

		// 以下代码防止表单重复提交
		boolean flag = true;
		Fare lastFare = fareService.getLastFare(); // 数据库中的最后一条记录
		if (lastFare.getFareId() != 0) {
			if (lastFare.getFareType().equals(fare.getFareType())
					&& ((String.valueOf((lastFare.getFareAmount()))
							.equals(String.valueOf(fare.getFareAmount()))))
					&& fare.getJdRoom().equals(lastFare.getJdRoom())
					&& fare.getOperateDate().equals(lastFare.getOperateDate())
					&& fare.getStoreCompany()
							.equals(lastFare.getStoreCompany())
					&& fare.getRemark().equals(lastFare.getRemark())) {
				// 全一样的话，flag设为false,无法添加
				flag = false;
			}
		}
		if (flag) {
			// // 先计算出fare金额的总和，
			// if (name[0] != "") {
			// double total = 0;
			// BigDecimal b2 = new BigDecimal(Double.toString(total));
			// for (int i = 0; i < name.length; i++) {
			// double sum1 = Double.parseDouble(amount[i]);
			// BigDecimal b1 = new BigDecimal(Double.toString(sum1));
			// b2 = b2.add(b1);
			// }
			// fare.setFareAmount(b2.doubleValue());
			// }
			// 先添加一条大费用到数据库
			long fareId = 0;
			if (fareService.addFare(fare)) {
				Fare lastFare1 = fareService.getLastFare();
				fareId = lastFare1.getFareId();
				// 再将大费用下的明细添加到数据库
				if (fareDetailsJson.size() != 0) {
					// 明细不为空的时才添加明细
					for (int i = 0; i < fareDetailsJson.size(); i++) {
						FareDetail fareDetail = new FareDetail();
						JSONObject jsonObject = fareDetailsJson
								.getJSONObject(i);
						fareDetail.setDetailName(jsonObject
								.getString("detailName"));
						fareDetail.setDetailTime(jsonObject
								.getString("detailTime"));
						fareDetail.setVoucherNo(jsonObject
								.getString("voucherNo"));
						fareDetail.setDetailAmount(jsonObject
								.getDouble("detailAmount"));
						fareDetail.setDetailAbstract(jsonObject
								.getString("abstract"));
						fareDetail.setRemark(jsonObject.getString("remark"));
						fareDetail.setFareId(fareId);
						fareDetailService.addFareDetail(fareDetail);
					}
					// 添加成功，记录到日志
					Log log = new Log();
					String username = (String) request.getSession()
							.getAttribute("username");
					log.setUserName(username);
					log.setOperateType("添加一条经费记录");
					log.setOperateTime(new Date());
					log.setRemark("添加的经费信息概要：" + fare.toString());
					UserLogService.SaveOperateLog(log);

					// 添加成功，返回1，在js中跳转到chekFare.jsp页面
					response.getWriter().write("1");
					return;

					// // 添加成功，数据库总数+1
					// sum = sum + 1;
					// long totalPageNum = sum % pageSize == 0 ? sum / pageSize
					// : (sum / pageSize + 1);
					// request.setAttribute("checkFareSum", sum);
					// request.setAttribute("totalPageNum", totalPageNum);//
					// 可能是没有的
					//
					// // 返回页面需要的，add不需要
					// request.setAttribute("startTime","");
					// request.setAttribute("endTime","");
					// request.setAttribute("storeCompany","");
					// request.setAttribute("builtType","");
					// // 获取该条数据所在的页数
					// // ArrayList<Integer> allOrder = new
					// ArrayList<Integer>();
					// // allOrder = fareService.getAllOrder();
					// // int order = allOrder.indexOf((int) fareId);
					// // order = order + 1;// 因为从零开始的所以加1
					// // long curPageNum = order % pageSize == 0 ? order /
					// pageSize : (order
					// // / pageSize + 1);
					//
					// //添加系统时间，默认回到首页
					// ArrayList<Fare> allFare =
					// fareService.getAllFareByDateAndFareType(
					// "", "", "", "",
					// 1, (int) pageSize);
					// request.setAttribute("check", allFare);
					// String path = "jsp/" + curFolder
					// + "/fare_management/checkFare.jsp?curPageNum=1&pageSize="
					// + pageSize + "&fareId=" + fareId;
					// request.getRequestDispatcher(path).forward(request,
					// response); // 把fareId也传过去
				} else {
					// 绕过前台，那么明细为空，删除刚才添加的大费用
					fareDetailService.deleteByfareId(fareId);
					response.getWriter().write("0");
					return;
				}
			} else {
				// 大费用添加失败
				response.getWriter().write("0");
				return;
			}
		} else {
			getFareByDateAndFareType(request, response);
			// System.out.println("重复提交!");
		}
	}

	/**
	 * 根据时间段、类型查询经费记录
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void getFareByDateAndFareType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		FareService fareService = new FareService();
		request.setCharacterEncoding("utf-8");
		// 获得前台的传来的当前页码和按多少条分页的参数，获取当前页的数据

		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		String type = request.getParameter("builtType");
		String[] types = request.getParameterValues("fareType");
		String storeCompany = (String) request.getParameter("storeCompany");

		// 得到总的字符串
		String idStr1 = request.getParameter("idStr1");
		// System.out.println("得到最初传过来的字符串="+idStr1);
		String idStr = "";// 最后生成的不重复的字符串
		if (idStr1 == null) {
			idStr1 = "";// idArray若为空则将其设为;了！
		}
		if (idStr1 != "") {
			String[] subs = idStr1.split(";");

			// 没选择的checkbox
			String noSelect = request.getParameter("noSelect1");
			if (noSelect == null) {
				noSelect = "";
			}
			String[] noSelectArray = null;
			if (noSelect != "") {
				noSelectArray = noSelect.split(";");// 得到没被选择的fareId存在一个数组中
			}
			HashSet hs = new HashSet();
			for (int i = 0; i < subs.length; i++) {
				hs.add(subs[i]);
			}
			Iterator it = hs.iterator();
			if (noSelectArray != null) {// ///
				for (int i = 0; i < noSelectArray.length; i++) {// 存在数组noSelectArray的全部删除
					hs.remove(noSelectArray[i]);
				}
			}

			Iterator it1 = hs.iterator();
			while (it1.hasNext()) {
				idStr = idStr + it1.next() + ";";// 再把重新得到的字符串组合
			}
		}

		if (type == "") {
			if (types != null && !types.equals("")) {// 如果没勾选type则type=null
				for (int i = 0; i < types.length; i++) { // 得到所勾选的
					String s = types[i];
					if (s != null) {
						StringBuilder stringBuilder = new StringBuilder();// 拼接字符串
						stringBuilder.append("'");
						stringBuilder.append(s);
						if (i != types.length - 1) {
							stringBuilder.append("',");
						} else {
							stringBuilder.append("'");
						}
						type = type + stringBuilder.toString();
					}
				}
			}
		}
		if (type == null || startDate == null || endDate == null
				|| storeCompany == null) {// 有一个是null，就说明全是
			type = "";
			startDate = "";
			endDate = "";
			storeCompany = "";
		} else if ("" != type) {// 若不是首次访问并且不为空，（首次访问翻页时就变成""了）
			int a = type.indexOf("'");
			if (a < 0) { // 超链接传过来的type不含单引号，需要重新加上
				type = type.replaceAll(",", "','");
				type = "'" + type + "'";
			}
		}
		// System.out.println("传到前台的字符串="+idStr);
		// 获取查询条件下的经费数目
		request.setAttribute("builtType", type);
		request.setAttribute("startTime", startDate);
		request.setAttribute("endTime", endDate);
		request.setAttribute("storeCompany", storeCompany);
		request.setAttribute("types", types);
		request.setAttribute("idStr", idStr);

		// 如果没选查询条件的情况下点击了“查询”按钮则跳到全部经费界面
		// if (type.equals("") && startDate.equals("") && endDate.equals("")
		// && (storeCompany == null || storeCompany.equals(""))) {
		// getFareByDateAndFareType(request, response);
		// findAllFare(request, response);
		// } else {
		long checkFareSum = fareService.getCheckFareSum(type, startDate,
				endDate, storeCompany);
		request.setAttribute("checkFareSum", checkFareSum);
		ArrayList<Fare> checkFare = fareService.getAllFareByDateAndFareType(
				startDate, endDate, type, storeCompany, curPageNum, pageSize);
		request.setAttribute("check", checkFare);

		String path = "jsp/" + curFolder
				+ "/fare_management/checkFare.jsp?curPageNum=" + curPageNum
				+ "&pageSize=" + pageSize;
		request.getRequestDispatcher(path).forward(request, response);
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
															// upload\3\5
		// File既可以代表文件也可以代表目录
		File file = new File(dir);
		// 如果目录不存在
		if (!file.exists()) {
			// 创建目录
			file.mkdirs();
		}
		return dir;
	}

	/**
	 * 用来导入excel的类
	 * 
	 * @param filePath
	 * @param sheetIndex
	 * @return
	 */
	private ArrayList<ArrayList<String>> importForm(String filePath,
			int sheetIndex) {
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		Workbook wb = null;
		try {
			InputStream inputStream = new FileInputStream(filePath);
			wb = WorkbookFactory.create(inputStream);
			Sheet sheet = wb.getSheetAt(sheetIndex);

			int firstRowNum = sheet.getFirstRowNum();// 表格中开始有数据的行的索引
			Row beginRow = sheet.getRow(firstRowNum);// 表格中开始有数据的行
			int lastRowNum = sheet.getLastRowNum();// 表格中最后一行的索引
			int firstColNum = beginRow.getFirstCellNum();// 表格中开始有数据的第一列的索引
			int colNum = beginRow.getLastCellNum() - firstColNum;// 表格中数据的最后一列减去第一列

			if (colNum > 1) {
				for (int i = sheet.getFirstRowNum(); i < lastRowNum + 1; i++) {
					ArrayList<String> tempList = new ArrayList<String>();
					Row tempRow = sheet.getRow(i);

					for (int k = firstColNum; k < colNum; k++) {
						Cell tempCell = tempRow.getCell(k,
								Row.CREATE_NULL_AS_BLANK);
						/**
						 * 
						 * switch，用来判断excel单元格中的数据是什么格式的 然后采用相应的方法来读取，否则会抛出异常
						 */
						switch (tempCell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							tempList.add(tempCell.getRichStringCellValue()
									.getString().trim());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (DateUtil.isCellDateFormatted(tempCell)) {
								// 这里为日期格式化成字符串
								Date date = tempCell.getDateCellValue();
								String dateString = MyDateFormat
										.changeDateToString(date);
								tempList.add(dateString);
							} else {
								tempCell.setCellType(Cell.CELL_TYPE_STRING);
								String tempString = tempCell
										.getStringCellValue().trim();
								if (tempString.indexOf(".") > -1) {
									tempList.add(String.valueOf(
											new Double(tempString)).trim());
								} else {
									tempList.add(tempString);
								}
							}
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							tempList.add(String.valueOf(tempCell
									.getBooleanCellValue()));
							break;
						case Cell.CELL_TYPE_FORMULA:
							tempCell.setCellType(Cell.CELL_TYPE_STRING);
							String tempString = tempCell.getStringCellValue()
									.trim();
							if (tempString != null) {
								tempString.replaceAll("#N/A", "").trim();
								tempList.add(tempString);
							}
							break;
						case Cell.CELL_TYPE_ERROR:
							tempList.add("");
							break;
						default:
							tempList.add("");
						}
					}
					list.add(tempList);
				}
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
