package net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket socket;

	// 建立socket
	public Client() {
		try {
			socket = new Socket("127.0.0.1", 3636);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 关闭连接方法
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 提取socket
	public Socket getSocket() {
		if (socket == null) {
			new Client();
		}
		return socket;
	}

}
