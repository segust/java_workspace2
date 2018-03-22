package cn.edu.cqupt.beans;

public class Role {
	private long roleId; // 角色在系统中的ID
	private String role;// 角色的名称
	private int contractManage;// 合同管理
	private int queryBusiness;// 业务查询
	private int storeMantain;// 存储维护
	private int warehouseManage;// 库房管理
	private int statistics;// 统计
	private int fareManage;// 经费管理
	private int qualificationManage;// 资质管理
	private int systemManage;// 系统管理
	private int userManage;// 用户管理
	private int borrowUpdate;// 轮换更新

	public Role(long roleId, String role, int contractManage,
			int queryBusiness, int storeMantain, int warehouseManage,
			int statistics, int fareManage, int qualificationManage,
			int systemManage, int userManage,int borrowUpdate) {
		super();
		this.roleId = roleId;
		this.role = role;
		this.contractManage = contractManage;
		this.queryBusiness = queryBusiness;
		this.storeMantain = storeMantain;
		this.warehouseManage = warehouseManage;
		this.statistics = statistics;
		this.fareManage = fareManage;
		this.qualificationManage = qualificationManage;
		this.systemManage = systemManage;
		this.userManage = userManage;
		this.borrowUpdate=borrowUpdate;
	}

	public Role() {
		super();
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getContractManage() {
		return contractManage;
	}

	public void setContractManage(int contractManage) {
		this.contractManage = contractManage;
	}

	public int getQueryBusiness() {
		return queryBusiness;
	}

	public void setQueryBusiness(int queryBusiness) {
		this.queryBusiness = queryBusiness;
	}

	public int getStoreMantain() {
		return storeMantain;
	}

	public void setStoreMantain(int storeMantain) {
		this.storeMantain = storeMantain;
	}

	public int getWarehouseManage() {
		return warehouseManage;
	}

	public void setWarehouseManage(int warehouseManage) {
		this.warehouseManage = warehouseManage;
	}

	public int getStatistics() {
		return statistics;
	}

	public void setStatistics(int statistics) {
		this.statistics = statistics;
	}

	public int getFareManage() {
		return fareManage;
	}

	public void setFareManage(int fareManage) {
		this.fareManage = fareManage;
	}

	public int getQualificationManage() {
		return qualificationManage;
	}

	public void setQualificationManage(int qualificationManage) {
		this.qualificationManage = qualificationManage;
	}

	public int getSystemManage() {
		return systemManage;
	}

	public void setSystemManage(int systemManage) {
		this.systemManage = systemManage;
	}

	public int getUserManage() {
		return userManage;
	}

	public void setUserManage(int userManage) {
		this.userManage = userManage;
	}

	public int getBorrowUpdate() {
		return borrowUpdate;
	}

	public void setBorrowUpdate(int borrowUpdate) {
		this.borrowUpdate = borrowUpdate;
	}

}
