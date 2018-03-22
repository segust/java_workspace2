package htmlParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;

import http.Http2R;
import model.MyHttpResponse;
import model.User;
import model.UserPageInfo;
import util.DomUtil;
import util.Query;
import util.QueryCallBack;

public class UserInfo {
	
	private static void parseUserInfo(String html, String uuid){
		User user = new User();
		
		//uuid
		user.setUuid(uuid);
		
		try {
			Document doc = Jsoup.parse(html);
			Element body = doc.body();
			
			//username
			Elements userNameEs = body.getElementsByClass("name fl text-overflow");
			user.setUsername(userNameEs.get(0).ownText());
			
			//info
			Elements infoM = body.getElementsByClass("infoM");
			String info = infoM.get(0).ownText();
			if(info.length() > 256) info = info.substring(0, 256);
			user.setInfo(info);
			
			//follow
			Elements followCountEs = body.getElementsByClass("fl follow");
			user.setFollow(Integer.valueOf(followCountEs.get(0).ownText()));
			
			//fans
			Elements fansEs = body.getElementsByClass("fl fans");
			user.setFans(Integer.valueOf(fansEs.get(0).ownText()));
			
			//work
			Elements workCountEs = body.getElementsByClass("fl sub");
			user.setWork(Integer.valueOf(workCountEs.get(0).ownText()));
			
			//three kinds of works
			if(user.getWork()>0){
				Elements worksEs = body.getElementsByClass("table clearfix");
				Elements worksE = worksEs.get(0).children();
				
				Element vidoWorkE = worksE.get(0);
				user.setVideoWork(Integer.valueOf(vidoWorkE.getElementsByTag("span").get(0).ownText()));
				
				Element articleE = worksE.get(1);
				user.setArticleWork(Integer.valueOf(articleE.getElementsByTag("span").get(0).ownText()));
				
				Element compilationE = worksE.get(2);
				user.setCompilation(Integer.valueOf(compilationE.getElementsByTag("span").get(0).ownText()));
			}
			
			user.setDone(true);
		} catch (NumberFormatException e) {
			user.setDone(false);
			e.printStackTrace();
		}
		
		user.saveUserInfo();
	}
	
