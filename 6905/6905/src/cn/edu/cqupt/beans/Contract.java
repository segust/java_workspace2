package cn.edu.cqupt.beans;

import java.util.Date;
/**
 * 
 * @author lynn
 * totalNumber 合同中产品总数
 * contractPrice 合同金额
 * JDS	军代室
 * signDate 签订日期
 * attachment 附件
 */
public class Contract {
	
	private String contractId;
	private int totalNumber;
	private double contractPrice;
	private String JDS;
	private Date signDate;
	private String attachment;
	private String buyer;
	private String ownUnit;
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public double getContractPrice() {
		return contractPrice;
	}
	public void setContractPrice(double contractPrice) {
		this.contractPrice = contractPrice;
	}
	public String getJDS() {
		return JDS;
	}
	public void setJDS(String jDS) {
		JDS = jDS;
	}
	
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		return "合同信息 [合同编号：" + contractId + ", 订货数量："
				+ totalNumber + ", 合同金额：" + contractPrice + ", 军代室："
				+ JDS + ", 签订日期：" + signDate + ", 采购单位：" + buyer + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((JDS == null) ? 0 : JDS.hashCode());
		result = prime * result
				+ ((attachment == null) ? 0 : attachment.hashCode());
		result = prime * result + ((buyer == null) ? 0 : buyer.hashCode());
		result = prime * result
				+ ((contractId == null) ? 0 : contractId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(contractPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((signDate == null) ? 0 : signDate.hashCode());
		result = prime * result + totalNumber;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contract other = (Contract) obj;
		if (JDS == null) {
			if (other.JDS != null)
				return false;
		} else if (!JDS.equals(other.JDS))
			return false;
		if (attachment == null) {
			if (other.attachment != null)
				return false;
		} else if (!attachment.equals(other.attachment))
			return false;
		if (buyer == null) {
			if (other.buyer != null)
				return false;
		} else if (!buyer.equals(other.buyer))
			return false;
		if (contractId == null) {
			if (other.contractId != null)
				return false;
		} else if (!contractId.equals(other.contractId))
			return false;
		if (Double.doubleToLongBits(contractPrice) != Double
				.doubleToLongBits(other.contractPrice))
			return false;
		if (signDate == null) {
			if (other.signDate != null)
				return false;
		} else if (!signDate.equals(other.signDate))
			return false;
		if (totalNumber != other.totalNumber)
			return false;
		return true;
	}
	public String getOwnUnit() {
		return ownUnit;
	}
	public void setOwnUnit(String ownUnit) {
		this.ownUnit = ownUnit;
	}
	
}
