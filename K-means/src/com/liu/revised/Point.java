package com.liu.revised;

public class Point {

	private float[] attributes;// 数据点的属性集合

	private int index;// 标志所属簇的索引

	public Point setAttributes(float[] attributes) {
		this.attributes = new float[attributes.length];
		System.arraycopy(attributes, 0, this.attributes, 0, attributes.length);
		return this;
	}

	public void printAttributes() {
		for (float attr : attributes)
			System.out.println(attr);
	}

	public float[] getAttributes() {
		return attributes;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void printAttr() {
		for (int j = 0; j < this.getAttributes().length; j++) {
			if (j != this.attributes.length - 1) {
				System.out.print("[" + this.getAttributes()[j] + "],");
			} else {
				System.out.print("[" + this.getAttributes()[j] + "]");
			}
		}
	}

	public static void main(String[] args) {
		float[] attrs = new float[] { 20.3f, 52.6f, 56.56f };
		Point point = new Point();
		point.setAttributes(attrs);
		point.printAttributes();
		point.printAttr();
	}
}
