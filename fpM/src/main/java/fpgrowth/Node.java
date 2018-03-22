package fpgrowth;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date 2017年7月24日 上午9:41:04
 * @parameter
 */
public class Node {

	String idName;
	Node parent = null;
	List<Node> children = new ArrayList<Node>();
	boolean isRoot = false;
	int count = 1;

	public Node(String idName, boolean isRoot) {
		this.idName = idName;
		this.isRoot = isRoot;
	}

	/**
	 * 节点展示方法，先输出本节点信息，再输出每个孩子节点的信息
	 */
	public void show() {
		System.out.print(idName + ":" + count);
		if (children.size() != 0)
			for (Node child : children) {
				child.show();
			}
	}

	public void addCount() {
		count++;
	}

	public String getIDName() {
		return idName;
	}

	public boolean hasChild(String idName) {
		for (Node node : children) {
			if (node.getIDName().equals(idName))
				return true;
		}
		return false;
	}

	public void addChild(Node node) {
		children.add(node);
	}

	public Node getChild(String idName) {
		for (Node node : children) {
			if (node.getIDName().equals(idName))
				return node;
		}
		return null;
	}

	public int getCount() {
		return count;
	}

	public int setParent(Node parent) {
		if (this.parent == null) {
			this.parent = parent;
			return 1;
		} else
			return 0;
	}

	public void minusCount() {
		this.count--;
	}
}
