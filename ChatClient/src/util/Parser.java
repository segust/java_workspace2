package util;

public class Parser {
	private String aim;
	private String content;

	public Parser(String mess) {
		String[] mes = mess.split("\n");
		aim = mes[0].substring(4, mes[0].length());
		content = mes[1].substring(8, mes[1].length());
	}

	public String getAim() {
		return aim;
	}

	public String getContent() {
		return content;
	}

}
