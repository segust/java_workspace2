package util;

import java.io.IOException;
import java.io.InputStream;

public class Parser {
	private String result;
	private String content;
	// StringBuffer mess = null;
	String mess = null;
	int i;

	public Parser(InputStream in) {
		try {
			byte[] buffer = new byte[2048];
			i = in.read(buffer);
			mess = new String(buffer, 0, i);
			System.out.println("收到的来自服务端的信息：\n" + mess);
			String[] mes = mess.split("\n");
			result = mes[0].substring(7, mes[0].length());
			content = mes[1].substring(8, mes[1].length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getResult() {
		return result;
	}

	public String getContent() {
		return content;
	}

}
