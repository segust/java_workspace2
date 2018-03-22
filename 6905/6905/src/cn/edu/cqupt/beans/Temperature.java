package cn.edu.cqupt.beans;

public class Temperature {
	private long temperatureId;// 温度在系统中的ID
	private double temperature;// 温度
	private String curRecordDate;//当时记录温度的时间
	private String position;// 位置

	public Temperature(long temperatureId, double temperature,String curRecordDate,
			String operateDate, String updateDate,String position) {
		super();
		this.temperatureId = temperatureId;
		this.temperature = temperature;
		this.curRecordDate=curRecordDate;
		this.position = position;
	}

	public Temperature() {
		super();
	}

	public long getTemperatureId() {
		return temperatureId;
	}

	public void setTemperatureId(long temperatureId) {
		this.temperatureId = temperatureId;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	public String getCurRecordDate() {
		return curRecordDate;
	}

	public void setCurRecordDate(String curRecordDate) {
		this.curRecordDate = curRecordDate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
