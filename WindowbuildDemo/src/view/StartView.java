package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import action.Login;
import view.Menu;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartView extends JFrame {
	private static final long serialVersionUID = 6135909001502200391L;
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField textField;
	private JTextField textField_1;

	public static void main(String[] args) {
		StartView frame = new StartView();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public StartView() {
		setTitle("\u767B\u5F55");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 329);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("\u56FE\u4E66\u7BA1\u7406\u7CFB\u7EDF");
		label.setFont(new Font("ø¨ÃÂ", Font.PLAIN, 18));
		label.setBounds(143, 34, 131, 36);
		contentPane.add(label);

		JLabel label_1 = new JLabel("\u7528\u6237\u540D\uFF1A");
		label_1.setBounds(71, 98, 88, 25);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("\u5BC6  \u7801\uFF1A");
		label_2.setBounds(71, 150, 88, 25);
		contentPane.add(label_2);

		passwordField = new JPasswordField();

		passwordField.setBounds(131, 150, 186, 25);
		contentPane.add(passwordField);

		textField = new JTextField();
		textField.setBounds(131, 98, 186, 25);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton button = new JButton("µ«¬º");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Login login = new Login(textField.getText(), passwordField.getPassword());
				if (login.getI() == 1) {
					Menu frame = new Menu();

					frame.setVisible(true);
					dispose();
				} else {
					textField_1.setText("’À∫≈ªÚ√‹¬Î¥ÌŒÛ£°");
				}
			}
		});
		button.setBounds(52, 203, 125, 36);
		contentPane.add(button);

		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		button_1.setBounds(224, 203, 125, 36);
		contentPane.add(button_1);

		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(24, 249, 135, 25);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
	}
}
