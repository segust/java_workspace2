package cn.edu.cqupt.service.transact_business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.dao.CommonShareDAO;
import cn.edu.cqupt.dao.ContractDAO;

public class ContractHandleService {
	private ContractDAO contractDAO = null;
	private CommonShareDAO commonShareDao = null;
	
	public ContractHandleService() {
		contractDAO = new ContractDAO();
		commonShareDao = new CommonShareDAO();
	}
	/**
	 * 新增合同
	 * @param contract
	 * @return
	 */
	public boolean saveContract(Contract contract) {
		boolean flag = false;
		flag = contractDAO.saveContract(contract);
		return flag;
	}
   
	/**
	 * 按合同号，型号/单元，产品名称，签订日期查询合同
	 * @param condition
	 * @return
	 */
	public List<Contract> queryContract(Map<String,String> condition,int curPageNum,int pageSize) {
		List<Contract> result = new ArrayList<Contract>();
		result = contractDAO.queryContract(condition,curPageNum,pageSize);
		return result;
	}
	
	/**
	 * 按合同编号查询合同
	 * @param contactId
	 * @return
	 */
	public Contract queryContractById(String contractId) {
		Contract contract = new Contract();
		contract = contractDAO.queryContractById(contractId);
		return contract;
	}
	
	/**
	 * 更新合同
	 * @param contract
	 * @return
	 */
	public boolean UpdateContract(Contract contract) {
		boolean flag = false;
		flag = contractDAO.UpdateContract(contract);
		return flag;
	}
	
	/**
	 * 删除合同并删除对应产品
	 * @param contractId
	 * @return
	 */
	public boolean DeleteContract(String contractId) {
		boolean flag = true;
		flag = contractDAO.DeleteContract(contractId);
		return flag;
	}
	
	/**
	 * 根据合同id找附件路径
	 * @param contractId
	 * @return
	 */
	public String findAttachByContractId(String contractId) {
		String path ="";
		path = contractDAO.findAttachByContractId(contractId);
		return path;
	}
	
	/**
	 * 删除附件
	 * @param contractId
	 * @return
	 */
	public boolean deleteAttah(String contractId) {
		boolean flag = false;
		flag = contractDAO.deleteAttah(contractId,null);
		return flag;
	}
	
	/**
	 * 
	 * @param contractId
	 * @param attach
	 * @return
	 */
	public boolean uploadAttach(String contractId,String attach) {
		boolean flag = false;
		flag = contractDAO.deleteAttah(contractId, attach);
		return flag;
	}
	
	/**
	 * 按条件计算总数
	 * @param condition
	 * @return
	 */
	public int querySum(Map<String,String> condition) {
		int count = 0;
		count = contractDAO.querySum(condition);
		return count;
	}
	
	/**
	 * 计算合同总金额
	 * @param condition
	 * @param curPageNum
	 * @param pageSize
	 * @return
	 */
	public double getContractPriceSum(Map<String, String> condition){
		double contractPriceSum=0.0;
		contractPriceSum=contractDAO.getContractPriceSum(condition);
		return contractPriceSum;
	}
	
	/**
	 * 根据产品id查找相应的合同
	 * @param contractIDs 产品二维数组
	 * @return
	 */
	public List<ArrayList<String>> queryContracts(List<ArrayList<String>> productDyadic){
		int rowLen = productDyadic.size();
		int colLen = productDyadic.get(0).size();
		
		//找到productId所在的列
		int productIDIndex = -1; 
		for(int i = 0; i < colLen; i++){
			if("productId".equals(productDyadic.get(0).get(i)))
				productIDIndex = i;
		}
		//提取productId
		List<String> productIDs = new ArrayList<String>();
		for(int i = 1; i < rowLen; i++){
			productIDs.add(productDyadic.get(i).get(productIDIndex));
		}
		//查询
		return contractDAO.queryContracts(productIDs);
	}
	
	/**
	 * 插入合同
	 * @param contracts 合同二维数组，第一行为标题
	 * @return
	 */
	public boolean saveContracts(List<ArrayList<String>> contracts){
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("qy_contract");
		return commonShareDao.insertThreeTables(contracts, null, null, tableNames);
	}
	
	/**
	 * 查看合同号是否重复
	 */
	public boolean isExistCid(String contractId) {
		return contractDAO.isExistCid(contractId);
	}
}
