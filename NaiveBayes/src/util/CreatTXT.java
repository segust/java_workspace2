package util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.Words;

public class CreatTXT {

	// 创建文件
	public void createFile(File fileName) throws Exception {
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 写

	public void writeTxtFile(HashMap<String, Words> WordsMap) throws Exception {

		ArrayList<String> message = new ArrayList<String>();
		Iterator<String> it = WordsMap.keySet().iterator();
		while (it.hasNext()) {
			String word = it.next();
			message.add(word + ":\t" + WordsMap.get(word).getNum() + "\tSpamRate:" + WordsMap.get(word).getSpamRate()
					+ "\tHamRate:" + WordsMap.get(word).getHamRate());
		}
		String[] string = new String[message.size()];
		for (int i = 0; i < message.size(); i++) {
			string[i] = message.get(i);
		}

		try {
			BufferedWriter output = new BufferedWriter(new FileWriter("E:/Workspace/NaiveBayes/src/WordsMap.txt"));
			for (int i = 0; i < string.length; i++) {
				output.write(String.valueOf(string[i]) + "\n");
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}