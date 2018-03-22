package algorithm;

public class MainClass {
	public static void main(String[] args) {
		AprioriGen apriori = new AprioriGen();
		apriori.loadData(Configuration.DATA_PATH, " ");
		apriori.execute();
		apriori.printCandidates();
		RulesGen generate = new RulesGen();
		generate.setDataSet(apriori.getDataSet());
		generate.setCandidates(apriori.getCandidates());
		generate.generateRules();
		generate.print();
	}
}
