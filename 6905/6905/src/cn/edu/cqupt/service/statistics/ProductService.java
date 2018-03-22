package cn.edu.cqupt.service.statistics;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.dao.ProductDAO;
import cn.edu.cqupt.db.DBConnection;

public class ProductService {
	private Connection conn = null;
	private ProductDAO productDAO = null;

	public ProductService() throws SQLException {
		this.conn = DBConnection.getConn();
		productDAO = new ProductDAO(this.conn);
	}

	public List<Map<String, String>> productSearchStatistic(
			Map<String, String> parameter) {
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		try {
			T = productDAO.productQueryStatistic(parameter);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return T;
	}

	public List<Map<String, String>> productHandleDetail(String productModel,String productUnit) {
		List<Map<String, String>> T = new ArrayList<Map<String, String>>();
		try {
			T = productDAO.productOperationDetail(productModel,productUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return T;
	}

	public int getProductQueryStatisticSum(Map<String, String> parameter){
		int productSum=0;
		try {
			productSum = productDAO.getProductQueryStatisticSum(parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productSum;
	}
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		// map.put("productModel","AK-47");
		// map.put("productUnit","tao");
		// map.put("productType", "gun");
		// map.put("manufacturer", "huawei");
		// map.put("keeper", "hua");
		List<Map<String, String>> list;
		try {
			list = new ProductService().productSearchStatistic(map);
			for (Map<String, String> ss : list) {
				for (String s : ss.keySet())
					System.out.println(s + "....." + ss.get(s));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
