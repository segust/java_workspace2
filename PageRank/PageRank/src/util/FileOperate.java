package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import model.Network;
import model.Node;
import model.WebLink;

/**
 * 文件读写类
 */
public class FileOperate {

	/**
	 * 从txt读取数据
	 * @param path 读取数据的相对路径
	 * @param splitType 分隔符
	 * @return List<WebLink> 数据用webLink封装，存放在list里
	 */
	public static List<WebLink> loadData(String path, String splitType){
		List<WebLink> list = new ArrayList<WebLink>();
		BufferedReader br  = null;
		try {
			br = new BufferedReader(new FileReader(path));
			String str = null;
			while ((str = br.readLine()) != null) {
				String[] strs = str.split(splitType);
				WebLink web = new WebLink(strs[0], strs[1]);
				list.add(web);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将结果依据pr值排序并存入txt中
	 * @param network 网页链接关系网络 
	 * @param path    写入文件的相对路径
	 */
	public static void writeData(Network network, String path){
		try {
			//新建文件
			File file = new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(path));
			//根据pr排序
			List<Map.Entry<String, Node>> list = FileOperate.sortData(network);
			//写数据
			for(int i = 0; i < network.getCount(); i++){
				Node node = list.get(i).getValue();
				bufferWritter.write(node.getWebName() + ": " + node.getCurrentPR() + "\r\n");
			}
			bufferWritter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("写入数据完成！");
	}
	
	/**
	 * 根据pr值排序
	 * @param network 网页链接关系网络 
	 * @return List<Map.Entry<String, Node>> 排序好后的网页和pr值
	 */
	public static List<Map.Entry<String, Node>> sortData(Network network){
		List<Map.Entry<String, Node>> list = new ArrayList<Map.Entry<String, Node>>(network.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Node>>() {
			public int compare(Entry<String, Node> entry1, Entry<String, Node> entry2){
				double v1 = entry1.getValue().getCurrentPR();
				double v2 = entry2.getValue().getCurrentPR();
				double value = v2 - v1;
				if(value > 0.0)
					return 1;
				else if(value > 0.0)
					return -1;
				else 
					return 0;
			}});
		return list;
	}
}
