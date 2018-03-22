package util;

public class Packager {
	private StringBuffer message;

	public void Package(String operateRes, String info) {

		message = new StringBuffer();
		message.append("result:" + operateRes);
		message.append("\n");
		message.append("content:" + info);
		System.out.println("服务器发送的数据:\n" + message);
	}

	public String getMessage() {
		return message.toString();
	}
}
