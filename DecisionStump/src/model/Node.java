package model;

import java.util.*;
import java.util.Map.Entry;

import util.Entropy;
import util.loadData;

public class Node {

	String attribute;

	double Gain = 0.0;
	int positiveNum = 0;
	int negativeNum = 0;
	String finalAtt;

	Set<Example> examples = new HashSet<Example>();
	Map<String, Set<Example>> kids = new HashMap<String, Set<Example>>();
	Set<String> attributes = new HashSet<String>();

	public Node(Set<Example> examples, String attribute) {
		this.examples = examples;
		this.attribute = attribute;
		attributes = examples.iterator().next().map.keySet();
	}

	public void grow() {

		System.out.println(attribute);

		for (Example E : examples) {
			if (E.getValue(loadData.Category).equals("yes"))
				positiveNum++;
			else
				negativeNum++;
		}

		// 根节点标志
		if (positiveNum == 0 || negativeNum == 0) {
			System.out.println(loadData.Category + ":" + (positiveNum == 0 ? "no" : "yes"));
			return;
		} else {

			giveBirth(examples);

			// 删去样本集中使用过的属性并生成新的结点
			for (Entry<String, Set<Example>> entry : kids.entrySet()) {
				Set<Example> newexamples = new HashSet<Example>();
				for (Example ex : entry.getValue()) {
					ex.map.remove(finalAtt);
					if (ex.map.size() == 0)
						break;
					newexamples.add(ex);
				}
				Node newNode = new Node(newexamples, entry.getKey());
				newNode.grow();

			}
		}

	}

	public void giveBirth(Set<Example> examples) {

		// 计算经验熵
		Entropy splitor = new Entropy(examples);
		Gain = splitor.CalEntropy();

		// 计算熵值
		// 遍历所有属性
		double max = 0.0;
		for (String attribute : attributes) {
			if (!attribute.equals(loadData.Category)) {
				String[] values = loadData.Attributes.get(attribute);
				/* 属性划分产生的不同集合 */
				int count = 0;
				HashMap<String, Set<Example>> exampleMap = new HashMap<String, Set<Example>>();
				for (int i = 0; i < values.length; i++) {
					Set<Example> set = new HashSet<Example>();
					exampleMap.put(values[i], set);
				}
				
				// 遍历所有样本
				for (Example example : examples) {
					// 判别样本归属
					String value = example.getValue(attribute);
					for (int i = 0; i < values.length; i++) {
						if (value.equals(values[i])) {
							exampleMap.get(values[i]).add(example);
							count++;
						}
					}
				}

				// 计算信息增益并确定最优划分属性finalAtt
				double Ent = 0.0;
				for (Entry<String, Set<Example>> entry : exampleMap.entrySet()) {
					Set<Example> set = (Set<Example>) entry.getValue();
					Entropy en = new Entropy(set);
					int n = set.size();
					double cal = en.CalEntropy();
					Ent += ((double) n / (double) count) * cal;
				}

				// 信息增益（ID3）
				double G = Gain - Ent;
				// // 信息增益率（C4.5）
				// double G = (Gain - Ent) / Ent;

				if (G > max) {
					finalAtt = attribute;
					max = Gain - Ent;
					kids = exampleMap;
				}
			}
		}
		System.out.println(finalAtt + "=?");

	}
}
