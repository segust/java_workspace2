package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.service.transact_business.AddBatchInApplyService;

public class TestAddBatchDeviceNo {

	public static void main(String[] arsg)
	{
		AddBatchInApplyService service = new AddBatchInApplyService();
		List<HashMap<String, String>> in = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> pi = new HashMap<String, String>();
		pi.put("contractId", "0002");
		pi.put("productName", "美式冲锋枪M-16");
		pi.put("productUnit", "单元名称1");
		pi.put("productModel", "AK-1");
		pi.put("startNo", "huang08huhu");
		pi.put("endNo", "huang11huhu");
		in.add(pi);
		for(int i = 0; i < in.size(); i++)
		{
			HashMap<String, String> one = in.get(i);
			String[] nos = service.getDeviceNoStream(one.get("startNo"), one.get("endNo"));
			for(int j = 0; j < nos.length; j++)
				System.out.println(nos[j]);
			System.out.println(nos.length);
			if(nos == null)
			{
				System.out.println("error");
				return;
			}
			boolean f1 = service.checkDeviceNoAlreadyExist(one.get("productModel"), nos);
			System.out.println(f1);
			boolean f2 = service.storeBatchDeviceNo(in);
			System.out.println(f2);
		}
		
	}
	
}
