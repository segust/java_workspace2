package cn.edu.cqupt.service.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.service.qualification_management.InfoService;

public class EquipmentCollectiveService {
	
	public static ProductDAO produceDAO = new ProductDAO();
	
	public List<Map<String, Object>> equipmentCollective(HashMap<String, String> condition,String version,
			 int curPageNum, int pageSize){
		
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();		
		try {
			condition = new InfoService().getOwnedUnitSQL(condition, version);
			list = produceDAO.selectEquipmentCollective(condition,version,curPageNum,pageSize);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return list;	
	}
	
	public int equipmentCollectiveCount(HashMap<String, String> condition,String version){
		
		int count = 0;
		try {
			condition = new InfoService().getOwnedUnitSQL(condition, version);
			count = produceDAO.selectEquipmentCollectiveCount(condition,version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
		
	}
}
