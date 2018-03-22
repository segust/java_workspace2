package cn.edu.cqupt.beans;

/**
 * @author austin
 * 产品待选库
 * PMNM;//产品内码
 * PMBM;//产品编码
 * PMCS;//产品名称
 * CKDJ;//单价
 * LBQF;//产品类型
 * */
public class Common9831 {


	private long id;


	private String PMNM="";//产品内码
	private String PMBM="";//产品编码
	private String QCBM="";
	private String PMCS="";//产品名称
	private String XHTH="";
	private String XLDJ="";
	private String XHDE="";
	private String JLDW="";
	private String MJYL="";
	private String QCXS="";
	private String BZZL="";
	private String BZJS="";
	private String BZTJ="";
	private String CKDJ="";//单价
	private String SCCJNM="";
	private String GHDWNM="";
	private String ZBSX="";
	private String LBQF="";//产品类型
	private String ZBBDSJ="";
	private String SYBZ="";
	private String YJDBZ="";
	private String SCBZ="";
	private String SCDXNF="";//生产定型年份
	
	public Common9831(String PMNM,String PMBM,String QCBM,String PMCS,String XHTH,String XLDJ,
String XHDE,String JLDW,String MJYL,String QCXS, String BZZL,String BZJS,String BZTJ,String CKDJ,String SCCJNM,
String GHDWNM,String ZBSX,String LBQF,String ZBBDSJ,String SYBZ,String YJDBZ,String SCBZ,String SCDXNF){
		super();
		this.PMNM=PMNM;
		this.PMBM=PMBM;
		this.QCBM=QCBM;
		this.PMCS=PMCS;
		this.XHTH=XHTH;
		this.XLDJ=XLDJ;
		this.XHDE=XHDE;
		this.JLDW=JLDW;
		this.MJYL=MJYL;
		this.QCXS=QCXS;
		this.BZZL=BZZL;
		this.BZJS=BZJS;
		this.BZTJ=BZTJ;
		this.CKDJ=CKDJ;
		this.SCCJNM=SCCJNM;
		this.GHDWNM=GHDWNM;
		this.ZBSX=ZBSX;
		this.LBQF=LBQF;
		this.ZBBDSJ=ZBBDSJ;
		this.SYBZ=SYBZ;
		this.YJDBZ=YJDBZ;
		this.SCBZ=SCBZ;
		this.SCDXNF=SCDXNF;
	}
	public String getSCDXNF() {
		return SCDXNF;
	}
	public void setSCDXNF(String sCDXNF) {
		SCDXNF = sCDXNF;
	}
	public Common9831() {
		super();
	}
	
	public long getId() {

		return id;

	}

	public void setId(long id) {

		this.id = id;

	}

	public String getPMNM() {
		return PMNM;
	}

	public void setPMNM(String pMNM) {
		PMNM = pMNM;
	}

	public String getPMBM() {
		return PMBM;
	}

	public void setPMBM(String pMBM) {
		PMBM = pMBM;
	}

	public String getQCBM() {
		return QCBM;
	}

	public void setQCBM(String qCBM) {
		QCBM = qCBM;
	}

	public String getPMCS() {
		return PMCS;
	}

	public void setPMCS(String pMCS) {
		PMCS = pMCS;
	}

	public String getXHTH() {
		return XHTH;
	}

	public void setXHTH(String xHTH) {
		XHTH = xHTH;
	}

	public String getXLDJ() {
		return XLDJ;
	}

	public void setXLDJ(String xLDJ) {
		XLDJ = xLDJ;
	}

	public String getXHDE() {
		return XHDE;
	}

	public void setXHDE(String xHDE) {
		XHDE = xHDE;
	}

	public String getJLDW() {
		return JLDW;
	}

	public void setJLDW(String jLDW) {
		JLDW = jLDW;
	}

	public String getMJYL() {
		return MJYL;
	}

	public void setMJYL(String mJYL) {
		MJYL = mJYL;
	}

	public String getQCXS() {
		return QCXS;
	}

	public void setQCXS(String qCXS) {
		QCXS = qCXS;
	}

	public String getBZZL() {
		return BZZL;
	}

	public void setBZZL(String bZZL) {
		BZZL = bZZL;
	}

	public String getBZJS() {
		return BZJS;
	}

	public void setBZJS(String bZJS) {
		BZJS = bZJS;
	}

	public String getBZTJ() {
		return BZTJ;
	}

	public void setBZTJ(String bZTJ) {
		BZTJ = bZTJ;
	}

	public String getCKDJ() {
		return CKDJ;
	}

	public void setCKDJ(String cKDJ) {
		CKDJ = cKDJ;
	}

	public String getSCCJNM() {
		return SCCJNM;
	}

	public void setSCCJNM(String sCCJNM) {
		SCCJNM = sCCJNM;
	}

	public String getGHDWNM() {
		return GHDWNM;
	}

	public void setGHDWNM(String gHDWNM) {
		GHDWNM = gHDWNM;
	}

	public String getZBSX() {
		return ZBSX;
	}

	public void setZBSX(String zBSX) {
		ZBSX = zBSX;
	}

	public String getLBQF() {
		return LBQF;
	}

	public void setLBQF(String lBQF) {
		LBQF = lBQF;
	}

	public String getZBBDSJ() {
		return ZBBDSJ;
	}

	public void setZBBDSJ(String zBBDSJ) {
		ZBBDSJ = zBBDSJ;
	}

	public String getSYBZ() {
		return SYBZ;
	}

	public void setSYBZ(String sYBZ) {
		SYBZ = sYBZ;
	}

	public String getYJDBZ() {
		return YJDBZ;
	}

	public void setYJDBZ(String yJDBZ) {
		YJDBZ = yJDBZ;
	}

	public String getSCBZ() {
		return SCBZ;
	}

	public void setSCBZ(String sCBZ) {
		SCBZ = sCBZ;
	}
}
