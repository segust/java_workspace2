package cn.edu.cqupt.service.transact_business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.OutList;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.dao.CommonShareDAO;
import cn.edu.cqupt.dao.OutListDAO;
import cn.edu.cqupt.dao.OutListProductRelationDAO;
import cn.edu.cqupt.dao.ProductDAO;

public class OutListHandleService {

	private OutListDAO outListDao = null;
	private OutListProductRelationDAO relationDAO = null;
	private CommonShareDAO commonShareDao = null;
	private ProductDAO productDao = null;

	public OutListHandleService() {
		outListDao = new OutListDAO();
		relationDAO = new OutListProductRelationDAO();
		commonShareDao = new CommonShareDAO();
		productDao = new ProductDAO();
	}

	/**
	 * 将导入的发料单存入数据库
	 * 
	 * @param list
	 * @return
	 */
	public boolean saveList(List<OutList> lists) {
		boolean flag = false;
		flag = outListDao.saveList(lists);
		return flag;
	}

	/**
	 * 按照料单编号查询料单
	 * 
	 * @param listId
	 * @return
	 */
	public List<OutList> findListById(String listId, String date) {
		List<OutList> lists = outListDao.findListById(listId, date);
		return lists;
	}

	/**
	 * 
	 * @param lId
	 * @param pId
	 * @return
	 */
	public boolean addRelation(String lId, String[] pId) {
		boolean flag = false;
		flag = relationDAO.addRelation(lId, pId);
		return flag;
	}

	/**
	 * 
	 * @param headMap
	 *            word文件的上面四个
	 * @param tableDyadic
	 *            word文件的下面表单
	 * @param 产品id组成的二维数组
	 * @param keeper
	 *            代储单位
	 * @param 发料单方式
	 *            ：调拨出库、轮换出库和更新出库
	 * @return key:runStatus(boolean), outList(List<ArrayList<String>>),
	 *         relationList<ArrayList<String>>)
	 * @author LiangYH
	 */
	public Map<String, Object> operationOutListInJDS(
			Map<String, String> headMap, List<ArrayList<String>> tableDyadic,
			List<ArrayList<String>> productIDs, String keeper, String outMeans) {

		// 封装查询product的条件
		// List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		// for(int i = 0; i < tableDyadic.size(); i++){
		// Map<String,String> tempMap = new HashMap<String,String>();
		// ArrayList<String> tempList = tableDyadic.get(i);
		//
		// tempMap.put("PMNM",tempList.get(1));
		// tempMap.put("productModel", tempList.get(2));
		// tempMap.put("unit", tempList.get(3));
		// tempMap.put("price", tempList.get(8));
		// tempMap.put("productNum", tempList.get(6));
		//
		// list.add(tempMap);
		// }

		// if(outMeans == null || "".equals(outMeans)){
		// Map<String,Object> tempMap = new HashMap<String,Object>();
		// tempMap.put("runStatus", false);
		// return tempMap;
		// }

		return outListDao.operationOutListInJDS(headMap, tableDyadic,
				productIDs, keeper, outMeans);
	}

	/**
	 * 向outlist表和relation表中插入数据库，同时根据改变产品表的状态
	 * <p>插入数据库的操作只是单纯地把excel表中的内容全部插入outlist和relation表的过程，中间没有其他操作</p>
	 * <p>如果是向企业版中导入发料单，那么产品表中的状态相应地应该变为调拨待出库、更新待出库、轮换待出库</p>
	 * <p>如果是军代室或以上版本，那么产品表中的状态只变成“已出库”这一种状态</p>
	 * @param outListDyadic
	 * @param relationDyadic
	 * @param productStatus 将要把导入的所有的产品改成此状态
	 * @param outListType
	 * 			  发料出库方式，比如发料轮换出库、发料更新出库
	 * @param version 版本号1-4
	 * @return
	 * @author LiangYH
	 */
	public boolean insertOutlistAndRelation(List<ArrayList<String>> outListDyadic, 
			List<ArrayList<String>> relationDyadic, String productStatus,
			String outListType,String version){
		if(outListDyadic == null || relationDyadic == null) return false;
		
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("qy_outlist");
		tableNames.add("qy_outlistproductrelation");
		
		//产品表的flag字段
		String productFlag = "2";
		if("1".equals(version)) productFlag = "1";
		
		boolean flag = false;
		flag = commonShareDao.insertThreeTables(outListDyadic, relationDyadic,null, tableNames);
		if(flag) 
			flag = productDao.updateProductStatus(relationDyadic, productStatus,outListType,productFlag);
		return flag;
	}
	
