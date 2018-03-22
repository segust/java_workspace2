package cn.edu.cqupt.beans;

public class JdjData {
	private long   jdjId;
	private String jdjName;
	private String ownedZhjName;
	private String jdjManager;
	private String leader;
	
	public JdjData(long jdjId, String jdjName, String ownedZhjName,String jdjManager, String leader){
		super();
		this.jdjId=jdjId;
		this.jdjName=jdjName;
		this.ownedZhjName=ownedZhjName;
		this.jdjManager=jdjManager;
		this.leader=leader;
	}
	
	public JdjData(){
		super();
	}
	
	public long getJdjId(){
		return jdjId;
	}
	
	public void setJdjId(long jdjId){
		this.jdjId=jdjId;
	}

	public String getJdjName(){
		return jdjName;
	}
	
	public void setJdjName(String jdjName){
		this.jdjName=jdjName;
	}
	
	public String getOwnedZhjName(){
		return ownedZhjName;
	}
	
	public void setOwnedZhjName(String ownedZhjName){
		this.ownedZhjName=ownedZhjName;
	}
	
	public String getJdjManager(){
		return jdjManager;
	}
	
	public void setJdjManager(String jdjManager){
		this.jdjManager=jdjManager;
	}
	
	public String getLeader(){
		return leader;
	}
	
	public void setLeader(String leader){
		this.leader=leader;
	}
}
