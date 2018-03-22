package control;

import java.io.*;
import java.net.Socket;

import net.Client;
import util.Packager;
import util.Parser;

public class Controller {
	private String result;
	private String content;
	InputStream in;
	Packager packager;
	Parser parser;
	Socket socket;
	OutputStream out;

	private void doResponse() {
		Client client = new Client();
		socket = client.getSocket();
		String message = packager.getMessage();
		try {
			out = socket.getOutputStream();
			out.write(message.getBytes());
			out.flush();

			// 去接收
			in = socket.getInputStream();
			parser = new Parser(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		result = parser.getResult();
		content = parser.getContent();

	}

	public void add(String bookname, String author, String price, String press) {
		packager = new Packager("addbook", bookname + "#" + author + "#" + price + "#" + press);
		doResponse();
	}

	public void login(String username, char[] pass) {
		String password = new String(pass, 0, pass.length);
		packager = new Packager("login", username + "#" + password);
		doResponse();
	}

	public void delete(String bookname) {
		packager = new Packager("deletebook", bookname);
		doResponse();
	}

	public void modify(String oldbook, String bookname, String author, String price, String press) {
		packager = new Packager("modifybook", oldbook + "#" + bookname + "#" + author + "#" + price + "#" + press);
		doResponse();
	}

	public void search(String bookname) {
		packager = new Packager("searchbook", bookname);
		doResponse();
		if (result.equals("查询成功！")) {
			String[] mes = content.split("#");
			content = "id:" + mes[0] + "\n书名：《" + mes[1] + "》\n作者：" + mes[2] + "\n价格：" + mes[3] + "\n出版社：" + mes[4];
		}
	}

	public String[][] showlist() {
		packager = new Packager("showlist", null);
		doResponse();
		int num = Integer.parseInt(getResult());
		String[] mes = content.split("#");
		String[][] content = new String[num][5];
		for (int i = 0, j = 0; i < num; i++, j++) {
			content[i][0] = mes[j];
			content[i][1] = mes[++j];
			content[i][2] = mes[++j];
			content[i][3] = mes[++j];
			content[i][4] = mes[++j];
		}
		return content;
	}

	public String getResult() {
		return result;
	}

	public String getContent() {
		return content;
	}
}
