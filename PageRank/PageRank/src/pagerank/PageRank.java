package pagerank;

import java.util.List;
import java.util.Map;

import model.Network;
import model.Node;
import util.FileOperate;

/**
 * PageRank算法类
 */
public class PageRank {
	
	protected Network network;        //网络结构关系
	
	/**
	 * 构造函数
	 * @param network 网络链接关系
	 */
	public PageRank(Network network) {
		this.network = network;
	}
	
	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * 初始化节点pr值：1/n （n为网页节点总数）
	 */
	public void initialNodeVale(){
		double first_value = 1.0 / network.getCount();
		for(Map.Entry<String, Node> entry: network.entrySet()){
			Node node = entry.getValue();
			node.setCurrentPR(first_value);
		}
	}
	
	/**
	 * 判断算法是否收敛
	 * 判断方式：所有节点pr值更新前后两次的差值<收敛参数
	 * @return boolean 是否收敛成功
	 */
	public boolean isCoverage(){
		double diff = 0.0;
		for(Map.Entry<String, Node> entry: network.entrySet()){
			Node node = entry.getValue();
			diff += Math.abs(node.getLastPR() - node.getCurrentPR());
		}
		if(Math.abs(diff) > Configuration.COVERAGE_VALUE)
			return false;
		else
			return true;
	}
	
	/**
	 * 更新pagerank
	 */
	public void updatePageRank(){
		int iteration_count = 1;
		//如果pr更新收敛或者达到设置的最大迭代次数，就跳出循环更新
		while(iteration_count <= Configuration.MAX_ITER){  
			System.out.println("第" + iteration_count + "次迭代");
			this.onceIteration(iteration_count);  //一次迭代更新所有节点的pr值
			iteration_count++;
			if(this.isCoverage())
				break;
		}
		System.out.println("更新完成！");
	}
	
	/**
	 * 一次迭代更新所有节点的pr值
	 * @param iterationNum 迭代的次数
	 */
	public void onceIteration(int iterationNum){
		for(Map.Entry<String, Node> entry: network.entrySet()){
			Node node = entry.getValue();
			node.updateNodePR(iterationNum, network.getCount()); //更新一个节点的pr值
		}
	}
	
	/**
	 * 根据pr值排序，并展示topK个节点
	 * @param network 网络链接关系网络
	 * @param topK 根据pr值排序的前k个网页
	 */
	public void showTopKPR(int topK){
		List<Map.Entry<String, Node>> list = FileOperate.sortData(network);
		
		System.out.println("----按顺序展示 Top" + topK + "个网页的pr值----");
		for(int i = 0; i < topK; i++){
			Node node = list.get(i).getValue();
			System.out.println("Top" + ( i + 1 ) + ":" + node.getWebName() + "   " + node.getCurrentPR());
		}
	}
}
