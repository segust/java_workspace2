package client;

import java.net.*;
import java.io.*;

public class HTTPClient {
	Socket socket;
	public static void main(String args[]) {
		HTTPClient client = new HTTPClient();
		client.doGet(); // ����GET����ʽ����HTTPServer
	}

	/** ����GET����ʽ����HTTPServer */
	

	public void doGet() {
		try {
			socket = new Socket("www.cqupt.edu.cn", 80); // ��HTTPServer����FTP����
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			/* ����HTTP���� */
			StringBuffer sb = new StringBuffer("GET " + "cqupt/index.shtml" + " HTTP/1.1\r\n");
			sb.append("Accept:*/*\r\n");
			sb.append("Accept-Language:zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			sb.append("Accept-Encoding: UTF-8, deflate\r\n");
			sb.append("User-Agent: HTTPClient\r\n");
			sb.append("Host:www.cqupt.edu.cn:80\r\n");
			sb.append("Connection:Keep-Alive\r\n\r\n");
			System.out.println(sb + "\n");
			/* ����HTTP���� */
			OutputStream socketOut = socket.getOutputStream(); // ��������
			socketOut.write(sb.toString().getBytes());

			Thread.sleep(2000); // ˯��2�룬�ȴ���Ӧ���

			/* ������Ӧ��� */
			InputStream socketIn = socket.getInputStream(); // ���������
			int size = socketIn.available();
			byte[] buffer = new byte[size];
			socketIn.read(buffer);
			System.out.println(new String(buffer)); // ��ӡ��Ӧ���

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // #doGet()
}
