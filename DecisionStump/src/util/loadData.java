package util;

import java.io.*;
import java.util.*;

import model.Example;

public class loadData {

	public static String Category;
	public static HashMap<String, String[]> Attributes = new HashMap<String, String[]>();

	public Set<Example> getData() {

		Set<Example> data = new HashSet<Example>();
		List<String> AttName = new ArrayList<String>();

		try {
			File file = new File("E:/workspace/DecisionStump/src/weather.txt");
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			String att = null;

			while ((lineTxt = bufferedReader.readLine()) != null) {

				// 读取属性参数
				if (lineTxt.startsWith("@attribute")) {
					String[] Att = lineTxt.split("\\{");
					att = Att[0].split(" ")[1].trim();
					AttName.add(att);

					String values = Att[1].replaceAll("}", "");
					String[] Values = values.trim().replaceAll(" ", "").split(",");

					Attributes.put(att, Values);
				}

				// 读取数据
				if (lineTxt.startsWith("@data")) {
					Category = att;
					while ((lineTxt = bufferedReader.readLine()) != null) {

						String[] Exp = lineTxt.split(",");
						for (int i = 0; i < Exp.length; i++)
							Exp[i] = AttName.get(i) + ":" + Exp[i];
						Example e = new Example(Exp);
						data.add(e);
					}
				}
			}
			bufferedReader.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
