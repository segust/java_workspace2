package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.User;
import util.Parser;

import java.awt.*;

import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;

public class mainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static User user;
	public static HashMap<String, ChatFrame> chatFrameMap = new HashMap<String, ChatFrame>();
	private JPanel contentPane;
	private int UserOnNum;
	private Parser parser;
	private Thread r;
	private Controller controller = new Controller();

	JPanel PANEL;
	JPanel userPanel;

	public mainFrame(int id, String username, String sex, int age, String[] userlist) {

		user = new User(id, username, sex, age);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					controller.logout(id);
					r.interrupt();
					Controller.socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}

		});
		setBounds(100, 100, 258, 540);
		setTitle(username);
		UserOnNum = userlist.length;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setBounds(0, 0, 242, 86);
		panel.setLayout(new BorderLayout(0, 0));

		JTextArea userinfo = new JTextArea();
		userinfo.setEditable(false);
		userinfo.setText("用户id：" + id + "\n用户名：" + username + "\n性别：" + sex + "\n年龄：" + age);
		panel.add(userinfo);

		JPanel card = new JPanel();
		CardLayout cardlayout = new CardLayout();
		card.setBounds(0, 84, 242, 417);
		contentPane.add(card);
		card.setLayout(cardlayout);

		JPanel UserOn = new JPanel();
		UserOn.setLayout(new BorderLayout(0, 0));

		JButton button = new JButton("在线用户");
		UserOn.add(button, BorderLayout.NORTH);

		JScrollPane scrollpane = new JScrollPane();
		userPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(userPanel, BoxLayout.Y_AXIS);
		userPanel.setLayout(boxLayout);

		scrollpane.setViewportView(userPanel);

		PANEL = new JPanel();
		BoxLayout layout = new BoxLayout(PANEL, BoxLayout.Y_AXIS);
		PANEL.setLayout(layout);

		for (int i = 0; i < UserOnNum; i++) {
			JPanel userp = new JPanel();
			JLabel user = new JLabel();
			userp.setName(userlist[i]);
			user.setText(userlist[i]);
			userp.add(user);
			userp.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 2) {
						boolean WindowOpen = false;
						String name = ((JPanel) e.getSource()).getName();
						int num = mainFrame.chatFrameMap.size();
						Object object[] = mainFrame.chatFrameMap.keySet().toArray();
						for (int i = 0; i < num; i++) {
							if (String.valueOf(object[i]).equals(name)) {
								WindowOpen = true;
								break;
							}
						}
						if (!WindowOpen) {
							ChatFrame chat = new ChatFrame(name);
							chatFrameMap.put(name, chat);
							chat.setVisible(true);
						}
					}
				}

				public void mouseExited(MouseEvent e) {
					userp.setBackground(new Color(240, 240, 240));
				}

				public void mouseEntered(MouseEvent e) {
					userp.setBackground(new Color(200, 200, 200));
				}

			});
			PANEL.add(userp);
		}

		userPanel.add(PANEL);
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(new Color(250, 250, 250));
		userPanel.add(textArea, null);

		UserOn.add(scrollpane, BorderLayout.CENTER);

		JButton button_1 = new JButton("离线用户");
		UserOn.add(button_1, BorderLayout.SOUTH);
		button_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				cardlayout.show(card, "Out");
			}
		});

		JPanel UserOutButtonPanel = new JPanel();
		UserOutButtonPanel.setLayout(new GridLayout(2, 1));

		JButton button_2 = new JButton("在线用户");
		button_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				cardlayout.show(card, "On");
			}

		});
		JButton button_3 = new JButton("离线用户");
		UserOutButtonPanel.add(button_2);
		UserOutButtonPanel.add(button_3);

		JPanel UserOut = new JPanel();
		UserOut.setLayout(new BorderLayout(0, 0));
		UserOut.add(UserOutButtonPanel, BorderLayout.NORTH);

		card.add(UserOn, "On");
		card.add(UserOut, "Out");
		contentPane.add(card, BorderLayout.CENTER);

		setVisible(true);

		StandBy standby = new StandBy();
		r = new Thread(standby);
		r.start();
	}

	// 一直在等待的线程
	private class StandBy implements Runnable {
		public void run() {
			try {
				while (true) {

					byte[] buffer = new byte[2048];
					int length = Controller.socket.getInputStream().read(buffer);
					String mess = new String(buffer, 0, length);
					System.out.println("收到的来自服务端的信息：\n" + mess);
					parser = new Parser(mess);
					String aim = parser.getAim();
					String content = parser.getContent();

					// 其他用户上线通知
					if (aim.equals("OnlineNotice")) {
						JPanel userp = new JPanel();
						JLabel user = new JLabel();
						userp.setName(content);
						user.setText(content);
						userp.add(user);
						userp.addMouseListener(new MouseAdapter() {

							public void mousePressed(MouseEvent e) {
								if (e.getClickCount() == 2) {
									boolean WindowOpen = false;
									String name = ((JPanel) e.getSource()).getName();
									int num = mainFrame.chatFrameMap.size();
									Object object[] = mainFrame.chatFrameMap.keySet().toArray();
									for (int i = 0; i < num; i++) {
										if (String.valueOf(object[i]).equals(name)) {
											WindowOpen = true;
											break;
										}
									}
									if (!WindowOpen) {
										ChatFrame chat = new ChatFrame(name);
										chatFrameMap.put(name, chat);
										chat.setVisible(true);
									}
								}
							}

							public void mouseExited(MouseEvent e) {
								userp.setBackground(new Color(240, 240, 240));
							}

							public void mouseEntered(MouseEvent e) {
								userp.setBackground(new Color(200, 200, 200));
							}

						});
						PANEL.add(userp);
						PANEL.updateUI();
					}

					// 其他用户下线通知
					else if (aim.equals("OutlineNotice")) {
						int count = PANEL.getComponentCount();
						for (int i = 0; i < count; i++) {
							JPanel panel = (JPanel) PANEL.getComponent(i);
							if (panel.getName().equals(content)) {
								PANEL.remove(panel);
								PANEL.updateUI();
							}
						}
					}

					// 聊天请求处理
					else if (aim.equals("chatInfo")) {

						boolean windowOpen = false;

						String[] mes = content.split(":");
						String fromwhom = mes[0];
						String chatinfo = mes[1];

						// 判断与该好友聊天窗口是否打开
						int num = mainFrame.chatFrameMap.size();
						Object object[] = mainFrame.chatFrameMap.keySet().toArray();
						for (int i = 0; i < num; i++) {
							if (String.valueOf(object[i]).equals(fromwhom)) {
								mainFrame.chatFrameMap.get(fromwhom).addInfo(chatinfo);
								windowOpen = true;
								break;
							}
						}

						// 强制打开窗口
						if (!windowOpen) {
							ChatFrame chatFrame = new ChatFrame(fromwhom);
							chatFrame.setVisible(true);
							mainFrame.chatFrameMap.put(fromwhom, chatFrame);
							chatFrame.addInfo(chatinfo);
						}
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
