package cn.edu.cqupt.beans;

import java.util.Date;

/**
 * 
 * @author austin
 * 出库申请表
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
 *  borrowLength 借出期限
 *  remark  备注
 *  chStatus  审核状态
 */
public class OutApply {
	private long outId;
	private String contractId;
	private String outMeans;
	private String wholeName;
	private String unitName;
	private String batch;
	private String deviceNo;
    private String unit;
    
    //仅在更新出库用
    private String productType;
	private String oldType;
    private Double newPrice=(double) 0;
    private Double oldPrice=(double) 0;
    private int num;
    private int oldNum;
    
    private String measure;
    private String manufacturer;
    private String keeper;
    private String location;
    private String storageTime;
    private String maintainCycle;
    private Date producedDate;
    private Date execDate;
    private String borrowLengthString;
    private String remark;
    private String chStatus;
    //产品编号
    private String productCode;
    //品名内码
    private String PMNM;
  //2015.0723审核人
    private String checkPerson;
    
    private String ownedUnit;
    
	public OutApply() {
		super();
	}

	public long getOutId() {
		return outId;
	}

	public void setOutId(long outId) {
		this.outId = outId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getOutMeans() {
		return outMeans;
	}

	public void setOutMeans(String outMeans) {
		this.outMeans = outMeans;
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

	public String getBorrowLengthString() {
		return borrowLengthString;
	}

	public void setBorrowLengthString(String borrowLengthString) {
		this.borrowLengthString = borrowLengthString;
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

	public String getPMNM() {
		return PMNM;
	}

	public void setPMNM(String pMNM) {
		PMNM = pMNM;
	}

	@Override
	public String toString() {
		return "OutApply [outId=" + outId + ", contractId=" + contractId
				+ ", outMeans=" + outMeans + ", wholeName=" + wholeName
				+ ", unitName=" + unitName + ", batch=" + batch + ", deviceNo="
				+ deviceNo + ", unit=" + unit + ", productType=" + productType
				+ ", oldType=" + oldType + ", newPrice=" + newPrice
				+ ", oldPrice=" + oldPrice + ", num=" + num + ", oldNum="
				+ oldNum + ", measure=" + measure + ", manufacturer="
				+ manufacturer + ", keeper=" + keeper + ", location="
				+ location + ", storageTime=" + storageTime
				+ ", maintainCycle=" + maintainCycle + ", producedDate="
				+ producedDate + ", execDate=" + execDate
				+ ", borrowLengthString=" + borrowLengthString + ", remark="
				+ remark + ", chStatus=" + chStatus + ", productCode="
				+ productCode + ", PMNM=" + PMNM + ", checkPerson="
				+ checkPerson + ", ownedUnit=" + ownedUnit + "]";
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

