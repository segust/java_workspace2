package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;

public class MyDateTest {

	
	public static void main(String[] args)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select signTime from qy_product where signTime is not null";
		try {
			conn=DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				Date d = rs.getDate(1);
				System.out.println(d);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, pstmt, rs);
		}
		
	}
}
