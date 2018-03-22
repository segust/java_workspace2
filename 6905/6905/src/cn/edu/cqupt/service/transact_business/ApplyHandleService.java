package cn.edu.cqupt.service.transact_business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.dao.CommonShareDAO;
import cn.edu.cqupt.dao.AccountDAO;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.dao.InfoDAO;
import cn.edu.cqupt.dao.LogDAO;
import cn.edu.cqupt.dao.OutApplyDAO;
import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.service.qualification_management.InfoService;
import cn.edu.cqupt.util.MyDateFormat;

public class ApplyHandleService {
	private InApplyDAO dao = null;
	private LogDAO logDao =null;
	private OutApplyDAO outApplyDao = null;
    private AccountDAO accountDao=null;
	private ProductDAO productDao = null;
	private CommonShareDAO commonShareDao = null;

	public ApplyHandleService() {
		dao = new InApplyDAO();
		outApplyDao = new OutApplyDAO();
		productDao = new ProductDAO();
		accountDao=new AccountDAO();
		logDao=new LogDAO();
		//this.applyFormOperation = new ApplyFormOperation();

		new ApplyFormOperation();
		this.commonShareDao = new CommonShareDAO();

	}

	/**
	 * 得到总条数
	 * 
	 * @return
	 */
	public int getAllSum() {
		int count = 0;
		count = dao.getSum();
		return count;
	}
	/**
	 * 得到更新历史总条数
	 * 
	 * @return
	 */
	public int SelecthistorySum(String deviceNo,String productModel) {
		int sum = 0;
		sum = logDao.SelecthistorySum(deviceNo,productModel);
		return sum;
	}
	/**
	 * 分页查询
	 */
	public List<HashMap<String, Object>> getInApply(int curPageNum, int pageSize) {
		List<HashMap<String, Object>> apply = new ArrayList<HashMap<String, Object>>();
		apply = dao.getInApply(curPageNum, pageSize);
		return apply;
	}
	
	/**
	 * 入库申请表的查询（军代局）
	 * 需要把查到的企业名字转换成对应的军代室
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 */
	public List<HashMap<String, Object>> getInApply_JDJ(int curPageNum, int pageSize) {
		List<HashMap<String, Object>> apply = new ArrayList<HashMap<String, Object>>();
		apply = dao.getInApply(curPageNum, pageSize);
		
		changeOwnedUnitNames(apply,true);
		
//		//把公司名字放到Map中（用到Key不重复的特性）
//		Map<String,String> names= new HashMap<String,String>();
//		int len = 0;
//		if(apply != null) len = apply.size();
//		//apply数组中的最后一个元素是sum
//		for(int i = 0; i < len-1; i++){
//			HashMap<String,Object> map = apply.get(i);
//			InApply ia = (InApply) map.get("apply");
//			names.put(ia.getOwnedUnit(), "");
//		}
//		//获取企业名和军代室名的对应关系
//		InfoService infoService = new InfoService();
//		List<String> nameList = new ArrayList<String>();
//		nameList.addAll(names.keySet());//只取key
//		Map<String,String> nameMap = infoService.getJdsThroughCompany(nameList);
//		//替换掉ownedUnit
//		for(int i = 0; i < len-1; i++){
//			InApply ia = (InApply)apply.get(i).get("apply");
//			String name_JDS = nameMap.get(ia.getOwnedUnit());
//			ia.setOwnedUnit(name_JDS);
//		}
		return apply;
	}

	/**
	 * 条件查询
	 * 
	 * @param contractId
	 * @param productType
	 * @param unitName
	 * @param operateType
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 */
//	public List<HashMap<String, Object>> selectInApply(String contractId,
//			String productType, String unitName, String operateType,
//			String fromDate, String toDate, String status, int curPageNum,
//			int pageSize) {
//		List<HashMap<String, Object>> apply = new ArrayList<HashMap<String, Object>>();
//		apply = dao.selectInApply(contractId, productType, unitName,operateType,
//				fromDate, toDate, status, null, null, curPageNum, pageSize);
//		return apply;
//	}

	/**
	 * 条件查询出库申请表
	 * 
	 * @param contractId
	 * @param productType
	 * @param unitName
	 * @param operateType
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 * @author LiangYH
	 */
	public List<HashMap<String, Object>> selectOutApply(String contractId,
			String productType, String unitName, String operateType,
			String fromDate, String toDate, String status, String keeper, 
			String manufacturer, int curPageNum, int pageSize) {
		
		List<HashMap<String, Object>> apply = new ArrayList<HashMap<String, Object>>();
		
		apply = outApplyDao.selectOutApply(contractId, productType, unitName,
				operateType, fromDate, toDate, status, keeper, manufacturer, curPageNum, pageSize);
		
		return apply;
	}

