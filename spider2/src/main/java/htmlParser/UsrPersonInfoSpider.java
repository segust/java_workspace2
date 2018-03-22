package htmlParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import util.Query;
import util.QueryCallBack;

public class UsrPersonInfoSpider implements Runnable{

	@Override
	public void run() {
		while(true){
			String sql = "SELECT `id` FROM page_rank.usr ORDER BY `id` DESC";
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
				try {
					UserInfo.getUserInfo(uuid);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
