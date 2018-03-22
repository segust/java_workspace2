package algorithm;

import util.FileOperate;
import model.Configuration;
import model.SMOStruct;

/**
 * 算法运行的主类，包括[加载数据-执行算法-输出结果]三个步骤
 */
public class MainClass {

	public static void main(String[] args) {
		
		float[][] dataMatIn = FileOperate.loadData(Configuration.DATA_PATH, "\t");// 加载数据
		float[][] testSet = FileOperate.loadData(Configuration.TEST_PATH, "\t");// 加载数据
		int[] labels = FileOperate.loadLabels(Configuration.DATA_PATH, "\t");// 加载标签
		int[] testLabels = FileOperate.loadLabels(Configuration.TEST_PATH, "\t");// 加载标签
		SMOStruct sStruct = new SMOStruct();// SMO算法运行过程中的数据、变量存放空间
		SMO smo = new SMO();
		// SMO主要运算过程
		sStruct = smo.run(dataMatIn, labels, sStruct);

		// 得到训练结果，主要是w和b两个参数
		float[] ws = sStruct.getW();
		float b = sStruct.getB();

		// 验证结果
		smo.test(testSet, testLabels, ws, b);

	}

}
