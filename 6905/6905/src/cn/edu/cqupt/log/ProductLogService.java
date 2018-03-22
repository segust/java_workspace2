package cn.edu.cqupt.log;


import cn.edu.cqupt.beans.Account;
import cn.edu.cqupt.dao.AccountDAO;

public class ProductLogService {
	private  static AccountDAO accountDAO = null;
	
	static {
		accountDAO = new AccountDAO();
	}
	/**
	 * 
	 * @param log 
	 * @return
	 * @throws Exception
	 * 增加一条产品操作明细记录
	 */
	public static boolean saveAccount(Account account) {
		boolean flag = false;
			flag = accountDAO.saveAccount(account);
		return flag;
	}
	
}
