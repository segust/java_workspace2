package test;

import java.util.Date;
import java.util.HashMap;

import cn.edu.cqupt.beans.Account;
import cn.edu.cqupt.dao.AccountDAO;
import cn.edu.cqupt.dao.LogDAO;
import cn.edu.cqupt.log.ProductLogService;
/*import org.gjt.mm.mysql.Driver;
import com.mysql.jdbc.Driver;
*/

public class TestLog {
	public static void main(String[] args) {
		//qy_log log = new qy_log();
		Account log = new Account();
		log.setProductId(16252434);
//		log.setInspectPerson("军代室");
//		log.setMainTainType("企业维护");
		log.setOperateTime(new Date());
		log.setOperateType("维护");
		log.setUserName("萌萌");
		boolean flag = false;
		try {
			//flag = LogDAO.saveMainTainLog(log);
			flag = ProductLogService.saveAccount(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(flag) {
			System.out.println("插入成功");
		}else {
			System.out.println("插入失败");
		}
	}
}
