package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ArticleNumList {

	String InputPath;
	List<String> ArticleNumList = new ArrayList<String>();

	private BufferedReader br;
	private InputStreamReader reader;

	public ArticleNumList(String InputPath) {
		this.InputPath = InputPath;
		catchFileInfo(this.InputPath);
	}

	private List<String> catchFileInfo(String InputPath) {
		try {
			reader = new InputStreamReader(new FileInputStream(InputPath));
			br = new BufferedReader(reader);
			String brLine = null;
			while ((brLine = br.readLine()) != null) {
				ArticleNumList.add(brLine);
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件未找到！");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ArticleNumList;
	}

	public List<String> getArt() {
		return ArticleNumList;
	}
}
