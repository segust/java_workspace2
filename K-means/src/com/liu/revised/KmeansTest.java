package com.liu.revised;

import java.util.ArrayList;

public class KmeansTest {
	public static void main(String[] args) {

		KMeans k = new KMeans(2);
		// ��������ת����ʽ
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

		// ����ԭʼ���ݼ�
		k.setDataSet(dataSet);
		// ִ���㷨
		k.execute();
		// �õ�������
		ArrayList<ArrayList<Point>> cluster = k.getCluster();
		// �鿴���
		for (int i = 0; i < cluster.size(); i++) {
			k.printDataArray(cluster.get(i), "cluster[" + i + "]");
		}
	}
}
