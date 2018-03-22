package util;

public class ArticleAddress {
	String articleID;
	String ArticleAddress;

	public ArticleAddress(String articleID) {
		this.articleID = articleID;
	}

	public String getAddress() {
		return Configure.ArticleSta + articleID;
	}
}
