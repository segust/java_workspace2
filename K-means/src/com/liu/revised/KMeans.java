package com.liu.revised;

import java.util.ArrayList;
import java.util.Random;

/**
 * K-均值聚类算法
 * 
 * @author lyt
 *
 */
public class KMeans {

	private int k; // 分成多少簇
	private int m; // 迭代次数
	private int dataSetLength; // 数据集元素个数，即数据集的长度
	private ArrayList<Point> dataSet; // 数据集链表
	private ArrayList<Point> center; // 中心链表
	private ArrayList<ArrayList<Point>> cluster; // 簇

	private ArrayList<Float> jc; // 误差平方和，k越接近dataSetlength误差越小
	private Random random;

	/**
	 * 设置需要分组的原始数据集
	 * 
	 * @param dataSet
	 */
	public void setDataSet(ArrayList<Point> dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * 获取结果分组
	 * 
	 * @return 结果集
	 */
	public ArrayList<ArrayList<Point>> getCluster() {
		return this.cluster;
	}

	/**
	 * 构造函数，传入需要分成的簇数量
	 * 
	 * @param k
	 * 
	 *            簇数量，若k<=0，设置为1，若k的数量大于源数据长度时，设置为源数据长度
	 */
	public KMeans(int k) {
		if (k <= 0) {
			k = 1;
		}
		this.k = k;
	}

	/**
	 * 初始化
	 */
	private void init() {
		m = 0;
		random = new Random();
		if (dataSet == null || dataSet.size() == 0) {
			initDataSet();
		}
		dataSetLength = dataSet.size();
		if (k > dataSetLength) {
			k = dataSetLength;
		}
		center = initCenters();
		cluster = initCluster();
		jc = new ArrayList<Float>();
	}

	/**
	 * 初始化簇集合
	 * 
	 * @return 一个分为k簇的空数据的簇集合
	 */
	private ArrayList<ArrayList<Point>> initCluster() {
		ArrayList<ArrayList<Point>> cluster = new ArrayList<ArrayList<Point>>();
		for (int i = 0; i < k; i++) {
			cluster.add(new ArrayList<Point>());
		}
		return cluster;
	}

	/**
	 * 初始化中心数据链表，分成多少簇就有多少个中心点
	 * 
	 * @return 中心点
	 */
	private ArrayList<Point> initCenters() {
		ArrayList<Point> center = new ArrayList<Point>();
		// 初始化时随机生成中心点
		boolean flag;
		int j;
		int[] randoms = new int[k];
		// 实际随机的是下标
		int temp = random.nextInt(dataSetLength);
		randoms[0] = temp;
		for (int i = 1; i < k; i++) {
			flag = true;
			while (flag) {
				temp = random.nextInt(dataSetLength);
				j = 0;
				// 判断重复
				while (j < i) {
					if (temp == randoms[j])
						break;
					j++;
				}
				if (j == i) {
					flag = false;
				}
			}
			randoms[i] = temp;
		}

		for (int i = 0; i < k; i++) {
			center.add(dataSet.get(randoms[i]));// 生成初始化中心链表
		}

		return center;
	}

	/**
	 * 如果调用者未初始化数据集，则采用内部初始数据
	 */
	private void initDataSet() {
		dataSet = new ArrayList<Point>();
		// 其中{6,3}是一样的，所以长度为15的数据集分成14簇和15簇的误差都为0
		float[][] dataSetArray = new float[][] { { 8.0f, 2.0f }, { 3.0f, 4.0f }, { 2.0f, 5.0f }, { 4.0f, 2.0f },
				{ 7.0f, 3.0f }, { 6.0f, 2.0f }, { 4.0f, 7.0f }, { 6.0f, 3.0f }, { 5.0f, 3.0f }, { 6.0f, 3.0f },
				{ 6.0f, 9.0f }, { 1.0f, 6.0f }, { 3.0f, 9.0f }, { 4.0f, 1.0f }, { 8.0f, 6.0f } };
		for (int i = 0; i < dataSetArray.length; i++) {
			Point point = new Point();
			point.setAttributes(dataSetArray[i]);
			dataSet.add(point);
			// dataSet.add(dataSetArray[i]);
		}
	}

	/**
	 * 计算点到簇心之间的距离
	 * 
	 * @param element
	 * @param center
	 * @return
	 */
	private float distance(Point element, Point center) {
		float distance = 0.0f;
		float sum = 0;
		if ((element.getAttributes().length == 0 && center.getAttributes().length == 0)
				|| element.getAttributes().length != center.getAttributes().length) {
			// 不做任何操作
			System.out.println("原始数据异常");
		} else {
			for (int i = 0; i < element.getAttributes().length; i++) {
				float dis = (float) Math.pow(element.getAttributes()[i] - center.getAttributes()[i], 2);
				sum += dis;
			}
			distance = (float) Math.sqrt(sum);
		}

		return distance;
	}

	/**
	 * 获取集合中最小距离的位置
	 * 
	 * @param distance
	 *            距离数组
	 * @return 最小距离在距离数组中的位置
	 */
	private int minDistance(float[] distance) {
		float minDistance = distance[0];
		int minLocation = 0;
		for (int i = 0; i < distance.length; i++) {
			if (distance[i] < minDistance) {
				minDistance = distance[i];
				minLocation = i;
			} else if (distance[i] == minDistance) {// 如果相等，随机返回一个位置
				if (random.nextInt(10) < 5) {
					minLocation = i;
				}
			}
		}
		return minLocation;
	}

	/**
	 * 核心，将当前元素放到最小距离中心相关的簇中
	 */
	public void clusterSet() {
		float[] distance = new float[k];
		// 将每个数据集元素划分到不同的簇中
		for (int i = 0; i < dataSetLength; i++) {
			for (int j = 0; j < k; j++) {
				distance[j] = distance(dataSet.get(i), center.get(j));
			}
			int minLocation = minDistance(distance);
			cluster.get(minLocation).add(dataSet.get(i));
		}
	}

	/**
	 * 计算亮点误差平方
	 * 
	 * @param element
	 * @param center
	 * @return
	 */
	public float errorSquare(Point element, Point center) {
		float errorSquare = 0.0f;
		if (element.getAttributes().length == 0 && center.getAttributes().length == 0
				|| element.getAttributes().length != center.getAttributes().length) {
			// do noting
			System.out.println("数据异常");
		} else {
			for (int i = 0; i < element.getAttributes().length; i++) {
				float dis = (float) Math.pow(element.getAttributes()[i] - center.getAttributes()[i], 2);
				errorSquare += dis;
			}
		}
		return errorSquare;
	}

	/**
	 * 计算误差平方和准则函数方法
	 */
	private void countRule() {
		float jcF = 0;
		for (int i = 0; i < cluster.size(); i++) {
			for (int j = 0; j < cluster.get(i).size(); j++) {
				jcF += errorSquare(cluster.get(i).get(j), center.get(i));
			}
		}
		jc.add(jcF);
	}

	/**
	 * 设置新的簇心的方法
	 */
	private void setNewCenter() {
		for (int i = 0; i < k; i++) {
			Point newCenter = new Point();
			int n = cluster.get(i).size();
			if (n != 0) {
				int attrLength = dataSet.get(0).getAttributes().length;
				float[] attrList = new float[attrLength];
				for (int j = 0; j < attrLength; j++) {
					for (int k = 0; k < n; k++) {
						// 计算每个点的特征属性值之和分别取均值
						attrList[j] += cluster.get(i).get(k).getAttributes()[j];
					}
					attrList[j] /= n;
				}
				newCenter.setAttributes(attrList);
				center.set(i, newCenter);
			}
		}
	}

	/**
	 * 打印数据
	 * 
	 * @param dataArray
	 *            数据集
	 * @param dataArrayName
	 *            数据集名字
	 */
	public void printDataArray(ArrayList<Point> dataArray, String dataArrayName) {
		for (int i = 0; i < dataArray.size(); i++) {
			/*
			 * System.out.println("print:"+ dataArrayName+"["+i+"]={"
			 * +dataArray.get(i)[0]+","+dataArray.get(i)[1]+"}");
			 */
			// System.out.println(dataArray.get(i)[0]+","+dataArray.get(i)[1]);
			for (int j = 0; j < dataArray.get(i).getAttributes().length; j++) {
				System.out.print(dataArray.get(i).getAttributes()[j] + ",");
			}
			System.out.println();
		}
		System.out.println("==================================");
	}

	/**
	 * kmeans核心工作过程
	 */
	private void kmeans() {
		init();
		boolean isConvergence = false;
		// 循环分组，直到误差不再变化
		while (!isConvergence) {
			clusterSet();
			countRule();
			// 误差不变，分组完成
			// m的非零判断很重要
			if (m != 0) {
				System.out.println("current jc is " + jc.get(m));
				if (jc.get(m) - jc.get(m - 1) == 0) {
					break;
				}
			}
			setNewCenter();
			m++;
			cluster.clear();
			cluster = initCluster();
		}
		System.out.println("note:the times of repeat:m=" + m);// 输出迭代次数
	}

	/**
	 * 执行算法
	 */
	public void execute() {
		long startTime = System.currentTimeMillis();
		System.out.println("kmeans begins:");
		kmeans();
		long endTime = System.currentTimeMillis();
		System.out.println("kmeans running time= " + (endTime - startTime) + "ms");
		System.out.println("the centroid are :");
		for (int i = 0; i < center.size(); i++) {
			for (int j = 0; j < center.get(0).getAttributes().length; j++) {
				System.out.print(center.get(i).getAttributes()[j] + " ");
			}
			System.out.println();
		}
		System.out.println("kmeans ends.");
		System.out.println();
	}
}
