package algorithm;

/**
 * 频繁项集生成类
 */

import java.util.ArrayList;
import java.util.List;

import model.Candidate;
import util.AlgorithmUtil;
import util.FileOperate;


public class AprioriGen {
	//保存交易记录，Set保存其有序性
	private ArrayList<ArrayList<String>> dataSet;
	//保存k长度的频繁项候选集
	private ArrayList<Candidate> candidates;
	
	public AprioriGen() {
		dataSet = new ArrayList<ArrayList<String>>();
		candidates = new ArrayList<Candidate>();
	}
	
	public void execute() {
		long startTime = System.currentTimeMillis();
		apriori();
		long endTime = System.currentTimeMillis();
		//System.out.println("����ʱ�䣺"+ (endTime - startTime));
	}
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
	/**
	 * 加载数据
	 * 转换格式
	 * @param path
	 * @param split
	 */
	public void loadData(String path,String split) {
		ArrayList<String[]> file = FileOperate.loadData(path, split);
		convertToList(file);
		
	}

	private void convertToList(ArrayList<String[]> file) {
		for (int i = 0; i < file.size(); i++) {
			ArrayList<String> temp = new ArrayList<String>();
			for (int j = 0; j < file.get(i).length; j++) {
				temp.add(file.get(i)[j]);
			}
			dataSet.add(temp);
		}
	}
	
	/**
	 * 创建初始长度为1的候选集合
	 * @return
	 */
	private ArrayList<Candidate> createCand1() {
		ArrayList<Candidate> cand = new ArrayList<Candidate>();
		List<String> cand1 = new ArrayList<String>();
		//找到所有不重复的长度为1的候选集
		for (int i = 0; i < dataSet.size(); i++) {
			ArrayList<String> row = dataSet.get(i);
			for (int j = 0; j < row.size(); j++) {
				String col = row.get(j);
				if(!cand1.contains(col)) {
					cand1.add(col);
				}
			}
		}
		//转换为候选集对象
		cand = convertToCandidate(cand, cand1);
		return cand;
	}

	private ArrayList<Candidate> convertToCandidate(ArrayList<Candidate> cand, List<String> cand1) {
		for (int i = 0; i < cand1.size(); i++) {
			Candidate candidate = new Candidate();
			ArrayList<String> candList = new ArrayList<String>();
			candList.add(cand1.get(i));
			candidate.setCand(candList);
			cand.add(candidate);
		}
		return cand;
	}
	
	/**
	 * 辅助扫描数据集函数
	 * 保证传入的候选集大于最小支持度
	 * @param candidates
	 * @return
	 */
	private ArrayList<Candidate> scanData(ArrayList<Candidate> candidates) {
		ArrayList<Candidate> cand = new ArrayList<Candidate>();
		for (int i = 0; i < dataSet.size(); i++) {
			for (int j = 0; j < candidates.size(); j++) {
				//判断每个候选集是否在每条记录里
				if(dataSet.get(i).containsAll(candidates.get(j).getCand())) {
					//增加该候选集的计数
					candidates.get(j).setCount(candidates.get(j).getCount()+1);
				}
			}
		}
		for (int i = 0; i < candidates.size(); i++) {
			//计算每一个候选集的置信度
			double confidence = (double)candidates.get(i).getCount()/dataSet.size();
			if(confidence > Configuration.MIN_SUPPORT) {
				cand.add(candidates.get(i));
			}
		}
		return cand;
	}
	
	/**
	 * 生成长度为k+1的频繁项集
	 * @param inputCand
	 * @param lenLk
	 * @return
	 */
	private ArrayList<Candidate> aprioriGen(List<Candidate> inputCand,int lenLk) {
		ArrayList<Candidate> retList = new ArrayList<Candidate>();
		for (int i = 0; i < inputCand.size(); i++) {
			List<String> tempL1 = new ArrayList<String>();
			List<String> tempL2 = new ArrayList<String>();
			for (int j = i+1; j < inputCand.size(); j++) {
				//如果不是L1
				if(lenLk > 2) {
					Candidate c1 = inputCand.get(i);
					Candidate c2 = inputCand.get(j);
					tempL1 = c1.getCand().subList(0, lenLk - 2);
					tempL2 = c2.getCand().subList(0, lenLk-2);
					if(AlgorithmUtil.compare(tempL1, tempL2)) {
						//取交集
						ArrayList<String> temp = new ArrayList<String>();
						temp.addAll(c1.getCand());
						temp.addAll(c2.getCand().subList(lenLk-2, c2.getCand().size()));
						Candidate cand = new Candidate();
						cand.setCand(temp);
						retList.add(cand);
					}
				}else {
					//如果为L1，则直接两两拼接
					ArrayList<String> L2 = new ArrayList<String>();
					L2.add(inputCand.get(i).getCand().get(0));
					L2.add(inputCand.get(j).getCand().get(0));
					Candidate cand2 = new Candidate();
					cand2.setCand(L2);
					retList.add(cand2);
				}
			}
		}
		return retList;
	}
	
	private void apriori() {
		//得到符合最小支持度的C1
		ArrayList<Candidate> c1 = scanData(createCand1());
		candidates.addAll(c1);
		ArrayList<Candidate> ck = new ArrayList<Candidate>();
		ck.addAll(c1);
		int k = 2;
		while(ck.size() > 0) {
			//新生成ck+1
			ck = scanData(aprioriGen(ck, k++));
			candidates.addAll(ck);
		}
	}
	
	public void printCandidates() {
		for (int i = 0; i < candidates.size(); i++) {
					System.out.print("{");
					for (int k = 0; k < candidates.get(i).getCand().size(); k++) {
						System.out.print(candidates.get(i).getCand().get(k)+",");
					}
					System.out.print("}");
					System.out.println();
		}
	}
	

	public ArrayList<ArrayList<String>> getDataSet() {
		return dataSet;
	}

	public ArrayList<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(ArrayList<Candidate> candidates) {
		this.candidates = candidates;
	}

	public void setDataSet(ArrayList<ArrayList<String>> dataSet) {
		this.dataSet = dataSet;
	}
	
}
