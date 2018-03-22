package control;

import java.sql.SQLException;

import model.Book;
import model.User;
import util.Operator;
import util.Packager;
import util.Parser;

public class Controller {
	private String out;
	Book book = null;
	User user = null;
	Operator operator = new Operator();
	Packager packager = new Packager();
	Parser parser;

	public Controller(String info) {
		String[] mes = new String[2];
		mes = info.split("\n");
		String operate = mes[0].substring(8, mes[0].length());
		String content = mes[1].substring(8, mes[1].length());

		// 根据请求不同建立不执行不同操作
		if (operate.equals("addbook")) {
			book = new Book(content, 1);
			try {
				operator.add(book);
				packager.Package("添加成功！", "添加成功！");
			} catch (SQLException e) {
				packager.Package("error", "添加失败！");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}

		}

		else if (operate.equals("modifybook")) {
			book = new Book(content, 1, 1);
			try {
				String oldbook = book.getOldbookname();
				operator.modify(oldbook, book);
				packager.Package("修改成功！", "修改成功！");
			} catch (SQLException e) {
				packager.Package("error", "没有找到这本书！");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}

		}

		else if (operate.equals("deletebook")) {
			book = new Book(content);
			try {
				operator.delete(book);
				packager.Package("删除成功！", "删除成功！");
			} catch (SQLException e) {
				packager.Package("error", "没有找到这本书！");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}
		}

		else if (operate.equals("searchbook")) {
			book = new Book(content);
			try {
				String bookin = operator.search(book);
				if (!bookin.equals("查询失败！")) {
					packager.Package("查询成功！", bookin);
				} else {
					packager.Package("查询失败", "未找到这本书！");
				}

			} catch (SQLException e) {
				packager.Package("error", "查询失败！");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}
		}

		else if (operate.equals("login")) {
			user = new User(content);
			try {
				if (operator.login(user.getUsername(), user.getPassword())) {
					packager.Package("agree", "登录成功！");
				} else {
					packager.Package("error", "用户名或密码错误！");
				}
			} catch (SQLException e) {
				packager.Package("error", "登录失败！");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}

		}

		else if (operate.equals("showlist")) {
			try {
				String message = operator.showlist();
				packager.Package("" + operator.getLength(), message);
			} catch (SQLException e) {
				packager.Package("error:查询失败！", "查询失败！");
			} catch (Exception e) {
				packager.Package("error", "查询错误！");
			} finally {
				out = new String(packager.getMessage().toString());
			}
		}
		
		else if (operate.equals("exit")) {
			out="exit";
		}
	}

	public String getOut() {
		return out;
	}

}
