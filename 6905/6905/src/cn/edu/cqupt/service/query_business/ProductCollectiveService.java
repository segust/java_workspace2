package cn.edu.cqupt.service.query_business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.service.qualification_management.InfoService;
import net.sf.json.JSONObject;

public class ProductCollectiveService {

	private ProductDAO productDAO=null;
	
	public ProductCollectiveService() throws SQLException {
		productDAO = new ProductDAO();
	}
	
	/**
	 * 查询产品汇总
	 * */
	public List<HashMap<String, Object>> selectProductCollective(HashMap<String, String> condition,
			int curPageNum,int pageSize,String version)throws Exception{
		List<HashMap<String, Object>> T=new ArrayList<HashMap<String, Object>>();
		try {
//			condition = new InfoService().getOwnedUnitSQL(condition, version);
			T=productDAO.selectProductCollective(condition,curPageNum,pageSize,version);
			for (int i = 0; i < T.size(); i++) {
				String jds = new InfoService().getJdsThroughCompany(T.get(i).get("ownedUnit")+"");
				T.get(i).put("jds", jds);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return T;
	}
	/**
	 * 按条件计算总数
	 * @param condition
	 * @return
	 */
	public int querySum(HashMap<String, String> condition, String version) {
		int count = 0;
		condition = new InfoService().getOwnedUnitSQL(condition, version);
		count = productDAO.querySum(condition,version);
		return count;
	}
	
	/**
	 * 设备汇总总金额
	 * @throws Exception 
	 * */
	public double selectTotalPrice(HashMap<String, String> condition,String version) throws Exception{
		condition = new InfoService().getOwnedUnitSQL(condition, version);
		double totalPrice = productDAO.selectTotalPrice(condition,version);
		return totalPrice;
	}
	/**
	 * 查询产品出入库信息
	 * @param condition
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 */
	public List<HashMap<String, Object>> ProductInOutInfo(HashMap<String,String> condition,int curPageNum,int pageSize){
		List<HashMap<String, Object>> T=new ArrayList<HashMap<String, Object>>();
		T = productDAO.queryInOut(condition,curPageNum,pageSize);
		return T;
	}
	public int queryInOutSum(HashMap<String, String> condition){
		int sum = 0;
		sum = productDAO.queryInOutSum(condition);
		return sum;
	}
	
	/**
	 *  删除企业版设备汇总查询中的某条记录
	 * <p>删除的条件是前台页面的字段</p>
	 * productModel,
	 * productUnit,measureUnit,
	 * productPrice,manufacturer,ownedUnit
	 * @param jo
	 * @return
	 */
	public boolean delectCollective(JSONObject jo){
		if(jo == null)return false;
		
		String productModel = jo.getString("productModel");
		String productUnit = jo.getString("productUnit");
		String measureUnit = jo.getString("measureUnit");
		double productPrice = jo.getDouble("productPrice");
		String manufacturer = jo.getString("manufacturer");
		String ownedUnit = jo.getString("ownedUnit");
		
		return productDAO.delectCollective(productModel, productUnit, measureUnit, productPrice, manufacturer, ownedUnit);
		
	}
}