	/**
	 * 出库申请表列表查询（军代局）
	 * @param contractId
	 * @param productType
	 * @param unitName
	 * @param operateType
	 * @param fromDate
	 * @param toDate
	 * @param status
	 * @param keeper
	 * @param manufacturer
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 */
	public List<HashMap<String, Object>> selectOutApply_JDJ(String contractId,
			String productType, String unitName, String operateType,
			String fromDate, String toDate, String status, String keeper, 
			String manufacturer, int curPageNum, int pageSize) {
		
		List<HashMap<String, Object>> apply = new ArrayList<HashMap<String, Object>>();
		
		apply = outApplyDao.selectOutApply(contractId, productType, unitName,
				operateType, fromDate, toDate, status, keeper, manufacturer, curPageNum, pageSize);
		
		changeOwnedUnitNames(apply,false);
		
		return apply;
	}

	/**
	 * 	条件查询入库申请表
	 * @author LiangYH 
	 */
	public List<HashMap<String, Object>> selectInApply(String contractId,
			String productType, String unitName, String operateType,
			String keeper, String manufacturer, String fromDate,
			String toDate, String status, int curPageNum,int pageSize) {
		List<HashMap<String, Object>> apply = new ArrayList<HashMap<String, Object>>();
		apply = dao.selectInApply(contractId, productType, unitName, operateType,
				fromDate, toDate, status, keeper, manufacturer, curPageNum,pageSize);
		return apply;
	}
	
	/**
	 * 	条件查询入库申请表（军代局）
	 * @author LiangYH 
	 */
	public List<HashMap<String, Object>> selectInApply_JDJ(String contractId,
			String productType, String unitName, String operateType,
			String keeper, String manufacturer, String fromDate,
			String toDate, String status, int curPageNum,int pageSize) {
		List<HashMap<String, Object>> apply = new ArrayList<HashMap<String, Object>>();
		apply = dao.selectInApply(contractId, productType, unitName, operateType,
				fromDate, toDate, status, keeper, manufacturer, curPageNum,pageSize);
		
		changeOwnedUnitNames(apply,true);
		
		return apply;
	}
	
	/**
	 * 把列表查询出来的入库申请表中的ownedUnit字段该成相对应的军代室的名字
	 * @param applys 是一个List，其中第一个元素是sum，其他的元素是Map,map的key为apply或者product
	 * @param isInApply 是否为入库申请
	 * @author liangyihuai
	 */
	private void changeOwnedUnitNames(List<HashMap<String, Object>> applys, boolean isInApply){
		//存放企业名字（利用key不重复的特性）
		Map<String,String> names = new HashMap<String,String>();
		int len = 0;
		if(applys != null)
			len = applys.size();
		if(isInApply){
			for(int i = 0; i < len-1; i++){
				InApply ia = (InApply)applys.get(i).get("apply");
				names.put(ia.getOwnedUnit(), "");
			}
		}else{
			for(int i = 0; i < len-1; i++){
				OutApply ia = (OutApply)applys.get(i).get("apply");
				names.put(ia.getOwnedUnit(), "");
			}
		}
		//获取企业名和军代室名的对应关系
		InfoService infoService = new InfoService();
		List<String> nameList = new ArrayList<String>();
		nameList.addAll(names.keySet());
		Map<String,String> nameMap = infoService.getJdsThroughCompany(nameList);
		//替换掉ownedUnit
		if(isInApply){
			for(int i = 0; i < len-1; i++){
				InApply ia = (InApply)applys.get(i).get("apply");
				String name_JDS = nameMap.get(ia.getOwnedUnit());
				ia.setOwnedUnit(name_JDS);
			}
		}else{
			for(int i = 0; i < len-1; i++){
				OutApply ia = (OutApply)applys.get(i).get("apply");
				String name_JDS = nameMap.get(ia.getOwnedUnit());
				ia.setOwnedUnit(name_JDS);
			}
		}
	}

	/**
	 * 修改产品状态
	 * 
	 * @param dyadicArray
	 * @return
	 */
	public boolean changeInApplyCheckStatus(List<ArrayList<String>> dyadicArray,String checkPerson, String applyType) {
		boolean flag = false;
		
		flag = dao.changeInApplyCheckStatus(dyadicArray,checkPerson,applyType);
		return flag;
	}

