package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件操作类
 */
public class FileOperate {

	// 数据加载类
	public static void main(String[] args) {
		float[][] dataMatIn = new float[100][2];
		int[] labels = new int[100];
		dataMatIn = loadData("testSet.txt", "\t");
		labels = loadLabels("testSet.txt", "\t");
		for (int i = 0; i < 100; i++) {
			System.out.println(dataMatIn[i][0] + "   " + dataMatIn[i][1]
					+ "   " + labels[i]);
		}
	}

	/**
	 * 得到数据的属性
	 * @param name 文件名
	 * @return float数组
	 */
	public static float[][] loadData(String name, String split) {
		int length = 0;
		FileReader fr;
		try {
			BufferedReader br = new BufferedReader(new FileReader(name));
			while ((br.readLine()) != null) {
				length++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		float[][] dataMatIn = new float[length][2];
		try {
			fr = new FileReader(name);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			String[] tokens = new String[3];
			float x = 0;
			float y = 0;
			int i = 0;
			while ((line = br.readLine()) != null) {
				tokens = line.split(split);
				x = Float.parseFloat(tokens[0]);
				y = Float.parseFloat(tokens[1]);
				dataMatIn[i][0] = x;
				dataMatIn[i][1] = y;
				i++;
				if (i >= 100)
					break;
			}
			br.close();
			return dataMatIn;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到数据的label
	 * @param name 文件名
	 * @return int数组标记的label
	 */
	public static int[] loadLabels(String name, String split) {
		int length = 0;
		FileReader fr;
		try {
			BufferedReader br = new BufferedReader(new FileReader(name));
			while ((br.readLine()) != null) {
				length++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		int[] dataLabel = new int[length];
		try {
			fr = new FileReader(name);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			String[] tokens = new String[3];
			int label = 0;

			int i = 0;
			while ((line = br.readLine()) != null) {
				tokens = line.split(split);
				label = Integer.parseInt(tokens[2]);
				dataLabel[i] = label;
				i++;
				if (i >= 100)
					break;
			}
			br.close();
			return dataLabel;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
