package util;

public class Parser {

	private String aim;
	private String content;
	private String[] mes = new String[2];

	public Parser(String mess) {
		mes = mess.split("\n");
	}

	// 获取第一行协议信息
	public String getAim() {
		aim = mes[0].substring(4, mes[0].length());
		return aim;
	}

	// 获取第二行信息
	public String getContent() {
		content = mes[1].substring(8, mes[1].length());
		return content;
	}

}
