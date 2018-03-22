package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pagerank.PageRank;

/** 
 * 网页链接关系网络  HashMap<String, Node>中String是网页id，Node是其对应的Node对象
 */
public class Network extends HashMap<String, Node>{

	/**
	 * 初始化网络，根据链接关系来建立网络
	 * @param data  数据：网页链接
	 */
	public void initialNetwork(List<WebLink> data){
		//添加节点
		for(int i = 0 ; i < data.size(); i++){
			WebLink weblink = data.get(i);
			String fromLink = weblink.getFromLink();
			String toLink = weblink.getToLink();
			if(!this.containsKey(fromLink)){
				this.put(fromLink, new Node(fromLink));
			}
			Node fromNode = this.get(fromLink);
			if(!this.containsKey(toLink)){
				this.put(toLink, new Node(toLink));
			}
			Node toNode = this.get(toLink);	
			fromNode.addOutLink(toNode);
			toNode.addInLink(fromNode);
		}
	}
	
	/**
	 * 展示网络结构
	 */
	public void show(){
		for(Map.Entry<String, Node> entry: this.entrySet()){
			Node no = entry.getValue();
			System.out.print(no.getWebName()+ " :  ");
			for(Node n: no.getOutLink())
				System.out.print(n.getWebName() + "  ");
			System.out.println();
		}
	}

	/**
	 * 网络中的节点总数
	 * @return int 节点总数
	 */
	public int getCount(){
		return this.size();
	}
	
	public static void main(String[] args) {
		//简单例子测试
		List<WebLink> data = new ArrayList<WebLink>();
		data.add(new WebLink("A", "B"));
		data.add(new WebLink("A", "C"));
		data.add(new WebLink("A", "D"));
		data.add(new WebLink("B", "C"));
		data.add(new WebLink("D", "A"));
		data.add(new WebLink("D", "C"));
		
		Network network = new Network();
		network.initialNetwork(data);
		network.show();
		
		int top_k = 4;
		PageRank pagerank = new PageRank(network);
		pagerank.initialNodeVale();
		pagerank.updatePageRank();
		pagerank.showTopKPR(top_k);
	}

}
