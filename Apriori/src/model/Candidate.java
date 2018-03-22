package model;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * 候选项集类
 */
public class Candidate {
	//候选项集
	private ArrayList<String> cand;
	//候选项集长度
	private int count;
	
	public Candidate() {
		setCount(0);
		setCand(new ArrayList<String>());
	}

	public ArrayList<String> getCand() {
		return cand;
	}

	public void setCand(ArrayList<String> cand) {
		this.cand = cand;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	

}
