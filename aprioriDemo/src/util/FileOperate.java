package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 
 * 文件读写类
 */
public class FileOperate {
	
	private static BufferedReader br;
	
	/**
	 * 读取数据
	 * @param path
	 * @param split
	 * @return
	 */
	public static ArrayList<String[]> loadData(String path,String split) {
		
		ArrayList<String[]> dataSet = new ArrayList<String[]>();
		File file = new File(path);
		String encoding = "UTF-8";
		if(file.isFile() && file.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);
				br = new BufferedReader(read);
				String lineText = null;
				while((lineText = br.readLine()) != null) {
					String[] line = lineText.split(split);
					dataSet.add(line);
				}
				read.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("找不到文件路径！");
		}
		return dataSet;
	}
	
}
