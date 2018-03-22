package model;
/**
 * 数据类
 * */
public class Node {

	private String type;//短信类别
	private String message;//短信内容
	
	public Node() {
		super();
	}
	
	public Node(String type, String message) {
		super();
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
