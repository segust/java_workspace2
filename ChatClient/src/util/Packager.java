package util;

public class Packager {
	private StringBuffer message;

	public void Package(String operate, String content) {
		message = new StringBuffer("");
		message.append("aim:" + operate);
		message.append("\n");
		message.append("content:" + content);
		System.out.println("��������Ϣ��" + message);
	}

	public String getMessage() {
		return message.toString();
	}
}
