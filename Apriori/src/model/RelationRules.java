package model;

import java.util.*;

public class RelationRules {
	LinkedHashMap<Set<Integer>, Integer> FrequentLinkedHashMap = new LinkedHashMap<Set<Integer>, Integer>(10,
			(float) 0.75, true);
	Set<Set<Integer>> InitialData = new HashSet<Set<Integer>>();
	Double confidence;

	public RelationRules(LinkedHashMap<Set<Integer>, Integer> FrequentLinkedHashMap, Set<Set<Integer>> InitialData,
			double confidence) {
		this.FrequentLinkedHashMap = FrequentLinkedHashMap;
		this.InitialData = InitialData;
		this.confidence = confidence;
	}

	// ��ȡ��������
	public void getRelationRules() {

		Map<Set<Integer>, Set<Integer>> RelationRules = new HashMap<Set<Integer>, Set<Integer>>();// �������򼯺�
		Set<Set<Integer>> DustBin = new HashSet<Set<Integer>>();// ���ŶȲ�����Set

		System.out.println("��������");
		for (Map.Entry<Set<Integer>, Integer> Entry : FrequentLinkedHashMap.entrySet()) {// ��ȡ����Ƶ���
			int s = 0;
			Set<Integer> frequentSet = Entry.getKey();
			Integer frequentNum = Entry.getValue();
			if (frequentSet.size() != s)
				DustBin.clear();
			s = frequentSet.size();
			HashSet<Set<Integer>> ProperSubset = getProperSubset(frequentSet);// ��ȡFrequentList�����Ӽ�

			for (Set<Integer> SubSet : ProperSubset) {
				Set<Integer> result = new HashSet<Integer>();
				for (Integer se : frequentSet)
					result.add(se);// �������Ƶ����ĸ���result
				result.removeAll(SubSet);// ��ȡ������Ӽ���Ӧ�Ĺ���result

				boolean flag = false;
				for (Set<Integer> dust : DustBin)
					if (result.containsAll(dust)) {
						flag = true;
						break;
					}
				if (flag == true)
					continue;// �ж����SubSet�ǲ���DustBin��ļ��ϵ��Ӽ�
				int count = 0;
				for (Set<Integer> iniData : InitialData)
					if (iniData.containsAll(SubSet))
						count++;// ����ԭʼ���ݼ������
				if (((double) frequentNum / (double) count) >= confidence) {
					for (Integer rea : SubSet)
						System.out.print(rea + " ");
					System.out.print("��");
					for (Integer res : result)
						System.out.print(res + " ");
					System.out.println();
					RelationRules.put(SubSet, result);
				} else
					DustBin.add(result);
			}
		}

	}

	// ��ȡ���Ӽ�
	HashSet<Set<Integer>> getProperSubset(Set<Integer> oriSet) {

		ArrayList<Integer> oriList = new ArrayList<Integer>();
		HashSet<Set<Integer>> ProperSubset = new HashSet<Set<Integer>>();

		for (Integer inte : oriSet)
			oriList.add(inte);
		int listlength = 1 << oriList.size();
		for (int loop = 1; loop < listlength; loop++) {
			int index = 0;
			int temp = loop;
			HashSet<Integer> currentIntegerSet = new HashSet<Integer>();
			while (temp > 0) {
				if ((temp & 1) > 0)
					currentIntegerSet.add(oriList.get(index));
				temp >>= 1;
				index++;
			}
			if (currentIntegerSet.size() != oriSet.size())
				ProperSubset.add(currentIntegerSet);
		}
		return ProperSubset;
	}
}
