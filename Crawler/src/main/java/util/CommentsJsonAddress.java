package util;

public class CommentsJsonAddress {
	String ArticleID;

	public CommentsJsonAddress(String ArticleID) {
		this.ArticleID = ArticleID;
	}

	public String getCommentsJsonAddress() {
		String ID = ArticleID.replace("ac", "");
		return Configure.JsonSta + ID + "&currentPage=";
	}
}
