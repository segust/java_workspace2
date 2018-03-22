package cn.edu.cqupt.beans;

import java.util.Date;

/**
 * 
 * @author lynn
 * 入库产品关系表
 */
public class InProductRelation {
	private long inId;
	private long productId;
	private String ownedUnit;
	private Date insertTime;
	private long id;
	private String deviceNo;
	
	public long getInId() {
		return inId;
	}
	public void setInId(long inId) {
		this.inId = inId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getOwnedUnit() {
		return ownedUnit;
	}
	public void setOwnedUnit(String ownedUnit) {
		this.ownedUnit = ownedUnit;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
}
