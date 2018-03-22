package com.liu.revised;

import java.util.ArrayList;

public class KmeansTest {
	public static void main(String[] args) {

		KMeans k = new KMeans(2);
		// 设置数据转换格式
		/*
		 * ArrayList<Point> dataSet=ConvertData.convertToPoint(
		 * FileManager.readFile("E:\\testSet.txt","\t"));
		 */
		ArrayList<Point> dataSet = new ArrayList<Point>();
		dataSet.add(new Point().setAttributes(new float[] { 1, 2 }));
		dataSet.add(new Point().setAttributes(new float[] { 3, 3 }));
		dataSet.add(new Point().setAttributes(new float[] { 5, 6 }));
		dataSet.add(new Point().setAttributes(new float[] { 8, 9 }));
		dataSet.add(new Point().setAttributes(new float[] { 4, 5 }));

		// 设置原始数据集
		k.setDataSet(dataSet);
		// 执行算法
		k.execute();
		// 得到聚类结果
		ArrayList<ArrayList<Point>> cluster = k.getCluster();
		// 查看结果
		for (int i = 0; i < cluster.size(); i++) {
			k.printDataArray(cluster.get(i), "cluster[" + i + "]");
		}
	}
}
