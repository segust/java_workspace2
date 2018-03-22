package htmlParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import http.Http2R;
import model.MyHttpResponse;

public class ArticleListInfo{
	
	public ArticleListInfo(int count) {
		for(int i = 0 ;i < count; i++){
			new Thread(new ArticleListInfoThread(i*100+1)).start();;
		}
	}
	
	public static void getArticleList(int j){
		try {
			String furl = "http://acfun.cn/v/list110/index_ourpage.htm";
			BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/article-list"+j/100));
			
			for(int i = j; i < j+100; i++){
				String url = furl.replace("ourpage", String.valueOf(i));
				System.out.println(i+"--"+url);
				MyHttpResponse r = Http2R.httpGet(url);
				if(r.getStatusLine().getStatusCode() < 300){
					try {
						Element body = Jsoup.parse(r.getEntity()).body();
						Element mainer = body.getElementsByClass("mainer").get(6);
						Elements items = mainer.getElementsByClass("item");
						for(Element item: items){
							String ptsString = item.getElementsByClass("a").first().ownText();
							if(Integer.valueOf(ptsString)<500) continue;
							
							Element article = item.getElementsByTag("a").first();
							String aUrl = "http://acfun.cn/"+article.attr("href");
							bw.newLine();
							bw.write(aUrl);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			bw.flush();
			bw.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
//		new ArticleListInfo(8);
		
		try {
			String path = "src/main/resources/article-list";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			for(int i = 0; i<8; i++){
				BufferedReader br = new BufferedReader(new FileReader(path+i));
				for(String line = null; null != (line = br.readLine());){
					bw.write(line);
					bw.newLine();
				}
				br.close();
			}
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
