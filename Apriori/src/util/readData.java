package util;

import java.io.*;
import java.util.*;

public class readData {
	public Set<Set<Integer>> getInitialData(String clas) {
		Set<Set<Integer>> Data = new HashSet<Set<Integer>>();
		try {
			File file = new File("E:/Workspace/Apriori/src/mushroom.txt");
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(read);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("2") || line.startsWith("1")) {
					Set<Integer> example = new HashSet<Integer>();
					for (String att : line.split(" "))
						if (!att.equals("2") && !att.equals("1"))
							example.add(Integer.parseInt(att));
					Data.add(example);
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Data;
	}
}
