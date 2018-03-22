package algorithm;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Start {

	public static void main(String[] args) {
		try {

			int k = 3;
			ArrayList<ArrayList<ArrayList<Double>>> DataList = new ArrayList<ArrayList<ArrayList<Double>>>(k);

			Kmeans re = new Kmeans(k);
			DataList = re.getDataList();
			if (DataList.get(0).get(0).size() == 2) {
				drawfunction frame = new drawfunction(DataList);
				frame.setTitle("DrawFunction");
				frame.setSize(800, 500);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.setResizable(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}