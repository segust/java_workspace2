package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import action.Action;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class searchBookFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	public searchBookFrame() {
		setResizable(false);
		setTitle("\u67E5\u8BE2\u56FE\u4E66");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 387, 418);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("\u8BF7\u8F93\u5165\u8981\u5220\u9664\u7684\u56FE\u4E66\u540D\u79F0\uFF1A");
		label.setBounds(0, 0, 0, 0);
		label.setFont(new Font("宋体", Font.PLAIN, 12));
		contentPane.add(label);

		textField = new JTextField();
		textField.setBounds(0, 0, 0, 0);
		textField.setColumns(10);
		contentPane.add(textField);

		JButton button = new JButton("\u67E5\u8BE2");
		button.setBounds(0, 0, 0, 0);
		contentPane.add(button);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(0, 0, 0, 0);
		contentPane.add(textPane);

		JLabel label_1 = new JLabel("\u786E\u8BA4\u56FE\u4E66\u4FE1\u606F\uFF1A");
		label_1.setBounds(0, 0, 0, 0);
		label_1.setFont(new Font("宋体", Font.PLAIN, 12));
		contentPane.add(label_1);

		JButton button_1 = new JButton("\u786E\u8BA4\u5E76\u5220\u9664\u8BE5\u4E66");
		button_1.setBounds(0, 0, 0, 0);
		contentPane.add(button_1);

		JLabel label_2 = new JLabel("\u8BF7\u8F93\u5165\u8981\u67E5\u8BE2\u7684\u56FE\u4E66\u540D\u79F0\uFF1A");
		label_2.setFont(new Font("宋体", Font.PLAIN, 12));
		label_2.setBounds(25, 115, 161, 15);
		contentPane.add(label_2);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(175, 112, 126, 21);
		contentPane.add(textField_1);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(25, 220, 296, 112);
		contentPane.add(textArea);

		JButton button_2 = new JButton("\u67E5\u8BE2");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Action action = new Action();
				if (action.searchBook(textField_1.getText()) == 1) {
					textArea.setText("ID:" + action.ID + "\n图书名称：《" + action.name + "》\n作者：" + action.author + "\n价格："
							+ action.price + "\n出版社：" + action.press);
					textField_1.setText("");
				} else {
					textArea.setText("查找失败！");
				}
			}
		});
		button_2.setBounds(120, 161, 93, 23);
		contentPane.add(button_2);

		JLabel label_3 = new JLabel("\u56FE\u4E66\u4FE1\u606F\uFF1A");
		label_3.setFont(new Font("宋体", Font.PLAIN, 12));
		label_3.setBounds(42, 198, 95, 15);
		contentPane.add(label_3);

		JButton button_3 = new JButton("\u786E\u8BA4");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		button_3.setBounds(42, 346, 126, 23);
		contentPane.add(button_3);

		JButton button_5 = new JButton("\u53D6\u6D88");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		button_5.setBounds(202, 346, 126, 23);
		contentPane.add(button_5);

		JLabel label_4 = new JLabel("\u67E5\u8BE2\u56FE\u4E66");
		label_4.setFont(new Font("宋体", Font.PLAIN, 18));
		label_4.setBounds(10, 27, 267, 32);
		contentPane.add(label_4);
	}
}
