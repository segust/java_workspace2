package net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
	private static ServerSocket serversocket;
	private Socket socket;
	public static HashMap<String, ServerThread> UserPool = new HashMap<String, ServerThread>();

	public Server() {
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

	public static void main(String[] args) {
		new Server();
	}

}
