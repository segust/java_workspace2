package cn.edu.cqupt.beans;

public class Humidity {
	private long humidityId;// 湿度在系统中的ID
	private double humidity;// 湿度
	private String curRecordDate;// 当时记录湿度的时间
	private String position;// 位置

	public Humidity(long humidityId, double humidity, String curRecordDate,
			String operateDate, String updateDate, String position) {
		super();
		this.humidityId = humidityId;
		this.humidity = humidity;
		this.curRecordDate=curRecordDate;
		this.position = position;
	}

	public Humidity() {
		super();
	}

	public long getHumidityId() {
		return humidityId;
	}

	public void setHumidityId(long humidityId) {
		this.humidityId = humidityId;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
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
