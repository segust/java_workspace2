package HttpClient;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class mainclass {

	public static void main(String[] args) throws IOException {
		String body = Jsoup.connect("http://www.acfun.cn/comment_list_json.aspx?contentId=3744733&currentPage=1")
				.ignoreContentType(true).execute().body();
		@SuppressWarnings("unused")
		JSONObject json = JSON.parseObject(body);
		System.out.println(getComments());

		// Elements title = doc.getElementsByClass("txt-title-view_1");
		// System.out.println(title.get(0).text());
		// Elements articlelines = doc.getElementsByClass("article-content");
		// if (0 != articlelines.size())
		// for (int i = 0; i < articlelines.size(); i++)
		// System.out.println(articlelines.get(i).text().toString());
		// else {
		// Element article = doc.getElementById("area-player");
		// System.out.println(article.text());
		// }
	}

	public static String getComments() {

		JSONObject Commentsjson;
		String body = null;
		StringBuffer CommentsInfo = new StringBuffer();

		int page = 0;
		int totalPage = 1;
		do {
			page++;
			try {
				body = Jsoup.connect("http://www.acfun.cn/comment_list_json.aspx?contentId=3744733&currentPage=" + page)
						.ignoreContentType(true).execute().body();
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
				String content = comment.get("content").toString();
				CommentsInfo.append("cid:" + cid + "\tquoteId:" + quoteId + "\tpostDate:" + postDate + "\tuserID:"
						+ userID + "\tcontent:" + content + "\n");
			}
		} while (page < totalPage);
		if (CommentsInfo.length() == 0)
			CommentsInfo.append("无评论！");
		return CommentsInfo.toString();
	}
}
