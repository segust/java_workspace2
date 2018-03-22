package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.service.transact_business.ContractHandleService;

public class TestContract {
	public static void main(String[] args) {
		TestContract test = new TestContract();
		test.delete();
	}
	
	public void delete() {
		String contractId = "都上升到问问";
		ContractHandleService service = new ContractHandleService();
		boolean flag = service.DeleteContract(contractId);
		System.out.println(flag);
	}
	
	public void query() {
		ContractHandleService service = new ContractHandleService();
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("qy_contract.contractId", "都上升到问问");
		//condition.put("productName", "2");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
	//	result = service.queryContractAndProducts(condition);
		if(result.size() >0) {
			Contract contract = (Contract)result.get(0).get("contract");
			System.out.println(contract);
			System.out.println("该合同下有以下产品:");
		}else {
			//则单独按合同Id查询
			System.out.println("没有产品");
			Contract contract = service.queryContractById(condition.get("qy_contract.contractId"));
			System.out.println(contract);
		}
		for(int i = 0;i<result.size();i++) {
			for(String key: result.get(i).keySet()) {
				if("product".equals(key)) {
					Product product = (Product)result.get(i).get(key);
					System.out.println(product);
				}
			}
		}
	}
	
	public void update() {
		Contract contract = new Contract();
		contract.setContractId("gdygd376754");
		contract.setBuyer("hh");
		contract.setAttachment("hh");
		contract.setContractPrice(23.2);
		contract.setJDS("hh");
		contract.setSignDate(new Date());
		contract.setTotalNumber(7286);
		ContractHandleService service = new ContractHandleService();
		boolean flag = service.UpdateContract(contract);
		System.out.println(flag);
	}
}