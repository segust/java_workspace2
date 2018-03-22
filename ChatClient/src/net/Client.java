package net;

import java.io.*;
import java.net.*;

public class Client {
	private static Socket socket;

	public Client() {
		try {
			socket = new Socket("127.0.0.1", 3636);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Socket getSocket() {
		if (socket == null)
			new Client();
		return socket;
	}

	public void close() {
		if (socket != null)
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
