package cn.edu.cqupt.service.query_business;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;
public class ApplyFormOperation implements Cloneable {

	/**
	 * 把二维数组导出成excel表
	 * 
	 * @param list
	 *            装载导出数据的二维数组
	 * @param absolutePath
	 * @author LiangYiHuai
	 */
	public boolean exportForm(List<ArrayList<String>> list, String absolutePath) {

		int index = absolutePath.lastIndexOf(".");
		String suffix = absolutePath.substring(index + 1);

		boolean flag = false;

		Workbook wb = null;
		if ("xls".equals(suffix)) {
			wb = new HSSFWorkbook();
		} else if ("xlsx".equals(suffix)|| StringUtil.SUFFIX_EXECL.equals(suffix)) {
			wb = new XSSFWorkbook();
		} else {
			return false;
		}
		Sheet sheet = wb.createSheet();
		
		if(list != null){
			int size = list.size();
			for (int k = 0; k < size; k++) {
				
				Row row = sheet.createRow(k);
				
				int size_2 = list.get(k).size();
				for (int i = 0; i < size_2; i++) {
					String tempString = list.get(k).get(i);
					row.createCell(i).setCellValue(tempString);
				}
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(absolutePath);
			wb.write(out);
			out.close();
			wb.close();
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param absolutePath
	 * @param dyadics 二维数组
	 * @param ownedUnit 所属单位
	 * @param version 版本
	 * @return runStatus
	 * 
	 * note：
	 * 第一个sheet可见，其他的sheet不可见，
	 * 可以用exportForm(List<ArrayList<String>> list, String absolutePath)方法测试一下是否成功导入
	 */
	public boolean exportForm(String absolutePath, String version, String ownedUnit, List<ArrayList<String>>... dyadics){
		
		int index = absolutePath.lastIndexOf(".");
		String suffix = absolutePath.substring(index + 1);

		boolean flag = false;

		Workbook wb = null;
		if ("xls".equals(suffix)) {
			wb = new HSSFWorkbook();
		} else if ("xlsx".equals(suffix) || StringUtil.SUFFIX_EXECL.equals(suffix)) {
			wb = new XSSFWorkbook();
		} else {
			return false;
		}
		
		Sheet firstSheet = wb.createSheet();
		List<ArrayList<String>> firstDyadic = dyadics[0];
		int size = firstDyadic.size();
		for (int k = 0; k < size; k++) {
			
			Row row = firstSheet.createRow(k);
			
			int size_2 = firstDyadic.get(k).size();
			for (int i = 0; i < size_2; i++) {
				String tempString = firstDyadic.get(k).get(i);
				row.createCell(i).setCellValue(tempString);
			}
		}
		
		int len = dyadics.length;
		for(int i = 1; i < len; i++){
			List<ArrayList<String>> dyadic = dyadics[i];
			
			Sheet sheet = wb.createSheet();
			//隐藏sheet.true为隐藏, flase为不隐藏
			wb.setSheetHidden(i, false);
			
			int dyadicSize = dyadic.size();
			for(int k = 0; k < dyadicSize; k++){
				Row row = sheet.createRow(k);
				
				int size_2 = dyadic.get(k).size();
				for (int j = 0; j < size_2; j++) {
					String tempString = dyadic.get(k).get(j);
					row.createCell(j).setCellValue(tempString);
				}
				if(version.equals("1"))
				row.createCell(size_2).setCellValue(ownedUnit);
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(absolutePath);
			wb.write(out);
			
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;	
	}
	
	/**
	 * 导入一个excel文件中的所有sheet
	 * @param filePath 绝对路径
	 * @param sheetNumber sheet的个数，可以少于实际的个数，但不可比实际的多,如果只有一个sheet，则值为1
	 * @return 键表示sheet的位置，从0开始；值表示一个sheet中的内容，用二维数组表示
	 */
	public Map<Integer, List<ArrayList<String>>> importAllSheetFromExcel(String filePath, int sheetNumber){
		
		Map<Integer, List<ArrayList<String>>> map = new HashMap<Integer, List<ArrayList<String>>>();

		Workbook wb = null;
		try {
			InputStream inp = new FileInputStream(filePath);
			wb = WorkbookFactory.create(inp);
			
			Sheet sheet = null;
			int j;
			for(j = 0; j < sheetNumber; j++){
				sheet = wb.getSheetAt(j);
				
				List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
				
				int firstRowNum = sheet.getFirstRowNum();// 表格中开始有数据的行的索引
				Row biginRow = sheet.getRow(firstRowNum);// 表哥中开始有数据的行
				int lastRowNum = sheet.getLastRowNum();// 表格中最后一行的索引
				int firstColNum = biginRow.getFirstCellNum();// 表格中开始有数据的第一列的索引
				int colNum = biginRow.getLastCellNum() - firstColNum;// 表格中数据的最后一列减去第一列
	
				if (colNum > 1) {
					for (int i = sheet.getFirstRowNum(); i < lastRowNum + 1; i++) {
						ArrayList<String> tempList = new ArrayList<String>();
						Row tempRow = sheet.getRow(i);
	
						for (int k = firstColNum; k < colNum; k++) {
							Cell tempCell = tempRow.getCell(k,Row.CREATE_NULL_AS_BLANK);
							/**
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
					map.put(j, list);
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
				if(wb != null)wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public static final String EQUIPMENT_COLLECTIVE_WORD_FILE="SampleFile/EquipmentCollective.docx";//器材实力汇总表
	public static final String EUIPMENT_DETAIL_WORD_FILE="SampleFile/EquipmentDetail.docx";//器材明细表
	public static final String EQUIPMENT_DETAIL_ACCOUNT_WORD_FILE="SampleFile/EquipmentDetailAccount.docx";//器材明细账表
	
	/**
	 * 将内存中的内容写到word文件中。
	 * 注：需要一定改动的word文件模板。
	 * @param fileName
	 *            文件的保存路径+文件名，其中要以.docx问拓展名
	 * @param map
	 *            用来填充模板文件前几行的内容 key: AYWH(案由文号);FLDH(发料单号);
	 *            YSFS(运输方式);YDBH(运单编号);totalPage;currentPage
	 * @param dyadic
	 *            是用来填充word文件的表格table的
	 * @throws IOException
	 */
	public void writeEquipmentCollectiveXWPFFile(String filename,
			List<ArrayList<String>> dyadic)throws IOException{
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			/**
			 * .class 获取类对象
			 * getClassLoader() 返回类加载器
			 * getResource() 获取资源
			 * getPath() 得到相对路径
			 */
			String url = ApplyFormOperation.class.getClassLoader().getResource(EQUIPMENT_COLLECTIVE_WORD_FILE).getPath();
			//当路径中存在中文和空格时,会对这些字符进行转换
			url = java.net.URLDecoder.decode(url,"utf-8");
			in = new FileInputStream(url);
			XWPFDocument doc = new XWPFDocument(in);
			
			// 得到doc表中的表格对象数组
			List<XWPFTable> tables = doc.getTables();
			XWPFTable table = tables.get(0);
			
			//新增加的表格行数
			int newRowSize = 0;
			if(dyadic.size() >= 1){
				newRowSize = dyadic.size();
			}
			
			//模板行
			XWPFParagraph templetParagraph = table.getRow(1).getCell(0).getParagraphs().get(0);
			
			
			for (int i = 0; i < newRowSize; i++) {
				//增加新的行对象
				table.createRow();				
			}
			//获取表格中的行
			List<XWPFTableRow> rows = table.getRows();
			//遍历每一行,添加数据.从第二行开始,因为第0行是标题，第二行作为模板行，最后会被删除
			for (int i = 2; i < rows.size(); i++) {
				int dyadicSize = dyadic.size();
				ArrayList<String> dataList = null;
				//避免数组下标溢出和空指针
				if(i-1 > dyadicSize){
					break;
				}
				dataList = dyadic.get(i-2);
				XWPFTableRow tr = rows.get(i);
				//得到行中的单元格数组
				List<XWPFTableCell> tableCells = tr.getTableCells();
				//得到每一行中的单元格
				int size = tableCells.size();
				for (int j = 0; j < size; j++) {
					//避免数组下标溢出
					if(j >= dataList.size()){
						break;
					}
					String string = dataList.get(j);
					XWPFTableCell tc = tableCells.get(j);
					
					//设置单元格的格式
					tc.setParagraph(templetParagraph);
					CTTcPr tcpr = tc.getCTTc().addNewTcPr();
					CTVerticalJc va = tcpr.addNewVAlign();
		     		va.setVal(STVerticalJc.CENTER);
					//添加文字
					tc.setText(string);
				}
			}
			//删除第二行
			table.removeRow(1);
			
			//此处开始填满word一页
			if(newRowSize>13){
				for (int i = 0; i < 14-(newRowSize-13)%14; i++) {
					//增加新的行对象
					table.createRow();				
				}
			}else{
				for (int i = 0; i < 13-newRowSize%13; i++) {
					//增加新的行对象
					table.createRow();				
				}
			}
			
			//生成word文件
			out = new FileOutputStream(filename);
			doc.write(out);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				out.close();
				in.close();				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeEquipmentDetailXWPFFile(String filename,
			List<ArrayList<String>> dyadic)throws IOException{
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			String url = ApplyFormOperation.class.getClassLoader().getResource(EUIPMENT_DETAIL_WORD_FILE).getPath();
			//当路径中存在中文和空格时,会对这些字符进行转换
			url = java.net.URLDecoder.decode(url,"utf-8");
			in = new FileInputStream(url);
			XWPFDocument doc = new XWPFDocument(in);
			
			// 得到doc表中的表格对象数组
			List<XWPFTable> tables = doc.getTables();
			XWPFTable table = tables.get(0);
			//新增加的表格行数
			int newRowSize = 0;
			if(dyadic.size() >= 1){
				newRowSize = dyadic.size();
			}
			
			//模板行
			XWPFParagraph templetParagraph = table.getRow(1).getCell(0).getParagraphs().get(0);
			
			for (int i = 0; i < newRowSize; i++) {
				//增加新的行对象
				table.createRow();
			}
			//获取表格中的行
			List<XWPFTableRow> rows = table.getRows();
			//遍历每一行,添加数据.从第二行开始,因为第0行是标题，第二行作为模板行，最后会被删除
			for (int i = 2; i < rows.size(); i++) {
				int dyadicSize = dyadic.size();
				ArrayList<String> dataList = null;
				//避免数组下标溢出和空指针
				if(i-1 > dyadicSize){
					break;
				}
				dataList = dyadic.get(i-2);
				XWPFTableRow tr = rows.get(i);
				//得到行中的单元格数组
				List<XWPFTableCell> tableCells = tr.getTableCells();
				//得到每一行中的单元格
				int size = tableCells.size();
				for (int j = 0; j < size; j++) {
					//避免数组下标溢出
					if(j >= dataList.size()){
						break;
					}
					String string = dataList.get(j);
					XWPFTableCell tc = tableCells.get(j);
					
					//设置单元格的格式
					tc.setParagraph(templetParagraph);
					CTTcPr tcpr = tc.getCTTc().addNewTcPr();
					CTVerticalJc va = tcpr.addNewVAlign();
		     		va.setVal(STVerticalJc.CENTER);
					
					//添加文字
					tc.setText(string);
				}
			}
			//删除第二行
			table.removeRow(1);
			
			//此处开始填满word一页
			if(newRowSize>13){
				for (int i = 0; i < 14-(newRowSize-13)%14; i++) {
					//增加新的行对象
					table.createRow();				
				}
			}else{
				for (int i = 0; i < 13-newRowSize%13; i++) {
					//增加新的行对象
					table.createRow();				
				}
			}
			
			//生成word文件
			out = new FileOutputStream(filename);
			doc.write(out);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				out.close();
				in.close();				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 测试用例
	 * */
	public void writeEquipmentDetailAccountXWPFFile(String filename, String number, String[] title, List<ArrayList<String>> dyadic) throws IOException{
		FileInputStream in =null;
		FileOutputStream out = null;
		
		try {
			/**
			 * .class 获取类对象
			 * getClassLoader() 返回类加载器
			 * getResource() 获取资源
			 * getPath() 得到相对路径
			 */
			String url = ApplyFormOperation.class.getClassLoader().getResource("SampleFile/EquipmentDetailAccount.docx").getPath();
			//当路径中存在中文和空格时,会对这些字符进行转换
			url = java.net.URLDecoder.decode(url,"utf-8");
			in = new FileInputStream(url);
			XWPFDocument docx = new XWPFDocument(in);
			
			//获得第一个段落对象<!--此处开始是的第一部分-->
			Iterator<XWPFParagraph> iter = docx.getParagraphsIterator();
			//标记文字段落
			int count = 0;
			while(iter.hasNext()){
				if(count > 1)
					break;
				XWPFParagraph paragraph = iter.next();
				//count表示第几个变量
				if(count == 1){
					List<XWPFRun> runs = paragraph.getRuns();
					for (int i = 0; i < runs.size(); i++) {
						//取得XWPFRun的字符串
						String string = runs.get(i).text().replaceAll("\\s*", "");
//						System.out.println("string:"+string);
						if("#".equals(string)){
							XWPFRun tempRun = runs.get(i);
							//替换掉井号
							tempRun.removeBreak();
							tempRun.setText(number,0);
						}
					}
				}
				count++;
			}
			
			//得到表格对象<!--此处开始是第二部分-->
			List<XWPFTable> tables = docx.getTables();
			XWPFTable table = tables.get(0);
			
			//新增加的表格行数
			int newRowSize = 0;
			if(dyadic.size() >= 1){
				newRowSize = dyadic.size();
			}
			
			//模板行
			XWPFParagraph templetParagraph = table.getRow(5).getCell(0).getParagraphs().get(0);
			for (int i = 0; i < newRowSize; i++) {
				//增加新的行对象
				table.createRow();
				XWPFTableRow row = table.getRow(6+i);//得到这一行
				for (int j = 0; j < 6; j++) {
					row.addNewTableCell();
				}
			}
			
			//获取表格中的行
			List<XWPFTableRow> rows = table.getRows();
			
			//从第0行开始添加数据
			for (int i = 0; i < rows.size(); i++) {
				//得到表格中的每一行
				XWPFTableRow tr = rows.get(i);
				//得到每一行中的单元格数组
				List<XWPFTableCell> tableCells = tr.getTableCells();
//				System.out.println(tableCells);
				int size = tableCells.size();
				for (int j = 0; j < size; j++) {
					//避免数组下标溢出
					if(i-6 >= dyadic.size()){
						break;
					}
					//模板第一行
					if(i == 0){
						if(j==0||j==2||j==4||j==6||j==8)continue;
						XWPFTableCell tc = tableCells.get(j);
						//设置单元格格式
						tc.setParagraph(templetParagraph);
						//添加文字
						if(j==1){
							tc.setText(title[0]);
						}else if(j==3){
							tc.setText(title[1]);
						}else if(j==5){
							tc.setText(title[2]);
						}else if(j==7){
							tc.setText(title[3]);
						}else if(j==9){
							tc.setText(title[4]);
						}
					//模板第二行
					}else if(i == 1){
						if(j==0||j==2||j==4||j==6)continue;
						XWPFTableCell tc = tableCells.get(j);
						//设置单元格格式
						tc.setParagraph(templetParagraph);
						//添加文字
						if(j==1){
							tc.setText(title[5]);
						}else if(j==3){
							tc.setText(title[6]);
						}else if(j==5){
							tc.setText(title[7]);
						}
					//模板第三行
					}else if(i == 2){
						if(j==0)continue;
						XWPFTableCell tc = tableCells.get(j);
						//设置单元格格式
						tc.setParagraph(templetParagraph);
						//添加文字
						if(j==1){
							tc.setText(title[8]);
						}
					//模板第四行
					}else if(i == 3){
						continue;
					//模板第五行	
					}else if(i == 4){
						continue;
					//模板第六行
					}else if(i == 5){
						//第6行作为模板行，会被删除
						continue;
					//明细账具体数据
					}else{
						if(j==9||j==10||j==11||j==12||j==13||j==14)continue;
						XWPFTableCell tc = tableCells.get(j);
						//设置单元格格式
						tc.setParagraph(templetParagraph);CTTcPr tcpr = tc.getCTTc().addNewTcPr();
						CTVerticalJc va = tcpr.addNewVAlign();
			     		va.setVal(STVerticalJc.CENTER);
						if(j==15){
							tc.setText(dyadic.get(i-6).get(9));
						}else{
							tc.setText(dyadic.get(i-6).get(j));
						}
					}
				}
			}
			
			//<!--此处开始是第三部分-->
			//删除第五行
			table.removeRow(5);
			//此处开始填满word一页
			if(dyadic.size() == 9 || (dyadic.size()-9)%13 == 0){
				
			}else if(dyadic.size() < 9){
				for (int k = 0; k < 9-dyadic.size(); k++) {
					table.createRow();
					XWPFTableRow row = table.getRow(5+dyadic.size()+k);//得到这一行
					for (int j = 0; j < 6; j++) {
						row.addNewTableCell();
					}
				}
			}else if(dyadic.size() > 9){
				for (int k = 0; k < 13-(dyadic.size()-9)%13; k++) {
					table.createRow();
					XWPFTableRow row = table.getRow(5+dyadic.size()+k);//得到这一行
					for (int j = 0; j < 6; j++) {
						row.addNewTableCell();
					}
				}
			}
			
			//生成word文件
			out = new FileOutputStream(filename);
			docx.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
//	public void writeEquipmentDetailAccountXWPFFile(String filename, String number, String[] title,
//			List<ArrayList<String>> dyadic)throws IOException{
//		FileInputStream in = null;
//		FileOutputStream out = null;
//		
//		try {
//			String url = ApplyFormOperation.class.getClassLoader().getResource(EQUIPMENT_DETAIL_ACCOUNT_WORD_FILE).getPath();
//			//当路径中存在中文和空格时,会对这些字符进行转换
//			url = java.net.URLDecoder.decode(url,"utf-8");
//			in = new FileInputStream(url);
//			XWPFDocument doc = new XWPFDocument(in);
//			
//			//获得第一个段落对象
//			Iterator<XWPFParagraph> iter = doc.getParagraphsIterator();
//			//标记文字段落
//			int count = 0;
//			while(iter.hasNext()){
//				if(count > 1)
//					break;
//				XWPFParagraph paragraph = iter.next();
//				//count变量表示第几个变量
//				if(count == 1){
//					List<XWPFRun> runs = paragraph.getRuns();
//					for (int i = 0; i < runs.size(); i++) {
//						//取得XWPFRun的字符串
//						String string = runs.get(i).text().replaceAll("\\s*", "");
//						System.out.println("string:"+string);
//						if("#".equals(string)){
//							XWPFRun tempRun = runs.get(i);
//							//替换掉井号
//							tempRun.removeBreak();
//							tempRun.setText(number, 0);
//						}
//					}
//				}
//				count++;
//			}
//			
//			// 得到doc表中的表格对象数组
//			List<XWPFTable> tables = doc.getTables();
//			XWPFTable table = tables.get(0);
//			
//			
//			//新增加的表格行数
//			int newRowSize = 0;
//			if(dyadic.size() >= 1){
//				newRowSize = dyadic.size();
//			}
//			
//			//模板行
//			XWPFParagraph templetParagraph = table.getRow(3).getCell(0).getParagraphs().get(0);
//			
//			for (int i = 0; i < newRowSize; i++) {
//				//增加新的行对象
//				table.createRow();
//			}
//			//获取表格中的行
//			List<XWPFTableRow> rows = table.getRows();
//			
//			//添加第1行数据,第0行为标题
//			XWPFTableRow tr1 = rows.get(1);
//			//得到行中的单元格数组
//			List<XWPFTableCell> tableCells1 = tr1.getTableCells();
//			//得到每一行中的单元格
//			int size1 = tableCells1.size();
//			for (int j = 0; j < size1; j++) {
//				//避免数组下标溢出
//				if(j >= title.length){
//					break;
//				}
//				String string = title[j];
//				XWPFTableCell tc = tableCells1.get(j);
//				
//				//设置单元格的格式
//				tc.setParagraph(templetParagraph);
//				//添加文字
//				tc.setText(string);
//			}
//			
//			//遍历每一行,添加数据.从第4行开始,因为第0\1\2行是标题，第3行作为模板行，最后会被删除
//			for (int i = 4; i < rows.size(); i++) {
//				int dyadicSize = dyadic.size();
//				ArrayList<String> dataList = null;
//				//避免数组下标溢出和空指针
//				if(i-3 > dyadicSize){
//					break;
//				}
//				dataList = dyadic.get(i-4);
//				XWPFTableRow tr = rows.get(i);
//				//得到行中的单元格数组
//				List<XWPFTableCell> tableCells = tr.getTableCells();
//				//得到每一行中的单元格
//				int size = tableCells.size();
//				System.out.println("tableCells.size:"+tableCells.size());
//				System.out.println("dataList.size:"+dataList.size());
//				for (int j = 0; j < size; j++) {
//					//避免数组下标溢出
//					if(j >= dataList.size()){
//						break;
//					}
//					String string = dataList.get(j);
//					XWPFTableCell tc = tableCells.get(j);
//					
//					//设置单元格的格式
//					tc.setParagraph(templetParagraph);
//					//添加文字
//					tc.setText(string);
//				}
//			}
//			//删除第五行
//			table.removeRow(3);
//			
//			//此处开始填满word一页
//			if(newRowSize>11){
//				for (int i = 0; i < 14-(newRowSize-11)%14; i++) {
//					//增加新的行对象
//					table.createRow();				
//				}
//			}else{
//				for (int i = 0; i < 11-newRowSize%11; i++) {
//					//增加新的行对象
//					table.createRow();				
//				}
//			}
//			
//			//生成word文件
//			out = new FileOutputStream(filename);
//			doc.write(out);	
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			try {
//				out.close();
//				in.close();	
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public static void main(String[] args) {
		ApplyFormOperation applyFormOperation = new ApplyFormOperation();
		List<ArrayList<String>> dyadic = new ArrayList<ArrayList<String>>();
//		for (int i = 0; i < 10; i++) {
//			ArrayList<String> t = new ArrayList<String>();
//			for (int j = 0; j < 16; j++) {
//				String string = i+":"+j;
//				t.add(string);
//			}
//			dyadic.add(t);
//		}
//		for (int i = 0; i < 1; i++) {
//			ArrayList<String> t = new ArrayList<String>();
//			for (int j = 0; j < 7; j++) {
//				String string = "aa";
//				t.add(string);
//			}
//			t.add("名称型号名称型号名称型号名称型号名称型号");
//			dyadic.add(t);
//		}
//		
//		String[] title = new String[9];
//		for (int i = 0; i < title.length; i++) {
//			title[i] = i+"";
//		}
		String number = "s111";
		String[] title = {"美式冲锋枪M-16","器材代码1","2223","3","3","6905","12.0","包装说明","配套说明"};
		for (int i = 0; i < 78; i++) {
			String year = i+"2015";
			String month = "3";
			String day = "28";
			String listId = "001";
			String neirongzhaiyao = "内容摘要";
//			String chang = "6905";
			String jiecunshuliang = "3";
			String shouru = "0";
			String fachu = "3";
			String jiecun = "3";
			String remark = "备注1";
			ArrayList<String> T = new ArrayList<String>();
			T.add(year);
			T.add(month);
			T.add(day);
			T.add(listId);
			T.add(neirongzhaiyao);
//			T.add(chang);
			T.add(jiecunshuliang);
			T.add(shouru);
			T.add(fachu);
			T.add(jiecun);
			T.add(remark);
			dyadic.add(T);
		}
		try {
//			applyFormOperation.writeEquipmentDetailAccountXWPFFile("C:\\Users\\Administrator\\Desktop\\test1.docx","S001",title, dyadic);
			applyFormOperation.writeEquipmentCollectiveXWPFFile("C:\\Users\\Administrator\\Desktop\\test2.docx", dyadic);
			applyFormOperation.writeEquipmentDetailXWPFFile("C:\\Users\\Administrator\\Desktop\\test3.docx", dyadic);
//			applyFormOperation.writeXWPFFile("C:\\Users\\Administrator\\Desktop\\test.docx", number, title, dyadic);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
