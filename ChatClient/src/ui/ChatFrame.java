package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.Controller;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ChatFrame extends JFrame {
	String toWhom;
	Controller controller = new Controller();
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea contentArea;
	Date date;

	public ChatFrame(String toWhom) {

		this.toWhom = toWhom;

		setTitle("与" + toWhom + "聊天中");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				mainFrame.chatFrameMap.remove(toWhom);
				dispose();
			}
		});

		setBounds(100, 100, 439, 477);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10, 10));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JTextArea chatArea = new JTextArea();
		panel.add(chatArea, BorderLayout.NORTH);
		chatArea.setRows(4);

		JButton sendButton = new JButton("发送");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = format.format(date);
				controller.chat(toWhom, chatArea.getText());
				contentArea.append(time + "\n你:\n" + chatArea.getText() + "\n\n");
				contentArea.updateUI();
				chatArea.setText("");
			}
		});
		panel.add(sendButton, BorderLayout.WEST);

		JButton closeButton = new JButton("关闭");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.chatFrameMap.remove(toWhom);
				dispose();
			}
		});
		panel.add(closeButton, BorderLayout.EAST);

		contentArea = new JTextArea();
		contentArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(contentArea);
		contentPane.add(scrollPane, BorderLayout.CENTER);

	}

	public void addInfo(String info) {
		date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		contentArea.append(time + "\n" + toWhom + "说：\n" + info + "\n\n");
		contentArea.updateUI();
	}

}
