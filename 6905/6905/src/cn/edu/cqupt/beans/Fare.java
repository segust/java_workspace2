package cn.edu.cqupt.beans;

import java.util.Date;

import cn.edu.cqupt.util.MyDateFormat;

public class Fare {

	private long fareId; // 费用在系统中的id
	private String fareType;// 费用类型
	private double fareAmount;// 费用的金额
	private String storeCompany;// 代储企业
	private String jdRoom;// 主管军代室
	private String operateDate;// 操作日期
	private String remark;// 备注

	public Fare(long fareId, String fareType, double fareAmount,
			String storeCompany, String jdRoom, String operateDate,
			String remark) {
		super();
		this.fareId = fareId;
		this.fareType = fareType;
		this.fareAmount = fareAmount;
		this.storeCompany = storeCompany;
		this.jdRoom = jdRoom;
		this.operateDate = operateDate;
		this.remark = remark;
	}

	public Fare() {
		super();
	}

	public long getFareId() {
		return fareId;
	}

	public String getFareType() {
		return fareType;
	}

	public double getFareAmount() {
		return fareAmount;
	}

	public String getStoreCompany() {
		return storeCompany;
	}

	public String getJdRoom() {
		return jdRoom;
	}

	public String getOperateDate() {
		return operateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setFareId(long fareId) {
		this.fareId = fareId;
	}

	public void setFareType(String fareType) {
		this.fareType = fareType;
	}

	public void setFareAmount(double fareAmount) {
		this.fareAmount = fareAmount;
	}

	public void setStoreCompany(String storeCompany) {
		this.storeCompany = storeCompany;
	}

	public void setJdRoom(String jdRoom) {
		this.jdRoom = jdRoom;
	}

	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Override
	public String toString() {
		StringBuffer sb=new StringBuffer();
		sb.append("费用类型："+fareType);
		sb.append("；费用金额："+fareAmount);
		sb.append("；代储企业："+storeCompany);
		sb.append("；军代室："+jdRoom);
		sb.append("；操作日期："+operateDate);
		sb.append("；备注："+remark);
		return sb.toString();
	}

}
