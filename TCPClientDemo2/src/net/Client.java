package net;

import java.io.IOException;
import java.net.Socket;

public class Client {
	private static Socket socket;

	// 建立socket
	public static void init() throws Exception {
		socket = new Socket("127.0.0.1", 3636);
	}

	// 关闭连接方法
	public static void close() {
		try {
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 提取socket
	public static Socket getSocket() {
		try {
			if (socket.isClosed()) {
				init();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return socket;
	}

}
