package Thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import util.ArticleNumList;

import http.Http2Article;
import util.Configure;

public class ArticleSearch extends Thread {

	int FileNum;
	List<String> ArticleNumList = new ArrayList<String>();

	public ArticleSearch(String InputPath, int FileNum) {
		this.FileNum = FileNum;
		ArticleNumList art = new ArticleNumList(InputPath + FileNum);
		this.ArticleNumList = art.getArt();
	}

	public void run() {
		try {
			FileOutputStream fs = new FileOutputStream(new File(Configure.OutputPath + FileNum));
			PrintStream p = new PrintStream(fs);

			for (String articleID : ArticleNumList) {
				Http2Article Article = new Http2Article(articleID);
				String ArticleInfo = new String(Article.getArticleInfo());
				p.println(ArticleInfo);
			}
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
