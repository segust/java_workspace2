package model;

/**
 * 算法的数据结构，专门用来存储SMO算法过程中的数据，参数，输入，输出和中间值等
 */
public class SMOStruct {

	public SMOStruct() {
	}

	public SMOStruct(float[][] dataMatIn, int[] labels, float[] alphas,
			float b, int m, float[] W, float[][] eCache) {
		this.dataMatIn = dataMatIn;
		this.labels = labels;
		
		
		this.alphas = alphas;
		this.b = b;
		this.m = m;
		this.W = W;
		this.eCache = eCache;// 用来保存误差Ei，它是一个二维向量，m行2列，每一列的第一个数字为0或者1，0代表这个Ei是无效的没有初始化。
	}

	public float[] getW() {
		return this.W;
	}

	public void setW(float[] W) {
		this.W = W;
	}

	public float[][] getDataMatIn() {
		return dataMatIn;
	}

	public void setDataMatIn(float[][] dataMatIn) {
		this.dataMatIn = dataMatIn;
	}

	public int[] getLabels() {
		return labels;
	}

	public void setLabels(int[] labels) {
		this.labels = labels;
	}

	public float[] getAlphas() {
		return alphas;
	}

	public void setAlphas(float[] alphas) {
		this.alphas = alphas;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public float[][] geteCache() {
		return eCache;
	}

	public void seteCache(float[][] eCache) {
		this.eCache = eCache;
	}

	private float[] W;// 分割超平面的w
	private float[][] dataMatIn;// 存放所有输入数据属性
	private int[] labels;// 存放所有输入数据标签
	private float[] alphas = new float[100];// SMO中的alpha
	private float b = 0;// SMO中的b
	private int m;// 数据长度
	private float[][] eCache;// E的缓存
}
