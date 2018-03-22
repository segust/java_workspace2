package com.liu.revised;

import java.util.ArrayList;
import java.util.Random;

/**
 * K-��ֵ�����㷨
 * 
 * @author lyt
 *
 */
public class KMeans {

	private int k; // �ֳɶ��ٴ�
	private int m; // ��������
	private int dataSetLength; // ���ݼ�Ԫ�ظ����������ݼ��ĳ���
	private ArrayList<Point> dataSet; // ���ݼ�����
	private ArrayList<Point> center; // ��������
	private ArrayList<ArrayList<Point>> cluster; // ��

	private ArrayList<Float> jc; // ���ƽ���ͣ�kԽ�ӽ�dataSetlength���ԽС
	private Random random;

	/**
	 * ������Ҫ�����ԭʼ���ݼ�
	 * 
	 * @param dataSet
	 */
	public void setDataSet(ArrayList<Point> dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return �����
	 */
	public ArrayList<ArrayList<Point>> getCluster() {
		return this.cluster;
	}

	/**
	 * ���캯����������Ҫ�ֳɵĴ�����
	 * 
	 * @param k
	 * 
	 *            ����������k<=0������Ϊ1����k����������Դ���ݳ���ʱ������ΪԴ���ݳ���
	 */
	public KMeans(int k) {
		if (k <= 0) {
			k = 1;
		}
		this.k = k;
	}

	/**
	 * ��ʼ��
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
	 * ��ʼ���ؼ���
	 * 
	 * @return һ����Ϊk�صĿ����ݵĴؼ���
	 */
	private ArrayList<ArrayList<Point>> initCluster() {
		ArrayList<ArrayList<Point>> cluster = new ArrayList<ArrayList<Point>>();
		for (int i = 0; i < k; i++) {
			cluster.add(new ArrayList<Point>());
		}
		return cluster;
	}

	/**
	 * ��ʼ���������������ֳɶ��ٴؾ��ж��ٸ����ĵ�
	 * 
	 * @return ���ĵ�
	 */
	private ArrayList<Point> initCenters() {
		ArrayList<Point> center = new ArrayList<Point>();
		// ��ʼ��ʱ����������ĵ�
		boolean flag;
		int j;
		int[] randoms = new int[k];
		// ʵ����������±�
		int temp = random.nextInt(dataSetLength);
		randoms[0] = temp;
		for (int i = 1; i < k; i++) {
			flag = true;
			while (flag) {
				temp = random.nextInt(dataSetLength);
				j = 0;
				// �ж��ظ�
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
			center.add(dataSet.get(randoms[i]));// ���ɳ�ʼ����������
		}

		return center;
	}

	/**
	 * ���������δ��ʼ�����ݼ���������ڲ���ʼ����
	 */
	private void initDataSet() {
		dataSet = new ArrayList<Point>();
		// ����{6,3}��һ���ģ����Գ���Ϊ15�����ݼ��ֳ�14�غ�15�ص���Ϊ0
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
	 * ����㵽����֮��ľ���
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
			// �����κβ���
			System.out.println("ԭʼ�����쳣");
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
	 * ��ȡ��������С�����λ��
	 * 
	 * @param distance
	 *            ��������
	 * @return ��С�����ھ��������е�λ��
	 */
	private int minDistance(float[] distance) {
		float minDistance = distance[0];
		int minLocation = 0;
		for (int i = 0; i < distance.length; i++) {
			if (distance[i] < minDistance) {
				minDistance = distance[i];
				minLocation = i;
			} else if (distance[i] == minDistance) {// �����ȣ��������һ��λ��
				if (random.nextInt(10) < 5) {
					minLocation = i;
				}
			}
		}
		return minLocation;
	}

	/**
	 * ���ģ�����ǰԪ�طŵ���С����������صĴ���
	 */
	public void clusterSet() {
		float[] distance = new float[k];
		// ��ÿ�����ݼ�Ԫ�ػ��ֵ���ͬ�Ĵ���
		for (int i = 0; i < dataSetLength; i++) {
			for (int j = 0; j < k; j++) {
				distance[j] = distance(dataSet.get(i), center.get(j));
			}
			int minLocation = minDistance(distance);
			cluster.get(minLocation).add(dataSet.get(i));
		}
	}

	/**
	 * �����������ƽ��
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
			System.out.println("�����쳣");
		} else {
			for (int i = 0; i < element.getAttributes().length; i++) {
				float dis = (float) Math.pow(element.getAttributes()[i] - center.getAttributes()[i], 2);
				errorSquare += dis;
			}
		}
		return errorSquare;
	}

	/**
	 * �������ƽ����׼��������
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
	 * �����µĴ��ĵķ���
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
						// ����ÿ�������������ֵ֮�ͷֱ�ȡ��ֵ
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
	 * ��ӡ����
	 * 
	 * @param dataArray
	 *            ���ݼ�
	 * @param dataArrayName
	 *            ���ݼ�����
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
	 * kmeans���Ĺ�������
	 */
	private void kmeans() {
		init();
		boolean isConvergence = false;
		// ѭ�����飬ֱ�����ٱ仯
		while (!isConvergence) {
			clusterSet();
			countRule();
			// ���䣬�������
			// m�ķ����жϺ���Ҫ
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
		System.out.println("note:the times of repeat:m=" + m);// �����������
	}

	/**
	 * ִ���㷨
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
