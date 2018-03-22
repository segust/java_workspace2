package controller;

import java.io.IOException;
import java.net.Socket;

import net.Client;
import ui.mainFrame;
import util.Packager;
import util.Parser;

public class Controller {

	public static Socket socket;
	private String result;
	private String message;
	Parser parser;
	String content;
	Packager packager = new Packager();

	static {
		Controller.socket = Client.getSocket();
	}

	public void send(String message) {
		try {
			socket.getOutputStream().write(message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void login(String username, char[] pass) {
		try {
			String password = new String(pass, 0, pass.length);
			packager.Package("login", username + "#" + password.toString());
			message = packager.getMessage();
			send(message);
			byte[] buffer = new byte[2048];
			int length = socket.getInputStream().read(buffer);
			String mess = new String(buffer, 0, length);
			System.out.println("收到的来自服务端的信息：\n" + mess);
			parser = new Parser(mess);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (parser.getAim().equals("loginAck")) {
			if (parser.getContent().equals("用户已经在线！") || parser.getContent().equals("用户名或密码错误！")) {
				result = "deny";
			} else {
				result = "agree";
			}
			content = parser.getContent();
		}
	}

	public String[] list() {
		String[] userList = null;
		try {
			packager.Package("list", "forUserlist");
			message = packager.getMessage();
			send(message);
			byte[] buffer = new byte[2048];
			int length = socket.getInputStream().read(buffer);
			String mess = new String(buffer, 0, length);
			System.out.println("收到的来自服务端的信息：\n" + mess);
			parser = new Parser(mess);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (parser.getAim().equals("listAck")) {
			if (!parser.getContent().equals("无人在线！")) {
				String infomation = parser.getContent();
				String[] info = new String[2];
				info = infomation.split("\\?");
				String userlist = info[1];
				userList = userlist.split("#");
			}
		}
		return userList;
	}

	public void chat(String toWhom, String content) {
		packager.Package("send",
				mainFrame.user.getUsername() + "(" + mainFrame.user.getId() + ")" + ":" + toWhom + "#" + content);
		message = packager.getMessage();
		send(message);
	}

	public void logout(int id) {
		String Id = String.valueOf(id);
		packager.Package("logout", Id);
		message = packager.getMessage();
		send(message);
	}

	public void logout() {
		packager.Package("logout", "nullName");
		message = packager.getMessage();
		send(message);
		close();
	}

	private void close() {
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getContent() {
		return content;
	}

	public String getResult() {
		return result;
	}

}
