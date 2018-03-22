package _10_18;

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

@SuppressWarnings("serial")
public class HelloApplet extends Applet implements Runnable {
	public HelloApplet() {
	}
	private int fontSize = 8;
	private Thread changer;
	private boolean stopFlag = true;
	private Button contrlButton = new Button("开始动态显示！");

	public void init() {
		contrlButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				if (stopFlag)
					start();
				else
					stop();
			}
		});

		setBackground(Color.WHITE);
		add(contrlButton);
		setSize(100, 100);
	}

	public void start() {
		changer = new Thread(this);
		stopFlag = false;
		fontSize = 8;
		contrlButton.setLabel("停止动态显示！");
		changer.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("newFont", Font.BOLD, fontSize));
		g.drawString("Hello", 30, 80);
	}

	public void stop() {
		stopFlag = true;
		contrlButton.setLabel("开始动态显示！");
	}

	public void run() {
		while (!stopFlag) {
			repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			if (fontSize++ > 40)
				fontSize = 8;
		}
	}
}
