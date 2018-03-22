package cn.edu.cqupt.beans;

public class JdsData {
	private long   jdsId;
	private String jdsName;
	private String ownedJdjName;
	private String jdsManager;
	private String leader;
	
	public JdsData(long jdsId, String jdsName, String ownedJdjNamee, String jdsManager, String leader){
		super();
		this.jdsId=jdsId;
		this.jdsName=jdsName;
		this.ownedJdjName=ownedJdjName;
		this.jdsManager=jdsManager;
		this.leader=leader;
	}
	
	public JdsData(){
		super();
	}
	
	public long getJdsId(){
		return jdsId;
	}
	
	public void setJdsId(long jdsId){
		this.jdsId=jdsId;
	}

	public String getJdsName(){
		return jdsName;
	}
	
	public void setJdsName(String jdsName){
		this.jdsName=jdsName;
	}
	
	public String getOwnedJdjName(){
		return ownedJdjName;
	}
	
	public void setOwnedJdjName(String ownedJdjName){
		this.ownedJdjName=ownedJdjName;
	}
	
	public String getJdsManager(){
		return jdsManager;
	}
	
	public void setJdsManager(String jdsManager){
		this.jdsManager=jdsManager;
	}
	
	public String getLeader(){
		return leader;
	}
	
	public void setLeader(String leader){
		this.leader=leader;
	}

}
