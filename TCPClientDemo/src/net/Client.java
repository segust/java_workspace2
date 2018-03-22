package net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket socket;

	// ����socket
	public Client() {
		try {
			socket = new Socket("127.0.0.1", 3636);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �ر����ӷ���
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ��ȡsocket
	public Socket getSocket() {
		if (socket == null) {
			new Client();
		}
		return socket;
	}

}
