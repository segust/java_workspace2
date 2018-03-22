package util;

import java.io.IOException;
import java.io.InputStream;

public class Parser {

	String mess = null;

	public Parser(InputStream in) {
		try {
			byte[] buffer = new byte[1024];
			int i = in.read(buffer);
			mess = new String(buffer, 0, i);
			System.out.println("客户端发送给服务端的信息：\n" + mess + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getInfo() {
		return mess;
	}

}
