package model;

public class Configuration {

	/**
	 * @param DATA_PATH 训练集路径
	 */
	public static final String DATA_PATH = "data//input.txt";
	
	/**
	 * @param TEST_PATH 测试集路径
	 */
	public static final String TEST_PATH = "data//test.txt";
	
	/**
	 * @param C SMO算法中的C参数，控制使分割间隔最大和所有的点都满足KKT条件
	 */
	public static float C = 0.6f;
	
	/**
	 * 一个接近0的数
	 */
	public static float TOLER = 0.00001f;
	
	/**
	 * @param MAX_ITER SMO最大迭代次数
	 */
	public static int MAX_ITER = 40;
}
