package cn.edu.cqupt.beans;

public class CompanyData {
	private long   companyId;
	private String companyName;
	private String ownedJdsName;
	private String companyManager;
	private String leader;
	
	public CompanyData(long companyId, String companyName, String ownedJdsName,
			String companyManager, String leader){
		super();
		this.companyId=companyId;
		this.companyName=companyName;
		this.ownedJdsName=ownedJdsName;
		this.companyManager=companyManager;
		this.leader=leader;
	}
	
	public CompanyData(){
		super();
	}
	
	public long getCompanyId(){
		return companyId;
	}
	
	public void setCompanyId(long companyId){
		this.companyId=companyId;
	}
	
	public String getCompanyName(){
		return companyName;
	}
	
	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}
	
	public String getOwnedJdsName(){
		return ownedJdsName;
	}
	
	public void setOwnedJdsName(String ownedJdsName){
		this.ownedJdsName=ownedJdsName;
	}
	
	public String getCompanyManager(){
		return companyManager;
	}
	
	public void setCompanyManager(String companyManager){
		this.companyManager=companyManager;
	}
	
	public String getLeader(){
		return leader;
	}
	
	public void setLeader(String leader){
		this.leader=leader;
	}
}
