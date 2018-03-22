package cn.edu.cqupt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.beans.Common9831;
import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.StringUtil;

public class Common9831DAO {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public Common9831DAO() {

	}

	public Common9831DAO(Connection conn) {
		this.conn = conn;
	}

	public ArrayList<Common9831> FirstSelect() {
		ArrayList<Common9831> u = new ArrayList<Common9831>();
		try {
			this.conn = DBConnection.getConn();
			String sql = "select * from qy_9831 ";
			this.pstmt = conn.prepareStatement(sql);
			ResultSet rs = this.pstmt.executeQuery();
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Common9831 b = new Common9831();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return u;
	}

	/**
	 * 根据id查询
	 * */
	public Common9831 Get9831ById(String id) {
		Common9831 b = new Common9831();
		String sql = "select * from qy_9831 where id = ?";
		try {
			conn = DBConnection.getConn();
			this.pstmt = conn.prepareStatement(sql);
			this.pstmt.setString(1, id);
			this.rs = this.pstmt.executeQuery();
			/*
			 * conn = DBConnection.getConn(); pstmt =
			 * conn.prepareStatement(sql);
			 */

			while (rs.next()) {

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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return b;

	}

	/**
	 * 批量存储9831
	 * */
	public boolean SavaOf9831(ArrayList<Common9831> dyadicArray) {
		boolean flag = false;
		for (int i = 0; i < dyadicArray.size(); i++) {
			Common9831 u = dyadicArray.get(i);
			String sql = "insert into qy_9831 (PMNM,PMBM,QCBM,PMCS,XHTH,XLDJ,XHDE,JLDW,MJYL,QCXS,BZZL,BZJS,BZTJ,CKDJ,SCCJNM,GHDWNM,ZBSX,LBQF,ZBBDSJ,SYBZ,YJDBZ,SCBZ,SCDXNF,ownedUnit) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,null) ";
			try {
				conn = DBConnection.getConn();
				pstmt = conn.prepareStatement(sql);
				// this.pstmt.setLong(1,u.getId());
				this.pstmt.setString(1, u.getPMNM());
				this.pstmt.setString(2, u.getPMBM());
				this.pstmt.setString(3, u.getQCBM());
				this.pstmt.setString(4, u.getPMCS());
				this.pstmt.setString(5, u.getXHTH());
				this.pstmt.setString(6, u.getXLDJ());
				this.pstmt.setString(7, u.getXHDE());
				this.pstmt.setString(8, u.getJLDW());
				this.pstmt.setString(9, u.getMJYL());
				this.pstmt.setString(10, u.getQCXS());
				this.pstmt.setString(11, u.getBZZL());
				this.pstmt.setString(12, u.getBZJS());
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
				//System.out.println(sql);
				int count = pstmt.executeUpdate();
				if (count > 0)
					flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBConnection.close(conn, pstmt, rs);
			}
		}
		return flag;
	}
	
	/**
	 * 批量存储单元表，9831库的子类
	 * */
	public boolean SavaOfUnit(ArrayList<Common9831> dyadicArray, String FKPMNM) {
		boolean flag = false;
		for (int i = 0; i < dyadicArray.size(); i++) {
			Common9831 u = dyadicArray.get(i);
			String sql = "insert into qy_unit (PMNM,PMBM,QCBM,PMCS,XHTH,XLDJ,XHDE,JLDW,MJYL,QCXS,BZZL,BZJS,BZTJ,CKDJ,SCCJNM,GHDWNM,ZBSX,LBQF,ZBBDSJ,SYBZ,YJDBZ,SCBZ,SCDXNF,FKPMNM,ownedUnit) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,null) ";
			try {
				conn = DBConnection.getConn();
				pstmt = conn.prepareStatement(sql);
				this.pstmt.setString(1, u.getPMNM());
				this.pstmt.setString(2, u.getPMBM());
				this.pstmt.setString(3, u.getQCBM());
				this.pstmt.setString(4, u.getPMCS());
				this.pstmt.setString(5, u.getXHTH());
				this.pstmt.setString(6, u.getXLDJ());
				this.pstmt.setString(7, u.getXHDE());
				this.pstmt.setString(8, u.getJLDW());
				this.pstmt.setString(9, u.getMJYL());
				this.pstmt.setString(10, u.getQCXS());
				this.pstmt.setString(11, u.getBZZL());
				this.pstmt.setString(12, u.getBZJS());
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
				this.pstmt.setString(24, FKPMNM);
				//System.out.println(sql);
				int count = pstmt.executeUpdate();
				if (count > 0)
					flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBConnection.close(conn, pstmt, rs);
			}
		}
		return flag;
	}

	/**
	 * 单个存储9831,后期合并到批量存储
	 * */
	public boolean SavaOfBasedata(Common9831 u) {
		boolean flag = false;
		String sql = "insert into qy_9831 VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			// this.pstmt.executeUpdate(sql);
			this.pstmt.setLong(1, u.getId());
			this.pstmt.setString(2, u.getPMNM());
			this.pstmt.setString(3, u.getPMBM());
			this.pstmt.setString(4, u.getQCBM());
			this.pstmt.setString(5, u.getPMCS());
			this.pstmt.setString(6, u.getXHTH());
			this.pstmt.setString(7, u.getXLDJ());
			this.pstmt.setString(8, u.getXHDE());
			this.pstmt.setString(9, u.getJLDW());
			this.pstmt.setString(10, u.getMJYL());
			this.pstmt.setString(11, u.getQCXS());
			this.pstmt.setString(12, u.getBZZL());
			this.pstmt.setString(13, u.getBZJS());
			this.pstmt.setString(14, u.getBZTJ());
			this.pstmt.setString(15, u.getCKDJ());
			this.pstmt.setString(16, u.getSCCJNM());
			this.pstmt.setString(17, u.getGHDWNM());
			this.pstmt.setString(18, u.getZBSX());
			this.pstmt.setString(19, u.getLBQF());
			this.pstmt.setString(20, u.getZBBDSJ());
			this.pstmt.setString(21, u.getSYBZ());
			this.pstmt.setString(22, u.getYJDBZ());
			this.pstmt.setString(23, u.getSCBZ());
			this.pstmt.setString(24, u.getSCDXNF());
			System.out.println(sql);
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return flag;
	}

	/**
	 * 编辑9831
	 * */
	public boolean UpdateOf9831(Common9831 u) {
		boolean flag = false;
		String sql = "UPDATE qy_9831 SET PMNM=?,PMBM=?,QCBM=?,PMCS=?,XHTH=?,XLDJ=?,XHDE=?,JLDW=?,MJYL=?,QCXS=?,BZZL=? ,BZJS=?,BZTJ=?,CKDJ=?,SCCJNM=?,GHDWNM=?,ZBSX=?,LBQF=?, ZBBDSJ=?,SYBZ=?,YJDBZ=?,SCBZ=? WHERE id=?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			// this.pstmt.setInt(1,u.getId());
			this.pstmt.setString(1, u.getPMNM());
			this.pstmt.setString(2, u.getPMBM());
			this.pstmt.setString(3, u.getQCBM());
			this.pstmt.setString(4, u.getPMCS());
			this.pstmt.setString(5, u.getXHTH());
			this.pstmt.setString(6, u.getXLDJ());
			this.pstmt.setString(7, u.getXHDE());
			this.pstmt.setString(8, u.getJLDW());
			this.pstmt.setString(9, u.getMJYL());
			this.pstmt.setString(10, u.getQCXS());
			this.pstmt.setString(11, u.getBZZL());
			this.pstmt.setString(12, u.getBZJS());
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
			this.pstmt.setLong(23, u.getId());
			System.out.println(sql);
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return flag;
	}

	/**
	 * 根据id删除9831
	 * */
	public boolean DelOf9831(String id) {
		boolean flag = false;
		String sql = "delete  from qy_9831 where id=?";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(id));
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return flag;
	}

	/**
	 * 清空9831
	 * */
	public boolean DeleteAll() {
		boolean flag = false;
		String sql = "delete from qy_9831";
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			int count = pstmt.executeUpdate();
			if (count > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return flag;
	}

	/**
	 * 查询9831
	 * */
	public ArrayList<Common9831> SearchOf9831(HashMap key) {
		ArrayList<Common9831> a = new ArrayList<Common9831>();
		String sql = "select * from qy_9831";

		int count = 0;
        int curpagenum=(Integer) key.get("curPageNum");
        int pageSize = (Integer) key.get("pageSize");
		Object m = key.get("PMNM");
		Object s = key.get("PMCS");
		Object P = key.get("PMBM");
		Object L = key.get("LBQF");
		if (m != null && m.equals("null")) {
			m = "";
		}
		if (s != null && s.equals("null")) {
			s = "";
		}
		if (P != null && P.equals("null")) {
			P = "";
		}
		if (L != null && L.equals("null")) {
			L = "";
		}
		// System.out.println(m.toString());
		// System.out.println(s.toString());
		if (StringUtil.isNotEmpty((String) m)
				|| StringUtil.isNotEmpty((String) s)
				|| StringUtil.isNotEmpty((String) P)
				|| StringUtil.isNotEmpty((String) L)) {
			sql = sql + " where";
			if (m.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " PMNM REGEXP '" + key.get("PMNM") + "'";
				count++;
			}
			if (s.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " PMCS REGEXP '" + key.get("PMCS") + "'";
				count++;
			}
			if (P.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " PMBM REGEXP '" + key.get("PMBM") + "'";
				count++;
			}
			if (L.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " LBQF REGEXP '" + key.get("LBQF") + "'";
				count++;
			}
		}
		sql=sql+" LIMIT ?,?";
//		System.out.println(sql);
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (curpagenum - 1) * pageSize);
			pstmt.setInt(2, pageSize);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Common9831 b = new Common9831();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}

		return a;
	}

	/**
	 * 查询9831库总条数
	 * */
	public int SearchOf9831Sum(HashMap<String, Object> key) {
		int sum = 0;
		String sql = "select * from qy_9831";

		int count = 0;

		Object m = key.get("PMNM");
		Object s = key.get("PMCS");
		Object P = key.get("PMBM");
		Object L = key.get("LBQF");
		if (m != null && m.equals("null")) {
			m = "";
		}
		if (s != null && s.equals("null")) {
			s = "";
		}
		if (P != null && P.equals("null")) {
			P = "";
		}
		if (L != null && L.equals("null")) {
			L = "";
		}
		// System.out.println(m.toString());
		// System.out.println(s.toString());
		if (StringUtil.isNotEmpty((String) m)
				|| StringUtil.isNotEmpty((String) s)
				|| StringUtil.isNotEmpty((String) P)
				|| StringUtil.isNotEmpty((String) L)) {
			sql = sql + " where";
			if (m.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " PMNM REGEXP '" + key.get("PMNM") + "'";
				count++;
			}
			if (s.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " PMCS REGEXP '" + key.get("PMCS") + "'";
				count++;
			}
			if (P.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " PMBM REGEXP '" + key.get("PMBM") + "'";
				count++;
			}
			if (L.toString().equals(""))
				sql = sql + "";
			else {
				if (count > 0)
					sql = sql + " and ";

				sql = sql + " LBQF REGEXP '" + key.get("LBQF") + "'";
				count++;
			}
		}
		//System.out.println(sql);
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				sum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn, pstmt, rs);
		}
		return sum;
	}

	/**
	 * 批量加入待选库
	 * */
	public boolean intoBaseData(List<String[]> T, String ownedUnit) {
		boolean flag = false;
		int sum = 0;
		for (int i = 0; i < T.size(); i++) {
			String sql = "insert into qy_basedata(PMNM,PMBM,QCBM,PMCS,XHTH,JLDW,CKDJ,BZTJ,BZJS,BZZL,QCXS,MJYL,XHDE,XLDJ,SCCJNM,GHDWNM,ZBSX,SCDXNF,LBQF,YJDBZ,SYBZ,SCBZ,ZBBDSJ,ownedUnit) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try {
				conn = DBConnection.getConn();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, T.get(i)[1]);
				pstmt.setString(2, T.get(i)[2]);
				pstmt.setString(3, T.get(i)[3]);
				pstmt.setString(4, T.get(i)[4]);
				pstmt.setString(5, T.get(i)[5]);
				pstmt.setString(6, T.get(i)[6]);
				pstmt.setString(7, T.get(i)[7]);
				pstmt.setString(8, T.get(i)[8]);
				pstmt.setString(9, T.get(i)[9]);
				pstmt.setString(10, T.get(i)[10]);
				pstmt.setString(11, T.get(i)[11]);
				pstmt.setString(12, T.get(i)[12]);
				pstmt.setString(13, T.get(i)[13]);
				pstmt.setString(14, T.get(i)[14]);
				pstmt.setString(15, T.get(i)[15]);
				pstmt.setString(16, T.get(i)[16]);
				pstmt.setString(17, T.get(i)[17]);
				pstmt.setString(18, T.get(i)[18]);
				pstmt.setString(19, T.get(i)[19]);
				pstmt.setString(20, T.get(i)[20]);
				pstmt.setString(21, T.get(i)[21]);
				pstmt.setString(22, T.get(i)[22]);
				pstmt.setString(23, T.get(i)[23]);
				pstmt.setString(24, ownedUnit);
				int count = pstmt.executeUpdate();
				sum += count;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBConnection.close(conn, pstmt);
			}
		}
		if (sum == T.size()) {
			flag = true;
		}
		return flag;
	}
}
