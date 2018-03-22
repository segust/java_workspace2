package net;

import java.io.*;
import java.net.*;

import controller.Controller;
import model.User;
import util.Operator;
import util.Packager;
import util.Parser;

public class ServerThread extends Thread {

	private Socket socket;
	private User user;
	private Parser parser;
	private Operator operator = new Operator();
	private Packager packager = new Packager();
	private boolean isOnline = false;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			String name = socket.getLocalAddress().getHostAddress() + "::" + socket.getPort();
			System.out.println("A user has connected:" + name);
			while (true) {
				byte[] buffer = new byte[1024];
				int length = socket.getInputStream().read(buffer);
				String mess = new String(buffer, 0, length);
				System.out.println("�յ����Կͻ��˵���Ϣ��" + mess + "\n");
				parser = new Parser(mess);

				// �жϵ�¼��Ϣ
				if (parser.getAim().equals("login")) {
					String mes[] = new String[2];
					mes = parser.getContent().split("#");
					String id = mes[0];
					String password = mes[1];
					user = operator.login(id, password);

					// ȷ�ϵ�¼��Ϣ��������¼��Ϣ��ServerThread����UserPool
					if (user != null) {

						Object[] object = Server.UserPool.keySet().toArray();
						int Num = Server.UserPool.size();

						// �û��Ѿ����ߴ���
						for (int j = 0; j < Num; j++) {
							if (String.valueOf(object[j]).equals(String.valueOf(user.getId()))) {
								packager.Package("loginAck", "�û��Ѿ����ߣ�");
								socket.getOutputStream().write(packager.getMessage().getBytes());
								isOnline = true;
								break;
							}
						}

						if (!isOnline) {
							packager.Package("loginAck", user.getId() + "#" + user.getUsername() + "#" + user.getSex()
									+ "#" + user.getAge());
							Server.UserPool.put(id, this);
							socket.getOutputStream().write(packager.getMessage().getBytes());

							// ֪ͨUserPool�������û�
							for (int i = 0; i < Num; i++) {
								packager.Package("OnlineNotice",
										user.getUsername() + "(" + String.valueOf(user.getId()) + ")");
								String message = packager.getMessage();
								Server.UserPool.get(object[i]).getSocket().getOutputStream().write(message.getBytes());
							}

							Controller controller = new Controller(socket);
							controller.Response();
							if (controller.isLogout) {
								break;
							}
						}
					} else {
						packager.Package("loginAck", "�û������������");
						socket.getOutputStream().write(packager.getMessage().getBytes());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String mess) {
		try {
			socket.getOutputStream().write(mess.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}
}
