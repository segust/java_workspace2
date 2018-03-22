package model;

import java.util.ArrayList;
import java.util.List;

import pagerank.Configuration;

/** 
 * Network中的节点，存放网页id和父节点，孩子节点以及pr值等信息
 */
public class Node {

	protected String webName;       //网页id
	protected double currentPR;     //当前迭代次数对应的pr值
	protected double lastPR;        //当前迭代次数-1对应的pr值
	protected int iterationNO;      //当前迭代次数
	protected List<Node> outLink;   //该节点的指向节点集合
	protected List<Node> inLink;    //指向该节点的节点集合
	
	/**
	 * @param webName 网页名
	 */
	public Node(String webName) {
		this.webName = webName;
		this.iterationNO = 0;
		this.outLink = new ArrayList<Node>();
		this.inLink = new ArrayList<Node>();
	}
	
	public String getWebName() {
		return webName;
	}

	public double getLastPR() {
		return lastPR;
	}

	public int getIterationNO() {
		return iterationNO;
	}

	public void setLastPR(double lastPR) {
		this.lastPR = lastPR;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public double getCurrentPR() {
		return currentPR;
	}

	/**
	 * @param currentPR 当前pr值
	 */
	public void setCurrentPR(double currentPR) {
		lastPR = this.currentPR;
		this.currentPR = currentPR;
		iterationNO ++;
	}
	
	/**
	 * 得到入链集合
	 * @return List<Node> 入链集合
	 */
	public List<Node> getOutLink() {
		return this.outLink;
	}

	public List<Node> getInLink() {
		return this.inLink;
	}

	/** 
	 * 增加出链
	 * @param node 添加的出链
	 * @return boolean 是否添加成功
	 */
	public boolean addOutLink(Node node){
		if(node != null && outLink.add(node))
			return true;
		else
			return false;
	}
	
	/** 
	 * 删除出链
	 * @param node 删除的出链
	 * @return boolean 是否删除成功
	 */
	public boolean deleteOut_link(Node node){
		if(node != null && outLink.remove(node))
			return true;
		else
			return false;
	}
	
	/**
	 * 增加入链
	 * @param node 增加的入链
	 * @return boolean 是否增加入链成功
	 */
	public boolean addInLink(Node node){
		if(node != null && inLink.add(node))
			return true;
		else
			return false;
	}
	
	/** 
	 * 删除入链
	 * @param node 删除的入链
	 * @return boolean 是否删除入链成功
	 */
	public boolean deleteInLink(Node node){
		if(node != null && inLink.remove(node))
			return true;
		else
			return false;
	}
	
	/** 
	 * 更新一个节点的pr值
	 * @param iterationNum  当前迭代次数
	 * @param alpha          alpha值
	 * @param count          网络总节点数
	 * @return boolean       是否更新一个节点pr值成功
	 */
	public boolean updateNodePR(int iterationNum, int count){
		if(iterationNum <= 0 || Configuration.ALPHA < 0 || count <= 0)
			return false;
		else{
			double updatePR = (1 - Configuration.ALPHA)/count;
			double temp = 0.0;
			for(Node nodePI: this.inLink){
				int piIterNO = nodePI.getIterationNO();
				int piOutLinkNO = nodePI.outLink.size();
				double piLastPR = 0.0;
				
				if(piIterNO - iterationNum == 0){
					piLastPR = nodePI.getCurrentPR();
				}else if(piIterNO - iterationNum == 1){
					piLastPR = nodePI.getLastPR();
				}
				temp += piLastPR / piOutLinkNO;
			}
			updatePR += Configuration.ALPHA * temp;
			this.setCurrentPR(updatePR);     //更新存储pr值
			return true;
		}
	}
}
