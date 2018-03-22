package util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC工具类
 *
 */
public class JDBCUtils {
	 /**
	  * 把参数数组放入PareparedStatement中 
	  * @param ps PreparedStatement
	  * @param params 参数数组
	  */
	public static void handleParamsForPreparedStatement(PreparedStatement ps,Object[] params){
		if(params!=null){
			for(int i=0;i<params.length;i++){
				try 
				{
					ps.setObject(i+1, params[i]);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