	private static UserPageInfo parseUserRelation(String json){
		JSONObject object = JSONObject.parseObject(json);
		if(0 == object.getInteger("errno")){
			JSONObject data = object.getJSONObject("data");
			JSONObject page = data.getJSONObject("page");
			
			if(data.getBooleanValue("success")){
				String html = data.getString("html");
				if(!html.isEmpty()){
					Document doc = Jsoup.parse(html);
					Elements figures = doc.getElementsByTag("figure");
					ArrayList<String> relationParamsTemp = new ArrayList<>(figures.size());
					for(Element figure: figures){
						String uuidGet = DomUtil.getUuidFromDom(figure.getElementsByClass("name").get(0).outerHtml());
						if(null != uuidGet)	relationParamsTemp.add(uuidGet);
					}
					
					UserPageInfo upi = new UserPageInfo();
					upi.setUserlist(relationParamsTemp);
					
					if(page.getIntValue("nextPage") < page.getIntValue("totalPage")){
						upi.setLastPage(false);
						upi.setNextPage(page.getIntValue("nextPage"));
					}else{
						upi.setLastPage(true);
					}
					
					return upi;
					
				}else{
					return null;
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	public static void getUserFollow(String uuid, int page){
		String url = "http://www.acfun.cn/space/next?uid=ouruuid&type=flow&orderBy=2&pageNo=ourpage";
		url = url.replace("ouruuid", uuid);
		url = url.replace("ourpage", String.valueOf(page));
		MyHttpResponse r = Http2R.httpGet(url);
		if(r.getStatusLine().getStatusCode() < 300){
			UserPageInfo upi = parseUserRelation(r.getEntity());
			if(null == upi) return;
			Object[] params = upi.getUserlist().toArray();
			String sql = "INSERT INTO `page_rank`.`relationship` (`fans`, `follow`) VALUES ('"+uuid+"', ?)";
			Query.executeDMLBatchForEachWithSingleParams(sql, params);
			sql = "INSERT INTO `page_rank`.`usr` (`id`) VALUES (?);";
			Query.executeDMLBatchForEachWithSingleParams(sql, params);
			if(!upi.isLastPage()){
				getUserFollow(uuid, upi.getNextPage());
			}
		}
	}
	
	public static void getUserWholeRelation(String uuid){
		
		// if user done
		String sql = "SELECT * FROM page_rank.usr WHERE id='"+uuid+"'";
		Integer flag = (Integer) Query.executeQueryTemplate(sql, null, new QueryCallBack() {
			
			@Override
			public Object doExecute(ResultSet rs) {
				Integer flagInRS = 0;
				try {
					if(rs.next()){
						if(rs.getBoolean("done")){
							flagInRS = 0;
						}else{
							flagInRS = 1;
						}
					}else{
						flagInRS = 2;
					}
				} catch (SQLException e) {
					flagInRS = 0;
					e.printStackTrace();
				}
				return flagInRS;
			}
		});
		
		if(flag > 0){
			getUserFans(uuid,1);
			getUserFollow(uuid, 1);
			
			if(flag < 2){
				sql = "UPDATE `page_rank`.`usr` SET `relation_done`='1' WHERE `id`='"+uuid+"'";
			}else{
				sql = "INSERT INTO `page_rank`.`usr` (`id`, `relation_done`) VALUES ('"+uuid+"', '1')";
			}
			
			Query.executeDML(sql, null);
		}
	}
	
	public static void spideRelation(){
		while(true){
			String sql = "SELECT `id` FROM page_rank.usr where relation_done='0' AND id!='4551417' ORDER BY `fans` DESC " ;
			final LinkedList<String> ids = new LinkedList<String>();
			Query.executeQueryTemplate(sql, null, new QueryCallBack() {
				
				@Override
				public Object doExecute(ResultSet rs) {
					try {
						while(rs.next()){
							ids.add(rs.getString(1));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				}
			});
			
			for(String uuid : ids){
				getUserWholeRelation(uuid);
			}
		}
	}
	
	public static void getUserFans(String uuid, int page){
		String url = "http://www.acfun.cn/space/next?uid=ouruuid&type=flowed&orderBy=2&pageNo=ourpage";
		url = url.replace("ouruuid", uuid);
		url = url.replace("ourpage", String.valueOf(page));
		
		MyHttpResponse r = Http2R.httpGet(url);
		if(r.getStatusLine().getStatusCode() < 300){
			UserPageInfo upi = parseUserRelation(r.getEntity());
			if(null == upi) return;
			Object[] params = upi.getUserlist().toArray();
			String sql = "INSERT INTO `page_rank`.`relationship` (`fans`, `follow`) VALUES (?, '"+uuid+"')";
			Query.executeDMLBatchForEachWithSingleParams(sql, params);
			sql = "INSERT INTO `page_rank`.`usr` (`id`) VALUES (?);";
			Query.executeDMLBatchForEachWithSingleParams(sql, params);
			
			if(!upi.isLastPage()){
				getUserFans(uuid, upi.getNextPage());
			}
		}
		
		
	}
	
	public static void getUserInfo(String uuid) throws Exception{
		MyHttpResponse r = Http2R.httpGet("http://www.acfun.cn/u/"+uuid+".aspx#page=1");
		System.out.println("http://www.acfun.cn/u/"+uuid+".aspx#page=1");
		if(r.getStatusLine().getStatusCode() < 300){
			parseUserInfo(r.getEntity(), uuid);
		}else{
			throw new Exception("get user info net error: "+r.getStatusLine());
		}
	}
	
	public static void main(String[] args) {
		new Thread(new UsrPersonInfoSpider()).start();
		UserInfo.spideRelation();
//		try {
//			getUserRelation("606168",1,"http://www.acfun.cn/space/next?uid=uuid&type=flow&orderBy=2&pageNo=page");
////			getUserInfo("606168");
//		} catch (Exception e) {
//			e.printStackTrace();

	}

}
