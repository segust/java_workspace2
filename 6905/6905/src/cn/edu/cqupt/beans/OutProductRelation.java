package cn.edu.cqupt.beans;

import java.util.Date;

/**
 * 
 * @author lynn
 * 出库产品关系表
 */
public class OutProductRelation {
	private long outId;
	private long productId;
	private long id;
	private String ownedUnit;
	private String deviceNo;
	private Date insertTime;
	public long getOutId() {
		return outId;
	}
	public void setOutId(long outId) {
		this.outId = outId;
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
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	
}
