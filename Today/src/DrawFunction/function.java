package DrawFunction;

import javax.swing.*;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class function extends JFrame {

	int x0;
	int y0;

	static int W = 800, H = 600;
	Graphics G;

	public void setOrigin(int x, int y) {
		this.x0 = x;
		this.y0 = y;
		drawLine1(-W, 0, W, 0);
		drawLine1(0, -H, 0, H);
		drawString("X", W / 2 - 30, -20);
		drawString("Y", -20, H / 2 - 20);
		for (int i = 1; i <= 10; i++) {
			draw(W / 2 - i - 6, i);
			draw(W / 2 - i - 6, -i);
		}
		for (int i = 1; i < 10; i++) {
			draw(-i, H / 2 - i);
			draw(i, H / 2 - i);
		}
	}

	public function() {
		add(new NewPanel());
	}

	public static void main(String[] args) {
		function frame = new function();
		frame.setTitle("DrawFunction");
		frame.setSize(W, H);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public class Coordinate2D {
		int x, y;

		public Coordinate2D(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getPixelPointX() {
			return x0 + x;
		}

		public int getPixelPointY() {
			return y0 - y;
		}
	}

	class NewPanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			G = g;
			setOrigin(W / 8, H * 4 / 5);
			ArrayList<ArrayList<Double>> data = getMessage();
			for (int i = 1; i < data.size(); i++) {
				drawLine1((int) (data.get(i - 1).get(0) * 20), (int) (data.get(i - 1).get(1) * 2.0),
						(int) (data.get(i).get(0) * 20), (int) (data.get(i).get(1) * 2.0));
			}
		}
	}

	public void drawLine1(int x1, int y1, int x2, int y2) {
		int X1 = new Coordinate2D(x1, y1).getPixelPointX();
		int Y1 = new Coordinate2D(x1, y1).getPixelPointY();
		int X2 = new Coordinate2D(x2, y2).getPixelPointX();
		int Y2 = new Coordinate2D(x2, y2).getPixelPointY();
		G.drawLine(X1, Y1, X2, Y2);
	}

	public void draw(int x, int y) {
		int X = new Coordinate2D(x, y).getPixelPointX();
		int Y = new Coordinate2D(x, y).getPixelPointY();
		G.drawLine(X, Y, X, Y);
	}

	public void drawRec(int x1, int y1, int x2, int y2) {
		int dx = x1 < x2 ? 1 : -1;
		int dy = y1 < y2 ? 1 : -1;
		for (int i = x1; i != x2 + dx; i += dx) {
			for (int j = y1; j != y2 + dy; j += dy) {
				draw(i, j);
			}
		}
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		int dx = x1 < x2 ? 1 : -1;
		if (x1 == x2)
			drawRec(x1, y1, x2, y2);
		else {
			double d = (double) (y2 - y1) / (x2 - x1);
			for (int i = x1; i != x2 + dx; i += dx) {
				draw(i, (int) (y1 + (i - x1) * d));
			}
		}
	}

	public void drawString(String s, int x, int y) {
		int X = new Coordinate2D(x, y).getPixelPointX();
		int Y = new Coordinate2D(x, y).getPixelPointY();
		G.drawString(s, X, Y);
	}

	@SuppressWarnings("finally")
	public ArrayList<ArrayList<Double>> getMessage() {
		BufferedReader message;
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> e = new ArrayList<Double>();
		try {
			message = new BufferedReader(
					new InputStreamReader(new FileInputStream("E:/Workspace/K-means/src/algorithm/wine.txt")));
			String mess;
			String[] d = null;
			while ((mess = message.readLine()) != null) {
				d = mess.split(",");
				for (int i = 0; i < 2; i++)
					e.add(Double.valueOf(d[i]));
				data.add(e);
				e = new ArrayList<Double>();
			}
			message.close();
		} catch (FileNotFoundException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} finally {
			return data;
		}
	}
}
