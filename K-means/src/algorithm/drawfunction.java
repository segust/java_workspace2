package algorithm;

import javax.swing.*;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class drawfunction extends JFrame {

	int x0;
	int y0;
	Graphics G;
	ArrayList<ArrayList<ArrayList<Double>>> datalist = null;

	static int W = 800, H = 500;

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
		Graphics2D g = (Graphics2D) G;
		g.setStroke(new BasicStroke(3.0f));
		g.drawLine(X, Y, X, Y);
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

	public void setOrigin(int x, int y) {
		this.x0 = x;
		this.y0 = y;
		drawLine1(-W, 0, W, 0);
		drawLine1(0, -H, 0, H);
		for (int i = 1; i <= 10; i++) {
			draw(W / 2 - i - 6, i);
			draw(W / 2 - i - 6, -i);
		}
		for (int i = 1; i < 10; i++) {
			draw(-i, H / 2 - i);
			draw(i, H / 2 - i);
		}
	}

	public drawfunction(ArrayList<ArrayList<ArrayList<Double>>> datalist) {
		this.datalist = datalist;
		add(new NewPanel());
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
			for (int j = 0; j < datalist.size(); j++) {
				for (int i = 1; i < datalist.get(j).size() - 1; i++) {
					drawLine1((int) (datalist.get(j).get(i - 1).get(0) * 30.0 - 250),
							(int) (datalist.get(j).get(i - 1).get(1) * 4.0 - 250),
							(int) (datalist.get(j).get(i).get(0) * 30.0 - 250),
							(int) (datalist.get(j).get(i).get(1) * 4.0 - 250));
				}
			}
		}

	}
}
