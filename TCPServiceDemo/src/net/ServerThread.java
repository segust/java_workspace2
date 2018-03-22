package net;

import java.io.*;
import java.net.*;

import control.Controller;
import util.Parser;

public class ServerThread extends Thread {
	private Parser parser;
	private Socket socket;
	InputStream in;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			String name = socket.getLocalAddress().getHostAddress() + "::" + socket.getPort();
			System.out.println("Receive a request from:" + name);
			parser = new Parser(socket.getInputStream());
			Controller controller = new Controller(parser.getInfo());
			if (controller.getOut().equals("exit")) {
				socket.close();
				Thread.interrupted();
			} else {
				byte[] message = controller.getOut().getBytes();
				socket.getOutputStream().write(message);
				System.out.println("信息发出!\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
