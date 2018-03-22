package ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import control.Controller;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class BookListFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;

	public BookListFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("\u56FE\u4E66\u5217\u8868");
		label.setFont(new Font("楷体", Font.PLAIN, 18));
		label.setBounds(10, 10, 99, 61);

		JButton button = new JButton("\u786E\u8BA4");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		button.setBounds(62, 421, 93, 23);
		contentPane.add(button);

		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button_1.setBounds(273, 421, 93, 23);
		contentPane.add(button_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 76, 342, 286);
		contentPane.add(scrollPane);

		table = new JTable();

		Controller controller = new Controller();
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setEnabled(false);
		String title[] = new String[] { "id", "名称", "作者", "价格", "出版社" };
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
		table.setModel(new DefaultTableModel(controller.showlist(), title));
		table.getColumnModel().getColumn(0).setPreferredWidth(46);
		table.getColumnModel().getColumn(1).setPreferredWidth(81);
		table.getColumnModel().getColumn(2).setPreferredWidth(102);
		table.getColumnModel().getColumn(3).setPreferredWidth(67);
		table.getColumnModel().getColumn(4).setPreferredWidth(156);

		scrollPane.setViewportView(table);

		JLabel label_1 = new JLabel("\u56FE\u4E66\u5217\u8868");
		label_1.setFont(new Font("楷体", Font.PLAIN, 18));
		label_1.setBounds(10, 22, 213, 44);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("共" + controller.getResult() + "本书");
		label_2.setBounds(284, 372, 105, 23);
		contentPane.add(label_2);

	}
}