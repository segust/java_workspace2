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

		// ��������ͬ������ִ�в�ͬ����
		if (operate.equals("addbook")) {
			book = new Book(content, 1);
			try {
				operator.add(book);
				packager.Package("��ӳɹ���", "��ӳɹ���");
			} catch (SQLException e) {
				packager.Package("error", "���ʧ�ܣ�");
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
				packager.Package("�޸ĳɹ���", "�޸ĳɹ���");
			} catch (SQLException e) {
				packager.Package("error", "û���ҵ��Ȿ�飡");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}

		}

		else if (operate.equals("deletebook")) {
			book = new Book(content);
			try {
				operator.delete(book);
				packager.Package("ɾ���ɹ���", "ɾ���ɹ���");
			} catch (SQLException e) {
				packager.Package("error", "û���ҵ��Ȿ�飡");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}
		}

		else if (operate.equals("searchbook")) {
			book = new Book(content);
			try {
				String bookin = operator.search(book);
				if (!bookin.equals("��ѯʧ�ܣ�")) {
					packager.Package("��ѯ�ɹ���", bookin);
				} else {
					packager.Package("��ѯʧ��", "δ�ҵ��Ȿ�飡");
				}

			} catch (SQLException e) {
				packager.Package("error", "��ѯʧ�ܣ�");
			} finally {
				String message = packager.getMessage();
				out = new String(message);
			}
		}

		else if (operate.equals("login")) {
			user = new User(content);
			try {
				if (operator.login(user.getUsername(), user.getPassword())) {
					packager.Package("agree", "��¼�ɹ���");
				} else {
					packager.Package("error", "�û������������");
				}
			} catch (SQLException e) {
				packager.Package("error", "��¼ʧ�ܣ�");
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
				packager.Package("error:��ѯʧ�ܣ�", "��ѯʧ�ܣ�");
			} catch (Exception e) {
				packager.Package("error", "��ѯ����");
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
