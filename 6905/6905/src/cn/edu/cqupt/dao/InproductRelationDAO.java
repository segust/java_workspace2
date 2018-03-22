package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.MyDateFormat;

public class InproductRelationDAO {
	/**
	 * 对入库申请-产品关联表的数据库相关操作
	 * @author  limengxin
	 */

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	public InproductRelationDAO() {

	}
    public boolean saveInproductRelation(long productId,long inId){
    	boolean flag = false;
    	String sql = "Insert into qy_inproductrelation (productId,inId) values (?,?)";
    	try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, productId);
			pstmt.setLong(2, inId);
			pstmt.execute();
			flag =true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
    }
    
    /**
	 * 存入数据库
	 * @param dyadic
	 * 			这个二位数组的第一行应该对应数据库的标题行
	 * @return
	 */
    public boolean saveInproductRelations(List<ArrayList<String>> dyadic){
    	if(dyadic == null)return false;
		
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		
		//组合sql语句
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("Insert Into qy_inproductrelation Values(");
		int columnLen = dyadic.get(0).size();
		for(int i = 0; i < columnLen-1; i++){
			sqlBuilder.append("?,");
		}
		//单独处理最后一个
		sqlBuilder.append("?)On DUPLICATE KEY UPDATE ");
		String tempString = null;
		ArrayList<String> tempArray = dyadic.get(0);
		for(int i = 0; i < columnLen-1; i++){
			tempString = tempArray.get(i);
			sqlBuilder.append(tempString+"=VALUES("+tempString+"),");
		}
		//单独处理最后一个
		String lastString = tempArray.get(columnLen-1);
		sqlBuilder.append(lastString+"=VALUES("+lastString+")");
		
		try {
			conn = DBConnection.getConn();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sqlBuilder.toString());
			
			int len = dyadic.size();
			for(int i = 1; i < len; i++){
				ArrayList<String> dataList = dyadic.get(i);
				int index = 1;
				for(int k = 0; k < columnLen; k++){
					String tempStr = dataList.get(k);
					if("".equals(tempStr)){
						tempStr = null;
					}
					ps.setString(index++, tempStr);
				}
				ps.execute();
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				flag = false;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, ps);
		}
		return flag;
    }

    public boolean saveInproductRelation(long productId,long inId, String ownedUnit, java.util.Date insertTime, String deviceNo){
    	boolean flag = false;
    	String sql = "Insert into qy_inproductrelation (inId, productId, ownedUnit, insertTime, deviceNo) values (?,?,?,?,?)";
    	try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, inId);
			pstmt.setLong(2, productId);
			pstmt.setString(3, ownedUnit);
			pstmt.setTimestamp(4, MyDateFormat.changeToSqlDate(insertTime));
			pstmt.setString(5, deviceNo);
			pstmt.execute();
			flag =true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, this.pstmt);
		}
		return flag;
    }
}
