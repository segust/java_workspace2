package com.sichuang.fpGrowth;

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
	int childrenCount = 0;

	public Node(String idName, boolean isRoot) {
		this.idName = idName;
		this.isRoot = isRoot;
	}

	/**
	 * 节点展示方法，先输出本节点信息，再输出每个孩子节点的信息
	 */
	public void show() {
		if (getCount() > 0) {
			System.out.print(idName + ":" + getCount() + "\t");
			minusCount();
		}
		if (childrenCount > 0) {
			Node child = children.get(0);
			child.show();
			if (child.getCount() == 0) {
				children.remove(0);
				childrenCount--;
			}
		} else {
			System.out.println();
		}
	}

	public void addCount() {
		this.count++;
	}

	public void minusCount() {
		this.count--;
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
		childrenCount++;
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
}
