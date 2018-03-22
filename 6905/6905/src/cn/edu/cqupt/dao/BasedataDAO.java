package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.beans.Basedata;
import cn.edu.cqupt.beans.Unit;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.StringUtil;

public class BasedataDAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	public BasedataDAO(Connection conn){
		this.conn=conn;
	}
	public BasedataDAO()
	{}
	
	public ArrayList<Basedata> FirstSelect()
	{	
		ArrayList<Basedata> u=new ArrayList<Basedata>();
		try {	
			this.conn = DBConnection.getConn();
		String sql="select * from qy_basedata " ;
		this.pstmt=conn.prepareStatement(sql);
		ResultSet rs=this.pstmt.executeQuery();
		conn = DBConnection.getConn();
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while(rs.next())
		{
			Basedata b=new Basedata();
			b.setId(rs.getInt("id"));
			b.setPMNM(rs.getString("PMNM"));
			b.setPMBM(rs.getString("PMBM"));
			b.setQCBM(rs.getString("QCBM"));
			b.setPMCS(rs.getString("PMCS"));
			b.setXHTH(rs.getString("XHTH"));
			b.setXLDJ(rs.getString("XLDJ"));
			b.setXHDE(rs.getString("XHDE"));
			b.setJLDW(rs.getString("JLDW"));
			b.setMJYL(rs.getString("MjYL"));
			b.setQCXS(rs.getString("QCXS"));
			b.setBZZL(rs.getString("BZZL"));
			b.setBZJS(rs.getString("BZJS"));
			b.setBZTJ(rs.getString("BZTJ"));
			b.setCKDJ(rs.getString("CKDJ"));
			b.setSCCJNM(rs.getString("SCCJNM"));
			b.setGHDWNM(rs.getString("GHDWNM"));
			b.setZBSX(rs.getString("ZBSX"));
			b.setLBQF(rs.getString("LBQF"));
			b.setZBBDSJ(rs.getString("ZBBDSJ"));
			b.setSYBZ(rs.getString("SYBZ"));
			b.setYJDBZ(rs.getString("YJDBZ"));
			b.setSCBZ(rs.getString("SCBZ"));
			u.add(b);
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return u;
	}
	
	public Basedata GetBasedataById(String id)
{
   Basedata b=new Basedata();
   String sql="select * from qy_basedata where id = ?";
   try{
	   conn=DBConnection.getConn();
	   this.pstmt=conn.prepareStatement(sql);
     this.pstmt.setInt(1,Integer.parseInt(id));
	 rs=this.pstmt.executeQuery();
	/*conn = DBConnection.getConn();
	pstmt = conn.prepareStatement(sql);*/
	
	while(rs.next())
	{	
		b.setId(rs.getInt("id"));
		b.setPMNM(rs.getString("PMNM"));
		b.setPMBM(rs.getString("PMBM"));
		b.setQCBM(rs.getString("QCBM"));
		b.setPMCS(rs.getString("PMCS"));
		b.setXHTH(rs.getString("XHTH"));
		b.setXLDJ(rs.getString("XLDJ"));
		b.setXHDE(rs.getString("XHDE"));
		b.setJLDW(rs.getString("JLDW"));
		b.setMJYL(rs.getString("MjYL"));
		b.setQCXS(rs.getString("QCXS"));
		b.setBZZL(rs.getString("BZZL"));
		b.setBZJS(rs.getString("BZJS"));
		b.setBZTJ(rs.getString("BZTJ"));
		b.setCKDJ(rs.getString("CKDJ"));
		b.setSCCJNM(rs.getString("SCCJNM"));
		b.setGHDWNM(rs.getString("GHDWNM"));
		b.setZBSX(rs.getString("ZBSX"));
		b.setLBQF(rs.getString("LBQF"));
		b.setZBBDSJ(rs.getString("ZBBDSJ"));
		b.setSYBZ(rs.getString("SYBZ"));
		b.setYJDBZ(rs.getString("YJDBZ"));
		b.setSCBZ(rs.getString("SCBZ"));
		b.setSCDXNF(rs.getString("SCDXNF"));
	   
   }
   }
	catch(Exception e)
	{
		e.printStackTrace();
	}finally{
		DBConnection.close(conn, pstmt, rs);
	}
   
   
   return b;

}

	/**
	 * 批量存储9831
	 * */
	public boolean SavaOfBaseData( ArrayList<Basedata> dyadicArray)
	{  boolean flag=false;
		for(int i=0;i<dyadicArray.size();i++){
		Basedata u=dyadicArray.get(i);
		String sql="insert into qy_basedata VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,null) ";
		try{
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);  
//			 this.pstmt.setLong(1,u.getId());
			 this.pstmt.setString(1,u.getPMNM());
			 this.pstmt.setString(2, u.getPMBM());
			 this.pstmt.setString(3, u.getQCBM());
			 this.pstmt.setString(4, u.getPMCS());
			 this.pstmt.setString(5, u.getXHTH());
			 this.pstmt.setString(6, u.getXLDJ());
			 this.pstmt.setString(7, u.getXHDE());
			 this.pstmt.setString(8, u.getJLDW());
			 this.pstmt.setString(9, u.getMJYL());
			 this.pstmt.setString(10,u.getQCXS());
			 this.pstmt.setString(11,u.getBZZL());
			 this.pstmt.setString(12,u.getBZJS());
			 this.pstmt.setString(13, u.getBZTJ());
			 this.pstmt.setString(14, u.getCKDJ());
			 this.pstmt.setString(15, u.getSCCJNM());
			 this.pstmt.setString(16, u.getGHDWNM());
			 this.pstmt.setString(17, u.getZBSX());
			 this.pstmt.setString(18, u.getLBQF());
			 this.pstmt.setString(19, u.getZBBDSJ());
			 this.pstmt.setString(20, u.getSYBZ());
			 this.pstmt.setString(21, u.getYJDBZ());
			 this.pstmt.setString(22, u.getSCBZ());
			 this.pstmt.setString(23, u.getSCDXNF());
			 System.out.println(sql);
		 int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		}
		return flag;
	}
	public boolean UpdateOfBasedata(Basedata u)
	{
		boolean flag=false;
		String sql="UPDATE qy_basedata SET PMNM=?,PMBM=?,QCBM=?,PMCS=?,XHTH=?,XLDJ=?,XHDE=?,JLDW=?,MJYL=?,QCXS=?,BZZL=? ,BZJS=?,BZTJ=?,CKDJ=?,SCCJNM=?,GHDWNM=?,ZBSX=?,LBQF=?, ZBBDSJ=?,SYBZ=?,YJDBZ=?,SCBZ=?,SCDXNF=? WHERE id=?";
		try{
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql);  
		
		 //this.pstmt.setInt(1,u.getId());
		 this.pstmt.setString(1,u.getPMNM());
		 this.pstmt.setString(2, u.getPMBM());
		 this.pstmt.setString(3, u.getQCBM());
		 this.pstmt.setString(4, u.getPMCS());
		 this.pstmt.setString(5, u.getXHTH());
		 this.pstmt.setString(6, u.getXLDJ());
		 this.pstmt.setString(7, u.getXHDE());
		 this.pstmt.setString(8, u.getJLDW());
		 this.pstmt.setString(9, u.getMJYL());
		 this.pstmt.setString(10,u.getQCXS());
		 this.pstmt.setString(11,u.getBZZL());
		 this.pstmt.setString(12,u.getBZJS());
		 this.pstmt.setString(13, u.getBZTJ());
		 this.pstmt.setString(14, u.getCKDJ());
		 this.pstmt.setString(15, u.getSCCJNM());
		 this.pstmt.setString(16, u.getGHDWNM());
		 this.pstmt.setString(17, u.getZBSX());
		 this.pstmt.setString(18, u.getLBQF());
		 this.pstmt.setString(19, u.getZBBDSJ());
		 this.pstmt.setString(20, u.getSYBZ());
		 this.pstmt.setString(21, u.getYJDBZ());
		 this.pstmt.setString(22, u.getSCBZ());
		 this.pstmt.setString(23, u.getSCDXNF());
		 this.pstmt.setInt(24, u.getId());
		 System.out.println(sql);
		 int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		
		
		return flag;
	}
	public boolean DelOfbasedata(String id)
	{  boolean flag=false;
		String sql="delete  from qy_basedata where id=?";
		try
		{
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			 int count = pstmt.executeUpdate();
				if (count > 0)
					flag = true;			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		return flag;
	}
	
	public ArrayList<Basedata> SearchOfBasedata(HashMap<String,Object> key, int curPageNum, int pageSize)
	{   
		ArrayList<Basedata> a=new ArrayList<Basedata>();
	    String sql="select * from qy_basedata";
	  
	    int count=0;
	  
	   Object m=key.get("PMNM");
	   Object s=key.get("PMCS");
	   if (m!=null&&m.equals("null")) {
		m="";
	   }
	   if(s!=null&&s.equals("null")){
		   s="";
	   }
//	   System.out.println(m.toString());
//	   System.out.println(s.toString());
	   if(StringUtil.isNotEmpty((String)m)||StringUtil.isNotEmpty((String)s))
	   {	   sql=sql+" where";
	   if(m.toString().equals(""))
		   sql=sql+"";
	   else{	if(count>0)
	    	sql=sql+" and ";
	    	
	    	sql=sql+" PMNM REGEXP '" +key.get("PMNM")+ "'";
	    	count++;}
	   if(s.toString().equals(""))
		   sql=sql+"";
	   else	{if(count>0)
	    	sql=sql+" and ";
	    	
	    	sql=sql+" PMCS REGEXP '" +key.get("PMCS")+ "'";
	    	count++;
	    }
	   }
	   sql += " LIMIT ?,?";
	    System.out.println(sql);
	    try
	    {conn=DBConnection.getConn();
		   this.pstmt=conn.prepareStatement(sql);
	    	pstmt=conn.prepareStatement(sql);
	    	pstmt.setInt(1, (curPageNum - 1) * pageSize);
			pstmt.setInt(2, pageSize);
		ResultSet rs=pstmt.executeQuery();
	   while(rs.next())
	   {
		   Basedata b=new Basedata();
			b.setId(rs.getInt("id"));
			b.setPMNM(rs.getString("PMNM"));
			b.setPMBM(rs.getString("PMBM"));
			b.setQCBM(rs.getString("QCBM"));
			b.setPMCS(rs.getString("PMCS"));
			b.setXHTH(rs.getString("XHTH"));
			b.setXLDJ(rs.getString("XLDJ"));
			b.setXHDE(rs.getString("XHDE"));
			b.setJLDW(rs.getString("JLDW"));
			b.setMJYL(rs.getString("MjYL"));
			b.setQCXS(rs.getString("QCXS"));
			b.setBZZL(rs.getString("BZZL"));
			b.setBZJS(rs.getString("BZJS"));
			b.setBZTJ(rs.getString("BZTJ"));
			b.setCKDJ(rs.getString("CKDJ"));
			b.setSCCJNM(rs.getString("SCCJNM"));
			b.setGHDWNM(rs.getString("GHDWNM"));
			b.setZBSX(rs.getString("ZBSX"));
			b.setLBQF(rs.getString("LBQF"));
			b.setZBBDSJ(rs.getString("ZBBDSJ"));
			b.setSYBZ(rs.getString("SYBZ"));
			b.setYJDBZ(rs.getString("YJDBZ"));
			b.setSCBZ(rs.getString("SCBZ"));
			b.setSCDXNF(rs.getString("SCDXNF"));
			a.add(b); 
	   }
	    }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }finally{
			DBConnection.close(conn, pstmt, rs);
		}
	    
		return a;
		
	}
	
	public int SearchOfBasedataSum(HashMap<String,Object> key )
	{   
		int sum=0;
		String sql="select * from qy_basedata";
		  
	    int count=0;
	  
	   Object m=key.get("PMNM");
	   Object s=key.get("PMCS");
	   if (m!=null&&m.equals("null")) {
		m="";
	   }
	   if(s!=null&&s.equals("null")){
		   s="";
	   }
//	   System.out.println(m.toString());
//	   System.out.println(s.toString());
	   if(StringUtil.isNotEmpty((String)m)||StringUtil.isNotEmpty((String)s))
	   {	   sql=sql+" where";
	   if(m.toString().equals(""))
		   sql=sql+"";
	   else{	if(count>0)
	    	sql=sql+" and ";
	    	
	    	sql=sql+" PMNM REGEXP '" +key.get("PMNM")+ "'";
	    	count++;}
	   if(s.toString().equals(""))
		   sql=sql+"";
	   else	{if(count>0)
	    	sql=sql+" and ";
	    	
	    	sql=sql+" PMCS REGEXP '" +key.get("PMCS")+ "'";
	    	count++;
	    }
	   }
	    System.out.println(sql);
	    try
	    {conn=DBConnection.getConn();
	    	pstmt=conn.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
	   if(rs.next())
	   {
		   sum++;
	   }
	    }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }finally{
			DBConnection.close(conn, pstmt, rs);
		}
	    
		return sum;
		
	}

	public boolean DeleteBaseData(List<Integer> id)
	{  
		boolean flag=false;
		int sum=0;
		for (int i = 0; i < id.size(); i++) {
		String sql="delete from qy_basedata where id=?";
		try
		{
			conn = DBConnection.getConn();
			pstmt=conn.prepareStatement(sql); 
			pstmt.setInt(1, id.get(i));			
			int count=pstmt.executeUpdate();
			sum+=count;
			System.out.println(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			DBConnection.close(conn, pstmt, rs);
		}
		}
		if(sum==id.size())flag=true;
		return flag;
	}
	
	/**
	 * 通过品名内码自动查询基础数据库中信息
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public Map<String,String> findInfoByPmnm(String pmnm) {
		Map<String,String> result = new HashMap<String, String>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_basedata WHERE PMNM=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pmnm);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result.put("price", rs.getString("CKDJ"));
				result.put("measure", rs.getString("JLDW"));
				result.put("model", rs.getString("XHTH"));
				result.put("code", rs.getString("QCBM"));
			}
			//如果没有在基础数据库中查到整机信息
			//则在单元表中查询信息
			if(result.size() == 0) {
				String unit_sql = "SELECT * FROM qy_unit WHERE PMNM=?";
				pstmt = conn.prepareStatement(unit_sql);
				pstmt.setString(1, pmnm);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					result.put("price", rs.getString("CKDJ"));
					result.put("measure", rs.getString("JLDW"));
					result.put("model", rs.getString("XHTH"));
					result.put("code", rs.getString("QCBM"));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}
	
	/**
	 * 通过品名内码自动查询基础数据库中信息
	 * 军代室以上查询版本
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public List<String> findInfoByPmnmAndKeeper(String pmnm,String keeper) {
		List<String> result = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = null;
			if("all".equals(keeper)) {
				sql = "SELECT distinct(productModel) FROM qy_product WHERE PMNM=?";
			}else {
				sql = "SELECT distinct(productModel) FROM qy_product WHERE PMNM=? and ownedUnit=?";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pmnm);
			if(!"all".equals(keeper)) {
				pstmt.setString(2, keeper);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String model = rs.getString("productModel");
				result.add(model);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}
	
	/**
	 * 通过品名内码自动查询基础数据库中信息
	 * 军代室以上查询版本
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public List<String> findInfoByPmnmModel(String pmnm,String keeper,String model) {
		List<String> result = new ArrayList<String>();
		try {
			conn = DBConnection.getConn();
			String sql = null;
			if("all".equals(keeper)) {
				sql = "SELECT distinct(measureUnit) FROM qy_product WHERE PMNM=? AND productModel=?";
			}else {
				sql = "SELECT distinct(measureUnit) FROM qy_product WHERE PMNM=? AND productModel=? AND ownedUnit=?";
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pmnm);
			pstmt.setString(2, model);
			if(!"all".equals(keeper)) {
				pstmt.setString(3, keeper);
			}
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String unit = rs.getString("measureUnit");
				result.add(unit);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}
	/**
	 * 查询所有品名内码、产品名称、单元名称
	 * 
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public Map<String,List<String>> findAllPmnm() {
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		try {
			conn = DBConnection.getConn();
			HashSet<String> pmnm = new HashSet<String>();
			List<String> pname = new ArrayList<String>();
			List<String> uname = new ArrayList<String>();
			String sql = "SELECT * FROM qy_basedata WHERE QCBM=0";
			//20151209
			//还需要查询单元名称，单元名称在qy_unit里
			String unit_sql = "SELECT * FROM qy_unit";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				pmnm.add(rs.getString("PMNM"));
				pname.add(rs.getString("PMCS"));
			}
			pstmt = conn.prepareStatement(unit_sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				pmnm.add(rs.getString("PMNM"));
				uname.add(rs.getString("PMCS"));
			}
			List<String> pmnm_list = new ArrayList<String>(pmnm);
			result.put("pmnm", pmnm_list);
			result.put("pname", pname);
			result.put("uname", uname);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}
	
	
	/**
	 * 查询所有品名内码、产品名称
	 *  军代室以上对应企业查询
	 * 	 by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public Map<String,List<String>> findAllPmnmAndKeeper(String keeper) {
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		try {
			conn = DBConnection.getConn();
			List<String> pmnm = new ArrayList<String>();
			List<String> pname = new ArrayList<String>();
			String sql = "SELECT * FROM qy_basedata WHERE ownedUnit=?";
			pstmt.setString(1, keeper);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				pmnm.add(rs.getString("PMNM"));
				pname.add(rs.getString("PMCS"));
			}
			result.put("pmnm", pmnm);
			result.put("pname", pname);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return result;
	}
	/**
	 * 查询所有单元名称
	 * by刘雨恬
	 * 2015.08.20
	 * @return
	 */
	public ArrayList<Unit> findAllUnitByPmnm(String pmnm) {
		ArrayList<Unit> units = new ArrayList<Unit>();
		try {
			conn = DBConnection.getConn();
			String sql = "SELECT * FROM qy_unit WHERE FKPMNM=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pmnm);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Unit unit = new Unit();
				unit.setId(rs.getInt("id"));
				unit.setPMNM(rs.getString("PMNM"));
				unit.setQCBM(rs.getString("QCBM"));
				unit.setCKDJ(rs.getString("CKDJ"));
				unit.setJLDW(rs.getString("JLDW"));
				units.add(unit);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return units;
	}
	
	/**
	 * 查找basedData表和Unit表中的所有数据，包括表的标题
	 * @return key: basedData\ unit
	 * @author LiangYH
	 */
	public Map<String, Object> findAllBasedataAndUnit(){
		PreparedStatement queryBased_ps = null;
		PreparedStatement queryUnit_ps = null;
		ResultSet queryBased_rs = null;
		ResultSet queryUnit_rs = null;
		
		String queryBased_sql = "Select * From qy_basedata";
		String queryUnit_sql = "Select * From qy_unit";
		
		Map<String, Object> map = new HashMap<String, Object>(); 
		
		//用于存储basedData表的所有数据
		ArrayList<ArrayList<String>> basedDataDyadic = new ArrayList<ArrayList<String>>();
		//用于存储unit表中的所有数据
		ArrayList<ArrayList<String>> unitDyadic = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> basedData_headline = new ArrayList<String>();
		ArrayList<String> unit_headline = new ArrayList<String>();
		//填充标题
		basedDataDyadic.add(basedData_headline);
		unitDyadic.add(unit_headline);
		
		try {
			conn = DBConnection.getConn();
			queryBased_ps = conn.prepareStatement(queryBased_sql);
			queryUnit_ps = conn.prepareStatement(queryUnit_sql);
			
			queryBased_rs = queryBased_ps.executeQuery();
			while(queryBased_rs.next()){
				ArrayList<String> basedData = new ArrayList<String>();
				
				//得到结果集(rs)的结构信息，比如字段数、字段名等 
				ResultSetMetaData md = queryBased_rs.getMetaData();  
				int columnCount = md.getColumnCount(); 	
		        for(int k = 1; k <= columnCount; k++){
		        	basedData.add(queryBased_rs.getString(k));
		        	//增加标题
		        	if(basedData_headline.size() != columnCount)
		        		basedData_headline.add(md.getColumnName(k));
		        }
		        basedDataDyadic.add(basedData);
			}//end while
			
			queryUnit_rs = queryUnit_ps.executeQuery();
			while(queryUnit_rs.next()){
				ArrayList<String> unit = new ArrayList<String>();
				
				//得到结果集(rs)的结构信息，比如字段数、字段名等 
				ResultSetMetaData md = queryUnit_rs.getMetaData();  
				int columnCount = md.getColumnCount(); 	
		        for(int k = 1; k <= columnCount; k++){
		        	unit.add(queryUnit_rs.getString(k));
		        	//增加标题
		        	if(unit_headline.size() != columnCount)
		        		unit_headline.add(md.getColumnName(k));
		        }
		        unitDyadic.add(unit);
			}//end while
			
//			queryUnit_rs = queryUnit_ps.executeQuery();
//			while(queryUnit_rs.next()){
//				ArrayList<String> unit = new ArrayList<String>();
//				
//				//得到结果集(rs)的结构信息，比如字段数、字段名等 
//				ResultSetMetaData md = queryUnit_rs.getMetaData();  
//				int columnCount = md.getColumnCount(); 	
//				for(int i = 1; i <= columnCount; i++){
//					unit.add(queryUnit_rs.getString(i));
//					//增加标题
//					if(unit_headline.size() != columnCount){
//						unit_headline.add(md.getCatalogName(i));
//					}
//				}
//				unitDyadic.add(unit);
//			}
			
			map.put("basedData", basedDataDyadic);
			map.put("unit", unitDyadic);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBConnection.close(conn, queryBased_ps, queryBased_rs);
			DBConnection.close(null, queryUnit_ps, queryUnit_rs);
		}
		return map;
	}
}

