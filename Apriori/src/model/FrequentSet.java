package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import util.calSet;

public class FrequentSet {

	double support;// 支持度
	calSet calSet = new calSet();
	Set<Set<Integer>> InitialData = new HashSet<Set<Integer>>();// 初始数据
	LinkedHashMap<Set<Integer>, Integer> SupportTable = new LinkedHashMap<Set<Integer>, Integer>();
	Set<Set<Integer>> DustBin;

	public FrequentSet(Set<Set<Integer>> InitialData, double support) {
		this.InitialData = InitialData;
		this.support = support;
	}

	public LinkedHashMap<Set<Integer>, Integer> apriori() {
		HashMap<Set<Integer>, Integer> orgSupportTable = getSupportTable(InitialData);// 获取最初数据的支持度列表
		DustBin = new HashSet<Set<Integer>>();
		int count = 1;
		HashMap<Set<Integer>, Integer> iniSupportTable = trim(orgSupportTable);// 删除支持度不足的项
		do {
			count++;
			Set<Set<Integer>> Canditate = getNewCanditate(iniSupportTable);// 形成新的候选集
			HashMap<Set<Integer>, Integer> newSupportTable = getNewSupportTable(Canditate);// 计算候选集的支持度列表
			HashMap<Set<Integer>, Integer> newf = trim(newSupportTable);// 删去支持度不足的项
			if (newf.size() != 0) {
				System.out.println("频繁" + count + "项集：");
				for (Map.Entry<Set<Integer>, Integer> entry : newf.entrySet()) {
					SupportTable.put(entry.getKey(), entry.getValue());
					for (Integer att : entry.getKey()) {
						System.out.print(att + "  ");
					}
					System.out.println(":" + entry.getValue());
				}
			}
			iniSupportTable = newf;
		} while (iniSupportTable.size() != 0);
		return SupportTable;
	}

	//获取原始数据的支持度列表
	HashMap<Set<Integer>, Integer> getSupportTable(Set<Set<Integer>> iniData) {
		HashMap<Set<Integer>, Integer> SupportTable = new HashMap<Set<Integer>, Integer>();
		for (Set<Integer> example : iniData) {// 遍历初始数据每一行
			for (Integer att : example) {// 遍历初始数据每一个属性
				Set<Integer> attribute = new HashSet<Integer>();
				attribute.add(att);
				if (SupportTable.containsKey(attribute)) {
					Integer value = SupportTable.get(attribute);
					SupportTable.put(attribute, ++value);
				} else {
					SupportTable.put(attribute, 1);
				}
			}
		}
		return SupportTable;
	}

	// 去除支持度列表中上从一支持度列表中已知非频繁的数据和支持度不够的数据
	HashMap<Set<Integer>, Integer> trim(HashMap<Set<Integer>, Integer> SupportTable) {

		HashMap<Set<Integer>, Integer> newSupportTable = new HashMap<Set<Integer>, Integer>();

		// 除去本频繁集中支持度不够的项
		for (Map.Entry<Set<Integer>, Integer> entry : SupportTable.entrySet())
			if ((double) entry.getValue() / (double) InitialData.size() > support)
				newSupportTable.put(entry.getKey(), entry.getValue());
			else
				DustBin.add(entry.getKey());
		return newSupportTable;
	}

	// 从上一层支持度列表中获取新的候选集

	// 通过上一级支持度列表组合出新的候选集
	Set<Set<Integer>> getNewCanditate(HashMap<Set<Integer>, Integer> SupportTable) {

		Set<Set<Integer>> Canditate = new HashSet<Set<Integer>>();

		Set<Set<Integer>> keySet = SupportTable.keySet();
		Set<Set<Integer>> helpSet = new HashSet<Set<Integer>>();
		for (Set<Integer> se : SupportTable.keySet())
			helpSet.add(se);
		for (Set<Integer> mainkey : keySet) {
			helpSet.remove(mainkey);
			for (Set<Integer> elsekey : helpSet) {
				Set<Integer> helpkey = new HashSet<Integer>();
				for (Integer in : mainkey) {
					helpkey.add(in);
				}
				if (calSet.matching(mainkey, elsekey)) {
					helpkey.add(calSet.getLastValue(elsekey));
					Canditate.add(helpkey);
				}
			}
		}
		//删去DustBin中的集合
		Set<Set<Integer>> HelpSet=new HashSet<Set<Integer>>();
		for(Set<Integer> can:Canditate)
			HelpSet.add(can);
		for(Set<Integer> can:HelpSet){
			for(Set<Integer> dust:DustBin){
				if(can.containsAll(dust))
					Canditate.remove(can);
			}
		}
		return Canditate;
	}

	// 从候选集中获取新的支持度列表
	HashMap<Set<Integer>, Integer> getNewSupportTable(Set<Set<Integer>> Candidate) {
		HashMap<Set<Integer>, Integer> NewSupportTable = new HashMap<Set<Integer>, Integer>();
		for (Set<Integer> attributes : Candidate) {
			for (Set<Integer> example : InitialData) {
				if (example.containsAll(attributes)) {
					if (NewSupportTable.containsKey(attributes)) {
						int num = NewSupportTable.get(attributes);
						NewSupportTable.put(attributes, ++num);
					} else {
						NewSupportTable.put(attributes, 1);
					}
				}
			}
		}
		return NewSupportTable;
	}

}