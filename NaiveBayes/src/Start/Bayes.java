package Start;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.SMS;
import model.Words;

public class Bayes {

	int count = 0;
	HashMap<String, Words> WordsMap = new HashMap<String, Words>();
	ArrayList<SMS> SMSList = new ArrayList<SMS>();
	double PofHam = 0.0;
	double PofSpam = 0.0;
	double accuracy = 0.0;

	public void init(ArrayList<SMS> SMSList, HashMap<String, Words> WordsMap, double PofHam, double PofSpam) {
		this.SMSList = SMSList;
		this.WordsMap = WordsMap;
		this.PofHam = PofHam;
		this.PofSpam = PofSpam;
	}

	public double getResult() {

		int Num = 0;
		int Right = 0;

		for (SMS SMS : SMSList) {

			double HamRate = 1.0;
			double SpamRate = 1.0;

			String regEx = "\\W";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(SMS.getContent().toLowerCase());
			String Words = m.replaceAll(" ");
			String[] words = Words.trim().split(" ");

			for (String word : words) {
				if (WordsMap.containsKey(word)) {
					SpamRate *= WordsMap.get(word).getSpamRate();
					HamRate *= WordsMap.get(word).getHamRate();
				}
			}

			String classify = HamRate * PofHam > SpamRate * PofSpam ? "ham" : "spam";

			if (classify.equals(SMS.getClassify())) {
				Right++;
				Num++;
			} else {
				count++;
				System.out.println("µÚ" + count + "Ìõ´íÎó¶ÌÐÅ£º" + SMS.getContent());
				Num++;
			}
		}
		return (double) Right / (double) Num;
	}
}
