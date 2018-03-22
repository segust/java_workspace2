package cn.edu.cqupt.beans;


public class FareDetail {

	private long fareDetailId; // 费用明细在系统中的id
	private String detailName;// 费用明细类型
	private double detailAmount;// 费用明细的金额
	private long fareId;// 所属费用记录的Id
	private String detailTime;//费用明细的时间
	private String voucherNo;//费用明细凭证号
	private String detailAbstract;//费用明细摘要
	private String remark;//费用明细备注

	public long getFareDetailId() {
		return fareDetailId;
	}

	public void setFareDetailId(long fareDetailId) {
		this.fareDetailId = fareDetailId;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public double getDetailAmount() {
		return detailAmount;
	}

	public void setDetailAmount(double detailAmount) {
		this.detailAmount = detailAmount;
	}

	public long getFareId() {
		return fareId;
	}

	public void setFareId(long fareId) {
		this.fareId = fareId;
	}

	public String getDetailTime() {
		return detailTime;
	}

	public void setDetailTime(String detailTime) {
		this.detailTime = detailTime;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getDetailAbstract() {
		return detailAbstract;
	}

	public void setDetailAbstract(String detailAbstract) {
		this.detailAbstract = detailAbstract;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
