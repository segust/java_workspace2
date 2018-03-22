package model;

/** 
 * 数据类
 */
public class WebLink {
	
	protected String fromLink;   //web ID
	protected String toLink;     //web ID (由fromLink链接到toLink)
	
	/**
	 * @param fromLink web ID (由fromLink链接到toLink)
	 * @param toLink web ID (由fromLink链接到toLink)
	 */
	public WebLink(String fromLink, String toLink) {
		this.fromLink = fromLink;
		this.toLink = toLink;
	}

	public String getFromLink() {
		return fromLink;
	}

	public void setFromLink(String fromLink) {
		this.fromLink = fromLink;
	}

	public String getToLink() {
		return toLink;
	}

	public void setToLink(String toLink) {
		this.toLink = toLink;
	}
	
}
