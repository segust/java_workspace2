package cn.edu.cqupt.service.sys_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.dao.OutDataDAO;
import cn.edu.cqupt.service.query_business.ApplyFormOperation;

public class OutDataService {

	/**
	 * 获取所需表的内容
	 * @param absolutePath 文件路径
	 * @param version 当前版本
	 * @param ownedUnit 所属单位
	 * @param pageList 数据表
	 * */
	@SuppressWarnings("unchecked")
	public void exportData(String absolutePath,String version,String ownedUnit,List<ArrayList<String>> pageList){
		OutDataDAO outDataDAO=new OutDataDAO();
		
		List<ArrayList<String>> logList=outDataDAO.selectLog(version);
		List<ArrayList<String>> productList=outDataDAO.selectProduct(version);
		List<ArrayList<String>> accountList=outDataDAO.selectAccount(version);
		List<ArrayList<String>> contractList=outDataDAO.selectContract(version);
		List<ArrayList<String>> equipmentDetailList=outDataDAO.selectEquipmentDetail(version);
		List<ArrayList<String>> inProductRelationList=outDataDAO.selectInProductRelation(version);
		List<ArrayList<String>> outProductRelationList=outDataDAO.selectOutProductRelation(version);
		List<ArrayList<String>> inapplyList=outDataDAO.selectInApply(version);
		List<ArrayList<String>> outapplyList=outDataDAO.selectOutApply(version);
		
		ApplyFormOperation applyFormOperation = new ApplyFormOperation();
		applyFormOperation.exportForm(absolutePath,version,ownedUnit,pageList,logList,
									  productList,accountList,contractList,
									  equipmentDetailList,inProductRelationList,outProductRelationList,
									  inapplyList,outapplyList);
	}
	
	/**
	 * 导入数据到内存
	 * @param absolutePath 文件路径 例"C://Users//Administrator//Desktop//设备汇总查询导出表20150722.xls"
	 * @param sheetNum 导入的sheet数,不能大于总数.正常情况下等于8
	 * @param apply 导入的申请表名字 inapply 或 outapply
	 * */
	public HashMap<String, Object> importData(String absolutePath,int sheetNum,String apply){
		ApplyFormOperation applyFormOperation = new ApplyFormOperation();
		java.util.Map<Integer, List<ArrayList<String>>> map = applyFormOperation.importAllSheetFromExcel(absolutePath, sheetNum);
		
		OutDataDAO outDataDAO=new OutDataDAO();
		boolean flag1=outDataDAO.addLog(map.get(1));
		boolean flag2=outDataDAO.addProduct(map.get(2));
		boolean flag3=outDataDAO.addAccount(map.get(3));
		boolean flag4=outDataDAO.addContract(map.get(4));
		boolean flag5=outDataDAO.addEquipmentDetail(map.get(5));
		boolean flag6=outDataDAO.addInProductRelation(map.get(6));
		boolean flag7=outDataDAO.addOutProductRelation(map.get(7));
		outDataDAO.addInApply(map.get(8));
		outDataDAO.addOutApply(map.get(9));
		boolean flag8=false;
		boolean flag9=false;
		if(apply.equals("inapply")){
			flag8=outDataDAO.addInApply(map.get(0));
		}else if(apply.equals("outapply")){
			flag9=outDataDAO.addOutApply(map.get(0));
		}
		
		for (int i = 0; i < map.size(); i++) {
			List<ArrayList<String>> d =  map.get(i);
			System.out.println("sheet"+i);
			System.out.println("length:"+d.size());
			for(ArrayList<String> l:d){
				for(String s:l){
					System.out.print(s+ "  ");
				}
				System.out.println();
				System.out.println("________________");
			}
		}
		
		HashMap<String, Object> result=new HashMap<String, Object>();
		List<ArrayList<String>> list=map.get(0);
		boolean flag=false;
		boolean sign=false;
		if(flag1&&flag2&&flag3&&flag4&&flag5&&flag6&&flag7){
			sign=true;
		}
		if((sign&&flag8)||(sign&&flag9)){
			flag=true;
		}
		
		result.put("flag", flag);
		result.put("applyList", list);
		return result;
	}
	
	public static void main(String[] args) {
		OutDataService outDataService=new OutDataService();
//		outDataService.exportData("6905");
		outDataService.importData("C://Users//Administrator//Desktop//设备汇总查询导出表20150723.xls",8,"");
	}
}