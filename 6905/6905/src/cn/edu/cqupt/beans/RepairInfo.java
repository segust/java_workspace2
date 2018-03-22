package cn.edu.cqupt.beans;

public class RepairInfo {
	private long repairId;	//维修表id
	private long deviceId;	//设备表id
	private String deviceName;	//设备名称
	private String deviceNo;	//设备编号
	private String repairMan;	//维修人名字
	private String repairTime;	//维修时间
	private String repairReason;	//维修原因
	
	public RepairInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public long getRepairId() {
		return repairId;
	}


	public void setRepairId(long repairId) {
		this.repairId = repairId;
	}


	public long getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getRepairMan() {
		return repairMan;
	}
	public void setRepairMan(String repairMan) {
		this.repairMan = repairMan;
	}
	public String getRepairTime() {
		return repairTime;
	}
	public void setRepairTime(String repairTime) {
		this.repairTime = repairTime;
	}
	public String getRepairReason() {
		return repairReason;
	}
	public void setRepairReason(String repairReason) {
		this.repairReason = repairReason;
	}
	
}
