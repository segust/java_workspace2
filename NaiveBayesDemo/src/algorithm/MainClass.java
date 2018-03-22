package algorithm;

import java.util.ArrayList;

import model.Classification;
import model.Node;
import util.FileOperate;
import util.AlgorithmUtil;
/**
 * 主函数类
 * */
public class MainClass {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		new MainClass();
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}
	
	public MainClass(){
		//1、初始化分类器
		Classification classification = new Classification();
		
		//2、读取数据
		String data_path = new Configuration().DATA_PATH;
		FileOperate dataOperation = new FileOperate();
		ArrayList<Node> dataList = dataOperation.loadData(data_path);
		
		//3、数据分类
		AlgorithmUtil randomGetter = new AlgorithmUtil();
		int[] trainingId = randomGetter.getTrainingId(4000,dataList.size());//训练集id
		int[] testId = randomGetter.getTestId(trainingId,dataList.size());//测试集id
		ArrayList<Node> trainingList = new ArrayList<Node>();//训练集
		ArrayList<Node> testList = new ArrayList<Node>();//测试集
		for (int i = 0; i < trainingId.length; i++) {//赋值
			trainingList.add(dataList.get(trainingId[i]));
		}
		for (int i = 0; i < testId.length; i++) {//赋值
			testList.add(dataList.get(testId[i]));
		}//经观察，训练集和测试集数据中有重复的信息
		
		//4、训练朴素贝叶斯分类器
		NaiveBayes naiveBayes = new NaiveBayes();
		classification = naiveBayes.training(trainingList);
		
		//5、测试
		naiveBayes.test(testList, classification);
	}
}