	/**
	 * @author 军代室 列表查询 中 产品详细信息的查询
	 * @param Inid
	 * @return
	 */
	public ArrayList<String> queryDeviceNobyApplyId(int Inid) {
		ArrayList<String> deviceNo = new ArrayList<String>();
		deviceNo = dao.queryDeviceNobyApplyId(Inid);
		return deviceNo;
	}
	/**
	 * @author 军代室 列表查询 中 产品详细信息的查询
	 * @param Outid
	 * @return
	 */
	public ArrayList<String> queryDeviceNobyOutId(int Outid) {
		ArrayList<String> deviceNo = new ArrayList<String>();
		deviceNo = dao.queryDeviceNobyOutId(Outid);
		return deviceNo;
	}
	/**
	 * @author 军代室 列表查询 中 产品详细信息的查询
	 * @param Inid
	 * @return
	 */
	public HashMap<String, Object> queryInProductbyDeviceNo(String deviceNo,int inId) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info = dao.queryInProductbyDeviceNo(deviceNo,inId);
		return info;
	}
	/**
	 * @author 军代室 列表查询 中 产品详细信息的查询
	 * @param Outid
	 * @return
	 */
	public HashMap<String, Object> queryOutProductbyDeviceNo(String deviceNo,int outId) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info = dao.queryOutProductbyDeviceNo(deviceNo,outId);
		return info;
	}
	/**
	 * @author 军代室 列表查询 中 产品维护历史查询
	 * @param Inid
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> selectMaintainhistory(String productModel,String deviceNo,int curPageNum,int pageSize) {
		ArrayList<HashMap<String, Object>> T = new ArrayList<HashMap<String, Object>>();
		T = logDao.selectMaintainhistory(productModel,deviceNo,curPageNum,pageSize);
		return T;
	}
	/*public boolean ChangApplyStatuebyInid(List<Integer> Inids, String opFlag,
			String type) {
		return dao.ChangApplyStatuebyInid(Inids, opFlag, type);
	}*/

	/**
	 * 将二维数组中的数据插入到数据库inApply表中 其中文件的第一行是标题行，具体为： { "inId", "产品型号", "单元名称",
	 * "机号", "单价","数量", "计量单位", "类型", "操作类型", "操作日期", "生产日期", "存储期限","企业定期维护周期",
	 * "承制单位", "代储单位", "合同编号", "状态" } （用于军代室导入入库申请excel文件）
	 */
	/*public boolean insertDyadicToInApply(List<ArrayList<String>> dyadic) {
		boolean runStatus = true;
		List<InApply> inApplyList = new ArrayList<InApply>();

		try {
			// i从1开始,因为第一行是标题行
			for (int i = 1; i < dyadic.size(); i++) {
				InApply inApply = new InApply();

				ArrayList<String> list = dyadic.get(i);

				inApply.setInId(Integer.parseInt(list.get(0)));
				inApply.setProductType(list.get(1));
				inApply.setUnitName(list.get(2));
				inApply.setDeviceNo(list.get(3));
				inApply.setNewPrice(Double.valueOf(list.get(4)));
				inApply.setNum(Integer.parseInt(list.get(5)));
				inApply.setMeasure(list.get(6));
				// 跳过第六个，需求问题
				inApply.setInMeans(list.get(8));
				inApply.setExecDate(MyDateFormat.changeStringToDate(list.get(9)));
				inApply.setProducedDate(MyDateFormat.changeStringToDate(list
						.get(10)));
				inApply.setStorageTime(list.get(11));
				inApply.setMaintainCycle(list.get(12));
				inApply.setManufacturer(list.get(13));
				inApply.setKeeper(list.get(14));
				inApply.setContractId(list.get(15));
				inApply.setChStatus(list.get(16));
				inApplyList.add(inApply);
			}
		} catch (Exception e) {
			runStatus = false;
			e.printStackTrace();
		}
		if (runStatus) {
			runStatus = dao.saveApply(inApplyList);
		}

		return runStatus;
	}
*/
	/**
	 * 查找最新插入的inapply表中的数据
	 * 
	 * @return
	 */
	/*public List<InApply> getInApplyOfLatestInsertTime() {
		return dao.selectByLatestInsertTime();
	}*/

	/**
	 * 插入outApply
	 * 
	 * @param outApplys
	 * @return runingStatus
	 * @author LiangYH
	 */
	public boolean saveOutApply(List<ArrayList<String>> dyadic,
			String operateType) {

		List<OutApply> outApplyList = new ArrayList<OutApply>();
		boolean runStatus = true;
		try {
			// i从1开始,因为第一行是标题行
			int len = dyadic.size();
			for (int i = 1; i < len; i++) {
				ArrayList<String> list = dyadic.get(i);
				OutApply apply = new OutApply();

				apply.setOutId(Integer.parseInt(list.get(0)));
				apply.setProductType(list.get(1));
				apply.setUnitName(list.get(2));
				apply.setDeviceNo(list.get(3));
				apply.setNewPrice(Double.valueOf(list.get(4)));
				apply.setNum(Integer.parseInt(list.get(5)));
				apply.setMeasure(list.get(6));
				// 第7个不清楚，需求问题
				apply.setOutMeans(list.get(8));
				apply.setExecDate(MyDateFormat.changeStringToDate(list.get(9)));
				apply.setProducedDate(MyDateFormat.changeStringToDate(list
						.get(10)));
				apply.setStorageTime(list.get(11));
				apply.setMaintainCycle(list.get(12));
				apply.setManufacturer(list.get(13));
				apply.setKeeper(list.get(14));
				apply.setContractId(list.get(15));
				apply.setChStatus(list.get(16));

				if ("GXCK".equals(operateType)) {
					apply.setOldPrice(Double.valueOf(list.get(17)));
				} else if ("LHCK".equals(operateType)) {
					apply.setBorrowLengthString(list.get(17));
				}
				apply.setOwnedUnit(list.get(18));
				System.out.println("apply = " + apply);
				outApplyList.add(apply);
			}
		} catch (Exception e) {
			runStatus = false;
			e.printStackTrace();
		}

		if (runStatus) {
			runStatus = outApplyDao.saveOutApply(outApplyList);
		}
		return runStatus;
	}

	/**
	 * 改变出库申请表的状态和备注
	 * 
	 * @param dyadic
	 *            note:第一行是标题行
	 * @return runingStatus
	 * @author LiangYH
	 */
