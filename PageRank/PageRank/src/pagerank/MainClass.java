package pagerank;

import java.util.List;

import model.Network;
import model.WebLink;
import util.FileOperate;

/**
 * 入口类
 */
public class MainClass {

	public static void main(String[] args) {
		//1. 获取数据
		List<WebLink> data = FileOperate.loadData(Configuration.DATA_PATH, "\t");
		
		//2. 初始化网络结构
		Network network = new Network();
		network.initialNetwork(data);        //初始网页结构
		
		//3. 运行PageRank算法
		PageRank pagerank = new PageRank(network);
		pagerank.initialNodeVale();          //初始网页节点初值为1/n（n为网页节点总数）
		pagerank.updatePageRank();           //更新pr值直至收敛
		
		//4. 结果展示和存储，展示top k个数据的pr值
		FileOperate.writeData(network, Configuration.RESULT_PATH);
		pagerank.showTopKPR(Configuration.TOP_K);
	}

}
