package cn.edu.cqupt.service.storage_maintenanc;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.util.MyDateFormat;

public class UpdateLatestMaintainTimeAfterMaintainService {

	private ProductDAO dao;
	
	public boolean updateMaintainTime(List<String> productIds)
	{
		this.dao = new ProductDAO();
		boolean isOK = true;
		Date now = new Date();
		Timestamp stamp = MyDateFormat.changeToSqlDate(now);
		for(int i = 0; i < productIds.size(); i++)
		{
			long productId = Long.parseLong(productIds.get(i));
			if(dao.setLastMaintainTime(productId, stamp) == false)
			{
				isOK = false;
				break;
			}
		}
		
		return isOK;
	}
	
}
