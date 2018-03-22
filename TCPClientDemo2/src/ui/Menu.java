package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import control.Controller;

@SuppressWarnings("serial")
public class Menu extends JFrame {

	Controller controller = new Controller();
	private JPanel contentPane;

	public Menu() {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.close();
				System.exit(0);
			}
		});
		setTitle("\u56FE\u4E66\u7BA1\u7406\u7CFB\u7EDF");
		setBounds(100, 100, 469, 259);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u6B22\u8FCE\u8FDB\u5165\u56FE\u4E66\u7BA1\u7406\u7CFB\u7EDF\uFF01");
		lblNewLabel.setBounds(10, 10, 198, 21);
		lblNewLabel.setFont(new Font("楷体", Font.PLAIN, 18));
		contentPane.add(lblNewLabel);

		JLabel label = new JLabel("\u8BF7\u9009\u62E9\u60A8\u8981\u8FDB\u884C\u7684\u64CD\u4F5C\uFF1A");
		label.setFont(new Font("新宋体", Font.PLAIN, 12));
		label.setBounds(10, 55, 179, 15);
		contentPane.add(label);

		JButton button = new JButton("\u4FEE\u6539\u56FE\u4E66\u4FE1\u606F");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyBookFrame1 mBookFrame = new modifyBookFrame1();
				mBookFrame.setVisible(true);
			}
		});
		button.setFont(new Font("新宋体", Font.PLAIN, 12));
		button.setBounds(288, 88, 129, 23);
		contentPane.add(button);

		JButton button_1 = new JButton("\u589E\u52A0\u56FE\u4E66");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBookFrame aBookFrame = new addBookFrame();
				aBookFrame.setVisible(true);
			}
		});
		button_1.setFont(new Font("新宋体", Font.PLAIN, 12));
		button_1.setBounds(10, 88, 129, 23);
		contentPane.add(button_1);

		JButton button_2 = new JButton("\u5220\u9664\u56FE\u4E66");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteBookFrame dBookFrame = new deleteBookFrame();
				dBookFrame.setVisible(true);
			}
		});
		button_2.setFont(new Font("宋体", Font.PLAIN, 12));
		button_2.setBounds(149, 88, 129, 23);
		contentPane.add(button_2);

		JButton button_3 = new JButton("\u67E5\u8BE2\u56FE\u4E66\u4FE1\u606F");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchBookFrame sBookFrame = new searchBookFrame();
				sBookFrame.setVisible(true);
			}
		});
		button_3.setFont(new Font("宋体", Font.PLAIN, 12));
		button_3.setBounds(10, 133, 129, 23);
		contentPane.add(button_3);

		JButton button_4 = new JButton("\u56FE\u4E66\u5217\u8868");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BookListFrame booklist = new BookListFrame();
				booklist.setVisible(true);
			}
		});
		button_4.setFont(new Font("新宋体", Font.PLAIN, 12));
		button_4.setBounds(149, 133, 129, 23);
		contentPane.add(button_4);

		JButton button_5 = new JButton("\u6CE8\u9500");
		button_5.setFont(new Font("宋体", Font.PLAIN, 12));
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartView logout = new StartView();
				logout.setVisible(true);
				dispose();
			}
		});
		button_5.setBounds(288, 133, 129, 23);
		contentPane.add(button_5);

		JButton button_6 = new JButton("\u9000\u51FA\u7CFB\u7EDF");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.close();
				System.exit(0);
			}
		});
		button_6.setFont(new Font("宋体", Font.PLAIN, 12));
		button_6.setBounds(288, 182, 129, 23);
		contentPane.add(button_6);
	}
}
