package algorithm;

import java.util.ArrayList;

import model.Candidate;
import model.Rule;

/**
 * 关联规则生成类
 */
public class RulesGen {
	//包含可信度的规则候选列表
	private ArrayList<Rule> bigRuleList;
	private ArrayList<ArrayList<String>> dataSet; 
	//k长度的频繁项集
	private ArrayList<Candidate> candidates;
	
	public RulesGen() {
		dataSet = new ArrayList<ArrayList<String>>();
		bigRuleList = new ArrayList<Rule>();
	}
	
	public ArrayList<Rule> getBigRuleList() {
		return bigRuleList;
	}

	public void setBigRuleList(ArrayList<Rule> bigRuleList) {
		this.bigRuleList = bigRuleList;
	}

	public ArrayList<ArrayList<String>> getDataSet() {
		return dataSet;
	}

	public void setDataSet(ArrayList<ArrayList<String>> dataSet) {
		this.dataSet = dataSet;
	}

	
	/**
	 *生成规则
	 */
	public void generateRules() {
		//得到初始规则
		ArrayList<Rule> rules = new ArrayList<Rule>();
		rules = convertToRule();
		//生成规则保存在bigList中
		createFirstRule(rules);
	}

	private void createFirstRule(ArrayList<Rule> rules) {
		for (int i = 0; i < rules.size(); i++) {
			if(rules.get(i).getLeft().size() >= 2) {
				rulesFormConseq(rules.get(i)); 
			}
		}
	}

	private ArrayList<Rule> convertToRule() {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		for (int i = 0; i < candidates.size(); i++) {
			ArrayList<String> cand = candidates.get(i).getCand();
			Rule rule = ConvertToRule(cand, new ArrayList<String>());
			rules.add(rule);
		}
		return rules;
	}
	
	/**
	 * 生成候选规则
	 * 如果左边候选集个数大于2
	 * 则分别分一个到右边，返回所有可能情况
	 */
	private void rulesFormConseq(Rule rule) {
		ArrayList<String> left = new ArrayList<String>();
		left.addAll(rule.getLeft());
		if(left.size() >= 2) {
			for (int j = 0; j < left.size(); j++) {
				String temp = left.get(j);
				ArrayList<String> tempL = new ArrayList<String>();
				ArrayList<String> tempR = new ArrayList<String>();
				tempR.addAll(rule.getRight());
				tempR.add(temp);
				tempL.addAll(left);
				tempL.remove(temp);
				Rule rule1 = new Rule();
				rule1 = ConvertToRule(tempL,tempR);
				if(calcConf(rule1))
					bigRuleList.add(rule1);
				rulesFormConseq(rule1);
			}
		}
	}
	
	private Rule ConvertToRule(ArrayList<String> tempL, ArrayList<String> tempR) {
		ArrayList<String> cand = new ArrayList<String>();
		Rule rule = new Rule();
		rule.setLeft(tempL);
		rule.setRight(tempR);
		cand.addAll(tempL);
		cand.addAll(tempR);
		rule.setCand(cand);
		return rule;
	}

	/**
	 * 评估生成规则
	 */
	private boolean calcConf(Rule rule) {
		boolean flag = false;
		//计算每个规则的置信度
		double joinp = 0;
		double p = 0;
		for (int j = 0; j < dataSet.size(); j++) {
			//若包含联合候选集，则同时也包含边缘候选集
			if(dataSet.get(j).containsAll(rule.getCand())) {
				rule.setCount(rule.getCount()+1);
				rule.setRightCount(rule.getRightCount()+1);
			}else if(dataSet.get(j).containsAll(rule.getRight())) {
				rule.setRightCount(rule.getRightCount()+1);
			}
		}
		joinp = (double)rule.getCount()/dataSet.size();
		p = (double)rule.getRightCount()/dataSet.size();
		double conf = joinp/p;
		if(conf > Configuration.MIN_CONF) {
			flag = true;
		}
		return flag;
	}

	public ArrayList<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(ArrayList<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	
	public void print() {
		for (int i = 0; i < bigRuleList.size(); i++) {
			Rule rule = bigRuleList.get(i);
			for (int j = 0; j < rule.getLeft().size(); j++) {
				System.out.print(rule.getLeft().get(j));
			}
			System.out.print("-->");
			for (int j = 0; j < rule.getRight().size(); j++) {
				System.out.print(rule.getRight().get(j));
			}
			System.out.println();
		}
	}
	
}
