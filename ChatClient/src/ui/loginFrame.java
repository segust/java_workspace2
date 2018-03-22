package ui;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.Controller;

import java.awt.event.*;

public class loginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField textField;
	private JPasswordField passwordField;
	private String username;
	private char[] password;

	private Controller controller = new Controller();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginFrame frame = new loginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public loginFrame() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.logout();
				System.exit(0);
			}
		});
		setBounds(100, 100, 416, 334);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel label = new JLabel("’À∫≈£∫");
		label.setBounds(87, 56, 57, 28);

		JLabel label_1 = new JLabel("√‹¬Î£∫");
		label_1.setBounds(87, 116, 57, 15);

		usernameField = new JTextField();
		usernameField.setBounds(134, 60, 170, 21);
		usernameField.setColumns(10);

		JButton logButton = new JButton("µ«¬º");
		logButton.setBounds(67, 174, 87, 23);
		logButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				boolean suc = true;

				if (usernameField.getText().isEmpty()) {
					textField.setText("«Î ‰»Î”√ªß√˚£°");
					suc = false;
				} else if (passwordField.getPassword().length == 0) {
					textField.setText("«Î ‰»Î√‹¬Î£°");
					suc = false;
				}

				if (suc) {
					username = usernameField.getText();
					password = passwordField.getPassword();
					controller.login(username, password);
					if (controller.getResult().equals("agree")) {
						String[] userinfo = controller.getContent().split("#");
						String[] userlist = controller.list();
						mainFrame mainFrame = new mainFrame(Integer.valueOf(userinfo[0]), userinfo[1], userinfo[2],
								Integer.valueOf(userinfo[3]), userlist);
						mainFrame.setVisible(true);
						dispose();
					} else {
						textField.setText(controller.getContent());
					}
				}
			}
		});

		JButton exitButton = new JButton("»°œ˚");
		exitButton.setBounds(240, 174, 87, 23);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(exitButton);
		contentPane.add(label_1);
		contentPane.add(label);
		contentPane.add(logButton);
		contentPane.add(usernameField);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(38, 222, 149, 23);
		contentPane.add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(134, 108, 170, 23);
		contentPane.add(passwordField);
	}
}
