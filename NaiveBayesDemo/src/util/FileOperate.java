package util;

import java.io.BufferedReader;
import util.AlgorithmUtil;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.Node;
/**
 * 文件读写类
 * */
public class FileOperate {
	
	/**
	 * 读取txt数据
	 * */
	public ArrayList<Node>loadData(String data_path){
		ArrayList<Node> dataList = new ArrayList<Node>(); 
		String message = new String();
		BufferedReader input = null;
		try {
			input=new BufferedReader(new FileReader(data_path));
			while((message = input.readLine()) != null){
				String[] info = message.split("\t");
				
				//清理数据
				info[1] = new AlgorithmUtil().cleanData(info[1]);
				
				 //装载数据
				if(info[1] != null){
					Node data = new Node(info[0], info[1]);
					dataList.add(data);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} finally {
			try {
				if(input != null)
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataList;
	}
	
	
}
