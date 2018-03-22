package start;

import java.util.*;

import model.FrequentSet;
import model.RelationRules;
import util.readData;

public class Start {
	public static void main(String[] args) {
		readData reader = new readData();
		// System.out.println("��ȡ��Ģ�����ݣ�1�����߿�ʳ��Ģ�����ݣ�2��");
		// Scanner scan = new Scanner(System.in);
		// String clas = scan.nextLine();
		// System.out.println("����֧�ֶ�");
		// String support = scan.nextLine();
		// scan.close();
		Set<Set<Integer>> InitialData = reader.getInitialData("2");
		FrequentSet apriori = new FrequentSet(InitialData, 0.5);
		LinkedHashMap<Set<Integer>, Integer> FrequentLinkedHashMap = apriori.apriori();
		RelationRules relation = new RelationRules(FrequentLinkedHashMap, InitialData, 0.7);
		relation.getRelationRules();
	}
}
