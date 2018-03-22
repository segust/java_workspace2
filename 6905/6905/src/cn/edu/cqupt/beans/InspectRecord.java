package cn.edu.cqupt.beans;

import java.util.Date;

/**
 * 
 * @author lynn
 * 上级检查表
 * unit  检查单位
 * date  检查日期
 * site  地点
 * item  检查事项
 * suggest  检查意见
 * feedback  反馈
 * remark 备注
 */
public class InspectRecord {
	private long inspectId;
	private String unit;
	private String site;
	private String item;
	private String suggest;
	private String feedback;
	private Date date;
	private String remark;
	public long getInspectId() {
		return inspectId;
	}
	public void setInspectId(long inspectId) {
		this.inspectId = inspectId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
