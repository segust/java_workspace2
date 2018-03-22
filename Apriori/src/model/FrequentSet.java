package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import util.calSet;

public class FrequentSet {

	double support;// ֧�ֶ�
	calSet calSet = new calSet();
	Set<Set<Integer>> InitialData = new HashSet<Set<Integer>>();// ��ʼ����
	LinkedHashMap<Set<Integer>, Integer> SupportTable = new LinkedHashMap<Set<Integer>, Integer>();
	Set<Set<Integer>> DustBin;

	public FrequentSet(Set<Set<Integer>> InitialData, double support) {
		this.InitialData = InitialData;
		this.support = support;
	}

	public LinkedHashMap<Set<Integer>, Integer> apriori() {
		HashMap<Set<Integer>, Integer> orgSupportTable = getSupportTable(InitialData);// ��ȡ������ݵ�֧�ֶ��б�
		DustBin = new HashSet<Set<Integer>>();
		int count = 1;
		HashMap<Set<Integer>, Integer> iniSupportTable = trim(orgSupportTable);// ɾ��֧�ֶȲ������
		do {
			count++;
			Set<Set<Integer>> Canditate = getNewCanditate(iniSupportTable);// �γ��µĺ�ѡ��
			HashMap<Set<Integer>, Integer> newSupportTable = getNewSupportTable(Canditate);// �����ѡ����֧�ֶ��б�
			HashMap<Set<Integer>, Integer> newf = trim(newSupportTable);// ɾȥ֧�ֶȲ������
			if (newf.size() != 0) {
				System.out.println("Ƶ��" + count + "���");
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

	//��ȡԭʼ���ݵ�֧�ֶ��б�
	HashMap<Set<Integer>, Integer> getSupportTable(Set<Set<Integer>> iniData) {
		HashMap<Set<Integer>, Integer> SupportTable = new HashMap<Set<Integer>, Integer>();
		for (Set<Integer> example : iniData) {// ������ʼ����ÿһ��
			for (Integer att : example) {// ������ʼ����ÿһ������
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

	// ȥ��֧�ֶ��б����ϴ�һ֧�ֶ��б�����֪��Ƶ�������ݺ�֧�ֶȲ���������
	HashMap<Set<Integer>, Integer> trim(HashMap<Set<Integer>, Integer> SupportTable) {

		HashMap<Set<Integer>, Integer> newSupportTable = new HashMap<Set<Integer>, Integer>();

		// ��ȥ��Ƶ������֧�ֶȲ�������
		for (Map.Entry<Set<Integer>, Integer> entry : SupportTable.entrySet())
			if ((double) entry.getValue() / (double) InitialData.size() > support)
				newSupportTable.put(entry.getKey(), entry.getValue());
			else
				DustBin.add(entry.getKey());
		return newSupportTable;
	}

	// ����һ��֧�ֶ��б��л�ȡ�µĺ�ѡ��

	// ͨ����һ��֧�ֶ��б���ϳ��µĺ�ѡ��
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
		//ɾȥDustBin�еļ���
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

	// �Ӻ�ѡ���л�ȡ�µ�֧�ֶ��б�
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