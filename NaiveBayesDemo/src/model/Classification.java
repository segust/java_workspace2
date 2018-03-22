package model;

import java.util.HashMap;
/**
 * 朴素贝叶斯分类器
 * */
public class Classification {

	private HashMap<String,Double> hamMap;//非垃圾短信单词表
	private HashMap<String,Double> spamMap;//垃圾短信单词表
	private double hamProbability;//非垃圾短信比率
	private double spamProbability;//垃圾短信比率
	private double hamWordsCount;//非垃圾短信单词数量
	private double spamWordsCount;//垃圾短信单词数量
	private double count;//训练集不重复数
	
	public Classification() {
		super();
	}

	public Classification(HashMap<String, Double> hamMap,
			HashMap<String, Double> spamMap, double hamProbability,
			double spamProbability, double hamWordsCount,
			double spamWordsCount, double count) {
		super();
		this.hamMap = hamMap;
		this.spamMap = spamMap;
		this.hamProbability = hamProbability;
		this.spamProbability = spamProbability;
		this.hamWordsCount = hamWordsCount;
		this.spamWordsCount = spamWordsCount;
		this.count = count;
	}

	public HashMap<String, Double> getHamMap() {
		return hamMap;
	}

	public void setHamMap(HashMap<String, Double> hamMap) {
		this.hamMap = hamMap;
	}

	public HashMap<String, Double> getSpamMap() {
		return spamMap;
	}

	public void setSpamMap(HashMap<String, Double> spamMap) {
		this.spamMap = spamMap;
	}

	public double getHamProbability() {
		return hamProbability;
	}

	public void setHamProbability(double hamProbability) {
		this.hamProbability = hamProbability;
	}

	public double getSpamProbability() {
		return spamProbability;
	}

	public void setSpamProbability(double spamProbability) {
		this.spamProbability = spamProbability;
	}

	public double getHamWordsCount() {
		return hamWordsCount;
	}

	public void setHamWordsCount(double hamWordsCount) {
		this.hamWordsCount = hamWordsCount;
	}

	public double getSpamWordsCount() {
		return spamWordsCount;
	}

	public void setSpamWordsCount(double spamWordsCount) {
		this.spamWordsCount = spamWordsCount;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
}
