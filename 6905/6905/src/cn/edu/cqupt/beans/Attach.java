package cn.edu.cqupt.beans;

public class Attach {
	
	private long  attachId;   //附件文件系统中的id
	private String attachTitle;//文件标题
	private String attachPath;//文件路径
	private long fareId;//费用在系统中的id
	
	
	public Attach(long attachId, String attachTitle, String attachPath) {
		super();
		this.attachId = attachId;
		this.attachTitle = attachTitle;
		this.attachPath = attachPath;
	}
	public Attach() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getAttachId() {
		return attachId;
	}
	public void setAttachId(long attachId) {
		this.attachId = attachId;
	}
	public long getFareId() {
		return fareId;
	}
	public void setFareId(long fareId) {
		this.fareId = fareId;
	}
	public String getAttachTitle() {
		return attachTitle;
	}
	public void setAttachTitle(String attachTitle) {
		this.attachTitle = attachTitle;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	

}
