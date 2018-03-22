package cn.edu.cqupt.beans;


/**
 * 
 * @author lynn
 * 产品表
 * productCount  产品数量
 * productType   产品类型
 * productModel  产品型号
 * productUnit  产品单元
 * measureUnit  计量单位
 * lastMainTainTime  上次维护时间
 * proStatus  产品状态
 * restKeepTime  剩余存储时间
 * restMaintainTime 剩余维护时间
 * productCode   产品编号/品名内码
 */
public class Product {
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	private long productId;
	private String contractId;
	private String productCount;
	private String productName;
	private String productType;
	private String productModel;
	private String productUnit;
	private String measureUnit;
	private String productPrice;
	private java.util.Date deliveryTime;
	private java.util.Date lastMainTainTime;
	private String manufacturer;
	private String keeper;
	private String buyer;
	private java.util.Date signTime;
	private String proStatus;
	private int restKeepTime;
	private int restMaintainTime;
	private String productCode;
	//2015.06.10  by lynn add weight and volume
	private String PMNM;
	private String deviceNo;
	private String oldPrice;
	private String location;
	private String producedDate;
	private String storageTime;
	private int borrowLength;
	private String borrowReason;
	private String remark;
	private String wholeName;
	private String maintainCycle;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getProductCount() {
		return productCount;
	}
	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductModel() {
		return productModel;
	}
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}
	public String getProductUnit() {
		return productUnit;
	}
	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public java.util.Date getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(java.util.Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public java.util.Date getLastMainTainTime() {
		return lastMainTainTime;
	}
	public void setLastMainTainTime(java.util.Date lastMainTainTime) {
		this.lastMainTainTime = lastMainTainTime;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getKeeper() {
		return keeper;
	}
	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public java.util.Date getSignTime() {
		return signTime;
	}
	public void setSignTime(java.util.Date signTime) {
		this.signTime = signTime;
	}
	public String getProStatus() {
		return proStatus;
	}
	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}
	public int getRestKeepTime() {
		return restKeepTime;
	}
	public void setRestKeepTime(int restKeepTime) {
		this.restKeepTime = restKeepTime;
	}
	public int getRestMaintainTime() {
		return restMaintainTime;
	}
	public void setRestMaintainTime(int restMaintainTime) {
		this.restMaintainTime = restMaintainTime;
	}
	@Override
	public String toString() {
		return "Qy_Product [productId=" + productId + ", contractId="
				+ contractId + ", productCount=" + productCount
				+ ", productName=" + productName + ", productType="
				+ productType + ", productModel=" + productModel
				+ ", productUnit=" + productUnit + ", measureUnit="
				+ measureUnit + ", productPrice=" + productPrice + ", DeliveryTime="
				+ deliveryTime + ", lastMainTainTime=" + lastMainTainTime
				+ ", manufacturer=" + manufacturer + ", keeper=" + keeper
				+ ", buyer=" + buyer + ", signTime=" + signTime
				+ ", proStatus=" + proStatus + ", restKeepTime=" + restKeepTime
				+ ", restMaintainTime=" + restMaintainTime + ", productCode="
				+ productCode 
				+ "]";
	}
	public String getPMNM() {
		return PMNM;
	}
	public void setPMNM(String pMNM) {
		PMNM = pMNM;
	}
	
	/**
	 * 
	 * @param pro
	 * @return
	 */
	public boolean isEmpty(Product pro) {
		boolean flag = false;
		if("".equals(pro.getBuyer()) ||"".equals(pro.getContractId()) || "".equals(pro.getDeliveryTime()) || "".equals(pro.getKeeper())
				||"".equals(pro.getLastMainTainTime()) || "".equals(pro.getManufacturer()) || "".equals(pro.getMeasureUnit())
				|| "".equals(pro.getPMNM()) || "".equals(pro.getProductCode()) || "".equals(pro.getProductId())
				||"".equals(pro.getProductModel()) ||"".equals(pro.getProductName()) ||"".equals(pro.getProductUnit())) {
			flag = true;
		}
		return flag;
	}
	public String getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getProducedDate() {
		return producedDate;
	}
	public void setProducedDate(String producedDate) {
		this.producedDate = producedDate;
	}
	public String getStorageTime() {
		return storageTime;
	}
	public void setStorageTime(String storageTime) {
		this.storageTime = storageTime;
	}
	public int getBorrowLength() {
		return borrowLength;
	}
	public void setBorrowLength(int borrowLength) {
		this.borrowLength = borrowLength;
	}
	public String getBorrowReason() {
		return borrowReason;
	}
	public void setBorrowReason(String borrowReason) {
		this.borrowReason = borrowReason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getWholeName() {
		return wholeName;
	}
	public void setWholeName(String wholeName) {
		this.wholeName = wholeName;
	}
	public String getMaintainCycle() {
		return maintainCycle;
	}
	public void setMaintainCycle(String maintainCycle) {
		this.maintainCycle = maintainCycle;
	}
	
}
