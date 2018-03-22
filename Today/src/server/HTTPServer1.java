package server;

import java.io.*;
import java.net.*;
import java.util.*;
public class HTTPServer1 {
	
	private static Map<String, Servlet> servletCache=new HashMap<String, Servlet>();
	public static void main(String args[]) {
		int port;
		ServerSocket serverSocket;

		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("port=8080（默认）");
			port = 8080;
			// TODO: handle exception
		}

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("服务器正在监听端口：" + serverSocket.getLocalPort());

			while (true) {
				try {
					final Socket socket = serverSocket.accept();
					System.out.println("建立了与客户的一个新的TCP连接，该客户的地址为：" + socket.getInetAddress() + ":" + socket.getPort());

					service(socket);
				} catch (Exception e) {
					e.printStackTrace();// TODO: handle exception
				}
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static void service(Socket socket) throws Exception {
		InputStream socketIn = socket.getInputStream();
		Thread.sleep(500);
		int size = socketIn.available();
		byte[] requestBuffer = new byte[size];
		socketIn.read(requestBuffer);
		String request = new String(requestBuffer);
		System.out.println(request);

		String firstLineOfRequest = request.substring(0, request.indexOf("\r\n"));
		String[] parts = firstLineOfRequest.split(" ");
		String uri = parts[1];
		
		if(uri.indexOf("servlet")!=-1){
			String servletName=null;
			if(uri.indexOf("?")!=-1)
				servletName=uri.substring(uri.indexOf("servlet/")+8, uri.indexOf("?"));
			else
				servletName=uri.substring(uri.indexOf("servlet/")+8,uri.length());
			Servlet servlet=(Servlet)servletCache.get(servletName);
			
			if(servlet==null){
				servlet=(Servlet)Class.forName("server."+servletName).newInstance();
				servlet.init();
				servletCache.put(servletName, servlet);
			}
			
			servlet.service(requestBuffer,socket.getOutputStream());
			
			Thread.sleep(1000);
			socket.close();
			return;
		}

		String contentType;
		if (uri.indexOf("html") != -1 || uri.indexOf("htm") != -1)
			contentType = "text/html";
		else if (uri.indexOf("jpg") != -1 || uri.indexOf("jpeg") != -1)
			contentType = "image/jpeg";
		else if (uri.indexOf("gif") != -1)
			contentType = "image/gif";
		else
			contentType = "application/octet-stream";

		String responseFirstLine = "HTTP/1.1 200 OK\r\n";

		String responseHeader = "Content-Type:" + contentType + "\r\n\r\n";
		InputStream in = HTTPServer.class.getResourceAsStream("root/" + uri);

		OutputStream socketOut = socket.getOutputStream();
		socketOut.write(responseFirstLine.getBytes());
		socketOut.write(responseHeader.getBytes());

		int len = 0;
		requestBuffer = new byte[1024];
		while ((len = in.read(requestBuffer))!= -1)
			socketOut.write(requestBuffer, 0, len);

		Thread.sleep(1000);
		socket.close();
	}
}
