package cn.edu.cqupt.beans;

/**
 * @author austin
 * 参数配置表
 * cycle;//企业定期维护周期
 * cycle_ahead_days;//企业定期维护提前启动天数
 * store_ahead_days;//装备存放到期提前提示启动天数
 * out_ahead_days;//装备轮换更新出库到期提前启动天数
 * price_difference;//差价
 * */
public class Parameter_Configuration {

	private int id;
	private String maintain_cycle;
	private int cycle_ahead_days;
	private int store_ahead_days;
	private int out_ahead_days;
	private int price_difference;
	private String alarm_cycle;
	private int alarm_ahead_days;
	
	public Parameter_Configuration() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMaintainCycle() {
		return maintain_cycle;
	}

	public void setMaintainCycle(String cycle) {
		this.maintain_cycle = cycle;
	}

	public int getCycle_ahead_days() {
		return cycle_ahead_days;
	}

	public void setCycle_ahead_days(int cycle_ahead_days) {
		this.cycle_ahead_days = cycle_ahead_days;
	}

	public int getStore_ahead_days() {
		return store_ahead_days;
	}

	public void setStore_ahead_days(int store_ahead_days) {
		this.store_ahead_days = store_ahead_days;
	}

	public int getOut_ahead_days() {
		return out_ahead_days;
	}

	public void setOut_ahead_days(int out_ahead_days) {
		this.out_ahead_days = out_ahead_days;
	}

	public int getPrice_difference() {
		return price_difference;
	}

	public void setPrice_difference(int price_difference) {
		this.price_difference = price_difference;
	}

	public String getAlarm_cycle() {
		return alarm_cycle;
	}

	public void setAlarm_cycle(String alarm_cycle) {
		this.alarm_cycle = alarm_cycle;
	}

	public int getAlarm_ahead_days() {
		return alarm_ahead_days;
	}

	public void setAlarm_ahead_days(int alarm_ahead_days) {
		this.alarm_ahead_days = alarm_ahead_days;
	}
}
