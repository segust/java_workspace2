package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import action.Action;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class modifyBookFrame1 extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	public modifyBookFrame1() {
		setResizable(false);
		setTitle("\u4FEE\u6539\u56FE\u4E66");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 495, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("\u4FEE\u6539\u56FE\u4E66\u4FE1\u606F");
		label.setBounds(10, 10, 155, 43);
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		contentPane.add(label);

		JLabel label_1 = new JLabel("\u8BF7\u8F93\u5165\u9700\u8981\u4FEE\u6539\u7684\u56FE\u4E66\u540D\u79F0\uFF1A");
		label_1.setBounds(20, 69, 162, 15);
		contentPane.add(label_1);

		textField = new JTextField();
		textField.setBounds(182, 66, 155, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(36, 155, 393, 105);
		contentPane.add(textArea);

		JButton button_1 = new JButton("\u786E\u8BA4\u4FE1\u606F\u5E76\u4FEE\u6539\u8BE5\u4E66");
		button_1.setEnabled(false);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyBookFrame2 modifyBookFrame2 = new modifyBookFrame2();
				modifyBookFrame2.setVisible(true);
				modifyBookFrame2.setOldname(textField.getText());
			}
		});
		button_1.setBounds(46, 270, 155, 23);
		contentPane.add(button_1);

		JButton button = new JButton("\u67E5\u8BE2");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Action action = new Action();
				if (action.searchBook(textField.getText()) == 1) {
					textArea.setText("ID:" + action.ID + "\n图书名称：《" + action.name + "》\n作者：" + action.author + "\n价格："
							+ action.price + "\n出版社：" + action.press);
					button_1.setEnabled(true);
				} else {
					textArea.setText("未找到这本书！");
				}
			}
		});
		button.setBounds(192, 97, 93, 23);
		contentPane.add(button);

		JLabel label_3 = new JLabel("\u786E\u8BA4\u56FE\u4E66\u4FE1\u606F\uFF1A");
		label_3.setFont(new Font("宋体", Font.PLAIN, 12));
		label_3.setBounds(26, 130, 95, 15);
		contentPane.add(label_3);

		JButton button_3 = new JButton("\u53D6\u6D88");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 setVisible(false);
			}
		});
		button_3.setBounds(325, 270, 93, 23);
		contentPane.add(button_3);

	}
}
