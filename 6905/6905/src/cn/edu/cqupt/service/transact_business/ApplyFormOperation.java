package cn.edu.cqupt.service.transact_business;


import java.io.File;
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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.dao.OutApplyDAO;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class ApplyFormOperation {

	/**
	 * 这个类用于excel文件的相关操作
	 */

	private InApplyDAO inApplyDAO = null;
	private OutApplyDAO outApplyDAO = null;

	public ApplyFormOperation() {

	}

	/**
	 * 添加出库申请记录
	 * 
	 * @author limengxin
	 * @param outapply
	 * @return
	 */
//	public long saveOutApply(OutApply outapply) {
//
//		outApplyDAO = new OutApplyDAO();
//		return outApplyDAO.saveOutApply(outapply);
//
//	}

	/**
	 * 添加入库申请记录
	 * 
	 * @author limengxin
	 * @param inapply
	 * @return
	 */
//	public long saveInApply(InApply inapply) {
//
//		inApplyDAO = new InApplyDAO();
//		// inApplyDAO.saveApply(inapply);
//		return inApplyDAO.saveApply(inapply);
//	}

	/**
	 * 把二维数组导出成excel表
	 * 
	 * @param list
	 *            装载导出数据的二维数组
	 * @param filePath
	 *            导出的路径
	 * @author LiangYiHuai
	 */
	/*
	 * public boolean exportForm(List<ArrayList<String>> list, String fileName)
	 * { try { //Windows风格
	 * UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
	 * ); } catch (Exception e1) { e1.printStackTrace(); } JFileChooser
	 * saveFileFrame = new JFileChooser();
	 * 
	 * 
	 * saveFileFrame.setSelectedFile(new File(fileName)); int values =
	 * saveFileFrame.showSaveDialog(null);
	 * saveFileFrame.setDialogTitle("另存为..."); saveFileFrame.setVisible(true);
	 * 
	 * if(values == 1){ return false; } String absolutePath =
	 * saveFileFrame.getSelectedFile().getAbsolutePath();
	 * System.out.println(absolutePath);
	 * 
	 * File file = new File(absolutePath);
	 * 
	 * 
	 * 
	 * if(!file.exists()){ try { if(!file.createNewFile()){
	 * JOptionPane.showMessageDialog(null, "文件保存失败！", "提示", 2); return false; }
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return false; } }else{
	 * if(JOptionPane.showConfirmDialog(null, "存在同名文件，确定覆盖？", "提示",
	 * JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){ return false; } }
	 * int index = absolutePath.lastIndexOf("."); String suffix =
	 * absolutePath.substring(index + 1);
	 * 
	 * boolean flag = false;
	 * 
	 * Workbook wb = null; if ("xls".equals(suffix)) { wb = new HSSFWorkbook();
	 * } else if ("xlsx".equals(suffix)) { wb = new XSSFWorkbook(); } else {
	 * return false; } Sheet sheet = wb.createSheet();
	 * 
	 * // 生成除标题行外的数据 int size = list.size(); for (int k = 0; k < size; k++) {
	 * 
	 * Row row = sheet.createRow(k);
	 * 
	 * int size_2 = list.get(k).size(); for (int i = 0; i < size_2; i++) {
	 * String tempString = list.get(k).get(i);
	 * row.createCell(i).setCellValue(tempString); } }
	 * 
	 * try { FileOutputStream out = new FileOutputStream(absolutePath);
	 * wb.write(out); out.close(); wb.close(); flag = true; } catch
	 * (FileNotFoundException e) { JOptionPane.showMessageDialog(null,
	 * "文件打开中，不能被覆盖或保存文件！", "提示", JOptionPane.YES_OPTION); e.printStackTrace();
	 * } catch (IOException e) { e.printStackTrace(); } finally { try {
	 * wb.close(); } catch (IOException e) { e.printStackTrace(); } } return
	 * flag; }
	 */

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
		} else if ("xlsx".equals(suffix)||StringUtil.SUFFIX_EXECL.equals(suffix)) {
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
	 * @param firstDyadic 将会填充excel表的第一个sheet的二维数组
	 * @param secondDyadic 将会填充excel表的第二个sheet的二维数组
	 * @param absolutePath 导出的文件的路径+文件名，文件拓展名.xlsx或.xls都可以
	 * @param isfirstSheetHidden 是否隐藏第一个sheet
	 * @param isSecondSheetHidden 是否隐藏第二个sheet
	 * @return 运行状态
	 * @author LiangYH
	 */
	public boolean exportForm(List<ArrayList<String>> firstDyadic, boolean isFirstSheetHidden,
			List<ArrayList<String>> secondDyadic, boolean isSecondSheetHidden, String absolutePath) {

		int index = absolutePath.lastIndexOf(".");
		String suffix = absolutePath.substring(index + 1);

		boolean flag = false;

		Workbook wb = null;
		if ("xls".equals(suffix)) {
			wb = new HSSFWorkbook();
		} else if ("xlsx".equals(suffix)||StringUtil.SUFFIX_EXECL.equals(suffix)) {
			wb = new XSSFWorkbook();
		} else {
			return false;
		}
		
		Sheet sheet = wb.createSheet();
		
		if(firstDyadic != null){
			int size = firstDyadic.size();
			for (int k = 0; k < size; k++) {
				
				Row row = sheet.createRow(k);
				
				int size_2 = firstDyadic.get(k).size();
				for (int i = 0; i < size_2; i++) {
					String tempString = firstDyadic.get(k).get(i);
					row.createCell(i).setCellValue(tempString);
				}
			}
		}
		
		if(secondDyadic != null){
			Sheet secondSheet = wb.createSheet();
			int secondDyadicSize = secondDyadic.size();
			for(int i = 0; i < secondDyadicSize; i++){
				Row row = secondSheet.createRow(i);
				int secondSize2 = secondDyadic.get(i).size();
				for(int j = 0; j < secondSize2; j++){
					String tempString = secondDyadic.get(i).get(j);
					row.createCell(j).setCellValue(tempString);
				}
			}
		}
		
		wb.setSheetHidden(0, isFirstSheetHidden);
		wb.setSheetHidden(1, isSecondSheetHidden);
		
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
	 * @return runStatus
	 * 
	 * note：
	 * 第一个sheet可见，其他的sheet不可见，
	 * 可以用exportForm(List<ArrayList<String>> list, String absolutePath)方法测试一下是否成功导入
	 */
	public boolean exportForm(String absolutePath, List<ArrayList<String>>... dyadics){
		
		int index = absolutePath.lastIndexOf(".");
		String suffix = absolutePath.substring(index + 1);

		boolean flag = false;

		Workbook wb = null;
		if ("xls".equals(suffix)) {
			wb = new HSSFWorkbook();
		} else if ("xlsx".equals(suffix)||StringUtil.SUFFIX_EXECL.equals(suffix)) {
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
			//隐藏sheet
			wb.setSheetHidden(i, false);
			
			int dyadicSize = dyadic.size();
			for(int k = 0; k < dyadicSize; k++){
				Row row = sheet.createRow(k);
				
				int size_2 = dyadic.get(k).size();
				for (int j = 0; j < size_2; j++) {
					String tempString = dyadic.get(k).get(j);
					row.createCell(j).setCellValue(tempString);
				}
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
	 * 导入excel表中的数据，并将这些数据存放到一个二位数组中
	 * 
	 * @param filePath
	 *            包含文件名
	 * @param sheetIndex
	 *            excel 表中sheet的位置 默认从0开始
	 * @return 带有excel表中的数据的二维数组
	 * @author LiangYiHuai
	 */
	public List<ArrayList<String>> importForm(String filePath, int sheetIndex) {
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		Workbook wb = null;
		try {
			InputStream inp = new FileInputStream(filePath);
			wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(sheetIndex);

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
						Cell tempCell = tempRow.getCell(k,
								Row.CREATE_NULL_AS_BLANK);

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

		return list;
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
	
	
	/**
	 * 创建文件
	 * 
	 * @return 创建成功就放回true，创建失败就返回false
	 */
	private boolean findSaving(String fileName) {
		JFileChooser saveFileFrame = new JFileChooser();
		saveFileFrame.setSelectedFile(new File(fileName));
		int values = saveFileFrame.showSaveDialog(null);
		saveFileFrame.setDialogTitle("另存为...");
		saveFileFrame.setVisible(true);

		if (values == 1) {
			return false;
		}
		File file = new File(saveFileFrame.getSelectedFile().getAbsolutePath());

		if (!file.exists()) {
			try {
				if (!file.createNewFile()) {
					JOptionPane.showMessageDialog(null, "文件保存失败！", "提示", 2);
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		} else {
			if (JOptionPane.showConfirmDialog(null, "存在同名文件，确定覆盖？", "提示",
					JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 读取office word文件中的表格中的数据，拓展名.doc或.docx均可
	 * 
	 * @param fileName包括路径和文件名
	 * @return 返回的是word文件中的表格里面的数据 这里为数组的形式，其中第一行是表格的标题行
	 * @throws IOException
	 * @author LiangYH
	 */
	public List<ArrayList<String>> importTableInWordFile(String fileName)
			throws IOException {
		List<ArrayList<String>> dyadic = new ArrayList<ArrayList<String>>();

		// 判断文件是否为word文件
		int index = -1;
		if ((index = fileName.lastIndexOf(".")) != -1) {
			String extension = fileName.substring(index + 1);

			if ("doc".equals(extension)) {
				// 以doc为拓展名的文件是office 2003本版的
				dyadic = this.readHWPFTable(fileName);
			} else if ("docx".equals(extension)) {
				// 以docx为拓展名的文件是office2007及以上本版的
				dyadic = this.readXWPFTable(fileName);
			}
		}
		return dyadic;
	}

	/**
	 * 读取以.doc为拓展名的word文档
	 * 
	 * @param fileName包括路径和文件名
	 * @return 返回的是word文件中的表格里面的数据 这里为数组的形式，其中第一行是表格的标题行
	 * @throws IOException
	 */
	private List<ArrayList<String>> readXWPFTable(String fileName)
			throws IOException {
		List<ArrayList<String>> dyadic = new ArrayList<ArrayList<String>>();

		FileInputStream in = new FileInputStream(fileName);
		XWPFDocument doc = new XWPFDocument(in);
		// 得到doc表中的表格对象数组
		List<XWPFTable> tables = doc.getTables();
		// 遍历表格对象数组
		for (XWPFTable table : tables) {
			// 取得表格中的行
			List<XWPFTableRow> rows = table.getRows();
			// 第一行的列数
			int firstRowNum = rows.get(0).getTableCells().size();
			// 遍历得到表格的行对象
			for (XWPFTableRow tr : rows) {

				List<XWPFTableCell> tableCells = tr.getTableCells();
				// 如果列数比第一行的列数少，那么就不读取
				if (tableCells.size() < firstRowNum) {
					continue;
				}
				ArrayList<String> list = new ArrayList<String>();
				// 遍历每一行，得到每一行中的单元格
				for (XWPFTableCell tc : tableCells) {
					// \\s表示 空格,回车,换行等空白符
					String text = tc.getText().replaceAll("\\s*", "");
					list.add(text);
				}
				dyadic.add(list);
			}
		}
		return dyadic;
	}

	/**
	 * 读取以.doc为拓展名的word文档
	 * 
	 * @param fileName包括路径和文件名
	 * @return 返回的是word文件中的表格里面的数据 这里为数组的形式，其中第一行是表格的标题行
	 * @throws IOException
	 */
	private List<ArrayList<String>> readHWPFTable(String fileName)
			throws IOException {
		List<ArrayList<String>> dyadic = new ArrayList<ArrayList<String>>();

		InputStream is = new FileInputStream(fileName);
		// 取得documen对象，也就是word文档
		HWPFDocument doc = new HWPFDocument(is);
		// 取得范围对象，也就是整个word文件
		Range range = doc.getRange();

		// 遍历range范围内的tables。
		TableIterator tableIter = new TableIterator(range);
		Table table;
		TableRow row;
		TableCell cell;

		while (tableIter.hasNext()) {
			table = tableIter.next();
			// 取得table的行数
			int rowNum = table.numRows();
			// 第一行的列数
			int firstColNum = table.getRow(0).numCells();
			// 遍历得到行
			for (int j = 0; j < rowNum; j++) {
				row = table.getRow(j);
				int cellNum = row.numCells();
				// 如果列数小于第一行的列数，则跳过
				if (cellNum < firstColNum) {
					continue;
				}
				// 用于存放着一行的数据
				ArrayList<String> list = new ArrayList<String>();
				for (int k = 0; k < cellNum; k++) {
					cell = row.getCell(k);
					// \\s表示 空格,回车,换行等空白符
					String text = cell.text().trim().replaceAll("\\s*", "");
					list.add(text);
				}
				dyadic.add(list);
			}
		}
		return dyadic;
	}

	/**
	 * 读取word文档中的前四段文字的内容,拓展名.doc或.docx均可
	 * 
	 * @param fileName包括路径和文件名
	 * @return键：FLDH（发料单号）
	 * @throws IOException
	 * @author LiangYH
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public Map<String, String> readWordParagraph(String fileName)
			throws IOException, InvalidFormatException {

		Map<String, String> map = new HashMap<String, String>();
		
		FileInputStream input = new FileInputStream(fileName);
		// 根据文件名判断是否word文档
		int index = -1;
		if ((index = fileName.lastIndexOf(".")) != -1) {
			String extension = fileName.substring(index + 1);

			if ("doc".equals(extension)) {
				// 以doc为拓展名的文件是office 2003本版的
				this.readWordParagraphOfHWPF(input, map);
			} else if ("docx".equals(extension)) {
				// 以docx为拓展名的文件是office2007及以上本版的
				this.readWordParagraphOfXWPF(input, map);
			}
		}
		return map;
	}

	/**
	 * 读取word文档中的前四段文字的内容,这个针对2003本版的
	 * 
	 * @param fileName包括路径和文件名
	 * @param map 获取的数据将会放在这里（类似于指针），键：FLDH（发料单号）
	 * @return
	 * @throws IOException
	 */
	private void readWordParagraphOfHWPF(FileInputStream input, Map<String, String> map)
			throws IOException {
//		Map<String, String> map = new HashMap<String, String>();

		// FileInputStream input = new FileInputStream(fileName);

		HWPFDocument document = new HWPFDocument(input);

		Range range = document.getRange();
		int num = range.numParagraphs();
		for (int i = 0; i < num; i++) {
			if (i > 4) {
				break;
			}
			Paragraph paragraph = range.getParagraph(i);
			String text = paragraph.text().replaceAll("\\s*", "");

			// 发料单号
			String FLDH = "";
			int index = -1;
			// 判断“发料单号”后面是否紧跟着中文或者英文冒号
			if ((index = text.indexOf("案由文号：")) != -1
					|| (index = text.indexOf("案由文号:")) != -1) {
				index += 5;
				FLDH = text.substring(index,text.indexOf("发"));
				map.put("案由文号", FLDH);
			} else if ((index = text.indexOf("案由文号")) != -1) {
				index += 4;
				FLDH = text.substring(index,text.indexOf("发"));
				map.put("案由文号", FLDH);
			}
			if ((index = text.indexOf("发料单号：")) != -1
					|| (index = text.indexOf("发料单号:")) != -1) {
				index += 5;
				FLDH = text.substring(index);
				map.put("发料单号", FLDH);
			} else if ((index = text.indexOf("发料单号")) != -1) {
				index += 4;
				FLDH = text.substring(index);
				map.put("发料单号", FLDH);
			}
			if ((index = text.indexOf("运输方式：")) != -1
					|| (index = text.indexOf("运输方式:")) != -1) {
				index += 5;
				FLDH = text.substring(index,text.indexOf("运单"));
				map.put("运输方式", FLDH);
			} else if ((index = text.indexOf("运输方式")) != -1) {
				index += 4;
				FLDH = text.substring(index,text.indexOf("运单"));
				map.put("运输方式", FLDH);
			}
			if ((index = text.indexOf("运单编号：")) != -1
					|| (index = text.indexOf("运单编号:")) != -1) {
				index += 5;
				FLDH = text.substring(index);
				map.put("运单编号", FLDH);
			} else if ((index = text.indexOf("运单编号")) != -1) {
				index += 4;
				FLDH = text.substring(index);
				map.put("运单编号", FLDH);
			}
		}
	}

	/**
	 * 读取word文档中的前四段文字的内容,这个是针对2007及以上本版的
	 * 
	 * @param fileName包括路径和文件名
	 * @param map 获取的数据将会放在这里（类似于指针），键：FLDH（发料单号）
	 * @return
	 * @throws IOException
	 */
	private void readWordParagraphOfXWPF(FileInputStream input, Map<String, String> map)
			throws IOException, InvalidFormatException {
//		Map<String, String> map = new HashMap<String, String>();

		// FileInputStream input = new FileInputStream(fileName);
		// 获得XWPFDocument对象
		XWPFDocument doc = new XWPFDocument(input);
		// 获得第一个段落对象
		Iterator<XWPFParagraph> iter = doc.getParagraphsIterator();
		int count = 0;
		while (iter.hasNext()) {
			if (count > 4) {
				break;
			}
			XWPFParagraph paragraph = iter.next();
			String text = paragraph.getText().trim().replaceAll("\\s*", "");

			// 发料单号
			String FLDH = "";
			int index = -1;
			// 判断“发料单号”后面是否紧跟着中文或者英文冒号
			if ((index = text.indexOf("案由文号：")) != -1
					|| (index = text.indexOf("案由文号:")) != -1) {
				index += 5;
				FLDH = text.substring(index,text.indexOf("发"));
				map.put("案由文号", FLDH);
			} else if ((index = text.indexOf("案由文号")) != -1) {
				index += 4;
				FLDH = text.substring(index,text.indexOf("发"));
				map.put("案由文号", FLDH);
			}
			if ((index = text.indexOf("发料单号：")) != -1
					|| (index = text.indexOf("发料单号:")) != -1) {
				index += 5;
				FLDH = text.substring(index);
				map.put("发料单号", FLDH);
			} else if ((index = text.indexOf("发料单号")) != -1) {
				index += 4;
				FLDH = text.substring(index);
				map.put("发料单号", FLDH);
			}
			if ((index = text.indexOf("运输方式：")) != -1
					|| (index = text.indexOf("运输方式:")) != -1) {
				index += 5;
				FLDH = text.substring(index,text.indexOf("运单"));
				map.put("运输方式", FLDH);
			} else if ((index = text.indexOf("运输方式")) != -1) {
				index += 4;
				FLDH = text.substring(index,text.indexOf("运单"));
				map.put("运输方式", FLDH);
			}
			if ((index = text.indexOf("运单编号：")) != -1
					|| (index = text.indexOf("运单编号:")) != -1) {
				index += 5;
				FLDH = text.substring(index);
				map.put("运单编号", FLDH);
			} else if ((index = text.indexOf("运单编号")) != -1) {
				index += 4;
				FLDH = text.substring(index);
				map.put("运单编号", FLDH);
			}
		}
	}
	
	
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
	
	// 这个路径是模板的所在路径
	public static final String TEMPLET_WORD_FILE = "SampleFile/OutListWord.docx";
	public void writeXWPFFile(String fileName, Map<String, String> map,
			List<ArrayList<String>> dyadic) throws IOException {
		
		String url = ApplyFormOperation.class.getClassLoader().getResource(TEMPLET_WORD_FILE).getPath();
		FileInputStream in = new FileInputStream(url);
		XWPFDocument doc = new XWPFDocument(in);
		// 获得第一个段落对象
		Iterator<XWPFParagraph> iter = doc.getParagraphsIterator();
		// 标记文字段落
		int count = 0;
		while (iter.hasNext()) {
			if (count > 5) {
				break;
			}
			XWPFParagraph paragraph = iter.next();
			// count变量表示第几个文字段落
			if (count == 2) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (int i = 0; i < runs.size(); i++) {
					// 取得这个XWPFRun的字符串
					// String str =
					// runs.get(i).getText(runs.get(i).getTextPosition()).trim();
					String str = runs.get(i).text().replaceAll("\\s*", "");
					if ("#".equals(str)) {
						XWPFRun tempRun = runs.get(i);
						// 替换掉井号
						tempRun.setText(map.get("AYWH"), 0);
						for (int k = 0; k < 4; k++) {
							tempRun.addTab();
						}
					} else if ("*".equals(str)) {
						XWPFRun tempRun = runs.get(i);
						// 替换掉星号
						tempRun.setText(map.get("FLDH"), 0);
					}
				}
			} else if (count == 3) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (int i = 0; i < runs.size(); i++) {
					// String str =
					// runs.get(i).getText(runs.get(i).getTextPosition()).trim();
					String str = runs.get(i).text().replaceAll("\\s*", "");
					if ("#".equals(str)) {
						XWPFRun tempRun = runs.get(i);
						tempRun.setText(map.get("YSFS"), 0);
						for (int k = 0; k < 4; k++) {
							tempRun.addTab();
						}
					} else if ("*".equals(str)) {
						XWPFRun tempRun = runs.get(i);
						tempRun.setText(map.get("YDBH"), 0);
					}
				}
			} else if (count == 5) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (int i = 0; i < runs.size(); i++) {
//					String str = runs.get(i).getText(runs.get(i).getTextPosition()).trim();
					String str = runs.get(i).text().replaceAll("\\s*", "");
					if ("共".equals(str)) {
						XWPFRun run = paragraph.insertNewRun(i + 1);
						String tempString = map.get("totalPage");
						run.setText(tempString);
					}
					if ("第".equals(str)) {
						XWPFRun run = paragraph.insertNewRun(i + 1);
						run.setText(map.get("currentPage"));
					}
				}
			}
			count++;
		}
		// 得到doc表中的表格对象数组
		List<XWPFTable> tables = doc.getTables();
		// 遍历表格对象数组
		for (XWPFTable table : tables) {
			// 暂时保存最后一行的对象
			XWPFTableRow lastRow = table.getRow(7);
			// 新增的表格的行数
			int newRowSize = 0;
			if (dyadic.size() > 5) {
				newRowSize = dyadic.size() - 5;
			}
			for (int i = 0; i < newRowSize; i++) {
				// 增加新的行对象
				XWPFTableRow newRow = table.createRow();
				// 设置表格的高度，只是这个好像没有作用！！！！！
				newRow.setHeight(16);
			}
			// 把最后一行添加到表格的最后，然后把旧的删掉
			table.addRow(lastRow);
			table.removeRow(7);
			// 取得表格中的行
			List<XWPFTableRow> rows = table.getRows();
			// 取得一个paragraph的对象,作为模板文字段落
			XWPFParagraph templetParagraph = rows.get(1).getCell(0)
					.getParagraphs().get(0);
			// 最后一行不操作,所以rows.size()-1。 i从2开始,因为第二行作为模板行，最后会被删掉
			for (int i = 2; i < rows.size() - 1; i++) {
				int dyadicSize = dyadic.size();
				ArrayList<String> dataList = null;
				// 避免数组下标溢出和空指针异常
				if ((i - 2) >= dyadicSize) {
					break;
				}
				dataList = dyadic.get(i - 2);
				XWPFTableRow tr = rows.get(i);
				// 得到所在行中的单元格数组
				List<XWPFTableCell> tableCells = tr.getTableCells();
				// 遍历每一行，得到每一行中的单元格
				int size = tableCells.size();
				for (int k = 0; k < size; k++) {
					// 避免数组下标溢出
					if (k >= dataList.size()) {
						break;
					}
					String string = dataList.get(k);
					XWPFTableCell tc = tableCells.get(k);
					// 根据模板文字段落设置tc单元格的段落格式
					tc.setParagraph(templetParagraph);
					// 添加文字
					tc.setText(string);
				}
			}
			// 删除掉第二行
			table.removeRow(1);
		}
		// 生成word文件
		FileOutputStream out = new FileOutputStream(fileName);
		doc.write(out);
		out.close();
	}
	
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
//	public void writeXWordFile(String fileName, Map<String, String> map,
//			List<ArrayList<String>> dyadic){
//		FileOutputStream out = null;
//		FileInputStream in = null;
//		try{
//			String url = ApplyFormOperation.class.getClassLoader().getResource(TEMPLET_WORD_FILE).getPath();
//			in = new FileInputStream(url);
//			XWPFDocument doc = new XWPFDocument(in);
//			// 获得第一个段落对象
//			Iterator<XWPFParagraph> iter = doc.getParagraphsIterator();
//			// 标记文字段落
//			int count = 0;
//			while (iter.hasNext()) {
//				if (count > 5) {
//					break;
//				}
//				XWPFParagraph paragraph = iter.next();
//				// count变量表示第几个文字段落
//				if (count == 2) {
//					List<XWPFRun> runs = paragraph.getRuns();
//					for (int i = 0; i < runs.size(); i++) {
//						// 取得这个XWPFRun的字符串
//						// String str =
//						// runs.get(i).getText(runs.get(i).getTextPosition()).trim();
//						String str = runs.get(i).text().replaceAll("\\s*", "");
//						if ("#".equals(str)) {
//							XWPFRun tempRun = runs.get(i);
//							// 替换掉井号
//							tempRun.setText(map.get("AYWH"), 0);
//							for (int k = 0; k < 4; k++) {
//								tempRun.addTab();
//							}
//						} else if ("*".equals(str)) {
//							XWPFRun tempRun = runs.get(i);
//							// 替换掉星号
//							tempRun.setText(map.get("FLDH"), 0);
//						}
//					}
//				} else if (count == 3) {
//					List<XWPFRun> runs = paragraph.getRuns();
//					for (int i = 0; i < runs.size(); i++) {
//						// String str =
//						// runs.get(i).getText(runs.get(i).getTextPosition()).trim();
//						String str = runs.get(i).text().replaceAll("\\s*", "");
//						if ("#".equals(str)) {
//							XWPFRun tempRun = runs.get(i);
//							tempRun.setText(map.get("YSFS"), 0);
//							for (int k = 0; k < 4; k++) {
//								tempRun.addTab();
//							}
//						} else if ("*".equals(str)) {
//							XWPFRun tempRun = runs.get(i);
//							tempRun.setText(map.get("YDBH"), 0);
//						}
//					}
//				} else if (count == 5) {
//					List<XWPFRun> runs = paragraph.getRuns();
//					for (int i = 0; i < runs.size(); i++) {
////						String str = runs.get(i).getText(runs.get(i).getTextPosition()).trim();
//						String str = runs.get(i).text().replaceAll("\\s*", "");
//						if ("共".equals(str)) {
//							XWPFRun run = paragraph.insertNewRun(i + 1);
//							String tempString = map.get("totalPage");
//							run.setText(tempString);
//						}
//						if ("第".equals(str)) {
//							XWPFRun run = paragraph.insertNewRun(i + 1);
//							run.setText(map.get("currentPage"));
//						}
//					}
//				}
//				count++;
//			}
//			// 得到doc表中的表格对象数组
//			XWPFTable table = doc.getTables().get(0);
//			
//				// 取得表格中的行
//				List<XWPFTableRow> rows = table.getRows();
//				// 最后一行不操作,所以rows.size()-1。 i从2开始,因为第二行作为模板行，最后会被删掉
//				for (int i = 1; i < rows.size() - 1; i++) {
//					int dyadicSize = dyadic.size();
//					ArrayList<String> dataList = null;
//					// 避免数组下标溢出和空指针异常
//					if ((i - 1) >= dyadicSize) {
//						break;
//					}
//					dataList = dyadic.get(i - 1);
//					XWPFTableRow tr = rows.get(i);
//					// 得到所在行中的单元格数组
//					List<XWPFTableCell> tableCells = tr.getTableCells();
//					// 遍历每一行，得到每一行中的单元格
//					int size = tableCells.size();
//					for (int k = 0; k < size; k++) {
//						// 避免数组下标溢出
//						if (k >= dataList.size()) {
//							break;
//						}
//						String string = dataList.get(k);
//						XWPFTableCell tc = tableCells.get(k);
//						// 添加文字
//						tc.setText(string);
//					}
//				}
//			// 生成word文件
//			out = new FileOutputStream(fileName);
//			doc.write(out);
//		}catch(Exception  e){
//			e.printStackTrace();
//		}finally{
//			try {
//				out.close();
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}

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
	public void writeXWordFile(String fileName, Map<String, String> map,
			List<ArrayList<String>> dyadic){
		FileOutputStream out = null;
		FileInputStream in = null;
		try{
			String url = ApplyFormOperation.class.getClassLoader().getResource(TEMPLET_WORD_FILE).getPath();
			in = new FileInputStream(url);
			XWPFDocument doc = new XWPFDocument(in);
			
			//删除word模板多余的页
			List<IBodyElement> elements = doc.getBodyElements();
			int len = dyadic.size();
			int countFlag = 23;
			int tatolPageNum = 3;
			if(len <= 6){
				countFlag = 7;
				tatolPageNum = 1;
			}else if(len > 6 && len <= 12){
				countFlag = 15;
				tatolPageNum = 2;
			}
			int j = elements.size()-1; 
			while(j > countFlag){
				doc.removeBodyElement(j);
				j = doc.getBodyElements().size()-1;
			}
			
			//总共有tatolPageNum个table
			for(int y = 0; y < tatolPageNum; y++){
				// 得到doc表中的表格对象数组
				XWPFTable table = doc.getTables().get(y);
				
				// 取得表格中的行
				List<XWPFTableRow> rows = table.getRows();
				for (int i = 0; i < 6; i++) {
					// 避免数组下标溢出
					System.out.println(dyadic.size());
					if(i >= dyadic.size()){
						break;
					}
					if((y*6+i) >= dyadic.size()){
						break;
					}
					ArrayList<String> dataList = dyadic.get(y*6+i);
					XWPFTableRow tr = rows.get(i+1);
					// 得到所在行中的单元格数组
					List<XWPFTableCell> tableCells = tr.getTableCells();
					// 遍历每一行，得到每一行中的单元格
					int size = tableCells.size();
					for (int k = 0; k < size; k++) {
						// 避免数组下标溢出
						if (k >= dataList.size()) {
							break;
						}
						String string = dataList.get(k);
						XWPFTableCell tc = tableCells.get(k);
						// 添加文字
						tc.setText(string);
					}
				}
				
			}
			
			// 获得第一个段落对象
			Iterator<XWPFParagraph> iter = doc.getParagraphsIterator();
			// 标记文字段落
			int count = 0;
			while (iter.hasNext()) {
				XWPFParagraph paragraph = iter.next();
				
				// count变量表示第几个文字段落
				if (count == 2 || count == 9 || count == 16) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (int i = 0; i < runs.size(); i++) {
						// 取得这个XWPFRun的字符串
						String str = runs.get(i).text().trim();
						if ("#".equals(str)) {
							XWPFRun tempRun = runs.get(i);
							// 替换掉井号
							tempRun.setText(map.get("AYWH"), 0);
							
						} else if ("*".equals(str)) {
							XWPFRun tempRun = runs.get(i);
							// 替换掉星号
							tempRun.setText(map.get("FLDH"), 0);
						}
					}
				} else if (count == 3 || count == 10 || count == 17) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (int i = 0; i < runs.size(); i++) {
						String str = runs.get(i).text().trim();
						if ("#".equals(str)) {
							XWPFRun tempRun = runs.get(i);
							tempRun.setText(map.get("YSFS"), 0);
						} else if ("*".equals(str)) {
							XWPFRun tempRun = runs.get(i);
							tempRun.setText(map.get("YDBH"), 0);
						}
					}
				} else if (count == 5 || count == 12 || count == 19) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (int i = 0; i < runs.size(); i++) {
						String str = runs.get(i).text().trim();
						if ("共".equals(str)) {
							XWPFRun run = paragraph.insertNewRun(i + 1);
							run.setText(String.valueOf(tatolPageNum));
						}
						if ("第".equals(str)) {
							XWPFRun run = paragraph.insertNewRun(i + 1);
							
							int currentPage = 1;
							if(count == 12)
								currentPage = 2;
							else if(count == 19)
								currentPage = 3;
							
							run.setText(""+currentPage);
						}
					}
				}
				count++;
			}
			
			
			// 生成word文件
			out = new FileOutputStream(fileName);
			doc.write(out);
		}catch(Exception  e){
			e.printStackTrace();
		}finally{
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
