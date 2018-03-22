package cn.edu.cqupt.service.query_business;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.util.MyDateFormat;
import cn.edu.cqupt.util.StringUtil;

public class ProductDetailService {

	private Connection conn=null;
	private ProductDAO productDAO=null;
	
	public ProductDetailService() throws SQLException {
		this.conn = DBConnection.getConn();
		productDAO = new ProductDAO(this.conn);
	}
	
	/**
	 * 查询产品明细
	 * flag=a:产品明细查询（包括不在库的）
	 * flag=b:轮换入库申请，更新入库申请（只包含在库）
	 * flag=c:填写发料单，填写轮换出库发料单，填写更新出库发料单（只包含在库）
	 * */
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, Object>> selectProductDetail(HashMap<String, String> condition,String flag, String version) throws Exception{
		List<HashMap<String, Object>> T=new ArrayList<HashMap<String, Object>>();
		HashMap<Integer, HashMap<String, Object>> T1 = new HashMap<Integer, HashMap<String, Object>>();
		try {
			if(flag.equals("a")){
				ArrayList<String> ownedUnitList = new ArrayList<String>();
				if(version.equals("3")){
					if(StringUtil.isNotEmpty(condition.get("JDS"))){
						ownedUnitList = new InfoService().getCompanyNameList(condition.get("JDS"), 2);;
//						ownedUnitList = new InfoService().getOwnedUnit(condition.get("JDS"),ownedUnitList);
						for (int i = 0; i < ownedUnitList.size(); i++) {
							condition.put("ownedUnit", ownedUnitList.get(i));
							List<HashMap<String, Object>> result = new ProductDetailService().getProductDetail(condition, flag);
							for (HashMap<String, Object> hashMap : result) {
								T.add(hashMap);
							}
						}
					}else{
						T = getProductDetail(condition, flag);
					}
				}else{
					T = getProductDetail(condition, flag);
				}
				if(StringUtil.isNotEmpty(condition.get("Means"))){
					List<HashMap<String, Object>> T_Means = new ArrayList<HashMap<String, Object>>();
					for (HashMap<String, Object> hashMap : T) {
						if(hashMap.get("Means").equals(condition.get("Means")))
							T_Means.add(hashMap);
					}
					return T_Means;
				}
			}else if(flag.equals("b")||flag.equals("c")){
				T1=productDAO.selectProductDetailInapply(condition, flag);
				Iterator iter = T1.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					T.add(T1.get(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return T;
	}
	
	/**
	 * 
	 * */
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, Object>> getProductDetail(HashMap<String, String> condition,String flag){
		HashMap<Integer, HashMap<String, Object>> T1 = new HashMap<Integer, HashMap<String, Object>>();
		HashMap<Integer, HashMap<String, Object>> T2 = new HashMap<Integer, HashMap<String, Object>>();
		HashMap<Integer, HashMap<String, Object>> T3 = new HashMap<Integer, HashMap<String, Object>>();
		List<HashMap<String, Object>> T=new ArrayList<HashMap<String, Object>>();
		
		try {
			T1=productDAO.selectProductDetailInapply(condition, flag);
			T2=productDAO.selectProductDetailOutapply(condition, flag);
			T3=productDAO.selectProductDetailOutlist(condition, flag);
			
			System.out.println(T1.size());
			System.out.println(T2.size());
			System.out.println(T3.size());
			Iterator iter = T1.entrySet().iterator();
			if(flag.equals("a")){
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					if(T2.get(key)!=null){
						long count1 = MyDateFormat.javaDateMills((Date)T1.get(key).get("insertTime"), (Date)T2.get(key).get("insertTime"));
						if(count1>=0){
							if(T3.get(key)!=null){
								long count2 = MyDateFormat.javaDateMills((Date)T2.get(key).get("insertTime"), (Date)T3.get(key).get("insertTime"));
								if(count2>=0){
									//T加入T3时，需要添加批次
									HashMap<String, Object> list = T3.get(key);
									String batch = T2.get(key).get("batch").toString();
									list.remove("batch");
									list.put("batch", batch);
									T.add(list);
								}else{
									T.add(T2.get(key));
								}
							}else{
								T.add(T2.get(key));
							}
						}else if(count1<0){
							if(T3.get(key)!=null){
								long count2 = MyDateFormat.javaDateMills((Date)T1.get(key).get("insertTime"), (Date)T3.get(key).get("insertTime"));
								if(count2>=0){
									//T加入T3时，需要添加批次
									HashMap<String, Object> list = T3.get(key);
									String batch = T1.get(key).get("batch").toString();
									list.remove("batch");
									list.put("batch", batch);
									T.add(list);
								}else{
									T.add(T1.get(key));
								}
							}else{
								T.add(T1.get(key));
							}
						}
					}else if(T3.get(key)!=null&&T2.get(key)==null){
						long count3 = MyDateFormat.javaDateMills((Date)T1.get(key).get("insertTime"), (Date)T3.get(key).get("insertTime"));
						if(count3>=0){
							//T加入T3时，需要添加批次
							HashMap<String, Object> list = T3.get(key);
							String batch = T1.get(key).get("batch").toString();
							list.remove("batch");
							list.put("batch", batch);
							T.add(list);
						}else{
							T.add(T1.get(key));
						}
					}else{
						T.add(T1.get(key));
					}
				}
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
	public int queryDetailSum(HashMap<String, Object> condition,String version) {
		int count = 0;
		count = productDAO.queryDetailSum(condition,version);
		return count;
	}
	
}
