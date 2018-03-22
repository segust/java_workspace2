package cn.edu.cqupt.beans;

import java.util.Date;

/**
 * 
 * @author lynn,lhs
 * 日志表
 * operateType  操作类型
 * mainTainType 维护类别
 * inspectPerson 检查单位
 * ownedUnit 所属单位
 */
public class Log {
	private long logId;
	private long productId;
	private String operateType;
	private Date operateTime;
	private String userName;
	private String mainTainType;
	private String inspectPerson;
	private String remark;
	private String ownedUnit;
	
	public long getLogId() {
		return logId;
	}
	public void setLogId(long logId) {
		this.logId = logId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMainTainType() {
		return mainTainType;
	}
	public void setMainTainType(String mainTainType) {
		this.mainTainType = mainTainType;
	}
	public String getInspectPerson() {
		return inspectPerson;
	}
	public void setInspectPerson(String inspectPerson) {
		this.inspectPerson = inspectPerson;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOwnedUnit() {
		return ownedUnit;
	}
	public void setOwnedUnit(String ownedUnit) {
		this.ownedUnit = ownedUnit;
	}
	
}
