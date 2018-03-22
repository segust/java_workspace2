package Start;

import java.util.ArrayList;
import java.util.HashMap;

import model.SMS;
import model.Words;
import util.LoadData;

public class Main {
	public static void main(String args[]) {

		double PofSpam;
		double PofHam;
		int SpamNum;
		int HamNum;
		ArrayList<SMS> trainSMSList = new ArrayList<SMS>();
		ArrayList<SMS> testSMSList = new ArrayList<SMS>();
		HashMap<String, Words> WordsMap = new HashMap<String, Words>();
		Training trainer;
		double accuracy;

		LoadData trainSetloader = new LoadData();
		trainSMSList = trainSetloader.getSMSList("trainSet");
		SpamNum = trainSetloader.getSpamNum();
		HamNum = trainSetloader.getHamNum();
		trainer = new Training(trainSMSList, SpamNum, HamNum);
		WordsMap = trainer.getWordsMap();
		PofSpam = trainer.getPofSpam();
		PofHam = trainer.getPofHam();

		LoadData Dataloader = new LoadData();
		testSMSList = Dataloader.getSMSList("dataSet");

		Bayes bayes = new Bayes();
		bayes.init(testSMSList, WordsMap, PofHam, PofSpam);
		accuracy = bayes.getResult();

		System.out.println("ÕýÈ·ÂÊ£º" + accuracy);
	}
}
