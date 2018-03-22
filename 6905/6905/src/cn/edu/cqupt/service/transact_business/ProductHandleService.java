package cn.edu.cqupt.service.transact_business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.beans.Unit;
import cn.edu.cqupt.dao.InproductRelationDAO;
import cn.edu.cqupt.dao.OutproductRelationDAO;
import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.dao.UnitDAO;

/**
 * 服务层
 * 执行产品相关操作
 * @author lynn
 *
 */
public class ProductHandleService {
	private ProductDAO productDAO = null;
	private InproductRelationDAO inproductRelationDAO=null;
	private OutproductRelationDAO outproductRelationDAO=null;
	private UnitDAO unitDAO = null;
	public ProductHandleService() {
		productDAO = new ProductDAO();
		inproductRelationDAO=new InproductRelationDAO();
		outproductRelationDAO=new OutproductRelationDAO();
		unitDAO = new UnitDAO();
	}
	
	/**
	 * author limengxin
	 * @param productId
	 * @return
	 */
	public boolean updateProductStatuc(long productId,String status){
		boolean flag = false;
		flag = productDAO.updateProductStatuc(productId,status);
		return flag;
	}
	public List<Map<String,String>> getProducts(String productStatus, String flag,int curPageNum,int pageSize){
		if(productStatus == null) 
			new NullPointerException("productStatus must not be null");
		return productDAO.selectProduct(productStatus, flag,curPageNum,pageSize);
	}	
	
	/**
	 * 用于修改产品使用
	 * @param product
	 * @return
	 */
	public boolean updateProduct(Product pro) {
		boolean flag = false;
		flag = productDAO.updateProduct(pro);
		return flag;
	}
	
	/**
	 * 添加产品-入库申请记录
	 * author limengxin
	 * @param product
	 * @return
	 */
	public boolean saveInproductRelation(long productId,long inId) {
		boolean flag = false;
		flag = inproductRelationDAO.saveInproductRelation(productId,inId);
		return flag;
	}
	/**
	 * 新增产品
	 * @param product
	 * @return
	 */
	public boolean SaveProduct(List<Product> products,String ownedUnit) {
		boolean flag = false;
		flag = productDAO.saveProduct(products,ownedUnit);
		return flag;
	}
	/**
	 * 按照Id删除产品
	 * @param productId
	 * @return
	 */
	public boolean DeleteProductById(long productId) {
		boolean flag = false;
		flag = productDAO.DeleteProductById(productId);
		return flag;
	}
	
	/**
	 * 根据合同编号查询对应产品
	 * @param contractId
	 * @return
	 */
	public List<HashMap<String, String>> queryProductByContractId(String contractId,int curPageNum,int pageSize,String status) {
		List<HashMap<String, String>> pros = new ArrayList<HashMap<String,String>>();
		pros = productDAO.queryProductsByContractId(contractId,curPageNum,pageSize,status);
		return pros;
	}
	
	/**
	 * 根据合同编号查询对应产品
	 * @param contractId
	 * @return
	 */
	public List<HashMap<String, String>> queryProductDetailByContractId(String contractId,int curPageNum,int pageSize,String status) {
		List<HashMap<String, String>> pros = new ArrayList<HashMap<String,String>>();
		pros = productDAO.queryProductDetailByContractId(contractId,curPageNum,pageSize,status);
		return pros;
	}
	
	/**
	 * 根据合同编号计算对应产品数量
	 * @param contractId
	 * @return
	 */
	public List<Integer> queryProNumByContractId(String contractId,String status) {
		List<Integer> counts = new ArrayList<Integer>();
		counts = productDAO.queryProNumByContractId(contractId,status);
		return counts;
	}
	
