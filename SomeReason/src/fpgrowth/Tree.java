package fpgrowth;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cqupt.util.ListUtil;

public class Tree {

	String license_no;
	ResultSet rs;

	Map<Node, List<Node>> RootMap = new HashMap<Node, List<Node>>();

	ArrayList<String> frequentList;
	ArrayList<ArrayList<String>> itemList;

	public Tree(ResultSet rs) {

		this.itemList = ListUtil.getList(rs);
	}

	public void growth() {
		boolean f = false;
		Node r = null;
		for (Node node : RootMap.keySet()) {
			if (node.getIDName().equals(frequentList.get(0))) {
				f = true;
				r = node;
				frequentList.remove(0);
				break;
			}
		}
		if (f) {
			r.addCount();
			r.growth(frequentList);
		} else {
			Node n = new Node(frequentList.get(0));
			RootMap.put(n, new ArrayList<Node>());
			frequentList.remove(0);
			n.growth(frequentList);
		}
	}

	public class Node {
		boolean isLeaf = false;
		String idName;
		int count = 1;
		ArrayList<Node> children = new ArrayList<Node>();

		public Node(String idName) {
			this.idName = idName;
		}

		public void growth(ArrayList<String> veinfo) {
			if (veinfo.size() == 1)
				setIsLeaf(true);
			for (Node child : children) {
				if (child.getIDName().equals(veinfo.get(0))) {
					child.addCount();
					veinfo.remove(0);
					child.growth(veinfo);
				} else {
					Node n = new Node(veinfo.get(0));
					children.add(n);
					veinfo.remove(0);
					n.growth(veinfo);
				}
			}
		}

		public void show() {

			if (!isLeaf) {
				System.out.print(this.idName + "\t");
				for (Node n : children) {
					n.show();
				}
			} else {
				System.out.println(this.idName + ":" + this.count);
			}
		}

		public void write() {

		}

		public void addCount() {
			count++;
		}

		public String getIDName() {
			return idName;
		}

		public int getCount() {
			return count;
		}

		public void setIsLeaf(boolean isLeaf) {
			this.isLeaf = isLeaf;
		}

		public boolean isLeaf() {
			return isLeaf;
		}
	}

	public String getVeId() {
		return license_no;
	}

	public void show() {
		System.out.println("license_no:" + this.getVeId());
		for (Node n : RootMap.keySet()) {
			n.show();
		}
	}
}