//	public boolean changeChStatusAndRemark(List<ArrayList<String>> dyadic,String checkPerson) {
//		return outApplyDao.changeChStatusAndRemark(dyadic,checkPerson);
//	}

	/*
	 * 改变出库申请表的状态
	 * 
	 * @param dyadic note:第一行是标题行
	 */
	public boolean changeOutApplyChStatus(List<ArrayList<String>> dyadic, String checkPerson) {
		return outApplyDao.changeOutApplyCheckStatus(dyadic,checkPerson);
	}

	/**
	 * 
	 * @return
	 */
/*	public List<OutApply> getOutApplyOfLatestInsertTime() {
		return outApplyDao.getOutApplyOfLatestInsertTime();

	}*/

/*	public InApply getInApply(long inId) {
		InApply apply = dao.getInApply(inId);
		return apply;
	}*/

	/**
	 * 企业轮换出库申请
	 * @param mapList 为产品机号及型号信息
	 * @return runStatus
	 * @author LiangYH
	 */
	public boolean borrowOutWarehouse(List<HashMap<String, String>> mapList,HashMap<String,String> applyMap) {
		if(mapList == null || mapList.size() == 0){
			return false;
		}
		// 判断用户输入的deviceNo是否正确
//		productDao = new ProductDAO();
//		boolean flag = false;
//		int len = mapList.size();
//		HashMap<String, String> map = null;
//		for (int i = 0; i < len; i++) {
//			map = mapList.get(i);
//			flag = productDao.checkByDeviceNo(map.get("deviceNo"));
//			if (!flag)
//				return false;
//		}

		// 执行数据库操作
		boolean runStatus = outApplyDao.borrowOutAwareOperate(mapList,applyMap);
		return runStatus;
	}

	/**
	 * 企业轮换入库申请
	 * 
	 * @param mapList
	 * @return runStatus
	 * @author LiangYH
	 */
	public boolean borrowInWarehouse(List<HashMap<String, String>> mapList,String inMeans) {
		if(mapList == null || mapList.size() == 0) return false;
		
		// 判断用户输入的deviceNo是否已经存在数据库中
//		boolean flag = false;
//		int len = mapList.size();
//		HashMap<String, String> map = null;
//		for (int i = 0; i < len; i++) {
//			map = mapList.get(i);
//			flag = productDao.checkByDeviceNo(map.get("deviceNo"));
//			if (flag) return false;
//		}
		
		//执行数据库操作
		return dao.update_borrow_InAwareOperate(mapList,inMeans);
	}

	/**
	 * 企业更新入库
	 * @param mapList
	 * @return
	 */
	public boolean updateInAwarehouse(List<HashMap<String, String>> mapList,String inMeans) {
		// 判断用户输入的deviceNo是否已经存在数据库中
//		boolean flag = false;
//		int len = mapList.size();
//		HashMap<String, String> map = null;
//		for (int i = 0; i < len; i++) {
//			map = mapList.get(i);
//			flag = productDao.checkByDeviceNo(map.get("deviceNo"));
//			if (flag) return false;
//		}
		//执行数据库操作
		return dao.update_borrow_InAwareOperate(mapList,inMeans);
	}

	/**
	 * 企业更新出库
	 * @param mapList
	 * @return
	 */
	public boolean updateOutAwarehouse(List<HashMap<String, String>> mapList,Map<String,String>applyMap) {
		// 判断用户输入的deviceNo是否正确
//		productDao = new ProductDAO();
//		boolean flag = false;
//		int len = mapList.size();
//		HashMap<String, String> map = null;
//		for (int i = 0; i < len; i++) {
//			map = mapList.get(i);
//			flag = productDao.checkByDeviceNo(map.get("deviceNo"));
//			if (!flag)
//				return false;
//		}
		// 执行数据库操作
		boolean runStatus = outApplyDao.updateOutAwareOperate(mapList,applyMap);
		return runStatus;
	}

	/**
	 * 多表查询入库申请表、产品表、申请-产品关系表
	 * @param inIDs 入库申请表的id数组
	 * @param ownedUnit ownedUnit
	 * @return
	 */
	public Map<String, Object> queryMultiFormByInId(List<Long> inIDs, String ownedUnit) {
		//存储数据库的表名
		List<String> tables = new ArrayList<String>();
		tables.add("qy_inapply");
		tables.add("qy_inproductrelation");
		tables.add("qy_product");
		return commonShareDao.queryMultiForm(inIDs,ownedUnit, tables, true);
	}

	/**
	 * 多表查询出库申请表、产品表、申请-产品关系表
	 * @param outIDs 出库申请表的id数组
	 * @param ownedUnit
	 * @return
	 */
	public Map<String, Object> queryMultiFormByOutID(List<Long> outIDs,String ownedUnit) {
		//存储数据库的表名
		List<String> tables = new ArrayList<String>();
		tables.add("qy_outapply");
		tables.add("qy_outproductrelation");
		tables.add("qy_product");
		return commonShareDao.queryMultiForm(outIDs,ownedUnit, tables, false);
	}
	

	
	/**
	 * 多表插入
	 * @param applyDyadic
	 * @param relationDyadic
	 * @param productDyadic
	 * @return
	 */
	public boolean saveOutApplys(List<ArrayList<String>> applyDyadic, 
			List<ArrayList<String>> relationDyadic, 
			List<ArrayList<String>> productDyadic){
		if(applyDyadic == null || relationDyadic == null || productDyadic == null){
			return false;
		}
		int s1 = applyDyadic.size();
		int s2 = relationDyadic.size();
		int s3 = productDyadic.size();
		if(s1 == 0 || s2 == 0 || s3 == 0){
			return false;
		}
		//增加数据库表的名字
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("qy_outapply");
		tableNames.add("qy_outproductrelation");
		tableNames.add("qy_product");
		
		return commonShareDao.insertThreeTables(applyDyadic,relationDyadic, productDyadic,tableNames);
	}
	
	/**
	 * 多表插入
	 * @param applyDyadic
	 * @param relationDyadic
	 * @param productDyadic
	 * @return
	 */
	public boolean saveInApplys(List<ArrayList<String>> applyDyadic, 
			List<ArrayList<String>> relationDyadic, 
			List<ArrayList<String>> productDyadic){
		if(applyDyadic == null || relationDyadic == null || productDyadic == null){
			return false;
		}
		int s1 = applyDyadic.size();
		int s2 = relationDyadic.size();
		int s3 = productDyadic.size();
		if(s1 == 0 || s2 == 0 || s3 == 0){
			return false;
		}
		//增加数据库表的名字
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("qy_inapply");
		tableNames.add("qy_inproductrelation");
		tableNames.add("qy_product");
		
		return commonShareDao.insertThreeTables(applyDyadic,
				relationDyadic, productDyadic,tableNames);
	}
	
	public List<InApply> getInApply(int a, int b, boolean flag){
		return dao.getInApply(a, b, true);
	}
	/**
	 * @author 军代室 列表查询 中 更新历史查询
	 * @param Inid
	 * @return
	 */
	public HashMap<String, Object> queryUpdateHistory(String DeviceNo,String productModel,String means) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		info = productDao.queryUpdateHistory(DeviceNo,productModel,means);
		return info;
	} 

}