package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import cn.edu.cqupt.service.transact_business.AddBatchService;

public class AddBatchApplyTest {

	
	public static void main(String[] args)
	{
		AddBatchService addBatchService = new AddBatchService();
		List<HashMap<String, Object>> in = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> one = new HashMap<String, Object>();		
		one.put("contractId", 10012);
		one.put("inMeans", "新入库");
		one.put("productName", "美式冲锋枪M-16");
		one.put("productModel", "M-16");
		one.put("batch", "1");
		one.put("deviceNo", "100");
		one.put("wholeName", "美式冲锋枪M-16");
		one.put("productUnit", "整枪");
		one.put("measure", "把");
		one.put("productPrice", 100.0);
		one.put("num", 1);
		one.put("PMNM", "X!!");
		one.put("manufacturer", "6905厂");
		one.put("keeper", "cqput");
		one.put("location", "A区");
		one.put("maintainCycle", "3月");
		one.put("producedDate", "2015-8-18 14:22:15");
		one.put("productCode", "huangkai001");
		one.put("storageTime", "1年");
		one.put("remark", "这支枪非常好");
		one.put("ownedUnit", "6905厂（从session里面获得的）");
		one.put("proStatus", "进库待审核");
		
		HashMap<String, Object> two = new HashMap<String, Object>();
		two.put("contractId", 10012);
		two.put("inMeans", "新入库");
		two.put("productName", "歼击十五型舰载战斗机");
		two.put("productModel", "AK-1");
		two.put("batch", "1");
		two.put("deviceNo", "机号008");
		two.put("wholeName", "歼击十五型舰载战斗机");
		two.put("productUnit", "整机");
		two.put("measure", "架");
		two.put("productPrice", 10000.0);
		two.put("num", 1);
		two.put("PMNM", "X!!1");
		two.put("manufacturer", "6905厂");
		two.put("keeper", "cqput");
		two.put("location", "B区");
		two.put("maintainCycle", "3年");
		two.put("producedDate", "2015-8-19 14:22:15");
		two.put("productCode", "huangkai002");
		two.put("storageTime", "10年");
		two.put("remark", "这架飞机非常好");
		two.put("ownedUnit", "6905厂（从session里面获得的）");
		two.put("proStatus", "进库待审核");
		
		HashMap<String, Object> three = new HashMap<String, Object>();
		three.put("contractId", 10012);
		three.put("inMeans", "新入库");
		three.put("productName", "美式冲锋枪M-16");
		three.put("productModel", "AK-1");
		three.put("batch", "1");
		three.put("deviceNo", "机号999");
		three.put("wholeName", "美式冲锋枪M-16");
		three.put("productUnit", "整枪");
		three.put("measure", "把");
		three.put("productPrice", 1000.0);
		three.put("num", 1);
		three.put("PMNM", "X!!2");
		three.put("manufacturer", "6905厂");
		three.put("keeper", "cqput");
		three.put("location", "c区");
		three.put("maintainCycle", "1年");
		three.put("producedDate", "2015-8-20 14:22:15");
		three.put("productCode", "huangkai003");
		three.put("storageTime", "1年");
		three.put("remark", "这把M-16非常好");
		three.put("ownedUnit", "6905厂（从session里面获得的）");
		three.put("proStatus", "进库待审核");
		
		
		HashMap<String, Object> four = new HashMap<String, Object>();
		four.put("contractId", 10012);
		four.put("inMeans", "新入库");
		four.put("productName", "美式冲锋枪M-16");
		four.put("productModel", "AK-1");
		four.put("batch", "1");
		four.put("deviceNo", "102");
		four.put("wholeName", "美式冲锋枪M-16");
		four.put("productUnit", "整枪");
		four.put("measure", "把");
		four.put("productPrice", 1000.0);
		four.put("num", 1);
		four.put("PMNM", "X!!3");
		four.put("manufacturer", "6905厂");
		four.put("keeper", "cqput");
		four.put("location", "D区");
		four.put("maintainCycle", "2年");
		four.put("producedDate", "2015-8-21 14:22:15");
		four.put("productCode", "huangkai004");
		four.put("storageTime", "10年");
		four.put("remark", "这把M-16非常好");
		four.put("ownedUnit", "6905厂（从session里面获得的）");
		four.put("proStatus", "进库待审核");
		
		in.add(one);
		in.add(two);
		in.add(three);
		in.add(four);
		
		boolean flag = addBatchService.storeBatch(in);
		System.out.println(flag);
	}
}
