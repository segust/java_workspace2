package test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.OutApply;
import cn.edu.cqupt.dao.OutApplyDAO;
import cn.edu.cqupt.service.query_business.ApplyFormOperation;
import cn.edu.cqupt.service.query_business.ProductCollectiveService;
import cn.edu.cqupt.service.transact_business.ApplyHandleService;
import cn.edu.cqupt.service.transact_business.ProductHandleService;
import cn.edu.cqupt.util.MyDateFormat;
import net.sf.json.JSONObject;

public class FinleyTest {

	public static void main(String args[]) throws IOException, SQLException {

		ProductCollectiveService ser = new ProductCollectiveService();
		
		/**
		 *  productModel,
	 * productUnit,measureUnit,
	 * productPrice,manufacturer,ownedUnit
		 */
		JSONObject jo = new JSONObject();
		jo.element("productModel", "AK-1");
		jo.element("productUnit", "产品名称1");
		jo.element("measureUnit", "辆");
		jo.element("productPrice", "123.0");
		jo.element("manufacturer", "承制单位");
		jo.element("ownedUnit", "代储企业1");
		
		System.out.println(ser.delectCollective(jo));
	}
}
