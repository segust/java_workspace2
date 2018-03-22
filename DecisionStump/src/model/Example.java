package model;

import java.util.HashMap;

public class Example {

	public HashMap<String, String> map = new HashMap<String, String>();

	public Example(String[] Exp) {
		for (String e : Exp) {
			String att = e.split(":")[0];
			String value = e.split(":")[1];
			map.put(att, value);
		}
	}

	public String getValue(String attribute) {
		return map.get(attribute);
	}

	public void removeAttribute(String attribute) {
		map.remove(attribute);
	}
}
