package cn.edu.cqupt.service.statistics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

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

import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.dao.OutApplyDAO;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;
public class ApplyFormOperation {
	/**
	 * 把二维数组导出成excel表
	 * @param list 装载导出数据的二维数组
	 * @param filePath 导出的路径
	 * @author LiangYiHuai
	 */
/*	public boolean exportForm(List<ArrayList<String>> list, String fileName) {
		try {
			//Windows风格
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception  e1) {
			e1.printStackTrace();
		}
		JFileChooser saveFileFrame = new JFileChooser();
		
		
		saveFileFrame.setSelectedFile(new File(fileName));
		int values = saveFileFrame.showSaveDialog(null);
		saveFileFrame.setDialogTitle("另存为...");
		saveFileFrame.setVisible(true);
		
		if(values == 1){
			return false;
		}
		String absolutePath  = saveFileFrame.getSelectedFile().getAbsolutePath();
		System.out.println(absolutePath);
		
		File file = new File(absolutePath);
		
		
		
		if(!file.exists()){
			try {
				if(!file.createNewFile()){
					JOptionPane.showMessageDialog(null, "文件保存失败！", "提示", 2);
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}else{
			if(JOptionPane.showConfirmDialog(null, "存在同名文件，确定覆盖？", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION){
				return false;
			}
		}
		int index = absolutePath.lastIndexOf(".");
		String suffix = absolutePath.substring(index + 1);
		
		boolean flag = false;

		Workbook wb = null;
		if ("xls".equals(suffix)) {
			wb = new HSSFWorkbook();
		} else if ("xlsx".equals(suffix)) {
			wb = new XSSFWorkbook();
		} else {
			return false;
		}
		Sheet sheet = wb.createSheet();

		// 生成除标题行外的数据
		int size = list.size();
		for (int k = 0; k < size; k++) {

			Row row = sheet.createRow(k);

			int size_2 = list.get(k).size();
			for (int i = 0; i < size_2; i++) {
				String tempString = list.get(k).get(i);
				row.createCell(i).setCellValue(tempString);
			}
		}

		try {
			FileOutputStream out = new FileOutputStream(absolutePath);
			wb.write(out);
			out.close();
			wb.close();
			flag = true;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "文件打开中，不能被覆盖或保存文件！", "提示", JOptionPane.YES_OPTION);
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
	}*/

	
	/**
	 * 把二维数组导出成excel表
	 * @param list 装载导出数据的二维数组
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

		int size = list.size();
		for (int k = 0; k < size; k++) {

			Row row = sheet.createRow(k);

			int size_2 = list.get(k).size();
			for (int i = 0; i < size_2; i++) {
				String tempString = list.get(k).get(i);
				row.createCell(i).setCellValue(tempString);
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
}