	/**
	 * 查询发料单信息
	 * */
	public List<HashMap<String, Object>> searcheList(int curPageNum,
			int pageSize) {
		List<HashMap<String, Object>> inApplyList = outListDao.searchList(
				curPageNum, pageSize);
		return inApplyList;
	}

	/**
	 * 查询发料单数量
	 * */
	public int searchListSum() {
		int count = outListDao.searchListSum();
		return count;
	}

	/**
	 * 以下部分是用于出库操作
	 * */

	/**
	 * 查询发料单信息 用于显示发料单
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<HashMap<String, String>> selectOutList(String type,int curPageNum,int pageSize) {
		// HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>();
		// resultMap = outListDao.selectOutListForOut(type);
		// resultMap = outListDao.selectOutListForIn(type,resultMap);

		// ArrayList<HashMap<String, String>> resultList = new
		// ArrayList<HashMap<String, String>>();
		// Iterator iter = resultMap.entrySet().iterator();
		// while(iter.hasNext()){
		// Map.Entry entry = (Map.Entry) iter.next();
		// HashMap<String, String> val = (HashMap<String,
		// String>)entry.getValue();
		// resultList.add(val);
		// }
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		resultList=outListDao.getOutList(type,curPageNum,pageSize);
		return resultList;
	}
	
	public int getOutListNum(String type){
		int outListNum=0;
		outListNum=outListDao.getOutListNum(type);
		return outListNum;
	}
	
	/**
	 * 每个发料单位下面产品的出库情况
	 * @param type
	 * @return
	 */
	public int getOutListProNum(String type,String listId){
		int outListNum=0;
		outListNum=outListDao.getOutListProNum(type,listId);
		return outListNum;
	}

	/**
	 * 查询发料单详细信息 用于显示发料单内部出库产品情况
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<HashMap<String, String>> selectOutListDetail(String type,String listId,int curPageNum,int pageSize) {
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		resultList=outListDao.selectOutListDetail(type,listId,curPageNum,pageSize);
		return resultList;
	}

	/**
	 * 查询要出库的机号
	 * */
	public ArrayList<Product> selectDeviceNo(HashMap<String, String> condition) {
		ArrayList<Product> productList = outListDao.selectDeviceNo(condition);
		return productList;
	}

	/**
	 * 产品出库
	 * change by lyt 1107
	 * */
	public List<Map<String,String>> updateProStatus(HashMap<String, String> condition) {
		List<Map<String,String>> resultList = outListDao.updateProStatus(condition);
		return resultList;
	}
	//delete by lyt 1107
/*	public static void main(String[] args) {
		OutListHandleService outListHandleService = new OutListHandleService();
		// ArrayList<HashMap<String, String>> result =
		// outListHandleService.selectOutList(1,10);
		// for (int i = 0; i < result.size(); i++) {
		// System.out.print(result.get(i).get("productModel")+" ");
		// System.out.print(result.get(i).get("num")+" ");
		// System.out.print(result.get(i).get("count")+" ");
		// System.out.print(result.get(i).get("leftCount")+" ");
		// System.out.println();
		// }
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("listId", "123");
		condition.put("ownedUnit", "代储企业1");
		condition.put("PMNM", "品名内码1");
		condition.put("productModel", "歼击十五型舰载战斗机-单元名称1");
		condition.put("count", "1");
		condition.put("outMeans", "已出库");
		// ArrayList<String> deviceNoList =
		// outListHandleService.selectDeviceNo(condition);
		// for (int i = 0; i < deviceNoList.size(); i++) {
		// System.out.println(deviceNoList.get(i));
		// }
		boolean flag = outListHandleService.updateProStatus(condition);
		if (flag)
			System.out.println("出库成功");
	}*/
}
