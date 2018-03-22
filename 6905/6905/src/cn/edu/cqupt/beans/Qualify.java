package cn.edu.cqupt.beans;


public class Qualify {

	private long   qualifyId;   //资质文件系统中的id
	private String qualifyType;//文件类型
	private String qualifyTitle;//文件标题
	private String qualifyPath;//资质文件路径
	private String ownedUnit;	//资质文件所属单位
	private String year;//资质文件年份
	private String qualifyAttr;//资质文件属性 自查还是上报
	
	public Qualify(long qualifyId, String qualifyType, String qualifyTitle,
			String qualifyPath,String ownedUnit,String year,String qualifyAttr) {
		super();
		this.qualifyId = qualifyId;
		this.qualifyType = qualifyType;
		this.qualifyTitle = qualifyTitle;
		this.qualifyPath = qualifyPath;
		this.ownedUnit=ownedUnit;
		this.year=year;
		this.qualifyAttr=qualifyAttr;
	}
	public Qualify() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getOwnedUnit() {
		return ownedUnit;
	}
	public void setOwnedUnit(String ownedUnit) {
		this.ownedUnit = ownedUnit;
	}
	public long getQualifyId() {
		return qualifyId;
	}
	public void setQualifyId(long qualifyId) {
		this.qualifyId = qualifyId;
	}
	public String getQualifyType() {
		return qualifyType;
	}
	public void setQualifyType(String qualifyType) {
		this.qualifyType = qualifyType;
	}
	public String getQualifyTitle() {
		return qualifyTitle;
	}
	public void setQualifyTitle(String qualifyTitle) {
		this.qualifyTitle = qualifyTitle;
	}
	public String getQualifyPath() {
		return qualifyPath;
	}
	public void setQualifyPath(String qualifyPath) {
		this.qualifyPath = qualifyPath;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQualifyAttr() {
		return qualifyAttr;
	}
	public void setQualifyAttr(String qualifyAttr) {
		this.qualifyAttr = qualifyAttr;
	}
		
}
