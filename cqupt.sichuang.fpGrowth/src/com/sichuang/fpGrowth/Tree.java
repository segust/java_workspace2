package com.sichuang.fpGrowth;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * FP_Growth算法实现， 构造方法Tree(ResultSet)、根节点生成方法growth(ArrayList<String>)、
 * 生长方法growth(Node, ArrayList<String>)
 * 
 * @author SeGust
 * @param license_no
 *            该树的license_no
 * @param RootList
 *            根节点列表ArrayList<Node>
 * @param itemList
 *            事务列表ArrayList<ArrayList<String>>
 */
public class Tree {

	String license_no;

	/* 根节点列表 */
	List<Node> RootList = new ArrayList<Node>();

	/* 事务列表 */
	ArrayList<ArrayList<String>> itemList;

	/* 构造方法，根据ResultSet获得itemList，继而生成根节点列表 */
	public Tree(ArrayList<ArrayList<String>> itemList) {
		this.itemList = itemList;
		for (ArrayList<String> day : itemList)
			growth(day);
	}

	/**
	 * 根节点生成方法，首先判断day中第一个元素是否存在于根节点列表，并从day中移除：
	 * 若不存在于根节点列表,则生成根节点，加入到Tree的NodeList中，并开始生长； 若存在于根节点列表，则该根节点次数+1，并开始生长
	 * 
	 */
	public void growth(ArrayList<String> day) {
		if (day.size() != 0) {
			String first = day.remove(0);
			if (!findRoot(first)) {
				Node root = new Node(first, true);
				RootList.add(root);
				growth(root, day);
			} else {
				for (Node node : RootList) {
					if (node.getIDName().equals(first)) {
						node.addCount();
						growth(node, day);
						break;
					}
				}
			}
		}
	}

	/**
	 * 节点生长方法，传入parent节点和事务列表day： 判断parent的孩子列表中是否存在day中第一个元素，并从day中移除：
	 * 若存在，则该孩子节点count+1，递归调用growth，传入该孩子节点和day；
	 * 若不存在，则新建孩子节点，设置该孩子节点父节点为parent，将该孩子接单添加到parent的children中，递归调用growth，传入该孩子节点和day
	 * 
	 */
	public void growth(Node parent, ArrayList<String> day) {
		if (day.size() != 0) {
			String first = day.remove(0);
			if (parent.hasChild(first)) {
				parent.getChild(first).addCount();
				growth(parent.getChild(first), day);
			} else {
				Node child = new Node(first, false);
				child.setParent(parent);
				parent.addChild(child);
				growth(child, day);
			}
		}

	}

	/**
	 * tree的展示方法，调用每一个root的show()
	 */
	public void show() {
		for (Node root : RootList) {
			while (root.getCount() > 0) {
				root.show();
			}
			System.out.println();
		}
	}

	public boolean findRoot(String idName) {
		for (Node node : RootList) {
			if (node.getIDName().equals(idName))
				return true;
		}
		return false;
	}
}
