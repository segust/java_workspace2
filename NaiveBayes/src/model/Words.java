package model;

public class Words {

	int num = 0;

	String word;
	int inHamNum = 0;
	int inSpamNum = 0;
	double HamRate;
	double SpamRate;

	public void addNum() {
		num++;
	}

	public void inHamNumadd() {
		inHamNum++;
	}

	public void inSpamNumadd() {
		inSpamNum++;
	}

	public String getWord() {
		return word;
	}

	public int getNum() {
		return num;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getHamRate() {
		return HamRate;
	}

	public void setHamRate(double hamRate) {
		HamRate = hamRate;
	}

	public double getSpamRate() {
		return SpamRate;
	}

	public void setSpamRate(double spamRate) {
		SpamRate = spamRate;
	}

	public int getInHamNum() {
		return inHamNum;
	}

	public int getInSpamNum() {
		return inSpamNum;
	}

	public Words(String word) {
		this.word = word;
	}
}
