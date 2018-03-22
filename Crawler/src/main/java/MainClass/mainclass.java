package MainClass;

import Thread.ArticleSearch;
import util.Configure;

public class mainclass {
	public static void main(String args[]) {
		for (int i = 0; i <= Configure.ArticleListNum; i++) {
			ArticleSearch a = new ArticleSearch(Configure.InputPath, i);
			a.start();
		}
	}
}