	public List<String> getProductByContractIdProductModelAndProductUnit(String contractId, String model, String unit)
	{
		List<String> info = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		info = productDAO.queryProductByContractIdProductModelAndProductUnit(contractId, model, unit);
		System.out.println(info);
		int count = Integer.parseInt(info.get(info.size() - 1));
		if(count == 1)
		{
			for(int i = 0; i < info.size() - 2; i++)
				result.add(info.get(i));
			result.add(info.get(info.size() - 2));
			result.add(info.get(info.size() - 2));
			result.add(info.get(info.size() - 1));
		}
		else
			for(int i = 0; i < info.size(); i++)
				if(i < (info.size() - count) || i > (info.size() - 3))
					result.add(info.get(i));
		
		return result;
	}
	/**
	 * 轮换管理中对产品的查询
	 * @author limengxin
	 */
	public List<HashMap<String, Object>> queryProductBorrow(Map<String,String> condition,int curPageNum,int pageSize) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		result = productDAO.queryProductBorrow(condition,curPageNum,pageSize);
		return result;
	}
	/**
	 * 轮换管理中对产品id的查询
	 * @author limengxin
	 */
	public List<Integer> queryProductID(Map<String,String> condition){
		List<Integer> ids=new ArrayList<Integer>();
		ids=productDAO.queryProductID(condition);
		return ids;
	}
	
	/**
	 * 通过合同Id查询产品条数
	 * 不group by
	 * 用于新入库
	 * @return
	 */
	public int getDetailSumByContractId(String contractId,String status) {
		int count = 0;
		count = productDAO.getDetailSumByContractId(contractId,status);
		return count;
	}
	
	/**
	 * 通过合同Id查询产品条数
	 * @return
	 */
	public int getSumByContractId(String contractId,String status) {
		int count = 0;
		count = productDAO.getSumByContractId(contractId,status);
		return count;
	}
	
	/**
	 * 通过inId查询产品
	 * @return
	 */
	public Product getProByInId(long inId) {
		Product pro = new Product();
		pro = productDAO.getProByInId(inId);
		return pro;
	}
	
	/**
	 * 保存出库产品关系表
	 * @param productId
	 * @param inId
	 * @return
	 */
	public boolean saveOutproductRelation(long productId, long inId) {
		boolean flag = false;
		flag = outproductRelationDAO.saveOutproductRelation(productId,inId);
		return flag;
	}
	
	/**
	 * 根据发料单查询出库产品
	 * @param conditions
	 * @return
	 */
	public List<List<Product>> getOutPros(List<Map<String,String>> conditions) {
		List<List<Product>> pros = new ArrayList<List<Product>>();
		pros = productDAO.getOutPros(conditions);
		return pros;
	}
	
	/**
	 * 改变产品状态
	 * @param pId
	 * @return
	 */
	public boolean changeProStatus(String[] pId,String[] status) {
		boolean flag = false;
		flag = productDAO.changeProStatus(pId, status);
		return flag;
	}
	
	/**
	 * 通过申请表查询产品
	 * @param inId
	 * @return
	 */
	public List<Product> findProsByInId(String inId) {
		List<Product> pros = productDAO.findProsByInId(inId);
		return pros;
	}
	/**
	 * 通过申请表查询出库产品
	 * @param inId
	 * @return
	 */
	public List<Product> findProsByOutId(String outId) {
		List<Product> pros = productDAO.findProsByOutId(outId);
		return pros;
	}
	/**
	 * 通过申请表查询产品原始价格
	 * @param inId
	 * @return
	 */
	public List<HashMap<String,String>> findOldPrice(String outId) {
		List<HashMap<String,String>> pros = productDAO.findOldPrice(outId);
		return pros;
	}
	/**
	 * 检查产品机号是否唯一
	 * @param deviceNo
	 * @return
	 */
	public boolean isDeviceNoExist(String deviceNo) {
		boolean flag = productDAO.isDeviceNoExist(deviceNo);
		return flag;
	}
	
	/**
	 * 根据品名内码、产品型号和产品单元查询对应产品
	 * @param pmnm,pname,punit
	 * @return
	 */
	public List<String> queryDeviceNoInByThreeP(String pmnm, String productName, String productUnit) {
		List<String> results = productDAO.queryDeviceNoInByThreeP(pmnm, productName, productUnit);
		return results;
	}
	
	/**
	 * 根据品名内码、产品型号和产品单元查询对应产品
	 * @param pmnm,pname,punit
	 * @return
	 */
	public List<String> queryDeviceNoApplyByThreeP(String pmnm, String productName, String productUnit) {
		List<String> results = productDAO.queryDeviceNoApplyByThreeP(pmnm, productName, productUnit);
		return results;
	}
	
	/**
	 * 
	 * @param unitName
	 * @return
	 */
	public Unit findUnitByName(String unitName) {
		Unit unit = unitDAO.findUnitByName(unitName);
		return unit;
	}
	/**
	 * 插入product
	 * @param dyadic
	 * @return
	 * @author LiangYH
	 */
