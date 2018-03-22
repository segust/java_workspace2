package cn.edu.cqupt.beans;

public class ZhjData {
	private long   zhjId;
	private String zhjName;
	private String zhjManager;
	private String leader;
	
	public ZhjData(long zhjId, String zhjName, String zhjManager, String leader){
		super();
		this.zhjId=zhjId;
		this.zhjName=zhjName;
		this.zhjManager=zhjManager;
		this.leader=leader;
	}
	
	public ZhjData(){
		super();
	}
	
	public long getzhjId(){
		return zhjId;
	}
	
	public void setzhjId(long zhjId){
		this.zhjId=zhjId;
	}
	
	public String zhjName(){
		return zhjName;
	}
	
	public void setzhjName(String zhjName){
		this.zhjName=zhjName;
	}
	
	public String getzhjManager(){
		return zhjManager;
	}
	
	public void setzhjManager(String zhjManager){
		this.zhjManager=zhjManager;
	}
	
	public String getLeader(){
		return leader;
	}
	
	public void setLeader(String leader){
		this.leader=leader;
	}
}