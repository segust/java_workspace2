package cn.edu.cqupt.beans;
/**
 * 
 * @author lynn
 * 出料单产品关系表
 */
public class OutListProductRelation {
	private long listId;
	private long productId;
	public long getListId() {
		return listId;
	}
	public void setListId(long listId) {
		this.listId = listId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
}