//	public boolean saveProducts(List<ArrayList<String>> dyadic){
//		return productDAO.saveProducts(dyadic);
//	}
	
	/**
	 * 插入relation
	 * @param dyadic
	 * @return
	 * @author LiangYH
	 */
	public boolean saveInProductRelations(List<ArrayList<String>> dyadic){
		return inproductRelationDAO.saveInproductRelations(dyadic);
	}
	
	/**
	 * 插入relation
	 * @param dyadic
	 * @return
	 * @author LiangYH
	 */
	public boolean saveOutProductRelations(List<ArrayList<String>> dyadic){
		return outproductRelationDAO.saveOutProductRelations(dyadic);
	}
	
	/**
	 * 通过机号查询产品信息
	 * 主要用于自动填写申请表
	 * @param condition
	 * @return
	 */
	public Product findProductyNo(String deviceNo,boolean isIn) {
		return productDAO.findProductyNo(deviceNo,isIn);
	}
	
	public Product findProductyNo(String deviceNo) {
		return productDAO.findProductyNo(deviceNo);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> findDeviceByContract(String contractId) {
		return productDAO.findDeviceByContract(contractId);
	}
	
	/**
	 * 查找所有机号
	 * @return
	 */
	public List<String> findAllInDeviceNo(boolean isIn) {
		return productDAO.findAllInDeviceNo(isIn);
	}
	
	/**
	 * 军代室查找代储企业对应所有pmnm
	 * 查出来是每一条产品，需要在后台group by
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findAllPmnmByKeeper(String keeper, int curPageNum) {
		List<Map<String,Object>> results = productDAO.findAllPmnmByKeeper(keeper,curPageNum);
		List<Map<String,Object>> newResults = new ArrayList<Map<String,Object>>();
		boolean flag = true;
		for(int i=0;i<results.size();i++) {
			//如果新结果为空直接插入
			if(newResults.size() == 0) {
				newResults.add(results.get(i));
			} else {
				//如果新结果不为空，判断Model、Unit是否重复
				//重复则将该行数量+1
				//否则将该行插入
				String model = results.get(i).get("productModel").toString();
				String unit = results.get(i).get("productUnit").toString();
				for(int j=0;j<newResults.size();j++) {
					if(model.equals(newResults.get(j).get("productModel")) && unit.equals(newResults.get(j).get("productUnit"))) {
						flag = false;
						List<String> ids  = (List<String>)newResults.get(j).get("productId");
						List<String> preIds =(List<String>)results.get(i).get("productId"); 
						ids.add(preIds.get(0));
						newResults.get(j).put("count", (Integer)newResults.get(j).get("count")+1);
						newResults.get(j).put("productId", ids);
						break;
					}
				}
				if(flag) newResults.add(results.get(i));
				flag = true;
			}
		}
		return newResults;
	}
	
	public List<Map<String,Object>> findAllPmnmByKeeper2(String keeper, int curPageNum){
		List<Map<String,Object>> results = productDAO.findAllPmnmByKeeper(keeper,curPageNum);
		List<Map<String,Object>> newResults = new ArrayList<Map<String,Object>>();
		
		
		
		boolean isAdded = false;
		for(int i = 0; i < results.size()-1; i++){
			String model = results.get(i).get("productModel").toString();
			String unit = results.get(i).get("productUnit").toString();
			
			for(int k = 1; k <results.size(); k++){
				if(model.equals(results.get(k).get("productModel")) && unit.equals(results.get(k).get("productUnit"))) {
					if(!isAdded){
						newResults.add(results.get(i));
						int newSize = newResults.size();
						newResults.get(newSize-1).put("count", (Integer)newResults.get(newSize-1).get("count")+1);
						List<String> tempNewProductIds = (List<String>)newResults.get(newSize-1).get("productId");
						List<String> tempOldProductIds = (List<String>)results.get(k).get("productId");
						tempNewProductIds.addAll(tempOldProductIds);
						isAdded = true;
						continue;
					}
					int newSize = newResults.size();
					newResults.get(newSize-1).put("count", (Integer)newResults.get(newSize-1).get("count")+1);
					List<String> tempNewProductIds = (List<String>)newResults.get(newSize-1).get("productId");
					List<String> tempOldProductIds = (List<String>)results.get(k).get("productId");
					tempNewProductIds.addAll(tempOldProductIds);
					
					results.remove(k);
				}
			}
		}
		
		int totalSize = newResults.size();
		int totalPageNum = 1;
		if(totalSize %10 == 0)
			totalPageNum = totalSize /10;
		else
			totalPageNum = totalSize/10 +1;
		
		if(curPageNum > totalPageNum || curPageNum < 1) curPageNum = 1;
		
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		
		int j = (curPageNum-1)*10;
		int temp = (curPageNum-1)*10+10;
		if(temp > totalSize) temp = totalSize;
		
		for(; j < temp; j++){
			returnList.add(newResults.get(j));
		}
		
		return returnList;
	}
	
	public List<Map<String,Object>> findAllPmnmByKeeper3(String keeper, int curPageNum){
		List<Map<String,Object>> results = productDAO.findAllPmnmByKeeper(keeper,curPageNum);
		List<Map<String,Object>> newResults = new ArrayList<Map<String,Object>>();
		
		for(Map<String,Object> m:results){
			System.out.println(m);
		}
//		System.out.println("ll");
		
		if(results.size() == 0) return null;
		
		newResults.add(results.get(0));
		results.get(0).put("count",1);
		
		Map<String,Object> mapFlag = results.get(0);
		int i = 1;
		for(; i <results.size(); i++){
			
			String model = mapFlag.get("productModel").toString().trim();
			String unit = "";
			if(mapFlag.containsKey("productUnit")) {
				unit = mapFlag.get("productUnit").toString().trim();
			}
			String tempProductModel = results.get(i).get("productModel").toString().trim();
			String tempUnit = results.get(i).get("productUnit").toString().trim();
			if(model.equals(tempProductModel) && unit.equals(tempUnit)) {
				int newSize = newResults.size();
				newResults.get(newSize-1).put("count", (Integer)newResults.get(newSize-1).get("count")+1);
				List<String> tempNewProductIds = (List<String>)newResults.get(newSize-1).get("productId");
				List<String> tempOldProductIds = (List<String>)results.get(i).get("productId");
				tempNewProductIds.addAll(tempOldProductIds);
			}else{
				results.get(i).put("count", 1);
				newResults.add(results.get(i));
				mapFlag = results.get(i);
			}
		}
		
		int totalSize = newResults.size();
		int totalPageNum = 1;
		if(totalSize %10 == 0)
			totalPageNum = totalSize /10;
		else
			totalPageNum = totalSize/10 +1;
		
		if(curPageNum > totalPageNum || curPageNum < 1) curPageNum = 1;
		
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		
		int j = (curPageNum-1)*10;
		int temp = (curPageNum-1)*10+10;
		if(temp > totalSize) temp = totalSize;
		
		for(; j < temp; j++){
			returnList.add(newResults.get(j));
		}
		
		return returnList;
	}
	
	
	/**
	 * 查询具有这个品名内码的产品数量
	 * @param PMNM
	 * @return
	 */
	public Map<String,String> getProductCountByPMNM(Map<String,String> paramsMap){
		return productDAO.getCountByPMNM(paramsMap);
	}
	
	/**
	 * 
	 * @param productStatus
	 * @param flag
	 * @return
	 * @author LiangYH
	 */
	public List<Map<String,String>> getProducts(String productStatus, String flag,String curPageNum,String pageSize,Map<String,String> condition){
		if(productStatus == null) 
			new NullPointerException("productStatus must not be null");
		return productDAO.selectProduct(productStatus, flag,curPageNum,pageSize,condition);
	}
	
	/**
	 * 企业导入申请表的时候，相应地，改变产品表的状态
	 * @param dyadic 导入excel表的时候相应的产品表
	 * @return
	 */
	public boolean updateProduct_qy(List<ArrayList<String>> dyadic){
		return productDAO.updateProduct_qy(dyadic);
	}
	
	/**
	 * 统计产品的数量
	 * @param productModel 产品型号
	 * @param productUnit 产品单元
	 * @return 产品数量
	 * @author liangyihuai
	 */
	public int getProductNum(String productModel, String productUnit){
		return productDAO.getProductNum(productModel, productUnit);
	}
	
	/**
	 * 统计已入库的并且产品型号相同的产品的数量
	 * @return key:productModel;value:num
	 * @author liangyihuai
	 */
	public Map<String,Integer> getEachProductModelNum(){
		return productDAO.getEachProductModelNum();
	}
}
