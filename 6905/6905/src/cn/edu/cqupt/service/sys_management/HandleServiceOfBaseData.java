package cn.edu.cqupt.service.sys_management;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.Basedata;
import cn.edu.cqupt.beans.Unit;
import cn.edu.cqupt.dao.BasedataDAO;
import cn.edu.cqupt.dao.CommonShareDAO;
import cn.edu.cqupt.dao.UnitDAO;
import cn.edu.cqupt.db.DBConnection;

public class HandleServiceOfBaseData {
	
	private Connection conn=null;
	private BasedataDAO basedataDAO=null;
	private UnitDAO unitDAO=null;
	private CommonShareDAO commonShareDao = null;

	public HandleServiceOfBaseData() {
		basedataDAO=new BasedataDAO();
		unitDAO=new UnitDAO();
		commonShareDao = new CommonShareDAO();
	}
	
	/**
	 * 查询总条数
	 * 方法太低级，需采用count
	 * */
	public int selectSum(HashMap<String, Object> key){
		int sum=0;
		sum=basedataDAO.SearchOfBasedataSum(key);
		return sum;
	}
	
	/**
	 * 查询基础数据库
	 * */
	public ArrayList<Basedata> selectBasedata(HashMap<String, Object> key,int curPageNum, int pageSize){
		ArrayList<Basedata> T=new ArrayList<Basedata>();
		T=basedataDAO.SearchOfBasedata(key,curPageNum,pageSize);
		return T;
	}
	
	/**
	 * 加入新记录
	 * */
	public boolean addBasedata(ArrayList<Basedata> dyadicArray){
		boolean flag=false;
		flag=basedataDAO.SavaOfBaseData(dyadicArray);
		return flag;
	}
	
	/**
	 * 根据id查询一条记录
	 * */
	public Basedata selectBasedataById(String id){
		Basedata basedata=new Basedata();
		basedata=basedataDAO.GetBasedataById(id);
		return basedata;
	}
	
	/**
	 * 编辑一条基础数据库记录
	 * */
	public boolean updateBasedata(Basedata basedata){
		boolean flag=false;
		flag=basedataDAO.UpdateOfBasedata(basedata);
		return flag;
	}
	
	/**
	 * 删除一条基础数据库纪录
	 * */
	public boolean deleteBasedata(String id){
		boolean flag=false;
		flag=basedataDAO.DelOfbasedata(id);
		return flag;
	}
	
	/**
	 * 批量删除基础数据库纪录
	 * */
	public boolean deleteBasedataBitch(List<Integer> T){
		boolean flag=false;
		flag=basedataDAO.DeleteBaseData(T);
		return flag;
	}
	
	/**
	 * 通过品名内码自动查询基础数据库中信息
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public Map<String,String> findInfoByPmnm(String pmnm) {
		Map<String,String> result = basedataDAO.findInfoByPmnm(pmnm);
		return result;
	}
	
	/**
	 * 通过品名内码自动查询基础数据库中信息
	 * 军代室及以上版本查询
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public List<String> findInfoByPmnmAndKeeper(String pmnm,String keeper) {
		List<String> result = basedataDAO.findInfoByPmnmAndKeeper(pmnm,keeper);
		return result;
	}
	
	public List<String> findInfoByPmnmModel(String pmnm,String keeper,String model) {
		List<String> result = basedataDAO.findInfoByPmnmModel(pmnm, keeper, model);
		return result;
	}
	/**
	 * 查询所有品名内码、产品名称
	 * 单元名称暂时未查
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public Map<String,List<String>> findAllPmnm() {
		Map<String,List<String>> result =basedataDAO.findAllPmnm();
		return result;
	}
	
	/**
	 * 查询所有品名内码、产品名称
	 * 军代室及以上按照企业查询
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public Map<String,List<String>> findAllPmnmAndKeeper(String keeper) {
		Map<String,List<String>> result =basedataDAO.findAllPmnmAndKeeper(keeper);
		return result;
	}
	
	/**
	 * 通过pmnm查询所有单元
	 * @param pmnm
	 * @return
	 */
	public ArrayList<Unit> findAllUnitByPmnm(String pmnm) {
		ArrayList<Unit> units = basedataDAO.findAllUnitByPmnm(pmnm);
		return units;
	}
	/**
	 * 编辑一条Unit记录
	 * */
	/*public boolean updateUnit(Unit unit){
		boolean flag=false;
		flag=unitDAO.updateUnit(unit);
		return flag;
	}*/
	
	/**
	 * 删除一条Unit记录
	 * */
	public boolean deleteUnit(int UnitId){
		boolean flag=false;
		flag=unitDAO.deleteUnit(UnitId);
		return flag;
	}
	/**
	 * 加入Unit新记录
	 * */
	public boolean addUnit(List<String> pmnmlist){
		boolean flag=false;
		flag=unitDAO.addUnit(pmnmlist);
		return flag;
	}
	/**
	 * 查询Unit中所有已添加的内容
	 * */
	public ArrayList<HashMap<String,String>> getAlladdedUnit(String PMNM){
		ArrayList<HashMap<String,String>> T=new ArrayList<HashMap<String,String>>();
		T=unitDAO.getAlladdedUnit(PMNM);
		return T;
	}
	/**
	 * 查询Unit中所有未添加的内容
	 * */
	public ArrayList<HashMap<String,String>> getNotaddedUnit(String PMNM){
		ArrayList<HashMap<String,String>> T=new ArrayList<HashMap<String,String>>();
		T=unitDAO.getNotaddedUnit(PMNM);
		return T;
	}
	/**
	 * 查找basedData表和Unit表中的所有数据，包括表的标题
	 * @return
	 */
	public Map<String,Object> findAllBasedDataAndUnit(){
		return basedataDAO.findAllBasedataAndUnit();
	}
	
	/**
	 * 向baseData表和unit表中插入数值
	 * @param baseddataDyadic 存储basedata表的数据的二维数组
	 * @param unitDyadic 存储unit表的数据的二维数组
	 * @return
	 */
	public boolean insertBasedAndUnit(List<ArrayList<String>> baseddataDyadic, List<ArrayList<String>> unitDyadic){
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("qy_basedata");
		tableNames.add("qy_unit");
		return commonShareDao.insertThreeTables(baseddataDyadic, unitDyadic, null, tableNames);
	}
}