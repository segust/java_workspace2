package cn.edu.cqupt.service.transact_business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.InProductRelation;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.dao.InproductRelationDAO;
import cn.edu.cqupt.dao.ProductDAO;

public class AddBatchService {

	private InApplyDAO inApplyDAO;
	private ProductDAO productDAO = new ProductDAO();
	private InproductRelationDAO inProductRelationDAO;
	private Date now = new Date();
	
	public boolean storeBatch(List<HashMap<String, Object>> in)
	{
		InApply inApply = new InApply();
		inApply.setContractId(in.get(0).get("contractId").toString());
		inApply.setInMeans("新入库");
		inApply.setBatch(in.get(0).get("batch").toString());
		inApply.setNum(in.size());
		inApply.setExecDate(now);
		inApply.setChStatus("进库待审核");
		inApply.setOwnedUnit(in.get(0).get("ownedUnit").toString());
		int inId = (int) storeInApply(inApply);//在inApply表中添加一条新数据
		if(inId == -1)
			return false;
		
		for(int i = 0; i < in.size(); i++)
		{
			HashMap<String, Object> one = in.get(i);
			if(!updateProduct(one.get("deviceNo").toString(), one.get("location").toString(),
					one.get("producedDate").toString(), one.get("storageTime").toString(),
					one.get("remark").toString(), one.get("wholeName").toString(),
					one.get("maintainCycle").toString(), one.get("proStatus").toString(),
					now, one.get("productCode").toString()))//更新产品信息
			{
				System.out.println("产品信息保存失败。");
				return false;
			}
			System.out.println("productModel" + one.get("productModel"));
			if(!stroeInProductRelation(inId, productDAO.queryProductIdByDeviceNoandProuctModel(one.get("deviceNo").toString(), one.get("productModel").toString()),
					one.get("ownedUnit").toString(), now, one.get("deviceNo").toString()))
			{
				System.out.println("inproductrelation保存失败。");
				return false;
			}
				
		}
		return true;
	}
	
	private long storeInApply(InApply inApply)//保存inApply并返回inId
	{
		inApplyDAO = new InApplyDAO();
		return inApplyDAO.saveApplySimple(inApply);
	}
	
	private boolean stroeInProductRelation(int inId, int productId, String ownedUnit, Date insertTime, 
			String deviceNo)//保存inproductRelationship
	{
		this.inProductRelationDAO = new InproductRelationDAO();
		return this.inProductRelationDAO.saveInproductRelation(productId, inId, ownedUnit, insertTime, deviceNo);
	}
	
	private boolean updateProduct(String deviceNo, String location, String producedDate,
			String storageTime, String remark, String wholeName, String maintianCycle, 
			String proStatus, Date latestMaintainTime, String productCode)//更新产品信息
	{
		return this.productDAO.updateProductWhenInApply(deviceNo, location, producedDate, 
				storageTime, remark, wholeName, maintianCycle, proStatus, latestMaintainTime, productCode);
	}
	
}
