package cn.edu.cqupt.beans;

public class Device {
	private long deviceId;	//设备表id
	private String deviceName;	//设备名称
	private String deviceNo;	//设备数量
	private String position;	//设备位置
	private String deviceInTime;	//设备进库时间
	private String repairTime;		//设备上一次维修时间
	private String status;		//设备状态
	
	public Device() {
		// TODO Auto-generated constructor stub
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDeviceInTime() {
		return deviceInTime;
	}

	public void setDeviceInTime(String deviceInTime) {
		this.deviceInTime = deviceInTime;
	}

	public String getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(String repairTime) {
		this.repairTime = repairTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		String str="设备名："+deviceName+"，设备启用时间："+deviceInTime+"，设备位置："+position+"，设备编号："+deviceNo+"，设备状态："+status;
		return str;
	}
}
