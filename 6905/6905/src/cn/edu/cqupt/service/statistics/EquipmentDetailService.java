package cn.edu.cqupt.service.statistics;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.service.qualification_management.InfoService;

public class EquipmentDetailService {

	private Connection conn=null;
	private ProductDAO productDAO=null;
	
	public EquipmentDetailService() throws SQLException {
		this.conn = DBConnection.getConn();
		productDAO = new ProductDAO(this.conn);
	}
	
	/**
	 * 器材明细统计
	 * */
	public List<HashMap<String, Object>> selectEquipmentDetail(HashMap<String, String> condition,String version,int curPageNum, int pageSize) throws Exception{
		List<HashMap<String, Object>> T=new ArrayList<HashMap<String, Object>>();
		try {
			condition = new InfoService().getOwnedUnitSQL(condition, version);
			T=productDAO.selectEquipmentDetail(condition,version,curPageNum, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return T;
	}
	
	/**
	 * 器材明细统计数量
	 * */
	public int selectEquipmentDetailCount(HashMap<String, String> condition, String version){
		int count = 0;
		try {
			condition = new InfoService().getOwnedUnitSQL(condition, version);
			count = productDAO.selectEquipmentDetailCount(condition, version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
