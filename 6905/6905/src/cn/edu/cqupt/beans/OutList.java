package cn.edu.cqupt.beans;

import java.util.Date;

/**
 * @author austin
 * 发料单
 * listId 料单编号
 * fileNo 案由文号
 * deliverNo 运单编号
 * diliverMean 运输方式
 * PMNM 品名内码
 * productModel 产品型号
 * oldModel 原产品型号
 * unit 单位
 * quanlity 品级
 * askCount 通知数量
 * realCount 实际数量
 * num 件数
 * oldNum 原件数
 * outMeans 出库方式
 * money 金额（元）
 * remark 备注
 * */
public class OutList {

	  private String listId;
	  private String fileNo;
	  private String deliverNo;
	  private String diliverMean;
	  private String PMNM;
	  private String productModel;
	  private String oldModel;
	  private String unit;
	  private String quanlity;
	  private String askCount;
	  private String realCount;
	  private String num;
	  private String oldNum;
	  private String outMeans;
	  private double money;
	  private double price;
	  private String remark;
	  private Date date;
	  private int orderId;
	  private String ownedUnit;
	public OutList() {
		super();
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getDiliverMean() {
		return diliverMean;
	}

	public void setDiliverMean(String diliverMean) {
		this.diliverMean = diliverMean;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getOldModel() {
		return oldModel;
	}

	public void setOldModel(String oldModel) {
		this.oldModel = oldModel;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getQuanlity() {
		return quanlity;
	}

	public void setQuanlity(String quanlity) {
		this.quanlity = quanlity;
	}

	public String getAskCount() {
		return askCount;
	}

	public void setAskCount(String askCount) {
		this.askCount = askCount;
	}

	public String getRealCount() {
		return realCount;
	}

	public void setRealCount(String realCount) {
		this.realCount = realCount;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getOldNum() {
		return oldNum;
	}

	public void setOldNum(String oldNum) {
		this.oldNum = oldNum;
	}

	public String getOutMeans() {
		return outMeans;
	}

	public void setOutMeans(String outMeans) {
		this.outMeans = outMeans;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPMNM() {
		return PMNM;
	}

	public void setPMNM(String pMNM) {
		PMNM = pMNM;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OutList other = (OutList) obj;
		if (PMNM == null) {
			if (other.PMNM != null)
				return false;
		} else if (!PMNM.equals(other.PMNM))
			return false;
		if (askCount == null) {
			if (other.askCount != null)
				return false;
		} else if (!askCount.equals(other.askCount))
			return false;
		if (deliverNo == null) {
			if (other.deliverNo != null)
				return false;
		} else if (!deliverNo.equals(other.deliverNo))
			return false;
		if (diliverMean == null) {
			if (other.diliverMean != null)
				return false;
		} else if (!diliverMean.equals(other.diliverMean))
			return false;
		if (fileNo == null) {
			if (other.fileNo != null)
				return false;
		} else if (!fileNo.equals(other.fileNo))
			return false;
		if (listId == null) {
			if (other.listId != null)
				return false;
		} else if (!listId.equals(other.listId))
			return false;
		if (Double.doubleToLongBits(money) != Double
				.doubleToLongBits(other.money))
			return false;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		if (oldModel == null) {
			if (other.oldModel != null)
				return false;
		} else if (!oldModel.equals(other.oldModel))
			return false;
		if (oldNum == null) {
			if (other.oldNum != null)
				return false;
		} else if (!oldNum.equals(other.oldNum))
			return false;
		if (outMeans == null) {
			if (other.outMeans != null)
				return false;
		} else if (!outMeans.equals(other.outMeans))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (productModel == null) {
			if (other.productModel != null)
				return false;
		} else if (!productModel.equals(other.productModel))
			return false;
		if (quanlity == null) {
			if (other.quanlity != null)
				return false;
		} else if (!quanlity.equals(other.quanlity))
			return false;
		if (realCount == null) {
			if (other.realCount != null)
				return false;
		} else if (!realCount.equals(other.realCount))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		OutList list = new OutList();
		list.setListId(this.getListId());
		list.setFileNo(this.getFileNo());
		list.setDeliverNo(this.getDeliverNo());
		list.setDiliverMean(this.getDiliverMean());
		list.setOutMeans(this.getOutMeans());
		list.setDate(this.getDate());
		return list;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOwnedUnit() {
		return ownedUnit;
	}

	public void setOwnedUnit(String ownedUnit) {
		this.ownedUnit = ownedUnit;
	}

	
	
}
