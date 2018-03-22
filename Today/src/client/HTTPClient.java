package client;

import java.net.*;
import java.io.*;

public class HTTPClient {
	Socket socket;
	public static void main(String args[]) {
		HTTPClient client = new HTTPClient();
		client.doGet(); // 按照GET请求方式访问HTTPServer
	}

	/** 按照GET请求方式访问HTTPServer */
	

	public void doGet() {
		try {
			socket = new Socket("www.cqupt.edu.cn", 80); // 与HTTPServer建立FTP连接
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			/* 创建HTTP请求 */
			StringBuffer sb = new StringBuffer("GET " + "cqupt/index.shtml" + " HTTP/1.1\r\n");
			sb.append("Accept:*/*\r\n");
			sb.append("Accept-Language:zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			sb.append("Accept-Encoding: UTF-8, deflate\r\n");
			sb.append("User-Agent: HTTPClient\r\n");
			sb.append("Host:www.cqupt.edu.cn:80\r\n");
			sb.append("Connection:Keep-Alive\r\n\r\n");
			System.out.println(sb + "\n");
			/* 发送HTTP请求 */
			OutputStream socketOut = socket.getOutputStream(); // 获得输出流
			socketOut.write(sb.toString().getBytes());

			Thread.sleep(2000); // 睡眠2秒，等待响应结果

			/* 接收响应结果 */
			InputStream socketIn = socket.getInputStream(); // 获得输入流
			int size = socketIn.available();
			byte[] buffer = new byte[size];
			socketIn.read(buffer);
			System.out.println(new String(buffer)); // 打印响应结果

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
