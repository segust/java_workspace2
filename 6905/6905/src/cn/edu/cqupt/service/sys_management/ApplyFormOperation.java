package cn.edu.cqupt.service.sys_management;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.util.MyDateFormat;

public class ApplyFormOperation {
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
	public boolean importForm(String filePath, int sheetIndex) {
		ArrayList<Common9831> List9831 = new ArrayList<Common9831>();
		ArrayList<Common9831> temp9831 = new ArrayList<Common9831>();
		ArrayList<Common9831> tempUnit = new ArrayList<Common9831>();
		boolean flag = true;
		Workbook wb = null;
		try {
			HandleServiceOf9831 handleServiceOf9831 = new HandleServiceOf9831();
			InputStream inp = new FileInputStream(filePath);
			wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(sheetIndex);

			int firstRowNum = sheet.getFirstRowNum(); // 表格中开始有数据的行的索引
			Row biginRow = sheet.getRow(firstRowNum); // 表格中开始有数据的行
			int lastRowNum = sheet.getLastRowNum(); // 表格中最后一行的索引
			int firstColNum = biginRow.getFirstCellNum(); // 表格中开始有数据的第一列的索引
			int colNum = biginRow.getLastCellNum() - firstColNum; // 表格中数据的最后一列减去第一列
			if (colNum > 1) {
				for (int i = firstRowNum; i < lastRowNum + 1; i++) {
					Common9831 common9831 = new Common9831();
					Row tempRow = sheet.getRow(i);
					for (int k = firstColNum; k < colNum; k++) {
						String cellValue = this.getCellValue(tempRow, k);
						switch (k) {
						case 0:
							common9831.setPMNM(cellValue);
							break;
						case 1:
							common9831.setPMBM(cellValue);
							break;
						case 2:
							common9831.setQCBM(cellValue);
							break;
						case 3:
							common9831.setPMCS(cellValue);
							break;
						case 4:
							common9831.setXHTH(cellValue);
							break;
						case 5:
							common9831.setJLDW(cellValue);
							break;
						case 6:
							common9831.setCKDJ(cellValue);	//CKDJ-->CKJG
							break;
						case 7:
							common9831.setBZTJ(cellValue);
							break;
						case 8:
							common9831.setBZJS(cellValue);
							break;
						case 9:
							common9831.setBZZL(cellValue);
							break;
						case 10:
							common9831.setQCXS(cellValue);
							break;
						case 11:
							common9831.setMJYL(cellValue);
							break;
						case 12:
							common9831.setXHDE(cellValue);
							break;
						case 13:
							common9831.setXLDJ(cellValue);
							break;
						case 14:
							common9831.setSCCJNM(cellValue);
							break;
						case 15:
							common9831.setGHDWNM(cellValue);
							break;
						case 16:
							common9831.setZBSX(cellValue);
							break;
						case 17:
							common9831.setSCDXNF(cellValue);
							break;
						case 18:
							common9831.setLBQF(cellValue);
							break;
						case 19:
							common9831.setYJDBZ(cellValue);
							break;
						case 20:
							common9831.setSYBZ(cellValue);
							break;
						case 21:
							common9831.setSCBZ(cellValue);
							break;
						case 23:
							common9831.setZBBDSJ(cellValue);
							break;
						default:
							break;
						}
					}
					List9831.add(common9831);
					
					//添加qy_unit
					//以0开头的第一个字段开始计算，之前的都不统计
					if(temp9831.size() == 0){
						if(common9831.getQCBM().startsWith("0"))
							temp9831.add(common9831);
					}else if(temp9831.size() == 1){
						if(!common9831.getQCBM().startsWith("0")){
							tempUnit.add(common9831);
						}else{
							//连续两个0的情况
							if(tempUnit.size() > 0){
								String FKPMNM = temp9831.get(0).getPMNM();
								if(!handleServiceOf9831.addUnit(tempUnit, FKPMNM))
								flag = false;
							}
							temp9831.clear();
							tempUnit.clear();
							temp9831.add(common9831);
						}
					}
				}
				//循环结束，最后一个父类的数据
				//连续两个0的情况
				if(tempUnit.size() > 0){
					String FKPMNM = temp9831.get(0).getPMNM();
					if(!handleServiceOf9831.addUnit(tempUnit, FKPMNM));
					flag = false;
				}
				temp9831.clear();
				tempUnit.clear();
			}
			if(!handleServiceOf9831.add9831(List9831))
			flag = false;
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return flag;
	}
	
	/**
	 * switch，用来判断excel单元格中的数据是什么格式的 然后采用相应的方法来读取，否则会抛出异常
	 * 返回String类型
	 */
	public String getCellValue(Row tempRow,int k){
		Cell tempCell = tempRow.getCell(k,
				Row.CREATE_NULL_AS_BLANK);
		String cellValue="";
		switch (tempCell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			cellValue=tempCell.getRichStringCellValue()
					.getString().trim();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(tempCell)) {
				// 这里为日期格式化成字符串
				Date date = tempCell.getDateCellValue();
				String dateString = MyDateFormat
						.changeDateToString(date);
				cellValue=dateString;
			} else {
				tempCell.setCellType(Cell.CELL_TYPE_STRING);
				String tempString = tempCell
						.getStringCellValue().trim();
				if (tempString.indexOf(".") > -1) {
					cellValue=String.valueOf(
							new Double(tempString)).trim();
				} else {
					cellValue=tempString;
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue=String.valueOf(tempCell
					.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			tempCell.setCellType(Cell.CELL_TYPE_STRING);
			String tempString = tempCell.getStringCellValue()
					.trim();
			if (tempString != null) {
				tempString.replaceAll("#N/A", "").trim();
				cellValue=tempString;
			}
			break;
		case Cell.CELL_TYPE_ERROR:
			cellValue="";
			break;
		default:
			cellValue="";
		}
		return cellValue;
	}

}
