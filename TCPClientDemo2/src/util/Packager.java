package util;

public class Packager {
	private StringBuffer message;

	public Packager(String operate, String bookinfo) {
		message = new StringBuffer("");
		message.append("operate:" + operate);
		message.append("\n");
		message.append("content:" + bookinfo);
	}

	public String getMessage() {
		return message.toString();
	}
}
