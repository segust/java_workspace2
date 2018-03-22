package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.Controller;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class modifyBookFrame2 extends JFrame {

	Controller controller = new Controller();
	private static final long serialVersionUID = -5565937033172267229L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private String oldname;
	private JTextField textField_4;

	public String getOldname() {
		return oldname;
	}

	public void setOldname(String oldname) {
		this.oldname = oldname;
	}

	public modifyBookFrame2() {
		setResizable(false);
		setTitle("\u4FEE\u6539\u56FE\u4E66");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 393, 312);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("\u4FEE\u6539\u56FE\u4E66\u4FE1\u606F");
		label.setFont(new Font("ËÎÌו", Font.PLAIN, 18));
		label.setBounds(10, 10, 155, 43);
		contentPane.add(label);

		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(235, 87, 100, 21);
		contentPane.add(textField);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(235, 130, 100, 21);
		contentPane.add(textField_1);

		JLabel label_1 = new JLabel("\u51FA\u7248\u793E\uFF1A");
		label_1.setBounds(189, 133, 58, 15);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("\u4F5C\u8005\uFF1A");
		label_2.setBounds(189, 90, 58, 15);
		contentPane.add(label_2);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(79, 87, 100, 21);
		contentPane.add(textField_2);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(79, 130, 100, 21);
		contentPane.add(textField_3);

		JLabel label_3 = new JLabel("\u4EF7\u683C\uFF1A");
		label_3.setBounds(24, 133, 52, 15);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("\u540D\u79F0\uFF1A");
		label_4.setBounds(24, 90, 45, 15);
		contentPane.add(label_4);

		JButton button = new JButton("\u5B8C\u6210\u4FEE\u6539");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.modify(oldname, textField_2.getText(), textField.getText(), textField_3.getText(),
						textField_1.getText());
				textField_4.setText(controller.getContent());
				textField_2.setText("");
				textField_1.setText("");
				textField.setText("");
				textField_3.setText("");
			}
		});
		button.setBounds(72, 199, 93, 23);
		contentPane.add(button);

		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		button_1.setBounds(189, 199, 93, 23);
		contentPane.add(button_1);

		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setBounds(52, 242, 195, 21);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
	}
}
