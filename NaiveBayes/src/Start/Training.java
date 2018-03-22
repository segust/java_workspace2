package Start;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.SMS;
import model.Words;
import util.CreatTXT;

public class Training {
	int SpamNum;
	int HamNum;
	int SpamFigureNum;
	int HamFigureNum;
	double PofSpam;
	double PofHam;
	int SpamWordNum = 0;
	int HamWordNum = 0;

	ArrayList<SMS> SMSList = new ArrayList<SMS>();
	HashMap<String, Words> priWordsMap = new HashMap<String, Words>();

	public Training(ArrayList<SMS> SMSList, int SpamNum, int HamNum) {
		this.SMSList = SMSList;
		this.SpamNum = SpamNum;
		this.HamNum = HamNum;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Words> getWordsMap() {

		for (SMS SMS : SMSList) {

			String Content = SMS.getContent().toLowerCase();
			String Classify = SMS.getClassify();

			String regEx = "\\W";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(Content);
			String words = m.replaceAll(" ");
			String[] Words = words.trim().split(" ");
			for (String Word : Words) {
				if (priWordsMap.get(Word) == null && !Word.equals("")) {
					Words word = new Words(Word);
					priWordsMap.put(Word, word);
					priWordsMap.get(Word).addNum();
					if (Classify.equals("ham")) {
						word.inHamNumadd();
						HamWordNum++;
					} else if (Classify.equals("spam")) {
						word.inSpamNumadd();
						SpamWordNum++;
					}
				} else if (priWordsMap.get(Word) != null && !Word.equals("")) {
					priWordsMap.get(Word).addNum();
					if (Classify.equals("ham")) {
						priWordsMap.get(Word).inHamNumadd();
						HamWordNum++;
					} else if (Classify.equals("spam")) {
						priWordsMap.get(Word).inSpamNumadd();
						SpamWordNum++;
					}
				}
			}
		}

		// WordsMapԤ����
		HashMap<String, Words> WordsMap = (HashMap<String, Words>) priWordsMap.clone();

		Iterator<Map.Entry<String, Words>> iter = priWordsMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Words> entry = (Map.Entry<String, Words>) iter.next();
			String Word = entry.getKey();
			Words word = entry.getValue();
			if (word.getWord().length() <= 0) {
				WordsMap.remove(Word);
				if (word.getInHamNum() != 0)
					HamWordNum -= word.getInHamNum();
				else if (word.getInSpamNum() != 0)
					SpamWordNum -= word.getInSpamNum();
			}
		}

		Iterator<Map.Entry<String, Words>> wordIt = WordsMap.entrySet().iterator();
		while (wordIt.hasNext()) {
			Map.Entry<String, Words> entry = (Map.Entry<String, Words>) wordIt.next();
			Words word = (Words) entry.getValue();
			word.setHamRate((double) (word.getInHamNum() + 1) / ((double) (HamWordNum + WordsMap.size())));
			word.setSpamRate((double) (word.getInSpamNum() + 1) / ((double) (SpamWordNum + WordsMap.size())));
		}

		CreatTXT txt = new CreatTXT();
		try {
			txt.createFile(new File("E:/Workspace/NaiveBayes/src/WordsMap.txt"));
			txt.writeTxtFile(WordsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WordsMap;

	}

	public double getPofSpam() {
		PofSpam = (double) SpamNum / ((double) HamNum + (double) SpamNum);
		return PofSpam;
	}

	public double getPofHam() {
		PofHam = (double) HamNum / ((double) HamNum + (double) SpamNum);
		return PofHam;
	}

}