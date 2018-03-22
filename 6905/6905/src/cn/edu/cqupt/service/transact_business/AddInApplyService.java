package cn.edu.cqupt.service.transact_business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.beans.Product;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.util.MyDateFormat;

public class AddInApplyService {

	private InApplyDAO inApplyDao = new InApplyDAO();
	private ProductDAO productDao = new ProductDAO();
	private Date now = new Date();
	
	/**
	 * servlet传过来的信息：String的List info，info中的信息要保存到inAplly表中，
	 * productId对应的产品的productCode要修改为用户传过来的productCode
	 * @param info
	 * @return
	 */
	
	public boolean inApplyService(List<String> info)//这里的number表示入库的产品的数量。只有相同的产品的批量入库时number才大于1
	{
		String contractId = info.get(0);
		String productModel = info.get(3);
		String productUnit = info.get(5);
		String myProductId = searchProductIdInApply(contractId, productModel, productUnit);
		if(!changeProductCode(myProductId, info.get(15)))//productId和productCode，通过productId找到一个product，并将它的productCode改为新的
		{
			System.out.println("failed");
			return false;
		}
		
		if(!saveSingleApplyInApply(info))
			return false;
		if(!changeProductProStatus(myProductId))
			return false;
		long inId = getInApplyIdByExecDate(now);
		if(inId == 0)
			return false;
		if(!addInProductRelation(inId, Long.parseLong(myProductId)))
			return false;
		return true;
	}
	
	public boolean inApplyBatchService(List<String> info, int number, List<String> productCodes)//这里的number表示入库的产品的数量。只有相同的产品的批量入库时number才大于1
	{
		String contractId = info.get(0);
		String productModel = info.get(3);
		String productUnit = info.get(5);
		List<String> myProductIds = searchProductIdsInApply(contractId, productModel, productUnit);
		if(myProductIds.size() < number)
			return false;
		for(int i = 0; i < number; i++)
		{
			if(!changeProductCode(myProductIds.get(i), productCodes.get(i)))//productId和productCode，通过productId找到一个product，并将它的productCode改为新的
			{
				System.out.println("failed");
				return false;
			}
			if(!changeProductProStatus(myProductIds.get(i)))
				return false;
		}
			
		
		if(!saveSingleApplyInApply(info))
			return false;

		long inId = getInApplyIdByExecDate(now);
		if(inId == 0)
			return false;
		for(int i = 0; i < number; i++)
		{
			if(!addInProductRelation(inId, Long.parseLong(myProductIds.get(i))))
				return false;
		}
		return true;
	}
	
	//public void deleteOriginalInfoBy
	
	public Product singleApplyQueryProduct(long productId)
	{
		Product product = productDao.selectProductDetail(productId);
		return product;
	}
	
	public boolean changeProductProStatus(String productId)
	{
		return productDao.updateProductProStatuc(productId);
	}
	
	
	private long getInApplyIdByExecDate(Date date)
	{
		return inApplyDao.getIdByExecDate(now);
	}
	
	
	/**
	 * 通过一些条件找出一个可以
	 * @param contractId
	 * @param productModel
	 * @param productUnit
	 * @return
	 */
	private String searchProductIdInApply(String contractId, String productModel, String productUnit)
	{
		List<String> productIds = this.productDao.getProductIdInApply(contractId, productModel, productUnit);
		String myProductId = productIds.get(0);
		System.out.println(myProductId);
		return myProductId;
	}
	
	private List<String> searchProductIdsInApply(String contractId, String productModel, String productUnit)
	{
		List<String> productIds = this.productDao.getProductIdInApply(contractId, productModel, productUnit);
		return productIds;
	}
	
	private boolean changeProductCode(String productId, String productCode)
	{
		boolean flag = false;
		flag = productDao.updateProductCode(productId, productCode);
		return flag;
	}
	
	private boolean saveSingleApplyInApply(List<String> info)
	{
		boolean flag = false;
		this.inApplyDao = new InApplyDAO();
		InApply apply = new InApply();
		apply.setContractId(info.get(0));
		apply.setInMeans(info.get(1));
		apply.setWholeName(info.get(4));
		apply.setUnitName(info.get(5));
		apply.setNewPrice(Double.parseDouble(info.get(7)));
		apply.setNum(Integer.parseInt(info.get(8)));
		apply.setMeasure(info.get(6));
		apply.setManufacturer(info.get(10));
		apply.setKeeper(info.get(11));
		apply.setLocation(info.get(12));
		apply.setMaintainCycle(info.get(13));
		apply.setProducedDate(MyDateFormat.changeStringToDate(info.get(14)));
		apply.setExecDate(now);
		apply.setChStatus("进库待审核");
		apply.setProductCode(info.get(15));
		apply.setPMNM(info.get(9));
		apply.setBatch(info.get(16));
		apply.setDeviceNo(info.get(17));
		apply.setStorageTime(info.get(18));
		apply.setRemark(info.get(19));
		flag = inApplyDao.saveSingleApply(apply);
		return flag;
	}
	
	private boolean addInProductRelation(long inId, long productId)
	{
		return this.inApplyDao.addInProductRelation(inId, productId);
	}
	/*
	 * 这个方法现在没有用了
	 * @param info
	 * @return
	 * 
	 */
	/*
	public boolean saveSingleApplyProduct(List<String> info)
	{
		boolean flag = false;
		this.productDao = new ProductDAO();
		Product p = new Product();
		p.setContractId(info.get(0));
		p.setProductCode(info.get(15));
		p.setProductName(info.get(0));
		p.setProductType(info.get(2));
		p.setProductModel(info.get(1));
		p.setProductUnit(info.get(5));
		p.setMeasureUnit(info.get(6));
		p.setPrice(Double.parseDouble(info.get(7)));
		p.setManufacturer(info.get(10));
		p.setKeeper(info.get(11));
		p.setSignTime(now);
		p.setProStatus("申请入库");
		p.setPMNM(info.get(15));
		flag = productDao.saveProductInApply(p);
		return flag;
	}
	*/
	
	
	
	
	
	
}
