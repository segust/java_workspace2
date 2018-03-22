package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static ServerSocket serversocket;
	private static Socket socket;

	static {
		try {
			serversocket = new ServerSocket(3636);
			while (true) {
				socket = serversocket.accept();
				ServerThread serverthread = new ServerThread(socket);
				serverthread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (serversocket != null) {
			try {
				serversocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
