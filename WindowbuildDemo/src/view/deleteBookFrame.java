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
public class deleteBookFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	public deleteBookFrame() {
		setResizable(false);
		setTitle("\u56FE\u4E66\u5220\u9664");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 376, 427);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u5220\u9664\u56FE\u4E66");
		lblNewLabel.setBounds(27, 28, 121, 41);
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		contentPane.add(lblNewLabel);
		
		JLabel label = new JLabel("\u8BF7\u8F93\u5165\u8981\u5220\u9664\u7684\u56FE\u4E66\u540D\u79F0\uFF1A");
		label.setBounds(37, 79, 161, 15);
		label.setFont(new Font("宋体", Font.PLAIN, 12));
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(181, 76, 105, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("\u786E\u8BA4\u56FE\u4E66\u4FE1\u606F\uFF1A");
		lblNewLabel_1.setBounds(27, 195, 95, 15);
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 12));
		contentPane.add(lblNewLabel_1);
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(27, 216, 297, 105);
		contentPane.add(textArea);
		
		JButton button_1 = new JButton("\u786E\u8BA4\u5E76\u5220\u9664\u8BE5\u4E66");
		button_1.setEnabled(false);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Action action=new Action();
				action.deleBook(textField.getText());
				textArea.setText("删除成功！");
				textField.setText("");
				button_1.setEnabled(false);
			}
		});
		button_1.setBounds(37, 343, 126, 23);
		contentPane.add(button_1);
		
		JButton button = new JButton("\u67E5\u8BE2");
		button.setBounds(124, 132, 93, 23);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Action action=new Action();
				action.searchBook(textField.getText());
				if(action.searchBook(textField.getText())==1){
					textArea.setText("ID:" + action.ID + "\n图书名称：《" +action.name + "》\n作者："+action.author + "\n价格：" + action.price+"\n出版社："+action.press); 
					button_1.setEnabled(true);
				}else{
					textArea.setText("查找失败！");
				}
			}
		});
		contentPane.add(button);
		
		JButton button_3 = new JButton("\u53D6\u6D88");
		button_3.setBounds(211, 343, 73, 23);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 setVisible(false);
			}
		});
		contentPane.add(button_3);
		
	}
}
