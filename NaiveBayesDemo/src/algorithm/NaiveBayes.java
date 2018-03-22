package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import model.Classification;
import model.Node;
/**
 * 训练朴素贝叶斯算法执行类
 * */
public class NaiveBayes {

	/**
	 * 训练朴素贝叶斯分类器
	 * */
	public Classification training(ArrayList<Node> trainingList){
		HashMap<String,Double> hamMap = new HashMap<String, Double>();//非垃圾短信单词表
		HashMap<String,Double> spamMap = new HashMap<String, Double>();//垃圾短信单词表
		HashSet<String> trainingSet = new HashSet<String>();//此长度为训练集不重复单词数
		double hamProbability = 0;//非垃圾短信比率
		double spamProbability = 0;//垃圾短信比率
		double hamWordsCount = 0;//非垃圾短信单词总数
		double spamWrodsCount = 0;//垃圾短信单词总数
		double hamCount = 0;//非垃圾短信数量
		double spamCount = 0;//垃圾短信数量
		double count = 0;//训练集不重复单词数
		ArrayList<String> hamWords = new ArrayList<String>();//非垃圾短信所有词
		ArrayList<String> spamWords = new ArrayList<String>();//垃圾短信所有词
		
		//1、建立单词集合
		for (int i = 0; i < trainingList.size(); i++) {//针对（矩阵）所有单词
			Node data = trainingList.get(i);//一条短信
			String type = data.getType();//短信类别
			String[] words = data.getMessage().split("\\s+");
			if(type.equals("ham")){
				hamCount++;//短信数量加1
				hamWordsCount += words.length;//单词数量加
				for (String word : words) {//赋值
					hamWords.add(word);
					trainingSet.add(word);
				}
			}else if(type.equals("spam")){
				spamCount++;//短信数量加1
				spamWrodsCount += words.length;//单词数量加
				for (String word : words) {
					spamWords.add(word);
					trainingSet.add(word);
				}
			}
		}
		
		//2、建立单词映射表
		for (String word : hamWords) {
			if(hamMap.containsKey(word)){
				hamMap.put(word, hamMap.get(word)+1);
			}else{
				hamMap.put(word, 1.0);
			}
		}
		for (String word : spamWords) {
			if(spamMap.containsKey(word)){
				spamMap.put(word, spamMap.get(word)+1);
			}else{
				spamMap.put(word, 1.0);
			}
		}
		
		//3、建立分类器
		hamProbability = hamCount/(hamCount+spamCount);//非垃圾短信概率
		spamProbability = spamCount/(hamCount+spamCount);//垃圾短信概率
		count = trainingSet.size();
		Classification classification = new Classification(hamMap, spamMap, 
				hamProbability, spamProbability, hamWordsCount, spamWrodsCount, count);
		
		return classification;
	}
	
	/**
	 * 测试朴素贝叶斯分类器
	 * */
	public void test(ArrayList<Node> testList, Classification classification){
		HashMap<String,Double> hamMap = classification.getHamMap();//非垃圾短信单词表
		HashMap<String,Double> spamMap = classification.getSpamMap();//垃圾短信单词表
		double hamProbability = classification.getHamProbability();//非垃圾短信比率
		double spamProbability = classification.getSpamProbability();//垃圾短信比率
		double hamWordsCount = classification.getHamWordsCount();//非垃圾短信单词数量
		double spamWordsCount = classification.getSpamWordsCount();//垃圾短信单词数量
		double count = classification.getCount();//训练集不重复单词数
		double correctCount = 0;//正确数量
		double rate = 0;//正确率
		
		for (Node data : testList) {//所有测试数据
			double hamRate = 1;//非垃圾短信概率
			double spamRate = 1;//垃圾短信概率
			String type = new String();//预测类型
			String[] words = data.getMessage().split("\\s+");
			
			//1、计算概率
			for (String word : words) {//一条测试数据
				if(hamMap.containsKey(word)){//计算成为非垃圾短信概率
					hamRate *= (hamMap.get(word)+1)/(hamWordsCount+count);
				}else{
					hamRate *= 1/(hamWordsCount+count);
				}
				if(spamMap.containsKey(word)){//计算成为垃圾短信概率
					spamRate *= (spamMap.get(word)+1)/(spamWordsCount+count);
				}else{
					spamRate *= 1/(spamWordsCount+count);
				}
			}
			hamRate *= hamProbability ;
			spamRate *= spamProbability ;
			hamRate = Math.log(hamRate);
			spamRate = Math.log(spamRate);
			
			//2、比较概率
			if(hamRate > spamRate){
				type = "ham";
			}else if(hamRate < spamRate){
				type = "spam";
			}else if(hamRate == spamRate){
				type = "unknown";
			}
			if(type.equals(data.getType()))
				correctCount++;
			
			if(!type.equals(data.getType())){
				System.out.println("真实结果："+data.getType());
				System.out.println("预测结果"+type);
				System.out.println("非垃圾"+hamRate);
				System.out.println("垃圾"+spamRate);
				System.out.println("-----------------");
			}//打印预测错误的
		}
		
		rate = correctCount/testList.size();
		System.out.println("不重复单词数："+count);
		System.out.println("测试集数量："+testList.size());
		System.out.println("预测正确的数量："+correctCount);
		System.out.println("预测错误的数量："+(testList.size()-correctCount));
		System.out.println("正确率："+rate);
	}
}
