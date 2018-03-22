package http;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import util.ArticleAddress;
import util.CommentsJsonAddress;

public class Http2Article {
	String Articleurl;
	String Commentsurl;
	Document Articledoc;
	String Comments;
	String title;
	String article;
	String comments;
	String time;
	String ArticleId;

	public Http2Article(String ArticleId) {
		this.ArticleId = ArticleId;
		ArticleAddress ad = new ArticleAddress(this.ArticleId);

		this.Articleurl = ad.getAddress();
		try {
			this.Articledoc = Jsoup.connect(Articleurl).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getTitle() {
		Elements Title = Articledoc.getElementsByClass("txt-title-view_1");
		title = "《" + Title.get(0).text().toString() + "》";
		return title;
	}

	public String getArticle() {
		StringBuffer sb = new StringBuffer();
		Elements articlelines = Articledoc.getElementsByClass("article-content");
		if (0 != articlelines.size())
			for (int i = 0; i < articlelines.size(); i++)
				sb.append(articlelines.get(i).text().toString() + "\n");
		else {
			Element articleline = Articledoc.getElementById("area-player");
			sb.append(articleline.text());
		}
		article = sb.toString();
		return article;
	}

	public String getComments() {

		JSONObject Commentsjson;
		String body = null;
		StringBuffer CommentsInfo = new StringBuffer();
		String CommentsJsonAddress;

		CommentsJsonAddress ad = new CommentsJsonAddress(ArticleId);
		CommentsJsonAddress = ad.getCommentsJsonAddress();

		int page = 0;
		int totalPage = 1;
		do {
			page++;
			try {
				body = Jsoup.connect(CommentsJsonAddress + page).ignoreContentType(true).execute().body();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Commentsjson = JSON.parseObject(body);
			JSONObject data = Commentsjson.getJSONObject("data");
			JSONArray commentList = data.getJSONArray("commentList");
			totalPage = data.getIntValue("totalPage");
			page = data.getIntValue("page");
			JSONObject commentContentArr = data.getJSONObject("commentContentArr");

			for (int j = 0; j < commentList.size(); j++) {
				String CID = "c" + commentList.getBigDecimal(j).toPlainString();
				JSONObject comment = commentContentArr.getJSONObject(CID);
				String cid = comment.getBigDecimal("cid").toPlainString();
				String userID = comment.getBigDecimal("userID").toPlainString();
				String quoteId = comment.getBigDecimal("quoteId").toPlainString();
				String postDate = comment.get("postDate").toString();
				String content = comment.get("content").toString().replaceAll("<br/>", "\t");
				CommentsInfo.append("\n"+"cid:" + cid + "\tquoteId:" + quoteId + "\tpostDate:" + postDate + "\tuserID:"
						+ userID + "\tcontent:" + content );
			}
		} while (page < totalPage);
		if (CommentsInfo.length() == 0)
			CommentsInfo.append("无评论！");
		return CommentsInfo.toString();
	}

	public String getTime() {
		Elements Time = Articledoc.getElementsByClass("time");
		time = Time.get(0).toString();
		return time;
	}

	public String getArticleInfo() {
		return getTitle() + getArticle() + getTime() + getComments();
	}

	public static void main(String args[]) {
		Http2Article a = new Http2Article("http://www.acfun.cn/a/ac3742181");
		System.out.println(a.getTitle());
		System.out.println(a.getArticle());
	}
}
