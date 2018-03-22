package cn.edu.cqupt.beans;

public class User {

	private long userId; // 用户在系统中的id
	private String identifyNum;// 用户的账号
	private String password;// 用户的密码
	private String name;// 用户的名字
	private String role;// 用户的角色
	private String duty;// 用户的职责
	private String ownedUnit;// 用户所属的单位
	private String authorityUnit;// 用户所属的职权单位

	public User() {

	}

	public User(long userId, String identifyNum, String password, String name,
			String role, String duty, String ownedUnit, String authorityUnit) {
		super();
		this.userId = userId;
		this.identifyNum = identifyNum;
		this.password = password;
		this.name = name;
		this.role = role;
		this.duty = duty;
		this.ownedUnit = ownedUnit;
		this.authorityUnit = authorityUnit;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getIdentifyNum() {
		return identifyNum;
	}

	public void setIdentifyNum(String identifyNum) {
		this.identifyNum = identifyNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getOwnedUnit() {
		return ownedUnit;
	}

	public void setOwnedUnit(String ownedUnit) {
		this.ownedUnit = ownedUnit;
	}

	public String getAuthorityUnit() {
		return authorityUnit;
	}

	public void setAuthorityUnit(String authorityUnit) {
		this.authorityUnit = authorityUnit;
	}
}
