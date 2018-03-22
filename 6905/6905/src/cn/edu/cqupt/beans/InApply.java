package cn.edu.cqupt.beans;

import java.util.Date;

/**
 * 
 * @author lynn
 * 入库申请表
 *  inMeans  入库方式
 *  productType 产品类型
 *  oldType   原产品类型
 *  wholeName  整机名称
 *  unitName    单元名称
 *  batch   入库批次
 *  deviceNo  机号
 *  unit    单位
 *  measure  计量单位
 *  manufacturer  生产单位
 *  keeper   代储企业
 *  location  摆放位置
 *  storageTime  存放期限
 *  maintainCycle 维护周期
 *  producedDate  生产日期
 *  execDate  操作日期
 *  remark  备注
 *  chStatus  审核状态
 */
public class InApply {
	private long inId;
	private String contractId;
	private String inMeans;
	private String productType;
	private String oldType;
	private String wholeName;
	private String unitName;
	private String batch;
	private String deviceNo;
    private String unit;
    
    //仅在更新入库用
    private Double newPrice=(double) 0;
    private Double oldPrice=(double) 0;
    private int num=0;
    private int oldNum=0;
    
    private String measure;
    private String manufacturer;
    private String keeper;
    private String location;
    private String storageTime;
    private String maintainCycle;
    private Date producedDate=new Date();
    private Date execDate=new Date();
    private String remark;
    private String chStatus;
    // 2015.06.11 add by lynn add PMNM
    private String PMNM;
    //产品编码
    private String productCode;
    //2015.0723审核人
    private String checkPerson;
    //增加所属单位
    private String ownedUnit;
    
	public String getPMNM() {
		return PMNM;
	}
	public void setPMNM(String pMNM) {
		PMNM = pMNM;
	}
	public long getInId() {
		return inId;
	}
	public void setInId(long inId) {
		this.inId = inId;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getInMeans() {
		return inMeans;
	}
	public void setInMeans(String inMeans) {
		this.inMeans = inMeans;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getOldType() {
		return oldType;
	}
	public void setOldType(String oldType) {
		this.oldType = oldType;
	}
	public String getWholeName() {
		return wholeName;
	}
	public void setWholeName(String wholeName) {
		this.wholeName = wholeName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}
	public Double getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getOldNum() {
		return oldNum;
	}
	public void setOldNum(int oldNum) {
		this.oldNum = oldNum;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStorageTime() {
		return storageTime;
	}
	public void setStorageTime(String storageTime) {
		this.storageTime = storageTime;
	}
	public String getMaintainCycle() {
		return maintainCycle;
	}
	public void setMaintainCycle(String maintainCycle) {
		this.maintainCycle = maintainCycle;
	}
	public Date getProducedDate() {
		return producedDate;
	}
	public void setProducedDate(Date producedDate) {
		this.producedDate = producedDate;
	}
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getChStatus() {
		return chStatus;
	}
	public void setChStatus(String chStatus) {
		this.chStatus = chStatus;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	@Override
	public String toString() {
		return "InApply [PMNM=" + PMNM + ", batch=" + batch + ", chStatus="
				+ chStatus + ", contractId=" + contractId + ", deviceNo="
				+ deviceNo + ", execDate=" + execDate + ", inId=" + inId
				+ ", inMeans=" + inMeans + ", keeper=" + keeper + ", location="
				+ location + ", maintainCycle=" + maintainCycle
				+ ", manufacturer=" + manufacturer + ", measure=" + measure
				+ ", newPrice=" + newPrice + ", num=" + num + ", oldNum="
				+ oldNum + ", oldPrice=" + oldPrice + ", oldType=" + oldType
				+ ", producedDate=" + producedDate + ", productCode="
				+ productCode + ", productType=" + productType + ", remark="
				+ remark + ", storageTime=" + storageTime + ", unit=" + unit
				+ ", unitName=" + unitName + ", wholeName=" + wholeName + "]";
	}
	public String getCheckPerson() {
		return checkPerson;
	}
	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}
	public String getOwnedUnit() {
		return ownedUnit;
	}
	public void setOwnedUnit(String ownedUnit) {
		this.ownedUnit = ownedUnit;
	}
    
    
}
