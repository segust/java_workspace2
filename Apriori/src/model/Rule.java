package model;

import java.util.ArrayList;
/**
 *	关联规则类
 */
public class Rule {
	
	private ArrayList<String> cand;//当前候选项
	
	private ArrayList<String> left;//规则左边
	
	private ArrayList<String> right;//规则右边
	
	private int joinCount;//联合计数
	
	private int rightCount;//边缘计数
	
	public Rule() {
		joinCount = 0;
		rightCount = 0;
		left = new ArrayList<String>();
		right = new ArrayList<String>();
		setCand(new ArrayList<String>());
	}
	
	public ArrayList<String> getLeft() {
		return left;
	}
	public void setLeft(ArrayList<String> left) {
		this.left = left;
	}
	public ArrayList<String> getRight() {
		return right;
	}
	public void setRight(ArrayList<String> right) {
		this.right = right;
	}
	public int getCount() {
		return joinCount;
	}
	public void setCount(int count) {
		this.joinCount = count;
	}

	public ArrayList<String> getCand() {
		return cand;
	}

	public void setCand(ArrayList<String> cand) {
		this.cand = cand;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}
	
	
}
