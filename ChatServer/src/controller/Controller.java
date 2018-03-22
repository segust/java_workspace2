package controller;

import java.io.IOException;
import java.net.Socket;

import net.Server;
import util.Operator;
import util.Packager;
import util.Parser;

public class Controller {
	private Parser parser;
	private Socket socket;
	private Packager packager = new Packager();
	private Operator operator = new Operator();
	public boolean isLogout = false;

	public Controller(Socket socket) {
		this.socket = socket;
	}

	public void Response() {
		while (!isLogout) {
			try {

				byte[] buffer = new byte[1024];
				int length = socket.getInputStream().read(buffer);
				String mess = new String(buffer, 0, length);
				System.out.println("�յ����Կͻ��˵���Ϣ��\n" + mess + "\n");
				parser = new Parser(mess);
				String aim = parser.getAim();

				// �ͻ����˳�������
				if (aim.equals("logout")) {
					isLogout = true;
					String id = parser.getContent();

					// �жϿͻ����Ƿ�ӵ�¼����ֱ���˳�
					if (!id.equals("nullName")) {
						Server.UserPool.remove(id);
						int ID = Integer.valueOf(id);
						String name = operator.findName(ID);
						int num = Server.UserPool.size();
						if (num > 0) {
							Object[] object = Server.UserPool.keySet().toArray();
							packager.Package("OutlineNotice", name + "(" + id + ")");
							String message = packager.getMessage();
							for (int i = 0; i < num; i++) {
								Server.UserPool.get(object[i]).send(message);
							}
						}
						close();
					} else {

						close();
					}
				}

				// �������Կͻ�����������

				// �ͻ��˺����б�������
				else if (aim.equals("list")) {
					int num = Server.UserPool.size();
					String username;
					if (num > 0) {
						Object[] object = Server.UserPool.keySet().toArray();
						String userlist = num + "?";
						for (int j = 0; j < num - 1; j++) {
							Operator operator = new Operator();
							username = operator.findName(Integer.valueOf((String) object[j]));
							userlist = userlist + username + "(" + object[j].toString() + ")" + "#";
						}
						Operator operator = new Operator();
						userlist = userlist + operator.findName(Integer.valueOf((String) object[num - 1])) + "("
								+ object[num - 1].toString() + ")";
						packager.Package("listAck", userlist);
						send(packager.getMessage());
					} else {
						packager.Package("listAck", "�������ߣ�");
						send(packager.getMessage());
					}
				}

				// �ͻ�������������
				else if (parser.getAim().equals("send")) {
					String mes[] = new String[2];
					mes = parser.getContent().split("#");
					String user[] = new String[2];
					user = mes[0].split(":");
					String fromWhom = user[0];
					String toWhom = user[1];
					String content = mes[1];
					System.out.println("���յ�����" + fromWhom + "�ͻ��˷�����" + toWhom + "����Ϣ��" + content);
					String[] userinfo = toWhom.split("[(]");
					String id = userinfo[1].replace(")", "");

					if (Server.UserPool.get(id) != null) { // �ж��û��Ƿ����߻����
						packager.Package("chatInfo", fromWhom + ":" + content);
						Server.UserPool.get(id).send(packager.getMessage());
						System.out.println("���ͳɹ���");
					} else {
						packager.Package("sendAck", "���û������߻򲻴��ڣ�");
						send(packager.getMessage());
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void send(String mess) {
		try {
			socket.getOutputStream().write(mess.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
