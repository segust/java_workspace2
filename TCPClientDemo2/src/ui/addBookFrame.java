package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.Controller;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class addBookFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_4;

	public addBookFrame() {
		setResizable(false);
		setTitle("\u6DFB\u52A0\u56FE\u4E66");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 375, 317);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("\u540D\u79F0\uFF1A");
		label.setBounds(10, 92, 45, 15);
		contentPane.add(label);

		textField = new JTextField();
		textField.setBounds(65, 89, 100, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel label_1 = new JLabel("\u4F5C\u8005\uFF1A");
		label_1.setBounds(175, 92, 58, 15);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("\u4EF7\u683C\uFF1A");
		label_2.setBounds(10, 135, 52, 15);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("\u8BF7\u8F93\u5165\u6DFB\u52A0\u56FE\u4E66\u7684\u76F8\u5173\u4FE1\u606F\uFF1A");
		label_3.setFont(new Font("ËÎÌו", Font.PLAIN, 18));
		label_3.setBounds(10, 22, 267, 32);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("\u51FA\u7248\u793E\uFF1A");
		label_4.setBounds(175, 135, 58, 15);
		contentPane.add(label_4);

		JButton button = new JButton("\u70B9\u51FB\u6DFB\u52A0");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Controller controller = new Controller();
				controller.add(textField.getText(), textField_1.getText(), textField_2.getText(),
						textField_3.getText());
				textField_4.setText(controller.getResult());
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");

			}
		});
		button.setBounds(52, 179, 93, 23);
		contentPane.add(button);

		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		button_1.setBounds(219, 179, 93, 23);
		contentPane.add(button_1);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(65, 132, 100, 21);
		contentPane.add(textField_2);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(221, 89, 100, 21);
		contentPane.add(textField_1);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(221, 132, 100, 21);
		contentPane.add(textField_3);

		textField_4 = new JTextField();
		textField_4.setFont(new Font("ËÎÌו", Font.PLAIN, 15));
		textField_4.setEditable(false);
		textField_4.setBounds(26, 239, 154, 21);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
	}
}
