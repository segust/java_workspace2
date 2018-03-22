package util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCUtil {

	public static void handleParamsForPreparedStatement(PreparedStatement ps, Object[] params) {
		if(params!=null){
			for(int i=0;i<params.length;i++){
				try {
					ps.setObject(i+1, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
